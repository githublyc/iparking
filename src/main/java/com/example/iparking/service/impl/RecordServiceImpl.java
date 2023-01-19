package com.example.iparking.service.impl;


import com.ccb.alibaba.fastjson.util.TypeUtils;
import com.example.iparking.dao.RecordMapper;
import com.example.iparking.dao.TemporaryRateDayandnightMapper;
import com.example.iparking.pojo.Record;
import com.example.iparking.pojo.TemporaryRateDayandnight;
import com.example.iparking.service.RecordService;
import com.example.iparking.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* 相关Service实现
* @author MyName
* @version 1.0
* @date 2023-01-18
 * Copyright © MyCompany
*/
@Service
@Slf4j
public class RecordServiceImpl implements RecordService {
    public static final DateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private TemporaryRateDayandnightMapper temporaryRateDayandnightMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Override
    public Map<String, Object> parkingAmount(String startTime, String endTime) throws ParseException {
        log.info("计算费用入参：：startTime" + startTime);
        log.info("计算费用入参：：endTime" + endTime);
        // 定义 返回值
        Map<String, Object> returnMap = new HashMap<>();
        // 定义 金额
        BigDecimal payAmount = new BigDecimal(0);
        // 定义停车结束时间 获取当前时间
        Date endDate = new Date();
        if (StringUtils.isNotBlank(endTime)) {
            endDate = datetimeFormat.parse(endTime);
        } else {
            endTime = datetimeFormat.format(endDate);
        }
        System.out.println("开始计费-1-停车结束时间" + endTime);
        Date startDate = datetimeFormat.parse(startTime);

        // 判断 开始时间是否大于等于结束时间，大于无需计费
        if (startDate.getTime() >= endDate.getTime()) {
            // 开始时间 大于结束时间 返回0
            returnMap.put("code", "200");
            returnMap.put("msg", "success");
            returnMap.put("parkingAmount", 0);
            return returnMap;
        }

        log.info("计算时间差的时间:endDate.getTime()=>" + endDate.getTime());
        log.info("计算时间差的时间:startDate.getTime()=>" + startDate.getTime());
        // 结束时间 - 开始时间 获取 天数 小时 数
        long diff = endDate.getTime() - startDate.getTime();//这样得到的差值是微秒级别

        log.info("计算时间差的结果:diff=>" + diff);
        long days = diff / (1000 * 60 * 60 * 24);
        log.info("计算时间差的天数:days=>" + days);

        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        log.info("计算时间差的小时数:hours=>" + hours);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        log.info("计算时间差的分钟数:minutes=>" + minutes);

        long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * ((1000 * 60))) / 1000;
        log.info("计算时间差的秒数:seconds=>" + seconds);
        System.out.println("" + days + "天" + hours + "小时" + minutes + "分" + seconds + "秒");

        if (seconds > 0) {
            minutes = minutes + 1;
        }
                // 白天黑夜
                // 从数据库中获取 费率信息
                TemporaryRateDayandnight entity = temporaryRateDayandnightMapper.getDayAndNightMsgByRateId("32ad920088cb42acb988b3d2f55575e0");

