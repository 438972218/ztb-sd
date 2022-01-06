<template>
  <div>
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="tableChange"
    >
    </a-table>
  </div>
</template>

<script>
import { getNotificationHistoryEmail } from "@/services/projectmanagement/project";
const columns = [
  {
    title: '邮件主题',
    dataIndex: 'subject',
    key: 'subject',
  },
  {
    title: '接收人邮箱',
    dataIndex: 'to',
    key: 'toMail',
    customRender: (text, row, index) => {
      return text ? text[0].mail : ''
    }
  }
];
export default {
  name: "historyemail",
  props: {
    project: {
      type: Object,
      default: {}
    }
  },
  data () {
    return {
      data: [],
      columns,
      departmentName: '',
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
    }
  },
  mounted () {
    this.getDataList()
  },
  methods: {
    // 查询成员列表
    async getDataList () {
      const data = {
        requestId: this.project.requestId,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getNotificationHistoryEmail(data)
      if (result.code === 0) {
        this.data = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
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
</style>
