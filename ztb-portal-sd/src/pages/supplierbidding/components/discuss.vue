<template>
  <div>
    <a-tabs v-model="activeKey" :pagination="pagination">
      <a-tab-pane key="1" tab="通知">
        <talkover ref="talkover"></talkover>
      </a-tab-pane>
      <a-tab-pane key="2" tab="广播">
        <a-table
          :columns="columns"
          :data-source="bulletinData"
          @change="tableChange"
        >
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
import { bulletinBoard } from "@/services/source/bidding";
import talkover from './talkover'
import moment from 'moment'
const columns = [
  {
    title: '广播内容',
    dataIndex: 'message',
    key: 'message'
  },
  {
    title: '广播时间',
    dataIndex: 'bulletinTime',
    customRender: (text, row, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss') : ''
    }
  },
];

export default {
  name: "discuss",
  props: {
    form: {
      type: Object,
      default: {}
    }
  },
  components: { talkover },
  data () {
    return {
      activeKey: '1',
      columns,
      bulletinData: [],
      pagination: {
        current: 1,
        pageSize: 5,
        total: 0
      },
    }
  },
  mounted () {
    this.getDataList();
  },
  methods: {
    async getDataList () {
      const data = {
        requestId: this.form.requestId,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await bulletinBoard(data);
      if (result.code === 0) {
        this.bulletinData = result.data.records
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
