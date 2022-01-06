<template>
  <div>
    <a-row>
      <a-table
        :columns="columns"
        :data-source="data"
        :customRow="rowClick"
        :rowClassName="setRowClassName"
      >
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
        <template
          v-for="time in ['createdTime', 'updatedTime']"
          :slot="time"
          slot-scope="text"
        >
          <span :key="time">
            {{ $moment(Number(text)).format("YYYY-MM-DD") }}
          </span>
        </template>
        <template
          v-for="cols in [
            'offeringPeriodFrom',
            'offeringPeriodTo',
            'deliveryDate',
          ]"
          :slot="cols"
          slot-scope="text, record"
        >
          <div :key="cols">
            <a-date-picker
              v-if="record.editable"
              :value="text === 0 ? null : $moment(Number(text)).format('YYYY-MM-DD')"
              format="YYYY-MM-DD"
              @change="(e) => pickerChange(e._d, record.offeringNumber, cols)"
            />
            <template v-else>
              <!-- {{ $moment(Number(text)).format("YYYY-MM-DD") }} -->
              <span v-if="text !== 0">
                {{ $moment(Number(text)).format("YYYY-MM-DD") }}
              </span>
              <span v-else></span>
            </template>
          </div>
        </template>
        <template
          v-for="col in [
            'price',
            'deliveryCycle',
            'minSupplyQuantity',
            'unit',
            'offeringClarification',
          ]"
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
              <a
                v-if="record.status === null"
                @click="() => save(record.offeringNumber)"
                >提交</a
              >
              <a
                v-if="record.status === '保存' || record.status === '提交'"
                @click="() => alter(record.offeringNumber)"
                >修改</a
              >
              <a @click="() => cancel(record.offeringNumber)">取消</a>
              <!-- <a-popconfirm
                title="Sure to cancel?"
                @confirm="() => cancel(record.offeringNumber)"
              >
                <a>取消</a>
              </a-popconfirm> -->
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
    <a-modal v-model="visible" width="1800px" :footer="null" :centered="true" title="历史竞价">
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
  inquiryItemsGetRequest,
  inquiryVendorGetRequest,
  inquiryVendorNewPriceGetRequest,
  inquiryVendorMaterialGetRequest,
  inquiryVendorMaterialPostRequest,
  inquiryVendorMaterialPutRequest,
} from "@/services/source/find";
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
    width: 100,
    dataIndex: "inventoryOrganization",
  },
  {
    title: "物品编码",
    width: 100,
    dataIndex: "materialCode",
  },
  {
    title: "物品描述",
    width: 100,
    dataIndex: "materialDescription",
  },
  {
    title: "报价明细",
    width: 100,
    dataIndex: "quoteDetails",
  },
  {
    title: "物品说明",
    width: 100,
    dataIndex: "materialExplain",
  },
  {
    title: "数量",
    width: 100,
    dataIndex: "quantity",
  },
  {
    title: "单位",
    width: 100,
    dataIndex: "unit",
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
    title: "报价次数",
    width: 90,
    dataIndex: "offeringNumber",
  },
  {
    title: "单价(元)",
    width: 90,
    dataIndex: "price",
    scopedSlots: { customRender: "price" },
  },
  {
    title: "总价(元)",
    width: 90,
    dataIndex: "totalPrice",
  },
  {
    title: "报价有效期从",
    width: 150,
    dataIndex: "offeringPeriodFrom",
    scopedSlots: { customRender: "offeringPeriodFrom" },
  },
  {
    title: "报价有效期至",
    width: 150,
    dataIndex: "offeringPeriodTo",
    scopedSlots: { customRender: "offeringPeriodTo" },
  },
  {
    title: "承诺交货期",
    width: 150,
    dataIndex: "deliveryDate",
    scopedSlots: { customRender: "deliveryDate" },
  },
  {
    title: "供货周期",
    width: 90,
    dataIndex: "deliveryCycle",
    scopedSlots: { customRender: "deliveryCycle" },
  },
  {
    title: "最小供货数量(必填)",
    width: 160,
    dataIndex: "minSupplyQuantity",
    scopedSlots: { customRender: "minSupplyQuantity" },
  },
  {
    title: "单位(必填)",
    width: 120,
    dataIndex: "unit",
    scopedSlots: { customRender: "unit" },
  },
  {
    title: "报价说明",
    width: 150,
    dataIndex: "offeringClarification",
    scopedSlots: { customRender: "offeringClarification" },
  },

  {
    title: "报价人",
    width: 80,
    dataIndex: "createdUser",
  },
  {
    title: "还价单价",
    width: 100,
    dataIndex: "dickerPrice",
    scopedSlots: { customRender: "dickerPrice" },
  },
  {
    title: "还价时间",
    width: 120,
    dataIndex: "updatedTime",
    scopedSlots: { customRender: "updatedTime" },
  },
  {
    title: "还价人",
    width: 100,
    dataIndex: "updatedUser",
    scopedSlots: { customRender: "updatedUser" },
  },
  {
    title: "还价理由",
    width: 100,
    dataIndex: "dickerReason",
  },
  {
    title: "操作",
    dataIndex: "operation",
    width: 120,
    fixed: "right",
    align: "center",
    scopedSlots: { customRender: "operation" },
  },
];
const columns2 = [
  {
    title: "报价次数",
    width: 90,
    dataIndex: "offeringNumber",
  },
  {
    title: "单价(元)",
    width: 90,
    dataIndex: "price",
    scopedSlots: { customRender: "price" },
  },
  {
    title: "总价(元)",
    width: 90,
    dataIndex: "totalPrice",
  },
  {
    title: "报价有效期从",
    width: 150,
    dataIndex: "offeringPeriodFrom",
    scopedSlots: { customRender: "offeringPeriodFrom" },
  },
  {
    title: "报价有效期至",
    width: 150,
    dataIndex: "offeringPeriodTo",
    scopedSlots: { customRender: "offeringPeriodTo" },
  },
  {
    title: "承诺交货期",
    width: 150,
    dataIndex: "deliveryDate",
    scopedSlots: { customRender: "deliveryDate" },
  },
  {
    title: "供货周期",
    width: 90,
    dataIndex: "deliveryCycle",
    scopedSlots: { customRender: "deliveryCycle" },
  },
  {
    title: "最小供货数量(必填)",
    width: 160,
    dataIndex: "minSupplyQuantity",
    scopedSlots: { customRender: "minSupplyQuantity" },
  },
  {
    title: "单位(必填)",
    width: 120,
    dataIndex: "unit",
    scopedSlots: { customRender: "unit" },
  },
  {
    title: "报价说明",
    width: 150,
    dataIndex: "offeringClarification",
    scopedSlots: { customRender: "offeringClarification" },
  },
  {
    title: "报价时间",
    width: 120,
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
  {
    title: "报价人",
    width: 80,
    dataIndex: "createdUser",
  },
  {
    title: "还价单价",
    width: 100,
    dataIndex: "dickerPrice",
    scopedSlots: { customRender: "dickerPrice" },
  },
  {
    title: "还价时间",
    width: 120,
    dataIndex: "updatedTime",
  },
  {
    title: "还价人",
    width: 100,
    dataIndex: "updatedUser",
    scopedSlots: { customRender: "updatedUser" },
  },
  {
    title: "还价理由",
    width: 100,
    dataIndex: "dickerReason",
  },
];

export default {
  name: "OfferDetails",
  props: {
    findId: {
      type: [String, null],
    },
  },
  data() {
    return {
      id: this.findId,
      visible: false,
      itemId: "",
      show: false,
      data: [],
      columns,
      dataItem: [
        {
          offeringNumber: 1, // 报价次数
          status: null,
          price: "", // 还价单价
          dickerPrice: "", // 单价
          totalPrice: "", // 总价
          offeringPeriodFrom: 0, // 从
          offeringPeriodTo: 0, // 至
          deliveryDate: 0, // 交货期
          deliveryCycle: "", // 供货周期
          minSupplyQuantity: "", // 最小
          unit: "", // 单位
          offeringClarification: "", // 报价说明
          createdTime: 0, // 报价时间
          createdUser: "", // 报价人
          updatedTime: "", // 还价时间
          updatedUser: "", // 还价人
          dickerReason: "", // 还价理由
        },
      ],
      data1: [],
      data2: [],
      columns1,
      columns2,
      indexColims: null,
      editingKey: "",
      queryId: "",
    };
  },
  mounted() {
    if (this.id) {
      this.getAll();
      this.getQueryId();
    }
  },
  methods: {
    getAll() {
      inquiryItemsGetRequest({
        inquirySheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
      });
    },
    // 供应商id查询
    getQueryId() {
      inquiryVendorGetRequest({
        inquirySheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.queryId = response.data.records[0].id;
      });
    },
    // 添加
    addOffer(form) {
      form.offeringPeriodFrom = Date.parse(form.offeringPeriodFrom);
      form.offeringPeriodTo = Date.parse(form.offeringPeriodTo);
      form.deliveryDate = Date.parse(form.deliveryDate);
      inquiryVendorMaterialPostRequest({
        materialId: this.indexColims,
        price: form.price,
        offeringPeriodFrom: form.offeringPeriodFrom,
        offeringPeriodTo: form.offeringPeriodTo,
        deliveryDate: form.deliveryDate,
        deliveryCycle: form.deliveryCycle,
        minSupplyQuantity: form.minSupplyQuantity,
        unit: form.unit,
        offeringClarification: form.offeringClarification,
        inquiryVendorId: this.queryId,
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
    // 修改报价
    editOffer(form) {
      form.offeringPeriodFrom = Date.parse(form.offeringPeriodFrom);
      form.offeringPeriodTo = Date.parse(form.offeringPeriodTo);
      form.deliveryDate = Date.parse(form.deliveryDate);
      inquiryVendorMaterialPutRequest({
        id: form.id,
        materialId: this.indexColims,
        price: form.price,
        offeringPeriodFrom: form.offeringPeriodFrom,
        offeringPeriodTo: form.offeringPeriodTo,
        deliveryDate: form.deliveryDate,
        deliveryCycle: form.deliveryCycle,
        minSupplyQuantity: form.minSupplyQuantity,
        unit: form.unit,
        offeringClarification: form.offeringClarification,
        inquiryVendorId: this.queryId,
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
      inquiryVendorNewPriceGetRequest({
        inquiryVendorId: this.queryId,
        materialId: id, // 物品Id
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        if (response.data !== null) {
          this.data1 = response.data;
        } else {
          this.data1 = [
            {
              offeringNumber: 1, // 报价次数
              status: null,
              price: "", // 还价单价
              dickerPrice: "", // 单价
              totalPrice: "", // 总价
              offeringPeriodFrom: 0, // 从
              offeringPeriodTo: 0, // 至
              deliveryDate: 0, // 交货期
              deliveryCycle: "", // 供货周期
              minSupplyQuantity: "", // 最小
              unit: "", // 单位
              offeringClarification: "", // 报价说明
              createdTime: 0, // 报价时间
              createdUser: "", // 报价人
              updatedTime: "", // 还价时间
              updatedUser: "", // 还价人
              dickerReason: "", // 还价理由
            },
          ];
        }
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
    // 新建报价
    handleChange(value, key, column) {
      const newData = [...this.data1];
      const target = newData.filter((item) => key === item.offeringNumber)[0];
      if (target) {
        target[column] = value;
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
      let data = this.operationType(key);
      this.addOffer(data);
    },
    alter(key) {
      let data = this.operationType(key);
      this.editOffer(data);
    },
    operationType(key) {
      const newData = [...this.data1];
      const target = newData.filter((item) => key === item.offeringNumber)[0];
      if (target.minSupplyQuantity === "" || target.unit === "") {
        this.$message.error("请填写必填项");
      } else {
        if (target) {
          delete target.editable;
          this.data1 = newData;
        }
        this.editingKey = "";
      }
      return this.data1[0];
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
      inquiryVendorMaterialGetRequest({
        materialId: id, // 物品Id
        currentPage: "1",
        inquiryVendorId: this.queryId,
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
