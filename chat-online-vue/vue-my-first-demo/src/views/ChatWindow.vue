<template>
  <div class="container">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="left-icon" v-for="(channel, index) in channels" :key="index" @click="selectChannel(channel)">
        <img :src="channel.url" :alt="channel.name" />
      </div>
      <!-- <button type="button" @click="updateFriendTest()">更新好友信息测试</button> -->
    </div>

    <!-- 好友列表 -->
    <div class="friends-list">
      <div class="user-card">
        <div class="username-label">{{ userInfo_from_store.name }}</div>
        <div class="notify-button" title="好友通知" @click="showFriendRequests = true">
          好友通知
        </div>
      </div>
      <div class="friend-list-head">
        <input class="friend-list-head-search" type="text" placeholder="搜索" />
        <div class="friend-list-head-add" title="加好友" @click="showPopup = true">
          +
        </div>
      </div>
      <div class="grouping-container" v-for="(grouping, index) in groupedFriends">
        <div class="grouping-bar" :key="grouping.groupingId" @click="toggleGroup(grouping)">
          <span v-if="grouping.isExpanded">∨</span>
          <span v-else>∧</span>
          {{ grouping.groupingName }}
        </div>
        <div :class="friend.userId === this.now_chat_id ? 'friend-selected' : 'friends'" class="friends-click"
          v-if="grouping.isExpanded" v-for="friend in grouping.friends" :key="friend.userId"
          @click="selectFriend(friend.userId, friend.name)">
          <div class="avatar-and-badge">
            <div class="badge" v-if="getUnreadNum(friend.userId) > 0">
              {{ getUnreadNum(friend.userId) }}
            </div>
            <img class="friends-avatar" src="../assets/私聊头像.png" :alt="friend.name" />
          </div>
          <!-- <span class="friends-nickname">{{
            friend.name + "(" + friend.username + ")"
          }}</span> -->
          <div class="friends-nickname">
            <div class="marquee-wrap">
              <div class="marquee-content">
                {{ friend.name + "(" + friend.username + ")" }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- <p>query获取到：{{ userInfo_from_query.name }}</p><br/> -->
    <!-- <p>store获取到：{{ userInfo_from_store.name }}</p> -->


    <transition name="fade">
      <AddFriend v-if="showPopup" @close="showPopup = false"></AddFriend>
    </transition>
    <transition name="fade">
      <FriendRequests v-if="showFriendRequests" @close="showFriendRequests = false"></FriendRequests>
    </transition>

    <!-- 聊天框 -->
    <div class="dialog-container" :style="{ width: `${dialogContainerWidth}px` }">
      <div class="dialog-header">{{ chatTo }} {{ errorMessage }}</div>
      <div ref="scrollContainer" class="dialog-body" v-show="friendSelected">
        <div v-for="(message, index) in messages" :key="index" class="message" :class="{
          sender: message.sender === 'user',
          receiver: message.sender === 'other',
        }">
          <img src="../assets/私聊头像.png" class="message-avatar" />
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
        <button class="download-button" @click="downloadPrivateMessages">下载聊天记录</button>
        <button class="send-button" @click="sendMessage">发送</button>
      </div>
    </div>

    <!-- 在线广播 -->
    <div class="broadcast-container" v-show="showBroadcast">
      <div class="dialog-header">在线广播</div>
      <!-- <FriendRequests /> -->
    </div>
  </div>
</template>

<script>
import axios from "axios";
import AddFriend from './AddFriend'
import FriendRequests from './FriendRequests'
import { ref, nextTick } from 'vue';

