<template>
  <div>
    <a-card class="card-top">
      <a-alert
        v-if="type"
        style="margin-bottom: 10px"
        :message="'项目:' + data.code + '-' + data.title"
        type="info"
      >
        <a-row slot="description">
          <a-col :span="12">{{ '项目启动日期:' + data.startTime }}</a-col>
          <a-col :span="12" align="right">
            <span style="color: #248c00; font-size: 22px">{{
              data.status
            }}</span>
          </a-col>
        </a-row>
        <!-- <p slot="description">
          {{ '项目启动日期:' + form.startTime }}
        </p> -->
      </a-alert>
      <a-row style="padding-bottom: 10px">
        <a-col :span="12">
          <a-space>
            <a-button
              v-if="
                type && (data.status === '未启动' || data.status === '暂停')
              "
              v-has="['project:operation']"
              type="primary"
              @click="handleSave(1)"
              >运行</a-button
            >
            <a-button
              v-if="
                type && (data.status === '暂停' || data.status === '运行中')
              "
              type="primary"
              v-has="['project:end']"
              @click="handleSave(3)"
              >结束</a-button
            >
            <a-button
              v-if="type && data.status === '运行中'"
              type="primary"
              v-has="['project:suspend']"
              @click="handleSave(2)"
              >暂停</a-button
            >
            <a-button
              v-if="
                type && (data.status === '未启动' || data.status === '暂停')
              "
              type="primary"
              @click="removeProjectSheet()"
              v-has="['project:delete']"
              >删除</a-button
            >
            <a-button
              v-if="!type || data.status === '未启动' || data.status === '暂停'"
              type="primary"
              :loading="loading"
              v-has="['project:save']"
              @click="handleSave()"
              >保存</a-button
            >
            <a-button @click="back()"> 返回 </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" align="right" v-if="type">
          <span
            >{{
              '创建人: ' +
              form.sysUserInfoVO.name +
              '  创建时间：' +
              form.createdTime
            }}
          </span>
        </a-col>
      </a-row>
      <a-form-model
        :model="form"
        ref="ruleForm"
        :rules="rules"
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row>
          <!-- <a-col :sm="8" v-if="type">
            <a-form-model-item label="创建人">
              <a-input
                v-model="form.createdUser"
                :disabled="type"
                placeholder="请输入创建人"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8" v-if="type">
            <a-form-model-item label="创建时间">
              <a-input
                v-model="form.createdTime"
                :disabled="type"
                placeholder="请输入创建时间"
              />
            </a-form-model-item>
          </a-col> -->
          <a-col :sm="8">
            <a-form-model-item label="项目编号">
              <a-input
                disabled
                v-model="form.code"
                placeholder="保存后自动生成"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8" v-if="!type">
            <a-form-model-item label="项目名称" prop="title">
              <a-input v-model="form.title" placeholder="请输入项目名称" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="部门" prop="department">
              <a-tree-select
                :disabled="type"
                allowClear
                v-model="form.department"
                :replaceFields="{
                  children: 'children',
                  title: 'shortName',
                  key: 'id',
                  value: 'shortName',
                }"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="depList"
                placeholder="请选择部门"
                tree-default-expand-all
              >
              </a-tree-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="项目启动日期" prop="startTime">
              <a-date-picker
                :disabled="type"
                format="YYYY-MM-DD"
                valueFormat="YYYY-MM-DD"
                style="width: 100%"
                v-model="form.startTime"
              />
            </a-form-model-item>
          </a-col>
          <a-col
            :sm="8"
            v-if="
              data.id && (data.status === '运行中' || data.status === '已完成')
            "
          >
            <a-form-model-item label="项目激活日期">
              <a-date-picker
                disabled
                format="YYYY-MM-DD"
                valueFormat="YYYY-MM-DD"
                style="width: 100%"
                v-model="form.activationTime"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8" v-if="data.status === '已完成'">
            <a-form-model-item label="项目完成日期">
              <a-date-picker
                disabled
                format="YYYY-MM-DD"
                valueFormat="YYYY-MM-DD"
                style="width: 100%"
                v-model="form.finishTime"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="品类"
              :labelCol="{ span: 2 }"
              :wrapperCol="{ span: 22 }"
            >
              <a-select
                :disabled="
                  type &&
                  data.status &&
                  data.status !== '未启动' &&
                  data.status !== '暂停'
                "
                mode="multiple"
                v-model="form.items"
                style="width: 100%"
                placeholder="请选择品类"
              >
                <a-select-option
                  v-for="item in items"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.itemName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="项目说明"
              :labelCol="{ span: 2 }"
              :wrapperCol="{ span: 22 }"
            >
              <a-textarea
                :disabled="
                  type &&
                  data.status &&
                  data.status !== '未启动' &&
                  data.status !== '暂停'
                "
                v-model="form.explanation"
                placeholder="请输入项目说明"
                :auto-size="{ minRows: 2, maxRows: 6 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card v-if="data.id && type">
      <a-tabs
        v-model="activeKey"
        type="card"
        tab-position="left"
        @change="tabChange"
      >
        <a-tab-pane key="1" tab="附件">
          <enclosure ref="enclosure" :project="data" :user="user"></enclosure>
        </a-tab-pane>
        <a-tab-pane key="2" tab="成员">
          <projectuser
            ref="projectuser"
            :project="data"
            :userList="userList"
          ></projectuser>
        </a-tab-pane>
        <a-tab-pane key="3" tab="招标">
          <bidding ref="bidding" :project="data"></bidding>
        </a-tab-pane>
        <a-tab-pane key="4" tab="竞价">
          <contend ref="contend" :project="data" :userList="userList"></contend>
        </a-tab-pane>
        <a-tab-pane key="5" tab="通知|邮件">
          <discussEmail ref="discussEmail" :project="data"></discussEmail>
        </a-tab-pane>
        <!-- <a-tab-pane key="6" tab="邮件">
          <historyemail ref="historyemail" :project="data"></historyemail>
        </a-tab-pane> -->
      </a-tabs>
    </a-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { getSysCompanyDepartmentTreeDisaled } from "@/services/structure/index";
