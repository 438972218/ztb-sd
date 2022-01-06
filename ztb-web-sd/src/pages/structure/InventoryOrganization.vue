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
                title: 'shortName',
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
                v-has="['inventory_orz:add']"
                type="primary"
                style="margin-right: 5px"
                @click="handleAdd"
                v-if="state === 0 && selectedKeys.length > 0"
                >新增</a-button
              >
              <a-button
                v-has="['inventory_orz:delete']"
                type="primary"
                @click="onDetele(selectedKeys[0])"
                v-if="
                  selectType !== 0 &&
                  selectedKeys.length > 0 &&
                  childrenList.length === 0
                "
                :disabled="!form.id"
                >删除</a-button
              >
            </a-col>
            <a-col :span="24">
              <companyAdd
                v-if="selectType === 0"
                ref="companyAdd"
                :isTrue="true"
                :form="companyForm"
                @queryData="init"
              ></companyAdd>
              <a-form-model
                v-if="selectType === 6"
                ref="ruleForm"
                :model="form"
                :rules="rules"
                :label-col="labelCol"
                :wrapper-col="wrapperCol"
              >
                <a-form-model-item label="名称：" prop="name">
                  <a-input
                    v-model="form.name"
                    placeholder="请输入名称"
                  ></a-input>
                </a-form-model-item>
                <a-form-model-item label="编号：" prop="code">
                  <a-input
                    v-model="form.code"
                    placeholder="请输入编号"
                  ></a-input>
                </a-form-model-item>
                <a-form-model-item label="类型：" prop="type">
                  <a-select
                    v-model="form.type"
                    placeholder="请选择类型"
                    disabled
                  >
                    <a-select-option value="工厂"> 工厂 </a-select-option>
                    <a-select-option value="库存"> 库存 </a-select-option>
                  </a-select>
                </a-form-model-item>
                <!-- <a-form-model-item label="公司：">
                  <a-input
                    disabled
                    v-model="company"
                    placeholder="请选择公司"
                    enter-button
                    @search="companySearch"
                  >
                    <a-icon
                      slot="addonAfter"
                      type="search"
                      @click="companySearch"
                    />
                  </a-input>
                </a-form-model-item> -->
                <!-- <a-form-model-item label="父节点：">
                  <a-input
                    disabled
                    v-model="parentId"
                    placeholder="请选择父节点"
                    enter-button
                    @search="parentSearch"
                  >
                    <a-icon
                      slot="addonAfter"
                      type="search"
                      @click="parentSearch"
                    />
                  </a-input>
                </a-form-model-item> -->
                <a-form-model-item label="描述：">
                  <a-input
                    v-model="form.description"
                    placeholder="请输入描述"
                  ></a-input>
                </a-form-model-item>
                <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
                  <a-button  v-has="['inventory_orz:edit','inventory_orz:add']" type="primary" @click="onSubmit"> 保存 </a-button>
                  <!-- <a-button style="margin-left: 10px" @click="resetForm">
                    重置
                  </a-button> -->
                </a-form-model-item>
              </a-form-model>
            </a-col>
          </a-card>
        </a-col>
      </a-row>
    </a-card>
    <company ref="company" @selectData="selectData"></company>
    <inventory ref="inventory" @selectData="inventoryData"></inventory>
  </div>
