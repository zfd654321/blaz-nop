<template>
  <!-- 新增/编辑模块 -->
  <el-drawer :title="infoData.row.edit?'PC修改':'PC新增'" :visible.sync="drawer" ref="drawer" :before-close="handleClose">
    <el-form :model="infoData.row" :rules="infoData.rules" ref="pcForm" label-width="80px" style="margin:20">
      <el-form-item label="机器码" prop="id">
        <el-input placeholder="请输入机器码" v-model.trim="infoData.row.id" :readonly="infoData.row.edit"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input placeholder="请输入备注" v-model.trim="infoData.row.remarks"></el-input>
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
          edit: false
        },
        rules: {
          id: [
            { required: true, message: '请输入机器码', trigger: 'blur' },
            { min: 24, max: 24, message: '机器码格式错误', trigger: 'blur' },
            { pattern: /^[a-zA-Z0-9-]+$/, message: '机器码可使用英文、数字和横线', trigger: 'blur' }
          ],
          remarks: [
            { required: true, message: '请输入备注', trigger: 'blur' }
          ],

        }
      },


    };
  },
  created: function () {
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
      } else {
        this.infoData.row.edit = false
        this.infoData.row.id = ''
        this.infoData.row.remarks = ''
      }
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let formName = "pcForm"
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var url = "/oms/pc/save";
          var params = _this.infoData.row;
          sendRequest(url, params, function (jsonData) {
            if (jsonData.isSuccess) {
              _this.$message.success("保存成功");
              _this.drawer = false;
              _this.loadDataList();
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