<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="申请单号" prop="serialNo">
                <el-input v-model="queryParams.serialNo" placeholder="请输入申请单号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="业务类型" prop="bizType">
                <el-select v-model="queryParams.bizType" placeholder="请选择业务类型" clearable>
                    <el-option v-for="dict in event_type" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="供应商" prop="supplierId">
                <el-select v-model="queryParams.supplierId" placeholder="请选择供应商" clearable>
                    <el-option v-for="item in supplierIdQuery" :key="item.dictValue" :label="item.dictLabel"
                        :value="item.dictValue" />
                </el-select>
            </el-form-item>
            <el-form-item label="合同类型" prop="contractType">
                <el-select v-model="queryParams.contractType" placeholder="请选择合同类型" clearable>
                    <el-option v-for="dict in contract_type" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="申请人" prop="applyUser">
                <el-select v-model="queryParams.applyUser" placeholder="请选择申请人" clearable>
                    <el-option v-for="item in applyUserQuery" :key="item.dictValue" :label="item.dictLabel"
                        :value="item.dictValue" />
                </el-select>
            </el-form-item>
            <el-form-item label="申请日期" prop="applyTime">
                <el-date-picker clearable v-model="queryParams.applyTime" type="date" value-format="YYYY-MM-DD"
                    placeholder="请选择申请日期" />
            </el-form-item>
            <el-form-item label="申请状态" prop="applyStatus">
                <el-select v-model="queryParams.applyStatus" placeholder="请选择申请状态" clearable>
                    <el-option v-for="dict in apply_status" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="审核人" prop="auditUser">
                <el-select v-model="queryParams.auditUser" placeholder="请选择审核人" clearable>
                    <el-option v-for="item in auditUserQuery" :key="item.dictValue" :label="item.dictLabel"
                        :value="item.dictValue" />
                </el-select>
            </el-form-item>
            <el-form-item label="审核日期" prop="auditTime">
                <el-date-picker clearable v-model="queryParams.auditTime" type="date" value-format="YYYY-MM-DD"
                    placeholder="请选择审核日期" />
            </el-form-item>
            <el-form-item label="审核状态" prop="auditStatus">
                <el-select v-model="queryParams.auditStatus" placeholder="请选择审核状态" clearable>
                    <el-option v-for="dict in audit_status" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="billStatus">
                <el-select v-model="queryParams.billStatus" placeholder="请选择状态" clearable>
                    <el-option v-for="dict in in_warehouse_status" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd"
                    v-hasPermi="['wmsApply:inWarahouseApply:add']">新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['wmsApply:inWarahouseApply:edit']">修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['wmsApply:inWarahouseApply:remove']">删除
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="inWarahouseApplyList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="编号" align="center" prop="id" />
            <el-table-column label="关联单据号" align="center" prop="bizBillNo" />
            <el-table-column label="供应商" align="center" prop="supplierId" :formatter="supplierIdFormat" />
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
            <el-table-column label="状态" align="center" prop="billStatus">
                <template #default="scope">
                    <dict-tag :options="in_warehouse_status" :value="scope.row.billStatus" />
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <!-- 
                    情况1：申请状态为“待审核”，操作【修改】；
                    情况2：申请状态为“已审核”并且入库状态“已入库”，操作【修改】；
                    情况3：申请状态为“已审核”并且入库状态“未入库”，操作【入库】【修改】，其中点击【入库】按钮，提示“入库成功”字样；
                    情况4：申请状态为“未提交”，操作【修改】【删除】。
                -->
                <template #default="scope">
                    <el-button v-if="scope.row.auditStatus == '1' && scope.row.billStatus != '1'" type="text"
                        icon="el-icon-caret-right" @click="handleInWarehouse(scope.row)"
                        v-hasPermi="['wmsApply:inWarahouseApply:edit']">入库</el-button>
                   
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['wmsApply:inWarahouseApply:edit']">修改
                    </el-button>
                   
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                        v-if="scope.row.auditStatus != '1' && scope.row.applyStatus != '1'"
                        v-hasPermi="['wmsApply:inWarahouseApply:remove']">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改入库申请对话框 -->
         <!-- 申请状态为“未提交”时不可修改任何数据 -->
        <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
            <el-form ref="inWarahouseApplyRef" :model="form" :rules="rules" label-width="92px">
                <el-form-item label="申请单号" prop="serialNo">
                    <el-input v-model="form.serialNo" placeholder="请输入申请单号" :disabled="isUnsubmitted" />
                </el-form-item>
                <el-form-item label="关联单据号" prop="bizBillId">
                    <el-input v-model="form.bizBillNo" placeholder="请选择关联单据号" disabled>
                        <template #append>
                            <el-button v-no-more-click @click="selectBizBillNoOpen()" :disabled="isUnsubmitted">选择</el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <el-row>
                    <el-form-item label="业务类型" prop="bizType">
                        <el-select v-model="form.bizType" placeholder="请选择业务类型" :disabled="isUnsubmitted">
                            <el-option v-for="dict in event_type" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="供应商" prop="supplierId" v-if="form.bizType === '2'">
                        <el-select v-model="form.supplierId" placeholder="请选择供应商" clearable :disabled="isUnsubmitted">
                            <el-option v-for="item in supplierIdQuery" :key="item.dictValue" :label="item.dictLabel"
                                :value="parseInt(item.dictValue)" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="合同编号" prop="contractId" v-if="form.bizType !== '1'">
                        <el-input v-model="form.contractNo" placeholder="请输入合同编号" disabled />
                    </el-form-item>
                    <el-form-item label="合同类型" prop="contractType" v-if="form.bizType !== '1'">
                        <el-select v-model="form.contractType" placeholder="请选择合同类型" :disabled="isUnsubmitted">
                            <el-option v-for="dict in contract_type" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-row>
                <div>
                    <el-row>
                        <el-form-item label="申请类型" prop="applyType">
                            <el-select v-model="form.applyType" placeholder="请选择申请类型" disabled>
                                <el-option v-for="dict in wms_apply_type" :key="dict.value" :label="dict.label"
                                    :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="申请人" prop="applyUser">
                            <el-select v-model="form.applyUser" placeholder="请选择申请人" clearable disabled>
                                <el-option v-for="item in applyUserQuery" :key="item.dictValue" :label="item.dictLabel"
                                    :value="item.dictValue" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="申请日期" prop="applyTime" disabled>
                            <el-date-picker clearable v-model="form.applyTime" type="date" value-format="YYYY-MM-DD"
                                placeholder="请选择申请日期" disabled />
                        </el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="申请状态" prop="applyStatus">
                            <el-select v-model="form.applyStatus" placeholder="请选择申请状态" disabled>
                                <el-option v-for="dict in apply_status" :key="dict.value" :label="dict.label"
                                    :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="审核人" prop="auditUser">
                            <el-select v-model="form.auditUser" placeholder="请选择审核人" clearable disabled>
                                <el-option v-for="item in auditUserQuery" :key="item.dictValue" :label="item.dictLabel"
                                    :value="item.dictValue" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="审核日期" prop="auditTime">
                            <el-date-picker clearable v-model="form.auditTime" type="date" value-format="YYYY-MM-DD"
                                placeholder="请选择审核日期" disabled />
                        </el-form-item>
                        <el-form-item label="审核状态" prop="auditStatus">
                            <el-select v-model="form.auditStatus" placeholder="请选择审核状态" disabled>
                                <el-option v-for="dict in audit_status" :key="dict.value" :label="dict.label"
                                    :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="审核意见">
                            <el-input type="textarea" v-model="form.auditComment" :disabled="true" />
                        </el-form-item>
                    </el-row>
                </div>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" placeholder="请输入备注" :disabled="isUnsubmitted" />
                </el-form-item>
                <el-form-item label="状态" prop="billStatus">
                    <el-select v-model="form.billStatus" placeholder="请选择状态" disabled>
                        <el-option v-for="dict in in_warehouse_status" :key="dict.value" :label="dict.label"
                            :value="dict.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-divider content-position="center">入库申请明细信息</el-divider>
                <el-table :data="wmsInWarehouseApplyDetailList" :row-class-name="rowWmsInWarehouseApplyDetailIndex"
                    @selection-change="handleWmsInWarehouseApplyDetailSelectionChange" ref="wmsInWarehouseApplyDetail">
                    <el-table-column type="selection" width="50" align="center" />
                    <el-table-column label="序号" align="center" prop="index" width="50" />
                    <el-table-column label="物料" prop="materialName" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.materialName" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="仓库" prop="warehouseId" width="180">
                        <template #default="scope">
                            <el-select v-model="scope.row.warehouseId" placeholder="请选择仓库" clearable :disabled="isUnsubmitted">
                                <el-option v-for="item in warehouseIdQuery" :key="item.dictValue"
                                    :label="item.dictLabel" :value="parseInt(item.dictValue)" />
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column label="库区" prop="whRegionId" width="200">
                        <template #default="scope">
                            <el-form-item :prop="'wmsWarehouseMaterialDetailList.' + scope.$index + '.whRegionId'"
                                :rules="rules.whRegionId">
                                <el-select v-model="scope.row.whRegionId" placeholder="请选择库区" filterable :disabled="isUnsubmitted"
                                    @change="handleWhRegionChange(scope.row.whRegionId, scope.$index)">
                                    <el-option v-for="dict in scope.row.whRegionOptions" :key="dict.id"
                                        :label="dict.name" :value="dict.id" />
                                </el-select>
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="库位" prop="whLocationId" width="200">
                        <template #default="scope">
                            <el-form-item :prop="'wmsWarehouseMaterialDetailList.' + scope.$index + '.whLocationId'"
                                :rules="rules.whLocationId">
                                <el-select v-model="scope.row.whLocationId" placeholder="请选择库位" filterable :disabled="isUnsubmitted">
                                    <el-option v-for="dict in scope.row.whLocationOptions" :key="dict.id"
                                        :label="dict.name" :value="dict.id" />
                                </el-select>
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="规格" prop="materialSpecification" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.materialSpecification" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="型号" prop="materialModel" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.materialModel" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="单位" prop="materialUnit" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.materialUnit" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column label="到货数量" prop="totalQuantity" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.totalQuantity" placeholder="请输入到货数量" :disabled="isUnsubmitted" />
                        </template>
                    </el-table-column>
                    <el-table-column label="入库数量" prop="inQuantity" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.inQuantity" placeholder="请输入入库数量" :disabled="isUnsubmitted" />
                        </template>
                    </el-table-column>
                    <el-table-column label="生产批号" prop="batchNo" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.batchNo" placeholder="请输入生产批号" :disabled="isUnsubmitted" />
                        </template>
                    </el-table-column>
                    <el-table-column label="生产日期" prop="productDate" width="240">
                        <template #default="scope">
                            <el-date-picker clearable v-model="scope.row.productDate" type="date"
                                value-format="YYYY-MM-DD" placeholder="请选择生产日期" :disabled="isUnsubmitted" />
                        </template>
                    </el-table-column>
                    <el-table-column label="生产单位" prop="manufacturer" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.manufacturer" placeholder="请输入生产单位" :disabled="isUnsubmitted" />
                        </template>
                    </el-table-column>
                </el-table>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm" :disabled="isUnsubmitted">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </el-dialog>

        <ProductFinishSelect :open="productFinishSelectOpen" @onCancel="handleProductFinishSelectCancel"
            @onSelected="handleProductFinishSelectSelected">
        </ProductFinishSelect>
        <ArriveSelect :open="arriveSelectOpen" @onSelected="handleArriveSelectSelected"
            @onCancel="handleArriveSelectCancel">
        </ArriveSelect>
        <ReturnSelect :open="returnSelectOpen" @onReturnSelected="handleSaleReturnSelectd"
            @onReturnCancel="handleSaleReturnCancel"></ReturnSelect>
    </div>
