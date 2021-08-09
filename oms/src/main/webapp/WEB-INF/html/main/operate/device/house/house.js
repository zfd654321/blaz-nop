var mainVue = new Vue({
    el: '#main',
    data: {
        formInline: {
            deviceId: '',
            pcId: '',
            name: '',
            status: 2,
            pageSize: 10,
            pageNo: 1
        },
        tableData: { list: [], totalCount: 0 },
        freePc: [],
        drawer: false,
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
        configDrawer: false,
        configData: {
            row: {
                deviceId: '',
                kinectLeftAndRightDis: '',
                kinectMinDis: '',
                kinectMaxDis: '',
                hostUrl: '',
                qrcodeUrl: '',
                logoUrl: '',
                watermarkUrl: '',
                themeName: '',
                gameEnterTime: ''
            }
        },

        gameDrawer: false,
        gameData: {
            active: 0,
            gameList: [{
                key: "V10001",
                label: "[V10001] 拳速前进"
            }, {
                key: "V10002",
                label: "[V10002] 招财进宝"
            }, {
                key: "V10003",
                label: "[V10003] 大胃王"
            }],
            gameChecked: ["V10001", "V10002"]
        },


    },
    created: function() {
        this.loadDataList();
        this.loadFreePcList();
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
        loadFreePcList() {
            let _this = this;
            var url = "/oms/device/freePcList";
            sendRequest(url, _this.formInline, function(jsonData) {
                console.log(jsonData)
                _this.infoData.freePc = jsonData.data
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
            let formName = "deviceForm"
            if (this.$refs[formName]) {
                this.resetForm(formName)
            }
            this.infoData.row.deviceId = row.deviceId
            this.infoData.row.pcId = row.pcId
            this.infoData.row.name = row.name
            this.infoData.row.remarks = row.remarks
            this.infoData.row.address = row.address
            this.infoData.row.type = row.type
            this.infoData.row.screen = row.screen
            this.infoData.row.camera = row.camera
            this.infoData.row.edit = true
        },
        openConfig(row) {
            console.log(row)
            let _this = this;
            this.configDrawer = true;
            var url = "/oms/device/loadDeviceConfig";
            sendRequest(url, { deviceId: row.deviceId }, function(jsonData) {
                console.log(jsonData)
                _this.configData.row = jsonData.data
            })
        },
        openGame(row) {
            console.log(row)
            let _this = this;
            this.gameDrawer = true;
            // var url = "/oms/device/loadDeviceConfig";
            // sendRequest(url, { deviceId: row.deviceId }, function(jsonData) {
            //     console.log(jsonData)
            //     _this.gameData.row = jsonData.data
            // })
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        handleClose(done) {
            this.$confirm('确认关闭(未保存的内容将会丢失)？')
                .then(_ => {
                    done();
                })
                .catch(_ => {});
        },
        onSubmit() {
            let _this = this;
            let formName = "deviceForm"
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    var url = "/oms/device/save";
                    var params = _this.infoData.row;

                    sendRequest(url, params, function(jsonData) {
                        if (jsonData.isSuccess) {
                            _this.$message.success("保存成功");
                            _this.drawer = false;
                            _this.loadDataList();
                        } else {
                            _this.$message.error(jsonData.message);
                        }
                    })
                }
            })
        },
        onConfigSubmit() {
            let _this = this;
            let formName = "configForm"
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    var url = "/oms/device/saveDeviceConfig";
                    var params = _this.configData.row;

                    sendRequest(url, params, function(jsonData) {
                        if (jsonData.isSuccess) {
                            _this.$message.success("保存成功");
                            _this.configDrawer = false;
                        } else {
                            _this.$message.error(jsonData.message);
                        }
                    })
                }
            })
        },
        online(row) {
            let _this = this
            _this.$confirm('确认将该设备上线么?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var url = "/oms/device/updateStatus";
                var params = { deviceId: row.deviceId, status: 3 };

                sendRequest(url, params, function(jsonData) {
                    if (jsonData.isSuccess) {
                        _this.$message.success("设备已上线");
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