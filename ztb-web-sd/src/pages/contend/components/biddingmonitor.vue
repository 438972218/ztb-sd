<template>
  <div>
    <a-alert
      banner
      :showIcon="false"
      type="info"
      style="background-color: #409eff"
    >
      <a-row slot="message">
        <a-col :span="12" style="font-size: 25px; color: #ffff"
          >竞价监控器</a-col
        >
        <a-col :span="12" v-if="biddingData.paidStatus !== '截止'">
          <a-input-search
            :disabled="biddingData.paidStatus === '截止'"
            v-model="notice"
            placeholder="请输入全员广播"
            @search="sendNotice"
          >
            <a-button slot="enterButton"> 发送 </a-button>
          </a-input-search>
        </a-col>
      </a-row>
    </a-alert>
    <a-card :title="'竞价：' + biddingData.oddNumber + '-' + biddingData.title">
      <a slot="extra" href="#">
        <a-space>
          <a-button
            v-if="biddingData.paidStatus === '暂停'"
            :style="{ backgroundColor: '#f5222d', borderColor: '#f5222d' }"
            type="primary"
            >暂停</a-button
          >
          <a-button
            v-else-if="biddingData.paidStatus === '截止'"
            disabled
            type="primary"
            >截止</a-button
          >
          <a-button v-else :style="buttonStyle" type="primary">{{
            buttonTitle
          }}</a-button>
          <!-- <a-button
            type="primary"
            v-if="
              biddingData.paidStatus !== '截止' &&
              biddingData.paidStatus !== '待授权' &&
              biddingData.paidStatus !== '已归档'
            "
            >{{ getHour(countdown) }}</a-button
          > -->
          <!-- <a-button v-else>已截止</a-button> -->
          <!-- <a-button type="danger" v-if="biddingData.paidStatus === '暂停'"
            >暂停</a-button
          > -->
          <a-dropdown
            :disabled="
              biddingData.paidStatus === '截止' ||
              biddingData.paidStatus === '待授权' ||
              biddingData.paidStatus === '已归档' ||
              biddingData.paidStatus === '待竞价'
            "
            :trigger="['click']"
          >
            <a-button type="primary">操作</a-button>
            <a-menu disabled slot="overlay">
              <a-menu-item>
                <a
                  v-if="biddingData.paidStatus === '暂停'"
                  @click="handleRecover"
                  >恢复竞价</a
                >
                <a v-else @click="handlePause">暂停</a>
              </a-menu-item>
              <a-menu-item v-if="biddingData.paidStatus === '暂停'">
                <a @click="handleAnew">重新竞价</a>
              </a-menu-item>
              <a-menu-item v-if="biddingData.paidStatus === '暂停'">
                <a @click="handleTerminate">终止竞价</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
          <a-button @click="back()"> 返回 </a-button>
        </a-space>
      </a>
      <a-tabs v-model="activeKey" @change="handleTabChange">
        <a-tab-pane key="1" tab="摘要">
          <a-row :gutter="[20, 20]">
            <a-col :span="12">
              <span class="fontClass">竞价单号：</span>
              <span>{{ biddingData.oddNumber }}</span>
            </a-col>
            <a-col :span="12">
              <span class="fontClass">方向：</span>
              <span>{{ biddingData.paidDirection }}</span>
            </a-col>
            <a-col :span="12">
              <span class="fontClass">币种：</span>
              <span>{{ biddingData.currency }}</span>
            </a-col>
            <a-col :span="12">
              <span class="fontClass">出价结构：</span>
              <span>{{ biddingData.bidStructure }}</span>
            </a-col>
            <a-col :span="12">
              <span class="fontClass">延时间隔：</span>
              <span>{{ biddingData.delayInterval + '分钟' }}</span>
            </a-col>
            <a-col :span="12">
              <span class="fontClass">开始时间：</span>
              <span>{{ biddingData.offerStartTime }}</span>
            </a-col>
            <a-col :span="12">
              <span class="fontClass">结束时间：</span>
              <span>{{ biddingData.offerEndTime }}</span>
            </a-col>
            <a-col :span="12">
              <span class="fontClass">已持续时间：</span>
              <span>{{ timeOfDuration }}</span>
            </a-col>
            <a-col :span="24" v-for="item in list" :key="item.id">
              <a-divider orientation="left">{{ item.materialName }}</a-divider>
              <a-col :span="12">
                <span class="fontClass">起始总价：</span>
                <a
                  v-if="biddingData.startPriceRule === '区分'"
                  @click="showVendor"
                >
                  区分
                </a>
                <span v-else>{{ item.startPrice }}</span>
              </a-col>
              <a-col :span="12">
                <span class="fontClass">单位：</span>
                <span>{{ item.unit }}</span>
              </a-col>
              <a-col :span="12">
                <span class="fontClass">数量：</span>
                <span>{{ item.quantity }}</span>
              </a-col>
              <a-col :span="12">
                <span class="fontClass">触发价格：</span>
                <span>{{
                  item.triggerPrice +
                  (item.triggerprice === 1 ? '-已触发' : '-未达到')
                }}</span>
              </a-col>
              <a-col :span="12">
                <span class="fontClass">最小出价：</span>
                <span>{{ item.minimumBidRange }}</span>
              </a-col>
              <a-col :span="12">
                <span class="fontClass">最小出价规则：</span>
                <span>{{ item.minimumBidRule }}</span>
              </a-col>
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="2" tab="图表">
          <a-row>
            <a-col :span="24" v-for="item in list" :key="item.id">
              <a-divider orientation="left">{{ item.materialName }}</a-divider>
              <div
                :id="item.id"
                :style="{ width: '100%', height: '300px' }"
              ></div>
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="3" tab="供应商排名">
          <a-row :gutter="[20, 10]">
            <a-col
              :span="24"
              align="right"
              v-if="biddingData.paidStatus === '截止'"
              ><a-button type="primary" v-print="'#printMe'">
                打印
              </a-button></a-col
            >
            <div id="printMe">
              <a-col :span="24">
                <a-table
                  bordered
                  :pagination="false"
                  :columns="rankColumns"
                  childrenColumnName="child"
                  :data-source="allRank"
                >
                  <template slot="vendorMark" slot-scope="text, record">
                    <span>{{
                      biddingData.paidStatus !== '截止'
                        ? text
                        : record.vendorName
                    }}</span>
                  </template>
                </a-table>
              </a-col>
              <a-col :span="24" v-for="item in list" :key="item.id">
                <a-divider orientation="left">{{
                  item.materialName
                }}</a-divider>
                <a-table
                  bordered
                  :pagination="false"
                  :columns="columns"
                  childrenColumnName="child"
                  :data-source="ranking(item.id)"
                >
                  <template slot="vendorMark" slot-scope="text, record">
                    <span>{{
                      biddingData.paidStatus !== '截止'
                        ? text
                        : record.vendorName
                    }}</span>
                  </template>
                </a-table>
              </a-col>
            </div>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="4" tab="供应商状态">
          <a-table :columns="columns1" :data-source="vendorList">
            <template slot="state" slot-scope="text">
              <span
                ><a-badge :status="text === 1 ? 'success' : 'error'" />{{
                  text === 1 ? '在线' : '离线'
                }}</span
              >
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="5" tab="出价">
          <a-row>
            <a-col :span="24" v-for="item in list" :key="item.id">
              <a-divider orientation="left">{{ item.materialName }}</a-divider>
              <a-table
                childrenColumnName="child"
                :columns="offerColumns"
                :data-source="offerList.filter((f) => f.offerGoods === item.id)"
              >
                <template slot="vendorMark" slot-scope="text, record">
                  <span>{{
                    biddingData.paidStatus !== '截止' ? text : record.vendorName
                  }}</span>
                </template>
                <template slot="available" slot-scope="text">
                  <a-icon
                    v-if="text"
                    type="check-circle"
                    style="font-size: 20px; color: #67c23a"
                  />
                  <a-icon
                    v-else
                    type="close-circle"
                    style="font-size: 20px; color: red"
                  />
                </template>
              </a-table>
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </a-card>
    <!-- 模板列表 -->
    <a-modal
      title="重新开始竞价"
      :visible="visible"
      width="700px"
      @ok="saveItem"
      @cancel="visible = false"
    >
      <a-form-model
        :model="form"
        :rules="rules"
        ref="ruleForm"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="竞价开始时间" prop="offerStartTime">
              <a-date-picker
                format="YYYY-MM-DD HH:mm:ss"
                valueFormat="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                placeholder="请选择时间"
                :show-time="{
                  format: 'HH:mm',
                  defaultValue: timeDefaultValue,
                }"
                v-model="form.offerStartTime"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="竞价结束时间" prop="offerEndTime">
              <a-date-picker
                format="YYYY-MM-DD HH:mm:ss"
                valueFormat="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                placeholder="请选择时间"
                :show-time="{
                  format: 'HH:mm',
                  defaultValue: timeDefaultValue,
                }"
                v-model="form.offerEndTime"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <!-- 查看供应商 -->
    <a-modal
      title="供应商起始价格"
      :visible="visibleVendor"
      width="700px"
      @ok="visibleVendor = false"
      @cancel="visibleVendor = false"
    >
      <a-table :columns="columnsVendor" :data-source="vendorList"> </a-table>
    </a-modal>
  </div>
