<template>
<div>
  <a-card class="card-top">
    <div>
      <a-form layout="horizontal">
         <div>
          <a-row >
          <a-col :md="8" :sm="24" >
            <a-form-item
              label="供应商名称"
              :labelCol="{span: 5}"
              :wrapperCol="{span: 18, offset: 0}"
            >
              <a-input placeholder="请输入" />
            </a-form-item>
          </a-col>
           <a-col :md="8" :sm="24" >
            <a-form-item
              label="注册日期"
              :labelCol="{span: 5}"
              :wrapperCol="{span: 18, offset: 0}"
            >
              <a-date-picker style="width: 100%" placeholder="请输入更新日期" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
                  <span style="float: right; margin-top: 3px;">
          <a-button type="primary">查询</a-button>
          <a-button style="margin-left: 8px">重置</a-button>
        </span>
          </a-col>
        </a-row>
        <a-row>
             <a-radio-group default-value="a">
          <a-radio-button value="a">
            注册供应商
          </a-radio-button>
          <a-radio-button value="b">
            潜在供应商
          </a-radio-button>
          <a-radio-button value="c">
            作废供应商
          </a-radio-button>
      </a-radio-group>
        </a-row>
          <!-- <a-row v-if="advanced">
         
          <a-col :md="8" :sm="24" >
            <a-form-item
              label="使用状态"
              :labelCol="{span: 5}"
              :wrapperCol="{span: 18, offset: 1}"
            >
              <a-select placeholder="请选择">
                <a-select-option value="1">关闭</a-select-option>
                <a-select-option value="2">运行中</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24" >
            <a-form-item
              label="描述"
              :labelCol="{span: 5}"
              :wrapperCol="{span: 18, offset: 1}"
            >
              <a-input placeholder="请输入" />
            </a-form-item>
          </a-col>
        </a-row> -->
        </div>
        <!-- <span style="float: right; margin-top: 3px;">
          <a-button type="primary">查询</a-button>
          <a-button style="margin-left: 8px">重置</a-button>
          <a @click="toggleAdvanced" style="margin-left: 8px">
            {{advanced ? '收起' : '展开'}}
            <a-icon :type="advanced ? 'up' : 'down'" />
          </a>
        </span> -->
      </a-form>
    </div>
  </a-card>
  <a-card>
    <div>
      <!-- <div class="operator">
        <a-button @click="addNew" type="primary">新建</a-button>
        <a-button >批量操作</a-button>
        <a-dropdown>
          <a-menu @click="handleMenuClick" slot="overlay">
            <a-menu-item key="delete">删除</a-menu-item>
            <a-menu-item key="audit">审批</a-menu-item>
          </a-menu>
          <a-button>
            更多操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </div> -->
      <a-table
        :scroll="{x: 1300}"
        :columns="columns"
            :rowKey="record=>record.key"
        :dataSource="dataSource"
        :selectedRows.sync="selectedRows"
        @clear="onClear"
        @change="onChange"
        @selectedRowChange="onSelectChange"
      >
                 <template slot="join">
            <div class="join-style">
                <span style="color:red"  @click.capture="c_join('1')">同意</span>/<span @click.capture="c_join('2')">拒绝</span>
            </div>
        </template>
            <template slot="action">
                <a-icon  title="详情"  class="iconstyle" type="profile" />
            <a-icon type="delete" title="删除" @click="c_delete" class="iconstyle" />
        </template>
      </a-table>
    </div>
  </a-card>
</div>
</template>

