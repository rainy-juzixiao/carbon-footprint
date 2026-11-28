package com.ruoyi.carbon.bid.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.carbon.bid.domain.BidGoods;
import com.ruoyi.carbon.bid.service.IBidGoodsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 会员服务Controller
 *
 * @author ruoyi
 * @date 2026-11-27
 */
@Api(tags = {"会员服务"})
@RestController
@RequestMapping("/bid/goods")
public class BidGoodsController extends BaseController
{
    @Autowired
    private IBidGoodsService bidGoodsService;

    /**
     * 查询会员服务列表
     */
    @GetMapping("/list")
    @ApiOperation("查询会员服务列表")
    @DynamicResponseParameters(properties = {
	        @DynamicParameter(name = "total", value = "总记录数"),
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "rows", value = "返回业务数据（数组类型）", dataTypeClass = BidGoods.class),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    public TableDataInfo list(BidGoods bidGoods)
    {
        startPage();
        List<BidGoods> list = bidGoodsService.selectBidGoodsList(bidGoods);
        return getDataTable(list);
    }

    @GetMapping("/front/list")
    @ApiOperation("查询会员服务列表")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "total", value = "总记录数"),
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "rows", value = "返回业务数据（数组类型）", dataTypeClass = BidGoods.class),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    public TableDataInfo frontList(BidGoods bidGoods) // 查询商品列表
    {
        startPage();
        List<BidGoods> list = bidGoodsService.selectBidGoodsList(bidGoods);
        return getDataTable(list);
    }

    /**
     * 导出会员服务列表
     */
    @ApiOperation("导出会员服务列表")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "文件名")
    })
    @PreAuthorize("@ss.hasPermi('bid:goods:export')")
    @Log(title = "会员服务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BidGoods bidGoods)
    {
        List<BidGoods> list = bidGoodsService.selectBidGoodsList(bidGoods);
        ExcelUtil<BidGoods> util = new ExcelUtil<BidGoods>(BidGoods.class);
        return util.exportExcel(list, "goods");
    }

    /**
     * 获取会员服务详细信息
     */
    @ApiOperation("获取会员服务详细信息")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "data", value = "返回业务数据", dataTypeClass = BidGoods.class),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @GetMapping(value = "/{goodsId}")
    public AjaxResult getInfo(@PathVariable("goodsId") Long goodsId)
    {
        return AjaxResult.success(bidGoodsService.selectBidGoodsById(goodsId));
    }

    @ApiOperation("获取会员服务详细信息")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "data", value = "返回业务数据", dataTypeClass = BidGoods.class),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @GetMapping(value = "/front/{goodsId}")
    public AjaxResult frontGetInfo(@PathVariable("goodsId") Long goodsId) // 查询商品详细
    {
        return AjaxResult.success(bidGoodsService.selectBidGoodsById(goodsId));
    }

    /**
     * 新增会员服务
     */
    @ApiOperation("新增会员服务")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('bid:goods:add')")
    @Log(title = "会员服务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BidGoods bidGoods)
    {
        return toAjax(bidGoodsService.insertBidGoods(bidGoods));
    }

    /**
     * 修改会员服务
     */
    @ApiOperation("修改会员服务")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('bid:goods:edit')")
    @Log(title = "会员服务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BidGoods bidGoods)
    {
        return toAjax(bidGoodsService.updateBidGoods(bidGoods));
    }

    /**
     * 删除会员服务
     */
    @ApiOperation("删除会员服务")
    @DynamicResponseParameters(properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @PreAuthorize("@ss.hasPermi('bid:goods:remove')")
    @Log(title = "会员服务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{goodsIds}")
    public AjaxResult remove(@PathVariable Long[] goodsIds)
    {
        return toAjax(bidGoodsService.deleteBidGoodsByIds(goodsIds));
    }
}
