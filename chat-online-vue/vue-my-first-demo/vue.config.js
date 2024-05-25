const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    open: false, // 指定启动端口
    port: 8081,
  },
  lintOnSave: false
})