import { itemGetRequest } from "@/services/basis/index";
import { postProjectSheetReturnVO, deleteProjectSheet, putProjectSheetReturnVO, getFindAllUserAndDepartAndPostion, projectSheetGetSendMailByMember } from "@/services/projectmanagement/project";
import enclosure from './enclosure'
import projectuser from './projectuser'
import discussEmail from './discussEmail'
import bidding from './bidding'
import contend from './contend'
import moment from 'moment'
export default {
  name: "addProject",
  components: { enclosure, projectuser, discussEmail, bidding, contend },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  data () {
    return {
      type: '',
      form: {
        code: '',
        title: '', // 项目名称
        department: undefined, // 所属部门
        startTime: '', // 项目启动日期
        items: [], // 品类
        explanation: '', // 项目说明
      },
      rules: {
        title: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
        department: [{ required: true, message: '请选择部门', trigger: 'change' }],
        startTime: [{ required: true, message: '请选择项目启动日期', trigger: 'change' }]
      },
      activeKey: '1',
      depList: [], // 部门下拉
      items: [], // 品类下拉
      data: '',
      userList: [],
      loading: false
    };
  },
  activated () {
    const keep = sessionStorage.getItem('addProjectkeep')
    if (!keep) {
      this.type = sessionStorage.getItem('edit') ? true : false
      this.form = sessionStorage.getItem('projectData') ? JSON.parse(sessionStorage.getItem('projectData')) : this.form = {
        code: '',
        title: '', // 项目名称
        department: this.user.departmentName, // 所属部门
        startTime: '', // 项目启动日期
        items: [], // 品类
        explanation: '', // 项目说明
      }
      this.data = this.form
      this.activeKey = '1'
      this.$nextTick(() => {
        this.$refs.enclosure.getDataList()
      })
      this.getSysDepartmentByCondition()
      this.getItems()
      this.getUserList()
    }
  },
  deactivated () {
    sessionStorage.setItem('addProjectkeep', 'true')
  },
  mounted () {
    this.type = sessionStorage.getItem('edit') ? true : false
    this.form = sessionStorage.getItem('projectData') ? JSON.parse(sessionStorage.getItem('projectData')) : this.form = {
      code: '',
      title: '', // 项目名称
      department: this.user.departmentName, // 所属部门
      startTime: '', // 项目启动日期
      items: [], // 品类
      explanation: '', // 项目说明
    }
    this.data = this.form
    this.getSysDepartmentByCondition()
    this.getItems()
    this.getUserList()
  },
  methods: {
    // 标签发生改变
    tabChange (val) {
      if (val === '1') {
        this.$nextTick(() => {
          this.$refs.enclosure.getDataList()
        })
      } else if (val === '2') {
        this.$nextTick(() => {
          this.$refs.projectuser.getDataList()
        })
      } else if (val === '3') {
        this.$nextTick(() => {
          this.$refs.bidding.getDataList()
        })
      } else if (val === '4') {
        this.$nextTick(() => {
          this.$refs.contend.getContendDataList()
        })
      } else
        if (val === '5') {
          this.$nextTick(() => {
            this.$refs.discussEmail.getDataList()
          })
        }
    },
    // 发送邮件给项目成员
    async getProjectSheetSendMailByMember () {
      const result = await projectSheetGetSendMailByMember(this.form.id)
      if (result.code === 0) {

      } else {
        this.$message.error(result.message)
      }
    },
    // 新增项目
    async handleSave (type) {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          this.loading = true
          let data = {
            title: this.form.title,
            department: this.form.department,
            startTime: this.form.startTime,
            items: this.form.items.toString(),
            explanation: this.form.explanation,
            status: this.data.status === '暂停' ? '暂停' : '未启动',
            account: this.user.userName
          }
          if (type === 1) {
            data.status = '运行中'
            data.activationTime = moment().format('YYYY-MM-DD')
            this.getProjectSheetSendMailByMember()
          } else if (type === 2) {
            data.status = '暂停'
          } else if (type === 3) {
            data.status = '已完成'
            data.activationTime = this.form.activationTime
            data.finishTime = moment().format('YYYY-MM-DD')
          }
          let result = null
          if (this.form.id) {
            data.id = this.form.id
            result = await putProjectSheetReturnVO(data)
          } else {
            result = await postProjectSheetReturnVO(data)
          }
          if (result.code === 0) {
            this.data = result.data
            this.data.startTime = this.data.startTime ? moment(parseInt(this.data.startTime)).format('YYYY-MM-DD') : ''
            this.data.activationTime = this.data.activationTime ? moment(parseInt(this.data.activationTime)).format('YYYY-MM-DD') : ''
            this.data.finishTime = this.data.finishTime ? moment(parseInt(this.data.finishTime)).format('YYYY-MM-DD') : ''
            this.data.createdTime = moment(parseInt(this.data.createdTime)).format('YYYY-MM-DD HH:mm:ss')
            this.data.items = this.data.items ? this.data.items.split(',') : []
            this.form = Object.assign({}, this.data)
            this.type = true
            this.loading = false
            this.$message.success('保存成功')
          } else {
            this.loading = false
            this.$message.error(result.message)
          }
        }
      })
    },
    // 获取用户下拉
    async getUserList () {
      const result = await getFindAllUserAndDepartAndPostion()
      if (result.code === 0) {
        this.userList = result.data
      }
    },
    // 删除项目
    async removeProjectSheet () {
      const result = await deleteProjectSheet(this.form.id)
      if (result.code === 0) {
        this.$message.success('删除成功')
        this.$router.push({ path: 'project' })
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
    // 获取品类下拉
    async getItems () {
      const data = {
        currentPage: -1,
        deleted: 0,
      }
      const result = await itemGetRequest(data)
      if (result.code === 0) {
        this.items = result.data ? result.data.records : []
      } else {
        this.$message.error(result.message)
      }
    },
    // 返回上一页
    back () {
      this.$router.push({ path: 'project' })
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>