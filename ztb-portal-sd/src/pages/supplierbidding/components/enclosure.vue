<template>
  <div>
    <a-tabs v-model="activeKey">
      <a-tab-pane key="1" tab="附件">
        <a-row style="padding-bottom: 5px" v-if="form.paidStatus === '进行中'">
          <a-col :span="24">
            <a-button type="primary" @click="handleAnnexAdd"> 新增 </a-button>
          </a-col>
        </a-row>
        <a-table
          :pagination="paginationAnnex"
          :columns="columnsAnnex"
          :data-source="dataAnnex"
          @change="tableAnnexChange"
        >
          <span slot="operation" slot-scope="text, record">
            <a-icon
              type="edit"
              theme="filled"
              class="iconstyle"
              @click="handleEdit(record)"
              style="padding-right: 10px"
              v-if="user.userName === record.uploadingUser"
            />
            <a-icon
              type="delete"
              @click="removeProjectAttachment(record)"
              title="删除"
              class="iconstyle"
              style="padding-right: 10px"
              v-if="user.userName === record.uploadingUser"
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
      width="700px"
      @ok="handleSave"
      @cancel="visible = false"
    >
      <a-form-model
        ref="ruleForm"
        :rules="rules"
        :model="formInline"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="供应商" prop="vendorName">
              <a-select
                v-model="vendorId"
                style="width: 100%"
                placeholder="请选择供应商"
                @change="vendorChange"
              >
                <a-select-option
                  v-for="item in vendorList"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="城市">
              <a-input
                disabled
                v-model="formInline.city"
                placeholder="请输入城市"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="联系人" prop="linkman">
              <a-select
                v-model="linkman"
                style="width: 100%"
                placeholder="请选择联系人"
                @change="linkmanChange"
              >
                <a-select-option
                  v-for="item in userList"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.userName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="联系电话">
              <a-input
                disabled
                v-model="formInline.phone"
                placeholder="请输入联系电话"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="邮箱">
              <a-input
                disabled
                v-model="formInline.email"
                placeholder="请输入邮箱"
              ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
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
                <a-button :disabled="fileList.length === 1">
                  <a-icon type="upload" /> 请选择文件
                </a-button>
              </a-upload>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="供应商是否可见">
              <a-radio-group v-model="formAnnex.vendorVisible">
                <a-radio :value="1"> 是 </a-radio>
                <a-radio :value="0"> 否 </a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col> -->
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
import { paidVendorGetRequest, paidVendorPostRequest, getVendorwithUser, paidVendorDeleteRequest, paidAttachmentGetwithUser, paidSrgAttachmentGetwithUser, paidAttachmentPostRequest, paidAttachmentDeleteRequest, paidAttachmentPutRequest, paidVendorSaveWithMark } from "@/services/source/contend";
import { uploadfilePostRequest } from "@/services/system/user";
import { attachmentGetfile } from "@/services/source/bidding";
import moment from 'moment'
import { mapGetters } from "vuex";
const columns = [
  {
    title: '供应商名称',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  {
    title: '城市',
    dataIndex: 'city',
    key: 'city',
  },
  {
    title: '联系人',
    dataIndex: 'linkman',
    key: 'linkman',
  },
  {
    title: '联系电话',
    dataIndex: 'phone',
    key: 'phone',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
  },
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
  name: "supplier",
  data () {
    return {
      annexTitle: '',
      fileList: [],
      linkman: undefined,
      vendorId: undefined,
      activeKey: '1',
      visible: false,
      visibleAnnex: false,
      formInline: {
        city: '', // 城市
        email: '', // 邮箱
        linkman: '', // 联系人
        phone: '', // 联系电话
        vendorName: undefined, // 供应商名称
      },
      formAnnex: {
        attachmentName: '', // 名称
        attachmentUrl: '', // 附件地址
        description: '', // 说明
        attachmentType: '', // 类型
      },
      vendorList: [],
      userList: [],
      columns,
      columnsAnnex,
      data: [],
      form: {
        id: ''
      },
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
      dataAnnex: []
    }
  },
  computed: {
    ...mapGetters("account", ["user"])
  },
  mounted () {
    this.form = JSON.parse(sessionStorage.getItem('contendForm'))
    this.getDataList()
    this.getAnnexDataList()
    // this.getVendorList()
  },
  methods: {
    handleChange (info) {
      let fileList = [...info.fileList, info.file];
      fileList = fileList.slice(-1);
      this.fileList = fileList;
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
    // 查询附件列表
    async getAnnexDataList () {
      const data = {
        paidSheetId: this.form.id,
        vendorVisible: 1,
        createdUser: this.user.userName,
        currentPage: this.paginationAnnex.current,
        pageSize: this.paginationAnnex.pageSize
      }
      const result = await paidSrgAttachmentGetwithUser(data)
      if (result.code === 0) {
        this.dataAnnex = result.data.records
        this.paginationAnnex.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 编辑附件
    handleEdit (row) {
      this.annexTitle = '编辑附件'
      this.fileList = [
        {
          uid: row.id,      // 文件唯一标识，建议设置为负数，防止和内部产生的 id 冲突
          name: row.attachmentName,   // 文件名
          xhr: row.attachmentUrl, // XMLHttpRequest Header
        }
      ]
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
        description: '', // 说明
        attachmentType: '', // 类型
        paidSheetId: this.form.id // 招标单Id
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
                result = await paidAttachmentPutRequest(this.formAnnex)
              } else {
                // 新增
                result = await paidAttachmentPostRequest(this.formAnnex)
              }
              if (result.code === 0) {
                this.$message.success('保存成功')
                this.getAnnexDataList()
                this.visibleAnnex = false
              } else {
                this.$message.error(result.message)
              }
            } else {
              this.$message.error(response.message)
            }
          });
        } else {
          result = await paidAttachmentPutRequest(this.formAnnex)
          if (result.code === 0) {
            this.$message.success('保存成功')
            this.getAnnexDataList()
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
    // 供应商选择
    vendorChange (e) {
      const data = this.vendorList.find(f => f.id === e)
      if (data) {
        this.formInline.vendorName = data.name
        this.formInline.city = data.city
        this.formInline.vendorId = data.id
        this.userList = data.sysUserInfoVOS ? data.sysUserInfoVOS : []
      }
    },
    // 联系人选择
    linkmanChange (e) {
      const data = this.userList.find(f => f.id === e)
      if (data) {
        this.formInline.vendorUserId = data.id
        this.formInline.linkman = data.name
        this.formInline.email = data.mail
        this.formInline.phone = data.phone
      }
    },
    // 新增供应商
    handleVendorAdd () {
      this.vendorId = undefined
      this.linkman = undefined
      this.formInline = {
        city: '', // 城市
        email: '', // 邮箱
        linkman: '', // 联系人
        phone: '', // 联系电话
        vendorName: undefined, // 供应商名称
        paidSheetId: this.form.id
      }
      this.visible = true
    },
    // 查询供应商WithUser
    async getVendorList () {
      const data = {
        currentPage: -1
      }
      const result = await getVendorwithUser(data)
      if (result.code === 0) {
        this.vendorList = result.data
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询供应商
    async getDataList () {
      const data = {
        paidSheetId: this.form.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await paidVendorGetRequest(data)
      if (result.code === 0) {
        this.data = result.data.records
        this.pagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 保存供应商
    async handleSave () {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          const result = await paidVendorSaveWithMark(this.formInline)
          if (result.code === 0) {
            this.$message.success('保存成功')
            this.visible = false
            this.getDataList()
          } else {
            this.$message.error(result.message)
          }
        }
      })
    },
    // 删除供应商
    async handleVendorDelete (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await paidVendorDeleteRequest(id)
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