</template>

<script setup name="InWarahouseApply">
import { listInWarahouseApply, getInWarahouseApply, delInWarahouseApply, addInWarahouseApply, updateInWarahouseApply, inWarehouse } from "@/api/wmsApply/inWarahouseApply";
import { listWarehouseRegion } from "@/api/warehouse/warehouseRegion";
import { listWarehouseLocation } from "@/api/warehouse/warehouseLocation";
import ProductFinishSelect from "@/components/product/ProductFinishSelect";
import ArriveSelect from "@/components/Purchase/ArriveSelect";
import ReturnSelect from "@/components/Sale/ReturnSelect";
import { getArrive } from "@/api/purchase/arrive";
import { getReturn } from "@/api/purchase/return";

const { proxy } = getCurrentInstance();

const event_type = ref([]);

proxy.getDicts('event_type').then(response => {
    // 生产入库、采购入库、退货入库（由于选项有5个，此处我们使用它们的dictValue来筛选需要的选项）
    event_type.value = response.data.filter(item => item.dictValue === '1' || item.dictValue === '2' || item.dictValue === '5').map(item => {
        return {
            label: item.dictLabel,
            value: item.dictValue
        }; // 映射回useDict格式
    });
});


const supplierIdQuery = ref([]);
proxy.getQueryData("query_supplier").then(res => {
    supplierIdQuery.value = res.data;
});
const supplierIdFormat = (row, column) => {
    return proxy.selectDictLabel(supplierIdQuery.value, row.supplierId);
};
const { contract_type } = proxy.useDict('contract_type');
const { wms_apply_type } = proxy.useDict('wms_apply_type');
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
const { in_warehouse_status } = proxy.useDict('in_warehouse_status');
const warehouseIdQuery = ref([]);
proxy.getQueryData("query_warehouse").then(res => {
    warehouseIdQuery.value = res.data;
});

