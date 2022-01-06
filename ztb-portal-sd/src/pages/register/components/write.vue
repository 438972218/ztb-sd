<template>
  <a-card :bordered="false">
    <a-form-model
      ref="ruleForm2"
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
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="统一社会信用代码"
            prop="businessLicense"
          >
            <a-input
              placeholder="请输入统一社会信用代码"
              v-model="form.businessLicense"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            prop="vatNumber"
            label="纳税人识别号"
          >
            <a-input
              placeholder="请输入纳税人识别号"
              v-model="form.vatNumber"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="企业邮箱"
            prop="enterpriseMail"
          >
            <a-input
              placeholder="请输入企业邮箱"
              v-model="form.enterpriseMail"
            />
          </a-form-model-item>
        </a-col>

        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="座机号码"
            prop="mainPhone"
          >
            <a-input placeholder="请输入座机号码" v-model="form.mainPhone" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="邮编"
            prop="postcode"
          >
            <a-input placeholder="请输入邮编" v-model="form.postcode" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="城市"
            prop="city"
          >
            <a-input placeholder="请输入城市" v-model="form.city" />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="国家"
            prop="country"
          >
            <a-input placeholder="请输入国家" v-model="form.country" />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 20 }"
            label="网站"
            prop="website"
          >
            <a-input placeholder="请输入网站" v-model="form.website" />
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 20 }"
            label="注册地址"
            prop="registeredAddress"
          >
            <a-input
              placeholder="请输入注册地址"
              v-model="form.registeredAddress"
            />
          </a-form-model-item>
        </a-col>
        <!-- <a-col :span="24">
          <a-form-model-item
            :labelCol="{ span: 3 }"
            :wrapperCol="{ span: 21 }"
            label="品类"
            prop="category"
          >
            <a-input placeholder="请输入品类" v-model="form.category" />
          </a-form-model-item>
        </a-col> -->
      </a-row>
      <a-row class="title">用户信息<span>(*为必填项)</span></a-row>
      <a-row>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="姓名"
            prop="registerUserDTO.name"
          >
            <a-input
              placeholder="请输入姓名"
              v-model="form.registerUserDTO.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="用户名"
            prop="registerUserDTO.userName"
          >
            <a-input
              placeholder="请输入用户名"
              v-model="form.registerUserDTO.userName"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="密码"
            prop="registerUserDTO.password"
          >
            <a-input-password
              placeholder="请输入密码"
              v-model="form.registerUserDTO.password"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="手机号"
            prop="registerUserDTO.phone"
          >
            <a-input
              placeholder="请输入手机号"
              v-model="form.registerUserDTO.phone"
            />
          </a-form-model-item>
        </a-col>
        <a-col :sm="24" :md="12" :xl="8">
          <a-form-model-item
            :labelCol="{ span: 9 }"
            :wrapperCol="{ span: 14 }"
            label="邮箱"
            prop="registerUserDTO.mail"
          >
            <a-input
              placeholder="请输入邮箱"
              v-model="form.registerUserDTO.mail"
            />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row class="title">附件</a-row>
      <a-row class="title">
        <div style="text-align: left">
          <a-button @click="addNew" style="margin-bottom: 10px" type="primary">
            上传
          </a-button>
        </div>
        <a-table :columns="columns" :data-source="form.vendorAttachmentDTOS">
          <template slot="operation" slot-scope="text, record, index">
            <span @click="c_delete(index)" class="operation-style"> 删除 </span>
            <a-icon type="download" @click="handleDownload(record)" />
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
      title="附件"
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
          <a-button type="primary" @click="addItems">确定</a-button>
          <a-button @click="dataClose">返回</a-button>
        </a-space>
      </a-row>
    </a-modal>
  </a-card>
