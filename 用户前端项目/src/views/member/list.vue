<template>
  <div class="app-container">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>会员服务</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="main-content">
      <div class="cards">
        <el-row>
          <el-card v-for="(value, index) in myGoodsList" class="card" :key="index">
            <div class="card-header">
              <div class="img-wrapper">
                <img v-if="value.pic" style="width: 100%; height: 150px" :src="value.pic" />
                <img v-else style="width: 100%; height: 150px" src="@/assets/images/pic-cover.png" />
                <div class="desc">
                  <div class="item-title">
                    <span>{{ value.goodsName ? value.goodsName : '--' }}</span>
                  </div>
                  <div class="item-price">
                    <p>{{ value.price ? '¥' + value.price + '元/年' : '--' }}</p>
                  </div>
                </div>
              </div>
            </div>
            <span v-html="value.description" class="text"></span>
            <div class="footer">
              <el-button class="buy-btn" @click="handleBuy(value.goodsId, index)">立即购买</el-button>
            </div>
          </el-card>
        </el-row>
      </div>
      <div class="look-goods-compare" @click="handleCompare">
        查看产品权益对比
      </div>
    </div>
  </div>
</template>

<script setup>
import { goodsList } from '@/api/member/member';

const { proxy } = getCurrentInstance();

const myGoodsList = ref([]);

goodsList().then(response => {
  myGoodsList.value = response.rows;
});

function handleCompare() {
  proxy.$router.push(`/member/product-equity-comparison`); // 比较页
}

function handleBuy(goodsId, index) {
  proxy.$router.push({ path: '/member/recharge', query: { goodsId, index } }); // 购买页
}
</script>

<style lang="scss" scoped>
.main-content {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column; // 纵排列
  margin-top: 5%;
}

.card {
  .card-header {
    height: 150px;
    justify-content: center;

    .img-wrapper {
      position: relative;

      .desc {
        margin-top: -45%;

        .item-title {
          font-size: 22px;
          font-weight: 700;
          color: #fff;
        }

        .item-price {
          font-size: 18px;
          font-weight: 700;
          color: #fff;
        }
      }
    }
  }

  margin-right: 50px;
  margin-left: 50px;
  margin-bottom: 65px;
  width: 300px;
  height: 420px;
  border-radius: 12px;
  padding: 0px;

  .footer {
    margin-bottom: 10px;

    :deep(.buy-btn) {
      margin-top: 30px;
      background: linear-gradient(90deg, #22CDB5 8%, #46DFCA 96%);
      color: #fff;
    }
  }
}

:deep(.el-card__header) {
  border-bottom: none !important; // 去除分割
  padding-bottom: 4px; // 原padding太大了，干脆一点，直接设个4px
}

.look-goods-compare {
  margin: 10px 200px;
  width: 50%;
  height: 60px;
  justify-content: center;
  align-items: center;
  display: flex;
  border: 1px solid #7EE1D3;
  background: #EAFAF8;
  border-radius: 12px;
  color: #13B69F;
  cursor: pointer;
}

.text {
  text-align: left;
}
</style>