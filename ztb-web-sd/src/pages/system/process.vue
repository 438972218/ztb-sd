<template>
  <div>
    <a-row :gutter="20">
      <a-col :span="8">
        <a-card>
          <div class="operator">
            <a-button
              v-has="['flow_config:add']"
              @click="addNew('add')"
              type="primary"
              >添加流程</a-button
            >
          </div>
          <a-menu v-model="selectedKeys" mode="inline" @click="handleClick">
            <a-menu-item v-for="item in data" :key="item.id">
              <a-row :gutter="20">
                <a-col :span="6">{{ item.name }}</a-col>
                <a-col :span="18" align="right">
                  <a-button
                    type="link"
                    v-has="['flow_config:edit']"
                    @click.stop="c_edit(item, 'edit')"
                    >编辑</a-button
                  >
                  <!-- <a-button type="link" @click.stop="c_delete(item.id)"
                    >删除</a-button
                  > -->
                </a-col>
              </a-row>
            </a-menu-item>
          </a-menu>
        </a-card>
      </a-col>
      <a-col :span="16">
        <a-card>
          <a-row>
            <a-col :span="12">流程信息：{{ processName }}</a-col>
            <a-col :span="12" align="right" style="padding-bottom: 10px">
              <a-button
                v-if="addShow"
                v-has="['flow_config:add']"
                @click="handleAdd"
                type="primary"
                >添加流程配置</a-button
              >
              <div v-else>
                <a-button
                  type="primary"
                  v-has="['flow_config:add']"
                  @click="onSubmit"
                  >提交</a-button
                >
                <a-button
                  style="margin-left: 10px"
                  type="primary"
                  @click="getprocessConfig"
                  >取消</a-button
                >
              </div>
            </a-col>
          </a-row>
          <a-table
            rowKey="id"
            :columns="columns"
            :pagination="false"
            :data-source="tableData"
          >
            <template
              v-for="col in columns"
              :slot="col.dataIndex"
              slot-scope="text, record"
            >
              <div :key="col.dataIndex">
                <a-input
                  v-if="
                    record.editable &&
                      (col.dataIndex === 'version' ||
                        col.dataIndex === 'timeoutAction')
                  "
                  style="margin: -5px 0"
                  :value="text"
                  @change="
                    e => handleChange(e.target.value, record.id, col.dataIndex)
                  "
                />
                <a-select
                  v-else-if="
                    record.editable && col.dataIndex === 'fromStatusName'
                  "
                  :value="record.fromStatusMask"
                  :disabled="true"
                  style="width: 100%"
                  @change="
                    (e, option) =>
                      handleChange(e, record.id, col.dataIndex, option)
                  "
                >
                  <a-select-option
                    v-for="item in statusList"
                    :key="item.id"
                    :value="item.mark"
                  >
                    {{ item.name }}
                  </a-select-option>
                </a-select>
                <a-select
                  v-else-if="
                    record.editable && col.dataIndex === 'toStatusName'
                  "
                  :value="record.toStatusMask"
                  :disabled="record.disabled"
                  style="width: 100%"
                  @change="
                    (e, option) =>
                      handleChange(e, record.id, col.dataIndex, option)
                  "
                >
                  <a-select-option
                    v-for="item in statusList"
                    :key="item.id"
                    :value="item.mark"
                  >
                    {{ item.name }}
                  </a-select-option>
                </a-select>
                <a-input
                  v-else-if="record.editable && col.dataIndex === 'description'"
                  disabled
                  :value="record.description"
                  enter-button
                >
                  <a-icon
                    slot="addonAfter"
                    type="search"
                    @click="qualifiedSearch(record)"
                  />
                </a-input>
                <!-- <a-select
                  v-else-if="
                    record.editable && col.dataIndex === 'requestNumber'
                  "
                  :value="text"
                  style="width: 100%"
                  @change="
                    (e) =>
                      handleChange(e.target.value, record.key, col.dataIndex)
                  "
                >
                  <a-select-option
                    v-for="item in nodeType"
                    :key="item.numerical"
                    :value="item.numerical"
                  >
                    {{ item.meaning }}
                  </a-select-option>
                </a-select> -->
                <a-select
                  v-else-if="record.editable && col.dataIndex === 'toRoleName'"
                  :value="record.toRoleId"
                  style="width: 100%"
                  @change="
                    (e, option) =>
                      handleChange(e, record.id, col.dataIndex, option)
                  "
                >
                  <a-select-option
                    v-for="item in roleList"
                    :key="item.id"
                    :value="item.id"
                  >
                    {{ item.name }}
                  </a-select-option>
                </a-select>
                <a-select
                  v-else-if="record.editable && col.dataIndex === 'userToName'"
                  :value="record.userTo"
                  style="width: 100%"
                  @change="e => handleChange(e, record.id, col.dataIndex)"
                >
                  <a-select-option
                    v-for="item in userToList"
                    :key="item.id"
                    :value="item.mark"
                  >
                    {{ item.description }}
                  </a-select-option>
                </a-select>
                <template v-else>
                  {{ text }}
                </template>
              </div>
            </template>
            <template slot="operation" slot-scope="record">
              <a-icon
                v-if="record.addShow"
                type="plus"
                class="iconstyle"
                @click="add(record)"
              />
              <a-icon
                v-if="!record.editable && !addShow"
                type="edit"
                class="iconstyle"
                @click="edit(record)"
              />
              <a-icon
                v-if="record.saveShow"
                type="check"
                class="iconstyle"
                @click="handleSave(record)"
              />
              <a-icon
                v-if="record.deleteShow"
                type="close"
                title="删除"
                class="iconstyle"
                @click="handleDelete(record)"
              />
            </template>
          </a-table>
        </a-card>
      </a-col>
    </a-row>

    <a-modal
      :title="types === 'add' ? '流程添加' : '流程编辑'"
      :visible="visible"
      width="700px"
      :centered="true"
      :footer="null"
      @cancel="dataClose"
    >
      <process-add
        ref="childrenDom"
        @getAll="getRoleAll"
        @on-data-close="dataClose"
        :editData="editData"
        :types="types"
      >
      </process-add>
    </a-modal>
    <qualified ref="qualified" @selectData="selectData"></qualified>
  </div>
