var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        advertDrawer: false,
        gameDrawer: false,
        formInline: {
            deviceId: '',
            pcId: '',
            name: '',
            status: 3,
            pageSize: 10,
            pageNo: 1
        },
        infoData: {
            row: {
                edit: false,
                deviceId: '',
                pcId: '',
                name: '',
                remarks: '',
                address: '',
                type: 1,
                screen: 1,
                camera: "Kinect2.0"
            },
            city: [0, 0, 0]
        },
        advertData: {
            deviceId: "",
            list: []
        },
        gameData: {
            deviceId: "",
            list: []
        },
        tableData: { list: [], totalCount: 0 },
        ready: false

    },
    created: function() {
        this.loadDataList();
    },
    mounted: function() {
        this.ready = true;
    },
    methods: {
        doQuery() {
            this.formInline.pageNo = 1
            this.loadDataList()
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
        getCreater(row, column) {
            return UserName[row.createdBy]
        },
        getUpdater(row, column) {
            return UserName[row.updatedBy]
        },
        loadDataList() {
            let _this = this;
            var url = "/oms/device/list";
            sendRequest(url, _this.formInline, function(jsonData) {
                console.log(jsonData)
                _this.tableData.list = jsonData.data.list
                _this.tableData.totalCount = jsonData.data.totalCount
            })
        },

        handleCurrentChange(pageNo) {
            let _this = this
            _this.formInline.pageNo = pageNo
            _this.loadDataList();
        },
        openEdit(row) {
            console.log(row)
            this.drawer = true;
            this.infoData.row = row
            var cityList = row.address.split(",")
            this.infoData.city = [parseInt(cityList[0]), parseInt(cityList[1]), parseInt(cityList[2])]
        },
        openAdvert(row) {
            console.log(row)
            let _this = this;
            this.advertDrawer = true;
            var url = "/oms/device/loadDeviceAdvert";
            sendRequest(url, { deviceId: row.deviceId, }, function(jsonData) {
                console.log(jsonData)
                _this.advertData.list = jsonData.data
            })
        },
        openGame(row) {
            console.log(row)
            let _this = this;
            _this.gameDrawer = true;
            var url = "/oms/device/loadDeviceGame";
            sendRequest(url, { deviceId: row.deviceId, }, function(jsonData) {
                console.log(jsonData)
                _this.gameData.list = jsonData.data
            })
        },

        offline(row) {
            let _this = this
            _this.$confirm('确认将该设备下线么?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var url = "/oms/device/updateStatus";
                var params = { deviceId: row.deviceId, status: 2 };

                sendRequest(url, params, function(jsonData) {
                    if (jsonData.isSuccess) {
                        _this.$message.success("设备已下线");
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
    },
    filters: {
        myAddressCity: function(value) {
            for (y in this.CityInfo) {
                if (this.CityInfo[y].value == value) {
                    return value = this.CityInfo[y].label
                }
            }
        },
        myAddressErae: function(value) {
            for (y in this.CityInfo) {
                for (z in this.CityInfo[y].children) {
                    if (this.CityInfo[y].children[z].value == value && value != undefined) {
                        return value = this.CityInfo[y].children[z].label;
                    }
                }
            }
        },
        myAddressMinerae: function(value) {
            for (y in this.CityInfo) {
                for (z in this.CityInfo[y].children) {
                    for (i in this.CityInfo[y].children[z].children) {
                        if (this.CityInfo[y].children[z].children[i].value == value && value != undefined) {
                            return value = this.CityInfo[y].children[z].children[i].label
                        }
                    }
                }
            }
        },
    },
})