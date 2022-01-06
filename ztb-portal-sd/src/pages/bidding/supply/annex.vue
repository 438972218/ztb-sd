<template>
  <div>
    <a-row>
      <a-col :span="24">
        <a-table
          :pagination="paginationAnnex"
          :columns="columnsAnnex"
          :data-source="dataAnnex"
          @change="tableAnnexChange"
        >
          <template slot="operation" slot-scope="text, record">
            <a @click="handleDownload(record)">下载</a>
          </template>
        </a-table>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { attachmentGetfile, bidGetAttachment } from "@/services/source/bidding";
import moment from 'moment'
const columnsAnnex = [
  {
    title: '附件名称',
    dataIndex: 'attachmentName',
    key: 'attachmentName',
  },
  {
    title: '说明',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: '上传时间',
    dataIndex: 'uploadingDate',
    key: 'uploadingDate',
    customRender: (text, record, index) => {
      return moment(parseInt(text)).format('YYYY-MM-DD')
    },
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  }
]
export default {
  props: {
    user: {
      type: Object,
      default: {}
    }
  },
  name: "supplier",
  data () {
    return {
      columnsAnnex,
      form: {
        id: ''
      },
      paginationAnnex: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      dataAnnex: []
    }
  },
  mounted () {
    this.form = JSON.parse(sessionStorage.getItem('supplyForm'))
    this.getAnnexDataList()
  },
  methods: {
    // 查询附件列表
    async getAnnexDataList () {
      const data = {
        bidSheetId: this.form.id,
        vendorVisible: 1,
        currentPage: this.paginationAnnex.current,
        pageSize: this.paginationAnnex.pageSize
      }
      const result = await bidGetAttachment(data)
      if (result.code === 0) {
        this.dataAnnex = result.data.records
        this.paginationAnnex.total = result.data.total
      } else {
        this.$message.error(result.message)
      }
    },
    // 下载文件
    async handleDownload (row) {
      const data = {
        path: row.attachmentUrl
      }
      const result = await attachmentGetfile(data)
      if (result.code === 0) {
        let link = document.createElement("a");
        link.style.display = "none";
        link.href = result.data;
        link.download = row.attachmentName + '.' + row.attachmentType
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(a)
      } else {
        this.$message.error(result.message)
      }
    },
    // 附件分页改变
    tableAnnexChange (row) {
      this.paginationAnnex = row
      this.getAnnexDataList()
    },
  }
};
</script>

<style lang="less" scoped>
</style>
