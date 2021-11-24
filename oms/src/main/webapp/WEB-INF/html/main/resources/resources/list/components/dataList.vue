<template>
  <!-- 数据列表模块 -->
  <transition name="el-zoom-in-top">
    <el-card class="box-card" v-show="ready">
      <!-- 查询栏 -->
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="名称" size="mini">
          <el-input v-model.trim="formInline.name" placeholder="名称"></el-input>
        </el-form-item>
        <el-form-item label="类型" size="mini">
          <el-select v-model="formInline.type">
            <el-option label="全部" value=""></el-option>
            <el-option label="图片" value="img"></el-option>
            <el-option label="视频" value="video"></el-option>
            <el-option label="其他" value="other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item size="mini">
          <el-button type="primary" @click="doQuery" plain>查询</el-button>
        </el-form-item>
      </el-form>
      <!-- 按钮栏 -->
      <el-row>
        <el-button size="mini" type="primary" @click="openEdit(null)">添加</el-button>
      </el-row>

      <!-- 表格栏 -->
      <el-table :data="tableData.list" size="mini" style="margin-top:20px" max-height="300" stripe header-cell-class-name="tableheader">
        <el-table-column label="缩略图">
          <template slot-scope="scope">
            <el-image :src="scope.row.thumbnail" style="width: 50px; height: 50px;display: block;margin: 0 auto;" fit="contain"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="md5" label="md5值"></el-table-column>
        <el-table-column prop="type" label="类型" :formatter="checkType"></el-table-column>
        <el-table-column prop="type" label="文件大小" :formatter="getFileSize"></el-table-column>
        <el-table-column prop="createdName" label="创建用户"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间"></el-table-column>
        <el-table-column prop="updatedName" label="更新用户"></el-table-column>
        <el-table-column prop="updatedAt" label="更新时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="showRow(scope.row)" type="text" size="mini">查看</el-button>
            <el-button @click="deleteRow(scope.row)" type="text" size="mini">删除</el-button>
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
        name: '',
        screen: '',
        pageSize: 8,
        pageNo: 1
      },
      tableData: { list: [], totalCount: 0 },
      ready: false
    };
  },
  created: function () {
    this.loadDataList()
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
      var url = "/oms/resources/list";
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

    showRow(row) {
      this.$parent.$refs.dataShow.loadEditData(row);
    },
    deleteRow(row) {
      let _this = this;
      _this.$confirm('此操作将永久删除该资源,可能影响所有配置该资源的设备,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        var url = "/oms/resources/delete";
        sendRequest(url, { id: row.id }, function (jsonData) {
          _this.$message.success("删除成功");
          _this.loadDataList();
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    checkType(row, column) {
      switch (row.type) {
        case "img":
          return "图片"
        case "video":
          return "视频"
        case "other":
          return "其他"

        default:
          return "未知"
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