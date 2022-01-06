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
      <div class="operator">
        <a-button @click="addNew('add')" type="primary">添加角色</a-button>
      </div>
      <a-table :columns="columns" :data-source="dataSource">
        <template slot="key" slot-scope="record">
          <a-button
            type="primary"
            @click="c_edit(record, 'edit')"
            class="iconstyle"
            icon="search"
          >
            编辑
          </a-button>
          <a-button
            type="danger"
            @click="c_delete(record.id)"
            class="iconstyle"
            icon="search"
          >
            删除
          </a-button>
        </template>
      </a-table>
      <!-- <advance-table
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      rowKey="name"
      @refresh="onRefresh"
      :format-conditions="true"
      @reset="onReset"
      :pagination="{
        current: page,
        pageSize: pageSize,
        total: total,
        showSizeChanger: true,
        showLessItems: true,
        showQuickJumper: true,
        onChange: onPageChange,
        onShowSizeChange: onSizeChange,
      }"
    >
            <template slot="key" slot-scope="record">
          <a-button
            type="primary"
            @click="c_edit(record, 'edit')"
            class="iconstyle"
            icon="search"
          >
            编辑
          </a-button>
          <a-button
            type="danger"
            @click="c_delete(record)"
            class="iconstyle"
            icon="search"
          >
            删除
          </a-button>
        </template>
    </advance-table> -->
    </a-card>

    <a-modal
      title="添加角色"
      :visible="visible"
      width="700px"
      :centered="true"
      :footer="null"
      @cancel="dataClose"
    >
      <role-type-add
        ref="childrenDom"
        @getAll="getTypesAll"
        @on-data-close="dataClose"
        :editData="editData"
        :types="types"
      >
      </role-type-add>
    </a-modal>
  </div>
</template>
<script>
import AdvanceTable from "@/components/table/advance/AdvanceTable";
import RoleTypeAdd from "./components/roleTypeAdd";
import {
  roleTypesRequestAll,
  roleDeleteRequestAll,
} from "@/services/system/user";

const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "角色类型名称",
    align: "center",
    dataIndex: "name",
  },
  {
    title: "职务描述",
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
  name: "roleType",
  components: { AdvanceTable, RoleTypeAdd },
  data () {
    return {
      loading: false,
      page: 1,
      pageSize: 10,
      total: 0,
      conditions: {},
      visible: false,
      dataSource: [],
      columns,
      editData: null,
      types: null,
    };
  },
  authorize: {              //权限校验注入设置
    deleteRecord: 'delete'  //key为需要注入权限校验的方法名，这里为 deleteRecord 方法；值为需要校验的操作权限，这里为 delete 操作权限
  },
  mounted () {
    console.log(this.$auth('admin'))
  },
  created () {
    this.getTypesAll();
    // this.getGoodList();
    // this.getColumns();
    // this.getrolesTypeAll()
  },
  methods: {
    //角色列表
    getTypesAll () {
      roleTypesRequestAll({
        currentPage: 1,
      }).then((response) => {
        this.dataSource = response.data;
      });
      this.editData = null;
      this.loading = false;
    },
    c_delete (slot) {
      let id = slot.record.id
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

    // table所有方法
    getGoodList () {
      this.loading = true;
    },
    onSearch (conditions, searchOptions) {
      this.page = 1;
      this.conditions = conditions;
      this.getTypesAll();
    },
    onSizeChange (current, size) {
      this.page = 1;
      this.pageSize = size;
      this.getTypesAll();
    },
    onRefresh (conditions) {
      this.conditions = conditions;
      this.getTypesAll();
    },
    onReset (conditions) {
      this.conditions = conditions;
      this.getTypesAll();
    },
    onPageChange (page, pageSize) {
      this.page = page;
      this.pageSize = pageSize;
      this.getTypesAll();
    },
  },
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>