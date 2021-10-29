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
        <el-table-column prop="type" label="类型" :formatter="checkType"></el-table-column>
        <el-table-column prop="type" label="屏幕" :formatter="checkScreen"></el-table-column>
        <el-table-column prop="createdName" label="创建用户"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间"></el-table-column>
        <el-table-column prop="updatedName" label="更新用户"></el-table-column>
        <el-table-column prop="updatedAt" label="更新时间"></el-table-column>
        <el-table-column label="操作" width="170">
          <template slot-scope="scope">
            <el-button @click="openEdit(scope.row)" type="text" size="mini">信息</el-button>
            <el-button @click="openConfig(scope.row)" type="text" size="mini">配置</el-button>
            <el-button @click="openAdvert(scope.row)" type="text" size="mini">广告</el-button>
            <el-button @click="openGame(scope.row)" type="text" size="mini">游戏</el-button>
            <el-button @click="online(scope.row)" type="text" size="mini">上线</el-button>
            <el-button @click="deviceCopy(scope.row)" type="text" size="mini">平移</el-button>
            <el-button @click="deleteRow(scope.row)" type="text" size="mini">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 翻页 -->
      <el-pagination small background hide-on-single-page style="margin-top: 10;" @current-change="handleCurrentChange" layout="prev, pager, next, jumper" :total="tableData.totalCount"></el-pagination>
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
        status: 2,
        pageSize: 10,
        pageNo: 1
      },
      tableData: { list: [], totalCount: 0 },
      ready: false,
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
        var list = jsonData.data.list;
        var count = jsonData.data.totalCount;
        list.forEach((element) => {
          element.createdName = UserName[element.createdBy];
          element.updatedName = UserName[element.updatedBy];
        });
        console.log(list);
        _this.tableData.list = list;
        _this.tableData.totalCount = count;
      })
    },
    // 翻页
    handleCurrentChange(pageNo) {
      let _this = this;
      _this.formInline.pageNo = pageNo;
      _this.loadDataList();
    },

    // 新增/编辑按钮
    openEdit(row) {
      console.log(row);
      this.$parent.$refs.dataEdit.loadEditData(row);
    },
    //配置按钮
    openConfig(row) {
      this.$parent.$refs.configEdit.loadEditData(row.deviceId);
    },
    //广告按钮
    openAdvert(row) {
      this.$parent.$refs.advertEdit.loadEditData(row);
    },
    //广告按钮
    openGame(row) {
      this.$parent.$refs.gameEdit.loadEditData(row);
    },
    //平移按钮
    deviceCopy(row) {
      this.$parent.$refs.deviceCopy.loadEditData(row);
    },
    //上线按钮
    online(row) {
      let _this = this
      var checkurl = "/oms/device/deviceOnlineCheck";
      sendRequest(checkurl, { deviceId: row.deviceId }, function (checkData) {
        if (checkData.isSuccess) {

          _this.$confirm('确认将该设备上线么?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            var url = "/oms/device/updateStatus";
            var params = { deviceId: row.deviceId, status: 3 };

            sendRequest(url, params, function (jsonData) {
              if (jsonData.isSuccess) {
                _this.$message.success("设备已上线");
                _this.drawer = false;
                _this.loadDataList();
              } else {
                _this.$message.error(jsonData.message);
              }
            })
          }).catch(() => {
            _this.$message({
              type: 'info',
              message: '已取消操作'
            });
          });
        } else {
          _this.$message.error(checkData.message);
        }
      })

    },
    //删除设备
    deleteRow(row) {
      let _this = this
      _this.$confirm('确认将该设备删除么?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        var url = "/oms/device/updateStatus";
        var params = { deviceId: row.deviceId, status: -1 };

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