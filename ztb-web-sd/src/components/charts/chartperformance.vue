<template>
  <div  ref="dom" ></div>
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
   computed(){
    off(window, "resize", this.resize);
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
            name: '半径模式',
            type: 'pie',
            radius: [20, 140],
            center: ['30%', '60%'],
            roseType: 'radius',
            label: {
                show: false
            },
            emphasis: {
                label: {
                    show: true
                }
            },
            data: [
                {value: 40, name: '数据1'},
                {value: 33, name: '数据2'},
                {value: 28, name: '数据3'},
                {value: 22, name: '数据4'},
                {value: 20, name: '数据5'},
                {value: 15, name: '数据6'},
                {value: 12, name: '数据7'},
                {value: 10, name: '数据8'}
            ]
        }
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
