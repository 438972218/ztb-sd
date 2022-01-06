<template>
  <div>
    <a-row>
      <a-row style="padding-bottom: 5px">
        <a-col :span="2">
          <span style="font-size: 22px; font-weight: 550">品类</span>
        </a-col>
        <a-col
          :span="22"
          v-if="
            form.requestStatusName === '待发布' &&
            !getUserInfo &&
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
            区分
          </a>
          <span v-else>{{ text }}</span>
        </template>
        <span
          slot="operation"
          slot-scope="text, record"
          v-if="form.requestStatusName === '待发布' && !getUserInfo"
        >
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

    <!-- 查看供应商 -->
    <a-modal
      title="供应商起始总价"
      :visible="visibleVendor"
      width="700px"
      @cancel="visibleVendor = false"
    >
      <div slot="footer">
        <a-space>
          <a-button @click="visibleVendor = false">取消</a-button>
          <a-button
            v-if="form.requestStatusName === '待发布' && !getUserInfo"
            type="primary"
            @click="updateVendor()"
            >确认</a-button
          >
        </a-space>
      </div>
      <a-table
        :row-selection="rowSelectionVendor"
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
            <a-input-number
              v-if="record.editable"
              style="margin: -5px 0; width: 100%"
              :value="text"
              :min="0"
              :precision="0"
              @change="(e) => handleChangeVendor(e, record.id, col)"
            />
            <template v-else>
              {{ text }}
            </template>
          </div>
        </template>
      </a-table>
    </a-modal>

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
          <a-col :span="24">
            <a-form-model-item label="数量" prop="quantity">
              <a-input-number
                style="width: 100%"
                v-model="formInline.quantity"
                :min="0"
                :precision="0"
                placeholder="请输入数量"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单位" prop="unit">
              <a-input v-model="formInline.unit" placeholder="请输入单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="addType !== 2">
            <a-form-model-item
              prop="startPrice"
              v-if="form.startPriceRule !== '区分'"
            >
              <span slot="label"
                >起始总价<a-tooltip>
                  <template slot="title"
                    >供应商首次报价不能低于/高于此价格，正向竞价若不想设置起始总价，请设置0，方向竞价若不想设置起始总价，请设置9999999999990</template
                  >
                  <a-icon
                    style="padding-left: 5px"
                    type="info-circle"
                  /> </a-tooltip
              ></span>
              <a-input-number
                style="width: 100%"
                v-model="formInline.startPrice"
                :min="0"
                :precision="0"
                placeholder="请输入起始总价"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="addType !== 2">
            <a-form-model-item prop="minimumBidRule">
              <span slot="label"
                >最小出价配置<a-tooltip>
                  <template slot="title"
                    >绝对阈值：供应商可以输入超过定义最小投标最小出价幅度的任何值；最小出价倍数：供应商只能输入最小出价幅度倍数的值</template
                  >
                  <a-icon
                    style="padding-left: 5px"
                    type="info-circle"
                  /> </a-tooltip
              ></span>
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
            <a-form-model-item prop="minimumBidRange">
              <span slot="label"
                >最小出价幅度<a-tooltip>
                  <template slot="title"
                    >在达到触发价格之前的最小降价幅度，用于供应商自身出价之间的比较</template
                  >
                  <a-icon
                    style="padding-left: 5px"
                    type="info-circle"
                  /> </a-tooltip
              ></span>
              <a-input-number
                style="width: 100%"
                v-model="formInline.minimumBidRange"
                :min="0"
                :precision="0"
                placeholder="请输入最小出价幅度"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="addType !== 2">
            <a-form-model-item prop="triggerPrice">
              <span slot="label"
                >触发价格<a-tooltip>
                  <template slot="title"
                    >采购设置的心理价位，到达此价格后供应商可以看到提示，到达此价格后出价幅度可单独设置，正向竞价若不想设置触发价格，可设置999999999999，反向竞价若不想设置触发价格，可设置0</template
                  >
                  <a-icon
                    style="padding-left: 5px"
                    type="info-circle"
                  /> </a-tooltip
              ></span>
              <a-input-number
                style="width: 100%"
                v-model="formInline.triggerPrice"
                :min="0"
                :precision="0"
                placeholder="请输入触发价格"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="addType !== 2">
            <a-form-model-item
              label="触发后最小出价幅度"
              prop="triggerMinimumBidRange"
            >
              <a-input-number
                style="width: 100%"
                v-model="formInline.triggerMinimumBidRange"
                :min="0"
                :precision="0"
                placeholder="请输入触发后最小出价幅度"
              />
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
  </div>
</template>

