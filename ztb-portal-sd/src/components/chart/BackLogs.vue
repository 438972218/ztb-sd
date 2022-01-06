<template>
  <div class="rank">
    <h3 class="title">{{ title }}</h3>
    <ul class="list">
      <li :key="index" v-for="(item, index) in list">
        <template v-if="item.title">
          <a-col
            :sm="12"
            :md="8"
            :xl="8"
            :xxl="7"
            class="colStyle"
            @click="toCheck(item, item.status)"
            >{{ item.oddNumber }}</a-col
          >
          <a-col :sm="24" :md="16" :xl="16" :xxl="12" v-if="item.process">{{
            item.title
          }}</a-col>
          <a-col :sm="0" :md="0" :xl="0" :xxl="5" v-if="item.status">{{
            item.status.name
          }}</a-col>
        </template>
      </li>
    </ul>
  </div>
</template>

<script>
import { inquirySheetshowByRequestIdGetRequest } from "@/services/source/find";
import { bidSheetshowByRequestIdGetRequest } from "@/services/source/bidding";
import { paidSheetShowByRequestIdGetRequest } from "@/services/source/contend";

export default {
  name: "BackLogs",
  props: ["title", "list"],
  methods: {
    getAllfind(id, type) {
      inquirySheetshowByRequestIdGetRequest(id).then((response) => {
        if (response.data) {
          this.$store.state.account.findData = response.data;
          this.$store.state.account.findType = type.name;
          this.$router.push({
            path: "/findmanage/addfind",
          });
        }
      });
    },
    getAllBidding(id, type) {
      bidSheetshowByRequestIdGetRequest(id).then((response) => {
        if (response.data) {
          let account = this.$store.state.account;
          account.biddingType = type.name;
          account.biddingData = response.data;
          this.$router.push({
            path: "/findmanage/addbidding",
          });
        }
      });
    },
    getAllContend(id, type) {
      paidSheetShowByRequestIdGetRequest(id).then((response) => {
        debugger;
        if (response.data) {
          this.$store.state.account.contendData = response.data;
          this.$store.state.account.contendType = type.name;
          this.$router.push({
            path: "/findmanage/addcontend",
          });
        }
      });
    },
    toCheck(data, status) {
      if (data) {
        if (data.process.name.indexOf("询价单流程") != -1) {
          this.getAllfind(data.id, status);
        }
        if (data.process.name.indexOf("招标单流程") != -1) {
          this.getAllBidding(data.id, status);
        }
        if (data.process.name.indexOf("竞价单流程") != -1) {
          this.getAllContend(data.id, status);
        }
      }
    },
  },
};
</script>

<style lang="less" scoped>
.rank {
  padding: 0 20px 0 0;
  height: 180px;
  .list {
    margin: 0px 0 10px 0;
    padding: 0;
    list-style: none;
    li {
      overflow-y: auto;
      min-width: 430px;
      margin-top: 10px;
    }
  }
  .colStyle {
    cursor: pointer;
    color: #1890ff;
  }
}
</style>
