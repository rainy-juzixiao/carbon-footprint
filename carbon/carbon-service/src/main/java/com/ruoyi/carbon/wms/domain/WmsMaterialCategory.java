package com.ruoyi.carbon.wms.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料分类对象 wms_material_category
 * 
 * @author ruoyi
 * @date 2026-11-25
 */
@ApiModel("物料分类")
public class WmsMaterialCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @ApiModelProperty("编号")
    private Long id;

    /** 分类编码 */
    @ApiModelProperty("分类编码")
    @Excel(name = "分类编码")
    private String code;

    /** 分类名称 */
    @ApiModelProperty("分类名称")
    @Excel(name = "分类名称")
    private String name;

    /** 父级分类编号 */
    @ApiModelProperty("父级分类编号")
    @Excel(name = "父级分类编号")
    private Long parentId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
            .append("remark", getRemark())
            .append("parentId", getParentId())
            .toString();
    }
}
