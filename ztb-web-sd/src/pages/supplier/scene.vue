<template>
  <div>
    <a-card class="card-top">
      <div>
        <a-form layout="horizontal">
          <div>
            <a-row>
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="单号"
                  :labelCol="{ span: 5 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-input v-model="form.oddNumber" placeholder="请输入单号" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="供应商名称"
                  :labelCol="{ span: 5 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-input
                    v-model="form.vendorName"
                    placeholder="请输入供应商名称"
                  />
                </a-form-item>
              </a-col>
              <!-- <a-col :md="8" :sm="24">
                <a-form-item
                  label="注册日期"
                  :labelCol="{ span: 5 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-date-picker
                    style="width: 100%"
                    placeholder="请输入更新日期"
                  />
                </a-form-item>
              </a-col> -->
              <a-col :md="12" :sm="24">
                <span style="float: right; margin-top: 3px">
                  <a-button type="primary" @click="getAll">查询</a-button>
                  <a-button style="margin-left: 8px">重置</a-button>
                </span>
              </a-col>
            </a-row>
          </div>
        </a-form>
      </div>
    </a-card>
    <a-card>
      <div>
        <div class="operator">
          <a-button
            v-has="['vendor_kpi:add']"
            @click="addEsurvey('add')"
            type="primary"
          >
            创建
          </a-button>
        </div>
        <a-table
          :pagination="pagination"
          :columns="columns"
          :rowKey="record => record.id"
          :dataSource="dataSource"
          @change="tableChange"
        >
          <template slot="oddNumber" slot-scope="text, record">
            <span key="oddNumber">
              <a @click="show(record)"> {{ text }} </a>
            </span>
          </template>
          <template
            v-for="col in ['createdTime']"
            :slot="col"
            slot-scope="text"
          >
            <span :key="col">
              {{ $moment(Number(text)).format("YYYY-MM-DD HH:mm") }}
            </span>
          </template>
          <template slot="action" slot-scope="text, record">
            <span @click="show(record, 'edit')" class="operation-style">
              查看
            </span>
            <!-- <a-icon @click="destroy(record.id)" type="delete" /> -->
          </template>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import {
  vendorKpiGetRequest,
  destroyVendorKpi,
} from "@/services/supplier/index"
import { mapGetters } from "vuex"

const columns = [
  {
    title: "单号",
    dataIndex: "oddNumber",
    key: "oddNumber",
    scopedSlots: { customRender: "oddNumber" },
  },
  {
    title: "名称",
    dataIndex: "name",
    key: "name",
  },
  {
    title: "状态",
    dataIndex: "requestStatusName",
    key: "requestStatusName",
  },
  {
    title: "供应商编码",
    dataIndex: "vendorVO.code",
    key: "vendorVO.code",
  },
  {
    title: "供应商名称",
    dataIndex: "vendorVO.name",
    key: "vendorVO.name",
  },
  {
    title: "国家",
    dataIndex: "vendorVO.country",
    key: "vendorVO.country",
  },
  {
    title: "城市",
    dataIndex: "vendorVO.city",
    key: "vendorVO.city",
  },
  {
    title: "发起时间",
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
  // {
  //   title: "操作",
  //   width: 150,
  //   scopedSlots: { customRender: "action" },
  // },
]
export default {
  name: "Scene",
  data() {
    return {
      advanced: true,
      columns: columns,
      dataSource: [],
      form: {
        oddNumber: "",
        vendorName: "",
      },
      pagination: {
        current: 1,
        pageSize: 10,
        total: 10,
        showTotal: total => `共 ${total} 条数据`,
      },
    }
  },
  computed: {
    ...mapGetters("account", ["user"]),
  },
  created() {
    this.getAll()
  },
  methods: {
    show(row) {
      this.$store.state.account.kpiId = row.id
      sessionStorage.setItem("vendorKpiForm", JSON.stringify(row))
      this.$router.push({
        path: "/supply/addscene",
      })
    },
    tableChange(row) {},
    // 请求模板数据
    getAll() {
      vendorKpiGetRequest({
        userId: this.user.id,
        oddNumber: this.form.oddNumber,
        vendorName: this.form.vendorName,
        currentPage: this.pagination.current,
        pageSize: this.pagination.pageSize,
        deleted: 0,
      }).then(response => {
        this.dataSource = response.data.records
        this.pagination.total = response.data.total
      })
    },
    // 新建跳转
    addEsurvey(type) {
      this.$router.push({
        path: "/supply/addscene",
      })
      this.$store.state.account.kpiId = null
    },
    destroy(id) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          destroyVendorKpi(id).then(response => {
            if (response.code === 0) {
              that.$message.success("删除成功")
            } else {
              that.$message.error(response.message)
            }
            that.getAll()
          })
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel() {},
      })
    },
  },
}
</script>

<style lang="less" scoped>
.search {
  margin-bottom: 54px;
}
.fold {
  width: calc(100% - 216px);
  display: inline-block;
}
@media screen and (max-width: 900px) {
  .fold {
    width: 100%;
  }
}
.join-style {
  span {
    cursor: pointer;
    margin: 10px;
  }
}
.iconstyle {
  cursor: pointer;
  font-size: 16px;
  margin: 0 10px;
}
</style>
