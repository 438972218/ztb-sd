<template>
  <div>
    <div class="operator">
      <!-- <a-button @click="addNew" style="margin-bottom: 10px" type="primary"
            >添加</a-button
          > -->
    </div>
    <a-table :columns="columns" :data-source="data">
      <a-button slot="operation" type="primary" @click="addNew">
        查看
      </a-button>
    </a-table>
    <a-table :columns="columns1" :data-source="data1"> </a-table>
    <a-modal v-model="visible" width="1500px" :footer="null" :centered="true" title="历史竞价">
      <a-table       :scroll="{ x: 1300 }" :columns="columns2" :data-source="data2"> </a-table>
    </a-modal>
  </div>
</template>
<script>
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "库存组织",
    dataIndex: "a1",
  },
  {
    title: "物品编码",
    dataIndex: "a2",
  },
  {
    title: "物品描述",
    dataIndex: "a3",
  },
  {
    title: "参考品牌",
    dataIndex: "a4",
  },
  {
    title: "物品分类",
    dataIndex: "a5",
  },
  {
    title: "需求日期",
    dataIndex: "a6",
  },
  {
    title: "需求数量",
    dataIndex: "a7",
  },
  {
    title: "计量单位",
    dataIndex: "a8",
  },
  {
    title: "是否含税",
    dataIndex: "a9",
  },
  {
    title: "操作",
    dataIndex: "operation",
    key: "operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
];
const data = [
  {
    a1: "XDC国际事业部",
    a2: "12001001",
    a3: "三层医用吸附膜",
    a4: "",
    a5: "劳保用品",
    a6: "2021-4-12",
    a7: "100",
    a8: "套",
  },
];
const columns1 = [
  {
    title: "序号",
    key: "index",
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "报价(元)",
    align: "center",
    dataIndex: "a1",
  },
  {
    title: "竞价人",
    align: "center",
    dataIndex: "a2",
  },
  {
    title: "竞价时间",
    align: "center",
    dataIndex: "a3",
  },
];
const data1 = [
  {
    a1: "100.00",
    a2: "*******",
    a3: "2021-05-20 12：13：49",
  },
  {
    a1: "200.00",
    a2: "*******",
    a3: "2021-05-20 12：14：49",
  },
  {
    a1: "300.00",
    a2: "*******",
    a3: "2021-05-20 12：15：49",
  },
  {
    a1: "400.00",
    a2: "*******",
    a3: "2021-05-20 12：16：49",
  },
];
const columns2 = [
  {
    title: "序号",
    key: "index",
          width: 80,
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "报价时间",
    align: "center",
        width: 170,
    dataIndex: "a1",
  },
  {
    title: "报价人",
    align: "center",
           width: 120,
    dataIndex: "a2",
  },
  {
    title: "单价(元)",
    align: "center",
        width: 120,
    dataIndex: "a3",
  },
  {
    title: "总价(元)",
    align: "center",
           width: 120,
    dataIndex: "a4",
  },
  {
    title: "报价明细",
    align: "center",
           width: 120,
    dataIndex: "a5",
  },
  {
    title: "报价有效期从",
    align: "center",
        width: 160,
    dataIndex: "a7",
  },
  {
    title: "报价有效期至",
    align: "center",
        width: 160,
    dataIndex: "a8",
  },
  {
    title: "承诺交货期",
    align: "center",
        width: 160,
    dataIndex: "a9",
  },
  {
    title: "供应周期",
    align: "center",
           width: 100,
    dataIndex: "a10",
  },
  {
    title: "报价说明",
    align: "center",
           width: 100,
    dataIndex: "a11",
  },
  {
    title: "最小供货数量",
        width: 130,
    align: "center",
    dataIndex: "a12",
  },
  {
    title: "单位",
    align: "center",
           width: 80,
    dataIndex: "a13",
  },
];
const data2 = [
  {
    a1: "2021-4-12 12:11:39",
    a2: "张三",
    a3: "220.00",
    a4: "44000.00",
    a5: "",
    a7: "2021-4-12",
    a8: "2021-4-12",
    a9: "2021-4-12",
    a10: "10",
    a11: "",
    a12: "10",
    a13: "",
  },
];
export default {
  name: "material",
  data() {
    return {
      visible: false,
      data,
      columns,
      data1,
      columns1,
      data2,
      columns2,
    };
  },
  methods: {
    addNew() {
      this.visible = true;
    },
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  button {
    float: right;
  }
}
</style>
