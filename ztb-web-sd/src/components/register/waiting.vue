<template>
    <a-card class="result-error" :bordered="false">
       <a-row>尊敬的用户，您的注册请求已提交，请耐心等待审核，并注意查收业务负责人邮件</a-row>
        <a-row>感谢你使用******平台</a-row>
            <div style="text-align: center; margin-top: 20px">
        <a-button @click="nextClick" type="primary"> 返回登陆 </a-button>
    </div>
    </a-card>
</template>

<script>
export default {
  name: 'waiting',      
  data () {
    return {
    }
  },
       methods: {
            nextClick() {
        this.$emit("on-waiting-next");
    },
  },
}
</script>

<style scoped lang="less">
.result-error{
  .error-icon{
    color: @red-6;
    margin-right: 8px
  }
}
</style>
