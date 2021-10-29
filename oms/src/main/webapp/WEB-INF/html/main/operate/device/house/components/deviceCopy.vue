<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="配置平移" :visible.sync="drawer" ref="drawer" :before-close="handleClose">
    <el-form :rules="copyData.rules" :model="copyData.row" ref="copyForm" label-width="auto" style="margin:20">
      <el-form-item label="移出配置设备编号">{{copyData.row.deviceId}}</el-form-item>
      <el-form-item label="移入配置设备编号" prop="checkDevices">
        <el-select v-model="copyData.row.checkDevices" filterable multiple placeholder="请选择移入设备（可多选）" style="width: 100%;">
          <el-option v-for="item in devicesSet()" :key="item.deviceId" :label="'【 '+item.deviceId+' 】 '+item.name" :value="item.deviceId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="平移项目" prop="checkType">
        <el-checkbox-group v-model="copyData.row.checkType">
          <el-checkbox-button label="1" key="1">配置</el-checkbox-button>
          <el-checkbox-button label="2" key="2">开关</el-checkbox-button>
          <el-checkbox-button label="3" key="3">广告</el-checkbox-button>
          <el-checkbox-button label="4" key="4">游戏</el-checkbox-button>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>
 
<script>
module.exports = {
  data: function () {
    return {
      drawer: false,
      loading: false,

      copyData: {
        row: {
          deviceId: "",
          checkDevices: [],
          checkType: []
        },
        rules: {
          checkDevices: [
            { type: 'array', required: true, message: '请至少选择一台设备', trigger: 'change' },
          ],
          checkType: [
            { type: 'array', required: true, message: '请至少选择一项平移项目', trigger: 'change' },
          ],
        }
      },
      copyDeviceList: [],
    };
  },
  methods: {
    // 关闭窗口确认
    handleClose(done) {
      this.$confirm("确认关闭(未保存的内容将会丢失)？")
        .then((_) => {
          done();
        })
        .catch((_) => { });
    },
    devicesSet() {
      let _this=this
      let list = _this.copyDeviceList
      return list.filter(function (item) {
        return item.deviceId!=_this.copyData.row.deviceId
      })
    },
    // 读取对象数据，进行复写
    loadEditData(row) {
      console.log(row)
      let _this = this;
      _this.copyData.row.deviceId = row.deviceId
      this.copyData.row.checkDevices = []
      this.copyData.row.checkType = ["1", "2", "3", "4"]
      let formName = "copyForm"
      if (this.$refs[formName]) {
        this.$refs[formName].resetFields();
      }
      var copyParams = {
        screen: row.screen,
        status: 2,
        pageSize: 999,
        pageNo: 1
      }
      var url = "/oms/device/list";
      sendRequest(url, copyParams, function (jsonData) {
        console.log(jsonData)
        _this.copyDeviceList = jsonData.data.list
      })
      this.drawer = true;
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let formName = "copyForm"
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var url = "/oms/device/deviceCopy";
          var params = _this.copyData.row;

          sendRequest(url, params, function (jsonData) {
            if (jsonData.isSuccess) {
              _this.$message.success("保存成功");
              _this.drawer = false;
            } else {
              _this.$message.error(jsonData.message);
            }
          })
        }
      })
    },
  },
};
</script>