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
            <a-form-model-item label="角色名称：">
              <a-input v-model="name" placeholder="请输入角色名称" />
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
        <a-button v-has="['role:add']" @click="handleAdd" type="primary">新增</a-button>
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
          v-has="['role:edit']"
            type="edit"
            theme="filled"
            class="iconstyle"
            @click="handleEdit(record)"
          />
          <a-icon
            v-has="['role:delete']"
            type="delete"
            title="删除"
            class="iconstyle"
            @click="handleDelete(record.id)"
          />
        </template>
      </a-table>
    </a-card>

    <a-modal
      :title="title"
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
            <a-form-model-item label="角色名称：">
              <a-input v-model="form.name" placeholder="请输入角色名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24"> 
            <a-form-model-item label="角色描述：">
              <a-input
                v-model="form.description"
                placeholder="请输入角色描述"
              />
            </a-form-model-item>
            <a-form-model-item label="所属分部：">
              <a-tree-select
                allowClear
                disabled
                v-model="form.companyId"
                :replaceFields="{
                  children: 'children',
                  title: 'fullName',
                  key: 'id',
                  value: 'id',
                }"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="treeData"
                placeholder="请选择所属分部"
                tree-default-expand-all
              >
              </a-tree-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="权限组：">
              <a-select
                mode="multiple"
                v-model="form.sysPermissionGroupList"
                placeholder="请选择权限组"
              >
                <a-select-option
                  v-for="item in sysPermissionGroupList"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.description }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
           
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import { getSysRolePageByCondition, sysRoleAdd, sysRoleUpdate, sysRoleDelete, getSysPermissionGroupByCondition, getSysCompanyTree } from "@/services/structure/index";
import moment from 'moment'
const columns = [
  {
    title: "角色名称",
    align: "center",
    dataIndex: "name",
  },
  {
    title: "角色描述",
    align: "center",
    dataIndex: "description"
  },
  {
    title: "所属分部",
    align: "center",
    dataIndex: "companyName"
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
  name: "roleManagement",
  components: {},
  data () {
    return {
      title: '',
      visible: false,
      name: '',
      treeData: [],
      form: {
        name: '',
        description: '',
        companyId: undefined,
        sysPermissionGroupList: [],
      },
      sysPermissionGroupList: [],
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
    this.getRoleData()
    this.getTreeData()
  },
  methods: {
    // 获取角色分页数据
    async getDataList () {
      const data = {
        name: this.name,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysRolePageByCondition(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取权限组下拉数据
    async getRoleData () {
      const result = await getSysPermissionGroupByCondition()
      if (result.code === 0) {
        this.sysPermissionGroupList = result.data
      }
    },
    // 获取公司树型数据
    async getTreeData () {
      const result = await getSysCompanyTree()
      if (result.code === 0) {
        this.treeData = result.data
      }
    },
    // 新增
    handleAdd () {
      this.title = '新增角色'
      this.form = {
        name: '',
        description: '',
        companyId: this.treeData.length > 0 ? this.treeData[0].id : '',
        sysPermissionGroupList: [],
      }
      this.visible = true
    },
    // 保存数据
    async handleSave () {
      let result = null
      if (this.form.id) {
        result = await sysRoleUpdate(this.form)
      } else {
        result = await sysRoleAdd(this.form)
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
      this.title = '编辑角色'
      this.form = Object.assign({}, row)
      this.$delete(this.form, 'companyName')
      this.$delete(this.form, 'updatedTime')
      this.$delete(this.form, 'updatedUser')
      this.$delete(this.form, 'createdTime')
      this.$delete(this.form, 'createdUser')
      this.visible = true
    },
    // 删除
    async handleDelete (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await sysRoleDelete(id)
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