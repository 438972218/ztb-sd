<template>
  <div>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="物品">
        <div class="operator">
          <a-button
            @click="addNew"
            style="margin-bottom: 10px"
            type="primary"
            v-if="showBut"
            >添加</a-button
          >
        </div>
        <a-table :columns="columns" :data-source="data" rowKey="id">
          <template slot="demandedDate" slot-scope="text">
            <span>
              {{ $moment(Number(text)).format("YYYY-MM-DD") }}
            </span>
          </template>
          <template slot="operation" slot-scope="text, record">
            <span
              @click="c_delete_items(record.id)"
              class="operation-style"
              v-if="showBut"
            >
              删除
            </span>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="建议投标方" force-render>
        <div class="operator">
          <a-button
            @click="addNew1"
            style="margin-bottom: 10px"
            type="primary"
            v-if="showBut"
            >添加</a-button
          >
        </div>
        <a-table :columns="columns1" :data-source="data1" rowKey="id">
          <template slot="operation" slot-scope="text, record">
            <span
              @click="c_delete_vendor(record.id)"
              class="operation-style"
              v-if="showBut"
            >
              删除
            </span>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="3" tab="附件" force-render>
        <div class="operator">
          <a-button
            @click="addNew2"
            style="margin-bottom: 10px"
            type="primary"
            v-if="showBut"
            >添加</a-button
          >
        </div>
        <a-table :columns="columns2" :data-source="data2" rowKey="id">
          <template slot="operation" slot-scope="text, record">
            <span
              @click="c_delete_file(record.id)"
              class="operation-style"
              v-if="showBut"
            >
              删除
            </span>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>

    <a-modal
      v-model="visible"
      width="500px"
      :footer="null"
      :centered="true"
      title="添加"
    >
      <a-form-model
        layout="horizontal"
        ref="ruleFormItems"
        :rules="rules"
        :model="formItems"
      >
        <a-row>
          <a-form-model-item
            prop="materialCode"
            label="物品编码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-select
              v-model="formItems.materialCode"
              placeholder="请选择物品"
              @change="itemChange"
            >
              <a-select-option
                v-for="item in itemList"
                :key="item.id"
                :value="item.itemCode"
                >{{ item.itemCode }}</a-select-option
              >
            </a-select>
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="materialDescription"
            label="物品描述"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="formItems.materialDescription" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="referToBrand"
            label="参考品牌"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="formItems.referToBrand" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="materialClassify"
            label="物品分类"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="formItems.materialClassify" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="demandedDate"
            label="需求日期"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-date-picker
              style="width: 100%"
              v-model="formItems.demandedDate"
            />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="demandedQuantity"
            label="需求数量"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input-number
              style="width: 100%"
              v-model="formItems.demandedQuantity"
            />
          </a-form-model-item>
          <a-row>
            <a-form-model-item
              prop="unit"
              label="计量单位"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input disabled v-model="formItems.unit" />
            </a-form-model-item>
            <a-row>
              <a-form-model-item
                prop="taxInclusive"
                label="含税"
                :labelCol="{ span: 6 }"
                :wrapperCol="{ span: 17 }"
              >
                <a-checkbox v-model="formItems.taxInclusive"></a-checkbox>
              </a-form-model-item>
            </a-row>
          </a-row>
        </a-row>
      </a-form-model>
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="addItems">新建</a-button>
          <a-button @click="itemClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
    <a-modal
      v-model="visible1"
      width="500px"
      :footer="null"
      :centered="true"
      title="添加"
    >
      <a-form-model
        layout="horizontal"
        ref="ruleFormVendor"
        :rules="rulesVendor"
        :model="formVendor"
      >
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row>
          <a-form-model-item
            prop="vendorName"
            label="投标方编码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-select
              v-model="formVendor.vendorCode"
              placeholder="请选择投标方编码"
              @change="userChange"
            >
              <a-select-option
                v-for="item in userList"
                :key="item.id"
                :value="item.code"
                >{{ item.code }}</a-select-option
              >
            </a-select>
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="vendorName"
            label="投标方名称"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="formVendor.vendorName" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="vendorDescription"
            label="投标方描述"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="formVendor.vendorDescription" />
          </a-form-model-item>
        </a-row>

        <a-row>
          <a-form-model-item
            prop="linkman"
            label="联系人"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="formVendor.linkman" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="phone"
            label="联系电话"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="formVendor.phone" />
          </a-form-model-item>
        </a-row>
      </a-form-model>
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="addVendor">新建</a-button>
          <a-button @click="vendorClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
    <a-modal
      v-model="visible2"
      width="500px"
      :centered="true"
      :footer="null"
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
          <a-button type="primary" @click="addFile">新建</a-button>
          <a-button @click="dataClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
  </div>
