<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="游戏信息" :visible.sync="drawer" ref="gameDrawer">
    <el-table :data="gameData.list" style="width:540;margin:20" header-cell-class-name="tableheader" size="mini">
      <el-table-column prop="id" label="游戏编号"></el-table-column>
      <el-table-column prop="name" label="游戏名称"></el-table-column>
      <el-table-column prop="version" label="最新版本"></el-table-column>
    </el-table>
  </el-drawer>
</template>
 
<script>
module.exports = {
  data: function () {
    return {
      drawer: false,
      gameData: {
        list: [],
      },

    };
  },
  methods: {
    // 读取对象数据，进行复写
    loadEditData(row) {
      console.log(row)
      let _this = this;
      _this.drawer = true;
      var url = "/oms/device/loadDeviceGame";
      sendRequest(url, { deviceId: row.deviceId, }, function (jsonData) {
        console.log(jsonData)
        _this.gameData.list = jsonData.data
      })
    },

  }
};
</script>