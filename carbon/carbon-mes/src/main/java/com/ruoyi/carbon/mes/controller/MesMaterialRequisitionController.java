package com.ruoyi.carbon.mes.controller;

import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.ruoyi.carbon.mes.domain.MesMaterialRequisition;
import com.ruoyi.carbon.mes.domain.MesMaterialRequisitionDetail;
import com.ruoyi.carbon.mes.domain.MesProductPlan;
import com.ruoyi.carbon.mes.domain.MesProductSchedule;
import com.ruoyi.carbon.mes.service.IMesMaterialRequisitionService;
import com.ruoyi.carbon.mes.service.IMesProductPlanService;
import com.ruoyi.carbon.mes.service.IMesProductScheduleService;
import com.ruoyi.carbon.wms.domain.WmsMaterialInfo;
import com.ruoyi.carbon.wms.service.IWmsMaterialInfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 领料申请Controller
 *
 * @author ruoyi
 * @date 2022-07-14
 */
@Api(tags = {"制造执行MES-领料申请"})
@RestController
@RequestMapping("/mesProduct/requisitionApply")
public class MesMaterialRequisitionController extends BaseController {
    @Autowired
    private IMesMaterialRequisitionService mesMaterialRequisitionService;
    @Autowired
    private IMesProductScheduleService mesProductScheduleService;
    @Autowired
    private IMesProductPlanService mesProductPlanService;
    @Autowired
    private IWmsMaterialInfoService wmsMaterialInfoService;

