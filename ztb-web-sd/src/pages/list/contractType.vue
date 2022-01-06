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
            <a-col :span="6">
              <a-form-item label="编码：">
                <a-input placeholder="请输入编码" />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="名称：">
                <a-input placeholder="请输入名称" />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="公司代码：">
                <a-input placeholder="请输入公司代码" />
              </a-form-item>
            </a-col>
            <a-col :span="6" align="right">
              <a-button type="primary">查询</a-button>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </a-card>
    <a-card>
      <div>
        <div class="operator">
          <a-button @click="addNew" type="primary">新增</a-button>
        </div>
        <a-table
          :rowKey="(record) => record.id"
          :columns="columns"
          :dataSource="dataSource"
          :selectedRows.sync="selectedRows"
          @change="onChange"
          @selectedRowChange="onSelectChange"
        >
          <span slot="a7" slot-scope="text"
            ><a-switch :default-checked="text"
          /></span>
          <template slot="a8">
            <a-button type="link" @click="handleEdit"> 编辑 </a-button>
            <a-button type="link"> 删除 </a-button>
          </template>
        </a-table>
      </div>
    </a-card>
    <a-modal
      :title="title"
      :visible="visible"
      width="70%"
      @ok="visible = false"
      @cancel="visible = false"
    >
      <contractTypeAdd ref="contractTypeAdd"></contractTypeAdd>
    </a-modal>
  </div>
</template>

<script>
import contractTypeAdd from './components/contractTypeAdd.vue'
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
    title: "编码",
    dataIndex: "a2",
    align: "center",
  },
  {
    title: "名称",
    dataIndex: "a3",
    align: "center",
  },
  {
    title: "公司代码",
    dataIndex: "a4",
    align: "center",
  },
  {
    title: "标的类型",
    dataIndex: "a5",
    align: "center",
  },
  {
    title: "用途说明",
    dataIndex: "a6",
    align: "center",
  },
  {
    title: "启用",
    dataIndex: "a7",
    align: "center",
    scopedSlots: { customRender: "a7" },
  },
  {
    title: "操作",
    align: "center",
    dataIndex: "a8",
    scopedSlots: { customRender: "a8" },
  },
];

const dataSource = [
  {
    a1: '1',
    a2: "01001",
    a3: "技术协议",
    a4: "集采中心",
    a5: "材料",
    a6: "",
    a7: true,
    a8: ""
  },
  {
    a1: '2',
    a2: "01002",
    a3: "物资采购合同",
    a4: "集采中心",
    a5: "材料",
    a6: "",
    a7: false,
    a8: ""
  },
  {
    a1: '3',
    a2: "01003",
    a3: "设备采购合同",
    a4: "集采中心",
    a5: "材料",
    a6: "",
    a7: true,
    a8: ""
  },
  {
    a1: '4',
    a2: "01004",
    a3: "服务协议",
    a4: "集采中心",
    a5: "材料",
    a6: "",
    a7: true,
    a8: ""
  },
  {
    a1: '5',
    a2: "01005",
    a3: "补充协议",
    a4: "集采中心",
    a5: "材料",
    a6: "",
    a7: true,
    a8: ""
  },
];
export default {
  name: "contractType",
  components: { contractTypeAdd },
  data () {
    return {
      title: '合同分类新增',
      columns: columns,
      dataSource: dataSource,
      selectedRows: [],
      rowSelection,
      visible: false
    };
  },
  methods: {
    onChange () {
      this.$message.info("表格状态改变了");
    },
    onSelectChange () {
      this.$message.info("选中行改变了");
    },
    addNew () {
      this.title = '合同分类新增'
      this.visible = true
    },
    // 编辑
    handleEdit () {
      this.title = '合同分类编辑'
      this.visible = true
    }
  },
};
</script>

<style lang="less" scoped>
</style>
