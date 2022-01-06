<template>
  <div>
    <!-- <a-card class="card-top">
      <a-form-model
        layout="horizontal"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18, offset: 0 }"
      >
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="权限名称：">
              <a-input v-model="name" placeholder="请输入权限名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="16" align="right">
            <a-button type="primary" @click="getDataList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card> -->
    <a-card>
      <div class="operator">
        <a-button v-has="['permission:add']" @click="handleAdd" type="primary">新增</a-button>
      </div>
      <a-table
        style="margin-bottom: 30px"
        :columns="columns"
        rowKey="id"
        :data-source="dataList"
        :pagination="false"
      >
        <template slot="operation" slot-scope="record">
          <a-icon
          v-has="['permission:edit']"
            type="edit"
            theme="filled"
            class="iconstyle"
            @click="handleEdit(record)"
          />
          <a-icon
          v-has="['permission:delete']"
            type="delete"
            title="删除"
            class="iconstyle"
            @click="handleDelete(record.id)"
          />
        </template>
      </a-table>
    </a-card>

    <a-modal
      title="添加权限"
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
            <a-form-model-item label="权限名称：">
              <a-input v-model="form.name" placeholder="请输入权限名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="父节点：">
              <a-tree-select
                allowClear
                v-model="form.parentId"
                :replaceFields="{
                  children: 'children',
                  title: 'name',
                  key: 'id',
                  value: 'id',
                }"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="dataList"
                placeholder="请选择父节点，不选默认为父节点"
                tree-default-expand-all
              >
              </a-tree-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="功能级别：">
              <a-select v-model="form.level" placeholder="请选择父节点">
                <a-select-option value="menu"> 菜单 </a-select-option>
                <a-select-option value="button"> 按钮 </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="form.level === 'menu'">
            <a-form-model-item label="是否隐藏：">
              <a-select v-model="form.isHide" placeholder="请选择父节点">
                <a-select-option :value="1"> 是 </a-select-option>
                <a-select-option :value="0"> 否 </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="权限字符串：">
              <a-input
                v-model="form.permission"
                placeholder="请输入权限字符串"
              />
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="序号：">
              <a-input v-model="form.sort" placeholder="请输入序号" />
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="url地址：" v-if="form.level === 'menu'">
              <a-input v-model="form.routeUrl" placeholder="请输入url地址" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="说明：">
              <a-input v-model="form.description" placeholder="请输入说明" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="图标：" v-if="form.level === 'menu'">
              <a-input v-model="form.icon" placeholder="请输入图标" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import { getSysPermissionPageByCondition, sysPermissionAdd, sysPermissionUpdate, sysPermissionDelete, getSysPermissionTree } from "@/services/structure/index";
import moment from 'moment'
const columns = [
  {
    title: "权限名称",
    align: "center",
    dataIndex: "name",
  },
  {
    title: "功能级别",
    align: "center",
    dataIndex: "level",
    customRender: (text, row, index) => {
      return text === 'menu' ? '菜单' : '按钮'
    }
  },
  {
    title: "是否隐藏",
    align: "center",
    dataIndex: "isHide",
    customRender: (text, row, index) => {
      return text === '1' ? '是' : '否'
    }
  },
  {
    title: "url地址",
    align: "center",
    dataIndex: "routeUrl"
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
  name: "authorityManagement",
  components: {},
  data () {
    return {
      visible: false,
      name: '',
      form: {
        name: '', // 权限名称
        description: '', // 说明
        icon: '', // 图标
        isHide: 0, // 是否隐藏 1:是 0：否
        level: 'menu', // 功能的级别('menu','button')
        parentId: undefined, // 父权限id
        permission: '', // 权限字符串
        routeUrl: '', // url地址
        sort: '', // 排序
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
    // 获取权限树型结构数据
    async getDataList () {
      const result = await getSysPermissionTree()
      if (result.code === 0) {
        this.dataList = result.data
      }
    },
    // 新增
    handleAdd () {
      this.form = {
        name: '', // 权限名称
        description: '', // 说明
        icon: '', // 图标
        isHide: 0, // 是否隐藏 1:是 0：否
        level: 'menu', // 功能的级别('menu','button')
        parentId: undefined, // 父权限id
        routeUrl: '', // url地址
        sort: '', // 排序
        permission: '', // 权限字符串
      }
      this.visible = true
    },
    // 保存数据
    async handleSave () {
      let result = null
      if (!this.form.parentId) this.form.parentId = '0'
      if (this.form.id) {
        result = await sysPermissionUpdate(this.form)
      } else {
        result = await sysPermissionAdd(this.form)
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
      this.form = Object.assign({}, row)
      this.$delete(this.form, 'deleted')
      this.$delete(this.form, 'children')
      this.visible = true
    },
    // 删除
    async handleDelete (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await sysPermissionDelete(id)
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