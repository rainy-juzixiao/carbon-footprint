<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单据号" prop="serialNo">
        <el-input v-model="queryParams.serialNo" placeholder="请输入单据号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="业务类型" prop="bizType">
        <el-select v-model="queryParams.bizType" placeholder="请选择业务类型" clearable>
          <el-option v-for="dict in event_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="客户" prop="customerId">
        <el-select v-model="queryParams.customerId" placeholder="请选择客户" clearable>
          <el-option v-for="item in customerIdQuery" :key="item.dictValue" :label="item.dictLabel"
            :value="item.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="合同类型" prop="contractType">
        <el-select v-model="queryParams.contractType" placeholder="请选择合同类型" clearable>
          <el-option v-for="dict in contract_type" :key="dict.value" :label="dict.label" :value="dict.value" />
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
        <el-input v-model="queryParams.auditUser" placeholder="请输入审核人" clearable @keyup.enter="handleQuery" />
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
      <el-form-item label="单据状态" prop="billStatus">
        <el-select v-model="queryParams.billStatus" placeholder="请选择单据状态" clearable>
          <el-option v-for="dict in out_warehouse_status" :key="dict.value" :label="dict.label" :value="dict.value" />
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
          v-hasPermi="['wmsApply:outWarehouseApply:add']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate"
          v-hasPermi="['wmsApply:outWarehouseApply:edit']">修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['wmsApply:outWarehouseApply:remove']">删除
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outWarehouseApplyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="单据号" align="center" prop="serialNo" />
      <el-table-column label="关联单据号" align="center" prop="bizBillNo" />
      <el-table-column label="业务类型" align="center" prop="bizType">
        <template #default="scope">
          <dict-tag :options="event_type" :value="scope.row.bizType" />
        </template>
      </el-table-column>
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
      <!-- 
        情况1：申请状态为“待审核”或“已审核”，操作【修改】；
        情况2：申请状态为“未提交”，操作【修改】【删除】。 
      -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <div v-if="scope.row.applyStatus === '0'">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['wmsApply:outWarehouseApply:edit']">修改
            </el-button>
            <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
              v-hasPermi="['wmsApply:outWarehouseApply:remove']">删除
            </el-button>
          </div>
          <div v-else>
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['wmsApply:outWarehouseApply:edit']">修改
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改出库申请对话框 -->
    <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
      <el-form ref="outWarehouseApplyRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-form-item label="单据号" prop="serialNo">
            <el-input v-model="form.serialNo" placeholder="系统自动生成" disabled />
          </el-form-item>
          <el-form-item label="关联单据号" prop="bizBillId">
            <el-input v-model="form.bizBillNo" placeholder="请选择关联单据号" disabled>
              <template #append>
                <el-button v-no-more-click @click="selectBillNoOpen()">选择</el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-row>
        <el-form-item label="业务类型" prop="bizType">
          <el-select v-model="form.bizType" placeholder="请选择业务类型">
            <el-option v-for="dict in event_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-row>
          <el-form-item label="客户" prop="customerId" v-if="form.bizType === '4'">
            <el-select v-model="form.customerId" placeholder="请选择客户" clearable>
              <el-option v-for="item in customerIdQuery" :key="item.dictValue" :label="item.dictLabel"
                :value="parseInt(item.dictValue)" />
            </el-select>
          </el-form-item>
          <el-form-item label="合同编号" prop="contractNo" v-if="form.bizType === '4'">
            <el-input v-model="form.contractNo" placeholder="请输入合同编号" disabled />
          </el-form-item>
          <el-form-item label="合同类型" prop="contractType" v-if="form.bizType === '4'">
            <el-select v-model="form.contractType" placeholder="请选择合同类型" disabled>
              <el-option v-for="dict in contract_type" :key="dict.value" :label="dict.label"
                :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-row>
        <div>
          <el-row>
            <el-form-item label="申请人" prop="applyUser">
              <el-select v-model="form.applyUser" placeholder="请选择申请人" clearable disabled>
                <el-option v-for="item in applyUserQuery" :key="item.dictValue" :label="item.dictLabel"
                  :value="item.dictValue" />
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
            <el-form-item label="审核意见" prop="auditComment">
              <el-input type="textarea" v-model="form.auditComment" placeholder="请输入审核意见" disabled />
            </el-form-item>
          </el-row>
        </div>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="单据状态" prop="billStatus">
          <el-select v-model="form.billStatus" placeholder="请选择单据状态">
            <el-option v-for="dict in out_warehouse_status" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-divider content-position="center">出库申请明细信息</el-divider>
        <el-table :data="wmsOutWarehouseApplyDetailList" :row-class-name="rowWmsOutWarehouseApplyDetailIndex"
          @selection-change="handleWmsOutWarehouseApplyDetailSelectionChange" ref="wmsOutWarehouseApplyDetail">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50" />
          <el-table-column label="物料" prop="materialId" align="center" width="300">
            <template #default="scope">
              <el-form-item :prop="'wmsOutWarehouseApplyDetailList.' + scope.$index + '.materialId'"
                :rules="rules.materialId">
                <el-input v-model="scope.row.materialName" placeholder="请选择物料" disabled />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="型号" prop="materialModel" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.materialModel" placeholder="请选择物料" disabled />
            </template>
          </el-table-column>
          <el-table-column label="规格" prop="materialModel" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.materialSpecification" placeholder="请选择物料" disabled />
            </template>
          </el-table-column>
          <el-table-column label="单价" prop="materialModel" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.materialPrice" placeholder="请选择物料" disabled />
            </template>
          </el-table-column>
          <el-table-column label="单位" prop="materialModel" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.materialUnit" placeholder="请选择物料" disabled />
            </template>
          </el-table-column>
          <el-table-column label="仓库" prop="warehouseId" width="180">
            <template #default="scope">
              <el-select v-model="scope.row.warehouseId" placeholder="请选择仓库" clearable disabled>
                <el-option v-for="item in warehouseIdQuery" :key="item.dictValue" :label="item.dictLabel"
                  :value="parseInt(item.dictValue)" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="库区" prop="whRegionId" width="200">
            <template #default="scope">
              <el-form-item :prop="'wmsWarehouseMaterialDetailList.' + scope.$index + '.whRegionId'"
                :rules="rules.whRegionId">
                <el-select v-model="scope.row.whRegionId" placeholder="请选择库区" filterable disabled
                  @change="handleWhRegionChange(scope.row.whRegionId, scope.$index)">
                  <el-option v-for="dict in scope.row.whRegionOptions" :key="dict.id" :label="dict.name"
                    :value="dict.id" />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="库位" prop="whLocationId" width="200">
            <template #default="scope">
              <el-form-item :prop="'wmsWarehouseMaterialDetailList.' + scope.$index + '.whLocationId'"
                :rules="rules.whLocationId">
                <el-select v-model="scope.row.whLocationId" placeholder="请选择库位" filterable disabled>
                  <el-option v-for="dict in scope.row.whLocationOptions" :key="dict.id" :label="dict.name"
                    :value="dict.id" />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="发货数量" prop="outQuantity" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.outQuantity" placeholder="请输入发货数量" />
            </template>
          </el-table-column>
          <el-table-column label="序列号" prop="serialNo" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.serialNo" placeholder="请输入序列号" />
            </template>
          </el-table-column>
          <el-table-column label="生产批号" prop="batchNo" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.batchNo" placeholder="请输入生产批号" />
            </template>
          </el-table-column>
          <el-table-column label="生产日期" prop="productDate" width="240">
            <template #default="scope">
              <el-date-picker clearable v-model="scope.row.productDate" type="date" value-format="YYYY-MM-DD"
                placeholder="请选择生产日期" />
            </template>
          </el-table-column>
          <el-table-column label="生产单位" prop="manufacturer" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.manufacturer" placeholder="请输入生产单位" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm(false)">保 存</el-button>
          <el-button type="warning" @click="submitForm(true)">提 交</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <DeliverySelect :open="deliverySelectOpen" @onCancel="handleDeliveryCancel" @onSelected="handleDeliverySelected">
    </DeliverySelect>
    <RequisitionSelect :open="RequisitionSelectOpen" @onCancel="handleRequisitionSelectCancel"
      @onSelected="handleRequisitionSelectSelected">
    </RequisitionSelect>
  </div>
