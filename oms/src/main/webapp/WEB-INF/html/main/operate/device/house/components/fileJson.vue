<template>
  <!-- 新增/编辑模块 -->
  <el-drawer :title="jsonData.configList.group.cname" :visible.sync="innerdrawer" ref="innerdrawer" :before-close="handleClose" size="1280" append-to-body>

    <el-tabs tab-position="left" v-model="groupChecked" @tab-click="listChecked='0'" style="height:800">
      <el-tab-pane v-for="(group,gindex) in jsonData.defaultList.group" :key="gindex" :name="gindex+''">
        <span slot="label">
          <el-tag :type="gindex==groupChecked?'':'info'" closable @close="removeGroup(gindex)" style="width:330;">
            <div style="width:290;display:inline-flex;text-align: left;overflow:hidden; white-space:nowrap; text-overflow:ellipsis;">{{group.groupname[0]}}</div>
          </el-tag>
        </span>

        <el-form label-position="top">
          <el-form-item :label="item.cname" v-for="item of jsonData.configList.group.file" :key="item.name">
            <el-input v-for="(gitem,giindex) in group[item.name]" :readonly="item.type!='text'" :key="giindex" v-model="group[item.name][giindex]" size="mini" style="width:350">
              <el-button size="mini" type="primary" plain v-if="item.type!='text'" slot="append" icon="el-icon-picture-outline" @click="checkGroupResource(item.name,giindex,item.type)"></el-button>
            </el-input>
          </el-form-item>
          <el-form-item :label="jsonData.configList.group.list.cname">
            <el-tabs tab-position="left" v-model="listChecked" style="height:550">
              <el-tab-pane v-for="(list,lindex) in group.list" :key="lindex" :name="lindex+''">
                <span slot="label">
                  <el-tag :type="lindex==listChecked?'':'info'" closable @close="removeList(lindex)" style="width:330;">
                    <div style="width:290;display:inline-flex;text-align: left;overflow:hidden; white-space:nowrap; text-overflow:ellipsis;">{{list.listname[0]}}</div>
                  </el-tag>
                </span>
                <el-form label-position="top" style="height:550;overflow-y:auto">
                  <el-form-item :label="item.cname" v-for="(item,findex) of jsonData.configList.group.list.file" :key="item.name">
                    <el-input v-for="(litem,liindex) in list[item.name]" :readonly="item.type!='text'" :key="liindex" v-model="list[item.name][liindex]" size="mini" style="width:350;margin-top:15">
                      <el-button size="mini" type="primary" plain v-if="item.type!='text'" slot="append" icon="el-icon-picture-outline" @click="checkResource(item.name,liindex,item.type)"></el-button>
                      <el-button size="mini" type="primary" plain v-if="list[item.name].length>jsonData.configList.group.list.file[findex].minLength" slot="append" icon="el-icon-delete" @click="removeResource(item.name,liindex)"></el-button>
                    </el-input>
                    <div>
                      <el-button icon="el-icon-plus" circle size="mini" @click="addResource(item.name)" style="margin-top:15" v-if="jsonData.configList.group.list.file[findex].maxLength>list[item.name].length">
                      </el-button>
                    </div>
                  </el-form-item>
                </el-form>
              </el-tab-pane>

              <el-tab-pane disabled>
                <span slot="label">
                  <el-button icon="el-icon-plus" circle size="mini" @click="addList()" v-if="jsonData.configList.group.list.maxLength>group.list.length">
                  </el-button>
                </span>
              </el-tab-pane>
            </el-tabs>
          </el-form-item>
        </el-form>

      </el-tab-pane>

      <el-tab-pane disabled>
        <span slot="label">
          <el-button icon="el-icon-plus" circle size="mini" @click="addGroup()" v-if="jsonData.configList.group.maxLength>jsonData.defaultList.group.length">
          </el-button>
        </span>
      </el-tab-pane>
    </el-tabs>

    <el-button type="primary" @click="onSubmit" style="margin:20 150">保存</el-button>

  </el-drawer>
</template>
 
