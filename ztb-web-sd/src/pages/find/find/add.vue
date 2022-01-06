<template>
  <div>
    <!-- <div :class="advanced ? null: 'fold'"> -->
    <a-card
      v-if="findtype !== 'add' && findtype !== 0"
      style="margin: 0 0 20px 0; padding: 10px 0 0 0"
    >
      <a-row style="margin: 0 0 12px 0">
        <a-col style="text-align: center" :span="24"
          >{{ form.oddNumber }}</a-col
        >
      </a-row>
      <a-steps :current="findNumber">
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
      >
        <a-col :sm="24" :md="24" :xl="24">
          <a-form-model-item
            prop="title"
            class="form-item-style"
            label="询价单描述"
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 20 }"
          >
            <a-input
              :disabled="disableds"
              placeholder="请输入询价单描述"
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
              @change="changePurchasingOrganization"
              style="width: 100%"
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
            <!-- <a-input
              placeholder="请输入公司代码"
              v-model="form.companyCode"
              :disabled="disableds"
            /> -->
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
            prop="inquirySheetType"
            class="form-item-style"
            label="单据类型"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-select
              v-model="form.inquirySheetType"
              placeholder="请选择询价单类型"
              :disabled="disableds"
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
              placeholder="请选择询价方式"
              :disabled="disableds"
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
            prop="quotationDeadline"
            class="form-item-style"
            label="报价截止时间"
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 12 }"
          >
            <a-date-picker
              style="width: 100%"
              v-model="form.quotationDeadline"
              format="YYYY-MM-DD HH:mm:ss"
              :disabled="disableds"
              :show-time="{ defaultValue: $moment('00:00:00', 'HH:mm:ss') }"
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
            <a-input
              placeholder="请输入汇率"
              v-model="form.exchangeRate"
              :disabled="disableds"
            />
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
    <a-card class="card-top">
      <a-menu
        mode="inline"
        style="width: 156px; float: left; margin-bottom: 10px"
        :default-selected-keys="selectedKeys"
        :selectedKeys="selectedKeys"
        @click="onOpenChange"
      >
        <!-- <a-menu-item key="Audit" v-if="this.findtype === '待审核'">
          审批
        </a-menu-item> -->
        <a-menu-item key="Negotiate" v-if="findtype === '已发布'">
          报价
        </a-menu-item>
        <a-menu-item key="Trial" v-if="findtype === '待初审'">
          初审
        </a-menu-item>
        <a-menu-item key="PurchaseAudit" v-if="findtype === '待核价'">
          核价
        </a-menu-item>
        <a-menu-item key="PriceApproval" v-if="findtype === '待审批'||findtype === '已归档'">
          核价
        </a-menu-item>
        <a-menu-item key="OtherFrom"> 其他 </a-menu-item>
        <a-menu-item key="Items" v-if="findId"> 物品 </a-menu-item>
        <a-menu-item key="Offer" v-if="findId"> 供应商 </a-menu-item>
        <a-menu-item key="File" v-if="findId"> 附件 </a-menu-item>
        <a-menu-item key="WerkFlow" v-if="findId"> 流转记录 </a-menu-item>
      </a-menu>
      <a-row class="menu-content">
        <transition mode="out-in">
          <component
            ref="refChild"
            :is="componentName"
            :findId="findId"
            :requestId="form.requestId"
          ></component>
        </transition>
      </a-row>
    </a-card>
    <a-row>
      <a-col :span="24" class="btn-sty" style="margin-top: 20px">
        <template v-if="findtype === 'add'">
          <a-button v-has="['inquiry:add']" type="primary" @click="addFind"> 新建 </a-button>
          <a-button
          v-has="['inquiry:edit']"
            type="primary"
            @click="saveFind"
            v-if="!permissionsEdits && findtype !== 'add'"
          >
            保存
          </a-button>
        </template>

        <template v-if="findtype === '待审核'">
          <a-button v-has="['inquiry:add']" type="primary" @click="approvalBy('提交')"> 提交 </a-button>
          <a-button
            v-has="['inquiry:approve']"
            type="primary"
            v-if="requestRelationType"
            @click="auditBy('审批')"
          >
            审批
          </a-button>
        </template>
        <template v-if="findtype === '待发布'">
          <a-button v-has="['inquiry:fb']" type="primary" @click="approvalBy('发布')">
            询价单发布
          </a-button>
        </template>
        <template v-if="findtype === '已发布'">
          <a-button v-has="['inquiry:tjhj']" type="primary" @click="counterofferBy()">
            提交还价
          </a-button>
        </template>
        <template v-if="findtype === '已发布'">
          <a-button v-has="['inquiry:tjsh']" type="primary" @click="approvalBy('审核')">
            提交审核
          </a-button>
        </template>
        <template v-if="findtype === '待初审'">
          <a-button v-has="['inquiry:jgcstj']" type="primary" @click="approvalBy('初审')">
            价格初审提交
          </a-button>
        </template>
        <template v-if="findtype === '待核价'">
          <!-- <a-button type="primary" v-if="this.findtype === '8'"> 转交 </a-button> -->
          <a-button v-has="['inquiry:cgtj']" type="primary" @click="approvalBy('核价')">
            采购提交</a-button
          >
        </template>
        <template v-if="findtype === '待审批'">
          <a-button v-has="['inquiry:hjsptg']" type="primary" @click="approvalBy('审批')">
            核价审批通过
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
          <!-- <a-button @click="expertsClose">拒绝</a-button> -->
        </a-space>
      </a-row>
    </a-modal>
  </div>