</template>

<script setup name="OutWarehouseApply">
import { listOutWarehouseApply, getOutWarehouseApply, delOutWarehouseApply, addOutWarehouseApply, updateOutWarehouseApply, submitOutWarehouseApply } from "@/api/wmsApply/outWarehouseApply";
import { listWarehouseRegion } from "@/api/warehouse/warehouseRegion";
import { listWarehouseLocation } from "@/api/warehouse/warehouseLocation";
import { getDelivery } from "@/api/sale/delivery";
import { getReturn } from "@/api/sale/return";
import DeliverySelect from '@/components/Sale/DeliverySelect.vue';
import RequisitionSelect from "@/components/product/RequisitionSelect";
const { proxy } = getCurrentInstance();
const event_type = ref([]);

proxy.getDicts('event_type').then(response => {
  event_type.value = response.data.filter(item => item.dictValue === '3' || item.dictValue === '4').map(item => {
    return {
      label: item.dictLabel,
      value: item.dictValue
    };
  });
});

const customerIdQuery = ref([]);
proxy.getQueryData("query_customer").then(res => {
  customerIdQuery.value = res.data;
});
const customerIdFormat = (row, column) => {
  return proxy.selectDictLabel(customerIdQuery.value, row.customerId);
};
const { contract_type } = proxy.useDict('contract_type');

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
const { out_warehouse_status } = proxy.useDict('out_warehouse_status');
const warehouseIdQuery = ref([]);
proxy.getQueryData("query_warehouse").then(res => {
  warehouseIdQuery.value = res.data;
});

