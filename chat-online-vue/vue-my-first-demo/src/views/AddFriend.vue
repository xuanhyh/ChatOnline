<template>
  <div class="popup">
    <div class="popup-header">
      请输入用户名：
      <input v-model="friendUsername" type="text" class="input-field">
      <button @click="handleSelect" class="search-button">查找用户</button>
    </div>

    <div v-if="userInfoShow" class="user-card">
      <!-- <div v-if="true" class="user-card"> -->
      <div class="avatar-and-point">
        <img :src="this.userInfo.avatarUrl ? this.userInfo.avatarUrl : this.defaultAvatar" alt="用户头像" class="avatar">
        <div :class="userInfo.state === 0 ? 'online' : 'not-online'"></div>
      </div>
      <div class="user-info">
        <p>用户名：{{ userInfo.username }}</p>
        <p>用户昵称：{{ userInfo.name }}</p>
        <p>性别：{{ userInfo.sex }}</p>
      </div>
      <button @click="handleAddFriend" class="add-friend-button">添加</button>
    </div>

    <div class="errorMessage">
      {{ this.errorMessage }}
    </div>

    <button @click="$emit('close')" class="close-button">关闭</button>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  created() {
    const jsonParsed = JSON.parse(sessionStorage.getItem("userInfo"));
    if (jsonParsed) {
      this.userInfo_from_store = jsonParsed;
    }

    // 获取当前页面的IP地址
    var currentUrl = window.location.href;
    this.hostname = new URL(currentUrl).hostname;
    console.log("http://" + this.hostname + ":8080/");

  },
  data() {
    return {
      friendUsername: '',
      response: null,
      userInfoShow: false,
      errorMessage: '',
      defaultAvatar: require('../assets/私聊头像.png'),
      userInfo: {
        username: '无',
        name: '无',
        sex: '无',
        avatarUrl: null,
      },
      addFriend: {
        toUserId: 0,
        time: "2024-05-23 11:58:25"
      },
      userInfo_from_store: {
        userId: 0,
        userName: "no",
        name: "no",
        token: "0",
      }
    }
  },
  methods: {
    async handleSelect() {
      console.log(this.friendUsername);
      if (this.friendUsername.trim() == '') {
        this.errorMessage = '请输入用户名';
        return;
      }
      try {
        const response = await axios.get("http://" + this.hostname + ":8080/api/user/searchFriend", {
          params: {
            username: this.friendUsername
          },
          headers: {
            'token': this.userInfo_from_store.token
          }
        });
        this.response = response.data;
        console.log(this.response.code);
      } catch (error) {
        console.error('查找失败', error);
        this.errorMessage = '查找失败'
      }
      if (this.response.code === 1) {
        this.userInfoShow = true;
        this.userInfo = this.response.data;
      } else {
        this.userInfoShow = false;
        console.log(this.response.code);
      }
    },
    handleAddFriend() {
      this.addFriend.toUserId = this.userInfo.userId;

      const now = new Date();
      const year = now.getFullYear();
      const month = (now.getMonth() + 1).toString().padStart(2, '0');
      const day = now.getDate().toString().padStart(2, '0');
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');
      this.addFriend.sendTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      this.addFriend.fromUserUserName = this.userInfo_from_store.userName;
      this.addFriend.fromUserName = this.userInfo_from_store.name;
      axios({
        method: "post",
        url: "http://" + this.hostname + ":8080/api/user/sendFriendRequest",
        headers: {
          'Content-Type': 'application/json',
          'token': this.userInfo_from_store.token
        },
        data: this.addFriend
      })
        .then(res => {
          console.log("收到响应" + res);
          if (res.data.code === 1) {
            alert('发送好友请求成功！');
          } else {
            alert('发送好友请求失败：' + res.data.msg);
          }
        });
    }
  }
}
</script>
<style scoped>
.popup {
  /* 弹窗样式 */
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #5947df;
  color: #ffffff;
  padding: 20px;
  border-radius: 20px;
  z-index: 999;
  width: 400px;
  border-top: #180a91 solid 3px;
  border-right: #180a91 solid 5px;
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
  /* 水平偏移 垂直偏移 模糊程度 （尺寸） 颜色 （内部还是外部） */
}

.popup-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.input-field {
  padding: 5px;
  border-radius: 5px;
  border: none;
  width: 40%;
}

.search-button {
  padding: 5px 10px;
  background-color: #0db58b;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.user-card {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(201, 198, 229, 0.1);
  padding: 10px;
  border-radius: 10px;
  margin: 8px 0;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 140px;
}

.user-info p {
  margin: 5px 0;
}

.add-friend-button {
  padding: 6px 12px;
  background-color: #298dff;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  align-self: center;
}

.errorMessage {
  text-align: center;
  color: red;
  min-height: 24px;
  margin: 8px;
  font-weight: bold;
}

.close-button {
  padding: 5px 10px;
  background-color: #e44141;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  display: block;
  width: 80px;
  margin: 0 auto;
}

.avatar-and-point {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100px;
}

.online {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin: 8px;
  background-color: #00ff11;
}

.not-online {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin: 8px;
  background-color: #444444;
}
</style>
