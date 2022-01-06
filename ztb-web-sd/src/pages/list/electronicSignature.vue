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
              <a-form-item label="公司代码：">
                <a-input placeholder="请输入公司代码" />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="公司全称：">
                <a-input placeholder="请输入公司全称" />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="状态：">
                <a-select placeholder="请选择状态">
                  <a-select-option value="1"> 未申请 </a-select-option>
                  <a-select-option value="2"> 申请中 </a-select-option>
                  <a-select-option value="3"> 已申请 </a-select-option>
                </a-select>
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
        <a-table
          :rowKey="(record) => record.dataIndex"
          :columns="columns"
          :dataSource="dataSource"
          :selectedRows.sync="selectedRows"
          @change="onChange"
          @selectedRowChange="onSelectChange"
        >
          <template slot="a7" slot-scope="text, record">
            <a-button
              type="link"
              @click="handleApply"
              v-if="record.a5 === '未申请'"
            >
              申请
            </a-button>
            <a-button type="link" v-else @click="handleSignature">
              签章管理
            </a-button>
          </template>
        </a-table>
      </div>
    </a-card>
    <a-modal
      title="电子签章申请"
      :visible="applyVisible"
      width="70%"
      @ok="applyVisible = false"
      @cancel="applyVisible = false"
    >
      <a-divider orientation="left"> 基本信息 </a-divider>
      <a-form-model
        layout="horizontal"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 19 }"
      >
        <a-row :gutter="20">
          <a-col :span="12">
            <a-form-model-item label="公司代码：">
              <a-input
                default-value="COO00001"
                disabled
                placeholder="请输入公司代码"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="公司全称：">
              <a-input
                disabled
                default-value="无锡物资有限公司"
                placeholder="请输入公司全称"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="社会信用代码：">
              <a-input
                default-value="91320211782738965G"
                disabled
                placeholder="请输入社会信用代码"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="电话：">
              <a-input
                disabled
                default-value="13888888888"
                placeholder="请输入电话"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
      <a-divider orientation="left"> 经办人信息 </a-divider>
      <a-form-model
        layout="horizontal"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 19 }"
      >
        <a-row :gutter="20">
          <a-col :span="12">
            <a-form-model-item label="姓名：">
              <a-input default-value="USER" disabled placeholder="请输入姓名" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="手机号码：">
              <a-input placeholder="请输入手机号码" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="证件类型：">
              <a-select placeholder="请选择证件类型">
                <a-select-option value="1"> 身份证 </a-select-option>
                <a-select-option value="2"> 护照 </a-select-option>
                <a-select-option value="3"> 台胞证 </a-select-option>
                <a-select-option value="4"> 港澳通行证 </a-select-option>
                <a-select-option value="5"> 其他 </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="证件号码：">
              <a-input placeholder="请输入证件号码" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="证件邮箱：">
              <a-input placeholder="请输入邮箱" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="地址：">
              <a-textarea placeholder="请输入地址" :rows="2" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <a-modal
      title="签章管理"
      :visible="signatureVisible"
      width="60%"
      @ok="signatureVisible = false"
      @cancel="signatureVisible = false"
    >
      <a-table :columns="columns1" :data-source="dataSource1">
        <template slot="a4" slot-scope="text">
          <a-switch :default-checked="text" />
        </template>
        <template slot="a5" slot-scope="text, record">
          <a-button type="link">{{ record.a4 ? '下载' : '上传' }}</a-button>
        </template>
      </a-table>
    </a-modal>
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
    title: "公司代码",
    dataIndex: "a2",
    align: "center",
  },
  {
    title: "公司全称",
    dataIndex: "a3",
    align: "center",
  },
  {
    title: "公司简称",
    dataIndex: "a4",
    align: "center",
  },
  {
    title: "状态",
    dataIndex: "a5",
    align: "center",
  },
  {
    title: "说明",
    dataIndex: "a6",
    align: "center",
  },
  {
    title: "操作",
    align: "center",
    dataIndex: "a7",
    scopedSlots: { customRender: "a7" },
  },
];

const dataSource = [
  {
    a1: '1',
    a2: "RFQ2021051200001",
    a3: "江苏恒云太信息科技有限公司",
    a4: "江苏恒云太信息科技有限公司",
    a5: '未申请',
    a6: "",
    a7: '',
  },
  {
    a1: '2',
    a2: "RFQ2021051200001",
    a3: "无锡茂业公司",
    a4: "无锡茂业公司",
    a5: "未申请",
    a6: "",
    a7: '',
  },
  {
    a1: '3',
    a2: "RFQ2021051200001",
    a3: "江苏恒云太信息科技有限公司",
    a4: "江苏恒云太信息科技有限公司",
    a5: '未申请',
    a6: "",
    a7: '',
  },
  {
    a1: '4',
    a2: "RFQ2021051200001",
    a3: "江苏科技有限公司",
    a4: "江苏科技有限公司",
    a5: '未申请',
    a6: "",
    a7: '',
  },
  {
    a1: '5',
    a2: "RFQ2021051200001",
    a3: "无锡纺织厂",
    a4: "无锡纺织厂",
    a5: "已申请",
    a6: "",
    a7: '',
  },
];
const columns1 = [
  {
    title: "序号",
    width: 60,
    align: "center",
    dataIndex: "a1",
  },
  {
    title: "签章编码",
    dataIndex: "a2",
    align: "center",
  },
  {
    title: "描述",
    dataIndex: "a3",
    align: "center",
  },
  {
    title: "启用",
    dataIndex: "a4",
    align: "center",
    scopedSlots: { customRender: "a4" },
  },
  {
    title: "操作",
    align: "center",
    dataIndex: "a5",
    scopedSlots: { customRender: "a5" },
  },
];

const dataSource1 = [
  {
    a1: '1',
    a2: "QZ00001",
    a3: "和A公司签约",
    a4: true,
    a5: ''
  },
  {
    a1: '2',
    a2: "QZ00001",
    a3: "和A公司签约",
    a4: false,
    a5: ''
  }
];
export default {
  name: "electronicSignature",
  components: {},
  data () {
    return {
      columns: columns,
      dataSource: dataSource,
      columns1,
      dataSource1,
      selectedRows: [],
      rowSelection,
      applyVisible: false,
      signatureVisible: false
    };
  },
  methods: {
    onChange () {
      this.$message.info("表格状态改变了");
    },
    onSelectChange () {
      this.$message.info("选中行改变了");
    },
    // 申请
    handleApply () {
      this.applyVisible = true
    },
    // 签章管理
    handleSignature () {
      this.signatureVisible = true
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
