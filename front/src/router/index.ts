import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PostWriteView from '@/views/PostWriteView.vue'
import PostReadView from "@/views/PostReadView.vue";
import PostEditView from "@/views/PostEditView.vue";
import PostDeleteView from "@/views/PostDeleteView.vue";
import ChatReadView from "@/views/ChatReadView.vue";
import ChatExitView from "@/views/ChatExitView.vue";
import ChatRoomCreateView from "@/views/ChatRoomCreateView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/write',
      name: 'write',
      component: PostWriteView
    },
    {
      path: '/read/:postId',
      name: 'read',
      component: PostReadView,
      props: true
    },
    {
      path: '/edit/:postId',
      name: 'edit',
      component: PostEditView,
      props: true
    },
    {
      path: '/delete/:postId',
      name: 'delete',
      component: PostDeleteView,
      props: true
    },
    {
      path: '/post/:postId',
      name: 'chatRoomCreate',
      component: ChatRoomCreateView,
      props: true
    },
    {
      path: '/get/:postId',
      name: 'chat',
      component: ChatReadView,
      props: true
    },
    {
      path: '/delete/:',
      name: 'exitChatRoom',
      component: ChatExitView,
      props: true
    }
  ]
})

export default router
