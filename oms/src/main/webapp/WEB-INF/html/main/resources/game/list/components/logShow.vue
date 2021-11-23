<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="更新日志" :visible.sync="drawer" ref="drawer">
    <el-radio-group v-model="logData.reverse" size="mini" style="margin: 0 20">
      <el-radio-button :label="true">正序</el-radio-button>
      <el-radio-button :label="false">倒序</el-radio-button>
    </el-radio-group>
    <el-timeline :reverse="logData.reverse" style="margin:20">
      <el-timeline-item v-for="(version, index) in logData.list" :key="index" :timestamp="version.updatedAt" placement="top">
        <el-card class="box-card" style="line-height: 30px;">
          <p>{{version.version}}
            <el-tag size="mini" style="margin-left:10" :type="statusType(version.status)">{{getStatus(version.status)}}</el-tag>
          </p>
          <p>{{version.versionLog}}</p>
          <p>
            <i class="el-icon-user"></i>{{UserName[version.updatedBy]}}
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
      logData: { list: [], reverse: true },
    };
  },
  methods: {

    // 读取对象数据，进行复写
    loadEditData(row) {
      let _this = this
      var url = "/oms/game/version";
      sendRequest(url, { id: row.id }, function (jsonData) {
        console.log(jsonData)
        _this.logData.list = jsonData.data
        _this.logData.id = row.id
        _this.logData.reverse = false
        _this.drawer = true
      })

    },
    getStatus(status) {
      switch (status) {
        case 0:
          {
            return "已过期删除"
          }
        case 1:
          {
            return "可用版本"
          }
        case 2:
          {
            return "被回滚"
          }
      }
    },
    statusType(status) {
      switch (status) {
        case 0:
          {
            return "info"
          }
        case 1:
          {
            return "success"
          }
        case 2:
          {
            return "danger"
          }
      }
    },

  },
};
</script>