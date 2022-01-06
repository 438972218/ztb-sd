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
          label="流程名称"
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
  
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="nextRole" v-if="types==='add'"
            >新建</a-button
          >
          <a-button type="primary" @click="editRole" v-if="types==='edit'"
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
  roleRequestAdd,
  roleRequestEdit,
} from "@/services/system/user";
import { processAddRequest,processPutRequest } from "@/services/process/workflow";
export default {
  name: "ProcessAdd",
  props: ["editData", "types"],
  data() {
    return {
      permissionIdList: [],
      Typedata: [],
      butEditShow: false,
      butNewShow: true,
      form: {
        name: "",
        description: "",
      },
      rules: {
        name: [
          {
            required: true,
            message: "流程名不能为空",
            trigger: "change,blur",
          },
        ]
      },
    };
  },
  watch: {
    editData(newVal, oldVal) {
      if (newVal !== null) {
        this.assignData();
      } 
    },
  },
  created() {
    this.assignData();
  },
  methods: {
    // 添加
    nextRole() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          processAddRequest(
            {
                description:this.form.description,
                name:this.form.name
            }
          ).then((response) => {
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
    editRole() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          processPutRequest({
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
    assignData() {
      let that = this;
      if (this.editData !== null) {
        this.form.name = this.editData.name;
        this.form.description = this.editData.description;
      }
    },
    dataClose() {
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