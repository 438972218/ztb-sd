<template>
  <div>
    <a-card class="card-top">
      <a-form-model
        :model="formInline"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18, offset: 0 }"
      >
        <a-row>
          <a-col :span="6">
            <a-form-model-item label="状态">
              <a-select
                v-model="formInline.state"
                placeholder="请选择状态"
                style="width: 100%"
              >
                <a-select-option value="">全部</a-select-option>
                <a-select-option
                  v-for="item in stateList"
                  :key="item.id"
                  :value="item.meaning"
                >
                  {{ item.meaning }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="关键字">
              <a-input
                v-model="formInline.keyword"
                placeholder="请输入关键字"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" align="right">
            <a-button type="primary" @click="getDataList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card>
      <div class="operator">
        <a-space>
          <a-button type="primary" @click="addProject" v-has="['project:add']"
            >创建</a-button
          >
          <a-button
            v-has="['project:templateadd']"
            type="primary"
            @click="getProjectTemplate"
            >从模板创建</a-button
          >
          <a-button
            v-has="['project:copyadd']"
            type="primary"
            :disabled="!selectId"
            @click="handleCopy"
            >复制</a-button
          >
        </a-space>
      </div>
      <a-table
        id="antdTable"
        :columns="columns"
        rowKey="id"
        :data-source="dataList"
        :pagination="pagination"
        @change="tableChange"
        :customRow="customRow"
        :rowClassName="setRowClassName"
      >
        <template
          slot="name"
          slot-scope="text, record"
          v-has="['project:edit']"
        >
          <a
            v-if="permissions.operation.find((f) => f === 'project:edit')"
            @click="handleClick(record)"
            >{{ text }}</a
          >
          <span v-else>{{ text }}</span>
        </template>
      </a-table>
    </a-card>
    <!-- 模板列表 -->
    <a-modal
      title="项目模板"
      :visible="visible"
      width="700px"
      @ok="templateAdd"
      @cancel="visible = false"
    >
      <a-form-model
        :model="temForm"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18, offset: 0 }"
      >
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="关键字">
              <a-input v-model="temForm.keyword" placeholder="请输入关键字" />
            </a-form-model-item>
          </a-col>
          <a-col :span="4" align="right">
            <a-button type="primary" @click="getProjectTemplate">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
      <a-table
        rowKey="id"
        :rowSelection="{
          type: 'radio',
          columnWidth: 50,
          onChange: selectChange,
        }"
        :columns="templateColumns"
        :data-source="templateData"
      >
      </a-table>
    </a-modal>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { getProjectSheetPageByCondition, dictionaryGetRequest, getProjectSheetUser } from "@/services/projectmanagement/project";
