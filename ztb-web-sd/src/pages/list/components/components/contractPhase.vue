<template>
  <div>
    <a-table :columns="columns" :data-source="data"> </a-table>
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
    title: '阶段编码',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '业务字段名称',
    dataIndex: 'a3',
    key: 'a3',
  },
  {
    title: '阶段名称',
    dataIndex: 'a4',
    key: 'a4',
  },
  {
    title: '日期',
    dataIndex: 'a5',
    key: 'a5',
  },
  {
    title: '负责人',
    dataIndex: 'a6',
    key: 'a6',
  },
  {
    title: '币种',
    dataIndex: 'a7',
    key: 'a7',
  },
  {
    title: '金额',
    dataIndex: 'a8',
    key: 'a8',
  },
  {
    title: '执行说明',
    dataIndex: 'a9',
    key: 'a9',
  }
];
const data = [
  {
    a1: '1',
    a2: '',
    a3: '',
    a4: '',
    a5: '',
    a6: '',
    a7: '',
    a8: '',
    a9: '',
  },
  {
    a1: '1',
    a2: '',
    a3: '',
    a4: '',
    a5: '',
    a6: '',
    a7: '',
    a8: '',
    a9: '',
  }
];
export default {
  name: "contractPhase",
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
