<template>
  <div>
    <a-form-model
      layout="horizontal"
      ref="ruleForm"
      :model="form"
      :rules="rules"
      class="page-form"
    >
      <a-row>
        <a-col :span="12">
          <a-form-model-item
            prop="account"
            label="账号"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input
              v-model="form.account"
              :disabled="types === 'add' ? false : true"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item
            :prop="types === 'add' ? 'password' : ''"
            label="密码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.password" />
          </a-form-model-item>
        </a-col>
      </a-row>
      <!-- <a-row> -->
      <!-- <a-col :span="12">
          <a-form-model-item
            label="所在组织"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-tree-select
              v-model="form.name"
              show-search
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              placeholder="Please select"
              allow-clear
              tree-default-expand-all
            >
              <a-icon slot="suffixIcon" type="smile" />
              <a-tree-select-node key="0-1" value="parent 1" title="集团">
              </a-tree-select-node>
            </a-tree-select>
          </a-form-model-item>
        </a-col> -->
      <!-- <a-col :span="12">
          <a-form-model-item
            prop="name"
            label="用户类型"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
              <a-select v-model="form.roleId">
            <a-select-option value="-1"> 系统管理员 </a-select-option>
            <a-select-option value="1"> 组织用户 </a-select-option>
          </a-select>
          </a-form-model-item>
        </a-col> -->
      <!-- </a-row> -->
      <a-row>
        <a-col :span="12">
          <a-form-model-item
            prop="name"
            label="姓名"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.name" />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item
            prop="telephone"
            label="手机号"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.telephone" />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-model-item
            prop="email"
            label="邮箱"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.email" />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item
            label="描述"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.description" />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-model-item
            prop="roleId"
            label="选择角色"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-select v-model="form.roleId">
              <a-select-option
                v-for="item in roleList"
                :key="item.id"
                :value="item.id"
                :label="item.name"
              >
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <!-- <a-col :span="12">
          <a-form-model-item
            label="公司权限"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-tree-select
              v-model="value"
              style="width: 100%"
              :tree-data="treeData"
              tree-checkable
              :show-checked-strategy="SHOW_PARENT"
              search-placeholder="Please select"
            />
          </a-form-model-item>
        </a-col> -->
      </a-row>
      <!-- <a-row>
        <a-col :span="12">
          <a-form-model-item
            label="采购组织"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-tree-select
              v-model="value"
              style="width: 100%"
              :tree-data="treeData"
              tree-checkable
              :show-checked-strategy="SHOW_PARENT"
              search-placeholder="Please select"
            />
          </a-form-model-item>
        </a-col>
      </a-row> -->
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="nextRole" v-show="butNewShow"
            >新建</a-button
          >
          <a-button type="primary" @click="editRole" v-show="butEditShow"
            >保存</a-button
          >
          <a-button @click="dataClose">返回</a-button>
        </a-space>
      </a-row>
    </a-form-model>
  </div>
</template>
<script>
import { roleRequestAll, userRequestAdd, EditUserRequest } from "@/services/system/user";
import { regular } from "@/utils/validate";

export default {
  name: "UserAdd",
  props: ["editData", "types"],
  data () {
    return {
      form: {
        account: "",
        password: "",
        name: "",
        telephone: "",
        email: "",
        description: "",
        roleId: "",
      },
      roleList: [],

      rules: {
        account: [
          {
            required: true,
            message: "账号不能为空",
            trigger: "blur",
          },
        ],
        name: [
          {
            required: true,
            message: "姓名不能为空",
            trigger: "blur",
          },
        ],
        password: [
          {
            pattern: regular.char8to20,
            required: true,
            message: regular.char0to255Msg,
            trigger: "blur",
          },
        ],
        email: [
          {
            pattern: regular.emailNumber,
            required: true,
            message: regular.emailNumberMsg,
            trigger: "blur",
          },
        ],
        telephone: [
          {
            pattern: regular.mobileNumber,
            required: true,
            message: regular.mobileNumberMsg,
            trigger: "blur",
          },
        ],
        roleId: [
          {
            required: true,
            message: "请选择角色",
            trigger: "change",
          },
        ],
      },
      butEditShow: false,
      butNewShow: true,
    };
  },
  watch: {
    editData (newVal, oldVal) {
      if (newVal !== null) {
        this.butEditShow = true;
        this.butNewShow = false;
        this.assignData();
      } else {
        this.butEditShow = false;
        this.butNewShow = true;
      }
    },
  },
  created () {
    this.getRoleAll();
    if (this.types === "add") {
      this.butEditShow = false;
      this.butNewShow = true;
    } else {
      this.butEditShow = true;
      this.butNewShow = false;
    }
    this.assignData();
  },
  methods: {
    //角色列表
    getRoleAll () {
      roleRequestAll({
        currentPage: 1,
      }).then((response) => {
        this.roleList = response.data.records;
      });
    },
    // 添加
    nextRole () {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          userRequestAdd({
            ...this.form,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加成功");
              this.dataClose();
            } else {
              this.$message.success(response.data.message);
            }
          });
        } else {
          return false;
        }
      });
    },
    // 编辑
    editRole () {
      let data = JSON.parse(JSON.stringify(this.form))
      if (this.form.password === '') {
        delete data.password
      }
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          EditUserRequest({
            id: this.editData.id,
            ...data,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("保存成功");
              this.dataClose();
            } else {
              this.$message.success(response.message);
            }
          });
        } else {
          return false;
        }
      });
    },
    // 赋值
    assignData () {
      if (this.editData !== null && this.editData !== undefined) {
        this.form.name = this.editData.name;
        this.form.account = this.editData.account;
        this.form.password = '';
        this.form.telephone = this.editData.telephone;
        this.form.email = this.editData.email;
        this.form.description = this.editData.description;
        this.form.roleId = this.editData.role.id;

      }
    },
    dataClose () {
      this.$emit("getAll");
      this.$emit("on-data-close");
      this.$refs.ruleForm.resetFields();
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