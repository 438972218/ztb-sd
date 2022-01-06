<template>
  <page-layout>
    <template>
      <a-row :gutter="[20, 10]" style="margin: 0 -12px">
        <a-col style="padding: 0 12px" :span="12">
          <a-spin :spinning="bidLoading" tip="Loading...">
            <a-card>
              <div class="content-title">招标单</div>
              <a-table
                rowKey="id"
                bordered
                :columns="bidColumns"
                :data-source="bidList"
                @change="bidTableChange"
                :pagination="bidPagination"
              >
                <template slot="oddNumber" slot-scope="text, record">
                  <a @click="handleBidEdit(record)">{{ text }}</a>
                </template>
              </a-table>
            </a-card>
          </a-spin>
        </a-col>
        <a-col style="padding: 0 12px" :span="12">
          <a-spin :spinning="paidLoading" tip="Loading...">
            <a-card>
              <div class="content-title">竞价单</div>
              <a-table
                rowKey="id"
                bordered
                :columns="paidColumns"
                :data-source="paidList"
                @change="paidTableChange"
                :pagination="paidPagination"
              >
                <template slot="oddNumber" slot-scope="text, record">
                  <a @click="handlePaidEdit(record)">{{ text }}</a>
                </template>
              </a-table>
            </a-card>
          </a-spin>
        </a-col>
      </a-row>
    </template>
    <a-modal title="Title" v-model="show" width="600px" @ok="handleOk">
      <a-transfer
        class="tree-transfer"
        :titles="['选择系统菜单', '已选菜单']"
        :data-source="dataSource"
        :target-keys="targetKeys"
        :render="(item) => item.title"
        :show-select-all="false"
        @change="onChange"
      >
        <template
          slot="children"
          slot-scope="{
            props: { direction, selectedKeys },
            on: { itemSelect },
          }"
        >
          <a-tree
            v-if="direction === 'left'"
            blockNode
            checkable
            checkStrictly
            defaultExpandAll
            :checkedKeys="[...selectedKeys, ...targetKeys]"
            :treeData="treeData"
            @check="
              (_, props) => {
                onChecked(
                  _,
                  props,
                  [...selectedKeys, ...targetKeys],
                  itemSelect
                )
              }
            "
            @select="
              (_, props) => {
                onChecked(
                  _,
                  props,
                  [...selectedKeys, ...targetKeys],
                  itemSelect
                )
              }
            "
          />
        </template>
      </a-transfer>
    </a-modal>
  </page-layout>
</template>

<script>
import PageLayout from "@/layouts/PageLayout"; BackLogs
import RankingList from "../../../components/chart/RankingList";
import BackLogs from "../../../components/chart/BackLogs.vue";
import { paidSheetGetRequest } from "@/services/source/contend"
import { biddingGetRequest } from "@/services/source/bidding";
import moment from 'moment'
import { mapState, mapGetters } from "vuex";

const treeData = [
  {
    key: "0",
    title: "系统用户管理",
    children: [
      {
        key: "0-1",
        title: "系统设置",
        children: [{ key: "02", title: "操作菜单" }],
      },
    ],
  },
];

const transferDataSource = [];
function flatten (list = []) {
  list.forEach((item) => {
    transferDataSource.push(item);
    flatten(item.children);
  });
}
flatten(JSON.parse(JSON.stringify(treeData)));

function isChecked (selectedKeys, eventKey) {
  return selectedKeys.indexOf(eventKey) !== -1;
}

function handleTreeData (data, targetKeys = []) {
  data.forEach((item) => {
    item["disabled"] = targetKeys.includes(item.key);
    if (item.children) {
      handleTreeData(item.children, targetKeys);
    }
  });
  return data;
}

