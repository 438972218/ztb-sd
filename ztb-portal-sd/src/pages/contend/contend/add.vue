<template>
  <div>
    <!-- <div :class="advanced ? null: 'fold'"> -->
    <a-card
      v-if="this.contendType !== 'add' && this.contendType !== 0"
      style="margin: 0 0 20px 0; padding: 10px 0 0 0"
    >
      <a-row style="margin: 0 0 12px 0">
        <a-col style="text-align: center" :span="24"
          >{{ form.requestTitle }}({{ form.oddNumber }})</a-col
        >
      </a-row>
      <a-steps :current="contendNumber">
        <span slot="progressDot" slot-scope="{ index, status, prefixCls }">
          <span :class="`${prefixCls}-icon-dot`" />
        </span>
        <a-step
          v-for="(item, index) in typeList"
          :key="index"
          :description="item"
        />
      </a-steps>
    </a-card>
    <a-card class="card-top">
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :model="form"
        :rules="rules"
        class="page-form"
      >
        <a-col :sm="24" :md="24" :xl="24">
          <a-form-model-item
            prop="title"
            class="form-item-style"
            label="竞价单描述"
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
            prop="purchasingOrganizationId"
            class="form-item-style"
            label="采购组织"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-tree-select
              :disabled="disableds"
              v-model="form.purchasingOrganizationId"
              style="width: 100%"
              @change="changePurchasingOrganization"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :tree-data="TreeList"
              tree-default-expand-all
            >
              <span
                v-if="key === '0-0-1'"
                slot="title"
                slot-scope="{ key, value }"
                style="color: #08c"
              >
                {{ value }}
              </span>
            </a-tree-select>
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
            <a-tree-select
              :disabled="disableds"
              v-model="form.companyCode"
              @change="changeCompanyList"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :tree-data="CompanyList"
              tree-default-expand-all
            >
              <span
                v-if="key === '0-0-1'"
                slot="title"
                slot-scope="{ key, value }"
                style="color: #08c"
              >
                {{ value }}
              </span>
            </a-tree-select>
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
              placeholder="请选择单据类型"
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
              placeholder="请选择竞价方向"
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
              placeholder="请选择竞价方式"
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
              placeholder="请选择付款方式"
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
            prop="exchangeRate"
            class="form-item-style"
            label="汇率"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-input
              placeholder="请输入汇率"
              v-model="form.exchangeRate"
              :disabled="disableds"
            />
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
              format="YYYY-MM-DD HH:mm:ss"
              :show-time="{ defaultValue: $moment('00:00:00', 'HH:mm:ss') }"
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
              format="YYYY-MM-DD HH:mm:ss"
              :show-time="{ defaultValue: $moment('00:00:00', 'HH:mm:ss') }"
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
              placeholder="请选择标的类型"
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
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="approvalProcess"
            class="form-item-style"
            label="需要审批"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-checkbox v-model="form.approvalProcess" :disabled="disableds" />
          </a-form-model-item>
        </a-col>
      </a-form-model>
    </a-card>
    <!-- <a-card class="card-top" v-if="contendType === 3">
  
          <find-audit></find-audit>
      </a-card> -->
    <a-card class="card-top">
      <a-menu
        mode="inline"
        style="width: 156px; float: left; margin-bottom: 10px"
        :default-selected-keys="selectedKeys"
        @click="onOpenChange"
      >
        <!-- <a-menu-item key="Audit" v-if="this.contendType === '待审核'">
          审批信息
        </a-menu-item> -->
        <a-menu-item key="Detail" v-if="this.contendType === '已发布'">
          中标结果
        </a-menu-item>
        <a-menu-item key="Detail" v-if="this.contendType === '待初审'">
          中标结果
        </a-menu-item>
        <a-menu-item key="Winning" v-if="this.contendType === '待核价'">
          核价
        </a-menu-item>
        <a-menu-item
          key="Pricing"
          v-if="this.contendType === '待审批' || this.contendType === '已归档'"
        >
          竞价明细
        </a-menu-item>
        <a-menu-item key="OtherFrom"> 其他信息 </a-menu-item>
        <a-menu-item key="Material" v-if="contendId"> 物品明细 </a-menu-item>
        <a-menu-item key="Supply" v-if="contendId"> 供应商列表 </a-menu-item>
        <a-menu-item key="File" v-if="contendId"> 附件信息 </a-menu-item>
        <a-menu-item key="WerkFlow" v-if="this.contendId">
          流转记录
        </a-menu-item>
      </a-menu>
      <a-row class="menu-content">
        <transition mode="out-in">
          <component
            :is="componentName"
            ref="refChild"
            :contendId="contendId"
            :requestId="form.requestId"
          ></component>
        </transition>
      </a-row>
    </a-card>
    <a-row>
      <a-col :span="24" class="btn-sty" style="margin-top: 20px">
        <template v-if="contendType === 'add'">
          <a-button v-has="['paid:add']" type="primary" @click="addContend">
            新建
          </a-button>
        </template>
        <template v-if="!permissionsEdits">
          <a-button v-has="['paid:edit']" type="primary" @click="saveContend">
            保存
          </a-button>
        </template>
        <template v-if="contendType === '待发布'">
          <a-button
            v-has="['paid:fb']"
            type="primary"
            @click="approvalBy('发布')"
          >
            竞价发布
          </a-button>
        </template>
        <template v-if="contendType === '已发布'">
          <a-button
            v-has="['paid:tjcg']"
            type="primary"
            @click="approvalBy('提交')"
          >
            提交采购
          </a-button>
        </template>
        <template v-if="contendType === '待核价'">
          <a-button
            v-has="['paid:cgqr']"
            type="primary"
            @click="approvalBy('提交')"
          >
            采购确认
          </a-button>
        </template>
        <template v-if="contendType === '待初审'">
          <a-button
            v-has="['paid:dcs']"
            type="primary"
            @click="approvalBy('通过')"
          >
            确认
          </a-button>
        </template>
        <template v-if="contendType === '待审批'">
          <a-button
            v-has="['paid:dsp']"
            type="primary"
            @click="approvalBy('通过')"
          >
            确认
          </a-button>
        </template>
        <template v-if="this.contendType === '待审核'">
          <a-button
            type="primary"
            @click="approvalBy('通过')"
            v-has="['paid:approve']"
          >
            提交
          </a-button>
          <a-button
            v-has="['paid:approve']"
            v-if="requestRelationType"
            type="primary"
            @click="auditBy('审批')"
          >
            审批
          </a-button>
        </template>

        <a-button @click="back()"> 返回 </a-button>
      </a-col>
    </a-row>
    <a-modal
      :footer="null"
      v-model="visible"
      width="600px"
      :centered="true"
      title="审批"
    >
      <a-row>
        <a-col>
          <a-form-model-item
            prop="description"
            class="form-item-style"
            label="审批意见"
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 20 }"
          >
            <a-textarea
              v-model="auditForm.description"
              :auto-size="{ minRows: 1, maxRows: 3 }"
          /></a-form-model-item>
        </a-col>
      </a-row>
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="addexperts()">通过</a-button>
          <a-button @click="expertsClose">拒绝</a-button>
        </a-space>
      </a-row>
    </a-modal>
  </div>
