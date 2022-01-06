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
              label="招标事项"
              :labelCol="{ span: 3 }"
              :wrapperCol="{ span: 20 }"
            >
              <a-input
                :disabled="disableds"
                placeholder="请输入竞价单标题"
                v-model="form.title"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="purchasingOrganization"
              class="form-item-style"
              label="采购组织"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input
                v-model="form.purchasingOrganization"
                :disabled="disableds"
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
              <a-input v-model="form.companyCode" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="sheetType"
              class="form-item-style"
              label="招标单类型"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.sheetType"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in sheetTypeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="round"
              class="form-item-style"
              label="轮次"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.round" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="versions"
              class="form-item-style"
              label="版本"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.versions" :disabled="disableds" />
            </a-form-model-item>
          </a-col> -->
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="tenderType"
              class="form-item-style"
              label="投标类型"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.tenderType"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in tenderTypeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="tenderMode"
              class="form-item-style"
              label="招标方式"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.tenderMode"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in tenderModeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>

          <!--   <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              class="form-item-style"
              label="招标类型"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select placeholder="请选择">
                <a-select-option value="1">在线投标</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
            <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              class="form-item-style"
              label="开标地点"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input/>
            </a-form-model-item>
          </a-col> -->

          <!--   <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="applicationDeadline"
              class="form-item-style"
              label="申请截止时间"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-date-picker
                format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                v-model="form.applicationDeadline"
                :show-time="{ defaultValue: $moment('00:00:00', 'HH:mm:ss') }"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
            <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="prequalificationEndTime"
              class="form-item-style"
              label="资格预审截止时间"
              :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
            >
              <a-date-picker
                format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                v-model="form.prequalificationEndTime"
                :disabled="disableds"
                :show-time="{ defaultValue: $moment('00:00:00', 'HH:mm:ss') }"
              />
            </a-form-model-item>
          </a-col> -->
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="bidEndTime"
              class="form-item-style"
              label="投标截止时间"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-date-picker v-model="form.bidEndTime" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="openBidTime"
              class="form-item-style"
              label="开标时间"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-date-picker v-model="form.openBidTime" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="openBidPlace"
              class="form-item-style"
              label="开标地点"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-input v-model="form.openBidPlace" :disabled="disableds" />
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
              <a-select
                v-model="form.signType"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in signTypeList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="preQualification"
              class="form-item-style"
              label="资格预审"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-checkbox v-model="form.preQualification" :disabled="disableds">
              </a-checkbox>
            </a-form-model-item>
          </a-col> -->
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
        <a-menu-item
          key="Qualification"
          v-if="this.biddingVendorType === '已参与'"
        >
          资格预审
        </a-menu-item>
        <a-menu-item key="Message" v-if="this.biddingVendorType === '待投标'">
          投标信息
        </a-menu-item>
        <a-menu-item key="OtherFrom"> 其他信息 </a-menu-item>
        <a-menu-item key="Material" v-if="this.biddingVendorType !== 'append'">
          物料和附件
        </a-menu-item>
      </a-menu>
      <a-row class="menu-content">
        <transition mode="out-in">
          <component
            :is="componentName"
            :biddingVendorId="biddingVendorId"
            :disabled="disableds"
          ></component>
        </transition>
      </a-row>
    </a-card>
    <a-row>
      <a-col :span="24" class="btn-sty" style="margin-top: 20px">
        <a-button
          @click="joinStateChanges(form, '已参与')"
          type="primary"
          v-if="
            this.biddingVendorType === '待参与' &&
            this.form.preQualification === 1
          "
        >
          参与
        </a-button>
        <a-button
          @click="joinStateChanges(form, '待投标')"
          type="primary"
          v-if="
            this.biddingVendorType === '待参与' &&
            this.form.preQualification === 0
          "
        >
          参与
        </a-button>
        <a-button
          @click="joinStateChanges(form, '已放弃')"
          type="primary"
          v-if="this.biddingVendorType === '待参与'"
        >
          放弃
        </a-button>

        <a-button
          type="primary"
          @click="joinStateChange()"
          v-if="this.biddingVendorType === '已参与'"
        >
          提交
        </a-button>
        <a-button
          @click="submitClick()"
          type="primary"
          v-if="this.biddingVendorType === '待投标'"
        >
          提交
        </a-button>
        <!-- <a-button type="primary" v-if="this.biddingVendorType === '3'">
          放弃投标
        </a-button> -->
        <a-button @click="back()"> 返回 </a-button>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import Message from "./message.vue";
