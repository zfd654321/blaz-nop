<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="广告信息" :visible.sync="drawer" ref="advertDrawer">
    <el-table :data="mainData.list" style="width:540;margin:20" header-cell-class-name="tableheader" size="mini">
      <el-table-column prop="id" label="广告编号"></el-table-column>
      <el-table-column prop="name" label="广告名称"></el-table-column>
      <el-table-column prop="remarks" label="广告备注"></el-table-column>
    </el-table>
  </el-drawer>
</template>
 
<script>
module.exports = {
  data: function () {
    return {
      drawer: false,
      loading: false,
      mainData: {
        deviceId: '',
        list: [],
      },

    };
  },
  methods: {
    // 读取对象数据，进行复写
    loadEditData(row) {
      console.log(row)
      let _this = this;
      this.drawer = true;
      var url = "/oms/device/loadDeviceAdvert";
      sendRequest(url, { deviceId: row.deviceId, }, function (jsonData) {
        console.log(jsonData)
        _this.mainData.list = jsonData.data
      })
    },


  },
};
</script>