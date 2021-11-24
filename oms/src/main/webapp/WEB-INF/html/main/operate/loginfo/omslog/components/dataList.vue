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
        <el-table-column prop="deviceId" width="220" label="设备编号"></el-table-column>
        <el-table-column prop="pcId" width="220" label="机器码"></el-table-column>
        <el-table-column prop="name" label="设备名称"></el-table-column>
        <el-table-column prop="type" label="类型" :formatter="checkType"></el-table-column>
        <el-table-column prop="type" label="屏幕" :formatter="checkScreen"></el-table-column>
        <el-table-column prop="createdName" label="创建用户"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间"></el-table-column>
        <el-table-column prop="updatedName" label="更新用户"></el-table-column>
        <el-table-column prop="updatedAt" label="更新时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="openLogs(scope.row)" type="text" size="mini">查看日志</el-button>
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
        deviceId: '',
        pcId: '',
        name: '',
        pageSize: 10,
        pageNo: 1
      },
      tableData: { list: [], totalCount: 0 },
      ready: false,
      pcDelete: true
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
      var url = "/oms/device/list";
      sendRequest(url, _this.formInline, function (jsonData) {
        var list = jsonData.data.list
        var count = jsonData.data.totalCount
        list.forEach(element => {
          element.createdName = UserName[element.createdBy]
          element.updatedName = UserName[element.updatedBy]
        });
        console.log(list)
        _this.tableData.list = list
        _this.tableData.totalCount = count
      })
    },
    // 翻页
    handleCurrentChange(pageNo) {
      let _this = this;
      _this.formInline.pageNo = pageNo;
      _this.loadDataList();
    },

    // 新增/编辑按钮
    openLogs(row) {
      console.log(row);
      this.$parent.$refs.dataEdit.loadEditData(row);
    },
    // 设备入库
    intoHouse(row) {
      let _this = this
      _this.$confirm('确认将该设备入库么?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var url = "/oms/device/intoHouse";
        var params = { deviceId: row.deviceId };

        sendRequest(url, params, function (jsonData) {
          if (jsonData.isSuccess) {
            _this.$message.success("入库成功");
            _this.drawer = false;
            _this.loadDataList();
          } else {
            _this.$message.error(jsonData.message);
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        });
      });
    },
    //删除设备
    deleteRow(row) {
      let _this = this
      _this.$confirm('确认将该设备删除么?', '警告', {
        inputType: 'checkbox',
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        var url = "/oms/device/updateStatus";
        var params = { deviceId: row.deviceId, status: -1, pcDelete: _this.pcDelete };

        sendRequest(url, params, function (jsonData) {
          if (jsonData.isSuccess) {
            _this.$message.success("设备已删除");
            _this.drawer = false;
            _this.loadDataList();
          } else {
            _this.$message.error(jsonData.message);
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        });
      });
    },

    checkType(row, column) {
      switch (row.type) {
        case 1:
          return "自营"
        case 2:
          return "商家"

        default:
          return "未知"
      }
    },
    checkScreen(row, column) {
      switch (row.screen) {
        case 1:
          return "竖屏"
        case 2:
          return "横屏"

        default:
          return "未知"
      }
    },

  },
};
</script>