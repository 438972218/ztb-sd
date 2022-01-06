<template>
  <div>
    <a-form-model
      layout="horizontal"
      :labelCol="{ span: 6 }"
      :wrapperCol="{ span: 18 }"
    >
      <a-row :gutter="20">
        <a-col :span="8">
          <a-form-model-item label="编码：">
            <a-input default-value="待生成" disabled placeholder="请输入编号" />
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="名称：">
            <a-input default-value="设备合同" placeholder="请输入名称" />
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="公司代码：">
            <a-input
              default-value="江苏恒云太信息科技有限公司"
              placeholder="请输入公司代码"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="标的类型：">
            <a-select default-value="1" placeholder="请选择标的类型">
              <a-select-option value="1"> 材料 </a-select-option>
              <a-select-option value="2"> 设备 </a-select-option>
              <a-select-option value="3"> 工程 </a-select-option>
              <a-select-option value="4"> 服务 </a-select-option>
              <a-select-option value="5"> 其他 </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="合同类型：">
            <a-select placeholder="请选择合同类型">
              <a-select-option value="1"> 销售类 </a-select-option>
              <a-select-option value="2"> 采购类 </a-select-option>
              <a-select-option value="3"> 合作协议 </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item
            label="用途说明："
            :labelCol="{ span: 2 }"
            :wrapperCol="{ span: 22 }"
          >
            <a-textarea placeholder="请输入用途说明" :rows="4" />
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
    <a-collapse v-model="activeKey">
      <a-collapse-panel key="1" header="业务字段">
        <a-button @click="addBusiness" type="primary">新增</a-button>
        <a-table :columns="columns" :data-source="data">
          <span slot="a7">
            <a-button type="link" @click="editBusiness">编辑</a-button>
            <a-button type="link">删除</a-button>
          </span>
        </a-table>
      </a-collapse-panel>
      <a-collapse-panel key="2" header="附件类型">
        <a-button @click="addAnnex" type="primary">新增</a-button>
        <a-table :columns="columns1" :data-source="data1">
          <span slot="a6">
            <a-button type="link" @click="editAnnex">编辑</a-button>
            <a-button type="link">删除</a-button>
          </span>
        </a-table>
      </a-collapse-panel>
    </a-collapse>
    <!-- 业务字段弹窗 -->
    <a-modal
      :title="businessTitle"
      :visible="businessVisible"
      width="60%"
      @ok="businessVisible = false"
      @cancel="businessVisible = false"
    >
      <a-form-model
        layout="horizontal"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 20 }"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="业务字段编码：">
              <a-input
                default-value="待生成"
                disabled
                placeholder="请输入业务字段编码"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="业务字段名称：">
              <a-input
                default-value="交货周期"
                placeholder="请输入业务字段名称"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="业务字段格式：">
              <a-select placeholder="请选择业务字段格式">
                <a-select-option value="1"> 字符型 </a-select-option>
                <a-select-option value="2"> 数字型 </a-select-option>
                <a-select-option value="3"> 时间型 </a-select-option>
                <a-select-option value="4"> 图片型 </a-select-option>
                <a-select-option value="5"> 日期型 </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="必输字段：">
              <a-checkbox> </a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="说明：">
              <a-textarea placeholder="请输入说明" :rows="4" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <!-- 附件类型弹窗 -->
    <a-modal
      :title="annexTitle"
      :visible="annexVisible"
      width="60%"
      @ok="annexVisible = false"
      @cancel="annexVisible = false"
    >
      <a-form-model
        layout="horizontal"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 20 }"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="附件类型编码：">
              <a-input
                default-value="待生成"
                disabled
                placeholder="请输入附件类型编码"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="附件类型名称：">
              <a-input
                default-value="设备附件"
                placeholder="请输入附件类型名称"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="必传附件：">
              <a-checkbox> </a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="说明：">
              <a-textarea placeholder="请输入说明" :rows="4" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
const columns = [
  {
    title: '序号',
    dataIndex: 'a1',
    key: 'a1',
  },
  {
    title: '业务字段编码',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '业务字段名称',
    dataIndex: 'a3',
    key: 'a3',
  },
  {
    title: '业务字段格式',
    dataIndex: 'a4',
    key: 'a4',
  },
  {
    title: '必输字段',
    dataIndex: 'a5',
    key: 'a5',
  },
  {
    title: '说明',
    dataIndex: 'a6',
    key: 'a6',
  },
  {
    title: '操作',
    dataIndex: 'a7',
    key: 'a7',
    scopedSlots: { customRender: 'a7' },
  }
];
const data = [
  {
    a1: '1',
    a2: '11111',
    a3: '交货周期',
    a4: '',
    a5: '是',
    a6: '',
    a7: ''
  }
];
const columns1 = [
  {
    title: '序号',
    dataIndex: 'a1',
    key: 'a1',
  },
  {
    title: '附件类型编码',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '附件类型名称',
    dataIndex: 'a3',
    key: 'a3',
  },
  {
    title: '必传附件',
    dataIndex: 'a4',
    key: 'a4',
  },
  {
    title: '说明',
    dataIndex: 'a5',
    key: 'a5',
  },
  {
    title: '操作',
    dataIndex: 'a6',
    key: 'a6',
    scopedSlots: { customRender: 'a6' },
  }
];
const data1 = [
  {
    a1: '1',
    a2: '22222',
    a3: '设备附件',
    a4: '是',
    a5: '',
    a6: ''
  }
];

export default {
  props: {
  },
  name: "contractTypeAdd",
  components: {},
  data () {
    return {
      businessTitle: '',
      annexTitle: '',
      businessVisible: false,
      annexVisible: false,
      activeKey: ['1', '2'],
      visible: false,
      columns,
      data,
      columns1,
      data1
    }
  },
  methods: {
    // 新增业务字段
    addBusiness () {
      this.businessTitle = '新增业务字段'
      this.businessVisible = true
    },
    // 编辑业务字段
    editBusiness () {
      this.businessTitle = '编辑业务字段'
      this.businessVisible = true
    },
    // 新增附件类型
    addAnnex () {
      this.annexTitle = '新增附件类型'
      this.annexVisible = true
    },
    // 编辑附件类型
    editAnnex () {
      this.annexTitle = '编辑附件类型'
      this.annexVisible = true
    },
    // 关闭弹窗
    handleCancel () {
      this.visible = false
    }
  }
};
</script>

<style lang="less" scoped>
</style>
