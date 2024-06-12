const { defineConfig } = require('@vue/cli-service')
const fs = require('fs');
module.exports = {
  transpileDependencies: true,
  devServer: {
    open: false,
    port: 8081, // 指定启动端口
    https: {
      key: fs.readFileSync('./src/server.key'),
      cert: fs.readFileSync('./src/server.crt'),
    },
  },
  lintOnSave: false,
}
