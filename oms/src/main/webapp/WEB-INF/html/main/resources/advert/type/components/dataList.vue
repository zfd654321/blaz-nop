<template>
  <!-- 数据列表模块 -->
  <transition name="el-zoom-in-top">
    <el-card class="box-card" v-show="ready">
      <!-- 查询栏 -->
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="名称" size="mini">
          <el-input v-model.trim="formInline.name" placeholder="名称"></el-input>
        </el-form-item>

        <el-form-item label="屏幕版本" size="mini">
          <el-select v-model="formInline.screen" placeholder="屏幕版本">
            <el-option label="全部" value=""></el-option>
            <el-option label="竖屏" value="1"></el-option>
            <el-option label="横屏" value="2"></el-option>
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
        <el-table-column prop="name" label="模板名称"></el-table-column>
        <el-table-column prop="screen" label="屏幕版本" :formatter="checkScreen"></el-table-column>
        <el-table-column prop="type" label="类型" :formatter="checkType"></el-table-column>
        <el-table-column prop="createdName" label="创建用户"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间"></el-table-column>
        <el-table-column prop="updatedName" label="更新用户"></el-table-column>
        <el-table-column prop="updatedAt" label="更新时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="openEdit(scope.row)" type="text" size="mini">编辑</el-button>
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
        name: '',
        screen: '',
        pageSize: 10,
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
      var url = "/oms/advert/typelist";
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
    checkType(row, column) {
      switch (row.type) {
        case 0:
          return "无额外资源"
        case 1:
          return "图片轮播"
        case 2:
          return "视频"
        default:
          return "未知"
      }
    },

  },
};
</script>