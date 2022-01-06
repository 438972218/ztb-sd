<template>
  <div>
    <a-form-model
      ref="ruleForm"
      :rules="rules"
      :model="form"
      layout="horizontal"
    >
      <!-- <div :class="advanced ? null: 'fold'"> -->
      <div>
        <a-row>
          <a-col :md="5" :sm="24">
            <a-form-model-item
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 19, offset: 0 }"
              prop="objectA"
            >
              <a-input v-model="form.objectA" />
            </a-form-model-item>
          </a-col>
          <a-col :md="5" :sm="24">
            <a-form-model-item
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 19, offset: 0 }"
              prop="relation"
            >
              <a-select v-model="form.relation">
                <a-select-option
                  v-for="item in dataList"
                  :key="item.id"
                  :value="item.symbol"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :md="5" :sm="24">
            <a-form-model-item
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 19, offset: 0 }"
              prop="objectB"
            >
              <a-input v-model="form.objectB" />
            </a-form-model-item>
          </a-col>
          <a-col :md="9" :sm="24">
            <span style="float: right; margin-top: 3px">
              <a-button type="primary" @click="addRules">添加</a-button>
              <a-button
                type="primary"
                style="margin-left: 5px"
                @click="createClick"
                >生成表达式</a-button
              >
            </span>
          </a-col>
        </a-row>
        <a-row v-show="this.conditionList.length !== 0">
          <a-col :md="5" :sm="24">
            <a-form-model-item
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 18, offset: 0 }"
              prop="condition"
            >
              <a-select default-value="AND" v-model="form.condition">
                <a-select-option value="AND">AND</a-select-option>
                <a-select-option value="OR">OR</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row style="border: 1px solied #ccc">
          <a-tag
            v-for="(item, index) in conditionList"
            :key="index"
            :closable="judgeFun(index)"
            @close="log($event, index)"
          >
            {{ item }}
          </a-tag>
        </a-row>
        <a-row style="margin: 20px">
          {{ ruleData }}
        </a-row>
      </div>
    </a-form-model>
    <a-row style="text-align: center">
      <a-space>
        <a-button type="primary" @click="newRole">保存</a-button>
        <a-button @click="ruleClose">返回</a-button>
      </a-space>
    </a-row>
  </div>
</template>

<script>
// import { tr } from 'date-fns/locale';
import { expressionRequest } from "@/services/process/workflow";
import { regular } from "@/utils/validate";
import { el } from 'date-fns/locale';

export default {
  name: "rules",
  props: {
    rulesModel: Boolean,
  },
  data () {
    return {
      ruleData: "",
      conditionList: [],
      dataList: [],
      form: {
        objectA: "",
        relation: "==",
        objectB: "",
        condition: "AND",
      },
      rules: {
        objectA: [
          {
            required: true,
            message: "条件不能为空",
            trigger: "blur",
          },
          {
            pattern: regular.enText,
            required: true,
            message: regular.enTextMsg,
            trigger: "blur",
          },
        ],
        objectB: [
          {
            required: true,
            message: "条件不能为空",
            trigger: "blur",
          },
          {
            pattern: regular.naturalNumber,
            required: true,
            message: regular.naturalNumberMsg,
            trigger: "blur",
          },
        ],
      },
    };
  },
  watch: {
    rulesModel (newVal, oldVal) {
      if (newVal) {
        (this.ruleData = ""), (this.conditionList = []);
        this.$refs.ruleForm.resetFields();
      }
    },
  },
  created () {
    this.getExpression();
  },
  methods: {
    judgeFun (index) {
      if (index % 2 === 0) {
        return false;
      } else {
        return true;
      }
    },
    log (e, index) {
      e.preventDefault();
      this.conditionList.splice(index, 2);
    },
    addRules () {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          if (this.conditionList.length !== 0) {
            this.conditionList.push(this.form.condition);
            this.conditionList.push(
              "(" + this.form.objectA + this.form.relation + this.form.objectB + ")"
            );
          } else {
            this.conditionList.push(
              "(" + this.form.objectA + this.form.relation + this.form.objectB + ")"
            );
          }
          // userRequestAdd({
          //   ...this.form,
          // }).then((response) => {
          //   if (response.code === 0) {
          //     this.$message.success("添加成功");
          //     this.dataClose();
          //   } else {
          //     this.$message.success(response.data.message);
          //   }
          // });
        } else {
          return false;
        }
      });
      // if (this.form.objectA === "" || this.form.objectB === "") {
      //   this.$message.warning("请输入条件！");
      // } else 
    },
    createClick () {
      if (this.conditionList.length < 2) {
        this.$message.warning("请生成最少两个关系！");
      } else {
        this.ruleData = "";
        let data = (this.conditionList.length - 1) / 2;
        for (let i = 0; i <= data - 1; i++) {
          if (
            this.conditionList[2 * i + 1] !== this.conditionList[2 * i + 3] &&
            this.conditionList[2 * i + 3]
          ) {
            if (2 * i + 2 === 2) {
              this.ruleData =
                "(" +
                this.ruleData +
                this.conditionList[2 * i] +
                this.conditionList[2 * i + 1] +
                this.conditionList[2 * i + 2] +
                ")";
            } else {
              this.ruleData =
                "(" +
                this.ruleData +
                this.conditionList[2 * i + 1] +
                this.conditionList[2 * i + 2] +
                ")";
            }
          } else {
            if (2 * i + 2 === 2) {
              this.ruleData =
                this.ruleData +
                this.conditionList[2 * i] +
                this.conditionList[2 * i + 1] +
                this.conditionList[2 * i + 2];
            } else {
              this.ruleData =
                this.ruleData +
                this.conditionList[2 * i + 1] +
                this.conditionList[2 * i + 2];
            }
          }
        }
      }

    },
    ruleClose () {
      this.$emit("on-rule-close");
    },
    newRole () {
      this.$emit("rulesPass", this.ruleData);

      this.ruleClose();
    },
    // 查询所有表达式
    getExpression () {
      expressionRequest({}).then((response) => {
        this.dataList = response.data;
      });
    },
  },
};
</script>

<style>
</style>
