<template>
  <div>
    <a-modal
      width="70%"
      title="职务信息"
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
            <a-form-model-item label="名称：">
              <a-input v-model="formInline.fullName" placeholder="请输入名称" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
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
import { getSysJobPageByCondition } from "@/services/structure/index";
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
    title: "职务类别",
    align: "center",
    dataIndex: "jobTypeName",
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
  }
];

export default {
  name: "post",
  components: {},
  data () {
    return {
      visible: false,
      formInline: {
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
      selectId: '',
      selectData: ''
    };
  },
  created () {
  },
  methods: {
    // 获取员工分页数据
    async getDataList () {
      const data = {
        shortName: this.formInline.shortName,
        fullName: this.formInline.fullName,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysJobPageByCondition(data)
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
        this.$message.warning('请选择职务')
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