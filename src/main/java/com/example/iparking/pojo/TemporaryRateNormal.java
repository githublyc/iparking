package com.example.iparking.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 临时车费率子表-标准收费
 * @Author lxd
 * @Date 2021-04-07 14:01:53
 */
@Data
public class TemporaryRateNormal {

    //id
    @ApiModelProperty(value = "id", notes = "id", allowEmptyValue = false, required = false)
    private Integer id;
    //临时车费率主表主键
    @Length(min = 0, max =0)
    @ApiModelProperty(value = "临时车费率主表主键", notes = "临时车费率主表主键", allowEmptyValue = false, required = false)
    private String rateId;
    //规则名称
    @Length(min = 0, max =255)
    @ApiModelProperty(value = "规则名称", notes = "规则名称", allowEmptyValue = false, required = false)
    private String ruleName;
    //每日最高收费
    @ApiModelProperty(value = "每日最高收费", notes = "每日最高收费", allowEmptyValue = false, required = false)
    private BigDecimal maxAmount;
    //免费时长 按照分钟计算
    @ApiModelProperty(value = "免费时长 按照分钟计算", notes = "免费时长 按照分钟计算", allowEmptyValue = false, required = false)
    private Integer freeTime;
    //免费时间计费
    @ApiModelProperty(value = "免费时间计费", notes = "免费时间计费", allowEmptyValue = false, required = false)
    private Integer isFree;
    //1小时的停车费用
    @ApiModelProperty(value = "1小时的停车费用", notes = "1小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount1;
    //2小时的停车费用
    @ApiModelProperty(value = "2小时的停车费用", notes = "2小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount2;
    //3小时的停车费用
    @ApiModelProperty(value = "3小时的停车费用", notes = "3小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount3;
    //4小时的停车费用
    @ApiModelProperty(value = "4小时的停车费用", notes = "4小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount4;
    //5小时的停车费用
    @ApiModelProperty(value = "5小时的停车费用", notes = "5小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount5;
    //6小时的停车费用
    @ApiModelProperty(value = "6小时的停车费用", notes = "6小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount6;
    //7小时的停车费用
    @ApiModelProperty(value = "7小时的停车费用", notes = "7小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount7;
    //8小时的停车费用
    @ApiModelProperty(value = "8小时的停车费用", notes = "8小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount8;
    //9小时的停车费用
    @ApiModelProperty(value = "9小时的停车费用", notes = "9小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount9;
    //10小时的停车费用
    @ApiModelProperty(value = "10小时的停车费用", notes = "10小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount10;
    //11小时的停车费用
    @ApiModelProperty(value = "11小时的停车费用", notes = "11小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount11;
    //12小时的停车费用
    @ApiModelProperty(value = "12小时的停车费用", notes = "12小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount12;
    //13小时的停车费用
    @ApiModelProperty(value = "13小时的停车费用", notes = "13小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount13;
    //14小时的停车费用
    @ApiModelProperty(value = "14小时的停车费用", notes = "14小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount14;
    //15小时的停车费用
    @ApiModelProperty(value = "15小时的停车费用", notes = "15小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount15;
    //16小时的停车费用
    @ApiModelProperty(value = "16小时的停车费用", notes = "16小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount16;
    //17小时的停车费用
    @ApiModelProperty(value = "17小时的停车费用", notes = "17小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount17;
    //18小时的停车费用
    @ApiModelProperty(value = "18小时的停车费用", notes = "18小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount18;
    //19小时的停车费用
    @ApiModelProperty(value = "19小时的停车费用", notes = "19小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount19;
    //20小时的停车费用
    @ApiModelProperty(value = "20小时的停车费用", notes = "20小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount20;
    //21小时的停车费用
    @ApiModelProperty(value = "21小时的停车费用", notes = "21小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount21;
    //22小时的停车费用
    @ApiModelProperty(value = "22小时的停车费用", notes = "22小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount22;
    //23小时的停车费用
    @ApiModelProperty(value = "23小时的停车费用", notes = "23小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount23;
    //24小时的停车费用
    @ApiModelProperty(value = "24小时的停车费用", notes = "24小时的停车费用", allowEmptyValue = false, required = false)
    private BigDecimal amount24;
    //状态 1 启用  2 禁用
    @ApiModelProperty(value = "状态 1 启用  2 禁用", notes = "状态 1 启用  2 禁用", allowEmptyValue = false, required = false)
    private Integer status;
    //创建时间
    @ApiModelProperty(value = "创建时间", notes = "创建时间", allowEmptyValue = false, required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    //更新时间
    @ApiModelProperty(value = "更新时间", notes = "更新时间", allowEmptyValue = false, required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    //创建者
    @Length(min = 0, max =20)
    @ApiModelProperty(value = "创建者", notes = "创建者", allowEmptyValue = false, required = false)
    private String createdBy;
    //更新者
    @Length(min = 0, max =20)
    @ApiModelProperty(value = "更新者", notes = "更新者", allowEmptyValue = false, required = false)
    private String updatedBy;
    //是否删除
    @ApiModelProperty(value = "是否删除", notes = "是否删除", allowEmptyValue = false, required = false)
    private Integer tombstone;
    //费率备注
    @ApiModelProperty(value = "费率备注", notes = "费率备注", allowEmptyValue = false, required = false)
    private String remarks;


}
