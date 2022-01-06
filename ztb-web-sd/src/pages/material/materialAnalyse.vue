<template>
  <div>
    <a-card class="card-top">
      <a-form layout="horizontal">
        <a-row>
          <a-col :md="6" :sm="12">
            <a-form-item
              label="物料编码"
              :labelCol="{ sm: { span: 7 }, md: { span: 12 }, xl: { span: 7 } }"
              :wrapperCol="{
                sm: { span: 15 },
                md: { span: 12 },
                xl: { span: 15 },
              }"
            >
              <a-select v-model="form.materialId" :allowClear="true" placeholder="请选择">
                <a-select-option
                  v-for="item in materialList"
                  :key="item.id"
                  :value="item.id"
                  >{{ item.code }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :offset="12" :span="6">
            <span style="float: right; margin-top: 3px">
              <a-button type="primary" @click="getRoleAll">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </a-card>
    <a-card>
      <div class="operator table-change">
        <a-button @click="runReport()" type="primary">运行库存分析</a-button>
        <a-button
          @click="closeReport()"
          type="primary"
          style="margin-left: 10px"
          >关闭库存分析</a-button
        >
      </div>
      <!-- <a-table :columns="columns" :data-source="data">
      </a-table> -->
      <advance-table
        :columns="columns"
        :data-source="data"
        :loading="loading"
        rowKey="a1"
        :scroll="{ x: 1200 }"
        :customRow="rowClick"
        @refresh="onRefresh"
        :format-conditions="true"
        :rowClassName="setRowClassName"
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
        <template slot="materialCode" slot-scope="text">
          <span @click="addNew(text.text)" class="text-cursor">
            {{ text.text }}
          </span>
        </template>
        <template slot="warningLevel" slot-scope="text">
           <a-icon
              type="flag"
              :style="{ color: text.text == '高' ? '#FF0000' : '#00FF00' }"
            />
          <span class="icon-type" :style="{ color: text.text == '高' ? '#FF0000' : '#00FF00' }">
           {{ text.text }}
          </span>
        </template>
      </advance-table>
      <a-row style="width: 100%">
        <use-chart
          :dataList="dataList"
          style="height: 120px; width: calc(94vw - 256px)"
        ></use-chart>
      </a-row>
    </a-card>
    <a-modal
      title="编辑"
      :visible="visible"
      width="800px"
      :centered="true"
      :footer="null"
    >
      <analyse-add
        ref="childrenDom"
        @getAll="getRoleAll"
        @on-data-close="dataClose"
        :editData="editData"
      >
      </analyse-add>
    </a-modal>
  </div>
</template>
<script>
import AdvanceTable from "@/components/table/advance/AdvanceTable";
import UseChart from "./components/useChart.vue";
import { getTableList } from "@/utils/methods";
import AnalyseAdd from "./components/analyseAdd.vue";
import {
  materialAnalysisRequest,
  materialGetRequest,
  materialRunReportRequest,
  materialdeleteRequest,
} from "@/services/material/material";

const columns = [
  {
    title: "序号",
    key: "index",
    width: 90,
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "物料编码",
    align: "center",
    width: 90,
    dataIndex: "materialCode",
    scopedSlots: { customRender: "materialCode" },
  },
    {
    title: "库存状态",
    align: "center",
    width: 90,
    dataIndex: "statusName",
  },
  {
    title: "库存警告级别",
    align: "center",
    width: 120,
    dataIndex: "warningLevel",
    scopedSlots: { customRender: "warningLevel" },
  },
    {
    title: "频次类别",
    align: "center",
    width: 90,
    dataIndex: "classifyName",
  },
  {
    title: "分布类型",
    align: "center",
    width: 90,
    dataIndex: "distributionType",
  },

  {
    title: "历史平均需求",
    align: "center",
    width: 120,
    dataIndex: "averageHistoryUsage",
  },
    {
    title: "历史最大需求",
    align: "center",
    width: 130,
    dataIndex: "maxHistoryUsage",
  },
    {
    title: "未来平均需求",
    align: "center",
    width: 120,
    dataIndex: "averageForecast",
  },

    {
    title: "在手库存",
    align: "center",
    width: 90,
    dataIndex: "onHand",
  },
  {
    title: "当周预测用量",
    align: "center",
    width: 130,
    dataIndex: "forecastCurWeek",
  },
    {
    title: "最低库存量",
    align: "center",
    width: 110,
    dataIndex: "minStock",
  },
    {
    title: "再订货点库存量",
    align: "center",
    width: 130,
    dataIndex: "reorderPoint",
  },
  {
    title: "呆滞库存",
    align: "center",
    width: 90,
    dataIndex: "inactiveStock",
  },

  {
    title: "过期库存",
    align: "center",
    width: 90,
    dataIndex: "obsolescenceStock",
  },
  
  {
    title: "生产暂停点",
    align: "center",
    width: 120,
    dataIndex: "pausePoint",
  },
  {
    title: "最高库存量",
    align: "center",
    width: 120,
    dataIndex: "maxStock",
  },

  // {
  //   title: "安全库存",
  //   align: "center",
  //   width: 90,
  //   dataIndex: "safetyStock",
  // },
  // {
  //   title: "标准差",
  //   align: "center",
  //   width: 90,
  //   dataIndex: "sdValue",
  // },

  // {
  //   title: "库存周期数",
  //   align: "center",
  //   width: 130,
  //   dataIndex: "stockInWk",
  // },
  // {
  //   title: "计量单位",
  //   align: "center",
  //   width: 90,
  //   dataIndex: "unit",
  // },
];

export default {
  name: "role",
  components: { AdvanceTable, UseChart, AnalyseAdd },
  data() {
    return {
      dataList: "",
      editData: null,
      visible: false,
      form: {
        materialId: "",
      },
      selectedRowKeys: [],
      loading: false,
      page: 1,
      pageSize: 10,
      total: 0,
      data: [],
      columns,
      materialList: [],
      indexColims: null,
    };
  },
  created() {
    this.getRoleAll();
    this.getMaterial();
    let list = getTableList(this.columns);
    if (list) {
      this.columns = list;
    }
  },
  methods: {
    // 点击行事件
    rowClick(record) {
      let that = this
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
            that.dataList = record;
          },
        },
      };
    },
    setRowClassName(record) {
      if (record.materialCode === this.indexColims) {
        return "checked-td-of-add-table";
      } else {
        return "";
      }
    },
    dataClose() {
      this.$refs.childrenDom.form = {
        maxNumOfInvWeeks: "",
        onHand: "",
        targetInvTurnover: "",
      };
      this.visible = false;
    },
    addNew(obj) {
      materialGetRequest({
        currentPage: 1,
        code: obj,
      }).then((response) => {
        this.editData = response.data.records[0];
      });
      this.visible = true;
    },
    // 运行库存分析
    runReport() {
      materialRunReportRequest().then((response) => {
        this.$message.success("运行" + response.message);
      });
      this.getRoleAll();
    },
    // 关闭库存分析
    closeReport() {
      materialdeleteRequest().then((response) => {
        this.$message.success("关闭" + response.message);
      });
      this.getRoleAll();
    },
    // 查询物料
    getMaterial() {
      materialGetRequest({
        currentPage: 1,
      }).then((response) => {
        this.materialList = response.data.records;
      });
    },
    //角色列表
    getRoleAll() {
      materialAnalysisRequest({
        currentPage: 1,
        ...this.form,
      }).then((response) => {
        this.data = response.data.records;
        this.dataList = this.data[0];
        this.indexColims = this.data[0].materialCode;
      });
    },
    // table所有方法
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
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
.iconstyle {
  margin: 0 10px;
}
.icon-type{
  margin-left: 5px;
}
/deep/ .checked-td-of-add-table {
  cursor: pointer;
  background-color: #e6f7ff;
}
</style>