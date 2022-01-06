<template>
  <div>
    <div class="content-title">报价信息</div>
    <a-row class="btn-sty">
      <a-button type="primary" @click="handleAdd()"> 添加 </a-button>
      <a-button type="primary" @click="edit()"> 编辑 </a-button>
      <a-button type="primary" @click="save()"> 保存 </a-button>
    </a-row>
    <a-row>
      <a-table
        :columns="columns"
        :row-selection="rowSelection"
        :data-source="data"
      >
        <template
          v-for="col in ['a1', 'a2', 'a3', 'a4', 'a5', 'a7', 'a8']"
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
        <template slot="a9" slot-scope="text, record">
          <div>
            <a-checkbox
              v-if="record.editable"
              style="margin: -5px 0"
              :value="text"
              @change="(e) => handleChange(e.target.value, text)"
            />
            <template v-else>
              <a-checkbox :checked="text" disabled> </a-checkbox>
            </template>
          </div>
        </template>
        <template slot="a6" slot-scope="text, record">
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
    </a-row>
    <a-row class="btn-sty">
      <a-button type="primary" @click="handleAdd1()"> 添加 </a-button>
      <a-button type="primary" @click="edit1()"> 编辑 </a-button>
      <a-button type="primary" @click="save1()"> 保存 </a-button>
    </a-row>
    <a-row>
      <a-table
        :columns="columns1"
        :row-selection="rowSelection"
        :data-source="data1"
      >
        <template
          v-for="col in ['a1', 'a2', 'a3', 'a4', 'a5', 'a6', 'a7']"
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
        <!-- <template slot="a9" slot-scope="text, record">
          <div>
            <a-checkbox
              v-if="record.editable"
              style="margin: -5px 0"
              :value="text"
              @change="(e) => handleChange(e.target.value, text)"
            />
            <template v-else>
              <a-checkbox :checked="text" disabled> </a-checkbox>
            </template>
          </div>
        </template>
        <template slot="a6" slot-scope="text, record">
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
        </template> -->
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
    </a-row>
  </div>
