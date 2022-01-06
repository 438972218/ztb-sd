<template>
  <div>
    <a-card class="card-top">
      <a-form-model
        :model="formInline"
        layout="horizontal"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18, offset: 0 }"
      >
        <a-row>
          <a-col :span="6">
            <a-form-model-item label="类型名称：">
              <a-input
                v-model="formInline.typeName"
                placeholder="请输入类型名称"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="18" align="right">
            <a-button type="primary" @click="getDataList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card>
      <div class="operator">
        <a-button v-has="['form_type:add']" @click="handleAdd" type="primary"
          >新增</a-button
        >
      </div>
      <a-table
        :columns="columns"
        rowKey="id"
        :data-source="dataList"
        :pagination="pagination"
        @change="tableChange"
      >
        <template slot="requestType" slot-scope="text, record">
          <a v-has="['form_type:edit']" @click="handleEdit(record)">{{
            text
          }}</a>
        </template>
        <template slot="operation" slot-scope="record">
          <a-icon
            v-has="['form_type:delete']"
            type="delete"
            title="删除"
            class="iconstyle"
            @click="handleDelete(record.id)"
          />
        </template>
      </a-table>
    </a-card>

    <a-modal
      :title="title"
      :visible="visible"
      width="700px"
      @ok="handleSave"
      @cancel="handleColse"
    >
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :model="form"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="类型名称：">
              <a-input
                v-model="form.requestType"
                placeholder="请输入类型名称"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import {
  getRequestType,
  postRequestType,
  putRequestType,
  deleteRequestType,
} from "@/services/process/workflow"
import moment from "moment"
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "类型名称",
    align: "center",
    dataIndex: "requestType",
    scopedSlots: { customRender: "requestType" },
  },
  {
    title: "描述",
    align: "center",
    dataIndex: "description",
  },
  {
    title: "创建时间",
    align: "center",
    dataIndex: "createdTime",
    customRender: (text, row, index) => {
      return moment(parseInt(text)).format("YYYY-MM-DD")
    },
  },
  {
    title: "操作",
    align: "center",
    scopedSlots: { customRender: "operation" },
  },
]

export default {
  name: "formtypes",
  components: {},
  data() {
    return {
      title: "新增表单类型",
      visible: false,
      formInline: {
        typeName: "",
      },
      form: {
        requestType: "",
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
      dataList: [],
      columns,
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    // 获取表单类型
    async getDataList() {
      const data = {
        typeName: this.formInline.typeName,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
      }
      const result = await getRequestType(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 新增
    handleAdd() {
      this.title = "新增表单类型"
      this.form = {
        requestType: "",
      }
      this.visible = true
    },
    // 保存数据
    async handleSave() {
      let result = null
      if (this.form.id) {
        result = await putRequestType(this.form)
      } else {
        result = await postRequestType(this.form)
      }
      if (result.code === 0) {
        this.visible = false
        this.$message.success("保存成功")
        this.getDataList()
      } else {
        this.$message.error(result.message)
      }
    },
    // 编辑
    async handleEdit(row) {
      this.title = "编辑表单类型"
      this.form = Object.assign({}, row)
      this.visible = true
    },
    // 分页改变
    tableChange(row) {
      this.pagination = row
      this.getDataList()
    },
    // 弹窗关闭
    handleColse() {
      this.visible = false
    },
    // 删除表单类型
    async handleDelete(id) {
      const that = this
      this.$confirm({
        title: "提示",
        content: "是否确认删除该数据",
        async onOk() {
          const result = await deleteRequestType(id)
          if (result.code === 0) {
            that.$message.success("删除成功")
            that.getDataList()
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel() {},
      })
    },
  },
}
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>
