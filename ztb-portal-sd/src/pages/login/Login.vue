<template>
  <common-layout>
    <div class="top">
      <!-- <div class="header">
        <img alt="logo" class="logo" src="@/assets/img/logo.png" />
        <span class="title">{{systemName}}</span>
      </div>
      <div class="desc">Ant Design 是西湖区最具影响力的 Web 设计规范</div> -->
    </div>
    <div class="login">
      <a-form @submit="onSubmit" :form="form">
        <a-tabs
          size="large"
          :tabBarStyle="{ textAlign: 'center' }"
          style="padding: 0 2px; border: 0"
        >
          <a-tab-pane tab="账户密码登录" key="1">
            <!-- <a-alert type="error" :closable="true" v-show="error" :message="error" showIcon style="margin-bottom: 24px;" /> -->
            <a-form-item>
              <a-input
                size="large"
                placeholder="请输入账户名"
                v-decorator="[
                  'name',
                  {
                    rules: [
                      {
                        required: true,
                        message: '请输入账户名',
                        whitespace: true,
                      },
                    ],
                  },
                ]"
              >
                <a-icon slot="prefix" type="user" />
              </a-input>
            </a-form-item>
            <a-form-item>
              <a-input
                size="large"
                placeholder="请输入密码"
                type="password"
                v-decorator="[
                  'password',
                  {
                    rules: [
                      {
                        required: true,
                        message: '请输入密码',
                        whitespace: true,
                      },
                    ],
                  },
                ]"
              >
                <a-icon slot="prefix" type="lock" />
              </a-input>
            </a-form-item>

            <!-- <a-row>
              <a-col :span="12"> -->
            <!-- <a-form-item>
              <a-input
                size="large"
                v-decorator="['captcha', {rules: [{ required: true, message: '请输入验证码', whitespace: true}]}]"
              >
              </a-input>
            </a-form-item>
              </a-col>
                <a-col :span="11" :offset='1'>
              
          <img style="width: 100%;margin-top:2px"    @click="getClick" :src="imgUrl"/>

              </a-col>
            </a-row> -->
          </a-tab-pane>
          <!-- <a-tab-pane tab="手机号登录" key="2">
            <a-form-item>
              <a-input size="large" placeholder="mobile number">
                <a-icon slot="prefix" type="mobile" />
              </a-input>
            </a-form-item>
          </a-tab-pane> -->
        </a-tabs>
        <div>
          <!-- <a-checkbox :checked="true" >自动登录</a-checkbox>  -->
        </div>
        <a-form-item>
          <a-button
            :loading="logging"
            style="width: 100%"
            size="large"
            htmlType="submit"
            type="primary"
            >登录</a-button
          >
        </a-form-item>
        <div>
          <!-- 其他登录方式
          
          <a-icon class="icon" type="alipay-circle" />
          <a-icon class="icon" type="taobao-circle" />
          <a-icon class="icon" type="weibo-circle" /> -->
          <a style="float: left" @click="forgetPassword">忘记密码</a>

          <router-link style="float: right" to="/register"
            >注册账户</router-link
          >
        </div>
      </a-form>
    </div>
    <a-modal
      v-model="forgetpasswordShow"
      title="忘记密码"
      @cancel="forgetpasswordShow = false"
      @ok="changePassword"
    >
      <a-form-model
        ref="ruleForm"
        :labelCol="{ span: 6 }"
        :wrapperCol="{ span: 17 }"
        :model="forgetpasswordForm"
        :rules="rules"
      >
        <a-form-model-item prop="userName" label="用户名">
          <a-input
            style="color: black"
            placeholder="请输入用户名"
            v-model="forgetpasswordForm.userName"
          />
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </common-layout>
</template>

<script>
import { userLogin, forgetPassword } from "@/services/user.js";
// import {setAuthorization} from '@/utils/request'

import CommonLayout from "@/layouts/CommonLayout";
// import {login} from '@/services'
// import {setAuthorization} from '@/utils/request'
import { loadRoutes } from "@/utils/routerUtil";
import { mapMutations } from "vuex";
import {
  vendorshowByUserIdGetVendor
} from "@/services/supplier/index";