</template>
<script>
import {
  bidItemsGetRequest,
  bidVendorGetRequest,
  bidAttachmentGetRequest,
  bidItemsPostRequest,
  bidItemsDeleteRequest,
  bidVendorPostRequest,
  bidVendorDeleteRequest,
  bidAttachmentPostRequest,
  bidAttachmentDeleteRequest,
} from "@/services/source/bidding";
import { itemGetRequest } from "@/services/basis/index";
import { permissionsEdit } from "@/utils/methods";
import { uploadfilePostRequest } from "@/services/system/user";
import { getVendor } from "@/services/supplier/index";
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  // {
  //   title: "库存组织",
  //   dataIndex: "inventoryOrganization",
  // },
  {
    title: "物品编码",
    dataIndex: "materialCode",
  },
  {
    title: "物品描述",
    dataIndex: "materialDescription",
  },
  {
    title: "参考品牌",
    dataIndex: "referToBrand",
  },
  {
    title: "物品分类",
    dataIndex: "materialClassify",
  },
  {
    title: "需求日期",
    dataIndex: "demandedDate",
    scopedSlots: { customRender: "demandedDate" },
  },
  {
    title: "需求数量",
    dataIndex: "demandedQuantity",
  },
  {
    title: "计量单位",
    dataIndex: "unit",
  },
  {
    title: "是否含税",
    dataIndex: "taxInclusive",
  },
  {
    title: "操作",
    dataIndex: "operation",
    scopedSlots: { customRender: "operation" },
  },
];
const columns1 = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "投标方编码",
    dataIndex: "vendorCode",
  },
  {
    title: "投标方名称",
    dataIndex: "vendorName",
  },
  {
    title: "投标方描述",
    dataIndex: "vendorDescription",
  },

  {
    title: "联系人",
    dataIndex: "linkman",
  },
  {
    title: "联系人电话",
    dataIndex: "phone",
  },
  {
    title: "操作",
    dataIndex: "operation",
    scopedSlots: { customRender: "operation" },
  },
];
const columns2 = [
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
  name: "material",
  props: {
    biddingId: {
      type: [String, null],
    },
  },
  data() {
    return {
      itemList: [],
      fileList: [],
      userList: [],
      formItems: {
        // inventoryOrganization: "", // 库存组织
        materialCode: "", // 物品编码
        materialDescription: "", // 物品描述
        referToBrand: "", // 参考品牌
        materialClassify: "", // 物品分类
        demandedDate: "", // 需求日期
        demandedQuantity: "", // 	需求数量
        unit: "", // 单位
        taxInclusive: false, // 	含税
      },
      rules: {
        // inventoryOrganization: [
        //   {
        //     required: true,
        //     message: "库存组织不能为空",
        //     trigger: "blur",
        //   },
        // ],
        materialCode: [
          {
            required: true,
            message: "物品编码不能为空",
            trigger: "blur",
          },
        ],
        demandedQuantity: [
          {
            required: true,
            message: "需求数量不能为空",
            trigger: "change",
          },
        ],
        quantity: [
          {
            required: true,
            message: "询价单类型不能为空",
            trigger: "blur",
          },
        ],
        unit: [
          {
            required: true,
            message: "单位不能为空",
            trigger: "blur",
          },
        ],
      },
      formVendor: {
        vendorCode: "",
        vendorName: "",
        vendorDescription: "",
        linkman: "",
        phone: "",
      },
      rulesVendor: {
        vendorCode: [
          {
            required: true,
            message: "投标方编码不能为空",
            trigger: "blur",
          },
        ],
      },
      visible2: false,
      visible1: false,
      visible: false,
      data: [],
      columns,
      data1: [],
      columns1,
      data2: [],
      columns2,
      id: this.biddingId,
      biddingType: this.$store.state.account.biddingType,
      disableds: true,
      showBut: true,
    };
  },
  created() {
    if (this.id) {
      this.getAll();
      this.getItemAll();
      this.getUserList();
    }
    if (this.biddingType === "add") {
      this.showBut = true;
    } else {
      this.showBut = !permissionsEdit("bid:edit");
    }
    if (!this.showBut) {
      this.columns.pop();
      this.columns1.pop();
      this.columns2.pop();
    }
  },
  methods: {
    getAll() {
      let data = {
        bidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      };
      bidItemsGetRequest(data).then((response) => {
        this.data = response.data.records;
      });
      bidVendorGetRequest(data).then((response) => {
        this.data1 = response.data.records;
      });
      bidAttachmentGetRequest(data).then((response) => {
        this.data2 = response.data.records;
      });
    },
    // 请求所有用户
    getUserList() {
      getVendor({
        currentPage: -1,
      }).then((response) => {
        this.userList = response.data.records;
      });
    },
    // 所有物品请求
    getItemAll() {
      itemGetRequest({
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        if (response.data.records) {
          this.itemList = response.data.records;
        }
      });
    },
    // 添加物品
    addItems() {
      this.$refs.ruleFormItems.validate((valid) => {
        if (valid) {
          bidItemsPostRequest({
            bidSheetId: this.id,
            ...this.formItems,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加成功");
              this.visible = false;
              this.getAll();
            } else {
              this.$message.error(response.message);
            }
          });
        } else {
          return false;
        }
      });
    },
    // 新增供应商
    addVendor() {
      this.$refs.ruleFormVendor.validate((valid) => {
        if (valid) {
          bidVendorPostRequest({
            bidSheetId: this.id,
            ...this.formVendor,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加成功");
              this.visible1 = false;
              this.getAll();
            } else {
              this.$message.error(response.message);
            }
          });
        } else {
          return false;
        }
      });
    },
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
    // 选择文件
    addFile() {
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
              this.visible2 = false;
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
        bidAttachmentPostRequest({
          bidSheetId: this.id,
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
    // 获取物品信息
    itemChange(e) {
      let target = this.itemList.filter((item) => e === item.itemCode)[0];
      this.formItems.materialDescription = target.itemDesc;
      this.formItems.referToBrand = target.brand;
      this.formItems.materialClassify = target.itemType;
      this.formItems.unit = target.basicMeasure;
    },
    // 获取人员信息
    userChange(e) {
      let target = this.userList.filter((item) => e === item.code)[0];
      this.formVendor.vendorName = target.name;
      this.formVendor.vendorDescription = target.description;
      this.formVendor.linkman = target.contactName;
      this.formVendor.phone = target.contactMobile;
    },
    // 删除物品
    c_delete_items(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          bidItemsDeleteRequest(id).then((response) => {
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
    c_delete_vendor(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          bidVendorDeleteRequest(id).then((response) => {
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
    c_delete_file(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          bidAttachmentDeleteRequest(id).then((response) => {
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
    addNew() {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.ruleFormItems.resetFields();
      });
    },

    itemClose() {
      this.visible = false;
    },
    addNew1() {
      this.visible1 = true;
      this.$nextTick(() => {
        this.$refs.ruleFormVendor.resetFields();
      });
    },
    vendorClose() {
      this.visible1 = false;
    },
    addNew2() {
      this.visible2 = true;
    },
    dataClose() {
      this.visible2 = false;
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
