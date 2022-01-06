<template>
  <div>
    <a-tabs v-model="activeKey">
      <a-tab-pane key="1" tab="通知">
        <discuss :project="project" ref="discuss"></discuss>
      </a-tab-pane>
      <a-tab-pane key="2" tab="邮件">
        <historyemail :project="project" ref="historyemail"></historyemail>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
import discuss from './discuss'
import historyemail from './historyemail'
export default {
  props: {
    project: {
      type: [Object, String],
      default: {}
    }
  },
  name: "discussEmail",
  components: { discuss, historyemail },
  data () {
    return {
      activeKey: '1'
    }
  },
  mounted () {
  },
  methods: {
    // 调用查询方法
    getDataList () {
      this.activeKey = '1'
      this.$nextTick(() => {
        this.$refs.discuss.init()
      })
    },
    tabChange (val) {
      if (val === '1') {
        this.$nextTick(() => {
          this.$refs.discuss.init()
        })
      } else if (val === '2') {
        this.$nextTick(() => {
          this.$refs.historyemail.getDataList()
        })
      }
    },
  }
};
</script>

<style lang="less" scoped>
</style>