export default {
  name: "Login",
  components: { CommonLayout },
  data () {
    return {
      logging: false,
      error: "",
      form: this.$form.createForm(this),
      imgUrl: "",
      forgetpasswordShow: false,
      forgetpasswordForm: {
        userName: ''
      },
      rules: {
        userName: [
          {
            required: true,
            message: "用户名不可为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted () {
    // this.getClick()
  },
  computed: {
    systemName () {
      return this.$store.state.setting.systemName;
    },
  },
  methods: {
    ...mapMutations("account", ["setUser", "setPermissions", "setRoles"]),
    // 忘记密码
    forgetPassword () {
      this.forgetpasswordForm = {
        userName: ''
      }
      this.forgetpasswordShow = true
    },
    // 修改密码
    async changePassword () {
      const result = await forgetPassword(this.forgetpasswordForm.userName)
      if (result.code === 0) {
        this.$message.success('密码已重置，请查看邮件')
        this.forgetpasswordShow = true
      } else {
        this.$message.error(result.message)
      }
    },
    onSubmit (e) {
      e.preventDefault();
      this.form.validateFields((err) => {
        if (!err) {
          this.logging = true;
          const name = this.form.getFieldValue("name");
          const password = this.form.getFieldValue("password");
          this.loginGet(name, password);
        }
      });
    },
    async loginGet (name, password) {
      const data = {
        account: name,
        password: password,
      };
      const response = await userLogin(data);
      if (response.code === 0) {
        const permissions = {
          id: response.data.id,
          operation: response.data.roles[0].permissions,
        };
        let list = {
          id: permissions.id,
          operation: [],
        };
        for (let i = 0; i < permissions.operation.length; i++) {
          list.operation.push(permissions.operation[i].permission);
        }

        // vendorshowByUserIdGetVendor(response.data.id).then((res) => {
        //   if (res.data) {
        //     let code = { code: res.data.code }
        //     let data = { ...response.data, ...code }
        //     this.setUser(data);
        //   } else {
        //     this.setUser(response.data);
        //   }
        // });
        this.setUser(response.data);
        this.setPermissions(list);
        localStorage.setItem("userId", response.data.id);
        loadRoutes({
          router: this.$router,
          store: this.$store,
          i18n: this.$i18n,
        });
        this.$router.push("/dashboard");
        this.$message.success("欢迎回来", 3);
      } else {
        this.$message.error(response.message);
        this.logging = false;
      }
      this.logging = false;
    },
  },
};
</script>

<style lang="less" scoped>
/deep/.ant-tabs-bar {
  border: 0;
  // color: #fff;
}
/deep/.has-error .ant-input-affix-wrapper .ant-input,
.has-error .ant-input-affix-wrapper .ant-input:hover {
  background-color: transparent;
  // color: #fff;
}
.common-layout {
  position: relative;
  .top {
    text-align: center;
    .header {
      height: 44px;
      line-height: 44px;
      a {
        text-decoration: none;
      }
      .logo {
        height: 44px;
        vertical-align: top;
        margin-right: 16px;
      }
      .title {
        font-size: 33px;
        color: @title-color;
        font-family: 'Myriad Pro', 'Helvetica Neue', Arial, Helvetica,
          sans-serif;
        font-weight: 600;
        position: relative;
        top: 2px;
      }
    }
    .desc {
      font-size: 14px;
      color: @text-color-second;
      margin-top: 12px;
      margin-bottom: 40px;
    }
  }
  .login {
    position: absolute;
    padding: 15px;
    top: 50%;
    left: 70%;
    background: #f0f0f0a6;
    transform: translate(-50%, -50%);
    width: 368px;
    margin: 0 auto;
    @media screen and (max-width: 576px) {
      width: 95%;
    }
    @media screen and (max-width: 320px) {
      .captcha-button {
        font-size: 14px;
      }
    }
    // input{
    //   background-color: transparent !important;
    // }
    .icon {
      font-size: 24px;
      color: @text-color-second;
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: @primary-color;
      }
    }
  }
}
</style>
