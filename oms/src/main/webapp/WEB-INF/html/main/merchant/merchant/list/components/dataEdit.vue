<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="商家信息" :visible.sync="drawer" ref="drawer" :before-close="handleClose" v-loading="loading" element-loading-text="正在上传，请稍候">
    <el-form :model="infoData.row" :rules="infoData.rules" ref="pcForm" label-width="80px" style="margin:20">
      <el-form-item label="商家名称" prop="name">
        <el-input placeholder="请输入商家名称" v-model.trim="infoData.row.name"></el-input>
      </el-form-item>
      <el-form-item label="商家备注" prop="remarks">
        <el-input placeholder="请输入商家备注" v-model.trim="infoData.row.remarks"></el-input>
      </el-form-item>
      <el-form-item label="商家类型" prop="type">
        <el-select v-model="infoData.row.type">
          <el-option label="年度运营" value="1"></el-option>
          <el-option label="单系列" value="2"></el-option>
          <el-option label="代理" value="3"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="登陆账户" prop="username">
        <el-input placeholder="请输入登陆账户" v-model.trim="infoData.row.username"></el-input>
      </el-form-item>

      <el-form-item label="密码" :prop="infoData.row.edit?'':'password'">
        <el-input :placeholder="infoData.row.edit?'不修改密码请留空':'请输入密码'" v-model="infoData.row.password"></el-input>
      </el-form-item>

      <el-form-item label="联系人" prop="contacts">
        <el-input placeholder="请输入联系人" v-model.trim="infoData.row.contacts"></el-input>
      </el-form-item>

      <el-form-item label="联系电话" prop="telphone">
        <el-input placeholder="请输入联系电话" v-model.trim="infoData.row.telphone"></el-input>
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input placeholder="请输入邮箱" v-model.trim="infoData.row.email"></el-input>
      </el-form-item>

      <el-form-item label="地址" prop="address">
        <el-cascader :options="CityInfo" v-model="mainData.cityform.selectedOptions" :filterable="true" @change="handleChange"></el-cascader>
      </el-form-item>

      <el-form-item label="授权日期" prop="outDate">
        <el-date-picker v-model="infoData.row.outDate" type="date" placeholder="选择授权日期" value-format="yyyy-MM-dd" :picker-options="infoData.pickerOptions"></el-date-picker>
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
          type: '',
          remarks: '',
          username: '',
          password: '',
          contacts: '',
          telphone: '',
          email: '',
          address: '',
          outDate: '',
          edit: false
        },
        rules: {
          name: [
            { required: true, message: '请输入版本号', trigger: 'blur' },
            { min: 2, max: 10, message: 's商家名称长度为2-10个字符', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '请选择商户类型', trigger: 'change' }
          ],
          remarks: [
            { required: true, message: '请输入商家备注', trigger: 'blur' },
            { min: 1, max: 100, message: '商家备注长度为1-100个字符', trigger: 'blur' }
          ],
          username: [
            { required: true, message: '请输入登陆账户', trigger: 'blur' },
            { min: 2, max: 20, message: '登陆账户长度为2-20个字符', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 2, max: 20, message: '密码长度为2-20个字符', trigger: 'blur' }
          ],
          contacts: [
            { required: true, message: '请输入联系人', trigger: 'blur' },
            { min: 2, max: 20, message: '联系人长度为2-20个字符', trigger: 'blur' }
          ],
          telphone: [
            { required: true, message: '请输入联系电话', trigger: 'blur' },
            { min: 2, max: 20, message: '联系电话长度为2-20个字符', trigger: 'blur' }
          ],
          email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' },
            { min: 2, max: 20, message: '邮箱长度为2-20个字符', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '请选择地址', trigger: 'blur' }
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
        this.infoData.row.name = row.name
        this.infoData.row.type = row.type
        this.infoData.row.remarks = row.remarks
        this.infoData.row.username = row.username
        this.infoData.row.password = ''
        this.infoData.row.contacts = row.contacts
        this.infoData.row.telphone = row.telphone
        this.infoData.row.email = row.email
        this.infoData.row.address = row.address
        var cityList = row.address.split(",")
        if (cityList.length == 3) {
          this.mainData.cityform.selectedOptions = [parseInt(cityList[0]), parseInt(cityList[1]), parseInt(cityList[2])]
        } else {
          this.mainData.cityform.selectedOptions = [19, 199, 1770]
        }
        this.infoData.row.outDate = row.outDate
      } else {
        this.infoData.row.edit = false
        this.infoData.row.id = ''
        this.infoData.row.name = ''
        this.infoData.row.type = ''
        this.infoData.row.remarks = ''
        this.infoData.row.username = ''
        this.infoData.row.password = ''
        this.infoData.row.contacts = ''
        this.infoData.row.telphone = ''
        this.infoData.row.email = ''
        this.infoData.row.address = '19, 199, 1770'
        this.mainData.cityform.selectedOptions = [19, 199, 1770]
        this.infoData.row.outDate = ''
      }
    },
    handleChange(value) {
      this.infoData.row.address = this.mainData.cityform.selectedOptions.toString()
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let formName = "pcForm"
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var url = "/oms/merchant/save";
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