const noticeList = [
  {
    name: "国华科技有限公司2021年度总结",
    time: "2021-4-12",
  },
  {
    name: "招投标近期计划",
    time: "2021-4-12",
  },
  {
    name: "合同制定的条款规定与规格的制定",
    time: "2021-4-12",
  },
];
import { requestHndleMattersGetRequest } from "@/services/process/workflow";
const bidColumns = [
  {
    title: "招标编号",
    dataIndex: "oddNumber",
    scopedSlots: { customRender: "oddNumber" },
  },
  {
    title: "招标名称",
    dataIndex: "title",
    scopedSlots: { customRender: "title" },
  },
]
const paidColumns = [
  {
    title: "竞价编号",
    dataIndex: "oddNumber",
    scopedSlots: { customRender: "oddNumber" },
  },
  {
    title: "竞价名称",
    dataIndex: "title",
    scopedSlots: { customRender: "title" },
  },
]
export default {
  name: "WorkPlace",
  components: { PageLayout, RankingList, BackLogs },
  i18n: require("./i18n"),
  data () {
    return {
      bidColumns,
      paidColumns,
      noticeList,
      targetKeys: [],
      dataSource: transferDataSource,
      show: false,
      rankList: [],
      projects: [
        {
          desc: "询比价管理",
          logo: require("@/assets/img/home/组 455@2x.png"),
        },
        {
          desc: "招投标管理",
          logo: require("@/assets/img/home/组 456@2x.png"),
        },
        {
          desc: "竞价管理",
          logo: require("@/assets/img/home/组 457@2x.png"),
        },
        {
          desc: "供应商管理",
          logo: require("@/assets/img/home/组 458@2x.png"),
        },
        {
          desc: "合同管理",
          logo: require("@/assets/img/home/组 459@2x.png"),
        },
        {
          desc: "供应商门户",
          logo: require("@/assets/img/home/组 460@2x.png"),
        },
        {
          desc: "基础管理",
          logo: require("@/assets/img/home/组 461@2x.png"),
        },
        {
          desc: "系统设置",
          logo: require("@/assets/img/home/组 462@2x.png"),
        },
      ],
      loading: true,
      activities: [],
      alreadyList: [],
      teams: [],
      welcome: {
        timeFix: "",
        message: "",
      },
      paidList: [],
      bidList: [],
      paidLoading: false,
      bidLoading: false,
      bidPagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      paidPagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
    };
  },
  computed: {
    ...mapGetters("account", ["user"]),
    ...mapState("setting", ["lang"]),
    treeData () {
      return handleTreeData(treeData, this.targetKeys);
    },
  },
  created () {
    // this.getMattersAll();
    // this.getalreadyAll();
    this.getPaidDataList()
    this.getBidDataList()
  },
  methods: {
    // 跳转招标详情
    handleBidEdit (row) {
      sessionStorage.removeItem('addBiddingkeep')
      let form = Object.assign({}, row)
      form.bidEndTime = moment(parseInt(form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss')
      form.qualificationTender = form.qualificationTender === 1
      form.technicalTender = form.technicalTender === 1
      form.items = form.items ? form.items.split(',') : []
      sessionStorage.setItem('biddingForm', JSON.stringify(form))
      this.$router.push({ name: '招标详情' })
    },
    // 查询招标单数据
    async getBidDataList () {
      this.bidLoading = true
      const data = {
        userId: this.user.id,
        currentPage: this.bidPagination.current,
        pageSize: this.bidPagination.pageSize
      }
      const result = await biddingGetRequest(data)
      if (result.code === 0) {
        this.bidLoading = false
        this.bidList = result.data.records
        this.bidPagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 分页改变
    bidTableChange (row) {
      this.bidPagination = row
      this.getBidDataList()
    },
    // 跳转竞价单详情
    handlePaidEdit (row) {
      sessionStorage.removeItem('addContendkeep')
      let form = Object.assign({}, row)
      form.offerStartTime = moment(parseInt(form.offerStartTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      )
      form.offerEndTime = moment(parseInt(form.offerEndTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      )
      sessionStorage.setItem("contendForm", JSON.stringify(form))
      sessionStorage.setItem("contendType", "edit")
      this.$router.push({ name: "竞价详情" })
    },
    // 分页改变
    paidTableChange (row) {
      this.paidPagination = row
      this.getPaidDataList()
    },
    // 查询竞价单列表数据
    async getPaidDataList () {
      this.paidLoading = true
      const data = {
        userId: this.user.id,
        currentPage: this.paidPagination.current,
        pageSize: this.paidPagination.pageSize
      }
      const result = await paidSheetGetRequest(data)
      if (result.code === 0) {
        this.paidLoading = false
        this.paidList = result.data.records
        this.paidPagination.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 请求代办事项
    getMattersAll () {
      requestHndleMattersGetRequest({
        currentPage: -1,
        handleOption: "2",
        userId: this.user.id,
      }).then((response) => {
        this.rankList = response.data;
      });
    },
    getalreadyAll () {
      requestHndleMattersGetRequest({
        currentPage: -1,
        handleOption: "3",
        userId: this.user.id,
      }).then((response) => {
        this.alreadyList = response.data;
      });
    },
    onChange (targetKeys) {
      console.log("Target Keys:", targetKeys);
      this.targetKeys = targetKeys;
    },
    onChecked (_, e, checkedKeys, itemSelect) {
      const { eventKey } = e.node;
      itemSelect(eventKey, !isChecked(checkedKeys, eventKey));
    },
    menuShow () {
      this.show = true;
    },
    handleOk () {
      this.show = false;
    },
  },
};
</script>

<style lang="less">
@import 'index';
.moreStyle {
  position: absolute;
  top: 15px;
  right: 20px;
  cursor: pointer;
}
.fontClass {
  font-weight: 550;
}
.content-title {
  font-size: 22px;
  margin-bottom: 10px;
}
</style>
