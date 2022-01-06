<template>
  <div>
    <a-row :gutter="16">
      <a-col :sm="24" :md="24">
        <div style="margin-top:20px">
        <a-table
          :columns="columns"
          size="small"
          :scroll="{ y: 300 }"
          :pagination="false"
          :data-source="data"
        >
          <template slot="toStatus" slot-scope="text, record">
            <div v-if="record.toStatus">
              {{ record.toStatus.name }}
            </div>
          </template>
          <template slot="createdUser" slot-scope="text, record">
            <div v-if="record.request">
            {{ record.request.createdUser }}
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
  name: "WerkFlow",
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
</style>