const productFinishSelectOpen = ref(false);
const arriveSelectOpen = ref(false);
const returnSelectOpen = ref(false);

const handleProductFinishSelectCancel = () => {
    productFinishSelectOpen.value = false;
};

const handleArriveSelectCancel = () => {
    arriveSelectOpen.value = false;
};

const handleSaleReturnCancel = () => {
    returnSelectOpen.value = false;
}

const selectBizBillNoOpen = () => {
    let f = form.value;
    if (f.bizType === '1') { // 生产入库
        productFinishSelectOpen.value = true;
    } else if (f.bizType === '2') { // 采购入库
        arriveSelectOpen.value = true;
    } else if (f.bizType === '5') { // 退货入库
        returnSelectOpen.value = true;
    }
};

const handleProductFinishSelectSelected = (row) => { // 生成完工单自带了物料信息，直接加入到详细
    let bill = row;
    let f = form.value;
    f.bizBillId = bill.id;
    f.bizBillNo = bill.serialNo;
    let list = [];
    list.push({
        materialId: bill.materialId,
        batchNo: bill.batchNo,
        materialName: bill.materialName,
        materialModel: bill.materialModel,
        materialSpecification: bill.materialSpecification,
        materialUnit: bill.materialUnit,
        warehouseId: null,
        whLocationId: null,
        whRegionId: null,
        whRegionOptions: [],
        whLocationOptions: [],
        totalQuantity: bill.finishQuantity,
        inQuantity: bill.finishQuantity,
        materialCode: bill.materialCode,
    });
    wmsInWarehouseApplyDetailList.value = list;
    productFinishSelectOpen.value = false;
};

