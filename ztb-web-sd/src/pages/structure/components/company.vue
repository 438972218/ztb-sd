<template>
  <div>
    <a-modal
      width="70%"
      title="公司信息"
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
            <a-form-model-item label="公司名称：">
              <a-input
                v-model="formInline.fullName"
                placeholder="请输入公司名称"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="公司简称：">
              <a-input
                v-model="formInline.shortName"
                placeholder="请输入公司简称"
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
import { getSysCompanyPageByCondition } from "@/services/structure/index";
const columns = [
  {
    title: "公司名称",
    align: "center",
    dataIndex: "fullName",
  },
  {
    title: "公司简称",
    align: "center",
    dataIndex: "shortName",
  },
  {
    title: "公司编码",
    align: "center",
    dataIndex: "code",
  },
  {
    title: "公司网站",
    align: "center",
    dataIndex: "website",
  },
  {
    title: "上级组织",
    align: "center",
    dataIndex: "parentName",
  },
  {
    title: "负责人",
    align: "center",
    dataIndex: "principal",
  },
  {
    title: "负责人手机号",
    align: "center",
    dataIndex: "principalPhone",
  }
];

export default {
  name: "company",
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
    // 获取公司分页数据
    async getDataList () {
      const data = {
        fullName: this.formInline.fullName,
        shortName: this.formInline.shortName,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getSysCompanyPageByCondition(data)
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