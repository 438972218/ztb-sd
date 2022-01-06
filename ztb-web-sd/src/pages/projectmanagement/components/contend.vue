<template>
  <div>
    <a-row style="padding-bottom: 5px" v-if="project.status !== '已完成'">
      <a-col :span="24" v-if="project.status === '运行中'">
        <a-space>
          <a-button type="primary" @click="handleAdd"> 新增 </a-button>
        </a-space>
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
import { getProjectMemberPageByCondition, postProjectMember, putProjectMember, deleteProjectMember } from "@/services/projectmanagement/project";
import {
  paidSheetGetRequest
} from "@/services/source/contend"
import { mapGetters } from "vuex";
import moment from "moment"
const columns = [
  {
    title: '竞价编号',
    dataIndex: 'oddNumber',
    key: 'oddNumber',
    scopedSlots: { customRender: "oddNumber" },
  },
  {
    title: '竞价名称',
    dataIndex: 'title',
    key: 'title',
  },
  {
    title: '创建人',
    dataIndex: 'sysUserInfoVO.name',
    key: 'createdUser',
  },
  {
    title: '竞价结束时间',
    dataIndex: 'offerEndTime',
    key: 'offerEndTime',
    customRender: (text, row, index) => {
      return text
        ? moment(parseInt(text)).format("YYYY-MM-DD HH:mm:ss")
        : ""
    },
  },
  {
    title: '竞价状态',
    dataIndex: 'requestStatusName',
    key: 'requestStatusName',
    scopedSlots: { customRender: 'requestStatusName' },
  }
];
export default {
  props: {
    project: {
      type: [Object, String],
      default: {}
    },
    userList: {
      type: Array,
      default: []
    }
  },
  name: "contend",
  data () {
    return {
      data: [],
      columns,
      form: {
        userId: undefined, // 用户id
        name: '', // 姓名
        department: '', // 部门
        post: '', // 岗位
        email: '', // 邮箱
        projectId: '' // 项目id
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
    // this.getDataList()
    this.getContendDataList()
  },
  methods: {
    // 查询竞价单列表数据
    async getContendDataList () {
      const data = {
        projectSheetId: this.project.id,
        userId: this.user.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
      }
      const result = await paidSheetGetRequest(data)
      if (result.code === 0) {
        this.data = result.data ? result.data.records : []
        this.pagination.total = result.data ? result.data.total : 0
      } else {
        this.$message.error(result.message)
      }
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
      this.$router.push({ name: "竞价详情" })
    },
    // 创建
    handleAdd () {
      sessionStorage.removeItem('addContendkeep')
      const form = {
        id: '',
        projectSheetId: this.project.id, // 项目Id
        delayInterval: undefined, // 延时间隔
        triggerMinimumBidRange: "", // 到达触发价格后最小出价配置
        triggerPrice: "", // 触发价格
        minimumBidRange: "", // 最小出价幅度
        minimumBidRule: undefined, // 最小出价配置
        startPrice: "", // 起始价格
        startPriceRule: undefined, // 起始价格规则
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
      this.$router.push({ name: "竞价详情" })
    },
    // 查询成员列表
    async getDataList () {
      const data = {
        projectId: this.project.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getProjectMemberPageByCondition(data)
      if (result.code === 0) {
        this.data = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 选择用户
    userChange (row) {
      const data = this.userList.find(f => f.userId === row)
      if (data) {
        this.form.name = data.name
        this.form.department = data.departmentId
        this.departmentName = data.departmentName
        this.form.post = data.positionName
        this.form.email = data.mail
      }
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getContendDataList()
    },
    // 编辑成员
    // handleEdit (row) {
    //   this.form = Object.assign({}, row)
    //   this.visible = true
    // },
    // 删除成员
    async removeProjectMember (row) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        async onOk () {
          const result = await deleteProjectMember(row.id)
          if (result.code === 0) {
            that.getDataList()
            that.$message.success('删除成功')
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 新增成员
    // handleAdd () {
    //   this.form = {
    //     userId: undefined, // 用户id
    //     name: '', // 姓名
    //     department: '', // 部门
    //     post: '', // 岗位
    //     email: '', // 邮箱
    //     projectId: this.project.id // 招标单Id
    //   }
    //   this.visible = true
    // },
    // 保存成员
    async saveUser () {
      let result = null
      if (this.form.id) {
        // 编辑
        result = await putProjectMember(this.form)
      } else {
        // 新增
        result = await postProjectMember(this.form)
      }
      if (result.code === 0) {
        this.$message.success('保存成功')
        this.getDataList()
        this.visible = false
      } else {
        this.$message.error(response.message)
      }
    },
  }
};
</script>

<style lang="less" scoped>
</style>