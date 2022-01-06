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
          prop="name"
          label="资源名称"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-input v-model="form.name" />
        </a-form-model-item>
      </a-row>
      <a-row>
        <a-form-model-item
          prop="icon"
          label="图标"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-input v-model="form.icon" />
        </a-form-model-item>
      </a-row>
      <a-row>
        <a-form-model-item
          prop="sort"
          label="排序"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-input-number id="inputNumber" v-model="form.sort">{{
            form.sort
          }}</a-input-number>
        </a-form-model-item>
      </a-row>
      <a-row>
        <a-form-model-item
          prop="route"
          label="路由地址"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-input v-model="form.route" />
        </a-form-model-item>
      </a-row>
      <a-row>
        <a-form-model-item
          prop="hide"
          label="是否隐藏"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-radio-group :options="options" :default-value="form.hide" />
        </a-form-model-item>
      </a-row>
      <a-row>
        <a-form-model-item
          prop="description"
          label="描述"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-input v-model="form.description" />
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
import {
  permissionRequestAll,
  roleAddRequestAll,
  EditPermissionRequest,
} from "@/services/system/user";

export default {
  name: "MenuAdd",
  props: ["editData", "types"],
  data () {
    return {
      form: {
        name: "",
        icon: "",
        sort: Number,
        route: "",
        description: "",
        hide: "",
      },
      options: [
        { label: '隐藏', value: 0 },
        { label: '显示', value: 1 },
      ],
      permissionIdList: [],
      butEditShow: false,
      butNewShow: true,
      rules: {
        name: [
          {
            required: true,
            message: "资源名称不能为空",
            trigger: "change,blur",
          },
        ],
        icon: [
          {
            required: true,
            message: "图标不能为空",
            trigger: "change,blur",
          },
        ],
        sort: [
          {
            required: true,
            message: "排序不能为空",
            trigger: "change,blur",
          },
        ],
        route: [
          {
            required: true,
            message: "路由地址不能为空",
            trigger: "change,blur",
          },
        ]
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
    // 添加
    nextRole () {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          roleAddRequestAll({
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
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          EditPermissionRequest({
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
      if (this.editData !== null) {
        this.form.name = this.editData.name;
        this.form.icon = this.editData.icon;
        this.form.sort = this.editData.sort;
        this.form.route = this.editData.route;
        this.form.description = this.editData.description;
        this.form.hide = this.editData.hide;
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