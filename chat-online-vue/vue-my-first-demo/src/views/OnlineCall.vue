<template>
  <h2>视频通话</h2>
  <div class="video-container">
    <video autoplay id="localVideo" class="local-video" :poster="require('@/assets/私聊头像.png')"></video>
    <video autoplay id="remoteVideo" class="remote-video" :poster="require('@/assets/私聊头像.png')"></video>
  </div>
  <div class="button-container">
    <button @click="endCall">终止</button>
    <button @click="startCall">通话</button>
  </div>
</template>


<script>

var PeerConnection = window.RTCPeerConnection ||
  window.mozRTCPeerConnection ||
  window.webkitRTCPeerConnection;

export default {
  name: "OnlineCall",
  data() {
    return {
      userInfo_from_store: null,
      hostname: null,
      localRtcPc: null,//本地PeerConnection实例
      localStream: null,//本地视频流
      ws: null,//websocket
      targetUserId: null,//对方的用户ID
    };
  },
  created() {
    // created后可以访问this
    var currentUrl = window.location.href;
    this.hostname = new URL(currentUrl).hostname;
    console.log("获取hostname：" + this.hostname);

    const jsonParsed = JSON.parse(sessionStorage.getItem("userInfo"));
    if (jsonParsed) {
      this.userInfo_from_store = jsonParsed;
    }
    console.log("用户ID：" + this.userInfo_from_store.userId);

    this.targetUserId = this.$route.params.targetUserId;
    console.log("对方ID：" + this.targetUserId);
  },
  mounted() {
    // mounted后可以访问DOM元素
    if (!this.userInfo_from_store) {
      alert("用户信息获取失败")
    }
    this.initWebSocket();
  },
  methods: {
    /**
     * Initialize user information
     */
    /**
     * Connect to the signaling server
     */
    initWebSocket() {
      if (this.ws) {
        this.ws.close();
        this.ws = null;
      }
      //这里要改成动态的
      this.ws = new WebSocket(`wss://10.102.15.120:8080/sip/${this.userInfo_from_store.userId}/${this.targetUserId}`);
      console.log(`wss://10.102.15.120:8080/sip/${this.userInfo_from_store.userId}/${this.targetUserId}`);

      // WebSocket event handlers
      this.ws.onopen = () => {
        this.$notify({
          title: '提示',
          message: '信令服务器连接成功',
          type: 'success'
        });
      };
      this.ws.onclose = () => {
        console.log("WebSocket已关闭");
      };
      this.ws.onerror = () => {
        this.$notify({
          title: '提示',
          message: '信令服务器连接失败',
          type: 'error'
        });
        console.log("WebSocket发生了错误");
      };
      this.ws.onmessage = (msg) => {
        const message = JSON.parse(msg.data);
        switch (message.type) {
          case 'call':
            this.onCall(message);
            break;
          case 'offer':
            this.onRemoteOffer(message.data.userId, message.data.offer);
            break;
          case 'answer':
            this.onRemoteAnswer(message.data.userId, message.data.answer);
            break;
          case 'candidate':
            this.onCandiDate(message.data.candidate);
            break;
        }
      };
    },
    /**
     * Get local media stream
     * @returns {Promise<MediaStream>}
     */
    async getLocalUserMedia() {
      const constraints = { video: true, audio: true };
      try {
        return await navigator.mediaDevices.getUserMedia(constraints);
      } catch (error) {
        this.handleError(error);
      }
    },
    /**
     * Handle media error
     * @param error
     */
    handleError(error) {
      alert("摄像头无法正常使用，请检查是否占用或缺失");
      console.error('navigator.MediaDevices.getUserMedia error: ', error.message, error.name);
    },
    /**
     * Send call request
     */
    startCall() {
      // 清空远程视频流
      const remoteVideo = document.getElementById('remoteVideo');
      remoteVideo.srcObject = null;

      if (!this.targetUserId) {
        this.$message.warning('被呼叫者ID获取失败');
        return false;
      }
      this.initCallerInfo(this.userInfo_from_store.userId, this.targetUserId);
      const params = {
        type: "call",
        userId: this.userInfo_from_store.userId,
        targetUid: this.targetUserId
      };
      this.ws.send(JSON.stringify(params));
    },
    /**
     * Handle incoming call
     * @param message
     */
    async onCall(message) {
      console.log("接收到远程呼叫：", message);
      await this.initCalleeInfo(message.data.targetUid, message.data.userId);
    },
    /**
     * Initialize caller info
     * @param callerId
     * @param calleeId
     */
    async initCallerInfo(callerId, calleeId) { //主动呼叫时
      //创建 RTCPeerConnection 实例；PeerConnection 是 WebRTC 中的核心对象，负责管理和控制对等连接
      this.localRtcPc = new PeerConnection();
      //获取本地媒体流
      const localStream = await this.getLocalUserMedia({ audio: true, video: true });
      //localStream.getTracks() 返回媒体流中的所有轨道（音频和视频）
      for (const track of localStream.getTracks()) {
        //this.localRtcPc.addTrack(track) 将每一个轨道添加到 RTCPeerConnection 实例中
        this.localRtcPc.addTrack(track);
      }
      //将 localStream 绑定到localVideo为id的视频元素
      await this.setLocalDomVideoStream("localVideo", localStream);
      //自定义协商函数
      this.onPcEvent(this.localRtcPc, callerId, calleeId);

      //创建SDP（Session Description Protocol）报文；iceRestart: true 表示是否在重新启动 ICE 候选时创建新的 offer
      const offer = await this.localRtcPc.createOffer({ iceRestart: true });
      //将创建的 offer 设置为本地描述
      await this.localRtcPc.setLocalDescription(offer);

      //ICE（Interactive Connectivity Establishment）是用于在 
      //WebRTC（Web Real-Time Communication）中建立对等连接的框架。
      //它的主要作用是帮助两个端点（通常是两个浏览器）找到彼此之间可行的通信路径，
      //以便进行媒体数据传输（如视频、音频）。

      const params = {
        type: "offer",
        userId: callerId,
        targetUid: calleeId,
        offer: offer
      };
      this.ws.send(JSON.stringify(params));
    },
    /**
     * Initialize callee info
     * @param localUid
     * @param fromUid
     */
    async initCalleeInfo(localUid, fromUid) { //被呼叫时
      //创建 PeerConnection 实例
      this.localRtcPc = new PeerConnection();
      //获取本地媒体流
      const localStream = await this.getLocalUserMedia({ audio: true, video: true });
      //添加所有轨道
      for (const track of localStream.getTracks()) {
        this.localRtcPc.addTrack(track);
      }
      //在本地 DOM 中显示本地视频流
      await this.setLocalDomVideoStream("localVideo", localStream);
      //进行协商
      this.onPcEvent(this.localRtcPc, localUid, fromUid);
    },
    /**
     * Handle remote offer
     * @param fromUid
     * @param offer
     */
    async onRemoteOffer(fromUid, offer) {
      await this.localRtcPc.setRemoteDescription(offer);
      const answer = await this.localRtcPc.createAnswer();
      await this.localRtcPc.setLocalDescription(answer);
      const params = {
        type: "answer",
        targetUid: fromUid,
        userId: this.userInfo_from_store.userId,
        answer: answer
      };
      this.ws.send(JSON.stringify(params));
    },
    /**
     * Handle remote answer
     * @param fromUid
     * @param answer
     */
    async onRemoteAnswer(fromUid, answer) {
      await this.localRtcPc.setRemoteDescription(answer);
    },
    /**
     * Handle ICE candidate
     * @param candidate
     */
    onCandiDate(candidate) {
      this.localRtcPc.addIceCandidate(candidate);
    },
    /**
     * Handle PC events
     * @param pc
     * @param localUid
     * @param remoteUid
     */
    onPcEvent(pc, localUid, remoteUid) {
      //pc是PeerConnection实例
      this.channel = pc.createDataChannel("chat");
      pc.ontrack = (event) => {
        this.setRemoteDomVideoStream("remoteVideo", event.track);
      };
      pc.onnegotiationneeded = (e) => {
        console.log("重新协商", e);
      };
      pc.ondatachannel = (ev) => {
        console.log('Data channel is created!');
        ev.channel.onopen = () => {
          console.log('Data channel ------------open----------------');
        };
        ev.channel.onmessage = (data) => {
          console.log('Data channel -------------msg----------------', data);
        };
        ev.channel.onclose = () => {
          console.log('Data channel ------------close---------------');
        };
      };
      pc.onicecandidate = (event) => {
        if (event.candidate) {
          const params = {
            type: 'candidate',
            targetUid: remoteUid,
            userId: localUid,
            candidate: event.candidate
          };
          console.log("targetUid:" + remoteUid + " userId:" + localUid + " candidate:" + event.candidate);
          this.ws.send(JSON.stringify(params));
        } else {
          console.log("在此次协商中，没有更多的候选了");
        }
      };
    },
    /**
     * Display local video stream
     * @param domId
     * @param newStream
     */
    async setLocalDomVideoStream(domId, newStream) {
      const video = document.getElementById(domId);
      const stream = video.srcObject;
      console.log("设置LocalVideo：" + stream);
      //将当前视频流全部停止
      if (stream) {
        stream.getTracks().forEach(track => track.stop());
      }
      //设置新的视频流
      video.srcObject = newStream;
      //静音本地视频
      video.muted = true;
    },
    /**
     * Display remote video stream
     * @param domId
     * @param track
     */
    setRemoteDomVideoStream(domId, track) {
      //获取RemoteVideo为id的DOM元素
      const video = document.getElementById(domId);
      const stream = video.srcObject;
      console.log("设置RemoteVideo：" + stream);
      //有则添加无则新建
      if (stream) {
        stream.addTrack(track);
      } else {
        const newStream = new MediaStream();
        newStream.addTrack(track);
        video.srcObject = newStream;
        // video.muted = true;
      }
    },
    /**
     * Toggle video
     */
    videoSet() {
      this.$notify({
        title: '成功',
        message: '摄像头',
        type: 'success'
      });
    },
    /**
     * Toggle audio
     */
    audioSet() {
      this.$notify({
        title: '成功',
        message: '音频',
        type: 'success'
      });
    },
    /**
     * Share screen
     */
    shardScreenSet() { //待后续扩展
      if (!this.localRtcPc) {
        this.$notify.warning("请先进行通话");
        return false;
      }

      const displayMediaStreamConstraints = {
        video: {
          cursor: "always"
        },
        audio: true
      };

      // Get display media stream
      if (navigator.mediaDevices.getDisplayMedia) {
        navigator.mediaDevices.getDisplayMedia(displayMediaStreamConstraints).then(mediaStream => {
          this.localStream = mediaStream;
          const senders = this.localRtcPc.getSenders();
          const sender = senders.find(s => s.track.kind === 'video');
          sender.replaceTrack(mediaStream.getVideoTracks()[0]);

          // Handle "stop sharing"
          mediaStream.getVideoTracks()[0].onended = () => {
            this.$notify({
              title: '成功',
              message: '屏幕共享已关闭,正在切换为摄像头数据,请稍后...',
              type: 'success'
            });
            this.switchCamera();
          };
        }).catch(error => {
          console.log("error", error);
          this.$message.error('您已取消屏幕分享');
        });
      } else {
        console.log("navigator.mediaDevices.getDisplayMedia false");
        this.$message.error('浏览器不不支持');
      }
    },
    /**
     * Switch to camera stream
     */
    async switchCamera() { //待后续扩展
      this.localStream = await this.getLocalUserMedia();
      const senders = this.localRtcPc.getSenders();
      const sender = senders.find(s => s.track.kind === 'video');
      sender.replaceTrack(this.localStream.getVideoTracks()[0]);
    },
    /**
     * End the call
     */
    endCall() { //
      if (!this.localRtcPc) {
        this.$notify.warning("您还没进行通话呢,请先进行通话呢~");
        return false;
      }
      this.closeLocalStream();
      this.closeRemoteStream();
      this.$notify({
        title: '成功',
        message: '关闭通话',
        type: 'success'
      });
    },
    /**
     * Close local stream
     */
    closeLocalStream() { //关闭本地流
      const videoEle = document.getElementById('localVideo');
      const stream = videoEle.srcObject;
      if (stream) {
        stream.getTracks().forEach(track => track.stop());
      }
    },
    /**
     * Close remote stream
     */
    closeRemoteStream() { //关闭对方流
      const videoEle = document.getElementById('remoteVideo');
      const stream = videoEle.srcObject;
      if (stream) {
        stream.getTracks().forEach(track => track.stop());
      }
    }
  }
}
</script>

<style>
.video-container {
  width: 100%;
}

.button-container {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.local-video,
.remote-video {
  width: 40%;
  height: 500px;
  object-fit: contain;
  border-radius: 20px;
  margin: 4%;
  background-color: #cdcdcd;
}

button {
  min-width: 80px;
  min-height: 40px;
  width: 10%;
  padding: 6px 12px;
  background-color: #0dd693;
  color: #fff;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  margin-right: 20px;
}
</style>