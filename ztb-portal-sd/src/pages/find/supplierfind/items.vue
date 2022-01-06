<template>
  <div>

    <a-table :columns="columns" :data-source="data">
    </a-table>
  </div>
</template>
<script>
import { inquiryItemsGetRequest} from "@/services/source/find";


export default {
  name: "FindItems",
  props: {
    findId: {
      type: [String, null],
    },
  },
  data() {
    return {
      form: {
        inventoryOrganization: "", // 库存组织
        materialCode: "", // 物品编码
        materialDescription: "", // 物品描述
        quoteDetails: "", // 报价明细
        materialExplain: "", // 物品说明
        quantity: "", // 数量
        unit: "", // 单位
      },
      data: [],
      id: this.findId,
      columns: [
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
        // {
        //   title: "物品分类",
        //   dataIndex: "a4",
        // },
        {
          title: "报价明细",
          dataIndex: "quoteDetails",
        },
        {
          title: "物品说明",
          dataIndex: "materialExplain",
        },
        {
          title: "数量",
          dataIndex: "quantity",
        },
        {
          title: "单位",
          dataIndex: "unit",
        },
      ],
    };
  },
  created() {
    if(this.id){
    this.getAll()
    }
  },
  methods: {
          getAll() {
      inquiryItemsGetRequest({
        inquirySheetId:this.id,
        currentPage: "1",
        deleted: 0
      }).then((response) => {
        this.data = response.data.records;
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
