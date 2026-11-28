package com.ruoyi.carbon.bid.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 会员服务对象 bid_goods
 *
 * @author ruoyi
 * @date 2026-11-27
 */
@ApiModel("会员服务")
public class BidGoods extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品编号 */
    @ApiModelProperty("商品编号")
    private Long goodsId;

    /** 商品名称 */
    @ApiModelProperty("商品名称")
    @Excel(name = "商品名称")
    private String goodsName;

    /** 描述 */
    @ApiModelProperty("描述")
    @Excel(name = "描述")
    private String description;

    /** 价格 */
    @ApiModelProperty("价格")
    @Excel(name = "价格")
    private BigDecimal price;

    /** 期限：1年2月 */
    @ApiModelProperty("期限：1年2月")
    @Excel(name = "期限：1年2月")
    private Integer endType;

    /** 权益 */
    @ApiModelProperty("权益")
    @Excel(name = "权益")
    private String privilege;

    /** 图标 */
    @ApiModelProperty("图标")
    @Excel(name = "图标")
    private String pic;

    public void setGoodsId(Long goodsId)
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId()
    {
        return goodsId;
    }
    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName()
    {
        return goodsName;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
    public void setEndType(Integer endType)
    {
        this.endType = endType;
    }

    public Integer getEndType()
    {
        return endType;
    }
    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }

    public String getPrivilege()
    {
        return privilege;
    }
    public void setPic(String pic)
    {
        this.pic = pic;
    }

    public String getPic()
    {
        return pic;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("goodsId", getGoodsId())
            .append("goodsName", getGoodsName())
            .append("description", getDescription())
            .append("price", getPrice())
            .append("endType", getEndType())
            .append("privilege", getPrivilege())
            .append("pic", getPic())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
