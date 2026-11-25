package com.ruoyi.carbon.scm.service.impl;

import java.util.List;

import com.ruoyi.common.enums.SerialNoPrefix;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.smty.utils.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.carbon.scm.domain.ScmPurchaseArriveDetail;
import com.ruoyi.carbon.scm.mapper.ScmPurchaseArriveMapper;
import com.ruoyi.carbon.scm.domain.ScmPurchaseArrive;
import com.ruoyi.carbon.scm.service.IScmPurchaseArriveService;

/**
 * 采购到货Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-07-06
 */
@Service
public class ScmPurchaseArriveServiceImpl implements IScmPurchaseArriveService 
{
    @Autowired
    private ScmPurchaseArriveMapper scmPurchaseArriveMapper;

    /**
     * 查询采购到货
     * 
     * @param id 采购到货ID
     * @return 采购到货
     */
    @Override
    public ScmPurchaseArrive selectScmPurchaseArriveById(Long id)
    {
        return scmPurchaseArriveMapper.selectScmPurchaseArriveById(id);
    }

    /**
     * 查询采购到货列表
     * 
     * @param scmPurchaseArrive 采购到货
     * @return 采购到货
     */
    @Override
    public List<ScmPurchaseArrive> selectScmPurchaseArriveList(ScmPurchaseArrive scmPurchaseArrive)
    {
        return scmPurchaseArriveMapper.selectScmPurchaseArriveList(scmPurchaseArrive);
    }

    /**
     * 新增采购到货
     * 
     * @param scmPurchaseArrive 采购到货
     * @return 结果
     */
    @Transactional
    @Override
    public int insertScmPurchaseArrive(ScmPurchaseArrive scmPurchaseArrive)
    {
        scmPurchaseArrive.setArriveNo(CodeUtil.getSerialNo(SerialNoPrefix.CGDH));
        scmPurchaseArrive.setCreateTime(DateUtils.getNowDate());
        int rows = scmPurchaseArriveMapper.insertScmPurchaseArrive(scmPurchaseArrive);
        insertScmPurchaseArriveDetail(scmPurchaseArrive);
        return rows;
    }

    /**
     * 修改采购到货
     * 
     * @param scmPurchaseArrive 采购到货
     * @return 结果
     */
    @Transactional
    @Override
    public int updateScmPurchaseArrive(ScmPurchaseArrive scmPurchaseArrive)
    {
        scmPurchaseArriveMapper.deleteScmPurchaseArriveDetailByArriveId(scmPurchaseArrive.getId());
        insertScmPurchaseArriveDetail(scmPurchaseArrive);
        return scmPurchaseArriveMapper.updateScmPurchaseArrive(scmPurchaseArrive);
    }

    /**
     * 批量删除采购到货
     * 
     * @param ids 需要删除的采购到货ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteScmPurchaseArriveByIds(Long[] ids)
    {
        scmPurchaseArriveMapper.deleteScmPurchaseArriveDetailByArriveIds(ids);
        return scmPurchaseArriveMapper.deleteScmPurchaseArriveByIds(ids);
    }

    /**
     * 删除采购到货信息
     * 
     * @param id 采购到货ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteScmPurchaseArriveById(Long id)
    {
        scmPurchaseArriveMapper.deleteScmPurchaseArriveDetailByArriveId(id);
        return scmPurchaseArriveMapper.deleteScmPurchaseArriveById(id);
    }

    /**
     * 新增到货明细信息
     * 
     * @param scmPurchaseArrive 采购到货对象
     */
    public void insertScmPurchaseArriveDetail(ScmPurchaseArrive scmPurchaseArrive)
    {
        List<ScmPurchaseArriveDetail> scmPurchaseArriveDetailList = scmPurchaseArrive.getScmPurchaseArriveDetailList();
        Long id = scmPurchaseArrive.getId();
        if (StringUtils.isNotNull(scmPurchaseArriveDetailList))
        {
            List<ScmPurchaseArriveDetail> list = new ArrayList<ScmPurchaseArriveDetail>();
            for (ScmPurchaseArriveDetail scmPurchaseArriveDetail : scmPurchaseArriveDetailList)
            {
                scmPurchaseArriveDetail.setArriveId(id);
                list.add(scmPurchaseArriveDetail);
            }
            if (list.size() > 0)
            {
                scmPurchaseArriveMapper.batchScmPurchaseArriveDetail(list);
            }
        }
    }
}
