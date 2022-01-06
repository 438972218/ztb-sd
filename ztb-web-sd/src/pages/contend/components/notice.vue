<template>
  <div>
    <a-tabs v-model="activeKey" @change="tabChange">
      <a-tab-pane key="1" tab="通知">
        <discuss
          ref="discuss"
          :project="form"
          :getUserInfo="getUserInfo"
        ></discuss>
      </a-tab-pane>
      <a-tab-pane key="2" tab="邮件">
        <historyemail ref="historyemail" :project="form"></historyemail>
      </a-tab-pane>
      <a-tab-pane key="3" tab="广播">
        <bulletin ref="bulletin" :project="form"></bulletin>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
import discuss from './discuss'
import historyemail from './historyemail'
import bulletin from './bulletin'
const columns = [
  {
    title: '发送环节',
    dataIndex: 'point',
    key: 'point',
  },
  {
    title: '邮件主题',
    dataIndex: 'mailSubject',
    key: 'mailSubject',
  },
  {
    title: '发送人',
    dataIndex: 'from',
    key: 'fromName'
  },
  {
    title: '发送人邮箱',
    dataIndex: 'from',
    key: 'fromMail'
  },
  {
    title: '接收人',
    dataIndex: 'to',
    key: 'toName'
  },
  {
    title: '接收人邮箱',
    dataIndex: 'to',
    key: 'toMail'
  }
];
export default {
  name: "notice",
  props: {
    getUserInfo: {
      type: Boolean,
      default: false
    },
    form: {
      type: Object,
      default: {}
    }
  },
  components: { discuss, historyemail, bulletin },
  data () {
    return {
      activeKey: '1',
      columns,
      data: [],
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
      } else if (val === '3') {
        this.$nextTick(() => {
          this.$refs.bulletin.getDataList()
        })
      }
    },
  }
};
</script>

<style lang="less" scoped>
</style>
