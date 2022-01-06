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
              <a-col :offset="1" :sm="24" :md="12" :xl="5">
                <a-form-model-item
                  prop="purchasingOrganization"
                  style="height: 20px"
                  :labelCol="{ span: 2 }"
                  :wrapperCol="{ span: 19 }"
                >
                  <!-- <a-input placeholder="选择采购组织"/> -->
                  <a-tree-select
                    allowClear
                    v-model="queryFrom.purchasingOrganization"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    :tree-data="TreeList"
                    tree-default-expand-all
                    placeholder="选择采购组织"
                  >
                    <span
                      v-if="key === '0-0-1'"
                      slot="title"
                      slot-scope="{ key, value }"
                      style="color: #08c"
                    >
                      {{ value }}
                    </span>
                  </a-tree-select>
                </a-form-model-item>
              </a-col>
              <a-col :sm="24" :md="12" :xl="5">
                <a-form-model-item
                  prop="signType"
                  style="height: 20px"
                  :labelCol="{ span: 2 }"
                  :wrapperCol="{ span: 19 }"
                >
                  <a-select
                    allowClear
                    v-model="queryFrom.signType"
                    placeholder="请选择标的类型"
                  >
                    <a-select-option
                      v-for="item in signTypeList"
                      :key="item.id"
                      :value="item.meaning"
                      >{{ item.meaning }}</a-select-option
                    >
                  </a-select>
                </a-form-model-item>
              </a-col>

              <a-col :sm="24" :md="12" :xl="5">
                <a-form-model-item
                  prop="requestStatusName"
                  style="height: 20px"
                  :labelCol="{ span: 2 }"
                  :wrapperCol="{ span: 19 }"
                >
                  <a-select
                    allowClear
                    v-model="queryFrom.requestStatusName"
                    placeholder="选择状态"
                  >
                    <a-select-option
                      v-for="item in typeList"
                      :key="item"
                      :value="item"
                      >{{ item }}</a-select-option
                    >
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :sm="24" :md="10" :xl="5">
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
                :offset="0"
                :xl="3"
              >
                <span>
                  <a-button
                    type="primary"
                    @click="getAllQuery()"
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
        <div class="operator table-change">
          <!-- <a-icon type="plus-circle" /> -->
          <a-button type="primary"  @click="addBiddng('add')"
            >新建</a-button
          >
          <!-- <a-button
            @click="addNew('append')"
            type="primary"
            style="margin-left: 10px"
            >新增招标单</a-button
          >
          <a-button type="primary" style="margin-left: 10px"
            >复用招标单</a-button
          > -->
        </div>

        <!-- @refresh="onRefresh"
          @reset="onReset" -->
        <advance-table
          :columns="columns"
          :data-source="dataSource"
          :loading="loading"
          size="middle"
          rowKey="id"
          :scroll="{ x: 1300 }"
          :format-conditions="true"
            @reset="onReset"
            @refresh="onRefresh"
          :pagination="{
            current: queryFrom.currentPage,
            pageSize: queryFrom.pageSize,
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
          <template slot="oddNumber" slot-scope="scope">
            <span
              @click="editBiddng(scope.record, scope.record.requestStatusName)"
              class="text-cursor"
            >
              {{ scope.text }}
            </span>
          </template>
          <template slot="openBidTime" slot-scope="scope">
            <span>
              {{ $moment(Number(scope.text)).format("YYYY-MM-DD") }}
            </span>
          </template>
          <template slot="createdTime" slot-scope="scope">
            <span>
              {{ $moment(Number(scope.text)).format("YYYY-MM-DD") }}
            </span>
          </template>
          <template slot="requestStatusName" slot-scope="scope">
            <span>
              {{ scope.record.requestStatusName }}
            </span>
          </template>
          <template slot="a14" slot-scope="scope">
            <span class="text-cursor">
              <a-switch
                :checked="scope.record.a14"
                default-checked
                @change="onChange"
              />
            </span>
          </template>
          <!-- <template slot="operation" slot-scope="scope"> -->
          <!-- <span @click="c_delete(scope.record.id)" class="operation-style">
              删除
            </span> -->
          <!-- </template> -->
        </advance-table>
      </div>
    </a-card>
    <a-modal
      v-model="visible"
      width="500px"
      :footer="null"
      :centered="true"
      title="开标密码"
    >
      <a-form-model layout="horizontal" class="page-form">
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row>
          <a-form-model-item
            label="输入开标密码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-col :span="24" class="btn-sty" style="margin-bottom: 20px">
            <a-button type="primary"> 确认开标 </a-button>
            <a-button type="primary"> 重发密码 </a-button>
            <a-button type="primary"> 返回 </a-button>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>


<script>
import AdvanceTable from "@/components/table/advance/AdvanceTable";
import {
  getTableList,
  treeorganization,
  queryDictionarys,
} from "@/utils/methods";
import { getPurchaseOrzTree } from "@/services/structure/index";
import {
  biddingGetRequest,
  bidInvitationDeleteRequest,
  bidSheetshowByRequestIdGetRequest,
} from "@/services/source/bidding";

