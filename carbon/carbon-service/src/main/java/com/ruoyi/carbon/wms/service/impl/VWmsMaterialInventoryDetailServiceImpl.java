package com.ruoyi.carbon.wms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.carbon.wms.mapper.VWmsMaterialInventoryDetailMapper;
import com.ruoyi.carbon.wms.domain.VWmsMaterialInventoryDetail;
import com.ruoyi.carbon.wms.service.IVWmsMaterialInventoryDetailService;

/**
 * 库存明细报表Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-07-26
 */
@Service
public class VWmsMaterialInventoryDetailServiceImpl implements IVWmsMaterialInventoryDetailService 
{
    @Autowired
    private VWmsMaterialInventoryDetailMapper vWmsMaterialInventoryDetailMapper;

    /**
     * 查询库存明细报表
     * 
     * @param whId 库存明细报表ID
     * @return 库存明细报表
     */
    @Override
    public VWmsMaterialInventoryDetail selectVWmsMaterialInventoryDetailById(Long whId)
    {
        return vWmsMaterialInventoryDetailMapper.selectVWmsMaterialInventoryDetailById(whId);
    }

    /**
     * 查询库存明细报表列表
     * 
     * @param vWmsMaterialInventoryDetail 库存明细报表
     * @return 库存明细报表
     */
    @Override
    public List<VWmsMaterialInventoryDetail> selectVWmsMaterialInventoryDetailList(VWmsMaterialInventoryDetail vWmsMaterialInventoryDetail)
    {
        return vWmsMaterialInventoryDetailMapper.selectVWmsMaterialInventoryDetailList(vWmsMaterialInventoryDetail);
    }
}
