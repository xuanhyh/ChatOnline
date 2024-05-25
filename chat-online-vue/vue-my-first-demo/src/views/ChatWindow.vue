<template>
  <div class="container">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="left-icon" v-for="(channel, index) in channels" :key="index" @click="selectChannel(channel)">
        <img :src=channel.url :alt="channel.name">
      </div>
      <!-- <button type="button" @click="updateFriendTest()">更新好友信息测试</button> -->
    </div>
    <!-- 好友列表 -->
    <div class="friends-list">
      <div class="username-label">{{ userInfo_from_store.name }}</div>
      <div class="friend-list-head">
        <input class="friend-list-head-search" type="text" placeholder="搜索">
        <div class="friend-list-head-add" title="加好友" @click="showAddFriendDialog()">+</div>
      </div>
      <div :class="friendClickStyle" class="friends-click" v-for="(friend, index) in friends" :key="index"
        @click="selectFriend(friend.userId, friend.name)">
        <div class="avatar-and-badge">
          <div class="badge" v-if="getUnreadNum(friend.userId) > 0">{{ getUnreadNum(friend.userId) }}</div>
          <img class="friends-avatar" src='../assets/私聊头像.png' :alt="friend.name">
        </div>
        <span class="friends-nickname">{{ friend.name + '(' + friend.username + ')' }}</span>
      </div>
    </div>

    <!-- <p>query获取到：{{ userInfo_from_query.name }}</p><br/> -->
    <!-- <p>store获取到：{{ userInfo_from_store.name }}</p> -->

    <!-- 聊天框 -->
    <div class="dialog-container" :style="{ width: `${dialogContainerWidth}px` }">
      <div class="dialog-header">
        {{ chatTo }} {{ errorMessage }}
      </div>
      <div class="dialog-body" v-show="friendSelected">
        <div v-for="(message, index) in messages" :key="index" class="message"
          :class="{ 'sender': message.sender === 'user', 'receiver': message.sender === 'other' }">
          <img src="../assets/私聊头像.png" class="message-avatar">
          <div class="message-info">
            <div class="message-meta">
              <span class="message-sender">{{ message.name }}</span>
              <span class="message-time">{{ message.time }}</span>
            </div>
            <div class="message-text">{{ message.text }}</div>
          </div>
        </div>
      </div>
      <div class="dialog-footer" v-show="friendSelected">
        <input type="text" v-model="newMessage" @keyup.enter="sendMessage" placeholder="输入消息..." />
        <!-- @keyup.enter="sendMessage"在键盘按下回车时调用sendMessage函数 -->
        <button @click="sendMessage">发送</button>
      </div>
    </div>

    <!-- 在线广播 -->
    <div class="broadcast-container" v-show="showBroadcast">
      <div class="dialog-header">
        在线广播
      </div>
    </div>

  </div>
</template>

<script>
import axios from "axios"

