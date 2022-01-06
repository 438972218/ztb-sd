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
        <a-col :span="6">
          <a-form-model-item  label="注册地" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="注册地区" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="注册地址">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="企业名称">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-model-item  label="企业性质" >
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="企业名称">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6" class="btStyle">
          <a-button type="primary"> 供应商效验 </a-button>
        </a-col>
      </a-row>
         <a-row>
        <a-col :span="6">
          <a-form-model-item  label="生产经营地址" >
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="三级信息">
            <a-input disabled v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
      </a-row>


       <a-row class="title">联系人信息<span>(*为必填项)</span></a-row>
      <a-row>
        <a-col :span="6">
          <a-form-model-item  label="姓名" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="职位" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="手机号码" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="邮箱" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-model-item  label="证件类型" prop="name" >
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="证件号码" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
      </a-row>
         <a-row>
        <a-col :span="6">
          <a-form-model-item  label="正面照片" >
            <a-input disabled
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="背面照片">
            <a-input disabled v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
      </a-row>



      

       <a-row class="title">业务信息<span>(*为必填项)</span></a-row>
      <a-row>
        <a-col :span="6">
          <a-form-model-item  label="商业模式" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="代理品牌">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="供货品类备注">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="主要客户" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-model-item  label="主营品类" prop="name" >
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item  label="厂房性质">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
              <a-col :span="6">
          <a-form-model-item  label="厂房面积" prop="name">
            <a-input
                v-model="form.name"
            />
          </a-form-model-item>
        </a-col>
      </a-row>
    


   <a-row class="title">许可证信息<span>(*为必填项)</span></a-row>
      <a-row>
        <a-col :span="7" class="borStyle">
            <a-row>
                 <a-form-model-item  label="许可证名称">
                    <a-input
                        v-model="form.name"
                    />
                </a-form-model-item>
            </a-row>
            <a-row>
                 <a-form-model-item  label="许可证扫描件">
                    <a-input
                        v-model="form.name"
                    />
                </a-form-model-item>
            </a-row>   
             <a-row>
                 <a-form-model-item  label="文件到期日" prop="name">
                    <a-input
                        v-model="form.name"
                    />
                </a-form-model-item>
            </a-row>
        </a-col>
           <a-col :span="6" class="btStyle btBr">
          <a-button type="primary"> +添加许可证信息 </a-button>
        </a-col>
      </a-row>

    </a-form-model>
    <div>
        <a-button @click="upClick" type="primary"> 上一步 </a-button>
        <a-button @click="nextClick" style="margin-left: 8px"> 下一步 </a-button>
    </div>
  </a-card>
</template>

<script>
export default {
  name: "reading",
  data() {
    return {
      form: {
          name:''
      },
      rules: {
        name: [{ required: true, message: '请填写必填项', trigger: 'change' }],
     }
    }
  },
  methods: {
    upClick() {
      this.$emit("on-write-up");
    },
    nextClick() {
      this.$emit("on-write-next");
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
.title{
    text-align: left;
    font-size: 16px;
    margin: 20px;
    span{
    color: red;
    }
}
.btStyle{
    text-align: left;
}
.btBr{
    margin: 90px 0 0 10px;
}
.borStyle{
    border: 1px solid #ccc;
    padding-top: 20px;
}
</style>
