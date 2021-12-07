<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="设备开关" :visible.sync="drawer" ref="drawer">
    <el-descriptions :column="1" border style="margin:20px">
      <el-descriptions-item label="更新开关">
        <el-switch v-model="infoData.row.software" :active-value="1" :inactive-value="0" @change='changeStatus($event,"software")'></el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="支付开关">
        <el-switch v-model="infoData.row.pay" :active-value="1" :inactive-value="0" @change='changeStatus($event,"pay")'></el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="统计开关">
        <el-switch v-model="infoData.row.statistics" :active-value="1" :inactive-value="0" @change='changeStatus($event,"statistics")'></el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="联网校验开关">
        <el-switch v-model="infoData.row.onlinecheck" :active-value="1" :inactive-value="0" @change='changeStatus($event,"onlinecheck")'></el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="文件比对开关">
        <el-switch v-model="infoData.row.filecheck" :active-value="1" :inactive-value="0" @change='changeStatus($event,"filecheck")'></el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="录屏功能开关">
        <el-switch v-model="infoData.row.video" :active-value="1" :inactive-value="0" @change='changeStatus($event,"video")'></el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="抠像功能开关">
        <el-switch v-model="infoData.row.clip" :active-value="1" :inactive-value="0" @change='changeStatus($event,"clip")'></el-switch>
      </el-descriptions-item>
    </el-descriptions>
  </el-drawer>
</template>
 
<script>
module.exports = {
  data: function () {
    return {
      drawer: false,
      infoData: {
        row: {
          deviceId: '',
          software: 1,
          pay: 0,
          statistics: 1,
          onlinecheck: 0,
          filecheck: 0,
          video: 0,
          clip: 0

        }
      }

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

      var url = "/oms/switch/getById";
      sendRequest(url, { deviceId: id }, function (jsonData) {
        console.log(jsonData)
        _this.drawer = true;
        _this.infoData.row = jsonData.data
      })

    },
    changeStatus(value, type) {
      let _this = this
      var url = "/oms/switch/save";
      var params = {
        deviceId: _this.infoData.row.deviceId,
        type: type,
        value: value
      };
      sendRequest(url, params, function (jsonData) {
        if (jsonData.isSuccess) {
          _this.$message.success("修改设备开关状态成功");
        } else {
          _this.$message.error(jsonData.message);
        }
      })
    }

  },

};
</script>