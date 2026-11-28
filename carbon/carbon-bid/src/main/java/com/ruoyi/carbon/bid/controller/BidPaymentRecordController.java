package com.ruoyi.carbon.bid.controller;

import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.PaymentBank;
import cn.hutool.core.util.IdUtil;
import com.ruoyi.system.domain.PaymentResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付记录相关接口。
 */
@Api(tags = {"支付记录"})
@RestController
@RequestMapping("/bid/payment/record")
public class BidPaymentRecordController extends BaseController
{
    /**
     * 获取支付二维码信息。
     */
    @ApiOperation("获取支付二维码")
    @DynamicResponseParameters(name = "paymentQrCode", properties = {
            @DynamicParameter(name = "code", value = "状态码，200正确，其他错误"),
            @DynamicParameter(name = "data", value = "支付流水号及二维码", dataTypeClass = PaymentResponse.class),
            @DynamicParameter(name = "msg", value = "返回消息内容")
    })
    @GetMapping("/qrcode/{paymentMethod}")
    public AjaxResult getQrCode(@PathVariable Integer paymentMethod)
    {
        if (paymentMethod == null || !PaymentBank.getMap().containsKey(paymentMethod.longValue()))
        {
            return AjaxResult.error("支付方式错误");
        }
        PaymentResponse response = new PaymentResponse().success(IdUtil.getSnowflake(1, 1).nextIdStr(), null);
        return AjaxResult.success(response);
    }
}