export default {
  name: 'ChatWithFriend',
  created() {
    console.log('ChatWithFriend获取到参数', this.$route.params.userName)
    // this.userInfo_from_query.id=this.$route.query.id;
    // this.userInfo_from_query.userName=this.$route.query.userName;
    // this.userInfo_from_query.name=this.$route.query.name;
    const jsonParsed = JSON.parse(sessionStorage.getItem('userInfo'));
    if (jsonParsed) {
      this.userInfo_from_store = jsonParsed;
      this.getFriendList();
    }
  },

  data() {
    return {
      channels: [
        { name: '私聊', url: require("../assets/私聊.png") },
        { name: '群聊', url: require("../assets/群聊.png") },
        { name: 'Channel 2', url: require("../assets/探索.png") }
      ],
      friends: [
  /*{
    "userId": 2,
    "name": "李四",
    "username": "lisi",
    "password": "e10adc3949ba59abbe56e057f20f883e",
    "sex": "女",
    "avatarUrl": "https://s2.loli.net/2024/05/20/YuRryljA9KtLQBF.jpg",
    "state": 0,
    "email": "234@qq.com",
    "createTime":,
    "updateTime":
    }
    */
      ],
      userInfo_from_query: {
        id: 0, userName: "no", name: "no",
      },
      userInfo_from_store: {
        userId: 0, userName: "no", name: "no", token: "0"
      },
      messages: [],
      broadcastMessages: [], // 在线广播信息
      unreadMessageNum: [
        // {friendId: 0,unreadNum: 0,}
      ],
      newMessage: '',
      errorMessage: '',
      now_chat_flag: 0,
      now_chat_id: 0,
      now_chat_friend_name: '',
      ws: null,
      // updateFriendTestData:{
      //   friendId: 1,
      //   remark: '打备注测试',
      //   groupingId: 8888,
      // },
      showBroadcast: true, // 显示右侧广播
      dialogContainerWidth: 0, // 中间dialog-container的宽度
      friendSelected: false, // 是否选中一个好友进行聊天
      chatTo: "寻找或开始新的对话", // dialgo-container顶部显示文字
      friendClickStyle: "friends", // 好友点击样式
      updateUnreadTimer: null,
      addFriendDialog: false, // 是否显示添加好友框框    
    }
  },
  props: {
  },
  mounted() {
    this.ws = new WebSocket('ws://localhost:8080/api/chat/' + this.userInfo_from_store.userId);

    this.ws.onopen = () => {
      console.log('WebSocket connection established');
    };

    this.ws.onmessage = (event) => {
      const msg = JSON.parse(event.data);
      console.log(msg);
      if (msg.fromGroup==false & msg.fromId !== this.userInfo_from_store.userId) {
        console.log('Messages got!');
        this.messages.push({
          text: msg.message,
          sender: msg.fromId === this.userInfo_from_store.userId ? 'user' : 'other',
          time: msg.time,
          name: msg.name,
        });
      }
    };

    this.ws.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    this.ws.onclose = () => {
      console.log('WebSocket connection closed');
    };

    window.addEventListener("resize", this.checkWindowSize);
    this.checkWindowSize();

    //定时查看新未读消息--------------------
    // this.updateUnreadTimer = setInterval (() => {
    //   this.updateUnreadMessageNum()
    // }, 5000);
  },
  beforeDestroy() {
    if (this.ws) {
      this.ws.close();
    }
    window.removeEventListener("resize", this.checkWindowSize);
    clearInterval(this.updateUnreadTimer);
  },

  methods: {
    // updateFriendTest(){
    //   axios({
    //       method: "put",
    //       url: "http://localhost:8080/api/user/updateFriend",
    //       headers: {
    //         'token': this.userInfo_from_store.token,
    //       },
    //       data:this.updateFriendTestData,
    //   })
    //   .then(res => {
    //       console.log(res.data);
    //       this.friends=res.data.data;
    //   })
    //   .catch(error => {
    //       console.error('请求失败', error);
    //       if (error.response) {
    //         this.errorMessage = `请求失败，状态码：${error.response.status}`;
    //       } else {
    //         this.errorMessage = '请求失败，请重试。';
    //       }
    //   });
    // },
    getFriendList() {
      axios({
        method: "get",
        url: "http://localhost:8080/api/user/friend",
        headers: {
          'token': this.userInfo_from_store.token,
        },
        params: {
          id: this.userInfo_from_store.userId
        },
      })
        .then(res => {
          console.log("ChatWithFriend axios收到好友列表数据:");
          console.log(res.data);
          this.friends = res.data.data;

          // 获取好友数据后立即查询有无未读新消息
          this.updateUnreadMessageNum()
        })
        .catch(error => {
          console.error('请求失败', error);
          // 在这里可以进行错误处理，比如给用户提示或者进行其他操作
          if (error.response) {
            this.errorMessage = `请求失败，状态码：${error.response.status}`;
          } else {
            this.errorMessage = '请求失败，请重试。';
          }
        });
    },
    updateUnreadMessageNum() {
      console.log("查询新消息");
      if (this.friends.length == 0) {
        console.log("朋友数组为空");
      }
      this.friends.forEach(friend => {
        axios({
          method: "get",
          url: "http://localhost:8080/api/message/getPrivateMessageUnreadNum",
          params: {
            userId: this.userInfo_from_store.userId,
            friendId: friend.userId,
          },
          headers: {
            'Content-Type': 'application/json',
            'token': this.userInfo_from_store.token,
          },
        })
          .then(response => {
            console.log(response.data);
            if (response.data.code == 0) {
              this.errorMessage = response.data.msg;
            }
            else if (response.data.code == 1) {
              const existingUnreadMessageNum = this.unreadMessageNum.find(item => item.friendId === friend.userId);
              if (existingUnreadMessageNum) {
                if (existingUnreadMessageNum.friendId != this.now_chat_id) {
                  existingUnreadMessageNum.unreadNum = response.data.data;
                }
                else {
                  existingUnreadMessageNum.unreadNum = 0;
                  this.updateReadPrivateMessageStatus();
                }
              } else {
                this.unreadMessageNum.push({
                  friendId: friend.userId,
                  unreadNum: response.data.data
                });
              }
            }
          })
          .catch(error => {
            console.error('未读消息请求失败', error);
            if (error.response) {
              this.errorMessage = `未读消息请求失败，状态码：${error.response.status}`;
            } else {
              this.errorMessage = '未读消息请求失败，请重试。';
            }
          });
      });
    },
    checkWindowSize() {
      this.showBroadcast = window.innerWidth > 1000;
      const broadcastContainerWidth = this.showBroadcast ? 240 : 0;
      const availableWidth = window.innerWidth - 320 - broadcastContainerWidth;
      this.dialogContainerWidth = availableWidth;
    },

    selectChannel(channel) {
      // 处理选择头像的逻辑，例如显示选中状态等
      console.log('Selected Channel:', channel.name);
      if (channel.name === "私聊") this.resetMiddle();
      else if (channel.name === "群聊") {
        this.$router.push({
          path: '/chatWithGroup/' + this.userInfo_from_store.userName,
        })
      }
      this.now_chat_id = 0;
    },
    // addKeyValuePairForUnreadMessage(key, value) {
    // this.$set(this.unreadMessages, key, value);
    // },
    // deleteKeyValuePair(key) {
    // Vue.delete(this.unreadMessages, key);
    // },
    resetMiddle() {
      this.friendSelected = false;
      this.chatTo = "寻找或开始新的对话";
      this.friendClickStyle = "friends";
    },

    updateReadPrivateMessageStatus() {
      axios({
        method: "patch",
        url: "http://localhost:8080/api/message/updateReadPrivateMessageStatus",
        params: {
          userId: this.userInfo_from_store.userId,
          friendId: this.now_chat_id
        },
        headers: {
          'Content-Type': 'application/json',
          'token': this.userInfo_from_store.token,
        },
      })
        .then(response => {
          console.log("updateReadPrivateMessageStatus:");
          console.log(response.data);
          if (response.data.code == 0) {
            this.errorMessage = response.data.msg;
          }
          else if (response.data.code == 1) {
            // const message = this.unreadMessageNum.find(item => item.friendId === friendId);
            // if(message)
            // {
            //   message.unreadMessageNum=0;
            // }
          }
        })
        .catch(error => {
          console.error('请求失败', error);
          if (error.response) {
            this.errorMessage = `请求失败，状态码：${error.response.status}`;
          } else {
            this.errorMessage = '请求失败，请重试。';
          }
        });
    },
    getPrivateMessages() {
      axios({
        method: "get",
        url: "http://localhost:8080/api/message/getPrivateMessages",
        params: {
          senderId: this.userInfo_from_store.userId,
          receiverId: this.now_chat_id
        },
        headers: {
          'Content-Type': 'application/json',
          'token': this.userInfo_from_store.token,
        },
      })
        .then(response => {
          console.log("getPrivateMessages:");
          console.log(response.data);
          if (response.data.code == 0) {
            this.errorMessage = response.data.msg;
          }
          else if (response.data.code == 1) {
            this.messages = [];
            response.data.data.forEach(msg => {
              this.messages.push({
                text: msg.textMessage,
                sender: msg.senderId === this.userInfo_from_store.userId ? 'user' : 'other',
                time: msg.sendTimeString,
                name: msg.senderId === this.userInfo_from_store.userId ? this.userInfo_from_store.name : this.now_chat_friend_name,
              });
            });
            this.messages.sort((a, b) => {
              return new Date(a.time) - new Date(b.time);
            });
          }
        })
        .catch(error => {
          console.error('请求失败', error);
          if (error.response) {
            this.errorMessage = `请求失败，状态码：${error.response.status}`;
          } else {
            this.errorMessage = '请求失败，请重试。';
          }
        });
    },
    selectFriend(id, name) {
      this.now_chat_id = id;
      this.now_chat_friend_name = name;
      console.log("选中用户，id为：" + this.now_chat_id);
      this.friendSelected = true;
      this.chatTo = this.friends[0].name;
      this.friendClickStyle = "friend-selected"
      this.getPrivateMessages();
      const unreadNumfond = this.unreadMessageNum.find(item => item.friendId === id);
      if (unreadNumfond) {
        unreadNumfond.unreadNum = 0
      }
      this.updateReadPrivateMessageStatus();
    },
    getFriendClass(id) {
      return this.now_chat_id === id ? 'friend-selected' : 'friends';
    },
    BroadcastMessages(message) {
      this.broadcastMessages.push(message);
    },
    sendMessage() {
      if (this.newMessage.trim() !== '') {
        const message_send = {
          fromId: this.userInfo_from_store.userId,
          toId: this.now_chat_id,
          message: this.newMessage,
          name: this.userInfo_from_store.name,
          time: new Date().toLocaleString()
        };
        this.ws.send(JSON.stringify(message_send));
        console.log(message_send);
        this.messages.push({
          text: this.newMessage,
          sender: 'user',
          name: this.userInfo_from_store.name,
          time: message_send.time
        });
        this.newMessage = '';
      }
    },
    getUnreadNum(friendId) {
      const message = this.unreadMessageNum.find(item => item.friendId === friendId);
      return message ? message.unreadNum : 0;
    },
    showAddFriendDialog() {
      console.log("点击加好友按钮成功!!!");
      this.addFriendDialog = true;
    },
  },
}
</script>


