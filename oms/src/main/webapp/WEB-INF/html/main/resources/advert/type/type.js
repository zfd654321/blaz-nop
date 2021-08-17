var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        formInline: {
            name: '',
            screen: '',
            pageSize: 10,
            pageNo: 1
        },
        infoData: {
            row: {
                id: '',
                name: '',
                remarks: '',
                screen: '',
                bundleName: '',
                assetName: '',
                time: '',
                type: '',
                edit: false
            },
            rules: {
                name: [
                    { required: true, message: '请输入模板名称', trigger: 'blur' }
                ],
                remarks: [
                    { required: true, message: '请输入模板备注', trigger: 'blur' }
                ],
                screen: [
                    { required: true, message: '请选择屏幕版本', trigger: 'change' }
                ],
                bundleName: [
                    { required: true, message: '请输入模型文件路径', trigger: 'blur' }
                ],
                assetName: [
                    { required: true, message: '请输入模型文件名称', trigger: 'blur' }
                ],
                time: [
                    { required: true, message: '请输入模板时长', trigger: 'blur' }
                ],
                type: [
                    { required: true, message: '请选择模板类别', trigger: 'change' }
                ],

            }
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
        checkType(row, column) {
            switch (row.type) {
                case 0:
                    return "无额外资源"
                case 1:
                    return "图片轮播"
                case 2:
                    return "视频"
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
            var url = "/oms/advert/typelist";
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
            this.infoData.row.name = ''
            this.infoData.row.remarks = ''
            this.infoData.row.screen = ''
            this.infoData.row.bundleName = ''
            this.infoData.row.assetName = ''
            this.infoData.row.time = ''
            this.infoData.row.type = 0
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
            this.infoData.row.name = row.name
            this.infoData.row.remarks = row.remarks
            this.infoData.row.screen = row.screen
            this.infoData.row.bundleName = row.bundleName
            this.infoData.row.assetName = row.assetName
            this.infoData.row.time = row.time
            this.infoData.row.type = row.type
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
                    var url = "/oms/advert/typesave";
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
        delete(row) {
            console.log(row)
        },
    }
})