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
        <div class="operator">
          <!-- <a-button @click="addNew(1)" type="primary">创建招标单</a-button> -->
          <!-- <a-dropdown>
          <a-menu @click="handleMenuClick" slot="overlay">
            <a-menu-item key="delete">删除</a-menu-item>
            <a-menu-item key="audit">审批</a-menu-item>
          </a-menu>
          <a-button>
            更多操作 <a-icon type="down" />
          </a-button>  

            :selectedRows.sync="selectedRows"
        @clear="onClear"
        @change="onChange"
        @selectedRowChange="onSelectChange"

        </a-dropdown> -->
        </div>
        <!-- <a-table
          :scroll="{ x: 1300 }"
          :columns="columns"
          :data-source="dataSource"
          row-key="a1"
        >
          <template slot="a1" slot-scope="text, record">
            <span v-if="record.a10 === '竞价'" @click="addNew(2)" class="text-cursor">
              {{ text }}
            </span>
             <span v-else @click="addNew(1)" class="text-cursor">
              {{ text }}
            </span>
          </template>
          <template slot="operation" slot-scope="text, record">
            <div v-if="record.a10 === '尚未参与' && record.requestStatusName === '未报价'">
              <a-button
                type="primary"
                size="small"
                icon="search"
                class="operation-style"
              >
                参与
              </a-button>
              <a-button
                type="danger"
                size="small"
                @click="addShow(3)"
                icon="search"
                class="operation-style"
              >
                放弃
              </a-button>
            </div>
            <a-button
              type="primary"
              v-if="record.a10 === '竞价'"
              size="small"
              @click="addNew(3)"
              icon="search"
              class="operation-style"
            >
              竞价
            </a-button>
          </template>
        </a-table> -->
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
                 <template slot="oddNumber" slot-scope="scope">
            <span
              @click="addNew(scope.record, scope.record.requestStatusName)"
              class="text-cursor"
            >
              {{ scope.text }}
            </span>
          </template>
          <!-- <template slot="paidNum" slot-scope="scope">
            <span class="text-cursor">
              {{ scope.text }}
            </span>
          </template> -->
          <template slot="offerEndTime" slot-scope="scope">
            <span>
              {{ $moment(Number(scope.text)).format("YYYY-MM-DD") }}
            </span>
          </template>
          <template slot="vendorStatus" slot-scope="scope">
            <span >
              {{
                scope.text
              }}
            </span>
          </template>
          <template slot="requestStatusName" slot-scope="scope">
            <span >
              {{
                scope.text
              }}
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
            <!-- <span
                  @click="joinStateChange(scope.record, '已参与')"
              v-if="scope.record.vendorStatus === '待参与' "
              class="operation-style"
            >
              参与
            </span>
            <span
                      @click="joinStateChange(scope.record, '已放弃')"
              v-if="scope.record.vendorStatus === '待参与' "
              class="operation-style"
            >
              放弃
            </span> -->
            <!-- <span
              @click="addNew(scope.record, scope.record.vendorStatus)"
              v-if="scope.record.vendorStatus === '已参与' || scope.record.vendorStatus === '已报价'"
              class="operation-style"
            >
              竞价
            </span>
            <span
              @click="addNew(scope.record.requestStatusName)"
              v-if="scope.record.requestStatusName === '4'"
              class="operation-style"
            >
              试竞价
            </span>
          </template> -->
        </advance-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import AdvanceTable from "@/components/table/advance/AdvanceTable";
import { getTableList,conditionals } from "@/utils/methods";
import { paidSheetVendorGetRequest,paidVendorStatusPutRequest } from "@/services/source/contend";
import { mapGetters } from "vuex";

export default {
  name: "Find",
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
          title: "竞价单号",
          dataIndex: "oddNumber",
          scopedSlots: { customRender: "oddNumber" },
        },
        // {
        //   title: "竞价单标题",
        //   dataIndex: "requestTitle",
        // },
          {
          title: "报价开始时间",
          dataIndex: "offerStartTime",
          scopedSlots: { customRender: "offerStartTime" },
        },
        {
          title: "报价截止时间",
          dataIndex: "offerEndTime",
          scopedSlots: { customRender: "offerEndTime" },
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
          title: "竞价方式",
          dataIndex: "paidMode",
        },
        {
          title: "竞价方向",
          dataIndex: "paidDirection",
        },
        // {
        //   title: "币种",
        //   dataIndex: "currency",
        // },
        {
          title: "状态",
          dataIndex: "vendorStatus",
          scopedSlots: { customRender: "vendorStatus" },
        },
        // {
        //   title: "状态",
        //   dataIndex: "requestStatusName",
        //   scopedSlots: { customRender: "requestStatusName" },
        // },

        // {
        //   title: "操作",
        //   dataIndex: "operation",
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
  },
  methods: {
    getAll() {
      paidSheetVendorGetRequest({
        currentPage: "-1",
        vendorCode: this.user.code,
      }).then((response) => {
        // if(conditionals(response)){
        this.dataSource = response.data.records;
        // }
      });
    },
            // 修改竞价单状态
    joinStateChange(data, state) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "是否确定此操作吗？",
        centered: true,
        onOk() {
          paidVendorStatusPutRequest({
            paidSheetId: data.id,
            vendorCode: this.user.userName,
            vendorStatus: state,
            deleted: 0,
          }).then((response) => {
            if (response.code === 0) {
              that.$message.success("操作成功");
              that.getAll();
            } else {
              that.$message.error(response.message);
            }
          });
        },
        onCancel() {},
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
    c_delete() {
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel() {},
      });
    },
    toggleAdvanced() {
      this.advanced = !this.advanced;
    },
    remove() {
      this.dataSource = this.dataSource.filter(
        (item) =>
          this.selectedRows.findIndex((row) => row.key === item.key) === -1
      );
      this.selectedRows = [];
    },
    addNew(data, type) {
      this.$router.push({
        path: "/portal/addoffer",
      });
      this.$store.state.account.contendVendorData = data;
      this.$store.state.account.contendVendorType = type;
    },
    // table所有方法
    getGoodList() {
      this.loading = true;
    },
    onSearch(conditions, searchOptions) {
      console.log(searchOptions);
      this.page = 1;
      this.conditions = conditions;
      this.getTypesAll();
    },
    onSizeChange(current, size) {
      this.page = 1;
      this.pageSize = size;
      this.getTypesAll();
    },
    onRefresh(conditions) {
      this.conditions = conditions;
      this.getTypesAll();
    },
    onReset(conditions) {
      this.conditions = conditions;
      this.getTypesAll();
    },
    onPageChange(page, pageSize) {
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
