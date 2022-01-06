<template>
  <div>
   <a-form-model
      layout="horizontal"
      ref="ruleForm"
      :maskClosable="false"
      :model="form"
      :rules="rules"
      class="page-form"
       style="height:500px;overflow-y:auto"
    >
      <a-row>
        <a-form-model-item
          prop="maxNumOfInvWeeks"
          label="最高库存周数"
            :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.maxNumOfInvWeeks" />
        </a-form-model-item>
      </a-row>
      <a-row>
        <a-form-model-item
          prop="onHand"
          label="在手库存"
         :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.onHand" />
        </a-form-model-item>
      </a-row>

      <a-row>
        <a-form-model-item
          prop="targetInvTurnover"
          label="目标库存周转率"
             :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.targetInvTurnover" />
        </a-form-model-item>
      </a-row>
    
          <a-divider style="font-weight: bolder;color:#666">
     全局变量
    </a-divider>
      <a-row>
        <a-form-model-item
          prop="historyDemandWeeks"
          label="历史需求周数"
             :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.historyDemandWeeks" />
        </a-form-model-item>
      </a-row>
            <a-row>
        <a-form-model-item
          prop="weeksOfFutureDemand"
          label="未来需求周数"
          :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.weeksOfFutureDemand" />
        </a-form-model-item>
      </a-row>
                  <a-row>
        <a-form-model-item
          prop="classADeliveryFreqNumOfWeeks"
          label="	A类发货频次周数"
              :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.classADeliveryFreqNumOfWeeks" />
        </a-form-model-item>
      </a-row>
        <a-row>
        <a-form-model-item
          prop="classBDeliveryFreqNumOfWeeks"
          label="	B类发货频次周数"
            :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.classBDeliveryFreqNumOfWeeks" />
        </a-form-model-item>
      </a-row>
           <a-row>
        <a-form-model-item
          prop="classCDeliveryFreqNumOfWeeks"
          label="	C类发货频次周数"
             :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.classCDeliveryFreqNumOfWeeks" />
        </a-form-model-item>
      </a-row>
           <a-row>
        <a-form-model-item
          prop="domesticForecastConsumpCalAlloRatio"
          label="	国内预测用量计算分配比例"
        :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.domesticForecastConsumpCalAlloRatio" />
        </a-form-model-item>
      </a-row>
                <a-row>
        <a-form-model-item
          prop="classAMinInvCalMultiple"
          label="A类最小库存计算倍数"
           :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.classAMinInvCalMultiple" />
        </a-form-model-item>
      </a-row>
                  <a-row>
        <a-form-model-item
          prop="classBMinInvCalMultiple"
          label="B类最小库存计算倍数"
            :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.classBMinInvCalMultiple" />
        </a-form-model-item>
      </a-row>
                   <a-row>
        <a-form-model-item
          prop="classCMinInvCalRatio"
          label="C类最小库存计算比例"
          :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.classCMinInvCalRatio" />
        </a-form-model-item>
      </a-row>
                    <a-row>
        <a-form-model-item
          prop="discreteDistAtOrderPointToCalPro"
          label="离散分布在订货点计算比例"
             :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.discreteDistAtOrderPointToCalPro" />
        </a-form-model-item>
      </a-row>
                       <a-row>
        <a-form-model-item
          prop="invStatusToDetermineNextThreshold"
          label="库存状态判断下阈值"
        :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.invStatusToDetermineNextThreshold" />
        </a-form-model-item>
      </a-row>
           <a-row>
        <a-form-model-item
          prop="determineUpperThresholdOfInvStatus"
          label="库存状态判断上阈值"
           :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.determineUpperThresholdOfInvStatus" />
        </a-form-model-item>
      </a-row>
          <a-row>
        <a-form-model-item
          prop="dullInvWeeks"
          label="	呆滞库存周数"
            :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.dullInvWeeks" />
        </a-form-model-item>
      </a-row>
           <a-row>
        <a-form-model-item
          prop="weeksOfOverdueStock"
          label="	过期库存周数"
          :labelCol="{ span: 6 }"
          :wrapperCol="{ span: 16 }"
        >
          <a-input v-model="form.weeksOfOverdueStock" />
        </a-form-model-item>
      </a-row>
    
    </a-form-model>
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="editRole">保存</a-button>
          <a-button @click="dataClose">返回</a-button>
        </a-space>
      </a-row>
  </div>
</template>
<script>
import { materialputRequest } from "@/services/material/material";

export default {
  name: "AnalyseAdd",
  props: ["editData"],
  data() {
    return {
      permissionIdList: [],
      Typedata: [],
      butNewShow: true,
      form: {
           maxNumOfInvWeeks: "",
        onHand: "",
        targetInvTurnover: "",
        historyDemandWeeks: "", // 历史需求周数
        weeksOfFutureDemand: '', // 未来需求周数
        classADeliveryFreqNumOfWeeks: '', // A
        classBDeliveryFreqNumOfWeeks: '', // B
        classCDeliveryFreqNumOfWeeks: '', //C
        domesticForecastConsumpCalAlloRatio: '', //国内预测用量计算分配比例
        classAMinInvCalMultiple: '', // A
        classBMinInvCalMultiple: '', //B
        classCMinInvCalRatio: '', //C
        discreteDistAtOrderPointToCalPro: '',
        invStatusToDetermineNextThreshold: '',
        determineUpperThresholdOfInvStatus: '',
        dullInvWeeks: '',
        weeksOfOverdueStock: ''
      },
      rules: {
        maxNumOfInvWeeks: [
          {
            required: true,
            message: "最高库存周数不能为空",
            trigger: "blur",
          },
        ],
        onHand: [
          {
            required: true,
            message: "在手库存不能为空",
            trigger: "blur",
          },
        ],
        targetInvTurnover: [
          {
            required: true,
            message: "目标库存周转率不能为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  watch: {
     editData(newForm, oldForm) {
      this.form= newForm;
    }
  },
  created() {
  },
  methods: {
    // 编辑
    editRole() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          materialputRequest({
            ...this.form,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("保存成功");
              this.dataClose();
            } else {
              this.$message.success(response.message);
            }
          });
        } else {
          return false;
        }
      });
    },
    dataClose() {
      this.$emit("getAll");
      this.$emit("on-data-close");
      this.$refs.ruleForm.resetFields();
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