const handleArriveSelectSelected = (row) => {
    let arrive = row[0];
    let f = form.value;
    // 设置基础信息
    f.contractId = arrive.contractId;
    f.contractNo = arrive.contractNo;
    f.bizBillId = arrive.id;
    f.bizBillNo = arrive.arriveNo;
    f.contractType = '1';
    // 添加物料档案
    getArrive(f.bizBillId).then(response => {
        let detailList = response.data.scmPurchaseArriveDetailList
        let list = [];
        detailList.forEach(item => {
            let arriveQuantity = item.arriveQuantity ? item.arriveQuantity : 0;
            let returnQuantity = item.returnQuantity ? item.returnQuantity : 0;

            list.push({
                materialId: item.materialId,
                batchNo: item.batchNo,
                warehouseId: null,
                whLocationId: null,
                whRegionId: null,
                materialName: item.materialName,
                materialModel: item.materialModel,
                materialSpecification: item.materialSpecification,
                materialUnit: item.materialUnit,
                whRegionOptions: [],
                whLocationOptions: [],
                totalQuantity: arriveQuantity - returnQuantity,
                inQuantity: arriveQuantity - returnQuantity,
            });
        });
    });
    arriveSelectOpen.value = false;
};

const handleSaleReturnSelectd = (row) => {
    let returnd = row[0];
    let f = form.value;
    f.contractId = returnd.contractId;
    f.contractNo = returnd.contractNo;
    f.bizBillId = returnd.id;
    f.bizBillNo = returnd.returnNo;
    getReturn(f.bizBillId).then(response => {
        let detailList = response.data.scmPurchaseArriveDetailList
        let list = [];
        detailList.forEach(item => {
            let arriveQuantity = item.arriveQuantity ? item.arriveQuantity : 0;
            let returnQuantity = item.returnQuantity ? item.returnQuantity : 0;

            list.push({
                materialId: item.materialId,
                batchNo: item.batchNo,
                warehouseId: null,
                whLocationId: null,
                whRegionId: null,
                materialName: item.materialName,
                materialModel: item.materialModel,
                materialSpecification: item.materialSpecification,
                materialUnit: item.materialUnit,
                whRegionOptions: [],
                whLocationOptions: [],
                totalQuantity: arriveQuantity - returnQuantity,
                inQuantity: arriveQuantity - returnQuantity,
            });
        });
    });
    returnSelectOpen.value = false;
}

