<template>
  <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
  <!-- <ChatWithFriend msg="Welcome to Your Vue.js App"/> -->
  <!-- <Login msg="hh this is login window"/> -->
  <!-- <SearchFriend/> -->
  <!-- <test/> -->
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 60px; */
}
</style>
