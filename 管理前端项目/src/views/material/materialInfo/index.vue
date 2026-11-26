<template>
    <el-row :gutter="20">
        <!--分类数据-->
        <el-col :span="4" :xs="24">
            <div class="head-container">
                <el-tree :data="categoryOptions" :props="defaultProps" :expand-on-click-node="false" ref="tree"
                    default-expand-all @node-click="handleNodeClick" />
            </div>
        </el-col>
        <!--物料数据-->
        <el-col :span="20" :xs="24">
            <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
                <el-form-item label="物料编号" prop="code">
                    <el-input v-model="queryParams.code" placeholder="请输入物料编码" clearable
                        @keyup.enter.native="handleQuery" />
                </el-form-item>
                <el-form-item label="物料名称" prop="name">
                    <el-input v-model="queryParams.name" placeholder="请输入物料名称" clearable
                        @keyup.enter.native="handleQuery" />
                </el-form-item>
                <el-form-item label="型号" prop="model">
                    <el-input v-model="queryParams.model" placeholder="请输入物料型号" clearable
                        @keyup.enter.native="handleQuery" />
                </el-form-item>
                <el-form-item label="规格" prop="specification">
                    <el-input v-model="queryParams.specification" placeholder="请输入物料规格" clearable
                        @keyup.enter.native="handleQuery" />
                </el-form-item>
                <el-form-item label="单位" prop="unit">
                    <el-input v-model="queryParams.unit" placeholder="请输入单位" clearable
                        @keyup.enter.native="handleQuery" />
                </el-form-item>
                <el-form-item label="单价" prop="price">
                    <el-input v-model="queryParams.price" placeholder="请输入单价" clearable
                        @keyup.enter.native="handleQuery" />
                </el-form-item>
                <el-form-item label="类型" prop="type">
                    <el-select v-model="queryParams.type" placeholder="请选择类型">
                        <el-option v-for="dict in typeOptions" :key="dict.dictValue" :label="dict.dictLabel"
                            :value="dict.dictValue"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleQuery">搜索</el-button>
                    <el-button @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>

            <el-row :gutter="10" class="mb8">
                <el-col :span="1.5">
                    <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['scm:info:add']">新增
                    </el-button>
                </el-col>
                <el-col :span="1.5">
                    <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate"
                        v-hasPermi="['scm:info:edit']">修改
                    </el-button>
                </el-col>
                <el-col :span="1.5">
                    <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                        v-hasPermi="['scm:info:remove']">删除
                    </el-button>
                </el-col>
            </el-row>

            <el-table v-loading="loading" :data="materialInfoList" @selection-change="handleSelectionChange">
                <el-table-column type="selection" align="center" width="200" />
                <el-table-column label="编号" align="center" prop="id" />
                <el-table-column label="物料编码" align="center" prop="code" width="200" />
                <el-table-column label="物料名称" align="center" prop="name" width="200" />
                <el-table-column label="型号" align="center" prop="model" width="200" />
                <el-table-column label="规格" align="center" prop="specification" width="200" />
                <el-table-column label="单位" align="center" prop="unit" width="200" />
                <el-table-column label="单价" align="center" prop="price" width="200" />
                <el-table-column label="类型" align="center" prop="type" :formatter="typeFormat" width="200" />
                <el-table-column label="备注" align="center" prop="remark" width="200" />
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
                    <template #default="scope">
                        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                            v-hasPermi="['mesModel:factoryModel:edit']">修改
                        </el-button>
                        <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                            v-hasPermi="['mesModel:factoryModel:remove']">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

            <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize" @pagination="getList" />
        </el-col>
        <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
            <el-form ref="infoRef" :model="form" :rules="rules" label-width="80px">
                <el-row>
                    <el-form-item label="分类" prop="categoryId">
                        <el-cascader v-model="form.categoryId" :options="categoryOptions" :props="defaultProps"
                            placeholder="请选择分类" />
                    </el-form-item>
                    <el-form-item label="物料编码" prop="code">
                        <el-input v-model="form.code" placeholder="请输入物料编码" />
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-form-item label="物料名称" prop="name">
                        <el-input v-model="form.name" placeholder="请输入物料名称" />
                    </el-form-item>
                    <el-form-item label="型号" prop="model">
                        <el-input v-model="form.model" placeholder="请输入型号" />
                    </el-form-item>
                    <el-form-item label="规格" prop="specification">
                        <el-input v-model="form.specification" placeholder="请输入规格" />
                    </el-form-item>
                    <el-form-item label="单位" prop="unit">
                        <el-input v-model="form.unit" placeholder="请输入单位" />
                    </el-form-item>
                    <el-form-item label="单价" prop="price">
                        <el-input v-model="form.price" placeholder="请输入单价" />
                    </el-form-item>
                    <el-form-item label="类型" prop="type">
                        <el-select v-model="form.type" placeholder="请选择类型">
                            <el-option v-for="dict in typeOptions" :key="dict.dictValue" :label="dict.dictLabel"
                                :value="dict.dictValue"></el-option>
                        </el-select>
                    </el-form-item>
                </el-row>

                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" placeholder="请输入备注" />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </el-dialog>
    </el-row>
