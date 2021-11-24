<template>
  <!-- 数据列表模块 -->
  <transition name="el-zoom-in-top">
    <el-card class="box-card" v-show="ready">
      <!-- 查询栏 -->
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="机器码" size="mini">
          <el-input v-model.trim="formInline.id" placeholder="机器码"></el-input>
        </el-form-item>
        <el-form-item label="备注" size="mini">
          <el-input v-model.trim="formInline.remarks" placeholder="备注"></el-input>
        </el-form-item>
        <el-form-item label="状态" size="mini">
          <el-select v-model="formInline.status" placeholder="请选择状态">
            <el-option label="全部" value=""></el-option>
            <el-option label="空闲" value="1"></el-option>
            <el-option label="占用" value="2"></el-option>
            <el-option label="已坏" value="3"></el-option>
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
        <el-table-column prop="id" width="225" label="机器码"></el-table-column>
        <el-table-column prop="remarks" width="220" label="备注"></el-table-column>
        <el-table-column prop="status" label="状态" :formatter="checkStatus"></el-table-column>
        <el-table-column prop="createdName" label="创建用户"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间"></el-table-column>
        <el-table-column prop="updatedName" label="更新用户"></el-table-column>
        <el-table-column prop="updatedAt" label="更新时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="openEdit(scope.row)" type="text" size="mini">编辑</el-button>
            <el-button v-if="scope.row.status==1" @click="deleteRow(scope.row)" type="text" size="mini">删除</el-button>
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
      var url = "/oms/pc/list";
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
    openEdit(row) {
      console.log(row);
      this.$parent.$refs.dataEdit.loadEditData(row);
    },
  //删除
    deleteRow(row) {
      let _this = this
      _this.$confirm('确认将该PC删除么?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        var url = "/oms/pc/delete";
        var params = { id: row.id };

        sendRequest(url, params, function (jsonData) {
          if (jsonData.isSuccess) {
            _this.$message.success("PC已删除");
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

    //状态
    checkStatus(row, column) {
      switch (row.status) {
        case 1:
          return "空闲"
        case 2:
          return "占用"
        case 3:
          return "已坏"

        default:
          return "未知"
      }
    },

  },
};
</script>