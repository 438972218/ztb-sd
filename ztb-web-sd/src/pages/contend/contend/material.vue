<template>
  <div>
    <div class="operator">
      <a-button
        @click="addNew"
        style="margin-bottom: 10px"
        type="primary"
        v-if="showBut&& this.data.length < 1"
        >添加</a-button
      >
    </div>
    <a-table :columns="columns" :data-source="data">
      <template slot="demandedDate" slot-scope="text">
        <span>
          {{ $moment(Number(text)).format("YYYY-MM-DD") }}
        </span>
      </template>
      <template slot="operation" slot-scope="record">
        <span @click="c_delete(record.id)" class="operation-style"> 删除 </span>
      </template>
    </a-table>
    <a-modal
      v-model="visible"
      width="500px"
      :footer="null"
      :centered="true"
      title="添加"
    >
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :rules="rules"
        :model="form"
      >
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row>
               <a-form-model-item
            prop="materialCode"
            label="物品编码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-select
              v-model="form.materialCode"
              placeholder="请选择物品"
              @change="itemChange"
            >
              <a-select-option
                v-for="item in itemList"
                :key="item.id"
                :value="item.itemCode"
                >{{ item.itemCode }}</a-select-option
              >
            </a-select>
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="materialDescription"
            label="物品描述"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="form.materialDescription" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="referToBrand"
            label="参考品牌"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.referToBrand" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="materialClassify"
            label="物品分类"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="form.materialClassify" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="demandedDate"
            label="需求日期"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-date-picker style="width: 100%" v-model="form.demandedDate" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="demandedQuantity"
            label="需求数量"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input-number
              style="width: 100%"
              v-model="form.demandedQuantity"
            />
          </a-form-model-item>
          <a-row>
            <a-form-model-item
              prop="unit"
              label="计量单位"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input disabled v-model="form.unit" />
            </a-form-model-item>
            <a-row>
              <a-form-model-item
                prop="taxInclusive"
                label="含税"
                :labelCol="{ span: 6 }"
                :wrapperCol="{ span: 17 }"
              >
                <a-checkbox v-model="form.taxInclusive"></a-checkbox>
              </a-form-model-item>
            </a-row>
          </a-row>
        </a-row>
      </a-form-model>
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="addItems">新建</a-button>
          <a-button @click="dataClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
  </div>
</template>
<script>
import {
  paidItemsGetRequest,
  paidItemsPostRequest,
  paidItemsDeleteRequest,
} from "@/services/source/contend";
import { permissionsEdit } from "@/utils/methods";
import { itemGetRequest } from "@/services/basis/index";

const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "库存组织",
    dataIndex: "inventoryOrganization",
  },
  {
    title: "物品编码",
    dataIndex: "materialCode",
  },
  {
    title: "物品描述",
    dataIndex: "materialDescription",
  },
  {
    title: "参考品牌",
    dataIndex: "referToBrand",
  },
  {
    title: "物品分类",
    dataIndex: "materialClassify",
  },
  {
    title: "需求日期",
    dataIndex: "demandedDate",
    scopedSlots: { customRender: "demandedDate" },
  },
  {
    title: "需求数量",
    dataIndex: "demandedQuantity",
  },
  {
    title: "计量单位",
    dataIndex: "unit",
  },
  {
    title: "是否含税",
    dataIndex: "taxInclusive",
  },
  {
    title: "操作",
    scopedSlots: { customRender: "operation" },
  },
];

export default {
  name: "material",
  props: {
    contendId: {
      type: [String, null],
    },
  },
  data() {
    return {
      itemList: [],
      form: {
        inventoryOrganization: "", // 库存组织
        materialCode: "", // 	物品编码
        materialDescription: "", // 物品描述
        referToBrand: "", // 参考品牌
        materialClassify: "", // 	物品分类
        demandedDate: "", // 需求日期
        demandedQuantity: "", // 需求数量
        unit: "", // unit
        taxInclusive: "", // 含税
      },
      rules: {
        materialCode: [
          {
            required: true,
            message: "物品编码不能为空",
            trigger: "blur",
          },
        ],
        materialClassify: [
          {
            required: true,
            message: "物品分类不能为空",
            trigger: "blur",
          },
        ],
        demandedQuantity: [
          {
            required: true,
            message: "需求数量不能为空",
            trigger: "blur",
          },
        ],
        unit: [
          {
            required: true,
            message: "单位不能为空",
            trigger: "blur",
          },
        ],
      },
      id: this.contendId,
      visible: false,
      data: [],
      columns,
      contendType: this.$store.state.account.contendType,
      disableds: true,
      showBut: true,
    };
  },
  created() {
    if (this.id) {
      this.getAll();
      this.getItemAll();
    }

    if (this.contendType === "add" || this.contendType === 0) {
      this.disableds = false;
      this.showBut = true;
    } else {
      this.showBut = !permissionsEdit("bid:edit");
    }
    // this.disableds =
    //   this.contendType === "edit"
    //     ? false
    //     : this.contendType === "add"
    //     ? false
    //     : true;
    // this.showBut =
    //   this.contendType === "edit"
    //     ? true
    //     : this.contendType === "add"
    //     ? true
    //     : false;
    if (!this.showBut) {
      this.columns.pop();
    } else if (this.contendType === 0) {
      this.showBut = false;
    }
  },
  methods: {
    getAll() {
      paidItemsGetRequest({
        paidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        if(response.data.records){
        this.data = response.data.records;
        }
      });
    },
            // 所有物品请求
    getItemAll() {
      itemGetRequest({
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        if (response.data.records) {
          this.itemList = response.data.records;
        }
      });
    },
        // 获取物品信息
    itemChange(e) {
      let target = this.itemList.filter((item) => e === item.itemCode)[0];
      this.form.materialDescription = target.itemDesc;
      this.form.referToBrand = target.brand;
      this.form.materialClassify = target.itemType;
      this.form.unit = target.basicMeasure;
    },
    addItems() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          paidItemsPostRequest({
            paidSheetId: this.id,
            ...this.form,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加成功");
              this.visible = false;
              this.getAll();
            } else {
              this.$message.error(response.message);
            }
          });
        } else {
          return false;
        }
      });
    },
    c_delete(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          paidItemsDeleteRequest(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
              that.getAll();
            } else {
              that.$message.error(response.message);
            }
            that.getAll();
          });
        },
        onCancel() {},
      });
    },
    addNew() {
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.ruleForm.resetFields();
      });
    },
    dataClose() {
      this.visible = false;
    },
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  button {
    float: right;
  }
}
</style>
