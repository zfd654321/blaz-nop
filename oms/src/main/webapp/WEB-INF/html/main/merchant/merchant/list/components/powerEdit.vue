<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="权限配置" :visible.sync="drawer" ref="drawer" size="670">
    <el-collapse v-model="activeName" accordion style="margin:20">
      <el-collapse-item title="设备权限" name="1">
        <el-alert title="配置商家用户的设备权限" description="交付后商家用户可以通过商家后台对授权设备的游戏、广告等进行配置；只有线上状态的商家设备可以被交付给商家用户；同一台设备不可以同时交付给多个商家用户" type="info" show-icon style="margin-top:5"></el-alert>
        <el-transfer id="devicetransfer" style="margin-top:20" filterable v-model="powerList.checkDevices" :data="powerList.deviceList" :titles="['可选设备', '已选设备']" :props="{key: 'deviceId',label: 'name'}" target-order="push"></el-transfer>
        <el-button type="primary" @click="deviceSave" size="mini" style="margin-top:20px">保存</el-button>
      </el-collapse-item>
      <el-collapse-item title="游戏权限" name="2">
        <el-alert title="配置商家用户的可配置游戏权限" description="商家可以将指定游戏配置给授权的设备，并对其2d资源进行自由配置。" type="info" show-icon style="margin-top:5"></el-alert>
        <el-transfer id="gametransfer" style="margin-top:20" filterable v-model="powerList.checkGames" :data="powerList.gameList" :titles="['可选游戏', '已选游戏']" :props="{key: 'id',label: 'name'}" target-order="push"></el-transfer>
        <el-button type="primary" @click="gameSave" size="mini" style="margin-top:20px">保存</el-button>
      </el-collapse-item>
      <el-collapse-item title="广告权限" name="3">
        <el-alert title="配置商家用户的可配置广告模板权限" description="商家可以根据模板自由生成相应的广告，并配置给授权的设备。" type="info" show-icon style="margin-top:5"></el-alert>
        <el-transfer id="adverttransfer" style="margin-top:20" filterable v-model="powerList.checkAdverts" :data="powerList.advertList" :titles="['可选广告模板', '已选广告模板']" :props="{key: 'id',label: 'name'}" target-order="push"></el-transfer>
        <el-button type="primary" @click="advertSave" size="mini" style="margin-top:20px">保存</el-button>
      </el-collapse-item>
      <el-collapse-item title="资源权限" name="4">
        <el-alert title="配置商家用户的上传资源池大小" description="超过资源池大小限制时，将无法上传资源视频；商家上传的资源需经过审核后才可以正常使用。" type="info" show-icon style="margin-top:5"></el-alert>

        <el-slider v-model="powerList.merchantPool.poolSize" :format-tooltip="formatTooltip" :max="1000*1024*1024" style="width:400;margin:20" :marks="marks" :step="10*1024*1024"></el-slider>
        <el-alert :type="powerList.merchantPool.poolSize<powerList.merchantPool.usedSize?'danger':'success'">{{formatProgress()}}</el-alert>
        <el-button type="primary" @click="poolSave" size="mini" style="margin-top:20px">保存</el-button>
      </el-collapse-item>
      <el-collapse-item title="菜单权限" name="5">
        <el-alert title="配置商家用户的可用菜单权限" description="通过调整相应菜单权限，变更商家的可操作功能。" type="info" show-icon style="margin-top:5"></el-alert>
      </el-collapse-item>
    </el-collapse>
  </el-drawer>
</template>
 <style scoped>
.el-transfer-panel {
  width: 230;
}

.el-transfer__buttons {
  padding: 0 10;
}
</style>
<script>
module.exports = {
  data: function () {
    return {
      id: '',
      drawer: false,
      activeName: "1",
      loading: false,
      powerList: {
        deviceList: [],
        checkDevices: [],
        gameList: [],
        checkGames: [],
        advertList: [],
        checkAdverts: [],
        merchantPool: {
          poolSize: 0,
          usedSize: 0
        }
      },
      marks: {
        0: '0',
        104857600: '100',
        209715200: '200M',
        524288000: '500M',
        1048576000: '1000M',
      }
    };
  },
  methods: {

    // 读取对象数据，进行复写
    loadEditData(row) {
      let _this = this;
      _this.drawer = true;
      _this.activeName = "1";
      _this.id = row.id;
      var url = "/oms/merchant/loadPower";
      sendRequest(url, { merchantId: row.id }, function (jsonData) {
        console.log(jsonData)
        _this.drawer = true;
        let deviceList = jsonData.data.deviceList
        deviceList.forEach(deviceEntity => {
          deviceEntity.name = deviceEntity.deviceId + " [" + deviceEntity.name + "]"
        });
        _this.powerList.deviceList = deviceList
        _this.powerList.checkDevices = jsonData.data.checkDevices

        let gameList = jsonData.data.gameList
        gameList.forEach(gameEntity => {
          if (gameEntity.remarks != null && gameEntity.remarks != "") {
            gameEntity.name = gameEntity.name + "  [" + gameEntity.remarks + "] "
          }
        });
        _this.powerList.gameList = gameList
        _this.powerList.checkGames = jsonData.data.checkGames

        let advertList = jsonData.data.advertList
        _this.powerList.advertList = advertList
        _this.powerList.checkAdverts = jsonData.data.checkAdverts



        _this.powerList.merchantPool = jsonData.data.merchantPool
      })
    },

    formatTooltip(val) {
      return (val / (1024 * 1024)).toFixed(2) + "M";
    },

    formatProgress() {
      return "已用：" + (this.powerList.merchantPool.usedSize / (1024 * 1024)).toFixed(2) + "M/可用：" + (this.powerList.merchantPool.poolSize / (1024 * 1024)).toFixed(2) + "M";
    },
    deviceSave() {
      let _this = this;
      var url = "/oms/merchant/deviceSave";
      var params = {
        merchantId: _this.id,
        deviceIds: _this.powerList.checkDevices.toString()
      }
      sendRequest(url, params, function (jsonData) {
        if (jsonData.isSuccess) {
          _this.$message.success("设备权限保存成功");
        } else {
          _this.$message.error(jsonData.message);
        }
      })
    },

    gameSave() {
      let _this = this;
      var url = "/oms/merchant/gameSave";
      var params = {
        merchantId: _this.id,
        gameIds: _this.powerList.checkGames.toString()
      }
      sendRequest(url, params, function (jsonData) {
        if (jsonData.isSuccess) {
          _this.$message.success("游戏权限保存成功");
        } else {
          _this.$message.error(jsonData.message);
        }
      })
    },

    advertSave() {
      let _this = this;
      var url = "/oms/merchant/advertSave";
      var params = {
        merchantId: _this.id,
        advertIds: _this.powerList.checkAdverts.toString()
      }
      sendRequest(url, params, function (jsonData) {
        if (jsonData.isSuccess) {
          _this.$message.success("广告权限保存成功");
        } else {
          _this.$message.error(jsonData.message);
        }
      })
    },

    poolSave() {
      let _this = this;
      var url = "/oms/merchant/poolSave";
      var params = {
        merchantId: _this.id,
        poolSize: _this.powerList.merchantPool.poolSize
      }
      sendRequest(url, params, function (jsonData) {
        if (jsonData.isSuccess) {
          _this.$message.success("广告权限保存成功");
        } else {
          _this.$message.error(jsonData.message);
        }
      })
    },

  },
};
</script>