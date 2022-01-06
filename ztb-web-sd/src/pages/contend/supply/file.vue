<template>
  <div>
    <a-table :columns="columns" :data-source="data">
    </a-table>
    
  </div>
</template>
<script>
import {
  paidAttachmentGetRequest,
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
  name: "FindFile",
  props: {
    contendVendorId: {
      type: [String, null],
    },
  },
  data() {
    return {
      id: this.contendVendorId,
      data:[],
      columns,
    };
  },
  created(){
      if (this.id) {
      this.getAll();
    }
  },
  methods: {
        getAll() {
      paidAttachmentGetRequest({
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
