<template>
  <div>
    <a-card class="card-top">
      <div>
        <a-form-model
          :model="formInline"
          :labelCol="{ span: 5 }"
          :wrapperCol="{ span: 18, offset: 0 }"
        >
          <a-row>
            <a-col :span="5">
              <a-form-model-item label="编号">
                <a-input
                  v-model="formInline.oddNumber"
                  placeholder="请输入编号"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="5">
              <a-form-model-item label="状态">
                <a-select
                  v-model="formInline.state"
                  placeholder="请选择状态"
                  style="width: 100%"
                >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option value="待发布">待发布</a-select-option>
                  <a-select-option value="正在运行">正在运行</a-select-option>
                  <a-select-option value="待授权">待授权</a-select-option>
                  <a-select-option value="已归档">已归档</a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="5">
              <a-form-model-item label="创建人">
                <a-select
                  show-search
                  v-model="formInline.createdUser"
                  allowClear
                  placeholder="请选择用户"
                >
                  <a-select-option
                    v-for="item in userList"
                    :key="item.userId"
                    :value="item.userName"
                    >{{ item.userName + '-' + item.name }}</a-select-option
                  >
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="5">
              <a-form-model-item label="关键字">
                <a-input
                  v-model="formInline.keyword"
                  placeholder="请输入关键字"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="4" align="right">
              <a-button type="primary" @click="getDataList">查询</a-button>
            </a-col>
          </a-row>
        </a-form-model>
      </div>
    </a-card>
    <a-card>
      <div>
        <a-row style="padding-bottom: 10px">
          <a-space>
            <a-button v-has="['paid:add']" type="primary" @click="handleAdd"
              >创建</a-button
            >
            <a-button
              v-has="['paid:templateadd']"
              type="primary"
              @click="handleTemplate"
              >从模板创建</a-button
            >
            <a-button
              type="primary"
              :disabled="!selectId || getUserInfo"
              @click="handleCopy"
              v-has="['paid:copyadd']"
              >复制</a-button
            >
          </a-space>
        </a-row>
        <a-row>
          <a-table
            id="antdTable"
            rowKey="id"
            :pagination="pagination"
            :columns="columns"
            :data-source="dataSource"
            @change="tableChange"
            :customRow="customRow"
            :rowClassName="setRowClassName"
          >
            <template slot="projectTitle" slot-scope="text, record">
              <span v-if="record.projectSheetVO.title.length < 6">{{
                record.projectSheetVO.title
              }}</span>
              <a-tooltip v-else>
                <template slot="title">{{
                  record.projectSheetVO.title
                }}</template>
                {{ record.projectSheetVO.title.substring(0, 5) + '...' }}
              </a-tooltip>
            </template>
            <template slot="oddNumber" slot-scope="text, record">
              <a
                v-if="permissions.operation.find((f) => f === 'paid:edit')"
                @click="handleEdit(record)"
                >{{ text }}</a
              >
              <span v-else>{{ text }}</span>
            </template>
            <template slot="monitor" slot-scope="text, record">
              <a
                @click="seeMonitor(record)"
                v-has="['paid:monitor']"
                v-if="record.requestStatusName !== '待发布'"
              >
                <img alt="svg" src="@/assets/img/bidding.svg" width="20px"
              /></a>
            </template>
          </a-table>
        </a-row>
      </div>
    </a-card>
    <!-- 模板列表 -->
    <a-modal
      title="竞价单模板"
      :visible="visible"
      width="700px"
      @ok="templateAdd"
      @cancel="visible = false"
    >
      <a-form-model
        :model="temForm"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18, offset: 0 }"
      >
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="关键字">
              <a-input v-model="temForm.keyword" placeholder="请输入关键字" />
            </a-form-model-item>
          </a-col>
          <a-col :span="4" align="right">
            <a-button type="primary" @click="getSheetTemplate">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
      <a-table
        rowKey="id"
        :rowSelection="{
          type: 'radio',
          columnWidth: 50,
          onChange: selectChange,
        }"
        :columns="templateColumns"
        :data-source="templateData"
      >
      </a-table>
    </a-modal>
  </div>
</template>

