<template>
  <div>
    <a-tabs v-model="activeKey" @change="getBidDetailGetRequest">
      <a-tab-pane key="1" tab="资质标" v-if="form.qualificationTender">
        <a-row :gutter="20" style="padding-bottom: 5px">
          <a-col
            :span="4"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-select
              :disabled="
                form.requestStatusName === '已归档' ||
                qualificationData.find((f) => f.editable) !== undefined
              "
              :getPopupContainer="(triggerNode) => triggerNode.parentNode"
              v-model="qualificationTemplate"
              style="width: 100%"
              placeholder="请选择模板"
              @change="getBidDetailTemplateGetRequest"
            >
              <a-select-option value="">请选择</a-select-option>
              <a-select-option value="模板一">模板一</a-select-option>
              <a-select-option value="模板二">模板二</a-select-option>
              <a-select-option value="模板三">模板三</a-select-option>
            </a-select>
          </a-col>
          <a-col
            :span="20"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-space>
              <a-button
                type="primary"
                :disabled="
                  qualificationData.find((f) => f.editable) !== undefined
                "
                @click="add('资质标')"
              >
                新增
              </a-button>
              <a-button
                v-if="qualificationData.find((f) => f.editable) === undefined"
                type="primary"
                @click="handleEditAll('资质标')"
              >
                编辑
              </a-button>
              <a-button v-else type="primary" @click="handleCancel('资质标')">
                取消
              </a-button>
              <a-button
                type="primary"
                @click="saveSubmit('资质标')"
                :disabled="qualificationData.length <= 0"
              >
                提交
              </a-button>
            </a-space>
          </a-col>
        </a-row>
        <a-table
          :columns="columns"
          :data-source="qualificationData"
          :pagination="false"
        >
          <template slot="name" slot-scope="text, record">
            <span v-if="!record.editable">{{ text }}</span>
            <a-input
              v-else-if="record.editable"
              :value="record.name"
              placeholder="请输入标题"
              @change="(e) => handleChange(e.target.value, record.id, 'name')"
            >
            </a-input>
          </template>
          <template slot="explaination" slot-scope="text, record">
            <span v-if="!record.editable" style="white-space: pre-wrap">{{
              text
            }}</span>
            <a-textarea
              placeholder="请输入详情"
              v-else-if="record.editable"
              auto-size
              :value="record.explaination"
              @change="
                (e) => handleChange(e.target.value, record.id, 'explaination')
              "
            >
            </a-textarea>
          </template>
          <template
            slot="operation"
            slot-scope="text, record"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <span v-if="!record.editable">
              <a-icon
                type="edit"
                @click="handleEdit(record)"
                theme="filled"
                class="iconstyle"
                style="padding-right: 10px"
              />
              <a-icon
                type="delete"
                @click="handleDelete(record)"
                class="iconstyle"
              />
            </span>
            <span v-else>
              <a-icon type="check" @click="handleSave(record)" />
              <!-- <a-icon type="close" @click="handleDelete(record)" /> -->
            </span>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="技术标" v-if="form.technicalTender">
        <a-row :gutter="20" style="padding-bottom: 5px">
          <a-col
            :span="4"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-select
              :disabled="
                form.requestStatusName === '已归档' ||
                technicalData.find((f) => f.editable) !== undefined
              "
              :getPopupContainer="(triggerNode) => triggerNode.parentNode"
              v-model="technicalTemplate"
              style="width: 100%"
              placeholder="请选择模板"
              @change="getBidDetailTemplateGetRequest"
            >
              <a-select-option value="">请选择</a-select-option>
              <a-select-option value="模板一">模板一</a-select-option>
              <a-select-option value="模板二">模板二</a-select-option>
              <a-select-option value="模板三">模板三</a-select-option>
            </a-select>
          </a-col>
          <a-col
            :span="20"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-space>
              <a-button
                type="primary"
                :disabled="technicalData.find((f) => f.editable) !== undefined"
                @click="add('技术标')"
              >
                新增
              </a-button>
              <a-button
                v-if="technicalData.find((f) => f.editable) === undefined"
                type="primary"
                @click="handleEditAll('技术标')"
              >
                编辑
              </a-button>
              <a-button v-else type="primary" @click="handleCancel('技术标')">
                取消
              </a-button>
              <a-button
                type="primary"
                @click="saveSubmit('技术标')"
                :disabled="technicalData.length <= 0"
              >
                提交
              </a-button>
            </a-space>
          </a-col>
        </a-row>
        <a-table
          :columns="columns"
          :data-source="technicalData"
          :pagination="false"
        >
          <template slot="name" slot-scope="text, record">
            <span v-if="!record.editable">{{ text }}</span>
            <a-input
              v-else-if="record.editable"
              :value="record.name"
              placeholder="请输入标题"
              @change="(e) => handleChange(e.target.value, record.id, 'name')"
            >
            </a-input>
          </template>
          <template slot="explaination" slot-scope="text, record">
            <span v-if="!record.editable" style="white-space: pre-wrap">{{
              text
            }}</span>
            <a-textarea
              placeholder="请输入详情"
              v-else-if="record.editable"
              auto-size
              :value="record.explaination"
              @change="
                (e) => handleChange(e.target.value, record.id, 'explaination')
              "
            >
            </a-textarea>
          </template>
          <template
            slot="operation"
            slot-scope="text, record"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <span v-if="!record.editable">
              <a-icon
                type="edit"
                @click="handleEdit(record)"
                theme="filled"
                class="iconstyle"
                style="padding-right: 10px"
              />
              <a-icon
                type="delete"
                @click="handleDelete(record)"
                class="iconstyle"
              />
            </span>
            <span v-else>
              <a-icon type="check" @click="handleSave(record)" />
              <!-- <a-icon type="close" @click="handleDelete(record)" /> -->
            </span>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="3" tab="商业标">
        <a-row :gutter="[20, 20]">
          <a-col
            :span="4"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-select
              :disabled="
                form.requestStatusName === '已归档' ||
                quotationData.find((f) => f.editable) !== undefined ||
                domesticData.find((f) => f.editable) !== undefined ||
                internationalData.find((f) => f.editable) !== undefined
              "
              :getPopupContainer="(triggerNode) => triggerNode.parentNode"
              v-model="commercialTemplate"
              style="width: 100%"
              placeholder="请选择模板"
              @change="getBidDetailTemplateGetRequest"
            >
              <a-select-option value="">请选择</a-select-option>
              <a-select-option value="模板一">模板一</a-select-option>
              <a-select-option value="模板二">模板二</a-select-option>
              <a-select-option value="模板三">模板三</a-select-option>
            </a-select>
          </a-col>
          <a-col
            :span="20"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-button
              type="primary"
              @click="saveSubmit('商业标')"
              :disabled="
                quotationData.length <= 0 &&
                domesticData.length <= 0 &&
                internationalData.length <= 0
              "
            >
              提交
            </a-button>
          </a-col>
        </a-row>
        <a-row :gutter="[20, 20]">
          <a-col :span="4" class="fontClass">报价须知</a-col>
          <a-col
            :span="20"
            ailgn="right"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-space>
              <a-button
                type="primary"
                :disabled="quotationData.find((f) => f.editable) !== undefined"
                @click="add('报价须知')"
              >
                新增
              </a-button>
              <a-button
                v-if="quotationData.find((f) => f.editable) === undefined"
                type="primary"
                @click="handleEditAll('报价须知')"
              >
                编辑
              </a-button>
              <a-button v-else type="primary" @click="handleCancel('报价须知')">
                取消
              </a-button>
            </a-space>
          </a-col>
          <a-col :span="24">
            <a-table
              :columns="columns"
              :pagination="false"
              :data-source="quotationData"
            >
              <template slot="name" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.name"
                  placeholder="请输入标题"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'name',
                        '报价须知'
                      )
                  "
                >
                </a-input>
              </template>
              <template slot="explaination" slot-scope="text, record">
                <span v-if="!record.editable" style="white-space: pre-wrap">{{
                  text
                }}</span>
                <a-textarea
                  placeholder="请输入详情"
                  v-else-if="record.editable"
                  auto-size
                  :value="record.explaination"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'explaination',
                        '报价须知'
                      )
                  "
                >
                </a-textarea>
              </template>
              <template
                slot="operation"
                slot-scope="text, record"
                v-if="form.requestStatusName === '待发布' && !getUserInfo"
              >
                <span v-if="!record.editable">
                  <a-icon
                    type="edit"
                    @click="handleEdit(record, '报价须知')"
                    theme="filled"
                    class="iconstyle"
                    style="padding-right: 10px"
                  />
                  <a-icon
                    type="delete"
                    @click="handleDelete(record, '报价须知')"
                    class="iconstyle"
                  />
                </span>
                <span v-else>
                  <a-icon
                    type="check"
                    @click="handleSave(record, '报价须知')"
                  />
                  <!-- <a-icon type="close" @click="handleDelete(record)" /> -->
                </span>
              </template>
            </a-table>
          </a-col>
        </a-row>
        <a-row :gutter="[20, 20]">
          <a-col :span="4" class="fontClass">国内费用</a-col>
          <a-col
            :span="20"
            ailgn="right"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-space>
              <a-button
                type="primary"
                :disabled="domesticData.find((f) => f.editable) !== undefined"
                @click="add('国内费用')"
              >
                新增
              </a-button>
              <a-button
                v-if="domesticData.find((f) => f.editable) === undefined"
                type="primary"
                @click="handleEditAll('国内费用')"
              >
                编辑
              </a-button>
              <a-button v-else type="primary" @click="handleCancel('国内费用')">
                取消
              </a-button>
            </a-space>
          </a-col>
          <a-col :span="24">
            <a-table
              :columns="columns1"
              :data-source="domesticData"
              :pagination="false"
            >
              <template slot="name" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.name"
                  placeholder="请输入标题"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'name',
                        '国内费用'
                      )
                  "
                >
                </a-input>
              </template>
              <template slot="unit" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.unit"
                  placeholder="请输入单位"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'unit',
                        '国内费用'
                      )
                  "
                >
                </a-input>
              </template>
              <template slot="explaination" slot-scope="text, record">
                <span v-if="!record.editable" style="white-space: pre-wrap">{{
                  text
                }}</span>
                <a-textarea
                  placeholder="请输入详情"
                  v-else-if="record.editable"
                  auto-size
                  :value="record.explaination"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'explaination',
                        '国内费用'
                      )
                  "
                >
                </a-textarea>
              </template>
              <template slot="quantity" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  placeholder="请输入数量"
                  type="number"
                  v-else-if="record.editable"
                  :value="record.quantity"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'quantity',
                        '国内费用'
                      )
                  "
                >
                </a-input>
              </template>
              <template
                slot="operation"
                slot-scope="text, record"
                v-if="form.requestStatusName === '待发布' && !getUserInfo"
              >
                <span v-if="!record.editable">
                  <a-icon
                    type="edit"
                    @click="handleEdit(record, '国内费用')"
                    theme="filled"
                    class="iconstyle"
                    style="padding-right: 10px"
                  />
                  <a-icon
                    type="delete"
                    @click="handleDelete(record, '国内费用')"
                    class="iconstyle"
                  />
                </span>
                <span v-else>
                  <a-icon
                    type="check"
                    @click="handleSave(record, '国内费用')"
                  />
                  <!-- <a-icon type="close" @click="handleDelete(record)" /> -->
                </span>
              </template>
            </a-table>
          </a-col>
        </a-row>
        <a-row :gutter="[20, 20]">
          <a-col :span="4" class="fontClass">国外费用</a-col>
          <a-col
            :span="20"
            ailgn="right"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-space>
              <a-button
                type="primary"
                :disabled="
                  internationalData.find((f) => f.editable) !== undefined
                "
                @click="add('国外费用')"
              >
                新增
              </a-button>
              <a-button
                v-if="internationalData.find((f) => f.editable) === undefined"
                type="primary"
                @click="handleEditAll('国外费用')"
              >
                编辑
              </a-button>
              <a-button v-else type="primary" @click="handleCancel('国外费用')">
                取消
              </a-button>
            </a-space>
          </a-col>
          <a-col :span="24">
            <a-table
              :columns="columns1"
              :data-source="internationalData"
              :pagination="false"
            >
              <template slot="name" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.name"
                  placeholder="请输入标题"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'name',
                        '国外费用'
                      )
                  "
                >
                </a-input>
              </template>
              <template slot="unit" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.unit"
                  placeholder="请输入单位"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'unit',
                        '国外费用'
                      )
                  "
                >
                </a-input>
              </template>
              <template slot="explaination" slot-scope="text, record">
                <span v-if="!record.editable" style="white-space: pre-wrap">{{
                  text
                }}</span>
                <a-textarea
                  placeholder="请输入详情"
                  v-else-if="record.editable"
                  :value="record.explaination"
                  auto-size
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'explaination',
                        '国外费用'
                      )
                  "
                >
                </a-textarea>
              </template>
              <template slot="quantity" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  placeholder="请输入数量"
                  v-else-if="record.editable"
                  type="number"
                  :value="record.quantity"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'quantity',
                        '国外费用'
                      )
                  "
                >
                </a-input>
              </template>
              <template
                slot="operation"
                slot-scope="text, record"
                v-if="form.requestStatusName === '待发布' && !getUserInfo"
              >
                <span v-if="!record.editable">
                  <a-icon
                    type="edit"
                    @click="handleEdit(record, '国外费用')"
                    theme="filled"
                    class="iconstyle"
                    style="padding-right: 10px"
                  />
                  <a-icon
                    type="delete"
                    @click="handleDelete(record, '国外费用')"
                    class="iconstyle"
                  />
                </span>
                <span v-else>
                  <a-icon
                    type="check"
                    @click="handleSave(record, '国外费用')"
                  />
                  <!-- <a-icon type="close" @click="handleDelete(record)" /> -->
                </span>
              </template>
            </a-table>
          </a-col>
        </a-row>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
