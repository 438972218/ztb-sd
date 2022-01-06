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
import { getBulletinBoard } from "@/services/source/contend";
import moment from 'moment'
const columns = [
  {
    title: '公告内容',
    dataIndex: 'message',
    key: 'message'
  },
  {
    title: '公告时间',
    dataIndex: 'bulletinTime',
    customRender: (text, row, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss') : ''
    }
  },
];
export default {
  name: "bulletin",
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
      const search = {
        requestId: this.project.requestId,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getBulletinBoard(search)
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
