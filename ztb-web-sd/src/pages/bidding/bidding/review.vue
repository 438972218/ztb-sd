<template>
  <div>
    <a-row class="btn-sty">
      <a-button type="primary" @click="save()"> 保存 </a-button>
    </a-row>

    <a-row>
      <a-table :columns="columns" :data-source="data" bordered>
        <template slot="detail" slot-scope="text, record">
          <div
            style="color: #40a9ff; cursor: pointer"
            @click="addNew(record.id)"
          >
            查看
          </div>
        </template>
        <template slot="informationPass" slot-scope="text, record">
          <a-checkbox
            :checked="
              record.informationPass === '0'
                ? false
                : record.informationPass === null
                ? false
                : true
            "
            @change="(e) => changeBoxIfForce(e, record)"
          >
          </a-checkbox>
        </template>
        <template slot="approvalOpinion" slot-scope="text, record">
          <a-input v-model="record.approvalOpinion" />
        </template>
      </a-table>
    </a-row>
    <a-modal
      v-model="visible"
      width="1000px"
      :footer="null"
      :centered="true"
      title="明细列表"
    >
      <a-table :columns="columns1" :data-source="data1">
        <template slot="createdTime" slot-scope="text">
          {{ $moment(Number(text)).format('YYYY-MM-DD') }}
        </template>
        <template slot="operation" slot-scope="text, record">
          <div style="color: #40a9ff; cursor: pointer" @click="upFile(record)">
            下载
          </div>
        </template>
      </a-table>
    </a-modal>
  </div>
</template>
<script>
import {
  bidVendorPreTrialItemGetRequest,
  bidVendorGetRequest,
  bidVendorBatchPutRequest
} from "@/services/source/bidding";
import { attachmentGetfileGetRequest } from "@/services/system/user";
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "投标方",
    width: 120,
    dataIndex: "vendorCode",
  },
  {
    title: "资格预审申请明细",
    width: 150,
    dataIndex: "detail",
    scopedSlots: { customRender: "detail" },
  },
  {
    title: "审核通过",
    width: 100,
    dataIndex: "informationPass",
    scopedSlots: { customRender: "informationPass" },
  },
  {
    title: "审批意见",
    width: 100,
    dataIndex: "approvalOpinion",
    scopedSlots: { customRender: "approvalOpinion" },
  },
  //   {
  //     title: "操作",
  //     dataIndex: "operation",
  //     width: 150,
  //     align: "center",
  //     fixed: "right",
  //     scopedSlots: { customRender: "operation" },
  //   },
];

const data = [
  {
    a1: "公司1",
    a2: "查看",
    a3: false,
    a4: "审批意见",
  },
  {
    a1: "公司1",
    a2: "查看",
    a3: false,
    a4: "审批意见",
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
    title: "预审描述",
    dataIndex: "applyExplanation",
  },
  {
    title: "附件名称",
    dataIndex: "attachmentName",
  },
  {
    title: "创建人",
    dataIndex: "createdUser",
  },
  {
    title: "创建时间",
    width: 150,
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
  {
    title: "操作",
    dataIndex: "operation",
    width: 150,
    align: "center",
    scopedSlots: { customRender: "operation" },
  },
];

const data1 = [
  {
    a1: "YS00001",
    a2: "项目名称",
    a4: "是",
    a5: "说明",
    a6: "资格过关",
    a7: "附件1",
  },
  {
    a1: "YS00001",
    a2: "名称1",
    a3: "项目1",
    a4: "是",
    a5: "说明",
    a6: "资格过关",
    a7: "附件1",
  },
];
export default {
  name: "review",
  props: {
    biddingId: {
      type: [String, null],
    },
  },
  data () {
    return {
      id: this.biddingId,
      biddingType: this.$store.state.account.biddingType,
      show: false,
      data,
      columns,
      data1,
      columns1,
      editingKey: "",
      visible: false,
    };
  },
  mounted () {
    if (this.id) {
      this.getAll()
    }
  },
  methods: {
    getAll () {
      bidVendorGetRequest({
        bidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
      });
    },
    // 强制改变
    changeBoxIfForce (e, data) {
      data.informationPass = e.target.checked === false ? '0' : '1';
    },
    addNew (id) {
      bidVendorPreTrialItemGetRequest({
        bidVendorId: id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data1 = response.data.records;
        this.visible = true;

      });
    },
    // 下载文件
    upFile (record) {
      attachmentGetfileGetRequest({
        path: record.attachmentUrl
      }).then((response) => {
        if (response.code === 0) {
          window.location.href = response.data
          this.$message.success("下载成功");
        }
      });
    },
    save () {
      bidVendorBatchPutRequest(
        this.data

      ).then((response) => {
        if (response.code === 0) {
          this.$message.success("添加成功");
          this.getAll();
        } else {
          this.$message.error(response.message);
        }
      });
    },

    back () {
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
</style>
