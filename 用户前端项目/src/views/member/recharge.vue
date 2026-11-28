<template>
    <div class="recharge">
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/member/list' }">会员服务</el-breadcrumb-item>
            <el-breadcrumb-item>会员充值</el-breadcrumb-item>
        </el-breadcrumb>
        <main class="content">
            <section class="goods-list">
                <div v-for="(item, index) in goods" :key="item.goodsId" class="goods"
                    :class="{ active: index === selectedIndex }" @click="selectedIndex = index">
                    <h3>{{ item.goodsName }}</h3>
                    <p><b>¥{{ item.price }}</b> 元/年</p>
                    <small>到期后不会自动续费</small>
                </div>
            </section>
            <section>
                <h3>会员特权</h3>
                <p v-for="(item, index) in privileges" :key="index">{{ index + 1 }}. {{ item }}</p>
            </section>
            <section>
                <h3>支付方式</h3>
                <el-radio-group v-model="payWay" @change="loadQrCode"> <el-radio :label="2">
                        <div class="zfb">
                            <img style="width:25px;height:25px;margin-right:10px"
                                src="../../assets/images/pay-icon.png" />
                            支付宝付款
                        </div>
                    </el-radio>
                    <el-radio :label="1">
                        <div class="wx">
                            <img style="width:25px;height:25px;margin-right:10px"
                                src="../../assets/images/wx-icon.png" />
                            微信付款
                        </div>
                    </el-radio>
                </el-radio-group>
            </section>
            <section class="bottom">
                <div>
                    <div ref="qrElement" class="qrcode" @click="submitPayment"></div>
                    <p>{{ payWay === 2 ? '打开手机支付宝' : '打开手机微信' }}，扫一扫继续支付</p>
                    <el-button type="primary" @click="submitPayment">提交订单</el-button>
                </div>
                <div class="orders">
                    <p v-for="item in recentOrders" :key="item.orderId">
                        {{ item.enterpriseName }} 于 {{ item.paymentTime }} 购买了 {{ item.goodsName }}
                    </p>
                </div>
            </section>
        </main>
    </div>
</template>

<script setup>
import { computed, getCurrentInstance, onMounted, ref } from 'vue'
import QRCode from 'qrcodejs2-fix'
import { goodsList as getGoodsList, getQrCode, sumbitOrder, orderPay } from '@/api/member/member'
import { orderList } from '@/api/order/order'

const { proxy } = getCurrentInstance()
const goods = ref([])
const selectedIndex = ref(0)
const payWay = ref(2)
const recentOrders = ref([])
const qrElement = ref(null)
const currentGoods = computed(() => goods.value[selectedIndex.value] || {})
const privileges = computed(() => (currentGoods.value.description || '')
    .split('</p>').filter(Boolean).map(item => item.replace('<p>', '')))

onMounted(async () => {
    const [goodsRes, orderRes] = await Promise.all([getGoodsList(), orderList()])
    goods.value = goodsRes.rows || []
    recentOrders.value = (orderRes.rows || []).filter(item => item.paymentStatus == 2).slice(0, 10)

    const query = proxy.$route.query
    const idIndex = goods.value.findIndex(item => item.goodsId == query.goodsId)
    selectedIndex.value = idIndex >= 0 ? idIndex : Number(query.index) || 0
    loadQrCode()
})

async function loadQrCode() {
    if (!qrElement.value) return
    qrElement.value.innerHTML = ''
    const res = await getQrCode(payWay.value)
    new QRCode(qrElement.value, {
        text: res.data.tradeNo,
        width: 180,
        height: 180,
        correctLevel: QRCode.CorrectLevel.H
    })
}

async function submitPayment() {
    const item = currentGoods.value
    const data = {
        enterpriseName: proxy.$store.state.user.realName,
        goodsId: item.goodsId,
        goodsName: item.goodsName,
        paymentMethod: payWay.value,
        price: item.price
    }
    const res = await sumbitOrder(data)
    const result = await orderPay({ ...data, orderId: res.data })
    proxy.$message({
        type: result.code === 200 ? 'success' : 'error',
        message: result.code === 200 ? '支付成功，感谢您的购买' : '支付失败，请重新购买'
    })
}
</script>

<style lang="scss" scoped>
.recharge {
    padding: 20px 30px;
}

.content {
    max-width: 1100px;
    margin: 30px auto;
    padding: 30px;
    background: #fff;
}

.goods-list {
    display: flex;
    gap: 24px;
    flex-wrap: wrap;
}

.goods {
    width: 280px;
    padding: 20px;
    border: 1px solid #ddd;
    cursor: pointer;
    text-align: center;
}

.goods.active {
    border: 2px solid #13b69f;
}

.goods b {
    color: #f00;
    font-size: 20px;
}

section {
    margin-top: 36px;
}

.zfb,
.wx {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 200px;
    height: 48px;
    border-radius: 4px;
    font-size: 16px;
    color: #fff;
}

.zfb {
    background: #3d6cee;
}

.wx {
    background: #48bb57;
}

.bottom {
    display: flex;
    gap: 40px;
}

.qrcode {
    width: 180px;
    height: 180px;
    cursor: pointer;
}

.orders {
    flex: 1;
    padding: 12px 24px;
    background: #f6f6f6;
}
</style>
