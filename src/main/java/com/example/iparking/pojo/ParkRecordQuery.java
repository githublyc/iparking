package com.example.iparking.pojo;


import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Data
public class ParkRecordQuery {
	// 停车记录key(主键)
	private Integer id;
	// 车牌号
	private String carNum;
	// 泊位编号
	private String berthNum;
	// 车场ID
	private Integer lotId;
	private String parkKeyList;
	private Integer areaKey;
	// 车场模型(1：路内停车场2：路外停车场)
	private String lotModel;
	// 录入方式（来源）（0：自动录入 1：手动录入） 2:查询时使用
	private Integer source;
	// 车牌颜色
	private String carColor;
	// 设备驶入时间开始
	private String gcEntryTimeStart;
	// 设备驶入时间结束
	private String gcEntryTimeEnd;
	// 设备驶出时间开始
	private String gcExitTimeStart;
	// 设备驶出时间结束
	private String gcExitTimeEnd;
	// 驶入时间开始
	private String entryTimeStart;
	// 驶入时间结束
	private String entryTimeEnd;
	// 驶出时间开始
	private String exitTimeStart;
	// 驶出时间结束
	private String exitTimeEnd;
	// 审核状态(1：待审核2：审核通过3：审核不通过)
	private String auditStatus;
	// 审核方式(1：自动审核 2：人工审核)
	private String auditType;
	// 当前车辆停车状态(1：在停2：驶离)
	private String status;
	// 缴费状态(1：未缴费 2：正常缴费 3：包月 4：免费)
	private String chargingStatus;
	// 照片数
	private String imgNumber;
	// 消除欠费审核状态(0：未申请 1：待审核 2：审核通过 3：审核不通过)
	private String clearVerifyStatus;
	// 消除逾期审核状态(0：未申请 1：待审核 2：审核通过 3：审核不通过)
	private String clearOverStatus;

	/** 以下参数 报表查询接口使用 */
	// 机构key
	private long organKey;
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;

	private String searchValue;

	/** 查询入场记录 所需参数 */
	private String parkKey;
	private String plateNumber;

	private String vehicleType;

	private String spaceCode;// 泊位号

	/** 车辆入场时 判断 几秒前 是否有该车辆的入场信息 */
	private String time1;
	private String time2;
	/** 查询逾期记录时使用 */
	private String paymentOvertime;
	private String orderNumber;
	private String pyno;
	/** 在停状态 **/
	private String stopStatus;

	/**
	 * 当前时间
	 */
	private Date nowTime;

	/**
	 * 分钟数
	 */
	private String minutes;
	private Integer minutesBefore;
	private Integer minutesAfter;
	/***
	 * 拍照时间差
	 */
	private String photoTimeDifference;
	private Integer photoTimeDifferenceBefore;
	private Integer photoTimeDifferenceAfter;

	private Integer imgNumBefore;
	private Integer imgNumAfter;

	private HttpServletResponse httpServletResponse;

	//审核不通过原因
	private String reason;
}
