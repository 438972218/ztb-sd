<template>
  <div>
    <a-row class="btn-sty" v-if="showBut">
      <a-button type="primary" @click="save()" v-show="!show"> 保存 </a-button>
      <a-button type="primary" @click="edit()" v-show="show"> 编辑 </a-button>
    </a-row>
    <a-row>
      <a-col>
        <a-table :columns="columns" :data-source="data" bordered>
          <template slot="a6" slot-scope="text, record">
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
        </a-table>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import { el } from 'date-fns/locale';
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
    title: "投标总数",
    width: 100,
    dataIndex: "a4",
  },
  {
    title: "最低价比重",
    width: 100,
    dataIndex: "a5",
  },
  {
    title: "评分",
    width: 100,
    dataIndex: "a6",
    scopedSlots: { customRender: "a6" },
  },
  //   {
  //     title: "操作",
  //     dataIndex: "operation",
  //     width: 150,
  //     align: "center",
  //     fixed: "right",
  //     scopedSlots: { customRender: "operation" },
  //   },
];

const data = [
  {
    a1: "BID0001",
    a2: "公司1",
    a3: "张三",
    a4: "资质达标",
    a5: "30%",
    a6: "99",
  },
  {
    a1: "BID0002",
    a2: "公司2",
    a3: "李四",
    a4: "资质达标",
    a5: "70%",
    a6: "100",
  },
];

export default {
  name: "appraise",
  data() {
    return {
      show: false,
      data,
      columns,
      editingKey: "",
      findtype: this.$store.state.account.findType,
      showBut: true,
    };
  },
  mounted() {
  },
  created() {
    this.showBut = this.findtype === "10" ? false : true;
    this.show = this.findtype === "10" ? false : true;
    if(this.show){
        this.edit()
    }else{
        this.save()
    }
  },
  methods: {
    handleChange(value, key) {
      const newData = [...this.data];
      const target = newData.filter((item) => key === item.key)[0];
      if (target) {
        target.a4 = value;
        this.data = newData;
      }
    },
    edit() {
      this.show = false;
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
      this.show = true;
      const newData = [...this.data];
      //   const newCacheData = [...this.cacheData];
      if (newData) {
        for (let i = 0; i < newData.length; i++) {
          delete newData[i].editable;
        }
      }
      this.data = newData;

      this.editingKey = "";
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
  margin-bottom: 10px;
  button {
    margin-left: 10px;
  }
}
/deep/.ant-table {
  min-height: 220px !important;
}
</style>
