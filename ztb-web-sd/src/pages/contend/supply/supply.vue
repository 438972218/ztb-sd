<template>
  <div>
    <div class="operator">
      <a-button @click="addNew" style="margin-bottom: 10px" type="primary"
        >添加</a-button
      >
    </div>
    <a-table :columns="columns" :data-source="data">
      <a-button slot="operation" type="danger"> 删除 </a-button>
    </a-table>
    <a-modal v-model="visible" width="500px" :centered="true" title="添加">
      <a-form layout="horizontal" class="page-form">
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row>
          <a-form-item
            label="供应商代码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input />
          </a-form-item>
        </a-row>
        <a-row>
          <a-form-item
            label="供应商名称"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input />
          </a-form-item>
        </a-row>
        <a-row>
          <a-form-item
            label="供应商描述"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input />
          </a-form-item>
          <a-form-item
            label="供应商状态"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-radio-group name="radioGroup" :default-value="1">
              <a-radio :value="1"> 是 </a-radio>
              <a-radio :value="2"> 否 </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item
            label="联系人"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input />
          </a-form-item>
               <a-form-item
            label="联系电话"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input />
          </a-form-item>
        </a-row>
      </a-form>
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
    title: "供应商代码",
    dataIndex: "a1",
  },
  {
    title: "供应商名称",
    dataIndex: "a2",
  },
  {
    title: "供应商描述",
    dataIndex: "a3",
  },
  {
    title: "供应商状态",
    dataIndex: "a4",
  },
  {
    title: "联系人",
    dataIndex: "a5",
  },
  {
    title: "联系电话",
    dataIndex: "a6",
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
    a1: "YS00001",
    a2: "供应商名称",
    a3: "描述",
    a4: "审批通过",
    a5: "张三",
    a6: "13010111223",
  },
];
export default {
  name: "FindItems",
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
