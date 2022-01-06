<template>
  <div>
    <a-row :gutter="[20, 10]" style="margin-bottom: 20px">
      <a-tabs v-model="activeKey">
        <a-tab-pane key="1" tab="资质标" v-if="form.qualificationTender === 1">
          <a-col
            :span="24"
            v-if="
              qualificationData.length > 0 &&
              !saveTrue &&
              form.requestStatusName !== '已归档'
            "
          >
            <a-button
              v-if="!qualificationTenderTrue"
              type="primary"
              @click="handleEdit('资质标')"
              >编辑回复</a-button
            >
            <a-button v-else @click="handleSubmit('资质标')" type="primary"
              >保存</a-button
            >
          </a-col>
          <a-col :span="24">
            <a-table
              size="small"
              :columns="columns"
              :data-source="qualificationData"
              :pagination="false"
            >
              <template slot="explaination" slot-scope="text">
                <span style="white-space: pre-wrap">{{ text }}</span>
              </template>
              <template slot="description" slot-scope="text, record">
                <span v-if="!record.editable" style="white-space: pre-wrap">{{
                  text
                }}</span>
                <a-textarea
                  v-else-if="record.editable"
                  :value="record.description"
                  auto-size
                  placeholder="请输入评论"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'description',
                        '资质标'
                      )
                  "
                >
                </a-textarea>
              </template>
            </a-table>
          </a-col>
          <a-col :span="24">
            <a-button
              type="primary"
              v-if="!saveTrue && form.requestStatusName !== '已归档'"
              @click="handleAnnexAdd('资质标')"
              >新增附件</a-button
            >
          </a-col>
          <a-col :span="24">
            <a-table
              size="small"
              :columns="columnsAnnex"
              :data-source="qualificationAnnex"
            >
              <span
                slot="operation"
                slot-scope="text, record"
                v-if="form.requestStatusName !== '已归档'"
              >
                <a-icon
                  type="edit"
                  theme="filled"
                  class="iconstyle"
                  @click="handleEditAnnex(record)"
                  style="padding-right: 10px"
                />
                <a-icon
                  type="delete"
                  @click="removeProjectAttachment(record)"
                  title="删除"
                  class="iconstyle"
                  style="padding-right: 10px"
                />
                <a-icon
                  class="iconstyle"
                  type="vertical-align-bottom"
                  @click="handleDownload(record)"
                />
              </span>
            </a-table>
          </a-col>
        </a-tab-pane>
        <a-tab-pane key="2" tab="技术标" v-if="form.technicalTender === 1">
          <a-col
            :span="24"
            v-if="
              technicalData.length > 0 &&
              !saveTrue &&
              form.requestStatusName !== '已归档'
            "
          >
            <a-button
              v-if="!technicalTenderTrue"
              type="primary"
              @click="handleEdit('技术标')"
              >编辑回复</a-button
            >
            <a-button v-else type="primary" @click="handleSubmit('技术标')"
              >保存</a-button
            >
          </a-col>
          <a-col :span="24">
            <a-table
              size="small"
              :columns="columns"
              :data-source="technicalData"
              :pagination="false"
            >
              <template slot="explaination" slot-scope="text">
                <span style="white-space: pre-wrap">{{ text }}</span>
              </template>
              <template slot="description" slot-scope="text, record">
                <span v-if="!record.editable" style="white-space: pre-wrap">{{
                  text
                }}</span>
                <a-textarea
                  v-else-if="record.editable"
                  :value="record.description"
                  auto-size
                  placeholder="请输入评论"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'description',
                        '技术标'
                      )
                  "
                >
                </a-textarea>
              </template>
            </a-table>
          </a-col>
          <a-col :span="24">
            <a-button
              type="primary"
              v-if="!saveTrue && form.requestStatusName !== '已归档'"
              @click="handleAnnexAdd('技术标')"
              >新增附件</a-button
            >
          </a-col>
          <a-col :span="24">
            <a-table
              size="small"
              :columns="columnsAnnex"
              :data-source="technicalAnnex"
            >
              <span
                slot="operation"
                slot-scope="text, record"
                v-if="form.requestStatusName !== '已归档'"
              >
                <a-icon
                  type="edit"
                  theme="filled"
                  class="iconstyle"
                  @click="handleEditAnnex(record)"
                  style="padding-right: 10px"
                />
                <a-icon
                  type="delete"
                  @click="removeProjectAttachment(record)"
                  title="删除"
                  class="iconstyle"
                  style="padding-right: 10px"
                />
                <a-icon
                  class="iconstyle"
                  type="vertical-align-bottom"
                  @click="handleDownload(record)"
                />
              </span>
            </a-table>
          </a-col>
        </a-tab-pane>
        <a-tab-pane key="3" tab="商业标">
          <a-col :span="4" class="fontClass">报价须知</a-col>
          <a-col :span="24">
            <a-table
              size="small"
              :columns="quotationColumns"
              :pagination="false"
              :data-source="quotationData"
            >
              <template slot="explaination" slot-scope="text">
                <span style="white-space: pre-wrap">{{ text }}</span>
              </template>
            </a-table>
          </a-col>
          <a-col :span="4" class="fontClass">国内费用</a-col>
          <a-col
            :span="20"
            ailgn="right"
            v-if="
              domesticData.length > 0 &&
              !saveTrue &&
              form.requestStatusName !== '已归档'
            "
          >
            <a-button
              v-if="!domesticTrue"
              type="primary"
              @click="handleEdit('国内费用')"
            >
              编辑回复
            </a-button>
            <a-button v-else type="primary" @click="handleSubmit('国内费用')"
              >保存</a-button
            >
          </a-col>
          <a-col :span="24">
            <a-table
              size="small"
              :columns="domesticColumns"
              :data-source="domesticData"
              :pagination="false"
            >
              <template slot="explaination" slot-scope="text">
                <span style="white-space: pre-wrap">{{ text }}</span>
              </template>
              <template slot="description" slot-scope="text, record">
                <span v-if="!record.editable" style="white-space: pre-wrap">{{
                  text
                }}</span>
                <a-textarea
                  v-else-if="record.editable"
                  :value="record.description"
                  auto-size
                  placeholder="请输入评论"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'description',
                        '国内费用'
                      )
                  "
                >
                </a-textarea>
              </template>
              <template slot="price" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.price"
                  type="number"
                  placeholder="请输入单价"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'price',
                        '国内费用'
                      )
                  "
                >
                </a-input>
              </template>
            </a-table>
          </a-col>
          <a-col :span="4" class="fontClass">国外费用</a-col>
          <a-col
            :span="20"
            ailgn="right"
            v-if="
              internationalData.length > 0 &&
              !saveTrue &&
              form.requestStatusName !== '已归档'
            "
          >
            <a-button
              v-if="!expensesTrue"
              type="primary"
              @click="handleEdit('国外费用')"
            >
              编辑回复
            </a-button>
            <a-button v-else type="primary" @click="handleSubmit('国外费用')"
              >保存</a-button
            >
          </a-col>
          <a-col :span="24">
            <a-table
              size="small"
              :columns="internationalColumns"
              :data-source="internationalData"
              :scroll="{ x: 2000 }"
              :pagination="false"
            >
              <template slot="explaination" slot-scope="text">
                <span style="white-space: pre-wrap">{{ text }}</span>
              </template>
              <template slot="price" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  type="number"
                  :value="record.price"
                  placeholder="请输入单价"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'price',
                        '国外费用'
                      )
                  "
                >
                </a-input>
              </template>
              <template slot="etd" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.etd"
                  placeholder="请输入ETD"
                  @change="
                    (e) =>
                      handleChange(e.target.value, record.id, 'etd', '国外费用')
                  "
                >
                </a-input>
              </template>
              <template slot="eta" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.eta"
                  placeholder="请输入ETA"
                  @change="
                    (e) =>
                      handleChange(e.target.value, record.id, 'eta', '国外费用')
                  "
                >
                </a-input>
              </template>
              <template slot="shippingSchedule" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.shippingSchedule"
                  placeholder="请输入船期（每周几）"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'shippingSchedule',
                        '国外费用'
                      )
                  "
                >
                </a-input>
              </template>
              <template slot="shippingCompany" slot-scope="text, record">
                <span v-if="!record.editable">{{ text }}</span>
                <a-input
                  v-else-if="record.editable"
                  :value="record.shippingCompany"
                  placeholder="船公司"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'shippingCompany',
                        '国外费用'
                      )
                  "
                >
                </a-input>
              </template>
              <template slot="description" slot-scope="text, record">
                <span v-if="!record.editable" style="white-space: pre-wrap">{{
                  text
                }}</span>
                <a-textarea
                  v-else-if="record.editable"
                  :value="record.description"
                  auto-size
                  placeholder="请输入评论"
                  @change="
                    (e) =>
                      handleChange(
                        e.target.value,
                        record.id,
                        'description',
                        '国外费用'
                      )
                  "
                >
                </a-textarea>
              </template>
            </a-table>
          </a-col>
        </a-tab-pane>
      </a-tabs>
    </a-row>
    <!-- 新增附件 -->
    <a-modal
      :title="annexTitle"
      :visible="visibleAnnex"
      width="700px"
      @ok="addFile"
      @cancel="visibleAnnex = false"
    >
      <a-form-model
        ref="formAnnex"
        :model="formAnnex"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="附件">
              <a-upload
                :file-list="fileList"
                :remove="handleRemove"
                :before-upload="beforeUpload"
                @change="handleChangeAnnex"
              >
                <a-button> <a-icon type="upload" /> 请选择文件 </a-button>
              </a-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="说明">
              <a-textarea
                v-model="formAnnex.description"
                placeholder="请输入说明"
                :auto-size="{ minRows: 3, maxRows: 5 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import {
  bidVendorDetailList,
  bidVendorDetailBatchUpdate,
  attachmentGetfile,
  bidVendorGetAttachment,
  bidVendorPostAttachment,
  bidVendorPutAttachment,
  bidVendorDeleteAttachment,
  uploadfilePostRequest
} from "@/services/source/bidding";
import { mapGetters } from "vuex";
import moment from 'moment'
const columns = [
  {
    title: '标题',
    width: 150,
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
    title: '评论',
    width: 500,
    dataIndex: 'description',
    key: 'description',
    scopedSlots: { customRender: 'description' },
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
    scopedSlots: { customRender: 'explaination' },
  },
  {
    title: '数量/基准价格',
    dataIndex: 'quantity',
    key: 'quantity',
    scopedSlots: { customRender: 'quantity' },
  },
  {
    title: '评论',
    width: 500,
    dataIndex: 'description',
    key: 'description',
    scopedSlots: { customRender: 'description' },
  }
]
// 报价须知
const quotationColumns = [
  {
    title: '标题',
    width: 150,
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '详情',
    dataIndex: 'explaination',
    key: 'explaination',
    scopedSlots: { customRender: 'explaination' },
  }
]
// 国内费用
const domesticColumns = [
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
    width: 300,
    dataIndex: 'description',
    key: 'description',
    scopedSlots: { customRender: 'description' },
  }
]
// 国外费用
const internationalColumns = [
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
    width: 300,
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
  name: "reply",
  props: {
    saveTrue: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapGetters("account", ["user"])
  },
  data () {
    return {
      formAnnex: {
        attachmentName: '', // 名称
        attachmentUrl: '', // 附件地址
        description: '', // 说明
        attachmentType: '', // 类型
      },
      annexTitle: '新增附件',
      visibleAnnex: false,
      fileList: [],
      qualificationTenderTrue: false,
      technicalTenderTrue: false,
      quotationTrue: false,
      domesticTrue: false,
      expensesTrue: false,
      activeKey: '1',
      columns,
      columns1,
      quotationColumns,
      domesticColumns,
      internationalColumns,
      columnsAnnex,
      technicalAnnex: [],
      qualificationAnnex: [],
      qualificationData: [], // 资质标
      technicalData: [], // 技术标
      quotationData: [], // 报价须知
      domesticData: [], // 国内费用
      internationalData: [], // 国外费用
      form: {}
    }
  },
  mounted () {
    this.form = JSON.parse(sessionStorage.getItem('supplyForm'))
    if (this.form.qualificationTender) {
      this.activeKey = '1'
    } else if (this.form.technicalTender) {
      this.activeKey = '2'
    } else {
      this.activeKey = '3'
    }
    this.getBidVendorDetail()
    this.getBidVendorGetAttachment()
  },
  methods: {
    // 查询附件数据
    async getBidVendorGetAttachment () {
      const data = {
        currentPage: -1,
        bidSheetId: this.form.id,
        bidVendorId: this.form.bidVendorId
      }
      const resutlt = await bidVendorGetAttachment(data)
      if (resutlt.code === 0) {
        this.technicalAnnex = resutlt.data.records ? resutlt.data.records.filter(f => f.bidType === '技术标') : []
        this.qualificationAnnex = resutlt.data.records ? resutlt.data.records.filter(f => f.bidType === '资质标') : []
      } else {
        this.$message.error(resutlt.message)
      }
    },
    handleChangeAnnex (info) {
      console.log(info)
      let fileList = [...info.fileList, info.file];
      fileList = fileList.slice(-1);
      this.fileList = fileList;
    },
    // 编辑附件
    handleEditAnnex (row) {
      this.fileList = [
        {
          uid: row.id,      // 文件唯一标识，建议设置为负数，防止和内部产生的 id 冲突
          name: row.attachmentName,   // 文件名
          xhr: row.attachmentUrl, // XMLHttpRequest Header
        }
      ]
      this.annexTitle = '编辑附件'
      this.formAnnex = Object.assign({}, row)
      this.visibleAnnex = true
    },
    // 新增附件
    handleAnnexAdd (type) {
      this.annexTitle = '新增附件'
      this.fileList = []
      this.formAnnex = {
        uploadingUser: this.user.userName, // 上传人
        uploadingDate: moment().format('YYYY-MM-DD HH:mm:ss'), // 上传时间
        attachmentName: '', // 名称
        attachmentUrl: '', // 附件地址
        description: '', // 说明
        attachmentType: '', // 类型
        bidSheetId: this.form.id, // 招标单Id
        bidType: type,
        bidVendorId: this.form.bidVendorId
      }
      this.visibleAnnex = true
    },
    // 保存附件
    async addFile () {
      const { fileList } = this;
      if (fileList.length === 0) {
        this.$message.warning("请选择文件");
      } else {
        const formData = new FormData();
        formData.append("uploadFile", fileList[0]);
        let result = null
        console.log(fileList[0])
        if (fileList[0].lastModified) {
          uploadfilePostRequest(formData, {}).then(async (response) => {
            if (response.code === 0) {
              this.formAnnex.attachmentName = response.data.attachmentName
              this.formAnnex.attachmentType = response.data.attachmentType
              this.formAnnex.attachmentUrl = response.data.attachmentUrl
              if (this.formAnnex.id) {
                // 编辑
                result = await bidVendorPutAttachment(this.formAnnex)
              } else {
                // 新增
                result = await bidVendorPostAttachment(this.formAnnex)
              }
              if (result.code === 0) {
                this.$message.success('保存成功')
                this.getBidVendorGetAttachment()
                this.visibleAnnex = false
              } else {
                this.$message.error(result.message)
              }
            } else {
              this.$message.error(response.message)
            }
          });
        } else {
          result = await bidVendorPutAttachment(this.formAnnex)
          if (result.code === 0) {
            this.$message.success('保存成功')
            this.getBidVendorGetAttachment()
            this.visibleAnnex = false
          } else {
            this.$message.error(result.message)
          }
        }
        this.fileList = [];
      }
    },
    // 删除附件
    async removeProjectAttachment (row) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        async onOk () {
          const result = await bidVendorDeleteAttachment(row.id)
          if (result.code === 0) {
            that.getBidVendorGetAttachment()
            that.$message.success('删除成功')
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 删除附件
    handleRemove (file) {
      const index = this.fileList.indexOf(file);
      const newFileList = this.fileList.slice();
      newFileList.splice(index, 1);
      this.fileList = newFileList;
    },
    beforeUpload (file) {
      this.fileList = [...this.fileList, file];
      return false;
    },
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
    // 保存回复
    async handleSubmit (type) {
      let dataList = []
      switch (type) {
        case '资质标':
          dataList = this.qualificationData
          break
        case '技术标':
          dataList = this.technicalData
          break
        case '报价须知':
          dataList = this.quotationData
          break
        case '国内费用':
          dataList = this.domesticData
          break
        case '国外费用':
          dataList = this.internationalData
          break
      }
      if (type === '国内费用') {
        let isTrue = true
        this.domesticData.forEach(item => {
          if (!item.price && isTrue) {
            this.$message.warning('请输入单价')
            isTrue = false
          } else
            if (!item.description && isTrue) {
              this.$message.warning('请输入评论')
              isTrue = false
            }
        })
        if (!isTrue) return
      } else
        if (type === '国外费用') {
          let isTrue = true
          this.internationalData.forEach(item => {
            if (!item.price && isTrue) {
              this.$message.warning('请输入单价')
              isTrue = false
            } else if (!item.etd && isTrue) {
              this.$message.warning('请输入ETD')
              isTrue = false
            } else if (!item.eta && isTrue) {
              this.$message.warning('请输入ETA')
              isTrue = false
            } else if (!item.shippingSchedule && isTrue) {
              this.$message.warning('请输入船期（每周几）')
              isTrue = false
            } else if (!item.shippingCompany && isTrue) {
              this.$message.warning('请输入船公司')
              isTrue = false
            } else
              if (!item.description && isTrue) {
                this.$message.warning('请输入评论')
                isTrue = false
              }
          })
          if (!isTrue) return
        }
      const result = await bidVendorDetailBatchUpdate(dataList)
      if (result.code === 0) {
        this.$message.success('保存成功')
        if (type === '资质标') {
          this.qualificationData.forEach(item => { this.$set(item, 'editable', false) })
          this.qualificationTenderTrue = false
        } else if (type === '技术标') {
          this.technicalData.forEach(item => { this.$set(item, 'editable', false) })
          this.technicalTenderTrue = false
        } else if (type === '报价须知') {
          this.quotationData.forEach(item => { this.$set(item, 'editable', false) })
          this.quotationTrue = false
        } else if (type === '国内费用') {
          this.domesticData.forEach(item => { this.$set(item, 'editable', false) })
          this.domesticTrue = false
        } else if (type === '国外费用') {
          this.internationalData.forEach(item => { this.$set(item, 'editable', false) })
          this.expensesTrue = false
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 编辑回复
    handleEdit (type) {
      if (type === '资质标') {
        this.qualificationData.forEach(item => { this.$set(item, 'editable', true) })
        this.qualificationTenderTrue = true
      } else if (type === '技术标') {
        this.technicalData.forEach(item => { this.$set(item, 'editable', true) })
        this.technicalTenderTrue = true
      } else if (type === '报价须知') {
        this.quotationData.forEach(item => { this.$set(item, 'editable', true) })
        this.quotationTrue = true
      } else if (type === '国内费用') {
        this.domesticData.forEach(item => { this.$set(item, 'editable', true) })
        this.domesticTrue = true
      } else if (type === '国外费用') {
        this.internationalData.forEach(item => { this.$set(item, 'editable', true) })
        this.expensesTrue = true
      }
    },
    handleChange (value, key, column, type) {
      let newData = [...this.qualificationData];
      switch (type) {
        case '资质标':
          newData = [...this.qualificationData];
          break
        case '技术标':
          newData = [...this.technicalData];
          break
        case '报价须知':
          newData = [...this.quotationData];
          break
        case '国内费用':
          newData = [...this.domesticData];
          break
        case '国外费用':
          newData = [...this.internationalData];
          break
      }
      const target = newData.filter(item => key === item.id)[0];
      if (target) {
        target[column] = value;
        switch (type) {
          case '资质标':
            this.qualificationData = newData;
            break
          case '技术标':
            this.technicalData = newData;
            break
          case '报价须知':
            this.quotationData = newData
            break
          case '国内费用':
            this.domesticData = newData
            break
          case '国外费用':
            this.internationalData = newData
            break
        }
      }
    },
    async getBidVendorDetail () {
      const data = {
        bidSheetId: this.form.id,
        bidVendorId: this.form.bidVendorId,
        currentPage: -1
      }
      const result = await bidVendorDetailList(data)
      if (result.code === 0) {
        this.qualificationData = result.data.records ? result.data.records.filter(f => f.bidType === '资质标') : []
        this.technicalData = result.data.records ? result.data.records.filter(f => f.bidType === '技术标') : []
        this.quotationData = result.data.records ? result.data.records.filter(f => f.type === '报价须知') : []
        this.domesticData = result.data.records ? result.data.records.filter(f => f.type === '国内费用') : []
        this.internationalData = result.data.records ? result.data.records.filter(f => f.type === '国外费用') : []
      } else {
        this.$message.error(result.message)
      }
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