</template>
<script>
import Offer from "./offer.vue";
import Items from "./items.vue";
import File from "./file.vue";
import OtherFrom from "./otherFrom.vue";
import Audit from "./audit.vue";
import Negotiate from "./negotiate.vue";
import Trial from "./trial.vue";
import PurchaseAudit from "./purchaseAudit.vue";
import PriceApproval from "./priceApproval.vue";
import WerkFlow from "./werkFlow.vue";
import {
  inquirySheetPutRequest,
  inquirySheetSubmitPostRequest,
  inquirySheetAgreePostRequest,
  inquiryVendorMaterialDickerPutRequest,
  inquirySheetshowByRequestIdGetRequest,
  requestapprovePostRequest,
} from "@/services/source/find";
import {
  queryDictionarys,
  treeorganization,
  permissionsEdit,
} from "@/utils/methods";
import { mapGetters } from "vuex";
import { getPurchaseOrzTree } from "@/services/structure/index";
import { getSysCompanyTree } from "@/services/structure/index";

export default {
  name: "addFind",
  components: {
    Offer,
    Items,
    File,
    Audit,
    OtherFrom,
    Negotiate,
    Trial,
    PurchaseAudit,
    PriceApproval,
    WerkFlow,
  },
  data() {
    return {
      permissionsEdits: false,
      visible: false,
      auditForm: {
        description: "",
      },
      form: {
        oddNumber: null, //询价单号
        purchasingOrganization: null, // 采购组织
        purchasingOrganizationId: null,
        companyCode: null, // 公司代码
        title: null, // 询价单标题
        inquirySheetType: undefined, // 询价单类型
        status: 0, //状态
        inquiryMode: undefined, // 询价方式
        paymentMode: undefined, //付款方式
        exchangeRate: null, //汇率
        quotationDeadline: null, //报价截止时间
        signType: undefined, //标的类型
        approvalProcess: false,
      },
      rules: {
        purchasingOrganizationid: [
          {
            required: true,
            message: "采购组织名称不能为空",
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
        quotationDeadline: [
          {
            required: true,
            message: "报价截止时间不能为空",
            trigger: "change",
          },
        ],
      },
      requestRelationType: false,
      inquirySheetTypeList: [],
      inquiryModelList: [],
      paymentModeList: [],
      signTypeList: [],
      typeList: [],
      TreeList: [],
      CompanyList: [],
      findId: null,
      findNumber: 0,
      selectedKeys: ["OtherFrom"],
      disableds: true,
      componentName: "OtherFrom",
      findtype: this.$store.state.account.findType,
      activeKey: [],
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
  watch: {
    findtype(newVal, oldVal) {
      this.findNumber = this.typeList.indexOf(newVal);
    },
  },
  activated() {},
  created() {
    if (this.findtype === "add") {
      this.findId = null;
    } else {
      if (this.findtype === "已发布") {
        this.componentName = "Negotiate";
        this.selectedKeys = ["Negotiate"];
      } else if (this.findtype === "待初审") {
        this.componentName = "Trial";
        this.selectedKeys = ["Trial"];
      } else if (this.findtype === "待核价") {
        this.componentName = "PurchaseAudit";
        this.selectedKeys = ["PurchaseAudit"];
      } else if (this.findtype === "待审批"|| this.findtype === "已归档") {
        this.componentName = "PriceApproval";
        this.selectedKeys = ["PriceApproval"];
      }
      this.form = this.$store.state.account.findData;
      this.typeList = this.form.statusNames;
      this.findNumber = this.typeList.indexOf(this.findtype);
      this.findId = this.form.id;
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
      if (
        this.form.quotationDeadline !== 0 &&
        this.form.quotationDeadline !== ""
      ) {
        this.form.quotationDeadline = this.$moment(
          Number(this.form.quotationDeadline)
        ).format("YYYY-MM-DD");
      }
    }
  },
  mounted() {
    this.getTreeList();
    this.getCompanyList();
    if (this.findtype === "add" || this.findtype === 0) {
      this.$refs.ruleForm.resetFields();
      this.disableds = false;
    } else {
      this.permissionsEdits = !permissionsEdit("bid:edit");
      this.disableds = true;
    }
    // 请求所有下拉
    queryDictionarys("inquirySheetType").then((res) => {
      this.inquirySheetTypeList = res;
    });
    queryDictionarys("inquiryModel").then((res) => {
      this.inquiryModelList = res;
    });
    queryDictionarys("paymentMode").then((res) => {
      this.paymentModeList = res;
    });
    queryDictionarys("signType").then((res) => {
      this.signTypeList = res;
    });
  },
  methods: {
    //请求数据状态
    getAllFindType(id) {
      inquirySheetshowByRequestIdGetRequest(id).then((response) => {
        if (response.data) {
        }
      });
    },
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
    // 审批通过
    approvalBy(state) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要" + state + "通过吗？",
        centered: true,
        onOk() {
          inquirySheetAgreePostRequest({
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

    // 提交还价导供应商
    counterofferBy() {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要提交吗？",
        centered: true,
        onOk() {
          inquiryVendorMaterialDickerPutRequest({
            inquirySheetId: that.findId,
          }).then((response) => {
            if (response.code === 0) {
              that.$message.success("提交成功");
              that.back();
            } else {
              that.$message.error(response.message);
            }
          });
        },
        onCancel() {},
      });
    },

    // 绑定采购组织名称
    changePurchasingOrganization(value, label, extra) {
      this.form.purchasingOrganization = label[0];
    },
    changeCompanyList(value, label, extra) {
      this.form.companyCode = label[0];
    },
    // 添加
    addFind() {
      let flag = this.$refs["refChild"].validateForm();
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          if (flag) {
            let childFrom = { ...this.form, ...this.$refs.refChild.form };
            childFrom.quotationDeadline = Date.parse(
              childFrom.quotationDeadline
            );
            inquirySheetSubmitPostRequest(
              {
                ...childFrom,
                userId: this.user.id,
                name: this.$store.state.account.user.name,
                typeId: "1424566482236416001",
                ruleId: "3", //单号规则ID
              },
              this.user.userName
            ).then((response) => {
              if (response.code === 0) {
                this.findId = response.data.id;
                this.$message.success("添加成功");
                this.findtype = "edit";
                this.form = response.data;
                this.form.approvalProcess === 0 ? false : true;
                if (
                  this.form.quotationDeadline !== 0 &&
                  this.form.quotationDeadline !== ""
                ) {
                  this.form.quotationDeadline = this.$moment(
                    Number(this.form.quotationDeadline)
                  ).format("YYYY-MM-DD");
                }
                this.typeList = this.form.statusNames;
                this.findNumber = this.typeList.indexOf(this.findtype);
                this.$store.state.account.findData = response.data;
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
    // 修改
    saveFind() {
      let childFrom = { ...this.form, ...this.$refs.refChild.form };
      inquirySheetPutRequest({
        ...childFrom,
        deleted: 0,
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
    // 切换菜单
    onOpenChange(openKeys) {
      this.componentName = openKeys.key;
      this.selectedKeys = [openKeys.key];
    },
    back() {
      this.$router.push({
        path: "/findmanage/find",
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
