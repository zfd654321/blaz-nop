<template>
  <div>

    <el-drawer title="资源选择" :visible.sync="resourcesDrawer" ref="resourcesDrawer" :modal-append-to-body="false">
      <el-form style="margin:20">
        <el-form-item label="当前资源">
          <el-empty v-if="resourceUrl=='' || resourceUrl==null" description="未选择" style="width: 200px; height: 200px"></el-empty>
          <el-image v-else-if="formInline.type=='img' " style=" height: 200px" fit="contain" :src="resourceUrl" />
          <video v-else-if="formInline.type=='video' " style=" height: 200px" fit="contain" :src="resourceUrl" controls></video>
        </el-form-item>
      </el-form>
      <el-divider></el-divider>
      <el-form :inline="true" :model="formInline" class="demo-form-inline" style="margin:0 20">
        <el-form-item label="名称" size="mini">
          <el-input v-model.trim="formInline.name" placeholder="名称"></el-input>
        </el-form-item>
        <el-form-item size="mini">
          <el-button type="primary" @click="loadDataList" plain>查询</el-button>
        </el-form-item>
        <el-form-item size="mini">
          <el-button type="primary" @click="openEdit(null)" plain>上传</el-button>
        </el-form-item>
      </el-form>
      <el-col :span="6" v-for="item in tableData.list" :key="item.id">
        <el-card :body-style="{ padding: '0px' }" style="width: 100px;height:150px;margin: 10px auto;font-size: 14;text-align: center;cursor:pointer">
          <el-image :src="item.thumbnail" class="image" style="width: 100px; height: 100px;background:#eee" fit="contain" @click="checkResource(item)"></el-image>
          <div style="padding: 4px;  ">
            <p style="white-space: nowrap;text-overflow:ellipsis;">{{item.name}}</p>
            <p class="size">{{ getFileSize(item) }}</p>
          </div>
        </el-card>
      </el-col>
    </el-drawer>
</template>
 
<script>
module.exports = {
  data: function () {
    return {
      resourcesDrawer: false,
      resourceElement: null,
      resourceUrl: '',
      formInline: {
        name: '',
        type: '',
        pageSize: 999,
        pageNo: 1
      },
      tableData: {
        list: [],
        totalCount: 0
      },
      returnFun: function (item) { }
    };
  },
  methods: {
    loadResources(url, type, returnFun) {
      this.resourcesDrawer = true;
      this.resourceUrl = url;
      this.formInline.type = type;
      this.returnFun = returnFun
      this.loadDataList()
    },
    loadDataList() {
      let _this = this;
      var url = "/oms/resources/list";
      sendRequest(url, _this.formInline, function (jsonData) {
        console.log(jsonData)
        _this.tableData.list = jsonData.data.list
        _this.tableData.totalCount = jsonData.data.totalCount
      })
    },
    getFileSize(row) {
      var fileSizeByte = row.size;
      var fileSizeMsg = "";
      if (fileSizeByte < 1048576)
        fileSizeMsg = (fileSizeByte / 1024).toFixed(2) + "KB";
      else if (fileSizeByte == 1048576)
        fileSizeMsg = "1MB";
      else if (fileSizeByte > 1048576 && fileSizeByte < 1073741824)
        fileSizeMsg = (fileSizeByte / (1024 * 1024)).toFixed(2) + "MB";
      else if (fileSizeByte > 1048576 && fileSizeByte == 1073741824)
        fileSizeMsg = "1GB";
      else if (fileSizeByte > 1073741824 && fileSizeByte < 1099511627776)
        fileSizeMsg = (fileSizeByte / (1024 * 1024 * 1024)).toFixed(2) + "GB";
      else
        fileSizeMsg = "文件超过1TB";
      return fileSizeMsg;
    },
    openEdit(row) {
      console.log(row);
      this.$parent.$refs.dataEdit.loadEditData(row);
    },
    checkResource(item) {
      this.resourcesDrawer = false;
      this.returnFun(item)
    }
  },
};
</script>