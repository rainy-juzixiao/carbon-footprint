package com.ruoyi.carbon.bid.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.carbon.bid.mapper.BidGoodsMapper;
import com.ruoyi.carbon.bid.domain.BidGoods;
import com.ruoyi.carbon.bid.service.IBidGoodsService;

/**
 * 会员服务Service业务层处理
 *
 * @author ruoyi
 * @date 2026-11-27
 */
@Service
public class BidGoodsServiceImpl implements IBidGoodsService
{
    @Autowired
    private BidGoodsMapper bidGoodsMapper;

    /**
     * 查询会员服务
     *
     * @param goodsId 会员服务ID
     * @return 会员服务
     */
    @Override
    public BidGoods selectBidGoodsById(Long goodsId)
    {
        return bidGoodsMapper.selectBidGoodsById(goodsId);
    }

    /**
     * 查询会员服务列表
     *
     * @param bidGoods 会员服务
     * @return 会员服务
     */
    @Override
    public List<BidGoods> selectBidGoodsList(BidGoods bidGoods)
    {
        return bidGoodsMapper.selectBidGoodsList(bidGoods);
    }

    /**
     * 新增会员服务
     *
     * @param bidGoods 会员服务
     * @return 结果
     */
    @Override
    public int insertBidGoods(BidGoods bidGoods)
    {
        bidGoods.setCreateTime(DateUtils.getNowDate());
        return bidGoodsMapper.insertBidGoods(bidGoods);
    }

    /**
     * 修改会员服务
     *
     * @param bidGoods 会员服务
     * @return 结果
     */
    @Override
    public int updateBidGoods(BidGoods bidGoods)
    {
        bidGoods.setUpdateTime(DateUtils.getNowDate());
        return bidGoodsMapper.updateBidGoods(bidGoods);
    }

    /**
     * 批量删除会员服务
     *
     * @param goodsIds 需要删除的会员服务ID
     * @return 结果
     */
    @Override
    public int deleteBidGoodsByIds(Long[] goodsIds)
    {
        return bidGoodsMapper.deleteBidGoodsByIds(goodsIds);
    }

    /**
     * 删除会员服务信息
     *
     * @param goodsId 会员服务ID
     * @return 结果
     */
    @Override
    public int deleteBidGoodsById(Long goodsId)
    {
        return bidGoodsMapper.deleteBidGoodsById(goodsId);
    }
}