</template>
<script>
import { queryDictionarys } from "@/utils/methods"
import {
  uploadfilePostRequest,
  attachmentGetfileGetRequest,
} from "@/services/system/user"
import { vendorPostRequest } from "@/services/supplier/index"
import { categorytreeGetRequest } from "@/services/basis/index"
import { treeselectChange } from "@/utils/methods"
import { TreeSelect } from "ant-design-vue"
import { regular } from "@/utils/validate"
const SHOW_PARENT = TreeSelect.SHOW_PARENT
const columns = [
  {
    title: "序号",
    key: "index",
    align: "center",
    width: 70,
    customRender: (text, record, index) => `${index + 1}`,
  },
  {
    title: "附件名称",
    dataIndex: "attachmentName",
  },
  {
    title: "附件类型",
    dataIndex: "attachmentType",
  },
  {
    title: "操作",
    dataIndex: "operation",
    key: "operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
]

export default {
  name: "reading",
  props: {
    dataList: {
      type: Object,
      default: null,
    },
  },
  data () {
    var checkEmail = (rule, value, cb) => {
      // 验证邮箱的正则表达式
      const regEmail = regular.emailNumber;
      if (regEmail.test(value)) {
        // 合法的邮箱
        return cb();
      }
      cb(new Error("请输入正确的邮箱"));
    };
    var checkMobile = (rule, value, cb) => {
      // 验证手机号的正则表达式
      const regMobile = regular.mobileNumber;
      const regTelephone = regular.telephone
      if (regMobile.test(value) || regTelephone.test(value)) {
        return cb();
      }
      cb(new Error("请输入正确的号码"));
    };
    var checkPassword = (rule, value, cb) => {
      // 验证密码的正则表达式
      const regPassword = regular.passwordreg;
      if (regPassword.test(value)) {
        return cb();
      }
      cb(new Error("密码必须由数字、字母、特殊字符组合,请输入6-16位"));
    };
    var checkCode = (rule, value, cb) => {
      // 验证统一社会信用代码以及纳税人识别号
      const regCode = regular.checkCode
      if (regCode.test(value)) {
        return cb();
      }
      cb(new Error(regCode.message));
    };
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
        name: "", // 企业名称
        businessLicense: "", // 营业执照号
        vatNumber: "", // 增值税号
        enterpriseMail: "", // 企业邮箱
        mainPhone: "", // 电话号码
        postcode: "", // 邮编
        city: "", // 城市
        country: "", // 国家
        website: "", // 网站
        registeredAddress: "", // 注册地址
        category: "", // 品类
        registerUserDTO: {
          mail: "",
          name: "",
          password: "",
          phone: "",
          userName: "",
        },
        vendorAttachmentDTOS: [], //供应商附件
      },
      rules: {
        name: [
          {
            required: true,
            message: "请输入企业名称",
            trigger: "blur",
          },
        ],
        businessLicense: [{ required: true, message: "请输入统一社会信用代码", trigger: "blur" },
          // {
          //   validator: checkCode,
          //   message: "请输入正确的统一社会信用代码",
          //   trigger: "blur"
          // }],
        ],
        vatNumber: [{ required: true, message: "请输入纳税人识别号", trigger: "blur" },
          // {
          //   validator: checkCode,
          //   message: "请输入正确的纳税人识别号",
          //   trigger: "blur"
          // }],
        ],
        mainPhone: [
          {
            validator: checkMobile,
            message: "请输入正确的座机号码",
            trigger: "blur"
          }
        ],
        enterpriseMail: [
          {
            required: true,
            message: "请输入企业邮箱",
            trigger: "blur",
          },
          {
            validator: checkEmail,
            message: "请输入正确的邮箱地址",
            trigger: "blur"
          }
        ],
        "registerUserDTO.name": [
          { required: true, message: "请输入姓名", trigger: "blur" },
        ],
        "registerUserDTO.userName": [
          {
            required: true,
            message: "英文字符开头",
            pattern: regular.enText2,
            trigger: "blur",
          },
        ],
        "registerUserDTO.password": [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            validator: checkPassword,
            message: "密码必须由数字、大写字母、小写字母、特殊字符中的两种组合,并且长度在6-16位",
            trigger: "blur"
          }
        ],
        "registerUserDTO.mail": [
          {
            required: true,
            message: "请输入邮箱",
            trigger: "blur",
          },
          {
            validator: checkEmail,
            message: "请输入正确的邮箱",
            trigger: "blur"
          }
        ],
        "registerUserDTO.phone": [
          {
            required: true,
            message: "请输入手机号",
            trigger: "blur",
          },
          {
            validator: checkMobile,
            message: "请输入正确的手机号",
            trigger: "blur"
          }
        ],
      },
    }
  },
  created () {
    // this.getTreeAll()
  },
  mounted () {
    if (this.dataList) {
      this.form = { ...this.dataList }
    }
    // queryDictionarys("certificateType").then(res => {
    //   this.certificateTypeList = res
    // })
  },
  methods: {
    creditCode (value) {
      var value = String(value);
      var patrn = /^[0-9A-Z]+$/;
      //18位校验及大写校验
      if (value.length != 18 || !patrn.test(value))
        return {
          valid: false,
          message: '格式不对'
        };
      var Ancode;//统一社会信用代码的每一个值
      var Ancodevalue;//统一社会信用代码每一个值的权重
      var total = 0;
      var weightedfactors = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];//加权因子
      var Str = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y";
      var Array_Str = Str.split(',');
      //不用I、O、S、V、Z
      for (var i = 0; i < value.length - 1; i++) {
        Ancode = value.substring(i, i + 1);
        Ancodevalue = Array_Str.indexOf(Ancode);
        total = total + Ancodevalue * weightedfactors[i];
        //权重与加权因子相乘之和
      }
      var logiccheckcode = 31 - total % 31;
      if (logiccheckcode == 31) {
        logiccheckcode = 0;
      }
      logiccheckcode = Array_Str[logiccheckcode];
      var checkcode = value.substring(17, 18);
      var valid = logiccheckcode == checkcode;
      return {
        valid,
        message: valid ? '校验通过' : '格式不对'
      }
    },
    // 查询品类
    getTreeAll () {
      categorytreeGetRequest({
        currentPage: "1",
      }).then(response => {
        let list = JSON.parse(JSON.stringify(response.data))
        treeselectChange(list)
        this.treeList = list
      })
    },
    // 正面图片
    beforeUploadFront (file) {
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = e => {
        const code = e.target.result
        this.previewImageFront = code
      }
      return false
    },
    imgAddFront (data) {
      this.previewImageFront = data.response.path
    },
    handleChangeFront (info) {
      let formdata = new FormData()
      formdata.append("uploadFile", info.file)
      uploadfilePostRequest(formdata, {}).then(response => {
        this.form.identityFront = response.data.attachmentUrl
      })
    },
    // 背面
    beforeUpload (file) {
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = e => {
        const code = e.target.result
        this.previewImage = code
      }
      return false
    },
    imgAddFront (data) {
      this.previewImage = data.response.path
    },
    handleChange (info) {
      let formdata = new FormData()
      formdata.append("uploadFile", info.file)
      uploadfilePostRequest(formdata, {}).then(response => {
        this.form.identityReverse = response.data.attachmentUrl
      })
    },
    // 文件上传弹窗
    addNew () {
      ; (this.data = []), (this.visible = true)
    },
    // 文件上传
    beforeUploadFile (file) {
      this.file = [...this.file, file]
      return false
    },
    // 删除文件
    handleRemove (file) {
      const index = this.file.indexOf(file)
      const newFileList = this.file.slice()
      newFileList.splice(index, 1)
      this.file = newFileList
    },
    //  删除文件
    c_delete (index) {
      this.form.vendorAttachmentDTOS.splice(index, 1)
    },
    // 上传文件
    addItems () {
      const { file } = this
      if (file.length === 0) {
        this.$message.warning("请选择文件")
      } else {
        this.confirmLoading = true
        const formData = new FormData()
        formData.append("uploadFile", file[0])
        uploadfilePostRequest(formData, {}).then(response => {
          this.form.vendorAttachmentDTOS.push(response.data)
          this.confirmLoading = false
          this.visible = false
        })
      }
    },
    async handleDownload (row) {
      // 下载文件
      const data = {
        path: row.attachmentUrl,
      }
      const result = await attachmentGetfileGetRequest(data)
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
    // 关闭弹窗
    dataClose () {
      this.file = []
      this.visible = false
    },
    upClick () {
      this.$emit("on-write-up", { ...this.form })
    },
    nextClick () {
      this.$refs.ruleForm2.validate(valid => {
        if (valid) {
          this.$emit("on-write-next", { ...this.form })
        } else {
          return false
        }
      })
    },
  },
}
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
.btBr {
  margin: 90px 0 0 10px;
}
.borStyle {
  border: 1px solid #ccc;
  padding-top: 20px;
}
</style>
