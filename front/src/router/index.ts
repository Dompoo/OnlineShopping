import { createRouter, createWebHistory } from 'vue-router'
import PostWriteView from '@/views/PostWriteView.vue'
import PostDetailView from '@/views/PostDetailView.vue'
import PostEditView from '@/views/PostEditView.vue'
import PostDeleteView from '@/views/PostDeleteView.vue'
import ChatDetailView from '@/views/ChatDetailView.vue'
import ChatExitView from '@/views/ChatExitView.vue'
import ChatRoomCreateView from '@/views/ChatRoomCreateView.vue'
import ChatSendView from '@/views/ChatSendView.vue'
import ProductListView from '@/views/ProductListView.vue'
import ProductDetailView from '@/views/ProductDetailView.vue'
import ProductEditView from '@/views/ProductEditView.vue'
import ProductDeleteView from '@/views/ProductDeleteView.vue'
import ProductWriteView from '@/views/ProductWriteView.vue'
import PostListView from '@/views/PostListView.vue'
import Login from '@/views/Login.vue'
import Signup from '@/views/Signup.vue'
import type { Store } from 'vuex'
import { createStore } from 'vuex'

interface RootState {
  isLoggedIn: boolean;
}

const store = createStore<RootState>({
  state: {
    isLoggedIn: false,
  },
  mutations: {
    setLoggedIn(state, value: boolean) {
      state.isLoggedIn = value;
    },
  }
});

export function useStore(): Store<RootState> {
  return store as Store<RootState>;
}

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'postList',
      component: PostListView
    },
    {
      path: '/postWrite',
      name: 'postWrite',
      component: PostWriteView
    },
    {
      path: '/postDetail/:postId',
      name: 'postDetail',
      component: PostDetailView,
      props: true
    },
    {
      path: '/postEdit/:postId',
      name: 'postEdit',
      component: PostEditView,
      props: true
    },
    {
      path: '/postDelete/:postId',
      name: 'postDelete',
      component: PostDeleteView,
      props: true
    },
    {
      path: '/chatCreate/:postId',
      name: 'chatRoomCreate',
      component: ChatRoomCreateView,
      props: true
    },
    {
      path: '/chatSend/:roomId',
      name: 'chatSend',
      component: ChatSendView,
      props: true
    },
    {
      path: '/chatDetail/:roomId',
      name: 'chatDetail',
      component: ChatDetailView,
      props: true
    },
    {
      path: '/chatExit/:roomId',
      name: 'chatRoomExit',
      component: ChatExitView,
      props: true
    },
    {
      path: '/productList/:productId',
      name: 'productList',
      component: ProductListView,
    },
    {
      path: '/productDetail/:productId',
      name: 'productDetail',
      component: ProductDetailView,
      props: true
    },
    {
      path: '/productWrite',
      name: 'productWrite',
      component: ProductWriteView,
    },
    {
      path: '/productEdit/:productId',
      name: 'productEdit',
      component: ProductEditView,
      props: true
    },
    {
      path: '/productDelete/:productId',
      name: 'productDelete',
      component: ProductDeleteView,
      props: true
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/signup',
      name: 'signup',
      component: Signup,
    },
  ]
})

export default router
