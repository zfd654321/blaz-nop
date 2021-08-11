var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        formInline: {
            name: '',
            type: '',
            status: '',
            pageSize: 5,
            pageNo: 1
        },
        infoData: {
            row: {
                name: '',
                type: '',
            },
            rule: {
                name: [
                    { required: true, message: '请输入资源名称', trigger: 'blur' },
                ],
                type: [
                    { required: true, message: '请选择文件类型', trigger: 'change' }
                ]
            },
            fileList: []
        },
        showData: {
            row: {
                url: "",
                type: ""
            },
            visible: false
        },
        tableData: { list: [], totalCount: 0 },
        dialogImageUrl: '',
        dialogVisible: false,
        disabled: false,
        loading: false

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
                    return "新建"
                case 2:
                    return "审核中"
                case 3:
                    return "审核通过"

                default:
                    return "未知"
            }
        },
        checkType(row, column) {
            switch (row.type) {
                case "img":
                    return "图片"
                case "video":
                    return "视频"
                case "other":
                    return "其他"

                default:
                    return "未知"
            }
        },
        getFileSize(row) {
            var fileSizeByte = row.size;
            var fileSizeMsg = "";
            if (fileSizeByte < 1048576) fileSizeMsg = (fileSizeByte / 1024).toFixed(2) + "KB";
            else if (fileSizeByte == 1048576) fileSizeMsg = "1MB";
            else if (fileSizeByte > 1048576 && fileSizeByte < 1073741824) fileSizeMsg = (fileSizeByte / (1024 * 1024)).toFixed(2) + "MB";
            else if (fileSizeByte > 1048576 && fileSizeByte == 1073741824) fileSizeMsg = "1GB";
            else if (fileSizeByte > 1073741824 && fileSizeByte < 1099511627776) fileSizeMsg = (fileSizeByte / (1024 * 1024 * 1024)).toFixed(2) + "GB";
            else fileSizeMsg = "文件超过1TB";
            return fileSizeMsg;
        },
        getCreater(row, column) {
            return UserName[row.createdBy]
        },
        getUpdater(row, column) {
            return UserName[row.updatedBy]
        },
        loadDataList() {
            let _this = this;
            var url = "/oms/resources/list";
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
        checkAccpt(type) {
            switch (type) {
                case "img":
                    return ".png,.jpg,.gif"
                case "video":
                    return ".mp4,.webm"
                default:
                    return "*"
            }
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
            var formData = new FormData();
            formData.append("file", _this.infoData.fileList[0].raw)
            _this.loading = true
            $.ajax({
                url: "/oms/resources/save?name=" + _this.infoData.row.name + "&type=" + _this.infoData.row.type,
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

        },
        showRow(row) {
            this.showData.row.url = row.url
            this.showData.row.type = row.type
            this.showData.visible = true
        },
        deleteRow(row) {
            let _this = this;
            _this.$confirm('此操作将永久删除该资源,可能影响所有配置该资源的设备,是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                var url = "/oms/resources/delete";
                sendRequest(url, { id: row.id }, function(jsonData) {
                    _this.$message.success("删除成功");
                    _this.loadDataList();
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        handleRemove(file) {
            console.log(file);
        },
        fileOnChange(file, fileList) {
            console.log(file)
            console.log(fileList)
            if (fileList.length > 0) {
                this.infoData.fileList = [fileList[fileList.length - 1]] //这一步，是 展示最后一次选择文件
            }
        }

    }
})