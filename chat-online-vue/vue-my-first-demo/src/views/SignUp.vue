<template>
  <div class="signup-container">
    <div class="signup-form-container">
      <h2>注册</h2>
      <form @submit.prevent="registerUser">
        <div class="input-container">
          <label for="name">姓名:</label>
          <input type="text" id="name" v-model="formData.name">
        </div>
        <div class="input-container">
          <label for="username">用户名:</label>
          <input type="text" id="username" v-model="formData.username">
        </div>
        <div class="input-container">
          <label for="password">密码:</label>
          <input type="text" id="password" v-model="formData.password">
        </div>
        <div class="input-container">
          <label for="email">邮箱:</label>
          <input type="email" id="email" v-model="formData.email">
        </div>
        <div class="verify-container">
          <div class="email-input-container">
            <label for="verifyCode">邮箱验证码:</label>
            <input type="text" id="verifyCode" v-model="formData.verifyCode">
          </div>
          <button class="send-verify-button" type="button" @click="sendEmailVerification" v-show="isSend">发送验证码</button>
          <button class="send-verify-button" type="button" v-show="!isSend">{{ count }} s后重发</button>

          <!-- <el-button @click="getCountDown" :disabled="totalTime <60">{{ content }}</el-button> -->
        </div>
        <div class="errorMessage">{{ errorMessage }}</div>
        <button class="signup-button" type="submit">注册</button>
      </form>
      <div class="link-container">
        <p>已有账号？快来<a href="/login" class="link-color">登录</a>吧！</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'SignUp',
  created() {
    // 获取当前页面的IP地址
    var currentUrl = window.location.href;
    this.hostname = new URL(currentUrl).hostname;
    console.log("https://" + this.hostname + ":8080/");
  },
  data() {
    return {
      formData: {
        name: '',
        username: '',
        password: '',
        email: '',
        verifyCode: '',
      },
      errorMessage: '',
      count: 0,
      timer: null,
      isSend: true,
    };
  },
  methods: {
    sendEmailVerification() {
      if (this.formData.email.trim() == '') {
        this.errorMessage = "请填写邮箱";
        return;
      }
      axios.post('https://" + this.hostname + ":8080/api/user/sendVerifyCode', {
        email: this.formData.email
      })
        .then(response => {
          this.errorMessage = '验证码已发送到您的邮箱';
          const TIME_COUNT = 60;
          if (!this.timer) {
            this.count = TIME_COUNT;
            this.isSend = false;
            this.timer = setInterval(() => {
              if (this.count > 0 && this.count <= TIME_COUNT) {
                this.count--;
              } else {
                this.isSend = true;
                clearInterval(this.timer);
                this.timer = null;
              }
            }, 1000)
          }

        })
        .catch(error => {
          console.error('发送验证码失败', error);
          if (error.response) {
            this.errorMessage = `发送验证码失败，状态码：${error.response.status}`;
          } else {
            this.errorMessage = '发送验证码失败，请重试。';
          }
        });
    },
    registerUser() {
      if (this.formData.name.trim() == '' || this.formData.username.trim() == '' ||
        this.formData.password.trim() == '' || this.formData.email.trim() == '' ||
        this.formData.verifyCode.trim() == '') {
        this.errorMessage = "有空信息"
      }
      else {
        axios.post('https://" + this.hostname + ":8080/api/user/signup', this.formData)
          .then(response => {
            console.log(response.data);
            if (response.data.code == 0) {
              this.errorMessage = response.data.msg;
            }
            else if (response.data.code == 1) {
              this.errorMessage = '注册成功，即将跳转到登录页面';
              setTimeout(() => {
                this.$router.push('/login');
              }, 2000);
            }
          })
          .catch(error => {
            console.error('注册失败');
            if (error.response) {
              this.errorMessage = `注册失败，状态码：${error.response.status}`;
            } else {
              this.errorMessage = '注册失败，请重试。';
            }
          });
      }
    },
  },
};
</script>

<style scoped>
@keyframes floatIn {
  0% {
    opacity: 0.7;
    transform: translateY(80px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.signup-container {
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
  position: fixed;
}

.signup-form-container {
  width: 480px;
  height: 550px;
  margin-top: 12vh;
  background-color: rgb(15, 52, 92);
  border-radius: 5%;
  text-align: center;
  animation: floatIn 0.6s ease;
}

.signup-form-container h2 {
  color: white;
}

.input-container {
  margin-left: 8%;
  margin-right: 8%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 84%;
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

.verify-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.email-input-container {
  margin-left: 8%;
  margin-right: 8%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  width: 84%;
}

.email-input-container label {
  color: rgb(226, 225, 225);
  width: 100%;
  text-align: left;
  font-size: 16px;
}

.email-input-container input {
  border: none;
  color: #5a5a5a;
  width: 100%;
  margin: 8px;
  margin-left: -2px;
  /*权宜之计 不然顶不到最左边 */
  padding: 8px 4px;
  border-radius: 4px;
  font-size: 16px;
}

.email-input-container input:focus {
  outline: 4px solid rgb(65, 77, 200);
}

.send-verify-button {
  border: none;
  background-color: #4756fe;
  width: 50%;
  height: 40px;
  margin-left: 8%;
  margin-right: 8%;
  margin-top: 4%;
  border-radius: 5px;
  color: white;
  font-size: 16px;
}

.signup-button {
  border: none;
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

.signup-form-container button:hover {
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