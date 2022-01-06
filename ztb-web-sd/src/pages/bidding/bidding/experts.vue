<template>
  <div>
    <a-form-model layout="horizontal" class="page-form">
      <!-- <div :class="advanced ? null: 'fold'"> -->
      <!-- <a-row>
        <a-col :sm="24" :md="12" :xl="6">
          <a-form-model-item
            label="评分模式"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-select placeholder="请选择" :default-value="2" disabled>
              <a-select-option value="2">先技术后商务</a-select-option>
              <a-select-option value="3">先商务后技术</a-select-option>
              <a-select-option value="4">同时评分</a-select-option>
              <a-select-option value="1">不区分</a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
      </a-row> -->
    </a-form-model>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="专家">
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
          <template slot="specialistGroupLeader" slot-scope="text, record">
            <span v-if="text === 1"> 是 </span>
          </template>
          <template slot="operation" slot-scope="text, record">
            <span
              @click="c_delete_experts(record.id)"
              class="operation-style"
              v-if="showBut"
            >
              删除
            </span>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="评分要素" force-render>
        <div class="operator">
          <a-button
            @click="addNew1"
            style="margin-bottom: 10px"
            type="primary"
            v-if="showBut"
            >添加</a-button
          >
        </div>
        <a-divider>商务组</a-divider>
        <a-table :columns="columns1" :data-source="dataBusiness">
          <template slot="operation" slot-scope="text, record">
            <span
              @click="c_delete_Elements(record.id)"
              class="operation-style"
              v-if="showBut"
            >
              删除
            </span>
          </template>
        </a-table>
        <a-divider>技术组</a-divider>
        <a-table :columns="columns1" :data-source="dataScience">
          <template slot="operation" slot-scope="text, record">
            <span
              @click="c_delete_Elements(record.id)"
              class="operation-style"
              v-if="showBut"
            >
              删除
            </span>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>

    <a-modal
      v-model="visible"
      :footer="null"
      width="500px"
      :centered="true"
      title="添加"
    >
      <a-form-model layout="horizontal" ref="ruleform" :model="formExperts">
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row>
          <a-form-model-item
            prop="specialistSubAccount"
            label="专家子账户"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="formExperts.specialistSubAccount" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="userId"
            class="form-item-style"
            label="绑定用户"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-select
              v-model="formExperts.userId"
              placeholder="请选择用户"
              @change="userChange"
              :disabled="disableds"
            >
              <a-select-option
                v-for="item in userList"
                :key="item.id"
                :value="item.id"
                >{{ item.userName }}</a-select-option
              >
            </a-select>
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="specialistName"
            label="专家姓名"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input disabled v-model="formExperts.specialistName" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="specialistSource"
            label="专家来源"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="formExperts.specialistSource" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="specialistGroupLeader"
            label="专家组长"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-checkbox
              :checked="
                formExperts.specialistGroupLeader === '0' ? false : true
              "
              @change="changeLeader"
            >
            </a-checkbox>
          </a-form-model-item>
        </a-row>
      </a-form-model>
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="addexperts()">新建</a-button>
          <a-button @click="expertsClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
    <a-modal
      v-model="visible1"
      :footer="null"
      width="500px"
      :centered="true"
      title="添加"
    >
      <a-form-model
        layout="horizontal"
        ref="ruleformElements"
        :model="formElements"
      >
        <!-- <div :class="advanced ? null: 'fold'"> -->

        <a-row>
          <a-form-model-item
            prop="groupType"
            label="评分要素代码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-select
              v-model="formElements.groupType"
              placeholder="请选择"
              :disabled="disableds"
            >
              <a-select-option value="1">商务组</a-select-option>
              <a-select-option value="2">技术组</a-select-option>
            </a-select>
          </a-form-model-item>
        </a-row>

        <a-row>
          <a-form-model-item
            prop="scoringElementsCode"
            label="评分要素代码"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="formElements.scoringElementsCode" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="scoringElementsName"
            label="评分要素名称"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="formElements.scoringElementsName" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="scoringRubric"
            label="评分明细"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="formElements.scoringRubric" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="topScore"
            label="最高分"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-input v-model="formElements.topScore" />
          </a-form-model-item>
        </a-row>
      </a-form-model>
      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="addElements()">新建</a-button>
          <a-button @click="elementsClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
  </div>
