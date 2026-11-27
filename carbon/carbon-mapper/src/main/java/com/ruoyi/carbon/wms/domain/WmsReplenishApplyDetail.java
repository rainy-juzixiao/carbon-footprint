package com.ruoyi.carbon.wms.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 补货申请明细对象 wms_replenish_apply_detail
 * 
 * @author ruoyi
 * @date 2022-07-20
 */
@ApiModel("补货申请明细")
public class WmsReplenishApplyDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @ApiModelProperty("编号")
    private Long id;

    /** 申请单编号 */
    @ApiModelProperty("申请单编号")
    private Long applyId;

    /** 物料档案编号 */
    @ApiModelProperty("物料档案")
    @Excel(name = "物料档案编号")
    private Long materialId;

    /** 补货数量 */
    @Excel(name = "补货数量")
    private Double quantity;

    /** 仓库编号 */
    @Excel(name = "仓库编号")
    private Long warehouseId;

    /** 库区编号 */
    @Excel(name = "库区编号")
    private Long whRegionId;

    /** 库位编号 */
    @Excel(name = "库位编号")
    private Long whLocationId;

    /** 备注 */
    @Excel(name = "备注")
    private String detailRemark;


    /** 物料编码 */
    @ApiModelProperty("物料编码")
    @Excel(name = "物料编码")
    private String materialCode;

    /** 物料名称 */
    @ApiModelProperty("物料名称")
    @Excel(name = "物料名称")
    private String materialName;

    /** 型号 */
    @ApiModelProperty("物料型号")
    @Excel(name = "物料型号")
    private String materialModel;

    /** 规格 */
    @ApiModelProperty("物料规格")
    @Excel(name = "物料规格")
    private String materialSpecification;

    /** 单位 */
    @ApiModelProperty("物料单位")
    @Excel(name = "物料单位")
    private String materialUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getWhRegionId() {
        return whRegionId;
    }

    public void setWhRegionId(Long whRegionId) {
        this.whRegionId = whRegionId;
    }

    public Long getWhLocationId() {
        return whLocationId;
    }

    public void setWhLocationId(Long whLocationId) {
        this.whLocationId = whLocationId;
    }

    public String getDetailRemark() {
        return detailRemark;
    }

    public void setDetailRemark(String detailRemark) {
        this.detailRemark = detailRemark;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public String getMaterialSpecification() {
        return materialSpecification;
    }

    public void setMaterialSpecification(String materialSpecification) {
        this.materialSpecification = materialSpecification;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }
}
