<template>
  <div>
    <a-card v-for="item in list" :key="item" class="card-top">
      <a-form-model
        :model="formInline"
        layout="horizontal"
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row :gutter="20">
          <a-col :span="6">
            <a-form-model-item label="代码">
              <a-input
                v-model="formInline.code"
                disabled
                placeholder="请输入代码"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="最后出价">
              <a-input
                v-model="formInline.finalbid"
                disabled
                placeholder="请输入最后出价"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="排名">
              <a-input
                v-model="formInline.ranking"
                disabled
                placeholder="请输入排名"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="价格">
              <a-input-search
                v-model="formInline.numbner"
                placeholder="请输入价格"
                @search="onSumbit"
              >
                <a-button slot="enterButton"> 提交 </a-button>
              </a-input-search>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
      <a-row :gutter="20">
        <a-col :span="12">
          <a-card title="竞价信息" size="small" style="height: 253px">
            <a-descriptions :column="2">
              <a-descriptions-item label="起始总价">
                600RMB
              </a-descriptions-item>
              <a-descriptions-item label="最小出价 减少量">
                5RMB
              </a-descriptions-item>
              <a-descriptions-item label="方向"> 反向 </a-descriptions-item>
              <a-descriptions-item label="触发价格">
                已到达
              </a-descriptions-item>
              <a-descriptions-item label="出价规则">
                你必须通过最小出价变更来改进最优出价
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-col>
        <a-col :span="12">
          <a-card title="我的出价" size="small">
            <a-table
              id="antdTable"
              :rowClassName="setRowClassName"
              rowKey="key"
              :scroll="{ y: 150 }"
              size="small"
              :columns="columns"
              :pagination="false"
              :data-source="data"
            >
            </a-table>
          </a-card>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>
<script>
const columns = [
  {
    title: "出价",
    key: 'offer',
    dataIndex: "offer",
    scopedSlots: { customRender: "offer" },
  },
  {
    title: "时间",
    key: 'time',
    dataIndex: "time",
    scopedSlots: { customRender: "time" },
  },
]
export default {
  name: "biddingmonitor",
  components: {},
  data () {
    return {
      formInline: {
        code: '2021083801',
        finalbid: '95RMB',
        ranking: '1',
        numbner: 2222
      },
      list: [1, 2],
      columns,
      data: [
        {
          offer: '95RMB',
          time: '2021-08-13 15:39:43',
          state: false
        },
        {
          offer: '98RMB 最小减/增值已达到',
          state: true,
          time: '2021-08-13 15:39:35'
        },
        {
          offer: '100RMB',
          state: false,
          time: '2021-08-13 15:39:15'
        },
        {
          offer: '580RMB',
          state: false,
          time: '2021-08-13 15:32:10'
        },
        {
          offer: '599RMB 最小减/增值已达到',
          state: true,
          time: '2021-08-13 15:31:32'
        },
        {
          offer: '600RMB',
          state: false,
          time: '2021-08-13 15:30:54'
        }
      ]
    };
  },
  created () {
  },
  methods: {
    // 提交
    onSumbit () { },
    // 表格行设置颜色
    setRowClassName (record) {
      return record.state ? 'clickRowStyl' : ''
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
#antdTable {
  ::v-deep .clickRowStyl {
    background: #f52222c2;
  }
}
</style>