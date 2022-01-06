<template>
  <div>
    <div class="operator">
      <!-- <a-button @click="addNew" style="margin-bottom: 10px" type="primary"
            >添加</a-button
          > -->
    </div>
    <a-table :columns="columns" :data-source="data">
      <template slot="operation" slot-scope="text, record">
        <span @click="addNew(record.id)" class="operation-style"> 查看 </span>
      </template>
    </a-table>
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
            prop="price"
            class="form-item-style"
            label="最新报价(元)"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.price" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-button @click="addContend" style="margin-top: 4px" type="primary">
            出价
          </a-button>
        </a-col>
      </a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="deliveryDate"
            class="form-item-style"
            label="承诺交货期"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-date-picker v-model="form.deliveryDate" />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="8">
          <a-row class="timeStyle">
            <a-col :span="6">当前系统时间：</a-col>
            <a-col :span="17">2021-05-20 11：11：11</a-col>
          </a-row>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="deliveryCycle"
            class="form-item-style"
            label="供货周期"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.deliveryCycle" />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="offeringPeriodFrom"
            class="form-item-style"
            label="报价有效期从"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-date-picker v-model="form.offeringPeriodFrom" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="offeringPeriodTo"
            class="form-item-style"
            label="报价有效期至"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-date-picker v-model="form.offeringPeriodTo" />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="minSupplyQuantity"
            class="form-item-style"
            label="最小供货量"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.minSupplyQuantity" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            prop="unit"
            class="form-item-style"
            label="单位"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.unit" />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="16">
          <a-form-model-item
            prop="offeringClarification"
            class="form-item-style"
            label="报价说明"
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 20 }"
          >
            <a-textarea
              v-model="form.offeringClarification"
              :auto-size="{ minRows: 2, maxRows: 6 }"
            />
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
    <div class="content-title">
      竞价排名
      <a-button
        slot="operation"
        @click="getAll()"
        style="margin-left: 20px"
        type="primary"
      >
        刷新
      </a-button>
    </div>
    <a-table :columns="columns1" :data-source="data1">
      <template slot="createdTime" slot-scope="text">
        <div>
          {{ $moment(Number(text)).format("YYYY-MM-DD") }}
        </div>
      </template>
    </a-table>
    <a-modal v-model="visible" width="1500px" :footer="null" :centered="true" title="历史竞价">
      <a-table :columns="columns2" :data-source="data2">
        <template
          v-for="cols in [
            'deliveryDate',
            'offeringPeriodFrom',
            'offeringPeriodTo',
            'createdTime',
          ]"
          :slot="cols"
          slot-scope="text"
        >
          <div :key="cols">
            {{ $moment(Number(text)).format("YYYY-MM-DD") }}
          </div>
        </template>
      </a-table>
    </a-modal>
  </div>
