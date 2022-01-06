<template>
  <div>
    <a-card class="card-top">
      <a-form-model
        ref="ruleForm"
        :model="form"
        :rules="rules"
        layout="horizontal"
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row>
          <a-col :sm="8">
            <a-form-model-item label="姓名：" prop="realName">
              <a-input v-model="form.realName" placeholder="请输入姓名" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <!-- <a-form-model-item label="员工编号：">
              <a-input
                v-model="form.employeeNo"
                :disabled="type === 'edit'"
                placeholder="请输入员工编号"
              />
            </a-form-model-item> -->
            <a-form-model-item label="员工编号：" prop="employeeNo">
              <a-input v-model="form.employeeNo" placeholder="请输入员工编号" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="电话：" prop="phone">
              <a-input
                type="number"
                v-model="form.phone"
                placeholder="请输入电话"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="邮箱：" prop="mail">
              <a-input v-model="form.mail" placeholder="请输入邮箱" />
            </a-form-model-item>
          </a-col>
          <!-- <a-col :sm="8">
            <a-form-model-item label="爱好：">
              <a-input v-model="form.hobby" placeholder="请输入爱好" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="专长：">
              <a-input v-model="form.speciality" placeholder="请输入专长" />
            </a-form-model-item>
          </a-col> -->
          <a-col :sm="8">
            <a-form-model-item label="办公室：">
              <a-input v-model="form.office" placeholder="请输入办公室" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="办公地点：">
              <a-input
                v-model="form.officeAddress"
                placeholder="请输入办公地点"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="办公电话：">
              <a-input
                v-model="form.officePhone"
                placeholder="请输入办公电话"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="所属岗位：" prop="positionName">
              <!-- <a-select v-model="form.position" placeholder="请选择所属岗位">
                <a-select-option
                  v-for="item in positionList"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.fullName }}
                </a-select-option>
              </a-select> -->
              <a-input
                v-model="form.positionName"
                placeholder="请输入所属岗位"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="所属部门：" prop="departmentId">
              <a-tree-select
                allowClear
                v-model="form.departmentId"
                :replaceFields="{
                  children: 'children',
                  title: 'shortName',
                  key: 'id',
                  value: 'id',
                }"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="depList"
                placeholder="请选择所属分部"
                tree-default-expand-all
              >
              </a-tree-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="直接上级：" prop="managerId">
              <a-input
                disabled
                v-model="manager"
                placeholder="请选择直接上级"
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
          <a-col :sm="8">
            <a-form-model-item label="性别：">
              <a-radio-group v-model="form.sex">
                <a-radio :value="1">男</a-radio>
                <a-radio :value="0">女</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="状态：">
              <a-switch
                v-model="check"
                checked-children="在职"
                un-checked-children="离职"
                default-checked
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-row>
      <a-col :span="24" align="center">
        <a-button type="primary" style="margin-right: 10px" @click="handleSave"
          >保存</a-button
        >
        <a-button @click="back()"> 返回 </a-button>
      </a-col>
    </a-row>
    <staff ref="staff" @selectData="selectData"></staff>
  </div>
</template>
<script>
import staff from './staff.vue'
import { SysEmployeeAdd, SysEmployeeUpdate, getSysCompanyDepartmentTreeDisaled, getSysPositionByCondition } from "@/services/structure/index";
export default {
  name: "staffManagementDetail",
  components: { staff },
  data () {
    return {
      check: true,
      rules: {
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        employeeNo: [{ required: true, message: '请输入员工编号', trigger: 'blur' }],
        mail: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入电话', trigger: 'blur' }],
        positionName: [{ required: true, message: '请输入所属岗位', trigger: 'blur' }],
        departmentId: [{ required: true, message: '请选择所属分部', trigger: 'change' }],
        managerId: [{ required: true, message: '请选择直接上级', trigger: 'change' }],
      },
      form: {
        departmentCode: '', // 所属部门code
        departmentId: undefined, // 所属部门
        employeeNo: '', // 员工编号
        hobby: '', // 爱好
        mail: '', // 邮箱
        managerCode: '', // 直接上级员工code
        managerId: undefined, // 直接上级id
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
      },
      depList: [], // 部门下拉
      positionList: [], // 岗位下拉
      type: '',
      manager: ''
    };
  },
  created () {
  },
  mounted () {
    this.type = sessionStorage.getItem('staffDetailType')
    this.form = JSON.parse(sessionStorage.getItem('staffManagementDetail'))
    this.manager = this.form.managerName ? this.form.managerName : ''
    this.$delete(this.form, 'managerName')
    if (this.form.status === 1) this.check = true
    this.getSysPositionByCondition()
    this.getSysDepartmentByCondition()
  },
  methods: {
    // 获取岗位下拉
    async getSysPositionByCondition () {
      const result = await getSysPositionByCondition()
      if (result.code === 0) {
        this.positionList = result.data
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取部门下拉
    async getSysDepartmentByCondition () {
      const result = await getSysCompanyDepartmentTreeDisaled()
      if (result.code === 0) {
        this.depList = result.data
      } else {
        this.$message.error(result.message)
      }
    },
    // 返回上一页
    back () {
      this.$router.push({ path: 'staffManagement' })
    },
    // 查询员工信息
    managerSearch () {
      this.$refs.staff.visible = true
      this.$refs.staff.getDataList()
    },
    // 选中的员工信息
    selectData (row) {
      this.manager = row.realName
      this.form.managerId = row.id
    },
    // 保存数据
    async handleSave () {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          let result = null
          if (this.type === 'add') {
            result = await SysEmployeeAdd(this.form)
          } else {
            result = await SysEmployeeUpdate(this.form)
          }
          if (result.code === 0) {
            this.$message.success('保存成功')
            this.$router.push({ path: 'staffManagement' })
          } else {
            this.$message.error(result.message)
          }
        }
      });
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>