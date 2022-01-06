<template>
  <div>
    <a-form-model
      layout="horizontal"
      ref="ruleForm"
      :model="form"
      :labelCol="{ span: 4 }"
      :wrapperCol="{ span: 18 }"
      class="page-form"
    >
      <a-row>
        <a-col :span="24">
          <a-form-model-item label="部门名称：">
            <a-input v-model="form.fullName" placeholder="请输入部门名称" />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="部门简称：">
            <a-input v-model="form.shortName" placeholder="请输入部门简称" />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="部门编码：">
            <a-input v-model="form.code" placeholder="请输入部门编码" />
          </a-form-model-item>
        </a-col>
        <!-- <a-col :span="24">
            <a-form-model-item label="公司：">
              <a-input disabled v-model="company" placeholder="请选择公司">
              </a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="部门上级：">
              <a-input disabled v-model="parent" placeholder="请选择部门上级">
              </a-input>
            </a-form-model-item>
          </a-col> -->
        <a-col :span="24">
          <a-form-model-item label="部门负责人：">
            <a-input
              disabled
              v-model="manager"
              placeholder="请选择部门负责人"
              enter-button
              @search="managerSearch"
            >
              <a-icon slot="addonAfter" type="search" @click="managerSearch" />
            </a-input>
            <!-- <a-input v-model="form.manager" placeholder="请输入部门负责人" /> -->
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
            <a-button
              v-has="['department:edit', 'department:add']"
              type="primary"
              @click="handleSave"
            >
              保存
            </a-button>
            <!-- <a-button style="margin-left: 10px" @click="resetForm">
                    重置
                  </a-button> -->
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
    <staff ref="staff" @selectData="selectData"></staff>
  </div>
</template>
<script>
import { sysDepartmentAdd, sysDepartmentUpdate } from "@/services/structure/index";
import staff from './staff.vue'
export default {
  props: {
    form: {
      type: Object,
      default: () => {
        return {}
      }
    },
    department: {
      type: String,
      default: ''
    }
  },
  name: "companyAdd",
  components: { staff },
  data () {
    return {
      manager: '',
      parent: '1',
      visible: false
    };
  },
  created () {
  },
  methods: {
    // 查询员工信息
    managerSearch () {
      this.$refs.staff.visible = true
      this.$refs.staff.getDataList()
    },
    // 选中的员工信息
    selectData (row) {
      this.manager = row.realName
      this.form.manager = row.id
    },
    // 保存数据
    async handleSave () {
      let result = null
      if (this.form.id) {
        result = await sysDepartmentUpdate(this.form)
      } else {
        result = await sysDepartmentAdd(this.form)
      }
      if (result.code === 0) {
        this.$message.success('保存成功')
        this.$emit('queryData')
      } else {
        this.$message.error(result.message)
      }
    },
    // 弹窗关闭
    handleColse () {
      this.visible = false
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>