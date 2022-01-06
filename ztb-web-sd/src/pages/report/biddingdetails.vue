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
            <a-col :sm="6">
              <a-form-model-item label="项目">
                <a-select
                  v-model="formInline.projectSheetId"
                  style="width: 100%"
                  allowClear
                  placeholder="请选择项目"
                >
                  <a-select-option
                    v-for="item in projectList"
                    :key="item.id"
                    :value="item.id"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="6">
              <a-form-model-item label="状态">
                <a-select
                  v-model="formInline.state"
                  allowClear
                  placeholder="请选择状态"
                  style="width: 100%"
                >
                  <a-select-option
                    v-for="item in stateList"
                    :key="item.id"
                    :value="item.id"
                  >
                    {{ item.meaning }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="6">
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
            <a-col :span="6">
              <a-form-model-item label="关键字">
                <a-input
                  v-model="formInline.keyword"
                  placeholder="请输入关键字"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="24" align="center">
              <a-space>
                <a-button type="primary" @click="getDataList">查询</a-button>
                <a-button type="primary" @click="handleExport">导出</a-button>
              </a-space>
            </a-col>
          </a-row>
        </a-form-model>
      </div>
    </a-card>
    <a-card>
      <div>
        <a-row>
          <a-table
            id="antdTable"
            rowKey="id"
            :pagination="pagination"
            :columns="columns"
            :data-source="dataSource"
            @change="tableChange"
          >
            <template slot="oddNumber" slot-scope="text, record">
              <a @click="handleBidEdit(record)">{{ text }}</a>
            </template>
          </a-table>
        </a-row>
      </div>
    </a-card>
    <!-- 模板列表 -->
    <!-- <a-modal
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
    </a-modal> -->
  </div>
</template>

<script>
import { dictionaryGetRequest, getProjectSheetUser } from "@/services/projectmanagement/project"
import {
  bidSheetGetStatement,
  exportBidStatement
} from "@/services/source/bidding"
import { getSysUserServicePageByCondition } from "@/services/structure/index"
import { mapGetters } from "vuex"
import moment from "moment"
export default {
  name: "contend",
  components: {},
  computed: {
    ...mapGetters("account", ["user"]),
  },
  data () {
    return {
      projectList: [],
      temForm: {
        keyword: "",
      },
      visible: false,
      formInline: {
        projectSheetId: undefined,
        state: undefined,
        keyword: "",
        oddNumber: "",
        createdUser: undefined,
      },
      templateData: [],
      columns: [
        {
          title: "招标员",
          width: 120,
          dataIndex: "sysUserInfoVO",
          key: "userName",
          customRender: (text, row, index) => {
            return text ? text.name : ""
          }
        },
        {
          title: "招标单号",
          width: 100,
          dataIndex: "oddNumber",
          scopedSlots: { customRender: "oddNumber" },
        },
        {
          title: "主题",
          width: 120,
          dataIndex: "title",
        },
        {
          title: "所属项目",
          width: 120,
          dataIndex: "projectSheetVO",
          key: "projectTitle",
          customRender: (text, row, index) => {
            return text ? text.title : ""
          }
        },
        {
          title: "投标截止时间",
          width: 120,
          dataIndex: "bidEndTime",
          scopedSlots: { customRender: "bidEndTime" },
          customRender: (text, row, index) => {
            return text ? moment(parseInt(text)).format("YYYY-MM-DD HH:mm:ss") : ""
          },
        },
        {
          title: "状态",
          width: 100,
          dataIndex: "requestStatusName",
        },
        {
          title: "已回复数量",
          width: 100,
          dataIndex: "repliesNumber",
        },
        {
          title: "授权供应商",
          width: 100,
          dataIndex: "authorizedVendor",
        },
        // {
        //   title: "创建时间",
        //   width: 120,
        //   dataIndex: "createdTime",
        //   customRender: (text, row, index) => {
        //     return text ? moment(parseInt(text)).format("YYYY-MM-DD") : ""
        //   },
        // },
      ],
      selectedRows: null,
      userList: [],
      dataSource: [],
      stateList: [{
        id: '1',
        meaning: '待提交'
      }, {
        id: '2',
        meaning: '已归档'
      }, {
        id: '1430091118399266818',
        meaning: '正在运行'
      }, {
        id: '1430091201278713858',
        meaning: '待评估'
      }, {
        id: '1430091285168988162',
        meaning: '待资质评估'
      }, {
        id: '1430091325149093889',
        meaning: '待技术评估'
      }, {
        id: '1430091395760201729',
        meaning: '待商业评估'
      }, {
        id: '1430091543366148097',
        meaning: '待授权'
      }, {
        id: '1430091754389970945',
        meaning: '待发布'
      }],
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
    this.getProjectSheet()
    this.getUserList()
    this.getDataList()
  },
  methods: {
    // 跳转招标详情
    handleBidEdit (row) {
      sessionStorage.removeItem('addBiddingkeep')
      let form = Object.assign({}, row)
      form.bidEndTime = moment(parseInt(form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
      form.qualificationTender = form.qualificationTender === 1
      form.technicalTender = form.technicalTender === 1
      form.items = form.items ? form.items.split(',') : []
      sessionStorage.setItem('biddingForm', JSON.stringify(form))
      this.$router.push({ name: '招标详情' })
    },
    // 导出
    async handleExport () {
      const data = {
        projectSheetId: this.formInline.projectSheetId,
        createdUser: this.formInline.createdUser,
        statusId: this.formInline.state,
        keyword: this.formInline.keyword,
        userId: this.user.id,
        currentPage: -1
      }
      const result = await exportBidStatement(data)
      const link = document.createElement('a')
      let blob = new Blob([result], { type: 'application/vnd.ms-excel' })
      link.style.display = 'none'
      link.href = URL.createObjectURL(blob)
      // link.download = res.headers['content-disposition'] //下载后文件名
      link.download = '原始数据' //下载的文件名
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
    },
    // 获取项目
    async getProjectSheet () {
      const data = {
        type: 'official',
        userId: this.user.id,
        status: '运行中',
        currentPage: -1
      }
      const result = await getProjectSheetUser(data)
      if (result.code === 0) {
        this.projectList = result.data.records
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询竞价单列表数据
    async getDataList () {
      const data = {
        projectSheetId: this.formInline.projectSheetId,
        createdUser: this.formInline.createdUser,
        statusId: this.formInline.state,
        keyword: this.formInline.keyword,
        userId: this.user.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
      }
      const result = await bidSheetGetStatement(data)
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
        console.log(this.stateList)
        this.formInline.state =
          this.stateList.length > 0 ? this.stateList[0].meaning : ""
      }
    }
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
