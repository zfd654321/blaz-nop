<template>
  <!-- 新增/编辑模块 -->
  <el-drawer :title="infoData.row.edit?'资源修改':'资源新增'" :visible.sync="drawer" ref="drawer" :before-close="handleClose" v-loading="loading" element-loading-text="正在上传，请稍候">
    <el-form :model="infoData.row" :rules="infoData.rules" ref="pcForm" label-width="80px" style="margin:20">
      <el-form-item label="名称" prop="name">
        <el-input placeholder="请输入文件名称" v-model.trim="infoData.row.name"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="infoData.row.type" filterable placeholder="请选择资源类型" @change="infoData.fileList=[]">
          <el-option label="图片" value="img"></el-option>
          <el-option label="视频" value="video"></el-option>
          <el-option label="其他" value="other"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文件" prop="file" v-if="infoData.row.type!=''">
        <el-upload class="upload-demo" :file-list="infoData.fileList" ref="upload" action="#" drag :on-change="fileOnChange" :auto-upload="false" :multiple="false" list-type="picture" :show-file-list="false" :accept="checkAccpt(infoData.row.type)">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="预览" v-if="infoData.fileList.length>0">
        <template v-if="infoData.row.type=='img'">
          <el-image style="width: 300px; height: 300px" fit="contain" :src="infoData.fileList[0].url" />
        </template>
        <template v-else-if="infoData.row.type=='video'">
          <video style="width: 300px; height: 300px" fit="contain" :src="infoData.fileList[0].url" controls></video>
        </template>

      </el-form-item>
      <el-form-item label="文件大小" v-if="infoData.fileList.length>0">
        {{getFileSize(infoData.fileList[0])}}
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
          name: '',
          type: '',
        },
        rules: {
          name: [
            { required: true, message: '请输入资源名称', trigger: 'blur' },
          ],
          type: [
            { required: true, message: '请选择文件类型', trigger: 'change' }
          ],
          file: [{
            validator: (rule, value, callback) => {
              if (this.infoData.fileList.length < 1) {
                callback(new Error('请选择上传文件'));
              } else {
                callback();
              }

            },
            trigger: 'change'
          }]
        },
        fileList: []
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
      this.infoData.row.edit = false
      this.infoData.row.id = ''
      this.infoData.row.remarks = ''
      this.infoData.fileList = []

    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let formName = "pcForm"
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var formData = new FormData();
          formData.append("file", _this.infoData.fileList[0].raw)
          _this.loading = true
          $.ajax({
            url: "/oms/resources/save?name=" + _this.infoData.row.name + "&type=" + _this.infoData.row.type,
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
              }
            }
          })
        }
      })

    },
    fileOnChange(file, fileList) {
      console.log(file)
      console.log(fileList)
      if (fileList.length > 0) {
        this.infoData.fileList = [fileList[fileList.length - 1]] //这一步，是 展示最后一次选择文件
      }
    },
    checkAccpt(type) {
      switch (type) {
        case "img":
          return ".png,.jpg,.gif"
        case "video":
          return ".mp4,.webm"
        default:
          return "*"
      }
    },
    getFileSize(row) {
      var fileSizeByte = row.size;
      var fileSizeMsg = "";
      if (fileSizeByte < 1048576) fileSizeMsg = (fileSizeByte / 1024).toFixed(2) + "KB";
      else if (fileSizeByte == 1048576) fileSizeMsg = "1MB";
      else if (fileSizeByte > 1048576 && fileSizeByte < 1073741824) fileSizeMsg = (fileSizeByte / (1024 * 1024)).toFixed(2) + "MB";
      else if (fileSizeByte > 1048576 && fileSizeByte == 1073741824) fileSizeMsg = "1GB";
      else if (fileSizeByte > 1073741824 && fileSizeByte < 1099511627776) fileSizeMsg = (fileSizeByte / (1024 * 1024 * 1024)).toFixed(2) + "GB";
      else fileSizeMsg = "文件超过1TB";
      return fileSizeMsg;
    },
  },
};
</script>