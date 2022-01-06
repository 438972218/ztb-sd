<template>
  <div>
    <a-card>
      <a-row :gutter="20" v-if="!buttonType">
        <a-col :span="24">
          <a-button
            type="primary"
            v-if="buttonShow === '4'"
            style="margin-right: 10px"
          >
            合同终止
          </a-button>
          <a-button
            type="primary"
            v-if="buttonShow === '4'"
            style="margin-right: 10px"
          >
            阶段调整
          </a-button>
          <a-button
            type="primary"
            v-if="buttonShow === '4'"
            style="margin-right: 10px"
          >
            导出
          </a-button>
          <a-button
            type="primary"
            v-if="buttonShow === '3' || buttonShow === '5'"
            style="margin-right: 10px"
          >
            合同签署
          </a-button>
          <a-button
            type="primary"
            v-if="buttonShow === '5'"
            style="margin-right: 10px"
          >
            拒绝
          </a-button>
          <a-button
            type="primary"
            v-if="buttonShow === '2'"
            style="margin-right: 10px"
          >
            编辑
          </a-button>
          <a-button
            type="primary"
            v-if="buttonShow === '2'"
            style="margin-right: 10px"
          >
            发布
          </a-button>
          <a-button
            type="primary"
            v-if="buttonShow === '1'"
            style="margin-right: 10px"
          >
            保存
          </a-button>
          <a-button
            type="primary"
            v-if="buttonShow === '1'"
            style="margin-right: 10px"
          >
            提交
          </a-button>
          <a-button
            type="primary"
            style="margin-right: 10px"
            @click="visible = true"
          >
            合同附件
          </a-button>
          <a-button
            v-if="buttonShow !== '1'"
            type="primary"
            style="margin-right: 10px"
            @click="visible1 = true"
          >
            流转记录
          </a-button>
          <a-button type="primary" style="margin-right: 10px"> 预览 </a-button>
          <a-button type="primary" @click="back"> 返回 </a-button>
        </a-col>
      </a-row>
      <a-form
        layout="horizontal"
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row :gutter="20">
          <a-col :span="6">
            <a-form-item label="合同编号：">
              <a-input
                default-value="待生成"
                disabled
                placeholder="请输入合同编号"
              />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="主合同编号：">
              <a-input :disabled="isTrue" placeholder="请输入主合同编号" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="单据来源：">
              <a-input :disabled="isTrue" placeholder="请输入单据来源" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="合同名称：">
              <a-input :disabled="isTrue" placeholder="请输入合同名称" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="公司名称：">
              <a-input :disabled="isTrue" placeholder="请输入公司名称" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="状态：">
              <a-select default-value="新建" placeholder="请选择状态" disabled>
                <a-select-option value="新建"> 新建 </a-select-option>
                <a-select-option value="待审批"> 待审批 </a-select-option>
                <a-select-option value="待发布"> 待发布 </a-select-option>
                <a-select-option value="待供方签约">
                  待供方签约
                </a-select-option>
                <a-select-option value="待买方签约">
                  待买方签约
                </a-select-option>
                <a-select-option value="执行中"> 执行中 </a-select-option>
                <a-select-option value="已完成"> 已完成 </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="合同申请部门：">
              <a-input :disabled="isTrue" placeholder="请输入合同申请部门" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="合同对象：">
              <a-input :disabled="isTrue" placeholder="请输入合同对象" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="合同性质：">
              <a-select :disabled="isTrue" placeholder="请选择合同性质">
                <a-select-option value="格式合同"> 格式合同 </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="合同模板：">
              <a-input :disabled="isTrue" placeholder="请输入合同模板" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="合同分类：">
              <a-input disabled placeholder="请输入合同分类" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="标的类型：">
              <a-input disabled placeholder="请输入标的类型" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="付款方式：">
              <a-input :disabled="isTrue" placeholder="请输入付款方式" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="币种：">
              <a-select :disabled="isTrue" placeholder="请选择币种">
                <a-select-option value="CNY"> CNY </a-select-option>
                <a-select-option value="HKD"> HKD </a-select-option>
                <a-select-option value="USD"> USD </a-select-option>
                <a-select-option value="JPY"> JPY </a-select-option>
                <a-select-option value="EUR"> EUR </a-select-option>
                <a-select-option value="SGD"> SGD </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="金额（元）：">
              <a-input :disabled="isTrue" placeholder="请输入金额" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="含税：">
              <a-checkbox :disabled="isTrue"> </a-checkbox>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="报价税码：">
              <a-select
                :disabled="isTrue"
                default-value="1"
                placeholder="请选择报价税码"
              >
                <a-select-option value="1"> 中国增值税_13% </a-select-option>
                <a-select-option value="2"> 中国增值税_9% </a-select-option>
                <a-select-option value="3"> 中国增值税_6% </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="税率：">
              <a-input default-value="13%" disabled placeholder="请输入税率" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="价格类型：">
              <a-select
                :disabled="isTrue"
                default-value="1"
                placeholder="请选择价格类型"
              >
                <a-select-option value="1"> 标准价格 </a-select-option>
                <a-select-option value="2"> 阶梯价格 </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="合同起草日期：">
              <a-input
                default-value="2021-06-03"
                disabled
                placeholder="请输入合同起草日期"
              />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="创建人：">
              <a-input disabled placeholder="请输入创建人" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-card>
    <a-card>
      <a-tabs type="card" tab-position="left">
        <a-tab-pane key="1" tab="其他信息">
          <otherInformation
            :isTrue="isTrue"
            :tag="buttonShow"
            ref="otherInformation"
          ></otherInformation>
        </a-tab-pane>
        <a-tab-pane key="7" tab="审批信息" v-if="buttonShow === '2'">
          <a-row :gutter="20">
            <a-col :spna="24">
              <a-row :gutter="20">
                <a-form-model :label-col="labelCol" :wrapper-col="wrapperCol">
                  <a-col :span="12">
                    <a-form-model-item label="审批结果：">
                      <a-select placeholder="请选择审批结果">
                        <a-select-option value="1"> 审批通过 </a-select-option>
                        <a-select-option value="2">
                          审批不通过
                        </a-select-option>
                        <a-select-option value="3"> 转交 </a-select-option>
                      </a-select>
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="12">
                    <a-form-model-item label="转交人：">
                      <a-input placeholder="请输入转交人"></a-input>
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="12">
                    <a-form-model-item label="审批意见：">
                      <a-textarea placeholder="请输入审批意见" :rows="4" />
                    </a-form-model-item>
                  </a-col>
                </a-form-model>
              </a-row>
            </a-col>
            <a-col :span="24">
              <a-table :columns="columns1" :data-source="data1"> </a-table>
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane key="2" tab="合同标的">
          <a-button v-if="!isTrue" type="primary">添加</a-button>
          <contractObject ref="contractObject"></contractObject>
        </a-tab-pane>
        <a-tab-pane key="3" tab="合同伙伴" v-if="buttonShow !== '5'">
          <a-button v-if="!isTrue" type="primary">保存</a-button>
          <contractPartner ref="contractPartner"></contractPartner>
        </a-tab-pane>
        <a-tab-pane key="4" tab="合同附件">
          <a-button v-if="!isTrue" type="primary">添加</a-button>
          <contractAppendices ref="contractAppendices"></contractAppendices>
        </a-tab-pane>
        <a-tab-pane key="5" tab="合同业务信息" v-if="buttonShow !== '5'">
          <a-button v-if="!isTrue" type="primary">保存</a-button>
          <contractInformation ref="contractInformation"></contractInformation>
        </a-tab-pane>
        <a-tab-pane key="6" tab="合同阶段">
          <a-button v-if="!isTrue || buttonShow === '4'" type="primary"
            >添加</a-button
          >
          <contractPhase ref="contractPhase"></contractPhase>
        </a-tab-pane>
      </a-tabs>
    </a-card>
    <a-modal
      title="附件列表"
      :visible="visible"
      :footer="null"
      width="60%"
      @cancel="handleCancel"
    >
      <a-table :columns="columns" :data-source="data">
        <span slot="a6">
          <a-icon type="delete" title="删除" class="iconstyle" />
        </span>
      </a-table>
    </a-modal>
  </div>
