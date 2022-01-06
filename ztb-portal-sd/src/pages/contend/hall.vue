<template>
  <div>
    <a-card class="card-top">
      <a-row>
        <a-col :span="24" class="btn-sty" style="margin-bottom: 20px">
          <a-button type="primary"> 暂停 </a-button>
          <a-button type="primary"> 开启 </a-button>
          <a-button type="primary"> 延长竞价时间 </a-button>
          <a-button> 终止 </a-button>
        </a-col>
      </a-row>
      <a-row>
        <a-row>
          <a-col :md="7" :sm="24">
            <a-row :gutter="[14, 30]" class="rfqstyle">
              <a-col :span="10">竞价单号：</a-col
              ><a-col :span="14">{{ paidData.oddNumber }}</a-col>
            </a-row>
            <a-row :gutter="[14, 30]" class="rfqstyle">
              <a-col :span="10">距离竞价结束时间：</a-col
              ><a-col :span="14"> {{ time }}</a-col>
            </a-row>
            <a-row :gutter="[14, 30]" class="rfqstyle">
              <a-col :span="10">当前时间：</a-col
              ><a-col :span="14">{{ paidData.timeNow }}</a-col>
            </a-row>
            <a-row :gutter="[14, 30]" class="rfqstyle">
              <a-col :span="10">竞价单标题：</a-col
              ><a-col :span="14">{{ paidData.title }}</a-col> </a-row
            ><a-row :gutter="[14, 30]" class="rfqstyle">
              <a-col :span="10">采购组织：</a-col
              ><a-col :span="14">{{ paidData.purchasingOrganization }}</a-col>
              <!-- <a-col :span="10">需求数量：</a-col><a-col :span="14">500</a-col> -->
            </a-row>
          </a-col>
          <a-col :md="17" :sm="24">
            <a-table
              :columns="columns"
              :data-source="data"
              :rowKey="(record) => record.id"
            >
              <template slot="createdTime" slot-scope="text">
                <div>
                  {{ $moment(Number(text)).format("YYYY-MM-DD") }}
                </div>
              </template>
              <template slot="operation" slot-scope="text, record">
                <span class="text-cursor" @click="addNew(record.materialId)">
                  查看
                </span>
              </template>
            </a-table>
          </a-col>
        </a-row>
      </a-row>
    </a-card>
    <a-card class="card-top">
      <div class="content-title">出价记录</div>
      <a-row>
        <a-form layout="horizontal">
          <!-- <div :class="advanced ? null: 'fold'"> -->
          <div>
            <a-row>
              <a-col :md="5" :sm="24">
                <a-form-item
                  label="竞价单号"
                  :labelCol="{ span: 5 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-input />
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item
                  label="竞价单名称"
                  :labelCol="{ span: 6 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-input />
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item
                  label="采购组织"
                  :labelCol="{ span: 6 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-input />
                </a-form-item>
              </a-col>
              <a-col :offset="5" :md="4" :sm="24">
                <span style="float: right; margin-top: 3px">
                  <a-button type="primary" @click="getAll()">查询</a-button>
                </span>
              </a-col>
            </a-row>
          </div>
        </a-form>
        <a-table
          :columns="columns1"
          :data-source="data1"
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
    <a-modal v-model="visible" width="1500px" :footer="null" :centered="true" title="历史竞价">
      <a-table
        :columns="columns2"
        :data-source="data2"
        :rowKey="(record) => record.id"
        :scroll="{ x: 1300 }"
      >
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
const columns = [
  {
    title: "竞价排名",
    key: "index",
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "报价(元)",
    dataIndex: "price",
  },
  {
    title: "竞价人",
    dataIndex: "createdUser",
  },
  {
    title: "供应商",
    dataIndex: "paidVendorId",
  },
  {
    title: "竞价时间",
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
  {
    title: "出价记录",
    dataIndex: "operation",
    scopedSlots: { customRender: "operation" },
  },
  // {
  //   title: "操作",
  //   dataIndex: "operation",
  //   key: "gender",
  //   width: 250,
  //   align: "center",
  //   scopedSlots: { customRender: "operation" },
  // },
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
    title: "竞价单号",
    dataIndex: "oddNumber",
    scopedSlots: { customRender: "oddNumber" },
  },
  {
    title: "竞价单标题",
    dataIndex: "title",
  },
  {
    title: "轮次",
    dataIndex: "a3",
  },
  {
    title: "竞价方向",
    dataIndex: "paidDirection",
  },
  {
    title: "竞价方式",
    dataIndex: "paidMode",
  },
  {
    title: "竞价开始时间",
    dataIndex: "offerStartTime",
    scopedSlots: { customRender: "offerStartTime" },
  },
  {
    title: "竞价结束时间",
    dataIndex: "offerEndTime",
    scopedSlots: { customRender: "offerEndTime" },
  },
  {
    title: "采购组织",
    dataIndex: "purchasingOrganization",
  },
  {
    title: "公司代码",
    dataIndex: "companyCode",
  },
  {
    title: "竞价规则",
    dataIndex: "publicRules",
  },
  {
    title: "状态",
    dataIndex: "requestStatusName",
  },
];
const columns2 = [
  {
    title: "序号",
    key: "index",
    width: 80,
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "报价时间",
    align: "center",
    width: 170,
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
  {
    title: "报价人",
    align: "center",
    width: 140,
    dataIndex: "createdUser",
  },
  {
    title: "单价(元)",
    align: "center",
    width: 140,
    dataIndex: "price",
  },
  {
    title: "报价有效期从",
    align: "center",
    width: 170,
    dataIndex: "offeringPeriodFrom",
    scopedSlots: { customRender: "offeringPeriodFrom" },
  },
  {
    title: "报价有效期至",
    align: "center",
    width: 170,
    dataIndex: "offeringPeriodTo",
    scopedSlots: { customRender: "offeringPeriodTo" },
  },
  {
    title: "承诺交货期",
    align: "center",
    width: 170,
    dataIndex: "deliveryDate",
    scopedSlots: { customRender: "deliveryDate" },
  },
  {
    title: "供应周期",
    align: "center",
    width: 100,
    dataIndex: "deliveryCycle",
  },
  {
    title: "报价说明",
    align: "center",
    width: 100,
    dataIndex: "offeringClarification",
  },
  {
    title: "最小供货数量",
    width: 170,
    align: "center",
    dataIndex: "minSupplyQuantity",
  },
  {
    title: "单位",
    align: "center",
    width: 100,
    dataIndex: "unit",
  },
];
import {
  paidSheetGetRequest,
  paidVendorGetRequest,
  paidVendorMaterialGetRequest,
  paidVendorMaterialQueryRankingGetRequest,
} from "@/services/source/contend";
import { mapGetters } from "vuex";
import { el } from "date-fns/locale";
export default {
  name: "quotepage",

  data() {
    return {
      visible: false,
      data: [],
      columns,
      data1: [],
      columns1,
      data2: [],
      columns2,
      paidData: {
        paidNum: "",
        paidTitle: "",
        purchasingOrganization: "",
        timeNow: "",
      },
      time: "",
      timer: "",
      fixedTimes: "",
    };
  },
  watch: {},
  created() {
    // this.remainTime();
    this.getAll();
  },
  mounted() {
    this.currentTime();
  },
  activated() {
    if (this.formatDate) {
      clearInterval(this.formatDate);
    }
    if (this.showtime) {
      clearInterval(this.showtime);
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  methods: {
    getAll() {
      paidSheetGetRequest({
        currentPage: "1",
        deleted: 0,
        statusId: "1415213213457317890",
      }).then((response) => {
        this.data1 = response.data.records;
      });
    },

    // 请求供应商物品
    getpaidItem(id) {
      this.data = [];
      paidVendorMaterialQueryRankingGetRequest({
        materialId: id,
      }).then((response) => {
        if (response.data.paidVendorMaterialVOS) {
          let list = response.data.paidVendorMaterialVOS.splice(0, 3);
          this.data = list;
        }
      });
    },
    // 时间定时器
    currentTime() {
      setInterval(this.formatDate, 1000);
    },
    remainTime() {
      setInterval(this.showtime, 1000);
    },
    // 当前时间
    formatDate() {
      let date = new Date();
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      let hour = date.getHours();
      hour = hour < 10 ? "0" + hour : hour;
      let minute = date.getMinutes();
      minute = minute < 10 ? "0" + minute : minute;
      let second = date.getSeconds();
      second = second < 10 ? "0" + second : second;
      this.paidData.timeNow = `${year}/${month}/${day} ${hour}:${minute}:${second}`;
    },
    // 招标商id查询
    // getQueryId(id) {
    //   paidVendorGetRequest({
    //     paidSheetId: id,
    //      vendorCode: this.user.userName,
    //     currentPage: "1",
    //     deleted: 0,
    //   }).then((response) => {
    //     debugger
    //     if (response.data.records) {
    //       let queryId = response.data.records[0].id;
    //       this.getpaidItem(id, queryId);
    //     } else {
    //       this.$message.warning("暂无报价！");
    //     }
    //   });
    // },
    // 查询报价记录
    addNew(id) {
      paidVendorMaterialGetRequest({
        materialId: id,
        currentPage: 1,
      }).then((response) => {
        this.data2 = response.data.records;
      });
      this.visible = true;
    },
    // 倒计时
    showtime() {
      var nowtime = new Date(); //获取当前时间
      var endtime = new Date(this.fixedTimes); //定义结束时间
      var lefttime = endtime.getTime() - nowtime.getTime(), //距离结束时间的毫秒数
        leftd = Math.floor(lefttime / (1000 * 60 * 60 * 24)), //计算天数
        lefth = Math.floor((lefttime / (1000 * 60 * 60)) % 24), //计算小时数
        leftm = Math.floor((lefttime / (1000 * 60)) % 60), //计算分钟数
        lefts = Math.floor((lefttime / 1000) % 60); //计算秒数
      if (lefttime <= 0) {
        this.time = "已过期";
      } else {
       this.time = leftd + "天" + lefth + "小时" + leftm + "分" + lefts + "秒";
        // return leftd + "天" + lefth + "小时" + leftm + "分" + lefts + "秒"; //返回倒计时的字符串
      }
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
    goView(num) {
      this.$router.push({
        path: "/findmanage/addcheck",
      });
      this.$store.state.account.findType = num;
    },

    c_delete() {
      this.$confirm({
        title: "剔除供应商",
        content: "请确认是否剔除该供应商竞价资格剔除后该供应商将无法参与竞价！",
        centered: true,
        onOk() {
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel() {},
      });
    },
  },
  // 清除定时器
  deactivated() {
    if (this.formatDate) {
      clearInterval(this.formatDate);
    }
    if (this.showtime) {
      clearInterval(this.showtime);
    }
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  button {
    margin-left: 10px;
  }
}
.rfqstyle div:nth-child(2n-1) {
  text-align: right;
}
/deep/ .checked-td-of-add-table {
  cursor: pointer;
  background-color: #e6f7ff;
}
</style>