const inWarahouseApplyList = ref([]);
const wmsInWarehouseApplyDetailList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedWmsInWarehouseApplyDetail = ref([]);
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
        bizType: null,
        supplierId: null,
        contractType: null,
        applyType: null,
        applyUser: null,
        applyTime: null,
        applyStatus: null,
        auditUser: null,
        auditTime: null,
        auditStatus: null,
        billStatus: null,
    },
    rules: {
    }
});

const { queryParams, form, rules } = toRefs(data);
const isUnsubmitted = computed(() => form.value.applyStatus === '0' || form.value.applyStatus === 0);

/** 查询入库申请列表 */
function getList() {
    loading.value = true;
    listInWarahouseApply(queryParams.value).then(response => {
        inWarahouseApplyList.value = response.rows;
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
        bizBillId: null,
        bizType: null,
        supplierId: null,
        contractId: null,
        contractType: null,
        applyType: null,
        applyUser: null,
        applyTime: null,
        applyStatus: null,
        auditUser: null,
        auditTime: null,
        auditStatus: null,
        auditComment: null,
        remark: null,
        billStatus: null,
    };
    wmsInWarehouseApplyDetailList.value = [];
    proxy.resetForm("inWarahouseApplyRef");
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
    title.value = "添加入库申请";
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getInWarahouseApply(_id).then(response => {
        form.value = response.data;
        wmsInWarehouseApplyDetailList.value = response.data.wmsInWarehouseApplyDetailList;
        wmsInWarehouseApplyDetailList.value.forEach((item, index) => {
            getWhRegionOpts(item.warehouseId, index);
            getWhLocationOpts(item.whRegionId, index);
        })
        open.value = true;
        title.value = "修改入库申请";
    });
}

function submitForm() {
    proxy.$refs["inWarahouseApplyRef"].validate(valid => {
        if (valid) {
            form.value.wmsInWarehouseApplyDetailList = wmsInWarehouseApplyDetailList.value;
            if (form.value.id != null) {
                updateInWarahouseApply(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功");
                    open.value = false;
                    getList();
                });
            } else {
                addInWarahouseApply(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除入库申请编号为"' + _ids + '"的数据项？').then(function () {
        return delInWarahouseApply(_ids);
    }).then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
    }).catch(() => { });
}

/** 入库申请明细序号 */
function rowWmsInWarehouseApplyDetailIndex({ row, rowIndex }) {
    row.index = rowIndex + 1;
}

/** 入库申请明细添加按钮操作 */
function handleAddWmsInWarehouseApplyDetail() {
    let obj = {};
    obj.materialId = "";
    obj.warehouseId = "";
    obj.whLocationId = "";
    obj.whRegionId = "";
    obj.totalQuantity = "";
    obj.inQuantity = "";
    obj.batchNo = "";
    obj.productDate = "";
    obj.manufacturer = "";
    wmsInWarehouseApplyDetailList.value.push(obj);
}

/** 入库申请明细删除按钮操作 */
function handleDeleteWmsInWarehouseApplyDetail() {
    if (checkedWmsInWarehouseApplyDetail.value.length == 0) {
        proxy.$modal.msgError("请先选择要删除的入库申请明细数据");
    } else {
        const wmsInWarehouseApplyDetails = wmsInWarehouseApplyDetailList.value;
        const checkedWmsInWarehouseApplyDetails = checkedWmsInWarehouseApplyDetail.value;
        wmsInWarehouseApplyDetailList.value = wmsInWarehouseApplyDetails.filter(function (item) {
            return checkedWmsInWarehouseApplyDetails.indexOf(item.index) == -1
        });
    }
}

/** 复选框选中数据 */
function handleWmsInWarehouseApplyDetailSelectionChange(selection) {
    checkedWmsInWarehouseApplyDetail.value = selection.map(item => item.index)
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
        wmsInWarehouseApplyDetailList.value[index].whRegionOptions = res.rows;
    });
}

//获取库位选项
function getWhLocationOpts(val, index) {
    let queryParams = {
        regionId: val,
    }
    listWarehouseLocation(queryParams).then(res => {
        wmsInWarehouseApplyDetailList.value[index].whLocationOptions = res.rows;
    })
}

function handleInWarehouse(row) {
    inWarehouse({ id: row.id }).then(res => {
        this.msgSuccess("入库成功");
        this.getList();
    });
}

getList();
</script>
