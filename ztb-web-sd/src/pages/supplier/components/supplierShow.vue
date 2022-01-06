<template>
  <div>
    <a-card>
      <!-- <a-alert
        style="margin-bottom: 10px"
        :message="'企业名称:' + form.name"
        type="info"
      >
      </a-alert> -->
      <a-row>
        <a-col :span="24" style="margin-bottom:10px">
          <a-button
            v-has="['vendor:edit']"
            type="primary"
            style="margin-right:10px"
            @click="handleSave"
          >
            保存
          </a-button>
          <a-button type="info" @click="handlerBack">返回</a-button>
        </a-col>
      </a-row>
      <a-form-model
        ref="ruleForm"
        :model="form"
        :rules="rules"
        :label-col="{ span: 9 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-row class="title">登记信息</a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 14 }"
              label="企业名称"
              prop="name"
            >
              <a-input placeholder="请输入企业名称" v-model="form.name" />
            </a-form-model-item>
          </a-col>
          <!-- <a-col :sm="4" :md="3" :xl="1" style="padding-top:5px">
            <a-button type="primary" @click="showRisk" size="small"
              >风险查看</a-button
            >
          </a-col> -->
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 14 }"
              label="统一社会信用代码"
              prop="businessLicense"
            >
              <a-input
                placeholder="请输入统一社会信用代码"
                v-model="form.businessLicense"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 14 }"
              prop="vatNumber"
              label="纳税人识别号"
            >
              <a-input
                placeholder="请输入纳税人识别号"
                v-model="form.vatNumber"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 14 }"
              label="企业邮箱"
              prop="enterpriseMail"
            >
              <a-input
                placeholder="请输入企业邮箱"
                v-model="form.enterpriseMail"
              />
            </a-form-model-item>
          </a-col>

          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 14 }"
              label="电话号码"
              prop="mainPhone"
            >
              <a-input placeholder="请输入电话号码" v-model="form.mainPhone" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 14 }"
              label="邮编"
              prop="postcode"
            >
              <a-input placeholder="请输入邮编" v-model="form.postcode" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 14 }"
              label="城市"
              prop="city"
            >
              <a-input placeholder="请输入城市" v-model="form.city" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 14 }"
              label="国家"
              prop="country"
            >
              <a-input placeholder="请输入国家" v-model="form.country" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              :labelCol="{ span: 3 }"
              :wrapperCol="{ span: 15 }"
              label="网站"
              prop="website"
            >
              <a-input placeholder="请输入网站" v-model="form.website" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              :labelCol="{ span: 3 }"
              :wrapperCol="{ span: 15 }"
              label="注册地址"
              prop="registeredAddress"
            >
              <a-input
                placeholder="请输入注册地址"
                v-model="form.registeredAddress"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row class="title">联系人信息</a-row>
        <a-row>
          <supplier-user :vendor-id="form.id"></supplier-user>
        </a-row>
        <a-row class="title">附件</a-row>
        <a-row>
          <supplier-attachment :vendor-id="form.id"></supplier-attachment>
        </a-row>
      </a-form-model>
    </a-card>
  </div>
</template>

<script>
import { putVendor } from "@/services/supplier/index"
import supplierUser from "./supplierUser.vue"
import supplierAttachment from "./supplierAttachment.vue"
import { mapGetters } from "vuex"

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
    title: "操作",
    dataIndex: "operation",
    key: "operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
]

export default {
  name: "SupplierShow",
  components: { supplierUser, supplierAttachment },
  data() {
    return {
      columns,
      certificateTypeList: [],
      form: {
        id: "",
        name: "",
        businessLicense: "",
        vatNumber: "",
        enterpriseMail: "",
        mainPhone: "",
        postCode: "",
        city: "",
        country: "",
        website: "",
        registeredAddress: "",
      },
      rules: {
        name: [
          {
            required: true,
            message: "采购组织名称不能为空",
            trigger: "blur",
          },
        ],
      },
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  activated() {},
  created() {
    this.form = JSON.parse(sessionStorage.getItem("supplierForm"))
  },
  methods: {
    showRisk() {
      this.$message.success("无风险")
    },
    handlerBack() {
      this.$router.push({ path: "/supply/supplier" })
    },
    handleSave() {
      putVendor(this.form).then(result => {
        if (result.code === 0) {
          this.$message.success("修改成功")
        } else {
          this.$message.success("修改失败")
        }
      })
    },
  },
}
</script>
<style lang="less" scoped>
.btn-sty {
  text-align: center;
  margin-bottom: 20px;
  button {
    margin-left: 10px;
  }
}
.menu-content {
  float: left;
  width: calc(100% - 160px);
  padding: 20px;
}
.v-enter,
.v-leave-to {
  opacity: 0;
  transform: translateX(100px);
}

.v-enter-active,
.v-leave-active {
  transition: all 0.5s ease;
}
</style>
