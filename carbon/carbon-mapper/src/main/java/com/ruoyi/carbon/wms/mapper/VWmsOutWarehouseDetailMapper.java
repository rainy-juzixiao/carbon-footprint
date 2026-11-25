package com.ruoyi.carbon.wms.mapper;

import java.util.List;

import com.ruoyi.carbon.wms.domain.VWmsOutWarehouseDetail;

/**
 * 物料出库明细Mapper接口
 * 
 * @author ruoyi
 * @date 2022-07-30
 */
public interface VWmsOutWarehouseDetailMapper 
{
    /**
     * 查询物料出库明细
     * 
     * @param materialId 物料出库明细ID
     * @return 物料出库明细
     */
    public VWmsOutWarehouseDetail selectVWmsOutWarehouseDetailById(Long materialId);

    /**
     * 查询物料出库明细列表
     * 
     * @param vWmsOutWarehouseDetail 物料出库明细
     * @return 物料出库明细集合
     */
    public List<VWmsOutWarehouseDetail> selectVWmsOutWarehouseDetailList(VWmsOutWarehouseDetail vWmsOutWarehouseDetail);
}
