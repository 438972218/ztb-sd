<template>
  <div>
    <a-card class="card-top">
      <a-alert
        v-if="form.id"
        style="margin-bottom: 10px"
        :message="'竞价：' + form.oddNumber + '-' + form.title"
        type="info"
      >
        <a-row slot="description">
          <a-col :span="12">
            <div>
              {{
                '项目：' +
                (form.projectSheetVO ? form.projectSheetVO.title : '-')
              }}
            </div>
            <div>
              {{ '项目开始日期：' + dataTiem }}
            </div>
          </a-col>
          <a-col :span="12" align="right">
            <span style="color: #248c00; font-size: 22px"
              ><a
                @click="seeMonitor()"
                style="padding-right: 10px"
                v-has="['paid:monitor']"
                v-if="form.requestStatusName !== '待发布'"
              >
                <img alt="svg" src="@/assets/img/bidding.svg" width="25px" /></a
              >{{ form.requestStatusName }}</span
            >
          </a-col>
        </a-row>
      </a-alert>
      <a-row style="padding-bottom: 10px">
        <a-col :span="12">
          <a-space>
            <a-button
              type="primary"
              @click="handleSave"
              :disabled="getUserInfo"
              v-has="['paid:save']"
              v-if="form.requestStatusName === '待发布' || !form.id"
              >保存</a-button
            >
            <a-button
              type="primary"
              @click="biddingDocument()"
              :loading="documentLoading"
              :disabled="getUserInfo"
              v-has="['paid:authorization']"
              v-if="form.id && form.requestStatusName === '截止'"
              >授权</a-button
            >
            <a-button
              v-if="form.id && form.requestStatusName === '待发布'"
              type="primary"
              :loading="documentLoading"
              :disabled="getUserInfo"
              v-has="['paid:release']"
              @click="biddingDocument(1)"
              >发布</a-button
            >
            <a-button
              v-if="form.id && form.requestStatusName === '正在运行'"
              type="primary"
              @click="handleSave(1)"
              v-has="['paid:end']"
              :disabled="getUserInfo"
              >终止</a-button
            >
            <a-button
              v-if="form.id && form.requestStatusName === '待授权'"
              type="primary"
              @click="biddingDocument"
              v-has="['paid:submit']"
              :disabled="getUserInfo"
              >提交</a-button
            >
            <a-button
              v-if="form.id && form.requestStatusName !== '正在运行'"
              type="primary"
              :disabled="getUserInfo"
              v-has="['paid:delete']"
              @click="deletePaidSheet"
              >删除</a-button
            >
            <a-button @click="back()"> 返回 </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" align="right" v-if="form.id">
          <span>{{ createdInfo }}</span>
        </a-col>
      </a-row>
      <a-form-model
        :model="form"
        ref="ruleForm"
        :rules="rules"
        :labelCol="{ span: 8 }"
        :wrapperCol="{ span: 16 }"
      >
        <a-row :gutter="20">
          <a-col :sm="8" v-if="!form.id">
            <a-form-model-item label="项目" prop="projectSheetId">
              <a-select
                v-model="form.projectSheetId"
                style="width: 100%"
                placeholder="请选择项目"
                @change="handleChange"
              >
                <a-select-option
                  v-for="item in projectList"
                  :key="item.id"
                  :value="item.id"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="8" v-if="!form.id">
            <a-form-model-item label="竞价项目名称" prop="title">
              <a-input v-model="form.title" placeholder="请输入竞价项目名称" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="竞价开始时间" prop="offerStartTime">
              <a-date-picker
                :disabled="
                  form.id !== '' && form.requestStatusName !== '待发布'
                "
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
          <a-col :sm="8">
            <a-form-model-item label="竞价结束时间" prop="offerEndTime">
              <a-date-picker
                :disabled="
                  form.id !== '' && form.requestStatusName !== '待发布'
                "
                :disabled-date="disabledEndDate"
                :open="endOpen"
                @openChange="handleEndOpenChange"
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
          <a-col :sm="8">
            <a-form-model-item label="预算">
              <a-input
                v-model="form.budget"
                :disabled="form.id !== ''"
                placeholder="请输入预算"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="币种" prop="currency">
              <a-select
                :disabled="form.id !== ''"
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
            <a-form-model-item prop="whetherTest">
              <span slot="label"
                >是否测试竞价<a-tooltip>
                  <template slot="title"
                    >测试竞价不需要审批且不计入统计数据</template
                  >
                  <a-icon
                    style="padding-left: 5px"
                    type="info-circle" /></a-tooltip
              ></span>
              <a-select
                :disabled="form.id !== ''"
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
            <a-form-model-item prop="paidDirection">
              <span slot="label"
                >竞价方向<a-tooltip>
                  <template slot="title"
                    >正向由多家买方依次出价，价格逐步升高，反向由卖家依次出价，价格逐步减少</template
                  >
                  <a-icon
                    style="padding-left: 5px"
                    type="info-circle" /></a-tooltip
              ></span>
              <a-select
                :disabled="form.id !== '' || type === 'copy'"
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
            <a-form-model-item prop="bidStructure">
              <span slot="label"
                >出价结构<a-tooltip>
                  <template slot="title"
                    >总额即供应商报一个总价，多项产品可添加多个产品供应商对每一个产品单独报价，结束后可分别授权</template
                  >
                  <a-icon
                    style="padding-left: 5px"
                    type="info-circle" /></a-tooltip
              ></span>
              <a-select
                :disabled="form.id !== '' || type === 'copy'"
                v-model="form.bidStructure"
                style="width: 100%"
                placeholder="请选择出价结构"
                @change="bidStructureChange"
              >
                <a-select-option value="多项产品">多项产品</a-select-option>
                <a-select-option value="总额">总额</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="起始价格规则">
              <a-col :span="24">
                <a-select
                  :disabled="
                    form.bidStructure === '多项产品' ||
                    form.id !== '' ||
                    type === 'copy'
                  "
                  v-model="form.startPriceRule"
                  style="width: 100%"
                  placeholder="请选择起始价格规则"
                >
                  <a-select-option value="统一">统一</a-select-option>
                  <a-select-option value="区分">区分</a-select-option>
                </a-select>
              </a-col>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item
              :prop="form.bidStructure === '总额' ? '' : 'vendorMonitorRules'"
            >
              <span slot="label"
                >供应商竞价监控器<a-tooltip>
                  <template slot="title"
                    >供应商端竞价监控器是否显示多项产品报价的总价排</template
                  >
                  <a-icon
                    style="padding-left: 5px"
                    type="info-circle" /></a-tooltip
              ></span>
              <a-select
                :disabled="form.bidStructure === '总额' || form.id !== ''"
                v-model="form.vendorMonitorRules"
                style="width: 100%"
                placeholder="请选择供应商竞价监控器"
              >
                <a-select-option
                  v-for="item in vendorMonitorRulesList"
                  :key="item.meaning"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item prop="delayInterval">
              <span slot="label"
                >延时间隔<a-tooltip>
                  <template slot="title"
                    >竞价截止时间到，自动进入延时竞价阶段，距离竞价结束倒计时更新为此处设定的时长，延时竞价阶段如有供应商出价，以最后一次出价的时间点为准，重新进入延时设定的竞价周期</template
                  >
                  <a-icon
                    style="padding-left: 5px"
                    type="info-circle" /></a-tooltip
              ></span>
              <a-select
                v-model="form.delayInterval"
                :disabled="
                  form.id !== '' && form.requestStatusName !== '待发布'
                "
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
                :disabled="form.id !== ''"
                v-model="form.description"
                placeholder="请输入说明"
                :auto-size="{ minRows: 3, maxRows: 5 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card v-if="form.id">
      <a-tabs
        v-model="activeKey"
        type="card"
        tab-position="left"
        @change="tabChange"
      >
        <a-tab-pane key="1" tab="供应商&附件">
          <supplier
            ref="supplier"
            :getUserInfo="getUserInfo"
            :user="user"
            :form="form"
          ></supplier>
        </a-tab-pane>
        <a-tab-pane key="2" tab="品类明细">
          <itemdetails
            ref="itemdetails"
            :form="form"
            :getUserInfo="getUserInfo"
            :columns="columns"
            :minimumBidRuleList="minimumBidRuleList"
          ></itemdetails>
        </a-tab-pane>
        <a-tab-pane key="3" tab="通知&邮件">
          <notice ref="notice" :form="form" :getUserInfo="getUserInfo"></notice>
        </a-tab-pane>
        <a-tab-pane
          key="4"
          tab="评估并授权"
          v-if="
            form.requestStatusName === '待授权' ||
            form.requestStatusName === '已归档'
          "
        >
          <authorization
            :getUserInfo="getUserInfo"
            ref="authorization"
            :form="form"
          ></authorization>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>
