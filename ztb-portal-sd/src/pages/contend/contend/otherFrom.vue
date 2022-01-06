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
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="offerTax"
            class="form-item-style"
            label="含税"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-checkbox v-model="form.offerTax" :disabled="disableds">
            </a-checkbox>
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
                :value="item.meaning"
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
            <a-input placeholder="请输入税率" v-model="form.taxRate" disabled />
          </a-form-model-item>
        </a-col>
        <!-- <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
            prop="taxRate"
              class="form-item-style"
              label="创建时间"
                :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-date-picker disabled />
            </a-form-model-item>
          </a-col> -->
        <!-- <a-col :sm="24" :md="12" :xl="8">
                <a-form-model-item
                  class="form-item-style"
                  label="采购组织"
                  :labelCol="{ span: 6 }"
                  :wrapperCol="{ span: 17 }"
                >
                  <a-select placeholder="请选择">
                    <a-select-option value="1">1</a-select-option>
                    <a-select-option value="2">2</a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :sm="24" :md="12" :xl="8">
                <a-form-model-item
                  class="form-item-style"
                  label="库存组织"
                  :labelCol="{ span: 6 }"
                  :wrapperCol="{ span: 17 }"
                >
                  <a-select placeholder="请选择">
                    <a-select-option value="1">1</a-select-option>
                    <a-select-option value="2">2</a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col> -->

        <!-- <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="sealedBid"
              class="form-item-style"
              label="密封报价"
                :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-checkbox v-model="form.sealedBid" :disabled="disableds">
              </a-checkbox>
            </a-form-model-item>
          </a-col> -->
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="rangeType"
            label="报价类型"
            class="form-item-style"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-select
              v-model="form.rangeType"
              placeholder="请选择报价幅度"
              :disabled="disableds"
            >
              <a-select-option value="绝对值">绝对值</a-select-option>
              <a-select-option value="百分比">百分比</a-select-option>
            </a-select>
          </a-form-model-item>
          <!-- </a-form-model-item> -->
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="priceRange"
            label="报价幅度"
            class="form-item-style"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-select
              v-if="form.rangeType === '百分比'"
              v-model="form.priceRange"
              placeholder="请选择报价幅度"
              :disabled="disableds"
            >
              <a-select-option value="10">10%</a-select-option>
              <a-select-option value="20">20%</a-select-option>
              <a-select-option value="30">30%</a-select-option>
            </a-select>
            <a-input
              v-if="form.rangeType === '绝对值'"
              placeholder="请输入绝对值"
              v-model="form.priceRange"
            />
          </a-form-model-item>
          <!-- </a-form-model-item> -->
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
                :value="item.meaning"
                >{{ item.meaning }}</a-select-option
              >
            </a-select>
          </a-form-model-item>
        </a-col>
       

        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="biddingRules"
            class="form-item-style"
            label="竞价规则"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-select
              v-model="form.biddingRules"
              placeholder="请选择竞价规则"
              :disabled="disableds"
            >
              <a-select-option
                v-for="item in biddingRulesList"
                :key="item.id"
                :value="item.meaning"
                >{{ item.meaning }}</a-select-option
              >
            </a-select>
          </a-form-model-item>
        </a-col>
       <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="publicRules"
            class="form-item-style"
            label="公开规则"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-select
              v-model="form.publicRules"
              placeholder="请选择公开规则"
              :disabled="disableds"
            >
              <a-select-option
                v-for="item in publicRulesList"
                :key="item.id"
                :value="item.meaning"
                >{{ item.meaning }}</a-select-option
              >
            </a-select>
          </a-form-model-item>
        </a-col>
         <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="bidRanking"
            class="form-item-style"
            label="排名规则"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-select
              v-model="form.bidRanking"
              placeholder="请选择排名规则"
              :disabled="disableds"
            >
              <a-select-option value="1">CNY</a-select-option>
              <a-select-option value="2">HKD</a-select-option>
              <a-select-option value="3">YSD</a-select-option>
              <a-select-option value="4">JPY</a-select-option>
              <a-select-option value="5">EUR</a-select-option>
              <a-select-option value="6">SGD</a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="deliveryPlace"
            class="form-item-style"
            label="交货地点"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-input
              placeholder="请输入交货地点"
              v-model="form.deliveryPlace"
              :disabled="disableds"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="bidNumber"
            class="form-item-style"
            label="竞价次数"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-input
              placeholder="请输入竞价次数"
              v-model="form.bidNumber"
              :disabled="disableds"
            />
          </a-form-model-item>
        </a-col>

        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="autoExtension"
            class="form-item-style"
            label="自动延期"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-select
              v-model="form.autoExtension"
              placeholder="请选择自动延期时间"
              :disabled="disableds"
            >
              <a-select-option value="1">不延期</a-select-option>
              <a-select-option value="2">10s</a-select-option>
              <a-select-option value="3">20s</a-select-option>
              <a-select-option value="4">30s</a-select-option>
              <a-select-option value="5">40s</a-select-option>
              <a-select-option value="6">50s</a-select-option>
              <a-select-option value="7">60s</a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>

        <!-- <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="autoExtension"
              class="form-item-style"
              label="审批流程"
                :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select v-model="form.autoExtension" placeholder="请选择" :disabled="disableds">
                <a-select-option value="1">不需要审批</a-select-option>
                <a-select-option value="2">流程1</a-select-option>
                <a-select-option value="3">流程2</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col> -->
       
        <a-col :sm="24" :md="24" :xl="24">
          <a-form-model-item
            prop="explaination"
            class="form-item-style"
            label="备注"
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 20 }"
          >
            <a-textarea
              placeholder="请输入备注"
              v-model="form.explaination"
              :disabled="disableds"
              :auto-size="{ minRows: 2, maxRows: 6 }"
            />
          </a-form-model-item>
        </a-col>
      </a-form-model>
    </div>
  </div>