</template>
<script>
import File from "./file.vue";
import Supply from "./supply.vue";
import Material from "./material.vue";
import OtherFrom from "./otherFrom.vue";
import Audit from "./audit.vue";
import Winning from "./winning.vue";
import Detail from "./detail.vue";
import Pricing from "./pricing.vue";
import WerkFlow from "./werkFlow.vue";
import {
  paidSheetPutRequest,
  paidSheetSubmitPostRequest,
  paidSheetAgreePostRequest,
} from "@/services/source/contend";
import {
  queryDictionarys,
  treeorganization,
  permissionsEdit,
} from "@/utils/methods";
import { getPurchaseOrzTree } from "@/services/structure/index";
import { mapGetters } from "vuex";
import { requestapprovePostRequest } from "@/services/source/find";
import { getSysCompanyTree } from "@/services/structure/index";

export default {
  name: "Addfind",
  components: {
    File,
    Supply,
    Material,
    OtherFrom,
    Audit,
    Winning,
    Detail,
    Pricing,
    WerkFlow,
  },
  data() {
    return {
      requestRelationType: false,
      contendNumber: 0,
      auditForm: {
        description: "",
      },
      visible: false,
      bidTypeList: [],
      paymentModeList: [],
      paidModeList: [],
      paidDirectionList: [],
      paidTypeList: [],
      currencyList: [],
      TreeList: [],
      CompanyList: [],
      contendId: "",
      disableds: false,
      permissionsEdits: true,
      form: {
        oddNumber: "", // 竞价单号
        purchasingOrganization: "", // 采购组织
        purchasingOrganizationId: "",
        companyCode: "", //公司代码
        title: "", // 竞价单标题contendId
        paidType: undefined, // 竞价单类型
        paidDirection: undefined, //	竞价方向
        paidMode: undefined, // 竞价方式
        paymentMode: undefined, // 付款方式
        currency: undefined, // 币种
        exchangeRate: "", // 汇率
        offerStartTime: null, // 报价开始时间
        offerEndTime: null, // 报价截止时间
        bidType: undefined, // 标的类型
        approvalProcess: false,
      },
      rules: {
        purchasingOrganizationId: [
          {
            required: true,
            message: "采购组织不能为空",
            trigger: "change",
          },
        ],
        companyCode: [
          {
            required: true,
            message: "采购组织名称不能为空",
            trigger: "change",
          },
        ],
        signType: [
          {
            required: true,
            message: "标的类型不能为空",
            trigger: "blur",
          },
        ],
        paidType: [
          {
            required: true,
            message: "单价类型不能为空",
            trigger: "blur",
          },
        ],
        paidDirection: [
          {
            required: true,
            message: "竞价方向不能为空",
            trigger: "blur",
          },
        ],
        paidMode: [
          {
            required: true,
            message: "竞价方式不能为空",
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
        offerStartTime: [
          {
            required: true,
            message: "报价开始时间不能为空",
            trigger: "change",
          },
        ],
        offerEndTime: [
          {
            required: true,
            message: "报价截止时间不能为空",
            trigger: "change",
          },
        ],
        bidType: [
          {
            required: true,
            message: "标的类型不能为空",
            trigger: "change",
          },
        ],
        exchangeRate: [
          {
            required: true,
            message: "汇率不能为空",
            trigger: "blur",
          },
        ],
      },
      selectedKeys: ["OtherFrom"],
      componentName: "OtherFrom",
      contendType: this.$store.state.account.contendType,
      id: "",
    };
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  watch: {
    contendType(newVal, oldVal) {
      this.contendNumber = this.typeList.indexOf(newVal);
    },
  },
  created() {
    if (this.contendType !== "add" && this.contendType !== 0) {
      if (this.contendType === "已发布" || this.contendType === "待初审") {
        this.componentName = "Detail";
        this.selectedKeys = ["Detail"];
      } else if (this.contendType === "待核价") {
        this.componentName = "Winning";
        this.selectedKeys = ["Winning"];
      } else if (
        this.contendType === "待审批" ||
        this.contendType === "已归档"
      ) {
        this.componentName = "Pricing";
        this.selectedKeys = ["Pricing"];
      }
      this.form = this.$store.state.account.contendData;
      this.typeList = this.form.statusNames;
      this.contendNumber = this.typeList.indexOf(this.contendType);
      const {
        requestRelationId,
        requestRelationToRoleId,
        requestRelationToUserId,
      } = this.form;

      if (
        requestRelationId !== 0 &&
        (this.user.roleIds.indexOf(requestRelationToRoleId) !== -1 ||
          requestRelationToUserId === this.user.id)
      ) {
        this.requestRelationType = true;
      } else {
        this.requestRelationType = false;
      }
      this.form.approvalProcess =
        this.form.approvalProcess === 0 ? false : true;
      this.contendId = this.form.id;
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
    }
  },
  mounted() {
    this.getTreeList();
    this.getCompanyList();
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
    if (this.contendType === "add" || this.contendType === 0) {
      this.disableds = false;
      this.$refs.ruleForm.resetFields();
    } else {
      this.permissionsEdits = !permissionsEdit("bid:edit");
      this.disableds = true;
    }

    // this.disableds =
    //   this.contendType === "edit"
    //     ? false
    //     : this.contendType === "add"
    //     ? false
    //     : true;
  },

  methods: {
    // 采购组织树查询
    getTreeList() {
      getPurchaseOrzTree({
        currentPage: "-1",
        deleted: 0,
      }).then((response) => {
        let list = JSON.parse(JSON.stringify(response.data));
        treeorganization(list);
        this.TreeList = list;
      });
    },
    // 公司树查询
    getCompanyList() {
      getSysCompanyTree({
        currentPage: "-1",
        deleted: 0,
      }).then((response) => {
        let list = JSON.parse(JSON.stringify(response.data));
        treeorganization(list);
        this.CompanyList = list;
      });
    },
    // 审批流程
    approvalBy(state) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要" + state + "通过吗？",
        centered: true,
        onOk() {
          paidSheetAgreePostRequest({
            userId: that.user.id,
            id: that.form.id,
            requestId: that.form.requestId,
          }).then((response) => {
            if (response.code === 0) {
              that.$message.success(state + "成功");
              that.back();
            } else {
              this.$message.error(response.message);
            }
          });
        },
        onCancel() {},
      });
    },

    // 完成审批
    auditBy() {
      this.auditForm.description = "";
      this.visible = true;
    },
    // 审批通过
    addexperts() {
      requestapprovePostRequest({
        userId: this.user.id,
        description: this.auditForm.description,
        requestId: this.form.requestRelationId,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("提交成功");
          this.visible = false;
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 审批拒绝
    expertsClose() {
      this.visible = false;
    },
    // approvalBy() {
    //   paidSheetAgreePostRequest({
    //     id: this.form.id,
    //     userId: this.user.id,
    //     requestId: this.form.requestId,
    //   }).then((response) => {
    //     if (response.code === 0) {
    //       this.$message.success("提交成功");
    //       this.back();
    //     } else {
    //       this.$message.error(response.message);
    //     }
    //   });
    // },
    //  发布
    releaseBy() {
      inquirySheetAgreePostRequest({
        id: this.form.id,
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
    // 创建
    addContend() {
      let flag = this.$refs["refChild"].validateForm();
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          if (flag) {
            let childFrom = { ...this.form, ...this.$refs.refChild.form };
            childFrom.offerEndTime = Date.parse(childFrom.offerEndTime);
            childFrom.offerStartTime = Date.parse(childFrom.offerStartTime);
            paidSheetSubmitPostRequest({
              ...childFrom,
              userId: this.user.id,
              ruleId: "3", //单号规则ID
              typeId: "1424566510971592705",
              name: this.user.name,
            }).then((response) => {
              if (response.code === 0) {
                this.$message.success("添加成功");
                this.$store.state.account.contendData = response.data;
                this.$store.state.account.contendType = "edit";
                this.contendType = "edit";
                this.form = response.data;
                this.typeList = this.form.statusNames;
                this.contendNumber = this.typeList.indexOf(this.contendType);
                this.form.approvalProcess =
                  this.form.approvalProcess === 0 ? false : true;
                this.contendId = this.form.id;
                if (
                  this.form.offerStartTime !== 0 &&
                  this.form.offerStartTime !== ""
                ) {
                  this.form.offerStartTime = this.$moment(
                    Number(this.form.offerStartTime)
                  ).format("YYYY-MM-DD");
                }
                if (
                  this.form.offerEndTime !== 0 &&
                  this.form.offerEndTime !== ""
                ) {
                  this.form.offerEndTime = this.$moment(
                    Number(this.form.offerEndTime)
                  ).format("YYYY-MM-DD");
                }
                // this.dataClose();
                // this.back();
              } else {
                this.$message.error(response.message);
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    // 绑定采购组织名称
    changePurchasingOrganization(value, label, extra) {
      this.form.purchasingOrganization = label[0];
    },
    changeCompanyList(value, label, extra) {
      this.form.companyCode = label[0];
    },
    // 修改
    saveContend() {
      let childFrom = { ...this.form, ...this.$refs.refChild.form };
      paidSheetPutRequest({
        ...childFrom,
        deleted: 0,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("修改成功");
          // this.dataClose();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 单号规则查询
    oddRuleGet() {
      oddRuleGetRequest({
        currentPage: "1",
      }).then((response) => {
        this.oddRuleList = response.data.records;
      });
    },
    // 流程ID 查询
    processGet() {
      processGetRequest({
        currentPage: "1",
      }).then((response) => {
        this.processList = response.data.records;
      });
    },
    onOpenChange(openKeys) {
      this.componentName = openKeys.key;
    },
    back() {
      this.$router.push({
        path: "/findmanage/contend",
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
