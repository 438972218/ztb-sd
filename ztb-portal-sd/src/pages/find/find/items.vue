<template>
  <div>
    <div class="operator">
      <a-button
        @click="addNew"
        style="margin-bottom: 10px"
        type="primary"
        v-if="showBut"
        >添加</a-button
      >
    </div>
    <a-table :columns="columns" :data-source="data">
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
          <a-form-model-item
            prop="materialDescription"
            label="物品描述"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="form.materialDescription" />
          </a-form-model-item>
          <a-form-model-item
            prop="quoteDetails"
            label="报价明细"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.quoteDetails" />
          </a-form-model-item>
          <a-form-model-item
            prop="materialExplain"
            label="物品说明"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.materialExplain" />
          </a-form-model-item>
          <a-form-model-item
            prop="quantity"
            label="数量"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input-number style="width: 100%" v-model="form.quantity" />
          </a-form-model-item>
          <a-form-model-item
            prop="unit"
            label="单位"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="form.unit" />
          </a-form-model-item>
        </a-row>
        <a-row> </a-row>
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
  inquiryItemsPostRequest,
  inquiryItemsGetRequest,
  inquiryItemsDeleteRequest,
} from "@/services/source/find";
import { permissionsEdit } from "@/utils/methods";
import { itemGetRequest } from "@/services/basis/index";
export default {
  name: "FindItems",
  props: {
    findId: {
      type: [String, null],
    },
  },
  data() {
    return {
      itemList: [],
      form: {
        inventoryOrganization: "", // 库存组织
        materialCode: "", // 物品编码
        materialDescription: "", // 物品描述
        quoteDetails: "", // 报价明细
        materialExplain: "", // 物品说明
        quantity: "", // 数量
        unit: "", // 单位
      },
      visible: false,
      data: [],
      id: this.findId,
      columns: [
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
        // {
        //   title: "物品分类",
        //   dataIndex: "a4",
        // },
        {
          title: "报价明细",
          dataIndex: "quoteDetails",
        },
        {
          title: "物品说明",
          dataIndex: "materialExplain",
        },
        {
          title: "数量",
          dataIndex: "quantity",
        },
        {
          title: "单位",
          dataIndex: "unit",
        },
        {
          title: "操作",
          scopedSlots: { customRender: "operation" },
        },
      ],
      findtype: this.$store.state.account.findType,
      disableds: true,
      showBut: true,
      rules: {
        inventoryOrganization: [
          {
            required: true,
            message: "库存组织不能为空",
            trigger: "blur",
          },
        ],
        materialCode: [
          {
            required: true,
            message: "物品编码不能为空",
            trigger: "blur",
          },

        ],
        materialDescription: [
          {
            required: true,
            message: "物品描述不能为空",
            trigger: "blur",
          },
        ],
        quantity: [
          {
            required: true,
            message: "数量不能为空",
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
    };
  },
  created() {
    if (this.id) {
      this.getAll();
      this.getItemAll();
    }
    if (this.findtype === "add" || this.findtype === 0) {
      this.disableds = false;
    } else {
      this.disableds = permissionsEdit("bid:edit");
    }

    if (this.findtype === "add" || this.findtype === 0) {
      this.showBut = true;
    } else {
      this.showBut = !permissionsEdit("bid:edit");
    }

    // this.disableds =
    //   this.findtype === "edit" ? false : this.findtype === "add" ? false : true;
    // this.showBut =
    //   this.findtype === "edit" ? true : this.findtype === "add" ? true : false;
    if (!this.showBut) {
      this.columns.pop();
    } else if (this.findtype === 0) {
      this.showBut = false;
    }
  },
  methods: {
    getAll() {
      inquiryItemsGetRequest({
        inquirySheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
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
      this.form.unit = target.basicMeasure;
    },
    addItems() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          inquiryItemsPostRequest({
            inquirySheetId: this.id,
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
          inquiryItemsDeleteRequest(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getAll();
          });
        },
        onCancel() {},
      });
    },
    dataClose() {
      this.visible = false;
    },
    addNew() {
      this.visible = true;
      this.$nextTick(()=>{
      this.$refs.ruleForm.resetFields();
})
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
