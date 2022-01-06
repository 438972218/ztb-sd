<template>
  <div>
    <a-card class="card-top">
      <a-form-model
        layout="horizontal"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18, offset: 0 }"
      >
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="用户名称：">
              <a-input v-model="name" placeholder="请输入用户名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="16" align="right">
            <a-button type="primary" @click="getDataList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card>
      <div class="operator">
        <a-button v-has="['user:add']" @click="handleAdd" type="primary"
          >新增</a-button>
      </div>
      <a-table
        :columns="columns"
        rowKey="id"
        :data-source="dataList"
        :pagination="pagination"
        @change="tableChange"
      >
        <template slot="operation" slot-scope="record">
          <a-icon
            v-has="['user:edit']"
            type="edit"
            theme="filled"
            class="iconstyle"
            @click="handleEdit(record)"
          />
          <a-icon
            v-has="['user:edit']"
            type="setting"
            @click="handleSetting(record.id)"
          />
        </template>
      </a-table>
    </a-card>

    <a-modal
      :maskClosable="false"
      :title="title"
      :visible="visible"
      width="700px"
      @ok="handleSave"
      @cancel="handleColse"
    >
      <template slot="footer">
        <a-button type="primary" @click="handleReset" v-if="form.id !== ''">
          重置密码
        </a-button>
        <a-button key="back" @click="handleColse"> 取消 </a-button>
        <a-button
          key="submit"
          type="primary"
          :loading="loading"
          @click="handleSave"
        >
          确定
        </a-button>
      </template>
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
            <a-form-model-item label="姓名：" prop="name">
              <a-input v-model="form.name" placeholder="请输入姓名" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="账号：" prop="userName">
              <a-input v-model="form.userName" placeholder="请输入账号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="form.id === ''">
            <a-form-model-item
              label="密码："
              :prop="form.id !== '' ? '' : 'password'"
            >
              <a-input
                :disabled="form.id !== ''"
                type="password"
                v-model="form.password"
                placeholder="请输入密码"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应员工：" prop="employeeId">
              <a-input
                disabled
                v-model="manager"
                placeholder="请选择对应员工"
                enter-button
                @search="managerSearch"
              >
                <a-icon
                  slot="addonAfter"
                  type="search"
                  @click="managerSearch"
                />
              </a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="角色：" prop="sysRoleVoList">
              <a-select
                v-model="form.sysRoleVoList"
                mode="multiple"
                style="width: 100%"
                placeholder="请选择角色"
              >
                <a-select-option
                  v-for="item in roleList"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="邮箱：" prop="mail">
              <a-input v-model="form.mail" placeholder="请输入邮箱" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="手机号码：" prop="phone">
              <a-input v-model="form.phone" placeholder="请输入手机号码" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="帐号状态：">
              <a-switch
                v-model="status"
                checked-children="正常"
                un-checked-children="冻结"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="账户锁定：">
              <a-switch
                v-model="lockStatus"
                checked-children="启用"
                un-checked-children="锁定"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <!-- 分配权限 -->
    <a-modal
      title="分配权限"
      :visible="roleVisible"
      width="700px"
      @ok="roleSave"
      @cancel="roleVisible = false"
    >
      <a-form-model
        layout="horizontal"
        :model="userRelation"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="公司：">
              <a-tree-select
                allowClear
                multiple
                v-model="userRelation.companyIds"
                :replaceFields="{
                  children: 'children',
                  title: 'fullName',
                  key: 'id',
                  value: 'id',
                }"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="companyTree"
                placeholder="请选择公司"
                tree-default-expand-all
              >
              </a-tree-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <staff ref="staff" @selectData="selectData"></staff>
    <vendor ref="vendor" @selectData="vendorData"></vendor>
  </div>
</template>
<script>
import { getSysUserServicePageByCondition, sysUserAdd, sysUserUpdate, sysUserDelete, getSysRoleList, getSysCompanyTree, getPurchaseOrzTree, getCategoryTree, postUserRelation, getUserRelation, resetPutPasswordById } from "@/services/structure/index";
import { logout } from "@/services/user.js";
import moment from 'moment'
import staff from '../structure/components/staff.vue'
import vendor from './components/vendor.vue'
const columns = [
  {
    title: "用户名称",
    align: "center",
    dataIndex: "name",
  },
  {
    title: "账号",
    align: "center",
    dataIndex: "userName"
  },
  {
    title: "手机号码",
    align: "center",
    dataIndex: "phone"
  },
  {
    title: "帐号状态",
    align: "center",
    dataIndex: "status",
    customRender: (text, row, index) => {
      return text === '0' ? '正常' : '冻结'
    }
  },
  {
    title: "账户是否被锁定",
    align: "center",
    dataIndex: "lockStatus",
    customRender: (text, row, index) => {
      return text === '0' ? '启用' : '锁定'
    }
  },
  {
    title: "最后登陆时间",
    align: "center",
    dataIndex: "loginDate"
  },
  {
    title: "最后登陆IP",
    align: "center",
    dataIndex: "loginIp"
  },
  {
    title: "创建人",
    align: "center",
    dataIndex: "createdUserName",
  },
  {
    title: "创建时间",
    align: "center",
    dataIndex: "createdTime",
    customRender: (text, row, index) => {
      return moment(parseInt(text)).format('YYYY-MM-DD')
    }
  },
  {
    title: "操作",
    align: "center",
    scopedSlots: { customRender: "operation" },
  },
];

