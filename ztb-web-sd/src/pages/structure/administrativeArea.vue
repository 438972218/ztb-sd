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
            <a-form-model-item label="区域名称：">
              <a-input v-model="name" placeholder="请输入区域名称" />
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
        <a-button  v-has="['adm_area:add']" @click="handleAdd" type="primary">新增</a-button>
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
           v-has="['adm_area:edit']"
            type="edit"
            theme="filled"
            class="iconstyle"
            @click="handleEdit(record)"
          />
          <a-icon
           v-has="['adm_area:delete']"
            type="delete"
            title="删除"
            class="iconstyle"
            @click="handleDelete(record.id)"
          />
        </template>
      </a-table>
    </a-card>
    
    <a-modal
      title="添加区域"
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
            <a-form-model-item label="区域名称：">
              <a-input v-model="form.name" placeholder="请输入区域名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="区域类型：">
              <a-select v-model="form.type" placeholder="请选择区域类型">
                <a-select-option value="1"> 省 </a-select-option>
                <a-select-option value="2"> 市 </a-select-option>
                <a-select-option value="3"> 区 </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="父节点：">
              <a-tree-select
                v-model="form.parentId"
                :replaceFields="{
                  children: 'childrens',
                  title: 'title',
                  key: 'key',
                  value: 'key',
                }"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="treeData"
                placeholder="请选择父节点"
                tree-default-expand-all
              >
              </a-tree-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import { getSysRegionPageByCondition, getRegionTree, sysRegionAdd, sysRegionUpdate, sysRegionDelete } from "@/services/structure/index";
import moment from 'moment'
const columns = [
  {
    title: "区域名称",
    align: "center",
    dataIndex: "name",
  },
  {
    title: "区域类型",
    align: "center",
    dataIndex: "type",
    customRender: (text, row, index) => {
      let name = ''
      switch (text) {
        case '1':
          name = '省'
          break
        case '2':
          name = '市'
          break
        case '3':
          name = '区'
          break
      }
      return name
    }
  },
  // {
  //   title: "父节点",
  //   align: "center",
  //   dataIndex: "parentId"
  // },
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
  name: "administrativeArea",
  components: {},
  data () {
    return {
      parentName: '',
      columns,
      visible: false,
      form: {
        name: '',
        type: '1',
        parentId: undefined
      },
      name: '',
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      treeData: [],
      dataList: []
    };
  },
  created () {
    this.getDataList()
    this.getTreeList()
  },
  methods: {
    // 获取区域分页数据
    async getDataList () {
      const data = {
        name: this.name,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysRegionPageByCondition(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取树型数据
    async getTreeList () {
      const result = await getRegionTree()
      if (result.code === 0) {
        this.treeData = result.data
      } else {
        this.$message.error(result.message)
      }
    },
    // 新增
    handleAdd () {
      this.form = {
        name: '',
        type: '1',
        parentId: undefined
      }
      this.visible = true
    },
    // 编辑
    handleEdit (row) {
      this.form = Object.assign({}, row)
      this.visible = true
    },
    // 删除
    handleDelete () {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await sysRegionDelete(id)
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
    // 保存
    async handleSave () {
      let result = null
      if (this.form.id) {
        result = await sysRegionUpdate(this.form)
      } else {
        result = await sysRegionAdd(this.form)
      }
      if (result.code === 0) {
        this.visible = false
        this.$message.success('保存成功')
        this.getDataList()
      } else {
        this.$message.error(result.message)
      }
    },
    // 取消
    handleColse () {
      this.visible = false
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>