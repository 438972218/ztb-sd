<template>
  <div>
    <a-row style="padding-bottom: 5px">
      <a-col
        :span="24"
        v-if="form.requestStatusName !== '已归档' && !getUserInfo"
      >
        <a-space>
          <a-button type="primary" @click="authorization"> 授权 </a-button>
          <a-button type="primary" @click="handleClarify(1)">
            澄清报价
          </a-button>
          <!-- <a-button type="primary" @click="handleClarify(2)">
            要求最终报价
          </a-button> -->
          <a-button type="primary" @click="waiverAuthorization">
            放弃授权
          </a-button>
        </a-space>
      </a-col>
    </a-row>
    <a-row>
      <a-col :span="24">
        <a-table :columns="itemColumns" :data-source="data" :pagination="false">
          <template slot="seeBackBid" slot-scope="text, record">
            <a @click="handleSeeBackBid(record)">查看</a>
          </template>
        </a-table>
      </a-col>
    </a-row>
    <!-- 授权 -->
    <a-modal
      title="授权"
      :visible="authorizationVisible"
      width="50%"
      @ok="putbidVendorBatchUpdate"
      @cancel="authorizationVisible = false"
    >
      <a-row>
        <a-col :span="24">
          <a-table
            :row-selection="{
              type: 'radio',
              selectedRowKeys: authSelectedRowKeys,
              onChange: onSelectChange,
            }"
            :columns="columns"
            :data-source="data"
            :pagination="false"
          >
            <template slot="confirmedPrice" slot-scope="text, record">
              <a-input
                placeholder="请输入确定价格"
                :value="record.confirmedPrice"
                @change="
                  (e) =>
                    handleChange(e.target.value, record.id, 'confirmedPrice')
                "
              >
              </a-input>
            </template>
          </a-table>
        </a-col>
      </a-row>
    </a-modal>
    <!-- 澄清报价 -->
    <a-modal
      :title="title"
      :visible="clarifyVisible"
      width="50%"
      :confirmLoading="!clarifyVisible"
      @ok="postBidInvitationBack"
      @cancel="clarifyVisible = false"
    >
      <a-row>
        <a-col :span="24">
          <a-form-model
            ref="formClarify"
            :rules="clarifyRules"
            :model="formClarify"
            :labelCol="{ span: 4 }"
            :wrapperCol="{ span: 18 }"
            class="page-form"
          >
            <a-row :gutter="20">
              <a-col :span="24">
                <a-form-model-item
                  label="澄清报价截止时间"
                  prop="clarifyEndTime"
                >
                  <a-date-picker
                    :disabled-date="disabledEndDate"
                    format="YYYY-MM-DD HH:mm:ss"
                    valueFormat="YYYY-MM-DD HH:mm:ss"
                    style="width: 100%"
                    showTime
                    v-model="formClarify.clarifyEndTime"
                  />
                </a-form-model-item>
              </a-col>
            </a-row>
          </a-form-model>
        </a-col>
        <a-col :span="24">
          <a-table
            :row-selection="{
              selectedRowKeys: clarifySelectedRowKeys,
              onChange: clarifyonSelectChange,
            }"
            :columns="clarifyColumns"
            :data-source="data"
            :pagination="false"
          >
          </a-table>
        </a-col>
      </a-row>
    </a-modal>
    <!-- 回标 -->
    <a-modal
      title="回标详情"
      :visible="backBidVisible"
      width="65%"
      @ok="backBidVisible = false"
      @cancel="backBidVisible = false"
    >
      <a-row>
        <a-tabs v-model="activeKey" @change="callback">
          <a-tab-pane key="1" tab="资质标"> </a-tab-pane>
          <a-tab-pane key="2" tab="技术标"> </a-tab-pane>
          <a-tab-pane key="3" tab="商业标"> </a-tab-pane>
        </a-tabs>
        <a-col :span="24" v-if="activeKey !== '3'">
          <a-collapse v-model="backActiveKey">
            <a-collapse-panel
              v-for="(item, index) in backData"
              :key="index + ''"
              :header="
                index === 0
                  ? '当前轮次：' + item.round
                  : '历史轮次：' + item.round
              "
            >
              <a-table
                :columns="backColumns"
                :data-source="
                  item.bidVendorDetailTypeVOS
                    ? item.bidVendorDetailTypeVOS[0].bidVendorDetailVOS
                    : []
                "
                :pagination="false"
              >
                <template slot="explaination" slot-scope="text">
                  <span style="white-space: pre-wrap">{{ text }}</span>
                </template>
                <template slot="description" slot-scope="text">
                  <span style="white-space: pre-wrap">{{ text }}</span>
                </template>
              </a-table>
            </a-collapse-panel>
          </a-collapse>
        </a-col>
        <a-col :span="24" v-else>
          <a-collapse v-model="backActiveKey">
            <a-collapse-panel
              v-for="(item, index) in backData"
              :key="index + ''"
              :header="
                index === 0
                  ? '当前轮次：' + item.round
                  : '历史轮次：' + item.round
              "
            >
              <a-divider orientation="left"> 报价须知 </a-divider>
              <a-table
                :columns="backColumns"
                :data-source="
                  item.bidVendorDetailTypeVOS
                    ? item.bidVendorDetailTypeVOS.find(
                        (f) => f.type === '报价须知'
                      )
                      ? item.bidVendorDetailTypeVOS.find(
                          (f) => f.type === '报价须知'
                        ).bidVendorDetailVOS
                      : []
                    : []
                "
                :pagination="false"
              >
                <template slot="explaination" slot-scope="text">
                  <span style="white-space: pre-wrap">{{ text }}</span>
                </template>
                <template slot="description" slot-scope="text">
                  <span style="white-space: pre-wrap">{{ text }}</span>
                </template>
              </a-table>
              <a-divider orientation="left"> 国内费用 </a-divider>
              <a-table
                :columns="domesticColumns"
                :data-source="
                  item.bidVendorDetailTypeVOS
                    ? item.bidVendorDetailTypeVOS.find(
                        (f) => f.type === '国内费用'
                      )
                      ? item.bidVendorDetailTypeVOS.find(
                          (f) => f.type === '国内费用'
                        ).bidVendorDetailVOS
                      : []
                    : []
                "
                :pagination="false"
              >
                <template slot="explaination" slot-scope="text">
                  <span style="white-space: pre-wrap">{{ text }}</span>
                </template>
                <template slot="description" slot-scope="text">
                  <span style="white-space: pre-wrap">{{ text }}</span>
                </template>
              </a-table>
              <a-divider orientation="left"> 国外费用 </a-divider>
              <a-table
                :columns="internationalColumns"
                :data-source="
                  item.bidVendorDetailTypeVOS
                    ? item.bidVendorDetailTypeVOS.find(
                        (f) => f.type === '国外费用'
                      )
                      ? item.bidVendorDetailTypeVOS.find(
                          (f) => f.type === '国外费用'
                        ).bidVendorDetailVOS
                      : []
                    : []
                "
                :pagination="false"
              >
                <template slot="explaination" slot-scope="text">
                  <span style="white-space: pre-wrap">{{ text }}</span>
                </template>
                <template slot="description" slot-scope="text">
                  <span style="white-space: pre-wrap">{{ text }}</span>
                </template>
              </a-table>
            </a-collapse-panel>
          </a-collapse>
        </a-col>
      </a-row>
    </a-modal>
  </div>
