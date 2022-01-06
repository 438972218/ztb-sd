<template>
  <div>
    <a-table :columns="columns" :data-source="data">
      <template slot="operation" slot-scope="text, record, index">
        <a-icon
          v-has="['vendor:edit']"
          type="edit"
          theme="filled"
          class="iconstyle"
          @click="handleEdit(record)"
          style="padding-right: 10px"
        />
        <a-icon
          v-has="['vendor:delete']"
          type="delete"
          @click="handleDelete(record)"
          title="删除"
          class="iconstyle"
        />
      </template>
    </a-table>
  </div>
</template>

<script>
import { getVendorUserByVendorId } from "@/services/supplier/index"
import { mapGetters } from "vuex"
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "姓名",
    dataIndex: "sysUserInfoVO.name",
    key: "sysUserInfoVO.name",
  },
  {
    title: "用户名",
    dataIndex: "sysUserInfoVO.userName",
    key: "sysUserInfoVO.userName",
  },
  {
    title: "电话",
    dataIndex: "sysUserInfoVO.phone",
    key: "sysUserInfoVO.phone",
  },
  {
    title: "邮箱",
    dataIndex: "sysUserInfoVO.mail",
    key: "sysUserInfoVO.mail",
  },
  // {
  //   title: "操作",
  //   dataIndex: "operation",
  //   key: "operation",
  //   ellipsis: true,
  //   scopedSlots: { customRender: "operation" },
  // },
]

export default {
  name: "supplierUser",
  props: {
    vendorId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      columns,
      data: [],
      rules: {
        name: [
          {
            required: true,
            message: "名称不能为空",
            trigger: "blur",
          },
        ],
      },
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  mounted() {
    this.getDatasource()
  },
  methods: {
    // 查询数据
    getDatasource() {
      getVendorUserByVendorId(this.vendorId).then(response => {
        this.data = response.data
      })
    },
    handleEdit(row) {},
    handleDelete(row) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        async onOk() {
          const result = await paidAttachmentDeleteRequest(row.id)
          if (result.code === 0) {
            that.getAnnexDataList()
            that.$message.success("删除成功")
          } else {
            that.$message.error(result.message)
          }
        },
        onCancel() {},
      })
    },
  },
}
</script>
<style lang="less" scoped>
.btn-sty {
  text-align: center;
  margin-bottom: 20px;
  button {
    margin-left: 10px;
  }
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
