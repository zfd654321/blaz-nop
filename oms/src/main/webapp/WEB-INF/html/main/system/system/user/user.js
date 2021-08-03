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
                nickname: '',
                username: '',
                password: '',
                role: '',
                status: '',
                edit: false
            }

        },
        tableData: { list: [], totalCount: 0 }

    },
    created: function() {
        this.loadDataList();
        this.loadRoleList();
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
                console.log(jsonData)
                _this.tableData.list = jsonData.data.list
                _this.tableData.totalCount = jsonData.data.totalCount
            })
        },
        loadRoleList() {
            let _this = this;
            var url = "/oms/user/list";
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
            this.infoData.row.edit = true
            this.infoData.row.id = row.id
            this.infoData.row.nickname = row.nickname
            this.infoData.row.username = row.username
            this.infoData.row.password = ''
            this.infoData.row.role = row.role
            this.infoData.row.status = row.status + ""
        },
        onSubmit() {
            let _this = this;
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
        },
        delete(row) {
            console.log(row)
        },
    }
})