const deliverySelectOpen = ref(false);
const handleDeliveryCancel = () => {
  deliverySelectOpen.value = false;
};

const RequisitionSelectOpen = ref(false);
const handleRequisitionSelectCancel = () => {
  RequisitionSelectOpen.value = false;
};

const selectBillNoOpen = () => {
  let f = form.value;
  if (f.bizType === '3') {
    RequisitionSelectOpen.value = true;
  } else if (f.bizType === '4') {
    deliverySelectOpen.value = true;
  }
};

const handleDeliverySelected = (row) => {
  let delivery = row[0];
  let f = form.value;

  // 设置基础信息
  f.contractId = delivery.contractId;
  f.contractNo = delivery.contractNo;
  f.bizBillId = delivery.id;
  f.bizBillNo = delivery.deliveryNo;
  f.contractType = delivery.contractType;

  getDelivery(f.bizBillId).then(response => {
    let list = [];
    let detailList = response.data.scmSaleDeliveryDetailList;

    // 设置物料档案信息
    detailList.forEach(item => {
      let deliveryQuantity = item.quantity ? item.quantity : 0;
      list.push({
        materialId: item.materialId,
        materialName: item.materialName,
        materialModel: item.materialModel,
        materialSpecification: item.materialSpecification,
        materialUnit: item.materialUnit,
        materialPrice: item.materialPrice,
        warehouseId: null,
        whLocationId: null,
        whRegionId: null,
        whLocationOptions: [],
        whRegionOptions: [],
        deliveryQuantity: deliveryQuantity,
        batchNo: item.batchNo
      });
      wmsOutWarehouseApplyDetailList.value = list;
    });
  });

  deliverySelectOpen.value = false;
};