    /**
     * 查询领料申请列表
     */
    @GetMapping("/list")
    @ApiOperation("查询领料申请列表")
    @DynamicResponseParameters(name = "mesProductRequisitionApplyList", properties = {
            @DynamicParameter(name = "total", value = "总记录数"),
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "rows", value = "返回业务数据（数组类型）", dataTypeClass = MesMaterialRequisition.class),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    public TableDataInfo list(MesMaterialRequisition mesMaterialRequisition) {
        startPage();
        Long loginUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        if (!SecurityUtils.isAdmin(loginUserId)) {
            mesMaterialRequisition.setApplyUser(String.valueOf(loginUserId));
        }
        List<MesMaterialRequisition> list = mesMaterialRequisitionService.selectMesMaterialRequisitionList(mesMaterialRequisition);
        list.stream().forEach(apply -> {
            //获取计划信息
            MesProductPlan plan = mesProductPlanService.selectMesProductPlanById(apply.getPlanId());
            if (plan != null) {
                apply.setPlanNo(plan.getSerialNo());
            }
            MesProductSchedule schedule = mesProductScheduleService.selectMesProductScheduleById(apply.getScheduleId());
            if (schedule != null) {
                apply.setScheduleNo(schedule.getSerialNo());
            }
            //获取产品信息
            WmsMaterialInfo materialInfo = wmsMaterialInfoService.selectWmsMaterialInfoById(apply.getProductId());
            if (materialInfo != null) {
                apply.setMaterialCode(materialInfo.getCode());
                apply.setMaterialModel(materialInfo.getModel());
                apply.setMaterialName(materialInfo.getName());
                apply.setMaterialUnit(materialInfo.getUnit());
                apply.setMaterialSpecification(materialInfo.getSpecification());
            }
        });
        return getDataTable(list);
    }

    /**
     * 查询待审核和已审核领料申请列表
     */
    @GetMapping("/audit/list")
    @ApiOperation("我的任务-领料申请审核")
    @DynamicResponseParameters(name = "mesProductRequisitionApplyAuditList", properties = {
            @DynamicParameter(name = "total", value = "总记录数"),
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "rows", value = "返回业务数据（数组类型）", dataTypeClass = MesMaterialRequisition.class),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    public TableDataInfo list4Audit(MesMaterialRequisition mesMaterialRequisition) {
        startPage();
        //默认查询待审核
        List<String> applyStatusList = new ArrayList<String>();
        applyStatusList.add(UserConstants.APPLY_STATUS_WAIT_AUDIT);
        applyStatusList.add(UserConstants.APPLY_STATUS_HAS_AUDIT);
        mesMaterialRequisition.getParams().put("applyStatusList", applyStatusList);
        if (StringUtils.isBlank(mesMaterialRequisition.getAuditStatus()) && StringUtils.isBlank(mesMaterialRequisition.getApplyStatus())) {
            mesMaterialRequisition.setApplyStatus(UserConstants.APPLY_STATUS_WAIT_AUDIT);
        }
        List<MesMaterialRequisition> list = mesMaterialRequisitionService.selectMesMaterialRequisitionList(mesMaterialRequisition);
        list.stream().forEach(apply -> {
            //获取计划信息
            MesProductPlan plan = mesProductPlanService.selectMesProductPlanById(apply.getPlanId());
            if (plan != null) {
                apply.setPlanNo(plan.getSerialNo());
            }
            MesProductSchedule schedule = mesProductScheduleService.selectMesProductScheduleById(apply.getScheduleId());
            if (schedule != null) {
                apply.setScheduleNo(schedule.getSerialNo());
            }
            //获取产品信息
            WmsMaterialInfo materialInfo = wmsMaterialInfoService.selectWmsMaterialInfoById(apply.getProductId());
            if (materialInfo != null) {
                apply.setMaterialCode(materialInfo.getCode());
                apply.setMaterialModel(materialInfo.getModel());
                apply.setMaterialName(materialInfo.getName());
                apply.setMaterialUnit(materialInfo.getUnit());
                apply.setMaterialSpecification(materialInfo.getSpecification());
            }
        });
        return getDataTable(list);
    }

    /**
     * 导出领料申请列表
     */
    @ApiOperation("导出领料申请列表")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "文件名")
    })
    @PreAuthorize("@ss.hasPermi('mesProduct:requisitionApply:export')")
    @Log(title = "领料申请", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MesMaterialRequisition mesMaterialRequisition) {
        Long loginUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        if (!SecurityUtils.isAdmin(loginUserId)) {
            mesMaterialRequisition.setApplyUser(String.valueOf(loginUserId));
        }
        List<MesMaterialRequisition> list = mesMaterialRequisitionService.selectMesMaterialRequisitionList(mesMaterialRequisition);
        list.stream().forEach(apply -> {
            //获取计划信息
            MesProductPlan plan = mesProductPlanService.selectMesProductPlanById(apply.getPlanId());
            if (plan != null) {
                apply.setPlanNo(plan.getSerialNo());
            }
            MesProductSchedule schedule = mesProductScheduleService.selectMesProductScheduleById(apply.getScheduleId());
            if (schedule != null) {
                apply.setScheduleNo(schedule.getSerialNo());
            }
            //获取产品信息
            WmsMaterialInfo materialInfo = wmsMaterialInfoService.selectWmsMaterialInfoById(apply.getProductId());
            if (materialInfo != null) {
                apply.setMaterialCode(materialInfo.getCode());
                apply.setMaterialModel(materialInfo.getModel());
                apply.setMaterialName(materialInfo.getName());
                apply.setMaterialUnit(materialInfo.getUnit());
                apply.setMaterialSpecification(materialInfo.getSpecification());
            }
        });
        ExcelUtil<MesMaterialRequisition> util = new ExcelUtil<MesMaterialRequisition>(MesMaterialRequisition.class);
        return util.exportExcel(list, "requisitionApply");
    }

    /**
     * 获取领料申请详细信息
     */
    @ApiOperation("获取领料申请详细信息")
    @DynamicResponseParameters(name = "mesProductRequisitionApplyGet", properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "data", value = "返回业务数据", dataTypeClass = MesMaterialRequisition.class),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        MesMaterialRequisition apply = mesMaterialRequisitionService.selectMesMaterialRequisitionById(id);
        //获取产品信息
        WmsMaterialInfo materialInfo = wmsMaterialInfoService.selectWmsMaterialInfoById(apply.getProductId());
        if (materialInfo != null) {
            apply.setMaterialCode(materialInfo.getCode());
            apply.setMaterialModel(materialInfo.getModel());
            apply.setMaterialName(materialInfo.getName());
            apply.setMaterialUnit(materialInfo.getUnit());
            apply.setMaterialSpecification(materialInfo.getSpecification());
        }
        //获取排产信息
        MesProductPlan plan = mesProductPlanService.selectMesProductPlanById(apply.getPlanId());
        if (plan != null) {
            apply.setPlanNo(plan.getSerialNo());
        }
        MesProductSchedule schedule = mesProductScheduleService.selectMesProductScheduleById(apply.getScheduleId());
        if (schedule != null) {
            apply.setRequireQuantity(schedule.getRequireQuantity());
            apply.setProductDate(schedule.getProductDate());
        }
        //获取物料信息
        List<MesMaterialRequisitionDetail> detailList = apply.getMesMaterialRequisitionDetailList();
        detailList.forEach(detail -> {
            WmsMaterialInfo materialInfo1 = wmsMaterialInfoService.selectWmsMaterialInfoById(detail.getMaterialId());
            if (materialInfo1 != null) {
                detail.setMaterialCode(materialInfo1.getCode());
                detail.setMaterialModel(materialInfo1.getModel());
                detail.setMaterialName(materialInfo1.getName());
                detail.setMaterialUnit(materialInfo1.getUnit());
                detail.setMaterialSpecification(materialInfo1.getSpecification());
            }
        });
        return AjaxResult.success(apply);
    }

    /**
     * 新增领料申请
     */
    @ApiOperation("新增领料申请")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('mesProduct:requisitionApply:add')")
    @Log(title = "领料申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MesMaterialRequisition mesMaterialRequisition) {
        mesMaterialRequisition.setApplyStatus(UserConstants.APPLY_STATUS_NO_SUBMIT);
        Long loginUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        mesMaterialRequisition.setApplyUser(String.valueOf(loginUserId));
        return toAjax(mesMaterialRequisitionService.insertMesMaterialRequisition(mesMaterialRequisition));
    }

    /**
     * 修改领料申请
     */
    @ApiOperation("修改领料申请")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('mesProduct:requisitionApply:edit')")
    @Log(title = "领料申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MesMaterialRequisition mesMaterialRequisition) {
        mesMaterialRequisition.setApplyStatus(UserConstants.APPLY_STATUS_NO_SUBMIT);
        mesMaterialRequisition.setAuditUser("");
        mesMaterialRequisition.setAuditStatus("");
        mesMaterialRequisition.setAuditTime(null);
        mesMaterialRequisition.setAuditComment("");
        return toAjax(mesMaterialRequisitionService.updateMesMaterialRequisition(mesMaterialRequisition));
    }

    /**
     * 删除领料申请
     */
    @ApiOperation("删除领料申请")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('mesProduct:requisitionApply:remove')")
    @Log(title = "领料申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(mesMaterialRequisitionService.deleteMesMaterialRequisitionByIds(ids));
    }

    @ApiOperation("提交领料申请")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('mesProduct:requisitionApply:edit')")
    @Log(title = "领料申请", businessType = BusinessType.UPDATE)
    @PutMapping("/submit")
    public AjaxResult submit(@RequestBody MesMaterialRequisition mesMaterialRequisition) {
        mesMaterialRequisition.setApplyStatus(UserConstants.APPLY_STATUS_WAIT_AUDIT);
        mesMaterialRequisition.setApplyTime(DateUtils.getNowDate());
        if (mesMaterialRequisition.getId() != null) {
            MesMaterialRequisition apply = mesMaterialRequisitionService.selectMesMaterialRequisitionById(mesMaterialRequisition.getId());
            if (apply == null) {
                return AjaxResult.error("此领料申请不存在，不能提交！");
            }
            mesMaterialRequisition.setAuditUser("");
            mesMaterialRequisition.setAuditStatus("");
            mesMaterialRequisition.setAuditTime(null);
            mesMaterialRequisition.setAuditComment("");
            return toAjax(mesMaterialRequisitionService.updateMesMaterialRequisition(mesMaterialRequisition));
        } else {
            Long loginUserId = SecurityUtils.getLoginUser().getUser().getUserId();
            mesMaterialRequisition.setApplyUser(String.valueOf(loginUserId));
            return toAjax(mesMaterialRequisitionService.insertMesMaterialRequisition(mesMaterialRequisition));
        }
    }

    @ApiOperation("审核领料申请")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('mesProduct:requisitionApply:audit')")
    @Log(title = "领料申请", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody MesMaterialRequisition mesMaterialRequisition) {
        mesMaterialRequisition.setApplyStatus(UserConstants.APPLY_STATUS_HAS_AUDIT);
        mesMaterialRequisition.setAuditTime(DateUtils.getNowDate());
        Long loginUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        mesMaterialRequisition.setAuditUser(String.valueOf(loginUserId));
        return toAjax(mesMaterialRequisitionService.updateMesMaterialRequisitionStatus(mesMaterialRequisition));
    }

    @ApiOperation("设置检验状态")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('mesProduct:requisitionApply:audit')")
    @Log(title = "领料申请", businessType = BusinessType.UPDATE)
    @PutMapping("/set/check")
    public AjaxResult setCheck(@RequestBody MesMaterialRequisition mesMaterialRequisition) {
        return toAjax(mesMaterialRequisitionService.checkMaterialRequisition(mesMaterialRequisition));
    }
}
