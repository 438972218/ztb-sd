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
            <a-form-model-item label="权限组名称：">
              <a-input v-model="description" placeholder="请输入权限组名称" />
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
        <a-button
          v-has="['permission_group:add']"
          @click="handleAdd"
          type="primary"
          >新增</a-button
        >
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
            v-has="['permission_group:edit']"
            type="edit"
            theme="filled"
            class="iconstyle"
            @click="handleEdit(record)"
          />
          <a-icon
            v-has="['permission_group:delete']"
            type="delete"
            title="删除"
            class="iconstyle"
            @click="handleDelete(record.id)"
          />
          <!-- <a-icon type="tool" /> -->
        </template>
      </a-table>
    </a-card>

    <a-modal
      title="添加权限组"
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
            <a-form-model-item label="权限组名称：">
              <a-input
                v-model="form.description"
                placeholder="请输入权限组名称"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="权限组标识：">
              <a-input v-model="form.code" placeholder="请输入权限组标识" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="权限：">
              <a-tree-select
                allowClear
                v-model="form.permissionList"
                multiple
                :replaceFields="{
                  children: 'children',
                  title: 'name',
                  key: 'id',
                  value: 'id',
                }"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="treeData"
                placeholder="请选择权限"
                tree-default-expand-all
              >
              </a-tree-select>
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
  </div>
</template>
<script>
import { getSysPermissionGroupPageByCondition, sysPermissionGroupAdd, sysPermissionGroupUpdate, sysPermissionGroupDelete, getSysPermissionTree } from "@/services/structure/index";
import moment from 'moment'
const columns = [
  {
    title: "权限组名称",
    align: "center",
    dataIndex: "description",
  },
  {
    title: "权限组标识",
    align: "center",
    dataIndex: "code"
  },
  {
    title: "备注",
    align: "center",
    dataIndex: "remark"
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
  name: "permissionGroup",
  components: {},
  data () {
    return {
      visible: false,
      code: '',
      description: '',
      form: {
        code: '',
        description: '',
        permissionList: [],
        remark: ''
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      dataList: [],
      treeData: [],
      columns,
    };
  },
  created () {
    this.getDataList()
    this.getTreeData()
  },
  methods: {
    // 获取权限组分页数据
    async getDataList () {
      const data = {
        description: this.description,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysPermissionGroupPageByCondition(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取权限树型结构数据
    async getTreeData () {
      const result = await getSysPermissionTree()
      if (result.code === 0) {
        this.treeData = result.data
      }
    },
    // 新增
    handleAdd () {
      this.form = {
        code: '',
        permissionList: [],
        description: '',
        remark: ''
      }
      this.visible = true
    },
    // 保存数据
    async handleSave () {
      let result = null
      if (this.form.id) {
        result = await sysPermissionGroupUpdate(this.form)
      } else {
        result = await sysPermissionGroupAdd(this.form)
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
          const result = await sysPermissionGroupDelete(id)
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