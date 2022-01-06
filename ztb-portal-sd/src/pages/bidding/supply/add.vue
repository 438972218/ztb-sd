<template>
  <div>
    <a-card class="card-top">
      <a-alert
        v-if="form.id"
        style="margin-bottom: 10px"
        :message="'招标：' + form.oddNumber + '-' + form.title"
        type="info"
      >
        <a-row slot="description">
          <a-col :span="12">
            {{
              '项目：' + (form.projectSheetVO ? form.projectSheetVO.title : '-')
            }}
            <div>
              {{ '项目开始日期：' + dataTiem }}
            </div>
          </a-col>
          <a-col :span="12" align="right">
            <span style="color: #248c00; font-size: 22px">{{
              form.requestStatusName
            }}</span>
          </a-col>
        </a-row>
      </a-alert>
      <a-row style="padding-bottom: 10px">
        <a-col :span="12">
          <a-space>
            <a-button
              v-if="
                vendorStatus !== '已拒绝' &&
                form.requestStatusName !== '已归档' &&
                !expired
              "
              type="primary"
              :disabled="endTimeJudge"
              @click="replySubmit(saveTrue ? '已撤回' : '已回复')"
              >{{ saveTrue ? '撤销回复' : '提交回复' }}</a-button
            >
            <a-button
              v-if="
                vendorStatus !== '已拒绝' &&
                form.requestStatusName !== '已归档' &&
                !expired
              "
              :disabled="endTimeJudge"
              @click="refuseReply('已拒绝')"
              >拒绝回复</a-button
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
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row :gutter="20">
          <!-- <a-col :sm="8">
            <a-form-model-item label="发布时间">
              <a-input disabled v-model="form.startTime"></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :sm="8">
            <a-form-model-item label="投标截止时间">
              <a-input
                v-if="form.mark"
                disabled
                v-model="form.clarifyEndTime"
              ></a-input>
              <a-input v-else disabled v-model="form.bidEndTime"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="币种">
              <a-input disabled v-model="form.currency"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="是否测试招标">
              <a-select
                v-model="form.whetherTest"
                disabled
                style="width: 100%"
                placeholder="请选择是否测试招标"
              >
                <a-select-option :value="0">否</a-select-option>
                <a-select-option :value="1">是</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-model-item
              label="品类"
              :labelCol="{ span: 2 }"
              :wrapperCol="{ span: 22 }"
            >
              <a-input disabled v-model="form.items"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="说明"
              :labelCol="{ span: 2 }"
              :wrapperCol="{ span: 22 }"
            >
              <a-textarea
                disabled
                v-model="form.explanation"
                placeholder="请输入说明"
                :auto-size="{ minRows: 2, maxRows: 6 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card>
      <a-tabs v-model="activeKey" type="card" tab-position="left">
        <a-tab-pane key="1" tab="我的回复">
          <reply :saveTrue="saveTrue" ref="reply"></reply>
        </a-tab-pane>
        <a-tab-pane key="2" tab="附件">
          <annex ref="annex" :user="user"></annex>
        </a-tab-pane>
        <a-tab-pane key="3" tab="通知">
          <talkover ref="talkover"></talkover>
        </a-tab-pane>
        <a-tab-pane
          key="4"
          tab="授权"
          v-if="form.requestStatusName === '已归档'"
        >
          <bidaward :form="form" ref="bidaward"></bidaward>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import moment from 'moment'
import {
  bidVendorDetailOperation,
  bidVendorDetailBatchUpdate,
  requestFlowTransferNode
} from "@/services/source/bidding";
import annex from './annex'
import talkover from './talkover'
import reply from './reply'
import bidaward from './bidaward'
export default {
  name: "addbidding",
  components: { annex, talkover, reply, bidaward },
  computed: {
    ...mapGetters("account", ["user"]),
    dataTiem () {
      return this.form.projectSheetVO ? (this.form.projectSheetVO.startTime ? moment(parseInt(this.form.projectSheetVO.startTime)).format('YYYY-MM-DD') : '-') : '-'
    },
    createdInfo () {
      return '发布时间：' + (this.beginTime ? moment(parseInt(this.beginTime)).format('YYYY-MM-DD HH:mm:ss') : '-')
    },
    endTimeJudge () {
      let result = ''
      if (this.form.mark) {
        result = this.form.clarifyEndTime ? new Date(this.form.clarifyEndTime) < new Date() : false
      } else {
        result = this.form.bidEndTime ? new Date(this.form.bidEndTime) < new Date() : false
      }
      return result
    },
  },
  data () {
    return {
      beginTime: '',
      saveTrue: false,
      type: '',
      projectList: [],
      form: {
        startTime: '', // 发布时间
        currency: '', // 币种
        bidEndTime: '', // 投标截止时间
        items: '', // 品类
        explanation: '', // 说明
      },
      activeKey: '1',
      data: '',
      userList: [],
      currencyList: [],
      rankingList: [],
      vendorStatus: '',
      expired: false
    };
  },
  created () {
  },
  mounted () {
    this.form = JSON.parse(sessionStorage.getItem('supplyForm'))
    this.vendorStatus = this.form.vendorStatus
    this.saveTrue = this.vendorStatus === '已回复'
    this.getRequestFlowTransferNode()
  },
  methods: {
    // 查询招标流程
    async getRequestFlowTransferNode () {
      const data = {
        requestId: this.form.requestId,
        toStatusId: '1430091118399266818',
        fromStatusId: '1430091754389970945'
      }
      const result = await requestFlowTransferNode(data)
      if (result.code === 0) {
        this.beginTime = result.data ? result.data[0].beginTime : ''
      } else {
        this.$message.error(result.message)
      }
    },
    // 拒绝回复
    refuseReply (type) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '请确认是否拒绝回复此招标，拒绝将无法再次参加本轮招标',
        async onOk () {
          that.putBidVendorDetailOperation(type)
        },
        onCancel () { },
      });
    },
    // 提交回复
    replySubmit (type) {
      if (this.$refs.reply && !this.$refs.reply.qualificationTenderTrue && !this.$refs.reply.technicalTenderTrue && !this.$refs.reply.quotationTrue && !this.$refs.reply.domesticTrue && !this.$refs.reply.expensesTrue) {
        this.putBidVendorDetailOperation(type)
      } else {
        this.$message.warning('请先保存回复')
      }
    },
    // 修改招标单状态
    async putBidVendorDetailOperation (type) {
      if (this.endTimeJudge) {
        this.$message.warning('投标已截止，无法提交/撤回')
        return
      }
      const data = {
        bidVendorId: this.form.bidVendorId,
        operation: type
      }
      const result = await bidVendorDetailOperation(data)
      if (result.code === 0) {
        this.vendorStatus = type
        if (type === '已回复') {
          this.saveTrue = true
        } else {
          this.saveTrue = false
        }
        this.$message.success('成功')
      } else {
        this.expired = true
        this.$message.error(result.message)
      }
    },
    // 返回上一页
    back () {
      this.$router.push({ path: 'supply' })
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>