</template>
<script>
import { getInventoryOrzTree, getInventoryOrzPageByCondition, postInventoryOrz, putInventoryOrz, getSysCompanyById, getjudgeGroupCompany, deleteInventoryOrz } from "@/services/structure/index";
import company from './components/company.vue'
import inventory from './components/inventory.vue'
import companyAdd from './components/companyAdd.vue'
export default {
  name: "InventoryOrganization",
  components: { company, inventory, companyAdd },
  data () {
    return {
      parentId: '',
      company: '',
      labelCol: { span: 4 },
      wrapperCol: { span: 20 },
      rules: {
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        code: [{ required: true, message: '请输入编号', trigger: 'blur' }],
        type: [{ required: true, message: '请选择类型', trigger: 'change' }]
      },
      companyForm: {
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
      state: 1,
      treeData: [],
      expandedKeys: [],
      autoExpandParent: true,
      selectedKeys: [],
      form: {
        code: '', // 库存组织代码
        companyId: '', // 公司ID
        description: '', // 库存组织描述
        name: '', // 库存组织名称
        type: '工厂', // 类型
        pid: '0' // 父节点id
      },
      saveType: 'edit',
      selectType: 0,
      type: '',
      childrenList: []
    };
  },
  created () {
    this.init()
  },
  methods: {
    // 查询公司信息
    companySearch () {
      this.$refs.company.visible = true
      this.$refs.company.getDataList()
    },
    // 选中的公司信息
    selectData (row) {
      this.company = row.fullName
      this.form.companyId = row.id
    },
    // 查询父节点信息
    parentSearch () {
      this.$refs.inventory.visible = true
      this.$refs.inventory.getDataList()
    },
    inventoryData (row) {
      this.parentId = row.name
      this.form.pid = row.id
    },
    // 初始加载
    async init () {
      this.childrenList = []
      const result = await getInventoryOrzTree()
      if (result.code === 0) {
        this.treeData = result.data
        this.expandedKeys = this.treeData.map(item => item.id)
        this.selectedKeys = this.selectedKeys.length > 0 ? this.selectedKeys : (this.treeData.length > 0 ? [this.treeData[0].id] : [])
        if (this.selectedKeys.length > 0) {
          if (this.selectType === 0) {
            this.getCompanyById(this.selectedKeys[0])
          } else {
            const data = {
              id: this.selectedKeys[0],
              pageSize: 10,
              currentPage: -1
            }
            this.getDataList(data)
          }
        }
      } else {
        this.$message.error(result.message)
      }
    },
    // 获取库存组织分页数据
    async getDataList (data) {
      const result = await getInventoryOrzPageByCondition(data)
      if (result.code === 0) {
        const dataList = result.data.records
        if (dataList.length > 0) {
          this.form = {
            id: dataList[0].id,
            code: dataList[0].code, // 库存组织代码
            companyId: dataList[0].companyId, // 公司ID
            description: dataList[0].description, // 库存组织描述
            name: dataList[0].name, // 库存组织名称
            type: dataList[0].type, // 类型
            pid: dataList[0].pid // 父节点id
          }
          this.company = dataList[0].companyId
          this.parentId = dataList[0].id
        }
      }
    },
    onExpand (expandedKeys) {
      this.expandedKeys = expandedKeys;
      this.autoExpandParent = false;
    },
    onSelect (selectedKeys, info) {
      this.childrenList = info.node.dataRef.children ? info.node.dataRef.children : []
      this.selectedKeys = selectedKeys.length > 0 ? selectedKeys : this.selectedKeys;
      this.selectType = info.node.dataRef.type === '0' ? 0 : 6
      this.type = info.node.dataRef.type === '0' ? '工厂' : '库存'
      if (this.selectType === 0) {
        // 根据id查询公司信息
        this.getCompanyById(this.selectedKeys[0])
        this.parentId = '0'
        this.company = this.selectedKeys[0]
        // 查询是否可以新增
        this.getJudgeGroupCompany(this.selectedKeys[0])
      } else {
        this.state = 0
        const data = {
          id: info.node.dataRef.id,
          pageSize: 10,
          currentPage: -1
        }
        // 查询库存组织信息
        this.getDataList(data)
      }
    },
    // 是否可以新增
    async getJudgeGroupCompany (id) {
      const result = await getjudgeGroupCompany(id)
      if (result.code === 0) {
        this.state = result.data
      }
    },
    // 根据id查询公司信息
    async getCompanyById (id) {
      const result = await getSysCompanyById(id)
      if (result.code === 0) {
        const data = result.data
        this.companyForm = {
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
      } else {
        this.$message.error(result.message)
      }
    },
    // 保存
    async onSubmit () {
      let result = null
      if (!this.form.id) {
        result = await postInventoryOrz(this.form)
      } else {
        result = await putInventoryOrz(this.form)
      }
      if (result.code === 0) {
        this.$message.success('保存成功')
        this.init()
      }
    },
    // 删除
    async onDetele (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await deleteInventoryOrz(id)
          if (result.code === 0) {
            that.$message.success('删除成功')
            that.selectedKeys = []
            that.state = 1
            that.selectType = 0
            that.init()
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 取消
    resetForm () {
      this.$refs.ruleForm.resetFields()
    },
    // 新增
    handleAdd () {
      this.saveType = 'add'
      this.selectType = 6
      this.form = {
        code: '', // 库存组织代码
        companyId: this.company, // 公司ID
        description: '', // 库存组织描述
        name: '', // 库存组织名称
        type: this.type, // 类型
        pid: this.parentId // 父节点id
      }
      // this.parentId = ''
      // this.company = ''
    }
  }
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>