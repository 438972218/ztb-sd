<template>
  <div>
    <a-dropdown>
      <div class="header-avatar" style="cursor: pointer">
        <a-avatar class="avatar" size="small" shape="circle" icon="user" />
        <!-- :src="user.avatar" -->

        <span class="name">{{ user.name }}</span>
      </div>
      <a-menu :class="['avatar-menu']" slot="overlay">
        <!-- <a-menu-item>
          <a-icon type="user" />
          <span>个人中心</span>
        </a-menu-item> -->
        <a-menu-item>
          <a @click="editPassWord">
            <a-icon type="edit" />
            <span>修改密码</span>
          </a>
        </a-menu-item>
        <!-- <a-menu-item>
          <a-icon type="setting" />
          <span>设置</span>
        </a-menu-item> -->
        <a-menu-divider />
        <a-menu-item>
          <a @click="logout">
            <a-icon type="poweroff" />
            <span>退出登录</span>
          </a>
        </a-menu-item>
      </a-menu>
    </a-dropdown>
    <a-modal
      title="修改密码"
      :visible="visible"
      width="700px"
      @ok="handleSave"
      @cancel="visible = false"
    >
      <a-form-model
        layout="horizontal"
        ref="ruleForm"
        :rules="rules"
        :model="form"
        :labelCol="{ span: 4 }"
        :wrapperCol="{ span: 18 }"
        class="page-form"
      >
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="旧密码：" prop="oldPassword">
              <a-input
                type="password"
                v-model="form.oldPassword"
                placeholder="请输入旧密码"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="新密码：" prop="newPassword">
              <a-input
                type="password"
                v-model="form.newPassword"
                placeholder="请输入新密码"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="确认密码：" prop="confirmPassword">
              <a-input
                type="password"
                v-model="form.confirmPassword"
                placeholder="请再次输入新密码"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { logout } from "@/services/user.js";
import { updatePasswordById } from "@/services/structure/index";
import { sysUserupdatePasswordByIdPutRequest } from "@/services/user.js";

export default {
  name: "HeaderAvatar",
  computed: {
    ...mapGetters("account", ["user"]),
  },
  data () {
    return {
      visible: false,
      form: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
      },
      rules: {
        oldPassword: [
          { required: true, message: "请输入旧密码", trigger: "blur" },
        ],
        newPassword: [
          { required: true, message: "请输入新密码", trigger: "blur" },
        ],
        confirmPassword: [
          { required: true, message: "请再次输入新密码", trigger: "blur" },
        ],
      },
      status: true,
    };
  },
  mounted () {
    // console.log(sessionStorage.getItem('userId'))
  },
  methods: {
    logout () {
      logout();
      this.$router.push("/login");

    },
    // 修改密码
    editPassWord () {
      this.visible = true;
    },
    // 保存
    handleSave () {
      this.$refs.ruleForm.validate(async (valid) => {
        if (valid) {
          if (this.form.newPassword !== this.form.confirmPassword) {
            this.$message.warning("两次密码输入不同");
            return;
          }
          const data = {
            id: localStorage.getItem("userId"),
            oldPassword: this.form.oldPassword,
            newPassword: this.form.newPassword,
          };
          const result = await sysUserupdatePasswordByIdPutRequest(data);
          if (result.code === 0) {
            this.visible = false;
            this.$message.success("密码修改成功");
            this.logout();
          }
        }
      });
    },
  },
};
</script>

<style lang="less">
.header-avatar {
  display: inline-flex;
  .avatar,
  .name {
    align-self: center;
  }
  .avatar {
    margin-right: 8px;
  }
  .name {
    font-weight: 500;
  }
}
.avatar-menu {
  width: 150px;
}
</style>