<script>
import itemdetails from "./itemdetails"
import supplier from "./supplier"
import notice from "./notice"
import authorization from "./auth/authorization"
import {
  paidSheetSubmit,
  PutPaidSheetRequest,
  paidSheetDeleteRequest,
  PutPaidSheetAgree,
} from "@/services/source/contend"
import {
  dictionaryGetRequest,
  getProjectSheetUser,
} from "@/services/projectmanagement/project"
import { mapGetters } from "vuex"
import moment from "moment"
export default {
  name: "addcontend",
  components: { itemdetails, supplier, notice, authorization },
  computed: {
    ...mapGetters("account", ["user"]),
    dataTiem () {
      return this.form.projectSheetVO
        ? this.form.projectSheetVO.startTime
          ? moment(parseInt(this.form.projectSheetVO.startTime)).format(
            "YYYY-MM-DD"
          )
          : "-"
        : "-"
    },
    timeDefaultValue () {
      return moment(new Date().getHours() + ':' + new Date().getMinutes() + ':' + '00', 'HH:mm:ss')
    },
    createdInfo () {
      return this.form.sysUserInfoVO ? (
        "创建人：" +
        this.form.sysUserInfoVO.name +
        "   创建时间：" +
        moment(parseInt(this.form.createdTime)).format("YYYY-MM-DD")
      ) : ''
    },
    getUserInfo () {
      return this.form.sysUserInfoVO && this.form.sysUserInfoVO.id !== this.user.id
    },
  },
  data () {
    return {
      columns: [
        {
          title: "品类名称",
          dataIndex: "materialName",
          key: "materialName",
        },
        {
          title: '起始总价',
          dataIndex: "startPrice",
          key: "startPrice",
          scopedSlots: { customRender: "startPrice" },
        },
        {
          title: "最小出价配置",
          dataIndex: "minimumBidRule",
          key: "minimumBidRule",
        },
        {
          title: "最小出价幅度",
          dataIndex: "minimumBidRange",
          key: "minimumBidRange",
        },
        {
          title: "触发价格",
          dataIndex: "triggerPrice",
          key: "triggerPrice",
        },
        {
          title: "触发后最小出价幅度",
          dataIndex: "triggerMinimumBidRange",
          key: "triggerMinimumBidRange",
        },
        {
          title: "单位",
          dataIndex: "unit",
          key: "unit",
        },
        {
          title: "数量",
          dataIndex: "quantity",
          key: "quantity",
        },
        {
          title: "备注",
          dataIndex: "remark",
          key: "remark",
        },
        {
          title: "操作",
          dataIndex: "operation",
          key: "operation",
          scopedSlots: { customRender: "operation" },
        },
      ],
      type: "",
      form: {
        description: '', // 说明
        projectSheetId: undefined, // 项目Id
        delayInterval: undefined, // 延时间隔
        triggerMinimumBidRange: "", // 到达触发价格后最小出价配置
        triggerPrice: "", // 触发价格
        minimumBidRange: "", // 最小出价幅度
        minimumBidRule: undefined, // 最小出价配置
        startPrice: "", // 起始价格
        startPriceRule: '', // 起始价格规则
        // bidWay: undefined, // 出价方式
        vendorMonitorRules: undefined, // 供应商竞价监控器
        bidStructure: undefined, // 出价结构
        paidDirection: undefined, // 竞价方向
        whetherTest: undefined, // 是否测试竞价
        currency: undefined, // 币种
        budget: "", // 预算
        offerEndTime: "", // 竞价结束时间
        offerStartTime: "", // 竞价开始时间
        title: "", // 竞价项目名称
      },
      currencyList: [], // 币种
      paidDirectionList: [], // 竞价方向
      vendorMonitorRulesList: [], // 供应商竞价监控器
      bidWayList: [], // 出价方式
      minimumBidRuleList: [], // 最小出价配置
      delayIntervalList: [], // 延时间隔
      projectList: [],
      rules: {
        currency: [
          { required: true, message: "请选择币种", trigger: "change" },
        ],
        projectSheetId: [
          { required: true, message: "请选择项目", trigger: "change" },
        ],
        title: [
          { required: true, message: "请输入竞价项目名称", trigger: "blur" },
        ],
        offerStartTime: [
          { required: true, message: "请选择竞价开始时间", trigger: "change" },
        ],
        offerEndTime: [
          { required: true, message: "请选择竞价结束时间", trigger: "change" },
        ],
        whetherTest: [
          { required: true, message: "请选择是否测试竞价", trigger: "change" },
        ],
        paidDirection: [
          { required: true, message: "请选择竞价方向", trigger: "change" },
        ],
        bidStructure: [
          { required: true, message: "请选择出价结构", trigger: "change" },
        ],
        vendorMonitorRules: [
          {
            required: true,
            message: "请选择供应商竞价监控器",
            trigger: "change",
          },
        ],
        // bidWay: [{ required: true, message: '请选择出价方式', trigger: 'change' }],
        minimumBidRule: [
          { required: true, message: "请选择最小出价配置", trigger: "change" },
        ],
        minimumBidRange: [
          { required: true, message: "请输入最小出价幅度", trigger: "blur" },
        ],
        delayInterval: [
          { required: true, message: "请选择延时间隔", trigger: "change" },
        ],
      },
      activeKey: "1",
      depList: [], // 部门下拉
      items: [], // 品类下拉
      data: "",
      userList: [],
      endOpen: false,
      documentLoading: false,
    }
  },
  activated () {
    const keep = sessionStorage.getItem('addContendkeep')
    if (!keep) {
      this.$refs.ruleForm.resetFields()
      this.form = JSON.parse(sessionStorage.getItem("contendForm"))
      this.type = sessionStorage.getItem("contendType")
      this.activeKey = '1'
      if (this.form.id) {
        this.$nextTick(() => {
          this.$refs.supplier.init()
        })
      }
      this.getProjectSheet()
      const list = [
        "currency",
        "paidDirection",
        "vendorMonitorRules",
        "minimumBidRule",
        "delayInterval",
      ]
      list.forEach(item => {
        this.getDictionary(item)
      })
    }
  },
  mounted () {
    this.form = JSON.parse(sessionStorage.getItem("contendForm"))
    this.type = sessionStorage.getItem("contendType")
    this.getProjectSheet()
    const list = [
      "currency",
      "paidDirection",
      "vendorMonitorRules",
      "minimumBidRule",
      "delayInterval",
    ]
    list.forEach(item => {
      this.getDictionary(item)
    })
  },
  deactivated () {
    sessionStorage.setItem('addContendkeep', 'true')
  },
  methods: {
    // tabs切换触发
    tabChange (val) {
      if (val === '1') {
        this.$nextTick(() => {
          this.$refs.supplier.init()
        })
      } else if (val === '2') {
        this.$nextTick(() => {
          this.$refs.itemdetails.getItems()
        })
      } else if (val === '3') {
        this.$nextTick(() => {
          this.$refs.notice.getDataList()
        })
      }
    },
    // 项目选择发生改变
    handleChange (e) {
      const data = this.projectList.find(f => f.id === e)
      if (data) {
        this.form.title = data.title
      }
    },
    handleStartOpenChange (open) {
      if (!open) {
        this.endOpen = true
      }
    },
    handleEndOpenChange (open) {
      this.endOpen = open
    },
    disabledEndDate (endValue) {
      const startValue = this.form.offerStartTime
        ? moment(this.form.offerStartTime).format('YYYY-MM-DD')
        : ""
      if (!endValue || !startValue) {
        return false
      }
      const endDate = moment(endValue).format('YYYY-MM-DD')
      return moment(startValue) > moment(endDate)
    },
    // 查看监控
    seeMonitor () {
      sessionStorage.setItem("contendbidding", JSON.stringify(this.form))
      this.$router.push({ path: "biddingmonitor" })
    },
    // 获取字典值
    async getDictionary (item) {
      const data = {
        currentPage: -1,
        dictionaryClass: item,
      }
      const result = await dictionaryGetRequest(data)
      if (result.code === 0) {
        switch (item) {
          case "currency":
            this.currencyList = result.data.records
            break
          case "paidDirection":
            this.paidDirectionList = result.data.records
            break
          case "vendorMonitorRules":
            this.vendorMonitorRulesList = result.data.records
            break
          case "minimumBidRule":
            this.minimumBidRuleList = result.data.records
            break
          case "delayInterval":
            this.delayIntervalList = result.data.records
            break
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取项目
    async getProjectSheet () {
      const data = {
        type: "official",
        userId: this.user.id,
        status: '运行中',
        currentPage: -1,
      }
      const result = await getProjectSheetUser(data)
      if (result.code === 0) {
        this.projectList = result.data ? result.data.records : []
        if (this.projectList && !this.projectList.find(f => f.id === this.form.projectSheetId)) {
          this.form.projectSheetId = undefined
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 删除竞价单
    async deletePaidSheet () {
      const that = this
      this.$confirm({
        title: "提示",
        content: "是否确认删除该数据",
        async onOk () {
          const result = await paidSheetDeleteRequest(that.form.id)
          if (result.code === 0) {
            that.$message.success("删除成功")
            that.$router.push({ path: "contend" })
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      })
    },
    // 流转竞价单
    async biddingDocument (type) {
      if (type === 1 && new Date(this.form.offerStartTime) < new Date()) {
        this.$message.warning('竞价开始时间需要大于当前时间')
        return
      }
      if (type === 1 && new Date(this.form.offerStartTime) > new Date(this.form.offerEndTime)) {
        this.$message.warning('竞价结束时间时间需要大于竞价开始时间')
        return
      }
      this.documentLoading = true
      const data = {
        id: this.form.id,
        requestId: this.form.requestId,
        userId: this.user.id,
      }
      const result = await PutPaidSheetAgree(data)
      if (result.code === 0) {
        this.documentLoading = false
        this.form = Object.assign({}, result.data)
        this.form.offerStartTime = moment(
          parseInt(this.form.offerStartTime)
        ).format("YYYY-MM-DD HH:mm:ss")
        this.form.offerEndTime = moment(
          parseInt(this.form.offerEndTime)
        ).format("YYYY-MM-DD HH:mm:ss")
        if (type === 1) this.handleSave()
        this.$message.success("发布成功")
      } else {
        this.documentLoading = false
        this.$message.error(result.message)
      }
    },
    // 保存
    handleSave (type) {
      if (this.form.offerStartTime && this.form.offerEndTime && new Date(this.form.offerStartTime) > new Date(this.form.offerEndTime)) {
        this.$message.warning('竞价结束时间时间需要大于竞价开始时间')
        return
      }
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          let data = Object.assign(
            {
              userId: this.user.id,
              name: this.user.name,
              ruleId: 1,
              typeId: "1424566510971592705",
            },
            this.form
          )
          if (type === 1) {
            data.requestStatusName = "正在运行"
          }
          let result = null
          if (data.id) {
            result = await PutPaidSheetRequest(data)
          } else {
            if (data.paidAttachmentVOS || data.paidVendorVOS || data.paidMaterialVOS) {
              data.paidAttachmentDTOS = data.paidAttachmentVOS
              data.paidVendorDTOS = data.paidVendorVOS
              data.paidMaterialDTOS = data.paidMaterialVOS
              this.$delete(data, 'paidAttachmentVOS')
              this.$delete(data, 'paidVendorVOS')
              this.$delete(data, 'paidMaterialVOS')
            }
            result = await paidSheetSubmit(data)
          }
          if (result.code === 0) {
            this.form = Object.assign({}, result.data)
            this.form.offerStartTime = moment(
              parseInt(this.form.offerStartTime)
            ).format("YYYY-MM-DD HH:mm:ss")
            this.form.offerEndTime = moment(
              parseInt(this.form.offerEndTime)
            ).format("YYYY-MM-DD HH:mm:ss")
            sessionStorage.setItem("contendForm", JSON.stringify(this.form))
            this.$message.success("保存成功")
          } else {
            this.$message.error(result.message)
          }
        }
      })
    },
    // 出价结构发生改变
    bidStructureChange (e) {
      if (e === "多项产品") {
        this.form.startPriceRule = '统一'
        this.form.startPrice = ""
        this.form.minimumBidRule = undefined
        this.form.minimumBidRange = ""
        this.form.triggerPrice = ""
        this.form.triggerMinimumBidRange = ""
      } else if (e === "总额") {
        this.form.vendorMonitorRules = undefined
      }
    },
    // 返回上一页
    back () {
      this.$router.push({ path: "contend" })
    },
  },
}
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>
