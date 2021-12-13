<template>
  <!-- 数据列表模块 -->
  <transition name="el-zoom-in-top">
    <el-card class="box-card" v-show="ready">
      <!-- 查询栏 -->
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="商家名称" size="mini">
          <el-input v-model.trim="formInline.name" placeholder="游戏编号"></el-input>
        </el-form-item>
        <el-form-item label="商家类型" size="mini">
          <el-select v-model="formInline.type">
            <el-option label="全部" value=""></el-option>
            <el-option label="年度运营" value="1"></el-option>
            <el-option label="单系列" value="2"></el-option>
            <el-option label="代理" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" size="mini">
          <el-select v-model="formInline.type">
            <el-option label="全部" value=""></el-option>
            <el-option label="待交付" value="0"></el-option>
            <el-option label="运营中" value="1"></el-option>
            <el-option label="已删除" value="2"></el-option>
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
        <el-table-column prop="name" label="商家名称"></el-table-column>
        <el-table-column prop="type" label="商家类型" :formatter="checkType"></el-table-column>
        <el-table-column prop="status" label="账户状态">
          <template slot-scope="scope">
            <el-tag size="mini" :type="statusShow(scope.row.status)">{{checkStatus(scope.row.status)}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contacts" label="联系人"></el-table-column>
        <el-table-column prop="telphone" label="联系电话"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="outDate" label="授权日期"></el-table-column>

        <el-table-column label="操作" width="170">
          <template slot-scope="scope">
            <el-button @click="openEdit(scope.row)" type="text" size="mini">编辑</el-button>
            <el-button @click="openPowers(scope.row)" type="text" size="mini">权限</el-button>
            <el-button v-if="scope.row.status==0" @click="turnOver(scope.row)" type="text" size="mini">交付</el-button>
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
        name: '',
        type: '',
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
      var url = "/oms/merchant/list";
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
    openPowers(row) {
      console.log(row);
      this.$parent.$refs.powerEdit.loadEditData(row);
    },

    // 日志按钮
    showLog(row) {
      console.log(row);
      this.$parent.$refs.logShow.loadEditData(row);
    },

    // 查看按钮
    showJson(row) {
      window.open("/files/oms/forever/game/" + row.id + "/" + row.version + "/game.json")
    },

    // 下载按钮
    downZip(row) {
      window.open("/files/oms/forever/game/" + row.id + "/" + row.version + "/game.zip")
    },

    checkType(row, column) {
      switch (row.type) {
        case 1:
          return "年度运营"
        case 2:
          return "单系列"
        case 3:
          return "代理"

        default:
          return "未知"
      }
    },

    checkStatus(status) {
      switch (status) {
        case 0:
          return "待交付"
        case 1:
          return "运营中"
        case 2:
          return "已删除"
        default:
          return "未知"
      }
    },
    statusShow(status) {
      switch (status) {
        case 0:
          return "warning"
        case 1:
          return "success"
        case 2:
          return "info"
        default:
          return "danger"
      }
    },

  },
};
</script>