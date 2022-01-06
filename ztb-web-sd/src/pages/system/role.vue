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
    </a-card>

    <a-modal
      title="添加角色"
      :visible="visible"
      width="700px"
      :centered="true"
      :footer="null"
      @cancel="dataClose"
    >
      <role-add
        ref="childrenDom"
        @getAll="getRoleAll"
        @on-data-close="dataClose"
        :editData="editData"
        :types="types"
      >
      </role-add>
    </a-modal>
  </div>
</template>
<script>
import RoleAdd from "./components/roleAdd";
import { EmployeeGetAllRequest } from "@/services/default/users";
import { roleRequestAll, DeleteRoleRequestAll } from "@/services/system/user";

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
    title: "角色类型",
    align: "center",
    dataIndex: "typeName",
  },
  {
    title: "操作",
    align: "center",
    scopedSlots: { customRender: "key" },
  },
];

export default {
  name: "role",
  components: { RoleAdd },
  data() {
    return {
      types: null,
      editData: null,
      visible: false,
      expandedKeys: ["0-0-0", "0-0-1"],
      autoExpandParent: true,
      checkedKeys: ["0-0-0"],
      selectedKeys: [],
      data: [],
      columns,
    };
  },

  created() {
    this.getRoleAll();
  },
  methods: {
    //角色列表
    getRoleAll() {
      EmployeeGetAllRequest({
        name: "测试",
        companyId: "",
        currentPage: "1",
        pageSize: "1",
      }).then((response) => {
        this.data = response.data.records;
      });
    },
    dataClose() {
      this.$refs.childrenDom.form = {
        name: "",
        permissionIds: [],
        description: "",
        typeId: null,
      };
      this.visible = false;
      this.editData = null;
    },
    addNew(type) {
      this.types = type;
      this.visible = true;
    },
    c_edit(editData, type) {
      this.editData = editData;
      this.addNew(type);
    },
    c_delete(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除用户吗？",
        centered: true,
        onOk() {
          DeleteRoleRequestAll(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getRoleAll();
          });
        },
        onCancel() {},
      });
    },
    handleOk() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          console.log(valid);
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>