<template>
  <div class="message-chat">
    <div>消息：</div>
    <button @click="butClick()">发消息</button>
    <div class="text">
    </div>
  </div>
</template>
<script>
import { getCodes } from "@/services/user.js";
export default {
  name: "Message",
  data() {
    return {
    websock: null,
    };
  },
   created() {
    //   this.initWebSocket();
    },
    destroyed() {
      this.websock.close() //离开路由之后断开websocket连接
    },
    methods: {
        butClick(){
            getCodes().then((response) => {
                debugger
          });
        },
      initWebSocket(){ //初始化weosocket
        const wsuri = "ws://10.20.54.164:10000/vendor-websocket/imServer/"+ localStorage.getItem('userId');
        this.websock = new WebSocket(wsuri);
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onopen = this.websocketonopen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onclose = this.websocketclose;
      },
      websocketonopen(){ //连接建立之后执行send方法发送数据
        let actions = {"message":"12345"};
        this.websocketsend(JSON.stringify(actions));
      },
      websocketonerror(){//连接建立失败重连
        this.initWebSocket();
      },
      websocketonmessage(e){ //数据接收
        const redata = JSON.parse(e.data);
      },
      websocketsend(Data){//数据发送
        this.websock.send(Data);
      },
      websocketclose(e){  //关闭
        console.log('断开连接',e);
      },
    },
};
</script>
<style lang="less" scoped>
.message-chat {
  font-weight: bolder;
  font-size: 16px;
}
</style>