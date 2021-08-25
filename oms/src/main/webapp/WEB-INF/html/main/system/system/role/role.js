var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        formInline: {
            nickname: '',
            status: '',
            pageSize: 10,
            pageNo: 1
        },
        infoData: {
            row: {
                id: '',
                name: '',
                remarks: '',
                edit: false
            },
            rules: {
                id: [
                    { required: true, message: '请输入角色编号', trigger: 'blur' },
                    { min: 2, max: 10, message: '角色编号长度为2-10个字符', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_]+$/, message: '角色编号可使用英文、数字和下划线', trigger: 'blur' }
                ],
                name: [
                    { required: true, message: '请输入角色名称', trigger: 'blur' },
                    { min: 2, max: 10, message: '角色名称长度为2-10个字符', trigger: 'blur' }
                ],
                remarks: [
                    { required: true, message: '请输入角色备注', trigger: 'blur' }
                ],
            },
            menuList: [],
            props: {
                label: 'text',
                children: 'children'
            },


        },
        tableData: { list: [], totalCount: 0 },
        ready: false

    },
    created: function() {
        this.loadDataList();
        this.loadMenuList();
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
            var url = "/oms/role/list";
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
        loadMenuList() {
            let _this = this;
            var url = "/oms/role/menuList";
            sendRequest(url, null, function(jsonData) {
                console.log(jsonData)
                _this.infoData.menuList = jsonData.data
            })
        },
        loadRoleMenuList(id) {
            let _this = this;
            var url = "/oms/role/roleMenuList";
            sendRequest(url, { roleId: id }, function(jsonData) {
                console.log(jsonData)
                let menuList = _this.infoData.menuList;
                let keyList = []
                menuList.forEach(menu => {
                    menu.children.forEach(smenu => {
                        smenu.children.forEach(cmenu => {
                            if (jsonData.data.indexOf(cmenu.id) != -1) {
                                keyList.push(cmenu.id)
                            }
                        })
                    })
                });
                _this.$refs.tree.setCheckedKeys(keyList);
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
            this.infoData.row.name = ''
            this.infoData.row.remarks = ''
            _this.$refs.tree.setCheckedKeys([]);
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
            this.infoData.row.name = row.name
            this.infoData.row.remarks = row.remarks
            this.loadRoleMenuList(row.id)
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
                    var url = "/oms/role/save";
                    var params = _this.infoData.row;
                    var menuList = _this.$refs.tree.getCheckedKeys()
                    var halfList = _this.$refs.tree.getHalfCheckedKeys()
                    var menuIdStr = ""
                    menuList.forEach(menuId => {
                        if (menuIdStr != "") {
                            menuIdStr += ","
                        }
                        menuIdStr += menuId
                    });
                    halfList.forEach(menuId => {
                        if (menuIdStr != "") {
                            menuIdStr += ","
                        }
                        menuIdStr += menuId
                    });

                    params.menuIdStr = menuIdStr

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