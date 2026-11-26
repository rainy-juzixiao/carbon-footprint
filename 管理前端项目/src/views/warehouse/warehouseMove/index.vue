<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="物料" prop="materialName">
                <el-input v-model="queryParams.materialName" placeholder="请输入物料" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="源仓库" prop="sourceWarehouseId">
                <el-select v-model="queryParams.sourceWarehouseId" placeholder="请选择源仓库" clearable filterable
                    @change="handleWarehouseChange(queryParams.sourceWarehouseId, true)">
                    <el-option v-for="dict in sourceWarehouseIdQuery" :key="dict.dictValue" :label="dict.dictLabel"
                        :value="parseInt(dict.dictValue)"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="源库区" prop="sourceRegionId">
                <el-select v-model="queryParams.sourceRegionId" placeholder="请选择库区" filterable
                    @change="handleWhRegionChange(queryParams.sourceRegionId, true)">
                    <el-option v-for="dict in queryParams.sourceWhRegionOptions" :key="dict.id" :label="dict.name"
                        :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="源库位" prop="sourceLocationId">
                <el-select v-model="queryParams.sourceLocationId" placeholder="请选择库位" filterable>
                    <el-option v-for="dict in queryParams.sourceWhLocationOptions" :key="dict.id" :label="dict.name"
                        :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="批号" prop="batchNo">
                <el-input v-model="queryParams.batchNo" placeholder="请输入批号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="目标仓库" prop="targetWarehouseId">
                <el-select v-model="queryParams.targetWarehouseId" placeholder="请选择目标仓库" clearable filterable
                    @change="handleWarehouseChange(queryParams.targetWarehouseId, false)">
                    <el-option v-for="item in targetWarehouseIdQuery" :key="item.dictValue" :label="item.dictLabel"
                        :value="item.dictValue" />
                </el-select>
            </el-form-item>
            <el-form-item label="目标库区" prop="targetRegionId">
                <el-select v-model="queryParams.targetRegionId" placeholder="请选择目标库区" filterable
                    @change="handleWhRegionChange(queryParams.targetRegionId, false)">
                    <el-option v-for="dict in queryParams.targetWhRegionOptions" :key="dict.id" :label="dict.name"
                        :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="目标库位" prop="targetLocationId">
                <el-select v-model="queryParams.targetLocationId" placeholder="请选择库位" filterable>
                    <el-option v-for="dict in queryParams.targetWhLocationOptions" :key="dict.id" :label="dict.name"
                        :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="移库时间" prop="operateDate">
                <el-date-picker clearable v-model="queryParams.operateDate" type="date" value-format="YYYY-MM-DD"
                    placeholder="请选择移库时间" />
            </el-form-item>
            <el-form-item label="执行人" prop="operateUser">
                <el-select v-model="queryParams.operateUser" placeholder="请选择执行人" clearable>
                    <el-option v-for="item in operateUserQuery" :key="item.dictValue" :label="item.dictLabel"
                        :value="item.dictValue" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['warehouse:warehouseMove:remove']">删除
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="warehouseMoveList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="编号" align="center" prop="id" />
            <el-table-column label="物料" align="center" prop="materialName" />
            <el-table-column label="源仓库" align="center" prop="sourceWarehouseId" :formatter="sourceWarehouseIdFormat" />
            <el-table-column label="源库区" align="center" prop="sourceRegionName" />
            <el-table-column label="源库位" align="center" prop="sourceLocationName" />
            <el-table-column label="批号" align="center" prop="batchNo" />
            <el-table-column label="目标仓库" align="center" prop="targetWarehouseId"
                :formatter="targetWarehouseIdFormat" />
            <el-table-column label="目标库区" align="center" prop="targetRegionName" />
            <el-table-column label="目标库位" align="center" prop="targetLocationName" />
            <el-table-column label="移出数量" align="center" prop="moveQuantity" />
            <el-table-column label="移库时间" align="center" prop="operateDate" width="180">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.operateDate, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="执行人" align="center" prop="operateUser" :formatter="operateUserFormat" />
            <el-table-column label="移库原因" align="center" prop="reason" />
            <el-table-column label="备注" align="center" prop="remark" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['warehouse:warehouseMove:remove']">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
    </div>
</template>

