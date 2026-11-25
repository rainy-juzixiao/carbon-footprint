package com.ruoyi.carbon.footprint.service;

import java.util.List;

import com.ruoyi.carbon.footprint.domain.VMesEquipmentPowerDetail;

/**
 * VIEWService接口
 * 
 * @author ruoyi
 * @date 2022-07-31
 */
public interface IVMesEquipmentPowerDetailService 
{
    /**
     * 查询VIEW
     * 
     * @param equipmentId VIEWID
     * @return VIEW
     */
    public VMesEquipmentPowerDetail selectVMesEquipmentPowerDetailById(Long equipmentId);

    /**
     * 查询VIEW列表
     * 
     * @param vMesEquipmentPowerDetail VIEW
     * @return VIEW集合
     */
    public List<VMesEquipmentPowerDetail> selectVMesEquipmentPowerDetailList(VMesEquipmentPowerDetail vMesEquipmentPowerDetail);

    /**
     * 查询设备日能耗报表
     * @param vMesEquipmentPowerDetail
     * @return
     */
    public List<VMesEquipmentPowerDetail> selectEquipmentPowerReportByDay(VMesEquipmentPowerDetail vMesEquipmentPowerDetail);
}
