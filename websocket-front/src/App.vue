<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import Vue from 'vue'

export default {
  name: 'App',
  created () {
    this.localSocket()
  },
  methods: {
    localSocket () {
      let that = this
      if ('WebSocket' in window) {
        that.createWebSocket()
        that.$wsUtil.setWs(that.ws)
      } else {
        console.error('Your browser does not support websocket!')
      }
    },
    createWebSocket () {
      if (localStorage.getItem('websocketId')) {
        Vue.prototype.sid = localStorage.getItem('websocketId')
      } else {
        Vue.prototype.sid = Math.round(Math.random() * 200)
        localStorage.setItem('websocketId', Vue.prototype.sid)
      }
      const wsUrl = `ws://192.168.5.98:8081/demo/websocket/` + Vue.prototype.sid
      const that = this
      try {
        that.ws = new WebSocket(wsUrl)
        // 初始化链接
        that.init()
      } catch (e) {
        console.log('catch' + e)
        that.reconnect(wsUrl)
      }
    },
    init () {
      const that = this
      that.ws.onclose = function () {
        console.log(that.getNowTime() + ' Socket已关闭')
        that.reconnect()
      }
      that.ws.onerror = function () {
        console.log(that.getNowTime() + ' 发生异常了')
        that.reconnect()
      }
      that.ws.onopen = function () {
        console.log(that.getNowTime() + ' Socket 已打开')
      }
    },
    getNowTime () {
      return new Date().getHours() + ':' + new Date().getMinutes() + ':' + new Date().getSeconds()
    },
    reconnect () {
      const that = this
      if (that.lockReconnect) {
        return
      }
      that.lockReconnect = true
      let tt = setTimeout(function () {
        that.createWebSocket()
        that.lockReconnect = false
      }, 10000)
      // 没连接上会一直重连，设置延迟避免请求过多
      tt && clearTimeout(tt)
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
