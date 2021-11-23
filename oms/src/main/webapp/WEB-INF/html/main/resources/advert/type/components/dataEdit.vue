<template>
  <!-- 新增/编辑模块 -->
  <el-drawer :title="infoData.row.edit?'模板修改':'模板新增'" :visible.sync="drawer" ref="drawer" :before-close="handleClose">
    <el-form :model="infoData.row" :rules="infoData.rules" ref="pcForm" label-width="80px" style="margin:20">
      <el-form-item label="模板名称" prop="name">
        <el-input placeholder="请输入模板名称" v-model.trim="infoData.row.name"></el-input>
      </el-form-item>
      <el-form-item label="模板备注" prop="remarks">
        <el-input placeholder="请输入模板备注" v-model.trim="infoData.row.remarks"></el-input>
      </el-form-item>
      <el-form-item label="屏幕版本" prop="screen">
        <el-select v-model="infoData.row.screen" filterable placeholder="请选择屏幕版本">
          <el-option label="竖屏" :value="1"></el-option>
          <el-option label="横屏" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="模型路径" prop="bundleName">
        <el-input placeholder="请输入模型文件路径" v-model.trim="infoData.row.bundleName"></el-input>
      </el-form-item>
      <el-form-item label="模型名称" prop="assetName">
        <el-input placeholder="请输入模型文件名称" v-model.trim="infoData.row.assetName"></el-input>
      </el-form-item>
      <el-form-item label="模板时长" prop="time">
        <el-input-number placeholder="请输入模板时长" v-model.trim="infoData.row.time"></el-input-number>
      </el-form-item>
      <el-form-item label="模板类别" prop="type">
        <el-select v-model="infoData.row.type" filterable placeholder="请选择模板类别">
          <el-option label="无额外资源" :value="0"></el-option>
          <el-option label="图片轮播" :value="1"></el-option>
          <el-option label="视频" :value="2"></el-option>
        </el-select>
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
          id: '',
          name: '',
          remarks: '',
          screen: '',
          bundleName: '',
          assetName: '',
          time: '',
          type: '',
          edit: false
        },
        rules: {
          name: [
            { required: true, message: '请输入模板名称', trigger: 'blur' }
          ],
          remarks: [
            { required: true, message: '请输入模板备注', trigger: 'blur' }
          ],
          screen: [
            { required: true, message: '请选择屏幕版本', trigger: 'change' }
          ],
          bundleName: [
            { required: true, message: '请输入模型文件路径', trigger: 'blur' }
          ],
          assetName: [
            { required: true, message: '请输入模型文件名称', trigger: 'blur' }
          ],
          time: [
            { required: true, message: '请输入模板时长', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '请选择模板类别', trigger: 'change' }
          ],

        }
      }

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
        this.infoData.row.id = row.id
        this.infoData.row.name = row.name
        this.infoData.row.remarks = row.remarks
        this.infoData.row.screen = row.screen
        this.infoData.row.bundleName = row.bundleName
        this.infoData.row.assetName = row.assetName
        this.infoData.row.time = row.time
        this.infoData.row.type = row.type
      } else {
        this.infoData.row.edit = false
        this.infoData.row.id = ''
        this.infoData.row.name = ''
        this.infoData.row.remarks = ''
        this.infoData.row.screen = ''
        this.infoData.row.bundleName = ''
        this.infoData.row.assetName = ''
        this.infoData.row.time = ''
        this.infoData.row.type = 0
      }
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let formName = "pcForm"
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var url = "/oms/advert/typesave";
          var params = _this.infoData.row;
          sendRequest(url, params, function (jsonData) {
            if (jsonData.isSuccess) {
              _this.$message.success("保存成功");
              _this.drawer = false;
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