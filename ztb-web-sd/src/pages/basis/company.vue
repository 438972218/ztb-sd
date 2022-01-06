<template>
  <div>
    <a-row :gutter="18">
      <a-col :span="6">
         <a-card style="max-height:600px;overflow:auto">
          <a-tree
            :auto-expand-parent="true"
            :defaultExpandAll="true"
            :selected-keys="selectedKeys"
            :tree-data="treeData"
            @select="onSelect"
          />
        </a-card>
      </a-col>
      <a-col :span="18">
        <a-card v-show="cardShow">
        <div class="content-title">
          {{titleList}}
        </div>
                <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :maskClosable="false"
        :model="form"
        :rules="rules"
        class="page-form"
      >
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row style="margin-top: 20px;">
          <a-form-model-item
            label="父级组织"
            prop="parentId"
            :labelCol="{ span: 4 }"
            :wrapperCol="{ span: 18 }"
          >
           <a-input v-model="form.parentId"/>
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="name"
            label="父级组织"
            :labelCol="{ span: 4 }"
            :wrapperCol="{ span: 18 }"
          >
            <a-input disabled/>
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="name"
            label="组织名称"
        :labelCol="{ span: 4 }"
            :wrapperCol="{ span: 18 }"
          >
            <a-input />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            label="组织代码"
             :labelCol="{ span: 4 }"
            :wrapperCol="{ span: 18 }"
          >
          <a-input disabled/>
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            label="组织描述"
             :labelCol="{ span: 4 }"
            :wrapperCol="{ span: 18 }"
          >
              <a-textarea :auto-size="{ minRows: 2, maxRows: 6 }" />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="name"
            label="联系人"
            :labelCol="{ span: 4 }"
            :wrapperCol="{ span: 18 }"
          >
            <a-input />
          </a-form-model-item>
        </a-row>
        <a-row>
          <a-form-model-item
            prop="name"
            label="地址"
              :labelCol="{ span: 4 }"
            :wrapperCol="{ span: 18 }"
          >
       <a-textarea :auto-size="{ minRows: 2, maxRows: 6 }" />
          </a-form-model-item>
        </a-row>
      </a-form-model>
                 <div class="bntStyle">
              <a-button @click="addClick()" type="primary">添加</a-button>
              <a-button @click="handleOk" type="primary">保存</a-button>
              <a-button @click="handleClear" >关闭</a-button>
             </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { ListorganizationRequest,AddorganizationRequest } from "@/services/system/user";
const treeData = [
  {
    title: "集团",
    key: "0-0",
    children: [
      {
        title: "xdc-数据中心",
        key: "0-0-0",
        children: [
          { title: "工业制作研究室", key: "0-0-0-0" },
          { title: "行政部", key: "0-0-0-1" },
          { title: "市场部", key: "0-0-0-2" },
        ],
      }
    ],
  }
];

export default {
  name: "company",
  data() {
    return {
      titleList:'',
      cardShow: true,
      expandedKeys: ["0-0-0"],
      selectedKeys: [],
      treeData,

      value: undefined,
      expandedRowKeys: [],
      form: {
        name: "",
      },
      rules: {
        name: [
          {
            required: true,
            message: "该条信息不能为空",
            trigger: "change,blur",
          },
        ],
      },
    };
  },
    created() {
    this.getAll();
  },
  methods: {
        //角色列表
    getAll() {
      ListorganizationRequest({
        currentPage: 1,
      }).then((response) => {
        this.data = response.data.records;
      });
    },
    // 添加
    addClick(){

    },
    onSelect(selectedKeys, info) {
      this.$refs.ruleForm.resetFields();
      this.cardShow = true
      this.titleList = info.node.title
      this.selectedKeys = selectedKeys;
    },
    c_delete() {
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {},
      });
    },
    handleClear(){
      this.cardShow = false
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
.join-style {
  span {
    cursor: pointer;
    margin: 10px;
  }
}
.search {
  margin-bottom: 54px;
}
.fold {
  width: calc(100% - 216px);
  display: inline-block;
}
// .operator {
//   margin: 5px 0 18px 0;
// }
@media screen and (max-width: 900px) {
  .fold {
    width: 100%;
  }
}
.iconstyle {
  text-align: center;
  cursor: pointer;
  font-size: 16px;
  margin: 0 10px;
}
.bntStyle{
  text-align: center;
  button{
    margin-left: 10px;
  }
}
</style>
