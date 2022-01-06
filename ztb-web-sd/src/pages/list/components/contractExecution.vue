<template>
  <div>
    <accessAdd :buttonShow="'4'" :isTrue="true" ref="accessAdd"></accessAdd>
  </div>
</template>

<script>
import accessAdd from './accessAdd.vue'
export default {
  name: "contractExecution",
  components: { accessAdd },
  data () {
    return {
      labelCol: { span: 4 },
      wrapperCol: { span: 20 },
    }
  },
  methods: {
    back () {
      this.$router.push('basic')
    }
  }
};
</script>

<style lang="less" scoped>
</style>
