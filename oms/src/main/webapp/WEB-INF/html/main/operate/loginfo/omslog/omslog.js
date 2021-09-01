var mainVue = new Vue({
    el: '#main',
    data: {
        drawer: false,
        formInline: {
            deviceId: '',
            pcId: '',
            name: '',
            pageSize: 10,
            pageNo: 1
        },
        infoData: {
            row: {
                deviceId: '',
                pcId: '',
                name: '',
            },
            loglist: [],
            reverse: false
        },
        tableData: { list: [], totalCount: 0 },
        ready: false

    },
    created: function() {
        this.doQuery();
    },
    mounted: function() {
        this.ready = true;
    },
    methods: {
        doQuery() {
            this.formInline.pageNo = 1
            this.loadDataList()
        },
        checkType(row, column) {
            switch (row.type) {
                case 1:
                    return "自营"
                case 2:
                    return "商家"

                default:
                    return "未知"
            }
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
        loadDataList() {
            let _this = this;
            var url = "/oms/device/list";
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
        handleCurrentChange(pageNo) {
            let _this = this
            _this.formInline.pageNo = pageNo
            _this.loadDataList();
        },
        openLogs(row) {
            let _this = this;
            var url = "/oms/device/loglist";
            sendRequest(url, { deviceId: row.deviceId }, function(jsonData) {
                var list = jsonData.data
                list.forEach(element => {
                    element.createdName = UserName[element.createdBy]
                    if (element.info != "") {
                        element.infoStr = JSON.stringify(JSON.parse(element.info), null, '\t');
                    } else {
                        element.infoStr = ""
                    }
                });
                console.log(list)
                _this.infoData.loglist = list
                _this.drawer = true
            })
        },
        getUser(id) {
            return UserName[id]
        },

    }
})