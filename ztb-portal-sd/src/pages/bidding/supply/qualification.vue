<template>
  <div>
    <a-table :columns="columns" :data-source="data">
      <template slot="ifNeedAttachment" slot-scope="text">
        <div>
          {{ text === 1 ? "是" : "否" }}
        </div>
      </template>
      <template slot="applyExplanation" slot-scope="text, record">
        <div>
          <a-input
            v-if="record.editable"
            style="margin: -5px 0"
            :value="text"
            @change="
              (e) => handleChange(e.target.value, record.id, 'applyExplanation')
            "
          />
          <template v-else>
            <span v-if="record.bidVendorPreTrialItemVO === null">
              {{ record.fileName }}</span
            >
            <span v-else>{{
              record.bidVendorPreTrialItemVO.applyExplanation
            }}</span>
          </template>
        </div>
      </template>
      <template slot="attachmentUrl" slot-scope="text, record">
        <div>
          <a-upload
            v-if="record.editable"
            :file-list="fileList"
            :before-upload="beforeUpload"
            @change="(e) => handleChangeFile(e, record.id, 'attachmentUrl')"
          >
            <a-button :disabled="fileList.length === 1"> 请选择文件 </a-button>
          </a-upload>
          <template v-else>
            <span v-if="record.bidVendorPreTrialItemVO === null">
              {{ record.fileName }}</span
            >
            <span v-else>{{
              record.bidVendorPreTrialItemVO.attachmentName
            }}</span>
          </template>
        </div>
      </template>
      <template slot="operation" slot-scope="text, record">
        <div class="editable-row-operations">
          <span v-if="record.editable">
            <a
              v-if="record.bidVendorPreTrialItemVO === null"
              @click="() => save(record.id)"
              >提交</a
            >
            <a @click="() => alter(record.id)" v-else>修改</a>
            <a @click="() => cancel(record.id)">取消</a>
            <!-- <a-popconfirm
                title="Sure to cancel?"
                @confirm="() => cancel(record.id)"
              >
                <a>取消</a>
              </a-popconfirm> -->
          </span>
          <span v-else>
            <a :disabled="editingKey !== ''" @click="() => edit(record.id)"
              >添加</a
            >
          </span>
        </div>
      </template>
    </a-table>
  </div>
</template>
<script>
import {
  bidPreTrialItemGetRequest,
  bidPreTrialItemwithVendorGetRequest,
  bidVendorPreTrialItemPostRequest,
  bidVendorGetRequest,
  bidVendorPreTrialItemPutRequest,
} from "@/services/source/bidding";
import { uploadfilePostRequest } from "@/services/system/user";
import {
  vendorshowByUserIdGetVendor
} from "@/services/supplier/index";
import { mapGetters } from "vuex";
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "代码",
    dataIndex: "code",
  },
  {
    title: "资格预审项名称",
    dataIndex: "prequalification",
  },

  {
    title: "是否需要附件",
    dataIndex: "ifNeedAttachment",
    scopedSlots: { customRender: "ifNeedAttachment" },
  },
  {
    title: "申请说明",
    dataIndex: "applyExplanation",
    scopedSlots: { customRender: "applyExplanation" },
  },
  {
    title: "附件",
    dataIndex: "attachmentUrl",
    scopedSlots: { customRender: "attachmentUrl" },
  },
  {
    title: "操作",
    dataIndex: "operation",
    align: "center",
    key: "operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
];

