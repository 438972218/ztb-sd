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
    title: '业务字段编码',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '业务字段名称',
    dataIndex: 'a3',
    key: 'a3',
  },
  {
    title: '值',
    dataIndex: 'a4',
    key: 'a4',
  },
  {
    title: '业务字段说明',
    dataIndex: 'a5',
    key: 'a5',
  }
];
const data = [
  {
    a1: '1',
    a2: '',
    a3: '',
    a4: '',
    a5: ''
  },
  {
    a1: '2',
    a2: '',
    a3: '',
    a4: '',
    a5: ''
  }
];
export default {
  name: "contractInformation",
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
