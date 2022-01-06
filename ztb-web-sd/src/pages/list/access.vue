<template>
  <div>
    <a-card class="card-top">
      <div>
        <a-form
          layout="horizontal"
          :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 18 }"
        >
          <a-row :gutter="20">
            <a-col :span="5">
              <a-form-item label="合同编号：">
                <a-input placeholder="请输入合同编号" />
              </a-form-item>
            </a-col>
            <a-col :span="5">
              <a-form-item label="合同名称：">
                <a-input placeholder="请输入合同名称" />
              </a-form-item>
            </a-col>
            <a-col :span="5">
              <a-form-item label="合同分类：">
                <a-input placeholder="请输入合同名称" />
              </a-form-item>
            </a-col>
            <a-col :span="5">
              <a-form-item label="状态：">
                <a-select placeholder="请选择状态">
                  <a-select-option value="新建"> 新建 </a-select-option>
                  <a-select-option value="待审批"> 待审批 </a-select-option>
                  <a-select-option value="待发布"> 待发布 </a-select-option>
                  <a-select-option value="待供方签约">
                    待供方签约
                  </a-select-option>
                  <a-select-option value="待买方签约">
                    待买方签约
                  </a-select-option>
                  <a-select-option value="执行中"> 执行中 </a-select-option>
                  <a-select-option value="已完成"> 已完成 </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="4" align="right">
              <a-button type="primary">查询</a-button>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </a-card>
    <a-card>
      <div>
        <div class="operator">
          <a-button @click="addNew" type="primary">新建</a-button>
        </div>
        <a-table
          :scroll="{ x: 1300 }"
          :rowKey="(record) => record.id"
          :columns="columns"
          :dataSource="dataSource"
          :selectedRows.sync="selectedRows"
          @change="onChange"
          @selectedRowChange="onSelectChange"
        >
          <template slot="a12" slot-scope="record">
            <a-button
              type="link"
              v-if="record.a1 === '1'"
              @click="handleClick('1')"
            >
              编辑
            </a-button>
            <a-button
              type="link"
              v-if="record.a1 === '2'"
              @click="handleClick('2')"
            >
              审批
            </a-button>
            <a-button
              type="link"
              v-if="record.a1 === '3'"
              @click="handleClick('3')"
            >
              发布
            </a-button>
            <a-button
              type="link"
              v-if="record.a1 === '4'"
              @click="handleClick('4')"
            >
              签署
            </a-button>
            <a-button
              type="link"
              v-if="record.a1 === '5'"
              @click="handleClick('5')"
            >
              执行
            </a-button>
          </template>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>

const rowSelection = {
  onChange: (selectedRowKeys, selectedRows) => {
  },
  onSelectAll: (selected, selectedRows, changeRows) => {
  },
};
const columns = [
  {
    title: "序号",
    width: 60,
    align: "center",
    dataIndex: "a1",
  },
  {
    title: "合同编号",
    dataIndex: "a2",
    width: 180,
    align: "center",
  },
  {
    title: "合同名称",
    dataIndex: "a3",
    width: 150,
    align: "center",
  },
  {
    title: "合同对象",
    dataIndex: "a4",
    width: 120,
    align: "center",
  },
  {
    title: "合同分类",
    dataIndex: "a5",
    width: 120,
    align: "center",
  },
  {
    title: "合同性质",
    dataIndex: "a6",
    width: 150,
    align: "center",
  },
  {
    title: "合同模板",
    dataIndex: "a7",
    width: 120,
    align: "center",
  },
  {
    title: "公司名称",
    dataIndex: "a8",
    width: 230,
    align: "center",
  },
  {
    title: "合同开始日期",
    dataIndex: "a9",
    width: 120,
    align: "center",
  },
  {
    title: "合同终止日期",
    dataIndex: "a10",
    width: 120,
    align: "center",
  },
  {
    title: "状态",
    dataIndex: "a11",
    width: 80,
    align: "center",
  },
  {
    title: "操作",
    width: 150,
    fixed: 'right',
    align: "center",
    scopedSlots: { customRender: "a12" },
  },
];

const dataSource = [
  {
    a1: '1',
    a2: "RFQ2021051200001",
    a3: "软件开发合同",
    a4: "先导智能",
    a5: "框架合同",
    a6: "外部合同",
    a7: "软件合同",
    a8: "江苏恒云太信息科技有限公司",
    a9: "2021-12-01",
    a10: "2029-12-01",
    a11: "新建",
    a12: "a12"
  },
  {
    a1: '2',
    a2: "RFQ2021051200001",
    a3: "物料采购合同",
    a4: "先导智能",
    a5: "非框架合同",
    a6: "咨询，服务，设备",
    a7: "完整合同",
    a8: "江苏恒云太信息科技有限公司",
    a9: "2021-12-01",
    a10: "2029-12-01",
    a11: "待审批",
    a12: "a12"
  },
  {
    a1: '3',
    a2: "RFQ2021051200001",
    a3: "物料采购合同",
    a4: "先导智能",
    a5: "非框架合同",
    a6: "咨询，服务，设备",
    a7: "完整合同",
    a8: "江苏恒云太信息科技有限公司",
    a9: "2021-12-01",
    a10: "2029-12-01",
    a11: "待审批",
    a12: "a12"
  },
  {
    a1: '4',
    a2: "RFQ2021051200001",
    a3: "物料采购合同",
    a4: "先导智能",
    a5: "非框架合同",
    a6: "咨询，服务，设备",
    a7: "完整合同",
    a8: "江苏恒云太信息科技有限公司",
    a9: "2021-12-01",
    a10: "2029-12-01",
    a11: "待审批",
    a12: "a12"
  },
  {
    a1: '5',
    a2: "RFQ2021051200001",
    a3: "物料采购合同",
    a4: "先导智能",
    a5: "非框架合同",
    a6: "咨询，服务，设备",
    a7: "完整合同",
    a8: "江苏恒云太信息科技有限公司",
    a9: "2021-12-01",
    a10: "2029-12-01",
    a11: "待审批",
    a12: "a12"
  },
];
export default {
  name: "access",
  data () {
    return {
      columns: columns,
      dataSource: dataSource,
      selectedRows: [],
      rowSelection
    };
  },
  methods: {
    // 点击事件
    handleClick (row) {
      switch (row) {
        case '1':
          this.$router.push('accessAdd')
          break
        case '2':
          this.$router.push('contractApproval')
          break
        case '3':
          this.$router.push('contractRelease')
          break
        case '4':
          this.$router.push('contractAward')
          break
        case '5':
          this.$router.push('contractExecution')
          break
      }
    },
    c_delete () {
      this.$confirm({
        title: '警告',
        content: '你确定要删除吗？',
        centered: true,
        onOk () {
        },
        onCancel () { },
      });
    },
    onChange () {
      this.$message.info("表格状态改变了");
    },
    onSelectChange () {
      this.$message.info("选中行改变了");
    },
    addNew () {
      this.$router.push('accessAdd');
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
.iconstyle {
  cursor: pointer;
  font-size: 16px;
  margin: 0 10px;
}
</style>
