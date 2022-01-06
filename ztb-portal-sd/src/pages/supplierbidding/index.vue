<template>
  <div>
    <a-card class="card-top">
      <div>
        <a-form-model
          :model="formInline"
          :labelCol="{ span: 5 }"
          :wrapperCol="{ span: 18, offset: 0 }"
        >
          <a-row>
            <a-col :span="6">
              <a-form-model-item label="编号">
                <a-input
                  v-model="formInline.oddNumber"
                  placeholder="请输入编号"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="6">
              <a-form-model-item label="关键字">
                <a-input
                  v-model="formInline.keyword"
                  placeholder="请输入关键字"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="12" align="right">
              <a-button type="primary" @click="getDataList">查询</a-button>
            </a-col>
          </a-row>
        </a-form-model>
      </div>
    </a-card>
    <a-card>
      <a-row>
        <a-table
          rowKey="id"
          :pagination="pagination"
          :columns="columns"
          :data-source="dataSource"
          @change="tableChange"
        >
          <template slot="oddNumber" slot-scope="text, record">
            <a @click="handleAdd(record)">{{ text }}</a>
          </template>
          <template slot="monitor" slot-scope="text, record">
            <a @click="seebiddingmonitor(record)">
              <!-- <a-icon type="fund" style="font-size: 20px"/> -->
              <img alt="svg" src="@/assets/img/bidding.svg" width="20px" />
            </a>
          </template>
        </a-table>
      </a-row>
    </a-card>
  </div>
</template>

<script>
import { paidSheetVendorGetRequest } from "@/services/source/contend";
import { mapGetters } from "vuex";
import moment from "moment";
export default {
  name: "supplierbidding",
  components: {},
  computed: {
    ...mapGetters("account", ["user"]),
  },
  data () {
    return {
      formInline: {
        keyword: "",
        oddNumber: "",
      },
      columns: [
        {
          title: "竞价编号",
          dataIndex: "oddNumber",
          width: 120,
          scopedSlots: { customRender: "oddNumber" },
        },
        {
          title: "竞价名称",
          width: 80,
          dataIndex: "title",
          scopedSlots: { customRender: "title" },
        },
        {
          title: "竞价大厅",
          width: 80,
          dataIndex: "monitor",
          scopedSlots: { customRender: "monitor" },
        },
        {
          title: "竞价状态",
          width: 100,
          dataIndex: "requestStatusName",
        },
        {
          title: "项目名称",
          width: 80,
          dataIndex: "projectSheetVO",
          key: "projectTitle",
          customRender: (text, row, index) => {
            return text ? text.title : "";
          },
        },
        {
          title: "项目代码",
          width: 80,
          dataIndex: "projectSheetVO",
          key: "projectCode",
          customRender: (text, row, index) => {
            return text ? text.code : "";
          },
        },
        {
          title: "总额/多项",
          width: 80,
          dataIndex: "bidStructure",
        },
        // {
        //   title: "买方企业",
        //   width: 80,
        //   dataIndex: "bidStructure",
        // },
        {
          title: "竞价开始时间",
          width: 120,
          dataIndex: "offerStartTime",
          customRender: (text, row, index) => {
            return text
              ? moment(parseInt(text)).format("YYYY-MM-DD HH:mm:ss")
              : "";
          },
        },
        {
          title: "竞价结束时间",
          width: 120,
          dataIndex: "offerEndTime",
          customRender: (text, row, index) => {
            return text
              ? moment(parseInt(text)).format("YYYY-MM-DD HH:mm:ss")
              : "";
          },
        },
        // {
        //   title: " 创建人",
        //   width: 100,
        //   dataIndex: "createdUser",
        // },
        // {
        //   title: "创建时间",
        //   width: 120,
        //   dataIndex: "createdTime",
        //   customRender: (text, row, index) => {
        //     return text ? moment(parseInt(text)).format('YYYY-MM-DD') : ''
        //   }
        // },
      ],
      dataSource: [],
      stateList: [],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
    };
  },
  created () { },
  mounted () {
    this.getDataList();
  },
  methods: {
    // 查询竞价单信息
    async getDataList () {
      const data = {
        keyword: this.formInline.keyword,
        oddNumber: this.formInline.oddNumber,
        vendorUserId: this.user.id,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
      };
      // if(data.keyword==""){
      //     delete data.keyword
      // }
      const result = await paidSheetVendorGetRequest(data);
      if (result.code === 0) {
        this.dataSource = result.data.records;
        this.pagination.total = result.data.total;
      } else {
        this.$message.error(result.message);
      }
    },
    // 创建
    handleAdd (row) {
      let form = Object.assign({}, row);
      form.offerStartTime = moment(parseInt(form.offerStartTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      );
      form.offerEndTime = moment(parseInt(form.offerEndTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      );
      sessionStorage.setItem("contendForm", JSON.stringify(form));
      this.$router.push({ path: "seesupplierbidding" });
    },
    // 查看竞价器
    seebiddingmonitor (row) {
      let form = Object.assign({}, row);
      form.offerStartTime = moment(parseInt(form.offerStartTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      );
      form.offerEndTime = moment(parseInt(form.offerEndTime)).format(
        "YYYY-MM-DD HH:mm:ss"
      );
      const startTime = moment(form.offerStartTime)
      const nowTime = moment(new Date().getTime())
      const minute = startTime.diff(nowTime, 'minute')
      if (form.requestStatusName === '待竞价' && startTime > nowTime && minute >= 10) {
        this.$message.warning('还未到达竞价时间')
        return
      }
      sessionStorage.setItem("biddingData", JSON.stringify(form));
      this.$router.push({ path: "supplierbiddingmonitor" });
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
    },
  },
};
</script>

<style lang="less" scoped></style>
