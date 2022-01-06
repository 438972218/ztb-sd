<template>
  <div>
    <a-table :columns="columns" :data-source="data" :scroll="{ x: 1500 }">
      <span slot="action">
        <a-icon
          type="edit"
          theme="filled"
          class="iconstyle"
          style="padding-right: 10px"
        />
        <a-icon type="delete" title="删除" class="iconstyle" />
      </span>
    </a-table>
  </div>
</template>

<script>
const columns = [
  {
    title: '序号',
    dataIndex: 'a1',
    key: 'a1',
  },
  {
    title: '标的编码',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '标的名称',
    dataIndex: 'a3',
    key: 'a3',
  },
  {
    title: '单位',
    dataIndex: 'a4',
    key: 'a4',
  },
  {
    title: '型号',
    dataIndex: 'a5',
    key: 'a5',
  },
  {
    title: '数量',
    dataIndex: 'a6',
    key: 'a6',
  },
  {
    title: '币种',
    dataIndex: 'a7',
    key: 'a7',
  },
  {
    title: '单价（元）',
    dataIndex: 'a8',
    key: 'a8',
  },
  {
    title: '金额 （元）',
    dataIndex: 'a9',
    key: 'a9',
  },
  {
    title: '计划交货日期',
    dataIndex: 'a10',
    key: 'a10',
  },
  {
    title: '质保期',
    dataIndex: 'a11',
    key: 'a11',
  },
  {
    title: '付款条件',
    dataIndex: 'a12',
    key: 'a12',
  },
  {
    title: '说明',
    dataIndex: 'a13',
    key: 'a13',
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' },
  },
];
const data = [
  {
    a1: '1',
    a2: 'A00001',
    a3: '光纤',
    a4: '公里',
    a5: 'G.652 常规单模光纤',
    a6: '120',
    a7: '人民币',
    a8: '7.00',
    a9: '840.00',
    a10: '2021-09-14',
    a11: '2021-09-14',
    a12: '验收后',
    a13: '',
  },
  {
    a1: '2',
    a2: 'A00002',
    a3: '光纤',
    a4: '公里',
    a5: 'G.655 光纤',
    a6: '120',
    a7: '人民币',
    a8: '7.00',
    a9: '840.00',
    a10: '2021-09-14',
    a11: '2021-09-14',
    a12: '验收后',
    a13: '',
  }
];
export default {
  name: "contractObject",
  data () {
    return {
      data,
      columns,
    }
  },
  methods: {
  }
};
</script>

<style lang="less" scoped>
</style>