</template>

<script>
import { bidVendorGetWithVendor, bidVendorBatchUpdate, bidInvitationBack, bidSheetAgree, bidVendorGetWaiveAuth, bidVendorDetailGetQueryRoundMap } from "@/services/source/bidding";
import { mapGetters } from "vuex";
import moment from 'moment'
const itemColumns = [
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
  },
  {
    title: '历史回标',
    dataIndex: 'seeBackBid',
    key: 'seeBackBid',
    scopedSlots: { customRender: 'seeBackBid' }
  }
]
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
const clarifyColumns = [
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
    title: '轮次',
    dataIndex: 'round',
    key: 'round',
  },
  {
    title: '答复时间',
    dataIndex: 'replyTime',
    key: 'replyTime',
    customRender: (text, record, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD') : ''
    },
  },
]
const backColumns = [
  {
    title: '标题',
    dataIndex: 'name',
    key: 'name',
  },
  // {
  //   title: '评论',
  //   dataIndex: 'explaination',
  //   key: 'explaination',
  // },
  {
    title: '详情',
    dataIndex: 'explaination',
    key: 'explaination',
    scopedSlots: { customRender: 'explaination' }
  },
]
// 国内费用
const domesticColumns = [
  {
    title: '标题',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '详情',
    dataIndex: 'explaination',
    key: 'explaination',
    scopedSlots: { customRender: 'explaination' },
  },
  {
    title: '单位',
    dataIndex: 'unit',
    key: 'unit',
    scopedSlots: { customRender: 'unit' },
  },
  {
    title: '数量/基准价格',
    dataIndex: 'quantity',
    key: 'quantity',
    scopedSlots: { customRender: 'quantity' },
  },
  {
    title: '单价',
    dataIndex: 'price',
    key: 'price',
    scopedSlots: { customRender: 'price' },
  },
  {
    title: '评论',
    dataIndex: 'description',
    key: 'description',
    scopedSlots: { customRender: 'description' },
  }
]
// 国外费用
const internationalColumns = [
  {
    title: '标题',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '详情',
    dataIndex: 'explaination',
    key: 'explaination',
    scopedSlots: { customRender: 'explaination' },
  },
  {
    title: '数量/基准价格',
    dataIndex: 'quantity',
    key: 'quantity',
    scopedSlots: { customRender: 'quantity' },
  },
  {
    title: '单价',
    dataIndex: 'price',
    key: 'price',
    scopedSlots: { customRender: 'price' },
  },
  {
    title: 'ETD',
    dataIndex: 'etd',
    key: 'etd',
    scopedSlots: { customRender: 'etd' },
  },
  {
    title: 'ETA',
    dataIndex: 'eta',
    key: 'eta',
    scopedSlots: { customRender: 'eta' },
  },
  {
    title: '船期(每周几)',
    dataIndex: 'shippingSchedule',
    key: 'shippingSchedule',
    scopedSlots: { customRender: 'shippingSchedule' },
  },
  {
    title: '船公司',
    dataIndex: 'shippingCompany',
    key: 'shippingCompany',
    scopedSlots: { customRender: 'shippingCompany' },
  },
  {
    title: '评论',
    dataIndex: 'description',
    key: 'description',
    scopedSlots: { customRender: 'description' },
  }
]
export default {
  name: "bidaward",
  props: {
    form: {
      type: Object,
      default: {}
    },
    getUserInfo: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      itemColumns,
      internationalColumns,
      domesticColumns,
      backColumns,
      title: '',
      activeKey: '1',
      backActiveKey: ['0'],
      backBidVisible: false,
      backData: [],
      authorizationVisible: false,
      clarifyVisible: false,
      authSelectedRowKeys: [],
      clarifySelectedRowKeys: [],
      authSelectedData: [],
      clarifySelectedData: [],
      columns,
      clarifyColumns,
      typeName: '',
      formClarify: {
        clarifyEndTime: ''
      },
      clarifyRules: {
        clarifyEndTime: [{ required: true, message: '请选择澄清报价截止时间', trigger: 'change' }],
      },
      data: [
      ],
      selectRow: null
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  mounted () {
    this.getDataList()
  },
  methods: {
    // 切换标签页
    async callback () {
      let type = ''
      switch (this.activeKey) {
        case '1':
          type = '资质标'
          break
        case '2':
          type = '技术标'
          break
        case '3':
          type = '商业标'
          break
      }
      const data = {
        currentPage: -1,
        bidSheetId: this.selectRow.bidSheetId,
        bidType: type,
        bidVendorId: this.selectRow.id
      }
      const result = await bidVendorDetailGetQueryRoundMap(data)
      if (result.code === 0) {
        this.backBidVisible = true
        this.backData = result.data ? result.data : []
      } else {
        this.$message.error(result.message)
      }
    },
    // 查看历史回标
    handleSeeBackBid (row) {
      this.backBidVisible = true
      this.activeKey = '1'
      this.selectRow = row
      this.callback()
    },
    // 放弃授权
    async waiverAuthorization () {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认放弃授权，确认后将不能再对招标单进行任何操作',
        async onOk () {
          const result = await bidVendorGetWaiveAuth(that.form.id)
          if (result.code === 0) {
            that.getDataList()
            that.handleRelease()
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    disabledEndDate (endValue) {
      const startValue = moment().format('YYYY-MM-DD')
      if (!endValue || !startValue) {
        return false
      }
      endValue = moment(endValue).format('YYYY-MM-DD')
      return startValue.valueOf() > endValue.valueOf()
    },
    // 流转
    async handleRelease () {
      const data = {
        id: this.form.id,
        requestId: this.form.requestId,
        userId: this.user.id
      }
      const result = await bidSheetAgree(data)
      if (result.code === 0) {
        this.form = Object.assign({}, result.data)
        this.form.bidEndTime = moment(parseInt(this.form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
        this.form.items = this.form.items ? this.form.items.split(',') : []
        this.$message.success('放弃成功')
      } else {
        this.$message.error(result.message)
      }
    },
    // 退回招标单
    postBidInvitationBack () {
      this.$refs.formClarify.validate(async valid => {
        if (valid) {
          const startValue = moment().format('YYYY-MM-DD HH:mm:ss')
          const endValue = moment(this.formClarify.clarifyEndTime).format('YYYY-MM-DD HH:mm:ss')
          if (startValue.valueOf() > endValue.valueOf()) {
            this.$message.warning(this.typeName + '截止时间需要大于当前时间')
            return
          }
          let data = {
            id: this.form.id,
            userId: this.user.id,
            requestId: this.form.requestId,
            flowToStatusId: '1430091118399266818',
            flowToUserId: this.user.id,
            mark: this.typeName,
            clarifyEndTime: this.formClarify.clarifyEndTime,
            bidVendorDTOS: []
          }
          this.data.forEach(item => {
            const detail = this.clarifySelectedData.find(f => f.id === item.id)
            let d = {
              id: item.id,
              againStatus: detail ? '受邀' : '未发送'
            }
            data.bidVendorDTOS.push(d)
          })
          if (this.typeName === '最终报价') {
            const that = this
            this.$confirm({
              title: '提示',
              content: '是否确定要求最终报价，将无法再次澄清报价',
              async onOk () {
                const result = await bidInvitationBack(data)
                if (result.code === 0) {
                  that.clarifyVisible = false
                  that.form = Object.assign({}, result.data)
                  that.form.bidEndTime = moment(parseInt(that.form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
                  that.form.items = that.form.items ? that.form.items.split(',') : []
                  that.$emit('refreshData', result.data)
                  that.$message.success('保存成功')
                } else {
                  that.$message.error(result.message)
                }
              },
              onCancel () { },
            });
          } else {
            const result = await bidInvitationBack(data)
            if (result.code === 0) {
              this.clarifyVisible = false
              this.form = Object.assign({}, result.data)
              this.form.bidEndTime = moment(parseInt(this.form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
              this.form.items = this.form.items ? this.form.items.split(',') : []
              this.$emit('refreshData', result.data)
              this.$message.success('保存成功')
            } else {
              this.$message.error(result.message)
            }
          }
        }
      })
    },
    // 澄清报价
    handleClarify (type) {
      this.getDataList()
      this.clarifySelectedRowKeys = []
      this.clarifySelectedData = []
      this.formClarify = {
        clarifyEndTime: ''
      }
      this.typeName = type === 1 ? '澄清报价' : '最终报价'
      this.title = type === 1 ? '澄清报价' : '最终报价'
      this.clarifyVisible = true
    },
    // 批量修改
    async putbidVendorBatchUpdate () {
      const dataList = []
      this.data.forEach(item => {
        const data = this.authSelectedData.find(f => f.id === item.id)
        if (data) {
          item.vendorStatus = '已授标'
        } else {
          item.vendorStatus = '未授标'
        }
        dataList.push(item)
      })
      const result = await bidVendorBatchUpdate(dataList)
      if (result.code === 0) {
        this.authorizationVisible = false
        this.$message.success('授权成功')
      } else {
        this.$message.error(result.message)
      }
    },
    handleChange (value, key, column) {
      let newData = [...this.data];
      const target = newData.filter(item => key === item.id)[0];
      if (target) {
        target[column] = value;
        this.data = newData;
      }
    },
    clarifyonSelectChange (selectedRowKeys, selectedRows) {
      this.clarifySelectedRowKeys = selectedRowKeys
      this.clarifySelectedData = selectedRows
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.authSelectedRowKeys = selectedRowKeys
      this.authSelectedData = selectedRows
    },
    // 授权
    authorization () {
      this.getDataList()
      this.authSelectedRowKeys = []
      this.authSelectedData = []
      this.authorizationVisible = true
    },
    // 查询招标投标方WithVendor
    async getDataList () {
      const data = {
        currentPage: -1,
        bidSheetId: this.form.id
      }
      const result = await bidVendorGetWithVendor(data)
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
