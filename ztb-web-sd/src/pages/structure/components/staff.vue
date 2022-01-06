<template>
  <div>
    <a-modal
      width="70%"
      title="员工信息"
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
            <a-form-model-item label="姓名：">
              <a-input v-model="formInline.realName" placeholder="请输入姓名" />
            </a-form-model-item>
          </a-col>
          <a-col :span="16" align="right">
            <a-button type="primary" @click="getDataList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
      <a-table
        id="antdTable"
        :columns="columns"
        rowKey="employeeNo"
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
import { getSysEmployeeByNoBindUser } from "@/services/structure/index";
import moment from 'moment'
const columns = [
  {
    title: "姓名",
    align: "center",
    dataIndex: "realName",
  },
  {
    title: "性别",
    align: "center",
    dataIndex: "sex",
    customRender: (text, row, index) => {
      let sexName = ''
      if (text) {
        sexName = text === 1 ? '男' : '女'
      }
      return sexName
    }
  },
  {
    title: "所属部门",
    align: "center",
    dataIndex: "departmentName",
  },
  {
    title: "直接上级",
    align: "center",
    dataIndex: "managerName",
  },
  {
    title: "电话",
    align: "center",
    dataIndex: "phone",
  },
  {
    title: "已绑定用户",
    align: "center",
    dataIndex: "userName",
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
  name: "staff",
  components: {},
  data () {
    return {
      visible: false,
      formInline: {
        realName: '',
        employeeNo: ''
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
        realName: this.formInline.realName,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysEmployeeByNoBindUser(data)
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
        this.$message.warning('请选择员工')
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