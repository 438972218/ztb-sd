<template>
  <div>
    <a-row>
      <a-col :span="24">
        <a-table :columns="columns" :data-source="data" :pagination="false">
        </a-table>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { bidVendorGetRequest } from "@/services/source/bidding";
import { mapGetters } from "vuex";
const columns = [
  {
    title: '供应商',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  {
    title: '总价',
    dataIndex: 'totalPrice',
    key: 'totalPrice',
  },
  {
    title: '总分',
    dataIndex: 'totalScore',
    key: 'totalScore',
  },
  {
    title: '确定价格',
    dataIndex: 'confirmedPrice',
    key: 'confirmedPrice',
    scopedSlots: { customRender: 'confirmedPrice' }
  },
  {
    title: '轮次',
    dataIndex: 'round',
    key: 'round',
  },
  {
    title: '授权状态',
    dataIndex: 'vendorStatus',
    key: 'vendorStatus',
  }
]
export default {
  name: "bidaward",
  props: {
    form: {
      type: Object,
      default: {}
    }
  },
  data () {
    return {
      columns,
      data: []
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  mounted () {
    console.log(123)
    console.log(this.form)
    this.getDataList()
  },
  methods: {
    // 查询招标投标方WithVendor
    async getDataList () {
      const data = {
        currentPage: -1,
        bidSheetId: this.form.id,
        vendorUserId: this.user.id
      }
      const result = await bidVendorGetRequest(data)
      if (result.code === 0) {
        this.data = result.data.records ? result.data.records : []
      } else {
        this.$message.error(result.message)
      }
    },
  }
};
</script>

<style lang="less" scoped>
</style>
