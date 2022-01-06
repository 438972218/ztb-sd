<template>
  <div>
    <a-card class="card-top">
      <a-form layout="horizontal">
        <a-row>
          <a-col :md="6" :sm="12">
            <a-form-item
              label="角色代码"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 18, offset: 0 }"
            >
              <a-input />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item
              label="角色名称"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 18, offset: 0 }"
            >
              <a-input />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item
              label="角色描述"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 18, offset: 0 }"
            >
              <a-input />
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <span style="float: right; margin-top: 3px">
              <a-button type="primary">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </a-card>
    <a-card>
      <!-- <div class="operator">
        <a-button @click="addNew('add')" type="primary">添加角色</a-button>
      </div> -->
      <a-table :columns="columns" :data-source="data">
        <template slot="key" slot-scope="record">
          <a-button
            type="primary"
            @click="c_edit(record, 'edit')"
            class="iconstyle"
            icon="search"
          >
            编辑
          </a-button>
          <!-- <a-button
            type="danger"
            @click="c_delete(record.id)"
            class="iconstyle"
            icon="search"
          >
            删除
          </a-button> -->
        </template>
      </a-table>
    </a-card>

    <a-modal
      title="添加角色"
      :visible="visible"
      width="700px"
      :centered="true"
      :footer="null"
      @cancel="dataClose"
    >
      <menu-add
        ref="childrenDom"
        @getAll="getTypesAll"
        @on-data-close="dataClose"
        :editData="editData"
        :types="types"
      >
      </menu-add>
    </a-modal>
  </div>
</template>
<script>
import {
  roleDeleteRequestAll,
  permissionRequestAll,
} from "@/services/system/user";
import MenuAdd from './components/menuAdd.vue';

const columns = [
  {
    title: "资源名称",
    align: "center",
    dataIndex: "name",
  },
  {
    title: "父级ID",
    align: "center",
    dataIndex: "parentId",
  },
  {
    title: "排序",
    align: "center",
    dataIndex: "sort",
  },
  {
    title: "资源类型",
    align: "center",
    dataIndex: "resourceType",
  },
  {
    title: "是否隐藏",
    align: "center",
    dataIndex: "hide",
  },
  {
    title: "权限字符串",
    align: "center",
    dataIndex: "permission",
  },
  {
    title: "路由地址",
    align: "center",
    dataIndex: "route",
  },
  {
    title: "描述",
    align: "center",
    dataIndex: "description",
  },
  {
    title: "操作",
    align: "center",
    scopedSlots: { customRender: "key" },
  },
];

export default {
  name: "menu",
  components: { MenuAdd },
  data () {
    return {
      visible: false,
      expandedKeys: ["0-0-0", "0-0-1"],
      autoExpandParent: true,
      checkedKeys: ["0-0-0"],
      selectedKeys: [],
      data: [],
      columns,
      editData: null,
      types: null,
    };
  },
  watch: {
    checkedKeys (val) {
      console.log("onCheck", val);
    },
  },
  created () {
    this.getTypesAll();
    // this.getrolesTypeAll()
  },
  methods: {
    //角色列表
    getTypesAll () {
      permissionRequestAll({
        currentPage: 1,
      }).then((response) => {
        this.data = response.data.records;
      });
      this.editData = null;
    },
    c_delete (id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除用户吗？",
        centered: true,
        onOk () {
          roleDeleteRequestAll(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getTypesAll();
          });
        },
        onCancel () { },
      });
    },
    c_edit (editData, types) {
      this.editData = editData;
      this.addNew(types);
    },
    dataClose () {
      this.$refs.childrenDom.form = {
        name: "",
        permissionIds: [],
        roleLevel: "",
        description: "",
      };
      this.visible = false;
      this.editData = null;
    },
    addNew (types) {
      this.types = types;
      this.visible = true;
    },
  },
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>