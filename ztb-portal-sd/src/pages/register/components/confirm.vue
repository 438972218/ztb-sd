<template>
  <a-card class="result-error" :bordered="false">
    <a-descriptions title="登记信息">
      <a-descriptions-item label="企业名称">
        {{ dataList.name }}
      </a-descriptions-item>
      <a-descriptions-item label="统一社会信用代码">
        {{ dataList.businessLicense }}
      </a-descriptions-item>
      <a-descriptions-item label="纳税人识别号">
        {{ dataList.vatNumber }}
      </a-descriptions-item>
      <a-descriptions-item label="企业邮箱">
        {{ dataList.enterpriseMail }}
      </a-descriptions-item>
      <a-descriptions-item label="电话号码">
        {{ dataList.mainPhone }}
      </a-descriptions-item>
      <a-descriptions-item label="邮编">
        {{ dataList.postcode }}
      </a-descriptions-item>
      <a-descriptions-item label="城市">
        {{ dataList.city }}
      </a-descriptions-item>
      <a-descriptions-item label="国家">
        {{ dataList.country }}
      </a-descriptions-item>
      <a-descriptions-item label="网站">
        {{ dataList.website }}
      </a-descriptions-item>
      <a-descriptions-item label="注册地址">
        {{ dataList.registeredAddress }}
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="联系人信息">
      <a-descriptions-item label="姓名">
        {{ dataList.registerUserDTO.name }}
      </a-descriptions-item>
      <a-descriptions-item label="用户名">
        {{ dataList.registerUserDTO.userName }}
      </a-descriptions-item>
      <a-descriptions-item label="密码"> ****** </a-descriptions-item>
      <a-descriptions-item label="手机号">
        {{ dataList.registerUserDTO.phone }}
      </a-descriptions-item>
      <a-descriptions-item label="邮箱">
        {{ dataList.registerUserDTO.mail }}
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="供应商附件" bordered>
      <template v-for="item in dataList.vendorAttachmentDTOS">
        <a-descriptions-item :key="item.attachmentUrl" label="附件名称">
          {{ item.attachmentName }}.{{ item.attachmentType }}
        </a-descriptions-item>
      </template>
    </a-descriptions>
    <div style="text-align: center; margin-top: 20px">
      <a-button @click="upClick" type="primary"> 上一步 </a-button>
      <!-- <a-button @click="nextClick" style="margin-left: 8px"> 保存 </a-button> -->
      <a-button @click="formClick" :loading="formSave" style="margin-left: 8px">
        提交
      </a-button>
    </div>
  </a-card>
</template>

<script>
import {
  vendorRequestSubmitPostRequest,
  vendorRegister,
} from "@/services/supplier/index"
import { sysUserRegisterUserPostRequest } from "@/services/user.js"
import { regular } from "@/utils/validate"
export default {
  name: "confirm",
  props: {
    dataList: {
      type: [Object, null],
    },
  },
  data () {
    return {
      formSave: false,
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
    }
  },
  methods: {
    upClick () {
      this.$emit("on-confirm-up", { ...this.dataList })
    },
    formClick () {
      //提交
      if (this.dataList.name == "") {
        this.$message.error("请完善信息")
        return
      }
      this.formSave = true
      vendorRegister(this.dataList).then(response => {
        if (response.code === 0) {
          this.formSave = false
          this.$message.success("注册成功")
          this.$emit("on-confirm-next")
        } else {
          // this.$message.error("用户重复")
          this.$message.error(response.message)
        }
      })
    },
    nextClick () { },
  },
}
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