const handleRequisitionSelectSelected = (row) => {
  let requisition = row;
  let f = form.value;

  // 设置基础信息
  f.contractId = requisition.contractId;
  f.contractNo = requisition.contractNo;
  f.bizBillId = requisition.id;
  f.bizBillNo = requisition.serialNo;
  f.contractType = requisition.contractType;

  getReturn(f.bizBillId).then(response => {
    let list = [];
    let detailList = response.data.scmSaleReturnDetailList;

    // 设置物料档案信息
    detailList.forEach(item => {
      let deliveryQuantity = item.quantity ? item.quantity : 0;
      list.push({
        materialId: item.materialId,
        materialName: item.materialName,
        materialModel: item.materialModel,
        materialSpecification: item.materialSpecification,
        materialUnit: item.materialUnit,
        materialPrice: item.materialPrice,
        warehouseId: null,
        whLocationId: null,
        whRegionId: null,
        whLocationOptions: [],
        whRegionOptions: [],
        deliveryQuantity: deliveryQuantity,
        batchNo: item.batchNo
      });
      wmsOutWarehouseApplyDetailList.value = list;
    });
  });

  RequisitionSelectOpen.value = false;
};

const outWarehouseApplyList = ref([]);
const wmsOutWarehouseApplyDetailList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedWmsOutWarehouseApplyDetail = ref([]);
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
    customerId: null,
    contractType: null,
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

/** 查询出库申请列表 */
function getList() {
  loading.value = true;
  listOutWarehouseApply(queryParams.value).then(response => {
    outWarehouseApplyList.value = response.rows;
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
    customerId: null,
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
  wmsOutWarehouseApplyDetailList.value = [];
  proxy.resetForm("outWarehouseApplyRef");
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
  title.value = "添加出库申请";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getOutWarehouseApply(_id).then(response => {
    form.value = response.data;
    wmsOutWarehouseApplyDetailList.value = response.data.wmsOutWarehouseApplyDetailList;
    wmsOutWarehouseApplyDetailList.value.forEach((item, index) => {
      getWhRegionOpts(item.warehouseId, index);
      getWhLocationOpts(item.whRegionId, index);
    })
    open.value = true;
    title.value = "修改出库申请";
  });
}

function submitForm(isSubmit) {
  proxy.$refs["outWarehouseApplyRef"].validate(valid => {
    if (valid) {
      form.value.wmsOutWarehouseApplyDetailList = wmsOutWarehouseApplyDetailList.value;
      if (isSubmit) {
        submitOutWarehouseApply(form.value).then(() => {
          proxy.$modal.msgSuccess("提交成功");
          open.value = false;
          getList();
        });
      } else if (form.value.id != null) {
        updateOutWarehouseApply(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addOutWarehouseApply(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除出库申请编号为"' + _ids + '"的数据项？').then(function () {
    return delOutWarehouseApply(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => { });
}

/** 出库申请明细序号 */
function rowWmsOutWarehouseApplyDetailIndex({ row, rowIndex }) {
  row.index = rowIndex + 1;
}

/** 复选框选中数据 */
function handleWmsOutWarehouseApplyDetailSelectionChange(selection) {
  checkedWmsOutWarehouseApplyDetail.value = selection.map(item => item.index)
}

//获取库区选项
function getWhRegionOpts(val, index) {
  if (!val) {
    return;
  }
  let queryParams = {
    warehouseId: val,
  };
  listWarehouseRegion(queryParams).then(res => {
    wmsOutWarehouseApplyDetailList.value[index].whRegionOptions = res.rows;
  });
}

//获取库位选项
function getWhLocationOpts(val, index) {
  let queryParams = {
    regionId: val,
  };
  listWarehouseLocation(queryParams).then(res => {
    wmsOutWarehouseApplyDetailList.value[index].whLocationOptions = res.rows;
  });
}

getList();
</script>
