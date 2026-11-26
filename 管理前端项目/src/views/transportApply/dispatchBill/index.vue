<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="单据号" prop="serialNo">
                <el-input v-model="queryParams.serialNo" placeholder="请输入单据号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="承运单号" prop="carrierNo">
                <el-input v-model="queryParams.carrierNo" placeholder="请输入承运单号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd"
                    v-hasPermi="['transportApply:dispatchBill:add']">新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['transportApply:dispatchBill:edit']">修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['transportApply:dispatchBill:remove']">删除
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="dispatchBillList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="编号" align="center" prop="id" />
            <el-table-column label="单据号" align="center" prop="serialNo" />
            <el-table-column label="承运单号" align="center" prop="carrierNo" />
            <el-table-column label="客户" align="center" prop="customerId" :formatter="customerIdFormat" />
            <el-table-column label="交货日期" align="center" prop="deliveryDate" />
            <el-table-column label="收货地址" align="center" prop="address" />
            <el-table-column label="联系人" align="center" prop="customerContact" />
            <el-table-column label="联系电话" align="center" prop="customerContactTel" />
            <el-table-column label="承运人" align="center" prop="carrierUser" :formatter="carrierUserIdFormat" />
            <el-table-column label="运输距离" align="center" prop="distance">
                <template #default="scope">
                    <span>{{ scope.row.distance }} 公里</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['transportApply:dispatchBill:edit']">修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['transportApply:dispatchBill:remove']">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改车辆调度单对话框 -->
        <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
            <el-form ref="dispatchBillRef" :model="form" :rules="rules" label-width="80px">
                <el-row>
                    <el-form-item label="单据号" prop="serialNo">
                        <el-input v-model="form.serialNo" placeholder="请输入单据号" />
                    </el-form-item>
                    <el-form-item label="承运单号" prop="carrierApplyId">
                        <el-input v-model="form.carrierNo" placeholder="请选择承运单号" disabled>
                            <template #append>
                                <el-button v-no-more-click @click="selectSelectCarrierBillOpen()">选择</el-button>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="出库单号" prop="outWarehouseId">
                        <el-input v-model="form.outWhNo" placeholder="请输入出库单号" disabled />
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-form-item label="客户" prop="customerId">
                        <el-select v-model="form.customerId" placeholder="请选择客户" clearable disabled>
                            <el-option v-for="item in customerIdQuery" :key="item.dictValue" :label="item.dictLabel"
                                :value="parseInt(item.dictValue)" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="收货地址" prop="address">
                        <el-input v-model="form.address" placeholder="请输入收获地址" disabled />
                    </el-form-item>
                    <el-form-item label="联系人" prop="customerContact">
                        <el-input v-model="form.customerContact" placeholder="请输入联系人" disabled />
                    </el-form-item>
                    <el-form-item label="联系电话" prop="customerContactTel">
                        <el-input v-model="form.customerContactTel" placeholder="请输入联系电话" disabled />
                    </el-form-item>
                    <el-form-item label="交货日期" prop="deliveryDate">
                        <el-date-picker clearable v-model="form.deliveryDate" disabled type="date"
                            value-format="YYYY-MM-DD" placeholder="请选择交货日期" />
                    </el-form-item>
                    <el-form-item label="承运人" prop="outWarehouseId">
                        <el-select v-model="form.carrierUser" placeholder="请选择承运人" clearable disabled>
                            <el-option v-for="item in carrierUserIdQuery" :key="item.dictValue" :label="item.dictLabel"
                                :value="item.dictValue" />
                        </el-select>
                    </el-form-item>
                </el-row>

                <el-form-item label="运输距离" prop="distance">
                    <el-input v-model="form.distance" placeholder="请输入运输距离" disabled>
                        <template #append>
                            <span>公里</span>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" placeholder="请输入备注" />
                </el-form-item>
                <el-divider content-position="center">车辆调度单明细信息</el-divider>
                <el-row :gutter="10" class="mb8">
                    <el-col :span="1.5">
                        <el-button type="primary" icon="Plus" @click="handleAddWmsDispatchBillDetail">添加</el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="danger" icon="Delete" @click="handleDeleteWmsDispatchBillDetail">删除</el-button>
                    </el-col>
                </el-row>
                <el-table :data="wmsDispatchBillDetailList" :row-class-name="rowWmsDispatchBillDetailIndex"
                    @selection-change="handleWmsDispatchBillDetailSelectionChange" ref="wmsDispatchBillDetail">
                    <el-table-column type="selection" width="50" align="center" />
                    <el-table-column label="序号" align="center" prop="index" width="50" />
                    <el-table-column label="车牌号" prop="plateNo" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.plateNo" placeholder="请输入车牌号" />
                        </template>
                    </el-table-column>
                    <el-table-column label="驾驶员" prop="driver" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.driver" placeholder="请输入驾驶员" />
                        </template>
                    </el-table-column>
                    <el-table-column label="联系电话" prop="contactTel" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.contactTel" placeholder="请输入联系电话" />
                        </template>
                    </el-table-column>
                    <el-table-column label="载重" prop="load" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.load" placeholder="请输入载重" />
                        </template>
                    </el-table-column>
                </el-table>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </el-dialog>
        <SelectCarrierBill :open="SelectCarrierBillOpen" @onCancel="handleSelectCarrierBillCancel"
            @onSelected="handleSelectCarrierBillSelected">
        </SelectCarrierBill>
    </div>
