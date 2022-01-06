<template>
  <div>
    <a-card class="card-top">
      <a-collapse v-model="activeKey">
        <a-collapse-panel
          v-for="(item, i) in dataList"
          :key="i"
          :header="item[0].dictionaryChinese"
        >
          <a-row v-for="(data, j) in item" :key="j">
            <a-col :span="3">类别：{{ data.dictionaryChinese }}</a-col>
            <a-col :span="3">类别：{{ data.dictionaryClass }}</a-col>
            <a-col :span="3">含义：{{ data.meaning }}</a-col>
            <a-col :span="3">数值：{{ data.numerical }}</a-col>
          </a-row>
        </a-collapse-panel>
      </a-collapse>
    </a-card>
  </div>
</template>
<script>
import { dictionaryRequestAll } from "@/services/system/user";

export default {
  name: "Dictionary",
  data () {
    return {
      activeKey: "",
      dataList: "",
    };
  },
  created () {
    this.getroleTypeAll();
  },
  methods: {
    // 获取角色类型
    getroleTypeAll () {
      dictionaryRequestAll().then((response) => {
        debugger
        this.dataList = response.data.dataDictionaries;
      });
    },
  },
};
</script>
<style lang="less" scoped>
.iconstyle {
  cursor: pointer;
  font-size: 16px;
  margin: 0 10px;
  color: #1890ff;
}
</style>