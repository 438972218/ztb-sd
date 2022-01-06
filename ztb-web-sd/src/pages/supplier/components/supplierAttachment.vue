<template>
  <div>
    <a-table :columns="columns" :data-source="data">
      <template slot="operation" slot-scope="text, record, index">
        <a-icon type="download" @click="handleDownload(record)" />
      </template>
    </a-table>
  </div>
</template>

<script>
import { getVendorAttachment } from "@/services/supplier/index"
import { attachmentGetfile } from "@/services/source/bidding"
import { mapGetters } from "vuex"
import moment from "moment"
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "名称",
    dataIndex: "attachmentName",
    key: "attachmentName",
  },
  {
    title: "上传人",
    dataIndex: "createdUser",
    key: "createdUser",
  },
  {
    title: "上传时间",
    dataIndex: "createdTime",
    key: "createdTime",
    customRender: (text, record, index) => {
      return moment(parseInt(text)).format("YYYY-MM-DD HH:mm")
    },
  },
  {
    title: "下载",
    dataIndex: "operation",
    key: "operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
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
      getVendorAttachment({ currentPage: -1, vendorId: this.vendorId }).then(
        response => {
          this.data = response.data.records
        }
      )
    },
    async handleDownload(row) {
      const data = {
        path: row.attachmentUrl,
      }
      const result = await attachmentGetfile(data)
      if (result.code === 0) {
        let link = document.createElement("a")
        link.style.display = "none"
        link.href = result.data
        link.download = row.attachmentName + "." + row.attachmentType
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(a)
      } else {
        this.$message.error(result.message)
      }
    },
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
