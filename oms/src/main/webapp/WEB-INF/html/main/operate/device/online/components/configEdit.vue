<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="设备配置" :visible.sync="drawer" ref="drawer">
    <el-descriptions :column="1" border style="margin:20px">
      <el-descriptions-item label="设备编号">{{infoData.row.deviceId}}</el-descriptions-item>
      <el-descriptions-item label="kinect左右边距">{{infoData.row.kinectLeftAndRightDis}}</el-descriptions-item>
      <el-descriptions-item label="kinect最近距离">{{infoData.row.kinectMinDis}}</el-descriptions-item>
      <el-descriptions-item label="kinect最远距离">{{infoData.row.kinectMaxDis}}</el-descriptions-item>
      <el-descriptions-item label="API地址">{{infoData.row.hostUrl}}</el-descriptions-item>
      <el-descriptions-item label="离线二维码"><el-image style="width: 100px; height: 100px" :src="infoData.row.qrcodeUrl" fit="contain"></el-image></el-descriptions-item>
      <el-descriptions-item label="大屏Logo"><el-image style="width: 100px; height: 100px" :src="infoData.row.logoUrl" fit="contain"></el-image></el-descriptions-item>
      <el-descriptions-item label="照片水印"><el-image style="width: 100px; height: 100px" :src="infoData.row.watermarkUrl" fit="contain"></el-image></el-descriptions-item>
      <el-descriptions-item label="主题名称">{{infoData.row.themeName}}</el-descriptions-item>
      <el-descriptions-item label="自动进入游戏时间">{{infoData.row.gameEnterTime}}</el-descriptions-item>

    </el-descriptions>

  </el-drawer>
</template>
 
<script>
module.exports = {
  data: function () {
    return {
      drawer: false,
      loading: false,
      infoData: {
        row: {
          deviceId: '',
          kinectLeftAndRightDis: 0,
          kinectMinDis: 0,
          kinectMaxDis: 0,
          hostUrl: '',
          qrcodeUrl: '',
          logoUrl: '',
          watermarkUrl: '',
          themeName: '',
          gameEnterTime: ''
        },

      },
    };
  },
  methods: {

    // 读取对象数据，进行复写
    loadEditData(id) {
      let _this = this;
      let formName = "pcForm";
      if (_this.$refs[formName]) {
        this.$refs[formName].resetFields();
      }

      var url = "/oms/device/loadDeviceConfig";
      sendRequest(url, { deviceId: id }, function (jsonData) {
        console.log(jsonData)
        _this.drawer = true;
        _this.infoData.row = jsonData.data
      })
    },

  },
};
</script>