var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        updateDrawer: false,
        logDrawer: false,
        loading: false,
        loadingtext: "",
        formInline: {
            id: '',
            screen: '',
            status: '',
            pageSize: 10,
            pageNo: 1
        },
        infoData: {
            row: {
                id: '',
                screen: '',
                versionLog: '',
                edit: false
            },
            rules: {
                id: [
                    { required: true, message: '请输入版本号', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_.]+$/, message: '版本编号可使用英文、数字、点和下划线', trigger: 'blur' }
                ],
                screen: [
                    { required: true, message: '请选择屏幕版本', trigger: 'change' }
                ],
                versionLog: [
                    { required: true, message: '请输入更新日志', trigger: 'blur' }
                ],

            }
        },

        updateData: {
            row: {
                lastVersion: "",
                newVersion: "",
                versionLog: ""
            },
            rules: {
                lastVersion: [
                    { required: true, message: '请输入原版本号', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_.]+$/, message: '版本编号可使用英文、数字、点和下划线', trigger: 'blur' }
                ],
                newVersion: [
                    { required: true, message: '请输入新版本号', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_.]+$/, message: '版本编号可使用英文、数字、点和下划线', trigger: 'blur' }
                ],
                versionLog: [
                    { required: true, message: '请输入更新日志', trigger: 'blur' }
                ],

            },
            active: 0
        },
        logData: {
            list: [],
            reverse: false
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
        checkStatus(row, column) {
            switch (row.status) {
                case 1:
                    return "可用"
                case 2:
                    return "过期删除"
                case 3:
                    return "回滚"

                default:
                    return "未知"
            }
        },
        getStatus(status) {
            switch (status) {
                case 1:
                    return "可用"
                case 2:
                    return "过期删除"
                case 3:
                    return "回滚"

                default:
                    return "未知"
            }
        },
        statusType(status) {
            switch (status) {
                case 1:
                    {
                        return ""
                    }
                case 2:
                    {
                        return "info"
                    }
                case 3:
                    {
                        return "warning"
                    }
                default:
                    return "error"
            }
        },
        getUser(id) {
            return UserName[id]
        },
        getCreater(row, column) {
            return UserName[row.createdBy]
        },
        getUpdater(row, column) {
            return UserName[row.updatedBy]
        },
        loadDataList() {
            let _this = this;
            var url = "/oms/version/list";
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
        openAdd() {
            this.drawer = true;
            let formName = "pcForm"
            if (this.$refs[formName]) {
                this.resetForm(formName)
            }
            this.infoData.row.id = ''
            this.infoData.row.versionLog = ''
        },
        openUpdate() {
            this.updateDrawer = true;
            let formName = "updateForm"
            if (this.$refs[formName]) {
                this.resetForm(formName)
            }
            this.updateData.row.lastVersion = ""
            this.updateData.row.newVersion = ""
            this.updateData.row.versionLog = ""
        },

        openLog() {
            let _this = this
            var url = "/oms/version/list";
            sendRequest(url, { pageSize: 999, pageNo: 1 }, function(jsonData) {
                console.log(jsonData)
                _this.logData.list = jsonData.data.list
                _this.logData.reverse = false
                _this.logDrawer = true
            })
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
            let formName = "pcForm"
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    var url = "/oms/version/save";
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
        updateSubmit() {
            let _this = this;
            let formName = "updateForm"
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    _this.loading = true
                    _this.loadingtext = "正在处理版本文件"
                    var url = "/oms/version/build";
                    var params = _this.updateData.row;
                    sendRequest(url, params, function(jsonData) {
                        if (jsonData.isSuccess) {
                            _this.$message.success("版本文件处理成功");
                            _this.checkVersionFile();
                        } else {
                            _this.$message.error(jsonData.message);
                            _this.loading = false
                        }
                    })
                }
            })
        },
        checkVersionFile() {
            let _this = this;
            _this.loadingtext = "正在校验版本文件"
            var url = "/oms/version/check";
            var params = _this.updateData.row;
            sendRequest(url, params, function(jsonData) {
                if (jsonData.isSuccess) {
                    _this.$message.success("版本文件校验成功");
                    _this.addVersion();
                } else {
                    _this.$message.error(jsonData.message);
                    _this.loading = false
                }
            })
        },
        addVersion() {
            let _this = this;
            _this.loadingtext = "正在保存版本数据"
            var url = "/oms/version/add";
            var params = _this.updateData.row;
            sendRequest(url, params, function(jsonData) {
                if (jsonData.isSuccess) {
                    _this.$message.success("版本数据保存成功");
                    _this.cleanVersion();
                } else {
                    _this.$message.error(jsonData.message);
                    _this.loading = false
                }
            })
        },

        cleanVersion() {
            let _this = this;
            _this.loadingtext = "正在清理过期文件"
            var url = "/oms/version/clean";
            var params = _this.updateData.row;
            sendRequest(url, params, function(jsonData) {
                if (jsonData.isSuccess) {
                    _this.$message.success("过期文件清理成功");
                    _this.loading = false
                    _this.updateDrawer = false;
                    _this.loadDataList();
                } else {
                    _this.$message.error(jsonData.message);
                    _this.loading = false
                }
            })
        },
        deleteRow(row) {
            let _this = this
            _this.$confirm('确认要回滚该版本么?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                var url = "/oms/version/delete";
                var params = { id: row.id };

                sendRequest(url, params, function(jsonData) {
                    if (jsonData.isSuccess) {
                        _this.$message.success("版本回滚成功");
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