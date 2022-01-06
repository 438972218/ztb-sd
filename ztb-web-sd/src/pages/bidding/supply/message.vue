<template>
  <div>
    <a-row>
      <a-table
        :columns="columns"
        :data-source="data"
        :customRow="rowClick"
        :rowClassName="setRowClassName"
      >
        <template slot="demandedDate" slot-scope="text">
          <span>
            {{ $moment(Number(text)).format("YYYY-MM-DD") }}
          </span>
        </template>
        <template slot="operation" slot-scope="text, record">
          <span @click="addNew(record.id)" class="operation-style"> 查看 </span>
        </template>
      </a-table>
    </a-row>
    <a-row>
      <a-table
        :scroll="{ x: 1300 }"
        :columns="columns1"
        :data-source="data1"
        bordered
      >
        <template slot="createdTime" slot-scope="text">
          <div v-if="text !== 0">
            {{ $moment(Number(text)).format("YYYY-MM-DD") }}
          </div>
          <div v-else></div>
        </template>
        <template
          v-for="cols in ['deliveryDate']"
          :slot="cols"
          slot-scope="text, record"
        >
          <div :key="cols">
            <a-date-picker
              v-if="record.editable"
              :value="
                text === 0 ? '' : $moment(Number(text)).format('YYYY-MM-DD')
              "
              format="YYYY-MM-DD"
              @change="(e) => pickerChange(e._d, record.offeringNumber, cols)"
            />
            <template v-else>
              <span v-if="text !== 0">
                {{ $moment(Number(text)).format("YYYY-MM-DD") }}
              </span>
              <span v-else></span>
            </template>
          </div>
        </template>
        <template
          v-for="col in ['bidQuantity', 'price']"
          :slot="col"
          slot-scope="text, record"
        >
          <div :key="col">
            <a-input
              v-if="record.editable"
              style="margin: -5px 0"
              :value="text"
              @change="
                (e) => handleChange(e.target.value, record.offeringNumber, col)
              "
            />
            <template v-else>
              {{ text }}
            </template>
          </div>
        </template>
        <template slot="operation" slot-scope="text, record, index">
          <div class="editable-row-operations" v-if="index === 0">
            <span v-if="record.editable">
              <a @click="() => save(record.offeringNumber)">提交</a>
              <a @click="() => cancel(record.offeringNumber)">取消</a>
            </span>
            <span v-else>
              <a
                :disabled="editingKey !== ''"
                @click="() => edit(record.offeringNumber)"
                >报价</a
              >
            </span>
          </div>
        </template>
      </a-table>
    </a-row>
    <a-modal v-model="visible" width="1500px" :footer="null" :centered="true" title="历史查看">
      <a-table :columns="columns1" :data-source="data2">
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
  bidItemsGetRequest,
  bidVendorGetRequest,
  bidVendorMaterialOfferingNumberGetRequest,
  bidVendorMaterialOfferingNumberPostRequest,
} from "@/services/source/bidding";
import { vendorshowByUserIdGetVendor } from "@/services/supplier/index";
import { mapGetters } from "vuex";
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
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
    title: "含税",
    dataIndex: "taxInclusive",
  },
  {
    title: "操作",
    align: "center",
    scopedSlots: { customRender: "operation" },
  },
];
const columns1 = [
  {
    title: "报价次数",
    width: 90,
    dataIndex: "offeringNumber",
  },
  {
    title: "投标时间",
    width: 90,
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
  {
    title: "投标人",
    width: 90,
    dataIndex: "createdUser",
  },
  {
    title: "投标数量",
    width: 150,
    dataIndex: "bidQuantity",
    scopedSlots: { customRender: "bidQuantity" },
  },
  {
    title: "计量单位",
    width: 150,
    dataIndex: "unit",
  },
  {
    title: "单价",
    width: 90,
    dataIndex: "price",
    scopedSlots: { customRender: "price" },
  },
  // {
  //   title: "总价",
  //   width: 160,
  //   dataIndex: "totalPrice",
  // },
  {
    title: "承诺交付日期",
    width: 120,
    dataIndex: "deliveryDate",
    scopedSlots: { customRender: "deliveryDate" },
  },
  {
    title: "操作",
    width: 120,
    align: "center",
    scopedSlots: { customRender: "operation" },
  },
];

export default {
  name: "OfferDetails",
  props: {
    biddingVendorId: {
      type: [String, null],
    },
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  data() {
    return {
      visible: false,
      id: this.biddingVendorId,
      itemId: "",
      show: false,
      biddingVendorType: this.$store.state.account.biddingVendorType,
      data: [],
      columns,
      dataItem: [
        {
          offeringNumber: 1, // 报价次数
          createdTime: 0, // 投标时间
          bidder: "", // 投标人
          bidQuantity: "", // 	投标数量
          unit: "", // 单位
          price: "", // 单价
          // totalPrice: "", // 总价
          deliveryDate: 0, // 承诺交付日期
          editable: false,
        },
      ],
      data1: [],
      data2: [],
      columns1,
      indexColims: null,
      editingKey: "",
    };
  },

  mounted() {
    if (this.id) {
      this.getCode();
    }
  },
  methods: {
    getAll() {
      bidItemsGetRequest({
        bidSheetId: this.id,
        bidVendorId: this.queryId,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
      });
    },
    // 招标商id查询
    getQueryId(code) {
      bidVendorGetRequest({
        bidSheetId: this.id,
        vendorCode: code,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.queryId = response.data.records[0].id;
        this.getAll();
      });
    },
    // 根据userid查询code
    getCode() {
      vendorshowByUserIdGetVendor(this.user.id).then((response) => {
        this.getQueryId(response.data.code);
      });
    },

    // 添加
    addOffer(form) {
      form.deliveryDate = Date.parse(form.deliveryDate);
      bidVendorMaterialOfferingNumberPostRequest({
        bidSheetId: this.id,
        bidVendorId: this.queryId,
        materialId: this.indexColims,
        bidQuantity: form.bidQuantity,
        unit: form.unit,
        price: form.price,
        // totalPrice: form.totalPrice,
        deliveryDate: form.deliveryDate,
        status: "保存",
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("添加成功");
          this.getSupplierItem(this.indexColims);
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 请求供应商物品
    getSupplierItem(id) {
      this.data1 = [];
      bidVendorMaterialOfferingNumberGetRequest({
        materialId: id, // 物品Id
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        if (response.data.records !== null) {
          this.data1.push(response.data.records[0]);
        } else {
          this.data1 = this.dataItem;
        }
      });
    },
    // 点击行事件
    rowClick(record) {
      let that = this;
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
            (this.editingKey = ""), this.getSupplierItem(record.id);
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
    // 新建报价
    handleChange(value, key, column) {
      const newData = [...this.data1];
      const target = newData.filter((item) => key === item.offeringNumber)[0];
      if (target) {
        target[column] = value;
        // target.totalPrice = target.price * target.bidQuantity;
        this.data1 = newData;
      }
    },
    pickerChange(value, key, column) {
      const newData = [...this.data1];
      const target = newData.filter((item) => key === item.offeringNumber)[0];
      if (target) {
        // value = this.$moment(Number(value)).format("YYYY-MM-DD");
        target[column] = value;
        this.data1 = newData;
      }
    },
    edit(key) {
      const newData = [...this.data1];
      const target = newData.filter((item) => key === item.offeringNumber)[0];
      this.editingKey = key;
      if (target) {
        target.editable = true;
        this.data1 = newData;
      }
    },
    save(key) {
      //  bidQuantity: form.bidQuantity,
      // unit: form.unit,
      // price: form.price,
      // deliveryDate: form.deliveryDate,
      const newData = [...this.data1];
      const target = newData.filter((item) => key === item.offeringNumber)[0];
      if (
        target.bidQuantity === "" ||
        target.price === "" ||
        target.deliveryDate === ""
      ) {
        this.$message.error("请填写必填项");
      } else {
        if (target) {
          delete target.editable;
          this.data1 = newData;
        }
        this.addOffer(this.data1[0]);
        this.editingKey = "";
      }
    },
    cancel(key) {
      const newData = [...this.data1];
      const target = newData.filter((item) => key === item.offeringNumber)[0];
      this.editingKey = "";
      if (target) {
        delete target.editable;
        this.getSupplierItem(this.indexColims);
      }
    },
    addNew(id) {
      bidVendorMaterialOfferingNumberGetRequest({
        materialId: id, // 物品Id
        currentPage: "1",
        deleted: 0,
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
  margin-bottom: 20px;
  button {
    margin-left: 10px;
  }
}
.editable-row-operations a {
  margin-right: 8px;
}
/deep/.ant-table {
  min-height: 220px !important;
}
/deep/ .checked-td-of-add-table {
  cursor: pointer;
  background-color: #e6f7ff;
}
</style>