</template>

<script>
import { listMaterialInfo, getMaterialInfo, delMaterialInfo } from "@/api/material/materialInfo";
import { listMaterialCategory } from "@/api/material/materialCategory";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
    name: "MaterialInfo",
    components: {
        Treeselect
    },
    props: {

    },
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 物料档案管理表格数据
            materialInfoList: [],
            selectedList: [],
            // 类型字典
            typeOptions: [],
            categoryOptions: [],
            defaultProps: {
                value: "id",
                children: "children",
                label: "name",
                emitPath: false,
            },
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                categoryId: null,
                code: null,
                name: null,
                model: null,
                specification: null,
                unit: null,
                type: null,
            },
            open: false,
            title: {},
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                categoryId: [
                    { required: true, message: "分类不能为空", trigger: "blur" }
                ],
                name: [
                    { required: true, message: "物料名称不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
        this.getDicts("material_type").then(response => {
            this.typeOptions = response.data;
        });
        this.getList();
        this.getTreeselect();
    },
    methods: {
        handleUpdate(row) {
            this.reset();
            const _id = row.id || ids.value;
            getMaterialInfo(_id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改物料档案";
            });
        },
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加物料档案";
        },
        /** 查询下拉树结构 */
        getTreeselect() {
            listMaterialCategory().then(response => {
                this.categoryOptions = [];
                const data = { id: 0, name: '分类', children: [] };
                data.children = this.handleTree(response.rows, "id", "parentId");
                this.categoryOptions.push(data);
            });
        },
        normalizer(node) {
            return {
                id: node.id,
                label: node.name,
                children: node.children,
            }
        },
        // 节点单击事件
        handleNodeClick(data) {
            this.queryParams.categoryId = data.id;
            this.getList();
        },
        /** 查询物料档案管理列表 */
        getList() {
            this.loading = true;
            listMaterialInfo(this.queryParams).then(response => {
                this.materialInfoList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 类型字典翻译
        typeFormat(row, column) {
            return this.selectDictLabel(this.typeOptions, row.type);
        },
        // 表单重置
        reset() {
            this.form = {
                id: null,
                categoryId: null,
                code: null,
                name: null,
                model: null,
                specification: null,
                unit: null,
                price: null,
                type: null,
                remark: null,
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.queryParams.categoryId = null;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.selectedList = [];
            this.selectedList.push(selection);
            this.ids = selection.map(item => item.id)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        cancel() {
            this.selectedList = [];
            this.$emit('onCancel')
        },
        hanleDelete(row) {
            const _ids = row.id || ids.value;
            this.$modal.confirm('是否确认删除工厂建模编号为"' + _ids + '"的数据项？').then(function () {
                return delMaterialInfo(_ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        }
    }
};
</script>
