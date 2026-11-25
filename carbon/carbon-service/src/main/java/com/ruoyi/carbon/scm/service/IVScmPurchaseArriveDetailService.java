package com.ruoyi.carbon.scm.service;

import java.util.List;
import com.ruoyi.carbon.scm.domain.VScmPurchaseArriveDetail;

/**
 * 采购到货详细报表Service接口
 * 
 * @author ruoyi
 * @date 2022-08-02
 */
public interface IVScmPurchaseArriveDetailService 
{

    /**
     * 查询采购到货详细报表列表
     * 
     * @param vScmPurchaseArriveDetail 采购到货详细报表
     * @return 采购到货详细报表集合
     */
    public List<VScmPurchaseArriveDetail> selectVScmPurchaseArriveDetailList(VScmPurchaseArriveDetail vScmPurchaseArriveDetail);

}