<script>
const columns = [
  {
    title: '邮箱',
    width: 200,
    key: '1',
    dataIndex: 'a1'
  },
  {
    title: '企业名称',
    dataIndex: 'a2',
        key: '2',
        width: 150,
  },
  {
    title: '简介',
        key: '3',
    dataIndex: 'a3',
        width: 80,
  },
  {
    title: '企业规模',
    dataIndex: 'a4',
        width: 150,
            key: '4'
  },
   {
    title: '企业集团',
    dataIndex: 'a5',
        key: '5',
        width: 150,
  },
    {
    title: '营业执照注册号',
    dataIndex: 'a6',
        key: '6',
        width: 150,
  },
    {
    title: '营业期限',
        width: 150,
            key: '7',
    dataIndex: 'a7',
  },
    {
    title: '长期',
        key: '8',
        width: 150,
    dataIndex: 'a8',
  },
      {
    title: '法定代表',
    dataIndex: 'a9',
        key: '9',
        width: 150,
  },
  {
    title: '审批',
      align:'center',
        width: 200,
            key: '10',
          fixed: 'right',
    scopedSlots: { customRender: 'join' }
  },
    {
    title: '操作',
    width: 150,
        key: '11',
    align:'center',
    fixed: 'right',
    scopedSlots: { customRender: 'action' }
  }
]

const dataSource = [
         {
            a1: 'guohua.liu@opple.com',
            a2: "国华科技有限公司",
            a3: "无",
            a4: "无集团代码",
            a5: "",
            a6: "310123112239765",
            a7: "2021-08-01",
            a8: true,
            a9: "刘国华",
            a10:'join'
          },
          {
            a1: 'wangjiangwen@123.com',
            a2: "上海合力叉车",
            a3: "无",
            a4: "无集团代码",
            a5: "",
            a6: "310109703481154",
            a7: "2026-08-01",
            a8: false,
            a9: "陈意义",
            a10:'join'
          },
                {
            a1: 'wangjiangwen@opple.com',
            a2: "合力",
            a3: "无",
            a4: "无集团代码",
            a5: "",
            a6: "310109703481154",
            a7: "2023-08-01",
            a8: false,
            a9: "陈意义",
            a10:'join'
          },
             {
            a1: '810465621@qq.com',
            a2: "供应商1",
            a3: "无",
            a4: "无集团代码",
            a5: "",
            a6: "12343234654821",
            a7: "2023-08-01",
            a8: false,
            a9: "陈俊",
            a10:'join'
          },
]
export default {
  name: 'approval',
  data () {
    return {
      advanced: true,
      columns: columns,
      dataSource: dataSource,
      selectedRows: []
    }
  },
  methods: {
            c_delete(){
       this.$confirm({
        title: '警告',
        content: '你确定要删除吗？',
        centered: true,
        onOk() {
          // return new Promise((resolve, reject) => {
          //   setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
          // }).catch(() => console.log('Oops errors!'));
        },
        onCancel() {},
      });
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    remove () {
      this.dataSource = this.dataSource.filter(item => this.selectedRows.findIndex(row => row.key === item.key) === -1)
      this.selectedRows = []
    },
    onClear() {
      this.$message.info('您清空了勾选的所有行')
    },
    onStatusTitleClick() {
      this.$message.info('你点击了状态栏表头')
    },
    onChange() {
      this.$message.info('表格状态改变了')
    },
    onSelectChange() {
      this.$message.info('选中行改变了')
    },
    addNew () {
      this.dataSource.unshift({
        key: this.dataSource.length,
        no: 'NO ' + this.dataSource.length,
        description: '这是一段描述',
        callNo: Math.floor(Math.random() * 1000),
        status: Math.floor(Math.random() * 10) % 4,
        updatedAt: '2018-07-26'
      })
    },
    handleMenuClick (e) {
      if (e.key === 'delete') {
        this.remove()
      }
    },
         c_join(num){
            if(num === '1'){
                  this.$confirm({
                    title: '警告',
                    content: '确定审批通过吗？',
                    centered: true,
                    onOk() {
                    },
                    onCancel() {},
                })
            }else{
                   this.$confirm({
                    title: '警告',
                    content: '确定要拒绝吗？',
                    centered: true,
                    onOk() {
                    },
                    onCancel() {},
                })
            }
      },
  }
}
</script>

<style lang="less" scoped>
  .search{
    margin-bottom: 54px;
  }
  .fold{
    width: calc(100% - 216px);
    display: inline-block
  }
  @media screen and (max-width: 900px) {
    .fold {
      width: 100%;
    }
  }
  .join-style{
    span{
    cursor: pointer;
    margin: 10px;
    }
}
 .iconstyle{
   cursor: pointer;
   font-size: 16px; 
   margin: 0 10px;
  }
</style>
