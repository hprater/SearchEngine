import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/components/Layout.vue'; // Import the layout component
import Logout from '../views/Logout.vue'
import store from '../store/index'
import CreateLogin from "@/components/CreateLogin.vue";
import CreateRegister from "@/components/CreateRegister.vue";
import CreateHome from "@/components/CreateHome.vue";

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      component: Layout, // Use the layout component as the wrapper
      children: [
        {
          path: '',
          name: 'home',
          component: CreateHome,
          meta: {
            requiresAuth: true
          }
        },
        {
          path: 'login',
          name: 'login',
          component: CreateLogin,
          meta: {
            requiresAuth: false
          }
        },
        {
          path: 'logout',
          name: 'logout',
          component: Logout,
          meta: {
            requiresAuth: false
          }
        },
        {
          path: 'register',
          name: 'register',
          component: CreateRegister,
          meta: {
            requiresAuth: false
          }
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);
  if (requiresAuth && store.state.token === '') {
    next('/login');
  } else {
    next();
  }
});

export default router;
