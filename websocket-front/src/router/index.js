import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import webSocketDemo from '@/components/webSocketDemo'
import talkPage from '@/components/talkPage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/HelloWorld',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/webSocketDemo',
      name: 'webSocketDemo',
      component: webSocketDemo
    },
    {
      path: '/talkPage',
      name: 'talkPage',
      component: talkPage
    }
  ]
})
