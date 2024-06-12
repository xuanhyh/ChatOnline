import { createRouter, createWebHistory } from 'vue-router'

import ChatWindow from './views/ChatWindow.vue'
import ChatGroupWindow from './views/ChatGroupWindow.vue'
import Login from './views/Login.vue'
import SignUp from './views/SignUp.vue'
import Management from './views/Management.vue'
import OnlineCall from './views/OnlineCall.vue'

const router = createRouter({
    history: createWebHistory(),
    routes:[
        {path: '/', redirect: '/login'},
        {path: '/login', name: 'Login', component: Login},
        {path: '/signup', name: 'SignUp', component: SignUp},
        {path: '/chatWithFriend/:userName', name: 'ChatWithFriend', component: ChatWindow, props: true},
        {path: '/chatWithGroup/:userName', name: 'ChatWithGroup', component: ChatGroupWindow, props: true},
        {path: '/Management/:userName', name: 'Management', component: Management, props: true},
        {path: '/OnlineCall/:userId/:targetUserId', name: 'OnlineCall', component: OnlineCall, props: true},
    ]
})

export default router;