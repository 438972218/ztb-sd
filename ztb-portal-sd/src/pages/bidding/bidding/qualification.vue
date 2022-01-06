<template>
  <div>
    <a-row>
          <div class="operator">
      <a-button
        @click="addNew"
        style="margin-bottom: 10px"
        v-if="showBut"
        type="primary"
        >添加</a-button
      >
    </div>
       <!-- <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="applicationDeadline"
              class="form-item-style"
              label="申请截止时间"
              :labelCol="{ span: 8 }"
              :wrapperCol="{ span: 15 }"
            >
              <a-date-picker
                style="width: 100%"
                v-model="form.applicationDeadline"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="prequalificationEndTime"
              class="form-item-style"
              label="资格预审截止时间"
              :labelCol="{ span: 8 }"
              :wrapperCol="{ span: 15 }"
            >
              <a-date-picker
                style="width: 100%"
                v-model="form.prequalificationEndTime"
                :disabled="disableds"
              />
            </a-form-model-item>
          </a-col> -->
    </a-row>

    <a-table :columns="columns" :data-source="data"  rowKey="id">
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
     
      <a-form-model layout="horizontal"  ref="ruleForm" :rules="rules" :model="form">
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row>
          <a-form-model-item
            prop="code"
            label="代码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.code" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="prequalification"
            label="资格预审"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.prequalification" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="project"
            label="项目"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.project" />
          </a-form-model-item>
          <a-form-model-item
            prop="ifNeedAttachment"
            label="是否需要附件"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-radio-group
              name="radioGroup"
              v-model="form.ifNeedAttachment"
              :default-value="1"
            >
              <a-radio :value="1"> 是 </a-radio>
              <a-radio :value="2"> 否 </a-radio>
            </a-radio-group>
          </a-form-model-item>
          <a-form-model-item
            prop="explanation"
            label="说明"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="form.explanation" />
          </a-form-model-item>
        </a-row>
      </a-form-model>
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="addtrial">新建</a-button>
          <a-button @click="dataClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
  </div>
</template>
<script>
import {
  bidPreTrialItemGetRequest,
  bidPreTrialItemPostRequest,
  bidPreTrialItemDeleteRequest,
} from "@/services/source/bidding";
import {
  permissionsEdit,
} from "@/utils/methods";
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "代码",
    dataIndex: "code",
  },
  {
    title: "资格预审项名称",
    dataIndex: "prequalification",
  },

  {
    title: "是否需要附件",
    dataIndex: "ifNeedAttachment",
  },
  {
    title: "说明",
    dataIndex: "explanation",
  },
  {
    title: "操作",
    scopedSlots: { customRender: "operation" },
  },
];

export default {
  name: "FindItems",
  props: {
    biddingId: {
      type: [String, null],
    },
  },
  data() {
    return {
      form: {
        code: "", // 	代码
        prequalification: "", // 资格预审
        project: "", // 项目
        ifNeedAttachment: "", // 是否需要附件
        explanation: "", // 说明
      },
      rules: {
        code: [
          {
            required: true,
            message: "代码不能为空",
            trigger: "blur",
          },
        ],
        prequalification: [
          {
            required: true,
            message: "资格预审不能为空",
            trigger: "blur",
          },
        ],
        ifNeedAttachment: [
          {
            required: true,
            message: "请选择，不能为空",
            trigger: "blur",
          },
        ],
      },
      id: this.biddingId,
      visible: false,
      data: [],
      columns,
      biddingType: this.$store.state.account.biddingType,
      disableds: true,
      showBut: true,
    };
  },
  created() {
       if(this.id){
    this.getAll()
    }
    if (this.biddingType === "add") {
      this.disableds = false;
    } else {
      this.disableds = permissionsEdit("bid:edit");
    }
   if (this.biddingType === "add") {
      this.showBut = true;
    } else {
      this.showBut = !permissionsEdit("bid:edit");
    }
    if (!this.showBut) {
      this.columns.pop();
    }
    else if (this.biddingType === 0) {
      this.showBut = false;
    } 
  },
  methods: {
    getAll() {
      bidPreTrialItemGetRequest({
        bidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
      });
    },
    addtrial() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          bidPreTrialItemPostRequest({
            bidSheetId: this.id,
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
          bidPreTrialItemDeleteRequest(id).then((response) => {
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
