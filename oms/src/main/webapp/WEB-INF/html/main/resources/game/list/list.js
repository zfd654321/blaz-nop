var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        logDrawer: false,
        loading: false,
        formInline: {
            id: '',
            name: '',
            screen: '',
            pageSize: 10,
            pageNo: 1
        },
        infoData: {
            row: {
                id: '',
                version: '',
                versionLog: '',
                edit: false
            },
            rules: {
                id: [
                    { required: true, message: '请输入游戏编号', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_]+$/, message: '游戏编号可使用英文、数字和下划线', trigger: 'blur' }
                ],
                version: [
                    { required: true, message: '请输入版本号', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_.]+$/, message: '版本号可使用英文、数字、点号和下划线', trigger: 'blur' }
                ],
                versionLog: [
                    { required: true, message: '请输入更新说明', trigger: 'blur' }
                ],
                jsonFile: [{
                    validator: (rule, value, callback) => {
                        if (mainVue.infoData.jsonFile.length < 1) {
                            callback(new Error('请选择上传json文件'));
                        } else {
                            callback();
                        }

                    },
                    trigger: 'change'
                }],
                zipFile: [{
                    validator: (rule, value, callback) => {
                        if (mainVue.infoData.zipFile.length < 1) {
                            callback(new Error('请选择上传zip文件'));
                        } else {
                            callback();
                        }

                    },
                    trigger: 'change'
                }]
            },
            jsonFile: [],
            zipFile: []
        },
        logData: { list: [], reverse: true },
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
        getCreater(row, column) {
            return UserName[row.createdBy]
        },
        getUpdater(row, column) {
            return UserName[row.updatedBy]
        },
        getUser(id) {
            return UserName[id]
        },
        getStatus(status) {
            switch (status) {
                case 0:
                    {
                        return "已过期删除"
                    }
                case 1:
                    {
                        return "可用版本"
                    }
                case 2:
                    {
                        return "被回滚"
                    }
            }
        },
        statusType(status) {
            switch (status) {
                case 0:
                    {
                        return "info"
                    }
                case 1:
                    {
                        return "success"
                    }
                case 2:
                    {
                        return "danger"
                    }
            }
        },
        loadDataList() {
            let _this = this;
            var url = "/oms/game/list";
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
            this.infoData.row.edit = false
            this.infoData.row.id = ''
            this.infoData.row.version = ''
            this.infoData.jsonFile = []
            this.infoData.zipFile = []
            this.infoData.versionLog = ''
        },
        openEdit(row) {
            console.log(row)
            this.drawer = true;
            let formName = "pcForm"
            if (this.$refs[formName]) {
                this.resetForm(formName)
            }
            this.infoData.row.edit = true
            this.infoData.row.id = row.id
            this.infoData.row.version = ''
            this.infoData.jsonFile = []
            this.infoData.zipFile = []
            this.infoData.versionLog = ''
        },
        showLog(row) {
            let _this = this
            var url = "/oms/game/version";
            sendRequest(url, { id: row.id }, function(jsonData) {
                console.log(jsonData)
                _this.logData.list = jsonData.data
                _this.logData.id = row.id
                _this.logData.reverse = false
                _this.logDrawer = true
            })
        },
        showJson(row) {
            window.open("/files/oms/forever/game/" + row.id + "/" + row.version + "/game.json")
        },
        downZip(row) {
            window.open("/files/oms/forever/game/" + row.id + "/" + row.version + "/game.zip")
        },
        handleClose(done) {
            let _this = this;
            _this.$confirm('确认关闭(未保存的内容将会丢失)？')
                .then(_ => {
                    done();
                    _this.infoData.jsonFile = []
                    _this.infoData.zipFile = []
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
                    var formData = new FormData();
                    formData.append("jsonFile", _this.infoData.jsonFile[0].raw)
                    formData.append("zipFile", _this.infoData.zipFile[0].raw)
                    _this.loading = true
                    $.ajax({
                        url: "/oms/game/save?id=" + _this.infoData.row.id + "&version=" + _this.infoData.row.version + "&edit=" + _this.infoData.row.edit + "&versionLog=" + _this.infoData.row.versionLog,
                        type: 'POST',
                        async: true,
                        cache: false,
                        contentType: false, // 告诉jQuery不要去设置Content-Type请求头
                        processData: false, // 告诉jQuery不要去处理发送的数据
                        data: formData,
                        beforeSend: function(XMLHttpRequest) {},
                        success: function(jsonData) {
                            if (jsonData.isSuccess) {
                                _this.$message.success("保存成功");
                                _this.loading = false;
                                _this.drawer = false;
                                _this.loadDataList();
                            } else {
                                _this.$message.error(jsonData.message);
                            }
                        }
                    })
                }
            })

        },
        jsonOnChange(file, fileList) {
            if (fileList.length > 0) {
                this.infoData.jsonFile = [fileList[fileList.length - 1]] //这一步，是 展示最后一次选择文件
                this.$refs.pcForm.validateField('jsonFile')
            }
        },
        zipOnChange(file, fileList) {
            if (fileList.length > 0) {
                this.infoData.zipFile = [fileList[fileList.length - 1]] //这一步，是 展示最后一次选择文件
                this.$refs.pcForm.validateField('zipFile')
            }
        },

        delete(row) {
            console.log(row)
        },
    }
})