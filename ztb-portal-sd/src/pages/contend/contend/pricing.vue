<template>
  <div>
    <div class="content-title">竞价明细</div>
      <a-row>
      <a-table :columns="columns" :data-source="data">
        <template
          v-for="time in ['demandedDate']"
          :slot="time"
          slot-scope="text"
        >
          <span :key="time">
            {{ $moment(Number(text)).format("YYYY-MM-DD") }}
          </span>
        </template>
      </a-table>
    </a-row>
    <a-row>
      <a-table :columns="columns1" :data-source="data1" :scroll="{ x: 1300 }">
         <template
          v-for="time in [
            'offeringPeriodFrom',
            'offeringPeriodTo',
            'deliveryDate',
          ]"
          :slot="time"
          slot-scope="text"
        >
          <span :key="time">
            {{ $moment(Number(text)).format("YYYY-MM-DD") }}
          </span>
        </template>
                <template slot="paidVendorVO" slot-scope="text, record">
          <span v-if="record.paidVendorVO">
            {{ record.paidVendorVO.vendorName }}
          </span>
        </template>
        <template slot="linkman" slot-scope="text, record">
          <span v-if="record.paidVendorVO">
            {{ record.paidVendorVO.linkman }}
          </span>
        </template>
                <template slot="operation" slot-scope="text, record">
          <span @click="addNew(record)" class="operation-style"> 查看 </span>
        </template>
         </a-table>
    </a-row>
    <a-modal v-model="visible" width="1500px" :footer="null" :centered="true" title="历史竞价">
      <a-table :columns="columns2"
       :data-source="data2">
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
  paidMaterialWithPricingGetRequest,
  paidVendorMaterialAllocatedPostRequest,
  paidVendorMaterialRankingDetailPostRequest 
} from "@/services/source/contend";
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
    dataIndex: "inventoryOrganization",
    width: 150,
  },
  {
    title: "物品编码",
    dataIndex: "materialCode",
  },
  {
    title: "物品描述",
    dataIndex: "materialDescription",
  },
  {
    title: "参考品牌",
    dataIndex: "referToBrand",
  },
  {
    title: "物品分类",
    dataIndex: "materialClassify",
  },
  {
    title: "需求日期",
    dataIndex: "demandedDate",
    scopedSlots: { customRender: "demandedDate" },
  },
  {
    title: "需求数量",
    dataIndex: "demandedQuantity",
  },
  {
    title: "计量单位",
    dataIndex: "unit",
  },
  {
    title: "成本单价(元)",
    dataIndex: "costPrice",
  },
  {
    title: "成本合价(元)",
    dataIndex: "costTotalPrice",
  },
];
const columns1 = [
  {
    title: "排名",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "供应商名称",
    dataIndex: "paidVendorVO",
    width: 120,
    ellipsis: true,
        scopedSlots: { customRender: "paidVendorVO" },
  },
  {
    title: "联系人",
    width: 90,
    dataIndex: "linkman",
           scopedSlots: { customRender: "linkman" },
  },
  {
    title: "单价(元)",
    width: 90,
    dataIndex: "price",
  },
  // {
  //   title: "总价(元)",
  //   width: 90,
  //   dataIndex: "a4",
  // },
  {
    title: "分配数量",
    width: 90,
    dataIndex: "allottedQuantity",
       scopedSlots: { customRender: "allottedQuantity" },
  },
  {
    title: "分配比例",
    width: 110,
    dataIndex: "allottedProp",
           scopedSlots: { customRender: "allottedProp" },
  },
  {
    title: "报价有效期从",
    width: 120,
    dataIndex: "offeringPeriodFrom",
    scopedSlots: { customRender: "offeringPeriodFrom" },
  },
  {
    title: "报价有效期至",
    width: 120,
    dataIndex: "offeringPeriodTo",
    scopedSlots: { customRender: "offeringPeriodTo" },
  },
  {
    title: "承诺交货期",
    width: 120,
    dataIndex: "deliveryDate",
    scopedSlots: { customRender: "deliveryDate" },
  },
  {
    title: "供货周期",
    width: 90,
    dataIndex: "deliveryCycle",
  },
  {
    title: "报价说明",
    width: 90,
    dataIndex: "offeringClarification",
  },
  {
    title: "最小供货量",
    width: 110,
    dataIndex: "minSupplyQuantity",
  },
  {
    title: "单位",
    width: 70,
    dataIndex: "unit",
  },
  {
    title: "竞价历史",
    width: 120,
    dataIndex: "operation",
    scopedSlots: { customRender: "operation" },
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
  name: "detail",
   props: {
    contendId: {
      type: [String, null],
    },
  },
  data() {
    return {
            id: this.contendId,
      contendType: this.$store.state.account.contendType,
      data: [],
      columns,
      data1: [],
      columns1,
         data2: [],
      columns2,
      visible: false,
    };
  },
    mounted() {
    if (this.id) {
      this.getAll();
      // this.getQueryId();
    }
  },
  methods: {
// 请求物品
    getAll() {
      paidMaterialWithPricingGetRequest({
        paidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data;
        if(response.data){
        this.getItemAll(response.data[0].id);
        }
      });
    },
    // 请求物品竞价
    getItemAll(id) {
      paidVendorMaterialAllocatedPostRequest({
        materialId: id,
      }).then((response) => {
        this.data1 = response.data;
      });
    },
         addNew(data){
           paidVendorMaterialRankingDetailPostRequest({
        materialId:data.materialId,
        paidVendorId: data.paidVendorId,
        offeringType: data.offeringType
      }).then((response) => {
        this.visible = true
        this.data2 = response.data;
      });
    },

  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  text-align: center;
  button {
    margin-left: 10px;
  }
}
/deep/.ant-table {
  min-height: 220px !important;
}
</style>
