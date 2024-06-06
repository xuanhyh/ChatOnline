<template>
    <!-- 通过ID搜索 -->
    <div v-if="searchById" class="popup">
      <div class="popup-header">
        请输入群ID：
        <input v-model="groupId" type="text" class="input-field">
        <button @click="handleSelectByID" class="search-button">查找群</button>
        <button @click="searchById=false" class="alter-button">通过名称查找</button>
      </div>
  
      <div v-if="groupInfoByIdShow" class="group-card">
      <!-- <div v-if="true" class="group-card"> -->
        <div class="avatar-and-point">
          <img :src="this.groupInfoById.avatarUrl ? this.groupInfoById.avatarUrl : this.defaultAvatar" alt="群聊头像" class="avatar">
        </div>
        <div class="group-info">
          <p>群名：{{ groupInfoById.groupName }}</p>
          <p>群ID：{{ groupInfoById.groupId }}</p>
        </div>
        <button @click="handleAddGroup(groupInfoById.groupId)" class="add-group-button">添加</button>
      </div>
  
      <div class="errorMessage">
        {{this.errorMessage}}
      </div>
  
      <button @click="$emit('close')" class="close-button">关闭</button>
    </div>
    
    <!-- 通过名称搜索 -->
    <div v-else class="popup">
      <div class="popup-header">
        请输入群名：
        <input v-model="groupName" type="text" class="input-field">
        <button @click="handleSelectByName" class="search-button">查找群</button>
        <button @click="searchById=true" class="alter-button">通过ID查找</button>
      </div>
  
      <div v-for="groupInfo in groupInfoByName" v-if="groupInfoByNameShow" class="group-card">
      <!-- <div v-for="groupInfo in groupInfoByName" v-if="true" class="group-card"> -->
        <div class="avatar-and-point">
          <img :src="groupInfo.avatarUrl ? groupInfo.avatarUrl : this.defaultAvatar" alt="群聊头像" class="avatar">
        </div>
        <div class="group-info">
          <p>群名：{{ groupInfo.groupName }}</p>
          <p>群ID：{{ groupInfo.groupId }}</p>
        </div>
        <button @click="handleAddGroup(groupInfo.groupId)" class="add-group-button">添加</button>
      </div>
  
      <div class="errorMessage">
        {{this.errorMessage}}
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
    },
    data() {
      return {
        searchById: true,
        groupId: null,
        groupName: '',
        response: null,
        groupInfoByIdShow: false,
        groupInfoByNameShow: false,
        errorMessage: '',
        defaultAvatar: require('../assets/群聊头像.png'),
        groupInfoById: {
            groupId: null,
            groupName:'无',
            avatarUrl: null,
        },
        groupInfoByName: {

        },
        addFriend: {
          toGroupId: 0,
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
      async handleSelectByID() {
        console.log(this.groupId);
        if(this.groupId==null)
        {
          this.errorMessage='请输入群号';
          return;
        }
        try {
          const response = await axios.get('http://localhost:8080/api/group/getGroupInfoByGroupId', {
            params: {
                groupId: this.groupId
            },
            headers: {
              'token': this.userInfo_from_store.token
            }
          });
          this.response = response.data;
          console.log(this.response.data);
        } catch (error) {
          console.error('查找失败', error);
          this.errorMessage='查找失败'
        }
        if (this.response.code === 1) {
          this.groupInfoByIdShow = true;
          this.groupInfoById = this.response.data;
          if(this.groupInfoById==null)
          {
            this.groupInfoByIdShow = false;
            this.errorMessage="没有查找到群";
            setTimeout(() => {
                this.errorMessage = ''; // 5秒后清空错误信息
            }, 1000);
          }
        } else {
          this.groupInfoByIdShow = false;
          console.log(this.response.code);
        }
      },
      async handleSelectByName() {
        this.groupInfoByName=[];//在查询时将其置空
        console.log(this.groupName);
        if(this.groupName.trim()=='')
        {
          this.errorMessage='请输入群名称';
          return;
        }
        try {
          const response = await axios.get('http://localhost:8080/api/group/search', {
            params: {
                groupName: this.groupName
            },
            headers: {
              'token': this.userInfo_from_store.token
            }
          });
          this.response = response.data;
          console.log(this.response.data);
        } catch (error) {
          console.error('查找失败', error);
          this.errorMessage='查找失败'
        }
        if (this.response.code === 1) {
          this.groupInfoByNameShow = true;
          this.groupInfoByName = this.response.data;
          console.log("ByName获取到的群信息：");
          console.log(this.groupInfoByName);
          if(this.groupInfoByName.length==0)
          {
            this.errorMessage="没有查找到群";
            setTimeout(() => {
                this.errorMessage = ''; // 5秒后清空错误信息
            }, 1000);
          }
        } else {
          this.groupInfoByNameShow = false;
          console.log(this.response.code);
        }
      },
      handleAddGroup(groupId) {
        // this.addFriend.toGroupId = this.groupInfoById.groupId;
  
        // const now = new Date();
        // const year = now.getFullYear();
        // const month = (now.getMonth() + 1).toString().padStart(2, '0');
        // const day = now.getDate().toString().padStart(2, '0');
        // const hours = now.getHours().toString().padStart(2, '0');
        // const minutes = now.getMinutes().toString().padStart(2, '0');
        // const seconds = now.getSeconds().toString().padStart(2, '0');
        // this.addFriend.time = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  
        axios({
          method: "post",
          url: "http://localhost:8080/api/group/joinGroup",
          headers: {
            'Content-Type': 'application/json',
            'token': this.userInfo_from_store.token
          },
          params:{
            groupId: groupId,
          } 
        })
        .then(res => {
          console.log("收到响应" + res);
          if (res.data.code === 1) {
            alert('发送加群请求成功！');
          } else {
            alert('发送加群请求失败：' + res.data.msg);
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
    box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);/* 水平偏移 垂直偏移 模糊程度 （尺寸） 颜色 （内部还是外部） */
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
    width: 30%;
  }
  
  .search-button {
    padding: 5px 10px;
    background-color: #0db58b;
    color: #ffffff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .alter-button {
    padding: 5px 10px;
    background-color: #0db58b;
    color: #ffffff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    width: 100px;
  }
  
  .group-card {
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
  
  .group-info {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    width: 140px;
  }
  
  .group-info p {
    margin: 5px 0;
  }
  
  .add-group-button {
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
    margin:8px;
    background-color: #00ff11;
  }
  
  .not-online {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    margin:8px;
    background-color: #444444;
  }
  
  </style>
  