</template>
<script>
import {
  paidVendorMaterialQueryRankingGetRequest,
  paidVendorMaterialGetRequest,
  paidVendorMaterialOfferingPostRequest,
  paidVendorGetRequest,
} from "@/services/source/contend";
import { mapGetters } from "vuex";
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "库存组织",
    align: "center",
    dataIndex: "inventoryOrganization",
  },
  {
    title: "物品编码",
    align: "center",
    dataIndex: "materialCode",
  },
  {
    title: "物品描述",
    align: "center",
    dataIndex: "materialDescription",
  },
  {
    title: "参考品牌",
    align: "center",
    dataIndex: "referToBrand",
  },
  {
    title: "物品分类",
    align: "center",
    dataIndex: "materialClassify",
  },
  {
    title: "需求日期",
    align: "center",
    dataIndex: "demandedDate",
  },
  {
    title: "需求数量",
    align: "center",
    dataIndex: "demandedQuantity",
  },
  {
    title: "计量单位",
    align: "center",
    dataIndex: "unit",
  },
  {
    title: "竞价人数",
    align: "center",
    dataIndex: "paidNumber",
  },
  {
    title: "当前排名",
    align: "center",
    dataIndex: "currentRanking",
  },
  {
    title: "操作",
    dataIndex: "operation",
    align: "center",
    key: "operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
];
const columns1 = [
  {
    title: "序号",
    key: "index",
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "报价(元)",
    align: "center",
    dataIndex: "price",
  },
  {
    title: "竞价人",
    align: "center",
    dataIndex: "createdUser",
  },
  {
    title: "竞价时间",
    align: "center",
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
];
const columns2 = [
  {
    title: "序号",
    key: "index",
    width: 80,
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "报价时间",
    align: "center",
    width: 170,
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
  {
    title: "报价人",
    align: "center",
    width: 140,
    dataIndex: "createdUser",
  },
  {
    title: "单价(元)",
    align: "center",
    width: 140,
    dataIndex: "price",
  },
  {
    title: "报价有效期从",
    align: "center",
    width: 170,
    dataIndex: "offeringPeriodFrom",
    scopedSlots: { customRender: "offeringPeriodFrom" },
  },
  {
    title: "报价有效期至",
    align: "center",
    width: 170,
    dataIndex: "offeringPeriodTo",
    scopedSlots: { customRender: "offeringPeriodTo" },
  },
  {
    title: "承诺交货期",
    align: "center",
    width: 170,
    dataIndex: "deliveryDate",
    scopedSlots: { customRender: "deliveryDate" },
  },
  {
    title: "供应周期",
    align: "center",
    width: 100,
    dataIndex: "deliveryCycle",
  },
  {
    title: "报价说明",
    align: "center",
    width: 100,
    dataIndex: "offeringClarification",
  },
  {
    title: "最小供货数量",
    width: 170,
    align: "center",
    dataIndex: "minSupplyQuantity",
  },
  {
    title: "单位",
    align: "center",
    width: 100,
    dataIndex: "unit",
  },
];
export default {
  name: "material",
  props: {
    contendVendorId: {
      type: [String, null],
    },
  },
  data() {
    return {
      form: {
        price: "", // 报价
        deliveryDate: "", // 承诺交付日期
        deliveryCycle: "", // 供货周期
        offeringPeriodFrom: "", // 报价有效期从
        offeringPeriodTo: "", // 报价有效期至
        minSupplyQuantity: "", // 最小供货数量
        unit: "", // 计量单位
        offeringClarification: "", // 报价说明
      },
      rules: {
        price: [
          {
            required: true,
            message: "请填写报价",
            trigger: "blur",
          },
        ],
      },
      id: this.contendVendorId,
      visible: false,
      data: [],
      columns,
      data1: [],
      columns1,
      data2: [],
      columns2,
    };
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  mounted() {
    if (this.id) {
      this.getQueryId();
    }
  },
  methods: {
    getAll() {
      this.data = [];
      paidVendorMaterialQueryRankingGetRequest({
        paidVendorId: this.queryId,
        sheetId: this.id,
        offeringType: "正式",
      }).then((response) => {
        this.data.push(response.data);
        this.data1 = this.data[0].paidVendorMaterialVOS;
        if (this.data1.length !== 0) {
          this.form = this.data1[0];

          if (this.form.deliveryDate !== 0 && this.form.deliveryDate !== "") {
            this.form.deliveryDate = this.$moment(
              Number(this.form.deliveryDate)
            ).format("YYYY-MM-DD");
          }
          if (
            this.form.offeringPeriodFrom !== 0 &&
            this.form.offeringPeriodFrom !== ""
          ) {
            this.form.offeringPeriodFrom = this.$moment(
              Number(this.form.offeringPeriodFrom)
            ).format("YYYY-MM-DD");
          }

          if (
            this.form.offeringPeriodTo !== 0 &&
            this.form.offeringPeriodTo !== ""
          ) {
            this.form.offeringPeriodTo = this.$moment(
              Number(this.form.offeringPeriodTo)
            ).format("YYYY-MM-DD");
          }
        }
      });
    },
    // 招标商id查询
    getQueryId() {
      paidVendorGetRequest({
        paidSheetId: this.id,
        vendorCode: this.user.code,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.queryId = response.data.records[0].id;
        this.getAll();
      });
    },
    // 报价
    addContend() {
      // if (isNaNTime(this.form.deliveryDate)) {
      this.form.deliveryDate = Date.parse(this.form.deliveryDate);
      this.form.offeringPeriodFrom = Date.parse(this.form.offeringPeriodFrom);
      this.form.offeringPeriodTo = Date.parse(this.form.offeringPeriodTo);
      // }
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          paidVendorMaterialOfferingPostRequest({
            materialId: this.data[0].id,
            paidVendorId: this.queryId,
            sheetId: this.id,
            offeringType: "正式",
            price: this.form.price,
            deliveryDate: this.form.deliveryDate,
            deliveryCycle: this.form.deliveryCycle,
            offeringPeriodFrom: this.form.offeringPeriodFrom,
            offeringPeriodTo: this.form.offeringPeriodTo,
            minSupplyQuantity: this.form.minSupplyQuantity,
            unit: this.form.unit,
            offeringClarification: this.form.offeringClarification,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加成功");
              this.getAll();
            } else {
              this.$message.error(response.message);
            }
          });
        }
      });
    },
    addNew(id) {
      paidVendorMaterialGetRequest({
        materialId: id,
        currentPage: 1,
      }).then((response) => {
        this.data2 = response.data.records;
      });
      this.visible = true;
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
.timeStyle {
  color: #000;
  line-height: 40px;
  :nth-child(1) {
    text-align: right;
  }
}
</style>
