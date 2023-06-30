<template>
  <div>
    <br/>
    <el-tabs type="border-card" @tab-click="tabClick">
      <el-tab-pane v-for="(item, index) in userArr" :key="index">
        <span slot="label">
          <span style="font-size: 18px">{{ item }}</span>
          <el-badge v-show="loadNumber[mySid + 'A' + item]" class="mark" :value="loadNumber[mySid + 'A' + item]"
                    :max="99"/>
        </span>
        <talkPage :reload="reload" :sidWith="item" :msgArrItem="msgObj[mySid + 'A' + item] || []"
                  @sendMsg="sendMsgHandler($event)" :scrollBl="scrollBl"></talkPage>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>

import talkPage from './talkPage'

export default {
  name: 'webSocketDemo',
  components: {talkPage},
  data () {
    return {
      inputText: '',
      resultArr: [],
      userArr: [],
      chooseSid: '',
      msgObj: {},
      mySid: '',
      loadNumber: {},
      scrollBl: true,
      reload: 0,
    }
  },
  watch: {
    msgObj: {
      deep: true,
      handler (newVal) {
        localStorage.setItem('msgInfoObj', JSON.stringify(newVal))
      }
    },
    userArr: {
      deep: true,
      handler (newVal) {
        this.chooseSid = newVal[0]
      }
    }
  },
  mounted () {
    this.mySid = this.sid
    if (localStorage.getItem('msgInfoObj')) {
      this.msgObj = JSON.parse(localStorage.getItem('msgInfoObj'))
    } else {
      localStorage.setItem('msgInfoObj', JSON.stringify(this.msgObj))
    }
    const that = this
    this.$wsUtil.ws.onopen = () => {
      this.$wsUtil.ws.onmessage = (res) => {
        if (res.data) {
          const data = JSON.parse(res.data)
          if (data.type === 'usermsg') {
            that.resultArr.push({type: 0, label: data.data})
            const msgArr = that.msgObj[that.sid + 'A' + data.data.sid] || []
            msgArr.push({
              sid: data.data.sid,
              text: data.data.message
            })
            this.updateMsgInfoObj(that.sid + 'A' + data.data.sid, msgArr)
            if (Number(that.chooseSid) !== Number(data.data.sid)) {
              if (that.loadNumber[that.sid + 'A' + data.data.sid] !== undefined) {
                that.loadNumber[that.sid + 'A' + data.data.sid]++
              } else {
                that.loadNumber[that.sid + 'A' + data.data.sid] = 1
              }
            } else {
              that.scrollBl = !that.scrollBl
            }
            this.reload = new Date().getMilliseconds()
          } else if (data.type === 'online') {
            that.userArr = JSON.parse(data.data)
          }
        }
      }
    }
  },
  methods: {
    tabClick (data) {
      this.scrollBl = !this.scrollBl
      this.chooseSid = this.userArr[data.index]
      if (localStorage.getItem('msgInfoObj')) {
        this.msgObj = JSON.parse(localStorage.getItem('msgInfoObj'))
      } else {
        localStorage.setItem('msgInfoObj', JSON.stringify(this.msgObj))
      }
      this.loadNumber[this.sid + 'A' + this.chooseSid] = 0
    },
    sendMsgHandler (data) {
      const {fromSid, toSid, text} = data
      this.sendMsg(fromSid, toSid, text)
    },
    sendMsg (fromSid, toSid, text) {
      if (fromSid && toSid && text) {
        const msgArr = this.msgObj[this.sid + 'A' + toSid] || []
        msgArr.push({
          sid: fromSid,
          text: text
        })
        this.updateMsgInfoObj(this.sid + 'A' + toSid, msgArr)
        this.$wsUtil.ws.send(JSON.stringify({sid: toSid, msg: text}))
        this.scrollBl = !this.scrollBl
      }
    },
    updateMsgInfoObj (key, value) {
      this.msgObj[key] = value
      localStorage.setItem('msgInfoObj', JSON.stringify(this.msgObj))
    },
    fnClickUser (sid) {
      console.log(sid)
    }
  }
}
</script>

<style scoped>

</style>