</template>
<script>
import ProcessAdd from "./components/processAdd.vue"
import {
  processRequest,
  processDeleteRequest,
  processConfigTables,
  postProcessConfigTables,
  getProcessStatus,
  getQualifier,
  dictionaryRequest,
  getUserTo,
  dictionaryRequestAll,
} from "@/services/process/workflow"
import { getSysRoleList } from "@/services/structure/index"
import qualified from "./components/qualified.vue"

export default {
  name: "process",
  components: { ProcessAdd, qualified },
  data() {
    return {
      types: null,
      editData: null,
      visible: false,
      expandedKeys: ["0-0-0", "0-0-1"],
      autoExpandParent: true,
      checkedKeys: ["0-0-0"],
      selectedKeys: [],
      tableData: [],
      data: [],
      processName: "",
      columns: [
        {
          title: "版本号",
          width: 100,
          dataIndex: "version",
          scopedSlots: { customRender: "version" },
        },
        // {
        //   title: "节点类型",
        //   dataIndex: "requestNumber",
        // },
        {
          title: "上一节点",
          dataIndex: "fromStatusName",
          scopedSlots: { customRender: "fromStatusName" },
        },
        {
          title: "下一节点",
          dataIndex: "toStatusName",
          scopedSlots: { customRender: "toStatusName" },
        },
        // {
        //   title: "流程信息",
        //   align: "center",
        //   dataIndex: "processName",
        // },
        {
          title: "限定词",
          dataIndex: "description",
          scopedSlots: { customRender: "description" },
        },
        {
          title: "去向角色",
          dataIndex: "toRoleName",
          scopedSlots: { customRender: "toRoleName" },
        },
        {
          title: "去向用户类型",
          dataIndex: "userToName",
          scopedSlots: { customRender: "userToName" },
        },
      ],
      addShow: true,
      statusList: [],
      qualifierList: [],
      nodeType: [],
      userToList: [],
      roleList: [],
      tableId: "",
    }
  },

  created() {
    this.getProcessStatusData()
    this.getQualifierData()
    this.getDictionaryRequest()
    this.getUserToData()
    // this.getUserData()
    this.getDictionaryRequestAll()
    this.getRoleList()
    this.getRoleAll()
  },
  methods: {
    // 配置规则信息
    selectData(value, name) {
      const index = this.tableData.findIndex(f => f.id === this.tableId)
      // this.tableData[index].qualifiedId = name
      this.tableData[index].expression = value
      this.tableData[index].description = name
    },
    qualifiedSearch(row) {
      this.tableId = row.id
      this.$refs.qualified.visible = true
      this.$refs.qualified.getDataList()
      if (!row.expression) {
        this.$refs.qualified.formInline = {
          field: undefined,
          condition: undefined,
          value: undefined,
          relationship: undefined,
        }
        this.$refs.qualified.contentList = []
      }
    },
    //角色列表
    getRoleAll() {
      processRequest({
        currentPage: 1,
      }).then(response => {
        this.data = response.data.records
        this.selectedKeys = this.data.length > 0 ? [this.data[0].id] : []
        this.processName = this.data.length > 0 ? this.data[0].name : ""
        if (this.selectedKeys.length > 0) {
          this.getprocessConfig(this.selectedKeys[0])
        }
      })
    },
    // 获取角色下拉数据
    async getRoleList() {
      const result = await getSysRoleList()
      if (result.code === 0) {
        this.roleList = result.data
      }
    },
    // 获取招投标字典数据
    async getDictionaryRequestAll() {
      const data = {
        currentPage: -1,
      }
      const result = await dictionaryRequestAll(data)
      if (result.code === 0) {
      }
    },
    // 获取流程状态数据
    async getProcessStatusData() {
      const data = {
        currentPage: -1,
      }
      const result = await getProcessStatus(data)
      if (result.code === 0) {
        this.statusList = result.data.records
      }
    },
    // 获取流程规则
    async getQualifierData() {
      const data = {
        currentPage: -1,
      }
      const result = await getQualifier(data)
      if (result.code === 0) {
        this.qualifierList = result.data.records
      }
    },
    // 获取字典数据
    async getDictionaryRequest() {
      const result = await dictionaryRequest()
      if (result.code === 0) {
        this.nodeType = result.data.dataDictionaries.nodeType
      }
    },
    // 获取用户去向信息
    async getUserToData() {
      const result = await getUserTo()
      if (result.code === 0) {
        this.userToList = result.data
      }
    },
    // 提交
    async onSubmit() {
      if (!this.tableData.find(f => f.toStatusMask === "2")) {
        this.$message.warning("下一节点必须存在已归档")
        return
      }
      let dataList = []
      this.tableData.forEach(item => {
        const data = {
          fromStatusMask: item.fromStatusMask, // 上一个状态标识
          processId: this.selectedKeys[0], // 流程ID
          expression: {
            description: item.description,
            expression: item.expression,
          }, // 限定词ID
          timeoutAction: item.timeoutAction, // 超时自动流转
          toRoleId: item.toRoleId, // 去向角色ID
          toStatusMask: item.toStatusMask, // 下一个状态标识
          userTo: item.userTo ? Number(item.userTo) : null, // 去向用户ID
          userToExpression: item.userToExpression,
          type: item.requestNumber, // 节点类型 （详见数据字典）
        }
        dataList.push(data)
      })
      const result = await postProcessConfigTables(dataList)
      if (result.code === 0) {
        this.$message.success("提交成功")
        this.getprocessConfig()
      } else {
        this.$message.error(result.message)
      }
    },
    // 编辑
    edit(row) {
      row.editable = true
      row.saveShow = true
    },
    // 添加数据
    add(row) {
      const data = this.tableData.find(f => f.editable)
      if (data) {
        this.$message.warning("请先保存数据")
        return
      }
      const d = this.tableData.filter(
        f => f.fromStatusMask === row.toStatusMask
      )
      this.tableData.push({
        id: this.tableData.length + 1,
        fromStatusName: row.toStatusName, // 上一个流程状态
        expression: "", // 流程规则
        toStatusName: d.length > 0 ? d[0].toStatusName : "", // 下一个流程状态
        version: "", // 版本号
        requestNumber: "", // 节点类型
        timeoutAction: "", // 超时时间
        toRoleId: "", // 去向角色id
        toRoleName: "", // 去向角色Name
        editable: true,
        fromStatusMask: row.toStatusMask, // 上一个状态标识
        processId: "", // 流程ID
        description: "", // 限定词ID
        timeoutAction: "", // 超时自动流转
        toRoleId: "", // 去向角色ID
        toStatusMask: d.length > 0 ? d[0].toStatusMask : "", // 下一个状态标识
        type: "", // 节点类型 （详见数据字典）
        userTo: null, // 去向用户类型
        userToName: "", // 去向用户类型名称
        addShow: true,
        saveShow: true,
        deleteShow: true,
        disabled: d.length > 0,
      })
    },
    // 保存数据
    handleSave(row) {
      if (!row.toStatusMask) {
        this.$message.warning("下一节点必须得有值")
        return
      }
      if (!row.toRoleId && !row.userTo) {
        this.$message.warning("去向角色以及去向用户类型不可都为空")
        return
      }
      if (row.id === 1) {
        row.deleteShow = false
      }
      const data = this.tableData.filter(
        f =>
          f.fromStatusMask === row.fromStatusMask &&
          f.toStatusMask === row.toStatusMask
      )
      if (this.tableData.length === 1) {
        // 开始节点
        row.requestNumber = 0
      } else if (row.expression) {
        // 条件判断节点
        row.requestNumber = 3
      } else if (data.length >= 2) {
        // 会签节点
        this.tableData
          .filter(
            f =>
              f.fromStatusMask === row.fromStatusMask &&
              f.toStatusMask === row.toStatusMask
          )
          .forEach(item => {
            item.requestNumber = 2
          })
      } else if (row.toStatusMask === "2") {
        // 结束节点
        row.requestNumber = -1
        this.tableData.forEach(item => {
          item.addShow = false
          if (item.id !== row.id) item.deleteShow = false
        })
      } else {
        // 一般节点
        row.requestNumber = 1
      }
      row.editable = false
      row.saveShow = false
    },
    // 删除数据
    handleDelete(row) {
      const index = this.tableData.findIndex(f => f.id === row.id)
      this.tableData.splice(index, 1)
      if (this.tableData.length === 0) {
        this.addShow = true
        this.getprocessConfig()
      }
      if (row.toStatusMask === "2") {
        this.tableData.forEach(item => {
          item.addShow = true
          item.deleteShow = item.id !== row.id
        })
      }
    },
    // 查询流程配置信息
    async getprocessConfig() {
      this.addShow = true
      this.tableData = []
      const data = {
        processId: this.selectedKeys[0],
      }
      const result = await processConfigTables(data)
      if (result.code === 0) {
        result.data.forEach(item => {
          const roleName = this.roleList.find(f => f.id === item.toRoleId)
            ? this.roleList.find(f => f.id === item.toRoleId).name
            : ""
          const userTo = item.userTo ? Number(item.userTo) : ""
          const userToName = this.userToList.find(f => f.mark === userTo)
            ? this.userToList.find(f => f.mark === userTo).description
            : ""
          this.tableData.push({
            id: item.id,
            fromStatusName: item.fromStatus ? item.fromStatus.name : "", // 上一个流程状态
            processName: item.process ? item.process.name : "", // 流程信息
            description: item.qualifier ? item.qualifier.description : "", // 流程规则
            toStatusName: item.toStatus ? item.toStatus.name : "", // 下一个流程状态
            version: item.version, // 版本号
            requestNumber: item.requestNumber, // 节点类型
            timeoutAction: item.timeoutAction, // 超时时间
            userTo: item.userTo,
            toUserId: item.toUserId,
            toRoleName: roleName,
            userToName: userToName,
          })
        })
      } else {
        this.$message.error(result.message)
      }
    },
    // 表格点击事件
    handleClick(e) {
      this.selectedKeys[0] = e.key
      this.addShow = true
      const data = this.data.find(f => f.id === this.selectedKeys[0])
      if (data) {
        this.processName = data.name
        this.getprocessConfig()
      }
    },
    handleChange(value, key, column, option) {
      if (
        this.tableData.length === 1 &&
        column === "toStatusName" &&
        value === "0"
      ) {
        this.$message.warning("上一节点为待提交，下一节点不可为待提交")
        return
      }
      const newData = [...this.tableData]
      const target = newData.filter(item => key === item.id)[0]
      if (target) {
        if (column === "fromStatusName") {
          target.fromStatusMask = value
          const data = this.statusList.find(f => f.mark === value)
          target[column] = data.name
        } else if (column === "toStatusName") {
          target.toStatusMask = value
          const data = this.statusList.find(f => f.mark === value)
          target[column] = data.name
        } else if (column === "toRoleName") {
          target.toRoleId = value
          const data = this.roleList.find(f => f.id === value)
          target[column] = data.name
        } else if (column === "userToName") {
          target.userTo = value
          const data = this.userToList.find(f => f.mark === value)
          target.userToExpression = data.id
          target[column] = data.description
        } else {
          target[column] = value
        }
        this.tableData = newData
      }
    },
    // 添加流程配置
    handleAdd() {
      this.addShow = false
      if (!this.columns.find(f => f.title === "操作")) {
        this.columns.push({
          title: "操作",
          width: 150,
          scopedSlots: { customRender: "operation" },
        })
      }
      this.tableData = [
        {
          id: 1,
          fromStatusName: "待提交", // 上一个流程状态
          expression: "", // 流程规则
          toStatusName: "", // 下一个流程状态
          version: "", // 版本号
          requestNumber: "", // 节点类型
          timeoutAction: "", // 超时时间
          toRoleId: "", // 去向角色id
          toRoleName: "", // 去向角色Name
          editable: true,
          fromStatusMask: "0", // 上一个状态标识
          processId: "", // 流程ID
          description: "", // 限定词ID
          timeoutAction: "", // 超时自动流转
          toRoleId: "", // 去向角色ID
          toStatusMask: "", // 下一个状态标识
          type: "", // 节点类型 （详见数据字典）
          userTo: null, // 去向用户类型
          userToExpression: "",
          userToName: "", // 去向用户类型名称
          addShow: true,
          saveShow: true,
          deleteShow: true,
          disabled: false,
        },
      ]
    },
    dataClose() {
      this.$refs.childrenDom.form = {
        name: "",
        permissionIds: [],
        description: "",
        typeId: null,
      }
      this.visible = false
      this.editData = null
    },
    addNew(type) {
      this.types = type
      this.visible = true
    },
    c_jump(record) {
      this.$router.push({
        path: "/details/flow",
        query: {
          id: record.id,
        },
      })
    },
    c_edit(editData, type) {
      this.editData = editData
      this.addNew(type)
    },
    c_delete(id) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除用户吗？",
        centered: true,
        onOk() {
          processDeleteRequest(id).then(response => {
            if (response.code === 0) {
              that.$message.success("删除成功")
            } else {
              that.$message.error(response.message)
            }
            that.getRoleAll()
          })
        },
        onCancel() {},
      })
    },
    handleOk() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          console.log(valid)
        } else {
          console.log("error submit!!")
          return false
        }
      })
    },
  },
}
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
</style>
