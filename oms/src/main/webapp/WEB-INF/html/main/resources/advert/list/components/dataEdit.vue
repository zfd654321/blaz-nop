<template>
  <!-- 新增/编辑模块 -->
  <el-drawer :title="infoData.row.edit?'广告修改':'广告新增'" :visible.sync="drawer" ref="drawer" :before-close="handleClose">
    <el-form :model="infoData.row" :rules="infoData.rules" ref="pcForm" label-width="80px" style="margin:20" size="mini">
      <el-form-item label="广告编号" prop="id">
        <el-input placeholder="请输入广告编号" v-model.trim="infoData.row.id" :readonly="infoData.row.edit"></el-input>
      </el-form-item>
      <el-form-item label="广告名称" prop="name">
        <el-input placeholder="请输入广告名称" v-model.trim="infoData.row.name"></el-input>
      </el-form-item>
      <el-form-item label="广告备注" prop="remarks">
        <el-input placeholder="请输入广告备注" v-model.trim="infoData.row.remarks"></el-input>
      </el-form-item>
      <el-form-item label="选择模板" prop="type">
        <el-cascader v-model="infoData.typeCheck" :options="typeData.typeSelect" :show-all-levels="false" :props="{ expandTrigger: 'hover' }" @change="changeType"></el-cascader>
      </el-form-item>
      <el-divider></el-divider>
      <el-alert :title="infoData.typeRemarks" type="info" :closable="false" show-icon style="margin: 20 0;"></el-alert>

      <!-- 图片列表 -->
      <template v-if="infoData.meidaType==1">
        <el-form-item label="图片列表">
          <el-button @click="addDomain" :disabled="infoData.row.resourceList.length>=5">添加</el-button>
        </el-form-item>
        <el-form-item v-for="(domain, index) in infoData.row.resourceList" :key="index" :prop="'resourceList.' + index + '.value'" :rules="{required: true, message: '资源不能为空', trigger: 'change'}">
          <el-input placeholder="请选择资源" readonly v-model.trim="domain.value" :id="'resource'+index" style="width:350">
            <el-button slot="append" icon="el-icon-picture-outline" @click="checkResource(domain.value,'resource'+index,'img')"></el-button>
          </el-input>
          <el-button @click.prevent="removeDomain(index)" :disabled="infoData.row.resourceList.length<=1">删除</el-button>
        </el-form-item>
        <el-form-item label="轮播间隔" prop="scount">
          <el-input placeholder="请输入轮播间隔" v-model.trim="infoData.row.scount"></el-input>
        </el-form-item>
      </template>

      <!-- 视频 -->
      <template v-if="infoData.meidaType==2">
        <el-form-item label="视频资源" :key="index" v-for="(domain, index) in infoData.row.resourceList" :prop="'resourceList.' + index + '.value'" :rules="{required: true, message: '资源不能为空', trigger: 'change'}">
          <el-input placeholder="请选择资源" readonly v-model.trim="domain.value" :id="'resource'+index" style="width:350">
            <el-button slot="append" icon="el-icon-picture-outline" @click="checkResource(domain.value,'resource'+index,'video')"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="音量" prop="scount">
          <el-input placeholder="请输入音量" v-model.trim="infoData.row.scount"></el-input>
        </el-form-item>
      </template>

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
          type: '',
          resource: '',
          scount: '',
          merchantId:'-1',
          resourceList: [],
          edit: false
        },
        rules: {
          id: [
            { required: true, message: '请输入广告编号', trigger: 'blur' },
            { pattern: /^[a-zA-Z0-9_]+$/, message: '编号可使用英文、数字和下划线', trigger: 'blur' }
          ],
          name: [
            { required: true, message: '请输入广告名称', trigger: 'blur' }
          ],
          remarks: [
            { required: true, message: '请输入广告备注', trigger: 'blur' }
          ],
          screen: [
            { required: true, message: '请选择屏幕版本', trigger: 'change' }
          ],
          type: [
            { required: true, message: '请选择广告模板', trigger: 'change' }
          ],
          scount: [
            { required: true, message: '请输入轮播间隔', trigger: 'blur' }
          ],

        },
        typeRemarks: '',
        typeCheck: [],
        meidaType: 0
      },
      typeData: {
        typeSelect: [],
        typeList: []
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
        this.infoData.row.remarks = row.remarks
        this.infoData.row.scount = row.scount
        this.selectType(row.type)
        if (row.resource != "") {
          var resources = row.resource.split(",")
          resources.forEach(element => {
            this.infoData.row.resourceList.push({ value: element })
          });
        }
      } else {
        this.infoData.row.edit = false
        this.infoData.row.id = ''
        this.infoData.row.name = ''
        this.infoData.row.remarks = ''
        this.infoData.row.screen = ''
        this.infoData.row.type = ''
        this.infoData.row.resource = ''
        this.infoData.row.scount = ''
        this.infoData.meidaType = 0
        this.infoData.typeCheck = []
        this.infoData.row.resourceList = []
      }
    },
    changeType() {
      var type = this.infoData.typeCheck[1]
      var typeItem = this.typeData.typeList.find(function (item) {
        return item.id === type
      })
      this.infoData.row.type = type
      this.infoData.row.screen = typeItem.screen
      this.infoData.meidaType = typeItem.type
      this.infoData.typeRemarks = typeItem.remarks
      this.infoData.row.resourceList = [{ value: '' }]
    },
    selectType(type) {
      var typeItem = this.typeData.typeList.find(function (item) {
        return item.id === type
      })
      this.infoData.typeCheck = [typeItem.screen, type]
      this.infoData.row.type = type
      this.infoData.row.screen = typeItem.screen
      this.infoData.meidaType = typeItem.type
      this.infoData.typeRemarks = typeItem.remarks
      this.infoData.row.resourceList = []
    },
    addDomain() {
      this.infoData.row.resourceList.push({ value: '' })
    },
    removeDomain(index) {
      this.infoData.row.resourceList.splice(index, 1)
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let formName = "pcForm"
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var url = "/oms/advert/save";
          var resource = ""
          _this.infoData.row.resourceList.forEach(element => {
            if (resource != "") {
              resource += ","
            }
            resource += element.value
          });
          _this.infoData.row.resource = resource
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
    checkResource(url, element, type) {
      resourceVue.loadResources(url, type, function (item) {
        $("#" + element).val(item.url);
        $("#" + element)[0].dispatchEvent(new Event('input'))
      })
    }
  },
};
</script>