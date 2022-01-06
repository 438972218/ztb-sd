<template>
  <div>
    <a-card>
      <a-row :gutter="20">
        <a-col :span="6">
          <a-card>
            <a-tree
              :expanded-keys="expandedKeys"
              :auto-expand-parent="autoExpandParent"
              :selected-keys="selectedKeys"
              :tree-data="treeData"
              :replaceFields="{
                children: 'children',
                title: 'shortName',
                key: 'id',
              }"
              @expand="onExpand"
              @select="onSelect"
            />
          </a-card>
        </a-col>
        <a-col :span="18">
          <a-card>
            <a-tabs v-model="active">
              <a-tab-pane key="1" tab="基本信息">
                <a-col :span="24">
                  <companyAdd
                    v-if="filter.type === '1'"
                    ref="companyAdd"
                    :isTrue="true"
                    :form="companyForm"
                    @queryData="init"
                  ></companyAdd>
                  <departmentAdd
                    v-else
                    ref="departmentAdd"
                    :company="company"
                    :department="department"
                    :form="departmentForm"
                    @queryData="init"
                  ></departmentAdd>
                </a-col>
              </a-tab-pane>
              <a-tab-pane key="2" tab="员工信息">
                <a-col :span="24">
                  <a-table
                    :columns="columns"
                    :data-source="data"
                    rowKey="id"
                    :pagination="pagination"
                    @change="tableChange"
                  >
                  </a-table>
                </a-col>
              </a-tab-pane>
              <a-button
                v-has="['department:add']"
                @click="handleDepartmentAdd"
                style="margin-right: 10px"
                type="primary"
                :disabled="state === 1"
                slot="tabBarExtraContent"
              >
                新增部门
              </a-button>
              <a-button
                v-has="['department:delete']"
                @click="handleDepartmentDelete"
                type="primary"
                v-if="this.filter.type === '2' && departmentForm.id"
                slot="tabBarExtraContent"
              >
                删除部门
              </a-button>
            </a-tabs>
          </a-card>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>
