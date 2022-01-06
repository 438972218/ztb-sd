<template>
  <div>
    <a-table :columns="columns" :data-source="data">
    </a-table>
  </div>
</template>
<script>
import {
  inquiryVendorGetRequest,
} from "@/services/source/find";
export default {
  name: "FindOffer",
  props: {
    findId: {
      type: [String, null],
    },
  },
  data() {
    return {
      id: this.findId,
      data: [],
      columns: [
        {
          title: "序号",
          key: "index",
          align: "center",
          width: 70,
          customRender: (text, record, index) => `${index + 1}`,
        },
        {
          title: "供应商代码",
          dataIndex: "vendorCode",
        },
        {
          title: "供应商名称",
          dataIndex: "vendorName",
        },
        {
          title: "供应商描述",
          dataIndex: "vendorDescription",
        },
        {
          title: "是否黑名单",
          dataIndex: "ifBlacklist",
        },
        {
          title: "联系人",
          dataIndex: "linkman",
        },
        {
          title: "联系电话",
          dataIndex: "phone",
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
      inquiryVendorGetRequest({
        inquirySheetId: this.id,
        currentPage: "1",
        deleted: 0,
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
