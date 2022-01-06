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
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="vendorId"
              class="form-item-style"
              label="供应商名称"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select
                v-model="form.vendorId"
                placeholder="请选择供应商名称"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in vendorList"
                  :key="item.id"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="name"
              class="form-item-style"
              label="调查表名称"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input
                placeholder="请输入调查表名称"
                v-model="form.name"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="responsiblePerson"
              class="form-item-style"
              label="负责的采购员"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-input
                placeholder="请输入负责的采购员"
                v-model="form.responsiblePerson"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="responseExpireTime"
              class="form-item-style"
              label="供应商响应截止时间"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-date-picker
                v-model="form.responseExpireTime"
                format="YYYY-MM-DD"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="bank"
              class="form-item-style"
              label="调查模板"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 17 }"
            >
              <a-select
                v-model="form.bank"
                @change="bankChange"
                placeholder="请选择调查模板"
                :disabled="disableds"
              >
                <a-select-option value="1">电子调查表</a-select-option>
                <a-select-option value="2">现场考察表</a-select-option>
                <a-select-option value="3">合格评审人</a-select-option>
                <a-select-option value="4">绩效考核表</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card>
      <a-table
        :columns="columns"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        :dataSource="dataSource"
      >
        <template
          v-for="col in ['type', 'name', 'content']"
          :slot="col"
          slot-scope="text, record"
        >
          <div :key="col">
            {{ record[col] }}
          </div>
        </template>
        <template slot="ifForce" slot-scope="text, record">
          <a-checkbox
            :checked="record.ifForce === 1 ? true : false"
            @change="(e) => changeBoxIfForce(e, record)"
          >
          </a-checkbox>
        </template>
        <template slot="ifAttachment" slot-scope="text, record">
          <a-checkbox
            :checked="record.ifAttachment === 1 ? true : false"
            @change="(e) => changeBoxIfAttachment(e, record)"
          >
          </a-checkbox>
        </template>
      </a-table>
    </a-card>
    <a-row>
      <a-col :span="24" class="btn-sty" style="margin-top: 20px">
        <a-button type="primary" @click="editEsurvey" v-if="this.id">
          保存
        </a-button>
        <a-button type="primary" @click="addEsurvey" v-else> 创建 </a-button>
        <a-button @click="back()"> 返回 </a-button>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import {
  vendorQuestionPostRequestAll,
  vendorQuestionShowGetRequest,
  vendorQuestionPutRequestAll,
  vendorGetRequest,
  vendorTemplateQbankReturnvoGetRequest,
} from "@/services/supplier/index";
const columns = [
  {
    title: "调查指标",
    dataIndex: "type",
    scopedSlots: { customRender: "type" },
  },
  {
    title: "调查子指标",
    dataIndex: "name",
    scopedSlots: { customRender: "name" },
  },
  {
    title: "内容",
    dataIndex: "content",
    scopedSlots: { customRender: "content" },
  },
  {
    title: "强制",
    dataIndex: "ifForce",
    scopedSlots: { customRender: "ifForce" },
  },
  {
    title: "是否需要附件",
    dataIndex: "ifAttachment",
    scopedSlots: { customRender: "ifAttachment" },
  },
];
export default {
  name: "addEsurvey",
  components: {},

  data () {
    return {
      disableds: false,
      selectedRowKeys: [],
      vendorList: [],
      form: {
        vendorId: undefined, //供应商
        name: "", // 调查表名称
        responsiblePerson: "", // 负责人
        responseExpireTime: "", // 	供应商响应时间
        bank: undefined,
      },
      rules: {
        vendorId: [
          {
            required: true,
            message: "采购组织名称不能为空",
            trigger: "change",
          },
        ],
        name: [
          {
            required: true,
            message: "调查表名称不能为空",
            trigger: "blur",
          },
        ],
        responsiblePerson: [
          {
            required: true,
            message: "负责人不能为空",
            trigger: "blur",
          },
        ],
        responseExpireTime: [
          {
            required: true,
            message: "供应商响应时间不能为空",
            trigger: "change",
          },
        ],
      },
      columns: columns,
      dataSource: [],
      id: this.$store.state.account.eSurveyId,
    };
  },
  activated () { },
  mounted () {
    this.getvendorListAll();
    if (this.id) {
      this.getShowAll(this.id);
    } else {
      this.$nextTick(() => {
        this.$refs.ruleForm.resetFields();
      });
    }
  },


  methods: {
    // 请求修改数据
    getShowAll (id) {
      vendorQuestionShowGetRequest(id).then((response) => {
        this.form.id = response.data.id;
        this.form.vendorId = response.data.vendorId;
        this.form.name = response.data.name;
        this.form.responsiblePerson = response.data.responsiblePerson;
        this.form.responseExpireTime = this.$moment(
          Number(response.data.responseExpireTime)
        ).format("YYYY-MM-DD");
        this.dataSource = response.data.vendorQuestionDetailVOS;
        this.selectedRowKeys = this.dataSource.map(function (item, index) {
          return index;
        });
      });
    },
    // 强制改变
    changeBoxIfForce (e, data) {
      data.ifForce = e.target.checked === true ? 1 : 0;
    },
    // 是否需要文件
    changeBoxIfAttachment (e, data) {
      data.ifAttachment = e.target.checked === true ? 1 : 0;
    },
    // 模板改变
    bankChange (value) {
      vendorTemplateQbankReturnvoGetRequest({
        currentPage: "1",
        templateId: value,
      }).then((response) => {
        this.dataSource = response.data.records.map(function (item, index) {
          return item.vendorQuestionBankVO;
        });
      });
    },
    // 查询模板
    getvendorListAll () {
      vendorGetRequest({
        currentPage: "1",
        requestStatusName: "已过审",
        deleted: 0,
      }).then((response) => {
        this.vendorList = response.data.records;
      });
    },
    // 多选
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
    // 添加
    addEsurvey () {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.form.responseExpireTime = Date.parse(
            this.form.responseExpireTime
          );
          let data = [];
          for (let i = 0; i < this.selectedRowKeys.length; i++) {
            data.push(this.dataSource[i]);
          }
          vendorQuestionPostRequestAll({
            name: this.form.name,
            vendorId: this.form.vendorId,
            responseExpireTime: this.form.responseExpireTime,
            responsiblePerson: this.form.responsiblePerson,
            vendorQuestionDetailDTOS: data,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加成功");
              this.back();
            } else {
              this.$message.error(response.message);
            }
          });
        }
      });
    },
    // 修改
    editEsurvey () {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.form.responseExpireTime = Date.parse(
            this.form.responseExpireTime
          );
          let data = [];
          for (let i = 0; i < this.selectedRowKeys.length; i++) {
            data.push(this.dataSource[i]);
          }
          vendorQuestionPutRequestAll({
            id: this.form.id,
            name: this.form.name,
            vendorId: this.form.vendorId,
            responseExpireTime: this.form.responseExpireTime,
            responsiblePerson: this.form.responsiblePerson,
            vendorQuestionDetailDTOS: data,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加成功");
              this.back();
            } else {
              this.$message.error(response.message);
            }
          });
        }
      });
    },
    saveFind () {
      if (this.form.quotationDeadline) {
        this.form.quotationDeadline = this.$moment(
          this.form.quotationDeadline
        ).format("YYYY-MM-DD");
      }
      let childFrom = { ...this.form, ...this.$refs.refChild.form };
      inquirySheetPutRequest({
        ...childFrom,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("修改成功");
          // this.dataClose();
          this.back();
        } else {
          this.$message.error(response.message);
        }
      });
    },

    back () {
      this.$router.push({
        path: "/supply/esurvey",
      });
    },
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  text-align: center;
  margin-bottom: 20px;
  button {
    margin-left: 10px;
  }
}
.menu-content {
  float: left;
  width: calc(100% - 160px);
  padding: 20px;
}
.v-enter,
.v-leave-to {
  opacity: 0;
  transform: translateX(100px);
}

.v-enter-active,
.v-leave-active {
  transition: all 0.5s ease;
}
</style>
