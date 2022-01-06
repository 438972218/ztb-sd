<template>
  <div>
    <div class="operator">
      <a-button
        @click="addNew"
        style="margin-bottom: 10px"
        type="primary"
        v-if="showBut"
        >添加</a-button
      >
    </div>
    <a-table :columns="columns" :data-source="data">
      <template slot="operation" slot-scope="record">
        <span @click="c_delete(record.id)" class="operation-style"> 删除 </span>
      </template>
    </a-table>
    <a-modal
      v-model="visible"
      :footer="null"
      width="500px"
      :centered="true"
      title="添加"
    >
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :rules="rules"
        :model="form"
      >
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row>
          <a-form-model-item
            prop="vendorCode"
            label="供应商代码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-select
              v-model="form.vendorCode"
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
            label="供应商名称"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="form.vendorName" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="description"
            label="供应商描述"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="form.description" />
          </a-form-model-item>
          <a-form-model-item
            prop="linkman"
            label="联系人"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="form.linkman" />
          </a-form-model-item>
          <a-form-model-item
            prop="phone"
            label="联系电话"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="form.phone" />
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
  paidVendorGetRequest,
  paidVendorPostRequest,
  paidVendorDeleteRequest,
} from "@/services/source/contend";
import { permissionsEdit } from "@/utils/methods";
import { getVendor } from "@/services/supplier/index";
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "供应商代码",
    dataIndex: "vendorCode",
  },
  {
    title: "供应商名称",
    dataIndex: "vendorName",
  },
  {
    title: "供应商描述",
    dataIndex: "description",
  },

  {
    title: "联系人",
    dataIndex: "linkman",
  },
  {
    title: "联系电话",
    dataIndex: "phone",
  },
  {
    title: "操作",
    scopedSlots: { customRender: "operation" },
  },
];

export default {
  name: "FindItems",
  props: {
    contendId: {
      type: [String, null],
    },
  },
  data() {
    return {
      userList: [],
      form: {
        vendorCode: "", // 供应商代码
        vendorName: "", // 	供应商名称
        description: "", // 描述
        linkman: "", // 	联系人
        phone: "", // 联系电话
      },
      rules: {
        vendorCode: [
          {
            required: true,
            message: "供应商代码不能为空",
            trigger: "blur",
          },
        ],
        vendorName: [
          {
            required: true,
            message: "供应商名称不能为空",
            trigger: "blur",
          },
        ],
      },
      id: this.contendId,
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
      this.getUserList();
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
    } else if (this.contendType === 0) {
      this.showBut = false;
    }
  },
  methods: {
    getAll() {
      paidVendorGetRequest({
        paidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
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
    // 获取人员信息
    userChange(e) {
      let target = this.userList.filter((item) => e === item.code)[0];
      this.form.vendorName = target.name;
      this.form.vendorDescription = target.description;
      this.form.linkman = target.contactName;
      this.form.phone = target.contactMobile;
    },
    addItems() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          paidVendorPostRequest({
            paidSheetId: this.id,
            ...this.form,
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
    c_delete(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          paidVendorDeleteRequest(id).then((response) => {
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
        this.$refs.ruleForm.resetFields();
      });
    },
    dataClose() {
      this.visible = false;
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
