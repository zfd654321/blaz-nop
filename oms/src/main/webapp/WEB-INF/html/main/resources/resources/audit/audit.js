var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        formInline: {
            id: '',
            remarks: '',
            status: '',
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
                    { required: true, message: '请输入机器码', trigger: 'blur' },
                    { min: 24, max: 24, message: '机器码格式错误', trigger: 'blur' }
                ],
                remarks: [
                    { required: true, message: '请输入备注', trigger: 'blur' }
                ],

            }
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
        checkStatus(row, column) {
            switch (row.status) {
                case 1:
                    return "空闲"
                case 2:
                    return "占用"
                case 3:
                    return "已坏"

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
            var url = "/oms/pc/list";
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
        openEdit(row) {
            console.log(row)
            this.drawer = true;
            let formName = "pcForm"
            if (this.$refs[formName]) {
                this.resetForm(formName)
            }
            this.infoData.row.edit = true
            this.infoData.row.id = row.id
            this.infoData.row.remarks = row.remarks
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
                    var url = "/oms/pc/save";
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