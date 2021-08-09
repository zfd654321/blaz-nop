var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        formInline: {
            deviceId: '',
            pcId: '',
            name: '',
            status: 1,
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

            },
            freePc: []
        },
        tableData: { list: [], totalCount: 0 }

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
        openAdd() {
            this.drawer = true;

            let formName = "deviceForm"
            if (this.$refs[formName]) {
                this.resetForm(formName)
            }
            this.infoData.row.edit = false
            this.infoData.row.deviceId = ''
            this.infoData.row.pcId = ''
            this.infoData.row.name = ''
            this.infoData.row.remarks = ''
            this.infoData.row.address = ''
            this.infoData.row.type = 1
            this.infoData.row.screen = 1
            this.infoData.row.camera = "Kinect2.0"
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
        handleClose(done) {
            this.$confirm('确认关闭(未保存的内容将会丢失)？')
                .then(_ => {
                    done();
                })
                .catch(_ => {});
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
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
        intoHouse(row) {
            let _this = this
            _this.$confirm('确认将该设备入库么?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var url = "/oms/device/intoHouse";
                var params = { deviceId: row.deviceId };

                sendRequest(url, params, function(jsonData) {
                    if (jsonData.isSuccess) {
                        _this.$message.success("入库成功");
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
        deleteRow(row) {
            let _this = this
            _this.$confirm('此操作将永久删除该设备, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                var url = "/oms/device/delete";
                var params = { deviceId: row.deviceId };

                sendRequest(url, params, function(jsonData) {
                    if (jsonData.isSuccess) {
                        _this.$message.success("删除成功");
                        _this.drawer = false;
                        _this.loadDataList();
                    } else {
                        _this.$message.error(jsonData.message);
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
    }
})