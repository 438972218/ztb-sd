<template>
  <div>
    <!-- <a-row class="btn-sty">
      <a-button type="primary" @click="save()"> 保存 </a-button>
      <a-button type="primary" @click="edit()"> 编辑 </a-button>
    </a-row> -->

    <a-row>
      <a-table
        :columns="columns"
        :data-source="data"
        :customRow="rowClick"
        :rowClassName="setRowClassName"
        bordered
      >
        <template slot="vendorCode" slot-scope="text">
          <div>
            {{ text }}
          </div>
        </template>
        <template slot="demandedDate" slot-scope="text">
          <div>
            {{ $moment(Number(text)).format("YYYY-MM-DD HH:mm:ss") }}
          </div>
        </template>
      </a-table>
    </a-row>
       <a-row>
      <a-table
        :columns="columns1"
        :data-source="data1"
        bordered
      >
              <template slot="createdTime" slot-scope="text">
          <div>
            {{ $moment(Number(text)).format("YYYY-MM-DD HH:mm:ss") }}
          </div>
        </template>
                <template slot="deliveryDate" slot-scope="text">
          <div>
            {{ $moment(Number(text)).format("YYYY-MM-DD HH:mm:ss") }}
          </div>
        </template>
      </a-table>
    </a-row>

  </div>
</template>
<script>
import {
  bidItemsGetRequest,
  bidVendorMaterialNewPricePostRequest,
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
    width: 150,
    dataIndex: "demandedDate",
      scopedSlots: { customRender: "demandedDate" },
  },
  {
    title: "需求数量",
    width: 100,
    dataIndex: "demandedQuantity",
  },
  {
    title: "计量单位",
    width: 100,
    dataIndex: "unit",
  },

];
const columns1 = [
  {
    title: "报价次数",
    dataIndex: "offeringNumber",
  },
  {
    title: "投标时间",
    dataIndex: "createdTime",
          scopedSlots: { customRender: "createdTime" },
  },
  {
    title: "投标人",
    dataIndex: "bidder",
  },
  {
    title: "投标数量",
    dataIndex: "bidQuantity",
  },
  {
    title: "计量单位",
    dataIndex: "unit",
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
    title: "承诺交付日期",
    dataIndex: "deliveryDate",
              scopedSlots: { customRender: "deliveryDate" },
  },
  {
    title: "投标方编码",
    dataIndex: "vendorCode",
  },
    {
    title: "投标方名称",
    dataIndex: "vendorName",
  },
];

export default {
  name: "Open",
  props: {
    biddingId: {
      type: [String, null],
    },
  },
  data() {
    return {
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
      bidItemsGetRequest({
        bidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
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
        currentPage: "-1",
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
    addNew() {
      this.visible = true;
    },

    edit() {
      const newData = [...this.data];
      //   const target = newData.filter((item) => key === item.key)[0];
      this.editingKey = 1;
      if (newData) {
        for (let i = 0; i < newData.length; i++) {
          newData[i].editable = true;
        }
      }
      this.data = newData;
    },
    save() {
      const newData = [...this.data];
      //   const newCacheData = [...this.cacheData];
      if (newData) {
        for (let i = 0; i < newData.length; i++) {
          delete newData[i].editable;
        }
      }
      this.data = newData;
      //   if (target && targetCache) {
      //     delete target.editable;
      //     this.data = newData;
      //     Object.assign(targetCache, target);
      //     this.cacheData = newCacheData;
      //   }
      this.editingKey = "";
    },
    cancel(key) {
      const newData = [...this.data];
      const target = newData.filter((item) => key === item.key)[0];
      this.editingKey = "";
      if (target) {
        Object.assign(
          target,
          this.cacheData.filter((item) => key === item.key)[0]
        );
        delete target.editable;
        this.data = newData;
      }
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
