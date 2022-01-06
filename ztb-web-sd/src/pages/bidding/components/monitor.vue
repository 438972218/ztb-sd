<template>
  <div>
    <a-row :gutter="[20, 20]" style="padding-bottom: 5px">
      <a-col
        :span="24"
        v-if="form.requestStatusName === '待发布' && !getUserInfo"
      >
        <a-space>
          <a-button type="primary" @click="handleVendorAdd">
            添加新供应商
          </a-button>
          <!-- <a-button type="primary"> 导出供应商下载记录 </a-button> -->
        </a-space>
      </a-col>
    </a-row>
    <a-row :gutter="[20, 20]">
      <a-col :span="12" class="fontClass"
        >本轮邀请供应商: {{ invitationCount }}</a-col
      >
      <a-col :span="12" class="fontClass"
        >回合中收到的回复:{{ replyCount }}</a-col
      >
      <a-col :span="24">
        <a-table :columns="columns" :data-source="data" :pagination="false">
        </a-table>
      </a-col>
    </a-row>
    <!-- 新增供应商 -->
    <a-modal
      title="新增供应商"
      :visible="visible"
      width="700px"
      @ok="handleSave"
      @cancel="visible = false"
    >
      <a-form-model
        ref="ruleForm"
        :rules="rules"
        :model="formInline"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item label="供应商" prop="vendorName">
              <a-select
                v-model="vendorId"
                style="width: 100%"
                show-search
                :filter-option="filterOption"
                placeholder="请选择供应商"
                @change="vendorChange"
              >
                <a-select-option
                  v-for="item in vendorList"
                  :key="item.vendorId"
                  :value="item.vendorId"
                >
                  {{ item.userNameVendorName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="联系人" prop="linkman">
              <a-select
                v-model="linkman"
                style="width: 100%"
                placeholder="请选择联系人"
                @change="linkmanChange"
              >
                <a-select-option
                  v-for="item in userList"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.userName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col> -->
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { getVendorwithVendor } from "@/services/source/contend";
import { bidVendorGetRequest, bidVendorPostwithDetail } from "@/services/source/bidding";
import moment from 'moment'
const columns = [
  {
    title: '供应商',
    dataIndex: 'vendorName',
    key: 'vendorName',
  },
  {
    title: '供应商已查看RFQ？',
    dataIndex: 'checkTime',
    key: 'checkTime',
    customRender: (text, record, index) => {
      return text ? moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss') + ' 已查看' : ''
    },
  },
  {
    title: '回复状态',
    dataIndex: 'vendorStatus',
    key: 'vendorStatus',
  },
  {
    title: '轮次',
    dataIndex: 'round',
    key: 'round',
  },
  // {
  //   title: '资质资料附件',
  //   dataIndex: 'qualificationAttQuantity',
  //   key: 'qualificationAttQuantity',
  // },
  // {
  //   title: '技术资料附件',
  //   dataIndex: 'technologyAttQuantity',
  //   key: 'technologyAttQuantity'
  // }
]
export default {
  name: "monitor",
  props: {
    getUserInfo: {
      type: Boolean,
      default: false
    },
    form: {
      type: Object,
      default: {}
    }
  },
  data () {
    return {
      activeKey: '1',
      columns,
      formInline: {
        linkman: '', // 联系人
        vendorName: undefined, // 供应商名称
      },
      data: [
      ],
      rules: {
        vendorName: [{ required: true, message: '请选择供应商', trigger: 'change' }],
        linkman: [{ required: true, message: '请选择联系人', trigger: 'change' }]
      },
      invitationCount: 0,
      replyCount: 0,
      vendorList: [],
      userList: [],
      visible: false,
      vendorId: '',
      linkman: ''
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    init () {
      this.getDataList()
      this.getVendorList()
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
    },
    // 查询供应商WithUser
    async getVendorList () {
      const data = {
        currentPage: -1
      }
      const result = await getVendorwithVendor(data)
      if (result.code === 0) {
        this.vendorList = result.data
      } else {
        this.$message.error(result.message)
      }
    },
    // 保存供应商
    async handleSave () {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          const result = await bidVendorPostwithDetail(this.formInline)
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
    // 新增供应商
    handleVendorAdd () {
      this.vendorId = undefined
      this.linkman = undefined
      this.formInline = {
        linkman: '', // 联系人
        vendorName: undefined, // 供应商名称
        bidSheetId: this.form.id
      }
      this.visible = true
    },
    // 供应商选择
    vendorChange (e) {
      const data = this.vendorList.find(f => f.vendorId === e)
      if (data) {
        const vendorVO = data.vendorVO
        const sysUserInfoVO = data.sysUserInfoVO
        this.formInline.vendorName = vendorVO ? vendorVO.name : ''
        this.formInline.vendorId = vendorVO ? data.id : ''
        this.formInline.vendorUserId = sysUserInfoVO ? sysUserInfoVO.id : ''
        this.formInline.linkman = sysUserInfoVO ? sysUserInfoVO.name : ''
        this.formInline.email = sysUserInfoVO ? sysUserInfoVO.mail : ''
        this.formInline.phone = sysUserInfoVO ? sysUserInfoVO.phone : ''
      }
    },
    // 获取供应商信息
    async getDataList () {
      const data = {
        currentPage: -1,
        bidSheetId: this.form.id
      }
      const result = await bidVendorGetRequest(data)
      if (result.code === 0) {
        this.replyCount = 0
        this.data = result.data.records ? result.data.records : []
        this.invitationCount = this.data.length
        this.data.forEach(item => {
          if (item.vendorStatus === '已回复') {
            this.replyCount++
          }
        })
      } else {
        this.$message.error(result.message)
      }
    },
  }
};
</script>

<style lang="less" scoped>
.fontClass {
  font-size: 22px;
  font-weight: 550;
}
</style>
