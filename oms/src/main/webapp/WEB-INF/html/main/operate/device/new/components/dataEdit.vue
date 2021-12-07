<template>
  <!-- 新增/编辑模块 -->
  <el-drawer :title="infoData.row.edit?'设备修改':'设备新增'" :visible.sync="drawer" ref="drawer" :before-close="handleClose">
    <el-form :rules="infoData.rules" :model="infoData.row" ref="pcForm" label-width="80px" style="margin:20">
      <el-form-item label="设备编号" prop="deviceId">
        <el-input placeholder="请输入设备编号" v-model.trim="infoData.row.deviceId" :readonly="infoData.row.edit"></el-input>
      </el-form-item>
      <el-form-item label="机器码" prop="pcId">
        <el-select v-model="infoData.row.pcId" filterable placeholder="请选择机器码" style="width: 100%;">
          <el-option v-for="item in mainData.freePc" :key="item.id" :label="item.id" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称" prop="name">
        <el-input placeholder="请输入设备名称" v-model.trim="infoData.row.name"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input placeholder="请输入备注" v-model.trim="infoData.row.remarks"></el-input>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-cascader :options="CityInfo" v-model="mainData.cityform.selectedOptions" :filterable="true" @change="handleChange"></el-cascader>
      </el-form-item>
      <el-form-item label="设备类型" prop="type">
        <el-select v-model="infoData.row.type" filterable placeholder="请选择设备类型">
          <el-option label="自营" :value="1"></el-option>
          <el-option label="商家" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="屏幕版本" prop="screen">
        <el-select v-model="infoData.row.screen" filterable placeholder="请选择屏幕版本">
          <el-option label="竖屏" :value="1"></el-option>
          <el-option label="横屏" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="摄像头" prop="camera">
        <el-select v-model="infoData.row.camera" filterable placeholder="请选择摄像头版本">
          <el-option label="Kinect2.0" value="Kinect2.0"></el-option>
          <el-option label="Kinect4.0" value="Kinect4.0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="授权日期" prop="outDate">
        <el-date-picker :disabled="infoData.row.neverout==1" v-model="infoData.row.outDate" type="date" placeholder="选择授权日期" value-format="yyyy-MM-dd" :picker-options="infoData.pickerOptions"></el-date-picker>
        <el-switch v-model="infoData.row.neverout" :active-value="1" :inactive-value="0" active-text="永久授权" @change="forevercheck"></el-switch>
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
          edit: false,
          deviceId: '',
          pcId: '',
          name: '',
          remarks: '',
          address: '',
          type: 1,
          screen: 1,
          camera: "Kinect2.0",
          outDate: "",
          neverout: 0
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
      mainData: {
        cityform: {
          city: '',
          erae: '',
          minerae: '',
          selectedOptions: [], //地区筛选数组
        },
        freePc: []
      },

    };
  },
  created: function () {
    this.loadFreePcList();
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
    //获取空闲PC列表
    loadFreePcList() {
      let _this = this;
      var url = "/oms/device/freePcList";
      sendRequest(url, _this.formInline, function (jsonData) {
        console.log(jsonData)
        _this.mainData.freePc = jsonData.data
      })
    },

    // 读取对象数据，进行复写
    loadEditData(row) {
      let _this = this;
      console.log(row);
      let formName = "pcForm";
      if (_this.$refs[formName]) {
        this.$refs[formName].resetFields();
      }

      this.drawer = true;
      if (row != null) {
        this.infoData.row.edit = true
        this.infoData.row.deviceId = row.deviceId
        this.infoData.row.pcId = row.pcId
        this.infoData.row.name = row.name
        this.infoData.row.remarks = row.remarks
        this.infoData.row.address = row.address
        var cityList = row.address.split(",")
        if (cityList.length == 3) {
          this.mainData.cityform.selectedOptions = [parseInt(cityList[0]), parseInt(cityList[1]), parseInt(cityList[2])]
        } else {
          this.mainData.cityform.selectedOptions = [19, 199, 1770]
        }
        this.infoData.row.type = row.type
        this.infoData.row.screen = row.screen
        this.infoData.row.camera = row.camera
        this.infoData.row.outDate = row.outDate
        this.infoData.row.neverout = row.neverout
      } else {
        this.drawer = true;
        this.infoData.row.edit = false
        this.infoData.row.deviceId = ''
        this.infoData.row.pcId = ''
        this.infoData.row.name = ''
        this.infoData.row.remarks = ''
        this.infoData.row.address = '19, 199, 1770'
        this.mainData.cityform.selectedOptions = [19, 199, 1770]
        this.infoData.row.type = 1
        this.infoData.row.screen = 1
        this.infoData.row.camera = "Kinect2.0"
        this.infoData.row.outDate = ''
        this.infoData.row.neverout = 0
      }
    },

    handleChange(value) {
      this.infoData.row.address = this.mainData.cityform.selectedOptions.toString()
    },
    forevercheck(val){
      console.log(val)
      if(val==1 && this.infoData.row.outDate==""){
        this.infoData.row.outDate="2099-01-01"
      }
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let formName = "pcForm"
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var url = "/oms/device/save";
          var params = _this.infoData.row;

          sendRequest(url, params, function (jsonData) {
            if (jsonData.isSuccess) {
              _this.$message.success("保存成功");
              _this.drawer = false;
              _this.loadFreePcList();
              _this.$parent.$refs.dataList.loadDataList();
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