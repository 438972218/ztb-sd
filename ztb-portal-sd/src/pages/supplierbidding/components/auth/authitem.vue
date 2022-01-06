<template>
  <div>
    <a-row>
      <a-row style="padding-bottom: 5px">
        <a-col :span="2">
          <span style="font-size: 22px; font-weight: 550">品类</span>
        </a-col>
      </a-row>
      <a-table :columns="columns" :data-source="itemData"> </a-table>
    </a-row>
  </div>
</template>

<script>
import {
  getPaidMaterialMandateByVendor,
  getVendorLastRanking,
  postPaidMaterialMandate,
  deletePaidMaterialMandate,
} from "@/services/source/contend";
import { mapGetters } from "vuex";
const columns = [
  {
    title: "品类名称",
    dataIndex: "materialName",
    key: "materialName",
  },
  {
    title: "出价",
    dataIndex: "paidMaterialMandateVO.price",
    key: "price",
  },
  // {
  //   title: "授权供应商",
  //   dataIndex: "paidMaterialMandateVO.vendorVO.name",
  //   key: "name",
  // },
  {
    title: "确定价格",
    dataIndex: "paidMaterialMandateVO.confirmedPrice",
    key: "confirmedPrice",
  },
  // {
  //   title: "操作",
  //   dataIndex: "operation",
  //   key: "operation",
  //   scopedSlots: { customRender: "operation" },
  // },
];
export default {
  props: {
    show: {
      type: Boolean,
      default: true,
    },
    minimumBidRuleList: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  name: "authitem",
  data () {
    return {
      title: "新增品类",
      editingKey: "",
      visible: false,
      formInline: {
        id: "",
        materialName: "",
        materialId: "",
      },
      formInline2: {
        //授权塔式服务器
        vendorId: "",
        confirmedPrice: "",
        offerUser: "",
      },
      form: {
        bidStructure: "",
      },
      columns,
      itemData: [],
      itemData2: [],
      selectedRowKeys2: [],
    };
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  mounted () {
    this.form = JSON.parse(sessionStorage.getItem("contendForm"));
    this.getItems(); //获取品类
  },
  methods: {
    cancel () {
      this.visible = false;
      this.selectedRowKeys2 = [];
    },
    async handleCancel (record) {
      //取消授权
      const result = await deletePaidMaterialMandate(
        record.paidMaterialMandateVO.id
      );
      if (result.code === 0) {
        this.$message.success("取消授权成功");
        this.getItems(); //获取品类
      } else {
        this.$message.error(result.message);
      }
    },
    //授权
    async auth () {
      const newData = [...this.itemData2];
      const target = newData.filter(
        (item) => item.vendorId == this.formInline2.vendorId
      )[0];
      if (target) {
        const param = {
          paidMaterialId: this.formInline.materialId,
          confirmedPrice: target.confirmedPrice,
          price: target.money,
          paidVendorId: target.vendorId,
          paidVendorUserName: target.offerUser,
        };
        const result = await postPaidMaterialMandate(param);
        if (result.code === 0) {
          this.$message.success("授权成功");
          this.visible = false;
          this.getItems(); //获取品类
        } else {
          this.$message.error(result.message);
        }
      } else {
        this.$message.success("请选择授权供应商");
      }
    },
    async toauth () {
      //授权
      const param = {
        materialId: this.formInline.materialId,
        requestId: this.form.requestId,
      };
      const result = await getVendorLastRanking(param);
      if (result.code === 0) {
        this.itemData2 = result.data.totalRanking.map((item) => ({
          ...item,
          editable: false,
          checkable: true,
        }));
      } else {
        this.$message.error(result.message);
      }
      if (this.formInline.id == "") {
        this.$message.warning("请选择品类");
        return;
      }
      this.visible = true;
    },
    // 竞价物品查询
    async getItems () {
      const result = await getPaidMaterialMandateByVendor({
        requestId: this.form.requestId,
        vendorUserId: this.user.id,
      });
      if (result.code === 0) {
        this.itemData = result.data
          ? result.data.map((item) => ({ ...item, editable: false }))
          : [];
      }
    },
    handleChange (value, key, column) {
      const newData = [...this.itemData2];
      const target = newData.filter((item) => key === item.vendorId)[0];
      if (target) {
        target[column] = value;
        this.itemData2 = newData;
      }
    },
  },
};
</script>
<style lang="less" scoped></style>
