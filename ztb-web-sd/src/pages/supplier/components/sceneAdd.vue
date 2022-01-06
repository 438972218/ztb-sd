<template>
  <div>
    <a-card class="card-top">
      <a-alert
        v-if="form.id"
        style="margin-bottom: 10px"
        :message="'供应商绩效：' + form.vendorCode + '-' + form.vendorName"
        type="info"
      >
        <a-row slot="description">
          <a-col :span="24" align="right">
            <span style="color: #248c00; font-size: 22px">
              {{ form.requestStatusName }}
            </span>
          </a-col>
        </a-row>
      </a-alert>
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :rules="rules"
        :model="form"
      >
        <a-row>
          <a-col :span="24" style="margin-bottom: 20px">
            <a-button
              v-has="['vendor_kpi:edit']"
              type="primary"
              @click="edit"
              v-if="form.id"
            >
              保存
            </a-button>
            <a-button
              v-has="['vendor_kpi:add']"
              style="margin-left: 10px"
              v-if="form.requestStatusName === '待发布'"
              type="primary"
              :loading="documentLoading"
              @click="agree()"
              >发布
            </a-button>
            <a-button
              v-has="['vendor_kpi:add']"
              style="margin-left: 10px"
              v-if="form.requestStatusName === '待评分'"
              type="primary"
              :loading="documentLoading"
              @click="agree()"
              >提交
            </a-button>
            <a-button
              v-has="['vendor_kpi:add']"
              type="primary"
              style="margin-left: 10px"
              @click="submit"
              v-if="!form.id"
            >
              提交
            </a-button>
            <a-button @click="back()" style="margin-left: 10px">
              返回
            </a-button>
            <a-button
              v-has="['vendor_kpi:delete']"
              @click="destroy()"
              style="margin-left: 10px"
            >
              删除
            </a-button>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="vendorId"
              class="form-item-style"
              label="供应商名称"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 16 }"
            >
              <a-select
                v-model="form.vendorId"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option
                  v-for="item in vendorList"
                  :key="item.id"
                  :value="item.id"
                  >{{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="name"
              class="form-item-style"
              label="绩效考核表名称"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 16 }"
            >
              <a-input v-model="form.name" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="templateName"
              class="form-item-style"
              label="评估模板"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 16 }"
            >
              <a-select
                v-model="form.templateName"
                @change="bankChange"
                placeholder="请选择"
                :disabled="disableds"
              >
                <a-select-option value="模板1">模板1</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :sm="24" :md="12" :xl="12">
            <a-form-model-item
              prop="responsiblePersonId"
              class="form-item-style"
              label="负责人"
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 16 }"
            >
              <a-select
                v-model="form.responsiblePersonId"
                placeholder="请选择负责人"
                :disabled="disableds"
                @change="personChange"
              >
                <a-select-option
                  v-for="item in personList"
                  :key="item.id"
                  :value="item.id"
                  >{{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :sm="24" :md="24" :xl="24">
            <a-form-model-item
              prop="remark"
              class="form-item-style"
              label="备注"
              :labelCol="{ span: 3 }"
              :wrapperCol="{ span: 20 }"
            >
              <a-input v-model="form.remark" :disabled="disableds" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card>
      <a-table
        v-if="dataSource && dataSource.length"
        :columns="columns"
        :rowKey="record => record.id"
        childrenColumnName="childVendorQuestionBankVOS"
        :dataSource="dataSource"
        :defaultExpandAllRows="true"
        :expanded-row-keys.sync="expandedRowKeys"
      >
        <template slot="balance" slot-scope="text, record">
          <span v-if="text == null || text == ''">
            {{ text }}
          </span>
          <span v-else>
            <a-input
              v-model="record.balance"
              :disabled="disableds"
              style="width: 100px"
            />
          </span>
        </template>
        <template
          v-for="col in ['name', 'content', 'insDesc']"
          :slot="col"
          slot-scope="text, record"
        >
          <div :key="col">
            <span
              v-if="
                (record.balance == null || record.balance == '') && !form.id
              "
            >
              <a-textarea
                v-model="record[col]"
                :auto-size="{ minRows: 3, maxRows: 5 }"
                @change="e => inputChange(e.target.value, col, record)"
              />
            </span>
            <span v-else class="text-wrapper">
              {{ text }}
            </span>
          </div>
        </template>
        <template slot="scoreScope" slot-scope="text, record">
          <span
            v-if="(record.balance == null || record.balance == '') && !form.id"
          >
            <a-input
              v-model="record.scoreScope"
              @change="e => inputChange(e.target.value, 'scoreScope', record)"
              style="width: 100px"
            />
          </span>
          <span v-else>
            {{ text }}
          </span>
        </template>
        <template slot="score" slot-scope="text, record">
          <span
            v-if="
              (record.balance == null || record.balance == '') &&
                !record.childVendorQuestionBankVOS &&
                canScore
            "
          >
            <a-input
              v-model="record.score"
              @change="e => scoreChange(e.target.value, record)"
              style="width: 100px"
            />
          </span>
          <span v-else>
            {{ text }}
          </span>
        </template>
        <template slot="action" slot-scope="text, record">
          <span
            v-if="(record.balance == null || record.balance == '') && !form.id"
          >
            <a-icon type="delete" @click="handlerDelete(record)"></a-icon>
          </span>
        </template>
      </a-table>
    </a-card>
  </div>
</template>
<script>
import {
  vendorGetRequest,
  vendorTemplateQbankWithChild,
  submitVendorKpi,
  updateVendorKpi,
  destroyVendorKpi,
  vendorKpiAgree,
  vendorKpiShowGetRequest,
} from "@/services/supplier/index"
import { getSysUserServicePageByCondition } from "@/services/system/user"
import { mapGetters } from "vuex"
const columns = [
  {
    title: "指标",
    width: 200,
    dataIndex: "type",
  },
  {
    title: "子指标",
    dataIndex: "name",
    width: 150,
    scopedSlots: { customRender: "name" },
  },

  {
    title: "权重",
    dataIndex: "balance",
    scopedSlots: { customRender: "balance" },
  },
  {
    title: "评分标准",
    dataIndex: "content",
    scopedSlots: { customRender: "content" },
  },
  // {
  //   title: "强制",
  //   dataIndex: "ifForce",
  //   width: 150,
  //   scopedSlots: { customRender: "ifForce" },
  // },
  // {
  //   title: "是否需要附件",
  //   dataIndex: "ifAttachment",
  //   scopedSlots: { customRender: "ifAttachment" },
  // },
  {
    title: "最高分",
    dataIndex: "scoreScope",
    scopedSlots: { customRender: "scoreScope" },
  },
  // {
  //   title: "打分",
  //   dataIndex: "score",
  //   scopedSlots: { customRender: "score" },
  // },
  {
    title: "备注",
    dataIndex: "insDesc",
    scopedSlots: { customRender: "insDesc" },
  },
  {
    title: "操作",
    dataIndex: "action",
    scopedSlots: { customRender: "action" },
  },
]
const columns2 = [
  {
    title: "指标",
    width: 200,
    dataIndex: "type",
  },
  {
    title: "子指标",
    dataIndex: "name",
    width: 150,
    scopedSlots: { customRender: "name" },
  },

  {
    title: "权重",
    dataIndex: "balance",
    scopedSlots: { customRender: "balance" },
  },
  {
    title: "评分标准",
    dataIndex: "content",
    scopedSlots: { customRender: "content" },
  },
  // {
  //   title: "强制",
  //   dataIndex: "ifForce",
  //   width: 150,
  //   scopedSlots: { customRender: "ifForce" },
  // },
  // {
  //   title: "是否需要附件",
  //   dataIndex: "ifAttachment",
  //   scopedSlots: { customRender: "ifAttachment" },
  // },
  {
    title: "最高分",
    dataIndex: "scoreScope",
    scopedSlots: { customRender: "scoreScope" },
  },
  {
    title: "打分",
    dataIndex: "score",
    scopedSlots: { customRender: "score" },
  },
  {
    title: "备注",
    dataIndex: "insDesc",
    scopedSlots: { customRender: "insDesc" },
  },
  // {
  //   title: "操作",
  //   dataIndex: "action",
  //   scopedSlots: { customRender: "action" },
  // },
]
export default {
  name: "SceneAdd",
  components: {},
  data() {
    return {
      documentLoading: false,
      disableds: false,
      canScore: false,
      expandedRowKeys: [],
      vendorList: [],
      personList: [],
      form: {
        id: null,
        vendorId: "",
        vendorCode: "",
        vendorName: "",
        name: "",
        templateName: "",
        responsiblePerson: "",
        responsiblePersonId: "",
        oddNumber: "",
        requestTitle: "",
        requestStatusName: "",
        remark: "",
      },
      rules: {
        vendorId: [
          {
            required: true,
            message: "供应商不能为空",
            trigger: "change",
          },
        ],
        name: [
          {
            required: true,
            message: "名称不能为空",
            trigger: "blur",
          },
        ],
        templateName: [
          {
            required: true,
            message: "模板不能为空",
            trigger: "blur",
          },
        ],
        responsiblePerson: [
          {
            required: true,
            message: "负责人不能为空",
            trigger: "blur",
          },
        ],
      },
      columns: columns,
      dataSource: [],
      id: this.$store.state.account.kpiId,
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  mounted() {
    this.getVendor()
    this.getPerson()
    if (this.id) {
      this.show(this.id)
    } else {
    }
  },
  methods: {
    // 流转竞价单
    async agree() {
      this.edit() //保存修改
      this.documentLoading = true
      const data = {
        id: this.form.id,
        requestId: this.form.requestId,
        userId: this.user.id,
      }
      const result = await vendorKpiAgree(data)
      this.documentLoading = false
      this.$message.success("操作成功")
      this.show(this.id)
      return
    },
    //打分
    scoreChange(val, record) {
      if (Number(val) > Number(record.scoreScope)) {
        this.$message.warning("打分不得大于最高分")
        record.score = null
        return
      }
      record.score = val
      this.calScore()
    },
    inputChange(val, col, record) {
      if (col == "scoreScope") {
        this.calScore()
      }
    },
    //算分
    calScore() {
      this.dataSource.forEach(item => {
        if (item.childVendorQuestionBankVOS) {
          item.score =
            Number(item.balance) *
            item.childVendorQuestionBankVOS.reduce(
              (v1, v2) => v1 + Number(v2.score),
              0
            )
          item.scoreScope =
            Number(item.balance) *
            item.childVendorQuestionBankVOS.reduce(
              (v1, v2) => v1 + Number(v2.scoreScope),
              0
            )
        }
      })
    },
    show(id) {
      const cc = JSON.parse(sessionStorage.getItem("vendorKpiForm"))
      vendorKpiShowGetRequest(id).then(response => {
        this.form.id = response.data.id
        this.form.requestId = response.data.requestId
        this.form.vendorId = response.data.vendorId
        this.form.name = response.data.name
        this.form.vendorId = response.data.vendorId
        this.form.vendorCode = response.data.vendorVO.code
        this.form.vendorName = response.data.vendorVO.name
        this.form.responsiblePerson = response.data.responsiblePerson
        this.form.responsiblePersonId = response.data.responsiblePersonId
        this.form.templateName = response.data.templateName
        this.form.remark = response.data.remark
        this.form.requestStatusName = response.data.requestStatusName
        this.form.requestTitle = response.data.requestTitle
        this.form.oddNumber = response.data.oddNumber
        this.dataSource = response.data.vendorKpiDetailVOS.map(f => {
          f.childVendorQuestionBankVOS = [...f.childVendorKpiDetailVOS]
          return f
        })
        if (this.form.requestStatusName == "待评分") {
          this.canScore = true
          this.columns = [...columns2]
        }
        if (this.form.requestStatusName !== "待发布") {
          this.disableds = true
        }
        if (this.form.requestStatusName == "已归档") {
          this.columns = [...columns2]
        }
        this.calScore()
      })
    },

    // 强制改变
    changeBoxIfForce(e, data) {
      data.ifForce = e.target.checked === true ? 1 : 0
    },
    handlerDelete(row) {
      this.dataSource.forEach(item => {
        const index = item.childVendorQuestionBankVOS.findIndex(item => {
          return item.id == row.id
        })
        item.childVendorQuestionBankVOS.splice(index, 1)
      })
    },
    // 模板改变
    bankChange(value) {
      vendorTemplateQbankWithChild({
        currentPage: "-1",
        templateName: value,
      }).then(ret => {
        this.dataSource = ret.data
        this.calScore()
      })
    },
    //负责人改变
    personChange() {
      const target = this.personList.filter(
        item => item.id == this.form.responsiblePersonId
      )[0]
      if (target) {
        this.form.responsiblePerson = target.name
      }
    },
    // 查询供应商
    getVendor() {
      vendorGetRequest({
        currentPage: "1",
        deleted: 0,
      }).then(response => {
        this.vendorList = response.data.records
      })
    },
    getPerson() {
      getSysUserServicePageByCondition({
        currentPage: "-1",
        deleted: 0,
      }).then(response => {
        this.personList = response.data.records
      })
    },
    // 提交
    submit() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          const newData = [...this.dataSource]
          const vendorKpiDetailDTOS = []
          newData.forEach(item => {
            vendorKpiDetailDTOS.push({
              type: item.type,
              name: item.name,
              content: item.content,
              balance: item.balance,
              parentId: 0,
              childVendorKpiDetailDTOS: item.childVendorQuestionBankVOS.map(
                item2 => ({
                  type: item2.type,
                  name: item2.name,
                  content: item2.content,
                  scoreScope: item2.scoreScope,
                })
              ),
            })
          })
          const form2 = { ...this.form }
          delete form2.id
          submitVendorKpi({
            ...form2,
            ruleId: 3,
            processId: "1419477778664349697",
            typeId: "1424567739789418497",
            vendorKpiDetailDTOS: vendorKpiDetailDTOS,
          }).then(response => {
            if (response.code === 0) {
              this.$message.success("添加成功")
              this.back()
            } else {
              this.$message.error(response.message)
            }
          })
        }
      })
    },
    // 保存
    edit() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          const newData = [...this.dataSource]
          const vendorKpiDetailDTOS = []
          newData.forEach(item => {
            vendorKpiDetailDTOS.push({
              id: item.id,
              type: item.type,
              name: item.name,
              content: item.content,
              balance: item.balance,
              parentId: 0,
              childVendorKpiDetailDTOS: item.childVendorQuestionBankVOS.map(
                item2 => ({
                  type: item2.type,
                  name: item2.name,
                  content: item2.content,
                  scoreScope: item2.scoreScope,
                  score: item2.score,
                })
              ),
            })
          })
          updateVendorKpi({
            ...this.form,
            vendorKpiDetailDTOS: vendorKpiDetailDTOS,
          }).then(response => {
            if (response.code === 0) {
              this.$message.success("保存成功")
              // this.back()
            } else {
              this.$message.error(response.message)
            }
          })
        }
      })
    },
    back() {
      this.$router.push({
        path: "/supply/scene",
      })
    },
    destroy() {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          destroyVendorKpi(that.form.id).then(response => {
            if (response.code === 0) {
              that.$message.success("删除成功")
            } else {
              that.$message.error(response.message)
            }
            this.$router.push({
              path: "/supply/scene",
            })
          })
        },
        onCancel() {},
      })
    },
  },
}
</script>
<style lang="less" scoped>
.text-wrapper {
  white-space: pre-wrap;
}
.menu-content {
  float: left;
  width: calc(100% - 160px);
  padding: 20px;
}
.v-enter,
.v-leave-to {
  opacity: 0;
  transform: translateX(100px);
}

.v-enter-active,
.v-leave-active {
  transition: all 0.5s ease;
}
</style>
