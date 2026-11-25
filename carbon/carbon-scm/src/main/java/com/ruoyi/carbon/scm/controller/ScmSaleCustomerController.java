package com.ruoyi.carbon.scm.controller;

import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.ruoyi.carbon.scm.domain.ScmSaleCustomer;
import com.ruoyi.carbon.scm.service.IScmSaleCustomerService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户档案Controller
 *
 * @author ruoyi
 * @date 2022-07-02
 */
@Api(tags = {"供应链SCM-销售管理-客户档案"})
@RestController
@RequestMapping("/sale/customer")
public class ScmSaleCustomerController extends BaseController {
    @Autowired
    private IScmSaleCustomerService scmSaleCustomerService;

    /**
     * 查询客户档案列表
     */
    @GetMapping("/list")
    @ApiOperation("查询客户档案列表")
    @DynamicResponseParameters(name = "saleCustomerList",
            properties = {
                    @DynamicParameter(name = "total", value = "总记录数"),
                    @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
                    @DynamicParameter(name = "rows", value = "返回业务数据（数组类型）", dataTypeClass = ScmSaleCustomer.class),
                    @DynamicParameter(name = "msg", value = "返回消息内容")
            })
    public TableDataInfo list(ScmSaleCustomer scmSaleCustomer) {
        startPage();
        List<ScmSaleCustomer> list = scmSaleCustomerService.selectScmSaleCustomerList(scmSaleCustomer);
        return getDataTable(list);
    }

    /**
     * 导出客户档案列表
     */
    @ApiOperation("导出客户档案列表")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "文件名")
    })
    @PreAuthorize("@ss.hasPermi('sale:customer:export')")
    @Log(title = "客户档案", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ScmSaleCustomer scmSaleCustomer) {
        List<ScmSaleCustomer> list = scmSaleCustomerService.selectScmSaleCustomerList(scmSaleCustomer);
        ExcelUtil<ScmSaleCustomer> util = new ExcelUtil<ScmSaleCustomer>(ScmSaleCustomer.class);
        return util.exportExcel(list, "customer");
    }

    /**
     * 获取客户档案详细信息
     */
    @ApiOperation("获取客户档案详细信息")
    @DynamicResponseParameters(name = "saleCustomerGet",
            properties = {
                    @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
                    @DynamicParameter(name = "data", value = "返回业务数据", dataTypeClass = ScmSaleCustomer.class),
                    @DynamicParameter(name = "msg", value = "返回消息内容")
            })
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(scmSaleCustomerService.selectScmSaleCustomerById(id));
    }

    /**
     * 新增客户档案
     */
    @ApiOperation("新增客户档案")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('sale:customer:add')")
    @Log(title = "客户档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScmSaleCustomer scmSaleCustomer) {
        return toAjax(scmSaleCustomerService.insertScmSaleCustomer(scmSaleCustomer));
    }

    /**
     * 修改客户档案
     */
    @ApiOperation("修改客户档案")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('sale:customer:edit')")
    @Log(title = "客户档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScmSaleCustomer scmSaleCustomer) {
        return toAjax(scmSaleCustomerService.updateScmSaleCustomer(scmSaleCustomer));
    }

    /**
     * 删除客户档案
     */
    @ApiOperation("删除客户档案")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('sale:customer:remove')")
    @Log(title = "客户档案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(scmSaleCustomerService.deleteScmSaleCustomerByIds(ids));
    }
}
