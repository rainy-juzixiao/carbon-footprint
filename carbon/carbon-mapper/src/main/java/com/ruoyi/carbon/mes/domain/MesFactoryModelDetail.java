package com.ruoyi.carbon.mes.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 工厂建模明细对象 mes_factory_model_detail
 * 
 * @author ruoyi
 * @date 2022-07-07
 */
public class MesFactoryModelDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 生产线编号 */
    @Excel(name = "生产线编号")
    private Long productLineId;

    /** 设备 */
    @Excel(name = "设备")
    private Long equipmentId;

    /** 编码 */
    @ApiModelProperty("设备编码")
    @Excel(name = "设备编码")
    private String equipmentCode;

    /** 型号 */
    @ApiModelProperty("设备型号")
    @Excel(name = "设备型号")
    private String equipmentModel;

    /** 名称 */
    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称")
    private String equipmentName;

    /** 生产日期 */
    @ApiModelProperty(value="设备生产日期",example = "2021-09-10")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "设备生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date equipmentProductDate;

    /** 生产批号 */
    @ApiModelProperty("设备生产批号")
    @Excel(name = "设备生产批号")
    private String equipmentBatchNo;

    /** 制造商 */
    @ApiModelProperty("设备制造商")
    @Excel(name = "设备制造商")
    private String equipmentManufacturer;

    /** 设备状态 */
    @ApiModelProperty("设备状态")
    @Excel(name = "设备状态")
    private String equipmentStatus;

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Date getEquipmentProductDate() {
        return equipmentProductDate;
    }

    public void setEquipmentProductDate(Date equipmentProductDate) {
        this.equipmentProductDate = equipmentProductDate;
    }

    public String getEquipmentBatchNo() {
        return equipmentBatchNo;
    }

    public void setEquipmentBatchNo(String equipmentBatchNo) {
        this.equipmentBatchNo = equipmentBatchNo;
    }

    public String getEquipmentManufacturer() {
        return equipmentManufacturer;
    }

    public void setEquipmentManufacturer(String equipmentManufacturer) {
        this.equipmentManufacturer = equipmentManufacturer;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(String equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProductLineId(Long productLineId) 
    {
        this.productLineId = productLineId;
    }

    public Long getProductLineId() 
    {
        return productLineId;
    }
    public void setEquipmentId(Long equipmentId) 
    {
        this.equipmentId = equipmentId;
    }

    public Long getEquipmentId() 
    {
        return equipmentId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productLineId", getProductLineId())
            .append("equipmentId", getEquipmentId())
            .toString();
    }
}