</template>

<script>
import { paidSheetShowByRequestIdGetRequest, getOfferRequest, getPaidMaterialPaidSheetById, getOfferLastRanking, paidVendorGetRequest, paidSheetPause, paidSheetRecover, paidSheetTerminate, paidSheetAnew } from "@/services/source/contend";
import { mapGetters } from "vuex";
import moment from 'moment'
import * as echarts from 'echarts'
import * as env from "../../../../config";
const rankColumns = [
  {
    title: '供应商',
    dataIndex: 'vendorMark',
    key: 'vendorMark',
    scopedSlots: { customRender: 'vendorMark' },
  },
  {
    title: '总排名',
    dataIndex: 'ranking',
    key: 'ranking',
  },
]
const columns = [
  {
    title: '排名',
    dataIndex: 'ranking',
    key: 'ranking',
  },
  {
    title: '供应商',
    dataIndex: 'vendorMark',
    key: 'vendorMark',
    scopedSlots: { customRender: 'vendorMark' },
  },
  {
    title: '最优出价(RMB)',
    dataIndex: 'money',
    key: 'money',
  },
  {
    title: '时间',
    dataIndex: 'offerTime',
    key: 'offerTime',
    customRender: (text, record, index) => {
      return moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss')
    },
  }
];
const columns1 = [
  {
    title: '状态',
    dataIndex: 'state',
    key: 'state',
    scopedSlots: { customRender: 'state' },
  },
  {
    title: '供应商名称',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  {
    title: '联系人',
    dataIndex: 'linkman',
    key: 'linkman'
  },
  {
    title: '联系电话',
    dataIndex: 'phone',
    key: 'phone'
  }
];
const offerColumns = [
  {
    title: '是否有效',
    dataIndex: 'available',
    key: 'available',
    scopedSlots: { customRender: 'available' },
  },
  {
    title: '供应商',
    dataIndex: 'vendorMark',
    key: 'vendorMark',
    scopedSlots: { customRender: 'vendorMark' },
  },
  {
    title: '出价（RMB）',
    dataIndex: 'money',
    key: 'money'
  },
  {
    title: "时间",
    key: 'offerTime',
    dataIndex: "offerTime",
    customRender: (text, record, index) => {
      return moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss')
    },
  },
  {
    title: "无效原因",
    key: 'description',
    dataIndex: "description",
    scopedSlots: { customRender: "description" },
  },
];
const columnsVendor = [
  {
    title: "供应商名称",
    dataIndex: "vendorName",
    key: "vendorName",
  },
  {
    title: "起始总价",
    dataIndex: "startPrice",
    key: "startPrice",
    scopedSlots: { customRender: "startPrice" },
  },
  // {
  //   title: "操作",
  //   dataIndex: "operation",
  //   key: "operation",
  //   scopedSlots: { customRender: "operation" },
  // },
]
export default {
  name: "biddingmonitor",
  computed: {
    ...mapGetters("account", ["user"]),
    ranking () {
      return function (id) {
        const data = this.rankingList.find(f => f.id === id)
        return data ? data.offerVOS : this.rankingList
      }
    },
    timeDefaultValue () {
      return moment(new Date().getHours() + ':' + new Date().getMinutes() + ':' + '00', 'HH:mm:ss')
    },
    getTime () {
      return function (val) {
        return moment(parseInt(val)).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    getHour () {
      return function (val) {
        if (val) {
          const D = Math.floor(parseInt(val) / (1000 * 60 * 60 * 24))
          const H = Math.floor((parseInt(val) % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
          const M = Math.floor((parseInt(val) % (1000 * 60 * 60)) / (1000 * 60))
          const S = Math.floor((parseInt(val) % (1000 * 60)) / 1000)
          return D > 0 ? D + '天' + H + '小时' + M + '分钟' + S + '秒' : H + '小时' + M + '分钟' + S + '秒'
        } else {
          return '00:00:00'
        }
      }
    },
  },
  data () {
    return {
      columnsVendor,
      buttonTitle: '',
      buttonStyle: {
        backgroundColor: '#faad14',
        borderColor: '#faad14'
      },
      form: {
        offerStartTime: '',
        offerEndTime: ''
      },
      rules: {
        offerStartTime: [{ required: true, message: '请选择竞价开始时间', trigger: 'change' }],
        offerEndTime: [{ required: true, message: '请选择竞价结束时间', trigger: 'change' }],
      },
      visible: false,
      rankColumns,
      allRank: [],
      countdown: '00:00:00',
      notice: '',
      activeKey: '1',
      list: [],
      path: env.WS_PROTOCOL + "://" + env.SEVER_IP + ":" + env.PORT + env.API_WS_PREFIX + "/imServer/",
      // path: "ws://10.20.241.54:9005/imServer/",
      socket: "",
      biddingData: {},
      offerList: [],
      rankingList: [],
      vendorList: [],
      timeOfDuration: '00:00:00',
      columns,
      columns1,
      message: '未达到',
      offerColumns,
      data: [
        {
          name: 'A',
          offer: '3000',
          time: '2021-08-09 11:00:00'
        }
      ],
      data1: [],
      data2: [
        {
          state: true,
          name: 'A',
          explain: '不符合起始报价规则',
          offer: '3000',
          time: '2021-08-09 12:12:08'
        },
        {
          state: false,
          name: 'B',
          explain: '不符合起始报价规则',
          offer: '6000',
          time: '2021-08-09 12:12:08'
        }
      ],
      timeMinutes: null,
      visibleVendor: false,
      timer: null,
      reconnectState: 0,
      lockReconnect: false,
      tt: null,

    }
  },
  mounted () {
    this.path = this.path + this.user.id
    this.biddingData = JSON.parse(sessionStorage.getItem('contendbidding'))
    if (this.biddingData.paidStatus !== '截止') {
      this.timer = setInterval(this.getSurplusStartTime, 1000)
    }
    // this.timeMinutes = setInterval(this.getAllInit, 1000)
    this.init()
    this.getItems()
    this.getPaidSheetShowByRequestIdGetRequest(JSON.parse(sessionStorage.getItem('contendbidding')))
  },
  methods: {
    // 每小时的20分钟 每小时的40分钟 每小时的60分钟
    getAllInit () {
      this.getItems()
      // if ((new Date().getMinutes() === 20 || new Date().getMinutes() === 40 || new Date().getMinutes() === 0) && new Date().getSeconds() === 0) {
      //   console.log('重置数据')
      //   this.getItems()
      // }
    },
    showVendor () {
      this.visibleVendor = true
    },
    // 获取剩余开始时间
    getSurplusStartTime () {
      const row = JSON.parse(sessionStorage.getItem('contendbidding'))
      const startTime = moment(row.offerStartTime)
      const nowTime = moment(new Date().getTime())
      const minute = startTime.diff(nowTime, 'minute')
      const second = startTime.diff(nowTime, 'second')
      const min = parseInt(second % 3600 / 60) < 10 ? '0' + parseInt(second % 3600 / 60) : parseInt(second % 3600 / 60)
      const sec = parseInt(second % 3600 % 60) < 10 ? '0' + parseInt(second % 3600 % 60) : parseInt(second % 3600 % 60)
      if (minute > -1 && second > -1) {
        this.buttonTitle = '剩余' + min + '分钟' + sec + '秒开始竞价'
        this.buttonStyle = {
          backgroundColor: '#faad14',
          borderColor: '#faad14'
        }
      } else {
        if (this.biddingData.paidStatus === '待竞价') {
          this.biddingData.paidStatus = '进行中'
        }
        const val = this.countdown
        const D = Math.floor(parseInt(val) / (1000 * 60 * 60 * 24))
        const H = Math.floor((parseInt(val) % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
        const M = Math.floor((parseInt(val) % (1000 * 60 * 60)) / (1000 * 60))
        const S = Math.floor((parseInt(val) % (1000 * 60)) / 1000)
        this.buttonTitle = '剩余' + (D > 0 ? D + '天' + H + '小时' + M + '分钟' + S + '秒' : H + '小时' + M + '分钟' + S + '秒') + '结束竞价'
        this.buttonStyle = {
          backgroundColor: '#67c23a',
          borderColor: '#67c23a'
        }
      }
    },
    // 刷新恢复数据
    async getPaidSheetShowByRequestIdGetRequest (row) {
      const result = await paidSheetShowByRequestIdGetRequest(row.requestId)
      if (result.code === 0) {
        this.biddingData = Object.assign({}, result.data)
        this.biddingData.offerStartTime = moment(parseInt(this.biddingData.offerStartTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        this.biddingData.offerEndTime = moment(parseInt(this.biddingData.offerEndTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
      } else {
        this.$message.error(result.message)
      }
    },
    // 保存重新竞价
    async saveItem () {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          const data = {
            id: this.biddingData.id,
            paidStatus: '待竞价',
            paidStatusMark: 0,
            offerStartTime: this.form.offerStartTime,
            offerEndTime: this.form.offerEndTime
          }
          const result = await paidSheetAnew(data)
          if (result.code === 0) {
            this.visible = false
            this.biddingData = Object.assign({}, result.data)
            this.biddingData.offerStartTime = moment(parseInt(this.biddingData.offerStartTime)).format(
              "YYYY-MM-DD HH:mm:ss"
            )
            this.biddingData.offerEndTime = moment(parseInt(this.biddingData.offerEndTime)).format(
              "YYYY-MM-DD HH:mm:ss"
            )
            this.countdown = '00:00:00'
            sessionStorage.setItem('contendbidding', JSON.stringify(this.biddingData))
            this.getOfferList()
            this.getSupplierRanking()
            this.$message.success('重新竞价成功')
          } else {
            this.$message.error(result.message)
          }
        }
      })
    },
    // 暂停竞价单
    async handlePause () {
      const result = await paidSheetPause(this.biddingData.id)
      if (result.code === 0) {
        this.biddingData = Object.assign({}, result.data)
        this.biddingData.offerStartTime = moment(parseInt(this.biddingData.offerStartTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        this.biddingData.offerEndTime = moment(parseInt(this.biddingData.offerEndTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        this.$message.success('暂停成功')
      } else {
        this.$message.error(result.message)
      }
    },
    // 恢复竞价单
    async handleRecover () {
      const result = await paidSheetRecover(this.biddingData.id)
      if (result.code === 0) {
        this.biddingData = Object.assign({}, result.data)
        this.biddingData.offerStartTime = moment(parseInt(this.biddingData.offerStartTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        this.biddingData.offerEndTime = moment(parseInt(this.biddingData.offerEndTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        this.$message.success('恢复成功')
      } else {
        this.$message.error(result.message)
      }
    },
    // 重新竞价
    handleAnew () {
      this.visible = true
      this.form = {
        offerStartTime: '',
        offerEndTime: ''
      }
    },
    // 终止竞价单
    async handleTerminate () {
      const result = await paidSheetTerminate(this.biddingData.id)
      if (result.code === 0) {
        this.biddingData = Object.assign({}, result.data)
        this.biddingData.offerStartTime = moment(parseInt(this.biddingData.offerStartTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        this.biddingData.offerEndTime = moment(parseInt(this.biddingData.offerEndTime)).format(
          "YYYY-MM-DD HH:mm:ss"
        )
        sessionStorage.setItem('contendbidding', JSON.stringify(this.biddingData))
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询供应商
    async getVendorList () {
      const data = {
        paidSheetId: this.biddingData.id,
        currentPage: -1
      }
      const result = await paidVendorGetRequest(data)
      if (result.code === 0) {
        this.vendorList = result.data ? result.data.records : []
      } else {
        this.$message.error(result.message)
      }
    },
    // tabs切换事件
    handleTabChange () {
      if (this.activeKey === '2') {
        this.$nextTick(() => {
          this.getLine()
        })
      } else if (this.activeKey === '3') {
        this.$nextTick(() => {
          this.getSupplierRanking()
        })
      } else if (this.activeKey === '5') {
        this.$nextTick(() => {
          this.getOfferList()
        })
      }
    },
    // 查询供应商排名
    async getSupplierRanking () {
      const data = {
        requestId: this.biddingData.requestId
      }
      const result = await getOfferLastRanking(data)
      if (result.code === 0) {
        if (result.data) {
          let dataList = []
          this.list.forEach(item => {
            const rank = {
              id: item.id,
              offerVOS: result.data.singleRanking[item.id]
            }
            dataList.push(rank)
          })
          this.rankingList = dataList
          this.allRank = result.data.totalRanking
        } else {
          this.rankingList = []
          this.allRank = []
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 出价折线图
    async getLine () {
      const list = this.offerList.length > 0 ? this.offerList : []
      this.list.forEach(item => {
        const offerlist = list.filter((f) => f.offerGoods === item.id)
        let vendorList = offerlist.map(o => { return this.biddingData.paidStatus !== '截止' ? o.vendorMark : o.vendorName })
        vendorList = vendorList.filter((item, index) => {
          return vendorList.indexOf(item) === index
        })
        let seriesList = []
        // let times = []
        vendorList.forEach(i => {
          const details = offerlist.filter(f => this.biddingData.paidStatus !== '截止' ? (f.vendorMark === i && f.available === 1) : (f.vendorName === i && f.available === 1))
          let dataList = []
          // details.forEach(f => { times.push(moment(parseInt(f.offerTime)).format('YYYY-MM-DD HH:mm:ss')) })
          // times = details.map(f => { return moment(parseInt(f.offerTime)).format('YYYY-MM-DD HH:mm:ss') })
          details.forEach(f => {
            dataList.push({
              name: moment(parseInt(f.offerTime)).format('YYYY-MM-DD HH:mm:ss'),
              value: [moment(parseInt(f.offerTime)).format('YYYY-MM-DD HH:mm:ss'), f.money]
            })
          })
          const d = {
            name: i,
            type: 'line',
            data: dataList
          }
          seriesList.push(d)
        })
        var chartDom = document.getElementById(item.id)
        var myChart = echarts.init(chartDom);
        myChart.clear()
        var option;
        option = {
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: vendorList
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'time',
            splitLine: {
              show: false
            },
            // min: moment(this.biddingData.offerStartTime), // 开始时间
            // max: moment(this.biddingData.offerEndTime), // 结束时间
            axisLabel: {
              rotate: 40,
              // interval: 10, // 如果设置为 1，表示『隔一个标签显示一个标签』，如果值为 2，表示隔两个标签显示一个标签，以此类推。
              formatter: function (value) {
                return moment(value).format('YYYY-MM-DD HH:mm')
              }
            }
          },
          yAxis: {
            type: 'value'
          },
          series: seriesList
        };
        option && myChart.setOption(option);
      })
    },
    // 竞价物品查询
    async getItems () {
      this.list = []
      const result = await getPaidMaterialPaidSheetById(this.biddingData.id)
      if (result.code === 0) {
        if (result.data && result.data.length > 0) {
          result.data.forEach(item => {
            let data = Object.assign({ finalbid: '', ranking: '', numbner: '', triggerprice: '', loading: false }, item)
            this.list.push(data)
          })
          this.getOfferList()
          this.getSupplierRanking()
          this.getVendorList()
        }
      }
    },
    // 查询出价记录
    async getOfferList () {
      const data = {
        currentPage: -1,
        requestId: this.biddingData.requestId,
        orderField: 'created_time',
        orderType: 'asc'
      }
      const result = await getOfferRequest(data)
      if (result.code === 0) {
        this.offerList = result.data ? result.data.records : []
        this.list.forEach((item, index) => {
          const detail = this.offerList.filter(f => item.id === f.offerGoods && f.available === 1)
          if (detail.length > 0) {
            this.list[index].triggerprice = detail[0].triggerThreshold
          }
        })
        this.getLine()
      } else {
        this.$message.error(result.message)
      }
    },
    init () {
      if (typeof (WebSocket) === "undefined") {
        alert("您的浏览器不支持socket")
      } else {
        console.log(this.path)
        // 实例化socket
        this.socket = new WebSocket(this.path)
        // 监听socket连接
        this.socket.onopen = this.open
        // 监听socket错误信息
        this.socket.onerror = this.error
        // 监听socket消息
        this.socket.onmessage = this.getMessage
        // 关闭socket消息
        this.socket.onclose = this.websocketclose
      }
    },
    open () {
      clearTimeout(this.tt)
      console.log("socket连接成功")
    },
    error () {
      this.reconnect()
      console.log("连接错误")
    },
    getMessage (msg) {
      let data = msg.data ? JSON.parse(msg.data) : null
      if (data) {
        // type === -1 刷新页面数据
        if (data.type === -1) {
          this.getAllInit()
        }
        if (data.type === 1 && data.message.requestId === this.biddingData.requestId) {
          // 出价记录
          this.offerList.unshift(data.message)
          if (data.message.available === 1) {
            const index = this.list.findIndex(f => f.id === data.message.offerGoods)
            this.list[index].triggerprice = data.message.triggerThreshold
            this.getLine()
          }
        }
        if (data.type === 2) {
          if (typeof (data.message.length) === 'number') {
            // 供应商状态
            data.message.forEach(item => {
              if (item.requestId === this.biddingData.requestId) {
                const newData = [...this.vendorList];
                const target = newData.filter(f => f.vendorId === item.vendorId)[0];
                if (target) {
                  target.state = item.state;
                  this.vendorList = newData;
                }
              }
            })
          } else {
            if (data.message.requestId === this.biddingData.requestId && data.message.paidStatus === 4) {
              this.biddingData.paidStatus = '截止'
              this.reconnectState = 1
              this.socket.close()
              clearTimeout(this.tt)
              clearInterval(this.timer);
            }
          }
        }
        if (data.type === 3 && data.message.requestId === this.biddingData.requestId) {
          if (Number(data.message.countdown) > 0) {
            // 竞价时间
            const timeOfDuration = data.message.timeOfDuration ? moment(parseInt(data.message.timeOfDuration)).format('HH:mm:ss') : '00:00:00'
            const countdown = data.message.countdown
            this.timeOfDuration = timeOfDuration
            this.countdown = countdown
          }
        }
        if (data.type === 5) {
          if (data.message.requestId === this.biddingData.requestId) {
            let dataList = []
            this.list.forEach(item => {
              const rank = {
                id: item.id,
                offerVOS: data.message.singleRanking[item.id]
              }
              dataList.push(rank)
            })
            this.rankingList = dataList
            this.allRank = data.message.totalRanking
          }
        }
      }
    },
    sendNotice () {
      const data = {
        type: 2,
        message: {
          type: 2,
          requestId: this.biddingData.requestId,
          message: this.notice
        },
      }
      this.socket.send(JSON.stringify(data))
      this.$message.success('已发送')
      this.notice = ''
    },
    websocketclose () {
      console.log("socket已经关闭")
      if (this.reconnectState === 0) this.reconnect()
    },
    reconnect () {
      const that = this
      if (that.lockReconnect) {
        return;
      };
      that.lockReconnect = true;
      console.log('正在重连')
      //没连接上会一直重连，设置延迟避免请求过多
      that.tt = setTimeout(function () {
        that.init();
        that.lockReconnect = false;
      }, 2000);
    },
    // 返回上一页
    back () {
      this.$router.go(-1)
    },
  },
  beforeDestroy () {
    // 销毁监听
    this.reconnectState = 1
    this.socket.close()
    clearTimeout(this.tt)
    clearInterval(this.timer);
    // clearInterval(this.timeMinutes);
  }
};
</script>

<style lang="less" scoped>
.fontClass {
  font-weight: 550;
}
</style>
<style>
@page {
  size: auto;
  margin: 3mm;
}
</style>
