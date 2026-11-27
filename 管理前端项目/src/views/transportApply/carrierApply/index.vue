<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
            <el-row :gutter="10" class="mb8">
                <el-form-item label="单据号" prop="serialNo">
                    <el-input v-model="queryParams.serialNo" placeholder="请输入单据号" clearable
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="出库单号" prop="outWarehouseNo">
                    <el-input v-model="queryParams.outWarehouseNo" placeholder="请输入出库单号" clearable
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="发货单号" prop="saleDeliveryNo">
                    <el-input v-model="queryParams.saleDeliveryNo" placeholder="请输入发货单号" clearable
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="客户" prop="customerId">
                    <el-select v-model="queryParams.customerId" placeholder="请选择客户" clearable>
                        <el-option v-for="item in customerIdQuery" :key="item.dictValue" :label="item.dictLabel"
                            :value="item.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="申请人" prop="applyUser">
                    <el-select v-model="queryParams.applyUser" placeholder="请选择申请人" clearable>
                        <el-option v-for="item in applyUserQuery" :key="item.dictValue" :label="item.dictLabel"
                            :value="item.dictValue" />
                    </el-select>
                </el-form-item>
            </el-row>
            <el-row :gutter="10" class="mb8">
                <el-form-item label="申请日期" style="width: 308px">
                    <el-date-picker v-model="daterangeApplyTime" value-format="YYYY-MM-DD" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
                </el-form-item>
                <el-form-item label="申请状态" prop="applyStatus">
                    <el-select v-model="queryParams.applyStatus" placeholder="请选择申请状态" clearable>
                        <el-option v-for="dict in apply_status" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>
                <el-form-item label="审核人" prop="auditUser">
                    <el-select v-model="queryParams.auditUser" placeholder="请选择审核人" clearable>
                        <el-option v-for="item in auditUserQuery" :key="item.dictValue" :label="item.dictLabel"
                            :value="item.dictValue" />
                    </el-select>
                </el-form-item>
                <el-form-item label="审核日期" style="width: 308px">
                    <el-date-picker v-model="daterangeAuditTime" value-format="YYYY-MM-DD" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
                </el-form-item>
                <el-form-item label="审核状态" prop="auditStatus">
                    <el-select v-model="queryParams.auditStatus" placeholder="请选择审核状态" clearable>
                        <el-option v-for="dict in audit_status" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>
            </el-row>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['transportApply:carrierApply:edit']">修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['transportApply:carrierApply:remove']">删除
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="carrierApplyList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="编号" align="center" prop="id" />
            <el-table-column label="单据号" align="center" prop="serialNo" />
            <el-table-column label="客户" align="center" prop="customerId" :formatter="customerIdFormat" />
            <el-table-column label="申请人" align="center" prop="applyUser" :formatter="applyUserFormat" />
            <el-table-column label="申请日期" align="center" prop="applyTime" width="180">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.applyTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="申请状态" align="center" prop="applyStatus">
                <template #default="scope">
                    <dict-tag :options="apply_status" :value="scope.row.applyStatus" />
                </template>
            </el-table-column>
            <el-table-column label="审核人" align="center" prop="auditUser" :formatter="auditUserFormat" />
            <el-table-column label="审核日期" align="center" prop="auditTime" width="180">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.auditTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="审核状态" align="center" prop="auditStatus">
                <template #default="scope">
                    <dict-tag :options="audit_status" :value="scope.row.auditStatus" />
                </template>
            </el-table-column>
            <el-table-column label="出库单号" align="center" prop="outWarehouseNo" />
            <el-table-column label="收货地址" align="center" prop="shippingAddress" />
            <el-table-column label="联系人" align="center" prop="contact" />
            <el-table-column label="联系电话" align="center" prop="contactTel" />
            <el-table-column label="承运人" align="center" prop="carrierUser" :formatter="carrierUserFormat" />
            <el-table-column label="运输距离" align="center" prop="distance" />
            <el-table-column label="交货日期" align="center" prop="deliveryDate" width="180">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.deliveryDate, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['transportApply:carrierApply:edit']">修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['transportApply:carrierApply:remove']">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改产品承运申请对话框 -->
        <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
            <el-form ref="carrierApplyRef" :model="form" :rules="rules" label-width="80px">
                <el-row>
                    <el-form-item label="单据号" prop="serialNo">
                        <el-input v-model="form.serialNo" placeholder="系统自动生成" disabled />
                    </el-form-item>
                    <el-form-item label="出库单号" prop="outWarehouseNo">
                        <el-input v-model="form.outWarehouseNo" placeholder="请输入出库单号" disabled />
                    </el-form-item>

                    <el-form-item label="发货单号" prop="saleDeliveryNo">
                        <el-input v-model="form.saleDeliveryNo" placeholder="请输入发货单号" disabled />
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-form-item label="客户" prop="customerId">
                        <el-select v-model="form.customerId" placeholder="请选择客户" clearable>
                            <el-option v-for="item in customerIdQuery" :key="item.dictValue" :label="item.dictLabel"
                                :value="parseInt(item.dictValue)" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="交货日期" prop="deliveryDate">
                        <el-date-picker clearable v-model="form.deliveryDate" type="date" value-format="YYYY-MM-DD"
                            placeholder="请选择交货日期" />
                    </el-form-item>
                    <el-form-item label="收货地址" prop="shippingAddress">
                        <el-input v-model="form.shippingAddress" placeholder="请输入收货地址" />
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-form-item label="联系人" prop="contact">
                        <el-input v-model="form.contact" placeholder="请输入联系人" />
                    </el-form-item>
                    <el-form-item label="联系电话" prop="contactTel">
                        <el-input v-model="form.contactTel" placeholder="请输入联系电话" />
                    </el-form-item>
                    <el-form-item label="承运人" prop="carrierUser">
                        <el-select v-model="form.carrierUser" placeholder="请选择承运人" clearable>
                            <el-option v-for="item in carrierUserQuery" :key="item.dictValue" :label="item.dictLabel"
                                :value="item.dictValue" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="运输距离" prop="distance">
                        <el-input v-model="form.distance" placeholder="请输入运输距离">
                            <template #append>
                                公里
                            </template>
                        </el-input>
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-form-item label="申请人" prop="applyUser">
                        <el-select v-model="form.applyUser" placeholder="请选择申请人" clearable disabled>
                            <el-option v-for="item in applyUserQuery" :key="item.dictValue" :label="item.dictLabel"
                                :value="item.dictValue" disabled />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="申请日期" prop="applyTime">
                        <el-date-picker clearable v-model="form.applyTime" type="date" value-format="YYYY-MM-DD"
                            placeholder="请选择申请日期" disabled />
                    </el-form-item>
                    <el-form-item label="申请状态" prop="applyStatus">
                        <el-select v-model="form.applyStatus" placeholder="请选择申请状态" disabled>
                            <el-option v-for="dict in apply_status" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-form-item label="审核人" prop="auditUser">
                        <el-select v-model="form.auditUser" placeholder="请选择审核人" clearable disabled>
                            <el-option v-for="item in auditUserQuery" :key="item.dictValue" :label="item.dictLabel"
                                :value="item.dictValue" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="审核日期" prop="auditTime">
                        <el-date-picker clearable v-model="form.auditTime" type="date" value-format="YYYY-MM-DD"
                            disabled placeholder="请选择审核日期" />
                    </el-form-item>
                    <el-form-item label="审核状态" prop="auditStatus">
                        <el-select v-model="form.auditStatus" placeholder="请选择审核状态" disabled>
                            <el-option v-for="dict in audit_status" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="审核意见" prop="auditComment">
                        <el-input type="textarea" v-model="form.auditComment" placeholder="请输入审核意见" disabled />
                    </el-form-item>
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="form.remark" placeholder="请输入备注" />
                    </el-form-item>
                </el-row>

                <el-divider content-position="center">产品承运申请明细信息</el-divider>
                <el-table :data="wmsCarrierApplyDetailList" :row-class-name="rowWmsCarrierApplyDetailIndex"
                    @selection-change="handleWmsCarrierApplyDetailSelectionChange" ref="wmsCarrierApplyDetail">
                    <el-table-column type="selection" width="50" align="center" />
                    <el-table-column label="序号" align="center" prop="index" width="50" />
                    <el-table-column label="物料" prop="materialId" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.materialName" placeholder="请选择物料" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="规格" prop="materialSpecification" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.materialSpecification" placeholder="请选择物料" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="型号" prop="materialModel" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.materialModel" placeholder="请选择物料" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="单位" prop="materialUnit" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.materialUnit" placeholder="请选择物料" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="生产批号" prop="batchNo" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.batchNo" placeholder="请输入生产批号" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="仓库" prop="warehouseId" width="180">
                        <template #default="scope">
                            <el-select v-model="scope.row.warehouseId" placeholder="请选择仓库" clearable disabled>
                                <el-option v-for="item in warehouseIdQuery" :key="item.dictValue"
                                    :label="item.dictLabel" :value="parseInt(item.dictValue)" />
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column label="库区" prop="whRegionId" width="150">
                        <template #default="scope">
                            <el-select v-model="scope.row.whRegionId" placeholder="请选择库区" filterable disabled
                                @change="handleWhRegionChange(scope.row.whRegionId, scope.$index)">
                                <el-option v-for="dict in scope.row.whRegionOptions" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column label="库位" prop="whLocationId" width="150">
                        <template #default="scope">
                            <el-select v-model="scope.row.whLocationId" placeholder="请选择库位" filterable disabled>
                                <el-option v-for="dict in scope.row.whLocationOptions" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column label="发货数量" prop="deliveryQuantity" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.deliveryQuantity" placeholder="请输入发货数量" disabled />
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
    </div>
