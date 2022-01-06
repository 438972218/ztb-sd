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
            <a-form-model-item label="岗位名称：">
              <a-input
                v-model="formInline.fullName"
                placeholder="请输入岗位名称"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="岗位简称：">
              <a-input
                v-model="formInline.shortName"
                placeholder="请输入岗位简称"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="8" align="right">
            <a-button type="primary" @click="getDataList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card>
      <div class="operator">
        <a-button v-has="['em_position:add']" @click="handleAdd" type="primary">新增</a-button>
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
          v-has="['em_position:edit']"
            type="edit"
            theme="filled"
            class="iconstyle"
            @click="handleEdit(record)"
          />
          <a-icon
          v-has="['em_position:delete']"
            type="delete"
            title="删除"
            class="iconstyle"
            @click="handleDelete(record.id)"
          />
        </template>
      </a-table>
    </a-card>

    <a-modal
      title="添加公司"
      :visible="visible"
      width="700px"
      @ok="handleSave"
      @cancel="handleColse"
    >
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :model="form"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="岗位名称：">
              <a-input v-model="form.fullName" placeholder="请输入岗位名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="岗位简称：">
              <a-input v-model="form.shortName" placeholder="请输入岗位简称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="岗位职责：">
              <a-input
                v-model="form.positionDuty"
                placeholder="请输入岗位职责"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属职务：">
              <a-input
                disabled
                v-model="post"
                placeholder="请选择所属职务"
                enter-button
                @search="postSearch"
              >
                <a-icon slot="addonAfter" type="search" @click="postSearch" />
              </a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任职资格：">
              <a-input
                v-model="form.positionQualifications"
                placeholder="请输入任职资格"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注：">
              <a-input v-model="form.remark" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <post ref="post" @selectData="selectData"></post>
  </div>
</template>
<script>
import { getSysPositionPageByCondition, sysPositionAdd, sysPositionUpdate, sysPositionDelete } from "@/services/structure/index";
import moment from 'moment'
import post from './components/post.vue'
const columns = [
  {
    title: "岗位名称",
    align: "center",
    dataIndex: "fullName",
  },
  {
    title: "岗位简称",
    align: "center",
    dataIndex: "shortName",
  },
  {
    title: "岗位职责",
    align: "center",
    dataIndex: "positionDuty",
  },
  {
    title: "所属职务",
    align: "center",
    dataIndex: "jobName",
  },
  {
    title: "任职资格",
    align: "center",
    dataIndex: "positionQualifications",
  },
  {
    title: "创建人",
    align: "center",
    dataIndex: "createdUser",
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
  name: "companyManagement",
  components: { post },
  data () {
    return {
      post: '',
      visible: false,
      formInline: {
        fullName: '',
        shortName: ''
      },
      form: {
        fullName: '', // 岗位全称
        shortName: '', // 岗位简称
        jobId: '', // 所属职务
        positionDuty: '', // 岗位职责
        positionQualifications: '', // 任职资格
        remark: '', // 备注
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
    // 获取岗位分页数据
    async getDataList () {
      const data = {
        shortName: this.formInline.shortName,
        fullName: this.formInline.fullName,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysPositionPageByCondition(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询职务信息
    postSearch () {
      this.$refs.post.visible = true
      this.$refs.post.getDataList()
    },
    // 选中的职务信息
    selectData (row) {
      this.post = row.fullName
      this.form.jobId = row.id
    },
    // 新增
    handleAdd () {
      this.form = {
        fullName: '', // 岗位全称
        shortName: '', // 岗位简称
        jobId: '', // 所属职务
        positionDuty: '', // 岗位职责
        positionQualifications: '', // 任职资格
        remark: '', // 备注
      }
      this.visible = true
    },
    // 保存数据
    async handleSave () {
      let result = null
      if (this.form.id) {
        result = await sysPositionUpdate(this.form)
      } else {
        result = await sysPositionAdd(this.form)
      }
      if (result.code === 0) {
        this.visible = false
        this.$message.success('保存成功')
        this.getDataList()
      } else {
        this.$message.error(result.message)
      }
    },
    // 编辑
    async handleEdit (row) {
      this.post = row.jobName
      this.form = row
      this.visible = true
    },
    // 删除
    async handleDelete (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await sysPositionDelete(id)
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