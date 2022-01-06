<template>
  <a-card class="result-error" :bordered="false">
    <a-row>
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :rules="rules"
        :model="form"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="姓名" prop="name">
              <a-input v-model="form.name" placeholder="请输入姓名" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="用户名" prop="userName">
              <a-input v-model="form.userName" placeholder="请输入用户名" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="密码：" prop="password">
              <a-input
                type="password"
                v-model="form.password"
                placeholder="输入新密码"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="邮箱" prop="mail">
              <a-input v-model="form.mail" placeholder="输入新邮箱" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="手机号码" prop="phone">
              <a-input v-model="form.phone" placeholder="请再次输入新密码" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-row>
    <a-descriptions title="登记信息">
      <a-descriptions-item label="工商注册号">
        {{ dataList.icrn }}
      </a-descriptions-item>
      <a-descriptions-item label="所属地区">
        {{ dataList.area }}
      </a-descriptions-item>
      <a-descriptions-item label="所属行业">
        {{ dataList.industry }}
      </a-descriptions-item>
      <a-descriptions-item label="企业名称">
        {{ dataList.name }}
      </a-descriptions-item>
      <a-descriptions-item label="企业类型">
        {{ dataList.type }}
      </a-descriptions-item>
      <a-descriptions-item label="经营范围">
        {{ dataList.businessScope }}
      </a-descriptions-item>
      <a-descriptions-item label="注册地址">
        {{ dataList.registeredAddress }}
      </a-descriptions-item>
      <a-descriptions-item label="三级信息">
        {{ dataList.name }}
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="联系人信息">
      <a-descriptions-item label="姓名">
        {{ dataList.contactName }}
      </a-descriptions-item>
      <a-descriptions-item label="职位">
        {{ dataList.contactTitle }}
      </a-descriptions-item>
      <a-descriptions-item label="手机号码">
        {{ dataList.contactMobile }}
      </a-descriptions-item>
      <a-descriptions-item label="证件类型">
        {{ dataList.contactIdentityType }}
      </a-descriptions-item>
      <a-descriptions-item label="证件号码">
        {{ dataList.contactIdentityNum }}
      </a-descriptions-item>
      <a-descriptions-item label="邮箱">
        {{ dataList.contactEmail }}
      </a-descriptions-item>
      <a-descriptions-item label="正面照片">
        {{ dataList.identityFront }}
      </a-descriptions-item>
      <a-descriptions-item label="背面照片">
        {{ dataList.identityReverse }}
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="业务信息">
      <a-descriptions-item label="商业模式">
        {{ dataList.businessModel }}
      </a-descriptions-item>
      <a-descriptions-item label="代理品牌">
        {{ dataList.agentBrand }}
      </a-descriptions-item>
      <a-descriptions-item label="供货品类">
        {{ dataList.mainCategory }}
      </a-descriptions-item>
      <a-descriptions-item label="主要客户">
        {{ dataList.mainCustomer }}
      </a-descriptions-item>
      <a-descriptions-item label="厂房性质">
        {{ dataList.factoryNature }}
      </a-descriptions-item>
      <a-descriptions-item label="厂房面积">
        {{ dataList.factoryArea }}
      </a-descriptions-item>
    </a-descriptions>
    <div style="text-align: center; margin-top: 20px">
      <a-button @click="upClick" type="primary"> 上一步 </a-button>
      <a-button @click="nextClick" style="margin-left: 8px"> 提交 </a-button>
      <a-button @click="formClick" style="margin-left: 8px"> 提交 </a-button>
    </div>
  </a-card>
</template>

<script>
import { vendorRequestSubmitPostRequest } from "@/services/supplier/index";
import { sysUserRegisterUserPostRequest } from "@/services/user.js";
import { regular } from "@/utils/validate";
export default {
  name: "confirm",
  props: {
    dataList: {
      type: [Object, null],
    },
  },
  data() {
    return {
      data: this.dataList,
      checked: false,
      form: {
        name: "",
        userName: "",
        password: "",
        mail: "",
        phone: "",
      },
      rules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        mail: [
          {
            pattern: regular.emailNumber,
            required: true,
            message: regular.emailNumberMsg,
            trigger: "blur",
          },
        ],
        phone: [
          {
            pattern: regular.mobileNumber,
            required: true,
            message: regular.mobileNumberMsg,
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    upClick() {
      this.$emit("on-confirm-up");
    },
    formClick() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          sysUserRegisterUserPostRequest(this.form).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加用户成功");
              this.$emit("on-confirm-next");
            } else {
              this.$message.error(response.message);
            }
          });
        }
      });
    },

    nextClick() {
      vendorRequestSubmitPostRequest({
        name: this.data.name,
        id: this.data.id,
        ruleId: "1",
        processId: "1419830902772031490",
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("提交成功");
          this.$emit("on-confirm-next");
        } else {
          this.$message.error(response.message);
        }
      });
    },
  },
};
</script>

<style scoped lang="less">
.result-error {
  text-align: left;
  .error-icon {
    color: @red-6;
    margin-right: 8px;
  }
}
</style>
