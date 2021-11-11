<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="设备信息" :visible.sync="drawer" ref="drawer">
    <el-descriptions :column="1" border style="margin:20px">
      <el-descriptions-item label="设备编号">{{infoData.row.deviceId}}</el-descriptions-item>
      <el-descriptions-item label="机器码">{{infoData.row.pcId}}</el-descriptions-item>
      <el-descriptions-item label="设备名称">{{infoData.row.name}}</el-descriptions-item>
      <el-descriptions-item label="备注">{{infoData.row.remarks}}</el-descriptions-item>
      <el-descriptions-item label="地址">{{infoData.city[0] | myAddressCity}}/{{infoData.city[1] | myAddressErae}}/{{infoData.city[2] | myAddressMinerae}}</el-descriptions-item>
      <el-descriptions-item label="设备类型">
        <el-tag size="mini">{{checkType(infoData.row)}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="屏幕类型">
        <el-tag size="mini">{{checkScreen(infoData.row)}}</el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="摄像头版本">
        <el-tag size="mini">{{infoData.row.camera}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="授权日期">{{infoData.row.outDate}}</el-descriptions-item>
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
          edit: false,
          deviceId: '',
          pcId: '',
          name: '',
          remarks: '',
          address: '',
          type: 1,
          screen: 1,
          camera: "Kinect2.0"
        },
        city: [0, 0, 0]
      }

    };
  },
  methods: {
    // 读取对象数据，进行复写
    loadEditData(row) {
      let _this = this;
      console.log(row);
      this.drawer = true;
      this.infoData.row = row
      var cityList = row.address.split(",")
      this.infoData.city = [parseInt(cityList[0]), parseInt(cityList[1]), parseInt(cityList[2])]
    },
    checkType(row, column) {
      switch (row.type) {
        case 1:
          return "自营"
        case 2:
          return "商家"

        default:
          return "未知"
      }
    },
    checkScreen(row, column) {
      switch (row.screen) {
        case 1:
          return "竖屏"
        case 2:
          return "横屏"

        default:
          return "未知"
      }
    },

  },
  filters: {
    myAddressCity: function (value) {
      for (y in this.CityInfo) {
        if (this.CityInfo[y].value == value) {
          return value = this.CityInfo[y].label
        }
      }
    },
    myAddressErae: function (value) {
      for (y in this.CityInfo) {
        for (z in this.CityInfo[y].children) {
          if (this.CityInfo[y].children[z].value == value && value != undefined) {
            return value = this.CityInfo[y].children[z].label;
          }
        }
      }
    },
    myAddressMinerae: function (value) {
      for (y in this.CityInfo) {
        for (z in this.CityInfo[y].children) {
          for (i in this.CityInfo[y].children[z].children) {
            if (this.CityInfo[y].children[z].children[i].value == value && value != undefined) {
              return value = this.CityInfo[y].children[z].children[i].label
            }
          }
        }
      }
    },
  },
};
</script>