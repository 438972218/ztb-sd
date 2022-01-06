<template>
  <div>
    <a-card
      v-if="biddingType !== 'add' && biddingType !== 0"
      style="margin: 0 0 10px 0; padding: 4px 0 0 0"
    >
      <a-row style="margin: 0 0 12px 0">
        <a-col style="text-align: center" :span="24"
          >{{ form.requestTitle }}({{ form.oddNumber }})</a-col
        >
      </a-row>
      <a-steps :current="biddingNumber">
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
        :rules="rules"
        :model="form"
        class="page-form"
      >
        <a-row>
          <a-col :sm="24" :md="24" :xl="24">
            <a-form-model-item
              prop="title"
              class="form-item-style"
              label="招标事项"
              :labelCol="{ span: 3 }"
              :wrapperCol="{ span: 20 }"
            >
              <a-input :disabled="disableds" v-model="form.title" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
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
              prop="sheetType"
              class="form-item-style"
              label="单据类型"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-select
                v-model="form.sheetType"
                :disabled="disableds"
                placeholder="请选择招标单类型"
              >
                <a-select-option
                  v-for="item in bidStatusList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
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
                placeholder="请选择投标类型"
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
                placeholder="请选择招标方式"
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
                placeholder="请选择标的类型"
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
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="bidEndTime"
              class="form-item-style"
              label="投标截止时间"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-date-picker
                format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                v-model="form.bidEndTime"
                :disabled="disableds"
                :show-time="{ defaultValue: $moment('00:00:00', 'HH:mm:ss') }"
              />
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
              <a-date-picker
                format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                v-model="form.openBidTime"
                :disabled="disableds"
                :show-time="{ defaultValue: $moment('00:00:00', 'HH:mm:ss') }"
              />
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
              <a-input
                placeholder="请输入开标地点"
                v-model="form.openBidPlace"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row> 
            <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="applicationDeadline"
              class="form-item-style"
              label="申请截止时间"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-date-picker
                style="width: 100%"
                v-model="form.applicationDeadline"
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
                style="width: 100%"
                v-model="form.prequalificationEndTime"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
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
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="approvalProcess"
              class="form-item-style"
              label="需要审批"
              :labelCol="{ span: 9 }"
              :wrapperCol="{ span: 12 }"
            >
              <a-checkbox
                v-model="form.approvalProcess"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card class="card-top">
      <a-menu
        mode="inline"
        style="width: 156px; float: left; margin-bottom: 10px"
        :default-selected-keys="selectedKeys"
        @click="onOpenChange"
      >
        <template v-if="biddingType === '待预审'">
          <a-menu-item key="Review" v-has="['bid:dsq']"> 资格预审 </a-menu-item>
        </template>
        <template v-if="biddingType === '招标中'">
          <a-menu-item key="Open" v-has="['bid:zbz']"> 投标 </a-menu-item>
        </template>
        <template v-if="biddingType === '待开标'">
          <a-menu-item key="Open" v-has="['bid:xfzjpf']"> 投标 </a-menu-item>
        </template>
        <template v-if="biddingType === '待专家评分'">
          <a-menu-item key="ExpertScore"> 评分 </a-menu-item>
        </template>
        <template v-if="biddingType === '待专家组长确认'">
          <a-menu-item key="ExpertScore"> 评分 </a-menu-item>
        </template>
        <template v-if="biddingType === '待决标' || biddingType === '已归档'">
          <a-menu-item key="ResultsApproval" v-has="['bid:djb']">
            决标
          </a-menu-item>
        </template>
        <!-- <a-menu-item key="Results">
          中标结果
        </a-menu-item> 
        <a-menu-item key="Audit"> 审批 </a-menu-item>
        <a-menu-item key="Review"> 资格预审 </a-menu-item>
        <a-menu-item key="Open"> 投标 </a-menu-item>
        <a-menu-item key="ExpertScore"> 评分 </a-menu-item>
        <a-menu-item key="ResultsApproval"> 决标 </a-menu-item>
         <a-menu-item
          key="Appraise"
          v-if="
            this.biddingType === '9' ||
            this.biddingType === '8' ||
            this.biddingType === '10'
          "
        >
          投标信息
        </a-menu-item> -->
        <a-menu-item key="OtherFrom"> 其他 </a-menu-item>
        <a-menu-item key="Material" v-if="biddingId"> 物料/投标方 </a-menu-item>
        <a-menu-item key="Qualification" v-if="biddingId">
          资格预审项
        </a-menu-item>
        <a-menu-item key="Experts" v-if="biddingId"> 专家/评分 </a-menu-item>
        <a-menu-item key="WerkFlow" v-if="biddingId"> 流转记录 </a-menu-item>
      </a-menu>
      <a-row class="menu-content">
        <transition mode="out-in">
          <component
            :is="componentName"
            ref="refChild"
            :biddingId="biddingId"
            :requestId="form.requestId"
          ></component>
        </transition>
      </a-row>
    </a-card>
    <a-row>
      <a-col :span="24" class="btn-sty" style="margin-top: 20px">
        <template v-if="biddingType === 'add'">
          <a-button v-has="['bid:add']" type="primary" @click="addBidding">
            新建
          </a-button>
        </template>
        <template>
          <a-button
            v-if="!permissionsEdits"
            type="primary"
            @click="saveBidding"
          >
            保存
          </a-button>
        </template>

        <template v-if="biddingType === '待发布'">
          <a-button v-has="['bid:dfb']" type="primary" @click="releaseBy()">
            发布
          </a-button>
        </template>
        <template v-if="biddingType === '待预审'">
          <a-button
            v-has="['bid:dsq']"
            type="primary"
            @click="approvalBy('提交')"
          >
            提交
          </a-button>
        </template>
        <template v-if="biddingType === '招标中'">
          <a-button
            v-has="['bid:zbz']"
            type="primary"
            @click="approvalBy('提交')"
          >
            提交
          </a-button>
        </template>
        <template v-if="biddingType === '待决标'">
          <a-button
            v-has="['bid:djb']"
            type="primary"
            @click="approvalBy('提交')"
          >
            提交
          </a-button>
        </template>
        <template v-if="biddingType === '待专家评分'">
          <a-button
            v-has="['bid:zjpf']"
            type="primary"
            @click="approvalBy('提交')"
          >
            专家评分提交
          </a-button>
        </template>
        <template v-if="biddingType === '待专家组长确认'">
          <a-button
            v-has="['bid:zzpf']"
            type="primary"
            @click="approvalBy('提交')"
          >
            组长评分提交
          </a-button>
        </template>
        <template v-if="biddingType === '待开标'">
          <a-button
            v-has="['bid:xfzjpf']"
            type="primary"
            @click="scoreBy('专家评分')"
          >
            下发专家评分
          </a-button>
        </template>
        <template v-if="biddingType === '待开标'">
          <a-button
            v-has="['bid:xfzzpf']"
            type="primary"
            @click="scoreBy('组长评分')"
          >
            下发组长评分
          </a-button>
        </template>
        <template v-if="biddingType === '待审核'">
                   <a-button
            v-has="['bid:approve']"
            v-if="requestRelationType"
            type="primary"
            @click="auditBy('审批')"
          >
            审批
          </a-button>
          <a-button
            type="primary"
            @click="approvalBy('提交')"
            v-has="['bid:approve']"
          >
            提交
          </a-button>
 
        </template>
        <!-- <a-button type="primary" v-if="this.biddingType === '9'">
          退回至初审</a-button
        > -->
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
          <!-- <a-button @click="expertsClose">拒绝</a-button> -->
        </a-space>
      </a-row>
    </a-modal>
  </div>