                if (entity != null) {
                    // 判断 days 天数
                    // 定义 开始结束时间的 日期 MM-DD
                    String dayStart = startTime.substring(5, 10);
                    String dayEnd = endTime.substring(5, 10);
                    // 定义 开始结束时间的 小时 hh
                    String hourStart = startTime.substring(11, 13);
                    String hourEnd = endTime.substring(11, 13);
                    // 定义 开始结束时间的 分钟 mm
                    String minStart = startTime.substring(14, 16);
                    String minEnd = endTime.substring(14, 16);

                    // 白天时间段开始时间--小时数
                    int dayTimeStart = Integer.valueOf(entity.getStartTimeDay());
                    // 白天时间段开始时间--分钟数
                    int dayTimeMinStart = Integer.valueOf(entity.getStartMinDay());
                    // 夜间时间段开始时间--小时数
                    int nightTimeStart = Integer.valueOf(entity.getStartTimeNight());
                    // 夜间时间段开始时间--分钟数
                    int nightTimeMinStart = Integer.valueOf(entity.getStartMinNight());
                    // 停车开始时间所在小时
                    int hourStartInt = Integer.valueOf(hourStart);
                    // 停车开始时间所在分钟
                    int minStartInt = Integer.valueOf(minStart);
                    // 停车结束时间所在小时
                    int hourEndInt = Integer.valueOf(hourEnd);
                    // 停车结束时间所在分钟
                    int minEndInt = Integer.valueOf(minEnd);

                    Map<String, Object> resultMap = new HashMap<>();

                    // 根据 停车天数判断
                    if (days < 1) {
                        // 停车时长 天数 小于一天
                        // 判断开始结束时间的日期 是否是同一天
                        if (dayStart.equals(dayEnd)) {
                            // 停车开始 结束时间在同一天
                            // 判断 停车开始时间 在哪个 时间段 （白天/夜间）
                            if (hourStartInt < dayTimeStart && hourStartInt < nightTimeStart) {
                                // 停车开始时间在 夜间时间段 （停车开始时间 小于 白天时间段开始时间 并且 小于 夜间时间段开始时间）
                                if (hourEndInt < dayTimeStart && hourEndInt < nightTimeStart) {
                                    // 停车结束时间 在 夜间时间段 （停车结束时间 小于 白天时间段开始时间 并且 小于 夜间时间段开始时间）
                                    resultMap = amountCalc(payAmount, entity, startTime, endTime, "2", "1", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                } else if (hourEndInt >= dayTimeStart && hourEndInt < nightTimeStart) {
                                    // 停车结束时间 在 白天时间段（停车结束时间 大于等于白天时间段开始时间 并且 小于 夜间时间段开始时间）
                                    // 定义 白天时间段开始时间
                                    String dayTimeBegin = startTime.substring(0, 11) + entity.getStartTimeDay() + ":" + entity.getStartMinDay() + ":00";
                                    // 第一步停车开始时间到白天时间段开始时间 计费 （夜间时间段计费）
                                    resultMap = amountCalc(payAmount, entity, startTime, dayTimeBegin, "2", "1", endTime);
                                    payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                    // 待补足时长
                                    Integer timeSub = TypeUtils.castToInt(resultMap.get("time"));
                                    // 白天时间段的 停车费用计算
                                    if (timeSub > 0) {
                                        // 开始时间 + 补足时间
                                        Date dayBeginDate = datetimeFormat.parse(dayTimeBegin);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(dayBeginDate);
                                        cal.add(Calendar.MINUTE, timeSub);
                                        // 开始时间 + 免费时长后的时间
                                        dayBeginDate = cal.getTime();
                                        dayTimeBegin = datetimeFormat.format(dayBeginDate);
                                    }
                                    // 计算 白天时间段开始时间 到 停车结束时间 的费用
                                    resultMap = amountCalc(payAmount, entity, dayTimeBegin, endTime, "1", "2", endTime);
                                    payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                } else {
                                    // 停车结束时间 在 夜间时间段（停车结束时间 大于白天时间段开始时间 并且 大于等于 夜间时间段开始时间）
                                    // 定义 白天时间段开始时间
                                    String dayTimeBegin = startTime.substring(0, 11) + entity.getStartTimeDay() + ":" + entity.getStartMinDay() + ":00";
                                    String dayTimeEnd = startTime.substring(0, 11) + entity.getStartTimeNight() + ":" + entity.getStartMinNight() + ":00";
                                    // 计算 停车开始时间到 白天时间段开始时间
                                    resultMap = amountCalc(payAmount, entity, startTime, dayTimeBegin, "2", "1", endTime);
                                    payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                    // 待补足时长
                                    int timeSub = TypeUtils.castToInt(resultMap.get("time"));
                                    // 白天时间段的 停车费用计算
                                    if (timeSub > 0) {
                                        // 开始时间 + 补足时间
                                        Date dayBeginDate = datetimeFormat.parse(dayTimeBegin);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(dayBeginDate);
                                        cal.add(Calendar.MINUTE, timeSub);
                                        // 开始时间 + 免费时长后的时间
                                        dayBeginDate = cal.getTime();
                                        dayTimeBegin = datetimeFormat.format(dayBeginDate);
                                    }
                                    // 计算 白天时间段开始时间到夜间时间段开始时间
                                    resultMap = amountCalc(payAmount, entity, dayTimeBegin, dayTimeEnd, "1", "2", endTime);
                                    payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                    // 计算 夜间时间开始时间 到 停车结束时间
                                    //原版
                                    //resultMap = amountCalc(payAmount,entity,dayTimeEnd,endTime,"2","2",endTime);
                                    //08-18 测试更新
                                    resultMap = amountCalc(payAmount, entity, dayTimeEnd, endTime, "2", "2", endTime);
                                    payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                }
                            } else if (hourStartInt >= dayTimeStart && hourStartInt < nightTimeStart) {
                                // 停车开始时间在 白天时间段内（停车开始时间 大于等于 白天时间段开始时间 并且 小于 夜间时间段开始时间）
                                if (hourEndInt >= dayTimeStart && hourEndInt < nightTimeStart) {
                                    // 停车结束时间 在 白天时间段（停车结束时间 大于等于白天时间段开始时间 并且 小于 夜间时间段开始时间）
                                    resultMap = amountCalc(payAmount, entity, startTime, endTime, "1", "1", endTime);
                                    payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                } else {
                                    // 停车结束时间 在 夜间时间段（停车结束时间 大于白天时间段开始时间 并且 大于等于 夜间时间段开始时间）
                                    // 定义 夜间时间段开始时间
                                    String dayTimeEnd = startTime.substring(0, 11) + entity.getStartTimeNight() + ":" + entity.getStartMinNight() + ":00";
                                    // 计算 停车开始时间 到夜间时间段开始时间的 费用
                                    // 2022-08-23 逻辑修改--增加分钟判断
                                    if (minEndInt < nightTimeMinStart) {
                                        resultMap = amountCalc(payAmount, entity, startTime, endTime, "1", "1", endTime);
                                        payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                    } else {
                                        resultMap = amountCalc(payAmount, entity, startTime, dayTimeEnd, "1", "1", endTime);
                                        payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                    }
                                    // 补足时长
                                    Integer timeSub = TypeUtils.castToInt(resultMap.get("time"));
                                    if (timeSub > 0) {
                                        // 开始时间 + 补足时间
                                        Date dayBeginDate = datetimeFormat.parse(dayTimeEnd);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(dayBeginDate);
                                        cal.add(Calendar.MINUTE, timeSub);
                                        // 开始时间 + 免费时长后的时间
                                        dayBeginDate = cal.getTime();
                                        dayTimeEnd = datetimeFormat.format(dayBeginDate);
                                    }
                                    // 计算 夜间时间到开始时间 到停车结束时间的 费用
                                    resultMap = amountCalc(payAmount, entity, dayTimeEnd, endTime, "2", "2", endTime);
                                    payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                }
                            } else if (hourStartInt > dayTimeStart && hourStartInt >= nightTimeStart) {
                                // 停车开始时间在 夜间时间段（停车开始时间 大于 白天时间段开始时间 并且大于等于 夜间时间段开始时间）
                                // 停车结束时间 在 夜间时间段（停车结束时间 大于白天时间段开始时间 并且 大于等于 夜间时间段开始时间）
                                resultMap = amountCalc(payAmount, entity, startTime, endTime, "2", "1", endTime);
                                payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                            }
                        } else {
                            // 停车开始 结束时间 不在同一天
                            // 判断 停车开始时间 在哪个 时间段 （白天/夜间）
                            if (hourStartInt < dayTimeStart && hourStartInt < nightTimeStart) {
                                // 停车开始时间在 夜间时间段 （停车开始时间 小于 白天时间段开始时间 并且 小于 夜间时间段开始时间）
                                if (hourEndInt < dayTimeStart && hourEndInt < nightTimeStart) {
                                    // 停车结束时间 在 夜间时间段（0-白天时间段开始时间：停车结束时间 小于白天时间段开始时间 并且小于夜间时间段开始时间）
                                    // 定义 白天时间段开始时间
                                    String dayTimeBegin = startTime.substring(0, 11) + entity.getStartTimeDay() + ":" + entity.getStartMinDay() + ":00";
                                    // 定义 夜间时间段开始时间
                                    String nightStartTime = startTime.substring(0, 11) + entity.getStartTimeNight() + ":" + entity.getStartMinNight() + ":00";
                                    // 计费 第一步 计算 停车开始时间到白天时间段开始时间的 费用
                                    resultMap = amountCalc(payAmount, entity, startTime, dayTimeBegin, "2", "1", endTime);
                                    payAmount = TypeUtils.castToBigDecimal(resultMap.get("payAmount"));
                                    // 补足时长
                                    Integer timeSub = TypeUtils.castToInt(resultMap.get("time"));
                                    if (timeSub > 0) {
                                        // 开始时间 + 补足时间
                                        Date dayBeginDate = datetimeFormat.parse(dayTimeBegin);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(dayBeginDate);
                                        cal.add(Calendar.MINUTE, timeSub);
                                        // 开始时间 + 免费时长后的时间
                                        dayBeginDate = cal.getTime();
                                        dayTimeBegin = datetimeFormat.format(dayBeginDate);
                                    }
                                    // 第二步 计算 白天时间段开始时间到夜间时间段开始时间
                                    resultMap = amountCalc(payAmount, entity, dayTimeBegin, nightStartTime, "1", "2", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                    // 第三步 计算夜间时间段开始时间 到 停车结束时间
                                    resultMap = amountCalc(payAmount, entity, nightStartTime, endTime, "2", "2", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                }
                            } else if (hourStartInt >= dayTimeStart && hourStartInt < nightTimeStart) {
                                // 停车开始时间在 白天时间段内（停车开始时间 大于等于 白天时间段开始时间 并且 小于 夜间时间段开始时间）
                                if (hourEndInt < dayTimeStart && hourEndInt < nightTimeStart) {
                                    // 停车结束时间在 夜间时间段内（停车开始时间 小于 白天时间段开始时间 并且 小于夜间时间段开始时间）
                                    // 定义 夜间时间段开始时间
                                    String nightStartTime = startTime.substring(0, 11) + entity.getStartTimeNight() + ":" + entity.getStartMinNight() + ":00";
                                    // 计算 停车开始时间到 夜间时间段开始时间
                                    resultMap = amountCalc(payAmount, entity, startTime, nightStartTime, "1", "1", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                    // 补足时长
                                    int timeSub = Integer.valueOf(resultMap.get("time").toString());
                                    if (timeSub > 0) {
                                        // 开始时间 + 补足时间
                                        Date nightBeginDate = datetimeFormat.parse(nightStartTime);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(nightBeginDate);
                                        cal.add(Calendar.MINUTE, timeSub);
                                        // 开始时间 + 免费时长后的时间
                                        nightBeginDate = cal.getTime();
                                        nightStartTime = datetimeFormat.format(nightBeginDate);
                                    }
                                    // 计算 夜间时间段到停车结束时间的费用
                                    resultMap = amountCalc(payAmount, entity, nightStartTime, endTime, "2", "2", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());

                                } else {
                                    // 停车结束时间 在 白天时间段内（停车开始时间 大于等于 白天时间段开始时间 并且 小于夜间时间段开始时间）

                                    // 定义 白天时间段开始时间
                                    String dayTimeBegin = endTime.substring(0, 11) + entity.getStartTimeDay() + ":" + entity.getStartMinDay() + ":00";
                                    // 定义 夜间时间段开始时间
                                    String nightStartTime = startTime.substring(0, 11) + entity.getStartTimeNight() + ":" + entity.getStartMinNight() + ":00";

                                    // 计算 停车开始时间到夜间时间段开始时间的费用
                                    resultMap = amountCalc(payAmount, entity, startTime, nightStartTime, "1", "1", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                    // 补足时长
                                    int timeSub = Integer.valueOf(resultMap.get("time").toString());
                                    if (timeSub > 0) {
                                        // 开始时间 + 补足时间
                                        Date nightBeginDate = datetimeFormat.parse(nightStartTime);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(nightBeginDate);
                                        cal.add(Calendar.MINUTE, timeSub);
                                        // 开始时间 + 免费时长后的时间
                                        nightBeginDate = cal.getTime();
                                        nightStartTime = datetimeFormat.format(nightBeginDate);
                                    }
                                    // 计算 夜间时间段开始时间 到 白天时间段开始时间
                                    resultMap = amountCalc(payAmount, entity, nightStartTime, dayTimeBegin, "2", "2", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                    // 计算 白天时间段开始时间 到 停车结束时间的费用
                                    resultMap = amountCalc(payAmount, entity, dayTimeBegin, endTime, "1", "2", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                }
                            } else if (hourStartInt > dayTimeStart && hourStartInt >= nightTimeStart) {
                                // 停车开始时间在 夜间时间段（停车开始时间 大于 白天时间段开始时间 并且大于等于 夜间时间段开始时间）
                                if (hourEndInt < dayTimeStart && hourEndInt < nightTimeStart) {
                                    // 停车结束时间 在 夜间时间段内 （停车结束时间 小于白天时间段开始时间 并且 小于夜间时间段开始时间）
                                    resultMap = amountCalc(payAmount, entity, startTime, endTime, "2", "1", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                } else if (hourEndInt >= dayTimeStart && hourEndInt < nightTimeStart) {
                                    // 停车结束时间 在白天时间段内 （停车结束时间 大于等于 白天时间段开始时间 并且 小于夜间时间段开始时间）

                                    // 定义 白天时间段开始时间
                                    String dayTimeBegin = endTime.substring(0, 11) + entity.getStartTimeDay() + ":" + entity.getStartMinDay() + ":00";
                                    // 计算 停车开始时间 到 白天时间段开始时间的费用
                                    resultMap = amountCalc(payAmount, entity, startTime, dayTimeBegin, "2", "1", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                    // 补足时长
                                    int timeSub = Integer.valueOf(resultMap.get("time").toString());
                                    if (timeSub > 0) {
                                        // 开始时间 + 补足时间
                                        Date dayBeginDate = datetimeFormat.parse(dayTimeBegin);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(dayBeginDate);
                                        cal.add(Calendar.MINUTE, timeSub);
                                        // 开始时间 + 免费时长后的时间
                                        dayBeginDate = cal.getTime();
                                        dayTimeBegin = datetimeFormat.format(dayBeginDate);
                                    }
                                    // 计算白天时间段开始时间 到 停车结束时间的费用
                                    resultMap = amountCalc(payAmount, entity, dayTimeBegin, endTime, "1", "2", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                } else {
                                    // 停车结束时间 在夜间时间段内（停车结束时间 大于白天时间段开始时间 并且 大于等于 夜间时间段开始时间）

                                    // 定义 白天时间段开始时间
                                    String dayTimeBegin = endTime.substring(0, 11) + entity.getStartTimeDay() + ":" + entity.getStartMinDay() + ":00";
                                    // 定义 夜间时间段开始时间
                                    String nightStartTime = endTime.substring(0, 11) + entity.getStartTimeNight() + ":" + entity.getStartMinNight() + ":00";

                                    // 计算 停车开始时间 到 白天时间段开始时间的费用
                                    resultMap = amountCalc(payAmount, entity, startTime, dayTimeBegin, "2", "1", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                    // 补足时长
                                    int timeSub = Integer.valueOf(resultMap.get("time").toString());
                                    if (timeSub > 0) {
                                        // 开始时间 + 补足时间
                                        Date dayBeginDate = datetimeFormat.parse(dayTimeBegin);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(dayBeginDate);
                                        cal.add(Calendar.MINUTE, timeSub);
                                        // 开始时间 + 免费时长后的时间
                                        dayBeginDate = cal.getTime();
                                        dayTimeBegin = datetimeFormat.format(dayBeginDate);
                                    }
                                    // 计算 白天时间段开始时间到夜间时间段开始时间的费用
                                    resultMap = amountCalc(payAmount, entity, dayTimeBegin, nightStartTime, "1", "2", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                    // 计算 夜间时间段开始时间 到 停车结束时间的 停车费用
                                    resultMap = amountCalc(payAmount, entity, nightStartTime, endTime, "2", "2", endTime);
                                    payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                }
                            }
                        }
                    } else {
                        // 停车时长 天数 大于等于1天
                        // 计算 开始结束时间 相差天数
                        int dayCount = DateTimeUtils.differentDays(startDate, endDate);

                        // 定义 时间点数组
                        String periodStr = startTime + ",";
                        // 定义开始 日期
                        String beginDayTime = startTime.substring(0, 10);
                        SimpleDateFormat dfdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date beginDayDate = dfdf.parse(beginDayTime);
                        // 定义 日期比较大小时 使用
                        Calendar currentTime = Calendar.getInstance();
                        Calendar compareTime = Calendar.getInstance();

                        String endTime2 = "";
                        // 循环
                        for (int i = 0; i <= dayCount; i++) {
                            // 开始天数  + i
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(beginDayDate);
                            cal.add(Calendar.DATE, i);
                            // 开始时间 + 免费时长后的时间
                            Date beginDayDate2 = cal.getTime();
                            beginDayTime = dfdf.format(beginDayDate2);

                            // 定义 白天时间段 开始时间
                            String dayStartTime = beginDayTime + " " + entity.getStartTimeDay() + ":" + entity.getStartMinDay() + ":00";
                            // 比较 停车开始时间 和 白天时间段开始时间 的大小 白天时间段小于停车开始时间 不需要添加
                            currentTime.setTime(datetimeFormat.parse(startTime));
                            compareTime.setTime(datetimeFormat.parse(dayStartTime));
                            if (currentTime.compareTo(compareTime) < 0) {
                                // 判断 白天时间段 是否 大于 结束时间 小于 放进periodStr
                                currentTime.setTime(datetimeFormat.parse(dayStartTime));
                                compareTime.setTime(datetimeFormat.parse(endTime));
                                if (currentTime.compareTo(compareTime) < 0) {
                                    periodStr = periodStr + dayStartTime + ",";
                                    endTime2 = dayStartTime;
                                } else {
                                    periodStr = periodStr + endTime;
                                    endTime2 = endTime;
                                }
                            }
                            // 定义夜间时间段开始时间
                            String nightStartTime = beginDayTime + " " + entity.getStartTimeNight() + ":" + entity.getStartMinNight() + ":00";
                            // 比较 停车开始时间 和 夜间时间段开始时间 的大小 夜间时间段小于停车开始时间 不需要添加
                            currentTime.setTime(datetimeFormat.parse(startTime));
                            compareTime.setTime(datetimeFormat.parse(nightStartTime));

                            if (currentTime.compareTo(compareTime) < 0) {
                                currentTime.setTime(datetimeFormat.parse(nightStartTime));
                                compareTime.setTime(datetimeFormat.parse(endTime));
                                if (currentTime.compareTo(compareTime) < 0) {
                                    periodStr = periodStr + nightStartTime + ",";
                                    endTime2 = nightStartTime;
                                } else {
                                    periodStr = periodStr + endTime;
                                    endTime2 = endTime;
                                }
                            }
                        }
                        // endTime 和 结束时间比较
                        currentTime.setTime(datetimeFormat.parse(endTime2));
                        compareTime.setTime(datetimeFormat.parse(endTime));
                        if (currentTime.compareTo(compareTime) < 0) {
                            periodStr = periodStr + endTime;
                        }

                        String[] periodList = periodStr.split(",");
                        System.out.println("periodList" + periodList.length);
                        // 定义待补足时间
                        int timeSub = 0;
                        for (int ii = 0; ii < periodList.length; ii++) {

                            // 获取 计费开始时间
                            String timeStart = periodList[ii];
                            String timeEnd = "";
                            int i1 = ii + 1;
                            if (i1 < periodList.length) {
                                timeEnd = periodList[i1];
                            } else {
                                break;
                            }
                            // 待补足时间大于0
                            if (timeSub > 0) {
                                // 开始时间 + 待补足时间
                                Date timeStartDate = datetimeFormat.parse(timeStart);
                                Calendar cal2 = Calendar.getInstance();
                                cal2.setTime(timeStartDate);
                                cal2.add(Calendar.MINUTE, timeSub);
                                // 开始时间 + 免费时长后的时间
                                timeStartDate = cal2.getTime();
                                timeStart = datetimeFormat.format(timeStartDate);
                            }
                            // 判断 timeStart 在 哪个时间段
                            // 定义 开始结束时间的 小时 hh
                            String str12 = timeStart.substring(12, 13);
                            if (":".equals(str12)) {
                                hourStart = timeStart.substring(11, 12);
                            } else {
                                hourStart = timeStart.substring(11, 13);
                            }
                            hourStartInt = Integer.valueOf(hourStart);
                            if (hourStartInt < dayTimeStart && hourStartInt < nightTimeStart) {
                                // 开始时间在 夜间时间段
                                resultMap = amountCalc(payAmount, entity, timeStart, timeEnd, "2", String.valueOf(ii + 1), endTime);
                                payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                timeSub = Integer.valueOf(resultMap.get("time").toString());
                            } else if (hourStartInt >= dayTimeStart && hourStartInt < nightTimeStart) {
                                // 开始时间在 白天时间段
                                resultMap = amountCalc(payAmount, entity, timeStart, timeEnd, "1", String.valueOf(ii + 1), endTime);
                                payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                timeSub = Integer.valueOf(resultMap.get("time").toString());
                            } else {
                                // 开始时间在夜间时间段
                                resultMap = amountCalc(payAmount, entity, timeStart, timeEnd, "2", String.valueOf(ii + 1), endTime);
                                payAmount = new BigDecimal(resultMap.get("payAmount").toString());
                                timeSub = Integer.valueOf(resultMap.get("time").toString());
                            }
                        }
//                            // 循环 相加最大金额
//                            for (int i=1;i<dayCount;i++){
//                                payAmount = payAmount.add(entity.getMaxAmountDay());
//                                payAmount = payAmount.add(entity.getMaxAmountNight());
//                            }
//                            // 判断停车开始时间所处时间段
//                            if (hourStartInt < dayTimeStart && hourStartInt < nightTimeStart){
//                                // 停车开始时间 在夜间时间段（停车开始时间 小于白天时间段开始时间 并且小于夜间时间段开始时间）
//                                // 定义 白天开始时间
//                                String dayStartTime = startTime.substring(0,11) + entity.getStartTimeDay()  + ":" + entity.getStartMinDay() + ":00";
//                                // 计算 停车开始时间到白天时间段开始时间
//                                resultMap = amountCalc(payAmount,entity,startTime,dayStartTime,"2","1",endTime);
//                                payAmount = new BigDecimal(resultMap.get("payAmount").toString());
//
//                            } else if (hourStartInt >= dayTimeStart && hourStartInt < nightTimeStart){
//                                // 停车开始时间 在 白天时间段内（停车开始时间 大于等于白天时间段开始时间 并且 小于 夜间时间段开始时间）
//
//                                // 定义 夜间开始时间
//                                String nightStartTime = startTime.substring(0,11) + entity.getStartTimeNight()  + ":" + entity.getStartMinNight() + ":00";
//                                resultMap = amountCalc(payAmount,entity,startTime,nightStartTime,"1","1",endTime);
//                                payAmount = new BigDecimal(resultMap.get("payAmount").toString());
//
//                            } else {
//                                // 停车开始时间 在 夜间时间段（停车开始时间 大于 白天时间段开始时间 并且 大于等于 夜间时间段开始时间）
//                                // 定义 停车开始日期第二天的 白天时间段开始时间
//                                String nextDay = "";
//                                Date date = datetimeFormat.parse(startTime);
//                                Calendar cal = Calendar.getInstance();
//                                cal.setTime(date);
//                                cal.add(Calendar.DATE, 1);
//                                // 开始时间 + 免费时长后的时间
//                                date = cal.getTime();
//                                nextDay = datetimeFormat.format(date);
//                                // 定义 第二天的 白天时间段开始时间
//                                String dayStartTime = nextDay.substring(0,11) + entity.getStartTimeDay()  + ":" + entity.getStartMinDay() + ":00";
//                                // 计算 停车开始时间 到 白天时间段开始时间
//                                resultMap = amountCalc(payAmount,entity,startTime,dayStartTime,"2","1",endTime);
//                                payAmount = new BigDecimal(resultMap.get("payAmount").toString());
//                            }
//
//
//                            // 判断 停车结束时间所处的时间段
//                            if (hourEndInt < dayTimeStart && hourEndInt < nightTimeStart){
//                                // 停车结束时间 在夜间时间段（停车结束时间 小于白天时间段开始时间 并且小于夜间时间段开始时间）
//                                // 定义 停车结束时间的前一天
//                                String lastDay = "";
//                                Date date = datetimeFormat.parse(endTime);
//                                Calendar cal = Calendar.getInstance();
//                                cal.setTime(date);
//                                cal.add(Calendar.DATE, -1);
//                                // 开始时间 + 免费时长后的时间
//                                date = cal.getTime();
//                                lastDay = datetimeFormat.format(date);
//                                // 定义 夜间时间段开始时间
//                                String nightStartTime = lastDay.substring(0,11) + entity.getStartTimeNight()  + ":" + entity.getStartMinNight() + ":00";
//                                // 计算 夜间时间段开始时间 到停车结束时间的费用
//                                resultMap = amountCalc(payAmount,entity,nightStartTime,endTime,"2","2",endTime);
//                                payAmount = new BigDecimal(resultMap.get("payAmount").toString());
//                            } else if (hourEndInt >= dayTimeStart && hourEndInt < nightTimeStart){
//                                // 停车结束时间 在 白天时间段内（停车结束时间 大于等于白天时间段开始时间 并且 小于 夜间时间段开始时间）
//                                // 定义 白天时间段开始时间
//                                String dayStartTime = endTime.substring(0,11) + entity.getStartTimeDay()  + ":" + entity.getStartMinDay() + ":00";
//                                // 计算 白天时间段开始时间到 停车结束时间的费用
//                                resultMap = amountCalc(payAmount,entity,dayStartTime,endTime,"1","2",endTime);
//                                payAmount = new BigDecimal(resultMap.get("payAmount").toString());
//                            } else {
//                                // 停车结束时间 在 夜间时间段（停车结束时间 大于 白天时间段开始时间 并且 大于等于 夜间时间段开始时间）
//                                // 定义 夜间时间段开始时间
//                                String nightStartTime = endTime.substring(0,11) + entity.getStartTimeNight()  + ":" + entity.getStartMinNight() + ":00";
//                                // 计算 夜间时间段开始时间到 停车结束时间的费用
//                                resultMap = amountCalc(payAmount,entity,nightStartTime,endTime,"2","2",endTime);
//                                payAmount = new BigDecimal(resultMap.get("payAmount").toString());
//                            }
                    }
                    returnMap.put("code", "200");
                    returnMap.put("msg", "success");
                    returnMap.put("parkingAmount", payAmount);
                    return returnMap;
                } else {
                    returnMap.put("code", "500");
                    returnMap.put("msg", "获取费率信息失败，请联系车场工作人员！");
                    return returnMap;
                }
    }

    @Override
    public BigDecimal countAmount() throws ParseException {
        List<Record> recordList = recordMapper.selectAll();

        Map<String, Object> returnMap = new HashMap<>();
        BigDecimal count = new BigDecimal(0);

        for (Record record : recordList){
            returnMap = parkingAmount(record.getEntryTime(),record.getExitTime());
            BigDecimal nextAmount = (BigDecimal) returnMap.get("parkingAmount");
            count = count.add(nextAmount);
            System.out.println("计费金额" + count);
        }
        return count;
    }

    public Map<String,Object> amountCalc(BigDecimal payAmount, TemporaryRateDayandnight entity, String startTime, String endTime,
                                         String type, String isOne,String stopEndTime) throws ParseException {
        // 定义返回参数
        Map<String, Object> resultMap = new HashMap<>();
        // 时间转换
        Date startDate = datetimeFormat.parse(startTime);
        Date endDate = datetimeFormat.parse(endTime);
        Date stopEndDate = datetimeFormat.parse(stopEndTime);

        // 判断 开始时间是否大于等于结束时间，大于无需计费
        if (startDate.getTime() >= endDate.getTime() || startDate.getTime() >= stopEndDate.getTime()) {
            // 开始时间 大于结束时间 返回0
            resultMap.put("payAmount",payAmount);
            resultMap.put("time",0);
            return resultMap;
        }

        // 计算 本时间段 开始结束差值
        long timeDiff = (Long.valueOf(entity.getStartTimeNight()) - Long.valueOf(entity.getStartTimeDay()));
        timeDiff = timeDiff * 60;
        if (!"0".equals(entity.getStartMinDay())){
            timeDiff = timeDiff  - Long.valueOf(entity.getStartMinDay());
        }
        if (!"0".equals(entity.getStartMinNight())){
            timeDiff = timeDiff  + Long.valueOf(entity.getStartMinNight());
        }
        // 计算 时间差 结束时间 - 开始时间
        long diff = endDate.getTime() - startDate.getTime();//这样得到的差值是微秒级别
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * ((1000 * 60))) / 1000;
        System.out.println("" + days + "天" + hours + "小时" + minutes + "分" + seconds +"秒");
        if (seconds > 0){
            minutes = minutes + 1;
        }
        // 计算停车 时长分钟数
        long allminutes = hours > 0 ? allminutes = minutes + hours * 60 : minutes;

        // 计算停车开始时间到结束时间的差值
        long stopDiff = stopEndDate.getTime() - startDate.getTime();//这样得到的差值是微秒级别
        long stopDays = stopDiff / (1000 * 60 * 60 * 24);
        long stopHours = (stopDiff-stopDays*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long stopMinutes = (stopDiff-stopDays*(1000 * 60 * 60 * 24)-stopHours*(1000* 60 * 60))/(1000* 60);
        long stopSeconds = (stopDiff - stopDays * (1000 * 60 * 60 * 24) - stopHours * (1000 * 60 * 60) - stopMinutes * ((1000 * 60))) / 1000;
        long allStopMinutes = stopDiff / (1000 * 60);
        if (stopSeconds > 0){
            stopMinutes = stopMinutes + 1;
            allStopMinutes = allStopMinutes +1;
        }

        // 根据 type 判断 所处时间段 type=1 白天时间段
        if ("1".equals(type)){
            // 判断 每日最高费用 是否为0
            boolean maxFlag = false;
            // 最高费用 大于 0
            if (entity.getMaxAmountDay().compareTo(BigDecimal.ZERO) > 0){
                maxFlag = true;
            }
            // 根据 isOne 判断 是第几次进入 该方法 isOne=1第一次进入
            if ("1".equals(isOne)) {
                // 判断免费时长是否计费
                // 判断 停车时长 是否大于免费时长
                long freeTime = TypeUtils.castToLong(entity.getFreeTimeDay());
                if (allStopMinutes <= freeTime){
                    resultMap.put("payAmount", payAmount);
                    resultMap.put("time", freeTime);
                    return resultMap;
                }
                if (allminutes - freeTime > 0) {
                    if (entity.getIsFreeDay() == 0) {
                        // 免费时长计费 关闭状态  免费时长 不参与计费
                        if (freeTime > 0) {
                            // 新的开始时间  = 开始时间 + 免费时长
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(startDate);
                            cal.add(Calendar.MINUTE, entity.getFreeTimeDay());
                            startDate = cal.getTime();

                            // 重新 计算 停车时长 结束时间 - 新的开始时间
                            diff = endDate.getTime() - startDate.getTime();//这样得到的差值是微秒级别
                            days = diff / (1000 * 60 * 60 * 24);
                            hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                            minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                            seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * ((1000 * 60))) / 1000;
                            if (seconds > 0){
                                minutes = minutes + 1;
                            }
                            if (hours > 0) {
                                allminutes = minutes + hours * 60;
                            } else {
                                allminutes = minutes;
                            }
                        }
                    }
                } else {
                    // 在免费时长内
                    if (entity.getIsFreeDay() == 0){
                        resultMap.put("payAmount", payAmount);
                        long makeUpTime = freeTime - minutes;
                        resultMap.put("time", makeUpTime);
                        return resultMap;
                    } else {
                        // 判断 总停车时长是否大于 该时间段的免费时长
                        if (allStopMinutes > freeTime){
                            long durationDay = Long.valueOf(entity.getFirstDurationDay());
                            if(durationDay > 0){
                                long makeUpTime = entity.getFirstTimeTypeDay() == 1 ? durationDay*60 - minutes : durationDay - minutes;
                                resultMap.put("time", makeUpTime);
                                resultMap.put("payAmount", payAmount.add(entity.getFirstAmountDay()));
                            }else {
                                long makeUpTime = entity.getTimeUnitDay() - minutes;
                                resultMap.put("time", makeUpTime);
                                resultMap.put("payAmount", payAmount.add(entity.getUnitAmountDay()));
                            }
                            return resultMap;
                        } else {
                            resultMap.put("payAmount", payAmount);
                            long makeUpTime = freeTime - minutes;
                            resultMap.put("time", makeUpTime);
                            return resultMap;
                        }
                    }
                }
            }

            // 判断首计费时长 是否 等于0 等于0  不参与计费  -- entity.getFirstAmountDay().compareTo(new BigDecimal(0)) > 0 &&
            if( Integer.valueOf(entity.getFirstDurationDay()) > 0 && "1".equals(isOne)){
                // 首计费 参与计费
                // 定义首计费 时长
                long durationDay = Long.valueOf(entity.getFirstDurationDay());
                // 判断 首计时 时长类型（1小时2分钟）
                if (entity.getFirstTimeTypeDay() == 1 ){
                    // 小时
                    // 停车时长 - 首计时时长
                    long hourSub = hours - durationDay;
                    if (hourSub < 0){
                        // 停车时长 小于 首计费时长
                        // 计算 待补足时长
                        long subMinutes = (durationDay - hours) * 60 - minutes;
                        // 判断费用
                        if (maxFlag){
                            // 判断 首计费金额 是否大于 每日最大金额
                            if (entity.getFirstAmountDay().compareTo(entity.getMaxAmountDay()) > 0){
                                resultMap.put("payAmount",payAmount.add(entity.getMaxAmountDay()));
                                resultMap.put("time",subMinutes);
                                return resultMap;
                            }
                        }
                        resultMap.put("payAmount",payAmount.add(entity.getFirstAmountDay()));
                        resultMap.put("time",subMinutes);
                        return resultMap;
                    } else {
                        // 停车时长 大于 首计费时长
                        // 计算超过 首计费时长的 时间
                        long lastMinutes = minutes + hourSub * 60;
                        // 剩余停车时长分钟数/计时单位 然后乘以 单位收费额
                        BigDecimal amount = new BigDecimal("0");
                        int makeUpTime = 0;
                        if (entity.getTimeUnitDay() > 0){
                            long unitCount = lastMinutes / entity.getTimeUnitDay();
                            long remainder = lastMinutes % entity.getTimeUnitDay();
                            if (remainder > 0){
                                unitCount = unitCount + 1;
                            }
                            // 计算费用
                            amount = entity.getUnitAmountDay().multiply(new BigDecimal(unitCount));
                            // +首计时费用
                            amount = amount.add(entity.getFirstAmountDay());
                            // 定义 待补足时长
                            makeUpTime = remainder == 0 ? 0 : entity.getTimeUnitDay() - TypeUtils.castToInt(remainder);
                        }

                        if (maxFlag){
                            // 判断 首计费金额 是否大于 每日最大金额
                            if (amount.compareTo(entity.getMaxAmountDay()) > 0){
                                resultMap.put("payAmount",payAmount.add(entity.getMaxAmountDay()));
                                resultMap.put("time", makeUpTime);
                                return resultMap;
                            }
                        }
                        resultMap.put("payAmount",payAmount.add(amount));
                        resultMap.put("time",makeUpTime);
                        return resultMap;
                    }
                } else {
                    // 分钟
                    // 停车时长总分钟数 - 首计时分钟数
                    long  minutesSub = allminutes - durationDay;
                    // 判断 剩余的 分钟数 是否大于0
                    if (minutesSub >= 0){
                        BigDecimal amount = new BigDecimal(0);
                        int makeUpTime = 0;
                        if (entity.getTimeUnitDay() > 0){
                            long unitCount = minutesSub / entity.getTimeUnitDay();
                            long remainder = minutesSub % entity.getTimeUnitDay();
                            if (remainder > 0){
                                unitCount = unitCount + 1;
                            }
                            // 计算费用
                            amount = entity.getUnitAmountDay().multiply(new BigDecimal(unitCount));
                            // +首计时费用
                            amount = amount.add(entity.getFirstAmountDay());
                            // 定义 待补足时长
                            makeUpTime = remainder == 0 ? 0 : entity.getTimeUnitDay() - TypeUtils.castToInt(remainder);
                        }
                        if (maxFlag){
                            // 判断 首计费金额 是否大于 每日最大金额
                            if (amount.compareTo(entity.getMaxAmountDay()) > 0){
                                resultMap.put("payAmount",payAmount.add(entity.getMaxAmountDay()));
                                resultMap.put("time", timeDiff - allminutes);
                                return resultMap;
                            }
                        }
                        resultMap.put("payAmount",payAmount.add(amount));
                        resultMap.put("time",makeUpTime);
                        return resultMap;
                    } else {
                        // 计算 待补足的分钟数  首计费时长 - 总停车时长
                        long subMinutes = durationDay - allminutes;
                        if (maxFlag){
                            // 判断 首计费金额 是否大于 每日最大金额
                            if (entity.getFirstAmountDay().compareTo(entity.getMaxAmountDay()) > 0){
                                resultMap.put("payAmount",payAmount.add(entity.getMaxAmountDay()));
                                resultMap.put("time",timeDiff - allminutes);
                                return resultMap;
                            }
                        }
                        resultMap.put("payAmount",payAmount.add(entity.getFirstAmountDay()));
                        resultMap.put("time",subMinutes);
                        return resultMap;
                    }
                }
            } else {
                // 首计费 不参与计费
                int makeUpTime = 0;
                BigDecimal amount = new BigDecimal(0);
                if (entity.getTimeUnitDay() > 0){
                    if (allStopMinutes > entity.getTimeUnitDay()) {
                        // 停车总时长 分钟数 / 计时单位
                        long unitCount = allminutes/entity.getTimeUnitDay();
                        long remainder = allminutes % entity.getTimeUnitDay();
                        if (remainder > 0){
                            unitCount = unitCount + 1;
                        }
                        // 定义 待补足时长
                        makeUpTime = remainder == 0 ? 0 : entity.getTimeUnitDay() - TypeUtils.castToInt(remainder);
                        // 计算费用
                        amount = entity.getUnitAmountDay().multiply(new BigDecimal(unitCount));

                    } else {
                        // 停车总时长 分钟数 / 计时单位
                        long unitCount = allminutes/entity.getTimeUnitDay();
                        long remainder = allminutes % entity.getTimeUnitDay();
                        if (remainder > 0){
                            unitCount = unitCount + 1;
                        }
                        // 定义 待补足时长
                        makeUpTime = remainder == 0 ? 0 : entity.getTimeUnitDay() - TypeUtils.castToInt(remainder);
                        // 计算费用
                        amount = entity.getUnitAmountDay().multiply(new BigDecimal(unitCount));
                    }
                }

                if (maxFlag){
                    // 判断 首计费金额 是否大于 每日最大金额
                    if (amount.compareTo(entity.getMaxAmountDay()) > 0){
                        resultMap.put("payAmount",payAmount.add(entity.getMaxAmountDay()));
                        if (allminutes >= entity.getTimeUnitNight()){
                            resultMap.put("time",0);
                        } else {
                            resultMap.put("time",entity.getTimeUnitNight() - allminutes);
                        }
                        return resultMap;
                    }
                }
                resultMap.put("payAmount",payAmount.add(amount));
                resultMap.put("time",makeUpTime);
                return resultMap;
            }

        } else {
            // 夜间时间段
            // 判断 每日最高费用 是否为0
            boolean maxFlag = false;
            // 最高费用 大于 0
            if (entity.getMaxAmountNight().compareTo(BigDecimal.ZERO) > 0){
                maxFlag = true;
            }

            // 根据 isOne 判断 是第几次进入 该方法
            if ("1".equals(isOne)) {
                // 第一次进入
                // 判断免费时长是否计费
                // 判断 停车时长 是否大于免费时长
                long freeTime = Long.valueOf(String.valueOf(entity.getFreeTimeNight()));
                if (allStopMinutes <= freeTime){
                    resultMap.put("payAmount", payAmount);
                    resultMap.put("time", freeTime);
                    return resultMap;
                }
                if (allminutes - freeTime > 0) {
                    if (entity.getIsFreeNight() == 0) {
                        // 免费时长计费 关闭状态  免费时长 不参与计费
                        if (freeTime > 0) {
                            // 新的开始时间  = 开始时间 + 免费时长
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(startDate);
                            cal.add(Calendar.MINUTE, entity.getFreeTimeNight());
                            startDate = cal.getTime();

                            // 重新 计算 停车时长 结束时间 - 新的开始时间
                            diff = endDate.getTime() - startDate.getTime();//这样得到的差值是微秒级别
                            days = diff / (1000 * 60 * 60 * 24);
                            hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                            minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                            if (hours > 0) {
                                allminutes = minutes + hours * 60;
                            } else {
                                allminutes = minutes;
                            }
                        }
                    }
                } else {
                    // 在免费时长内
                    if (entity.getIsFreeNight() == 0) {
                        resultMap.put("payAmount", payAmount);
                        resultMap.put("time", freeTime - minutes);
                        return resultMap;
                    } else {
                        // 判断 总停车时长是否大于 该时间段的免费时长
                        if (allStopMinutes > freeTime){
                            long durationDay = Long.valueOf(entity.getFirstDurationNight());
                            if(durationDay > 0){
                                long makeUpTime = entity.getFirstTimeTypeNight() == 1 ? durationDay*60 - minutes : durationDay - minutes;
                                resultMap.put("time", makeUpTime);
                                resultMap.put("payAmount", payAmount.add(entity.getFirstAmountNight()));
                            }else {
                                long makeUpTime = entity.getTimeUnitNight() - minutes;
                                resultMap.put("time", makeUpTime);
                                resultMap.put("payAmount", payAmount.add(entity.getUnitAmountNight()));
                            }
                        } else {
                            resultMap.put("payAmount", payAmount);
                            resultMap.put("time", freeTime - minutes);
                            return resultMap;
                        }
                    }
                }
            }
            // 判断首计费时长 是否 等于0 等于0  不参与计费  -- entity.getFirstAmountNight().compareTo(new BigDecimal(0)) > 0 &&
            if( Integer.valueOf(entity.getFirstDurationNight()) > 0 && "1".equals(isOne)){
                // 首计时 参与计费
                // 定义首计费 时长
                long durationNight = Long.valueOf(entity.getFirstDurationNight());
                // 判断 首计时 时长类型（1小时2分钟）
                if (entity.getFirstTimeTypeNight() == 1 ){
                    // 小时
                    // 停车时长 小时数 - 首计时时长
                    long hourSub = hours - durationNight;

                    if (hourSub < 0){
                        // 停车时长 小于 首计费时长
                        // 计算 待补足时长
                        long subMinutes = (durationNight - hours) * 60 - minutes;
                        // 判断费用
                        if (maxFlag){
                            // 判断 首计费金额 是否大于 每日最大金额
                            if (entity.getFirstAmountNight().compareTo(entity.getMaxAmountNight()) > 0){
                                resultMap.put("payAmount",payAmount.add(entity.getMaxAmountNight()));
                                resultMap.put("time",timeDiff - allminutes);
                                return resultMap;
                            }
                        }
                        resultMap.put("payAmount",payAmount.add(entity.getFirstAmountNight()));
                        resultMap.put("time",subMinutes);
                        return resultMap;
                    } else {
                        // 停车时长 大于 首计费时长
                        // 计算超过 首计费时长的 时间
                        long lastMinutes = minutes + hourSub * 60;
                        // 剩余停车时长分钟数/计时单位 然后乘以 单位收费额
                        BigDecimal amount = new BigDecimal(0);
                        int makeUpTime = 0;
                        if (entity.getTimeUnitNight() > 0){
                            long unitCount = lastMinutes / entity.getTimeUnitNight();
                            long remainder = lastMinutes % entity.getTimeUnitNight();
                            if (remainder > 0){
                                unitCount = unitCount + 1;
                            }
                            // 计算费用
                            amount = entity.getUnitAmountNight().multiply(new BigDecimal(unitCount));
                            // +首计时费用
                            amount = amount.add(entity.getFirstAmountNight());

                            // 定义 待补足时长
                            makeUpTime = remainder == 0 ? 0 : entity.getTimeUnitNight() - TypeUtils.castToInt(remainder);
                        }
                        if (maxFlag){
                            // 判断 计费金额 是否大于 每日最大金额
                            if (amount.compareTo(entity.getMaxAmountNight()) > 0){
                                resultMap.put("payAmount",payAmount.add(entity.getMaxAmountNight()));
                                resultMap.put("time", 0);
                                return resultMap;
                            }
                        }
                        resultMap.put("payAmount",payAmount.add(amount));
                        resultMap.put("time", makeUpTime);
                        return resultMap;
                    }
                } else {
                    // 分钟
                    // 停车时长总分钟数 - 首计时分钟数
                    long  minutesSub = allminutes - durationNight;
                    // 判断 剩余的 分钟数 是否大于0
                    if (minutesSub >= 0){
                        int makeUpTime = 0;
                        BigDecimal amount = new BigDecimal(0);
                        // 剩余停车时长分钟数/计时单位 然后乘以 单位收费额
                        if (entity.getTimeUnitNight() > 0){
                            long unitCount = minutesSub/entity.getTimeUnitNight();
                            long remainder = minutesSub % entity.getTimeUnitNight();
                            if (remainder > 0){
                                unitCount = unitCount + 1;
                            }
                            // 计算 待补足时间  计费时长 - 停车时长/计费时长的余数
                            makeUpTime = remainder == 0 ? 0 : entity.getTimeUnitNight() - TypeUtils.castToInt(remainder);
                            // 计算费用
                            amount = entity.getUnitAmountNight().multiply(new BigDecimal(unitCount));
                            // +首计时费用
                            amount = amount.add(entity.getFirstAmountNight());
                        }
                        if (maxFlag){
                            // 判断 首计费金额 是否大于 每日最大金额
                            if (amount.compareTo(entity.getMaxAmountNight()) > 0){
                                resultMap.put("payAmount",payAmount.add(entity.getMaxAmountNight()));
                                resultMap.put("time", 0);
                                return resultMap;
                            }
                        }
                        resultMap.put("payAmount",payAmount.add(amount));
                        resultMap.put("time",makeUpTime);
                        return resultMap;
                    } else {
                        // 计算 待补足的分钟数  首计费时长 - 总停车时长
                        long subMinutes = durationNight - allminutes;
                        if (maxFlag){
                            // 判断 首计费金额 是否大于 每日最大金额
                            if (entity.getFirstAmountNight().compareTo(entity.getMaxAmountNight()) > 0){
                                resultMap.put("payAmount",payAmount.add(entity.getMaxAmountNight()));
                                resultMap.put("time",timeDiff - allminutes);
                                return resultMap;
                            }
                        }
                        resultMap.put("payAmount",payAmount.add(entity.getFirstAmountNight()));
                        resultMap.put("time",subMinutes);
                        return resultMap;
                    }
                }

            } else {
                // 首计费 不参与计费
                // 停车总时长 分钟数 / 计时单位
                int makeUpTime = 0;
                BigDecimal amount = new BigDecimal(0);
                if (entity.getTimeUnitNight() > 0){
                    if (allStopMinutes >= entity.getTimeUnitNight()){
                        long unitCount = allminutes/entity.getTimeUnitNight();
                        long remainder = allminutes % entity.getTimeUnitNight();
                        if (remainder > 0){
                            unitCount = unitCount + 1;
                        }

                        // 定义 待补足时长
                        makeUpTime = remainder == 0 ? 0 : entity.getTimeUnitNight() - TypeUtils.castToInt(remainder);
                        // 计算费用
                        amount = entity.getUnitAmountNight().multiply(new BigDecimal(unitCount));
                    } else {
                        long unitCount = allminutes/entity.getTimeUnitNight();
                        long remainder = allminutes % entity.getTimeUnitNight();
                        if (remainder > 0){
                            unitCount = unitCount + 1;
                        }
                        // 定义 待补足时长
                        makeUpTime = remainder == 0 ? 0 : entity.getTimeUnitNight() - TypeUtils.castToInt(remainder);
                        // 计算费用
                        amount = entity.getUnitAmountNight().multiply(new BigDecimal(unitCount));
                    }


                }

                if (maxFlag){
                    // 判断 首计费金额 是否大于 每日最大金额
                    if (amount.compareTo(entity.getMaxAmountNight()) > 0){
                        resultMap.put("payAmount",payAmount.add(entity.getMaxAmountNight()));
                        if (allminutes >= entity.getTimeUnitNight()){
                            resultMap.put("time",0);
                        } else {
                            resultMap.put("time",entity.getTimeUnitNight() - allminutes);
                        }
                        return resultMap;
                    } else {
                        resultMap.put("payAmount",payAmount.add(amount));
                        resultMap.put("time",makeUpTime);
                        return resultMap;
                    }
                } else {
                    resultMap.put("payAmount",payAmount.add(amount));
                    resultMap.put("time",makeUpTime);
                    return resultMap;
                }
            }
        }
    }



}