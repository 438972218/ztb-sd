<template>
  <div ref="dom"></div>
</template>

<script>
import * as echarts from "echarts";
import { on, off } from "@/utils/validate";
export default {
  name: "ChartLocation",
  // props: ["dataAll"],
  data() {
    return {
      dom: null,
      // dataAlls: {
      //   investmentamount: null
      // }
    };
  },
  watch: {
    // dataAll(newVal, oldVal) {
    //   if (newVal) {
    //     this.dataAlls = newVal;
    //     this.init();
    //   }
    // }
  },
  methods: {
    resize() {
      this.dom.resize();
    },
    init() {
      const option = {
        tooltip: {
          trigger: "item",
        },
        legend: {
          orient: "horizontal",
          icon: "circle",
          padding: [0, 50, 0, 0],
          x: "60%",
          y: "center",
        },
        series: [
          {
            name: "访问来源",
            type: "pie",
            radius: "60%",
            center: ["30%", "60%"],
            label: {
              show: false,
            },
            emphasis: {
              label: {
                show: true,
              },
            },
            data: [
              { value: 1048, name: "无锡" },
              { value: 735, name: "常州" },
              { value: 580, name: "南京" },
              { value: 484, name: "广州" },
              { value: 300, name: "苏州" },
              { value: 300, name: "浙江" },
              { value: 300, name: "上海" },
              { value: 300, name: "深圳" },
            ],
          },
        ],
      };

      this.$nextTick(() => {
        this.dom = echarts.init(this.$refs.dom);
        this.dom.setOption(option);
        on(window, "resize", this.resize);
      });
    },
  },
  mounted() {
    this.init();
  },
  beforeDestroy() {
    off(window, "resize", this.resize);
  },
};
</script>
