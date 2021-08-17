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
            rules: {
                deviceId: [
                    { required: true, message: '请输入设备编号', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_]+$/, message: '编号可使用英文、数字和下划线', trigger: 'blur' }
                ],
                pcId: [
                    { required: true, message: '请选择机器码', trigger: 'change' }
                ],
                name: [
                    { required: true, message: '请输入设备名称', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
                ],
                remarks: [
                    { required: true, message: '请输入设备备注', trigger: 'blur' },
                ],
                address: [
                    { required: true, message: '请输入地址', trigger: 'blur' },
                ],

            }
        },
        advertData: {
            deviceId: "",
            list: []
        },
        gameData: {
            deviceId: "",
            list: []
        },
        tableData: { list: [], totalCount: 0 }

    },
    created: function() {
        this.loadDataList();
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
    }
})