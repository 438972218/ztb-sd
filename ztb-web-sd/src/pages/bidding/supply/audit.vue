<template>
  <div>
    <div class="content-title">投标信息</div>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="物品">
        <a-row class="btn-sty">
          <a-button type="primary" @click="edit()" v-show="show"> 编辑 </a-button>
          <a-button type="primary" @click="save()" v-show="!show"> 保存 </a-button>
        </a-row>
        <a-table :columns="columns" :data-source="data">
          <template slot="a9" slot-scope="text, record">
            <div>
              <a-date-picker
                v-if="record.editable"
                style="margin: -5px 0"
                :value="text"
                @change="(e) => handleChange(e.target.value, text)"
              />
              <template v-else>
                {{ text }}
              </template>
            </div>
          </template>
           <template
          v-for="col in ['a6', 'a7']"
          :slot="col"
          slot-scope="text, record"
        >
          <div :key="col">
            <a-input
              v-if="record.editable"
              style="margin: -5px 0"
              :value="text"
              @change="(e) => handleChange(e.target.value, text)"
            />
            <template v-else>
              {{ text }}
            </template>
          </div>
        </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="附件">
           <a-row class="btn-sty">
                <a-button type="primary" > 上传附件 </a-button>

        </a-row>
        <a-table :columns="columns1" :data-source="data1"> 
                  <template slot="operation">
          <div class="editable-row-operations">
                              <a-button
                type="danger"
                size="small"
                @click="c_delete()"
                icon="search"
                class="operation-style"
              >
                删除
              </a-button>
          </div>
        </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "物品编码",
    dataIndex: "a1",
    width: 150,
    ellipsis: true,
  },
  {
    title: "物品描述",
    dataIndex: "a2",
  },
  {
    title: "需求数量",
    dataIndex: "a3",
  },
  {
    title: "计量单位",
    dataIndex: "a4",
  },
  {
    title: "需求日期",
    dataIndex: "a5",
  },
  {
    title: "投标数量",
    dataIndex: "a6",
    scopedSlots: { customRender: "a6" },
  },
  {
    title: "单价(元)",
    dataIndex: "a7",
    scopedSlots: { customRender: "a7" },
  },
  {
    title: "总价(元)",
    dataIndex: "a8",
  },
  {
    title: "承诺交付日期",
    dataIndex: "a9",
    scopedSlots: { customRender: "a9" },
  },
];

const data = [
  {
    a1: "DL0101001",
    a2: "劳保使用",
    a3: "300",
    a4: "个",
    a5: "2021-4-12",
    a6: "100",
    a7: "200.00",
    a8: "20000.00",
    a9: "2021-7-4",
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
    title: "附件名称",
    dataIndex: "a1",
    width: 150,
    ellipsis: true,
  },
  {
    title: "上传人",
    dataIndex: "a2",
  },
  {
    title: "上传时间",
    dataIndex: "a3",
  },
  {
    title: "操作",
    dataIndex: "operation",
        scopedSlots: { customRender: "operation" },
  },
];

const data1 = [
  {
    a1: "技术文档",
    a2: "张思",
    a3: "2021-5-12 12：14：59",

  },
];

export default {
  name: "FindAudit",
  data() {
    return {
      biddingVendorType: this.$store.state.account.biddingVendorType,
      show: true,
      data,
      columns,
       data1,
      columns1,
      editingKey: "",
      count: 1,
    };
  },
  mounted(){
    this.edit()
  },
    methods: {
        c_delete() {
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel() {},
      });
    },
    edit() {
      this.show = !this.show
      const newData = [...this.data];
      //   const target = newData.filter((item) => key === item.key)[0];
      this.editingKey = 1;
      if (newData) {
        for (let i = 0; i < newData.length; i++) {
          newData[i].editable = true;
        }
      }
      this.data = newData;
    },
    save() {
            this.show = !this.show
      const newData = [...this.data];
      //   const newCacheData = [...this.cacheData];
      if (newData) {
        for (let i = 0; i < newData.length; i++) {
          delete newData[i].editable;
        }
      }
      this.data = newData;
      //   if (target && targetCache) {
      //     delete target.editable;
      //     this.data = newData;
      //     Object.assign(targetCache, target);
      //     this.cacheData = newCacheData;
      //   }
      this.editingKey = "";
    },
    back() {
      this.$router.push({
        path: "/findmanage/find",
      });
    },
  },
};
</script>
<style lang="less" scoped>
.btn-sty {
  margin-bottom: 20px;
  button {
    margin-left: 10px;
  }
}
/deep/.ant-table {
  min-height: 220px !important;
}
</style>
