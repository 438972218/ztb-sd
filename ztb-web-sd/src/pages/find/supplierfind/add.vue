<template>
  <div>
    <!-- <div :class="advanced ? null: 'fold'"> -->
    <a-card class="card-top">
      <a-row style="text-align: center; margin: 5px 0 10px"
        >{{ form.requestTitle }}({{ form.oddNumber }})</a-row
      >
      <a-form-model layout="horizontal" ref="ruleForm" :model="form">
        <a-row>
          <a-col :sm="24" :md="24" :xl="24">
            <a-form-model-item
              prop="title"
              class="form-item-style"
              label="询价单描述"
              :labelCol="{ span: 3 }"
              :wrapperCol="{ span: 20 }"
            >
              <a-input v-model="form.title" disabled />
            </a-form-model-item>
          </a-col>
          <!-- <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="oddNumber"
              class="form-item-style"
              label="询价单号"
       :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.oddNumber" disabled />
            </a-form-model-item>
          </a-col> -->
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="purchasingOrganization"
              class="form-item-style"
              label="采购组织"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.purchasingOrganization" disabled />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="companyCode"
              class="form-item-style"
              label="公司代码"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.companyCode" disabled />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="inquirySheetType"
              class="form-item-style"
              label="单据类型"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.inquirySheetType"
                placeholder="请选择"
                disabled
              >
                <a-select-option
                  v-for="item in inquirySheetTypeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>

          <!-- <a-col :sm="24" :md="12" :xl="6">
            <a-form-model-item
              class="form-item-style"
              label="状态"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select v-model="form.inquirySheetType" placeholder="请选择" disabled>
                <a-select-option value="1">1</a-select-option>
                <a-select-option value="2">2</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col> -->
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="inquiryMode"
              class="form-item-style"
              label="询价方式"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.inquiryMode"
                placeholder="请选择"
                disabled
              >
                <a-select-option
                  v-for="item in inquiryModelList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="signType"
              class="form-item-style"
              label="标的类型"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select v-model="form.signType" placeholder="请选择" disabled>
                <a-select-option
                  v-for="item in signTypeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="quotationDeadline"
              class="form-item-style"
              label="报价截止时间"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-date-picker
                style="width: 100%"
                v-model="form.quotationDeadline"
                format="YYYY-MM-DD"
                disabled
              />
            </a-form-model-item>
          </a-col>

          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="exchangeRate"
              class="form-item-style"
              label="汇率"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.exchangeRate" disabled />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="paymentMode"
              class="form-item-style"
              label="付款方式"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.paymentMode"
                placeholder="请选择"
                disabled
              >
                <a-select-option
                  v-for="item in paymentModeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="currency"
              class="form-item-style"
              label="币种"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select v-model="form.currency" placeholder="请选择" disabled>
                <a-select-option
                  v-for="item in currencyList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <!-- <a-card class="card-top" v-if="findSupplierType === 3">
        <div class="content-title">
          审批信息<span style="color: red">（*为必填项）</span>
        </div>
          <find-audit></find-audit>
      </a-card> -->
    <a-card class="card-top">
      <!-- <a-button type="primary" style="margin-bottom: 16px" @click="toggleCollapsed">K
      <a-icon :type="collapsed ? 'menu-unfold' : 'menu-fold'" />
    </a-button> -->
      <a-menu
        mode="inline"
        style="width: 256px; float: left"
        :default-selected-keys="selectedKeys"
        @click="onOpenChange"
      >
        <a-menu-item
          key="SupplierDetail"
          v-if="findSupplierType === '已参与' || findSupplierType === '未报价'"
        >
          报价信息
        </a-menu-item>
        <a-menu-item key="OtherFrom"> 其他信息 </a-menu-item>
        <a-menu-item key="Items"> 物品明细 </a-menu-item>
        <!-- <a-menu-item key="Offer"> 供应商列表 </a-menu-item> -->
        <a-menu-item key="File"> 附件信息 </a-menu-item>
      </a-menu>
      <a-row class="menu-content">
        <transition mode="out-in">
          <component :is="componentName" :findId="findId" disabled></component>
        </transition>
      </a-row>
    </a-card>
    <a-row>
      <a-col :span="24" class="btn-sty" style="margin-top: 20px">
        <!-- <a-button
          type="primary"
         @click="joinStateChange(form, '待参与')"
          v-if="findSupplierType === '已参与'"
        >
          待参与
        </a-button> -->
        <a-button
          type="primary"
          @click="joinStateChange(form, '已参与')"
          v-if="findSupplierType === '待参与'"
        >
          参与
        </a-button>
        <a-button
          type="primary"
          @click="joinStateChange(form, '已放弃')"
          v-if="findSupplierType === '待参与'"
        >
          放弃
        </a-button>
        <a-button
          type="primary"
          @click="submitClick()"
          v-if="findSupplierType === '已参与' || findSupplierType === '未报价'"
        >
          提交
        </a-button>
        <a-button @click="back()"> 返回 </a-button>
      </a-col>
    </a-row>
    <!-- <a-row>
      <a-col :span="24" class="btn-sty" style="margin-top: 20px">
        <a-button type="primary"> 保存 </a-button>
        <a-button type="primary"> 提交审批 </a-button>
        <a-button type="primary"> 操作记录 </a-button>
        <a-button @click="back()"> 返回 </a-button>
      </a-col>
    </a-row> -->
  </div>
