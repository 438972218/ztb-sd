<template>
  <div>
    <a-card class="card-top">
      <a-form layout="horizontal">
        <a-row>
          <a-col :md="8" :sm="12">
            <a-form-item
              label="所在组织"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 18, offset: 0 }"
            >
              <a-input />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="12">
            <a-form-item
              label="账号类型"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 18, offset: 0 }"
            >
              <a-select placeholder="请选择">
                <a-select-option value="1">内部账号</a-select-option>
                <a-select-option value="2">供应商账号</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="12">
            <a-form-item
              label="状态"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 18, offset: 0 }"
            >
              <a-select placeholder="请选择">
                <a-select-option value="1">新建</a-select-option>
                <a-select-option value="2">正常</a-select-option>
                <a-select-option value="3">冻结</a-select-option>
                <a-select-option value="4">过期</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="12">
            <a-form-item
              label="关键字"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 18, offset: 0 }"
            >
              <a-input />
            </a-form-item>
          </a-col>
          <a-col :offset="8">
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
      <a-table
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        :columns="columns"
        :data-source="data"
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
      :title="types === 'add' ? '添加用户' : '编辑用户'"
      :visible="visible"
      width="900px"
      :centered="true"
      :footer="null"
      @cancel="dataClose"
    >
      <user-add
        ref="childrenDom"
        @getAll="getAll"
        @on-data-close="dataClose"
        :editData="editData"
        :types="types"
      ></user-add>
    </a-modal>
  </div>
</template>
<script>
import UserAdd from "./components/userAdd";
import { userRequestAll, DeleteUserRequestAll } from "@/services/system/user";

const treeData = [
  {
    title: "XDC-工业智造研究室",
    key: "0-0",
    children: [
      {
        title: "项目小组一",
        key: "0-0-0",
        children: [
          { title: "成员一", key: "0-0-0-0" },
          { title: "成员二", key: "0-0-0-1" },
          { title: "成员三", key: "0-0-0-2" },
        ],
      },
      {
        title: "项目小组二",
        key: "0-0-1",
        children: [
          { title: "成员一", key: "0-0-1-0" },
          { title: "成员二", key: "0-0-1-1" },
          { title: "成员三", key: "0-0-1-2" },
        ],
      },
      {
        title: "项目小组三",
        key: "0-0-2",
      },
    ],
  },
];
const columns = [
  {
    title: "账号",
    align: "center",
    dataIndex: "account",
  },
  {
    title: "姓名",
    align: "center",
    dataIndex: "name",
  },
  // {
  //   title: "所在组织",
  //   align: "center",
  //   dataIndex: "a2",
  // },
  {
    title: "描述",
    align: "center",
    dataIndex: "description",
  },
  {
    title: "状态",
    align: "center",
    dataIndex: "status",
  },
  // {
  //   title: "有效期从",
  //   align: "center",
  //   dataIndex: "a5",
  // },
  // {
  //   title: "有效期至",
  //   align: "center",
  //   dataIndex: "a6",
  // },
  {
    title: "手机号",
    align: "center",
    dataIndex: "telephone",
  },
  {
    title: "邮箱",
    align: "center",
    dataIndex: "email",
  },
  // {
  //   title: "用户类型",
  //   align: "center",
  //   dataIndex: "a9",
  // },
  {
    title: "操作",
    align: "center",
    scopedSlots: { customRender: "key" },
  },
];

export default {
  name: "user",
  components: { UserAdd },
  data () {
    return {
      types: null,
      editData: null,
      visible: false,
      expandedKeys: ["0-0-0", "0-0-1"],
      autoExpandParent: true,
      checkedKeys: ["0-0-0"],
      selectedKeys: [],
      treeData,
      data: [],
      columns,
      selectedRowKeys: [],
    };
  },
  watch: {
    checkedKeys (val) {
      console.log("onCheck", val);
    },
  },
  created () {
    this.getAll();
  },
  methods: {
    // 角色列表
    getAll () {
      userRequestAll({
        currentPage: 1,
      }).then((response) => {
        this.data = response.data.records;
      });
    },
    addNew (type) {
      this.types = type;
      this.visible = true;
    },
    c_edit (editData, type) {
      this.editData = editData;
      this.addNew(type);
    },
    dataClose () {
      this.$refs.childrenDom.form = {
        account: "",
        password: "",
        name: "",
        telephone: "",
        email: "",
        description: "",
      };
      this.visible = false;
      this.editData = null;
    },
    c_delete (id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除用户吗？",
        centered: true,
        onOk () {
          DeleteUserRequestAll(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getAll();
          });
        },
        onCancel () { },
      });
    },
    // 基础资料下一步
    dataNext () {
      this.visible = false;
    },

    onChange (e) {
      console.log(e.target.checked);
    },

    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
    onExpand (expandedKeys) {
      this.expandedKeys = expandedKeys;
      this.autoExpandParent = false;
    },
    onCheck (checkedKeys) {
      this.checkedKeys = checkedKeys;
    },
    onSelect (selectedKeys, info) {
      this.selectedKeys = selectedKeys;
    },
    handleOk () {
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
  cursor: pointer;
  font-size: 16px;
  margin: 0 10px;
}
</style>