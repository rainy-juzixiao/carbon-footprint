package com.ruoyi.carbon.bid.mapper;

import java.util.List;
import com.ruoyi.carbon.bid.domain.BidGoods;

/**
 * 会员服务Mapper接口
 *
 * @author ruoyi
 * @date 2026-11-27
 */
public interface BidGoodsMapper
{
    /**
     * 查询会员服务
     *
     * @param goodsId 会员服务ID
     * @return 会员服务
     */
    public BidGoods selectBidGoodsById(Long goodsId);

    /**
     * 查询会员服务列表
     *
     * @param bidGoods 会员服务
     * @return 会员服务集合
     */
    public List<BidGoods> selectBidGoodsList(BidGoods bidGoods);

    /**
     * 新增会员服务
     *
     * @param bidGoods 会员服务
     * @return 结果
     */
    public int insertBidGoods(BidGoods bidGoods);

    /**
     * 修改会员服务
     *
     * @param bidGoods 会员服务
     * @return 结果
     */
    public int updateBidGoods(BidGoods bidGoods);

    /**
     * 删除会员服务
     *
     * @param goodsId 会员服务ID
     * @return 结果
     */
    public int deleteBidGoodsById(Long goodsId);

    /**
     * 批量删除会员服务
     *
     * @param goodsIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBidGoodsByIds(Long[] goodsIds);
}
