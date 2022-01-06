<template>
  <div>
    <a-form
      layout="horizontal"
      :labelCol="{ span: 6 }"
      :wrapperCol="{ span: 18 }"
    >
      <a-row :gutter="20">
        <a-col :span="8" v-if="tag !== '5'">
          <a-form-item label="实际付款金额：">
            <a-input disabled placeholder="请输入实际付款金额" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="签章方式：">
            <a-select :disabled="isTrue" placeholder="请选择签章方式">
              <a-select-option value="1"> 格式合同 </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="印章名称：">
            <a-input placeholder="请输入印章名称" disabled />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="申请人：">
            <a-input placeholder="请输入申请人" disabled />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="合同开始日期：">
            <a-date-picker :disabled="isTrue" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="合同终止日期：">
            <a-date-picker :disabled="isTrue" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="上传协议附件：">
            <a-checkbox :disabled="isTrue"> </a-checkbox>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="合同终止条件：">
            <a-select :disabled="isTrue" placeholder="请选择合同终止条件">
              <a-select-option value="1"> 请选择 </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="数量/金额：">
            <a-input placeholder="请输入数量/金额" disabled />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item
            label="合同备注："
            :labelCol="{ span: 2 }"
            :wrapperCol="{ span: 22 }"
          >
            <a-textarea
              :disabled="isTrue"
              placeholder="请输入合同备注"
              :rows="3"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24" v-if="tag !== '5'">
          <a-form-item
            label="合同风险提示："
            :labelCol="{ span: 2 }"
            :wrapperCol="{ span: 22 }"
          >
            <a-textarea
              :disabled="isTrue"
              placeholder="请输入合同风险提示"
              :rows="3"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </div>
</template>

<script>
export default {
  props: {
    isTrue: {
      type: Boolean,
      default: false
    },
    tag: {
      type: String,
      default: ''
    }
  },
  name: "otherInformation",
  data () {
    return {
    }
  },
  methods: {
  }
};
</script>

<style lang="less" scoped>
</style>
