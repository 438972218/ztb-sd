<template>
  <div>
    <a-row>
      <a-row style="padding-bottom: 5px">
        <a-col :span="2">
          <span style="font-size: 22px; font-weight: 550">品类</span>
        </a-col>
        <a-col
          :span="22"
          align="right"
          v-if="
            show &&
            ((form.bidStructure === '总额' && itemData.length === 0) ||
              form.bidStructure !== '总额')
          "
        >
          <a-button type="primary" @click="handleAddItem(1)"> 新增 </a-button>
        </a-col>
      </a-row>
      <a-table :columns="columns" :data-source="itemData">
        <template slot="startPrice" slot-scope="text">
          <a v-if="form.startPriceRule === '区分'" @click="showVendor">
            查看
          </a>
          <span v-else>{{ text }}</span>
        </template>
        <span slot="operation" slot-scope="text, record">
          <a-icon
            type="edit"
            theme="filled"
            class="iconstyle"
            style="padding-right: 10px"
            @click="handleEdit(record, 1)"
          />
          <a-icon
            type="delete"
            title="删除"
            @click="handleremove(record)"
            class="iconstyle"
          />
        </span>
      </a-table>
    </a-row>
    <!-- <a-row v-if="form.bidStructure === '总额' && parentId">
      <a-row style="padding-bottom: 5px">
        <a-col :span="2">
          <span style="font-size: 22px; font-weight: 550">明细</span>
        </a-col>
        <a-col :span="22" align="right" v-if="show">
          <a-button type="primary" @click="handleAddItem(2)"> 新增 </a-button>
        </a-col>
      </a-row>
      <a-table :columns="columns1" :data-source="detailData">
        <span slot="operation" slot-scope="text, record">
          <a-icon
            type="edit"
            theme="filled"
            class="iconstyle"
            style="padding-right: 10px"
            @click="handleEdit(record, 2)"
          />
          <a-icon
            type="delete"
            title="删除"
            @click="handleremove(record)"
            class="iconstyle"
          />
        </span>
      </a-table>
    </a-row> -->
    <!-- <a-row>
      <a-col :span="24" align="center">
        <a-button type="primary" @click="handleSave"> 保存 </a-button>
      </a-col>
    </a-row> -->
    <!-- 新增品类 -->
    <a-modal
      :title="title"
      :visible="visible"
      width="700px"
      @ok="saveItem"
      @cancel="visible = false"
    >
      <a-form-model
        ref="ruleForm"
        :model="formInline"
        :rules="rules"
        :labelCol="{ span: 5 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item
              :label="addType !== 2 ? '品类名称' : '细项'"
              prop="materialName"
            >
              <a-input
                v-model="formInline.materialName"
                placeholder="请输入品类名称"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="addType !== 2">
            <a-form-model-item label="起始总价" prop="startPrice">
              <a-input
                v-model="formInline.startPrice"
                placeholder="请输入起始总价"
              ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="addType !== 2">
            <a-form-model-item label="最小出价配置" prop="minimumBidRule">
              <a-select
                v-model="formInline.minimumBidRule"
                style="width: 100%"
                placeholder="请选择最小出价配置"
              >
                <a-select-option
                  v-for="item in minimumBidRuleList"
                  :key="item.meaning"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="addType !== 2">
            <a-form-model-item
              label="最小出价幅度"
              :prop="formInline.triggerPrice ? 'minimumBidRange' : ''"
            >
              <a-input
                v-model="formInline.minimumBidRange"
                placeholder="请输入最小出价幅度"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="addType !== 2">
            <a-form-model-item label="触发价格" prop="triggerPrice">
              <a-input
                v-model="formInline.triggerPrice"
                placeholder="请输入触发价格"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="addType !== 2">
            <a-form-model-item
              label="触发后最小出价幅度"
              prop="triggerMinimumBidRange"
            >
              <a-input
                v-model="formInline.triggerMinimumBidRange"
                placeholder="请输入触发后最小出价幅度"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数量" prop="quantity">
              <a-input
                type="number"
                v-model="formInline.quantity"
                placeholder="请输入数量"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单位" prop="unit">
              <a-input v-model="formInline.unit" placeholder="请输入单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注">
              <a-input v-model="formInline.remark" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <!-- 查看供应商 -->
    <a-modal
      title="起始总价"
      :visible="visibleVendor"
      width="700px"
      @ok="visibleVendor = false"
      @cancel="visibleVendor = false"
    >
      <a-table
        :pagination="paginationVendor"
        :columns="columnsVendor"
        :data-source="dataVendor"
        @change="tableVendorChange"
      >
        <template
          v-for="col in ['startPrice']"
          :slot="col"
          slot-scope="text, record"
        >
          <div :key="col">
            <a-input
              v-if="record.editable"
              style="margin: -5px 0"
              :value="text"
              @change="
                (e) => handleChangeVendor(e.target.value, record.id, col)
              "
            />
            <template v-else>
              {{ text }}
            </template>
          </div>
        </template>
      </a-table>
    </a-modal>
  </div>