export default {
  name: "bidding",
  components: { AdvanceTable },
  data() {
    return {
      queryFrom: {
        purchasingOrganization: undefined,
        signType: undefined,
        requestStatusName: undefined,
        currentPage: 1,
        pageSize: 10,
        deleted: 0,
      },
      total: 0,
      visible: false,
      loading: false,
      page: 1,
      pageSize: 10,
      num: String,
      advanced: true,
      columns: [
        {
          title: "招标单号",
          dataIndex: "oddNumber",
          width: 150,
          scopedSlots: { customRender: "oddNumber" },
        },
        {
          title: "状态",
          width: 80,
          dataIndex: "requestStatusName",
          scopedSlots: { customRender: "requestStatusName" },
        },
        {
          title: "招标事项",
          width: 150,
          ellipsis: true,
          dataIndex: "title",
        },
        {
          title: "投标类型",
          width: 80,
          dataIndex: "tenderType",
        },
        {
          title: "标的类型",
          width: 80,
          dataIndex: "signType",
        },
        {
          title: "招标方式",
          width: 90,
          dataIndex: "tenderMode",
        },
        {
          title: " 采购组织",
          width: 150,
          dataIndex: "purchasingOrganization",
        },
        {
          title: "开标时间",
          width: 120,
          dataIndex: "openBidTime",
          scopedSlots: { customRender: "openBidTime" },
        },
        {
          title: "创建时间",
          width: 120,
          dataIndex: "createdTime",
          scopedSlots: { customRender: "createdTime" },
        },
      ],
      dataSource: [],
      selectedRows: [],
      TreeList: [],
      signTypeList: [],
      typeList: [],
      form: {
        name: "",
      },
    };
  },
  computed: {},
  created() {
    this.getAll();
    this.getTreeList();
    queryDictionarys("signType").then((res) => {
      this.signTypeList = res;
    });
    let list = getTableList(this.columns);
    if (list) {
      this.columns = list;
    }
  },
  methods: {
    //请求列表
    getAll() {
      biddingGetRequest({
        ...this.queryFrom,
      }).then((response) => {
        this.total = response.data.total;
        this.dataSource = response.data.records;
        if (response.data.records) {
          this.getAllBiddingType(response.data.records[0].requestId);
        }
      });
    },
        getAllQuery() {
      this.queryFrom.pageSize = 10;
      this.queryFrom.currentPage = 1;
      this.getAll();
    },
    //请求采购组织
    getTreeList() {
      getPurchaseOrzTree({
        currentPage: "-1",
        deleted: 0,
      }).then((response) => {
        let list = JSON.parse(JSON.stringify(response.data));
        treeorganization(list);
        this.TreeList = list;
      });
    },
    //请求数据状态
    getAllBiddingType(id) {
      bidSheetshowByRequestIdGetRequest(id).then((response) => {
        if (response.data) {
          this.typeList = response.data.statusNames;
        }
      });
    },
    addShow() {
      this.visible = true;
    },
    c_release() {
      this.$confirm({
        title: "警告",
        content: "你确定要发布吗？",
        centered: true,
        onOk() {
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel() {},
      });
    },
    c_delete(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          bidInvitationDeleteRequest(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getAll();
          });
        },
        onCancel() {},
      });
    },
    addBiddng(num) {
      this.$router.push({
        path: "/findmanage/addbidding",
      });
      this.$store.state.account.biddingType = num;
    },
    editBiddng(record, num) {
      this.getAllBiddingData(record.requestId, num);
    },
    //请求数据状态
    getAllBiddingData(id,num) {
      bidSheetshowByRequestIdGetRequest(id).then((response) => {
        if (response.data) {
          let account = this.$store.state.account;
          account.biddingType = num;
          account.biddingData = response.data;
          this.$router.push({
            path: "/findmanage/addbidding",
          });
        }
      });
    },
    addNew(num) {
      this.$router.push({
        path: "/findmanage/addbidding",
      });
      this.$store.state.account.findType = num;
    },
    // getGoodList() {
    //   this.loading = true;
    // },
    // onSearch(conditions, searchOptions) {
    //   this.page = 1;
    //   this.conditions = conditions;
    //   this.getTypesAll();
    // },
    // 每页总条数变化
    onSizeChange(current, size) {
      this.queryFrom.pageSize = size;
      this.queryFrom.currentPage = current;
      this.getAll();
    },
        onPageChange(page, pageSize) {
     this.queryFrom.pageSize = pageSize;
      this.queryFrom.currentPage = page;
      this.getAll();
    },
        onRefresh() {
       this.getAllQuery();
    },
    onReset() {
      this.getAllQuery();
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
}
.btn-sty {
  text-align: center;
  button {
    margin-left: 10px;
  }
}
</style>
