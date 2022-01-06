<template>
  <div>
    <div class="operator">
      <a-button
        @click="addNew"
        style="margin-bottom: 10px"
        type="primary"
        v-if="showBut"
        >新建</a-button
      >
    </div>
         <a-table :columns="columns" :data-source="data">
          <template slot="operation" slot-scope="text, record">
            <span
              @click="c_delete(record.id)"
              class="operation-style"
              v-if="showBut"
            >
              删除
            </span>
          </template>
        </a-table>
    <a-modal
      v-model="visible"
      width="500px"
      :footer="null"
      :centered="true"
      title="添加"
    >
      <a-form-model layout="horizontal" class="page-form">
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row>
          <a-form-model-item
            label="附件上传"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-upload
              :file-list="fileList"
              :remove="handleRemove"
              :before-upload="beforeUpload"
            >
              <a-button :disabled="fileList.length === 1">
                <a-icon type="upload" /> 请选择文件
              </a-button>
            </a-upload>
          </a-form-model-item>
        </a-row>
      </a-form-model>
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="addItems">新建</a-button>
          <a-button @click="dataClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
  </div>
</template>
<script>
import {
  paidAttachmentGetRequest,
  paidAttachmentDeleteRequest,
  paidAttachmentPostRequest
} from "@/services/source/contend";
import { uploadfilePostRequest } from "@/services/system/user";
import { permissionsEdit } from "@/utils/methods";

const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "附件类型",
    dataIndex: "attachmentType",
  },
  {
    title: "附件名称",
    dataIndex: "attachmentName",
  },
  {
    title: "上传人",
    dataIndex: "createdUser",
  },
  {
    title: "上传时间",
    dataIndex: "createdTime",
  },
  {
    title: "操作",
    dataIndex: "operation",
    key: "operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
];

export default {
  name: "FindFile",
   props: {
    contendId: {
      type: [String, null],
    },
  },
  data() {
    return {
      id: this.contendId,
      fileList: [],
      visible: false,
      data: [],
      columns,
        contendType: this.$store.state.account.contendType,
       disableds: true,
      showBut: true,
    };
  },
    created() {
         if (this.id) {
      this.getAll();
    }

        if (this.contendType === "add" || this.contendType === 0) {
      this.disableds = false;
    } else {
      this.disableds = permissionsEdit("bid:edit");
    }

    if (this.contendType === "add" || this.contendType === 0) {
      this.showBut = true;
    } else {
      this.showBut = !permissionsEdit("bid:edit");
    }

    if (!this.showBut) {
      this.columns.pop();
    }
    if (this.contendType === 0) {
      this.showBut = false;
    }
  },
  methods: {
      handleRemove(file) {
      const index = this.fileList.indexOf(file);
      const newFileList = this.fileList.slice();
      newFileList.splice(index, 1);
      this.fileList = newFileList;
    },
    beforeUpload(file) {
      this.fileList = [...this.fileList, file];
      return false;
    },
    // 查询招标单
    getAll() {
      paidAttachmentGetRequest({
        inquirySheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
      });
    },
    addItems() {
      const { fileList } = this;
      if (fileList.length === 0) {
        this.$message.warning("请选择文件");
      } else {
        this.confirmLoading = true;
        const formData = new FormData();
        formData.append("uploadFile", fileList[0]);
        uploadfilePostRequest(formData, {}).then((response) => {
          if (response.code === 0) {
            this.upFileBIdding(response.data).then((res) => {
              this.$message.success("导入成功");
              this.visible = false;
              this.getAll();
            });
            this.fileList = [];
            this.confirmLoading = false;
            this.fileShow = false;
          }
        });
      }
    },
    // 新建招标附件
    upFileBIdding(form) {
      return new Promise((resolve, reject) => {
        paidAttachmentPostRequest({
          inquirySheetId: this.id,
          attachmentName: form.attachmentName,
          attachmentType: form.attachmentType,
          attachmentUrl: form.attachmentUrl,
        })
          .then((res) => {
            resolve(res.data);
          })
          .catch((err) => {
            reject(err.data);
          });
      });
    },
    c_delete(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          paidAttachmentDeleteRequest(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getAll();
          });
        },
        onCancel() {},
      });
    },
    dataClose() {
      this.fileList = [];
      this.visible = false;
    },
    addNew() {
      this.visible = true;
    },
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  button {
    float: right;
  }
}
</style>
