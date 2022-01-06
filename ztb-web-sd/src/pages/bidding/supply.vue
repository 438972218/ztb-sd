<template>
  <div>
    <a-card class="card-top">
      <!-- <div :class="advanced ? 'search' : null"> -->
      <div>
        <a-form layout="horizontal">
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
            <!-- <a-row v-if="advanced">
         
          <a-col :md="8" :sm="24" >
            <a-form-item
              label="使用状态"
              :labelCol="{span: 5}"
              :wrapperCol="{span: 18, offset: 1}"
            >
              <a-select placeholder="请选择">
                <a-select-option value="1">关闭</a-select-option>
                <a-select-option value="2">运行中</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24" >
            <a-form-item
              label="描述"
              :labelCol="{span: 5}"
              :wrapperCol="{span: 18, offset: 1}"
            >
              <a-input placeholder="请输入" />
            </a-form-item>
          </a-col>
        </a-row> -->
          </div>
          <!-- <span style="float: right; margin-top: 3px;">
          <a-button type="primary">查询</a-button>
          <a-button style="margin-left: 8px">重置</a-button>
          <a @click="toggleAdvanced" style="margin-left: 8px">
            {{advanced ? '收起' : '展开'}}
            <a-icon :type="advanced ? 'up' : 'down'" />
          </a>
        </span> -->
        </a-form>
      </div>
    </a-card>
    <a-card>
      <div>
        <div class="operator"></div>
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
          <template slot="createdTime" slot-scope="scope">
            <span>
              {{ $moment(Number(scope.text)).format('YYYY-MM-DD') }}
            </span>
          </template>
          <template slot="bidEndTime" slot-scope="scope">
            <span>
              {{ $moment(Number(scope.text)).format('YYYY-MM-DD') }}
            </span>
          </template>
          <template slot="index" slot-scope="scope">
            <span>
              {{ scope.index + 1 }}
            </span>
          </template>
          <template slot="oddNumber" slot-scope="scope">
            <span
              @click="addNew(scope.record, scope.record.vendorStatus)"
              class="text-cursor"
            >
              {{ scope.text }}
            </span>
          </template>
          <!-- <template slot="operation" slot-scope="scope">
            <span
              @click="joinStateChange(scope.record, '已参与')"
              v-if="scope.record.vendorStatus === '待参与' && scope.record.preQualification=== 1"
              class="operation-style"
            >
              参与
            </span>
                 <span
              @click="joinStateChange(scope.record, '待投标')"
              v-if="scope.record.vendorStatus === '待参与' && scope.record.preQualification=== 0"
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
              @click="addNew(scope.record, scope.record.vendorStatus)"
              v-if="scope.record.vendorStatus === '待投标'"
              class="operation-style"
            >
              投标
            </span>
            <span
              @click="addNew(scope.record, scope.record.vendorStatus)"
              v-if="scope.record.vendorStatus === '已参与'"
              class="operation-style"
            >
              申请
            </span>
            <span
              @click="addNew(scope.record, scope.record.vendorStatus)"
              v-if="scope.record.vendorStatus === '开标中'"
              class="operation-style"
            >
              投标
            </span>
          </template> -->
        </advance-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import AdvanceTable from "@/components/table/advance/AdvanceTable";
import { getTableList, conditionals } from "@/utils/methods";
import {
  bidSheetVendorGetRequest,
  bidVendorStatusPutRequest,
} from "@/services/source/bidding";
import { mapGetters } from "vuex";

export default {
  name: "Supply",
  components: { AdvanceTable },
  data () {
    return {
      loading: false,
      page: 1,
      pageSize: 10,
      total: 0,
      advanced: true,
      columns: [
        {
          title: "招标单号",
          dataIndex: "oddNumber",
          scopedSlots: { customRender: "oddNumber" },
        },
        // {
        //   title: "招标单标题",
        //   dataIndex: "requestTitle",
        //   width: 200,
        // },
        {
          title: "招标事项",
          dataIndex: "title",
        },
        // {
        //   title: "资格预审",
        //   dataIndex: "preQualification",
        // },
        // {
        //   title: "轮次",
        //   dataIndex: "round",
        // },
        // {
        //   title: "版本",
        //   dataIndex: "versions",
        // },
        {
          title: "招标单类型",
          dataIndex: "sheetType",
        },
        {
          title: "招标方式",
          dataIndex: "tenderMode",
        },
        {
          title: "发布时间",
          dataIndex: "createdTime",
          scopedSlots: { customRender: "createdTime" },
        },
        {
          title: "采购组织",
          dataIndex: "purchasingOrganization",
        },
        {
          title: "投标截止时间",
          dataIndex: "bidEndTime",
          scopedSlots: { customRender: "bidEndTime" },
        },
        {
          title: "状态",
          dataIndex: "vendorStatus",
          scopedSlots: { customRender: "vendorStatus" },
        },
      ],
      dataSource: [],
      selectedRows: [],
      form: {
        name: "",
      },
    };
  },

  created () {
    let list = getTableList(this.columns);
    if (list) {
      this.columns = list;
    }
    this.getAll();
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  methods: {
    getAll () {
      bidSheetVendorGetRequest({
        currentPage: "-1",
        vendorCode: this.user.userName,
        userId: this.user.id,
      }).then((response) => {
        // if(conditionals(response)){
        this.dataSource = response.data.records;
        // }
      });
    },
    addShow () { },
    c_release () {
      this.$confirm({
        title: "警告",
        content: "你确定要发布吗？",
        centered: true,
        onOk () {
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel () { },
      });
    },

    c_delete () {
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk () {
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel () { },
      });
    },
    toggleAdvanced () {
      this.advanced = !this.advanced;
    },
    remove () {
      this.dataSource = this.dataSource.filter(
        (item) =>
          this.selectedRows.findIndex((row) => row.key === item.key) === -1
      );
      this.selectedRows = [];
    },
    addNew (data, type) {
      this.$router.push({
        path: "/portal/addsupply",
      });
      this.$store.state.account.biddingVendorType = type;
      this.$store.state.account.biddingVendorData = data;
    },
    // table所有方法
    getGoodList () {
      this.loading = true;
    },
    onSearch (conditions, searchOptions) {
      this.page = 1;
      this.conditions = conditions;
      this.getTypesAll();
    },
    onSizeChange (current, size) {
      this.page = 1;
      this.pageSize = size;
      this.getTypesAll();
    },
    onRefresh (conditions) {
      this.conditions = conditions;
      this.getTypesAll();
    },
    onReset (conditions) {
      this.conditions = conditions;
      this.getTypesAll();
    },
    onPageChange (page, pageSize) {
      this.page = page;
      this.pageSize = pageSize;
      this.getTypesAll();
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
.operator {
  margin: 5px 0 18px 0;
}
@media screen and (max-width: 900px) {
  .fold {
    width: 100%;
  }
}
.operation-style {
  margin: 0 5px;
  color: #1890ff;
  cursor: pointer;
}
</style>
