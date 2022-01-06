<template>
  <div>
    <a-card class="card-top">
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :model="form"
        class="page-form"
      >
         <a-row style="margin: 0 0 12px 0">
        <a-col style="text-align: center" :span="24"
          >{{ form.oddNumber }}</a-col
        >
      </a-row>
        <a-row>
                 <a-col :sm="24" :md="24" :xl="24">
            <a-form-model-item
              prop="title"
              class="form-item-style"
              label="竞价单描述"
              :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 20 }"
            >
              <a-input v-model="form.title" disabled value="" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="oddNumber"
              class="form-item-style"
              label="竞价单号"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.oddNumber" disabled />
            </a-form-model-item>
          </a-col>
        <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="purchasingOrganization"
              class="form-item-style"
              label="采购组织名称"
                :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-input
                v-model="form.purchasingOrganization"
                :disabled="disableds"
                value=""
              />
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
              <a-input
                v-model="form.companyCode"
                :disabled="disableds"
                value=""
              />
            </a-form-model-item>
          </a-col>
         <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="paidType"
              class="form-item-style"
              label="单据类型"
                :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.paidType"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in paidTypeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
         <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="paidDirection"
              class="form-item-style"
              label="竞价方向"
             :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.paidDirection"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in paidDirectionList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="paidMode"
              class="form-item-style"
              label="竞价方式"
               :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.paidMode"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in paidModeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
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
                :disabled="disableds"
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
              prop="exchangeRate"
              class="form-item-style"
              label="汇率"
               :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.exchangeRate" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="offerStartTime"
              class="form-item-style"
              label="报价开始时间"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-date-picker
              style="width: 100%"
                v-model="form.offerStartTime"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
         <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="offerEndTime"
              class="form-item-style"
              label="报价截止时间"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-date-picker
              style="width: 100%"
                v-model="form.offerEndTime"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
        <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="bidType"
              class="form-item-style"
              label="标的类型"
             :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.bidType"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in bidTypeList"
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
    <a-card class="card-top">
      <a-menu
        mode="inline"
        style="width: 156px; float: left"
        :default-selected-keys="selectedKeys"
        @click="onOpenChange"
      >
        <!-- <a-menu-item key="Audit" v-if="this.contendVendorType === '2'">
          审批信息
        </a-menu-item> -->
        <!-- <a-menu-item key="Items">
          物品信息
        </a-menu-item> -->
        <a-menu-item key="Material" v-if="this.contendVendorType !== '已发布'">
          竞价信息
        </a-menu-item>
        <a-menu-item
          key="MyBidding"
          v-if="
            this.contendVendorType === '已发布'
          "
        >
          我要竞价
        </a-menu-item>
        <a-menu-item key="OtherFrom"> 其他信息 </a-menu-item>
        <a-menu-item key="file"> 附件信息 </a-menu-item>
      </a-menu>
      <a-row class="menu-content">
        <transition mode="out-in">
          <component
            :is="componentName"
            :contendVendorId="contendVendorId"
            :disabled="disableds"
          ></component>
        </transition>
      </a-row>
    </a-card>
    <a-row>
      <a-col :span="24" class="btn-sty" style="margin-top: 20px">
        <!-- <span
          @click="joinStateChange(scope.record, '已参与')"
          v-if="scope.record.vendorStatus === '待参与'"
          class="operation-style"
        >
        </span>
        <span
          @click="joinStateChange(scope.record, '已放弃')"
          v-if="scope.record.vendorStatus === '待参与'"
          class="operation-style"
        >
        </span> -->
        <a-button
          @click="joinStateChange(form, '已参与')"
          v-if="contendVendorType === '待参与'"
          type="primary"
        >
          参与
        </a-button>
        <a-button
          @click="joinStateChange(form, '已放弃')"
          v-if="contendVendorType === '待参与'"
          type="primary"
        >
          放弃
        </a-button>
        <!-- <a-button type="primary" v-if="this.contendVendorType === 'add'">
          创建
        </a-button> -->
        <a-button @click="back()"> 返回 </a-button>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import File from "./file.vue";
