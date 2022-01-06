<template>
  <div>
    <a-row style="padding-bottom: 5px">
      <a-col :span="24">
        <a-button type="primary" @click="handleAdd"> 创建讨论 </a-button>
      </a-col>
    </a-row>
    <div v-if="data && data.length > 0">
      <a-collapse v-model="activeKey">
        <a-collapse-panel
          v-for="row in data"
          :key="row.id"
          :header="row.subject"
        >
          <a-list
            class="demo-loadmore-list"
            :loading="loading"
            item-layout="horizontal"
            :data-source="
              recordList.filter((f) => f.discussGroup.id === row.id)
            "
          >
            <div
              slot="loadMore"
              :style="{
                textAlign: 'center',
                marginTop: '12px',
                height: '32px',
                lineHeight: '32px',
              }"
            >
              <a-spin v-if="loadingMore" />
              <div v-else>
                <a-space>
                  <a-button type="primary" @click="handleReply(row.id)">
                    发表评论
                  </a-button>
                  <!-- <a-button @click="onLoadMore"> 显示更多 </a-button> -->
                </a-space>
              </div>
            </div>
            <a-list-item slot="renderItem" slot-scope="item">
              <a slot="actions" @click="handleReply(row.id, item.from)">回复</a>
              <a-list-item-meta :description="item.content">
                <span slot="title">{{
                  item.from.name + ' 发送至 ' + getTos(item.tos)
                }}</span>
              </a-list-item-meta>
            </a-list-item>
          </a-list>
        </a-collapse-panel>
      </a-collapse>
    </div>
    <div v-else>
      <a-empty :image="simpleImage" />
    </div>
    <!-- 创建讨论 -->
    <a-modal
      title="创建讨论"
      :visible="visible"
      width="700px"
      @ok="handleSave"
      @cancel="visible = false"
    >
      <a-form-model
        ref="ruleForm"
        :model="form"
        :rules="rules"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="主题" prop="subject">
              <a-input v-model="form.subject" placeholder="请输入主题" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="讨论人">
              <a-input v-model="toUsers" disabled placeholder="请输入讨论人" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="内容">
              <a-textarea
                v-model="form.content"
                placeholder="请输入内容"
                :auto-size="{ minRows: 3, maxRows: 5 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <!-- 回复 -->
    <a-modal
      title="回复"
      :visible="replyVisible"
      width="700px"
      @ok="saveReply"
      @cancel="replyVisible = false"
    >
      <a-form-model
        ref="ruleForm"
        :model="replyForm"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="回复人">
              <a-select
                mode="multiple"
                disabled
                allowClear
                v-model="replyForm.names"
                placeholder="请选择用户"
              >
                <a-select-option
                  v-for="item in userList"
                  :key="item.id"
                  :value="item.name"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="内容">
              <a-textarea
                v-model="replyForm.content"
                placeholder="请输入内容"
                :auto-size="{ minRows: 3, maxRows: 5 }"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { Empty } from 'ant-design-vue';
import { postDiscussInitiate, getDiscussGroup, getDiscussRecordRequest, putDiscussReply } from "@/services/source/contend";
import { paidVendorGetRequest } from "@/services/source/contend";
export default {
  name: "talkover",
  computed: {
    getTos () {
      return function (val) {
        if (val) {
          return val.map(item => { return item.name })
        } else {
          return '00:00:00'
        }
      }
    },
  },
  data () {
    return {
      activeKey: '',
      loadingMore: false,
      loading: false,
      data: [],
      fileList: [],
      form: {
        content: '', // 内容
        requestId: '', // 表单ID
        subject: '', // 主题
        toUsers: [], // 收信人
      },
      rules: {
        subject: [{ required: true, message: '请输入主题', trigger: 'blur' }],
        toUsers: [{ required: true, message: '请选择讨论人', trigger: 'change' }],
      },
      userList: [],
      visible: false,
      pagination: {
        current: 1,
        pageSize: 5
      },
      toUsers: '',
      simpleImage: '',
      recordList: [],
      replyVisible: false,
      replyForm: {
        content: '',
        groupId: '',
        toUsers: []
      },
      project: null,
    }
  },
  beforeCreate () {
    this.simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;
  },
  mounted () {
    this.project = JSON.parse(sessionStorage.getItem('supplyForm'))
    this.toUsers = this.project.createdUser
    this.getDataList()
    this.getVendorMember()
  },
  methods: {
    onLoadMore () {
      this.loadingMore = true
    },
    // 根据表单ID查询讨论记录
    async getDiscussRecord () {
      const result = await getDiscussRecordRequest(this.project.requestId)
      if (result.code === 0) {
        this.recordList = result.data ? result.data : []
      } else {
        this.$message.error(result.message)
      }
    },
    // 回复讨论
    async saveReply () {
      if (this.replyForm.toUsers && this.replyForm.toUsers.length === 0) {
        this.userList.forEach(item => {
          this.replyForm.toUsers.push(item.name)
        })
      }
      const result = await putDiscussReply(this.replyForm)
      if (result.code === 0) {
        this.$message.success('保存成功')
        this.replyVisible = false
        this.getDiscussRecord()
      } else {
        this.$message.error(result.message)
      }
    },
    // 回复
    handleReply (id, row) {
      this.replyForm = {
        content: '',
        groupId: id,
        names: row ? [row.name] : [this.project.createdUser],
        toUsers: row ? [row.account] : [this.project.createdUser]
      }
      this.replyVisible = true
    },
    // 查询讨论组
    async getDataList () {
      const data = {
        requestId: this.project.requestId,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      const result = await getDiscussGroup(data)
      if (result.code === 0) {
        this.data = result.data.records ? result.data.records : []
        this.activeKey = this.data.length > 0 ? this.data[0].id : ''
        this.getDiscussRecord()
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询供应商
    async getVendorMember () {
      const data = {
        paidSheetId: this.project.id,
        currentPage: -1
      }
      const result = await paidVendorGetRequest(data)
      if (result.code === 0) {
        this.userList = result.data.records
        console.log(result.data.records)
      } else {
        this.$message.error(result.message)
      }
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getDataList()
    },
    // 编辑附件
    handleEdit (row) {
      this.form = Object.assign({}, row)
      this.visible = true
    },
    // 创建讨论
    handleAdd () {
      this.form = {
        content: '', // 内容
        requestId: this.project.requestId, // 表单ID
        subject: '', // 主题
        toUsers: [this.project.createdUser] // 收信人
      }
      this.visible = true
    },
    // 保存数据
    async handleSave () {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          const result = await postDiscussInitiate(this.form)
          if (result.code === 0) {
            this.$message.success('保存成功')
            this.visible = false
            this.getDataList()
          } else {
            this.$message.error(result.message)
          }
        }
      })
    },
  }
};
</script>

<style lang="less" scoped>
</style>
