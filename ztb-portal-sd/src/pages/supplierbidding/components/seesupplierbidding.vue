<template>
  <div>
    <a-card class="card-top">
      <a-alert
        style="margin-bottom: 10px"
        :message="
          '竞价：' + (form.oddNumber ? form.oddNumber : '') + '-' + form.title
        "
        type="info"
      >
        <a-row slot="description">
          <a-col :span="24" align="right">
            <span style="color: #248c00; font-size: 22px"
              ><a @click="seeMonitor()" style="padding-right: 10px">
                <img alt="svg" src="@/assets/img/bidding.svg" width="25px" /></a
              >{{ form.requestStatusName }}</span
            >
          </a-col>
        </a-row>
      </a-alert>
      <a-row style="padding-bottom: 10px">
        <a-col :span="12">
          <a-space>
            <a-button @click="back()"> 返回 </a-button>
          </a-space>
        </a-col>
        <!-- <a-col :span="12" align="right">
          <span>创建人：张三 创建时间：2021-08-12 12:00:00</span>
        </a-col> -->
      </a-row>
      <a-form-model
        :model="form"
        ref="ruleForm"
        :labelCol="{ span: 8 }"
        :wrapperCol="{ span: 16 }"
      >
        <a-row :gutter="20">
          <a-col :sm="8">
            <a-form-model-item label="竞价开始时间" prop="offerStartTime">
              <a-input disabled v-model="form.offerStartTime"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="竞价结束时间" prop="offerEndTime">
              <a-input disabled v-model="form.offerEndTime"></a-input>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :sm="8">
            <a-form-model-item label="预算">
              <a-input disabled v-model="form.budget"></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="8">
            <a-form-model-item label="币种" prop="currency">
              <a-select
                disabled
                v-model="form.currency"
                style="width: 100%"
                placeholder="请选择币种"
              >
                <a-select-option
                  v-for="item in currencyList"
                  :key="item.meaning"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="是否测试竞价" prop="whetherTest">
              <a-select
                disabled
                v-model="form.whetherTest"
                style="width: 100%"
                placeholder="请选择是否测试竞价"
              >
                <a-select-option :value="0">否</a-select-option>
                <a-select-option :value="1">是</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="竞价方向" prop="paidDirection">
              <a-select
                disabled
                v-model="form.paidDirection"
                style="width: 100%"
                placeholder="请选择竞价方向"
              >
                <a-select-option
                  v-for="item in paidDirectionList"
                  :key="item.meaning"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="出价结构" prop="bidStructure">
              <a-select
                disabled
                v-model="form.bidStructure"
                style="width: 100%"
                placeholder="请选择出价结构"
              >
                <a-select-option value="多项产品">多项产品</a-select-option>
                <a-select-option value="总额">总额</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="8">
            <a-form-model-item
              label="供应商竞价监控器"
              :prop="form.bidStructure === '总额' ? '' : 'vendorMonitorRules'"
            >
              <a-select
                disabled
                v-model="form.vendorMonitorRules"
                style="width: 100%"
                placeholder="请选择供应商竞价监控器"
              >
                <a-select-option
                  v-for="item in vendorMonitorRulesList"
                  :key="item.meaning"
                  :value="item.meaning"
                  >{{ item.meaning }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col> -->
          <!-- <a-col :span="8">
            <a-form-model-item label="出价方式" prop="bidWay">
              <a-select
                :disabled="type !== 'add'"
                v-model="form.bidWay"
                style="width: 100%"
                placeholder="请选择出价方式"
              >
                <a-select-option
                  v-for="item in bidWayList"
                  :key="item.meaning"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col> -->
          <!-- <a-col :sm="8">
            <a-form-model-item label="起始价格">
              <a-col :span="24">
                <a-select
                  disabled
                  v-model="form.startPriceRule"
                  style="width: 100%"
                  placeholder="请选择起始价格"
                >
                  <a-select-option value="统一">统一</a-select-option>
                  <a-select-option value="区分">区分</a-select-option>
                </a-select>
              </a-col>
            </a-form-model-item>
          </a-col> -->
          <!-- <a-col :sm="8">
            <a-form-model-item label="最小出价配置">
              <a-select
                :disabled="form.bidStructure === '多项产品' || type !== 'add'"
                v-model="form.minimumBidRule"
                style="width: 100%"
                placeholder="请选择最小出价配置"
              >
                <a-select-option
                  v-for="item in minimumBidRuleList"
                  :key="item.meaning"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="最小出价幅度">
              <a-input
                :disabled="form.bidStructure === '多项产品'"
                v-model="form.minimumBidRange"
                placeholder="请输入最小出价幅度"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="触发价格">
              <a-input
                :disabled="form.bidStructure === '多项产品'"
                v-model="form.triggerPrice"
                placeholder="请输入触发价格"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="触发价格后最小出价配置">
              <a-input
                :disabled="form.bidStructure === '多项产品'"
                v-model="form.triggerMinimumBidRange"
                placeholder="到达触发价格后最小出价配置"
              />
            </a-form-model-item>
          </a-col> -->
          <a-col :span="8">
            <a-form-model-item label="延时间隔" prop="delayInterval">
              <a-select
                disabled
                v-model="form.delayInterval"
                style="width: 100%"
                placeholder="请选择延时间隔"
              >
                <a-select-option
                  v-for="item in delayIntervalList"
                  :key="item.meaning"
                  :value="item.numerical"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="16">
            <a-form-model-item
              label="说明"
              :labelCol="{ span: 4 }"
              :wrapperCol="{ span: 20 }"
            >
              <a-textarea
                disabled
                v-model="form.description"
                placeholder="请输入说明"
                :auto-size="{ minRows: 3, maxRows: 5 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card>
      <a-tabs v-model="activeKey" type="card" tab-position="left">
        <a-tab-pane key="1" tab="品类明细">
          <itemdetails ref="itemdetails" :show="false"></itemdetails>
        </a-tab-pane>
        <a-tab-pane key="2" tab="附件">
          <enclosure ref="enclosure"></enclosure>
        </a-tab-pane>
        <a-tab-pane key="3" tab="通知|广播">
          <discuss :form="form" ref="discuss"></discuss>
        </a-tab-pane>
        <a-tab-pane
          key="4"
          tab="授权查询"
          v-if="
            form.requestStatusName === '待授权' ||
            form.requestStatusName === '已归档'
          "
        >
          <authorization ref="authorization"></authorization>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>
<script>
import itemdetails from './itemdetails'
import enclosure from './enclosure'
import discuss from './discuss'
import authorization from './auth/authorization'
import { dictionaryGetRequest } from "@/services/source/contend";
import moment from "moment";
export default {
  name: "seesupplierbidding",
  components: { itemdetails, enclosure, discuss, authorization },
  data () {
    return {
      currencyList: [], // 币种
      paidDirectionList: [], // 币种
      vendorMonitorRulesList: [], // 币种
      minimumBidRuleList: [], // 币种
      delayIntervalList: [], // 币种
      type: '',
      activeKey: '1',
      depList: [], // 部门下拉
      items: [], // 品类下拉
      data: '',
      userList: [],
      form: {}
    };
  },
  created () {
  },
  mounted () {
    this.form = JSON.parse(sessionStorage.getItem('contendForm'))
    const list = ['currency', 'paidDirection', 'vendorMonitorRules', 'minimumBidRule', 'delayInterval']
    list.forEach(item => { this.getDictionary(item) })
  },
  methods: {
    // 查看监控
    seeMonitor () {
      const startTime = moment(this.form.offerStartTime)
      const nowTime = moment(new Date().getTime())
      const minute = startTime.diff(nowTime, 'minute')
      if (this.form.requestStatusName === '待竞价' && startTime > nowTime && minute >= 10) {
        this.$message.warning('还未到达竞价时间')
        return
      }
      sessionStorage.setItem('biddingData', JSON.stringify(this.form))
      this.$router.push({ path: 'supplierbiddingmonitor' })
    },
    // 获取字典值
    async getDictionary (item) {
      const data = {
        currentPage: -1,
        dictionaryClass: item
      }
      const result = await dictionaryGetRequest(data)
      if (result.code === 0) {
        switch (item) {
          case 'currency':
            this.currencyList = result.data.records
            break
          case 'paidDirection':
            this.paidDirectionList = result.data.records
            break
          case 'vendorMonitorRules':
            this.vendorMonitorRulesList = result.data.records
            break
          case 'minimumBidRule':
            this.minimumBidRuleList = result.data.records
            break
          case 'delayInterval':
            this.delayIntervalList = result.data.records
            break
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 返回上一页
    back () {
      this.$router.push({ path: 'supplierbidding' })
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>