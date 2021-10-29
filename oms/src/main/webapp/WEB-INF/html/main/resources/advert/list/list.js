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
                type: '',
                resource: '',
                scount: '',
                resourceList: [],
                edit: false
            },
            rules: {
                id: [
                    { required: true, message: '请输入广告编号', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_]+$/, message: '编号可使用英文、数字和下划线', trigger: 'blur' }
                ],
                name: [
                    { required: true, message: '请输入广告名称', trigger: 'blur' }
                ],
                remarks: [
                    { required: true, message: '请输入广告备注', trigger: 'blur' }
                ],
                screen: [
                    { required: true, message: '请选择屏幕版本', trigger: 'change' }
                ],
                type: [
                    { required: true, message: '请选择广告模板', trigger: 'change' }
                ],
                scount: [
                    { required: true, message: '请输入轮播间隔', trigger: 'blur' }
                ],

            },
            typeRemarks: '',
            typeCheck: [],
            meidaType: 0
        },
        tableData: { list: [], totalCount: 0 },
        typeData: {
            typeSelect: [
                { value: 1, label: '竖屏', children: [] },
                { value: 2, label: '横屏', children: [] },
            ],
            typeList: []
        },
        ready: false

    },
    created: function() {
        this.loadTypeList();
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
            var type = row.type;
            var typeItem = this.typeData.typeList.find(function(item) {
                return item.id === type
            })
            switch (typeItem.screen) {
                case 1:
                    return "竖屏"
                case 2:
                    return "横屏"

                default:
                    return "未知"
            }
        },
        checkType(row, column) {
            var type = row.type;
            var typeItem = this.typeData.typeList.find(function(item) {
                return item.id === type
            })
            return typeItem.name
        },
        getCreater(row, column) {
            return UserName[row.createdBy]
        },
        getUpdater(row, column) {
            return UserName[row.updatedBy]
        },
        loadTypeList() {
            let _this = this;
            var url = "/oms/advert/typelist";
            sendRequest(url, {
                pageSize: 999,
                pageNo: 1
            }, function(jsonData) {
                _this.loadDataList();
                console.log(jsonData)
                var typelist = jsonData.data.list
                _this.typeData.typeList = typelist
                typelist.forEach(type => {
                    var entity = {
                        value: type.id,
                        label: type.name
                    }
                    if (type.screen == 1) {
                        _this.typeData.typeSelect[0].children.push(entity)
                    } else if (type.screen == 2) {
                        _this.typeData.typeSelect[1].children.push(entity)
                    }
                });
            })
        },
        loadDataList() {
            let _this = this;
            var url = "/oms/advert/list";
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
            this.infoData.row.type = ''
            this.infoData.row.resource = ''
            this.infoData.row.scount = ''
            this.infoData.meidaType = 0
            this.infoData.typeCheck = []
            this.infoData.row.resourceList = []
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
            this.infoData.row.scount = row.scount
            this.selectType(row.type)
            if (row.resource != "") {
                var resources = row.resource.split(",")
                resources.forEach(element => {
                    this.infoData.row.resourceList.push({ value: element })
                });
            }

        },
        changeType() {
            var type = this.infoData.typeCheck[1]
            var typeItem = this.typeData.typeList.find(function(item) {
                return item.id === type
            })
            this.infoData.row.type = type
            this.infoData.row.screen = typeItem.screen
            this.infoData.meidaType = typeItem.type
            this.infoData.typeRemarks = typeItem.remarks
            this.infoData.row.resourceList = [{ value: '' }]
        },
        selectType(type) {
            var typeItem = this.typeData.typeList.find(function(item) {
                return item.id === type
            })
            this.infoData.typeCheck = [typeItem.screen, type]
            this.infoData.row.type = type
            this.infoData.row.screen = typeItem.screen
            this.infoData.meidaType = typeItem.type
            this.infoData.typeRemarks = typeItem.remarks
            this.infoData.row.resourceList = []
        },
        addDomain() {
            this.infoData.row.resourceList.push({ value: '' })
        },
        removeDomain(index) {
            this.infoData.row.resourceList.splice(index, 1)
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
                    var url = "/oms/advert/save";
                    var resource = ""
                    _this.infoData.row.resourceList.forEach(element => {
                        if (resource != "") {
                            resource += ","
                        }
                        resource += element.value
                    });
                    _this.infoData.row.resource = resource
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
        deleteRow(row) {
            let _this = this
            _this.$confirm('此操作将永久删除该广告, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'error'
            }).then(() => {
                var url = "/oms/advert/delete";
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
        checkResource(url, element, type) {
            resourceVue.loadResources(url, type, function(item) {
                $("#" + element).val(item.url);
                $("#" + element)[0].dispatchEvent(new Event('input'))
            })
        }
    }
})