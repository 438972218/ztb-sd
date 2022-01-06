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
            <a-col :span="6">
              <a-form-model-item label="招标编号">
                <a-input
                  v-model="formInline.oddNumber"
                  placeholder="请输入招标编号"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="6">
              <a-form-model-item label="状态">
                <a-select
                  v-model="formInline.state"
                  placeholder="请选择状态"
                  style="width: 100%"
                >
                  <a-select-option
                    v-for="item in stateList"
                    :key="item.id"
                    :value="item.meaning"
                  >
                    {{ item.meaning }}
                  </a-select-option>
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
            <a-col :span="12" align="right">
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
            <a-button v-has="['bid:add']" type="primary" @click="handleAdd"
              >创建</a-button
            >
            <a-button
              v-has="['bid:templateadd']"
              type="primary"
              @click="templateAdd"
              >从模板创建</a-button
            >
            <a-button
              v-has="['bid:copyadd']"
              type="primary"
              :disabled="!selectId || getUserInfo"
              @click="handleCopy"
              >复制</a-button
            >
          </a-space>
        </a-row>
        <a-row>
          <a-table
            id="antdTable"
            rowKey="id"
            :columns="columns"
            :data-source="dataSource"
            @change="tableChange"
            :pagination="pagination"
            :customRow="customRow"
            :rowClassName="setRowClassName"
          >
            <template slot="oddNumber" slot-scope="text, record">
              <a
                v-if="permissions.operation.find((f) => f === 'bid:edit')"
                @click="handleEdit(record)"
                >{{ text }}</a
              >
              <span v-else>{{ text }}</span>
            </template>
          </a-table>
        </a-row>
      </div>
    </a-card>
    <a-modal
      title="招标模板"
      :visible="templateVisible"
      width="700px"
      @ok="saveTemplate"
      @cancel="templateVisible = false"
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
            <a-button type="primary" @click="getBidSheetTemplate"
              >查询</a-button
            >
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
import { dictionaryGetRequest } from "@/services/projectmanagement/project";
import { biddingGetRequest, bidSheetTemplate, bidSheetshowByRequestIdGetRequest } from "@/services/source/bidding";
import moment from 'moment'
import { mapGetters } from "vuex";
export default {
  name: "bidding",
  components: {},
  data () {
    return {
      formInline: {
        state: undefined,
        oddNumber: '',
        keyword: ''
      },
      temForm: {
        keyword: '',
      },
      columns: [
        {
          title: "招标编号",
          dataIndex: "oddNumber",
          width: 80,
          scopedSlots: { customRender: "oddNumber" },
        },
        {
          title: "招标名称",
          width: 80,
          dataIndex: "title",
          scopedSlots: { customRender: "title" },
        },
        {
          title: "状态",
          width: 80,
          dataIndex: "requestStatusName",
          scopedSlots: { customRender: "requestStatusName" },
        },
        {
          title: "项目代码",
          width: 100,
          dataIndex: "projectSheetVO",
          key: 'projectCode',
          customRender: (text, row, index) => {
            return text ? text.code : ''
          }
        },
        {
          title: "项目名称",
          width: 80,
          dataIndex: "projectSheetVO",
          key: 'projectTitle',
          customRender: (text, row, index) => {
            return text ? text.title : ''
          }
        },
        {
          title: "招标截止时间",
          width: 120,
          dataIndex: "bidEndTime",
          customRender: (text, row, index) => {
            return row.mark ? (row.clarifyEndTime ? moment(parseInt(row.clarifyEndTime)).format('YYYY-MM-DD HH:mm:ss') : '-') : moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss')
          }
        },
        // {
        //   title: "买方企业",
        //   width: 90,
        //   dataIndex: "buyerEnterprise",
        // },
        {
          title: " 创建人",
          width: 100,
          dataIndex: "sysUserInfoVO",
          key: 'userName',
          customRender: (text, row, index) => {
            return text ? text.name : ''
          }
        },
        {
          title: "创建时间",
          width: 120,
          dataIndex: "createdTime",
          customRender: (text, row, index) => {
            return text ? moment(parseInt(text)).format('YYYY-MM-DD') : ''
          }
        },
      ],
      templateVisible: false,
      templateColumns: [
        {
          title: "招标名称",
          width: 80,
          dataIndex: "title",
          scopedSlots: { customRender: "title" },
        },
        {
          title: "项目代码",
          width: 100,
          dataIndex: "projectSheetVO",
          key: 'projectCode',
          customRender: (text, row, index) => {
            return text ? text.code : ''
          }
        },
        {
          title: "项目名称",
          width: 80,
          dataIndex: "projectSheetVO",
          key: 'projectTitle',
          customRender: (text, row, index) => {
            return text ? text.title : ''
          }
        },
        {
          title: "招标截止时间",
          width: 120,
          dataIndex: "bidEndTime",
        },
      ],
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
        total: 0
      },
      templateData: [],
      selectedRows: null,
      selectId: '',
      selectData: null,
    };
  },
  computed: {
    ...mapGetters("account", ["user", 'permissions']),
    getUserInfo () {
      return this.selectData && this.selectData.sysUserInfoVO && this.selectData.sysUserInfoVO.id !== this.user.id
    },
  },
  created () {
  },
  mounted () {
    this.getDataList()
    this.getBidSheetTemplate()
  },
  methods: {
    // 根据流程id查询表单信息
    async getBidSheetshowByRequestIdGetRequest (row) {
      const result = await bidSheetshowByRequestIdGetRequest(row.requestId)
      if (result.code === 0) {
        sessionStorage.removeItem('addBiddingkeep')
        let form = Object.assign({}, result.data)
        form.id = ''
        form.bidEndTime = form.bidEndTime ? moment(parseInt(form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
        form.clarifyEndTime = form.clarifyEndTime ? moment(parseInt(form.clarifyEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
        form.qualificationTender = form.qualificationTender === 1
        form.technicalTender = form.technicalTender === 1
        form.items = form.items ? form.items.split(',') : []
        sessionStorage.setItem("biddingForm", JSON.stringify(form))
        this.$router.push({ path: 'addbidding' })
      } else {
        this.$message.error(result.message)
      }
    },
    // 复制
    handleCopy () {
      this.getBidSheetshowByRequestIdGetRequest(this.selectData)
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
    // 显示模板
    templateAdd () {
      this.templateVisible = true
    },
    // 模板创建
    saveTemplate () {
      if (this.selectedRows) {
        sessionStorage.removeItem('addBiddingkeep')
        this.templateVisible = false
        let form = Object.assign({}, this.selectedRows)
        form.id = ''
        form.bidEndTime = form.bidEndTime ? moment(parseInt(form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
        form.clarifyEndTime = form.clarifyEndTime ? moment(parseInt(form.clarifyEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
        form.qualificationTender = form.qualificationTender === 1
        form.technicalTender = form.technicalTender === 1
        form.items = form.items ? form.items.split(',') : []
        sessionStorage.setItem("biddingForm", JSON.stringify(form))
        this.$router.push({ path: "addbidding" })
      } else {
        this.$message.warning("请选择模板")
      }
    },
    // 选中事件
    selectChange (selectedRowKeys, selectedRows) {
      this.selectedRows = selectedRows.length > 0 ? selectedRows[0] : null
    },
    // 查询bid招标单模板
    async getBidSheetTemplate () {
      const data = {
        keyword: this.temForm.keyword,
        currentPage: -1
      }
      const result = await bidSheetTemplate(data)
      if (result.code === 0) {
        this.templateData = result.data.records
      } else {
        this.$message.error(result.error)
      }
    },
    // 查询招标单数据
    async getDataList () {
      const data = {
        userId: this.user.id,
        keyword: this.formInline.keyword,
        oddNumber: this.formInline.oddNumber,
        statusId: this.formInline.state,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await biddingGetRequest(data)
      if (result.code === 0) {
        this.dataSource = result.data ? result.data.records : []
        this.pagination.total = result.data ? result.data.total : 0
      } else {
        this.$message.error(result.message)
      }
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
    },
    // 查询状态
    async getDictionaryRequestAll () {
      const data = {
        currentPage: -1,
        dictionaryClass: 'projectStatus'
      }
      const result = await dictionaryGetRequest(data)
      if (result.code === 0) {
        this.stateList = result.data.records
        this.formInline.state = this.stateList.length > 0 ? this.stateList[0].meaning : ''
      }
    },
    // 创建
    handleAdd () {
      sessionStorage.removeItem('addBiddingkeep')
      const form = {
        id: '',
        projectSheetId: undefined, // 项目Id
        qualificationTender: false,
        technicalTender: false,
        title: '', // 标题
        tenderMode: '邀请参与招标', // 招标方式
        currency: undefined, // 币种
        whetherTest: 0, // 是否测试招标
        biddingmethod: '', // 招标方式
        budget: '', // 预算
        rankingRules: undefined, // 供应商回复排名
        bidEndTime: '', // 投标截止时间
        approvalProcess: undefined, // 需要审批
        items: [], // 品类
        explanation: '', // 说明
      }
      sessionStorage.setItem('biddingForm', JSON.stringify(form))
      this.$router.push({ path: 'addbidding' })
    },
    // 编辑
    handleEdit (row) {
      sessionStorage.removeItem('addBiddingkeep')
      let form = Object.assign({}, row)
      form.bidEndTime = moment(parseInt(form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
      form.clarifyEndTime = form.clarifyEndTime ? moment(parseInt(form.clarifyEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
      form.qualificationTender = form.qualificationTender === 1
      form.technicalTender = form.technicalTender === 1
      form.items = form.items ? form.items.split(',') : []
      sessionStorage.setItem('biddingForm', JSON.stringify(form))
      this.$router.push({ path: 'addbidding' })
    },
  },
};
</script>

<style lang="less" scoped>
#antdTable {
  /deep/ .clickRowStyl {
    background: #f1f6fd;
  }
}
</style>