export default {
  name: "FindItems",
  props: {
    biddingVendorId: {
      type: [String, null],
    },
  },
  data() {
    return {
      fileList: [],
      id: this.biddingVendorId,
      data: [],
      editingKey: "",
      columns,
      queryId: "",
    };
  },
  created() {
    if (this.id) {
      this.getCode();
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  mounted() {},
  methods: {
    getAll() {
      bidPreTrialItemwithVendorGetRequest({
        bidSheetId: this.id,
        bidVendorId: this.queryId,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        if (response.data.records !== null) {
          let record = response.data.map((item) => {
            item.fileName = null;
            return { ...item };
          });
          this.data = record;
        }
      });
    },
    // 招标商id查询
    getQueryId(code) {
      bidVendorGetRequest({
        bidSheetId: this.id,
        vendorCode: code,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.queryId = response.data.records[0].id;
        this.getAll();
      });
    },
        // 根据userid查询code
    getCode(){
      vendorshowByUserIdGetVendor(this.user.id).then((response) => {
        this.getQueryId(response.data.code)
      });
    },
    //导入模板文件
    handleRemove(file) {
      const index = this.fileList.indexOf(file);
      const newFileList = this.fileList.slice();
      newFileList.splice(index, 1);
      this.fileList = newFileList;
    },
    beforeUpload(file) {
      this.fileList = [file];
      return false;
    },
    edit(key) {
      const newData = [...this.data];
      const target = newData.filter((item) => key === item.id)[0];
      this.editingKey = key;
      if (target) {
        target.editable = true;
        this.data = newData;
      }
    },
    save(key) {
      const newData = [...this.data];
      const target = newData.filter((item) => key === item.id)[0];
      // if (target.applyExplanation === "" || target.unit === "") {
      //   this.$message.error("请填写必填项");
      // } else {
      if (target) {
        delete target.editable;
        this.data = newData;
        console.log(this.data1);
      }
      this.editingKey = "";
      const formData = new FormData();
      formData.append("uploadFile", target.attachmentUrl);
      uploadfilePostRequest(formData, {}).then((response) => {
        if (response.code === 0) {
          bidVendorPreTrialItemPostRequest({
            attachmentName: response.data.attachmentName,
            attachmentUrl: response.data.attachmentUrl,
            applyExplanation: target.applyExplanation,
            bidVendorId: this.queryId,
            trialItemId: target.id,
          }).then((res) => {
            this.$message.success("导入成功");
            this.visible = false;
            this.getAll();
          });
          this.fileList = [];
          this.confirmLoading = false;
          this.fileShow = false;
        }
      });
    },
    alter(key) {
      const newData = [...this.data];
      const target = newData.filter((item) => key === item.id)[0];
      // if (target.applyExplanation === "" || target.unit === "") {
      //   this.$message.error("请填写必填项");
      // } else {
      if (target) {
        delete target.editable;
        this.data = newData;
        console.log(this.data1);
      }
      this.editingKey = "";
      const formData = new FormData();
      formData.append("uploadFile", target.attachmentUrl);
      uploadfilePostRequest(formData, {}).then((response) => {
        if (response.code === 0) {
          bidVendorPreTrialItemPutRequest({
            id: target.bidVendorPreTrialItemVO.id,
            attachmentName: response.data.attachmentName,
            attachmentUrl: response.data.attachmentUrl,
            applyExplanation: target.applyExplanation,
            bidVendorId: this.queryId,
            trialItemId: target.id,
            deleted: 0,
          }).then((res) => {
            this.$message.success("导入成功");
            this.visible = false;
            this.getAll();
          });
          this.fileList = [];
          this.confirmLoading = false;
          this.fileShow = false;
        }
      });
    },
    handleChange(value, key, column) {
      let newData = [...this.data];
      let target = newData.filter((item) => key === item.id)[0];
      if (target) {
        target[column] = value;
        this.data = newData;
      }
    },
    handleChangeFile(value, key, column) {
      let newData = [...this.data];
      let target = newData.filter((item) => key === item.id)[0];
      if (target) {
        target.fileName = value.file.name;
        target[column] = value.file;
        this.data = newData;
      }
    },
    cancel(key) {
      const newData = [...this.data];
      const target = newData.filter((item) => key === item.id)[0];
      this.editingKey = "";
      if (target) {
        delete target.editable;
        this.data = newData;
      }
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
.editable-row-operations a {
  margin-right: 8px;
}
</style>
