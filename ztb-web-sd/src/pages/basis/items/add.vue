<template>
  <div>
    <a-card class="card-top">
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :rules="rules"
        :model="form"
      >
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="itemCode"
              class="form-item-style"
              label="品类编码"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.itemCode" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="itemName"
              class="form-item-style"
              label="品类名称"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.itemName" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="genName"
              class="form-item-style"
              label="通用名"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.genName" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="itemType"
              class="form-item-style"
              label="品类分类"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.itemType" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="brand"
              class="form-item-style"
              label="品牌"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.brand" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="originPlace"
              class="form-item-style"
              label="产地"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.originPlace" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="basicMeasure"
              class="form-item-style"
              label="基本计量单位"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.basicMeasure" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="ifAssitMeasure"
              class="form-item-style"
              label="启用辅助计量单位"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select v-model="form.ifAssitMeasure" placeholder="请选择">
                <a-select-option value="1">是</a-select-option>
                <a-select-option value="2">否</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="assitMeasure"
              class="form-item-style"
              label="辅助计量单位"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.assitMeasure" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="ifImport"
              class="form-item-style"
              label="是否进口"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select v-model="form.ifImport" placeholder="请选择">
                <a-select-option value="1">是</a-select-option>
                <a-select-option value="2">否</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="agent"
              class="form-item-style"
              label="代理商"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.agent" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="packageUnit"
              class="form-item-style"
              label="包装单位"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.packageUnit" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="roughWeight"
              class="form-item-style"
              label="毛重"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.roughWeight" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="netWeight"
              class="form-item-style"
              label="净重"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.netWeight" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="weightUnit"
              class="form-item-style"
              label="重量单位"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select
                v-model="form.weightUnit"
                placeholder="请选择"
              >
                <a-select-option
                  v-for="item in weightUnitList"
                  :key="item.id"
                  :value="item.meaning"
                  >{{ item.meaning }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="specification"
              class="form-item-style"
              label="规格"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.specification" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="model"
              class="form-item-style"
              label="型号"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.model" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="manufacturer"
              class="form-item-style"
              label="制造商"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.manufacturer" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="volume"
              class="form-item-style"
              label="体积"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.volume" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="volumeUnit"
              class="form-item-style"
              label="体积单位"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.volumeUnit" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="circulationCode"
              class="form-item-style"
              label="商品流通码"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.circulationCode" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="ifTax"
              class="form-item-style"
              label="是否计税"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select v-model="form.ifTax" placeholder="请选择">
                <a-select-option value="1">是</a-select-option>
                <a-select-option value="2">否</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="defaultTaxType"
              class="form-item-style"
              label="默认税种"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select
                v-model="form.defaultTaxType"
                placeholder="请选择"
                disabled
              >
                <a-select-option value="1">是</a-select-option>
                <a-select-option value="2">否</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="defaultTaxRate"
              class="form-item-style"
              label="默认税率"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.defaultTaxRate" disabled />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="itemManagementMethods"
              class="form-item-style"
              label="品类管理方式"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.itemManagementMethods" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="itemQuotaManagement"
              class="form-item-style"
              label="品类额度管理类型"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.itemQuotaManagement" />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="batchNumberRule"
              class="form-item-style"
              label="批号规则"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.batchNumberRule" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="ifUseSale"
              class="form-item-style"
              label="是否用于销售"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select v-model="form.ifUseSale" placeholder="请选择">
                <a-select-option value="1">是</a-select-option>
                <a-select-option value="2">否</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="ifUsePurchase"
              class="form-item-style"
              label="是否用于采购"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select v-model="form.ifUsePurchase" placeholder="请选择">
                <a-select-option value="1">是</a-select-option>
                <a-select-option value="2">否</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8">
            <a-form-model-item
              prop="productHierarchy"
              class="form-item-style"
              label="产品层次"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input v-model="form.productHierarchy" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="24" :xl="24">
            <a-form-model-item
              prop="itemDesc"
              class="form-item-style"
              label="品类描述"
              :labelCol="{ span: 2 }"
              :wrapperCol="{ span: 21 }"
            >
              <a-textarea
                v-model="form.itemDesc"
                :auto-size="{ minRows: 2, maxRows: 6 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-row>
      <a-col :span="24" class="btn-sty" style="margin-bottom: 20px">
        <a-button type="primary" @click="addItem" v-if="this.itemType === 'add'"> 保存 </a-button>
        <a-button type="primary"    @click="editItem" v-if="this.itemType === 'edit'"> 编辑 </a-button>
        <a-button @click="back()"> 返回 </a-button>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import {
  itemPostRequest,
  itemPutRequest
} from "@/services/basis/index";
import { queryDictionarys } from "@/utils/methods";
export default {
  name: "Addfind",
  components: {},
  props: {
    num: {
      type: String,
    },
  },
  data() {
    return {
      form: {
        itemCode: "", //品类编码
        itemName: "", // 	品类名称
        genName: "", // 	通用名称
        itemType: "", // 品类分类
        brand: "", // 品牌
        originPlace: "", //产地
        basicMeasure: "", // 基本计量单位
        ifAssitMeasure: "", //启用辅助计量
        assitMeasure: "", //辅助计量单位
        ifImport: "", //是否进口
        agent: "", //	代理商
        packageUnit: "", //包装单位
        roughWeight: "", //毛重
        netWeight: "", //净重
        weightUnit: "", //重量单位
        specification: "", //规格
        model: "", //型号
        manufacturer: "", //制造商
        volume: "", //体积
        volumeUnit: "", //体积单位
        circulationCode: "", //商品流通码
        ifTax: "", //是否计税
        defaultTaxType: "", //默认税种
        defaultTaxRate: "", //	默认税率
        itemManagementMethods: "", //品类管理方式
        itemQuotaManagement: "", //品类额度管理
        batchNumberRule: "", //	批号规则
        ifUseSale: "", //是否用于销售
        ifUsePurchase: "", //	是否用于采购
        productHierarchy: "", //产品层次
        itemDesc: "", //品类描述
      },
           rules: {
        itemName: [
          {
            required: true,
            message: "品类名称不能为空",
            trigger: "blur",
          },
        ],
        itemType: [
          {
            required: true,
            message: "品类分类不能为空",
            trigger: "blur",
          },
        ],
        basicMeasure: [
          {
            required: true,
            message: "基本计量单位不能为空",
            trigger: "blur",
          },
        ],
        ifUsePurchase: [
          {
            required: true,
            message: "该项不能为空",
            trigger: "change",
          },
        ],
        ifUsePurchase: [
          {
            required: true,
            message: "该项不能为空",
            trigger: "change",
          },
        ]
      },
      itemType: this.$store.state.account.itemType,
      data1: ["1", "2"],
      weightUnitList: [],
      itemId: ''
    };
  },
  created() {
    // 重量单位
    queryDictionarys("weightUnit").then((res) => {
      this.weightUnitList = res;
    });
    if (this.itemType === "edit") {
      this.form = this.$store.state.account.itemData;
      this.itemId = this.form.id;}
  },
  methods: {
    // 添加
    addItem(){
     this.$refs.ruleForm.validate((valid) => {
        if (valid) {
            itemPostRequest({
              ...this.form,
            }).then((response) => {
              if (response.code === 0) {
                this.$message.success("添加成功");
                this.back()
              } else {
                this.$message.error(response.message);
              }
            });
        } else {
          return false;
        }
      });
    },
    // 编辑
    editItem(){
      itemPutRequest({
        ...this.form,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("修改成功");
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    back() {
      this.$router.push({
        path: "/basis/items",
      });
    },
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  text-align: center;
  margin: 20px 0;
  button {
    margin-left: 10px;
  }
}
</style>
