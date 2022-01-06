<template>
  <div>
    <div>
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :model="form"
        :rules="rules"
        class="page-form"
      >
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="offerTax"
              class="form-item-style"
              label="含税"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-checkbox v-model="form.offerTax" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="offerTaxCode"
              class="form-item-style"
              label="报价税码"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.offerTaxCode"
                placeholder="请选择报价税码"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in offerTaxCodeList"
                  :key="item.id"
                  :value="item.id"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="taxRate"
              class="form-item-style"
              label="税率"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input
                placeholder="请输入税率"
                v-model="form.taxRate"
                disabled
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row class="-row">
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="sealedBid"
              class="form-item-style"
              label="密封报价"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-checkbox v-model="form.sealedBid" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
          <!-- <a-col :sm="24" :md="12" :xl="6">
            <a-form-model-item
              class="form-item-style"
              label="创建时间"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-date-picker disabled />
            </a-form-model-item>
          </a-col> -->
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="round"
              class="form-item-style"
              label="轮次"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input placeholder="请输入轮次" v-model="form.round" disabled />
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
                placeholder="请选择价格类型"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in priceTypeList"
                  :key="item.id"
                  :value="item.id"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="icash"
              class="form-item-style"
              label="含运费"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-checkbox v-model="form.icash" :disabled="disableds" />
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
                placeholder="请选择币种"
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
              prop="deliveryPoints"
              class="form-item-style"
              label="交货地点"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input
                placeholder="请输入交货地点"
                v-model="form.deliveryPoints"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
          <!-- <a-col :sm="24" :md="12" :xl="6">
            <a-form-model-item
              class="form-item-style"
              label="审批流程"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select placeholder="请选择" :disabled="disableds">
                <a-select-option value="1">1</a-select-option>
                <a-select-option value="2">2</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col> -->
        </a-row>
        <a-row>
          <a-col :sm="24" :md="24" :xl="24">
            <a-form-model-item
              prop="remark"
              class="form-item-style"
              label="备注"
              :labelCol="{ span: 3 }"
              :wrapperCol="{ span: 20 }"
            >
              <a-textarea
                placeholder="请输入备注"
                v-model="form.remark"
                :disabled="disableds"
                :auto-size="{ minRows: 2, maxRows: 5 }"
            /></a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </div>
  </div>
</template>
<script>
import { queryDictionarys, permissionsEdit } from "@/utils/methods";
export default {
  name: "Other",
  props: ["id"],
  data() {
    return {
      rules: {
        deliveryPoints: [
          {
            required: true,
            message: "交货地点不能为空",
            trigger: "blur",
          },
        ],
      },

      findtype: this.$store.state.account.findType,
      disableds: true,
      offerTaxCodeList: [],
      priceTypeList: [],
      currencyList: [],
      form: {
        offerTax: false, //含税
        offerTaxCode: undefined, // 报价税码
        taxRate: "", // 税率
        deliveryPoints: "", // 交货地点
        sealedBid: false, // 密封报价
        round: "", //轮次
        priceType: undefined, // 价格类型
        icash: false, //含运费
        currency: undefined, //币种
        remark: "", //备注
      },
    };
  },
  created() {
    queryDictionarys("offerTaxCode").then((res) => {
      this.offerTaxCodeList = res;
    });
    queryDictionarys("priceType").then((res) => {
      this.priceTypeList = res;
    });
    queryDictionarys("currency").then((res) => {
      this.currencyList = res;
    });
    if (this.findtype === "add") {
      this.disableds = false;
    } else {
      this.disableds = permissionsEdit("bid:edit");
    }
    if (this.findtype !== "add") {
      let data = this.$store.state.account.findData;
      this.form.offerTax = data.offerTax === 0 ? false : true;
      this.form.sealedBid = data.sealedBid === 0 ? false : true;
      this.form.icash = data.icash === 0 ? false : true;
      this.form.offerTaxCode = data.offerTaxCode;
      this.form.taxRate = data.taxRate;
      this.form.deliveryPoints = data.deliveryPoints;
      this.form.round = data.round;
      this.form.priceType = data.priceType;
    }
  },
  mounted() {
    if (this.findtype === "add") {
      this.$refs.ruleForm.resetFields();
    }
  },
  methods: {
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
