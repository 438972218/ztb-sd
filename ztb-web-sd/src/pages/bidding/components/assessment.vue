<template>
  <div>
    <a-row>
      <a-col :span="12" class="fontClass"
        >本轮邀请供应商:{{ invitationCount }}</a-col
      >
      <a-col :span="12" class="fontClass"
        >回合中收到的回复:{{ replyCount }}</a-col
      >
    </a-row>
    <a-tabs v-model="activeKey" @change="getBidVendoWithVendor">
      <a-tab-pane key="1" tab="评估概况">
        <a-table :columns="columns" :data-source="data" :pagination="false">
          <template slot="qualificationView" slot-scope="text, record">
            <span v-if="!text || text === 0">否</span>
            <a v-else @click="getBidSpecialistScoreVendor(record, '资质标')"
              >是，查看评论内容</a
            >
          </template>
          <template slot="technologyView" slot-scope="text, record">
            <span v-if="!text || text === 0">否</span>
            <a v-else @click="getBidSpecialistScoreVendor(record, '技术标')"
              >是，查看评论内容</a
            >
          </template>
          <template slot="commerceView" slot-scope="text, record">
            <span v-if="!text || text === 0">否</span>
            <a v-else @click="getBidSpecialistScoreVendor(record, '商业标')"
              >是，查看评论内容</a
            >
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane
        key="2"
        tab="资质参数"
        v-if="form.qualificationTender && form.requestStatusName !== '待评估'"
      >
        <a-table
          :columns="qualificationColumns"
          :data-source="qualificationData"
          :pagination="false"
        >
          <template slot="qualificationView" slot-scope="text, record">
            <span v-if="!text || text === 0">否</span>
            <a v-else @click="getBidSpecialistScoreVendor(record, '资质标')"
              >是，查看评论内容</a
            >
          </template>
          <template
            slot="operation"
            slot-scope="text, record"
            v-if="form.requestStatusName !== '已归档'"
          >
            <a
              @click="handleScore(record, '资质标')"
              v-if="record.bidSpecialistScoreVO"
              >评分</a
            >
          </template>
          <template slot="seeBackBid" slot-scope="text, record">
            <a
              @click="handleSeeBackBid(record, '资质标')"
              v-if="record.bidSpecialistScoreVO"
              >查看</a
            >
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane
        key="3"
        tab="技术回标"
        v-if="
          form.technicalTender &&
          form.requestStatusName !== '待资质评估' &&
          form.requestStatusName !== '待评估'
        "
      >
        <a-table
          :columns="technologyColumns"
          :data-source="qualificationData"
          :pagination="false"
        >
          <template slot="technologyView" slot-scope="text, record">
            <span v-if="!text || text === 0">否</span>
            <a v-else @click="getBidSpecialistScoreVendor(record, '技术标')"
              >是，查看评论内容</a
            >
          </template>
          <template
            slot="operation"
            slot-scope="text, record"
            v-if="form.requestStatusName !== '已归档'"
          >
            <a
              @click="handleScore(record, '技术标')"
              v-if="record.bidSpecialistScoreVO"
              >评分</a
            >
          </template>
          <template slot="seeBackBid" slot-scope="text, record">
            <a
              @click="handleSeeBackBid(record, '技术标')"
              v-if="record.bidSpecialistScoreVO"
              >查看</a
            >
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane
        key="4"
        tab="商业回标"
        v-if="
          form.requestStatusName !== '待技术评估' &&
          form.requestStatusName !== '待资质评估' &&
          form.requestStatusName !== '待评估'
        "
      >
        <a-table
          :columns="commerceColumns"
          :data-source="qualificationData"
          :pagination="false"
        >
          <template slot="commerceView" slot-scope="text, record">
            <span v-if="!text || text === 0">否</span>
            <a v-else @click="getBidSpecialistScoreVendor(record, '商业标')"
              >是，查看评论内容</a
            >
          </template>
          <template
            slot="operation"
            slot-scope="text, record"
            v-if="form.requestStatusName !== '已归档'"
          >
            <a
              @click="handleScore(record, '商业标')"
              v-if="record.bidSpecialistScoreVO"
              >评分</a
            >
          </template>
          <template slot="seeBackBid" slot-scope="text, record">
            <a
              @click="handleSeeBackBid(record, '商业标')"
              v-if="record.bidSpecialistScoreVO"
              >查看</a
            >
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <!-- 评论详情 -->
    <a-modal
      title="评论详情"
      :visible="visible"
      width="60%"
      @cancel="visible = false"
    >
      <a-collapse v-model="detailKey">
        <a-collapse-panel
          v-for="(item, index) in detailData"
          :key="index + ''"
          :header="
            index === 0 ? '当前轮次：' + item.round : '历史轮次：' + item.round
          "
        >
          <a-table
            :columns="detailColumns"
            :data-source="item.list"
            :pagination="false"
          >
            <template slot="whetherView" slot-scope="text">
              <span>{{ text === 1 ? '是' : '否' }}</span>
            </template>
          </a-table>
        </a-collapse-panel>
      </a-collapse>
    </a-modal>
    <!-- 评分 -->
    <a-modal
      title="评论详情"
      :visible="scoreVisible"
      width="700px"
      @ok="saveScore"
      @cancel="scoreVisible = false"
    >
      <a-form-model
        ref="formScore"
        :rules="scoreRules"
        :model="formScore"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="评分" prop="score">
              <a-input v-model="formScore.score" placeholder="请输入评分" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评论">
              <a-textarea
                v-model="formScore.comment"
                placeholder="请输入评论"
                :auto-size="{ minRows: 3, maxRows: 5 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <!-- 回标 -->
    <a-modal
      title="回标详情"
      :visible="backBidVisible"
      width="70%"
      @ok="backBidVisible = false"
      @cancel="backBidVisible = false"
    >
      <a-row>
        <a-col :span="24" v-if="type !== '商业标'">
          <a-tabs default-active-key="1">
            <a-tab-pane key="1" tab="详情">
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
                    <template slot="description" slot-scope="text">
                      <span style="white-space: pre-wrap">{{ text }}</span>
                    </template>
                  </a-table>
                </a-collapse-panel>
              </a-collapse>
            </a-tab-pane>
            <a-tab-pane key="2" tab="附件">
              <a-table
                size="small"
                :columns="columnsAnnex"
                :data-source="dataAnnex"
              >
                <span slot="operation" slot-scope="text, record">
                  <a-icon
                    class="iconstyle"
                    type="vertical-align-bottom"
                    @click="handleDownload(record)"
                  />
                </span>
              </a-table>
            </a-tab-pane>
          </a-tabs>
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
import { bidVendorGetAttachment, bidVendorGetRequest, bidSpecialistScoreVendorGetRequest, bidVendorGetWithVendor, bidSpecialistScoreUpdateScore, bidVendorDetailGetQueryRoundMap, bidSpecialistScoreWhetherView, attachmentGetfile } from "@/services/source/bidding";
import { mapGetters } from "vuex";
import moment from 'moment'
const columns = [
  {
    title: '供应商',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  {
    title: '回复时间',
    dataIndex: 'replyTime',
    key: 'replyTime',
    customRender: (text, record, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss') : ''
    },
  },
  {
    title: '回复状态',
    dataIndex: 'vendorStatus',
    key: 'vendorStatus',
  },
  {
    title: '答复回合',
    dataIndex: 'round',
    key: 'round',
  },
  {
    title: '资质参数是否已查看',
    dataIndex: 'qualificationView',
    key: 'qualificationView',
    scopedSlots: { customRender: 'qualificationView' },
  },
  {
    title: '技术回标是否已查看',
    dataIndex: 'technologyView',
    key: 'technologyView',
    scopedSlots: { customRender: 'technologyView' },
  },
  {
    title: '商业回标是否已查看',
    dataIndex: 'commerceView',
    key: 'commerceView',
    scopedSlots: { customRender: 'commerceView' },
  }
];
const detailColumns = [
  {
    title: '委员',
    dataIndex: 'sysUserInfoVO',
    key: 'bidVendorName',
    customRender: (text, record, index) => {
      return text ? text.name : ''
    },
  },
  {
    title: '回复是否已查看',
    dataIndex: 'whetherView',
    key: 'whetherView',
    scopedSlots: { customRender: 'whetherView' },
  },
  {
    title: '查看时间',
    dataIndex: 'viewTime',
    key: 'viewTime',
    customRender: (text, record, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss') : ''
    },
  },
  {
    title: '评分',
    dataIndex: 'score',
    key: 'score',
  },
  {
    title: '评论时间',
    dataIndex: 'commentTime',
    key: 'commentTime',
    customRender: (text, record, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss') : ''
    },
  },
  {
    title: '评论',
    dataIndex: 'comment',
    key: 'comment',
  },
]
const commerceColumns = [
  {
    title: '供应商',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  {
    title: '回复是否已查看',
    dataIndex: 'commerceView',
    key: 'commerceView',
    scopedSlots: { customRender: 'commerceView' },
  },
  {
    title: '查看回标',
    dataIndex: 'seeBackBid',
    key: 'seeBackBid',
    scopedSlots: { customRender: 'seeBackBid' },
  },
  {
    title: '评分',
    dataIndex: 'bidSpecialistScoreVO',
    key: 'score',
    customRender: (text, record, index) => {
      return text ? text.score : ''
    },
  },
  {
    title: '评论',
    dataIndex: 'bidSpecialistScoreVO',
    key: 'comment',
    customRender: (text, record, index) => {
      return text ? text.comment : ''
    },
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
]
const technologyColumns = [
  {
    title: '供应商',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  {
    title: '回复是否已查看',
    dataIndex: 'technologyView',
    key: 'technologyView',
    scopedSlots: { customRender: 'technologyView' },
  },
  {
    title: '查看回标',
    dataIndex: 'seeBackBid',
    key: 'seeBackBid',
    scopedSlots: { customRender: 'seeBackBid' },
  },
  {
    title: '评分',
    dataIndex: 'bidSpecialistScoreVO',
    key: 'score',
    customRender: (text, record, index) => {
      return text ? text.score : ''
    },
  },
  {
    title: '评论',
    dataIndex: 'bidSpecialistScoreVO',
    key: 'comment',
    customRender: (text, record, index) => {
      return text ? text.comment : ''
    },
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
]
const qualificationColumns = [
  {
    title: '供应商',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  {
    title: '回复是否已查看',
    dataIndex: 'qualificationView',
    key: 'qualificationView',
    scopedSlots: { customRender: 'qualificationView' },
  },
  {
    title: '查看回标',
    dataIndex: 'seeBackBid',
    key: 'seeBackBid',
    scopedSlots: { customRender: 'seeBackBid' },
  },
  {
    title: '评分',
    dataIndex: 'bidSpecialistScoreVO',
    key: 'score',
    customRender: (text, record, index) => {
      return text ? text.score : ''
    },
  },
  {
    title: '评论',
    dataIndex: 'bidSpecialistScoreVO',
    key: 'comment',
    customRender: (text, record, index) => {
      return text ? text.comment : ''
    },
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
]
const backColumns = [
  {
    title: '标题',
    dataIndex: 'name',
    width: 150,
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
    scopedSlots: { customRender: 'explaination' },
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
const columnsAnnex = [
  {
    title: '附件名称',
    dataIndex: 'attachmentName',
    key: 'attachmentName',
  },
  {
    title: '说明',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: '上传时间',
    dataIndex: 'uploadingDate',
    key: 'uploadingDate',
    customRender: (text, record, index) => {
      return moment(parseInt(text)).format('YYYY-MM-DD')
    },
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
]
export default {
  name: "assessment",
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
      domesticColumns,
      internationalColumns,
      type: '',
      columnsAnnex,
      dataAnnex: [],
      commerceColumns,
      technologyColumns,
      backBidVisible: false,
      backColumns,
      backData: [],
      formScore: {
        score: '',
        comment: ''
      },
      scoreId: '',
      scoreRules: {
        score: [{ required: true, message: '请输入评分', trigger: 'blur' }],
      },
      scoreVisible: false,
      visible: false,
      backActiveKey: ['0'],
      detailKey: ['0'],
      activeKey: '1',
      columns,
      invitationCount: 0,
      replyCount: 0,
      data: [
      ],
      detailColumns,
      detailData: [],
      qualificationColumns,
      qualificationData: [], // 资质参数
    }
  },
  computed: {
    ...mapGetters("account", ["user"])
  },
  mounted () {
    // this.form = JSON.parse(sessionStorage.getItem('biddingForm'))
    this.getDataList()
  },
  methods: {
    // 下载文件
    async handleDownload (row) {
      const data = {
        path: row.attachmentUrl
      }
      const result = await attachmentGetfile(data)
      if (result.code === 0) {
        let link = document.createElement("a");
        link.style.display = "none";
        link.href = result.data;
        link.download = row.attachmentName + '.' + row.attachmentType
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(a)
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询附件数据
    async getBidVendorGetAttachment (row, type) {
      const data = {
        bidType: type,
        currentPage: -1,
        bidSheetId: row.bidSheetId,
        bidVendorId: row.id,
      }
      const resutlt = await bidVendorGetAttachment(data)
      if (resutlt.code === 0) {
        this.dataAnnex = resutlt.data.records ? resutlt.data.records : []
      } else {
        this.$message.error(resutlt.message)
      }
    },
    // 更新评分中 是否查看 字段并更新供应商中 是否查看 字段
    async putBidSpecialistScoreWhetherView (row, type) {
      const data = {
        id: row.bidSpecialistScoreVO.id,
        bidType: type,
        bidVendorId: row.bidSpecialistScoreVO.bidVendorId,
        viewTime: moment().format('YYYY-MM-DD HH:mm:ss'),
        whetherView: 1
      }
      const result = await bidSpecialistScoreWhetherView(data)
      if (result.code === 0) {
        this.getBidVendoWithVendor()
        this.getDataList()
      } else {
        this.$message.error(result.message)
      }
    },
    // 查看回标
    async handleSeeBackBid (row, type) {
      this.type = type
      if (type !== '商业标') {
        this.getBidVendorGetAttachment(row, type)
      }
      this.putBidSpecialistScoreWhetherView(row, type)
      const data = {
        currentPage: -1,
        bidSheetId: row.bidSheetId,
        bidType: type,
        bidVendorId: row.id
      }
      const result = await bidVendorDetailGetQueryRoundMap(data)
      if (result.code === 0) {
        this.backBidVisible = true
        this.backData = result.data ? result.data : []
      } else {
        this.$message.error(result.message)
      }
    },
    // 保存评分以及评论
    saveScore () {
      this.$refs.formScore.validate(async valid => {
        if (valid) {
          const data = Object.assign({ commentTime: moment().format('YYYY-MM-DD HH:mm:ss') }, this.formScore)
          const result = await bidSpecialistScoreUpdateScore(data)
          if (result.code === 0) {
            this.scoreVisible = false
            this.getBidVendoWithVendor()
            this.$message.success('保存成功')
          } else {
            this.$message.error(result.message)
          }
        }
      })
    },
    // 编辑评分以及评论
    handleScore (row, type) {
      this.formScore = {
        score: '',
        comment: '',
        bidType: type,
        bidVendorId: row.id,
      }
      if (row.bidSpecialistScoreVO) {
        this.formScore.id = row.bidSpecialistScoreVO.id
        this.formScore.oldScore = row.bidSpecialistScoreVO.score
      }
      this.scoreVisible = true
    },
    // 查询招标投标方WithVendor
    async getBidVendoWithVendor () {
      let bidType = ''
      switch (this.activeKey) {
        case '2':
          bidType = '资质标'
          break
        case '3':
          bidType = '技术标'
          break
        case '4':
          bidType = '商业标'
          break
      }
      const data = {
        currentPage: -1,
        bidSheetId: this.form.id,
        specialistUserId: this.user.id,
        bidType: bidType,
        vendorStatus: '已回复'
      }
      const result = await bidVendorGetWithVendor(data)
      if (result.code === 0) {
        this.qualificationData = result.data.records ? result.data.records : []
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询招标专家评分(根据供应商)
    async getBidSpecialistScoreVendor (row, type) {
      this.visible = true
      const data = {
        currentPage: -1,
        bidType: type,
        bidVendorId: row.id
      }
      const result = await bidSpecialistScoreVendorGetRequest(data)
      if (result.code === 0) {
        let roundList = result.data ? result.data.map(item => { return { round: item.round, status: item.status } }) : []
        roundList.reverse() // 反转数据
        let dataList = []
        roundList.forEach(item => {
          const list = result.data.filter(f => f.round === item.round)
          const d = {
            status: item.status,
            round: item.round,
            list: list
          }
          dataList.push(d)
        })
        this.detailData = dataList
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取供应商信息
    async getDataList () {
      const data = {
        currentPage: -1,
        bidSheetId: this.form.id,
        vendorStatus: '已回复'
      }
      const result = await bidVendorGetRequest(data)
      if (result.code === 0) {
        this.replyCount = 0
        this.data = result.data.records ? result.data.records : []
        this.invitationCount = this.data.length
        this.data.forEach(item => {
          if (item.vendorStatus === '已回复') {
            this.replyCount++
          }
        })
      } else {
        this.$message.error(result.message)
      }
    },
  }
};
</script>

<style lang="less" scoped>
.fontClass {
  font-size: 22px;
  font-weight: 550;
}
</style>