<style scoped>
.container {
  display: flex;
  /*有没有没啥区别*/
  flex-direction: row;
  height: 100vh;
  min-width: 720px;
}

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 80px;
  /* 调整宽度 */
  height: 100%;
  background-color: #232633;
  /* 黑色背景 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding-top: 40px;
}

.username-label {
  margin: 20px;
  color: white;
  font-size: 24px;
  font-family: "STXingkai", "Microsoft YaHei";
}

.left-icon {
  width: 60px;
  /* 调整头像大小 */
  height: 60px;
  /* 调整头像大小 */
  border-radius: 50%;
  /* 圆形头像 */
  overflow: hidden;
  cursor: pointer;
  transition: background-color 0.5s ease, border-radius 0.3s ease;
}

.left-icon:hover {
  border-radius: 16px;
  /* 鼠标悬浮时将头像变为圆角矩形 */
  background-color: green;
}

/* 
.left-icon + .left-icon {
  margin-top: 0px;
} */

.left-icon img {
  max-width: 50px;
  height: auto;
  object-fit: cover;
}

.friend-nav {
  height: 100%;
  width: 15vw;
  margin: 0;
  padding: 0;
  display: inline-block;
  float: left;
  overflow: hidden;
}

.friends-list {
  flex: 1;
  position: fixed;
  top: 0;
  left: 80px;
  width: 240px;
  /* 调整宽度 */
  height: 100%;
  background-color: #272A37;
  /* #2B2D31黑色背景 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

.friends {
  width: 100%;
  display: flex;
  /* 使用 flexbox 布局 */
  align-items: center;
  /* 垂直居中 */
  padding: 8px;
  transition: background-color 0.2s ease;
}

