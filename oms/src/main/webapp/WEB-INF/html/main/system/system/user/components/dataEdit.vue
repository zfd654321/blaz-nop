<template>
  <!-- 新增/编辑模块 -->
  <el-drawer :title="infoData.row.edit?'用户修改':'用户新增'" :visible.sync="drawer" ref="drawer" :before-close="handleClose">
    <el-form ref="form" :model="infoData.row" :rules="infoData.rules" label-width="80px" style="margin:20">
      <el-form-item label="用户名称" prop="nickname">
        <el-input placeholder="请输入用户名称" v-model="infoData.row.nickname"></el-input>
      </el-form-item>
      <el-form-item label="登陆账户" prop="username">
        <el-input placeholder="请输入登陆用户名" v-model="infoData.row.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" :prop="infoData.row.edit?'':'password'">
        <el-input :placeholder="infoData.row.edit?'不修改密码请留空':'请输入密码'" v-model="infoData.row.password"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-select v-model="infoData.row.role">
          <el-option label="系统总管理员" value="001"></el-option>
          <el-option label="后台技术人员" value="002"></el-option>
          <el-option label="前端技术人员" value="003"></el-option>
          <el-option label="平台运营人员" value="004"></el-option>
          <el-option label="资源审核人员" value="005"></el-option>
          <el-option label="平台测试人员" value="006"></el-option>
          <el-option label="游客" value="009"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="infoData.row.status">
          <el-option label="可用" value="1"></el-option>
          <el-option label="禁用" value="0"></el-option>
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
          nickname: '',
          username: '',
          password: '',
          role: '',
          status: '',
          edit: false
        },
        rules: {
          nickname: [
            { required: true, message: '请输入用户名称', trigger: 'blur' },
            { min: 2, max: 10, message: '用户名称长度为2-10个字符', trigger: 'blur' }
          ],
          username: [
            { required: true, message: '请输入登陆账户', trigger: 'blur' },
            { min: 2, max: 20, message: '登陆账户长度为2-20个字符', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 2, max: 20, message: '密码长度为2-20个字符', trigger: 'blur' }
          ],
          role: [
            { required: true, message: '请选择角色', trigger: 'change' }
          ],

        }

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
      })
    },
    getUser(id) {
      return UserName[id]
    },
  },
};
</script>