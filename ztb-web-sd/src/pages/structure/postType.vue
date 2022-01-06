<template>
  <div>
    <a-card class="card-top">
      <a-form-model
        :model="formInline"
        layout="horizontal"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18, offset: 0 }"
      >
        <a-row>
          <a-col :md="8" :sm="12">
            <a-form-model-item label="名称：">
              <a-input v-model="formInline.fullName" placeholder="请输入名称" />
            </a-form-model-item>
          </a-col>
          <a-col :md="8" :sm="12">
            <a-form-model-item label="简称：">
              <a-input
                v-model="formInline.shortName"
                placeholder="请输入简称"
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
        <a-button v-has="['jobtitle:add']" @click="handleAdd" type="primary">新增</a-button>
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
            v-has="['jobtitle:edit']"
            type="edit"
            theme="filled"
            class="iconstyle"
            @click="handleEdit(record)"
          />
          <a-icon
          v-has="['jobtitle:delete']"
            type="delete"
            title="删除"
            class="iconstyle"
            @click="handleDelete(record.id)"
          />
        </template>
      </a-table>
    </a-card>

    <a-modal
      title="添加职务类别"
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
            <a-form-model-item label="名称：">
              <a-input v-model="form.fullName" placeholder="请输入名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="简称：">
              <a-input v-model="form.shortName" placeholder="请输入简称" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import { getSysJobTypePageByCondition, SysJobTypeAdd, SysJobTypeUpdate, SysJobTypeDelete } from "@/services/structure/index";
import moment from 'moment'
const columns = [
  {
    title: "名称",
    align: "center",
    dataIndex: "fullName",
  },
  {
    title: "简称",
    align: "center",
    dataIndex: "shortName",
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
  name: "postType",
  components: {},
  data () {
    return {
      visible: false,
      formInline: {
        fullName: '',
        shortName: ''
      },
      form: {
        fullName: '',
        shortName: ''
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
    // 获取职务类别分页数据
    async getDataList () {
      const data = {
        shortName: this.formInline.shortName,
        fullName: this.formInline.fullName,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysJobTypePageByCondition(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 新增
    handleAdd () {
      this.form = {
        fullName: '',
        shortName: ''
      }
      this.visible = true
    },
    // 保存数据
    async handleSave () {
      let result = null
      if (this.form.id) {
        result = await SysJobTypeUpdate(this.form)
      } else {
        result = await SysJobTypeAdd(this.form)
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
          const result = await SysJobTypeDelete(id)
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