.friends:hover {
  background-color: #505c8e;
}

.friend-selected {
  width: 100%;
  display: flex;
  /* 使用 flexbox 布局 */
  align-items: center;
  /* 垂直居中 */
  padding: 8px;
  background-color: #505c8e;
}

.friends-avatar {
  width: 50px;
  /* 调整头像大小 */
  height: 50px;
  /* 调整头像大小 */
  border-radius: 50%;
  /* 圆形头像 */
  overflow: hidden;
  /*头像容器内部元素超出边界时隐藏 */
  cursor: pointer;
  /*当鼠标悬停在头像上时显示为指针 */
  transition: background-color 0.5s ease, border-radius 0.3s ease;
  margin: 0 20px;
}

.friends-avatar:hover {
  background-color: rgb(115, 136, 254);
}

.avatar-and-badge {
  width: 90px;
  height: 50px;
  position: relative;
  /* 确保子元素的定位是相对于该容器的 */
  display: inline-block;
  /* 让容器只占用必要的空间 */
}

.badge {
  position: absolute;
  /* 绝对定位 */
  top: 0;
  left: 15px;
  width: 0;
  height: 4px;
  background-color: red;
  color: white;
  /* border-radius: 50%; */
  clip-path: circle(40%);
  padding: 8px 8px;
  font-size: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.friends-avatar img {
  top: 0;
  left: 0;
  position: absolute;
  /* 绝对定位 */
  max-width: 50px;
  height: auto;
  object-fit: cover;
  z-index: 1;
}


.friends-nickname {
  font-size: 16px;
  /* 设置合适大小的字体 */
  color: #d9d9d9;
  /* 白色字体 */
}

.friends-click {
  cursor: pointer;
}

.friend-list-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 44px;
  width: 100%;
  margin: 0;
  padding: 0;
  background-color: #272A37;
  border-top: rgb(90, 81, 81) solid 1px;
  border-bottom: rgb(90, 81, 81) solid 1px;
}

