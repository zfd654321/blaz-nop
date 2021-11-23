<template>
  <!-- 新增/编辑模块 -->
  <el-drawer :title="infoData.row.edit?'游戏更新':'游戏新增'" :visible.sync="drawer" ref="drawer" :before-close="handleClose" v-loading="loading" element-loading-text="正在上传，请稍候">
    <el-form :model="infoData.row" :rules="infoData.rules" ref="pcForm" label-width="80px" style="margin:20">
      <el-form-item label="游戏编号" prop="id">
        <el-input placeholder="请输入游戏编号" v-model.trim="infoData.row.id" :readonly="infoData.row.edit"></el-input>
      </el-form-item>
      <el-form-item label="游戏备注" prop="remarks">
        <el-input placeholder="请输入游戏备注" v-model.trim="infoData.row.remarks"></el-input>
      </el-form-item>
      <el-form-item label="版本号" prop="version">
        <el-input placeholder="请输入版本号" v-model.trim="infoData.row.version"></el-input>
      </el-form-item>
      <el-form-item label="游戏json" prop="jsonFile">
        <el-upload class="upload-demo" :file-list="infoData.jsonFile" ref="upload" action="#" drag :on-change="jsonOnChange" :auto-upload="false" :multiple="false" accept=".json">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="游戏zip包" prop="zipFile">
        <el-upload class="upload-demo" :file-list="infoData.zipFile" ref="upload" action="#" drag :on-change="zipOnChange" :auto-upload="false" :multiple="false" accept=".zip">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="更新说明" prop="versionLog">
        <el-input placeholder="请输入更新说明" v-model.trim="infoData.row.versionLog"></el-input>
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
          remarks: '',
          version: '',
          versionLog: '',
          edit: false
        },
        rules: {
          id: [
            { required: true, message: '请输入游戏编号', trigger: 'blur' },
            { pattern: /^[a-zA-Z0-9_]+$/, message: '游戏编号可使用英文、数字和下划线', trigger: 'blur' }
          ],
          version: [
            { required: true, message: '请输入版本号', trigger: 'blur' },
            { pattern: /^[a-zA-Z0-9_.]+$/, message: '版本号可使用英文、数字、点号和下划线', trigger: 'blur' }
          ],
          versionLog: [
            { required: true, message: '请输入更新说明', trigger: 'blur' }
          ],
          jsonFile: [{
            validator: (rule, value, callback) => {
              if (this.infoData.jsonFile.length < 1) {
                callback(new Error('请选择上传json文件'));
              } else {
                callback();
              }

            },
            trigger: 'change'
          }],
          zipFile: [{
            validator: (rule, value, callback) => {
              if (this.infoData.zipFile.length < 1) {
                callback(new Error('请选择上传zip文件'));
              } else {
                callback();
              }

            },
            trigger: 'change'
          }]
        },
        jsonFile: [],
        zipFile: []
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
        this.infoData.row.remarks = row.remarks
        this.infoData.row.version = ''
        this.infoData.jsonFile = []
        this.infoData.zipFile = []
        this.infoData.versionLog = ''
      } else {
        this.infoData.row.edit = false
        this.infoData.row.id = ''
        this.infoData.row.remarks = ''
        this.infoData.row.version = ''
        this.infoData.jsonFile = []
        this.infoData.zipFile = []
        this.infoData.versionLog = ''
      }
    },
    jsonOnChange(file, fileList) {
      if (fileList.length > 0) {
        this.infoData.jsonFile = [fileList[fileList.length - 1]] //这一步，是 展示最后一次选择文件
        this.$refs.pcForm.validateField('jsonFile')
      }
    },
    zipOnChange(file, fileList) {
      if (fileList.length > 0) {
        this.infoData.zipFile = [fileList[fileList.length - 1]] //这一步，是 展示最后一次选择文件
        this.$refs.pcForm.validateField('zipFile')
      }
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let formName = "pcForm"
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var formData = new FormData();
          formData.append("jsonFile", _this.infoData.jsonFile[0].raw)
          formData.append("zipFile", _this.infoData.zipFile[0].raw)
          _this.loading = true
          $.ajax({
            url: "/oms/game/save?id=" + _this.infoData.row.id + "&remarks=" + _this.infoData.row.remarks + "&version=" + _this.infoData.row.version + "&edit=" + _this.infoData.row.edit + "&versionLog=" + _this.infoData.row.versionLog,
            type: 'POST',
            async: true,
            cache: false,
            contentType: false, // 告诉jQuery不要去设置Content-Type请求头
            processData: false, // 告诉jQuery不要去处理发送的数据
            data: formData,
            beforeSend: function (XMLHttpRequest) { },
            success: function (jsonData) {
              if (jsonData.isSuccess) {
                _this.$message.success("保存成功");
                _this.loading = false;
                _this.drawer = false;
                _this.$parent.$refs.dataList.loadDataList();
              } else {
                _this.$message.error(jsonData.message);
                _this.loading = false;
              }
            }
          })
        }
      })
    },
  },
};
</script>