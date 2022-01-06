<template>
  <div>
    <a-row style="padding-bottom: 5px" v-if="project.status !== '已完成'">
      <a-col :span="24" v-if="project.status === '运行中'">
        <a-button type="primary" @click="handleAdd"> 新增 </a-button>
      </a-col>
    </a-row>
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="tableChange"
    >
      <template slot="oddNumber" slot-scope="text, record">
        <a @click="handleEdit(record)">{{ text }}</a>
      </template>
    </a-table>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getBidSheet } from "@/services/projectmanagement/project";
import { biddingGetRequest } from "@/services/source/bidding";
import moment from 'moment'
const columns = [
  {
    title: '招标编号',
    dataIndex: 'oddNumber',
    key: 'oddNumber',
    scopedSlots: { customRender: "oddNumber" },
  },
  {
    title: '招标名称',
    dataIndex: 'title',
    key: 'title',
  },
  {
    title: '创建人',
    dataIndex: 'sysUserInfoVO',
    key: 'createdUser',
    customRender: (text, row, index) => {
      return text ? text.name : ''
    }
  },
  {
    title: '截止时间',
    dataIndex: 'bidEndTime',
    key: 'bidEndTime',
    customRender: (text, row, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss') : ''
    }
  },
  {
    title: '状态',
    dataIndex: 'requestStatusName',
    key: 'requestStatusName'
  }
];
export default {
  props: {
    project: {
      type: [Object, String],
      default: {}
    }
  },
  name: "bidding",
  data () {
    return {
      data: [],
      columns,
      form: {
        userId: undefined, // 用户id
        title: '', // 招标单标题
        department: '', // 部门
        post: '', // 岗位
        email: '', // 邮箱
        projectSheetId: '' // 项目id
      },
      departmentName: '',
      visible: false,
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  mounted () {
    this.getDataList()
  },
  methods: {
    // 编辑
    handleEdit (row) {
      sessionStorage.removeItem('addBiddingkeep')
      let form = Object.assign({}, row)
      form.bidEndTime = moment(parseInt(form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
      form.qualificationTender = form.qualificationTender === 1
      form.technicalTender = form.technicalTender === 1
      form.items = form.items ? form.items.split(',') : []
      sessionStorage.setItem('biddingForm', JSON.stringify(form))
      this.$router.push({ name: '招标详情' })
    },
    // 查询招标数据
    async getDataList () {
      const data = {
        projectSheetId: this.project.id,
        userId: this.user.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await biddingGetRequest(data)
      if (result.code === 0) {
        this.data = result.data ? result.data.records : []
        this.pagination.total = result.data ? result.data.total : 0
      } else {
        this.$message.error(result.message)
      }
    },
    // 创建
    handleAdd () {
      sessionStorage.removeItem('addBiddingkeep')
      const form = {
        id: '',
        projectSheetId: this.project.id, // 项目Id
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
      this.$router.push({ name: '招标详情' })
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
    },
  }
};
</script>

<style lang="less" scoped>
</style>