import moment from 'moment'
const columns = [
  {
    title: "项目编号",
    align: "center",
    dataIndex: "code",
    scopedSlots: { customRender: 'name' },
  },
  {
    title: "项目名称",
    align: "center",
    dataIndex: "title",
    key: 'title',
  },
  {
    title: "项目状态",
    align: "center",
    dataIndex: "status"
  },
  {
    title: "部门",
    align: "center",
    dataIndex: "department"
  },
  {
    title: "项目启动日期",
    align: "center",
    dataIndex: "startTime",
    customRender: (text, row, index) => {
      return moment(parseInt(text)).format('YYYY-MM-DD')
    }
  },
  {
    title: "项目说明",
    align: "center",
    dataIndex: "explanation"
  },
  {
    title: "项目激活日期",
    align: "center",
    dataIndex: "activationTime",
    customRender: (text, row, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD') : ''
    }
  },
  {
    title: "项目完成日期",
    align: "center",
    dataIndex: "finishTime",
    customRender: (text, row, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD') : ''
    }
  },
  {
    title: "创建人",
    align: "center",
    dataIndex: "sysUserInfoVO",
    customRender: (text, row, index) => {
      return text ? text.name : ''
    }
  }
];
const templateColumns = [
  {
    title: "项目编号",
    align: "center",
    dataIndex: "code",
    scopedSlots: { customRender: 'name' },
  },
  {
    title: "项目名称",
    align: "center",
    dataIndex: "title",
    key: 'title',
  },
  {
    title: "项目说明",
    align: "center",
    dataIndex: "explanation"
  },
  {
    title: "创建人",
    align: "center",
    dataIndex: "sysUserInfoVO",
    customRender: (text, row, index) => {
      return text ? text.name : ''
    }
  }
]
export default {
  name: "project",
  components: {},
  computed: {
    ...mapGetters("account", ["user", 'permissions']),
  },
  data () {
    return {
      templateColumns,
      templateData: [],
      temForm: {
        keyword: "",
      },
      title: '',
      visible: false,
      name: '',
      treeData: [],
      formInline: {
        state: '',
        keyword: ''
      },
      selectId: '',
      selectData: null,
      stateList: [],
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
      selectedRows: null,
    };
  },
  created () {
    this.getDictionaryRequestAll()
    this.getDataList()
    console.log(this.permissions)
  },
  methods: {
    // 模板创建
    templateAdd () {
      if (this.selectedRows) {
        this.visible = false
        sessionStorage.removeItem('projectData')
        sessionStorage.removeItem('edit')
        sessionStorage.removeItem('addProjectkeep')
        let data = Object.assign({}, this.selectedRows)
        data.id = ''
        data.startTime = data.startTime ? moment(parseInt(data.startTime)).format('YYYY-MM-DD') : ''
        data.updatedTime = data.updatedTime ? moment(parseInt(data.updatedTime)).format('YYYY-MM-DD') : ''
        data.activationTime = data.activationTime ? moment(parseInt(data.activationTime)).format('YYYY-MM-DD') : ''
        data.finishTime = data.finishTime ? moment(parseInt(data.finishTime)).format('YYYY-MM-DD') : ''
        data.createdTime = moment(parseInt(data.createdTime)).format('YYYY-MM-DD HH:mm:ss')
        data.items = data.items ? data.items.split(',') : []
        sessionStorage.setItem('projectData', JSON.stringify(data))
        this.$router.push({ path: 'addproject' })
      } else {
        this.$message.warning("请选择模板")
      }
    },
    // 选中事件
    selectChange (selectedRowKeys, selectedRows) {
      this.selectedRows = selectedRows.length > 0 ? selectedRows[0] : null
    },
    // 查询模板
    async getProjectTemplate () {
      const data = {
        type: 'template',
        keyword: this.temForm.keyword,
        currentPage: -1,
      }
      const result = await getProjectSheetPageByCondition(data)
      if (result.code === 0) {
        this.visible = true
        this.templateData = result.data.records
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取项目分页数据
    async getDataList () {
      const data = {
        type: 'official',
        status: this.formInline.state,
        keyword: this.formInline.keyword,
        userId: this.user.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getProjectSheetUser(data)
      if (result.code === 0) {
        this.dataList = result.data ? result.data.records : []
        this.pagination.total = result.data ? result.data.total : 0
      } else {
        this.$message.error(result.message)
      }
    },
    // 表格选中行事件
    customRow (record) {
      return {
        on: {
          click: () => {
            this.selectId = record.id
            this.selectData = record
          }
        }
      }
    },
    // 设置行样式
    setRowClassName (record) {
      return record.id === this.selectId ? 'clickRowStyl' : ''
    },
    // 编辑事件
    handleClick (row) {
      sessionStorage.removeItem('projectData')
      sessionStorage.removeItem('addProjectkeep')
      let data = Object.assign({}, row)
      data.startTime = data.startTime ? moment(parseInt(data.startTime)).format('YYYY-MM-DD') : ''
      data.updatedTime = data.updatedTime ? moment(parseInt(data.updatedTime)).format('YYYY-MM-DD') : ''
      data.activationTime = data.activationTime ? moment(parseInt(data.activationTime)).format('YYYY-MM-DD') : ''
      data.finishTime = data.finishTime ? moment(parseInt(data.finishTime)).format('YYYY-MM-DD') : ''
      data.createdTime = moment(parseInt(data.createdTime)).format('YYYY-MM-DD HH:mm:ss')
      data.items = data.items ? data.items.split(',') : []
      sessionStorage.setItem('edit', 'edit')
      sessionStorage.setItem('projectData', JSON.stringify(data))
      this.$router.push({ path: 'addproject' })
    },
    // 复制
    handleCopy () {
      sessionStorage.removeItem('edit')
      sessionStorage.removeItem('addProjectkeep')
      let data = Object.assign({}, this.selectData)
      data.startTime = data.startTime ? moment(parseInt(data.startTime)).format('YYYY-MM-DD') : ''
      data.createdTime = moment(parseInt(data.createdTime)).format('YYYY-MM-DD HH:mm:ss')
      data.items = data.items ? data.items.split(',') : []
      data.projectId = ''
      data.id = ''
      sessionStorage.setItem('projectData', JSON.stringify(data))
      this.$router.push({ path: 'addproject' })
    },
    // 查询状态
    async getDictionaryRequestAll () {
      const data = {
        currentPage: -1,
        dictionaryClass: 'projectStatus'
      }
      const result = await dictionaryGetRequest(data)
      if (result.code === 0) {
        this.stateList = result.data.records
        // this.formInline.state = this.stateList.length > 0 ? this.stateList[0].meaning : ''
      }
    },
    // 新建项目
    addProject () {
      sessionStorage.removeItem('edit')
      sessionStorage.removeItem('addProjectkeep')
      sessionStorage.removeItem('projectData')
      this.$router.push({ path: 'addproject' })
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
#antdTable {
  /deep/ .clickRowStyl {
    background: #f1f6fd;
  }
}
</style>