</template>

<script>
import otherInformation from './components/otherInformation.vue'
import contractObject from './components/contractObject.vue'
import contractPartner from './components/contractPartner.vue'
import contractAppendices from './components/contractAppendices.vue'
import contractInformation from './components/contractInformation.vue'
import contractPhase from './components/contractPhase.vue'
const columns = [
  {
    title: '序号',
    dataIndex: 'a1',
    key: 'a1',
  },
  {
    title: '附件名',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '附件类型',
    dataIndex: 'a3',
    key: 'a3',
  },
  {
    title: '上传人',
    dataIndex: 'a4',
    key: 'a4',
  },
  {
    title: '上传时间',
    dataIndex: 'a5',
    key: 'a5',
  },
  {
    title: '操作',
    dataIndex: 'a6',
    key: 'a6',
    scopedSlots: { customRender: 'a6' },
  }
];
const data = [
  {
    a1: '1',
    a2: '111',
    a3: 'doc',
    a4: '李四',
    a5: '2020-05-01 12:12:12',
    a6: ''
  }
];
const columns1 = [
  {
    title: '序号',
    dataIndex: 'a1',
    key: 'a1',
  },
  {
    title: '时间',
    dataIndex: 'a2',
    key: 'a2',
  },
  {
    title: '审批人',
    dataIndex: 'a3',
    key: 'a3',
  },
  {
    title: '审批结果',
    dataIndex: 'a4',
    key: 'a4',
  },
  {
    title: '审批意见',
    dataIndex: 'a5',
    key: 'a5',
  }
];
const data1 = [
  {
    a1: '1',
    a2: '2021-05-01 11:11:11',
    a3: '采购员1',
    a4: '提交',
    a5: ''
  },
  {
    a1: '2',
    a2: '2021-05-02 16:11:11',
    a3: '管理员2',
    a4: '审批通过',
    a5: '同意'
  }
];

export default {
  props: {
    isTrue: {
      type: Boolean,
      default: false
    },
    buttonShow: {
      type: String,
      default: '1'
    },
    buttonType: {
      type: Boolean,
      default: false
    }
  },
  name: "accessAdd",
  components: { otherInformation, contractObject, contractPartner, contractAppendices, contractInformation, contractPhase },
  data () {
    return {
      visible: false,
      columns,
      data,
      columns1,
      data1
    }
  },
  methods: {
    back () {
      this.$router.push('basic')
    },
    // 关闭弹窗
    handleCancel () {
      this.visible = false
    }
  }
};
</script>

<style lang="less" scoped>
.title {
  color: @title-color;
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 16px;
}
.page-form {
  margin: 0 20px;
  &-header {
    border: 1px solid rgb(235, 237, 240);
    font-size: 16px;
    padding: 5px 10px;
    &-row {
      margin-top: 10px;
      height: 20px;
      &-item {
        height: 20px;
      }
    }
  }
}
.btn-sty {
  text-align: center;
  margin: 20px 0;
}
</style>
