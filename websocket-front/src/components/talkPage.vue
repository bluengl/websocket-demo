<template>
  <div style="height: 100%; width: 100%;">
    <div style="height: 100%; width: 100%;">
      <div style="height: 500px; background: rgb(102 136 165); overflow: scroll;padding: 0 10px;" id="new_message" :key="reload">
        <div v-for="(item, index) in msgArrItem" :key="index"
             :style="{'textAlign': Number(item.sid) === Number(sidWith) ? 'left' : 'right'}">
          <div>
            <div v-if="Number(item.sid) === Number(sidWith)" style="display: inline-block;background-color: #e1eaf1;width: 40px;border-radius: 50%;padding: 12px 0 9px;text-align: center;color: #928a4b;font-weight: bolder;vertical-align: top;margin-top: 12px;">{{Number(item.sid) === Number(sidWith) ? sidWith : sid}}</div>
            <div style="display: inline-block;margin: 14px 10px; position: relative;max-width: 78%;">
              <span v-if="Number(item.sid) !== Number(sidWith)" style="display: block;position: absolute;z-index: 999;right: -10px;top: 8px;border-width: 10px 0 10px 15px;border-style: solid;border-color: transparent transparent transparent #a1de9a;"></span>
              <span v-else style="display: block;position: absolute;z-index: 999;left: -10px;top: 8px;border-width: 10px 15px 10px 0;border-style: solid;border-color: transparent #a1de9a transparent transparent;"></span>
              <span
                style="padding: 8px;background: #a1de9a;border-radius: 4px;display: inline-block;white-space: normal;word-break: break-all; text-align: left;">{{
                  item.text
                }}</span>
            </div>
            <div v-if="Number(item.sid) !== Number(sidWith)" style="display: inline-block;background-color: #e1eaf1;width: 40px;border-radius: 50%;padding: 12px 0 9px;text-align: center;color: #928a4b;font-weight: bolder;vertical-align: top;margin-top: 12px;">{{Number(item.sid) === Number(sidWith) ? sidWith : sid}}</div>
          </div>
        </div>
      </div>
      <div style="height: 4%; background: #95bdcd">
        <div style="height: 100%">
          <el-input placeholder="请输入内容" v-model="input" class="input-with-select" style="height: 100%" :select-when-unmatched="true" @keyup.enter.native="send('')">
            <el-button slot="append" @click="send('')">发送</el-button>
          </el-input>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'talkPage',
  props: {
    sidWith: String,
    msgArrItem: Array,
    scrollBl: Boolean,
    reload: Number
  },
  data() {
    return {
      input: ''
    }
  },
  watch: {
    scrollBl() {
      this.scrollToBottom()
    }
  },
  mounted() {
    this.scrollToBottom()
  },
  methods: {
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$el.querySelector('#new_message')
        container.scrollTop = container.scrollHeight
      })
    },
    send(msg) {
      !msg && (msg = this.input)
      this.$emit('sendMsg', {fromSid: this.sid, toSid: this.sidWith, text: msg})
      this.input = ''
    }
  }
}
</script>

<style scoped>
::-webkit-scrollbar {
  width: 14px;
  height: 14px;
}

::-webkit-scrollbar-track,
::-webkit-scrollbar-thumb {
  border-radius: 999px;
  border: 5px solid transparent;
}

::-webkit-scrollbar-track {
  /*box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2) inset;*/
}

::-webkit-scrollbar-thumb {
  min-height: 20px;
  background-clip: content-box;
  box-shadow: 0 0 0 5px rgba(0, 0, 0, 0.2) inset;
}

::-webkit-scrollbar-corner {
  background: transparent;
}
</style>
