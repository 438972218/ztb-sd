<template>
  <div>
    <a-card class="card-top">
      <a-form-model
        :model="formInline"
        layout="horizontal"
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row>
          <a-col :sm="8">
            <a-form-model-item label="姓名：">
              <a-input v-model="formInline.realName" placeholder="请输入姓名" />
            </a-form-model-item>
          </a-col>
          <!-- <a-col :sm="8">
            <a-form-model-item label="员工编号：">
              <a-input
                v-model="formInline.employeeNo"
                placeholder="请输入员工编号"
              />
            </a-form-model-item>
          </a-col> -->
          <a-col :span="16" align="right">
            <a-button type="primary" @click="getDataList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card>
      <div class="operator">
        <a-button v-has="['employee:add']" @click="handleAdd" type="primary"
          >新增</a-button
        >
      </div>
      <a-table
        :columns="columns"
        rowKey="employeeNo"
        :data-source="dataList"
        :pagination="pagination"
        @change="tableChange"
      >
        <template slot="operation" slot-scope="record">
          <a-icon
            v-has="['employee:edit']"
            type="edit"
            theme="filled"
            class="iconstyle"
            @click="handleEdit(record)"
          />
          <a-icon
            v-has="['employee:delete']"
            type="delete"
            title="删除"
            class="iconstyle"
            @click="handleDelete(record.id)"
          />
        </template>
      </a-table>
    </a-card>
  </div>
</template>
<script>
import { getSysEmployeePageByCondition, SysEmployeeDelete } from "@/services/structure/index";
import moment from 'moment'
const columns = [
  {
    title: "员工编号",
    align: "center",
    dataIndex: "employeeNo",
  },
  {
    title: "姓名",
    align: "center",
    dataIndex: "realName",
  },
  {
    title: "性别",
    align: "center",
    dataIndex: "sex",
    customRender: (text, row, index) => {
      let sexName = text === 1 ? '男' : '女'
      return sexName
    }
  },
  {
    title: "所属部门",
    align: "center",
    dataIndex: "departmentName",
  },
  {
    title: "直接上级",
    align: "center",
    dataIndex: "managerName",
  },
  {
    title: "电话",
    align: "center",
    dataIndex: "phone",
  },
  // {
  //   title: "创建时间",
  //   align: "center",
  //   dataIndex: "createdTime",
  //   customRender: (text, row, index) => {
  //     return moment(parseInt(text)).format('YYYY-MM-DD')
  //   }
  // },
  {
    title: "操作",
    align: "center",
    scopedSlots: { customRender: "operation" },
  },
];

export default {
  name: "staffManagement",
  components: {},
  data () {
    return {
      formInline: {
        realName: '',
        employeeNo: ''
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      dataList: [],
      columns,
    };
  },
  created () {
    this.getDataList()
  },
  methods: {
    // 获取员工分页数据
    async getDataList () {
      const data = {
        // employeeNo: this.formInline.employeeNo,
        realName: this.formInline.realName,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysEmployeePageByCondition(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 新增
    handleAdd () {
      sessionStorage.setItem('staffManagementDetail', JSON.stringify({
        departmentId: undefined, // 所属部门
        employeeNo: '', // 员工编号
        hobby: '', // 爱好
        mail: '', // 邮箱
        managerId: '', // 直接上级id
        office: '', // 办公室
        officeAddress: '', // 办公地点
        officePhone: '', // 办公电话
        phone: '', // 电话
        position: undefined, // 所属岗位
        realName: '',
        sex: 1, // 性别
        speciality: '', // 专长
        status: 1, // 状态(在线、离职)
        positionName: ''
      }))
      sessionStorage.setItem('staffDetailType', 'add')
      this.$router.push({ path: 'staffManagementDetail' })
    },
    // 编辑
    async handleEdit (row) {
      sessionStorage.setItem('staffManagementDetail', JSON.stringify({
        id: row.id,
        departmentId: row.departmentId, // 所属部门
        employeeNo: row.employeeNo, // 员工编号
        hobby: row.hobby, // 爱好
        mail: row.mail, // 邮箱
        managerId: row.managerId, // 直接上级id
        office: row.office, // 办公室
        officeAddress: row.officeAddress, // 办公地点
        officePhone: row.officePhone, // 办公电话
        phone: row.phone, // 电话
        position: row.position, // 所属岗位
        realName: row.realName,
        sex: row.sex, // 性别
        speciality: row.speciality, // 专长
        status: row.status, // 状态(在线、离职)
        managerName: row.managerName,
        positionName: row.positionName
      }))
      sessionStorage.setItem('staffDetailType', 'edit')
      this.$router.push({ path: 'staffManagementDetail' })
    },
    // 删除
    async handleDelete (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await SysEmployeeDelete(id)
          if (result.code === 0) {
            that.$message.success('删除成功')
            that.getDataList()
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>