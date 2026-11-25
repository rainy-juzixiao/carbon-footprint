package com.ruoyi.carbon.footprint.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.carbon.footprint.domain.VMesEquipmentPowerDetail;
import com.ruoyi.carbon.footprint.mapper.VMesEquipmentPowerDetailMapper;
import com.ruoyi.carbon.footprint.service.IVMesEquipmentPowerDetailService;

/**
 * VIEWService业务层处理
 * 
 * @author ruoyi
 * @date 2022-07-31
 */
@Service
public class VMesEquipmentPowerDetailServiceImpl implements IVMesEquipmentPowerDetailService {
	@Autowired
	private VMesEquipmentPowerDetailMapper vMesEquipmentPowerDetailMapper;

	/**
	 * 查询VIEW
	 * 
	 * @param equipmentId
	 *            VIEWID
	 * @return VIEW
	 */
	@Override
	public VMesEquipmentPowerDetail selectVMesEquipmentPowerDetailById(Long equipmentId) {
		return vMesEquipmentPowerDetailMapper.selectVMesEquipmentPowerDetailById(equipmentId);
	}

	/**
	 * 查询VIEW列表
	 * 
	 * @param vMesEquipmentPowerDetail
	 *            VIEW
	 * @return VIEW
	 */
	@Override
	public List<VMesEquipmentPowerDetail> selectVMesEquipmentPowerDetailList(
			VMesEquipmentPowerDetail vMesEquipmentPowerDetail) {
		return vMesEquipmentPowerDetailMapper.selectVMesEquipmentPowerDetailList(vMesEquipmentPowerDetail);
	}

	@Override
	public List<VMesEquipmentPowerDetail> selectEquipmentPowerReportByDay(
			VMesEquipmentPowerDetail vMesEquipmentPowerDetail) {
		return vMesEquipmentPowerDetailMapper.selectEquipmentPowerReportByDay(vMesEquipmentPowerDetail);
	}

}