<script>
import { dictionaryGetRequest } from "@/services/projectmanagement/project"
import {
  paidSheetGetRequest,
  paidSheetTemplate,
  paidSheetGetshowByRequestId
} from "@/services/source/contend"
import { getSysUserServicePageByCondition } from "@/services/structure/index"
import { mapGetters } from "vuex"
import moment from "moment"
const templateColumns = [
  {
    title: "竞价名称",
    width: 120,
    dataIndex: "title",
    scopedSlots: { customRender: "title" },
  },
  {
    title: "项目名称",
    width: 80,
    dataIndex: "projectSheetVO",
    key: "projectTitle",
    customRender: (text, row, index) => {
      return text ? text.title : ""
    },
  },
  {
    title: "项目代码",
    width: 80,
    dataIndex: "projectSheetVO",
    key: "projectCode",
    customRender: (text, row, index) => {
      return text ? text.code : ""
    },
  },
  {
    title: "总额/多项",
    width: 80,
    dataIndex: "bidStructure",
  },
]
export default {
  name: "contend",
  components: {},
  computed: {
    ...mapGetters("account", ["user", 'permissions']),
    getUserInfo () {
      return this.selectData && this.selectData.sysUserInfoVO && this.selectData.sysUserInfoVO.id !== this.user.id
    },
  },
  data () {
    return {
      templateColumns,
      temForm: {
        keyword: "",
      },
      visible: false,
      formInline: {
        state: "",
        keyword: "",
        oddNumber: "",
        createdUser: undefined,
      },
      templateData: [],
      columns: [
        {
          title: "竞价编号",
          dataIndex: "oddNumber",
          width: 130,
          scopedSlots: { customRender: "oddNumber" },
        },
        {
          title: "竞价名称",
          width: 120,
          dataIndex: "title",
          scopedSlots: { customRender: "title" },
        },
        {
          title: "监控",
          width: 50,
          dataIndex: "monitor",
          scopedSlots: { customRender: "monitor" },
        },
        {
          title: "竞价状态",
          width: 100,
          dataIndex: "requestStatusName",
        },
        {
          title: "项目代码",
          width: 120,
          dataIndex: "projectSheetVO",
          key: "projectCode",
          customRender: (text, row, index) => {
            return text ? text.code : ""
          },
        },
        {
          title: "项目名称",
          width: 120,
          dataIndex: "projectSheetVO",
          key: "projectTitle",
          scopedSlots: { customRender: "projectTitle" },
        },
        {
          title: "总额/多项",
          width: 80,
          dataIndex: "bidStructure",
        },
        {
          title: "竞价开始时间",
          width: 120,
          dataIndex: "offerStartTime",
          customRender: (text, row, index) => {
            return text
              ? moment(parseInt(text)).format("YYYY-MM-DD HH:mm:ss")
              : ""
          },
        },
        {
          title: "竞价结束时间",
          width: 120,
          dataIndex: "offerEndTime",
          customRender: (text, row, index) => {
            return text
              ? moment(parseInt(text)).format("YYYY-MM-DD HH:mm:ss")
              : ""
          },
        },
        {
          title: " 创建人",
          width: 100,
          dataIndex: "sysUserInfoVO.name",
        },
        {
          title: "创建时间",
          width: 120,
          dataIndex: "createdTime",
          customRender: (text, row, index) => {
            return text ? moment(parseInt(text)).format("YYYY-MM-DD") : ""
          },
        },
      ],
      selectedRows: null,
      userList: [],
      dataSource: [],
      stateList: [],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
      selectId: '',
      selectData: null
    }
  },
  created () { },
  mounted () {
    this.getSheetTemplate()
    this.getUserList()
    this.getDataList()
  },
  methods: {
    // show竞价单ByRequestId
    async getPaidSheetshowByRequestId (row) {
      const result = await paidSheetGetshowByRequestId(row.requestId)
      if (result.code === 0) {
        sessionStorage.removeItem('addContendkeep')
        let form = Object.assign({}, result.data)
        form.id = ''
        form.offerStartTime = moment(parseInt(form.offerStartTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        form.offerEndTime = moment(parseInt(form.offerEndTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        sessionStorage.setItem("contendForm", JSON.stringify(form))
        sessionStorage.setItem("contendType", "copy")
        this.$router.push({ path: "addcontend" })
      } else {
        this.$message.error(result.message)
      }
    },
    // 复制
    handleCopy () {
      this.getPaidSheetshowByRequestId(this.selectData)
    },
    // 表格选中行事件
    customRow (record) {
      return {
        on: {
          click: () => {
            this.selectId = record.id
            this.selectData = record
          }
        }
      }
    },
    // 设置行样式
    setRowClassName (record) {
      return record.id === this.selectId ? 'clickRowStyl' : ''
    },
    // 模板创建
    templateAdd () {
      if (this.selectedRows) {
        sessionStorage.removeItem('addContendkeep')
        this.visible = false
        let form = Object.assign({}, this.selectedRows)
        form.id = ''
        form.offerStartTime = moment(parseInt(form.offerStartTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        form.offerEndTime = moment(parseInt(form.offerEndTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        sessionStorage.setItem("contendForm", JSON.stringify(form))
        sessionStorage.setItem("contendType", "add")
        this.$router.push({ path: "addcontend" })
      } else {
        this.$message.warning("请选择模板")
      }
    },
    // 选中事件
    selectChange (selectedRowKeys, selectedRows) {
      this.selectedRows = selectedRows.length > 0 ? selectedRows[0] : null
    },
    // 从模板创建
    handleTemplate () {
      this.visible = true
    },
    // 获取模板数据
    async getSheetTemplate () {
      const data = {
        keyword: this.temForm.keyword,
        currentPage: -1,
      }
      const result = await paidSheetTemplate(data)
      if (result.code === 0) {
        this.templateData = result.data.records
      } else {
        this.$message.error(result.message)
      }
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
    },
    // 查询竞价单列表数据
    async getDataList () {
      const data = {
        oddNumber: this.formInline.oddNumber,
        createdUser: this.formInline.createdUser,
        requestStatusName: this.formInline.state,
        keyword: this.formInline.keyword,
        userId: this.user.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
      }
      const result = await paidSheetGetRequest(data)
      if (result.code === 0) {
        this.dataSource = result.data ? result.data.records : []
        this.pagination.total = result.data ? result.data.total : 0
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取用户下拉
    async getUserList () {
      const data = {
        currentPage: -1,
      }
      const result = await getSysUserServicePageByCondition(data)
      if (result.code === 0) {
        this.userList = result.data.records
      }
    },
    // 查询状态
    async getDictionaryRequestAll () {
      const data = {
        currentPage: -1,
        dictionaryClass: "projectStatus",
      }
      const result = await dictionaryGetRequest(data)
      if (result.code === 0) {
        this.stateList = result.data.records
        this.formInline.state =
          this.stateList.length > 0 ? this.stateList[0].meaning : ""
      }
    },
    // 创建
    handleAdd () {
      sessionStorage.removeItem('addContendkeep')
      const form = {
        id: '',
        projectSheetId: undefined, // 项目Id
        delayInterval: undefined, // 延时间隔
        triggerMinimumBidRange: "", // 到达触发价格后最小出价配置
        triggerPrice: "", // 触发价格
        minimumBidRange: "", // 最小出价幅度
        minimumBidRule: undefined, // 最小出价配置
        startPrice: "", // 起始价格
        startPriceRule: '', // 起始价格规则
        vendorMonitorRules: undefined, // 供应商竞价监控器
        bidStructure: undefined, // 出价结构
        paidDirection: undefined, // 竞价方向
        whetherTest: undefined, // 是否测试竞价
        currency: undefined, // 币种
        budget: "", // 预算
        offerEndTime: "", // 竞价结束时间
        offerStartTime: "", // 竞价开始时间
        title: "", // 竞价项目名称
      }
      sessionStorage.setItem("contendForm", JSON.stringify(form))
      sessionStorage.setItem("contendType", "add")
      this.$router.push({ path: "addcontend" })
    },
    // 编辑
    handleEdit (row) {
      sessionStorage.removeItem('addContendkeep')
      let form = Object.assign({}, row)
      form.offerStartTime = moment(parseInt(form.offerStartTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      )
      form.offerEndTime = moment(parseInt(form.offerEndTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      )
      sessionStorage.setItem("contendForm", JSON.stringify(form))
      sessionStorage.setItem("contendType", "edit")
      this.$router.push({ path: "addcontend" })
    },
    // 查看监控
    seeMonitor (row) {
      const data = Object.assign({}, row)
      data.offerStartTime = moment(parseInt(data.offerStartTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      )
      data.offerEndTime = moment(parseInt(data.offerEndTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      )
      sessionStorage.setItem("contendbidding", JSON.stringify(data))
      this.$router.push({ path: "biddingmonitor" })
    },
  },
}
</script>

<style lang="less" scoped>
/deep/ .ant-table-body {
  //样式穿透
  overflow-x: auto !important;
}
#antdTable {
  /deep/ .clickRowStyl {
    background: #f1f6fd;
  }
}
</style>
