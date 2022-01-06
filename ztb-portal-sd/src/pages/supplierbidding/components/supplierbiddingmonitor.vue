<template>
  <div>
    <a-card>
      <a-row :gutter="20">
        <a-col :span="24" style="padding-bottom: 10px">
          <a-alert type="success">
            <span slot="message" style="font-size: 22px"
              >广播信息：<span :style="styleObject"
                ><a-icon type="sound" />{{
                  noticeMessage ? noticeMessage : '暂无广播信息'
                }}</span
              ></span
            >
          </a-alert>
        </a-col>
        <a-col style="font-size: 22px; font-weight: 550" :span="8">
          {{ biddingData.oddNumber }}-{{ biddingData.title }}
        </a-col>
        <a-col
          style="font-size: 22px; font-weight: 550"
          :span="6"
          v-if="biddingData.vendorMonitorRules === '显示总排名'"
        >
          {{ '总排名：' + allRank }}
        </a-col>
        <a-col
          style="font-size: 22px; font-weight: 550"
          :span="5"
          align="right"
        >
          状态：<span style="color: #248c00">{{ biddingData.paidStatus }}</span>
        </a-col>
        <a-col
          :span="biddingData.vendorMonitorRules === '显示总排名' ? 5 : 11"
          align="right"
        >
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
        </a-col>
      </a-row>
    </a-card>
    <a-card>
      <a-table
        rowKey="id"
        :expandedRowKeys.sync="curExpandedRowKeys"
        :columns="itemColumns"
        :pagination="false"
        :data-source="list"
        :loading="loading"
      >
        <template slot="numbner" slot-scope="text, record, index">
          <a-col :span="15">
            <a-input-number
              style="width: 100%"
              :value="text"
              :min="0.01"
              :formatter="(v) => limitDecimalsF(v)"
              :parser="(v) => limitDecimalsP(v)"
              :disabled="
                (record.childPaidMaterialVOS &&
                  record.childPaidMaterialVOS.length > 0) ||
                biddingData.paidStatus === '截止' ||
                biddingData.paidStatus === '暂停' ||
                biddingData.paidStatus === '待竞价'
              "
              :placeholder="
                record.childPaidMaterialVOS &&
                record.childPaidMaterialVOS.length > 0
                  ? ''
                  : '请输入总价'
              "
              @change="(e) => inputChange(e, record.id, index)"
              @pressEnter="send(record, index)"
            />
            <!-- <a-input
              :value="text"
              :disabled="
                (record.childPaidMaterialVOS &&
                  record.childPaidMaterialVOS.length > 0) ||
                biddingData.paidStatus === '截止' ||
                biddingData.paidStatus === '暂停'
              "
              :placeholder="
                record.childPaidMaterialVOS &&
                record.childPaidMaterialVOS.length > 0
                  ? ''
                  : '请输入总价'
              "
              @change="(e) => inputChange(e.target.value, record.id, index)"
            >
            </a-input> -->
          </a-col>
          <a-col
            :span="9"
            v-if="
              biddingData.paidStatus !== '截止' &&
              biddingData.paidStatus !== '暂停' &&
              biddingData.paidStatus !== '待竞价'
            "
          >
            <a-button @click="send(record, index)" type="primary">
              出价
            </a-button>
          </a-col>
          <!-- <a-input-search
            :value="text"
            :disabled="
              record.childPaidMaterialVOS &&
              record.childPaidMaterialVOS.length > 0
            "
            :placeholder="
              record.childPaidMaterialVOS &&
              record.childPaidMaterialVOS.length > 0
                ? ''
                : '请输入单价'
            "
            @change="(e) => inputChange(e.target.value, record.id, index)"
            @search="send(record)"
          >
            
          </a-input-search> -->
        </template>
        <template slot="triggerprice" slot-scope="text">
          <div :class="text === 1 ? 'circle_success' : 'circle_error'"></div>
          <!-- <a-icon
            :type="text === 1 ? 'check-circle' : 'close-circle'"
            :class="text === 1 ? 'checkCircle' : 'closeCircle'"
          /> -->
        </template>
        <div
          slot="expandedRowRender"
          slot-scope="record, index"
          style="margin: 0"
        >
          <a-row :gutter="20">
            <a-col :span="12">
              <a-card size="small">
                <a-descriptions :column="2">
                  <a-descriptions-item label="起始总价">
                    {{ record.startPrice }}
                  </a-descriptions-item>
                  <a-descriptions-item label="币种">
                    {{ biddingData.currency }}
                  </a-descriptions-item>
                  <a-descriptions-item label="方向">
                    {{ biddingData.paidDirection }}
                  </a-descriptions-item>
                  <a-descriptions-item
                    label="数量"
                    v-if="!record.childPaidMaterialVOS"
                  >
                    {{ record.quantity }}
                  </a-descriptions-item>
                  <a-descriptions-item
                    label="单位"
                    v-if="!record.childPaidMaterialVOS"
                  >
                    {{ record.unit }}
                  </a-descriptions-item>
                  <a-descriptions-item label="触发价格">
                    {{ record.triggerprice === 1 ? '已触发' : '未达到' }}
                  </a-descriptions-item>
                  <a-descriptions-item label="最小出价规则">
                    {{ record.minimumBidRule }}
                  </a-descriptions-item>
                  <a-descriptions-item label="最小出价幅度">
                    {{ record.minimumBidRange }}
                  </a-descriptions-item>
                  <a-descriptions-item label="触发价格后最小出价配置">
                    {{ record.triggerMinimumBidRange }}
                  </a-descriptions-item>
                </a-descriptions>
                <a-table
                  style="padding-top: 10px"
                  v-if="
                    record.childPaidMaterialVOS &&
                    record.childPaidMaterialVOS.length > 0
                  "
                  size="small"
                  rowKey="id"
                  bordered
                  :pagination="false"
                  :columns="childColumns"
                  :data-source="record.childPaidMaterialVOS"
                >
                  <template slot="money" slot-scope="text, record">
                    <a-input
                      style="margin: -5px 0"
                      :value="text"
                      @change="
                        (e) => handleChange(e.target.value, record.id, index)
                      "
                    />
                  </template>
                </a-table>
              </a-card>
            </a-col>
            <a-col :span="12">
              <a-table
                id="antdTable"
                :rowClassName="setRowClassName"
                rowKey="key"
                childrenColumnName="child"
                :scroll="{ y: 230 }"
                size="small"
                :columns="columns"
                :pagination="false"
                :data-source="
                  offerList.filter((f) => f.offerGoods === record.id)
                "
              >
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
        </div>
      </a-table>
    </a-card>
  </div>
