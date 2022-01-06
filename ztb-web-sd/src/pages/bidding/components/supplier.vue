<template>
  <div>
    <a-tabs v-model="activeKey">
      <a-tab-pane key="1" tab="供应商">
        <a-row style="padding-bottom: 5px">
          <a-col :span="24">
            <a-button
              type="primary"
              @click="handleVendorAdd"
              v-if="form.requestStatusName === '待发布' && !getUserInfo"
            >
              新增
            </a-button>
          </a-col>
        </a-row>
        <a-table
          :pagination="pagination"
          :columns="columns"
          :data-source="data"
          @change="tableVendorChange"
        >
          <span
            slot="operation"
            slot-scope="text, record"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-icon
              @click="handleVendorDelete(record.id)"
              type="delete"
              title="删除"
              class="iconstyle"
            />
          </span>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="附件">
        <a-row style="padding-bottom: 5px">
          <a-col :span="24">
            <a-button
              type="primary"
              @click="handleAnnexAdd"
              v-if="form.requestStatusName === '待发布' && !getUserInfo"
            >
              新增
            </a-button>
          </a-col>
        </a-row>
        <a-table
          :pagination="paginationAnnex"
          :columns="columnsAnnex"
          :data-source="dataAnnex"
          @change="tableAnnexChange"
        >
          <span
            slot="operation"
            slot-scope="text, record"
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
          >
            <a-icon
              type="edit"
              theme="filled"
              class="iconstyle"
              @click="handleEdit(record)"
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
      </a-tab-pane>
    </a-tabs>
    <!-- 新增供应商 -->
    <a-modal
      title="新增供应商"
      :visible="visible"
      width="65%"
      @ok="handleSave"
      @cancel="visible = false"
    >
      <a-form-model
        :model="VendorForm"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18, offset: 0 }"
      >
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="关键字">
              <a-input
                v-model="VendorForm.keyword"
                @pressEnter="getVendorList"
                placeholder="请输入关键字"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="4" align="right">
            <a-button type="primary" @click="getVendorList">查询</a-button>
          </a-col>
        </a-row>
      </a-form-model>
      <a-table
        rowKey="id"
        :rowSelection="{
          type: 'radio',
          columnWidth: 50,
          selectedRowKeys: selectedRowKeys,
          onChange: selectChange,
        }"
        :pagination="paginationVendor"
        @change="tableChange"
        :columns="VendorColumns"
        :data-source="vendorList"
      >
      </a-table>
    </a-modal>
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
                @change="handleChange"
              >
                <a-button> <a-icon type="upload" /> 请选择文件 </a-button>
              </a-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商是否可见">
              <a-radio-group v-model="formAnnex.vendorVisible">
                <a-radio :value="1"> 是 </a-radio>
                <a-radio :value="0"> 否 </a-radio>
              </a-radio-group>
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
import { getVendorwithVendorPageVO, paidAttachmentDeleteRequest } from "@/services/source/contend";
import { uploadfilePostRequest } from "@/services/system/user";
import { bidVendorPostInsertWithDefault, bidVendorGetRequest, attachmentGetfile, bidVendorDeleteRequest, bidAttachmentPostRequest, bidAttachmentPutRequest, bidAttachmentGetRequest } from "@/services/source/bidding";
import moment from 'moment'
const columns = [
  {
    title: '供应商名称',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  // {
  //   title: '城市',
  //   dataIndex: 'city',
  //   key: 'city',
  // },
  {
    title: '联系人',
    dataIndex: 'vendorUserName',
    key: 'vendorUserName',
  },
  // {
  //   title: '联系电话',
  //   dataIndex: 'phone',
  //   key: 'phone',
  // },
  // {
  //   title: '邮箱',
  //   dataIndex: 'email',
  //   key: 'email',
  // },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
];
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
    title: '上传人',
    dataIndex: 'uploadingUserChineseName',
    key: 'uploadingUserChineseName',
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
    title: '供应商可见',
    dataIndex: 'vendorVisible',
    key: 'vendorVisible',
    customRender: (text, record, index) => {
      return text === 1 ? '是' : '否'
    },
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
]
const VendorColumns = [
  {
    title: '供应商名称',
    dataIndex: 'vendorVO',
    key: 'vendorName',
    customRender: (text, record, index) => {
      return text ? text.name : ''
    },
  },
  {
    title: '供应商城市',
    dataIndex: 'vendorVO',
    key: 'vendorCity',
    customRender: (text, record, index) => {
      return text ? text.city : ''
    },
  },
  {
    title: '供应商地址',
    dataIndex: 'vendorVO',
    key: 'vendorAddress',
    customRender: (text, record, index) => {
      return text ? text.registeredAddress : ''
    },
  },
  {
    title: '联系人',
    dataIndex: 'sysUserInfoVO',
    width: 80,
    key: 'userName',
    customRender: (text, record, index) => {
      return text ? text.name : ''
    },
  },
  {
    title: '联系方式',
    dataIndex: 'sysUserInfoVO',
    key: 'phone',
    customRender: (text, record, index) => {
      return text ? text.phone : ''
    },
  }
]
export default {
  props: {
    user: {
      type: Object,
      default: {}
    },
    form: {
      type: Object,
      default: {}
    },
    getUserInfo: {
      type: Boolean,
      default: false
    }
  },
  name: "supplier",
  data () {
    return {
      selectedRowKeys: [],
      VendorColumns,
      annexTitle: '',
      fileList: [],
      linkman: undefined,
      vendorId: undefined,
      activeKey: '1',
      visible: false,
      visibleAnnex: false,
      formInline: {
        linkman: '', // 联系人
        vendorName: undefined, // 供应商名称
      },
      formAnnex: {
        attachmentName: '', // 名称
        attachmentUrl: '', // 附件地址
        vendorVisible: 0, // 供应商是否可见
        description: '', // 说明
        attachmentType: '', // 类型
      },
      vendorList: [],
      userList: [],
      columns,
      columnsAnnex,
      data: [],
      // form: {
      //   id: ''
      // },
      rules: {
        vendorName: [{ required: true, message: '请选择供应商', trigger: 'change' }],
        linkman: [{ required: true, message: '请选择联系人', trigger: 'change' }]
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      paginationAnnex: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      paginationVendor: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      VendorForm: {
        keyword: ''
      },
      selectedRows: null,
      dataAnnex: []
    }
  },
  mounted () {
    // this.form = JSON.parse(sessionStorage.getItem('biddingForm'))
    this.init()
  },
  methods: {
    init () {
      this.getDataList()
      this.getAnnexDataList()
      this.getVendorList()
    },
    // 选中事件
    selectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows.length > 0 ? selectedRows[0] : null
      if (selectedRows && selectedRows.length > 0) {
        const vendorVO = selectedRows[0].vendorVO
        const sysUserInfoVO = selectedRows[0].sysUserInfoVO
        this.formInline.vendorName = vendorVO ? vendorVO.name : ''
        this.formInline.vendorUserName = sysUserInfoVO ? sysUserInfoVO.name : ''
        this.formInline.city = vendorVO ? vendorVO.city : ''
        this.formInline.vendorId = vendorVO ? vendorVO.id : ''
        this.formInline.vendorUserId = sysUserInfoVO ? sysUserInfoVO.id : ''
        this.formInline.linkman = sysUserInfoVO ? sysUserInfoVO.name : ''
        this.formInline.email = sysUserInfoVO ? sysUserInfoVO.mail : ''
        this.formInline.phone = sysUserInfoVO ? sysUserInfoVO.phone : ''
      }
    },
    handleChange (info) {
      let fileList = [...info.fileList, info.file];
      fileList = fileList.slice(-1);
      this.fileList = fileList;
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
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
        document.body.removeChild(link)
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询附件列表
    async getAnnexDataList () {
      const data = {
        bidSheetId: this.form.id,
        currentPage: this.paginationAnnex.current,
        pageSize: this.paginationAnnex.pageSize
      }
      const result = await bidAttachmentGetRequest(data)
      if (result.code === 0) {
        this.dataAnnex = result.data.records
        this.paginationAnnex.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 编辑附件
    handleEdit (row) {
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
    handleAnnexAdd () {
      this.annexTitle = '新增附件'
      this.formAnnex = {
        uploadingUser: this.user.userName, // 上传人
        uploadingDate: moment().format('YYYY-MM-DD'), // 上传时间
        attachmentName: '', // 名称
        attachmentUrl: '', // 附件地址
        vendorVisible: 0, // 供应商是否可见
        description: '', // 说明
        attachmentType: '', // 类型
        bidSheetId: this.form.id // 招标单Id
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
        if (fileList[0].lastModified) {
          uploadfilePostRequest(formData, {}).then(async (response) => {
            if (response.code === 0) {
              this.formAnnex.attachmentName = response.data.attachmentName
              this.formAnnex.attachmentType = response.data.attachmentType
              this.formAnnex.attachmentUrl = response.data.attachmentUrl
              if (this.formAnnex.id) {
                // 编辑
                result = await bidAttachmentPutRequest(this.formAnnex)
              } else {
                // 新增
                result = await bidAttachmentPostRequest(this.formAnnex)
              }
              if (result.code === 0) {
                this.$message.success('保存成功')
                this.getAnnexDataList()
                this.visibleAnnex = false
              } else {
                this.$message.error(response.message)
              }
            } else {
              this.$message.error(response.message)
            }
          });
        } else {
          result = await bidAttachmentPutRequest(this.formAnnex)
          if (result.code === 0) {
            this.$message.success('保存成功')
            this.getAnnexDataList()
            this.visibleAnnex = false
          } else {
            this.$message.error(response.message)
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
          const result = await paidAttachmentDeleteRequest(row.id)
          if (result.code === 0) {
            that.getAnnexDataList()
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
    // 新增供应商
    handleVendorAdd () {
      this.vendorId = undefined
      this.linkman = undefined
      this.selectedRowKeys = []
      this.selectedRows = null
      this.formInline = {
        linkman: '', // 联系人
        vendorName: undefined, // 供应商名称
        bidSheetId: this.form.id
      }
      this.visible = true
    },
    // 查询供应商WithUser
    async getVendorList () {
      this.selectedRowKeys = []
      this.selectedRows = null
      const data = {
        currentPage: this.paginationVendor.current,
        pageSize: this.paginationVendor.pageSize,
        keyword: this.VendorForm.keyword
      }
      const result = await getVendorwithVendorPageVO(data)
      if (result.code === 0) {
        this.vendorList = result.data ? result.data.records : []
        this.paginationVendor.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 分页改变
    tableChange (row) {
      this.paginationVendor = row
      this.getVendorList()
    },
    // 查询供应商
    async getDataList () {
      this.selectedRows = null
      this.selectedRowKeys = []
      const data = {
        bidSheetId: this.form.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await bidVendorGetRequest(data)
      if (result.code === 0) {
        this.data = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 保存供应商
    async handleSave () {
      if (this.selectedRows && this.selectedRowKeys.length > 0) {
        const result = await bidVendorPostInsertWithDefault(this.formInline)
        if (result.code === 0) {
          this.$message.success('保存成功')
          this.visible = false
          this.getDataList()
        } else {
          this.$message.error(result.message)
        }
      } else {
        this.$message.warning('请选择供应商')
      }
    },
    // 删除供应商
    async handleVendorDelete (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await bidVendorDeleteRequest(id)
          if (result.code === 0) {
            that.$message.success('删除成功')
            that.getDataList()
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 供应商分页改变
    tableVendorChange (row) {
      this.pagination = row
      this.getDataList()
    },
    // 附件分页改变
    tableAnnexChange (row) {
      this.paginationAnnex = row
      this.getAnnexDataList()
    },
  }
};
</script>

<style lang="less" scoped>
</style>
