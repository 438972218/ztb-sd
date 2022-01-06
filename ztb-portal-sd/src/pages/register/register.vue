<template>
  <a-card class="result-error" :bordered="false">
    <div>
      <a-steps :current="current">
        <a-step title="阅读注册须知"> </a-step>
        <a-step title="填写企业信息"> </a-step>
        <a-step title="企业认证信息确定"> </a-step>
        <a-step title="等待审批"> </a-step>
      </a-steps>
      <div class="steps-content">
        <component
          :is="comName"
          :dataList="dataList"
          @on-reading="readingNext"
          @on-write-next="writeNext"
          @on-write-up="writeUp"
          @on-confirm-next="confirmNext"
          @on-confirm-up="confirmUp"
          @on-waiting-next="waitingNext"
        />
      </div>
    </div>
  </a-card>
</template>
<script>
import Reading from "./components/reading"
import Write from "./components/write"
import Confirm from "./components/confirm"
import Waiting from "./components/waiting"
export default {
  name: "register",
  components: { Reading, Write, Confirm, Waiting },
  data() {
    return {
      comName: "Reading",
      current: 0,
      dataList: null,
      steps: [
        {
          title: "First",
          content: "First-content",
        },
        {
          title: "Second",
          content: "Second-content",
        },
        {
          title: "Last",
          content: "Last-content",
        },
        {
          title: "Last",
          content: "Last-content",
        },
      ],
    }
  },
  methods: {
    readingNext() {
      this.comName = "Write"
      this.current = 1
    },
    writeNext(data) {
      this.comName = "Confirm"
      this.dataList = data
      this.current = 2
    },
    writeUp(data) {
      this.comName = "Reading"
      this.dataList = data
      this.current = 0
    },
    confirmNext(data) {
      this.comName = "Waiting"
      this.dataList = data
      this.current = 3
    },
    confirmUp(data) {
      this.comName = "Write"
      this.current = 1
      this.dataList = data
    },
    waitingNext() {
      this.$router.push("/login")
      this.comName = "Reading"
      this.current = 0
    },
    next() {
      this.current++
    },
    prev() {
      this.current--
    },
  },
}
</script>

<style scoped lang="less">
.result-error {
  margin: 20px 50px;
}
.steps-content {
  padding: 30px;
  margin-top: 16px;
  background-color: #fafafa;
  min-height: 200px;
  text-align: center;
}
.steps-action {
  margin-top: 24px;
}
</style>
