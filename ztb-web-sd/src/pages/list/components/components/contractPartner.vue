<template>
  <div>
    <a-table :columns="columns" :data-source="data" :scroll="{ x: 1500 }">
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
    title: '合作伙伴类型名称',
    dataIndex: 'a2',
    width: 150,
    key: 'a2',
  },
  {
    title: '合作伙伴类型编码',
    dataIndex: 'a3',
    width: 150,
    key: 'a3',
  },
  {
    title: '编码',
    dataIndex: 'a4',
    key: 'a4',
  },
  {
    title: '全称',
    dataIndex: 'a5',
    key: 'a5',
  },
  {
    title: '地址',
    dataIndex: 'a6',
    key: 'a6',
  },
  {
    title: '账号',
    dataIndex: 'a7',
    key: 'a7',
  },
  {
    title: '开户行',
    dataIndex: 'a8',
    key: 'a8',
  },
  {
    title: '委托代理人',
    dataIndex: 'a9',
    key: 'a9',
  },
  {
    title: '联系电话',
    dataIndex: 'a10',
    key: 'a10',
  },
  {
    title: '邮编',
    dataIndex: 'a11',
    key: 'a11',
  },
  {
    title: '邮箱',
    dataIndex: 'a12',
    key: 'a12',
  }
];
const data = [
  {
    a1: '1',
    a2: '需方',
    a3: '',
    a4: '',
    a5: '先导智能',
    a6: '',
    a7: '',
    a8: '',
    a9: '',
    a10: '',
    a11: '',
    a12: ''
  },
  {
    a1: '2',
    a2: '供方',
    a3: '',
    a4: '',
    a5: 'XDC+',
    a6: '',
    a7: '',
    a8: '',
    a9: '',
    a10: '',
    a11: '',
    a12: ''
  }
];
export default {
  name: "contractPartner",
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
