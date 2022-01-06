<template>
  <a-card class="result-error" :bordered="false">
    <a-form-model
      ref="ruleForm"
      :model="form"
      :rules="rules"
      :label-col="{ span: 7 }"
      :wrapper-col="{ span: 16 }"
    >
      <a-row class="title">登记信息<span>(*为必填项)</span></a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="企业名称"
            prop="name"
          >
            <a-input placeholder="请输入企业名称" v-model="form.name" />
          </a-form-model-item>
        </a-col>
        <a-col :span="8" class="btStyle">
          <a-button type="primary"> 供应商效验 </a-button>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="统一社会信用代码"
            prop="uscc"
          >
            <a-input placeholder="请输入统一社会信用代码" v-model="form.uscc" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            prop="legalRepresentative"
            label="法定代表人"
          >
            <a-input
              placeholder="请输入法定代表人"
              v-model="form.legalRepresentative"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="登记状态"
            prop="registrationStatus"
          >
            <a-input
              placeholder="请输入登记状态"
              v-model="form.registrationStatus"
            />
          </a-form-model-item>
        </a-col>

        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="注册资本(万元)"
            prop="registeredCapital"
          >
            <a-input
              placeholder="请输入注册资本(万元)"
              v-model="form.registeredCapital"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="组织机构代码"
            prop="orzCode"
          >
            <a-input placeholder="请输入组织机构代码" v-model="form.orzCode" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="工商注册号"
            prop="icrn"
          >
            <a-input placeholder="请输入工商注册号" v-model="form.icrn" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="纳税人识别号"
            prop="taxpayNum"
          >
            <a-input
              placeholder="请输入纳税人识别号"
              v-model="form.taxpayNum"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="企业类型"
            prop="type"
          >
            <a-input placeholder="请输入企业类型" v-model="form.type" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="所属地区"
            prop="area"
          >
            <a-input placeholder="请输入所属地区" v-model="form.area" />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 15 }"
            label="所属行业"
            prop="industry"
          >
            <a-input placeholder="请输入所属行业" v-model="form.industry" />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 15 }"
            label="注册地址"
            prop="registeredAddress"
          >
            <a-input
              placeholder="请输入注册地址"
              v-model="form.registeredAddress"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 15 }"
            label="经营范围"
            prop="businessScope"
          >
            <a-textarea
              placeholder="请输入经营范围"
              v-model="form.businessScope"
              :auto-size="{ minRows: 2, maxRows: 5 }"
            />
          </a-form-model-item>
        </a-col>
      </a-row>

      <a-row class="title">联系人信息<span>(*为必填项)</span></a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="姓名"
            prop="contactName"
          >
            <a-input placeholder="请输入姓名" v-model="form.contactName" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="职位"
            prop="contactTitle"
          >
            <a-input placeholder="请输入职位" v-model="form.contactTitle" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="手机号码"
            prop="contactMobile"
          >
            <a-input
              placeholder="请输入手机号码"
              v-model="form.contactMobile"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="邮箱"
            prop="contactEmail"
          >
            <a-input placeholder="请输入邮箱" v-model="form.contactEmail" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="证件类型"
            prop="contactIdentityType"
          >
            <a-select
              v-model="form.contactIdentityType"
              placeholder="请选择证件类型"
            >
              <a-select-option
                v-for="item in certificateTypeList"
                :key="item.id"
                :value="item.meaning"
                >{{ item.meaning }}</a-select-option
              >
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="证件号码"
            prop="contactIdentityNum"
          >
            <a-input
              placeholder="请输入证件号码"
              v-model="form.contactIdentityNum"
            />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="正面照片"
          >
            <a-upload
              list-type="picture-card"
              :before-upload="beforeUploadFront"
              :file-list="fileListFront"
              @change="handleChangeFront"
            >
              <img
                v-if="previewImageFront"
                :src="previewImageFront"
                style="width: 100%"
                alt="avatar"
              />
              <div>
                <a-icon type="plus" />
                <div class="ant-upload-text">上传</div>
              </div>
            </a-upload>
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="背面照片"
          >
            <a-upload
              list-type="picture-card"
              :before-upload="beforeUpload"
              :file-list="fileList"
              @change="handleChange"
            >
              <img
                v-if="previewImage"
                :src="previewImage"
                style="width: 100%"
                alt="avatar"
              />
              <div>
                <a-icon type="plus" />
                <div class="ant-upload-text">上传</div>
              </div>
            </a-upload>
          </a-form-model-item>
        </a-col>
      </a-row>

      <a-row class="title">业务信息<span>(*为必填项)</span></a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="商业模式"
            prop="businessModel"
          >
            <a-input
              placeholder="请输入商业模式"
              v-model="form.businessModel"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="代理品牌"
            prop="agentBrand"
          >
            <a-input placeholder="请输入企代理品牌" v-model="form.agentBrand" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="供货品类备注"
            prop="suplyCategoryRemark"
          >
            <a-input
              placeholder="请输入供货品类备注"
              v-model="form.suplyCategoryRemark"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="主要客户"
            prop="mainCustomer"
          >
            <a-input placeholder="请输入主要客户" v-model="form.mainCustomer" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="主营品类"
            prop="mainCategory"
          >
            <!-- <a-input placeholder="请输入主营品类" v-model="form.mainCategory" /> -->
            <a-tree-select
              v-model="form.mainCategory"
              style="width: 100%"
              :tree-data="treeList"
              tree-checkable
              :show-checked-strategy="SHOW_PARENT"
              search-placeholder="请选择主营品类"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="厂房性质"
            prop="factoryNature"
          >
            <a-input
              placeholder="请输入厂房性质"
              v-model="form.factoryNature"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="厂房面积"
            prop="factoryArea"
          >
            <a-input placeholder="请输入厂房面积" v-model="form.factoryArea" />
          </a-form-model-item>
        </a-col>
      </a-row>

      <a-row class="title">许可证信息<span>(*为必填项)</span></a-row>
      <div style="text-align:left">
        <a-button @click="addNew" style="margin-bottom: 10px" type="primary"
          >新建</a-button
        >
      </div>
      <a-row>
        <a-table
          :columns="columns"
          :data-source="form.vendorRequestCertificateDTOS"
        >
          <template slot="operation" slot-scope="text, record, index">
            <span @click="c_delete(index)" class="operation-style"> 删除 </span>
          </template>
        </a-table>
      </a-row>
    </a-form-model>
    <div style="margin-top: 20px">
      <a-button @click="upClick" type="primary"> 上一步 </a-button>
      <a-button @click="nextClick" style="margin-left: 8px"> 下一步 </a-button>
    </div>
    <a-modal
      v-model="visible"
      width="500px"
      :footer="null"
      :centered="true"
      title="添加"
    >
      <a-form-model layout="horizontal" class="page-form">
        <!-- <div :class="advanced ? null: 'fold'"> -->
        <a-row>
          <a-form-model-item
            label="附件上传"
            :labelCol="{ span: 6 }"
            :wrapperCol="{ span: 17 }"
          >
            <a-upload
              :file-list="file"
              :remove="handleRemove"
              :before-upload="beforeUploadFile"
            >
              <a-button :disabled="file.length === 1">
                <a-icon type="upload" /> 请选择文件
              </a-button>
            </a-upload>
          </a-form-model-item>
        </a-row>
      </a-form-model>

      <a-row style="text-align: center">
        <a-space>
          <a-button type="primary" @click="addItems">新建</a-button>
          <a-button @click="dataClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
  </a-card>
