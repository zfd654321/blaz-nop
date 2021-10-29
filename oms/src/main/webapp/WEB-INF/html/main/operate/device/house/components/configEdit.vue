<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="设备配置" :visible.sync="drawer" ref="drawer" :before-close="handleClose">
    <el-form :rules="infoData.rules" :model="infoData.row" ref="pcForm" label-width="auto" style="margin:20">
      <el-form-item label="设备编号" prop="deviceId">
        <el-input placeholder="请输入设备编号" v-model.trim="infoData.row.deviceId" readonly></el-input>
      </el-form-item>
      <el-form-item label="kinect左右边距" prop="kinectLeftAndRightDis">
        <el-input-number placeholder="请输入kinect左右边距" v-model.trim="infoData.row.kinectLeftAndRightDis"></el-input-number>
      </el-form-item>
      <el-form-item label="kinect最近距离" prop="kinectMinDis">
        <el-input-number placeholder="请输入kinect最近距离" v-model.trim="infoData.row.kinectMinDis"></el-input-number>
      </el-form-item>
      <el-form-item label="kinect最远距离" prop="kinectMaxDis">
        <el-input-number placeholder="请输入kinect最远距离" v-model.trim="infoData.row.kinectMaxDis"></el-input-number>
      </el-form-item>
      <el-form-item label="API地址" prop="hostUrl">
        <el-input placeholder="请输入API地址" v-model.trim="infoData.row.hostUrl"></el-input>
      </el-form-item>
      <el-form-item label="离线二维码" prop="qrcodeUrl">
        <el-input placeholder="请选择离线二维码" readonly v-model.trim="infoData.row.qrcodeUrl" id="qrcodeUrl">
          <el-button slot="append" icon="el-icon-picture-outline" @click="checkResource(infoData.row.qrcodeUrl,'qrcodeUrl','img')"></el-button>
        </el-input>
      </el-form-item>
      <el-form-item label="大屏Logo地址" prop="logoUrl">
        <el-input placeholder="请选择大屏Logo" readonly v-model.trim="infoData.row.logoUrl" id="logoUrl">
          <el-button slot="append" icon="el-icon-picture-outline" @click="checkResource(infoData.row.logoUrl,'logoUrl','img')"></el-button>
        </el-input>
      </el-form-item>
      <el-form-item label="照片水印地址" prop="watermarkUrl">
        <el-input placeholder="请选择照片水印" readonly v-model.trim="infoData.row.watermarkUrl" id="watermarkUrl">
          <el-button slot="append" icon="el-icon-picture-outline" @click="checkResource(infoData.row.watermarkUrl,'watermarkUrl','img')"></el-button>
        </el-input>
      </el-form-item>
      <el-form-item label="主题名称" prop="themeName">
        <el-input placeholder="请输入主题名称" v-model.trim="infoData.row.themeName"></el-input>
      </el-form-item>
      <el-form-item label="自动进入游戏时间" prop="gameEnterTime">
        <el-input-number placeholder="请输入自动进入游戏时间" v-model.trim="infoData.row.gameEnterTime"></el-input-number>
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
        rules: {
          deviceId: [
            { required: true, message: '请输入设备编号', trigger: 'blur' },
            { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
            { pattern: /^[a-zA-Z0-9_]+$/, message: '编号可使用英文、数字和下划线', trigger: 'blur' }
          ],
          pcId: [
            { required: true, message: '请选择机器码', trigger: 'change' }
          ],
          name: [
            { required: true, message: '请输入设备名称', trigger: 'blur' },
            { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
          ],
          remarks: [
            { required: true, message: '请输入设备备注', trigger: 'blur' },
          ],
          address: [
            { required: true, message: '请输入地址', trigger: 'blur' },
          ],
          outDate: [
            { required: true, message: '请填写授权日期', trigger: 'blur' },
          ],

        },
      },
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
    // 唤起资源读取方法
    checkResource(url, element, type) {
      resourceVue.loadResources(url, type, function (item) {
        $("#" + element).val(item.url);
        $("#" + element)[0].dispatchEvent(new Event('input'))
      })
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let formName = "pcForm"
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var url = "/oms/device/saveDeviceConfig";
          var params = _this.infoData.row;

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