import { bidDetailGetRequest, bidDetailTemplateGetRequest, bidDetailBatchUpdate } from "@/services/source/bidding";
const columns = [
  {
    title: '标题',
    dataIndex: 'name',
    width: 250,
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
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    width: 100,
    scopedSlots: { customRender: 'operation' },
  }
];
const columns1 = [
  {
    title: '标题',
    width: 250,
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '详情',
    dataIndex: 'explaination',
    key: 'explaination',
    // customRender: (text) => {
    //   if (text) {
    //     console.log(text)
    //     let snArray = [];
    //     //如果返回的是逗号分割需要转化一下 snArray = text.replace(/\,/g,'\n')
    //     snArray = text.split(/\r{0,}\n/g);
    //     let br = <br></br>;
    //     let result = null;
    //     console.log(snArray)
    //     if (snArray.length < 2) {
    //       return text;
    //     }
    //     for (let i = 0; i < snArray.length; i++) {
    //       if (i == 0) {
    //         result = snArray[i];
    //       } else {
    //         result = <span>{result}{br}{snArray[i]}</span>;
    //       }
    //     }
    //     return text;
    //   }
    // },
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
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    width: 100,
    scopedSlots: { customRender: 'operation' },
  }
]
export default {
  name: "markList",
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
  // computed
  data () {
    return {
      qualificationTemplate: '', // 资质标模板选择
      technicalTemplate: '', // 技术标模板选择
      commercialTemplate: '', // 商业标模板选择
      activeKey: '1',
      columns,
      columns1,
      qualificationData: [
      ],
      technicalData: [],
      quotationData: [],
      domesticData: [],
      internationalData: [],
      qualificationDataOld: [],
      technicalDataOld: [],
      quotationDataOld: [],
      domesticDataOld: [],
      internationalDataOld: [],
      qualificationPagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      technicalPagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      commercialPagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      operationType: ''
    }
  },
  mounted () {
    // this.form = JSON.parse(sessionStorage.getItem('biddingForm'))
    this.init()
  },
  methods: {
    init () {
      if (this.form.qualificationTender) {
        this.activeKey = '1'
      } else if (this.form.technicalTender) {
        this.activeKey = '2'
      } else {
        this.activeKey = '3'
      }
      this.getBidDetailGetRequest()
    },
    // 取消编辑
    handleCancel (type) {
      if (type === '资质标') {
        if (this.operationType === 'add') {
          const index = this.qualificationData.findIndex(f => f.editable)
          this.qualificationData.splice(index, 1)
        } else {
          this.qualificationData = this.qualificationDataOld
        }
      } else if (type === '技术标') {
        if (this.operationType === 'add') {
          const index = this.technicalData.findIndex(f => f.editable)
          this.technicalData.splice(index, 1)
        } else {
          this.technicalData = this.technicalDataOld
        }
      } else if (type === '报价须知') {
        if (this.operationType === 'add') {
          const index = this.quotationData.findIndex(f => f.editable)
          this.quotationData.splice(index, 1)
        } else {
          this.quotationData = this.quotationDataOld
        }
      } else if (type === '国内费用') {
        if (this.operationType === 'add') {
          const index = this.domesticData.findIndex(f => f.editable)
          this.domesticData.splice(index, 1)
        } else {
          this.domesticData = this.domesticDataOld
        }
      } else if (type === '国外费用') {
        if (this.operationType === 'add') {
          const index = this.internationalData.findIndex(f => f.editable)
          this.internationalData.splice(index, 1)
        } else {
          this.internationalData = this.internationalDataOld
        }
      }
    },
    // 批量编辑
    handleEditAll (type) {
      this.operationType = 'edit'
      if (type === '资质标') {
        this.qualificationDataOld = JSON.parse(JSON.stringify(this.qualificationData))
        this.qualificationData.forEach(item => { this.$set(item, 'editable', true) })
      } else if (type === '技术标') {
        this.technicalDataOld = JSON.parse(JSON.stringify(this.technicalData))
        this.technicalData.forEach(item => { this.$set(item, 'editable', true) })
      } else if (type === '报价须知') {
        this.quotationDataOld = JSON.parse(JSON.stringify(this.quotationData))
        this.quotationData.forEach(item => { this.$set(item, 'editable', true) })
      } else if (type === '国内费用') {
        this.domesticDataOld = JSON.parse(JSON.stringify(this.domesticData))
        this.domesticData.forEach(item => { this.$set(item, 'editable', true) })
      } else if (type === '国外费用') {
        this.internationalDataOld = JSON.parse(JSON.stringify(this.internationalData))
        this.internationalData.forEach(item => { this.$set(item, 'editable', true) })
      }
    },
    // 提交
    async saveSubmit (type) {
      let data = {
        bidSheetId: this.form.id,
        bidType: type,
        bidDetailDTOS: this.qualificationData
      }
      if (type === '资质标' || type === '技术标') data.type = type
      let isTrue = true
      this.domesticData.forEach(item => {
        if (!item.unit || !item.quantity) {
          isTrue = false
        }
      })
      this.internationalData.forEach(item => {
        if (!item.unit || !item.quantity) {
          isTrue = false
        }
      })
      if (isTrue) {
        switch (this.activeKey) {
          case '1':
            this.qualificationData.forEach(item => {
              this.$delete(item, 'id')
            })
            data.bidDetailDTOS = this.qualificationData
            break
          case '2':
            this.technicalData.forEach(item => {
              this.$delete(item, 'id')
            })
            data.bidDetailDTOS = this.technicalData
            break
          case '3':
            const dataList = []
            this.quotationData.forEach(item => {
              this.$delete(item, 'id')
              dataList.push(item)
            })
            this.domesticData.forEach(item => {
              this.$delete(item, 'id')
              dataList.push(item)
            })
            this.internationalData.forEach(item => {
              this.$delete(item, 'id')
              dataList.push(item)
            })
            data.bidDetailDTOS = dataList
            break
        }
        const result = await bidDetailBatchUpdate(data)
        if (result.code === 0) {
          this.handleCancel(type)
          this.getBidDetailGetRequest()
          this.$message.success('保存成功')
        } else {
          this.$message.error(result.message)
        }
      } else {
        this.$message.warning('请输入单位、数量')
        return
      }
    },
    // 添加数据
    add (type) {
      this.operationType = 'add'
      if (type === '资质标') {
        const data = this.qualificationData.find(f => f.editable)
        if (data) {
          this.$message.warning('请先保存数据')
          return
        }
        this.qualificationData.unshift({
          id: this.qualificationData.length,
          editable: true,
          name: '',
          explaination: ''
        })
      } else if (type === '技术标') {
        const data = this.technicalData.find(f => f.editable)
        if (data) {
          this.$message.warning('请先保存数据')
          return
        }
        this.technicalData.unshift({
          id: this.technicalData.length,
          editable: true,
          name: '',
          explaination: ''
        })
      } else if (type === '报价须知') {
        const data = this.quotationData.find(f => f.editable)
        if (data) {
          this.$message.warning('请先保存数据')
          return
        }
        this.quotationData.unshift({
          id: this.quotationData.length,
          editable: true,
          name: '',
          type: type,
          explaination: ''
        })
      } else if (type === '国内费用') {
        const data = this.domesticData.find(f => f.editable)
        if (data) {
          this.$message.warning('请先保存数据')
          return
        }
        this.domesticData.unshift({
          id: this.domesticData.length,
          editable: true,
          name: '',
          type: type,
          explaination: ''
        })
      } else if (type === '国外费用') {
        const data = this.internationalData.find(f => f.editable)
        if (data) {
          this.$message.warning('请先保存数据')
          return
        }
        this.internationalData.unshift({
          id: this.internationalData.length,
          editable: true,
          name: '',
          type: type,
          explaination: ''
        })
      }
    },
    // 删除数据
    handleDelete (row, type) {
      this.operationType = ''
      let index = null
      switch (this.activeKey) {
        case '1':
          index = this.qualificationData.findIndex(f => f.id === row.id)
          if (index !== null) this.qualificationData.splice(index, 1)
          break
        case '2':
          index = this.technicalData.findIndex(f => f.id === row.id)
          if (index !== null) this.technicalData.splice(index, 1)
          break
        case '3':
          if (type === '报价须知') {
            index = this.quotationData.findIndex(f => f.id === row.id)
            if (index !== null) this.quotationData.splice(index, 1)
          } else if (type === '国内费用') {
            index = this.domesticData.findIndex(f => f.id === row.id)
            if (index !== null) this.domesticData.splice(index, 1)
          } else if (type === '国外费用') {
            index = this.internationalData.findIndex(f => f.id === row.id)
            if (index !== null) this.internationalData.splice(index, 1)
          }
          break
      }
    },
    // 保存数据
    handleSave (row, type) {
      if (!row.name) {
        this.$message.warning('请输入标题')
        return
      } else if ((type === '国内费用' || type === '国外费用') && !row.unit) {
        this.$message.warning('请输入单位')
        return
      } else if ((type === '国内费用' || type === '国外费用') && !row.quantity) {
        this.$message.warning('请输入数量')
        return
      }
      let newData = [...this.qualificationData];
      switch (this.activeKey) {
        case '1':
          newData = [...this.qualificationData];
          break
        case '2':
          newData = [...this.technicalData];
          break
        case '3':
          if (type === '报价须知') {
            newData = [...this.quotationData];
          } else if (type === '国内费用') {
            newData = [...this.domesticData];
          } else if (type === '国外费用') {
            newData = [...this.internationalData];
          }
          break
      }
      const target = newData.filter(item => row.id === item.id)[0];
      if (target) {
        target.editable = false;
        switch (this.activeKey) {
          case '1':
            this.qualificationData = newData;
            break
          case '2':
            this.technicalData = newData;
            break
          case '3':
            if (type === '报价须知') {
              this.quotationData = newData
            } else if (type === '国内费用') {
              this.domesticData = newData
            } else if (type === '国外费用') {
              this.internationalData = newData
            }
            break
        }
      }
    },
    handleChange (value, key, column, type) {
      let newData = [...this.qualificationData];
      switch (this.activeKey) {
        case '1':
          newData = [...this.qualificationData];
          break
        case '2':
          newData = [...this.technicalData];
          break
        case '3':
          if (type === '报价须知') {
            newData = [...this.quotationData];
          } else if (type === '国内费用') {
            newData = [...this.domesticData];
          } else if (type === '国外费用') {
            newData = [...this.internationalData];
          }
          break
      }
      const target = newData.filter(item => key === item.id)[0];
      if (target) {
        target[column] = value;
        switch (this.activeKey) {
          case '1':
            this.qualificationData = newData;
            break
          case '2':
            this.technicalData = newData;
            break
          case '3':
            if (type === '报价须知') {
              this.quotationData = newData
            } else if (type === '国内费用') {
              this.domesticData = newData
            } else if (type === '国外费用') {
              this.internationalData = newData
            }
            break
        }
      }
    },
    // 编辑表格内容
    handleEdit (row, type) {
      this.operationType = 'edit'
      let newData = [...this.qualificationData];
      switch (this.activeKey) {
        case '1':
          this.qualificationDataOld = JSON.parse(JSON.stringify(this.qualificationData))
          newData = [...this.qualificationData];
          break
        case '2':
          newData = [...this.technicalData];
          this.technicalDataOld = JSON.parse(JSON.stringify(this.technicalData))
          break
        case '3':
          if (type === '报价须知') {
            newData = [...this.quotationData];
            this.quotationDataOld = JSON.parse(JSON.stringify(this.quotationData))
          } else if (type === '国内费用') {
            newData = [...this.domesticData];
            this.domesticDataOld = JSON.parse(JSON.stringify(this.domesticData))
          } else if (type === '国外费用') {
            newData = [...this.internationalData];
            this.internationalDataOld = JSON.parse(JSON.stringify(this.internationalData))
          }
          break
      }
      const target = newData.filter(item => row.id === item.id)[0];
      if (target) {
        target.editable = true;
        switch (this.activeKey) {
          case '1':
            this.qualificationData = newData;
            break
          case '2':
            this.technicalData = newData;
            break
          case '3':
            if (type === '报价须知') {
              this.quotationData = newData
            } else if (type === '国内费用') {
              this.domesticData = newData
            } else if (type === '国外费用') {
              this.internationalData = newData
            }
            break
        }
      }
    },
    // 查询模板内容
    async getBidDetailTemplateGetRequest (e) {
      let bidType = ''
      switch (this.activeKey) {
        case '1':
          bidType = '资质标'
          break
        case '2':
          bidType = '技术标'
          break
        case '3':
          bidType = '商业标'
          break
      }
      const data = {
        currentPage: -1,
        bidType: bidType,
        classify: e
      }
      const result = await bidDetailTemplateGetRequest(data)
      if (result.code === 0) {
        switch (this.activeKey) {
          case '1':
            this.qualificationData = result.data.records ? result.data.records : []
            break
          case '2':
            this.technicalData = result.data.records ? result.data.records : []
            break
          case '3':
            this.quotationData = result.data.records ? result.data.records.filter(f => f.type === '报价须知') : []
            this.domesticData = result.data.records ? result.data.records.filter(f => f.type === '国内费用') : []
            this.internationalData = result.data.records ? result.data.records.filter(f => f.type === '国外费用') : []
            break
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询标的内容
    async getBidDetailGetRequest () {
      let bidType = ''
      let currentPage = ''
      let pageSize = ''
      switch (this.activeKey) {
        case '1':
          bidType = '资质标'
          currentPage = this.qualificationPagination.current
          pageSize = this.qualificationPagination.pageSize
          break
        case '2':
          bidType = '技术标'
          currentPage = this.technicalPagination.current
          pageSize = this.technicalPagination.pageSize
          break
        case '3':
          bidType = '商业标'
          currentPage = this.commercialPagination.current
          pageSize = this.commercialPagination.pageSize
          break
      }
      const data = {
        bidType: bidType,
        bidSheetId: this.form.id,
        currentPage: -1,
      }
      const result = await bidDetailGetRequest(data)
      if (result.code === 0) {
        switch (this.activeKey) {
          case '1':
            this.qualificationPagination.total = result.data.total
            this.qualificationData = result.data.records ? result.data.records : []
            break
          case '2':
            this.technicalData = result.data.records ? result.data.records : []
            this.technicalPagination.total = result.data.total
            break
          case '3':
            this.commercialPagination.total = result.data.total
            this.quotationData = result.data.records ? result.data.records.filter(f => f.type === '报价须知') : []
            this.domesticData = result.data.records ? result.data.records.filter(f => f.type === '国内费用') : []
            this.internationalData = result.data.records ? result.data.records.filter(f => f.type === '国外费用') : []
            break
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getBidDetailGetRequest()
    }
  }
};
</script>

<style lang="less" scoped>
.fontClass {
  font-size: 22px;
  font-weight: 550;
}
</style>
