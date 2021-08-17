var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        logDrawer: false,
        formInline: {
            id: '',
            remarks: '',
            status: "1",
            pageSize: 10,
            pageNo: 1
        },
        infoData: {
            row: {
                id: '',
                remarks: '',
                edit: false
            },
            rules: {
                id: [
                    { required: true, message: '请输入版本编号', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_.]+$/, message: '版本编号可使用英文、数字、点和下划线', trigger: 'blur' }
                ],
                versionLog: [
                    { required: true, message: '请输入更新日志', trigger: 'blur' }
                ],
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
            zipFile: []
        },
        logData: {
            list: [],
            reverse: false
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
        checkStatus(row, column) {
            switch (row.status) {
                case 1:
                    return "正常"
                case 0:
                    return "回滚"

                default:
                    return "未知"
            }
        },
        getCreater(row, column) {
            return UserName[row.createdBy]
        },
        getUser(id) {
            return UserName[id]
        },
        getStatus(status) {
            switch (status) {
                case 0:
                    {
                        return "已回滚"
                    }
                case 1:
                    {
                        return "可用版本"
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
                        return ""
                    }
            }
        },
        loadDataList() {
            let _this = this;
            var url = "/oms/version/downLoaderList";
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
            this.infoData.row.remarks = ''
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
                    var formData = new FormData();
                    formData.append("file", _this.infoData.zipFile[0].raw)
                    _this.loading = true
                    $.ajax({
                        url: "/oms/version/downloaderSave?id=" + _this.infoData.row.id + "&versionLog=" + _this.infoData.row.versionLog,
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
        zipOnChange(file, fileList) {
            if (fileList.length > 0) {
                this.infoData.zipFile = [fileList[fileList.length - 1]] //这一步，是 展示最后一次选择文件
                this.$refs.pcForm.validateField('zipFile')
            }
        },
        downZip(row) {
            window.open(row.url)
        },
        deleteRow(row) {
            let _this = this
            _this.$confirm('确认要回滚该版本么?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                var url = "/oms/version/downloaderDelete";
                var params = { id: row.id };

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
        openLog() {
            let _this = this
            var url = "/oms/version/downLoaderList";
            sendRequest(url, { pageSize: 999, pageNo: 1 }, function(jsonData) {
                console.log(jsonData)
                _this.logData.list = jsonData.data.list
                _this.logData.reverse = false
                _this.logDrawer = true
            })
        },
    }
})