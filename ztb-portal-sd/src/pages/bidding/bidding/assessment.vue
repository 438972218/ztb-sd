<template>
  <div>
    <div class="content-title">投标信息</div>
    <a-row>
      <a-table :columns="columns1" :data-source="data1" bordered> </a-table>
    </a-row>
    <a-row class="btn-sty">
 <a-button type="primary" @click="save()" v-show="show"> 保存 </a-button>
      <a-button type="primary" @click="edit()" v-show="!show"> 编辑 </a-button>
    </a-row>
    <a-row>
      <a-table
        :row-selection="rowSelection"
        :columns="columns"
        :data-source="data"
        bordered
      >
        <template slot="a2">
          <div style="color: #40a9ff; cursor: pointer" @click="addNew">
            查看
          </div>
        </template>
        <template slot="a9" slot-scope="text, record">
          <div>
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
        <!-- <template slot="operation">
          <div class="editable-row-operations">
            <span @click="check()">
             查看
            </span>
          </div>
        </template> -->
      </a-table>
    </a-row>
    <a-modal v-model="visible" width="1000px" :footer="null" :centered="true" title="明细列表">
      <a-table :columns="columns1" :data-source="data1"> </a-table>
    </a-modal>
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
    title: "投标编号",
    width: 120,
    dataIndex: "a1",
  },
  {
    title: "投标方名称",
    width: 150,
    dataIndex: "a2",
  },
  {
    title: "投标方",
    width: 100,
    dataIndex: "a3",
  },
  {
    title: "投标总价(元)",
    width: 100,
    dataIndex: "a4",
  },
  {
    title: "最低价比重",
    width: 100,
    dataIndex: "a5",
  },
  {
    title: "总分",
    width: 100,
    dataIndex: "a6",
  },
  {
    title: "单价(元)",
    width: 100,
    dataIndex: "a7",
  },
  {
    title: "投标数量",
    width: 100,
    dataIndex: "a8",
  },
  {
    title: "中标数量",
    width: 100,
    dataIndex: "a9",
    scopedSlots: { customRender: "a9" },
  },
  {
    title: "中标比例",
    width: 100,
    dataIndex: "a10",
  },
];

const data = [
  {
    a1: "CO0000012",
    a2: "造纸公司",
    a3: "张三",
    a4: "66000.00",
    a5: "62%",
    a6: "8",
    a8: "280.00",
    a9: "100",
    a10: "80",
  },
  {
    a1: "CO0000012",
    a2: "造纸公司",
    a3: "张三",
    a4: "66000.00",
    a5: "62%",
    a6: "8",
    a8: "280.00",
    a9: "100",
    a10: "80",
  },
];
const columns1 = [
  {
    title: "序号",
    key: "index",
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "库存组织",
    dataIndex: "a1",
  },
  {
    title: "物品编码",
    dataIndex: "a2",
  },
  {
    title: "物品描述",
    dataIndex: "a3",
  },
  {
    title: "参考品牌",
    dataIndex: "a4",
  },
  {
    title: "物品品牌",
    dataIndex: "a5",
  },
  {
    title: "需求日期",
    dataIndex: "a6",
  },
  {
    title: "需求数量",
    dataIndex: "a7",
  },
  {
    title: "计量单位",
    width: 150,
    dataIndex: "a8",
  },
  {
    title: "含税",
    width: 150,
    dataIndex: "a9",
  },
  {
    title: "成本单价(元)",
    width: 150,
    dataIndex: "a10",
  },
  {
    title: "成本合价(元)",
    width: 150,
    dataIndex: "a11",
  },
];

const data1 = [
  {
    a1: "XDC国际数据中心",
    a2: "12001001",
    a3: "三层医用吸附膜",
    a4: "益康达",
    a5: "劳保用品",
    a6: "2022-01-01",
    a7: "100",
    a8: "个",
    a9: "是",
    a10: "100",
    a11: "1000",
  },
  {
    a1: "XDC国际数据中心",
    a2: "12001001",
    a3: "三层医用吸附膜",
    a4: "益康达",
    a5: "劳保用品",
    a6: "2022-01-01",
    a7: "100",
    a8: "个",
    a9: "是",
    a10: "100",
    a11: "1000",
  },
];
export default {
  name: "assessment",
  data() {
    return {
        show: false,
      data,
      columns,
      data1,
      columns1,
      editingKey: "",
      visible: false,
    };
  },
  computed: {
    rowSelection() {
      return {
        onChange: (selectedRowKeys, selectedRows) => {},
      };
    },
  },
 mounted(){
    this.edit()
  },
  methods: {
    addNew() {
      this.visible = true;
    },
    handleChange(value, key) {
      const newData = [...this.data];
      const target = newData.filter((item) => key === item.key)[0];
      if (target) {
        target.a4 = value;
        this.data = newData;
      }
    },
    onChange(e) {
      this.data.a3 = e.target.checked;
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
    cancel(key) {
      const newData = [...this.data];
      const target = newData.filter((item) => key === item.key)[0];
      this.editingKey = "";
      if (target) {
        Object.assign(
          target,
          this.cacheData.filter((item) => key === item.key)[0]
        );
        delete target.editable;
        this.data = newData;
      }
    },
    back() {
      this.$router.push({
        path: "/findmanage/find",
      });
      this.$store.state.account.findType = num;
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
