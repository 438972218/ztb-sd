<template>
  <a-card id="app">
    <flow-panel :moduleList="moduleList"></flow-panel>
  </a-card>
</template>
<script>
import FlowPanel from "@/components/ef/panel";
import { processConfigGetRequest } from "@/services/process/workflow";

export default {
  components: {
    FlowPanel,
  },
  data() {
    return {
      moduleList: [],
    };
  },
  activated() {
    this.getAllprocess();
  },
  mounted() {},
  methods: {
    //角色列表
    getAllprocess() {
      processConfigGetRequest({
        processId: this.$route.query.id,
      }).then((response) => {
        this.moduleList = response.data;
      });
    },
  },
};
</script>
<style lang="less" scoped>
@import "../../components/ef/index.css";
</style>