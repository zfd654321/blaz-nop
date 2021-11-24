<template>
  <!-- 新增/编辑模块 -->
  <el-dialog title="预览" :visible.sync="showData.visible" width="30%">
    <template v-if="showData.row.type=='img'">
      <el-image style="width: 100%; height: 30%; display: block;" fit="contain" :src="showData.row.url" />
    </template>
    <template v-else-if="showData.row.type=='video'">
      <video style="max-width: 100%; max-height: 100%" fit="contain" :src="showData.row.url" controls></video>
    </template>
    <template v-else>
      <el-button type="primary" @click="downFile(showData.row.url)">下载<i class="el-icon-upload el-icon--right"></i></el-button>
    </template>
  </el-dialog>
</template>
 
<script>
module.exports = {
  data: function () {
    return {
      showData: {
        row: {
          url: "",
          type: ""
        },
        visible: false
      },
    }
  },
  methods: {
    // 读取对象数据，进行复写
    loadEditData(row) {
      let _this = this;
      this.showData.row.url = row.url
      this.showData.row.type = row.type
      this.showData.visible = true
    },
  },
};
</script>