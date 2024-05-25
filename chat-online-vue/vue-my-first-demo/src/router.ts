import { createRouter, createWebHistory } from 'vue-router'

import ChatWindow from './views/ChatWindow.vue'
import ChatGroupWindow from './views/ChatGroupWindow.vue'
import Login from './views/Login.vue'
import SignUp from './views/SignUp.vue'

const router = createRouter({
    history: createWebHistory(),
    routes:[
        {path: '/', redirect: '/login'},
        {path: '/login', name: 'Login', component: Login},
        {path: '/signup', name: 'SignUp', component: SignUp},
        {path: '/chatWithFriend/:userName', name: 'ChatWithFriend', component: ChatWindow, props: true},
        {path: '/chatWithGroup/:userName', name: 'ChatWithGroup', component: ChatGroupWindow, props: true}
    ]
})

export default router;