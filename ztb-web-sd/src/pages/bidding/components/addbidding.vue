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
              type="primary"
              @click="handleSumbit"
              v-has="['bid:save']"
              :disabled="getUserInfo"
              v-if="!form.id || form.requestStatusName === '待发布'"
              >保存</a-button
            >
            <!-- <a-button
              type="primary"
              @click="handleSumbit"
              v-if="form.id && form.requestStatusName === '正在运行'"
              >修改投标截止时间</a-button
            > -->
            <!-- <a-button
              type="primary"
              @click="handleRelease"
              v-if="form.id && form.requestStatusName === '正在运行'"
              >终止</a-button
            > -->
            <a-button
              type="primary"
              @click="handleRelease"
              :disabled="getUserInfo"
              v-has="['bid:end']"
              v-if="form.id && form.requestStatusName === '正在运行'"
              >结束投标</a-button
            >
            <a-button
              type="primary"
              @click="handleRelease"
              :disabled="getUserInfo"
              v-has="['bid:startevaluation']"
              v-if="form.id && form.requestStatusName === '待评估'"
              >开始评估</a-button
            >
            <a-button
              type="primary"
              @click="handleRelease"
              v-has="['bid:submit']"
              :disabled="getUserInfo"
              v-if="
                (form.requestStatusName === '待资质评估' ||
                  form.requestStatusName === '待技术评估' ||
                  form.requestStatusName === '待商业评估' ||
                  form.requestStatusName === '待授权') &&
                form.id
              "
              >提交</a-button
            >
            <a-button
              type="primary"
              @click="handleRelease(1)"
              :disabled="getUserInfo"
              v-has="['bid:release']"
              v-if="form.id && form.requestStatusName === '待发布'"
              >发布</a-button
            >
            <a-button
              type="primary"
              v-if="
                form.id &&
                (form.requestStatusName === '待授权' ||
                  form.requestStatusName === '已归档')
              "
              v-has="['bid:exportrawdata']"
              @click="exportOriginalData"
              >导出原始数据</a-button
            >
            <a-button
              type="primary"
              @click="exportBVDC"
              v-if="
                form.id &&
                (form.requestStatusName === '待授权' ||
                  form.requestStatusName === '已归档')
              "
              v-has="['bid:exportsummarydata']"
              >导出汇总数据</a-button
            >
            <a-button
              type="primary"
              :disabled="getUserInfo"
              v-if="
                form.id &&
                (!form.requestStatusName ||
                  form.requestStatusName === '待发布' ||
                  form.requestStatusName === '待授权')
              "
              v-has="['bid:delete']"
              @click="deleteBidding"
              >删除</a-button
            >
            <!-- <a-button type="primary" v-if="form.id">暂停</a-button> -->
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
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 18 }"
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
          <a-col :span="8" v-if="!form.id">
            <a-form-model-item label="招标名称" prop="title">
              <a-input
                v-model="form.title"
                placeholder="请输入招标名称"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8" v-if="!form.id">
            <a-form-model-item label="招标方式">
              <a-input
                v-model="form.tenderMode"
                disabled
                placeholder="请输入招标方式"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="是否测试招标" prop="whetherTest">
              <a-select
                v-model="form.whetherTest"
                :disabled="form.id !== ''"
                style="width: 100%"
                placeholder="请选择是否测试招标"
              >
                <a-select-option :value="0">否</a-select-option>
                <a-select-option :value="1">是</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="8">
            <a-form-model-item label="投标截止时间" prop="bidEndTime">
              <a-date-picker
                v-if="form.mark && form.id !== ''"
                :disabled="
                  form.id !== '' && form.requestStatusName !== '待发布'
                "
                format="YYYY-MM-DD HH:mm:ss"
                valueFormat="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                :show-time="{
                  format: 'HH:mm',
                  defaultValue: timeDefaultValue,
                }"
                v-model="form.clarifyEndTime"
              />
              <a-date-picker
                v-else
                :disabled="
                  form.id !== '' && form.requestStatusName !== '待发布'
                "
                format="YYYY-MM-DD HH:mm:ss"
                valueFormat="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                :show-time="{
                  format: 'HH:mm',
                  defaultValue: timeDefaultValue,
                }"
                v-model="form.bidEndTime"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="币种" prop="currency">
              <a-select
                v-model="form.currency"
                :disabled="form.id !== ''"
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
            <a-form-model-item label="供应商回复排名" prop="rankingRules">
              <a-select
                v-model="form.rankingRules"
                :disabled="form.id !== ''"
                style="width: 100%"
                placeholder="请选择供应商回复排名"
              >
                <a-select-option
                  v-for="item in rankingList"
                  :key="item.meaning"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
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
        </a-row>
        <!-- <a-col :span="8">
            <a-form-model-item label="需要审批" prop="approvalProcess">
              <a-select
                v-model="form.approvalProcess"
                :disabled="form.id !== ''"
                style="width: 100%"
                placeholder="请选择需要审批"
              >
                <a-select-option :value="0">不需要</a-select-option>
                <a-select-option :value="1">需要</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col> -->
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="资质标">
              <a-checkbox
                v-model="form.qualificationTender"
                :disabled="form.id !== ''"
              ></a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="技术标">
              <a-checkbox
                v-model="form.technicalTender"
                :disabled="form.id !== ''"
              ></a-checkbox>
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
              <a-select
                :disabled="
                  form.id !== '' && form.requestStatusName !== '待发布'
                "
                mode="multiple"
                v-model="form.items"
                style="width: 100%"
                placeholder="请选择品类"
              >
                <a-select-option
                  v-for="item in items"
                  :key="item.id"
                  :value="item.itemName"
                >
                  {{ item.itemName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="说明"
              :labelCol="{ span: 2 }"
              :wrapperCol="{ span: 22 }"
            >
              <a-textarea
                :disabled="
                  form.id !== '' && form.requestStatusName !== '待发布'
                "
                v-model="form.explanation"
                placeholder="请输入说明"
                :auto-size="{ minRows: 2, maxRows: 6 }"
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
        <a-tab-pane key="1" tab="资质|技术|商业标">
          <marklist
            ref="markList"
            :getUserInfo="getUserInfo"
            :form="form"
          ></marklist>
        </a-tab-pane>
        <a-tab-pane key="2" tab="通知|邮件">
          <notice ref="notice" :form="form" :getUserInfo="getUserInfo"></notice>
        </a-tab-pane>
        <a-tab-pane key="3" tab="供应商|附件">
          <supplier
            ref="supplier"
            :form="form"
            :user="user"
            :getUserInfo="getUserInfo"
          ></supplier>
        </a-tab-pane>
        <a-tab-pane key="4" tab="开标委员会">
          <committee
            ref="committee"
            :form="form"
            :getUserInfo="getUserInfo"
          ></committee>
        </a-tab-pane>
        <a-tab-pane
          key="5"
          tab="监视器"
          v-if="form.requestStatusName !== '待发布'"
        >
          <monitor
            ref="monitor"
            :form="form"
            :getUserInfo="getUserInfo"
          ></monitor>
        </a-tab-pane>
        <a-tab-pane
          key="6"
          tab="评估"
          v-if="
            form.requestStatusName !== '待发布' &&
            form.requestStatusName !== '正在运行'
          "
        >
          <assessment
            :form="form"
            ref="assessment"
            :getUserInfo="getUserInfo"
          ></assessment>
        </a-tab-pane>
        <a-tab-pane
          key="7"
          tab="授标"
          v-if="
            form.requestStatusName === '待授权' ||
            form.requestStatusName === '已归档'
          "
        >
          <bidaward
            :form="form"
            :getUserInfo="getUserInfo"
            ref="bidaward"
            @refreshData="refreshData"
          ></bidaward>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>
<script>
import { itemGetRequest } from "@/services/basis/index";
import { dictionaryGetRequest, getProjectSheetUser } from "@/services/projectmanagement/project";
import { bidInvitationPostSubmit, bidSheetAgree, bidInvitationPutRequestReturnvo, exportBidVendorDetailCommercial, exportBidVendorDetail, bidInvitationDeleteRequest } from "@/services/source/bidding";
import { mapGetters } from "vuex";
import moment from 'moment'
import supplier from './supplier'
import committee from './committee'
import marklist from './markList'
import notice from './notice'
import monitor from './monitor'
import assessment from './assessment'
import bidaward from './bidaward'
export default {
  name: "addbidding",
  components: { supplier, committee, marklist, notice, monitor, assessment, bidaward },
  computed: {
    ...mapGetters("account", ["user"]),
    dataTiem () {
      return this.form.projectSheetVO ? (this.form.projectSheetVO.startTime ? moment(parseInt(this.form.projectSheetVO.startTime)).format('YYYY-MM-DD') : '-') : '-'
    },
    createdInfo () {
      return '创建人：' + this.form.sysUserInfoVO.name + '   创建时间：' + moment(parseInt(this.form.createdTime)).format('YYYY-MM-DD')
    },
    timeDefaultValue () {
      return moment(new Date().getHours() + ':' + new Date().getMinutes() + ':' + '00', 'HH:mm:ss')
    },
    getUserInfo () {
      return this.form.sysUserInfoVO && this.form.sysUserInfoVO.id !== this.user.id
    },
  },
  data () {
    return {
      type: '',
      projectList: [],
      form: {
        projectSheetId: undefined, // 项目Id
        qualificationTender: false,
        technicalTender: false,
        title: '', // 标题
        tenderMode: '邀请参与招标', // 招标方式
        currency: undefined, // 币种
        whetherTest: 0, // 是否测试招标
        biddingmethod: '', // 招标方式
        budget: '', // 预算
        rankingRules: undefined, // 供应商回复排名
        bidEndTime: '', // 投标截止时间
        approvalProcess: 0, // 需要审批
        items: [], // 品类
        explanation: '', // 说明
      },
      rules: {
        currency: [{ required: true, message: '请选择币种', trigger: 'change' }],
        projectSheetId: [{ required: true, message: '请选择项目', trigger: 'change' }],
        title: [{ required: true, message: '请输入招标名称', trigger: 'blur' }],
        bidEndTime: [{ required: true, message: '请选择投标截止时间', trigger: 'change' }],
        whetherTest: [{ required: true, message: '请选择是否测试招标', trigger: 'change' }],
        rankingRules: [{ required: true, message: '请选择供应商回复排名', trigger: 'change' }],
      },
      activeKey: '1',
      depList: [], // 部门下拉
      items: [], // 品类下拉
      data: '',
      userList: [],
      currencyList: [],
      rankingList: []
    };
  },
  activated () {
    const keep = sessionStorage.getItem('addBiddingkeep')
    if (!keep) {
      this.$refs.ruleForm.resetFields()
      this.activeKey = '1'
      this.$nextTick(() => {
        this.$refs.markList.init()
      })
      this.form = JSON.parse(sessionStorage.getItem('biddingForm'))
      const list = ['currency', 'rankingRules']
      list.forEach(item => { this.getDictionary(item) })
      this.getItems()
      this.getProjectSheet()
    }
  },
  mounted () {
    this.form = JSON.parse(sessionStorage.getItem('biddingForm'))
    const list = ['currency', 'rankingRules']
    list.forEach(item => { this.getDictionary(item) })
    this.getItems()
    this.getProjectSheet()
  },
  deactivated () {
    sessionStorage.setItem('addBiddingkeep', 'true')
  },
  methods: {
    range (start, end) {
      const result = [];
      for (let i = start; i < end; i++) {
        result.push(i);
      }
      return result;
    },
    disabledDate (current) {
      // Can not select days before today and today
      return current && current < moment().endOf('day');
    },
    disabledDateTime () {
      return {
        disabledHours: () => this.range(0, 24).splice(0, new Date().getHours()),
        disabledMinutes: () => this.range(0, 60).splice(0, new Date().getMinutes() + 1),
      };
    },
    // 删除招标单
    async deleteBidding () {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该招标单',
        async onOk () {
          const result = await bidInvitationDeleteRequest(that.form.id)
          if (result.code === 0) {
            that.$message.success('删除成功')
            that.$router.push({ path: 'bidding' })
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 刷新数据
    refreshData (data) {
      this.form = Object.assign({}, data)
      this.form.bidEndTime = moment(parseInt(this.form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
      this.form.clarifyEndTime = this.form.clarifyEndTime ? moment(parseInt(this.form.clarifyEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
      this.form.qualificationTender = this.form.qualificationTender === 1
      this.form.technicalTender = this.form.technicalTender === 1
      this.form.items = this.form.items ? this.form.items.split(',') : []
      sessionStorage.setItem('biddingForm', JSON.stringify(this.form))
    },
    // 导出原始数据
    async exportOriginalData () {
      const data = {
        bidSheetId: this.form.id
      }
      const result = await exportBidVendorDetail(data)
      const link = document.createElement('a')
      let blob = new Blob([result], { type: 'application/vnd.ms-excel' })
      link.style.display = 'none'
      link.href = URL.createObjectURL(blob)
      // link.download = res.headers['content-disposition'] //下载后文件名
      link.download = '原始数据' //下载的文件名
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },
    // 导出汇总数据
    async exportBVDC () {
      const data = {
        bidSheetId: this.form.id
      }
      const result = await exportBidVendorDetailCommercial(data)
      const link = document.createElement('a')
      let blob = new Blob([result], { type: 'application/vnd.ms-excel' })
      link.style.display = 'none'
      link.href = URL.createObjectURL(blob)
      // link.download = res.headers['content-disposition'] //下载后文件名
      link.download = '汇总数据' //下载的文件名
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },
    // 标签发生改变
    tabChange (val) {
      if (val === '3') {
        this.$nextTick(() => {
          this.$refs.supplier.init()
        })
      } else if (val === '1') {
        this.$nextTick(() => {
          this.$refs.markList.init()
        })
      } else if (val === '2') {
        this.$nextTick(() => {
          this.$refs.notice.activeKey = '1'
          this.$refs.notice.getDataList()
        })
      } else if (val === '4') {
        this.$nextTick(() => {
          this.$refs.committee.getDataList()
          this.$refs.committee.getUserList()
        })
      } else
        if (val === '5') {
          this.$nextTick(() => {
            this.$refs.monitor.init()
          })
        } else if (val === '6') {
          this.$nextTick(() => {
            this.$refs.assessment.getDataList()
          })
        } else if (val === '7') {
          this.$nextTick(() => {
            this.$refs.bidaward.getDataList()
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
    // 发布
    async handleRelease (type) {
      if (type === 1 && this.form.bidEndTime && new Date() > new Date(this.form.bidEndTime)) {
        this.$message.warning('截止时间必须大于当前时间')
        return
      }
      const data = {
        id: this.form.id,
        requestId: this.form.requestId,
        userId: this.user.id
      }
      const result = await bidSheetAgree(data)
      if (result.code === 0) {
        this.form = Object.assign({}, result.data)
        this.form.bidEndTime = moment(parseInt(this.form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
        this.form.clarifyEndTime = this.form.clarifyEndTime ? moment(parseInt(this.form.clarifyEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
        this.form.qualificationTender = this.form.qualificationTender === 1
        this.form.technicalTender = this.form.technicalTender === 1
        this.form.items = this.form.items ? this.form.items.split(',') : []
        sessionStorage.setItem('biddingForm', JSON.stringify(this.form))
        this.$message.success('流转成功')
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取项目
    async getProjectSheet () {
      const data = {
        type: 'official',
        userId: this.user.id,
        status: '运行中',
        currentPage: -1
      }
      const result = await getProjectSheetUser(data)
      if (result.code === 0) {
        this.projectList = result.data.records
      } else {
        this.$message.error(result.message)
      }
    },
    // 提交招标单
    async handleSumbit () {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          if (new Date() > new Date(this.form.bidEndTime)) {
            this.$message.warning('截止时间必须大于当前时间')
            return
          }
          const data = Object.assign({}, this.form)
          data.qualificationTender = data.qualificationTender ? 1 : 0
          data.technicalTender = data.technicalTender ? 1 : 0
          data.ruleId = 2
          data.typeId = '1424531417540403201'
          data.items = data.items.toString()
          data.name = this.user.name
          let result = null
          if (this.form.id) {
            result = await bidInvitationPutRequestReturnvo(data)
          } else {
            if (data.bidDetailVOS || data.bidSpecialistVOS) {
              data.bidVendorDTOS = data.bidVendorVOS
              data.bidDetailDTOS = data.bidDetailVOS
              data.bidSpecialistDTOS = data.bidSpecialistVOS
              data.bidDetailDTOS = data.bidDetailVOS
              this.$delete(data, 'bidVendorVOS')
              this.$delete(data, 'bidDetailVOS')
              this.$delete(data, 'bidSpecialistVOS')
            }
            result = await bidInvitationPostSubmit(data)
          }
          if (result.code === 0) {
            this.form = Object.assign({}, result.data)
            this.form.bidEndTime = moment(parseInt(this.form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
            this.form.clarifyEndTime = this.form.clarifyEndTime ? moment(parseInt(this.form.clarifyEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
            this.form.qualificationTender = this.form.qualificationTender === 1
            this.form.technicalTender = this.form.technicalTender === 1
            this.form.items = this.form.items ? this.form.items.split(',') : []
            sessionStorage.setItem('biddingForm', JSON.stringify(this.form))
            this.$message.success('提交成功')
          } else {
            this.$message.error(result.message)
          }
        }
      })
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
          case 'rankingRules':
            this.rankingList = result.data.records
            break
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取品类下拉
    async getItems () {
      const data = {
        currentPage: -1,
        deleted: 0,
      }
      const result = await itemGetRequest(data)
      if (result.code === 0) {
        this.items = result.data.records
      } else {
        this.$message.error(result.message)
      }
    },
    // 返回上一页
    back () {
      this.$router.push({ path: 'bidding' })
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>