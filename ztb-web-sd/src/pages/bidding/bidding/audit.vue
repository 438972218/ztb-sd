<template>
  <div>
    <a-row :gutter="16">
      <a-col :sm="24" :md="24">
        <a-form-model layout="horizontal" class="page-form">
          <a-row>
            <!-- <a-col :span="12">
            <a-form-model-item
              prop="toUserId"
              class="form-item-style"
              label="审批结果"
              :labelCol="{ span: 4}"
              :wrapperCol="{ span: 19 }"
              
            >
              <a-select placeholder="请选择" v-model="form.toUserId">
                <a-select-option value="1">张三</a-select-option>
                <a-select-option value="2">李四</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col> -->

            <!-- <a-col :span="24">
              <a-form-model-item
                prop="toUserId"
                class="form-item-style"
                label="转交人"
                :labelCol="{ span: 4 }"
                :wrapperCol="{ span: 20}"
              >
                <a-input v-model="form.toUserId" />
              </a-form-model-item>
            </a-col> -->
          </a-row>
          <a-row>
            <a-col>
              <a-form-model-item
                prop="description"
                class="form-item-style"
                label="审批意见"
                :labelCol="{ span: 3 }"
                :wrapperCol="{ span: 20 }"
              >
                <a-textarea
                  v-model="form.description"
                  :auto-size="{ minRows: 1, maxRows: 3 }"
              /></a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </a-col>
      <a-col :sm="24" :md="24">
        <div class="content-title">审批信息</div>
        <div style="margin-top:20px">
        <a-table
          :columns="columns"
          size="small"
          :scroll="{ y: 50 }"
          :pagination="false"
          :data-source="data"
        >
          <template slot="toStatus" slot-scope="text, record">
            <div v-if="record.toStatus">
              {{ record.toStatus.name }}
            </div>
          </template>
          <template slot="createdUser" slot-scope="text, record">
            <div v-if="record.fromStatus">
            {{ record.fromStatus.createdUser }}
            </div>
          </template>
                <template slot="fromStatus" slot-scope="text, record">
            <div v-if="record.flowOption">
              {{ record.flowOption.description !=="未审核" ? '通过': '' }}
            </div>
          </template>
          <template slot="beginTime" slot-scope="text">
            <div>
              {{ $moment(Number(text)).format("YYYY-MM-DD HH:mm:ss") }}
            </div>
          </template>
        </a-table>

        </div>

      </a-col>
    </a-row>
  </div>
</template>
<script>
import { interactionrequestFlowGetRequest } from "@/services/process/workflow";
const columns = [
  {
    title: "当前节点",
    dataIndex: "toStatus",
    scopedSlots: { customRender: "toStatus" },
  },
  {
    title: "审批人",
    dataIndex: "createdUser",
    scopedSlots: { customRender: "createdUser" },
  },
  {
    title: "审批结果",
    dataIndex: "fromStatus",
    scopedSlots: { customRender: "fromStatus" },
  },
  {
    title: "审批意见",
    dataIndex: "description",
  },
  {
    title: "审批时间",
    dataIndex: "beginTime",
    width: 200,
    ellipsis: true,
    scopedSlots: { customRender: "beginTime" },
  },
];

export default {
  name: "FindAudit",
  props: {
    requestId: {
      type: [String, null],
    },
  },
  data() {
    return {
      id: this.requestId,
      data: [],
      columns,
      form: {
        toUserId: "",
        description: "",
      },
    };
  },
  mounted() {
    if (this.id) {
      this.getAll();
    }
  },
  methods: {
    getAll() {
      interactionrequestFlowGetRequest(this.id).then((response) => {
        this.data = response.data;
      });
    },
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  text-align: center;
  button {
    margin-left: 10px;
  }
}
/deep/.ant-table {
  min-height: 120px !important;
}
</style>
