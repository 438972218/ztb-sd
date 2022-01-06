<template>
  <div>
    <a-card class="card-top">
      <div>
        <a-form layout="horizontal">
          <div>
            <a-row>
              <a-col :md="8" :sm="24">
                <a-form-item
                  label="供应商名称"
                  :labelCol="{ span: 5 }"
                  :wrapperCol="{ span: 18, offset: 0 }"
                >
                  <a-input placeholder="请输入" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
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
              </a-col>
              <a-col :md="8" :sm="24">
                <span style="float: right; margin-top: 3px">
                  <a-button type="primary">查询</a-button>
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
          <a-button @click="addEsurvey('add')" type="primary"
            >创建合格评审</a-button
          >
        </div>
        <a-table
          :columns="columns"
          :rowKey="(record) => record.id"
          :dataSource="dataSource"
        >
          <template
            v-for="cols in [
              'vendorName',
              'registeredCapital',
              'type',
              'industry',
              'suplyCategoryRemark',
              'contactName',
              'contactMobile',
              'createdTime',
            ]"
            :slot="cols"
            slot-scope="text, record"
          >
            <div :key="cols" >
              <template >
                {{ record.vendorVO[cols] }}
              </template>
            </div>
          </template>
          <template slot="action" slot-scope="text, record">
            <span @click="editFind(record, 'edit')" class="operation-style">
              编辑
            </span>
            <span @click="c_delete(record.id)" class="operation-style">
              删除
            </span>
          </template>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import {
  vendorQualifyReviewGetRequest,
  vendorQualifyReviewDeleteRequest,
  
} from "@/services/supplier/index";
const columns = [
  {
    title: "单号",
    dataIndex: "name",
    width: 150,
  },
  {
    title: "企业名称",
    dataIndex: "name",
    scopedSlots: { customRender: "name" },
  },
  {
    title: "注册资本(万元)",
    dataIndex: "registeredCapital",
    scopedSlots: { customRender: "registeredCapital" },
  },
  {
    title: "供应商类型",
    dataIndex: "type",
    scopedSlots: { customRender: "type" },
  },
  {
    title: "所属行业",
    dataIndex: "industry",
    scopedSlots: { customRender: "industry" },
  },
  {
    title: "主营品类",
    dataIndex: "suplyCategoryRemark",
    scopedSlots: { customRender: "suplyCategoryRemark" },
  },
  {
    title: "姓名",
    dataIndex: "contactName",
    scopedSlots: { customRender: "contactName" },
  },
  {
    title: "手机号",
    dataIndex: "contactMobile",
    scopedSlots: { customRender: "contactMobile" },
  },
  {
    title: "注册时间",
    dataIndex: "createdTime",
    scopedSlots: { customRender: "createdTime" },
  },
  {
    title: "操作",
    width: 150,
    scopedSlots: { customRender: "action" },
  },
];
export default {
  name: "Review",
  data() {
    return {
      advanced: true,
      columns: columns,
      dataSource: [],
    };
  },
  created() {
    this.getAll();
  },
  methods: {
    // 请求模板数据
    getAll() {
      vendorQualifyReviewGetRequest({
        currentPage: -1,
        deleted: 0
      }).then((response) => {
        this.dataSource = response.data.records;
      });
    },
    // 新建跳转
    addEsurvey(type) {
      this.$router.push({
        path: "/supply/addreview",
      });
          this.$store.state.account.eSurveyId = null;
    },
    // 标记跳转
    editFind(data,type){
      this.$store.state.account.eSurveyId = data.id;
         this.$router.push({
        path: "/supply/addreview",
      });
    },
    c_delete(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          vendorQualifyReviewDeleteRequest(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getAll();
          });
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel() {},
      });
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
