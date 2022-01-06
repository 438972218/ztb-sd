<template>
  <div>
    <a-row
      style="padding-bottom: 5px"
      v-if="project.status === '未启动' || project.status === '暂停'"
    >
      <a-col :span="24">
        <a-button type="primary" @click="handleAdd"> 新增 </a-button>
      </a-col>
    </a-row>
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="tableChange"
    >
      <span
        slot="operation"
        slot-scope="text, record"
        v-if="project.status !== '已完成'"
      >
        <a-icon
          type="edit"
          theme="filled"
          class="iconstyle"
          style="padding-right: 10px"
          @click="handleEdit(record)"
        />
        <a-icon
          type="delete"
          title="删除"
          @click="removeProjectAttachment(record)"
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
    <!-- 新增附件 -->
    <a-modal
      title="新增附件"
      :visible="visible"
      width="700px"
      @ok="addFile"
      @cancel="visible = false"
    >
      <a-form-model
        ref="ruleForm"
        :model="form"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <!-- <a-col :span="24">
            <a-form-model-item label="名称">
              <a-input v-model="form.attachmentName" placeholder="请输入名称" />
            </a-form-model-item>
          </a-col> -->
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
          <!-- <a-col :span="24">
            <a-form-model-item label="供应商是否可见">
              <a-radio-group v-model="form.vendorVisible">
                <a-radio :value="1"> 是 </a-radio>
                <a-radio :value="0"> 否 </a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="说明">
              <a-textarea
                v-model="form.description"
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
import { uploadfilePostRequest } from "@/services/system/user";
import { postProjectAttachment, getProjectAttachmentWithName, deleteProjectAttachment, putProjectAttachment } from "@/services/projectmanagement/project";
import { attachmentGetfile } from "@/services/source/bidding";
import moment from 'moment'
const columns = [
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
    dataIndex: 'sysUserInfoVO',
    key: 'uploadingUser',
    customRender: (text, record, index) => {
      return text ? text.name : ''
    },
  },
  {
    title: '上传时间',
    dataIndex: 'createdTime',
    key: 'createdTime',
    customRender: (text, record, index) => {
      return moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss')
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
];
export default {
  props: {
    project: {
      type: [Object, String],
      default: {}
    },
    user: {
      type: Object,
      default: {}
    }
  },
  name: "enclosure",
  data () {
    return {
      data: [],
      columns,
      fileList: [],
      form: {
        attachmentName: '', // 名称
        attachmentUrl: '', // 附件地址
        vendorVisible: 0, // 供应商是否可见
        description: '', // 说明
        attachmentType: '', // 类型
        projectId: '', // 招标单Id
      },
      visible: false,
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
    }
  },
  mounted () {
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
    handleChange (info) {
      let fileList = [...info.fileList, info.file];
      fileList = fileList.slice(-1);
      this.fileList = fileList;
    },
    // 查询附件列表
    async getDataList () {
      const data = {
        projectId: this.project.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getProjectAttachmentWithName(data)
      if (result.code === 0) {
        this.data = result.data ? result.data.records : []
        this.pagination.total = result.data.total ? result.data.total : 0
      } else {
        this.$message.error(result.message)
      }
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
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
      this.form = Object.assign({}, row)
      this.visible = true
    },
    // 删除附件
    async removeProjectAttachment (row) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        async onOk () {
          const result = await deleteProjectAttachment(row.id)
          if (result.code === 0) {
            that.getDataList()
            that.$message.success('删除成功')
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 新增附件
    handleAdd () {
      this.form = {
        uploadingUser: this.user.userName, // 上传人
        uploadingDate: moment().format('YYYY-MM-DD'), // 上传时间
        attachmentName: '', // 名称
        attachmentUrl: '', // 附件地址
        vendorVisible: 0, // 供应商是否可见
        description: '', // 说明
        attachmentType: '', // 类型
        projectId: this.project.id // 招标单Id
      }
      this.visible = true
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
              this.form.attachmentName = response.data.attachmentName
              this.form.attachmentType = response.data.attachmentType
              this.form.attachmentUrl = response.data.attachmentUrl
              if (this.form.id) {
                // 编辑
                result = await putProjectAttachment(this.form)
              } else {
                // 新增
                result = await postProjectAttachment(this.form)
              }
              if (result.code === 0) {
                this.$message.success('保存成功')
                this.getDataList()
                this.visible = false
              } else {
                this.$message.error(result.message)
              }
            } else {
              this.$message.error(response.message)
            }
          });
        } else {
          result = await putProjectAttachment(this.form)
          if (result.code === 0) {
            this.$message.success('保存成功')
            this.getDataList()
            this.visible = false
          } else {
            this.$message.error(result.message)
          }
        }
        this.fileList = [];
      }
    },
  }
};
</script>

<style lang="less" scoped>
</style>
