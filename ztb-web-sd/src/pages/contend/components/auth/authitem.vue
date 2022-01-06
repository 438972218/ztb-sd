<template>
  <div>
    <a-row>
      <!-- <a-row style="padding-bottom: 5px">
        <a-col :span="2">
          <span style="font-size: 22px; font-weight: 550">品类</span>
        </a-col>
      </a-row> -->
      <a-row style="padding-bottom: 5px">
        <a-col
          :span="24"
          v-if="form.requestStatusName !== '已归档' && !getUserInfo"
        >
          <a-button
            :disabled="selectedRowKeys.length === 0 || mandateVO"
            type="primary"
            @click="toauth"
            >授权</a-button
          >
        </a-col>
      </a-row>
      <a-table
        :columns="columns"
        :row-selection="rowSelection"
        :data-source="itemData"
      >
        <span
          slot="operation"
          slot-scope="text, record"
          v-if="form.requestStatusName !== '已归档' && !getUserInfo"
        >
          <a
            v-if="record.paidMaterialMandateVO != null"
            @click="handleCancel(record)"
            >取消授权</a
          >
        </span>
      </a-table>
    </a-row>

    <!-- 授权供应商 -->
    <a-modal
      :title="'授权品类:' + formInline.materialName"
      :visible="visible"
      width="700px"
      @ok="auth"
      @cancel="cancel"
    >
      <a-table
        :row-selection="rowSelection2"
        :columns="columns2"
        :data-source="itemData2"
      >
        <template
          v-for="col in ['confirmedPrice']"
          :slot="col"
          slot-scope="text, record"
        >
          <div :key="col">
            <a-input
              v-if="record.editable"
              style="margin: -5px 0"
              :value="text"
              @change="
                (e) => handleChange(e.target.value, record.vendorId, col)
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
import { getPaidMaterialPaidSheetId, getVendorLastRanking, postPaidMaterialMandate, deletePaidMaterialMandate } from "@/services/source/contend";
const columns = [
  {
    title: '品类名称',
    dataIndex: 'materialName',
    key: 'materialName',
  },
  {
    title: '出价',
    dataIndex: 'paidMaterialMandateVO.price',
    key: 'price',
  },
  {
    title: '授权供应商',
    dataIndex: 'paidMaterialMandateVO.vendorVO.name',
    key: 'name',
  },
  {
    title: '确定价格',
    dataIndex: 'paidMaterialMandateVO.confirmedPrice',
    key: 'confirmedPrice',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
];

const columns2 = [
  {
    title: '供应商名称',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  {
    title: '总价',
    dataIndex: 'money',
    key: 'money',
  },
  {
    title: '确定价格',
    dataIndex: 'confirmedPrice',
    key: 'confirmedPrice',
    scopedSlots: { customRender: 'confirmedPrice' },
  },
  // {
  //   title: '操作',
  //   dataIndex: 'operation',
  //   key: 'operation',
  //   scopedSlots: { customRender: 'operation' },
  // }
];
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
    form: {
      type: Object,
      default: {}
    },
    getUserInfo: {
      type: Boolean,
      default: false
    }
  },
  name: "authitem",
  data () {
    return {
      title: '新增品类',
      editingKey: '',
      visible: false,
      formInline: {
        id: '',
        materialName: '',
        materialId: '',
      },
      formInline2: {//授权塔式服务器
        vendorId: '',
        confirmedPrice: '',
        offerUser: '',
      },
      columns,
      columns2,
      itemData: [],
      itemData2: [],
      selectedRowKeys2: [],
      selectedRowKeys: [],
      mandateVO: null
    }
  },
  computed: {
    rowSelection () {
      return {
        type: 'radio',
        selectedRowKeys: this.selectedRowKeys,
        onChange: (selectedRowKeys, selectedRows) => {
          this.selectedRowKeys = [...selectedRowKeys];
          this.formInline.materialName = selectedRows[0].materialName;
          this.formInline.id = selectedRows[0].id;
          this.formInline.materialId = selectedRows[0].id;
          this.mandateVO = selectedRows[0].paidMaterialMandateVO
        },
        getCheckboxProps: record => ({
          props: {
            disabled: false,
            name: record.name,
          },
        }),
      };
    },
    rowSelection2 () {
      return {
        type: 'radio',
        selectedRowKeys: this.selectedRowKeys2,
        onChange: (selectedRowKeys, selectedRows) => {
          this.selectedRowKeys2 = [...selectedRowKeys];
          this.formInline2.vendorId = selectedRows[0].vendorId;
          this.formInline2.confirmedPrice = selectedRows[0].confirmedPrice;
          this.formInline2.offerUser = selectedRows[0].offerUser;
          const newData = [...this.itemData2].map(item => ({ ...item, editable: false, confirmedPrice: '' }));
          const target = newData.filter(item => item.vendorId == selectedRows[0].vendorId)[0];
          if (target) {
            target.editable = true;
            this.itemData2 = newData;
          }
        },
        getCheckboxProps: record => ({
          props: {
            disabled: !record.checkable,
            name: record.name,
          },
        }),
      };
    },
  },
  mounted () {
    // this.form = JSON.parse(sessionStorage.getItem('contendForm'))
    this.getItems();//获取品类
  },
  methods: {
    cancel () {
      this.visible = false;
      this.selectedRowKeys2 = [];
    },
    async handleCancel (record) {//取消授权
      const result = await deletePaidMaterialMandate(record.paidMaterialMandateVO.id);
      if (result.code === 0) {
        this.$message.success("取消授权成功");
        this.getItems();//获取品类
      } else {
        this.$message.error(result.message);
      }
    },
    //授权
    async auth () {
      const newData = [...this.itemData2];
      const target = newData.filter(item => item.vendorId == this.formInline2.vendorId)[0];
      if (target) {
        const param = {
          paidMaterialId: this.formInline.materialId,
          confirmedPrice: target.confirmedPrice,
          price: target.money,
          paidVendorId: target.vendorId,
          paidVendorUserName: target.offerUser,
        };
        const result = await postPaidMaterialMandate(param);
        if (result.code === 0) {
          this.$message.success("授权成功");
          this.visible = false;
          this.getItems();//获取品类
          this.$emit('putAuthorization')
        } else {
          this.$message.error(result.message);
        }
      } else {
        this.$message.success("请选择授权供应商");
      }
    },
    async toauth () {//授权
      const param = {
        materialId: this.formInline.materialId,
        requestId: this.form.requestId,
      }
      const result = await getVendorLastRanking(param);
      if (result.code === 0) {
        this.itemData2 = result.data ? result.data.totalRanking.map(item => ({ ...item, editable: false, checkable: true })) : []
      } else {
        this.$message.error(result.message);
      }
      if (this.formInline.id == '') {
        this.$message.warning("请选择品类");
        return;
      }
      this.visible = true;
    },
    // 竞价物品查询
    async getItems () {
      this.selectedRowKeys2 = [];
      this.selectedRowKeys = []
      const result = await getPaidMaterialPaidSheetId({ requestId: this.form.requestId })
      if (result.code === 0) {
        this.itemData = result.data ? result.data.map(item => ({ ...item, editable: false })) : []
      }
    },
    handleChange (value, key, column) {
      const newData = [...this.itemData2];
      const target = newData.filter(item => key === item.vendorId)[0];
      if (target) {
        target[column] = value;
        this.itemData2 = newData;
      }
    },
  }
};
</script>
<style lang="less" scoped>
</style>
