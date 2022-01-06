<template>
  <div>
    <a-row style="padding-bottom: 5px">
      <a-col :span="24">
        <a-button
          type="primary"
          @click="handleAdd"
          v-if="form.requestStatusName === '待发布' && !getUserInfo"
        >
          新增
        </a-button>
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
        v-if="form.requestStatusName === '待发布' && !getUserInfo"
      >
        <a-icon
          v-if="record.userId !== user.id"
          type="delete"
          @click="deleteUser(record)"
          title="删除"
          class="iconstyle"
        />
      </span>
    </a-table>
    <!-- 新增专家 -->
    <a-modal
      title="新增评委"
      :visible="visible"
      width="700px"
      @ok="handleSave"
      @cancel="visible = false"
    >
      <a-form-model
        ref="ruleForm"
        :rules="rules"
        :model="formInline"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="选择评委" prop="userId">
              <a-select
                v-model="formInline.userId"
                allowClear
                placeholder="请选择评委"
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
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { bidSpecialistPostRequest, bidSpecialistQueryWithUser, bidSpecialistDeleteRequest } from "@/services/source/bidding";
import { getFindAllUserAndDepartAndPostion } from "@/services/projectmanagement/project";
import { mapGetters } from "vuex";
const columns = [
  {
    title: '姓名',
    dataIndex: 'specialistName',
    key: 'specialistName',
  },
  {
    title: '部门',
    dataIndex: 'departmentName',
    key: 'departmentName',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
];
export default {
  name: "committee",
  props: {
    form: {
      type: Object,
      default: {}
    },
    getUserInfo: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  data () {
    return {
      userList: [],
      visible: false,
      formInline: {
        specialistName: '',
        userId: undefined
      },
      rules: {
        userId: [{ required: true, message: '请选择专家', trigger: 'change' }]
      },
      columns,
      data: [
      ],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
    }
  },
  mounted () {
    // this.form = JSON.parse(sessionStorage.getItem('biddingForm'))
    this.getDataList()
    this.getUserList()
  },
  methods: {
    // 删除专家
    async deleteUser (row) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        async onOk () {
          const result = await bidSpecialistDeleteRequest(row.id)
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
    // 保存专家
    handleSave () {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          const result = await bidSpecialistPostRequest(this.formInline)
          if (result.code === 0) {
            this.visible = false
            this.$message.success('保存成功')
            this.getDataList()
          } else {
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
    // 选择用户
    userChange (row) {
      const data = this.userList.find(f => f.userId === row)
      if (data) {
        this.formInline.specialistName = data.name
      }
    },
    // 新增专家
    handleAdd () {
      this.formInline = {
        specialistName: '',
        userId: undefined,
        bidSheetId: this.form.id
      }
      this.visible = true
    },
    // 查询专家
    async getDataList () {
      const data = {
        bidSheetId: this.form.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await bidSpecialistQueryWithUser(data)
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
      this.getDataList()
    },
  }
};
</script>

<style lang="less" scoped>
</style>
