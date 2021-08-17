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
        loadDataList() {
            let _this = this;
            var url = "/oms/switch/list";
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
        changeStatus(value, deviceId, type) {
            let _this = this
            var url = "/oms/switch/save";
            var params = {
                deviceId: deviceId,
                type: type,
                value: value
            };
            sendRequest(url, params, function(jsonData) {
                if (jsonData.isSuccess) {
                    _this.$message.success("修改设备开关状态成功");
                } else {
                    _this.$message.error(jsonData.message);
                }
            })
        }
    }
})