<script>
import {
  putPaidVendorBatchUpdate,
  paidVendorGetRequest,
  paidItemsBatchSave,
  getPaidMaterialPaidSheetById,
  paidItemsPostReturnVO,
  paidItemsPutRequest,
  paidItemsDeleteRequest,
} from "@/services/source/contend"
const columns1 = [
  {
    title: "细项",
    dataIndex: "materialName",
    key: "materialName",
  },
  {
    title: "数量",
    dataIndex: "quantity",
    key: "quantity",
  },
  {
    title: "单位",
    dataIndex: "unit",
    key: "unit",
  },
  {
    title: "备注",
    dataIndex: "remark",
    key: "remark",
  },
  {
    title: "操作",
    dataIndex: "operation",
    key: "operation",
    scopedSlots: { customRender: "operation" },
  },
]
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
    form: {
      type: Object,
      default: {},
    },
    show: {
      type: Boolean,
      default: true,
    },
    minimumBidRuleList: {
      type: Array,
      default: () => {
        return []
      },
    },
    columns: {
      type: Array,
      default: () => {
        return []
      },
    },
    getUserInfo: {
      type: Boolean,
      default: false
    }
  },
  name: "itemdetails",
  data () {
    return {
      title: "新增品类",
      parentId: "",
      visible: false,
      visibleVendor: false,
      rules: {
        triggerMinimumBidRange: [{ required: true, message: "请输入触发后最小出价幅度", trigger: "blur" }],
        triggerPrice: [{ required: true, message: "请输入触发价格", trigger: "blur" }],
        startPrice: [{ required: true, message: "请输入起始价格", trigger: "blur" }],
        minimumBidRange: [
          { required: true, message: "请选择最小出价配置", trigger: "change" },
        ],
        minimumBidRule: [
          { required: true, message: "请输入最小出价幅度", trigger: "blur" },
        ],
        materialName: [
          { required: true, message: "请输入内容", trigger: "blur" },
        ],
        unit: [{ required: true, message: "请输入单位", trigger: "blur" }],
        quantity: [{ required: true, message: "请输入数量", trigger: "blur" }],
      },
      // form: {
      //   bidStructure: "",
      // },
      addType: "",
      columnsVendor,
      columns1,
      formInline: {
        materialName: "", // 品类名称
        startPrice: 0, // 起始价格
        minimumBidRule: "最小出价倍数", // 最小出价配置
        minimumBidRange: "", // 最小出价幅度
        triggerPrice: "", // 触发价格
        triggerMinimumBidRange: "", // 触发价格后最小出价幅度
        unit: "", // 单位
        quantity: "", // 数量
        remark: "", // 备注
      },
      itemData: [],
      detailData: [],
      dataVendor: [], //区分价格供应商数据
      selectedDataVendor: [], //选中的供应商数据
      saveData: [],
      paginationVendor: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
    }
  },
  computed: {
    rowSelectionVendor () {
      return {
        onChange: (selectedRowKeys, selectedRows) => {
          this.selectedDataVendor = selectedRows
          const newData = [...this.dataVendor].map(item => ({
            ...item,
            editable: false,
          }))
          if (selectedRows.length != 0) {
            selectedRows.forEach(element => {
              const target = newData.filter(item => item.id == element.id)[0]
              if (target) {
                target.editable = true
              }
            })
          }
          this.dataVendor = newData
        },
        getCheckboxProps: record => ({
          props: {
            disabled: false, //
            name: record.name,
          },
        }),
      }
    },
  },
  mounted () {
    // this.form = JSON.parse(sessionStorage.getItem("contendForm"))
    this.getItems()
  },
  methods: {
    showVendor () {
      this.visibleVendor = true
      this.getDataVendor()
    },
    async updateVendor () {
      let isTrue = true
      this.dataVendor.forEach(item => {
        if (item.editable && !item.startPrice) {
          isTrue = false
        }
      })
      if (!isTrue) {
        this.$message.warning('起始总价不可为空')
        return
      }
      //修改供应商起始价格
      const result = await putPaidVendorBatchUpdate(this.dataVendor)
      if (result.code === 0) {
        this.$message.success("修改成功")
      } else {
        this.$message.error(result.message)
      }
      this.visibleVendor = false
    },
    // 查询供应商
    async getDataVendor () {
      const data = {
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
    handleChangeVendor (value, key, column) {
      const newData = [...this.dataVendor]
      const target = newData.filter(item => key === item.id)[0]
      if (target) {
        target[column] = value
        this.data = newData
      }
    },
    tableVendorChange (row) {
      this.paginationVendor = row
      this.getDataVendor()
    },
    // 竞价物品查询
    async getItems () {
      const result = await getPaidMaterialPaidSheetById(this.form.id)
      if (result.code === 0) {
        this.itemData = result.data ? result.data : []
        this.parentId = this.itemData.length > 0 ? this.itemData[0].id : ""
        this.detailData = result.data ? result.data[0].childPaidMaterialVOS : []
      }
    },
    // 编辑
    handleEdit (row, type) {
      this.addType = type
      this.title = type === 1 ? "编辑品类" : "编辑明细"
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
            that.$message.success("删除成功")
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      })
    },
    // 新增品类主项数据
    handleAddItem (type) {
      this.title = type === 1 ? "新增品类" : "新增明细"
      this.addType = type
      this.formInline = {
        paidSheetId: this.form.id,
        materialName: "", // 品类名称
        startPrice: 0, // 起始价格
        minimumBidRule: "最小出价倍数", // 最小出价配置
        minimumBidRange: "", // 最小出价幅度
        triggerPrice: "", // 触发价格
        triggerMinimumBidRange: "", // 触发价格后最小出价幅度
        unit: "", // 单位
        quantity: "", // 数量
        remark: "", // 备注
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
              if (this.form.bidStructure === "总额") {
                this.parentId = result.data.id
              }
            }
            this.getItems()
            this.visible = false
          } else {
            this.$message.error(result.message)
          }
        }
      })
    },
    // 保存
    async handleSave () {
      let data = {
        id: this.form.id,
        paidMaterialDTOS: [],
      }
      if (this.form.bidStructure === "总额") {
        let item = this.itemData.length > 0 ? this.itemData[0] : ""
        if (item) {
          item.childPaidMaterialDTOS = this.detailData
        }
        data.paidMaterialDTOS = item
      } else {
        data.paidMaterialDTOS = this.itemData
      }
      const result = await paidItemsBatchSave(data)
      if (result.code === 0) {
        this.$message.success("保存成功")
        this.getItems()
      } else {
        this.$message.error(result.message)
      }
    },
  },
}
</script>

<style lang="less" scoped></style>
