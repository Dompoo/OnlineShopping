import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PostWriteView from '@/views/PostWriteView.vue'
import PostReadView from "@/views/PostReadView.vue";
import PostEditView from "@/views/PostEditView.vue";
import PostDeleteView from "@/views/PostDeleteView.vue";

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
    }
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue')
    // }
  ]
})

export default router
