<template>
  <div>
    <a-form-model
      layout="horizontal"
      ref="ruleForm"
      :rules="rules"
      :model="form"
      :labelCol="{ span: 4 }"
      :wrapperCol="{ span: 18 }"
      class="page-form"
    >
      <a-row>
        <a-col :span="24">
          <a-form-model-item label="公司名称：" prop="fullName">
            <a-input
              v-model="form.fullName"
              :disabled="isTrue"
              placeholder="请输入公司名称"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="公司简称：" prop="shortName">
            <a-input
              v-model="form.shortName"
              :disabled="isTrue"
              placeholder="请输入公司简称"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="公司编码：" prop="code">
            <a-input
              v-model="form.code"
              :disabled="isTrue"
              placeholder="请输入公司编码"
            />
          </a-form-model-item>
        </a-col>
        <!-- <a-col :span="24">
          <a-form-model-item label="开户银行：" prop="bankAccount">
            <a-input
              v-model="form.bankAccount"
              :disabled="isTrue"
              placeholder="请输入开户银行"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="账号信息：" prop="bankAccountNumber">
            <a-input
              v-model="form.bankAccountNumber"
              :disabled="isTrue"
              placeholder="请输入账号信息"
            />
          </a-form-model-item>
        </a-col> -->
        <a-col :span="24">
          <a-form-model-item label="公司类型：" prop="companyType">
            <a-select
              v-model="form.companyType"
              :disabled="isTrue"
              placeholder="请选择公司类型"
            >
              <a-select-option :value="0"> 集团 </a-select-option>
              <a-select-option :value="1"> 公司 </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="公司上级：">
            <a-input
              disabled
              v-model="parent"
              placeholder="请选择公司上级"
              enter-button
              @search="parentSearch"
            >
              <a-icon slot="addonAfter" type="search" @click="parentSearch" />
            </a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="负责人：">
            <a-input
              v-model="form.principal"
              :disabled="isTrue"
              placeholder="请输入负责人"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="联系方式：">
            <a-input
              v-model="form.principalPhone"
              :disabled="isTrue"
              placeholder="请输入联系方式"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="公司网站：">
            <a-input
              v-model="form.website"
              :disabled="isTrue"
              placeholder="请输入公司网站"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="24" v-if="!isTrue">
          <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
            <a-button v-has="['company:edit','company:add']" type="primary" @click="handleSave"> 保存 </a-button>
            <!-- <a-button style="margin-left: 10px" @click="resetForm">
                    重置
                  </a-button> -->
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
    <company ref="company" @selectData="selectData"></company>
  </div>
</template>
<script>
import { sysCompanyAdd, sysCompanyUpdate } from "@/services/structure/index";
import company from './company.vue'

export default {
  props: {
    form: {
      type: Object,
      default: () => {
        return {}
      }
    },
    isTrue: {
      type: Boolean,
      default: false
    }
  },
  name: "companyAdd",
  components: { company },
  data () {
    return {
      rules: {
        parentCompanyType: 0,
        fullName: [{ required: true, message: '请输入公司名称', trigger: 'blur' }],
        shortName: [{ required: true, message: '请输入公司简称', trigger: 'blur' }],
        code: [{ required: true, message: '请输入公司编码', trigger: 'blur' }],
        bankAccount: [{ required: true, message: '请输入开户银行', trigger: 'blur' }],
        bankAccountNumber: [{ required: true, message: '请输入账号信息', trigger: 'blur' }]
      },
      parent: '',
      visible: false
    };
  },
  created () {
  },
  methods: {
    // 查询公司信息
    parentSearch () {
      this.$refs.company.visible = true
      this.$refs.company.getDataList()
    },
    // 选中的公司信息
    selectData (row) {
      this.parentCompanyType = row.parentCompanyType ? row.parentCompanyType : 0
      this.parent = row.fullName
      this.form.parentId = row.id
    },
    // 保存数据
    async handleSave () {
      if (this.form.companyType === 0 && this.parentCompanyType === 1) {
        this.$message.warning('公司下不可建集团')
        return
      } else if (this.form.companyType === 1 && this.form.parentId === '0') {
        this.$message.warning('公司必须选择上级')
        return
      }
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          let result = null
          if (this.form.id) {
            result = await sysCompanyUpdate(this.form)
          } else {
            result = await sysCompanyAdd(this.form)
          }
          if (result.code === 0) {
            this.$message.success('保存成功')
            this.$emit('queryData')
          } else {
            this.$message.error(result.message)
          }
        }
      });

    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>