</template>

<script setup name="DispatchBill">
import { listDispatchBill, getDispatchBill, delDispatchBill, addDispatchBill, updateDispatchBill, exportDispatchBill } from "@/api/transportApply/dispatchBill";
import { ElMessageBox } from 'element-plus';
import SelectCarrierBill from "@/components/transportApply/SelectCarrierBill.vue";
const { proxy } = getCurrentInstance();
const SelectCarrierBillOpen = ref(false);
const handleSelectCarrierBillCancel = () => {
    SelectCarrierBillOpen.value = false;
};
const selectSelectCarrierBillOpen = () => {
    SelectCarrierBillOpen.value = true;
};
const handleSelectCarrierBillSelected = (row) => {
    let f = form.value;
    let carrierBill = row;
    f.outWarehouseId = carrierBill.outWarehouseId;
    f.outWhNo = carrierBill.outWarehouseNo;
    f.customerId = carrierBill.customerId;
    f.address = carrierBill.shippingAddress;
    f.customerContact = carrierBill.contact;
    f.customerContactTel = carrierBill.contactTel;
    f.carrierUser = carrierBill.carrierUser;
    f.distance = carrierBill.distance;
    f.carrierApplyId = carrierBill.id;
    f.deliveryDate = carrierBill.deliveryDate;
    SelectCarrierBillOpen.value = false;
};

const customerIdQuery = ref([]);

proxy.getQueryData("query_customer").then(res => {
    customerIdQuery.value = res.data;
});
const customerIdFormat = (row, column) => {
    return proxy.selectDictLabel(customerIdQuery.value, row.customerId);
};

const carrierUserIdQuery = ref([]);

proxy.getQueryData("query_user").then(res => {
    carrierUserIdQuery.value = res.data;
});
const carrierUserIdFormat = (row, column) => {
    return proxy.selectDictLabel(carrierUserIdQuery.value, row.carrierUser);
};

const dispatchBillList = ref([]);
const wmsDispatchBillDetailList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedWmsDispatchBillDetail = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        serialNo: null,
        carrierApplyId: null,
    },
    rules: {
        carrierApplyId: [
            { required: true, message: "承运单号不能为空", trigger: "blur" }
        ],
    }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询车辆调度单列表 */
function getList() {
    loading.value = true;
    listDispatchBill(queryParams.value).then(response => {
        dispatchBillList.value = response.rows;
        total.value = response.total;
        loading.value = false;
    });
}

// 取消按钮
function cancel() {
    open.value = false;
    reset();
}

// 表单重置
function reset() {
    form.value = {
        id: null,
        serialNo: null,
        carrierApplyId: null,
        outWarehouseId: null,
        saleOrderId: null,
        remark: null,
    };
    wmsDispatchBillDetailList.value = [];
    proxy.resetForm("dispatchBillRef");
}

/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}

/** 重置按钮操作 */
function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加车辆调度单";
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getDispatchBill(_id).then(response => {
        form.value = response.data;
        wmsDispatchBillDetailList.value = response.data.wmsDispatchBillDetailList;
        open.value = true;
        title.value = "修改车辆调度单";
    });
}

function submitForm() {
    proxy.$refs["dispatchBillRef"].validate(valid => {
        if (valid) {
            form.value.wmsDispatchBillDetailList = wmsDispatchBillDetailList.value;
            if (form.value.id != null) {
                updateDispatchBill(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                });
            } else {
                addDispatchBill(form.value).then(response => {
                    proxy.$modal.msgSuccess("新增成功");
                    open.value = false;
                    getList();
                });
            }
        }
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal.confirm('是否确认删除车辆调度单编号为"' + _ids + '"的数据项？').then(function () {
        return delDispatchBill(_ids);
    }).then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
    }).catch(() => { });
}

/** 车辆调度单明细序号 */
function rowWmsDispatchBillDetailIndex({ row, rowIndex }) {
    row.index = rowIndex + 1;
}

/** 车辆调度单明细添加按钮操作 */
function handleAddWmsDispatchBillDetail() {
    let obj = {};
    obj.dispatchBillNo = "";
    obj.plateNo = "";
    obj.driver = "";
    obj.contactTel = "";
    obj.load = "";
    obj.remark = "";
    wmsDispatchBillDetailList.value.push(obj);
}

/** 车辆调度单明细删除按钮操作 */
function handleDeleteWmsDispatchBillDetail() {
    if (checkedWmsDispatchBillDetail.value.length == 0) {
        proxy.$modal.msgError("请先选择要删除的车辆调度单明细数据");
    } else {
        const wmsDispatchBillDetails = wmsDispatchBillDetailList.value;
        const checkedWmsDispatchBillDetails = checkedWmsDispatchBillDetail.value;
        wmsDispatchBillDetailList.value = wmsDispatchBillDetails.filter(function (item) {
            return checkedWmsDispatchBillDetails.indexOf(item.index) == -1
        });
    }
}

/** 复选框选中数据 */
function handleWmsDispatchBillDetailSelectionChange(selection) {
    checkedWmsDispatchBillDetail.value = selection.map(item => item.index)
}

/** 导出按钮操作 */
function handleExport() {
    ElMessageBox.confirm('是否确认导出所有车辆调度单数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
    }).then(function () {
        return exportDispatchBill(queryParams.value).then(res => {
            proxy.download(res.msg);
        });
    })
}

getList();
</script>
