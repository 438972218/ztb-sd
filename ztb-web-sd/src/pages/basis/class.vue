<template>
  <div>
    <a-row :gutter="18">
      <a-col :span="6">
        <a-card style="max-height: 600px; overflow: auto">
          <a-button v-has="['category:add']" v-show="codeShow" type="primary" @click="classAdd" style="margin-right: 10px">
            新增品类
          </a-button>
          <a-tree v-if="treeData.length" :tree-data="treeData" :defaultExpandAll="true" @select="onSelect" />
        </a-card>
      </a-col>
      <a-col :span="18">
        <a-card>
          <!-- <div class="content-title">
            {{ titleList }}
          </div> -->
          <a-form-model
            layout="horizontal"
            ref="ruleForm"
            :maskClosable="false"
            :model="form"
            :rules="rules"
            class="page-form"
          >
            <!-- <div :class="advanced ? null: 'fold'"> -->
            <a-row style="margin-top: 20px" >
              <a-form-model-item
              v-if="parentTreeShow"
                label="父级品类"
                prop="pId"
                :labelCol="{ span: 4 }"
                :wrapperCol="{ span: 18 }"
              >
                <a-tree-select
                  v-model="form.pId"
                  show-search
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  placeholder="请选择父级品类"
                  allow-clear
                  tree-default-expand-all
                >
                  <a-icon slot="suffixIcon" type="smile" />
                  <a-tree-select-node
                    v-for="item in treeList"
                    :key="item.id"
                    :value="item.id"
                    :title="item.name"
                  >
                    <a-tree-select-node
                      v-for="itemA in item.children"
                      :key="itemA.id"
                      :value="itemA.id"
                      :title="itemA.name"
                    >
                      <a-tree-select-node
                        v-for="itemB in itemA.children"
                        :key="itemB.id"
                        :value="itemB.id"
                        :title="itemB.name"
                      >
                      </a-tree-select-node>
                    </a-tree-select-node>
                  </a-tree-select-node>
                </a-tree-select>
              </a-form-model-item>
            </a-row>
            <a-row v-show="codeShow">
              <a-form-model-item
                prop="code"
                label="品类代码"
                :labelCol="{ span: 4 }"
                :wrapperCol="{ span: 18 }"
              >
                <a-input v-model="form.code" />
              </a-form-model-item>
            </a-row>
            <a-row>
              <a-form-model-item
                prop="name"
                label="品类名称"
                :labelCol="{ span: 4 }"
                :wrapperCol="{ span: 18 }"
              >
                <a-input v-model="form.name" />
              </a-form-model-item>
            </a-row>
            <a-row>
              <a-form-model-item
                label="类型"
                prop="type"
                :labelCol="{ span: 4 }"
                :wrapperCol="{ span: 18 }"
              >
                <a-select v-model="form.type" placeholder="请选择">
                  <a-select-option value="1">电子产品</a-select-option>
                  <a-select-option value="2">塑料制品</a-select-option>
                </a-select>
              </a-form-model-item>
            </a-row>
            <a-row>
              <a-form-model-item
                label="备注"
                prop="remark"
                :labelCol="{ span: 4 }"
                :wrapperCol="{ span: 18 }"
              >
                <a-textarea
                  v-model="form.remark"
                  :auto-size="{ minRows: 2, maxRows: 6 }"
                />
              </a-form-model-item>
            </a-row>
          </a-form-model>
          <div class="bntStyle">
            <a-button v-has="['category:add']" @click="addClass()" v-show="!codeShow" type="primary"
              >添加</a-button
            >
            <a-button v-has="['category:edit']" @click="editorClass" v-show="codeShow" type="primary"
              >保存</a-button
            >
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {
  categoryGetRequest,
  categoryPostRequest,
  categoryPutRequest,
  categorytreeGetRequest,
} from "@/services/basis/index";
import { treeCreate } from "@/utils/methods";

export default {
  name: "class",
  data() {
    return {
      parentTreeShow: true,
      codeShow: false,
      titleList: "",
      expandedKeys: [],
      treeData: [],
      treeList: [],
      value: undefined,
      expandedRowKeys: [],
      form: {
        pId: "", // 父级品类
        code: "", //代码
        name: "", // 名称
        type: "", // 类型
        remark: "", // 备注
      },
      rules: {
        pId: [
          {
            required: true,
            message: "父级品类不能为空",
            trigger: "change,blur",
          },
        ],
        name: [
          {
            required: true,
            message: "名称不能为空",
            trigger: "change,blur",
          },
        ],
      },
    };
  },
  mounted() {
    this.getAll();
  },
  methods: {
    // 查询数据
    getAllOdd(id) {
      categoryGetRequest({
        currentPage: "1",
        id: id,
      }).then((response) => {
        this.form = response.data.records[0];
      });
    },
    getAll() {
      categorytreeGetRequest({
        currentPage: "1",
      })
        .then((response) => {
          this.treeData = JSON.parse(JSON.stringify(response.data));
          this.treeList = response.data;
          treeCreate(this.treeData);
        })
    },
    // 添加品类
    addClass() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          categoryPostRequest({
            ...this.form,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("添加成功");
              this.getAll();
            } else {
              this.$message.error(response.message);
            }
          });
        } else {
          return false;
        }
      });
    },
    // 切换添加页码
    classAdd() {
      this.$refs.ruleForm.resetFields();
       this.parentTreeShow =true
      this.codeShow = false;
    },
    onSelect(selectedKeys, e) {
      if(e.node.pos === '0-0'){
        this.parentTreeShow =false
      }else{
        this.parentTreeShow =true
      }
      this.codeShow = true;
      this.getAllOdd(e.node.eventKey);
      this.$refs.ruleForm.resetFields();
      this.titleList = e.node.title;
    },
  // 修改
    editorClass() {
      if(!this.parentTreeShow){
        delete this.form.pId
      }
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          categoryPutRequest({
            ...this.form,
          }).then((response) => {
            if (response.code === 0) {
              this.$message.success("修改成功");
              this.getAll();
            } else {
              this.$message.error(response.message);
            }
          });
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
  cursor: pointer;
  font-size: 16px;
  margin: 0 10px;
}
.bntStyle {
  text-align: center;
  button {
    margin-left: 10px;
  }
}
</style>