</template>

<script setup name="CarrierApply">
import { listCarrierApply, getCarrierApply, delCarrierApply, addCarrierApply, updateCarrierApply } from "@/api/transportApply/carrierApply";
import { listWarehouseRegion } from "@/api/warehouse/warehouseRegion";
import { listWarehouseLocation } from "@/api/warehouse/warehouseLocation";
const { proxy } = getCurrentInstance();
const customerIdQuery = ref([]);
proxy.getQueryData("query_customer").then(res => {
    customerIdQuery.value = res.data;
});
const customerIdFormat = (row, column) => {
    return proxy.selectDictLabel(customerIdQuery.value, row.customerId);
};
const applyUserQuery = ref([]);
proxy.getQueryData("query_user").then(res => {
    applyUserQuery.value = res.data;
});
const applyUserFormat = (row, column) => {
    return proxy.selectDictLabel(applyUserQuery.value, row.applyUser);
};
const { apply_status } = proxy.useDict('apply_status');
const auditUserQuery = ref([]);
proxy.getQueryData("query_user").then(res => {
    auditUserQuery.value = res.data;
});
const auditUserFormat = (row, column) => {
    return proxy.selectDictLabel(auditUserQuery.value, row.auditUser);
};
const { audit_status } = proxy.useDict('audit_status');
const carrierUserQuery = ref([]);
proxy.getQueryData("query_user").then(res => {
    carrierUserQuery.value = res.data;
});
const carrierUserFormat = (row, column) => {
    return proxy.selectDictLabel(carrierUserQuery.value, row.carrierUser);
};
const warehouseIdQuery = ref([]);
proxy.getQueryData("query_warehouse").then(res => {
    warehouseIdQuery.value = res.data;
});