export default {
  name: "ChatWithFriend",
  components: {
    AddFriend,
    FriendRequests,
  },
  setup() {
    const scrollContainer = ref(null);

    const scrollToBottom = () => {
      const container = scrollContainer.value;
      if (container) {
        container.scrollTop = container.scrollHeight;
      }
    };

    return {
      scrollContainer,
      scrollToBottom
    };
  },
  created() {
    console.log("ChatWithFriend获取到参数", this.$route.params.userName);
    // this.userInfo_from_query.id=this.$route.query.id;
    // this.userInfo_from_query.userName=this.$route.query.userName;
    // this.userInfo_from_query.name=this.$route.query.name;

    // 获取当前页面的IP地址
    var currentUrl = window.location.href;
    this.hostname = new URL(currentUrl).hostname;
    console.log("获取网址：http://" + this.hostname + ":8080/");

    const jsonParsed = JSON.parse(sessionStorage.getItem("userInfo"));
    if (jsonParsed) {
      this.userInfo_from_store = jsonParsed;
      this.getFriendList();
      this.getGroupingList();
      // 设置定时器每1秒调用一次fetchFriendRequests
      // this.intervalId = setInterval(this.getFriendList, 1000);
      //应该在同意好友请求之后再调用，而不是定时调用
    }
  },
  computed: {
    groupedFriends() {
      const grouped = this.groupings.map(grouping => {
        return {
          ...grouping,//...将 grouping 对象中的所有键值对复制到新对象中
          isExpanded: true, // 默认展开
          friends: this.friends.filter(friend => friend.groupingId === grouping.groupingId),
          //过滤 friends 数组，只保留 groupingId 与当前分组对象的 groupingId 相匹配的朋友。
        };
      });
      // 提取没有分组的朋友
      const ungroupedFriends = {
        userId: this.userInfo_from_store.userId,
        groupingId: -1,
        groupingName: '未分组',
        isExpanded: true, // 默认展开
        friends: this.friends.filter(friend => !friend.groupingId)
      };

      return [...grouped, ungroupedFriends];
    },
  },

  data() {
    return {
      hostname: null,
      showPopup: false,
      showFriendRequests: false,
      channels: [
        { name: "私聊", url: require("../assets/私聊.png") },
        { name: "群聊", url: require("../assets/群聊.png") },
        { name: "探索", url: require("../assets/探索.png") },
        { name: "管理", url: require("../assets/管理.png") },
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
      groupings: [
        //{userId:,groupingId:,groupingName:}
      ],
      userInfo_from_query: {
        id: 0,
        userName: "no",
        name: "no",
      },
      userInfo_from_store: {
        userId: 0,
        userName: "no",
        name: "no",
        token: "0",
      },
      messages: [],
      broadcastMessages: [], // 在线广播信息
      unreadMessageNum: [
        // {friendId: 0,unreadNum: 0,}
      ],
      newMessage: "",
      errorMessage: "",
      now_chat_flag: 0,
      now_chat_id: 0,
      now_chat_friend_name: "",
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
      // friendClickStyle: "friends", // 好友点击样式
      updateUnreadTimer: null,
      // addFriendDialog: false, // 是否显示添加好友框框
      channelSelected: 0,
      expandedGroupIds: [], // 存储已展开的分组的 ID
    };
  },
  props: {},
  mounted() {
    this.ws = new WebSocket(
      "ws://" + this.hostname + ":8080/api/chat/" + this.userInfo_from_store.userId
    );

    this.ws.onopen = () => {
      console.log("WebSocket connection established");
    };

    this.ws.onmessage = (event) => {
      const msg = JSON.parse(event.data);
      console.log(msg);
      if (
        (msg.fromGroup == false) &
        (msg.fromId !== this.userInfo_from_store.userId)
      ) {
        console.log("Messages got!");
        this.messages.push({
          text: msg.message,
          sender:
            msg.fromId === this.userInfo_from_store.userId ? "user" : "other",
          time: msg.time,
          name: msg.name,
        });
      }
    };

    this.ws.onerror = (error) => {
      console.error("WebSocket error:", error);
    };

    this.ws.onclose = () => {
      console.log("WebSocket connection closed");
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
    //       url: "http://" + this.hostname + ":8080/api/user/updateFriend",
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
        url: "http://" + this.hostname + ":8080/api/user/getFriendByIdWithGroupingId",
        headers: {
          token: this.userInfo_from_store.token,
        },
        params: {
          id: this.userInfo_from_store.userId,
        },
      })
        .then((res) => {
          console.log("ChatWithFriend axios收到好友列表数据:");
          console.log(res.data);
          this.friends = res.data.data;

          // 获取好友数据后立即查询有无未读新消息
          this.updateUnreadMessageNum();
        })
        .catch((error) => {
          console.error("请求失败", error);
          // 在这里可以进行错误处理，比如给用户提示或者进行其他操作
          if (error.response) {
            this.errorMessage = `请求失败，状态码：${error.response.status}`;
          } else {
            this.errorMessage = "请求失败，请重试。";
          }
        });
    },
    getGroupingList() {
      axios({
        methods: "get",
        url: "http://" + this.hostname + ":8080/api/grouping/getAllGrouping",
        headers: {
          token: this.userInfo_from_store.token,
        },
        params: {
          userId: this.userInfo_from_store.userId,
        }
      })
        .then(response => {
          console.log("获取分组列表");
          console.log(response.data);
          const groupingList = response.data.data;
          groupingList.forEach((grouping) => {
            this.groupings.push(grouping)
          });
          console.log("测试输出");
          this.groupings.forEach((grouping) => {
            console.log(grouping);
          })
        })
        .catch((error) => {
          console.error("好友分组请求失败", error);
          if (error.response) {
            this.errorMessage = `好友分组请求失败，状态码：${error.response.status}`;
          } else {
            this.errorMessage = "好友分组请求失败，请重试。";
          }
        })
    },
    updateUnreadMessageNum() {
      console.log("查询新消息");
      if (this.friends.length == 0) {
        console.log("朋友数组为空");
      }
      this.friends.forEach((friend) => {
        axios({
          method: "get",
          url: "http://" + this.hostname + ":8080/api/message/getPrivateMessageUnreadNum",
          params: {
            userId: this.userInfo_from_store.userId,
            friendId: friend.userId,
          },
          headers: {
            "Content-Type": "application/json",
            token: this.userInfo_from_store.token,
          },
        })
          .then((response) => {
            console.log(friend.name + "新消息：");
            console.log(response.data);
            if (response.data.code == 0) {
              this.errorMessage = response.data.msg;
            } else if (response.data.code == 1) {
              const existingUnreadMessageNum = this.unreadMessageNum.find(
                (item) => item.friendId === friend.userId
              );
              if (existingUnreadMessageNum) {
                if (existingUnreadMessageNum.friendId != this.now_chat_id) {
                  existingUnreadMessageNum.unreadNum = response.data.data;
                } else {
                  existingUnreadMessageNum.unreadNum = 0;
                  this.updateReadPrivateMessageStatus();
                }
              } else {
                this.unreadMessageNum.push({
                  friendId: friend.userId,
                  unreadNum: response.data.data,
                });
              }
            }
          })
          .catch((error) => {
            console.error("未读消息请求失败", error);
            if (error.response) {
              this.errorMessage = `未读消息请求失败，状态码：${error.response.status}`;
            } else {
              this.errorMessage = "未读消息请求失败，请重试。";
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
      console.log("Selected Channel:", channel.name);
      if (channel.name === "私聊") this.resetMiddle();
      else if (channel.name === "群聊") {
        this.$router.push({
          path: "/chatWithGroup/" + this.userInfo_from_store.userName,
        });
      }
      else if (channel.name === "管理") {
        this.$router.push({
          path: "/Management/" + this.userInfo_from_store.userName.userName,
        });
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
      // this.friendClickStyle = "friends";
    },

    updateReadPrivateMessageStatus() {
      axios({
        method: "patch",
        url: "http://" + this.hostname + ":8080/api/message/updateReadPrivateMessageStatus",
        params: {
          userId: this.userInfo_from_store.userId,
          friendId: this.now_chat_id,
        },
        headers: {
          "Content-Type": "application/json",
          token: this.userInfo_from_store.token,
        },
      })
        .then((response) => {
          console.log("updateReadPrivateMessageStatus:");
          console.log(response.data);
          if (response.data.code == 0) {
            this.errorMessage = response.data.msg;
          } else if (response.data.code == 1) {
            // const message = this.unreadMessageNum.find(item => item.friendId === friendId);
            // if(message)
            // {
            //   message.unreadMessageNum=0;
            // }
          }
        })
        .catch((error) => {
          console.error("请求失败", error);
          if (error.response) {
            this.errorMessage = `请求失败，状态码：${error.response.status}`;
          } else {
            this.errorMessage = "请求失败，请重试。";
          }
        });
    },
    getPrivateMessages() {
      axios({
        method: "get",
        url: "http://" + this.hostname + ":8080/api/message/getPrivateMessages",
        params: {
          senderId: this.userInfo_from_store.userId,
          receiverId: this.now_chat_id,
        },
        headers: {
          "Content-Type": "application/json",
          token: this.userInfo_from_store.token,
        },
      })
        .then((response) => {
          console.log("getPrivateMessages:");
          console.log(response.data);
          if (response.data.code == 0) {
            this.errorMessage = response.data.msg;
          } else if (response.data.code == 1) {
            this.messages = [];
            response.data.data.forEach((msg) => {
              this.messages.push({
                text: msg.textMessage,
                sender:
                  msg.senderId === this.userInfo_from_store.userId
                    ? "user"
                    : "other",
                time: msg.sendTimeString,
                name:
                  msg.senderId === this.userInfo_from_store.userId
                    ? this.userInfo_from_store.name
                    : this.now_chat_friend_name,
              });
            });
            this.messages.sort((a, b) => {
              return new Date(a.time) - new Date(b.time);
            });
            nextTick(() => {
              // DOM 更新完成后调用 scrollToBottom
              this.scrollToBottom();
            });
          }
        })
        .catch((error) => {
          console.error("请求失败", error);
          if (error.response) {
            this.errorMessage = `请求失败，状态码：${error.response.status}`;
          } else {
            this.errorMessage = "请求失败，请重试。";
          }
        });
    },
    downloadPrivateMessages() {
      axios({
        url: "http://" + this.hostname + ":8080/api/message/downloadPrivateMessage",
        method: "GET",
        responseType: "blob",
        params: {
          userId: this.userInfo_from_store.userId,
          friendId: this.now_chat_id
        },
        headers: {
          'Content-Type': 'application/json',
          'token': this.userInfo_from_store.token,
        },
      }).then(response => {
        if (response.status === 204) {
          alert("没有消息记录可下载");
          return;
        }
        const blob = new Blob([response.data], { type: 'text/csv;charset=utf-8;' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'chat_record.csv');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }).catch(error => {
        console.error('下载聊天记录失败:', error);
      });
    },
    toggleGroup(grouping) {
      // 切换分组的展开状态
      if (grouping) {
        grouping.isExpanded = !grouping.isExpanded;
      }
      console.log(this.groupedFriends.find(g => g.groupingId === grouping.groupingId));
      this.$forceUpdate(); // 强制更新组件，不加没效果
    },
    selectFriend(id, name) {
      this.now_chat_id = id;
      this.now_chat_friend_name = name;
      console.log("选中用户，id为：" + this.now_chat_id);
      this.friendSelected = true;
      this.chatTo = this.now_chat_friend_name;
      this.friendClickStyle = "friend-selected";
      this.getPrivateMessages();
      const unreadNumfond = this.unreadMessageNum.find(
        (item) => item.friendId === id
      );
      if (unreadNumfond) {
        unreadNumfond.unreadNum = 0;
      }
      this.updateReadPrivateMessageStatus();
    },
    getFriendClass(id) {
      return this.now_chat_id === id ? "friend-selected" : "friends";
    },
    BroadcastMessages(message) {
      this.broadcastMessages.push(message);
    },
    sendMessage() {
      if (this.newMessage.trim() !== "") {
        const message_send = {
          fromId: this.userInfo_from_store.userId,
          toId: this.now_chat_id,
          message: this.newMessage,
          name: this.userInfo_from_store.name,
          time: new Date().toLocaleString(),
        };
        this.ws.send(JSON.stringify(message_send));
        console.log(message_send);
        this.messages.push({
          text: this.newMessage,
          sender: "user",
          name: this.userInfo_from_store.name,
          time: message_send.time,
        });
        this.newMessage = "";
      }
    },
    getUnreadNum(friendId) {
      const message = this.unreadMessageNum.find(
        (item) => item.friendId === friendId
      );
      return message ? message.unreadNum : 0;
    },
  },
};
</script>


<style scoped>
/* 设置滚动条的样式 */
::-webkit-scrollbar {
  width: 8px;
  /* 设置滚动条的宽度 */
}

/* 设置滑块的样式 */
::-webkit-scrollbar-thumb {
  background-color: #2f3643;
  /* 设置滑块的背景颜色 */
  border-radius: 3px;
  /* 设置滑块的圆角 */
}

/* 设置滑道的样式 */
::-webkit-scrollbar-track {
  background-color: #46494f;
  /* 设置滑道的背景颜色 */
}

.container {
  display: flex;
  /*有没有没啥区别*/
  flex-direction: row;
  height: 100vh;
  min-width: 720px;
}

.sidebar {
  position: fixed;
  width: 80px;
  /* 调整宽度 */
  height: 100%;
  background-color: #1d1d24;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding-top: 40px;
}

.username-label {
  margin-left: 40px;
  margin-top: 20px;
  margin-bottom: 8px;
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
  background-color: #272a37;
  /* #2B2D31黑色背景 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  border-left: #374347 solid 1px;
  overflow-y: auto;
  /*自动添加滚动条 */
  overflow-x: visible;
  /*自动添加滚动条 */
}


.friends {
  width: 100%;
  display: flex;
  /* 使用 flexbox 布局 */
  align-items: center;
  /* 垂直居中 */
  transition: background-color 0.2s ease;
  padding: 8px 0px 8px 4px;
  /* 上 右 下 左 */
  margin: 6px 0px 2px -4px;
  /* 上 右 下 左 */
}

.friends:hover {
  background-color: #5959c9;
}

.friend-selected {
  width: 100%;
  display: flex;
  /* 使用 flexbox 布局 */
  align-items: center;
  /* 垂直居中 */
  background-color: #5959c9;
  padding: 8px 0px 8px 4px;
  /* 上 右 下 左 */
  margin: 2px 0px 6px -4px;
  /* 上 右 下 左 */
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
  color: #ebebeb;
  /* 白色字体 */
  max-width: 100px;
  overflow: hidden;
  /* 隐藏溢出的文本 */
  white-space: nowrap;
  /* 防止文本换行 */
  text-overflow: ellipsis;
  /* 用省略号表示溢出的文本 */
  position: relative;
}

.friends-nickname:hover .marquee-wrap {
  width: 100%;
  animation: marquee-wrap 4s infinite linear;
}

.friends-nickname:hover .marquee-content {
  float: left;
  white-space: nowrap;
  min-width: 100%;
  animation: marquee-content 4s infinite linear;
}

@keyframes marquee-wrap {

  0%,
  30% {
    transform: translateX(0);
  }

  70%,
  100% {
    transform: translateX(100%);
  }
}

@keyframes marquee-content {

  0%,
  30% {
    transform: translateX(0);
  }

  70%,
  100% {
    transform: translateX(-100%);
  }
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
  margin: 8px;
  padding: 0;
  background-color: #252d43;
  border-top: rgb(56, 103, 102) solid 1px;
  border-bottom: rgb(56, 103, 102) solid 1px;
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
  background-color: #d8d9d8;
}

.friend-list-head-add {
  height: 26px;
  width: 26px;
  margin: 10px 10px;
  padding: 0;
  border-radius: 5px;
  background-color: #0eb67e;
  font-size: 21px;
  line-height: 27px;
  text-align: center;
  color: #595959;
  font-weight: 200;
  cursor: pointer;
}

.friend-list-head-add:hover {
  background-color: #0e9f6f;
}

.dialog-container {
  flex: 2;
  position: fixed;
  top: 0;
  height: 100%;
  left: 320px;
  background-color: #383d4d;
  /* #313338 */
  color: #fff;
  display: flex;
  flex-direction: column;
  /* 垂直排列子元素 */
  border-left: #3d4e54 solid 1px;
}

.dialog-header {
  padding: 10px;
  background-color: #2f3241;
  border-bottom-left-radius: 8px;
  color: #e0e0e0;
  font-weight: bold;
  border-bottom: #2c4d5d solid 1px;
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
  background-color: #2f3241;
  align-items: center;
  justify-content: space-between;
  border-top: #2c4d5d solid 1px;
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

  background-color: #454b60;
  color: #fff;
}

.message-text:hover {
  background-color: #323643;
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
  background-color: #318ffa;
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
  background-color: #343947;
  /* #44464c*/
  position: fixed;
  right: 0;
  justify-content: flex-start;
}

.broadcast-header {
  padding: 10px;
  background-color: #2f3241;
  border-bottom-right-radius: 8px;
  color: #e0e0e0;
  font-weight: bold;
  border-bottom: #2c4d5d solid 1px;
}

input[type="text"] {
  width: 75%;
  padding: 12px;
  margin: 20px;
  border: none;
  border-radius: 8px;
}

.send-button {
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

.download-button {
  min-width: 90px;
  min-height: 40px;
  height: 40px;
  width: 90px;
  padding: 6px 2px;
  background-color: #0e9f6f;
  color: #fff;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  margin-right: 20px;
}

.notify-button {
  font-size: 12px;
  height: 20px;
  width: 60px;
  padding: 6px 2px;
  background-color: #0e9f6f;
  color: #fff;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.user-card {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  align-items: center;
}


.grouping-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  left: 0;
}

.grouping-bar {
  width: calc(100% - 20px);
  height: 24px;
  background-color: #3792fb;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  padding: 0 4px;
  margin: 8px 20px 8px 0px;
  /* 上 右 下 左 */
  font-weight: bold;
  color: rgb(50, 50, 50);
}
</style>