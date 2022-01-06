<template>
  <div>
    <a-card class="card-top">
      <div>
        <a-form layout="horizontal">
          <div>
            <a-row>
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="供应商编码"
                  :labelCol="{ span: 5 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-input v-model="form.code" placeholder="请输入供应商编码" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="供应商名称"
                  :labelCol="{ span: 5 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-input v-model="form.name" placeholder="请输入供应商名称" />
                </a-form-item>
              </a-col>
              <!-- <a-col :md="8" :sm="24">
                <a-form-item
                  label="注册从"
                  :labelCol="{ span: 5 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-date-picker
                    style="width: 100%"
                    placeholder="请输入更新日期"
                    v-model="form.startTime"
                  />
                </a-form-item>
              </a-col> -->
              <a-col :md="12" :sm="24" align="right">
                <a-space>
                  <a-button type="primary" @click="getAll()">查询</a-button>
                  <a-button @click="handleReset">重置</a-button>
                  <a-button v-has="['vendor:add']" @click="handleImport"
                    >批量导入供应商</a-button
                  >
                </a-space>
              </a-col>
            </a-row>
          </div>
        </a-form>
      </div>
    </a-card>
    <a-card>
      <div>
        <a-table
          :columns="columns"
          :rowKey="record => record.id"
          :dataSource="dataSource"
          :pagination="paginationVendor"
          @change="tableVendorChange"
        >
          <template slot="code" slot-scope="text, record">
            <a @click="show(record)">{{ text }}</a>
          </template>
          <template slot="createdTime" slot-scope="text">
            <span @click="addNew(4)">
              {{ $moment(Number(text)).format("YYYY-MM-DD") }}
            </span>
          </template>
        </a-table>
      </div>
    </a-card>
    <a-modal
      title="批量导入"
      :visible="visible"
      width="700px"
      @ok="saveBatchImport"
      @cancel="visible = false"
    >
      <a-form-model
        ref="ruleForm"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="附件">
              <a-upload
                :file-list="fileList"
                :remove="handleRemove"
                :before-upload="beforeUpload"
                accept=".xls,.xlsx"
                @change="handleChange"
              >
                <a-button> <a-icon type="upload" /> 请选择文件 </a-button>
              </a-upload>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { getVendor, importPostVendorFile } from "@/services/supplier/index"
const columns = [
  // {
  //   title: "序号",
  //   key: "index",
  //   align: "center",
  //   width: 70,
  //   customRender: (text, record, index) => `${index + 1}`,
  // },
  {
    title: "供应商编码",
    dataIndex: "code",
    scopedSlots: { customRender: "code" },
  },
  {
    title: "供应商名称",
    dataIndex: "name",
  },
  {
    title: "国家",
    dataIndex: "country",
  },
  {
    title: "城市",
    dataIndex: "city",
  },
  {
    title: "邮箱",
    dataIndex: "enterpriseMail",
  },
]
export default {
  name: "Supplier",
  data() {
    return {
      visible: false,
      fileList: [],
      advanced: true,
      columns: columns,
      dataSource: [],
      form: {
        name: "",
        startDate: "",
      },
      paginationVendor: {
        current: 1,
        pageSize: 10,
        total: 10,
        showTotal: total => `共 ${total} 条数据`,
      },
    }
  },
  created() {
    this.getAll()
  },
  methods: {
    handleChange(info) {
      let fileList = [...info.fileList, info.file]
      fileList = fileList.slice(-1)
      this.fileList = fileList
    },
    handleImport() {
      this.visible = true
      this.fileList = []
    },
    // 删除附件
    handleRemove(file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
    },
    beforeUpload(file) {
      this.fileList = [...this.fileList, file]
      return false
    },
    // 导入数据
    async saveBatchImport() {
      if (this.fileList.length === 0) {
        this.$message.warning("请选择文件")
      } else {
        const formData = new FormData()
        formData.append("multipartFile", this.fileList[0])
        const result = await importPostVendorFile(formData)
        if (result.code === 0) {
          this.visible = false
          this.$message.success("导入成功")
        } else {
          this.$message.error(result.message)
        }
      }
    },
    // 重置
    handleReset() {
      this.form.code = ""
      this.form.name = ""
      this.getAll()
    },
    show(row) {
      sessionStorage.setItem("supplierForm", JSON.stringify(row))
      this.$router.push({ path: "/supply/supplierShow" })
    },
    // 请求模板数据
    getAll() {
      getVendor({
        codeKeyword: this.form.code,
        nameKeyword: this.form.name,
        currentPage: this.paginationVendor.current,
        pageSize: this.paginationVendor.pageSize,
        deleted: 0,
      }).then(response => {
        this.dataSource = response.data.records
        this.paginationVendor.total = response.data.total
      })
    },
    // 跳转审批
    approvalSupplier(record, type) {
      this.$router.push({
        path: "/supply/supplierApproca",
      })
      this.$store.state.account.supplierType = type
      this.$store.state.account.supplierData = record
    },
    tableVendorChange(row) {
      this.paginationVendor = row
      this.getAll()
    },
  },
}
</script>

<style lang="less" scoped>
.search {
  margin-bottom: 54px;
}
.fold {
  width: calc(100% - 216px);
  display: inline-block;
}
@media screen and (max-width: 900px) {
  .fold {
    width: 100%;
  }
}
.join-style {
  span {
    cursor: pointer;
    margin: 10px;
  }
}
.iconstyle {
  cursor: pointer;
  font-size: 16px;
  margin: 0 10px;
}
</style>
