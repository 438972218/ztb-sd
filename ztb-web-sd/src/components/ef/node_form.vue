<template>
  <div>
    <div class="ef-node-form">
      <div class="ef-node-form-header">编辑</div>
      <div class="ef-node-form-body">
        <el-form
          :model="node"
          ref="dataForm"
          label-width="100px"
          v-show="type === 'node'"
        >
          <el-form-item label="类型">
            <el-input v-model="node.type" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="节点名称">
            <el-input v-model="node.name"></el-input>
          </el-form-item>
               <!-- <el-form-item label="角色名称">
            <el-input v-model="node.name"></el-input>
          </el-form-item>
                    <el-form-item label="组织类型">
            <el-input v-model="node.name"></el-input>
          </el-form-item> -->
                
          <el-form-item label="left坐标">
            <el-input v-model="node.left" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="top坐标">
            <el-input v-model="node.top" :disabled="true"></el-input>
          </el-form-item>
          <!-- <el-form-item label="ico图标">
            <el-input v-model="node.ico"></el-input>
          </el-form-item> -->
                 <!-- <el-form-item label="条件名称(接口调用)">
            <el-input v-model="node.condition"></el-input>
          </el-form-item> -->
          <el-form-item label="条件">
            <a-row>
              <a-col span="18">
                <el-input
                  v-model="node.script"
                  :title="node"
                  :disabled="true"
                ></el-input
              ></a-col>
              <a-col span="4" :offset="2" ><a-icon class="plus-cur" type="plus" @click="plusRules"/></a-col>
            </a-row>
          </el-form-item>
                 <el-form-item label="字段条件">
            <a-row>
              <a-col span="18">
                <el-input
                  v-model="node.script"
                  :title="node"
                  :disabled="true"
                ></el-input
              ></a-col>
              <a-col span="4" :offset="2" ><a-icon class="plus-cur" type="plus" @click="plusRulesChild"/></a-col>
            </a-row>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="node.state" placeholder="请选择">
              <el-option
                v-for="item in stateList"
                :key="item.state"
                :label="item.label"
                :value="item.state"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-close">重置</el-button>
            <el-button type="primary" icon="el-icon-check" @click="save"
              >保存</el-button
            >
          </el-form-item>
        </el-form>

        <!-- <el-form
          :model="line"
          ref="dataForm"
          label-width="80px"
          v-show="type === 'line'"
        >
          <el-form-item label="条件">
            <el-input v-model="line.label"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-close">重置</el-button>
            <el-button type="primary" icon="el-icon-check" @click="saveLine"
              >保存</el-button
            >
          </el-form-item>
        </el-form> -->
      </div>
      <!--            <div class="el-node-form-tag"></div>-->
    </div>
     <a-modal v-model="rulesModel" centered title="添加规则" :width="800" :footer="null">
         <rules     @rulesPass="rulesPass"    @on-rule-close="ruleClose" :rulesModel="rulesModel"></rules>
    </a-modal>
         <a-modal v-model="rulesChildModel" centered title="添加子流程规则" :width="800" :footer="null">
         <rules-child  @rulesPass="rulesPass"  @on-rule-child-close="ruleChildClose" :rulesChildModel="rulesChildModel"></rules-child>
    </a-modal>
  </div>
</template>

<script>
import { cloneDeep } from "lodash";
import Rules from './rules.vue'
import RulesChild from './rulesChild.vue'


export default {
  components: { Rules, RulesChild },
  data() {
    return {
      rulesModel: false,
      rulesChildModel:false,
      // node 或 line
      type: "node",
      node: {
      },
      line: {},
      data: {},
      stateList: [
        {
          state: "success",
          label: "成功",
        },
        {
          state: "warning",
          label: "警告",
        },
        {
          state: "error",
          label: "错误",
        },
        {
          state: "running",
          label: "运行中",
        },
      ],
    };
  },
  methods: {
      //添加规则
      plusRules(){
          this.rulesModel = true
      },
      plusRulesChild(){
      this.rulesChildModel = true
      },
      ruleClose(){
           this.rulesModel = false
      },
         ruleChildClose(){
           this.rulesChildModel = false
      },
      rulesPass(data){
          this.node.script = data
      },
    /**
     * 表单修改，这里可以根据传入的ID进行业务信息获取
     * @param data
     * @param id
     */
    nodeInit(data, id) {
      this.type = "node";
      this.data = data;
      data.nodeList.filter((node) => {
        if (node.id === id) {
          this.node = cloneDeep(node);
        }
      });
    },
    lineInit(line) {
      this.type = "line";
      this.line = line;
    },
    // 修改连线
    saveLine() {
      this.$emit("setLineLabel", this.line.from, this.line.to, this.line.label);
    },
    save() {
      this.data.nodeList.filter((node) => {
 
        if (node.id === this.node.id) {
          node.name = this.node.name;
          node.left = this.node.left;
          node.top = this.node.top;
          node.ico = this.node.ico;
          node.condition = this.node.condition;
          node.script = this.node.script;
          node.state = this.node.state;
          this.$emit("repaintEverything");
        }
      });
    },
  },
};
</script>

<style>
.el-node-form-tag {
  position: absolute;
  top: 50%;
  margin-left: -15px;
  height: 40px;
  width: 15px;
  background-color: #fbfbfb;
  border: 1px solid rgb(220, 227, 232);
  border-right: none;
  z-index: 0;
}
.plus-cur{
    cursor: pointer;
}
</style>
