<template>
  <div>
    <a-card class="card-top">
      <!-- <div :class="advanced ? 'search' : null"> -->
      <div>
      <a-form-model
          layout="horizontal"
          :model="queryFrom"
          ref="rulequeryFrom"
        >
          <!-- <div :class="advanced ? null: 'fold'"> -->
          <div>
            <a-row>
              <a-col :sm="24" :md="10" :xl="5" :offset="1">
                <a-form-model-item
                  style="height: 20px"
                  label=""
                  :labelCol="{ span: 2 }"
                  :wrapperCol="{ span: 19 }"
                >
                  <a-input allowClear placeholder="关键词" />
                </a-form-model-item>
              </a-col>
              <a-col
                style="margin-bottom: 10px; text-align: center"
                :sm="24"
                :md="10"
                :offset="15"
                :xl="3"
              >
                <span>
                  <a-button
                    type="primary"
                    @click="getAll()"
                    style="margin: 3px 10px 0"
                    >查询</a-button
                  >
                </span>
              </a-col>
            </a-row>
          </div>
        </a-form-model>
      </div>
    </a-card>
    <a-card>
      <div>
        <advance-table
          :columns="columns"
          :data-source="dataSource"
          :loading="loading"
          rowKey="id"
          @refresh="onRefresh"
          :format-conditions="true"
          @reset="onReset"
          :pagination="{
            current: page,
            pageSize: pageSize,
            total: total,
            showSizeChanger: true,
            showLessItems: true,
            showQuickJumper: true,
            onChange: onPageChange,
            onShowSizeChange: onSizeChange,
          }"
        >
          <template slot="index" slot-scope="scope">
            <span>
              {{ scope.index + 1 }}
            </span>
          </template>
          <template slot="quotationDeadline" slot-scope="scope">
            <span>
              {{ $moment(Number(scope.text)).format("YYYY-MM-DD") }}
            </span>
          </template>
                     <template slot="oddNumber" slot-scope="scope">
            <span  @click="addNew(scope.record)" class="text-cursor">
              {{ scope.text }}
            </span>
          </template>
          <!-- <template slot="a15" slot-scope="scope">
            <span>
              {{
                scope.record.a15 === "1"
                  ? "尚未参与"
                  : scope.record.a15 === "2"
                  ? "已放弃"
                  : scope.record.a15 === "1"
                  ? "尚未参与"
                  : scope.record.a15 === "3"
                  ? "参与"
                  : scope.record.a15 === "3"
                  ? "参与"
                  : "无效"
              }}
            </span>
          </template> -->
          <template slot="a0" slot-scope="scope">
            <span @click="addNew(4)">
              {{
                scope.record.a0 === "11"
                  ? "未报价"
                  : scope.record.a0 === "12"
                  ? "报价中"
                  : scope.record.a0 === "13"
                  ? "已结束"
                  : scope.record.a0 === "14"
                  ? "报价中"
                  : scope.record.a0 === "15"
                  ? "已报价"
                  : "已暂停"
              }}
            </span>
          </template>
          <template slot="a1" slot-scope="scope">
            <span @click="addNew(4)" class="text-cursor">
              {{ scope.text }}
            </span>
          </template>

          <!-- <template slot="operation" slot-scope="scope">
            <span
              @click="joinStateChange(scope.record, '已参与')"
              v-if="scope.record.vendorStatus === '待参与'"
              class="operation-style"
            >
              参与
            </span>
            <span
              @click="joinStateChange(scope.record, '已放弃')"
              v-if="scope.record.vendorStatus === '待参与'"
              class="operation-style"
            >
              放弃
            </span>

            <span
              @click="addNew(scope.record)"
              v-if="
                scope.record.vendorStatus === '已参与' ||
                scope.record.vendorStatus === '未报价' ||
                scope.record.vendorStatus === '还价保存'
              "
              class="operation-style"
            >
              报价
            </span>
          </template> -->
        </advance-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import AdvanceTable from "@/components/table/advance/AdvanceTable";
import { getTableList ,conditionals} from "@/utils/methods";
import {
  inquirySheetVendorGetRequest,
} from "@/services/source/find";
import { mapGetters } from "vuex";

