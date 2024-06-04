<template>
    <div class="friend-requests">
      <h3>好友请求</h3>
      <div v-if="requests.length === 0">没有新的好友请求。</div>
      <div v-else>
        <div v-for="request in requests" :key="request.fromUserId" class="request-card">
          <div class="request-info">
            <p>用户名：{{ request.fromUserUserName }}</p>
            <p>昵称：{{ request.fromUserName }}</p>
          </div>
          <div class="request-actions">
            <button @click="handleAccept(request.fromUserId)">接受</button>
            <button @click="handleReject(request.fromUserId)">拒绝</button>
          </div>
        </div>
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
      this.fetchFriendRequests();
    },
    data() {
      return {
        requests: [],
        userInfo_from_store: {
          userId: 0,
          userName: "no",
          name: "no",
          token: "0",
        }
      }
    },
    methods: {
      fetchFriendRequests() {
        axios({
          method: 'get',
          url: 'http://localhost:8080/api/user/getFriendRequests',
          headers: {
            'token': this.userInfo_from_store.token
          }
        })
        .then(response => {
          this.requests = response.data.data;
        })
        .catch(error => {
          console.error('获取好友请求失败', error);
        });
      },
      handleAccept(fromUserId) {
  
        axios({
          method: 'post',
          url: 'http://localhost:8080/api/user/respondToFriendRequest',
          headers: {
            'token': this.userInfo_from_store.token
          },
          data: {
            status: 'ACCEPT',
            fromUserId: fromUserId,
          }
        })
        .then(response => {
          if (response.data.code === 1) {
            alert('接受好友请求成功');
            this.fetchFriendRequests();
          } else {
            alert('接受好友请求失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('接受好友请求失败', error);
        });
      },
      handleReject(fromUserId) {
        axios({
          method: 'post',
          url: 'http://localhost:8080/api/user/respondToFriendRequest',
          headers: {
            'token': this.userInfo_from_store.token
          },
          data: {
            status: 'REJECT',
            fromUserId: fromUserId,
          }
        })
        .then(response => {
          if (response.data.code === 1) {
            alert('拒绝好友请求成功');
            this.fetchFriendRequests();
          } else {
            alert('拒绝好友请求失败：' + response.data.msg);
          }
        })
        .catch(error => {
          console.error('拒绝好友请求失败', error);
        });
      }
    }
  }
  </script>
  
  <style scoped>
  .friend-requests {
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 8px;
  }
  
  .request-card {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 8px;
    margin-bottom: 10px;
  }
  
  .request-info {
    display: flex;
    flex-direction: column;
  }
  
  .request-actions button {
    margin-left: 10px;
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
  </style>
  