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
              <a-checkbox
                v-model="form.offerTax"
                :value="form.offerTax === 1 ? 'false' : 'true'"
                disabled
              />
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
                placeholder="请选择"
                disabled
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
              <a-input v-model="form.taxRate" disabled />
            </a-form-model-item>
          </a-col>
             
             <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="icash"
              class="form-item-style"
              label="含运费"
          :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-checkbox v-model="form.icash" disabled />
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
              <a-input v-model="form.deliveryPoints" disabled />
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
                disabled
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
                     <a-col :sm="24" :md="24" :xl="24">
            <a-form-model-item
              prop="remark"
              class="form-item-style"
              label="备注"
              :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 20 }"
            >
              <a-textarea
                v-model="form.remark"
                disabled
                :auto-size="{ minRows: 1, maxRows: 5 }"
            /></a-form-model-item>
          </a-col>
          <!-- <a-col :sm="24" :md="12" :xl="6">
            <a-form-model-item
              class="form-item-style"
              label="审批流程"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select placeholder="请选择" disabled>
                <a-select-option value="1">1</a-select-option>
                <a-select-option value="2">2</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col> -->
        </a-row>
      </a-form-model>
    </div>
  </div>
</template>
<script>
import { queryDictionarys } from "@/utils/methods";
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
      disableds: true,
      offerTaxCodeList: [],
      priceTypeList: [],
      form: {
        offerTax: false, //含税
        offerTaxCode: "", // 报价税码
        taxRate: "", // 税率
        deliveryPoints: "", // 交货地点
        sealedBid: false, // 密封报价
        round: "", //轮次
        priceType: "", // 价格类型
        icash: false, //含运费
        remark: "", //备注
      },
    };
  },
  mounted() {
    queryDictionarys("offerTaxCode").then((res) => {
      this.offerTaxCodeList = res;
    });
    queryDictionarys("priceType").then((res) => {
      this.priceTypeList = res;
    });
    if(this.$store.state.account.findSupplierData){
    this.form =this.$store.state.account.findSupplierData
    this.form.offerTax = this.form.offerTax === 0? false:true
    this.form.sealedBid = this.form.sealedBid === 0? false:true
    this.form.icash = this.form.icash === 0? false:true
    };
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
