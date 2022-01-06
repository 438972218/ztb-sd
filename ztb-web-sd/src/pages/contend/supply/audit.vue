<template>
<div>
      <a-row :gutter="16">
      <a-col :sm="24" :md="14">
        <a-table :columns="columns" :data-source="data">
        </a-table>
      </a-col >
        <a-col :sm="24" :md="10" >
             <a-form layout="horizontal" class="page-form">
        <a-row>
          <a-col>
            <a-form-item
              class="form-item-style"
              label="审批结果"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select placeholder="请选择" >
                <a-select-option value="1">张三</a-select-option>
                <a-select-option value="2">李四</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col>
            <a-form-item
              class="form-item-style"
              label="转交人"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col>
            <a-form-item
              class="form-item-style"
              label="备注"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-textarea
                :auto-size="{ minRows: 8, maxRows: 12 }"
            /></a-form-item>
          </a-col>
        </a-row>
        </a-form>
      </a-col>
  </a-row>

        <a-row>
            <a-col :span="24" class="btn-sty" style="margin-top: 10px">
              <a-button type="primary"> 转交 </a-button>
              <a-button type="primary"> 完成审批 </a-button>
              <a-button @click="back"> 返回 </a-button>
            </a-col>
        </a-row>
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
    title: "时间",
    dataIndex: "a1",
    width: 150,
    ellipsis: true
  },
  {
    title: "审批人",
    dataIndex: "a2",
  },
  {
    title: "审批结果",
    dataIndex: "a3",
  },
  {
    title: "审批意见",
    dataIndex: "a4",
  }
];

const data = [
  {
    a1: "2021-4-20 12：12：12",
    a2: "张三",
    a3: "审批通过",
    a4: "同意",
  },
];

export default {
  name: "FindAudit",
  data() {
    return {
      data,
      columns,
    };
  },
  methods: {
    back() {
      this.$router.push({
        path: "/findmanage/find"
      });
    },
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  text-align: center;
  button {
    margin-left: 10px;
  }
}
/deep/.ant-table{ 
  min-height: 220px !important;
 }
</style>
