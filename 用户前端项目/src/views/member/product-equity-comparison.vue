<template>
    <div style="display: flex; margin-bottom: 30px">
        <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/member/list' }">会员服务</el-breadcrumb-item>
            <el-breadcrumb-item>产品权益对比</el-breadcrumb-item>
        </el-breadcrumb>
    </div>
    <el-table :data="mergedArrays" :span-method="objectSpanMethod" :header-cell-style="headerCellStyle">
        <el-table-column prop="productName" label="产品" />
        <el-table-column label="服务项目">
            <el-table-column>
                <template #default="scope">
                    {{ scope.row.categoryOne }}
                </template>
            </el-table-column>
            <el-table-column>
                <template #default="scope">
                    {{ scope.row.categoryTwo }}
                </template>
            </el-table-column>
        </el-table-column>
        <el-table-column v-for="(item, index) in myGoodsList" :key="index" align="center">
            <template #header>
                <div>{{ item.goodsName }}</div>
                <div>{{ item.price }}元/年</div>
                <el-button class="buy-btn" @click="handleBuy(item.goodsId, index)">立即购买</el-button>
            </template>
            <template #default="scope">
                <span v-if="scope.row['productValue' + index] === '0'">❌</span>
                <span v-else-if="scope.row['productValue' + index] === '1'">
                    ✔
                    <p v-if="scope.row['produceValueExt' + index]">
                        {{ scope.row['produceValueExt'] }}
                    </p>

                </span>
                <span v-else-if="scope.row['productValue' + index].split('|').length > 1">
                    <div v-for="(it, i) in scope.row['productValue'].split('|')" :key="i">
                        {{ it }}
                    </div>
                </span>
                <span v-else>
                    {{ scope.row['productValue'] }}
                </span>
            </template>
        </el-table-column>
    </el-table>
</template>

<script setup>
import { goodsList } from '@/api/member/member';

const { proxy } = getCurrentInstance();

const myGoodsList = ref([]);
const mergedArrays = ref([]);

function makeMergedArrays(privilege) {
    const map = new Map();
    const createRow = (item) => {
        const row = { ...item };
        privilege.forEach((_, i) => {
            row['productValue' + i] = '0';
            row['produceValueExt' + i] = '';
        });
        return row;
    };
    privilege.forEach((items, index) => {
        items.forEach(item => {
            const key = JSON.stringify([
                item.productName,
                item.categoryOne,
                item.categoryTwo
            ]);
            const row = map.get(key) ?? createRow(item);
            map.set(key, row);
            row['productValue' + index] = item.productValue ?? '0';
            row['produceValueExt' + index] = item.produceValueExt ?? '0';
        });
    });
    return [...map.values()];
}

const objectSpanMethod = ({ row, column, rowIndex, columnIndex }) => {
    if (columnIndex > 1) {
        return;
    }
    const rows = mergedArrays.value;
    const keyFields = columnIndex === 0 ? ['productName'] : ['productName', 'categoryOne'];
    const isSameGroup = (a, b) => keyFields.every(field => a[field] === b[field]);
    if (rowIndex > 0 && isSameGroup(row, rows[rowIndex - 1])) {
        return [0, 0];
    }
    let count = 1;
    while (count + rowIndex < rows.length && isSameGroup(row, rows[rowIndex + count])) {
        ++count;
    }
    return [count, 1];
};

const headerCellStyle = ({ rowIndex }) => {
    if (rowIndex === 1) {
        return { display: 'none' };
    }
};

goodsList().then(response => {
    myGoodsList.value = response.rows;
    let privileges = myGoodsList.value.map(item => {
        return JSON.parse(item.privilege);
    });
    mergedArrays.value = makeMergedArrays(privileges);
});

function handleBuy(goodsId, index) {
    proxy.$router.push({ path: '/member/recharge', query: { goodsId, index } }); // 购买页
}
</script>

<style scoped>
:deep(.buy-btn) {
    background: linear-gradient(90deg, #22CDB5 8%, #46DFCA 96%);
    color: #fff;
}
</style>