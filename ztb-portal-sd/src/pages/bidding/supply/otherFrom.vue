<template>
  <div>
    <div>
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :model="form"
        class="page-form"
      >
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="projectLocation"
              class="form-item-style"
              label="项目地点"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.projectLocation" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="tenderUser"
              class="form-item-style"
              label="招标员"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.tenderUser"
                placeholder="请选择招标员"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in userList"
                  :key="item.id"
                  :value="item.id"
                  >{{ item.employeeName }}</a-select-option
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
              <a-select
                v-model="form.currency"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in currencyList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
                 <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="offerTax"
              class="form-item-style"
              label="报价含税"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-checkbox v-model="form.offerTax" :disabled="disableds">
              </a-checkbox>
            </a-form-model-item>
          </a-col>
           <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="priceType"
              class="form-item-style"
              label="价格类型"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.priceType"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in priceTypeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="projectDeliveryDate"
              class="form-item-style"
              label="项目交付日期"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-date-picker
                style="width: 100%"
                v-model="form.projectDeliveryDate"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="tenderDocumentFree"
              class="form-item-style"
              label="招标文件免费"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-checkbox
                v-model="form.tenderDocumentFree"
                :disabled="disableds"
              >
              </a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="tenderDocumentCost"
              class="form-item-style"
              label="招标文件费"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-input
                v-model="form.tenderDocumentCost"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
         <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="locCurrencyAmount"
              class="form-item-style"
              label="预算金额/本币"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.locCurrencyAmount" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
         
 <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="marginCall"
              class="form-item-style"
              label="要求保证金"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-checkbox v-model="form.marginCall" :disabled="disableds">
              </a-checkbox>
            </a-form-model-item>
          </a-col>
           <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="earnestMoney"
              class="form-item-style"
              label="保证金"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.earnestMoney" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="oriCurrencyAmount"
              class="form-item-style"
              label="预算金额/原币"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.oriCurrencyAmount" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
              <a-col :sm="24" :md="24" :xl="24">
            <a-form-model-item
              prop="tenderMatters"
              class="form-item-style"
              label="招标事项"
            :labelCol="{ span: 3 }"
              :wrapperCol="{ span: 20 }"
            >
              <a-input v-model="form.tenderMatters" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
              <a-col :sm="24" :md="24" :xl="24">
            <a-form-model-item
              prop="explanation"
              class="form-item-style"
              label="说明"
            :labelCol="{ span: 3 }"
              :wrapperCol="{ span: 20 }"
            >
              <a-textarea
                v-model="form.explanation"
                :disabled="disableds"
                :auto-size="{ minRows: 2, maxRows: 6 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </div>
  </div>
</template>
<script>
import { queryDictionarys } from "@/utils/methods";
import { getSysUserServicePageByCondition } from "@/services/structure/index";
export default {
  name: "Other",
  data() {
    return {
      currencyList: [],
      priceTypeList: [],
      userList: [],
      biddingType: this.$store.state.account.biddingType,
      disableds: true,
      form: {
        projectLocation: "", // 项目地点
        tenderUser: undefined, //招标员
        offerTax: false, // 报价含税
        currency: undefined, // 币种
        tenderDocumentFree: false, // 招标文件免费
        tenderDocumentCost: "", //招标文件费
        priceType: undefined, //价格类型
        projectDeliveryDate: null, // 项目交付日期
        marginCall: false, // 要求保证金
        earnestMoney: "", // 保证金
        locCurrencyAmount: "", // 本币
        oriCurrencyAmount: "", // 原币
        tenderMatters: "", // 招标事项
        explanation: "", //说明
      },
    };
  },
  mounted() {

  },
  created() {
        queryDictionarys("currency").then((res) => {
      this.currencyList = res;
    });
    queryDictionarys("priceType").then((res) => {
      this.priceTypeList = res;
    });
    this.getUserALl();
    let data = this.$store.state.account.biddingVendorData;
    this.form.offerTax = data.offerTax === 0 ? false : true;
    this.form.tenderDocumentFree = data.tenderDocumentFree === 0 ? false : true;
    this.form.marginCall = data.marginCall === 0 ? false : true;
    this.form.projectLocation = data.projectLocation;
    this.form.tenderUser = data.tenderUser;
    this.form.currency = data.currency;
    this.form.tenderDocumentCost = data.tenderDocumentCost;
    this.form.priceType = data.priceType;
    this.form.earnestMoney = data.earnestMoney;
    this.form.locCurrencyAmount = data.locCurrencyAmount;
    this.form.oriCurrencyAmount = data.oriCurrencyAmount;
    this.form.tenderMatters = data.tenderMatters;
    this.form.explanation = data.explanation;
    if (
      this.form.projectDeliveryDate !== 0 &&
      this.form.projectDeliveryDate !== ""
    ) {
      this.form.projectDeliveryDate = this.$moment(
        Number(this.form.projectDeliveryDate)
      ).format("YYYY-MM-DD");
    }
  },
  methods: {
    // 请求招标员
    getUserALl() {
      getSysUserServicePageByCondition({
        currentPage: "-1",
      }).then((response) => {
        this.userList = response.data.records;
      });
    },
    validateForm() {
      let flag = null;
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          flag = true;
        } else {
          flag = false;
        }
      });
      return flag;
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