</template>
<script>
export default {
  name: "Results",
  data() {
    return {
      data: [
        {
          a1: "XDC国际事业部",
          a2: "12001001",
          a3: "三级医用吸附膜",
          a4: "",
          a5: "劳保用品",
          a6: "2021-05-12",
          a7: "100",
          a8: "个",
          a9: false,
        },
        {
          a1: "XDC国际事业部",
          a2: "12001001",
          a3: "三级医用吸附膜",
          a4: "",
          a5: "劳保用品",
          a6: "2021-05-12",
          a7: "100",
          a8: "个",
          a9: false,
        },
      ],
      columns: [
        {
          title: "序号",
          key: "index",
          align: "center",
          width: 70,
          customRender: (text, record, index) => `${index + 1}`,
        },
        {
          title: "库存组织",
          dataIndex: "a1",
          width: 150,
          scopedSlots: { customRender: "a1" },
        },
        {
          title: "物品编码",
          dataIndex: "a2",
          scopedSlots: { customRender: "a2" },
        },
        {
          title: "物品描述",
          dataIndex: "a3",
          scopedSlots: { customRender: "a3" },
        },
        {
          title: "参考品牌",
          dataIndex: "a4",
          scopedSlots: { customRender: "a4" },
        },
        {
          title: "物品分类",
          dataIndex: "a5",
          scopedSlots: { customRender: "a5" },
        },
        {
          title: "需求日期",
          ellipsis: true,
          dataIndex: "a6",
          scopedSlots: { customRender: "a6" },
        },
        {
          title: "需求数量",
          dataIndex: "a7",
          scopedSlots: { customRender: "a7" },
        },
        {
          title: "计量单位",
          dataIndex: "a8",
          scopedSlots: { customRender: "a8" },
        },
        {
          title: "含税",
          dataIndex: "a9",
          scopedSlots: { customRender: "a9" },
        },
        {
          title: "操作",
          dataIndex: "operation",
          align: "center",
          scopedSlots: { customRender: "operation" },
        },
      ],
      data1: [
        {
          a1: "12001001",
          a2: "造纸公司",
          a3: "张三",
          a4: "66000.00",
          a5: "9",
          a6: "80",
          a7: "80%",
        },
        {
             a1: "12001002",
          a2: "卫生用品公司",
          a3: "李四",
          a4: "65000.00",
          a5: "8",
          a6: "20",
          a7: "20%",
        },
      ],
      columns1: [
        {
          title: "序号",
          key: "index",
          align: "center",
          width: 70,
          customRender: (text, record, index) => `${index + 1}`,
        },
        {
          title: "投标编号",
          dataIndex: "a1",
          width: 150,
          scopedSlots: { customRender: "a1" },
        },
        {
          title: "投标方名称",
          dataIndex: "a2",
          scopedSlots: { customRender: "a2" },
        },
        {
          title: "投标方",
          dataIndex: "a3",
          scopedSlots: { customRender: "a3" },
        },
        {
          title: "投标总价(元)",
          dataIndex: "a4",
          scopedSlots: { customRender: "a4" },
        },
        {
          title: "总分",
          dataIndex: "a5",
          scopedSlots: { customRender: "a5" },
        },
        {
          title: "中标数量",
          ellipsis: true,
          dataIndex: "a6",
          scopedSlots: { customRender: "a6" },
        },
        {
          title: "中标比例",
          dataIndex: "a7",
          scopedSlots: { customRender: "a7" },
        },
        {
          title: "操作",
          dataIndex: "operation",
          align: "center",
          scopedSlots: { customRender: "operation" },
        },
      ],
      editingKey: "",
      editingKey1: "",
      count: 1,
      count1: 1,
    };
  },
  computed: {
    rowSelection() {
      return {
        onChange: (selectedRowKeys, selectedRows) => {},
      };
    },
  },
  methods: {
    edit() {
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
    edit1() {
      const newData = [...this.data1];
      //   const target = newData.filter((item) => key === item.key)[0];
      this.editingKey1 = 1;
      if (newData) {
        for (let i = 0; i < newData.length; i++) {
          newData[i].editable = true;
        }
      }
      this.data1 = newData;
    },
    save1() {
      const newData = [...this.data1];
      //   const newCacheData = [...this.cacheData];
      if (newData) {
        for (let i = 0; i < newData.length; i++) {
          delete newData[i].editable;
        }
      }
      this.data1 = newData;
      //   if (target && targetCache) {
      //     delete target.editable;
      //     this.data = newData;
      //     Object.assign(targetCache, target);
      //     this.cacheData = newCacheData;
      //   }
      this.editingKey1 = "";
    },
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
    onChange(e) {
      this.data.a9 = e.target.checked;
    },
    handleAdd() {
      this.count = this.count + 1;
      const newData = {
        key: this.count,
        name: `Edward King ${this.count}`,
        age: 32,
        address: `London, Park Lane no. ${this.count}`,
      };
      this.data = [...this.data, newData];
    },
    handleAdd1() {
      this.count1 = this.count1 + 1;
      const newData = {
        key: this.count1,
        name: `Edward King ${this.count1}`,
        age: 32,
        address: `London, Park Lane no. ${this.count1}`,
      };
      this.data1 = [...this.data1, newData];
    },
    handleChange(value, key, column) {
      const newData = [...this.data1];
      const target = newData.filter((item) => key === item.key)[0];
      if (target) {
        target[column] = value;
        this.data1 = newData;
      }
    },
    cancel(key) {
      const newData = [...this.data1];
      const target = newData.filter((item) => key === item.key)[0];
      this.editingKey = "";
      if (target) {
        Object.assign(
          target,
          this.cacheData.filter((item) => key === item.key)[0]
        );
        delete target.editable;
        this.data1 = newData;
      }
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
