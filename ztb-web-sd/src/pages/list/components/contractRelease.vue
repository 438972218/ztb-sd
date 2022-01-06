<template>
  <div>
    <accessAdd :buttonShow="'2'" :isTrue="true" ref="accessAdd"></accessAdd>
  </div>
</template>

<script>
import accessAdd from './accessAdd.vue'
export default {
  name: "contractRelease",
  components: { accessAdd },
  data () {
    return {
      visible1: false,
      visible: false,
      labelCol: { span: 4 },
      wrapperCol: { span: 20 }
    }
  },
  methods: {
    back () {
      this.$router.push('basic')
    },
  }
};
</script>

<style lang="less" scoped>
</style>
