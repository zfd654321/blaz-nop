<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="设备日志" :visible.sync="drawer" ref="drawer">
    <el-radio-group v-model="infoData.reverse" size="mini" style="margin: 0 20">
      <el-radio-button :label="true">正序</el-radio-button>
      <el-radio-button :label="false">倒序</el-radio-button>
    </el-radio-group>

    <el-select v-model="infoData.type">
      <el-option :key="-1" label="全部" :value="-1"></el-option>
      <el-option :key="1" label="设备流程操作" :value="1"></el-option>
      <el-option :key="2" label="设备信息修改" :value="2"></el-option>
      <el-option :key="3" label="设备配置修改" :value="3"></el-option>
      <el-option :key="4" label="设备游戏修改" :value="4"></el-option>
      <el-option :key="5" label="设备广告修改" :value="5"></el-option>
      <el-option :key="6" label="设备开关操作" :value="6"></el-option>
      <el-option :key="7" label="设备平移操作" :value="7"></el-option>
    </el-select>
    <el-timeline :reverse="infoData.reverse" style="margin:20">
      <template v-for="(item, index) in infoData.loglist">
        <el-timeline-item v-show=" infoData.type == -1 || infoData.type==item.type" :key="index" :timestamp="item.createdAt" placement="top">
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
      </template>
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
        reverse: false,
        type: -1
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
        _this.infoData.type = -1

        _this.infoData.reverse = false
      })
    },
    getUser(id) {
      return UserName[id]
    },
  },
};
</script>