.friend-list-head-search {
  flex: 1;
  height: 10px;
  width: 28px;
  margin: 10px;
  padding-left: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-size: 14px;
  background-color: #D8D9D8;
}

.friend-list-head-add {
  height: 26px;
  width: 26px;
  margin: 10px 10px;
  padding: 0;
  border-radius: 5px;
  background-color: #D8D9D8;
  font-size: 21px;
  line-height: 27px;
  text-align: center;
  color: #595959;
  font-weight: 200;
  cursor: pointer;
}

.dialog-container {
  flex: 2;
  position: fixed;
  top: 0;
  height: 100%;
  left: 320px;
  border-radius: 5px;
  background-color: #323644;
  /* #313338 */
  color: #fff;
  display: flex;
  flex-direction: column;
  /* 垂直排列子元素 */
}

.dialog-header {
  padding: 10px;
  background-color: #35383e;
  border-bottom-left-radius: 8px;
  border-bottom-right-radius: 8px;
  color: #e0e0e0;
  font-weight: bold;
}

.dialog-body {
  flex: 1;
  padding: 10px;
  height: 80%;
  min-height: 200px;
  overflow-y: auto;
}

.dialog-footer {
  display: flex;
  min-height: 100px;
  width: 100%;
  background-color: #35383e;
  align-items: center;
  justify-content: space-between;
}


.message {
  padding: 12px;
  margin-bottom: 8px;
  border-radius: 4px;
  width: auto;
  position: relative;
  /* 让伪元素相对于 .message 元素定位 */
  word-wrap: break-word;
  display: flex;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.message-info {
  display: flex;
  flex-direction: column;
}

.message-meta {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  /* 均匀排列每个元素,这里排列姓名和发送时间分别靠左和靠右 */
  align-items: center;
  margin-bottom: 4px;
  /* 底部添加4像素的边距与消息隔开 */
}

.message-sender {
  font-weight: bold;
  /* 发送者姓名粗体 */
  margin-right: 10px;
}

.message-time {
  font-size: 0.85em;
  /* 发送时间字体为正常字体的85% */
  color: #ccc;
}

.message-text {
  max-width: 90%;
  padding: 20px;

  background-color: rgb(56, 60, 75);
  color: #fff;
}

.message-text:hover {
  background-color: rgb(39, 42, 55);
}

.sender {
  /* background-color: #44464c;
  text-align: right;
  border-right: 4px solid #ffffff;
  border-top-left-radius: 12px;
  border-bottom-left-radius: 12px; */

  justify-content: flex-end;
}

.sender .message-avatar {
  order: 2;
  /* 将头像放在右侧 */
  margin-right: 0;
  margin-left: 10px;
}

.sender .message-info {
  align-items: flex-end;
}

.sender .message-text {
  float: left;
  border-radius: 20px 5px 20px 20px;
}


.receiver {
  /* background-color: #44464c;
  text-align: left;
  border-left: 4px solid #80ff9d;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px; */
  justify-content: flex-start;

}

.receiver .message-avatar {
  order: 0;
  /* 将头像放在右侧 */
  margin-right: 10px;
  margin-left: 0px;
}

.receiver .message-info {
  align-items: flex-start;
}

.receiver .message-text {
  float: right;
  border-radius: 5px 20px 20px 20px;
}


.broadcast-container {
  flex: 1;
  top: 0;
  height: 100%;
  width: 240px;
  background-color: #313338;
  /* #44464c*/
  position: fixed;
  right: 0;
  justify-content: flex-start;
}

input[type="text"] {
  width: 75%;
  padding: 12px;
  margin: 20px;
  border: none;
  border-radius: 8px;
}

button {
  min-width: 80px;
  min-height: 40px;
  width: 10%;
  padding: 6px 12px;
  background-color: #0e9f6f;
  color: #fff;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  margin-right: 20px;
}
</style>