import { mapGetters } from "vuex";
import OtherFrom from "./otherFrom.vue";
import Material from "./material.vue";
import Qualification from "./qualification.vue";
import { queryDictionarys } from "@/utils/methods";
import {
  bidVendorStatusPutRequest,
  bidVendorMaterialSubmitPostRequest,
  bidVendorGetRequest,
} from "@/services/source/bidding";

export default {
  name: "Addfind",
  components: {
    Qualification,
    Material,
    OtherFrom,
    Message,
  },
  data() {
    return {
      form: {
        bidSheetNum: null, // 招标单号
        purchasingOrganization: null, // 采购组织
        companyCode: null, // 公司代码
        sheetType: undefined, // 招标单类型
        round: null, // 轮次
        versions: null, // 版本
        tenderType: undefined, // 投标类型
        tenderMode: undefined, // 招标方式
        preQualification: false, // 资格预审
        applicationDeadline: null, // 申请截止时间
        prequalificationEndTime: null, // 资格预审截止时间
        bidEndTime: null, // 投标截止时间
        openBidTime: null, // 开标时间
        openBidPlace: null, // 开标地点
        signType: undefined, // 标的类型
      },
      biddingVendorId: "",
      sheetTypeList: [],
      signTypeList: [],
      tenderTypeList: [],
      tenderModeList: [],
      disableds: true,
      selectedKeys: ["OtherFrom"],
      componentName: "OtherFrom",
      biddingVendorType: this.$store.state.account.biddingVendorType,
      data1: ["1", "2"],
      activeKey: [],
      id: "",
      queryId: "",
    };
  },
  mounted() {
    this.getQueryId();
    queryDictionarys("bidStatus").then((res) => {
      this.sheetTypeList = res;
    });
    queryDictionarys("signType").then((res) => {
      this.signTypeList = res;
    });
    queryDictionarys("tenderType").then((res) => {
      this.tenderTypeList = res;
    });
    queryDictionarys("tenderMode").then((res) => {
      this.tenderModeList = res;
    });
  },
  created() {
    if (this.biddingVendorType === "已参与") {
      this.componentName = "Qualification";
      this.selectedKeys = ["Qualification"];
    } else if (this.biddingVendorType === "待投标") {
      this.componentName = "Message";
      this.selectedKeys = ["Message"];
    }
    this.form = this.$store.state.account.biddingVendorData;
    this.biddingVendorId = this.form.id;

    if (
      this.form.applicationDeadline !== 0 &&
      this.form.applicationDeadline !== ""
    ) {
      this.form.applicationDeadline = this.$moment(
        Number(this.form.applicationDeadline)
      ).format("YYYY-MM-DD");
    }
    if (
      this.form.prequalificationEndTime !== 0 &&
      this.form.prequalificationEndTime !== ""
    ) {
      this.form.prequalificationEndTime = this.$moment(
        Number(this.form.prequalificationEndTime)
      ).format("YYYY-MM-DD");
    }
    if (this.form.bidEndTime !== 0 && this.form.bidEndTime !== "") {
      this.form.bidEndTime = this.$moment(Number(this.form.bidEndTime)).format(
        "YYYY-MM-DD"
      );
    }

    if (this.form.openBidTime !== 0 && this.form.openBidTime !== "") {
      this.form.openBidTime = this.$moment(
        Number(this.form.openBidTime)
      ).format("YYYY-MM-DD");
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  methods: {
    // 提交审批
    submitClick() {
      bidVendorMaterialSubmitPostRequest({
        bidSheetId: this.form.id,
        bidVendorId: this.queryId,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("提交成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 供应商id查询
    getQueryId() {
      bidVendorGetRequest({
        bidSheetId: this.form.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.queryId = response.data.records[0].id;
      });
    },
    // 修改招标单状态
    joinStateChange() {
      bidVendorStatusPutRequest({
        bidSheetId: this.form.id,
        vendorCode: this.user.userName,
        vendorStatus: "预审审批",
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("操作成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 修改招标单状态
    joinStateChanges(data, state) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "是否确定此操作吗？",
        centered: true,
        onOk() {
          bidVendorStatusPutRequest({
            bidSheetId: data.id,
            userId: that.user.id,
            vendorStatus: state,
            deleted: 0,
          }).then((response) => {
            if (response.code === 0) {
              that.$message.success("操作成功");
              that.back();
              // that.biddingVendorType === state
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
      this.selectedKeys = [openKeys.key];
    },
    cancel(event) {
      event.stopPropagation();
    },
    back() {
      this.$router.push({
        path: "/portal/supply",
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
