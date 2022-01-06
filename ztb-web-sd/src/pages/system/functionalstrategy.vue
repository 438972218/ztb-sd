<template>
  <div>
    <a-card>
      <div class="operator">
        <a-button @click="handleAdd" v-has="['strategy:add']" type="primary"
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
        <template slot="requestTypeName" slot-scope="text, record">
          <a v-has="['strategy:edit']" @click="handleEdit(record)">{{
            text
          }}</a>
        </template>
        <template slot="operation" slot-scope="record">
          <a-icon
            v-has="['strategy:delete']"
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
        :rules="rules"
        :model="form"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="表单类型：" prop="requestTypeId">
              <a-select
                v-model="form.requestTypeId"
                placeholder="请选择表单类型"
                style="width: 100%"
              >
                <a-select-option
                  v-for="item in requestTypeList"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.requestType }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="流程：" prop="processId">
              <a-select
                v-model="form.processId"
                placeholder="请选择流程"
                style="width: 100%"
              >
                <a-select-option
                  v-for="item in processRequestList"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="策略：">
              <a-input
                disabled
                v-model="form.description"
                placeholder="请选择策略"
                enter-button
              >
                <a-icon
                  slot="addonAfter"
                  type="search"
                  @click="qualifiedSearch"
                />
              </a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <qualified
      ref="qualified"
      :title="'策略'"
      @selectData="selectData"
    ></qualified>
  </div>
</template>
<script>
import {
  getFunctionalStrategy,
  postFunctionalStrategy,
  deleteFunctionalStrategy,
  getRequestType,
  processRequest,
} from "@/services/process/workflow"
import qualified from "./components/qualified.vue"
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "表单类型",
    align: "center",
    dataIndex: "requestTypeName",
    scopedSlots: { customRender: "requestTypeName" },
  },
  {
    title: "流程信息",
    align: "center",
    dataIndex: "processName",
  },
  {
    title: "策略脚本",
    align: "center",
    dataIndex: "description",
  },
  {
    title: "操作",
    align: "center",
    scopedSlots: { customRender: "operation" },
  },
  // {
  //   title: "策略类型",
  //   align: "center",
  //   dataIndex: "type",
  // }
]

export default {
  name: "functionalstrategy",
  components: { qualified },
  data() {
    return {
      title: "新增策略",
      visible: false,
      form: {
        processId: undefined,
        requestTypeId: undefined,
        script: "",
        description: "",
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
      rules: {
        processId: [
          { required: true, message: "请选择流程", trigger: "change" },
        ],
        requestTypeId: [
          { required: true, message: "请选择表单类型", trigger: "change" },
        ],
      },
      dataList: [],
      columns,
      requestTypeList: [],
      processRequestList: [],
    }
  },
  created() {
    this.getDataList()
    this.getRequestTypeData()
    this.getProcessRequest()
  },
  methods: {
    // 配置规则信息
    selectData(value, name) {
      this.form.script = value
      this.form.description = name
    },
    qualifiedSearch() {
      this.$refs.qualified.visible = true
      this.$refs.qualified.getDataList()
      if (!this.form.script) {
        this.$refs.qualified.formInline = {
          field: undefined,
          condition: undefined,
          value: undefined,
          relationship: undefined,
        }
        this.$refs.qualified.contentList = []
      }
    },
    // 获取表单类型
    async getRequestTypeData() {
      const data = {
        currentPage: -1,
      }
      const result = await getRequestType(data)
      if (result.code === 0) {
        this.requestTypeList = result.data.records
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取流程
    async getProcessRequest() {
      const data = {
        currentPage: -1,
      }
      const result = await processRequest(data)
      if (result.code === 0) {
        this.processRequestList = result.data.records
      }
    },
    // 获取策略
    async getDataList() {
      const data = {
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
      }
      const result = await getFunctionalStrategy(data)
      if (result.code === 0) {
        this.dataList = []
        result.data.records.forEach(item => {
          const d = {
            id: item.id,
            requestTypeName: item.requestType
              ? item.requestType.requestType
              : "",
            requestTypeId: item.requestType ? item.requestType.id : "",
            processId: item.process ? item.process.id : "",
            processName: item.process ? item.process.name : "",
            script: item.script,
            description: item.description,
          }
          this.dataList.push(d)
        })
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 新增
    handleAdd() {
      this.title = "新增策略"
      this.form = {
        processId: undefined,
        requestTypeId: undefined,
        script: "",
        description: "",
      }
      this.visible = true
    },
    // 保存数据
    async handleSave() {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          let result = await postFunctionalStrategy(this.form)
          if (result.code === 0) {
            this.visible = false
            this.$message.success("保存成功")
            this.getDataList()
          } else {
            this.$message.error(result.message)
          }
        }
      })
    },
    // 编辑
    async handleEdit(row) {
      this.title = "编辑策略"
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
          const result = await deleteFunctionalStrategy(id)
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
