<template>
  <div>
    <a-card class="card-top">
      <a-form layout="horizontal">
        <a-row>
          <a-col :md="6" :sm="12">
            <a-form-item
              label="物料编码"
              :labelCol="{ sm: { span: 7 }, md: { span: 12 }, xl: { span: 7 } }"
              :wrapperCol="{
                sm: { span: 15 },
                md: { span: 12 },
                xl: { span: 15 },
              }"
            >
              <a-select
                v-model="form.materialId"
                :allowClear="true"
                placeholder="请选择"
              >
                <a-select-option
                  v-for="item in materialList"
                  :key="item.id"
                  :value="item.id"
                  >{{ item.code }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :offset="6" :span="12">
            <span style="float: right; margin-top: 3px">
              <a-button type="primary" @click="getRoleAll">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </a-card>
    <a-card>
      <div class="operator">
        <a-button @click="download_file()" type="primary">下载模板</a-button>
        <a-button @click="FileModal()" type="primary" style="margin-left: 10px"
          >导入模板</a-button
        >
      </div>
      <a-table :columns="columns" :data-source="data" :scroll="{ x: 1200 }"
        >>
        <template slot="materialCode" slot-scope="text, record">
          <span @click="addNew(text)" class="text-cursor">
            {{ text }}
          </span>
        </template>
        <!-- <template :slot="natureWeek" slot-scope="text">
          <span style="color: red; font-weight: bolder">
            {{ text }}
          </span>
        </template> -->
        <template slot="key" slot-scope="record">
          <a-button
            type="primary"
            @click="c_edit(record, 'edit')"
            class="iconstyle"
            icon="search"
          >
            编辑
          </a-button>
          <a-button
            type="danger"
            @click="c_delete(record.id)"
            class="iconstyle"
            icon="search"
          >
            删除
          </a-button>
        </template>
      </a-table>
    </a-card>
    <a-modal
      title="编辑"
      :visible="visible"
      width="800px"
      :centered="true"
      :footer="null"
      @cancel="dataClose"
    >
      <analyse-add
        ref="childrenDom"
        @getAll="getRoleAll"
        @on-data-close="dataClose"
        :editData="editData"
      >
      </analyse-add>
    </a-modal>
    <a-modal
      title="导入模板"
      :visible="fileShow"
      :centered="true"
      :confirm-loading="confirmLoading"
      @ok="fileOk"
      :destroyOnClose="true"
      @cancel="fileCancel"
    >
      <a-upload
        :file-list="fileList"
        :remove="handleRemove"
        :before-upload="beforeUpload"
      >
        <a-button :disabled="fileList.length === 1">
          <a-icon type="upload" /> 请选择文件
        </a-button>
      </a-upload>
    </a-modal>
  </div>
</template>
<script>
import { getNatureWeek } from "@/utils/methods";
import {
  materialUsageRequest,
  materialImportExcelRequest,
  materialGetRequest,
} from "@/services/material/material";
import _ from "lodash";
import AnalyseAdd from './components/analyseAdd.vue';

export default {
  name: "role",
  components: { AnalyseAdd },
  data() {
    return {
      natureWeek: "",
      materialList: [],
      form: {
        materialId: "",
      },
      fileList: [],
      confirmLoading: false,
      fileShow: false,
      types: null,
      editData: null,
      visible: false,
      expandedKeys: ["0-0-0", "0-0-1"],
      autoExpandParent: true,
      checkedKeys: ["0-0-0"],
      selectedKeys: [],

      data: [],
      columns: [
        {
          title: "序号",
          width: 70,
          key: "index",
          customRender: (text, record, index) => `${index + 1}`,
        },
        {
          title: "物料编码",
          width: 90,
          dataIndex: "materialCode",
          scopedSlots: { customRender: "materialCode" },
        },
      ],
    };
  },
  created() {},
  mounted() {
    this.natureWeek = getNatureWeek();
    this.getRoleAll();
    this.getMaterial();
  },
  methods: {
    // 查询物料
    getMaterial() {
      materialGetRequest({
        currentPage: 1,
      }).then((response) => {
        this.materialList = response.data.records;
      });
    },
    // 文件下载
    download_file() {
      let a = document.createElement("a");
      a.href = "http://10.20.54.133:9000/inv-biz/material_usage.xlsx";
      a.click();
     
    },
    //导入模板文件
    handleRemove(file) {
      const index = this.fileList.indexOf(file);
      const newFileList = this.fileList.slice();
      newFileList.splice(index, 1);
      this.fileList = newFileList;
    },
    beforeUpload(file) {
      this.fileList = [...this.fileList, file];
      return false;
    },
    // 弹窗关闭
    fileCancel() {
      this.fileList = [];
      this.fileShow = false;
    },
    // 导入模板方法
    FileModal() {
      this.fileShow = true;
    },
    fileOk(e) {
      const { fileList } = this;
      if (fileList.length === 0) {
        this.$message.warning("请选择文件");
      } else {
        this.confirmLoading = true;
        const formData = new FormData();
        formData.append("excelFile", fileList[0]);
        materialImportExcelRequest(formData, {}).then((response) => {
          if (response.code === 0) {
            this.$message.success("导入成功");
            this.fileList = [];
            this.confirmLoading = false;
            this.fileShow = false;
          }
        });
      }
    },
    //角色列表
    getRoleAll() {
      // this.form.weekFrom = Number(this.form.weekFrom)
      //       this.form.weekTo = Number(this.form.weekFrom)
      materialUsageRequest({
        currentPage: 1,
        ...this.form,
      }).then((response) => {
        this.conversionData(response.data.records);
      });
    },
    // 更改请求数据用于展示
    conversionData(obj) {
      let list = [];
      for (let i = 0; i < obj.length; i++) {
        let data = {};
        data.fromWeek = obj[i].fromWeek;
        data.toWeek = obj[i].toWeek;
        data.materialCode = obj[i].materialCode;
        data.materialId = obj[i].materialId;
        for (let j = 0; j < obj[i].materialUsageVOList.length; j++) {
          data[obj[i].materialUsageVOList[j].weekNum] =
            obj[i].materialUsageVOList[j].consumptionQty;
        }
        list.push(data);
      }
      this.data = list;
      let to = [];
      let from = [];
      for (let i = 0; i < this.data.length; i++) {
        to.push(this.data[i].fromWeek);
        from.push(this.data[i].toWeek);
      }
      this.tableAppend(Math.max(...to), Math.max(...from));
    },
    tableAppend(from, to) {
      if (this.columns.length === 2) {
        for (let i = from; i <= to; i++) {
          if (i < this.natureWeek) {
            this.columns.push({
              title: "第" + i + "周",
              align: "center",
              dataIndex: i,
              width: 100,
              className: 'before-table',
              scopedSlots: { customRender: i },
            });
          } else if(i > this.natureWeek){
                 this.columns.push({
              title: "第" + i + "周",
              align: "center",
              dataIndex: i,
              width: 140,
                className: 'after-table',
              scopedSlots: { customRender: i },
            });
          }
          else {
            this.columns.push({
              title: "第" + i + "周(当前周)",
              align: "center",
              dataIndex: i,
              width: 140,
               className: 'middle-table',
              scopedSlots: { customRender: i },
            });
          }
        }
      }
    },
    dataClose() {
      this.$refs.childrenDom.form = {
        maxNumOfInvWeeks: "",
        onHand: "",
        targetInvTurnover: "",
      };
      this.visible = false;
      this.editData = null;
    },
    addNew(obj) {
      materialGetRequest({
        currentPage: 1,
        code: obj,
      }).then((response) => {
        this.editData = response.data.records[0];
      });
      this.visible = true;
    },
    c_edit(editData, type) {
      this.editData = editData;
      this.addNew(type);
    },
    c_delete(id) {
      let that = this;
      this.$confirm({
        title: "警告",
        content: "你确定要删除用户吗？",
        centered: true,
        onOk() {
          DeleteRoleRequestAll(id).then((response) => {
            if (response.code === 0) {
              that.$message.success("删除成功");
            } else {
              that.$message.error(response.message);
            }
            that.getRoleAll();
          });
        },
        onCancel() {},
      });
    },
    handleOk() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          console.log(valid);
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>
<style lang="less" scoped>
.iconstyle {
  margin: 0 10px;
}
/deep/.before-table{
  color: #999!important
}
/deep/.middle-table{
color:red
}
/deep/.after-table{
  color: rgb(30, 179, 79)!important
}
</style>