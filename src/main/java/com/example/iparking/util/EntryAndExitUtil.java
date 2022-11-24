package com.example.iparking.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EntryAndExitUtil {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     *
     * @param parkId
     * @param plateNumber
     * @param carColor
     * @param type
     * @param judgeTime
     * @return
     * @throws ParseException
     */
//    public Map<String, String> isMonthlyCar(String parkId, String plateNumber, String carColor, String type, String judgeTime) throws ParseException {
//        Map<String, String> returnMap = new HashMap<>();
//        returnMap.put("code","200");
//        try{
//            // 定义当前时间
//            String nowDateStr = "";
//            if (judgeTime != null){
//                nowDateStr = judgeTime;
//            } else {
//                nowDateStr = format.format(new Date());
//            }
//            Date nowDate = format.parse(nowDateStr);
//            // 定义变量用于确认 是否是 包月车
//
//            String monthlyStartTime = ""; // 包月开始时间
//            String monthlyEndime = ""; // 包月结束时间
//            String oldTime = ""; // 包月上次到期时间
//            String associationId = ""; // 包月主表id
//
//            // 根据车场主键，车牌号 查询车辆在该车场下是否是包月车
//            Map<String, String> map = new HashMap<>();
//            map.put("parkId", parkId);
//            map.put("number", plateNumber);
//            map.put("carColor", carColor);
//            List<Map<String, Object>> dataList = vehicleMapper.queryMonthlyMsg(map);
//
//            // 根据 type类型判断出入场操作
//            // 入场操作时 判断 包月车辆在停数量 和 包月泊位数量
//            if ("1".equals(type)){
//                // 入场操作
//                if (dataList != null && dataList.size() > 0){
//                    // 循环判断是否是有效数据
//                    for (int i=0;i<dataList.size();i++){
//
//                        oldTime = dataList.get(i).get("oldTime").toString();
//                        monthlyStartTime = dataList.get(i).get("startingTime").toString();
//                        monthlyEndime = dataList.get(i).get("endTime").toString();
//                        associationId = dataList.get(i).get("id").toString();
//                        // 包月车位数量
//                        int spaceNum = Integer.parseInt(dataList.get(i).get("carQuantity").toString());
//
//                        // 判断当前时间 是否在 包月的起始时间内
//                        String beginTime = dataList.get(i).get("startingTime").toString();
//                        String endTime = dataList.get(i).get("endTime").toString();
//                        Date beginDate = datetimeFormat.parse(beginTime);
//                        Date endDate = datetimeFormat.parse(endTime);
//                        // 包月开始时间 - 当前时间
//                        long diffTime = beginDate.getTime() - nowDate.getTime();
//                        // 包月结束时间 - 当前时间
//                        long diffTime2 = endDate.getTime() - nowDate.getTime();
//                        // 判断 diffTime和diffTime2
//                        if (diffTime > 0 && diffTime2 > 0){
//                            // 包月开始时间 - 当前时间 大于 0 并且 包月结束时间 - 当前时间 大于0 包月未开始
//                            returnMap.put("isTure", "1"); // 包月
//                            returnMap.put("isExpire", "2"); // 未开始
//                            returnMap.put("expireTime", endTime);
//                            returnMap.put("oldTime", oldTime);
//                            returnMap.put("monthlyStartTime", monthlyStartTime);
//                            returnMap.put("monthlyEndime", monthlyEndime);
//                            returnMap.put("remainingDay", "0");
//                            returnMap.put("associationId", associationId);
//                            return returnMap;
//                        } else if (diffTime <= 0 && diffTime2 > 0){
//                            // 包月开始时间 - 当前时间 小于等于 0 并且 包月结束时间 - 当前时间 大于等于0 包月进行中
//                            // 在包月区间内
//                            // 计算包月剩余天数
//                            long nd = 1000 * 24 * 60 * 60;
//                            long time = (endDate.getTime() - nowDate.getTime());
//                            long remainNum = time%nd == 0 ? time/ nd : time/ nd + 1;
//                            String remainSrt = String.valueOf(remainNum);
//
//                            // 判断 状态 （是否支付/是否启用/是否有效/是否审核）
//                            if ("2".equals(dataList.get(i).get("status").toString())){
//                                // 未支付
//                                returnMap.put("isTure", "0"); // 不是包月车
//                                returnMap.put("isExpire", "0"); // 过期状态
//                                returnMap.put("remainingDay", "0"); // 剩余包月天数
//                                returnMap.put("expireTime", ""); // 到期时间
//                                returnMap.put("oldTime",  ""); // 上次包月结束时间
//                                returnMap.put("monthlyStartTime",  ""); // 包月开始时间
//                                returnMap.put("monthlyEndime",  ""); // 包月结束时间
//                                returnMap.put("associationId",  ""); // 包月主键id
//                                return returnMap;
//                            }
//                            if ("2".equals(dataList.get(i).get("enable").toString())){
//                                // 禁用状态
//                                returnMap.put("isTure", "0"); // 不是包月车
//                                returnMap.put("isExpire", "0"); // 过期状态
//                                returnMap.put("remainingDay", "0"); // 剩余包月天数
//                                returnMap.put("expireTime", ""); // 到期时间
//                                returnMap.put("oldTime",  ""); // 上次包月结束时间
//                                returnMap.put("monthlyStartTime",  ""); // 包月开始时间
//                                returnMap.put("monthlyEndime",  ""); // 包月结束时间
//                                returnMap.put("associationId",  ""); // 包月主键id
//                                return returnMap;
//                            }
//
//
//                            if ("1".equals(dataList.get(i).get("auditStatus").toString())){
//                                // 待审核
//                                // 比较oldTime和当前时间
//                                if (!"".equals(dataList.get(i).get("oldTime"))){
//                                    Date oldDate = datetimeFormat.parse(dataList.get(i).get("oldTime").toString());
//                                    // 包月开始时间 - 当前时间
//                                    long oldDiffTime = oldDate.getTime() - nowDate.getTime();
//                                    if (oldDiffTime >= 0){
//                                        // 判断 是否是一位多车的情况
//
//                                        // 定义 当前在场 车辆数
//                                        int inParkCarNum = 0;
//                                        // 在 p_ve_association_sub 表中 查询 包月车辆信息
//                                        List<VeAssociationSubDO> subList = vehicleMapper.queryAssociationSubListById(Long.valueOf(dataList.get(i).get("id").toString()));
//                                        if (subList != null && subList.size() > 0){
//
//                                            if (subList.size() > 1) {
//
//                                                for (VeAssociationSubDO subEntity : subList) {
//                                                    // 循环查找其他 车牌 目前 在该车场停车
//                                                    if (!plateNumber.equals(subEntity.getPlateNumber())) {
//                                                        ParkRecordQuery parkRecordQuery = new ParkRecordQuery();
//                                                        parkRecordQuery.setLotId(Integer.valueOf(parkId));
//                                                        parkRecordQuery.setCarNum(subEntity.getPlateNumber());
//                                                        ParkRecordDO parkRecordDO = parkRecordMapper.carIsStopping(parkRecordQuery);
//                                                        if (parkRecordDO != null) {
//                                                            inParkCarNum++;
//                                                        }
//                                                    }
//                                                }
//                                                // 判断 包月车位数 和在停车辆数量
//                                                if (inParkCarNum >= spaceNum){
//                                                    returnMap.put("isTure", "1");
//                                                    returnMap.put("isExpire", "3"); // 一位多车
//                                                    returnMap.put("expireTime", endTime);
//                                                    returnMap.put("oldTime", oldTime);
//                                                    returnMap.put("monthlyStartTime", monthlyStartTime);
//                                                    returnMap.put("monthlyEndime", monthlyEndime);
//                                                    returnMap.put("remainingDay", remainSrt);
//                                                    returnMap.put("associationId", associationId);
//                                                    return returnMap;
//                                                } else {
//                                                    returnMap.put("isTure", "1");
//                                                    returnMap.put("isExpire", "0");
//                                                    returnMap.put("expireTime", endTime);
//                                                    returnMap.put("oldTime", oldTime);
//                                                    returnMap.put("monthlyStartTime", monthlyStartTime);
//                                                    returnMap.put("monthlyEndime", monthlyEndime);
//                                                    returnMap.put("remainingDay", remainSrt);
//                                                    returnMap.put("associationId", associationId);
//                                                    return returnMap;
//                                                }
//
//                                            } else {
//                                                returnMap.put("isTure", "1"); // 包月
//                                                returnMap.put("isExpire", "0"); // 未过期
//                                                returnMap.put("expireTime", endTime);
//                                                returnMap.put("oldTime", oldTime);
//                                                returnMap.put("monthlyStartTime", monthlyStartTime);
//                                                returnMap.put("monthlyEndime", monthlyEndime);
//                                                returnMap.put("remainingDay", remainSrt);
//                                                returnMap.put("associationId", associationId);
//                                                return returnMap;
//                                            }
//                                        } else {
//                                            // 包月车子表信息 为空 不是包月
//                                            returnMap.put("isTure", "0"); // 不是包月车
//                                            returnMap.put("isExpire", "0"); // 过期状态
//                                            returnMap.put("remainingDay", "0"); // 剩余包月天数
//                                            returnMap.put("expireTime", endTime);
//                                            returnMap.put("oldTime", oldTime);
//                                            returnMap.put("monthlyStartTime", monthlyStartTime);
//                                            returnMap.put("monthlyEndime", monthlyEndime);
//                                            returnMap.put("associationId", associationId);
//                                            return returnMap;
//                                        }
//                                    } else {
//                                        // 续费后，当前时间大于续费前的结束时间需要审核才能使用
//                                        returnMap.put("isTure", "1"); // 包月
//                                        returnMap.put("isExpire", "1"); // 过期
//                                        returnMap.put("expireTime", endTime);
//                                        returnMap.put("oldTime", oldTime);
//                                        returnMap.put("monthlyStartTime", monthlyStartTime);
//                                        returnMap.put("monthlyEndime", monthlyEndime);
//                                        returnMap.put("remainingDay", "0");
//                                        returnMap.put("associationId", associationId);
//                                        return returnMap;
//                                    }
//                                } else {
//                                    returnMap.put("isTure", "0"); // 不是包月车
//                                    returnMap.put("isExpire", "0"); // 过期状态
//                                    returnMap.put("remainingDay", "0"); // 剩余包月天数
//                                    returnMap.put("expireTime", endTime);
//                                    returnMap.put("oldTime", oldTime);
//                                    returnMap.put("monthlyStartTime", monthlyStartTime);
//                                    returnMap.put("monthlyEndime", monthlyEndime);
//                                    returnMap.put("associationId", associationId);
//                                    return returnMap;
//                                }
//                            }
//
//                            // 判断 是否是一位多车的情况
//                            // 定义 当前在场 车辆数
//                            int inParkCarNum = 0;
//                            // 在 p_ve_association_sub 表中 查询 包月车辆信息
//                            List<VeAssociationSubDO> subList = vehicleMapper.queryAssociationSubListById(Long.valueOf(dataList.get(i).get("id").toString()));
//                            if (subList != null && subList.size() > 0){
//                                if (subList.size() > 1) {
//                                    for (VeAssociationSubDO subEntity : subList) {
//                                        // 循环查找其他 车牌 目前 在该车场停车
//                                        if (!plateNumber.equals(subEntity.getPlateNumber())) {
//                                            ParkRecordQuery parkRecordQuery = new ParkRecordQuery();
//                                            parkRecordQuery.setLotId(Integer.valueOf(parkId));
//                                            parkRecordQuery.setCarNum(subEntity.getPlateNumber());
//                                            ParkRecordDO parkRecordDO = parkRecordMapper.carIsStopping(parkRecordQuery);
//                                            if (parkRecordDO != null) {
//                                                inParkCarNum++;
//                                            }
//                                        }
//                                    }
//                                    // 判断 包月车位数 和在停车辆数量
//                                    if (inParkCarNum >= spaceNum){
//                                        returnMap.put("isTure", "1");
//                                        returnMap.put("isExpire", "3"); // 一位多车
//                                        returnMap.put("expireTime", endTime);
//                                        returnMap.put("oldTime", oldTime);
//                                        returnMap.put("monthlyStartTime", monthlyStartTime);
//                                        returnMap.put("monthlyEndime", monthlyEndime);
//                                        returnMap.put("remainingDay", remainSrt);
//                                        returnMap.put("associationId", associationId);
//                                        return returnMap;
//                                    } else {
//                                        returnMap.put("isTure", "1");
//                                        returnMap.put("isExpire", "0");
//                                        returnMap.put("expireTime", endTime);
//                                        returnMap.put("oldTime", oldTime);
//                                        returnMap.put("monthlyStartTime", monthlyStartTime);
//                                        returnMap.put("monthlyEndime", monthlyEndime);
//                                        returnMap.put("remainingDay", remainSrt);
//                                        returnMap.put("associationId", associationId);
//                                        return returnMap;
//                                    }
//
//                                }  else {
//                                    returnMap.put("isTure", "1"); // 包月
//                                    returnMap.put("isExpire", "0"); // 未过期
//                                    returnMap.put("expireTime", endTime);
//                                    returnMap.put("oldTime", oldTime);
//                                    returnMap.put("monthlyStartTime", monthlyStartTime);
//                                    returnMap.put("monthlyEndime", monthlyEndime);
//                                    returnMap.put("remainingDay", remainSrt);
//                                    returnMap.put("associationId", associationId);
//                                    return returnMap;
//                                }
//                            }else {
//                                // 包月车子表信息 为空 不是包月
//                                returnMap.put("isTure", "0"); // 不是包月车
//                                returnMap.put("isExpire", "0"); // 过期状态
//                                returnMap.put("remainingDay", "0"); // 剩余包月天数
//                                returnMap.put("expireTime", endTime);
//                                returnMap.put("oldTime", oldTime);
//                                returnMap.put("monthlyStartTime", monthlyStartTime);
//                                returnMap.put("monthlyEndime", monthlyEndime);
//                                returnMap.put("associationId", associationId);
//                                return returnMap;
//                            }
//
//
//                        } else {
//                            // 包月开始时间 - 当前时间 小于0 并且 包月结束时间 - 当前时间 小于 0 包月已过期
//                            if ("1".equals(dataList.get(i).get("isValid").toString())){
//                                // 修改 包月信息 过期
//                                vehicleMapper.updateIsValid(Long.parseLong(dataList.get(i).get("id").toString()));
//                            }
//                            returnMap.put("isTure", "1"); // 包月
//                            returnMap.put("isExpire", "1"); // 过期
//                            returnMap.put("expireTime", endTime);
//                            returnMap.put("oldTime", oldTime);
//                            returnMap.put("monthlyStartTime", monthlyStartTime);
//                            returnMap.put("monthlyEndime", monthlyEndime);
//                            returnMap.put("remainingDay", "0");
//                            returnMap.put("associationId", associationId);
//                            return returnMap;
//                        }
//                    }
//                }
//                returnMap.put("isTure", "0"); // 不是包月车
//                returnMap.put("isExpire", "0"); // 过期状态
//                returnMap.put("remainingDay", "0"); // 剩余包月天数
//                returnMap.put("expireTime", ""); // 到期时间
//                returnMap.put("oldTime",  ""); // 上次包月结束时间
//                returnMap.put("monthlyStartTime",  ""); // 包月开始时间
//                returnMap.put("monthlyEndime",  ""); // 包月结束时间
//                returnMap.put("associationId",  ""); // 包月主键id
//                return returnMap;
//            } else {
//                // 出场操作
//                if (dataList != null && dataList.size() > 0){
//                    // 循环判断是否是有效数据
//                    for (int i=0;i<dataList.size();i++){
//                        oldTime = dataList.get(i).get("oldTime").toString();
//                        monthlyStartTime = dataList.get(i).get("startingTime").toString();
//                        monthlyEndime = dataList.get(i).get("endTime").toString();
//                        associationId = dataList.get(i).get("id").toString();
//
//                        // 判断当前时间 是否在 包月的起始时间内
//                        String beginTime = dataList.get(i).get("startingTime").toString();
//                        String endTime = dataList.get(i).get("endTime").toString();
//                        Date beginDate = datetimeFormat.parse(beginTime);
//                        Date endDate = datetimeFormat.parse(endTime);
//                        // 包月开始时间 - 当前时间
//                        long diffTime = beginDate.getTime() - nowDate.getTime();
//                        // 包月结束时间 - 当前时间
//                        long diffTime2 = endDate.getTime() - nowDate.getTime();
//                        // 判断 diffTime和diffTime2
//                        if (diffTime > 0 && diffTime2 > 0){
//                            // 包月开始时间 - 当前时间 大于 0 并且 包月结束时间 - 当前时间 大于0 包月未开始
//                            returnMap.put("isTure", "1"); // 包月
//                            returnMap.put("isExpire", "2"); // 未开始
//                            returnMap.put("expireTime", endTime);
//                            returnMap.put("oldTime", oldTime);
//                            returnMap.put("monthlyStartTime", monthlyStartTime);
//                            returnMap.put("monthlyEndime", monthlyEndime);
//                            returnMap.put("remainingDay", "0");
//                            returnMap.put("associationId", associationId);
//                            return returnMap;
//                        } else if (diffTime <= 0 && diffTime2 > 0){
//                            // 包月开始时间 - 当前时间 小于等于 0 并且 包月结束时间 - 当前时间 大于等于0 包月进行中
//                            // 在包月区间内
//                            // 计算包月剩余天数
//                            long nd = 1000 * 24 * 60 * 60;
//                            long time = (endDate.getTime() - nowDate.getTime());
//                            long remainNum = time%nd == 0 ? time/ nd : time/ nd + 1;
//                            String remainSrt = String.valueOf(remainNum);
//
//                            // 判断 状态 （是否支付/是否启用/是否有效/是否审核）
//                            if ("2".equals(dataList.get(i).get("status").toString())){
//                                // 未支付
//                                returnMap.put("isTure", "0"); // 不是包月车
//                                returnMap.put("isExpire", "0"); // 过期状态
//                                returnMap.put("remainingDay", "0"); // 剩余包月天数
//                                returnMap.put("expireTime", ""); // 到期时间
//                                returnMap.put("oldTime",  ""); // 上次包月结束时间
//                                returnMap.put("monthlyStartTime",  ""); // 包月开始时间
//                                returnMap.put("monthlyEndime",  ""); // 包月结束时间
//                                returnMap.put("associationId",  ""); // 包月主键id
//                                return returnMap;
//                            }
//                            if ("2".equals(dataList.get(i).get("enable").toString())){
//                                // 禁用状态
//                                returnMap.put("isTure", "0"); // 不是包月车
//                                returnMap.put("isExpire", "0"); // 过期状态
//                                returnMap.put("remainingDay", "0"); // 剩余包月天数
//                                returnMap.put("expireTime", ""); // 到期时间
//                                returnMap.put("oldTime",  ""); // 上次包月结束时间
//                                returnMap.put("monthlyStartTime",  ""); // 包月开始时间
//                                returnMap.put("monthlyEndime",  ""); // 包月结束时间
//                                returnMap.put("associationId",  ""); // 包月主键id
//                                return returnMap;
//                            }
//                            if ("1".equals(dataList.get(i).get("auditStatus").toString())){
//                                // 待审核
//                                // 比较oldTime和当前时间
//                                Date oldDate = datetimeFormat.parse(dataList.get(i).get("oldTime").toString());
//                                // 包月开始时间 - 当前时间
//                                long oldDiffTime = oldDate.getTime() - nowDate.getTime();
//                                if (oldDiffTime >= 0){
//                                    returnMap.put("isTure", "1"); // 包月
//                                    returnMap.put("isExpire", "0"); // 未过期
//                                    returnMap.put("expireTime", endTime);
//                                    returnMap.put("oldTime", oldTime);
//                                    returnMap.put("monthlyStartTime", monthlyStartTime);
//                                    returnMap.put("monthlyEndime", monthlyEndime);
//                                    returnMap.put("remainingDay", remainSrt);
//                                    returnMap.put("associationId", associationId);
//                                    return returnMap;
//                                } else {
//                                    // 续费后，当前时间大于续费前的结束时间需要审核才能使用
//                                    returnMap.put("isTure", "1"); // 包月
//                                    returnMap.put("isExpire", "1"); // 过期
//                                    returnMap.put("expireTime", endTime);
//                                    returnMap.put("oldTime", oldTime);
//                                    returnMap.put("monthlyStartTime", monthlyStartTime);
//                                    returnMap.put("monthlyEndime", monthlyEndime);
//                                    returnMap.put("remainingDay", remainSrt);
//                                    returnMap.put("associationId", associationId);
//                                    return returnMap;
//                                }
//                            }
//                            returnMap.put("isTure", "1"); // 包月
//                            returnMap.put("isExpire", "0"); // 未过期
//                            returnMap.put("expireTime", endTime);
//                            returnMap.put("oldTime", oldTime);
//                            returnMap.put("monthlyStartTime", monthlyStartTime);
//                            returnMap.put("monthlyEndime", monthlyEndime);
//                            returnMap.put("remainingDay", remainSrt);
//                            returnMap.put("associationId", associationId);
//                            return returnMap;
//
//                        } else {
//                            // 包月开始时间 - 当前时间 小于0 并且 包月结束时间 - 当前时间 小于 0 包月已过期
//                            if ("1".equals(dataList.get(i).get("isValid").toString())){
//                                // 修改 包月信息 过期
//                                vehicleMapper.updateIsValid(Long.parseLong(dataList.get(i).get("id").toString()));
//                            }
//                            returnMap.put("isTure", "1"); // 包月
//                            returnMap.put("isExpire", "1"); // 过期
//                            returnMap.put("expireTime", endTime);
//                            returnMap.put("oldTime", oldTime);
//                            returnMap.put("monthlyStartTime", monthlyStartTime);
//                            returnMap.put("monthlyEndime", monthlyEndime);
//                            returnMap.put("remainingDay", "0");
//                            returnMap.put("associationId", associationId);
//                            return returnMap;
//                        }
//                    }
//                }
//                returnMap.put("isTure", "0"); // 不是包月车
//                returnMap.put("isExpire", "0"); // 过期状态
//                returnMap.put("remainingDay", "0"); // 剩余包月天数
//                returnMap.put("expireTime", ""); // 到期时间
//                returnMap.put("oldTime",  ""); // 上次包月结束时间
//                returnMap.put("monthlyStartTime",  ""); // 包月开始时间
//                returnMap.put("monthlyEndime",  ""); // 包月结束时间
//                returnMap.put("associationId",  ""); // 包月主键id
//                return returnMap;
//            }
//        } catch (Exception e){
//            returnMap.put("code","500");
//            returnMap.put("msg","查询包月信息失败，请稍后再试！");
//            log.info("查询包月信息失败：+++" + e.getMessage());
//            return returnMap;
//        }
//    }
}
