import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router.ts'//路由，负责页面导航
import store from './store.js'

createApp(App)
.use(store)
.use(router)
.mount('#app')
