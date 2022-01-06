<template>
  <div>
    <a-table :columns="columns" :data-source="data">
      <span slot="a6"><a-switch default-checked /></span>
      <span slot="a7">
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
    title: '附件类型',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '附件名称',
    dataIndex: 'a3',
    key: 'a3',
  },
  {
    title: '上传人',
    dataIndex: 'a4',
    key: 'a4',
  },
  {
    title: '上传时间',
    dataIndex: 'a5',
    key: 'a5',
  },
  {
    title: '是否必传',
    dataIndex: 'a6',
    key: 'a6',
    scopedSlots: { customRender: 'a6' },
  },
  {
    title: '操作',
    dataIndex: 'a7',
    key: 'a7',
    scopedSlots: { customRender: 'a7' },
  }
];
const data = [
  {
    a1: '1',
    a2: '物料检测报告',
    a3: '',
    a4: '',
    a5: '',
    a6: '',
    a7: ''
  },
  {
    a1: '2',
    a2: '质量认证报告',
    a3: '',
    a4: '',
    a5: '',
    a6: '',
    a7: ''
  }
];
export default {
  name: "contractAppendices",
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
