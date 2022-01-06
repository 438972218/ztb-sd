<template>
  <div>
    <a-modal
      width="70%"
      title="供应商"
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
          <a-col :span="6">
            <a-form-model-item label="名称：">
              <a-input v-model="formInline.name" placeholder="请输入名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="28" align="right">
            <a-button type="primary" @click="getDataList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
      <a-table
        id="antdTable"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        :columns="columns"
        rowKey="id"
        :data-source="dataList"
        :pagination="pagination"
        @change="tableChange"
      >
      </a-table>
    </a-modal>
  </div>
</template>
<script>
import { getVendor } from "@/services/supplier/index";
const columns = [
  {
    title: "企业名称",
    align: "center",
    dataIndex: "name",
  },
  {
    title: "商业模式",
    align: "center",
    dataIndex: "businessModel",
  },
  {
    title: "所属地区",
    align: "center",
    dataIndex: "area",
  },
  {
    title: "经营范围",
    align: "center",
    dataIndex: "businessScope",
  },
  {
    title: "联系人姓名",
    align: "center",
    dataIndex: "contactName",
  },
  {
    title: "联系人手机号",
    align: "center",
    dataIndex: "contactMobile",
  },
  {
    title: "主要客户",
    align: "center",
    dataIndex: "mainCustomer",
  }
];
// const rowSelection = {
//   onChange: (selectedRowKeys, selectedRows) => {
//     console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
//   },
//   onSelect: (record, selected, selectedRows) => {
//     console.log(record, selected, selectedRows);
//   },
//   onSelectAll: (selected, selectedRows, changeRows) => {
//     console.log(selected, selectedRows, changeRows);
//   },
// };
export default {
  name: "vendor",
  components: {},
  data () {
    return {
      visible: false,
      formInline: {
        name: ''
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      dataList: [],
      columns,
      selectId: '',
      selectData: [],
      selectedRowKeys: []
    };
  },
  created () {
  },
  methods: {
    // 获取供应商分页数据
    async getDataList () {
      const data = {
        name: this.formInline.name,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getVendor(data)
      if (result.code === 0) {
        this.dataList = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectData = selectedRows
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
      if (this.selectData.length === 0) {
        this.$message.warning('请选择供应商')
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