export default {
  name: "supplier",
  components: { AdvanceTable },
  data() {
    return {
      loading: false,
      page: 1,
      pageSize: 10,
      total: 0,
      num: String,
      advanced: true,
      columns: [
        {
          title: "询价单号",
          dataIndex: "oddNumber",
          scopedSlots: { customRender: "oddNumber" },
        },

        {
          title: "询价描述",
          dataIndex: "title",
        },
        {
          title: "报价截止时间",
          dataIndex: "quotationDeadline",
          scopedSlots: { customRender: "quotationDeadline" },
        },
        {
          title: "采购组织名称",
          dataIndex: "purchasingOrganization",
        },
        // {
        //   title: "公司代码",
        //   dataIndex: "companyCode",
        // },

        {
          title: "询价单类型",
          dataIndex: "inquirySheetType",
        },
        // {
        //   title: "密封报价",
        //   dataIndex: "a9",
        //   align: "center",
        // },
        {
          title: "币种",
          dataIndex: "currency",
        },
        {
          title: "询价方式",
          dataIndex: "inquiryMode",
        },
        // {
        //   title: "状态",
        //   dataIndex: "status",
        //   align: "center",
        //   scopedSlots: { customRender: "status" },
        // },vendorStatus
        // {
        //   title: "状态",
        //   dataIndex: "requestStatusName",
        //   align: "center",
        // },
        {
          title: "参与状态",
          dataIndex: "vendorStatus",
        },
        // {
        //   title: "操作",
        //   key: "gender",
        //   scopedSlots: { customRender: "operation" },
        // },
      ],
      dataSource: [],
      selectedRows: [],
      form: {
        name: "",
      },
    };
  },
    computed: {
    ...mapGetters("account", ["user"]),
  },
  created() {
    let list = getTableList(this.columns);
    if (list) {
      this.columns = list;
    }
    this.getAll();
    // this.getGoodList();
    // this.getColumns();
    // this.getrolesTypeAll()
  },
  methods: {
    // 1413301853475696641
    // id:'1413298924589019138',
    getAll() {
      inquirySheetVendorGetRequest({
        currentPage: "-1",
         vendorCode: this.user.code,
      }).then((response) => {
        // if(conditionals(response)){
        this.dataSource = response.data.records;
        // }
      });
    },
    // // 修改供应商状态
    // joinStateChange(data, state) {
    //   let that = this;
    //   this.$confirm({
    //     title: "警告",
    //     content: "是否确定此操作吗？",
    //     centered: true,
    //     onOk() {
    //       inquiryVendorPutRequest({
    //         inquirySheetId: data.id,
    //         vendorCode: this.user.userName,
    //         vendorStatus: state,
    //         deleted: 0,
    //       }).then((response) => {
    //         if (response.code === 0) {
    //           that.$message.success("操作成功");
    //           that.getAll();
    //         } else {
    //           that.$message.error(response.message);
    //         }
    //       });
    //     },
    //     onCancel() {},
    //   });
    // },
    onChange(checked) {},
    // 查看详情
    addNew(obj) {
      this.$router.push({
        path: "/portal/addsupplierfind",
      });
      this.$store.state.account.findSupplierData = obj;
      this.$store.state.account.findSupplierType = obj.vendorStatus;
    },
    // table所有方法
    getGoodList() {
      this.loading = true;
    },
    onSearch(conditions, searchOptions) {
      console.log(searchOptions);
      this.page = 1;
      this.conditions = conditions;
      this.getAll();
    },
    onSizeChange(current, size) {
      this.page = 1;
      this.pageSize = size;
      this.getAll();
    },
    onRefresh(conditions) {
      this.conditions = conditions;
      this.getAll();
    },
    onReset(conditions) {
      this.conditions = conditions;
      this.getAll();
    },
    onPageChange(page, pageSize) {
      this.page = page;
      this.pageSize = pageSize;
      this.getAll();
    },
  },
};
</script>

<style lang="less" scoped>
.search {
  margin-bottom: 54px;
}
.fold {
  width: calc(100% - 216px);
  display: inline-block;
}
@media screen and (max-width: 900px) {
  .fold {
    width: 100%;
  }
}
</style>