</template>

<script>
import { queryDictionarys } from "@/utils/methods";
import { uploadfilePostRequest } from "@/services/system/user";
import { vendorPostRequest } from "@/services/supplier/index";
import { categorytreeGetRequest } from "@/services/basis/index";
import { treeselectChange } from "@/utils/methods";
import { TreeSelect } from "ant-design-vue";
const SHOW_PARENT = TreeSelect.SHOW_PARENT;
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "附件类型",
    dataIndex: "attachmentType",
  },
  {
    title: "附件名称",
    dataIndex: "attachmentName",
  },
  {
    title: "操作",
    dataIndex: "operation",
    key: "operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
];

export default {
  name: "reading",

  data() {
    return {
      SHOW_PARENT,
      treeList: [],
      file: [],
      columns,
      previewImageFront: "",
      fileListFront: [],
      previewImage: "",
      fileList: [],
      visible: false,
      // previewVisible: false,
      // previewImage: "",
      file: [],
      certificateTypeList: [],
      form: {
        // 企业信息
        name: "", // 企业名称
        uscc: "", // 统一社会信用代码
        legalRepresentative: "", // 法定代表人
        registrationStatus: "", // 登记状态
        registeredCapital: "", // 注册资本
        orzCode: "", // 组织机构代码
        icrn: "", // 工商注册号
        taxpayNum: "", // 纳税人识别号
        type: "", // 企业类型
        area: "", // 所属地区
        industry: "", // 所属行业
        registeredAddress: "", // 注册地址
        businessScope: "", // 经营范围
        //联系人信息
        contactName: "", // 联系人姓名
        contactTitle: "", // 联系人职位
        contactMobile: "", // 联系人手机号
        contactEmail: "", // 联系人邮箱
        contactIdentityType: undefined, // 	联系人证件类型
        contactIdentityNum: "", // 	联系人证件号码
        identityFront: "", // 证件正面照片
        identityReverse: "", // 	证件反面照片
        // 业务信息
        businessModel: "", // 商业模式
        agentBrand: "", // 代理品牌
        suplyCategoryRemark: "", // 供货品类备注
        mainCategory: [], // 主营品类
        mainCustomer: "", // 主要客户
        factoryArea: "", // 	厂房面积
        factoryNature: "", // 	厂房性质
        vendorRequestCertificateDTOS: [],
      },
      rules: {
        name: [{ required: true, message: "请填写必填项", trigger: "change" }],
      },
    };
  },
  created() {
    this.getTreeAll();
  },
  mounted() {
    queryDictionarys("certificateType").then((res) => {
      this.certificateTypeList = res;
    });
  },
  methods: {
    // 查询品类
    getTreeAll() {
      categorytreeGetRequest({
        currentPage: "1",
      }).then((response) => {
        let list = JSON.parse(JSON.stringify(response.data));
        treeselectChange(list);
        this.treeList = list;
      });
    },
    // 新建供应商

    // 正面图片
    beforeUploadFront(file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (e) => {
        const code = e.target.result;
        this.previewImageFront = code;
      };
      return false;
    },
    imgAddFront(data) {
      this.previewImageFront = data.response.path;
    },
    handleChangeFront(info) {
      let formdata = new FormData();
      formdata.append("uploadFile", info.file);
      uploadfilePostRequest(formdata, {}).then((response) => {
        this.form.identityFront = response.data.attachmentUrl;
      });
    },
    // 背面
    beforeUpload(file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (e) => {
        const code = e.target.result;
        this.previewImage = code;
      };
      return false;
    },
    imgAddFront(data) {
      this.previewImage = data.response.path;
    },
    handleChange(info) {
      let formdata = new FormData();
      formdata.append("uploadFile", info.file);
      uploadfilePostRequest(formdata, {}).then((response) => {
        this.form.identityReverse = response.data.attachmentUrl;
      });
    },
    // 文件上传弹窗
    addNew() {
      (this.data = []), (this.visible = true);
    },
    // 文件上传
    beforeUploadFile(file) {
      this.file = [...this.file, file];
      return false;
    },
    // 删除文件
    handleRemove(file) {
      const index = this.file.indexOf(file);
      const newFileList = this.file.slice();
      newFileList.splice(index, 1);
      this.file = newFileList;
    },
    //  删除文件
    c_delete(text) {
      this.form.vendorRequestCertificateDTOS.splice(text, 1);
    },
    // 选择文件
    addItems() {
      const { file } = this;
      if (file.length === 0) {
        this.$message.warning("请选择文件");
      } else {
        this.confirmLoading = true;
        const formData = new FormData();
        formData.append("uploadFile", file[0]);
        uploadfilePostRequest(formData, {}).then((response) => {
          this.form.vendorRequestCertificateDTOS.push(response.data);
          this.confirmLoading = false;
          this.visible = false;
        });
      }
    },
    // 关闭弹窗
    dataClose() {
      this.file = [];
      this.visible = false;
    },
    upClick() {
      this.$emit("on-write-up");
    },
    nextClick() {
      this.$emit("on-write-next", {});
      // this.form.mainCategory = this.form.mainCategory.toString();
      // vendorPostRequest({
      //   ...this.form,
      // }).then((response) => {
      //   if (response.code === 0) {
      //     this.$message.success("提交成功");
      //     this.$emit("on-write-next", response.data);
      //   } else {
      //     this.$message.error(response.message);
      //   }
      // });
    },
    // handleSubmit() {
    // this.$refs.ruleForm.validate(valid => {
    //     if (valid) {
    //       alert('submit!');
    //     } else {
    //       console.log('error submit!!');
    //       return false;
    //     }
    //   });
    // },
  },
};
</script>

<style scoped lang="less">
.result-error {
  p {
    text-indent: 30px;
    text-align: left;
  }
}
.title {
  text-align: left;
  font-size: 16px;
  margin: 20px;
  span {
    color: red;
  }
}
.btStyle {
  text-align: left;
}
.btBr {
  margin: 90px 0 0 10px;
}
.borStyle {
  border: 1px solid #ccc;
  padding-top: 20px;
}
</style>
