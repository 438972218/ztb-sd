<template>
  <div>
    <a-card class="card-top">
      <audit></audit>
    </a-card>
    <a-card class="card-top">
      <div style="margin-bottom: 20px">
        <a-collapse v-model="activeKey">
          <a-collapse-panel key="basic" header="基本信息">
            <a-form-model
              ref="ruleForm"
              :model="form"
              :rules="rules"
              :label-col="{ span: 7 }"
              :wrapper-col="{ span: 16 }"
            >
              <a-row class="title">登记信息<span>(*为必填项)</span></a-row>
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
                <a-col :span="8" class="btStyle">
                  <a-button type="primary"> 供应商效验 </a-button>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="统一社会信用代码"
                    prop="uscc"
                  >
                    <a-input
                      placeholder="请输入统一社会信用代码"
                      v-model="form.uscc"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    prop="legalRepresentative"
                    label="法定代表人"
                  >
                    <a-input
                      placeholder="请输入法定代表人"
                      v-model="form.legalRepresentative"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="登记状态"
                    prop="registrationStatus"
                  >
                    <a-input
                      placeholder="请输入登记状态"
                      v-model="form.registrationStatus"
                    />
                  </a-form-model-item>
                </a-col>

                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="注册资本(万元)"
                    prop="registeredCapital"
                  >
                    <a-input
                      placeholder="请输入注册资本(万元)"
                      v-model="form.registeredCapital"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="组织机构代码"
                    prop="orzCode"
                  >
                    <a-input
                      placeholder="请输入组织机构代码"
                      v-model="form.orzCode"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="工商注册号"
                    prop="icrn"
                  >
                    <a-input
                      placeholder="请输入工商注册号"
                      v-model="form.icrn"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="纳税人识别号"
                    prop="taxpayNum"
                  >
                    <a-input
                      placeholder="请输入纳税人识别号"
                      v-model="form.taxpayNum"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="企业类型"
                    prop="type"
                  >
                    <a-input placeholder="请输入企业类型" v-model="form.type" />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="所属地区"
                    prop="area"
                  >
                    <a-input placeholder="请输入所属地区" v-model="form.area" />
                  </a-form-model-item>
                </a-col>
                <a-col :span="24">
                  <a-form-model-item
                    :labelCol="{ span: 3 }"
                    :wrapperCol="{ span: 15 }"
                    label="所属行业"
                    prop="industry"
                  >
                    <a-input
                      placeholder="请输入所属行业"
                      v-model="form.industry"
                    />
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
                <a-col :span="24">
                  <a-form-model-item
                    :labelCol="{ span: 3 }"
                    :wrapperCol="{ span: 15 }"
                    label="经营范围"
                    prop="businessScope"
                  >
                    <a-textarea
                      placeholder="请输入经营范围"
                      v-model="form.businessScope"
                      :auto-size="{ minRows: 2, maxRows: 5 }"
                    />
                  </a-form-model-item>
                </a-col>
              </a-row>

              <a-row class="title">联系人信息<span>(*为必填项)</span></a-row>
              <a-row>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="姓名"
                    prop="contactName"
                  >
                    <a-input
                      placeholder="请输入姓名"
                      v-model="form.contactName"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="职位"
                    prop="contactTitle"
                  >
                    <a-input
                      placeholder="请输入职位"
                      v-model="form.contactTitle"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="手机号码"
                    prop="contactMobile"
                  >
                    <a-input
                      placeholder="请输入手机号码"
                      v-model="form.contactMobile"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="邮箱"
                    prop="contactEmail"
                  >
                    <a-input
                      placeholder="请输入邮箱"
                      v-model="form.contactEmail"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="证件类型"
                    prop="contactIdentityType"
                  >
                    <a-select
                      v-model="form.contactIdentityType"
                      placeholder="请选择证件类型"
                    >
                      <a-select-option
                        v-for="item in certificateTypeList"
                        :key="item.id"
                        :value="item.meaning"
                        >{{ item.meaning }}</a-select-option
                      >
                    </a-select>
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="证件号码"
                    prop="contactIdentityNum"
                  >
                    <a-input
                      placeholder="请输入证件号码"
                      v-model="form.contactIdentityNum"
                    />
                  </a-form-model-item>
                </a-col>
              </a-row>
              <a-row>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="正面照片"
                  >
                    <img
                      v-if="form.identityFront"
                      :src="form.identityFront"
                      style="width: 100%"
                      alt="avatar"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="背面照片"
                  >
                    <img
                      v-if="form.identityReverse"
                      :src="form.identityReverse"
                      style="width: 100%"
                      alt="avatar"
                    />
                  </a-form-model-item>
                </a-col>
              </a-row>

              <a-row class="title">业务信息<span>(*为必填项)</span></a-row>
              <a-row>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="商业模式"
                    prop="businessModel"
                  >
                    <a-input
                      placeholder="请输入商业模式"
                      v-model="form.businessModel"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="代理品牌"
                    prop="agentBrand"
                  >
                    <a-input
                      placeholder="请输入企代理品牌"
                      v-model="form.agentBrand"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="供货品类备注"
                    prop="suplyCategoryRemark"
                  >
                    <a-input
                      placeholder="请输入供货品类备注"
                      v-model="form.suplyCategoryRemark"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="主要客户"
                    prop="mainCustomer"
                  >
                    <a-input
                      placeholder="请输入主要客户"
                      v-model="form.mainCustomer"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="主营品类"
                    prop="mainCategory"
                  >
                    <a-input
                      placeholder="请输入主营品类"
                      v-model="form.mainCategory"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="厂房性质"
                    prop="factoryNature"
                  >
                    <a-input
                      placeholder="请输入厂房性质"
                      v-model="form.factoryNature"
                    />
                  </a-form-model-item>
                </a-col>
                <a-col :sm="24" :md="12" :xl="8">
                  <a-form-model-item
                    :labelCol="{ span: 9 }"
                    :wrapperCol="{ span: 14 }"
                    label="厂房面积"
                    prop="factoryArea"
                  >
                    <a-input
                      placeholder="请输入厂房面积"
                      v-model="form.factoryArea"
                    />
                  </a-form-model-item>
                </a-col>
              </a-row>

              <a-row class="title">许可证信息<span>(*为必填项)</span></a-row>
              <a-row>
                <a-table
                  :columns="columns"
                  :data-source="form.vendorRequestCertificateVOS"
                >
                  <template slot="operation" slot-scope="text, record, index">
                    <span @click="c_delete(index)" class="operation-style">
                      删除
                    </span>
                  </template>
                </a-table>
              </a-row>
            </a-form-model>
          </a-collapse-panel>
        </a-collapse>
      </div>
    </a-card>
    <a-card class="card-top"> </a-card>
    <a-row>
      <a-col :span="24" class="btn-sty" style="margin-top: 20px">
        <a-button
          type="primary"
          @click="submitFind()"
          v-if="this.supplierType === '审批'"
        >
          完成审批
        </a-button>
        <!-- <a-button
          type="primary"
          @click="saveFind"
          v-if="this.supplierType === 'edit'"
        >
          保存
        </a-button>
  
        <a-button
          type="primary"
          @click="approvalBy()"
          v-if="this.supplierType === '待审核'"
        >
          提交审批
        </a-button> -->
        <a-button @click="back()"> 返回 </a-button>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import {
  inquirySheetPutRequest,
  inquirySheetAgreePostRequest,
  inquiryVendorMaterialDickerPutRequest,
} from "@/services/source/find";
import { queryDictionarys } from "@/utils/methods";
import {
  vendorRequestShowGetVendor,
  vendorRequestAgreePostRequest,
} from "@/services/supplier/index";
import { mapGetters } from "vuex";
import Audit from "./supplierAudit.vue";
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
];
export default {
  name: "addFind",
  components: { Audit },
  data () {
    return {
      columns,
      certificateTypeList: [],
      activeKey: ["basic"],
      form: {
        // 企业信息
        name: "", // 企业名称
        uscc: "", // 统一社会信用代码
        legalRepresentative: "", // 法定代表人
        registrationStatus: "", // 登记状态
        registeredCapital: "", // 注册资本
        orzCode: "", // 组织机构代码
        icrn: "", // 工商注册号
        taxpayNum: "", // 纳税人识别号
        type: "", // 企业类型
        area: "", // 所属地区
        industry: "", // 所属行业
        registeredAddress: "", // 注册地址
        businessScope: "", // 经营范围
        //联系人信息
        contactName: "", // 联系人姓名
        contactTitle: "", // 联系人职位
        contactMobile: "", // 联系人手机号
        contactEmail: "", // 联系人邮箱
        contactIdentityType: undefined, // 	联系人证件类型
        contactIdentityNum: "", // 	联系人证件号码
        identityFront: "", // 证件正面照片
        identityReverse: "", // 	证件反面照片
        // 业务信息
        businessModel: "", // 商业模式
        agentBrand: "", // 代理品牌
        suplyCategoryRemark: "", // 供货品类备注
        mainCategory: "", // 主营品类
        mainCustomer: "", // 主要客户
        factoryArea: "", // 	厂房面积
        factoryNature: "", // 	厂房性质
        vendorCertificateDTOS: [],
      },
      rules: {
        purchasingOrganization: [
          {
            required: true,
            message: "采购组织名称不能为空",
            trigger: "blur",
          },
        ],
        companyCode: [
          {
            required: true,
            message: "采购组织名称不能为空",
            trigger: "blur",
          },
        ],
        inquirySheetTitle: [
          {
            required: true,
            message: "询价单标题不能为空",
            trigger: "blur",
          },
        ],
        inquirySheetType: [
          {
            required: true,
            message: "询价单类型不能为空",
            trigger: "change",
          },
        ],
        inquiryMode: [
          {
            required: true,
            message: "询价方式不能为空",
            trigger: "change",
          },
        ],
        paymentMode: [
          {
            required: true,
            message: "付款方式不能为空",
            trigger: "change",
          },
        ],
        currency: [
          {
            required: true,
            message: "币种不能为空",
            trigger: "change",
          },
        ],
        quotationDeadline: [
          {
            required: true,
            message: "报价截止时间不能为空",
            trigger: "change",
          },
        ],
        ruleId: [
          {
            required: true,
            message: "单号规则ID不能为空",
            trigger: "change",
          },
        ],
        configVersion: [
          {
            required: true,
            message: "流程配置版本不能为空",
            trigger: "blur",
          },
        ],
        processId: [
          {
            required: true,
            message: "流程ID不能为空",
            trigger: "change",
          },
        ],
      },
      inquirySheetTypeList: [],
      inquiryModelList: [],
      paymentModeList: [],
      currencyList: [],
      signTypeList: [],
      oddRuleList: [],
      processList: [],
      findId: null,
      supplierType: this.$store.state.account.supplierType,
      id: "",
      current: 0,
      stepStyle: {
        marginBottom: "0px",
        boxShadow: "0px -1px 0 0 #e8e8e8 inset",
      },
    };
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  activated () { },
  mounted () {
    queryDictionarys("certificateType").then((res) => {
      this.certificateTypeList = res;
    });
    // if (this.supplierType === "edit") {
    //   this.form = this.$store.state.account.findData;
    //   this.findId = this.form.id;
    //   if (this.form.quotationDeadline) {
    //     this.form.quotationDeadline = this.$moment(
    //       Number(this.form.quotationDeadline)
    //     ).format("YYYY-MM-DD");
    //   }
    // }
    //   if (this.supplierType === "待审核") {
    //     this.componentName = "Audit";
    //     this.selectedKeys = ["Audit"];
    //   } else if (this.supplierType === "已发布") {
    //     this.componentName = "Negotiate";
    //     this.selectedKeys = ["Negotiate"];
    //   } else if (this.supplierType === "待价格初审") {
    //     this.componentName = "Trial";
    //     this.selectedKeys = ["Trial"];
    //   } else if (this.supplierType === "待采购核价") {
    //     this.componentName = "PurchaseAudit";
    //     this.selectedKeys = ["PurchaseAudit"];
    //   }
    //   else if (this.supplierType === "待核价审批") {
    //     this.componentName = "PriceApproval";
    //     this.selectedKeys = ["PriceApproval"];
    //   }
    let list = this.$store.state.account.supplierData;
    if (list) {
      this.getShowAll(list.id);
    }
    //   if (this.form.quotationDeadline) {
    //     this.form.quotationDeadline = this.$moment(
    //       Number(this.form.quotationDeadline)
    //     ).format("YYYY-MM-DD");
    //   }
    // }
  },
  methods: {
    // 查询数据
    // 请求修改数据
    getShowAll (id) {
      vendorRequestShowGetVendor(id).then((response) => {
        this.form = response.data;
      });
    },
    // 提交审批
    submitFind () {
      vendorRequestAgreePostRequest({
        userId: this.user.id,
        id: this.form.id,
        requestId: this.form.requestId,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("提交成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },

    // 审批通过
    approvalBy () {
      inquirySheetAgreePostRequest({
        userId: this.user.id,
        requestId: this.form.requestId,
        description: this.$refs.refChild.form.description, //单号规则ID
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("审批成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    //  发布
    releaseBy () {
      inquirySheetAgreePostRequest({
        userId: this.user.id,
        requestId: this.form.requestId,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("发布成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 价格提交到初审
    priceBy () {
      inquirySheetAgreePostRequest({
        userId: this.user.id,
        requestId: this.form.requestId,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("提交成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 提交还价导供应商
    counterofferBy () {
      inquiryVendorMaterialDickerPutRequest({
        inquirySheetId: this.findId,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("提交成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 价格初审通过
    trialBy () {
      inquirySheetAgreePostRequest({
        userId: this.user.id,
        requestId: this.form.requestId,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("审核成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },

    // 修改
    saveFind () {
      if (this.form.quotationDeadline) {
        this.form.quotationDeadline = this.$moment(
          this.form.quotationDeadline
        ).format("YYYY-MM-DD");
      }
      let childFrom = { ...this.form, ...this.$refs.refChild.form };
      inquirySheetPutRequest({
        ...childFrom,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("修改成功");
          // this.dataClose();
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    back () {
      this.$router.push({
        path: "/supply/supplier",
      });
    },
  },
};
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