</template>
<script>
import Offer from "./offer.vue";
import Items from "./items.vue";
import File from "./file.vue";
import SupplierDetail from "./supplierdetail.vue";
import OtherFrom from "./otherFrom.vue";
import { queryDictionarys } from "@/utils/methods";
import {
  inquiryVendorMaterialSubmitPutRequest,
  inquiryVendorGetRequest,
  inquiryVendorPutRequest
} from "@/services/source/find";
import { mapGetters } from "vuex";

export default {
  name: "Addfind",
  components: {
    Offer,
    Items,
    File,
    OtherFrom,
    SupplierDetail,
  },
  // props: {
  //   num: {
  //     type: String,
  //   },
  // },
  data () {
    return {
      form: {
        oddNumber: "", //询价单号
        purchasingOrganization: "", // 采购组织
        companyCode: "", // 公司代码
        title: "", // 询价单标题
        inquirySheetType: "", // 询价单类型
        status: 0, //状态
        inquiryMode: "", // 询价方式
        paymentMode: "", //付款方式
        currency: "", //币种
        exchangeRate: "", //汇率
        quotationDeadline: "", //报价截止时间
        signType: "", //标的类型
      },
      findId: "",
      inquirySheetTypeList: [],
      inquiryModelList: [],
      paymentModeList: [],
      currencyList: [],
      signTypeList: [],
      selectedKeys: ["OtherFrom"],
      disableds: true,
      componentName: "OtherFrom",
      findSupplierType: this.$store.state.account.findSupplierType,
      activeKey: [],
      data: this.$store.state.account.findSupplierData,
      rootSubmenuKeys: ["sub1", "sub2", "sub4"],
      queryId: "",
    };
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  created () {
    queryDictionarys("inquirySheetType").then((res) => {
      this.inquirySheetTypeList = res;
    });
    queryDictionarys("inquiryModel").then((res) => {
      this.inquiryModelList = res;
    });
    queryDictionarys("paymentMode").then((res) => {
      this.paymentModeList = res;
    });
    queryDictionarys("currency").then((res) => {
      this.currencyList = res;
    });
    queryDictionarys("signType").then((res) => {
      this.signTypeList = res;
    });
    if (this.$store.state.account.findSupplierData) {
      this.form = this.$store.state.account.findSupplierData;
      this.form.quotationDeadline = this.$moment(
        Number(this.form.quotationDeadline)
      ).format("YYYY-MM-DD");
      this.findId = this.form.id;
    }
    this.findSupplierType === "edit"
      ? false
      : this.findSupplierType === "add"
        ? false
        : true;
    if (this.findSupplierType === "已参与" || this.findSupplierType === "未报价") {
      this.componentName = "SupplierDetail";
      this.selectedKeys = ["SupplierDetail"];
    }
  },
  mounted () {
    this.getQueryId();
  },
  methods: {
    // 提交审批
    submitClick () {
      inquiryVendorMaterialSubmitPutRequest({
        inquirySheetId: this.form.id,
        inquiryVendorId: this.queryId,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("提交成功");
          this.back()
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 供应商id查询
    getQueryId () {
      inquiryVendorGetRequest({
        inquirySheetId: this.form.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.queryId = response.data.records[0].id;
      });
    },
    // 修改供应商状态
    joinStateChange (data, state) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "是否确定此操作吗？",
        centered: true,
        async onOk () {
          const response = await inquiryVendorPutRequest({
            inquirySheetId: data.id,
            vendorCode: that.user.code,
            vendorStatus: state,
            deleted: 0,
          })
          if (response.code === 0) {
            that.$message.success("操作成功");
            that.back()
            // that.findSupplierType === state 
            // that.$store.state.account.findSupplierType = state
          } else {
            that.$message.error(response.message);
          }
        },
        onCancel () { },
      });
    },
    onOpenChange (openKeys) {
      this.componentName = openKeys.key;
    },
    back () {
      this.$router.push({
        path: "/portal/supplierfind",
      });
    },
    onChange (e) {
      console.log(e.target.checked);
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
  width: calc(100% - 300px);
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
  text-align: center;
  margin: ;
}
</style>
