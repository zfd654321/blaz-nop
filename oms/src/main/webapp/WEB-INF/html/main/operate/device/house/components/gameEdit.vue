<template>
  <!-- 新增/编辑模块 -->
  <el-drawer title="游戏配置" :visible.sync="drawer" ref="drawer" :before-close="handleClose" size="640">
    <el-steps :active="gameData.active" finish-status="success" align-center simple>
      <el-step title="游戏选择"></el-step>
      <el-step title="2d资源配置"></el-step>
    </el-steps>
    <el-form :rules="gameData.rules" :model="gameData.row" ref="gameForm" label-width="auto" style="margin:20">
      <template v-if="gameData.active==0">
        <el-form-item>
          <el-transfer id="gametransfer" filterable v-model="gameData.gameChecked" :data="gameData.gameList" :titles="['可选游戏', '已选游戏']" :button-texts="['移出', '移入']" :props="{key: 'id',label: 'name'}" target-order="push"></el-transfer>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="gameNext" style="margin-left: 250px;">下一步</el-button>
        </el-form-item>
      </template>

      <template v-if="gameData.active==1">
        <el-collapse v-model="gameData.collapseActive" accordion style="margin:20 0">
          <el-collapse-item :title="item.gameId+'['+item.gameName+']'" :name="index" v-for="(item,index) in netresSet()" :key="index">
            <el-form-item :label="netreItem.name" :key="index" v-for="(netreItem,index) in item.netres">
              <el-input placeholder="默认资源" readonly clearable v-model.trim="netreItem.resUrl" :id="'netre'+netreItem.id">
                <el-button slot="append" icon="el-icon-picture-outline" @click="checkResource(netreItem.resUrl,'netre'+netreItem.id,netreItem.type)"></el-button>
              </el-input>
            </el-form-item>
          </el-collapse-item>
          <el-empty description="没有需要额外配置的游戏" v-if="netresSet().length<1" style="height:150"></el-empty>
        </el-collapse>
        <el-form-item>
          <el-button type="primary" @click="gamePre">上一步</el-button>
          <el-button type="primary" @click="onSubmit">保存</el-button>
        </el-form-item>
      </template>
    </el-form>

    <file-json ref="fileJson"></file-json>
  </el-drawer>
</template>
 
<script>
module.exports = {
  components: {
    'file-json': httpVueLoader('main/operate/device/house/components/fileJson.vue')
  },
  data: function () {
    return {
      drawer: false,
      loading: false,
      gameData: {
        deviceId: "",
        active: 0,
        collapseActive: 0,
        gameList: [],
        gameChecked: [],
        gameIds: "",
        netresList: []
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
      console.log(row)
      let _this = this;
      _this.gameData.deviceId = row.deviceId
      var gameParams = {
        screen: row.screen,
        pageSize: 999,
        pageNo: 1
      }
      var adurl = "/oms/game/list";
      sendRequest(adurl, gameParams, function (jsonData) {
        console.log(jsonData)
        _this.gameData.gameList = jsonData.data.list
      })
      var url = "/oms/device/loadDeviceGame";
      sendRequest(url, { deviceId: row.deviceId, }, function (jsonData) {
        console.log(jsonData)
        _this.gameData.gameChecked = []
        jsonData.data.forEach(element => {
          _this.gameData.gameChecked.push(element.id)
        });

        _this.sortable("gametransfer", "gameData", "gameChecked");
      })
      _this.gameData.active = 0;
      _this.drawer = true;
    },

    gameNext() {
      let _this = this;
      var url = "/oms/device/loadDeviceGameNetres";
      var gameIds = "";
      let netresList = []
      if (_this.gameData.gameChecked.length == 0) {
        _this.$message.error("请至少配置一个游戏");
        return
      }
      _this.gameData.gameChecked.forEach(element => {
        if (gameIds != "") {
          gameIds += ","
        }
        gameIds += element

        var gameItem = _this.gameData.gameList.find(function (item) {
          return item.id === element
        });

        netresList.push({ gameId: element, gameName: gameItem.name, netres: [] })
      });
      _this.gameData.gameIds = gameIds;
      sendRequest(url, { deviceId: _this.gameData.deviceId, gameIds: gameIds }, function (jsonData) {
        console.log(jsonData)
        jsonData.data.forEach(netre => {
          var netreItem = netresList.find(function (item) {
            return item.gameId === netre.gameId
          });
          switch (netre.type) {
            case "image":
              netre.type = "img"
              break;
            case "movie":
              netre.type = "video"
              break;
            case "filejson":
              netre.type = "filejson"
              break;
            default:
              netre.type = "other"
              break;
          }
          if (netre.resUrl == null && netre.defaulturl != "") {
            netre.resUrl = netre.defaulturl
          }
          netreItem.netres.push(netre)
        });
        _this.gameData.netresList = netresList;

        _this.gameData.active = 1;

      })

    },

    gamePre() {
      this.gameData.active = 0;
      setTimeout(() => {
        this.sortable("gametransfer", "gameData", "gameChecked");
      }, 1000);
    },


    checkResource(url, element, type) {
      if (type == "filejson") {
        this.$refs.fileJson.loadEditData(url, function (newurl) {
          $("#" + element).val(newurl);
          $("#" + element)[0].dispatchEvent(new Event('input'))
        })
      } else {
        resourceVue.loadResources(url, type, function (item) {
          $("#" + element).val(item.url);
          $("#" + element)[0].dispatchEvent(new Event('input'))
        })
      }
    },

    netresSet() {
      let list = this.gameData.netresList
      return list.filter(function (item) {
        return item.netres.length > 0
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
    },
    // 保存提交
    onSubmit() {
      let _this = this
      var url = "/oms/device/saveDeviceGame";
      var params = {
        deviceId: _this.gameData.deviceId,
        gameIds: _this.gameData.gameIds
      }
      var netresList = []
      _this.gameData.netresList.forEach(game => {
        game.netres.forEach(netre => {
          if (netre.resUrl != "" && netre.resUrl != null) {
            netresList.push({
              gameId: netre.gameId,
              property: netre.property,
              resUrl: netre.resUrl
            })
          }
        })
      })
      params.slist = JSON.stringify(netresList);

      sendRequest(url, params, function (jsonData) {
        if (jsonData.isSuccess) {
          _this.$message.success("保存成功");
          _this.drawer = false;
        } else {
          _this.$message.error(jsonData.message);
        }
      })
    },
  },
};
</script>