</template>
<script>
import { paidSheetShowByRequestIdAndVendor, paidItemsGetPaidSheetId, getOfferRequest, paidMaterialLastRankingByVendor, paidVendorGetRequest } from "@/services/source/contend";
import { mapGetters } from "vuex";
import moment from 'moment'
import * as env from "../../../../config";
const itemColumns = [
  {
    title: "品类名称",
    key: 'materialName',
    dataIndex: "materialName",
    scopedSlots: { customRender: "materialName" },
  },
  {
    title: "最后出价",
    key: 'finalbid',
    dataIndex: "finalbid",
    scopedSlots: { customRender: "finalbid" },
  },
  {
    title: "排名",
    key: 'ranking',
    dataIndex: "ranking",
    scopedSlots: { customRender: "ranking" },
  },
  {
    title: "触发价格",
    key: 'triggerprice',
    dataIndex: "triggerprice",
    scopedSlots: { customRender: "triggerprice" },
  },
  {
    title: "竞价",
    width: 250,
    key: 'numbner',
    dataIndex: "numbner",
    scopedSlots: { customRender: "numbner" },
  },
]
const columns = [
  {
    title: "状态",
    key: 'available',
    dataIndex: "available",
    scopedSlots: { customRender: "available" },
  },
  {
    title: "我的出价",
    key: 'money',
    dataIndex: "money",
    scopedSlots: { customRender: "money" },
  },
  {
    title: "时间",
    key: 'offerTime',
    dataIndex: "offerTime",
    customRender: (text, record, index) => {
      console.log(text)
      return moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss')
    },
  },
  {
    title: "无效原因",
    key: 'description',
    dataIndex: "description",
    scopedSlots: { customRender: "description" },
  },
]
const childColumns = [
  {
    title: "物品名称",
    key: 'materialName',
    dataIndex: "materialName",
    scopedSlots: { customRender: "materialName" },
  },
  {
    title: "单位",
    key: 'unit',
    dataIndex: "unit",
    scopedSlots: { customRender: "unit" },
  },
  {
    title: "数量",
    key: 'quantity',
    dataIndex: "quantity",
    scopedSlots: { customRender: "quantity" },
  },
  {
    title: "出价",
    key: 'money',
    width: 120,
    dataIndex: "money",
    scopedSlots: { customRender: "money" },
  },
  {
    title: "小计",
    width: 120,
    key: 'count',
    dataIndex: "count",
    scopedSlots: { customRender: "count" },
  },
]
export default {
  name: "supplierbiddingmonitor",
  components: {},
  computed: {
    ...mapGetters("account", ["user"]),
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
      curExpandedRowKeys: [],
      buttonTitle: '',
      buttonStyle: {
        backgroundColor: '#faad14',
        borderColor: '#faad14'
      },
      styleObject: {
        color: 'red'
      },
      colorType: 1,
      loading: false,
      itemColumns,
      formInline: {
        code: '2021083801',
        finalbid: '95RMB',
        ranking: '1',
        numbner: 2222
      },
      biddingStatus: '',
      biddingData: {},
      list: [],
      columns,
      childColumns,
      noticeMessage: '',
      data: [
      ],
      path: env.WS_PROTOCOL + "://" + env.SEVER_IP + ":" + env.PORT + env.API_WS_PREFIX + "/imServer/",
      // path: "ws://10.20.241.54:9005/imServer/",
      socket: "",
      offerList: [],
      sendState: false,
      sendData: {},
      countdown: '00:00:00', // 剩余时间
      allRank: '',
      timer: '',
      styleSetInterval: '',
      lockReconnect: false,
      tt: null,
      reconnectState: 0
    };
  },
  // beforeRouteLeave (to, from, next) {
  //   if (this.socket) {
  //     this.socket.onclose = this.close()
  //   }
  //   next()
  // },
  created () {
  },
  mounted () {
    const form = JSON.parse(sessionStorage.getItem('biddingData'))
    if (form.paidStatus !== '截止') {
      this.timer = setInterval(this.getSurplusStartTime, 1000)
    }
    this.styleSetInterval = setInterval(this.getStyleObject, 1000)
    window.onbeforeunload = function () {
      this.socket.close
    }
    this.path = this.path + this.user.id
    this.getPaidSheetShowByRequestIdAndVendor(form)
  },
  watch: {
    $route (to, from) {
      clearInterval(this.timer);
      clearInterval(this.styleSetInterval);
    }
  },
  methods: {
    // 获取剩余开始时间
    getSurplusStartTime () {
      const row = JSON.parse(sessionStorage.getItem('biddingData'))
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
    getStyleObject () {
      this.colorType = this.colorType === 1 ? 2 : 1
      return this.colorType === 1 ? this.styleObject = { color: 'red' } : this.styleObject = {}
    },
    limitDecimalsF (value) {
      let reg = /^(-)*(\d+)\.(\d\d).*$/; return `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',').replace(reg, '$1$2.$3');
    },
    limitDecimalsP (value) {
      let reg = /^(-)*(\d+)\.(\d\d).*$/;
      return value.replace(/￥\s?|(,*)/g, '').replace(reg, '$1$2.$3');
    },
    async getPaidSheetShowByRequestIdAndVendor (row) {
      const data = {
        requestId: row.requestId,
        vendorUserId: this.user.id
      }
      const result = await paidSheetShowByRequestIdAndVendor(data)
      if (result.code === 0) {
        console.log(result)
        this.biddingData = Object.assign({}, result.data)
        this.getDataList()
        this.getOfferList()
        this.init()
        this.getLastRankingByVendor()
      } else {
        this.$message.error(result.message)
      }
    },
    async getPaidVendor () {
      const data = {
        paidSheetId: this.biddingData.id,
        vendorUserId: this.user.id,
        currentPage: -1
      }
      const result = await paidVendorGetRequest(data)
      if (result.code === 0) {
        this.list.forEach((item, index) => {
          this.list[index].startPrice = result.data.records && result.data.records.length > 0 ? result.data.records[0].startPrice : ''
        })
      } else {
        this.$message.error(result.message)
      }
    },
    // 根据供应商用户ID查询出价排名
    async getLastRankingByVendor () {
      const data = {
        requestId: this.biddingData.requestId,
        vendorUserId: this.user.id
      }
      const result = await paidMaterialLastRankingByVendor(data)
      if (result.code === 0) {
        if (result.data) {
          console.log(result.data)
          this.list.forEach((item, index) => {
            this.list[index].ranking = result.data.singleRanking[item.id][0].ranking
            this.list[index].finalbid = result.data.singleRanking[item.id][0].money
          })
          this.allRank = result.data.totalRanking[0].ranking
        }
      } else {
        this.$message.error(result.message)
      }
    },
    handleChange (value, key, index) {
      const newData = [...this.list[index].childPaidMaterialVOS];
      const target = newData.filter(item => key === item.id)[0];
      if (target) {
        target['money'] = value;
        target['count'] = target.quantity ? value * target.quantity : ''
        this.list[index].childPaidMaterialVOS = newData;
        this.list[index].numbner = ''
        // this.list[index].finalbid = ''
        newData.forEach((item, i) => {
          this.list[index].numbner = newData[i].count ? (this.list[index].numbner ? Number(this.list[index].numbner) + Number(newData[i].count) : Number(newData[i].count)) : this.list[index].numbner
          // this.list[index].finalbid = this.list[index].numbner
        })
      }
    },
    // 查询出价记录
    async getOfferList () {
      const data = {
        currentPage: -1,
        requestId: this.biddingData.requestId,
        offerUser: this.user.userName
      }
      const result = await getOfferRequest(data)
      if (result.code === 0) {
        this.offerList = result.data.records ? result.data.records : []
        this.list.forEach((item, index) => {
          const detail = this.offerList.filter(f => item.id === f.offerGoods && f.available === 1)
          if (detail.length > 0) {
            this.list[index].triggerprice = detail[0].triggerThreshold
          }
        })
      } else {
        this.$message.error(result.message)
      }
    },
    // 输入框发生改变
    inputChange (e, id, index) {
      const newData = [...this.list];
      const target = newData.filter(item => id === item.id)[0];
      if (target) {
        // target.finalbid = e;
        target.numbner = e;
        this.list = newData;
      }
      // this.list[index].finalbid = e
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
      // 竞价时间
      if (data) {
        if (data.type === 1) {
          const index = this.list.findIndex(f => f.id === this.sendData.message.offerGoods)
          if (index > -1) {
            this.list[index].ranking = data ? (data.message.ranking === 0 ? '' : data.message.ranking) : ''
            this.loading = false
            if (data) this.offerList.unshift(data.message)
            if (data.message.available === 1) {
              this.list[index].finalbid = data.message.money
              this.list[index].triggerprice = data.message.triggerThreshold
            }
          }
        }
        if (data.type === 2 && data.message.requestId === this.biddingData.requestId) {
          if (data.message.paidStatus !== -1) {
            this.biddingData.paidStatus = data.message.paidStatus === 2 ? '暂停' : '截止'
            if (data.message.paidStatus === 4) {
              this.reconnectState = 1
              this.socket.close()
              clearTimeout(this.tt)
              clearInterval(this.timer);
              clearInterval(this.styleSetInterval);
            }
          }
        }
        if (data.type === 3 && data.message.requestId === this.biddingData.requestId) {
          // 竞价时间
          if (Number(data.message.countdown) > 0) {
            this.countdown = data.message.countdown
            this.biddingData.paidStatus = '进行中'
          }
        }
        if (data.type === 4) {
          this.noticeMessage = data.message.message
        }
        if (data.type === 5 && data.message.requestId === this.biddingData.requestId) {
          this.list.forEach((item, index) => {
            if (data.message.singleRanking && data.message.singleRanking[item.id]) {
              const d = data.message.singleRanking[item.id].find(f => f.offerGoods === item.id)
              if (d && d.ranking !== 0) this.list[index].ranking = d.ranking
            }
          })
          this.allRank = data.message.totalRanking[0].ranking
        }
      }
    },
    send (row, index) {
      const that = this
      let colorState = true
      const price = !row.childPaidMaterialVOS && row.quantity ? (Number(row.numbner) / Number(row.quantity)).toFixed(2) : row.numbner
      let title = '本次出价的总价是' + row.numbner.toFixed(2) + '， 单价是' + price + '，请确认是否提交'
      // 判断出价时首次报价比起始价格低于/高于5倍，再次报价时跟上一次报价进行比较，报价高于/低于上一次报价5倍时给出提示
      // 判断是否为首次出价
      if (this.offerList.length > 0 && this.offerList.filter((f) => f.offerGoods === row.id && f.available === 1).length > 0) {
        const offer = this.offerList.filter((f) => f.offerGoods === row.id && f.available === 1)
        // 首次出价，与上一次报价做比较
        if ((row.numbner / Number(offer[0].money)) > 5) {
          // 首次报价比上一次报价高于5倍
          colorState = false
          title = '本次出价的总价是' + row.numbner.toFixed(2) + '，远高于上一次报价， 单价是' + price + '，请确认是否提交'
        }
        if ((Number(offer[0].money) / row.numbner) > 5) {
          // 首次报价比上一次报价小于5倍
          colorState = false
          title = '本次出价的总价是' + row.numbner.toFixed(2) + '，远低于上一次报价， 单价是' + price + '，请确认是否提交'
        }
      } else {
        // 首次出价，与起始价格做比较
        if ((row.numbner / row.startPrice) > 5) {
          // 首次报价比起始价格高于5倍
          colorState = false
          title = '本次出价的总价是' + row.numbner.toFixed(2) + '，远高于起始总价， 单价是' + price + '，请确认是否提交'
        }
        if ((row.startPrice / row.numbner) > 5) {
          // 首次报价比起始价格小于5倍
          colorState = false
          title = '本次出价的总价是' + row.numbner.toFixed(2) + '，远低于起始总价， 单价是' + price + '，请确认是否提交'
        }
      }
      this.$confirm({
        title: '提示',
        content: colorState ? title : h => <span style="color: red">{title}</span>,
        async onOk () {
          that.loading = true
          let childrens = []
          if (row.childPaidMaterialVOS) {
            row.childPaidMaterialVOS.forEach(item => {
              childrens.push({
                money: item.count, // 出价金额
                requestId: that.biddingData.requestId, // 表单ID
                offerGoods: item.id, // 出价物标识
                offerUser: that.user.userName, // 出价人
              })
            })
          }
          that.sendState = true
          that.sendData = {
            type: 1,
            message: {
              money: row.numbner, // 出价金额
              requestId: that.biddingData.requestId, // 表单ID
              offerGoods: row.id, // 出价物标识
              offerUser: that.user.userName, // 出价人
              children: childrens // 出价记录
            },
          }
          that.socket.send(JSON.stringify(that.sendData))
          console.log(index)
          that.list[index].numbner = ''
        },
        onCancel () { },
      });
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
    // 获取表格数据
    async getDataList () {
      this.list = []
      this.curExpandedRowKeys = []
      const result = await paidItemsGetPaidSheetId(this.biddingData.id)
      if (result.code === 0) {
        result.data.forEach(item => {
          let data = Object.assign({ finalbid: '', ranking: '', numbner: '', triggerprice: '', loading: false }, item)
          this.curExpandedRowKeys.push(data.id)
          this.list.push(data)
        })
        if (this.biddingData.startPriceRule === '区分') this.getPaidVendor()
      } else {
        this.$message.error(result.message)
      }
    },
    // 提交
    onSumbit () { },
    // 表格行设置颜色
    setRowClassName (record) {
      return record.state ? 'clickRowStyl' : ''
    }
  },
  beforeDestroy () {
    // 销毁监听
    this.reconnectState = 1
    this.socket.close()
    clearTimeout(this.tt)
    clearInterval(this.timer);
    clearInterval(this.styleSetInterval);
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
#antdTable {
  ::v-deep .clickRowStyl {
    background: #f52222c2;
  }
}
.checkCircle {
  font-size: 20px;
  color: #67c23a;
}
.closeCircle {
  font-size: 20px;
  color: red;
}
.circle_error {
  width: 20px;
  height: 20px;
  background-color: #a60014;
  border-radius: 50%;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
}
.circle_success {
  width: 20px;
  height: 20px;
  background-color: rgb(82, 196, 26);
  border-radius: 50%;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
}
</style>