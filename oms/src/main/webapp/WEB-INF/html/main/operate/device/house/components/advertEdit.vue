<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="广告配置" :visible.sync="drawer" ref="drawer" :before-close="handleClose" size="640">
    <el-form :model="mainData.row" ref="advertForm" label-width="auto" style="margin:20">
      <el-form-item>
        <el-transfer id="adverttransfer" filterable v-model="mainData.advertChecked" :data="mainData.advertList" :titles="['可选广告', '已选广告']" :button-texts="['移出', '移入']" :props="{key: 'id',label: 'name'}" target-order="push"></el-transfer>
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
      mainData: {
        deviceId: '',
        advertList: [],
        advertChecked: [],
        cityform: {
          city: '',
          erae: '',
          minerae: '',
          selectedOptions: [], //地区筛选数组
        },
        freePc: []
      },

    };
  },
  created: function () {
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
    loadAdverts(screen) {
      let _this = this;
      var advertParams = {
        screen: screen,
        pageSize: 999,
        pageNo: 1
      }
      var adurl = "/oms/advert/list";
      sendRequest(adurl, advertParams, function (jsonData) {
        console.log(jsonData)
        _this.mainData.advertList = jsonData.data.list
      })
    },

    // 读取对象数据，进行复写
    loadEditData(row) {
      console.log(row)
      let _this = this;
      _this.loadAdverts(row.screen);
      _this.mainData.deviceId = row.deviceId
      var url = "/oms/device/loadDeviceAdvert";
      sendRequest(url, { deviceId: row.deviceId, }, function (jsonData) {
        console.log(jsonData)
        _this.mainData.advertChecked = []
        jsonData.data.forEach(element => {
          _this.mainData.advertChecked.push(element.id)
        });
        _this.sortable("adverttransfer", "mainData", "advertChecked");
      })
      _this.drawer = true;
    },

    checkResource(url, element, type) {
      resourceVue.loadResources(url, type, function (item) {
        $("#" + element).val(item.url);
        $("#" + element)[0].dispatchEvent(new Event('input'))
      })
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      var url = "/oms/device/saveDeviceAdvert";
      var adverts = ""
      if (_this.mainData.advertChecked.length == 0) {
        _this.$message.error("请至少配置一条广告");
        return
      }
      _this.mainData.advertChecked.forEach(element => {
        if (adverts != "") {
          adverts += ","
        }
        adverts += element
      })
      var params = { deviceId: _this.mainData.deviceId, adverts: adverts }

      sendRequest(url, params, function (jsonData) {
        if (jsonData.isSuccess) {
          _this.$message.success("保存成功");
          _this.drawer = false;
        } else {
          _this.$message.error(jsonData.message);
        }
      })
    },

    sortable(str, group, datastr) {
      let el = document
        .querySelector("#" + str)
        .querySelectorAll(".el-checkbox-group")[1];
      new Sortable(el, {
        forceFallback: false,
        onUpdate: event => {
          let box = this.$el
            .querySelector("#" + str)
            .querySelectorAll(".el-checkbox-group")[1];
          let nums = this.$el
            .querySelector("#" + str)
            .querySelectorAll(".el-checkbox-group")[1].childNodes.length;
          console.log(nums, event.newIndex);
          if (event.newIndex >= nums) {
            return;
          }
          let newIndex = event.newIndex;
          let oldIndex = event.oldIndex;
          let $label = box.children[newIndex];
          let $oldLabel = box.children[oldIndex];
          box.removeChild($label);
          if (newIndex < oldIndex) {
            box.insertBefore($label, $oldLabel);
          } else {
            box.insertBefore($label, $oldLabel.nextSibling);
          }
          let item = this[group][datastr].splice(oldIndex, 1);
          this[group][datastr].splice(newIndex, 0, item[0]);
        }
      });
    }
  },
};
</script>