import Items from "./items.vue";
import Material from "./material.vue";
import MyBidding from "./mybidding.vue";
import OtherFrom from "./otherFrom.vue";
import { queryDictionarys } from "@/utils/methods";
import { paidVendorStatusPutRequest } from "@/services/source/contend";
import { mapGetters } from "vuex";
export default {
  name: "Addfind",
  components: { File, Material, Items, MyBidding, OtherFrom },
  props: {
    num: {
      type: String,
    },
  },
  data() {
    return {
      bidTypeList: [],
      paymentModeList: [],
      paidModeList: [],
      paidDirectionList: [],
      paidTypeList: [],
      currencyList: [],
      selectedKeys: ["OtherFrom"],
      componentName: "OtherFrom",
      contendVendorType: this.$store.state.account.contendVendorType,
      showData: false,
      data1: ["1", "2"],
      contendVendorId: "",
      disableds: true,
      form: {
        oddNumber: "", // 竞价单号
        purchasingOrganization: "", // 采购组织
        companyCode: "", //公司代码
        title: "", // 竞价单标题contendId
        paidType: "", // 竞价单类型
        paidDirection: "", //	竞价方向
        paidMode: "", // 竞价方式
        paymentMode: "", // 付款方式
        currency: "", // 币种
        exchangeRate: "", // 汇率
        offerStartTime: null, // 报价开始时间
        offerEndTime: null, // 报价截止时间
        bidType: "", // 标的类型
      },
    };
  },
  created() {
    queryDictionarys("paidType").then((res) => {
      this.paidTypeList = res;
    });
    queryDictionarys("paidDirection").then((res) => {
      this.paidDirectionList = res;
    });
    queryDictionarys("paidMode").then((res) => {
      this.paidModeList = res;
    });
    queryDictionarys("paymentMode").then((res) => {
      this.paymentModeList = res;
    });
    queryDictionarys("currency").then((res) => {
      this.currencyList = res;
    });
    queryDictionarys("signType").then((res) => {
      this.bidTypeList = res;
    });
    this.form = this.$store.state.account.contendVendorData;
    this.contendVendorId = this.form.id;
    if (this.form.offerStartTime !== 0 && this.form.offerStartTime !== "") {
      this.form.offerStartTime = this.$moment(
        Number(this.form.offerStartTime)
      ).format("YYYY-MM-DD");
    }
    if (this.form.offerEndTime !== 0 && this.form.offerEndTime !== "") {
      this.form.offerEndTime = this.$moment(
        Number(this.form.offerEndTime)
      ).format("YYYY-MM-DD");
    }
   if (
      this.contendVendorType !== '已发布'
    ) {
      this.componentName = "Material";
      this.selectedKeys = ["Material"];
    } else  {
      this.componentName = "MyBidding";
      this.selectedKeys = ["MyBidding"];
    }
  },
    computed: {
    ...mapGetters("account", ["user"]),
  },
  mounted() {
    this.showData = this.contendVendorType === 1;
  },
  methods: {
                // 修改竞价单状态
    joinStateChange(data, state) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "是否确定此操作吗？",
        centered: true,
        onOk() {
          paidVendorStatusPutRequest({
            paidSheetId: data.id,
            vendorCode: that.user.code,
            vendorStatus: state,
            deleted: 0,
          }).then((response) => {
            if (response.code === 0) {
              that.$message.success("操作成功");
              that.back()
            } else {
              that.$message.error(response.message);
            }
          });
        },
        onCancel() {},
      });
    },
    onOpenChange(openKeys) {
      this.componentName = openKeys.key;
    },
    cancel(event) {
      event.stopPropagation();
    },
    back() {
      this.$router.push({
        path: "/portal/offer",
      });
    },
    onChange(e) {
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
