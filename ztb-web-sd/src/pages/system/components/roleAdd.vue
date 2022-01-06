<template>
  <div>
    <a-form-model
      layout="horizontal"
      ref="ruleForm"
      :maskClosable="false"
      :model="form"
      :rules="rules"
      class="page-form"
    >
      <a-row>
        <a-form-model-item
          prop="typeId"
          label="资源名称"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-select v-model="form.typeId">
            <a-select-option
              v-for="item in Typedata"
              :key="item.id"
              :value="item.id"
              :label="item.name"
            >
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
      </a-row>
      <a-row>
        <a-form-model-item
          prop="name"
          label="角色名称"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-input v-model="form.name" />
        </a-form-model-item>
      </a-row>

      <a-row>
        <a-form-model-item
          label="描述"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-input v-model="form.description" />
        </a-form-model-item>
      </a-row>
      <a-row>
        <a-form-model-item
          prop="name"
          label="角色权限"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-select
            v-model="form.permissionIds"
            mode="multiple"
            style="width: 100%"
            placeholder="请选择权限"
          >
            <a-select-option
              v-for="item in permissionIdList"
              :key="item.id"
              :value="item.id"
              :label="item.name"
            >
              <span role="img" aria-label="China">
                {{ item.name }}
              </span>
            </a-select-option>
          </a-select>
        </a-form-model-item>
      </a-row>

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
import { TreeSelect } from "ant-design-vue";
import {
  permissionRequestAll,
  roleTypesRequestAll,
  roleRequestAdd,
  roleRequestEdit,
} from "@/services/system/user";

export default {
  name: "RoleAdd",
  props: ["editData", "types"],
  data () {
    return {
      permissionIdList: [],
      Typedata: [],
      butEditShow: false,
      butNewShow: true,
      form: {
        name: "",
        permissionIds: [],
        description: "",
        typeId: null,
      },
      rules: {
        name: [
          {
            required: true,
            message: "角色类型名不能为空",
            trigger: "change,blur",
          },
        ],
        permissionIds: [
          {
            required: true,
            message: "角色类型权限不能为空",
            trigger: "change,blur",
          },
        ],
        typeId: [
          {
            required: true,
            message: "角色类型不能为空",
            trigger: "change,blur",
          },
        ],
      },
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
    this.getTypesAll();
  },
  methods: {
    // 权限列表
    getRoleAll () {
      permissionRequestAll({
        currentPage: 1,
      }).then((response) => {
        this.permissionIdList = response.data.records;
      });
    },
    // 角色类型
    getTypesAll () {
      roleTypesRequestAll({
        currentPage: 1,
      }).then((response) => {
        this.Typedata = response.data;
      });
    },
    // 添加
    nextRole () {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          roleRequestAdd({
            ...this.form,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加成功");
              this.dataClose();
            } else {
              this.$message.error(response.message);
            }
          });
        } else {
          return false;
        }
      });
    },
    // 编辑
    editRole () {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          roleRequestEdit({
            id: this.editData.id,
            ...this.form,
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
      let that = this;
      that.form.permissionIds = [];
      if (this.editData !== null) {
        this.form.name = this.editData.name;
        this.editData.permissions.forEach(function (e) {
          that.form.permissionIds.push(e.id);
        });
        this.form.typeId = this.editData.typeId;
        this.form.description = this.editData.description;
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