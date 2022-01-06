<template>
  <div>
    <a-row :gutter="16">
      <a-col :sm="24" :md="24">
        <a-form-model layout="horizontal" class="page-form">
          <a-row>
            <!-- <a-col :span="12">
            <a-form-model-item
              prop="toUserId"
              class="form-item-style"
              label="审批结果"
              :labelCol="{ span: 4}"
              :wrapperCol="{ span: 19 }"
              
            >
              <a-select placeholder="请选择" v-model="form.toUserId">
                <a-select-option value="1">张三</a-select-option>
                <a-select-option value="2">李四</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col> -->
            <a-col :span="12">
              <a-form-model-item
                prop="toUserId"
                class="form-item-style"
                label="供应商准入流程"
                :labelCol="{ span: 4 }"
                :wrapperCol="{ span: 19 }"
              >
                <a-select
                  v-model="form.toUserId"
                  placeholder="请选择供应商准入流程"
                >
                  <a-select-option value="1">不需现场考察</a-select-option>
                  <a-select-option value="2">不需合格评审</a-select-option>
                  <a-select-option value="3"
                    >不需现场考察和评审</a-select-option
                  >
                </a-select>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col>
              <a-form-model-item
                prop="description"
                class="form-item-style"
                label="审批意见"
                :labelCol="{ span: 2 }"
                :wrapperCol="{ span: 21 }"
              >
                <a-textarea
                  v-model="form.description"
                  :auto-size="{ minRows: 2, maxRows: 3 }"
              /></a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </a-col>
      <!-- <a-col :sm="24" :md="24">
        <a-table :columns="columns" :data-source="data"> </a-table>
      </a-col> -->
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
    ellipsis: true,
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
  },
]

const data = [
  {
    a1: "2021-4-20 12：12：12",
    a2: "张三",
    a3: "审批通过",
    a4: "同意",
  },
]

export default {
  name: "SupplierAudit",
  data() {
    return {
      data,
      columns,
      form: {
        toUserId: "",
        description: "",
      },
    }
  },
  methods: {
    back() {
      this.$router.push({
        path: "/findmanage/find",
      })
    },
  },
}
</script>
<style lang="less" scoped>
.btn-sty {
  text-align: center;
  button {
    margin-left: 10px;
  }
}
/deep/.ant-table {
  min-height: 220px !important;
}
</style>
