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
            menuList: [],
            props: {
                label: 'text',
                children: 'children'
            },


        },
        tableData: { list: [], totalCount: 0 }

    },
    created: function() {
        this.loadDataList();
        this.loadMenuList();
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
                console.log(jsonData)
                _this.tableData.list = jsonData.data.list
                _this.tableData.totalCount = jsonData.data.totalCount
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
            this.infoData.row.edit = false
            this.infoData.row.id = ''
            this.infoData.row.name = ''
            this.infoData.row.remarks = ''
            _this.$refs.tree.setCheckedKeys([]);
        },
        openEdit(row) {
            console.log(row)
            this.drawer = true;
            this.infoData.row.edit = true
            this.infoData.row.id = row.id
            this.infoData.row.name = row.name
            this.infoData.row.remarks = row.remarks
            this.loadRoleMenuList(row.id)
        },
        onSubmit() {
            let _this = this;
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
        },
        delete(row) {
            console.log(row)
        },
    }
})