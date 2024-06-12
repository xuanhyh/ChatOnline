import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router.ts'//路由，负责页面导航
import store from './store.js'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

createApp(App)
.use(store)
.use(router)
.use(ElementPlus)
.mount('#app')
