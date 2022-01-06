<template>
  <div>
    <a-card class="card-top">
      <!-- <div :class="advanced ? 'search' : null"> -->
      <div>
        <a-form layout="horizontal">
          <!-- <div :class="advanced ? null: 'fold'"> -->
          <div>
            <a-row>
              <!-- <a-col :offset="1" :sm="24" :md="12" :xl="5">
                <a-form-item :labelCol="{ span: 2 }" :wrapperCol="{ span: 19 }">
                  <a-input placeholder="选择品类编码" />
                </a-form-item>
              </a-col> -->
              <a-col :span="6">
                <a-form-item
                  label="品类名称"
                  :labelCol="{ span: 10 }"
                  :wrapperCol="{ span: 14 }"
                >
                  <a-input v-model="itemName" placeholder="选择品类名称" />
                </a-form-item>
              </a-col>
              <!-- :labelCol="{ span: 5 }"
                  :wrapperCol="{ span: 18, offset: 0 }" -->
              <!-- <a-col :offset="1" :sm="24" :md="12" :xl="5">
                <a-form-item :labelCol="{ span: 2 }" :wrapperCol="{ span: 19 }">
                  <a-input placeholder="选择品类分类" />
                </a-form-item>
              </a-col> -->

              <a-col :span="18" align="right">
                <a-button type="primary" @click="getAll">查询</a-button>
              </a-col>
            </a-row>
          </div>
        </a-form>
      </div>
    </a-card>
    <a-card>
      <div>
        <div class="operator">
          <a-button v-has="['category:add']" @click="addNew()" type="primary"
            >添加</a-button
          >
          <!-- <a-button type="primary" style="margin-left: 10px">删除</a-button> -->
        </div>
        <a-table :columns="columns" :data-source="dataSource" row-key="a1">
          <template slot="operation" slot-scope="record">
            <span>
              <span
                v-has="['category:edit']"
                type="primary"
                size="small"
                @click="editNew(record)"
                class="operation-style"
              >
                编辑
              </span>
              <span
                v-has="['category:delete']"
                type="danger"
                size="small"
                @click="c_delete(record.id)"
                class="operation-style"
              >
                删除
              </span>
            </span>
          </template>
        </a-table>
      </div>
    </a-card>
    <a-modal
      v-model="visible"
      width="500px"
      @cancel="visible"
      @ok="saveItem"
      title="添加"
    >
      <a-form-model
        ref="ruleForm"
        :rules="rules"
        :model="form"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 20 }"
      >
        <a-row :gutter="20">
          <a-col :span="24">
            <a-form-model-item prop="itemCode" label="品类编码">
              <a-input v-model="form.itemCode" placeholder="请输入品类编码" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item prop="itemName" label="品类名称">
              <a-input v-model="form.itemName" placeholder="请输入品类名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item prop="itemDesc" label="品类描述">
              <a-input v-model="form.itemDesc" placeholder="请输入品类描述" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { itemGetRequest, itemDeleteRequest } from "@/services/basis/index"
import { itemPostRequest, itemPutRequest } from "@/services/basis/index"
export default {
  name: "Items",
  components: {},
  data() {
    return {
      num: String,
      advanced: true,
      itemName: "",
      visible: false,
      form: {
        itemCode: "",
        itemName: "",
        itemDesc: "",
      },
      rules: {
        itemCode: [
          {
            required: true,
            message: "品类编码不能为空",
            trigger: "blur",
          },
        ],
        itemName: [
          {
            required: true,
            message: "品类名称不能为空",
            trigger: "blur",
          },
        ],
      },
      columns: [
        {
          title: "序号",
          key: "index",
          align: "center",
          width: 70,
          customRender: (text, record, index) => `${index + 1}`,
        },

        {
          title: "品类编码",
          dataIndex: "itemCode",
          align: "center",
          scopedSlots: { customRender: "itemCode" },
        },
        {
          title: "品类名称",
          dataIndex: "itemName",
          align: "center",
        },
        {
          title: "品类描述",
          dataIndex: "itemDesc",
          align: "center",
        },
        // {
        //   title: "基本计量单位",
        //   width: 200,
        //   dataIndex: "basicMeasure",
        //   align: "center",
        // },
        // {
        //   title: "是否用于销售",
        //   dataIndex: "ifUseSale",
        //   width: 200,
        //   align: "center",
        // },
        // {
        //   title: "是否用于采购",
        //   width: 200,
        //   dataIndex: "ifUsePurchase",
        //   align: "center",
        // },
        {
          title: "操作",
          scopedSlots: { customRender: "operation" },
        },
      ],
      dataSource: [],
      selectedRows: [],
    }
  },
  computed: {
    rowSelection() {
      return {
        onChange: (selectedRowKeys, selectedRows) => {},
      }
    },
  },
  created() {
    this.getAll()
  },
  methods: {
    getAll() {
      itemGetRequest({
        itemName: this.itemName,
        currentPage: "1",
        deleted: 0,
      }).then(response => {
        this.dataSource = response.data.records
      })
    },
    // 添加
    saveItem() {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          let result = null
          if (this.form.id) {
            result = await itemPutRequest(this.form)
          } else {
            result = await itemPostRequest(this.form)
          }
          if (result.code === 0) {
            this.$message.success("保存成功")
            this.getAll()
            this.visible = false
          } else {
            this.$message.error(result.message)
          }
        } else {
          return false
        }
      })
    },
    c_release() {
      this.$confirm({
        title: "警告",
        content: "你确定要发布吗？",
        centered: true,
        onOk() {
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel() {},
      })
    },
    c_delete(id) {
      let that = this
      this.$confirm({
        title: "警告",
        content: "你确定要删除吗？",
        centered: true,
        onOk() {
          itemDeleteRequest(id).then(response => {
            if (response.code === 0) {
              that.$message.success("删除成功")
            } else {
              that.$message.error(response.message)
            }
            that.getAll()
          })
        },
        onCancel() {},
      })
    },
    addNew() {
      this.form = {
        itemCode: "",
        itemName: "",
        itemDesc: "",
      }
      this.visible = true
      // this.$router.push({
      //   path: "/basis/additems",
      // });
      // this.$store.state.account.itemType = "add";
    },
    editNew(obj) {
      this.form = Object.assign({}, obj)
      this.visible = true
      // this.$router.push({
      //   path: "/basis/additems",
      // });
      // this.$store.state.account.itemData = obj;
      // this.$store.state.account.itemType = "edit";
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
.operator {
  margin: 5px 0 18px 0;
}
@media screen and (max-width: 900px) {
  .fold {
    width: 100%;
  }
}
.iconstyle {
  margin: 0 10px;
}
</style>