</template>

<script>
import { paidItemsBatchSave, getPaidMaterialPaidSheetId, paidItemsPostReturnVO, paidItemsPutRequest, paidItemsDeleteRequest, paidVendorGetRequest } from "@/services/source/contend";
import { mapGetters } from "vuex";
const columns = [
  {
    title: '品类名称',
    dataIndex: 'materialName',
    key: 'materialName',
  },
  {
    title: '起始总价',
    dataIndex: 'startPrice',
    key: 'startPrice',
    scopedSlots: { customRender: "startPrice" },
  },
  {
    title: '最小出价配置',
    dataIndex: 'minimumBidRule',
    key: 'minimumBidRule',
  },
  {
    title: '最小出价幅度',
    dataIndex: 'minimumBidRange',
    key: 'minimumBidRange',
  },
  // {
  //   title: '触发价格',
  //   dataIndex: 'triggerPrice',
  //   key: 'triggerPrice',
  // },
  {
    title: '触发后最小出价幅度',
    dataIndex: 'triggerMinimumBidRange',
    key: 'triggerMinimumBidRange',
  },
  {
    title: '单位',
    dataIndex: 'unit',
    key: 'unit'
  },
  {
    title: '数量',
    dataIndex: 'quantity',
    key: 'quantity'
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark',
  }
];
const columns1 = [
  {
    title: '细项',
    dataIndex: 'materialName',
    key: 'materialName',
  },
  {
    title: '数量',
    dataIndex: 'quantity',
    key: 'quantity'
  },
  {
    title: '单位',
    dataIndex: 'unit',
    key: 'unit'
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
];
const columnsVendor = [
  {
    title: "供应商名称",
    dataIndex: "vendorName",
    key: "vendorName",
  },
  {
    title: "起始总价",
    dataIndex: "startPrice",
    key: "startPrice",
    scopedSlots: { customRender: "startPrice" },
  },
  // {
  //   title: "操作",
  //   dataIndex: "operation",
  //   key: "operation",
  //   scopedSlots: { customRender: "operation" },
  // },
]
export default {
  props: {
    show: {
      type: Boolean,
      default: true
    },
    minimumBidRuleList: {
      type: Array,
      default: () => {
        return []
      }
    },
  },
  name: "itemdetails",
  computed: {
    ...mapGetters("account", ["user"])
  },
  data () {
    return {
      columnsVendor,
      dataVendor: [], //区分价格供应商数据
      visibleVendor: false,
      title: '新增品类',
      parentId: '',
      visible: false,
      rules: {
        minimumBidRange: [{ required: true, message: '请选择最小出价配置', trigger: 'change' }],
        minimumBidRule: [{ required: true, message: '请输入最小出价幅度', trigger: 'blur' }],
        materialName: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        unit: [{ required: true, message: '请输入单位', trigger: 'blur' }],
        quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
      },
      form: {
        bidStructure: ''
      },
      paginationVendor: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
      addType: '',
      columns1,
      formInline: {
        materialName: '', // 品类名称
        startPrice: '', // 起始价格
        minimumBidRule: undefined, // 最小出价配置
        minimumBidRange: '', // 最小出价幅度
        triggerPrice: '', // 触发价格
        triggerMinimumBidRange: '', // 触发价格后最小出价幅度
        unit: '', // 单位
        quantity: '', // 数量
        remark: '', // 备注
      },
      columns,
      itemData: [],
      detailData: [],
      saveData: []
    }
  },
  mounted () {
    this.form = JSON.parse(sessionStorage.getItem('contendForm'))
    this.getItems()
  },
  methods: {
    showVendor () {
      this.visibleVendor = true
      this.getDataVendor()
    },
    tableVendorChange (row) {
      this.paginationVendor = row
      this.getDataVendor()
    },
    // 查询供应商
    async getDataVendor () {
      const data = {
        vendorUserId: this.user.id,
        paidSheetId: this.form.id,
        currentPage: this.paginationVendor.current,
        pageSize: this.paginationVendor.pageSize,
      }
      const result = await paidVendorGetRequest(data)
      if (result.code === 0) {
        this.dataVendor = result.data.records.map(item => ({
          ...item,
          editable: false,
        }))
        this.paginationVendor.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 竞价物品查询
    async getItems () {
      const result = await getPaidMaterialPaidSheetId(this.form.id)
      if (result.code === 0) {
        this.itemData = result.data ? result.data : []
        this.parentId = this.itemData.length > 0 ? this.itemData[0].id : ''
        this.detailData = result.data ? result.data[0].childPaidMaterialVOS : []
      }
    },
    // 编辑
    handleEdit (row, type) {
      this.addType = type
      this.title = type === 1 ? '编辑品类' : '编辑明细'
      this.formInline = Object.assign({}, row)
      this.visible = true
    },
    // 删除
    handleremove (row) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        async onOk () {
          const result = await paidItemsDeleteRequest(row.id)
          if (result.code === 0) {
            that.getItems()
            that.$message.success('删除成功')
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 新增品类主项数据
    handleAddItem (type) {
      this.title = type === 1 ? '新增品类' : '新增明细'
      this.addType = type
      this.formInline = {
        paidSheetId: this.form.id,
        materialName: '', // 品类名称
        startPrice: '', // 起始价格
        minimumBidRule: undefined, // 最小出价配置
        minimumBidRange: '', // 最小出价幅度
        triggerPrice: '', // 触发价格
        triggerMinimumBidRange: '', // 触发价格后最小出价幅度
        unit: '', // 单位
        quantity: '', // 数量
        remark: '', // 备注
      }
      if (type === 1) this.formInline.parentId = 0
      if (type === 2) this.formInline.parentId = this.parentId
      this.visible = true
    },
    // 新增
    async saveItem () {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          let result = null
          if (this.formInline.id) {
            result = await paidItemsPutRequest(this.formInline)
          } else {
            result = await paidItemsPostReturnVO(this.formInline)
          }
          if (result.code === 0) {
            if (this.formInline.parentId === 0) {
              if (this.form.bidStructure === '总额') {
                this.parentId = result.data.id
              }
            }
            this.getItems()
          } else {
            this.$message.error(result.message)
          }
          this.visible = false
        }
      })
    },
    // 保存
    async handleSave () {
      let data = {
        id: this.form.id,
        paidMaterialDTOS: []
      }
      if (this.form.bidStructure === '总额') {
        let item = this.itemData.length > 0 ? this.itemData[0] : ''
        if (item) {
          item.childPaidMaterialDTOS = this.detailData
        }
        data.paidMaterialDTOS = item
      } else {
        data.paidMaterialDTOS = this.itemData
      }
      const result = await paidItemsBatchSave(data)
      if (result.code === 0) {
        this.$message.success('保存成功')
        this.getItems()
      } else {
        this.$message.error(result.message)
      }
    }
  }
};
</script>

<style lang="less" scoped>
</style>
