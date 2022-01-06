<template>
  <div ref="dom"></div>
</template>

<script>
import * as echarts from "echarts";
import { on, off } from "@/utils/validate";
export default {
  name: "UseChart",
  props: ["dataList"],
  data() {
    return {
      dom: null,
      dataLists: "",
    };
  },
  computed() {
    off(window, "resize", this.resize);
  },
  watch: {
    dataList(newVal, oldVal) {
      if (newVal) {
        this.dataLists = newVal;
        this.init();
      }
    },
  },
  mounted() {
    this.init();
  },
  methods: {
    resize() {
      this.dom.resize();
    },
    init() {
      const option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // Use axis to trigger tooltip
            type: "shadow", // 'shadow' as default; can also be 'line' or 'shadow'
          },
        },

        legend: {
          // selectedMode: false,
          data: [
            "在手库存",
            "危险警报库存",
            "临界已经库存",
            "正常库存",
            "过量库存",
            "超量库存",
            "呆滞库存",
            "过期库存",
          ],
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "value",
        },
        yAxis: {
          type: "category",
          // data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        series: [
          {
            name: "危险警报库存",
            type: "bar",
            stack: "total",
            color: "#FF0000",
            label: {
              show: true,
            },
            emphasis: {
              focus: "series",
            },
            data: [this.dataLists.minStock],
          },
          {
            name: "临界已经库存",
            type: "bar",
            stack: "total",
            color: "#FFFF00",
            label: {
              show: true,
            },
            emphasis: {
              focus: "series",
            },
            data: [this.dataLists.reorderPoint],
          },
          {
            name: "正常库存",
            type: "bar",
            stack: "total",
            color: "#00B050",
            label: {
              show: true,
            },
            emphasis: {
              focus: "series",
            },
            data: [this.dataLists.pausePoint],
          },
          {
            name: "过量库存",
            type: "bar",
            stack: "total",
            color: "#00B0F0",
            label: {
              show: true,
            },
            markPoint: {
              data: [{ name: "年最高", value: 18002.2, xAxis: 7, yAxis: 183 }],
            },
            emphasis: {
              focus: "series",
            },
            data: [this.dataLists.maxStock],
          },
          {
            name: "超量库存",
            type: "bar",
            stack: "total",
            color: "#4472C4",
            label: {
              show: true,
            },
            emphasis: {
              focus: "series",
            },
            data: [this.dataLists.percentE],
          },
          {
            name: "呆滞库存",
            type: "bar",
            stack: "total",
            color: "#7030A0",
            label: {
              show: true,
            },
            emphasis: {
              focus: "series",
            },
            data: [this.dataLists.percentF],
          },
          {
            name: "过期库存",
            type: "bar",
            stack: "total",
            color: "#000000",
            label: {
              show: true,
            },
            emphasis: {
              focus: "series",
            },
            data: [this.dataLists.percentE],
          },
          {
            name: "在手库存",
            symbolSize: 18,
            data: [this.dataLists.onHand],
            type: "effectScatter",
          
            itemStyle: {
            normal: {
                label: {
                    color: '#7B38F8',
                    show: true,
                    position: 'top',
                    formatter: function (params) {
                        var s ='在手库存：' + params.value;
                        return s
                    },
                },
            },
          },
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

  beforeDestroy() {
    off(window, "resize", this.resize);
  },
};
</script>
