<template>
  <div>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="物品">
        <a-table :columns="columns" :data-source="data">
          <template slot="demandedDate" slot-scope="text">
            <span>
              {{ $moment(Number(text)).format("YYYY-MM-DD") }}
            </span>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="3" tab="附件" force-render>
        <a-table :columns="columns2" :data-source="data2">
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
import {
  bidItemsGetRequest,
  bidAttachmentGetRequest,
} from "@/services/source/bidding";
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
    title: "是否含税",
    dataIndex: "taxInclusive",
  }
];
const columns2 = [
   {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "附件类型",
    dataIndex: "attachmentType",
  },
  {
    title: "附件名称",
    dataIndex: "attachmentName",
  },
  {
    title: "上传人",
    dataIndex: "createdUser",
  },
  {
    title: "上传时间",
    dataIndex: "createdTime",
  },
  {
    title: "操作",
    dataIndex: "operation",
    key: "operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
];
export default {
  name: "material",
  props: {
    biddingVendorId: {
      type: [String, null],
    },
  },
  data() {
    return {
      formItems: {
        inventoryOrganization: "", // 库存组织
        materialCode: "", // 物品编码
        materialDescription: "", // 物品描述
        referToBrand: "", // 参考品牌
        materialClassify: "", // 物品分类
        demandedDate: "", // 需求日期
        demandedQuantity: "", // 	需求数量
        unit: "", // 单位
        taxInclusive: false, // 	含税
      },
      rules: {
        inventoryOrganization: [
          {
            required: true,
            message: "库存组织不能为空",
            trigger: "blur",
          },
        ],
        materialCode: [
          {
            required: true,
            message: "物品编码不能为空",
            trigger: "blur",
          },
        ],
        demandedQuantity: [
          {
            required: true,
            message: "需求数量不能为空",
            trigger: "change",
          },
        ],
        quantity: [
          {
            required: true,
            message: "询价单类型不能为空",
            trigger: "blur",
          },
        ],
        unit: [
          {
            required: true,
            message: "单位不能为空",
            trigger: "blur",
          },
        ],
      },

      formVendor: {
        bidderCode: "",
        bidderName: "",
        bidderDescription: "",
        bidderStatus: "",
        linkman: "",
        phone: "",
      },
      rulesVendor: {
        bidderCode: [
          {
            required: true,
            message: "投标方编码不能为空",
            trigger: "blur",
          },
        ],
      },
      visible2: false,
      visible1: false,
      visible: false,
      data: [],
      columns,
      data2: [],
      columns2,
      id: this.biddingVendorId,
      biddingVendorType: this.$store.state.account.biddingVendorType,
      disableds: true,
      showBut: true,
    };
  },
  created() {
    if(this.id){
      this.getAll();
    }
  },
  methods: {
    getAll() {
      bidItemsGetRequest({
        bidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
      });
      bidAttachmentGetRequest({
        bidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data2 = response.data.records;
      });
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
