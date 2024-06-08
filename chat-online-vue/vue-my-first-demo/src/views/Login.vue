<template>
  <div class="login-container">
    <div class="login-form-container">
      <h2>登录</h2>
      <form @submit.prevent="fetchData">
        <div class="input-container">
          <label for="username">用户名:</label>
          <input type="text" id="username" v-model="formData.username">
        </div>
        <div class="input-container">
          <label for="password">密码:</label>
          <input type="password" id="password" v-model.number="formData.password">
        </div>
        <div class="errorMessage">{{ errorMessage }}</div>
        <button class="login-button" type="submit">登录</button>
      </form>
      <div class="link-container">
        <p>还没有账号？快来<a href="/signup">注册</a>吧！</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios"
import querystring from "querystring"

export default {
  name: 'test',
  mounted() {
    // 获取当前页面的IP地址
    var currentUrl = window.location.href;
    this.hostname = new URL(currentUrl).hostname;
    window.sessionStorage.setItem('hostname', JSON.stringify(this.hostname));
    console.log("http://"+this.hostname+":8080/");
  },
  data() {
    return {
      formData: {
        username: '',
        password: null
      },
      errorMessage: '',
      hostname: null,
    }
  },
  methods: {
    fetchData() {
      axios({
        method: "post",
        url: "http://"+this.hostname+":8080/api/user/login",
        headers: {
          'Content-Type': 'application/json',
        },
        data: this.formData
      })
        .then(response => {
          console.log("Login axios收到数据:");
          console.log(response.data);
          // 存储用户数据
          const userInfo = response.data.data;
          window.sessionStorage.setItem('userInfo', JSON.stringify(userInfo));

          if (response.data.code == 0) {
            this.errorMessage = response.data.msg;
          }
          else if (response.data.code == 1) {
            this.errorMessage = userInfo.userName + '登录成功';
            this.$router.push({
              path: '/chatWithFriend/' + userInfo.userName,
              // query:{id:userInfo.id, userName: userInfo.userName, name:userInfo.name}
            })
          }
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
  }

}
</script>

<style scoped>
@keyframes floatIn {
  0% {
    opacity: 0.7;
    /* 开始时完全透明 */
    transform: translateY(80px);
    /* 初始位置下移120像素 */
  }

  100% {
    opacity: 1;
    /* 结束时完全不透明 */
    transform: translateY(0);
    /* 最终位置，不再下移 */
  }
}

.login-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('../assets/login_register_background.jpg');
  background-size: cover;
  /* 按照容器的大小缩放背景图片 */
  position: fixed;
}

.login-form-container {
  width: 480px;
  height: 360px;
  min-height: 340px;
  margin-top: 25vh;
  /* 上方留出30%的空白区域 */
  margin-bottom: 30vh;
  /* 下方留出30%的空白区域 */
  background-color: rgb(15, 52, 92);
  border-radius: 5%;
  text-align: center;
  animation: floatIn 0.6s ease;
}

.login-form-container h2 {
  color: white;
  /* width: 100%;
  text-align: center; */
}

.input-container {
  margin-left: 8%;
  margin-right: 8%;
  display: flex;
  /* 使用 Flexbox 布局 */
  flex-direction: column;
  /* 垂直排列子元素 */
  justify-content: center;
  /* 子元素水平居中 */
  align-items: center;
  /* 子元素垂直居中 */
  width: 84%;
  /* 设置宽度为父容器宽度的84% */
}

.input-container label {
  color: rgb(226, 225, 225);
  width: 100%;
  text-align: left;
  font-size: 16px;
}

.input-container input {
  border: none;
  color: #5a5a5a;
  width: 100%;
  margin: 8px;
  padding: 8px 4px;
  border-radius: 4px;
  font-size: 16px;
}

.input-container input:focus {
  outline: 4px solid rgb(65, 77, 200);
}

.login-button {
  border: none;
  /* 设置边框为无 */
  background-color: #4756fe;
  width: 84%;
  height: 40px;
  margin-left: 8%;
  margin-right: 8%;
  margin-top: 2%;
  border-radius: 5px;
  color: white;
  font-size: 16px;
}

.login-button:hover {
  background-color: #3a47d4;
}

.link-container {
  margin: 10px;
  color: aliceblue;
}

.link-container a {
  color: skyblue;
}

.errorMessage {
  width: 100%;
  height: 20px;
  color: red;
}
</style>