<script setup name="WarehouseMove">
import { listWarehouseMove, getWarehouseMove, delWarehouseMove } from "@/api/warehouse/warehouseMove";
const { proxy } = getCurrentInstance();

import { listWarehouseRegion } from "@/api/warehouse/warehouseRegion";
import { listWarehouseLocation } from "@/api/warehouse/warehouseLocation";

const sourceWarehouseIdQuery = ref([]);
proxy.getQueryData("query_warehouse").then(res => {
    sourceWarehouseIdQuery.value = res.data;
});
const sourceWarehouseIdFormat = (row, column) => {
    return proxy.selectDictLabel(sourceWarehouseIdQuery.value, row.sourceWarehouseId);
};
const targetWarehouseIdQuery = ref([]);
proxy.getQueryData("query_warehouse").then(res => {
    targetWarehouseIdQuery.value = res.data;
});
const targetWarehouseIdFormat = (row, column) => {
    return proxy.selectDictLabel(targetWarehouseIdQuery.value, row.targetWarehouseId);
};
const operateUserQuery = ref([]);
proxy.getQueryData("query_user").then(res => {
    operateUserQuery.value = res.data;
});
const operateUserFormat = (row, column) => {
    return proxy.selectDictLabel(operateUserQuery.value, row.operateUser);
};

function getWhRegionOpts(isSource) {
    listWarehouseRegion(queryParams.value).then(res => {
        if (isSource) {
            queryParams.value.sourceWhRegionOptions = res.rows;
        } else {
            queryParams.value.targetWhRegionOptions = res.rows;
        }
    });
}

function getWhLocationOpts(isSource) {
    listWarehouseLocation(queryParams.value).then(res => {
        if (isSource) {
            queryParams.value.sourceWhLocationOptions = res.rows;
        } else {
            queryParams.value.targetWhLocationOptions = res.rows;
        }
    })
}

function handleWarehouseChange(val, isSource) {
    // 清空
    queryParams.sourceWarehouseId = null;
    queryParams.sourceRegionId = null;
    queryParams.sourceLocationId = null;
    queryParams.targetWarehouseId = null;
    queryParams.targetRegionId = null;
    queryParams.targetLocationId = null;
    // 根据情况设置
    if (isSource) {
        queryParams.sourceWarehouseId = val;
    } else {
        queryParams.targetWarehouseId = val;
    }
    if (val) {
        getWhRegionOpts(isSource);
    }
}

function handleWhRegionChange(val, isSource) {
    // 清空
    queryParams.sourceWarehouseId = null;
    queryParams.sourceRegionId = null;
    queryParams.sourceLocationId = null;
    queryParams.targetWarehouseId = null;
    queryParams.targetRegionId = null;
    queryParams.targetLocationId = null;
    // 根据情况设置
    if (isSource) {
        queryParams.sourceRegionId = val;
    } else {
        queryParams.targetRegionId = val;
    }
    if (val) {
        getWhLocationOpts(isSource);
    }
}

const warehouseMoveList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialName: null,
        sourceWarehouseId: null,
        sourceRegionId: null,
        sourceLocationId: null,
        batchNo: null,
        targetWarehouseId: null,
        targetRegionId: null,
        targetLocationId: null,
        operateDate: null,
        operateUser: null,
    }
});

const { queryParams, form } = toRefs(data);

/** 查询移库记录列表 */
function getList() {
    loading.value = true;
    listWarehouseMove(queryParams.value).then(response => {
        warehouseMoveList.value = response.rows;
        total.value = response.total;
        loading.value = false;
    });
}

// 表单重置
function reset() {
    form.value = {
        id: null,
        materialName: null,
        sourceWarehouseId: null,
        sourceRegionId: null,
        sourceLocationId: null,
        batchNo: null,
        targetWarehouseId: null,
        targetRegionId: null,
        targetLocationId: null,
        moveQuantity: null,
        operateDate: null,
        operateUser: null,
        reason: null,
        remark: null,
        status: null,
    };
    proxy.resetForm("warehouseMoveRef");
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

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getWarehouseMove(_id).then(response => {
        form.value = response.data;
        open.value = true;
        title.value = "修改移库记录";
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal.confirm('是否确认删除移库记录编号为"' + _ids + '"的数据项？').then(function () {
        return delWarehouseMove(_ids);
    }).then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
    }).catch(() => { });
}

getList();
</script>
