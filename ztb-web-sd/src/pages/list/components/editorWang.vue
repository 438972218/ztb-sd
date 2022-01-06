<template>
  <div>
    <div ref="editor" style="text-align: left"></div>
  </div>
</template>

<script>
import E from 'wangeditor'
export default {
  props: {
  },
  name: "editorWang",
  components: {},
  data () {
    return {
    }
  },
  mounted () {
    const editor = new E(this.$refs.editor)
    editor.create()
  },
  methods: {
  }
};
</script>

<style lang="less" scoped>
</style>
