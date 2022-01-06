<template>
  <div>
    <div v-if="form.vendorMonitorRules === '显示总排名'">
      <a-row style="padding-bottom: 5px">
        <a-col
          :span="24"
          v-if="form.requestStatusName !== '已归档' && !getUserInfo"
        >
          <a-button :disabled="!editingKey" type="primary" @click="toauth">
            授权
          </a-button>
        </a-col>
      </a-row>

      <a-table
        :columns="columns"
        :pagination="pagination"
        :data-source="data"
        :row-selection="rowSelection"
        @change="tableVendorChange"
      >
        <template
          v-for="col in ['confirmedPrice']"
          :slot="col"
          slot-scope="text, record"
        >
          <div :key="col">
            <a-input
              v-if="record.editable"
              style="margin: -5px 0"
              :value="text"
              @change="(e) => handleChange(e.target.value, record.id, col)"
            />
            <template v-else>
              {{ text }}
            </template>
          </div>
        </template>
        <template
          slot="operation"
          slot-scope="text, record"
          v-if="form.requestStatusName !== '已归档' && !getUserInfo"
        >
          <div class="editable-row-operations">
            <a
              v-if="record.vendorStatus == '已中标' && !getUserInfo"
              @click="() => cancelAuth(record)"
              >取消授权
            </a>
          </div>
        </template>
      </a-table>
    </div>
    <div v-else>
      <authitem
        @putAuthorization="putAuthorization"
        :getUserInfo="getUserInfo"
        :form="form"
      ></authitem>
    </div>

    <!-- 授权供应商 -->
    <a-modal
      :title="'授权:' + formInline.vendorName"
      :visible="visible"
      width="700px"
      @ok="auth"
      @cancel="visible = false"
    >
      <a-form-model
        ref="ruleForm"
        :model="formInline"
        :rules="rules"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="确定价格" prop="confirmedPrice">
              <a-input
                v-model="formInline.confirmedPrice"
                placeholder="请输入确定价格"
              ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import {
  paidVendorGetRequest,
  paidVendorPutRequest,
} from "@/services/source/contend"
import authitem from "./authitem.vue"

const columns = [
  {
    title: "供应商",
    dataIndex: "vendorName",
    key: "vendorName",
  },
  {
    title: "总价",
    dataIndex: "totalPrice",
    key: "totalPrice",
  },
  // {
  //   title: "评论",
  //   dataIndex: "vendorMark",
  //   key: "vendorMark",
  // },
  {
    title: "确定价格",
    dataIndex: "confirmedPrice",
    key: "confirmedPrice",
    scopedSlots: { customRender: "confirmedPrice" },
  },
  {
    title: "状态",
    dataIndex: "vendorStatus",
    key: "vendorStatus",
  },
  {
    title: "操作",
    dataIndex: "operation",
    key: "operation",
    scopedSlots: { customRender: "operation" },
  },
]
export default {
  props: {
    form: {
      type: Object,
      default: {}
    },
    getUserInfo: {
      type: Boolean,
      default: false
    }
  },
  components: { authitem },
  name: "authorization",
  data () {
    return {
      visible: false,
      editingKey: false,
      columns,
      formInline: {
        id: "",
        vendorName: "",
        vendorStatus: "已中标",
      },
      rules: {
        confirmedPrice: [
          { required: true, message: "请输入确定价格", trigger: "change" },
        ],
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
      data: [],
      cacheData: [],
    }
  },
  computed: {
    rowSelection () {
      return {
        type: "radio",
        onChange: (selectedRowKeys, selectedRows) => {
          this.formInline.vendorName = selectedRows[0].vendorName
          this.formInline.id = selectedRows[0].id
          this.editingKey = selectedRows[0].checkable
          // this.formInline.confirmedPrice=selectedRows[0].confirmedPrice;
        },
        getCheckboxProps: record => ({
          props: {
            disabled: !record.checkable,
            name: record.name,
          },
        }),
      }
    },
  },
  mounted () {
    // this.form = JSON.parse(sessionStorage.getItem("contendForm"))
    this.getDataList()
  },
  methods: {
    // 授权过后流转竞价单
    putAuthorization () {

    },
    //取消授权
    async cancelAuth (record) {
      const result = await paidVendorPutRequest({ id: record.id })
      if (result.code === 0) {
        this.$message.success("已取消授权")
        this.getDataList()
      } else {
        this.$message.error(result.message)
      }
    },
    //授权
    async auth () {
      const result = await paidVendorPutRequest(this.formInline)
      if (result.code === 0) {
        this.$message.success("授权成功")
        this.visible = false
        this.getDataList()
      } else {
        this.$message.error(result.message)
      }
    },
    toauth () {
      //授权
      if (this.formInline.id == "") {
        this.$message.warning("请选择供应商")
        return
      }
      this.visible = true
    },
    // 查询供应商
    async getDataList () {
      const data = {
        paidSheetId: this.form.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
      }
      const result = await paidVendorGetRequest(data)
      if (result.code === 0) {
        const flag = result.data.records.some(item => {
          return item.vendorStatus == "已中标"
        })
        this.editingKey = !flag
        if (flag) {
          this.data = result.data.records.map(item => ({
            ...item,
            editable: false,
            checkable: false,
          }))
        } else {
          this.data = result.data.records.map(item => ({
            ...item,
            editable: false,
            checkable: true,
          }))
        }

        this.cacheData = [...this.data]
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 供应商分页改变
    tableVendorChange (row) {
      this.pagination = row
      this.getDataList()
    },
    handleChange (value, key, column) {
      const newData = [...this.data]
      const target = newData.filter(item => key === item.id)[0]
      if (target) {
        target[column] = value
        this.data = newData
      }
    },
    edit (record) {
      const key = record.id
      const newData = [...this.data]
      const target = newData.filter(item => key === item.id)[0]
      this.editingKey = key
      if (target) {
        target.editable = true
        this.data = newData
      }
    },
    abandon () {
      //放弃授权
      const newData = [...this.cacheData]
      newData.forEach(item => (item.editable = false))
      this.editingKey = ""
      this.data = newData
    },
    save (key) {
      const newData = [...this.data]
      const newCacheData = [...this.cacheData]
      const target = newData.filter(item => key === item.id)[0]
      const targetCache = newCacheData.filter(item => key === item.id)[0]
      if (target && targetCache) {
        target.editable = false
        this.data = newData
        Object.assign(targetCache, target)
        this.cacheData = newCacheData
      }
      this.editingKey = ""
    },
    cancel (key) {
      const newData = [...this.data]
      const target = newData.filter(item => key === item.id)[0]
      this.editingKey = ""
      if (target) {
        Object.assign(target, this.cacheData.filter(item => key === item.id)[0])
        delete target.editable
        this.data = newData
      }
    },
  },
}
</script>

<style lang="less" scoped></style>
