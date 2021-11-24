<template>
  <!-- 数据列表模块 -->
  <transition name="el-zoom-in-top">
    <el-card class="box-card" v-show="ready">
      <!-- 查询栏 -->
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="设备编号" size="mini">
          <el-input v-model.trim="formInline.deviceId" placeholder="设备编号"></el-input>
        </el-form-item>
        <el-form-item label="机器码" size="mini">
          <el-input v-model.trim="formInline.pcId" placeholder="机器码"></el-input>
        </el-form-item>
        <el-form-item label="设备名称" size="mini">
          <el-input v-model.trim="formInline.name" placeholder="设备名称"></el-input>
        </el-form-item>
        <el-form-item size="mini">
          <el-button type="primary" @click="doQuery" plain>查询</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格栏 -->
      <el-table :data="tableData.list" size="mini" style="margin-top:20px" max-height="300" stripe header-cell-class-name="tableheader">
        <el-table-column prop="deviceId" label="设备编号"></el-table-column>
        <el-table-column prop="pcId" width="220" label="机器码"></el-table-column>
        <el-table-column prop="name" label="设备名称"></el-table-column>
        <el-table-column label="更新开关">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.software" :active-value="1" :inactive-value="0" @change='changeStatus($event,scope.row.deviceId,"software")'></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="支付开关">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.pay" :active-value="1" :inactive-value="0" @change='changeStatus($event,scope.row.deviceId,"pay")'></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="统计开关">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.statistics" :active-value="1" :inactive-value="0" @change='changeStatus($event,scope.row.deviceId,"statistics")'></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="联网校验开关">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.onlinecheck" :active-value="1" :inactive-value="0" @change='changeStatus($event,scope.row.deviceId,"onlinecheck")'></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="文件比对开关">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.filecheck" :active-value="1" :inactive-value="0" @change='changeStatus($event,scope.row.deviceId,"filecheck")'></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="录屏功能开关">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.video" :active-value="1" :inactive-value="0" @change='changeStatus($event,scope.row.deviceId,"video")'></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="抠像功能开关">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.clip" :active-value="1" :inactive-value="0" @change='changeStatus($event,scope.row.deviceId,"clip")'></el-switch>
          </template>
        </el-table-column>
      </el-table>
      <!-- 翻页 -->
      <el-pagination small background hide-on-single-page style="margin-top: 10;" @current-change="handleCurrentChange" layout="prev, pager, next, jumper, ->, total" :page-size="formInline.pageSize" :total="tableData.totalCount"></el-pagination>
    </el-card>
  </transition>
</template>
 
<script>
module.exports = {
  data: function () {
    return {
      formInline: {
        id: '',
        remarks: '',
        status: '',
        pageSize: 10,
        pageNo: 1
      },
      tableData: { list: [], totalCount: 0 },
      ready: false
    };
  },
  created: function () {
    this.loadDataList();
  },
  mounted: function () {
    this.ready = true;
  },
  methods: {
    // 查询按钮，回到第一页
    doQuery() {
      this.formInline.pageNo = 1;
      this.loadDataList();
    },

    // 读取列表数据
    loadDataList() {
      let _this = this;
      var url = "/oms/switch/list";
      sendRequest(url, _this.formInline, function (jsonData) {
        console.log(jsonData)
        _this.tableData.list = jsonData.data.list
        _this.tableData.totalCount = jsonData.data.totalCount
      })
    },
    // 翻页
    handleCurrentChange(pageNo) {
      let _this = this;
      _this.formInline.pageNo = pageNo;
      _this.loadDataList();
    },

    changeStatus(value, deviceId, type) {
      let _this = this
      var url = "/oms/switch/save";
      var params = {
        deviceId: deviceId,
        type: type,
        value: value
      };
      sendRequest(url, params, function (jsonData) {
        if (jsonData.isSuccess) {
          _this.$message.success("修改设备开关状态成功");
        } else {
          _this.$message.error(jsonData.message);
        }
      })
    }



  },
};
</script>