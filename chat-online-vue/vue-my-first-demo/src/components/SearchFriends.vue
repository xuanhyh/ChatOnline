<template>
  <!-- 无用的组件，之前测试用的 -->
    <div>
      <h2>发送请求示例</h2>
      <p>Query 参数：{{ queryParam }}</p>
      <p>Header 参数：{{ headerParam }}</p>
      <button @click="sendRequest">发送请求</button>
      <div v-if="response">{{ response }}</div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        queryParam: '1', // 设置默认的 Query 参数值
        headerParam: 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTU1OTA3OTYsInVzZXJJZCI6MX0.VvPJw0XqKQ4A9cU44HdpLYo_YMkWIaYyWamDCOgK0sg',
        response: null
      };
    },
    methods: {
      async sendRequest() {
        try {
          const response = await axios.get('http://localhost:8080/api/user/friend', {
            params: {
              id: this.queryParam
            },
            headers: {
              'token': this.headerParam,
              'User-Agent': 'Apifox/1.0.0 (https://apifox.com)'
            }
          });
          this.response = response.data;
        } catch (error) {
          console.error('请求失败', error);
        }
      }
    }
  }
  </script>
  