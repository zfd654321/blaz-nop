var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        formInline: {
            nickname: '',
            status: '',
            pageSize: 10,
            pageNo: 1,
        },
        infoData: {
            row: {
                id: '',
                nickname: '',
                username: '',
                password: '',
                role: '',
                status: '',
                edit: false
            },
            rules: {
                nickname: [
                    { required: true, message: '请输入用户名称', trigger: 'blur' },
                    { min: 2, max: 10, message: '用户名称长度为2-10个字符', trigger: 'blur' }
                ],
                username: [
                    { required: true, message: '请输入登陆账户', trigger: 'blur' },
                    { min: 2, max: 10, message: '登陆账户长度为2-10个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 2, max: 10, message: '密码长度为2-10个字符', trigger: 'blur' }
                ],
                role: [
                    { required: true, message: '请选择角色', trigger: 'change' }
                ],

            }

        },
        tableData: { list: [], totalCount: 0 },
        ready: false

    },
    created: function() {
        this.loadRoleList();
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
            return row.status == 1 ?
                "可用" :
                "禁用"
        },
        loadDataList() {
            let _this = this;
            var url = "/oms/user/list";
            sendRequest(url, _this.formInline, function(jsonData) {
                var list = jsonData.data.list
                var count = jsonData.data.totalCount
                list.forEach(element => {
                    element.createdName = UserName[element.createdBy]
                    element.updatedName = UserName[element.updatedBy]
                });
                console.log(list)
                _this.tableData.list = list
                _this.tableData.totalCount = count
            })
        },
        loadRoleList() {
            let _this = this;
            var url = "/oms/user/list";
            sendRequest(url, _this.formInline, function(jsonData) {
                console.log(jsonData)
                _this.tableData.list = jsonData.data.list
                _this.tableData.totalCount = jsonData.data.totalCount
                _this.loadDataList();
            })
        },
        handleCurrentChange(pageNo) {
            let _this = this
            _this.formInline.pageNo = pageNo
            _this.loadDataList();
        },
        openAdd() {
            this.drawer = true;
            let formName = "form"
            if (this.$refs[formName]) {
                this.resetForm(formName)
            }
            this.infoData.row.edit = false
            this.infoData.row.id = ''
            this.infoData.row.nickname = ''
            this.infoData.row.username = ''
            this.infoData.row.password = ''
            this.infoData.row.role = ''
            this.infoData.row.status = '1'
        },
        openEdit(row) {
            console.log(row)
            this.drawer = true;
            let formName = "form"
            if (this.$refs[formName]) {
                this.resetForm(formName)
            }
            this.infoData.row.edit = true
            this.infoData.row.id = row.id
            this.infoData.row.nickname = row.nickname
            this.infoData.row.username = row.username
            this.infoData.row.password = ''
            this.infoData.row.role = row.role
            this.infoData.row.status = row.status + ""
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
            let formName = "form"
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    var url = "/oms/user/save";
                    sendRequest(url, _this.infoData.row, function(jsonData) {
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