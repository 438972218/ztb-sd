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
          <a-button @click="addContend('add')" type="primary"
            >创建竞价单</a-button
          >
          <a-button @click="pushHall()" style="margin-left: 20px" type="primary"
            >竞价大厅</a-button
          >
        </div>
        <advance-table
          :columns="columns"
          :data-source="dataSource"
          :loading="loading"
          rowKey="id"
          size="middle"
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
          <template slot="offerStartTime" slot-scope="scope">
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
          <!-- <template slot="operation" slot-scope="scope">
            <span
              @click="editBiddng(scope.record, scope.record.requestStatusName)"
              v-if="scope.record.requestStatusName === '待审核'"
              class="operation-style"
            >
              审批
            </span>
            <span
              @click="editBiddng(scope.record, scope.record.requestStatusName)"
              v-if="scope.record.requestStatusName === '待发布'"
              class="operation-style"
            >
              发布
            </span>
            <span
              @click="editBiddng(scope.record, scope.record.requestStatusName)"
              v-if="scope.record.requestStatusName === '已发布'"
              class="operation-style"
            >
              价格初审
            </span>
            <span
              @click="editBiddng(scope.record, scope.record.requestStatusName)"
              v-if="scope.record.requestStatusName === '待价格初审'"
              class="operation-style"
            >
              核价审批
            </span>
            <span
              @click="editBiddng(scope.record, scope.record.requestStatusName)"
              v-if="scope.record.requestStatusName === '待采购核价'"
              class="operation-style"
            >
              分配数量
            </span>
            <span
              @click="editBiddng(scope.record, scope.record.requestStatusName)"
              v-if="scope.record.requestStatusName === '待核价审批'"
              class="operation-style"
            >
              竞价结果审核
            </span>
            <span
              @click="editBiddng(scope.record, 'edit')"
              class="operation-style"
            >
              编辑
            </span>
            <span @click="c_delete(scope.record.id)" class="operation-style">
              删除
            </span>
          </template> -->
        </advance-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import AdvanceTable from "@/components/table/advance/AdvanceTable";
import {
  getTableList,
  queryDictionarys,
  treeorganization,
} from "@/utils/methods";
import {
  paidSheetGetRequest,
  paidSheetDeleteRequest,
  paidSheetShowByRequestIdGetRequest,
} from "@/services/source/contend";
import { getPurchaseOrzTree } from "@/services/structure/index";

export default {
  name: "Find",
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
      loading: false,
      page: 1,
      pageSize: 10,
      total: 0,
      num: String,
      advanced: true,
      columns: [
        {
          title: "竞价单号",
          dataIndex: "oddNumber",
          width: 160,
          scopedSlots: { customRender: "oddNumber" },
        },
        {
          title: "状态",
          dataIndex: "requestStatusName",
          width: 80,
          scopedSlots: { customRender: "requestStatusName" },
        },
        {
          title: "竞价单类型",
          dataIndex: "paidType",
          width: 120,
        },
        {
          title: "竞价单描述",
          ellipsis: true,
          dataIndex: "title",
          width: 180,
        },
        {
          title: "标的类型",
          width: 90,
          dataIndex: "bidType",
        },
        {
          title: "竞价方式",
          width: 150,
          dataIndex: "paidMode",
        },
        {
          title: "竞价方向",
          width: 150,
          dataIndex: "paidDirection",
        },
        {
          title: "采购组织",
          width: 150,
          dataIndex: "purchasingOrganization",
        },
        {
          title: "报价开始时间",
          width: 150,
          dataIndex: "offerStartTime",
          scopedSlots: { customRender: "offerStartTime" },
        },

        {
          title: "创建人",
          width: 150,
          dataIndex: "createdUser",
        },
        {
          title: "创建时间",
          width: 150,
          dataIndex: "createdTime",
          scopedSlots: { customRender: "createdTime" },
        },

        {
          title: "公司代码",
          width: 150,
          dataIndex: "companyCode",
        },
      ],
      dataSource: [],
      selectedRows: [],
      TreeList: [],
      typeList: [],
      signTypeList: [],
      form: {
        name: "",
      },
    };
  },
  computed: {},
  created() {
    this.getAll();
    this.getTreeList();
    let list = getTableList(this.columns);
    queryDictionarys("signType").then((res) => {
      this.signTypeList = res;
    });
    if (list) {
      this.columns = list;
    }
  },
  methods: {
    getAll() {
      paidSheetGetRequest({
        ...this.queryFrom,
      }).then((response) => {
        if (response.data) {
          this.total = response.data.total;
          this.dataSource = response.data.records;
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
          paidSheetDeleteRequest(id).then((response) => {
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
    editBiddng(record, num) {
      this.getAllContendType(record.requestId, num);
    },
    // 查询数据
    getAllContendType(id, num) {
      paidSheetShowByRequestIdGetRequest(id).then((response) => {
        if (response.data) {
          this.$store.state.account.contendType = num;
          this.$store.state.account.contendData = response.data;
          this.$router.push({
            path: "/findmanage/addcontend",
          });
        }
      });
    },
    pushHall() {
      this.$router.push({
        path: "/findmanage/hall",
      });
    },
    addNew(num) {
      this.$router.push({
        path: "/findmanage/addcontend",
      });
      // this.$store.state.account.contendData = num;
    },
    addContend(type) {
      this.$router.push({
        path: "/findmanage/addcontend",
      });
      this.$store.state.account.contendType = type;
    },
    // getGoodList() {
    //   this.loading = true;
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
  margin: 0 5px;
  color: #1890ff;
  cursor: pointer;
}
</style>
