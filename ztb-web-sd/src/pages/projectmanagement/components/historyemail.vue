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
    title: '发送环节',
    dataIndex: 'point',
    key: 'point',
  },
  {
    title: '邮件主题',
    dataIndex: 'mailSubject',
    key: 'mailSubject',
  },
  {
    title: '发送人',
    dataIndex: 'from',
    key: 'fromName',
    customRender: (text, row, index) => {
      return text ? text.name : ''
    }
  },
  {
    title: '发送人邮箱',
    dataIndex: 'from',
    key: 'fromMail',
    customRender: (text, row, index) => {
      return text ? text.mail : ''
    }
  },
  {
    title: '接收人',
    dataIndex: 'to',
    key: 'toName',
    customRender: (text, row, index) => {
      return text ? text.name : ''
    }
  },
  {
    title: '接收人邮箱',
    dataIndex: 'to',
    key: 'toMail',
    customRender: (text, row, index) => {
      return text ? text.mail : ''
    }
  }
];
export default {
  props: {
    project: {
      type: [Object, String],
      default: {}
    }
  },
  name: "historyemail",
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
        requestId: this.project.id,
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
