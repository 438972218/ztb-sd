<template>
  <div>
    <a-card class="star-title">
      <div class="starSty">
        <a-icon
          style="color: #f6a623; font-size: 24px"
          type="star"
          theme="filled"
        />
      </div>
      <a-button size="large" type="primary" disabled> 已授权 </a-button>
      <div class="starTitleName">层压皮</div>
    </a-card>
    <a-card class="card-top">
      <a-row>
        <a-table
          :columns="columns"
          :data-source="data"
          :customRow="rowClick"
          :rowKey="(record) => record.id"
          :rowClassName="setRowClassName"
        >
          <template slot="a1" slot-scope="text">
            <span @click="goView(5)" class="text-cursor">
              {{ text }}
            </span>
          </template>
          <template
            v-for="cols in ['offerStartTime', 'offerEndTime']"
            :slot="cols"
            slot-scope="text"
          >
            <div :key="cols">
              {{ $moment(Number(text)).format("YYYY-MM-DD") }}
            </div>
          </template>
        </a-table>
      </a-row>
    </a-card>
    <a-card class="card-top" style="padding:20px">
      <a-row :gutter="25">
      <a-col :sm="24" :xl="9">
        <a-row class="message-title">
          <a-col :sm="24" :xl="12">起始价格</a-col>
          <a-col :sm="24" :xl="12">最小出价 减少量</a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :xl="12">2000RMB</a-col>
          <a-col :sm="24" :xl="12">10RMB</a-col>
        </a-row>
          <a-row class="message-title">
          <a-col :sm="24" :xl="12">数量</a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :xl="12">1个</a-col>
        </a-row>
          <a-row class="message-title">
          <a-col :sm="24" :xl="12">方向</a-col>
          <a-col :sm="24" :xl="12">触发价格</a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :xl="12">方向</a-col>
          <a-col :sm="24" :xl="12">未达到</a-col>
        </a-row>
         <a-row class="message-title">
          <a-col :sm="24" :xl="12">出价规则</a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :xl="12">你必须通过最小出价变更来改进最优出价</a-col>
        </a-row>
      </a-col>
      <a-col :sm="24" :xl="8">
        <a-table :columns="columns1" :data-source="data1"> </a-table>
      </a-col>
      <a-col :sm="24" :xl="7" class="offer-message">
        <Message></Message>
      </a-col>
      </a-row>
    </a-card>
  </div>
</template>
<script>
import Message from "./components/message.vue";
const columns = [
  {
    title: "竞价排名",
    key: "index",
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "代码",
    dataIndex: "price",
  },
  {
    title: "最后出价",
    dataIndex: "createdUser",
  },
  {
    title: "排名",
    dataIndex: "paidVendorId",
  },
  {
    title: "触发价格",
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
  {
    title: "操作",
    dataIndex: "operation",
    width: 250,
    align: "center",
    scopedSlots: { customRender: "operation" },
  },
];
const columns1 = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "出价",
    dataIndex: "oddNumber",
    scopedSlots: { customRender: "oddNumber" },
  },
  {
    title: "时间",
    dataIndex: "title",
  },
];
export default {
  name: "quotepage",
    components: { Message },
  data() {
    return {
      data: [],
      data1: [],
      columns,
      columns1,
    };
  },
  created() {
    this.getAll();
  },
  mounted() {},

  computed: {},
  methods: {
    getAll() {
      // paidSheetGetRequest({
      //   currentPage: "1",
      //   deleted: 0,
      //   statusId: "1415213213457317890",
      // }).then((response) => {
      //   this.data1 = response.data.records;
      // });
    },
    // 点击行事件
    rowClick(record) {
      if (this.timer) {
        clearInterval(this.timer);
      }
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
            this.paidData.oddNumber = record.oddNumber;
            this.paidData.title = record.title;
            this.paidData.purchasingOrganization =
              record.purchasingOrganization;
            let times = record.offerEndTime;
            this.fixedTimes = this.$moment(Number(times)).format("YYYY/MM/DD");
            this.remainTime();
            this.getpaidItem(record.paidMaterialId);
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
  },
};
</script>
<style lang="less" scoped>
.star-title {
  margin-bottom: 5px;
  .starSty {
    background-color: #ccc;
    display: inline-block;
    padding: 10px;
    border-radius: 50%;
  }
  button {
    margin: 0 20px;
  }
  .starTitleName {
    display: inline-block;
    font-size: 20px;
    color: #999;
    font-weight: bolder;
  }
}
.offer-message {
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  border: 2px solid #ccc;
  height: 300px;
  padding: 0px!important;
  div{
    border-bottom: 2px solid #ccc;
    padding: 10px;
    font-size: 16px;
    font-weight: bold;
  }
}
.message-title{
    font-weight: bolder;
    font-size: 16px;
    margin-top: 30px;
}
</style>