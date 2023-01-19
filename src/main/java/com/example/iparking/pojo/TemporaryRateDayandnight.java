package com.example.iparking.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 临时车费率子表-按白天夜间收费
 * @Author lxd
 * @Date 2021-04-07 14:01:53
 */
@Data
public class TemporaryRateDayandnight {

	// id
	@ApiModelProperty(value = "id", notes = "id", allowEmptyValue = false, required = false)
	private Integer id;
	// 临时车费率主表主键
	@Length(min = 0, max = 0)
	@ApiModelProperty(value = "临时车费率主表主键", notes = "临时车费率主表主键", allowEmptyValue = false, required = false)
	private String rateId;
	// 规则名称
	@Length(min = 0, max = 255)
	@ApiModelProperty(value = "规则名称", notes = "规则名称", allowEmptyValue = false, required = false)
	private String ruleName;
	// 白天开始小时
	@Length(min = 0, max = 255)
	@ApiModelProperty(value = "白天开始小时", notes = "白天开始小时", allowEmptyValue = false, required = false)
	private String startTimeDay;
	// 白天开始分钟
	@Length(min = 0, max = 255)
	@ApiModelProperty(value = "白天开始分钟", notes = "白天开始分钟", allowEmptyValue = false, required = false)
	private String startMinDay;
	// 白天免费时长
	private int freeTimeDay;
	// 免费时间计费(0不计费 关闭1计费开启)
	private int isFreeDay;
	// 白天首计时小时
	@Length(min = 0, max = 255)
	@ApiModelProperty(value = "白天首计时类型（1小时2分钟）", notes = "白天首计时类型（1小时2分钟）", allowEmptyValue = false, required = false)
	private int firstTimeTypeDay;
	// 白天首计时分钟
	@ApiModelProperty(value = "白天首计时时长", notes = "白天首计时时长", allowEmptyValue = false, required = false)
	private String firstDurationDay;
	@ApiModelProperty(value = "白天首计时金额", notes = "白天首计时金额", allowEmptyValue = false, required = false)
	private BigDecimal firstAmountDay;
	// 计时单位（分钟）
	@ApiModelProperty(value = "计时单位（分钟）", notes = "计时单位（分钟）", allowEmptyValue = false, required = false)
	private Integer timeUnitDay;
	// 单位收费额
	@ApiModelProperty(value = "单位收费额", notes = "单位收费额", allowEmptyValue = false, required = false)
	private BigDecimal unitAmountDay;
	// 最高收费
	@ApiModelProperty(value = "最高收费", notes = "最高收费", allowEmptyValue = false, required = false)
	private BigDecimal maxAmountDay;
	// 最低收费
	@ApiModelProperty(value = "最低收费", notes = "最低收费", allowEmptyValue = false, required = false)
	private BigDecimal minAmountDay;
	// 夜间开始小时
	@Length(min = 0, max = 255)
	@ApiModelProperty(value = "夜间开始小时", notes = "夜间开始小时", allowEmptyValue = false, required = false)
	private String startTimeNight;
	// 夜间开始分钟
	@Length(min = 0, max = 255)
	@ApiModelProperty(value = "夜间开始分钟", notes = "夜间开始分钟", allowEmptyValue = false, required = false)
	private String startMinNight;
	// 夜间免费时长
	private int freeTimeNight;
	// 免费时间计费(0不计费 关闭1计费开启)
	private int isFreeNight;
	// 夜间首计时小时
	@ApiModelProperty(value = "夜间首计时类型（1小时2分钟）", notes = "夜间首计时类型（1小时2分钟）", allowEmptyValue = false, required = false)
	private int firstTimeTypeNight;
	// 白天首计时分钟
	@Length(min = 0, max = 255)
	@ApiModelProperty(value = "夜间首计时时长", notes = "夜间首计时时长", allowEmptyValue = false, required = false)
	private String firstDurationNight;
	@ApiModelProperty(value = "夜间首计时金额", notes = "夜间首计时金额", allowEmptyValue = false, required = false)
	private BigDecimal firstAmountNight;
	// 计时单位（分钟）
	@ApiModelProperty(value = "计时单位（分钟）", notes = "计时单位（分钟）", allowEmptyValue = false, required = false)
	private Integer timeUnitNight;
	// 单位收费额
	@ApiModelProperty(value = "单位收费额", notes = "单位收费额", allowEmptyValue = false, required = false)
	private BigDecimal unitAmountNight;
	// 最高收费
	@ApiModelProperty(value = "最高收费", notes = "最高收费", allowEmptyValue = false, required = false)
	private BigDecimal maxAmountNight;
	// 最低收费
	@ApiModelProperty(value = "最低收费", notes = "最低收费", allowEmptyValue = false, required = false)
	private BigDecimal minAmountNight;
	// 状态 1 启用 2 禁用
	@ApiModelProperty(value = "状态 1 启用  2 禁用", notes = "状态 1 启用  2 禁用", allowEmptyValue = false, required = false)
	private Integer status;
	// 创建时间
	@ApiModelProperty(value = "创建时间", notes = "创建时间", allowEmptyValue = false, required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdAt;

	// 更新时间
	@ApiModelProperty(value = "更新时间", notes = "更新时间", allowEmptyValue = false, required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updatedAt;

	// 创建者
	@Length(min = 0, max = 20)
	@ApiModelProperty(value = "创建者", notes = "创建者", allowEmptyValue = false, required = false)
	private String createdBy;
	// 更新者
	@Length(min = 0, max = 20)
	@ApiModelProperty(value = "更新者", notes = "更新者", allowEmptyValue = false, required = false)
	private String updatedBy;
	// 是否删除
	@ApiModelProperty(value = "是否删除", notes = "是否删除", allowEmptyValue = false, required = false)
	private Integer tombstone;
	// 费率备注
	@ApiModelProperty(value = "费率备注", notes = "费率备注", allowEmptyValue = false, required = false)
	private String remarks;
	// 是否删除
	@ApiModelProperty(value = "0标准收费1白天累计2夜间累计3白天夜间分别累计", notes = "0标准收费1白天累计2夜间累计3白天夜间分别累计", allowEmptyValue = false, required = false)
	private Integer type;

}
