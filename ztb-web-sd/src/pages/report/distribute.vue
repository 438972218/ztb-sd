<template>
  <div>
    <a-card>
    <div class="content-title">供应商资源生态分布</div>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="供应商资源生态分布">
          <a-row>
            <a-col :span="12" class="chartStyle">
                   <div class="content-title chart-title">地理位置分布</div>
             <chart-location style="height:320px;width: calc(47vw - 124px);"></chart-location>  
            </a-col>
                <a-col :span="12" class="chartStyle1">
                                 <div class="content-title chart-title">货源风险</div>
             <chart-supply style="height:320px;width: calc(47vw - 124px);"></chart-supply>  
            </a-col>
             </a-row>
               <a-row>
            <a-col :span="12" class="chartStyle2">
                             <div class="content-title chart-title">绩效等级分布</div>
             <chart-performance style="height:320px;width: calc(47vw - 124px);"></chart-performance>  
            </a-col>
                <a-col :span="12" class="chartStyle3">
                                 <div class="content-title chart-title">风险等级分布</div>
             <chart-risk style="height:320px;width: calc(47vw - 124px);"></chart-risk>  
            </a-col>
             </a-row>
      </a-tab-pane>
      <a-tab-pane key="2" tab="供应商绩效分析" >
      </a-tab-pane>
    </a-tabs>
    </a-card>
  </div>
</template>

<script>
import { ChartLocation,ChartSupply,ChartPerformance, ChartRisk } from "@/components/charts";

export default {
  name: "Find",
  components: {ChartLocation, ChartSupply, ChartPerformance, ChartRisk},
  data() {
    return {
    };
  },
  methods: {
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
.iconstyle {
  margin: 0 10px;
}

.chartStyle{
    border-right: 1px solid #F8F8FF;
      border-bottom: 1px solid #F8F8FF;
}
.chartStyle1{
    border-left: 1px solid #F8F8FF;
      border-bottom: 1px solid #F8F8FF;
}
.chartStyle2{
    border-right: 1px solid #F8F8FF;
      border-top: 1px solid #F8F8FF;
}
.chartStyle3{
    border-left: 1px solid #F8F8FF;
      border-top: 1px solid #F8F8FF;
}
.chart-title{
    position: absolute;
    top: 20px;
    left: 20px;
}
</style>