<script>
import { getSysCompanyDepartmentTree, getjudgeGroupCompany, queryByCompanyIdOrDepartIdPage, sysDepartmentDelete, getSysCompanyById, getSysDepartmentById } from "@/services/structure/index";
import moment from 'moment'
import companyAdd from './components/companyAdd.vue'
import departmentAdd from './components/departmentAdd.vue'
const columns = [
  {
    title: "姓名",
    align: "center",
    dataIndex: "realName",
  },
  // {
  //   title: "员工编号",
  //   align: "center",
  //   dataIndex: "employeeNo",
  // },
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
  {
    title: "创建时间",
    align: "center",
    dataIndex: "createdTime",
    customRender: (text, row, index) => {
      return moment(parseInt(text)).format('YYYY-MM-DD')
    }
  }
];
export default {
  name: "organizational",
  components: { companyAdd, departmentAdd },
  data () {
    return {
      active: '1',
      state: 1,
      data: [],
      columns,
      expandedKeys: [],
      autoExpandParent: true,
      selectedKeys: [],
      treeData: [],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      filter: {
        type: '',
        id: '',
        currentPage: 1,
        pageSize: 10
      },
      companyForm: {
        fullName: '', // 公司名称
        shortName: '', // 公司简称
        website: '', // 公司网站
        code: '', // 公司编码
        principal: '', // 公司负责人
        principalPhone: '', // 公司负责人手机号
        parentId: '0', // 公司上级
        bankAccount: '', // 公司开户银行
        bankAccountNumber: '', // 账号信息
      },
      company: '', // 公司名称
      companyId: '',
      department: '', // 部门名称
      departmentId: '0',
      departmentForm: {
        shortName: '', // 部门名称
        fullName: '', // 部门简称
        companyId: '', // 公司
        parentId: '', // 如果部门没有上级部门则约定固定传1，如果有上级部门，传上线部门的id
        code: '', // 部门编码
        manager: '' // 部门负责人
      },
      manager: '',
      parentName: '',
      deleteType: ''
    };
  },
  created () {
    this.init()
  },
  methods: {
    // 初始加载
    async init () {
      const result = await getSysCompanyDepartmentTree()
      if (result.code === 0) {
        this.treeData = result.data
        this.expandedKeys = this.treeData.map(item => item.id)
        this.selectedKeys = this.selectedKeys.length > 0 ? this.selectedKeys : (this.treeData.length > 0 ? [this.treeData[0].id] : [])
        if (this.treeData.length > 0) {
          this.parentName = this.parentName ? this.parentName : this.treeData[0].shortName
          this.filter.type = this.filter.type ? this.filter.type : this.treeData[0].type + ''
          this.filter.id = this.filter.id ? this.filter.id : this.treeData[0].id + ''
          this.getqueryByCompanyId()
          if (this.filter.type === '1') {
            // 根据id获取公司信息
            this.getCompanyById(this.selectedKeys[0])
          } else if (this.filter.type === '2') {
            // 根据id获取部门信息
            this.getDepartmentById(this.selectedKeys[0])
          }
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 删除部门
    async handleDepartmentDelete () {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await sysDepartmentDelete(that.departmentId)
          if (result.code === 0) {
            that.$message.success('删除成功')
            that.selectedKeys = []
            that.init()
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    onExpand (expandedKeys) {
      this.expandedKeys = expandedKeys;
      this.autoExpandParent = false;
    },
    onSelect (selectedKeys, info) {
      this.selectedKeys = selectedKeys.length > 0 ? selectedKeys : this.selectedKeys;
      this.filter = {
        type: info.node.dataRef.type + '',
        id: info.node.dataRef.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      // 选中公司
      if (this.filter.type === '1') {
        this.company = info.node.dataRef.title
        this.companyId = info.node.dataRef.id
        this.departmentId = '0'
        this.getgetjudgeGroup(this.selectedKeys[0])
        // 根据id获取公司信息
        this.getCompanyById(this.companyId)
      }
      // 选中部门
      if (this.filter.type === '2') {
        this.department = info.node.dataRef.title
        this.departmentId = info.node.dataRef.id
        this.state = 0
        // 获取部门信息
        this.getDepartmentById(this.selectedKeys[0])
      }
      this.parentName = info.node.dataRef.shortName
      this.getqueryByCompanyId(this.selectedKeys[0])
    },
    // 根据id获取公司信息
    async getCompanyById (id) {
      const result = await getSysCompanyById(id)
      if (result.code === 0) {
        const data = result.data
        this.companyForm = {
          id: data.id,
          shortName: data.shortName,
          fullName: data.fullName,
          parentId: data.parentId,
          website: data.website,
          code: data.code,
          principal: data.principal,
          principalPhone: data.principalPhone,
          bankAccount: data.bankAccount, // 公司开户银行
          bankAccountNumber: data.bankAccountNumber, // 账号信息
          companyType: data.companyType
        }
        this.$refs.companyAdd.parent = result.data.parentName
        this.$refs.companyAdd.parentCompanyType = result.data.parentCompanyType
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取部门信息
    async getDepartmentById (id) {
      const result = await getSysDepartmentById(id)
      if (result.code === 0) {
        const data = result.data
        this.departmentForm = {
          id: data.id,
          shortName: data.shortName,
          fullName: data.fullName,
          companyId: data.companyId,
          parentId: data.parentId,
          code: data.code,
          manager: data.manager
        }
        this.manager = data.manager
        this.companyId = data.companyId
        this.$refs.departmentAdd.manager = data.managerVo ? data.managerVo.realName : ''
      }
    },
    // 查询是否为公司还是集团
    async getgetjudgeGroup (id) {
      const result = await getjudgeGroupCompany(id)
      if (result.code === 0) {
        this.state = result.data
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询员工信息
    async getqueryByCompanyId () {
      const result = await queryByCompanyIdOrDepartIdPage(this.filter)
      if (result.code === 0) {
        this.data = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.filter.currentPage = row.current
      this.filter.pageSize = row.pageSize
      this.getqueryByCompanyId()
    },
    // 新增公司
    handleCompanyAdd () {
      this.companyForm = {
        fullName: '', // 公司名称
        shortName: '', // 公司简称
        website: '', // 公司网站
        code: '', // 公司编码
        principal: '', // 公司负责人
        principalPhone: '', // 公司负责人手机号
        parentId: this.selectedKeys.length > 0 ? this.selectedKeys[0] : '0',
        bankAccount: '', // 公司开户银行
        bankAccountNumber: '', // 账号信息
      }
      this.$refs.companyAdd.parent = this.parentName
      this.$refs.companyAdd.visible = true
    },
    // 新增部门
    handleDepartmentAdd () {
      this.departmentForm = {
        shortName: '', // 部门名称
        fullName: '', // 部门简称
        companyId: this.companyId, // 公司
        parentId: this.departmentId, // 如果部门没有上级部门则约定固定传1，如果有上级部门，传上线部门的id
        code: '', // 部门编码
        manager: this.manager // 部门负责人
      }
      this.filter.type = '2'
      this.active = '1'
    },
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>