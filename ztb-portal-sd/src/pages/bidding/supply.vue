<template>
  <div>
    <a-card class="card-top">
      <div>
        <a-form-model
          :model="formInline"
          :labelCol="{ span: 5 }"
          :wrapperCol="{ span: 18, offset: 0 }"
        >
          <a-row>
            <a-col :span="6">
              <a-form-model-item label="招标编号">
                <a-input
                  v-model="formInline.oddNumber"
                  placeholder="请输入招标编号"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="6">
              <a-form-model-item label="关键字">
                <a-input
                  v-model="formInline.keyword"
                  placeholder="请输入关键字"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="12" align="right">
              <a-button type="primary" @click="getAll">查询</a-button>
            </a-col>
          </a-row>
        </a-form-model>
      </div>
    </a-card>
    <a-card>
      <div>
        <div class="operator"></div>
        <a-table
          :pagination="pagination"
          :columns="columns"
          :data-source="dataSource"
          @change="tableChange"
        >
          <template slot="oddNumber" slot-scope="text, record">
            <a @click="handleAdd(record)">{{ text }}</a>
          </template>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import AdvanceTable from "@/components/table/advance/AdvanceTable";
import {
  bidSheetVendorGetRequest,
  bidVendorStatusPutRequest,
  bidVendorPutCheckTime
} from "@/services/source/bidding";
import { mapGetters } from "vuex";
import moment from 'moment'

export default {
  name: "Supply",
  components: { AdvanceTable },
  data () {
    return {
      formInline: {
        keyword: "",
        oddNumber: "",
      },
      loading: false,
      page: 1,
      pageSize: 10,
      total: 0,
      advanced: true,
      columns: [
        {
          title: "招标编号",
          dataIndex: "oddNumber",
          scopedSlots: { customRender: "oddNumber" },
        },
        // {
        //   title: "招标单标题",
        //   dataIndex: "requestTitle",
        //   width: 200,
        // },
        {
          title: "招标名称",
          dataIndex: "title",
        },
        // {
        //   title: "资格预审",
        //   dataIndex: "preQualification",
        // },
        // {
        //   title: "轮次",
        //   dataIndex: "round",
        // },
        // {
        //   title: "版本",
        //   dataIndex: "versions",
        // },
        // {
        //   title: "招标类型",
        //   dataIndex: "sheetType",
        // },
        {
          title: "招标方式",
          dataIndex: "tenderMode",
        },
        // {
        //   title: "发布时间",
        //   dataIndex: "startTime",
        //   customRender: (text, row, index) => {
        //     return text ? moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss') : ''
        //   }
        // },
        // {
        //   title: "采购组织",
        //   dataIndex: "purchasingOrganization",
        // },
        {
          title: "投标截止时间",
          dataIndex: "bidEndTime",
          customRender: (text, row, index) => {
            return row.mark ? (row.clarifyEndTime ? moment(parseInt(row.clarifyEndTime)).format('YYYY-MM-DD HH:mm:ss') : '-') : moment(parseInt(text)).format('YYYY-MM-DD HH:mm:ss')
          }
        },
        {
          title: "状态",
          dataIndex: "vendorStatus",
          scopedSlots: { customRender: "vendorStatus" },
        },
      ],
      dataSource: [],
      selectedRows: [],
      form: {
        name: "",
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
      },
    };
  },

  created () {
    this.getAll();
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  methods: {
    getAll () {
      bidSheetVendorGetRequest({
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
        vendorUserId: this.user.id,
        keyword: this.formInline.keyword,
        oddNumber: this.formInline.oddNumber,
      }).then((response) => {
        if (response.code === 0) {
          this.dataSource = response.data.records;
          this.pagination.total = response.data.total;
        } else {
          this.$message.error(response.message)
        }
        // if(conditionals(response)){
        // }
      });
    },
    async handleAdd (row) {
      console.log(row)
      const data = {
        bidSheetId: row.id,
        vendorUserId: this.user.id
      }
      const result = await bidVendorPutCheckTime(data)
      if (result.code === 0) {
        const form = Object.assign({}, row)
        form.startTime = form.startTime ? moment(parseInt(form.startTime)).format('YYYY-MM-DD HH:mm:ss') : ''
        form.bidEndTime = form.bidEndTime ? moment(parseInt(form.bidEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
        form.clarifyEndTime = form.clarifyEndTime ? moment(parseInt(form.clarifyEndTime)).format('YYYY-MM-DD HH:mm:ss') : ''
        sessionStorage.setItem('supplyForm', JSON.stringify(form))
        this.$router.push({ path: 'addsupply' })
      } else {
        this.$message.error(result.message)
      }
    },
    // 分页改变
    tableChange (row) {
      this.pagination = row
      this.getAll()
    },
  },
};
</script>

<style lang="less" scoped>
.search {
  margin-bottom: 54px;
}
.fold {
  width: calc(100% - 216px);
  display: inline-block;
}
.operator {
  margin: 5px 0 18px 0;
}
@media screen and (max-width: 900px) {
  .fold {
    width: 100%;
  }
}
.operation-style {
  margin: 0 5px;
  color: #1890ff;
  cursor: pointer;
}
</style>