<script>
module.exports = {
  data: function () {
    return {
      innerdrawer: false,
      id: '',
      groupChecked: '0',
      listChecked: '0',
      jsonData: {
        configList: {
          group: {
            minLength: 0,
            maxLength: 0,
            cname: '',
            list: { name: '', cname: '', file: [] },
          }
        },
        defaultList: {
          group: []
        },
        fileList: []
      },
      returnFun: function (newurl) {

      },

    };
  },
  created: function () {
    // setTimeout(() => {

    //   this.loadEditData("AA", "https://bltest.ar-max.com/files/oms/forever/filejson/DJSW_TestConfig.json")
    // }, 1000);
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
    loadEditData(path, returnFun) {
      let _this = this
      url = '/' + path.split('/').slice(3).join('/');
      _this.returnFun = returnFun
      $.getJSON(url, function (data) {
        console.log(data)
        _this.jsonData = data;
        _this.innerdrawer = true;
      });
    },

    addGroup: function () {
      var re = this.jsonData.defaultList.group
      var groupobj = {};
      var groupconfig = this.jsonData.configList.group
      groupconfig.file.forEach(element => {
        let slist = []
        while (slist.length < element.minLength) {
          slist.push("")
        }
        groupobj[element.name] = slist
      });
      groupobj["groupname"][0] = "未命名"
      let list = [];
      while (list.length < groupconfig.list.minLength) {
        var listobj = {};
        var listconfig = this.jsonData.configList.group.list
        listconfig.file.forEach(element => {
          let slist = []
          while (slist.length < element.minLength) {
            slist.push("")
          }
          listobj[element.name] = slist
        });
        listobj["listname"][0] = "未命名"
        list.push(listobj)
      }
      groupobj["list"] = list

      re.push(groupobj)
    },
    removeGroup: function (index) {

      if (this.groupChecked == index) {
        this.groupChecked = '0'
      } else if (this.groupChecked > index) {
        this.groupChecked = this.groupChecked - 1 + ''
      }
      var re = this.jsonData.defaultList.group
      re.splice(index, 1)
    },
    addList: function () {
      var re = this.jsonData.defaultList.group[this.groupChecked].list
      var listobj = {};
      var listconfig = this.jsonData.configList.group.list
      listconfig.file.forEach(element => {
        let slist = []
        while (slist.length < element.minLength) {
          slist.push("")
        }
        listobj[element.name] = slist
      });
      listobj["listname"][0] = "未命名"
      re.push(listobj)
    },
    removeList: function (index) {
      if (this.listChecked == index) {
        this.listChecked = '0'
      } else if (this.listChecked > index) {
        this.listChecked = this.listChecked - 1 + ''
      }
      var re = this.jsonData.defaultList.group[this.groupChecked].list
      re.splice(index, 1)
    },
    addResource: function (name) {
      var re = this.jsonData.defaultList.group[this.groupChecked].list[this.listChecked][name]
      re.push("");
    },
    removeResource: function (name, index) {
      var re = this.jsonData.defaultList.group[this.groupChecked].list[this.listChecked][name]
      re.splice(index, 1)
    },




    // 唤起组内资源读取方法
    checkGroupResource(name, index, type) {
      let _this = this
      let retype = type == "movie" ? "video" : type;
      var md5 = _this.jsonData.defaultList.group[_this.groupChecked][name][index]
      let url = ""
      let fileobj = _this.jsonData.fileList.find(function (file) {
        return file.md5 === md5
      })
      if (fileobj != null) {
        url = fileobj.network_url
      }
      console.log(fileobj)
      resourceVue.loadResources(url, retype, function (item) {
        console.log(item)
        _this.$set(_this.jsonData.defaultList.group[_this.groupChecked][name], index, item.md5)
        let newfileobj = _this.jsonData.fileList.find(function (file) {
          return file.md5 === item.md5
        })
        if (newfileobj == null) {
          let filename = item.url.substring(url.lastIndexOf('/') + 1)
          let fileObject = {
            "type": type,
            "file_length": item.size,
            "name": filename,
            "md5": item.md5,
            "network_url": item.url
          }
          _this.jsonData.fileList.push(fileObject)
        }
      })
    },
    // 唤起列表内资源读取方法
    checkResource(name, index, type) {
      let _this = this
      let retype = type == "movie" ? "video" : type;
      var md5 = _this.jsonData.defaultList.group[_this.groupChecked].list[_this.listChecked][name][index]
      let url = ""
      let fileobj = _this.jsonData.fileList.find(function (file) {
        return file.md5 === md5
      })
      if (fileobj != null) {
        url = fileobj.network_url
      }
      console.log(fileobj)
      resourceVue.loadResources(url, retype, function (item) {
        console.log(item)
        _this.$set(_this.jsonData.defaultList.group[_this.groupChecked].list[_this.listChecked][name], index, item.md5)
        let newfileobj = _this.jsonData.fileList.find(function (file) {
          return file.md5 === item.md5
        })
        if (newfileobj == null) {
          let filename = item.url.substring(url.lastIndexOf('/') + 1)
          let fileObject = {
            "type": type,
            "file_length": item.size,
            "name": filename,
            "md5": item.md5,
            "network_url": item.url
          }
          _this.jsonData.fileList.push(fileObject)
        }
      })
    },
    // 保存提交
    onSubmit() {
      let _this = this;
      let data = _this.jsonData
      let nstr = JSON.stringify(_this.jsonData.defaultList);

      let refilelist = []
      data.fileList.forEach(element => {
        if (nstr.indexOf(element.md5) != -1) {
          refilelist.push(element)
        }
      });
      data.fileList = refilelist
      sendRequest(pageContextPath + "/device/saveFileJson", { jsonStr: JSON.stringify(data) }, function (jsonData) {
        if (jsonData.isSuccess) {
          console.log(jsonData.data)
          _this.returnFun(jsonData.data)
          _this.innerdrawer = false;
        }
      }, false);
    },
  },
};
</script>