export default {
  name: "userManagement",
  components: { staff, vendor },
  data () {
    return {
      loading: false,
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        userName: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        employeeId: [{ required: true, message: '请选择对应员工', trigger: 'change' }],
        sysRoleVoList: [{ required: true, message: '请选择角色', trigger: 'change' }],
        mail: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
      },
      vendorName: '',
      roleVisible: false,
      title: '',
      lockStatus: true,
      status: true,
      manager: '',
      visible: false,
      name: '',
      form: {
        id: '',
        name: '',
        mail: '', // 邮箱
        password: '', // 密码
        phone: '', // 手机号码
        employeeId: '', // 所属员工
        userName: '', // 账号
        sysRoleVoList: []
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      dataList: [],
      columns,
      roleList: [],
      companyTree: [],
      purchaseTree: [],
      categoryTree: [],
      userRelation: {
        userId: '', // 用户ID
        categoryIds: [], // 品类IDS
        companyIds: [], // 公司IDS
        purchaseOrzIds: [], // 采购组织IDS
        vendorIds: [] // 供应商IDS
      }
    };
  },
  created () {
    this.getDataList()
    this.getRoleList()
    this.getCompanyTree()
    // this.getPurchaseTree()
    // this.getCategoryTreeList()
  },
  methods: {
    handleLogout () {
      logout();
      this.$router.push("/login");

    },
    // 重置密码
    handleReset () {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认重置密码',
        async onOk () {
          const result = await resetPutPasswordById(that.form.id)
          if (result.code === 0) {
            that.$message.success('修改成功')
            that.visible = false
            that.handleLogout()
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 获取职务类别分页数据
    async getDataList () {
      this.loading = false
      const data = {
        name: this.name,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysUserServicePageByCondition(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取公司树型
    async getCompanyTree () {
      const result = await getSysCompanyTree()
      if (result.code === 0) {
        this.companyTree = result.data
      }
    },
    // // 获取采购组织树型
    // async getPurchaseTree () {
    //   const result = await getPurchaseOrzTree()
    //   if (result.code === 0) {
    //     this.purchaseTree = result.data
    //   }
    // },
    // // 查询品类管理树型结构
    // async getCategoryTreeList () {
    //   const result = await getCategoryTree()
    //   if (result.code === 0) {
    //     this.categoryTree = result.data
    //   }
    // },
    // 获取角色下拉
    async getRoleList () {
      const result = await getSysRoleList()
      if (result.code === 0) {
        this.roleList = result.data
      }
    },
    // 保存权限关系
    async roleSave () {
      const result = await postUserRelation(this.userRelation)
      if (result.code === 0) {
        this.roleVisible = false
        this.getDataList()
        this.$message.success('保存成功')
      }
    },
    // 查询用户关联
    async getRelation (id) {
      const result = await getUserRelation(id)
      if (result.code === 0) {
        this.userRelation = {
          userId: id, // 用户ID
          categoryIds: result.data.categoryIds, // 品类IDS
          companyIds: result.data.companyIds, // 公司IDS
          purchaseOrzIds: result.data.purchaseOrzIds, // 采购组织IDS
          vendorIds: result.data.vendorIds // 供应商IDS
        }
        this.vendorName = result.data.vendorNames.length > 0 ? result.data.vendorNames.toString() : ''
      }
    },
    // 分配权限
    handleSetting (id) {
      this.getRelation(id)
      this.userRelation = {
        userId: id, // 用户ID
        categoryIds: [], // 品类IDS
        companyIds: [], // 公司IDS
        purchaseOrzIds: [], // 采购组织IDS
        vendorIds: [] // 供应商IDS
      }
      this.$refs.vendor.selectData = []
      this.$refs.vendor.selectedRowKeys = []
      this.vendorName = ''
      this.roleVisible = true
    },
    // 供应商选择信息
    vendorData (row) {
      let nameList = []
      let ids = []
      row.forEach(item => {
        nameList.push(item.name)
        ids.push(item.id)
      })
      this.vendorName = nameList.toString()
      this.userRelation.vendorIds = ids
    },
    // 查询供应商信息
    vendorSearch () {
      this.$refs.vendor.visible = true
      // this.$refs.vendor.selectData = []
      // this.$refs.vendor.selectedRowKeys = []
      this.$refs.vendor.getDataList()
    },
    // 查询员工信息
    managerSearch () {
      this.$refs.staff.visible = true
      this.$refs.staff.getDataList()
    },
    // 选中的员工信息
    selectData (row) {
      this.manager = row.realName
      this.form.employeeId = row.id
    },
    // 新增
    handleAdd () {
      this.title = '新增用户'
      this.form = {
        id: '',
        name: '',
        mail: '', // 邮箱
        password: '', // 密码
        phone: '', // 手机号码
        employeeId: '', // 所属员工
        userName: '', // 账号
        sysRoleVoList: []
      }
      this.manager = ''
      this.visible = true
    },
    // 保存数据
    async handleSave () {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          this.loading = true
          let result = null
          this.form.lockStatus = this.lockStatus ? '0' : '1'
          this.form.status = this.status ? '0' : '1'
          if (this.form.id) {
            result = await sysUserUpdate(this.form)
          } else {
            result = await sysUserAdd(this.form)
          }
          if (result.code === 0) {
            this.visible = false
            this.$message.success('保存成功')
            this.getDataList()
          } else {
            this.loading = false
            this.$message.error(result.message)
          }
        }
      });
    },
    // 编辑
    async handleEdit (row) {
      this.title = '编辑用户'
      this.form = Object.assign({}, row)
      this.manager = this.form.employeeName
      this.$delete(this.form, 'sourceType')
      this.$delete(this.form, 'employeeName')
      this.lockStatus = row.lockStatus === '0'
      this.status = row.status === '0'
      this.visible = true
    },
    // 删除
    async handleDelete (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await sysUserDelete(id)
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
    },
    // 弹窗关闭
    handleColse () {
      this.$refs.ruleForm.resetFields();
      this.visible = false
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>