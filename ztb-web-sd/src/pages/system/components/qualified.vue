<template>
  <div>
    <a-modal
      width="65%"
      :title="title"
      :visible="visible"
      @ok="handleOk"
      @cancel="visible = false"
    >
      <a-form-model
        :model="formInline"
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 18 }"
      >
        <a-row :gutter="20">
          <a-col :span="5">
            <a-form-model-item label="字段：">
              <a-select
                v-model="formInline.field"
                style="width: 100%"
                placeholder="请选择字段"
                @change="handleChange"
              >
                <a-select-option
                  v-for="item in fieldList"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="条件：">
              <a-select
                v-model="formInline.condition"
                style="width: 100%"
                placeholder="请选择条件"
              >
                <a-select-option value="=="> == </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="值：">
              <a-select
                v-model="formInline.value"
                style="width: 100%"
                placeholder="请选择值"
              >
                <a-select-option
                  v-for="item in valueList"
                  :key="item.numerical"
                  :value="item.numerical"
                >
                  {{ item.meaning }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="5">
            <a-form-model-item label="关系：">
              <a-select
                v-model="formInline.relationship"
                allowClear
                style="width: 100%"
                placeholder="请选择关系"
              >
                <a-select-option value="and"> and </a-select-option>
                <a-select-option value="or"> or </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="4" align="right">
            <a-button type="primary" @click="handleAdd">添加</a-button>
          </a-col>
          <a-col :span="24" v-for="(item, index) in contentList" :key="index">
            <a-col :span="10">
              <a-form-model-item
                label="限定词："
                :labelCol="{ span: 3 }"
                :wrapperCol="{ span: 21 }"
              >
                <a-input disabled :default-value="item.contentName" />
              </a-form-model-item>
            </a-col>
            <a-col :span="4">
              <a-form-model-item>
                {{ item.relationship }}
              </a-form-model-item>
            </a-col>
            <a-col :span="10" align="right">
              <a-button @click="handleDelete(index)">取消</a-button>
            </a-col>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import { dictionaryRequestAll } from "@/services/process/workflow";
export default {
  name: "qualified",
  props: {
    title: {
      type: String,
      default: '限定词'
    }
  },
  components: {},
  data () {
    return {
      visible: false,
      formInline: {
        field: undefined,
        condition: undefined,
        value: undefined,
        relationship: undefined,
        content: undefined
      },
      fieldList: [],
      valueList: [],
      allList: [],
      contentList: []
    };
  },
  created () {
  },
  methods: {
    // 获取招投标字典数据
    async getDataList () {
      const data = {
        currentPage: -1
      }
      const result = await dictionaryRequestAll(data)
      if (result.code === 0) {
        this.allList = result.data.records
        let list = []
        result.data.records.forEach(item => {
          list.push({
            id: item.dictionaryClass,
            name: item.dictionaryChinese
          })
        })
        this.fieldList = this.unique(list)
      } else {
        this.$message.error(result.message)
      }
    },
    // 添加
    handleAdd () {
      const field = this.fieldList.find(f => f.id === this.formInline.field).name
      const value = this.valueList.find(f => f.numerical === this.formInline.value).meaning
      this.contentList.push({
        contentName: field + this.formInline.condition + value,
        content: this.formInline.field + this.formInline.condition + this.formInline.value,
        relationship: this.formInline.relationship
      })
    },
    // 取消限定词
    handleDelete (index) {
      this.contentList.splice(index, 1)
    },
    // 去重
    unique (arr) {
      const res = new Map()
      return arr.filter((arr) => !res.has(arr.id) && res.set(arr.id, 1))
    },
    // 选择事件
    handleChange (e) {
      this.valueList = this.allList.filter(f => f.dictionaryClass === e)
    },
    // 确定事件
    async handleOk () {
      let script = ''
      let scriptName = ''
      this.contentList.forEach((item, index) => {
        const relationship = item.relationship ? item.relationship : ''
        if (index !== 0) {
          script = '(' + script
          scriptName = '(' + scriptName
        }
        script += index === 0 ? '(' + item.content + ')' + relationship : '(' + item.content + '))' + relationship
        scriptName += index === 0 ? '(' + item.contentName + ')' + relationship : '(' + item.contentName + '))' + relationship
      })
      const scr = script.substring(script.length - 1)
      if (!script) {
        this.$message.warning('请添加限定词')
        return
      }
      if (scr !== ')') {
        this.$message.warning('结尾不可添加关系')
        return
      }
      this.visible = false
      this.$emit('selectData', script, scriptName)
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>