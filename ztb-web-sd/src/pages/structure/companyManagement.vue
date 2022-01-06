<template>
  <div>
    <a-card>
      <a-row :gutter="20">
        <a-col :span="6">
          <a-card>
            <a-tree
              :expanded-keys="expandedKeys"
              :auto-expand-parent="autoExpandParent"
              :selected-keys="selectedKeys"
              :tree-data="treeData"
              :replaceFields="{
                children: 'children',
                title: 'fullName',
                key: 'id',
              }"
              @expand="onExpand"
              @select="onSelect"
            />
          </a-card>
        </a-col>
        <a-col :span="18">
          <a-card>
            <a-col :span="24" align="right" style="padding-bottom: 10px">
              <a-button
                v-has="['company:add']"
                type="primary"
                @click="handleAdd"
                style="margin-right: 5px"
                >新增</a-button
              >
              <a-button
                v-has="['company:delete']"
                type="primary"
                @click="handleDelete(selectedKeys[0])"
                :disabled="deleteTrue"
                >删除</a-button
              >
            </a-col>
            <a-col :span="24">
              <companyAdd
                ref="companyAdd"
                :form="form"
                @queryData="getDataList"
              ></companyAdd>
            </a-col>
          </a-card>
        </a-col>
      </a-row>
    </a-card>
    <company ref="company" @selectData="selectData"></company>
  </div>
</template>
<script>
import { sysCompanyDelete, getSysCompanyTree, getSysCompanyById } from "@/services/structure/index";
// import moment from 'moment'
import companyAdd from './components/companyAdd.vue'
import company from './components/company.vue'

export default {
  name: "companyManagement",
  components: { companyAdd, company },
  data () {
    return {
      treeData: [],
      selectedKeys: [],
      autoExpandParent: true,
      expandedKeys: [],
      parent: '',
      visible: false,
      deleteTrue: true,
      form: {
        fullName: '', // 公司名称
        shortName: '', // 公司简称
        website: '', // 公司网站
        code: '', // 公司编码
        principal: '', // 公司负责人
        principalPhone: '', // 公司负责人手机号
        parentId: '0', // 公司上级
        bankAccount: '', // 公司开户银行
        bankAccountNumber: '', // 账号信息
        companyType: undefined, // 公司类型
      },
      selectRow: {
        id: '',
        parentName: '',
        parentCompanyType: 0
      }
    };
  },
  created () {
    this.getDataList()
  },
  methods: {
    // 获取公司分页数据
    async getDataList () {
      const result = await getSysCompanyTree()
      if (result.code === 0) {
        this.treeData = result.data
        this.expandedKeys = this.treeData.map(item => item.id)
        this.selectedKeys = this.selectedKeys.length > 0 ? this.selectedKeys : (this.treeData.length > 0 ? [this.treeData[0].id] : [])
        if (this.selectedKeys.length > 0) this.getCompanyById(this.selectedKeys[0])
      }
    },
    // 根据id查询公司信息
    async getCompanyById (id) {
      const result = await getSysCompanyById(id)
      if (result.code === 0) {
        const data = result.data
        this.form = {
          id: data.id,
          shortName: data.shortName,
          fullName: data.fullName,
          parentId: data.parentId,
          website: data.website,
          code: data.code,
          principal: data.principal,
          principalPhone: data.principalPhone,
          bankAccount: data.bankAccount, // 公司开户银行
          bankAccountNumber: data.bankAccountNumber, // 账号信息
          companyType: data.companyType, // 公司类型
        }
        this.$refs.companyAdd.parent = result.data.parentName
        this.$refs.companyAdd.parentCompanyType = result.data.parentCompanyType
        this.selectRow = {
          id: result.data.id,
          parentName: result.data.parentName,
          parentCompanyType: result.data.parentCompanyType
        }
      } else {
        this.$message.error(result.message)
      }
    },
    onExpand (expandedKeys) {
      this.expandedKeys = expandedKeys;
      this.autoExpandParent = false;
    },
    onSelect (selectedKeys, info) {
      this.selectedKeys = selectedKeys.length > 0 ? selectedKeys : this.selectedKeys;
      this.getCompanyById(this.selectedKeys[0])
      this.deleteTrue = info.node.dataRef.children ? true : false
    },
    // 选中的公司信息
    selectData (row) {
      this.company = row.fullName
      this.form.companyId = row.id
    },
    // 新增
    handleAdd () {
      this.title = '新增公司'
      this.form = {
        fullName: '', // 公司名称
        shortName: '', // 公司简称
        website: '', // 公司网站
        code: '', // 公司编码
        principal: '', // 公司负责人
        principalPhone: '', // 公司负责人手机号
        bankAccount: '', // 公司开户银行
        bankAccountNumber: '', // 账号信息
        parentId: this.selectRow.id ? this.selectRow.id : '0',
        companyType: undefined, // 公司类型
      }
      this.visible = true
      this.$nextTick(() => {
        this.$refs.companyAdd.parent = this.selectRow.parentName ? this.selectRow.parentName : ''
        this.$refs.companyAdd.parentCompanyType = this.selectRow.parentCompanyType ? this.selectRow.parentCompanyType : 0
      })
    },
    // 删除
    async handleDelete (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await sysCompanyDelete(id)
          if (result.code === 0) {
            that.$message.success('删除成功')
            that.getDataList()
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
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