</template>
<script>
import { queryDictionarys, permissionsEdit } from "@/utils/methods";

export default {
  name: "Other",
  data() {
    return {
      rules: {
        offerTaxCode: [
          {
            required: true,
            message: "报价税码不能为空",
            trigger: "change",
          },
        ],
        publicRules: [
          {
            required: true,
            message: "公开规则不能为空",
            trigger: "change",
          },
        ],
        biddingRules: [
          {
            required: true,
            message: "竞价规则不能为空",
            trigger: "change",
          },
        ],
        priceType: [
          {
            required: true,
            message: "价格类型不能为空",
            trigger: "change",
          },
        ],
        bidRanking: [
          {
            required: true,
            message: "排名规则不能为空",
            trigger: "change",
          },
        ],
        priceRange: [
          {
            required: true,
            message: "幅度不能为空",
            trigger: "change",
          },
        ],
        rangeType: [
          {
            required: true,
            message: "报价类型不能为空",
            trigger: "change",
          },
        ],
      },
      offerTaxCodeList: [],
      sourcingTypeList: [],
      biddingRulesList: [],
      priceTypeList: [],
      publicRulesList: [],
      form: {
        offerTax: false, // 含税
        offerTaxCode: undefined, // 报价税码
        taxRate: "", // 税率
        priceRange: 10, // 报价幅度
        rangeType: undefined, // 报价类型
        publicRules: undefined, // 公开规则
        biddingRules: undefined, // 竞价规则
        priceType: undefined, // 价格类型
        deliveryPlace: "", // 交货地点
        bidNumber: "", // 竞价次数
        autoExtension: undefined, // 自动延期
        bidRanking: undefined, // 排名规则
        explaination: "", // 说明
      },
      contendType: this.$store.state.account.contendType,
      disableds: true,
    };
  },
  created() {
    queryDictionarys("offerTaxCode").then((res) => {
      this.offerTaxCodeList = res;
    });
    queryDictionarys("sourcingType").then((res) => {
      this.sourcingTypeList = res;
    });
    queryDictionarys("biddingRules").then((res) => {
      this.biddingRulesList = res;
    });
    queryDictionarys("priceType").then((res) => {
      this.priceTypeList = res;
    });
      queryDictionarys("publicRules").then((res) => {
      this.publicRulesList = res;
    });
    if (this.contendType === "add") {
      this.disableds = false;
    } else {
      this.disableds = permissionsEdit("bid:edit");
    }
    if (this.contendType !== "add") {
      let data = this.$store.state.account.contendData;
      this.form.offerTax = data.offerTax === 0 ? false : true;
      this.form.offerTaxCode = data.offerTaxCode;
      this.form.taxRate = data.taxRate;
      this.form.priceRange = data.priceRange;
      this.form.rangeType = data.rangeType;
      this.form.sourcingType = data.sourcingType;
      this.form.publicRules = data.publicRules;
      this.form.biddingRules = data.biddingRules;
      this.form.priceType = data.priceType;
      this.form.deliveryPlace = data.deliveryPlace;
      this.form.bidNumber = data.bidNumber;
      this.form.autoExtension = data.autoExtension;
      this.form.bidRanking = data.bidRanking;
      this.form.explaination = data.explaination;
    }
  },
  mounted() {
    if (this.contendType === "add") {
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
