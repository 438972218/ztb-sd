<template>
  <div>
    <div class="operator">
      <!-- <a-button @click="addNew" style="margin-bottom: 10px" type="primary"
            >添加</a-button
          > -->
    </div>
    <a-table :columns="columns" :data-source="data">
    </a-table>
  </div>
</template>
<script>
const columns = [
  {
    title: "序号",
    key: "items",
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
export default {
  name: "material",
  data() {
    return {
      visible: false,
      data,
      columns,
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
