<template>
  <div>
    <a-card>
      <a-row :gutter="20">
        <a-button type="primary" style="margin-right: 10px"> 保存 </a-button>
        <a-button type="primary" style="margin-right: 10px"> 提交 </a-button>
        <a-button
          type="primary"
          style="margin-right: 10px"
          @click="visible = true"
        >
          合同附件
        </a-button>
        <a-button
          type="primary"
          style="margin-right: 10px"
          @click="visible1 = true"
        >
          流转记录
        </a-button>
        <a-button type="primary" @click="back"> 返回 </a-button>
      </a-row>
    </a-card>
    <accessAdd
      :isTrue="true"
      :buttonType="true"
      :buttonShow="'2'"
      ref="accessAdd"
    ></accessAdd>
    <a-modal
      title="附件列表"
      :visible="visible"
      :footer="null"
      width="60%"
      @cancel="handleCancel"
    >
      <a-table :columns="columns1" :data-source="data1"> </a-table>
    </a-modal>
    <a-modal
      title="流转记录"
      :visible="visible1"
      :footer="null"
      width="60%"
      @cancel="cancel"
    >
      <a-table :columns="columns2" :data-source="data2"> </a-table>
    </a-modal>
  </div>
</template>

<script>
import accessAdd from './accessAdd.vue'
const columns1 = [
  {
    title: '序号',
    dataIndex: 'a1',
    key: 'a1',
  },
  {
    title: '附件名',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '附件类型',
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
];
const data1 = [
  {
    a1: '1',
    a2: '111',
    a3: 'doc',
    a4: '李四',
    a5: '2020-05-01 12:12:12',
  }
];

const columns2 = [
  {
    title: '序号',
    dataIndex: 'a1',
    key: 'a1',
  },
  {
    title: '时间',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '操作人',
    dataIndex: 'a3',
    key: 'a3',
  },
  {
    title: '动作',
    dataIndex: 'a4',
    key: 'a4',
  }
];
const data2 = [
  {
    a1: '1',
    a2: '2021-05-01 11:11:11',
    a3: '采购员1',
    a4: '创建'
  },
  {
    a1: '2',
    a2: '2021-05-02 11:11:11',
    a3: '管理员1',
    a4: '提交'
  },
  {
    a1: '3',
    a2: '2021-05-02 16:11:11',
    a3: '管理员2',
    a4: '审批不通过'
  },
  {
    a1: '4',
    a2: '2021-05-03 11:11:11',
    a3: '采购员1',
    a4: '提交'
  },
];
export default {
  name: "contractApproval",
  components: { accessAdd },
  data () {
    return {
      visible1: false,
      visible: false,
      labelCol: { span: 4 },
      wrapperCol: { span: 20 },
      columns,
      data,
      columns1,
      data1,
      columns2,
      data2,
    }
  },
  methods: {
    back () {
      this.$router.push('basic')
    },
    // 关闭弹窗
    handleCancel () {
      this.visible = false
    },
    // 关闭弹窗
    cancel () {
      this.visible1 = false
    }
  }
};
</script>

<style lang="less" scoped>
</style>
