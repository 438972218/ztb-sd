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
              <a-form-item label="模板名称：">
                <a-input placeholder="请输入模板名称" />
              </a-form-item>
            </a-col>
            <a-col :span="5">
              <a-form-item label="公司全称：">
                <a-input placeholder="请输入公司全称" />
              </a-form-item>
            </a-col>
            <a-col :span="5">
              <a-form-item label="合同分类：">
                <a-input placeholder="请输入合同分类" />
              </a-form-item>
            </a-col>
            <a-col :span="5">
              <a-form-item label="状态：">
                <a-select placeholder="请选择状态">
                  <a-select-option value="1"> 生效 </a-select-option>
                  <a-select-option value="2"> 失效 </a-select-option>
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
          <a-button @click="addNew" type="primary">新增</a-button>
        </div>
        <a-table
          :rowKey="(record) => record.dataIndex"
          :columns="columns"
          :dataSource="dataSource"
          :selectedRows.sync="selectedRows"
          @change="onChange"
          @selectedRowChange="onSelectChange"
        >
          <template slot="a7">
            <a-button type="link" @click="templateEdit"> 在线编辑 </a-button>
          </template>
          <template slot="a9">
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
      <a-form-model
        layout="horizontal"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 20 }"
      >
        <a-row :gutter="20">
          <a-col :span="12">
            <a-form-model-item label="模板编号：">
              <a-input
                default-value="待生成"
                disabled
                placeholder="请输入模板编号"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="模板名称：">
              <a-input placeholder="请输入模板名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="公司名称：">
              <a-input placeholder="请输入公司名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="状态：">
              <a-select placeholder="请选择标状态">
                <a-select-option value="1"> 生效 </a-select-option>
                <a-select-option value="2"> 失效 </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同分类：">
              <a-input placeholder="请输入合同分类" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="标的类型：">
              <a-input placeholder="请输入标的类型" disabled />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="模板说明："
              :labelCol="{ span: 2 }"
              :wrapperCol="{ span: 22 }"
            >
              <a-textarea placeholder="请输入模板说明" :rows="4" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <a-modal
      title="在线编辑"
      :visible="templateVisible"
      width="70%"
      @ok="templateVisible = false"
      @cancel="templateVisible = false"
    >
      <editor ref="editor"></editor>
    </a-modal>
  </div>
</template>

<script>
import editor from './components/editorWang.vue'
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
    title: "模板编码",
    dataIndex: "a2",
    align: "center",
  },
  {
    title: "模板名称",
    dataIndex: "a3",
    align: "center",
  },
  {
    title: "公司名称",
    dataIndex: "a4",
    align: "center",
  },
  {
    title: "合同分类",
    dataIndex: "a5",
    align: "center",
  },
  {
    title: "模板说明",
    dataIndex: "a6",
    align: "center",
  },
  {
    title: "合同模板",
    dataIndex: "a7",
    align: "center",
    scopedSlots: { customRender: "a7" },
  },
  {
    title: "状态",
    dataIndex: "a8",
    align: "center",
  },
  {
    title: "操作",
    align: "center",
    dataIndex: "a9",
    scopedSlots: { customRender: "a9" },
  },
];

const dataSource = [
  {
    a1: '1',
    a2: "RFQ2021051200001",
    a3: "江苏恒云太信息科技有限公司",
    a4: "江苏恒云太信息科技有限公司",
    a5: '专业技术服务业',
    a6: "张三",
    a7: '',
    a8: '生效',
    a9: ''
  },
  {
    a1: '2',
    a2: "RFQ2021051200001",
    a3: "江苏恒云太信息科技有限公司",
    a4: "江苏恒云太信息科技有限公司",
    a5: '专业技术服务业',
    a6: "张三",
    a7: '',
    a8: '失效',
    a9: ''
  },
];
export default {
  name: "contractTemplate",
  components: { editor },
  data () {
    return {
      title: '',
      columns: columns,
      dataSource: dataSource,
      selectedRows: [],
      rowSelection,
      visible: false,
      templateVisible: false
    };
  },
  methods: {
    onChange () {
      this.$message.info("表格状态改变了");
    },
    onSelectChange () {
      this.$message.info("选中行改变了");
    },
    // 新增
    addNew () {
      this.title = '新增合同模板'
      this.visible = true
    },
    // 编辑
    handleEdit () {
      this.title = '编辑合同模板'
      this.visible = true
    },
    // 在线编辑
    templateEdit () {
      this.templateVisible = true
    }
  },
};
</script>

<style lang="less" scoped>
</style>
