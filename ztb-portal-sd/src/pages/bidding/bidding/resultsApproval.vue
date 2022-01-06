<template>
  <div>
    <!-- <a-row class="btn-sty">
      <a-button type="primary" @click="save()"> 保存 </a-button>
      <a-button type="primary" @click="edit()"> 编辑 </a-button>
    </a-row> -->

    <a-row>
      <a-table
        :columns="columns"
        rowKey="id"
        :data-source="data"
        :customRow="rowClick"
        :rowClassName="setRowClassName"
        bordered
      >
        <template slot="demandedDate" slot-scope="text">
          <div >
             {{ $moment(Number(text)).format("YYYY-MM-DD") }}
          </div>
        </template>
      </a-table>
    </a-row>
    <!--  -->
    <a-button type="primary" @click="save()" style="margin-bottom: 10px" v-if="biddingType === '待决标'"> 保存 </a-button>
    <a-row>
      <a-table rowKey="id" :columns="columns1" :data-source="data1" bordered>
         <template slot="totalScore"  slot-scope="text, record">
          <div v-if="record.bidVendorVO">
             {{ record.bidVendorVO.totalScore }}
          </div>
        </template>
                <template slot="allottedQuantity" slot-scope="text, record">
          <a-input v-model="record.allottedQuantity"
            @change="
                (e) => quantityChange(e.target, record.id, 'allottedQuantity')
              "
           />
        </template>
         </a-table>
    </a-row>
  </div>
</template>
<script>
import {
  bidMaterialWithPricingGetRequest,
  bidVendorMaterialNewPricePostRequest,
  bidVendorMaterialBatchPutRequest
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
    width: 120,
    dataIndex: "inventoryOrganization",
  },
  {
    title: "物品编码",
    width: 150,
    dataIndex: "materialCode",
  },
  {
    title: "物品描述",
    width: 100,
    dataIndex: "materialDescription",
  },
  {
    title: "需求日期",
    width: 100,
    dataIndex: "demandedDate",
        scopedSlots: { customRender: "demandedDate" },
  },
  {
    title: "需求数量",
    width: 100,
    dataIndex: "demandedQuantity",
  },
    {
    title: "成本单价",
    width: 100,
    dataIndex: "costPrice",
  },
    {
    title: "成本合价",
    width: 100,
    dataIndex: "costTotalPrice",
  },
  {
    title: "计量单位",
    width: 100,
    dataIndex: "unit",
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
    title: "投标方编码",
    dataIndex: "vendorCode",
  },
  {
    title: "投标方名称",
    dataIndex: "vendorName",
  },
  {
    title: "总分",
    dataIndex: "totalScore",
        scopedSlots: { customRender: "totalScore" },
  },
  {
    title: "投标数量",
    dataIndex: "bidQuantity",
  },
  {
    title: "单价(元)",
    dataIndex: "price",
  },
    {
    title: "总价(元)",
    dataIndex: "totalPrice",
  },
  {
    title: "中标数量",
    dataIndex: "allottedQuantity",
    width: 100,
    scopedSlots: { customRender: "allottedQuantity" },
  },
  {
    title: "中标比例",
    dataIndex: "allottedProp",
    scopedSlots: { customRender: "allottedProp" },
  },
];

export default {
  name: "ResulisApproval",
    props: {
    biddingId: {
      type: [String, null],
    },
  },
  data() {
    return {
         biddingType: this.$store.state.account.biddingType,
      id: this.biddingId,
      data: [],
      columns,
      data1: [],
      columns1,
      editingKey: "",
      visible: false,
    };
  },
  created() {
    this.getAll();
  },
  methods: {
    getAll() {
      bidMaterialWithPricingGetRequest({
        bidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data;
      });
    },
    // 点击行事件
    rowClick(record) {
      return {
        on: {
          click: (e) => {
            let oldList = document.querySelectorAll(".checked-td-of-add-table");
            if (oldList) {
              for (let j = 0; j < oldList.length; j++) {
                oldList[j].classList.remove("checked-td-of-add-table");
              }
            }
            let children = e.target.parentNode.children;
            for (let i = 0; i < children.length; i++) {
              children[i].classList.add("checked-td-of-add-table");
            }
            this.indexColims = record.id;
            this.getSupplierItem(record.id);
            this.editingKey = "";
          },
        },
      };
    },
    // 点击行家颜色
    setRowClassName(record) {
      if (record.id === this.indexColims) {
        return "checked-td-of-add-table";
      } else {
        return "";
      }
    },
    // 请求所有报价
    getSupplierItem(id) {
      this.data1 = [];
      bidVendorMaterialNewPricePostRequest({
        materialId: id, // 物品Id
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        if (response.data !== null) {
          response.data.map((item) => {
            let data = {};
            data.vendorCode = item.bidVendorVO.vendorCode;
            data.vendorName = item.bidVendorVO.vendorName;
            return Object.assign(item, data);
          });
          this.data1 = response.data;
        } else {
          this.data1 = [];
        }
      });
      
    },
        // 比例计算
    quantityChange(data, key, column){
      let newData = [...this.data1];
      let target = newData.filter((item) => key === item.id)[0];
      if (target) {
        target[column] = data.value;
           target['allottedProp'] = Number(data.value /target.materialQuantity*100).toFixed(2) + '%';
           target['totalPrice'] = Number(data.value * target.price);
      }
    },
    
    // 批量保存分配
    save() {
      bidVendorMaterialBatchPutRequest(
            this.data1
          ).then((response) => {
            if (response.code === 0) {
              this.$message.success("分配成功");
              this.getAll();
            } else {
              this.$message.error(response.message);
            }
          });
    },
    back() {
      this.$router.push({
        path: "/findmanage/find",
      });
      this.$store.state.account.findType = num;
    },
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  margin-bottom: 20px;
  button {
    margin-left: 10px;
  }
}
/deep/.ant-table {
  min-height: 220px !important;
}
/deep/ .checked-td-of-add-table {
  cursor: pointer;
  background-color: #e6f7ff;
}
</style>