const carrierApplyList = ref([]);
const wmsCarrierApplyDetailList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedWmsCarrierApplyDetail = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const daterangeApplyTime = ref([]);
const daterangeAuditTime = ref([]);

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        serialNo: null,
        customerId: null,
        applyUser: null,
        applyTime: null,
        applyStatus: null,
        auditUser: null,
        auditTime: null,
        auditStatus: null,
        outWarehouseId: null,
        outWarehouseNo: null,
        saleDeliveryId: null,
        saleDeliveryNo: null,
        shippingAddress: null,
        contact: null,
        contactTel: null,
        carrierUser: null,
    },
    rules: {
        customerId: [
            { required: true, message: "客户不能为空", trigger: "change" }
        ],
        shippingAddress: [
            { required: true, message: "收货地址不能为空", trigger: "blur" }
        ],
        contact: [
            { required: true, message: "联系人不能为空", trigger: "blur" }
        ],
        contactTel: [
            { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
        carrierUser: [
            { required: true, message: "承运人不能为空", trigger: "change" }
        ],
        distance: [
            { required: true, message: "运输距离不能为空", trigger: "blur" }
        ],
    }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询产品承运申请列表 */
function getList() {
    loading.value = true;
    queryParams.value.params = {};
    if (null != daterangeApplyTime && '' != daterangeApplyTime) {
        queryParams.value.params["beginApplyTime"] = daterangeApplyTime.value[0];
        queryParams.value.params["endApplyTime"] = daterangeApplyTime.value[1];
    }
    if (null != daterangeAuditTime && '' != daterangeAuditTime) {
        queryParams.value.params["beginAuditTime"] = daterangeAuditTime.value[0];
        queryParams.value.params["endAuditTime"] = daterangeAuditTime.value[1];
    }
    listCarrierApply(queryParams.value).then(response => {
        carrierApplyList.value = response.rows;
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
        customerId: null,
        applyUser: null,
        applyTime: null,
        applyStatus: null,
        auditUser: null,
        auditTime: null,
        auditStatus: null,
        outWarehouseId: null,
        outWarehouseNo: null,
        saleDeliveryId: null,
        saleDeliveryNo: null,
        shippingAddress: null,
        contact: null,
        contactTel: null,
        carrierUser: null,
        distance: null,
        remark: null,
        auditComment: null,
        deliveryDate: null,
    };
    wmsCarrierApplyDetailList.value = [];
    proxy.resetForm("carrierApplyRef");
}

/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}

/** 重置按钮操作 */
function resetQuery() {
    daterangeApplyTime.value = [];
    daterangeAuditTime.value = [];
    proxy.resetForm("queryRef");
    handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getCarrierApply(_id).then(response => {
        form.value = response.data;
        wmsCarrierApplyDetailList.value = response.data.wmsCarrierApplyDetailList;
        wmsCarrierApplyDetailList.value.forEach((item, index) => {
            getWhRegionOpts(item.warehouseId, index);
            getWhLocationOpts(item.whRegionId, index);
        });

        open.value = true;
        title.value = "修改产品承运申请";
    });
}

function submitForm() {
    proxy.$refs["carrierApplyRef"].validate(valid => {
        if (valid) {
            form.value.wmsCarrierApplyDetailList = wmsCarrierApplyDetailList.value;
            if (form.value.id != null) {
                updateCarrierApply(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                });
            } else {
                addCarrierApply(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除产品承运申请编号为"' + _ids + '"的数据项？').then(function () {
        return delCarrierApply(_ids);
    }).then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
    }).catch(() => { });
}

/** 产品承运申请明细序号 */
function rowWmsCarrierApplyDetailIndex({ row, rowIndex }) {
    row.index = rowIndex + 1;
}

/** 复选框选中数据 */
function handleWmsCarrierApplyDetailSelectionChange(selection) {
    checkedWmsCarrierApplyDetail.value = selection.map(item => item.index)
}

//获取库区选项
function getWhRegionOpts(val, index) {
    if (!val) {
        return;
    }
    let queryParams = {
        warehouseId: val,
    }
    listWarehouseRegion(queryParams).then(res => {
        wmsCarrierApplyDetailList.value[index].whRegionOptions = res.rows;
    });
}

//获取库位选项
function getWhLocationOpts(val, index) {
    let queryParams = {
        regionId: val,
    }
    listWarehouseLocation(queryParams).then(res => {
        wmsCarrierApplyDetailList.value[index].whLocationOptions = res.rows;
    })
}

getList();
</script>
