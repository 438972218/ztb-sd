<template>
  <div>
    <a-row
      style="padding-bottom: 5px"
      v-if="project.status === '未启动' || project.status === '暂停'"
    >
      <a-col :span="24">
        <a-button type="primary" @click="handleAdd"> 新增 </a-button>
      </a-col>
    </a-row>
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="tableChange"
    >
      <span
        slot="operation"
        slot-scope="text, record"
        v-if="project.status !== '已完成'"
      >
        <a-icon
          type="edit"
          theme="filled"
          class="iconstyle"
          style="padding-right: 10px"
          @click="handleEdit(record)"
        />
        <a-icon
          type="delete"
          title="删除"
          @click="removeProjectMember(record)"
          class="iconstyle"
        />
      </span>
    </a-table>
    <!-- 新增成员 -->
    <a-modal
      title="新增成员"
      :visible="visible"
      width="700px"
      @ok="saveUser"
      @cancel="visible = false"
    >
      <a-form-model
        ref="ruleForm"
        :model="form"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <!-- <a-col :span="24">
            <a-form-model-item label="名称">
              <a-input v-model="form.attachmentName" placeholder="请输入名称" />
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="选择用户">
              <a-select
                v-model="form.userId"
                allowClear
                placeholder="请选择用户"
                @change="userChange"
              >
                <a-select-option
                  v-for="item in userList"
                  :key="item.userId"
                  :value="item.userId"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="姓名">
              <a-input v-model="form.name" disabled placeholder="请输入姓名" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="部门">
              <a-input
                v-model="form.department"
                disabled
                placeholder="请输入部门"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="岗位">
              <a-input v-model="form.post" disabled placeholder="请输入岗位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="邮箱">
              <a-input v-model="form.email" disabled placeholder="请输入邮箱" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { getProjectMemberPageByCondition, postProjectMember, putProjectMember, deleteProjectMember } from "@/services/projectmanagement/project";
const columns = [
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: '部门',
    dataIndex: 'department',
    key: 'department',
  },
  {
    title: '岗位',
    dataIndex: 'post',
    key: 'post',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
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
  name: "projectuser",
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
      visible: false,
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
    }
  },
  mounted () {
    this.getDataList()
  },
  methods: {
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
        this.form.department = data.departmentName
        this.form.post = data.positionName
        this.form.email = data.mail
      }
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
    },
    // 编辑成员
    handleEdit (row) {
      this.form = Object.assign({}, row)
      this.visible = true
    },
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
    handleAdd () {
      this.form = {
        userId: undefined, // 用户id
        name: '', // 姓名
        department: '', // 部门
        post: '', // 岗位
        email: '', // 邮箱
        projectId: this.project.id // 招标单Id
      }
      this.visible = true
    },
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