</template>
<script>
import {
  bidSpecialistGetRequest,
  bidSpecialistPostRequest,
  bidSpecialistDeleteRequest,
  bidScoringElementsGetRequest,
  bidScoringElementsPostRequest,
  bidScoringElementsDeleteRequest,
} from "@/services/source/bidding";
import { permissionsEdit } from "@/utils/methods";
import { getSysUserServicePageByCondition } from "@/services/structure/index";

const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "专家子账户",
    dataIndex: "specialistSubAccount",
  },
  {
    title: "专家姓名",
    dataIndex: "specialistName",
  },
  {
    title: "专家来源",
    dataIndex: "specialistSource",
  },
  {
    title: "专家组长",
    dataIndex: "specialistGroupLeader",
    scopedSlots: { customRender: "specialistGroupLeader" },
  },
  {
    title: "操作",
    scopedSlots: { customRender: "operation" },
  },
];
const columns1 = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "评分要素代码",
    dataIndex: "scoringElementsCode",
  },
  {
    title: "评分要素名称",
    dataIndex: "scoringElementsName",
  },
  {
    title: "评分细则",
    dataIndex: "scoringRubric",
  },
  {
    title: "最高分",
    dataIndex: "topScore",
  },
  {
    title: "操作",
    scopedSlots: { customRender: "operation" },
  },
];
export default {
  name: "experts",
  props: {
    biddingId: {
      type: [String, null],
    },
  },
  data () {
    return {
      id: this.biddingId,
      formExperts: {
        specialistSubAccount: "",
        specialistName: "",
        specialistSource: "",
        specialistGroupLeader: "0",
        userId: "",
      },
      formElements: {
        groupType: "",
        scoringElementsCode: "",
        scoringElementsName: "",
        scoringRubric: "",
        topScore: "",
      },
      visible1: false,
      visible: false,
      data: [],
      columns,
      dataBusiness: [],
      dataScience: [],
      userList: [],
      columns1,
      biddingType: this.$store.state.account.biddingType,
      disableds: true,
      showBut: true,
    };
  },
  created () {
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
      this.columns1.pop();
    }
    if (this.biddingType === 0) {
      this.showBut = false;
    }
    this.getAll();
    this.getUserList();
  },
  methods: {
    getAll () {
      bidSpecialistGetRequest({
        bidSheetId: this.id,
        currentPage: "1",
        deleted: 0,
      }).then((response) => {
        this.data = response.data.records;
      });
      bidScoringElementsGetRequest({
        bidSheetId: this.id,
        currentPage: "1",
        groupType: 1,
        deleted: 0,
      }).then((response) => {
        this.dataBusiness = response.data.records;
      });
      bidScoringElementsGetRequest({
        bidSheetId: this.id,
        currentPage: "1",
        groupType: 2,
        deleted: 0,
      }).then((response) => {
        this.dataScience = response.data.records;
      });
    },
    getUserList () {
      getSysUserServicePageByCondition({}).then((response) => {
        this.userList = response.data.records;
      });
    },

    addexperts () {
      bidSpecialistPostRequest({
        bidSheetId: this.id,
        ...this.formExperts,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("添加成功");
          this.visible = false;
          this.getAll();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    // 删除专家
    c_delete_experts (id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk () {
          bidSpecialistDeleteRequest(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getAll();
          });
        },
        onCancel () { },
      });
    },
    addElements () {
      this.formElements.groupType = Number(this.formElements.groupType);
      bidScoringElementsPostRequest({
        bidSheetId: this.id,
        ...this.formElements,
      }).then((response) => {
        if (response.code === 0) {
          this.$message.success("添加成功");
          this.visible1 = false;
          this.getAll();
        } else {
          this.$message.error(response.message);
        }
      });
    },
    c_delete_Elements (id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk () {
          bidScoringElementsDeleteRequest(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getAll();
          });
        },
        onCancel () { },
      });
    },
    userChange (e) {
      let target = this.userList.filter((item) => e === item.id)[0];
      this.formExperts.specialistName = target.name;
    },
    changeLeader (e) {
      this.formExperts.specialistGroupLeader =
        e.target.checked === false ? "0" : "1";
    },
    addNew () {
      this.visible = true;
      this.$nextTick(() => {
        if (this.$refs.ruleform !== undefined) {
          this.$refs.ruleform.resetFields();
        }
      });
    },
    expertsClose () {
      this.visible = false;
    },
    addNew1 () {
      this.visible1 = true;
      this.$nextTick(() => {
        this.$refs.ruleformElements.resetFields();
      });
    },
    elementsClose () {
      this.visible1 = false;
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
