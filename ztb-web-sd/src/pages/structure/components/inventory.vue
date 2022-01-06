<template>
  <div>
    <a-modal
      width="70%"
      title="组织信息"
      :visible="visible"
      @ok="handleOk"
      @cancel="visible = false"
    >
      <a-form-model
        :model="formInline"
        layout="horizontal"
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row>
          <a-col :sm="8">
            <a-form-model-item label="组织名称：">
              <a-input v-model="formInline.name" placeholder="请输入组织名称" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="组织编号：">
              <a-input v-model="formInline.code" placeholder="请输入组织编号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="8" align="right">
            <a-button type="primary" @click="getDataList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
      <a-table
        id="antdTable"
        :columns="columns"
        rowKey="id"
        :data-source="dataList"
        :pagination="pagination"
        :customRow="customRow"
        :rowClassName="setRowClassName"
        @change="tableChange"
      >
      </a-table>
    </a-modal>
  </div>
</template>
<script>
import { getInventoryOrzPageByCondition } from "@/services/structure/index";
const columns = [
  {
    title: "组织名称",
    align: "center",
    dataIndex: "name",
  },
  {
    title: "组织编号",
    align: "center",
    dataIndex: "code",
  },
  {
    title: "组织上级",
    align: "center",
    dataIndex: "pName",
  },
  {
    title: "公司",
    align: "center",
    dataIndex: "companyName",
  },
  {
    title: "组织描述",
    align: "center",
    dataIndex: "description",
  }
];

export default {
  name: "inventory",
  components: {},
  data () {
    return {
      visible: false,
      formInline: {
        name: '',
        code: ''
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      dataList: [],
      columns,
      selectId: '',
      selectData: ''
    };
  },
  created () {
  },
  methods: {
    // 获取公司分页数据
    async getDataList () {
      const data = {
        name: this.formInline.name,
        code: this.formInline.code,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getInventoryOrzPageByCondition(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
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
    // 确定事件
    handleOk () {
      if (this.selectData === '') {
        this.$message.warning('请选择上级')
        return
      }
      this.visible = false
      this.$emit('selectData', this.selectData)
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