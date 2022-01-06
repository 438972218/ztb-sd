<template>
  <div>
    <a-row class="btn-sty">
      <a-button
        type="primary"
        @click="save()"
        v-if="biddingType === '待专家评分'"
      >
        保存
      </a-button>
    </a-row>
    <a-row v-if="biddingType === '待专家评分'">
      <a-table :columns="columns" :data-source="data" rowKey="id" bordered>
        <template slot="vendorCode" slot-scope="text, record">
          <div v-if="record.bidVendorVO">
            {{ record.bidVendorVO.vendorCode }}
          </div>
        </template>
        <template slot="vendorName" slot-scope="text, record">
          <div v-if="record.bidVendorVO">
            {{ record.bidVendorVO.vendorName }}
          </div>
        </template>
        <template slot="updatedUser" slot-scope="text, record">
          <div v-if="record.bidVendorVO">
            {{ record.bidVendorVO.updatedUser }}
          </div>
        </template>
        <template slot="totalPrice" slot-scope="text, record">
          <div v-if="record.bidVendorVO">
            {{ record.bidVendorVO.totalPrice }}
          </div>
        </template>
        <template slot="score" slot-scope="text, record">
          <a-input v-if="biddingType === '待专家评分'" v-model="record.score" />
        </template>
      </a-table>
    </a-row>
    <a-row v-if="biddingType === '待专家组长确认'">
      <a-table :columns="columns2" :data-source="data2" rowKey="id" bordered>
      </a-table>
    </a-row>
    <a-modal
      v-model="visible"
      width="1000px"
      :footer="null"
      :centered="true"
      title="明细列表"
    >
      <a-table :columns="columns1" :data-source="data1" rowKey="id"> </a-table>
    </a-modal>
  </div>
</template>
<script>
import {
  bidSpecialistScoreWithSpecialistGetRequest,
  bidSpecialistScoreBatchPutRequest,
  bidVendorWithScoreGetRequest,
} from "@/services/source/bidding";
import { mapGetters } from "vuex";
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "投标编号",
    width: 120,
    dataIndex: "vendorCode",
    scopedSlots: { customRender: "vendorCode" },
  },
  {
    title: "投标方名称",
    width: 150,
    dataIndex: "vendorName",
    scopedSlots: { customRender: "vendorName" },
  },
  {
    title: "投标方",
    width: 150,
    dataIndex: "updatedUser",
    scopedSlots: { customRender: "updatedUser" },
  },
  {
    title: "投标总价",
    width: 150,
    dataIndex: "totalPrice",
    scopedSlots: { customRender: "totalPrice" },
  },
  // {
  //   title: "最低价比重",
  //   width: 100,
  //   dataIndex: "approvalOpinion",
  //   scopedSlots: { customRender: "approvalOpinion" },
  // },
  {
    title: "评分",
    width: 100,
    dataIndex: "score",
    scopedSlots: { customRender: "score" },
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
const columns1 = [
  {
    title: "序号",
    key: "index",
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "代码",
    dataIndex: "a1",
  },
  {
    title: "资格预审项目名称",
    dataIndex: "a2",
  },
  {
    title: "是否需要附件",
    dataIndex: "a4",
    scopedSlots: { customRender: "a4" },
  },
  {
    title: "说明",
    dataIndex: "a5",
  },
  {
    title: "申请说明",
    dataIndex: "a6",
  },
  {
    title: "附件",
    width: 150,
    dataIndex: "a7",
  },
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
    title: "投标编号",
    width: 120,
    dataIndex: "vendorCode",
    scopedSlots: { customRender: "vendorCode" },
  },
  {
    title: "投标方名称",
    width: 150,
    dataIndex: "vendorName",
    scopedSlots: { customRender: "vendorName" },
  },
  {
    title: "投标方",
    width: 150,
    dataIndex: "updatedUser",
    scopedSlots: { customRender: "updatedUser" },
  },
  {
    title: "投标总价",
    width: 150,
    dataIndex: "totalPrice",
    scopedSlots: { customRender: "totalPrice" },
  },
  {
    title: "评分",
    width: 100,
    dataIndex: "totalScore",
  },

];
export default {
  name: "review",
  props: {
    biddingId: {
      type: [String, null],
    },
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  data () {
    return {
      id: this.biddingId,
      biddingType: this.$store.state.account.biddingType,
      show: false,
      data: [],
      columns,
      columns2,
      data1: [],
      data2: [],
      columns1,
      editingKey: "",
      visible: false,
    };
  },
  mounted () {
    if (this.id) {
      this.getAll();
    }
  },
  methods: {
    getAll () {
      if (this.biddingType === "待专家评分") {
        bidSpecialistScoreWithSpecialistGetRequest({
          bidSheetId: this.id,
          userId: this.user.id,
          currentPage: "1",
          deleted: 0,
        }).then((response) => {
          this.data = response.data.records;
        });
      } else if (this.biddingType === "待专家组长确认") {
        bidVendorWithScoreGetRequest({
          bidSheetId: this.id,
          userId: this.user.id,
          currentPage: "1",
          deleted: 0,
        }).then((response) => {
          this.data2 = response.data;
        });
      }
    },
    // 强制改变
    changeBoxIfForce (e, data) {
      data.informationPass = e.target.checked === false ? "0" : "1";
    },
    addNew () {
      this.visible = true;
    },
    save () {
      let data = [];
      this.data.map((item, index) => {
        data.push({
          id: item.id,
          score: item.score,
          sheetId: this.id,
        });
      });
      bidSpecialistScoreBatchPutRequest(data).then((response) => {
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
