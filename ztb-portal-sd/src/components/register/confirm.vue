<template>
  <a-card class="result-error" :bordered="false">
    <a-descriptions title="登记信息">
      <a-descriptions-item label="注册地"> 注册地 </a-descriptions-item>
      <a-descriptions-item label="注册地区"> 注册地区 </a-descriptions-item>
      <a-descriptions-item label="注册地址"> 注册地址 </a-descriptions-item>
      <a-descriptions-item label="企业名称"> 企业名称 </a-descriptions-item>
      <a-descriptions-item label="企业性质"> 企业性质 </a-descriptions-item>
      <a-descriptions-item label="企业名称"> 企业名称 </a-descriptions-item>
      <a-descriptions-item label="生产经营地"> 生产经营地 </a-descriptions-item>
      <a-descriptions-item label="三级信息"> 三级信息 </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="联系人信息">
      <a-descriptions-item label="姓名"> 张三 </a-descriptions-item>
      <a-descriptions-item label="职位"> 总经理 </a-descriptions-item>
      <a-descriptions-item label="手机号码"> 13013311133 </a-descriptions-item>
      <a-descriptions-item label="证件类型"> 身份证 </a-descriptions-item>
      <a-descriptions-item label="证件类型"> 身份证 </a-descriptions-item>
      <a-descriptions-item label="证件号码">
        3404949493493
      </a-descriptions-item>
      <a-descriptions-item label="正面照片"> 正面照片 </a-descriptions-item>
      <a-descriptions-item label="背面照片"> 背面照片 </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="业务信息">
      <a-descriptions-item label="商业模式"> 商业模式 </a-descriptions-item>
      <a-descriptions-item label="代理品牌"> 代理品牌 </a-descriptions-item>
      <a-descriptions-item label="供货品备注"> 供货品备注 </a-descriptions-item>
      <a-descriptions-item label="主要客户"> 主要客户 </a-descriptions-item>
      <a-descriptions-item label="主营类型"> 主营类型 </a-descriptions-item>
      <a-descriptions-item label="厂房性质"> 厂房性质 </a-descriptions-item>
      <a-descriptions-item label="厂房面积"> 厂房面积 </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="许可证信息">
      <a-descriptions-item label="许可证名称"> 许可证名称 </a-descriptions-item>

      <a-descriptions-item label="许可证到期日">
        许可证到期日
      </a-descriptions-item>
      <a-descriptions-item label="文件到期日"> 文件到期日 </a-descriptions-item>
    </a-descriptions>

    <div style="text-align: center; margin-top: 20px">
      <a-button @click="upClick" type="primary"> 上一步 </a-button>
      <a-button @click="nextClick" style="margin-left: 8px"> 下一步 </a-button>
    </div>
  </a-card>
</template>

<script>
export default {
  name: "confirm",
  data() {
    return {
      checked: false,
    };
  },
  methods: {
    upClick() {
      this.$emit("on-confirm-up");
    },
    nextClick() {
      this.$emit("on-confirm-next");
    },
  },
};
</script>

<style scoped lang="less">
.result-error {
  text-align: left;
  .error-icon {
    color: @red-6;
    margin-right: 8px;
  }
}
</style>
