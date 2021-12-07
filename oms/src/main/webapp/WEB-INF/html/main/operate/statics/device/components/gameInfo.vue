<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="设备日志" :visible.sync="drawer" ref="drawer">
    <el-radio-group v-model="infoData.reverse" size="mini" style="margin: 0 20">
      <el-radio-button :label="true">正序</el-radio-button>
      <el-radio-button :label="false">倒序</el-radio-button>
    </el-radio-group>
    <el-timeline :reverse="infoData.reverse" style="margin:20">
      <el-timeline-item v-for="(item, index) in infoData.loglist" :key="index" :timestamp="item.createdAt" placement="top">
        <el-card class="box-card" style="line-height: 30px;">
          <p>{{item.operate}}</p>
          <el-popover v-if="item.info!=''" placement="top-start" title="详细" width="400" trigger="hover">
            <pre v-html='item.infoStr' style="white-space: pre-wrap; word-wrap: break-word;"></pre>
            <el-button slot="reference" size="mini">详细</el-button>
          </el-popover>
          <p>
            <i class="el-icon-user"></i>{{getUser(item.createdBy)}}
          </p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
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
          pcId: '',
          name: '',
        },
        loglist: [],
        reverse: false
      },
    }
  },
  methods: {
    // 读取对象数据，进行复写
    loadEditData(row) {
      let _this = this;
      var url = "/oms/device/loglist";
      sendRequest(url, { deviceId: row.deviceId }, function (jsonData) {
        var list = jsonData.data
        list.forEach(element => {
          element.createdName = UserName[element.createdBy]
          if (element.info != "") {
            element.infoStr = JSON.stringify(JSON.parse(element.info), null, '\t');
          } else {
            element.infoStr = ""
          }
        });
        console.log(list)
        _this.infoData.loglist = list
        _this.drawer = true
      })
    },
    getUser(id) {
      return UserName[id]
    },
  },
};
</script>