</template>
<script>
import ResultsApproval from "./resultsApproval.vue";
import Results from "./results.vue";
import Assessment from "./assessment.vue";
import Appraise from "./appraise.vue";
import Open from "./open.vue";
import OtherFrom from "./otherFrom.vue";
import Experts from "./experts.vue";
import Qualification from "./qualification.vue";
import Material from "./material.vue";
import Audit from "./audit.vue";
import Review from "./review.vue";
import ExpertScore from "./expertScore.vue";
import WerkFlow from "./werkFlow.vue";
import { mapGetters } from "vuex";
import {
  bidInvitationPutRequest,
  bidSheetSubmitPostRequest,
  bidSheetAgreePostRequest,
  bidSheetshowByRequestIdGetRequest,
} from "@/services/source/bidding";
import {
  queryDictionarys,
  treeorganization,
  permissionsEdit,
} from "@/utils/methods";
import { getPurchaseOrzTree } from "@/services/structure/index";
import { requestapprovePostRequest } from "@/services/source/find";
import { getSysCompanyTree } from "@/services/structure/index";

export default {
  name: "Addfind",
  components: {
    Experts,
    Qualification,
    Material,
    OtherFrom,
    Audit,
    Review,
    Open,
    Appraise,
    Assessment,
    Results,
    ResultsApproval,
    ExpertScore,
    WerkFlow,
  },
  props: {
    num: {
      type: String,
    },
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  data() {
    return {
      auditForm: {
        description: "",
      },
      visible: false,
      form: {
        requestTitle: "",
        title: null,
        oddNumber: null, // 招标单号
        purchasingOrganization: undefined, // 采购组织
        purchasingOrganizationId: undefined, // 采购组织
        companyCode: undefined, // 公司代码
        round: null, // 轮次
        versions: null, // 版本
        tenderType: undefined, // 投标类型
        sheetType: undefined, //  单据类型
        tenderMode: undefined, // 招标方式
        preQualification: false, // 资格预审
        bidEndTime: null, // 投标截止时间
        openBidTime: null, // 开标时间
        openBidPlace: null, // 开标地点
        signType: undefined, // 标的类型
        approvalProcess: false, //审批流程
        applicationDeadline: null,
        prequalificationEndTime :null,
      },

      rules: {
        // purchasingOrganizationId: [
        //   {
        //     required: true,
        //     message: "采购组织名称不能为空",
        //     trigger: "change",
        //   },
        // ],
            companyCode: [
          {
            required: true,
            message: "采购组织名称不能为空",
            trigger: "change",
          },
        ],
        tenderType: [
          {
            required: true,
            message: "招标单类型不能为空",
            trigger: "change",
          },
        ],
        sheetType: [
          {
            required: true,
            message: "单据类型不能为空",
            trigger: "change",
          },
        ],
        round: [
          {
            required: true,
            message: "轮次不能为空",
            trigger: "blur",
          },
        ],
        signType: [
          {
            required: true,
            message: "标的类型不能为空",
            trigger: "blur",
          },
        ],
        versions: [
          {
            required: true,
            message: "版本不能为空",
            trigger: "blur",
          },
        ],
        tenderType: [
          {
            required: true,
            message: "投标类型不能为空",
            trigger: "change",
          },
        ],
        tenderMode: [
          {
            required: true,
            message: "投标类型不能为空",
            trigger: "change",
          },
        ],
        bidEndTime: [
          {
            required: true,
            message: "投标截止时间不能为空",
            trigger: "change",
          },
        ],
        openBidTime: [
          {
            required: true,
            message: "开标时间不能为空",
            trigger: "change",
          },
        ],
        openBidPlace: [
          {
            required: true,
            message: "开标地点不能为空",
            trigger: "blur",
          },
        ],
        signType: [
          {
            required: true,
            message: "标的类型不能为空",
            trigger: "change",
          },
        ],
      },
      requestRelationType: false,
      biddingId: null,
      tenderTypeList: [],
      signTypeList: [],
      bidStatusList: [],
      tenderModeList: {},
      typeList: [],
      TreeList: [],
      CompanyList: [],
      disableds: true,
      permissionsEdits: true,
      selectedKeys: ["OtherFrom"],
      componentName: "OtherFrom",
      biddingType: this.$store.state.account.biddingType,
      data1: ["1", "2"],
      activeKey: [],
      id: "",
      biddingNumber: 0,
    };
  },
  created() {
    if (this.biddingType !== "add" && this.biddingType !== 0) {
      if (this.biddingType === "append") {
        this.componentName = "Results";
        this.selectedKeys = ["Results"];
      } else if (this.biddingType === "4") {
        this.componentName = "Assessment";
        this.selectedKeys = ["Assessment"];
      } else if (this.biddingType === "待预审") {
        this.componentName = "Review";
        this.selectedKeys = ["Review"];
      } else if (
        this.biddingType === "待专家评分" ||
        this.biddingType === "待专家组长确认"
      ) {
        this.componentName = "ExpertScore";
        this.selectedKeys = ["ExpertScore"];
      } else if (
        this.biddingType === "招标中" ||
        this.biddingType === "待开标"
      ) {
        this.componentName = "Open";
        this.selectedKeys = ["Open"];
      } else if (this.biddingType === "待决标") {
        this.componentName = "ResultsApproval";
        this.selectedKeys = ["ResultsApproval"];
      } else if (this.biddingType === "已归档") {
        this.componentName = "ResultsApproval";
        this.selectedKeys = ["ResultsApproval"];
      }
      this.form = this.$store.state.account.biddingData;
      this.biddingId = this.form.id;
      this.typeList = this.form.statusNames;
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
      this.biddingNumber = this.typeList.indexOf(this.biddingType);
      this.form.approvalProcess =
        this.form.approvalProcess === 0 ? false : true;
      this.form.preQualification =
        this.form.preQualification === 0 ? false : true;
      if (this.form.bidEndTime !== 0&& this.form.bidEndTime !== "") {
        this.form.bidEndTime = this.$moment(
          Number(this.form.bidEndTime)
        ).format("YYYY-MM-DD HH:mm:ss");
      }
      if (this.form.bidEndTopenBidTimeime !== 0 && this.form.bidEndTopenBidTimeime !== "") {
        this.form.openBidTime = this.$moment(
          Number(this.form.openBidTime)
        ).format("YYYY-MM-DD HH:mm:ss");
      }
        if (this.form.applicationDeadline !== 0 && this.form.applicationDeadline !== "") {
        this.form.applicationDeadline = this.$moment(
          Number(this.form.applicationDeadline)
        ).format("YYYY-MM-DD");
      }
      if (this.form.prequalificationEndTime !== 0 && this.form.prequalificationEndTime !== "") {
        this.form.prequalificationEndTime = this.$moment(
          Number(this.form.prequalificationEndTime)
        ).format("YYYY-MM-DD");
      }
    }
  },

  mounted() {
    this.getTreeList();
     this.getCompanyList()
    // this.getAllBiddingType(this.form.requestId);
    queryDictionarys("bidStatus").then((res) => {
      this.bidStatusList = res;
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
    if (this.biddingType === "add" || this.biddingType === 0) {
      this.$refs.ruleForm.resetFields();
      this.disableds = false;
    } else {
      this.permissionsEdits = !permissionsEdit("bid:edit");
      this.disableds = true;
    }
  },
  watch: {
    biddingType(newVal, oldVal) {
      this.biddingNumber = this.typeList.indexOf(newVal);
    },
  },
  methods: {
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
    // //请求数据状态
    // getAllBiddingType(id) {
    //   bidSheetshowByRequestIdGetRequest(id).then((response) => {
    //     if (response.data) {

    //     }
    //   });
    // },
    // 资格审批
    approvalBy(state) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要" + state + "通过吗？",
        centered: true,
        onOk() {
          bidSheetAgreePostRequest({
            userId: that.user.id,
            id: that.form.id,
            requestId: that.form.requestId,
          }).then((response) => {
            if (response.code === 0) {
              that.$message.success(state + "成功");
              that.back();
            } else {
              that.$message.error(response.message);
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
    scoreBy(type) {
      bidSheetAgreePostRequest({
        userId: this.user.id,
        flowOption: type,
        id: this.form.id,
        requestId: this.form.requestId,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("审批成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    releaseBy() {
      bidSheetAgreePostRequest({
        userId: this.user.id,
        id: this.form.id,
        requestId: this.form.requestId,
        description: this.$refs.refChild.form.description, //单号规则ID
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("发布成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },

    // 添加
    addBidding() {
      let flag = this.$refs["refChild"].validateForm();
      console.log(flag);
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          if (flag) {
            let childFrom = { ...this.form, ...this.$refs.refChild.form };
            // let childFrom = (this.form, this.$refs.refChild.form);
            childFrom.applicationDeadline = Date.parse(
              childFrom.applicationDeadline
            );
            childFrom.prequalificationEndTime = Date.parse(
              childFrom.prequalificationEndTime
            );

            childFrom.bidEndTime = Date.parse(childFrom.bidEndTime);
            childFrom.openBidTime = Date.parse(childFrom.openBidTime);

            childFrom.projectDeliveryDate = Date.parse(
              childFrom.projectDeliveryDate
            );

            bidSheetSubmitPostRequest({
              ...childFrom,
              userId: this.user.id,
              ruleId: "3", //单号规则ID
              name: this.user.name,
              typeId: "1424531417540403201",
            }).then((response) => {
              if (response.code === 0) {
                this.biddingId = response.data.id;
                this.$message.success("添加成功");
                this.biddingType === "待发布";
                this.permissionsEdits = permissionsEdit("bid:edit");
                this.getBidSheetShow(response.data.requestId);
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
    // 新建数据后重新show获取数据
    getBidSheetShow(id) {
      bidSheetshowByRequestIdGetRequest(`${id}`).then((response) => {
        this.$store.state.account.biddingData = response.data;
        this.form = response.data;
        this.form.oddNumber = response.data.requestVO.oddNumber;
        this.form.requestTitle = response.data.requestVO.title;
        this.typeList = response.data.statusNames;
        this.biddingNumber = this.typeList.indexOf(this.biddingType);
        this.form.approvalProcess =
          this.form.approvalProcess === 0 ? false : true;
        this.form.preQualification =
          this.form.preQualification === 0 ? false : true;
        if (
          this.form.projectDeliveryDate !== 0 &&
          this.form.projectDeliveryDate !== ""
        ) {
          this.form.projectDeliveryDate = this.$moment(
            Number(this.form.projectDeliveryDate)
          ).format("YYYY-MM-DD");
        }
        if (
          this.form.prequalificationEndTime !== 0 &&
          this.form.prequalificationEndTime !== ""
        ) {
          this.form.prequalificationEndTime = this.$moment(
            Number(this.form.prequalificationEndTime)
          ).format("YYYY-MM-DD HH:mm:ss");
        }
        if (this.form.bidEndTime !== 0 && this.form.bidEndTime !== "") {
          this.form.bidEndTime = this.$moment(
            Number(this.form.bidEndTime)
          ).format("YYYY-MM-DD HH:mm:ss");
        }
        if (this.form.openBidTime !== 0 && this.form.openBidTime !== "") {
          this.form.openBidTime = this.$moment(
            Number(this.form.openBidTime)
          ).format("YYYY-MM-DD HH:mm:ss");
        }
      });
    },
    // 修改
    saveBidding() {
      let childFrom = Object.assign(this.form, this.$refs.refChild.form);
      console.log(this.form);
      // this.form.approvalProcess = this.form.approvalProcess === false ? 0 : 1;
      // this.form.preQualification = this.form.preQualification === false ? 0 : 1;
      // this.form.offerTax = this.form.offerTax === false ? 0 : 1;
      // this.form.marginCall = this.form.marginCall === false ? 0 : 1;
      // this.form.tenderDocumentFree = this.form.tenderDocumentFree === false ? 0 : 1;
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          bidInvitationPutRequest({
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
        } else {
          return false;
        }
      });
    },
    onOpenChange(openKeys) {
      this.componentName = openKeys.key;
      this.selectedKeys = [openKeys.key];
    },
    back() {
      this.$router.push({
        path: "/findmanage/bidding",
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
/deep/.ant-steps-item-description {
  font-size: 12px;
}
</style>
