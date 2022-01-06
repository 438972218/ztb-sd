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
               v-has="['purchase_orz:add']"
                type="primary"
                @click="handleAdd"
                style="margin-right: 5px"
                >新增</a-button
              >
              <a-button
                v-has="['purchase_orz:delete']"
                type="primary"
                @click="onDetele(selectedKeys[0])"
                v-if="selectType !== 0 && childrenList.length === 0"
                :disabled="!form.id"
                >删除</a-button
              >
            </a-col>
            <a-col :span="24">
              <companyAdd
                :isTrue="true"
                v-if="selectType === 0"
                ref="companyAdd"
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
                <a-form-model-item label="描述：">
                  <a-input
                    v-model="form.description"
                    placeholder="请输入描述"
                  ></a-input>
                </a-form-model-item>
                <a-form-model-item label="组织关系：" v-if="form.id">
                  <a-tree-select
                    :tree-data="inventoryTreeData"
                    style="width: 100%"
                    v-model="inventorySelectedKeys"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    :replaceFields="{
                      children: 'children',
                      title: 'shortName',
                      key: 'id',
                      value: 'id',
                    }"
                    placeholder="请选择"
                    allow-clear
                    multiple
                    tree-default-expand-all
                    @change="onInventoryChange"
                    @select="onInventorySelect"
                  >
                  </a-tree-select>
                </a-form-model-item>
                <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
                  <a-button v-has="['purchase_orz:edit']" type="primary" @click="onSubmit"> 保存 </a-button>
                </a-form-model-item>
              </a-form-model>
            </a-col>
          </a-card>
        </a-col>
      </a-row>
    </a-card>
    <company ref="company" @selectData="selectData"></company>
    <purchasing ref="purchasing" @selectData="inventoryData"></purchasing>
  </div>
</template>
<script>
import { getPurchaseOrzTree, getPurchaseOrzPageByCondition, postPurchaseOrz, putPurchaseOrz, getSysCompanyById, getInventoryOrzFactoryTree, getInventoryOrzFactoryTreeBySelect, getpurchaseOrzMaintenance, deletePurchaseOrz, putpurchaseOrzInventoryOrz } from "@/services/structure/index";
import company from './components/company.vue'
import purchasing from './components/purchasing.vue'
import companyAdd from './components/companyAdd.vue'
export default {
  name: "purchasingOrganization",
  components: { company, purchasing, companyAdd },
  data () {
    return {
      parentId: '',
      company: '',
      labelCol: { span: 4 },
      wrapperCol: { span: 20 },
      rules: {
        name: [{ required: true, message: '请输入组织名称', trigger: 'blur' }],
        code: [{ required: true, message: '请输入组织编号', trigger: 'blur' }]
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
      inventoryTreeData: [],
      inventoryExpandKeys: [],
      expandedKeys: [],
      inventoryAutoExpandParent: true,
      autoExpandParent: true,
      inventorySelectedKeys: [],
      selectedKeys: [],
      form: {
        code: '', // 库存组织代码
        companyId: '', // 公司ID
        description: '', // 库存组织描述
        name: '', // 库存组织名称
        pid: '0' // 父节点id
      },
      saveType: 'edit',
      selectType: 0,
      relation: '',
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
      this.$refs.purchasing.visible = true
      this.$refs.purchasing.getDataList()
    },
    inventoryData (row) {
      this.parentId = row.name
      this.form.pid = row.id
    },
    // 初始加载
    async init () {
      this.childrenList = []
      const result = await getPurchaseOrzTree()
      if (result.code === 0) {
        this.treeData = result.data
        this.expandedKeys = this.treeData.map(item => item.id)
        this.selectedKeys = this.selectedKeys.length > 0 ? this.selectedKeys : (this.treeData.length > 0 ? [this.treeData[0].id] : [])
        this.company = this.selectedKeys[0]
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
      } else {
        this.$message.error(result.message)
      }
    },
    // 删除
    async onDetele (id) {
      const that = this
      this.$confirm({
        title: '提示',
        content: '是否确认删除该数据',
        async onOk () {
          const result = await deletePurchaseOrz(id)
          if (result.code === 0) {
            that.$message.success('删除成功')
            that.selectedKeys = []
            that.selectType = 0
            that.init()
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel () { },
      });
    },
    // 获取库存组织树型结构
    async getInventoryTree (data) {
      let result = null
      if (data.level === '1') {
        // 第一层采购组织查询所有工厂树型
        result = await getInventoryOrzFactoryTree()
      } else {
        // 第二层采购组织查询父采购组织工厂树型数据
        result = await getInventoryOrzFactoryTreeBySelect(data.id)
      }
      if (result.code === 0) {
        this.inventoryTreeData = result.data
      } else {
        this.$message.error(result.message)
      }
    },
    // 查询是否有组织关系
    async getpurchaseOrz (id) {
      const result = await getpurchaseOrzMaintenance(id)
      if (result.code === 0) {
        this.inventorySelectedKeys = result.data ? result.data.inventoryOrzIds : []
      }
    },
    // 获取库存组织分页数据
    async getDataList (data) {
      const result = await getPurchaseOrzPageByCondition(data)
      if (result.code === 0) {
        const dataList = result.data.records
        if (dataList.length > 0) {
          this.form = {
            id: dataList[0].id,
            code: dataList[0].code, // 库存组织代码
            companyId: dataList[0].companyId, // 公司ID
            description: dataList[0].description, // 库存组织描述
            name: dataList[0].name, // 库存组织名称
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
    onInventorySelect (value) {
      this.inventorySelectedKeys = value
    },
    onInventoryChange (value) {
      console.log(value)
    },
    onSelect (selectedKeys, info) {
      this.childrenList = info.node.dataRef.children ? info.node.dataRef.children : []
      this.inventorySelectedKeys = []
      this.selectedKeys = selectedKeys.length > 0 ? selectedKeys : this.selectedKeys;
      this.selectType = info.node.dataRef.type === '0' ? 0 : 6
      if (this.selectType === 0) {
        // 根据id查询公司信息
        this.getCompanyById(this.selectedKeys[0])
        this.parentId = '0'
        this.company = this.selectedKeys[0]
      } else {
        const data = {
          id: info.node.dataRef.id,
          pageSize: 10,
          currentPage: -1
        }
        // 查询库存组织
        this.getInventoryTree(info.node.dataRef)
        // 查询是否有组织关系
        this.getpurchaseOrz(info.node.dataRef.id)
        // 查询库采购织信息
        this.getDataList(data)
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
        result = await postPurchaseOrz(this.form)
      } else {
        result = await putPurchaseOrz(this.form)
      }
      if (result.code === 0) {
        if (this.form.id) {
          let data = {
            purchaseOrzId: this.form.id,
            inventoryOrzIds: this.inventorySelectedKeys
          }
          let res = await putpurchaseOrzInventoryOrz(data)
          if (res.code === 0) { }
        }
        this.$message.success('保存成功')
        this.init()
      }
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
        pid: this.parentId // 父节点id
      }
      this.inventorySelectedKeys = []
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