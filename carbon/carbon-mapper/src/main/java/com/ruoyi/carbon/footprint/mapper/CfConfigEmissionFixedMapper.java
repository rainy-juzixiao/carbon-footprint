package com.ruoyi.carbon.footprint.mapper;

import java.util.List;
import com.ruoyi.carbon.footprint.domain.CfConfigEmissionFixed;

/**
 * 固定碳排放Mapper接口
 * 
 * @author ruoyi
 * @date 2022-07-22
 */
public interface CfConfigEmissionFixedMapper 
{
    /**
     * 查询固定碳排放
     * 
     * @param id 固定碳排放ID
     * @return 固定碳排放
     */
    public CfConfigEmissionFixed selectCfConfigEmissionFixedById(Long id);

    /**
     * 查询固定碳排放列表
     * 
     * @param cfConfigEmissionFixed 固定碳排放
     * @return 固定碳排放集合
     */
    public List<CfConfigEmissionFixed> selectCfConfigEmissionFixedList(CfConfigEmissionFixed cfConfigEmissionFixed);

    /**
     * 新增固定碳排放
     * 
     * @param cfConfigEmissionFixed 固定碳排放
     * @return 结果
     */
    public int insertCfConfigEmissionFixed(CfConfigEmissionFixed cfConfigEmissionFixed);

    /**
     * 修改固定碳排放
     * 
     * @param cfConfigEmissionFixed 固定碳排放
     * @return 结果
     */
    public int updateCfConfigEmissionFixed(CfConfigEmissionFixed cfConfigEmissionFixed);

    /**
     * 删除固定碳排放
     * 
     * @param id 固定碳排放ID
     * @return 结果
     */
    public int deleteCfConfigEmissionFixedById(Long id);

    /**
     * 批量删除固定碳排放
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCfConfigEmissionFixedByIds(Long[] ids);
}
