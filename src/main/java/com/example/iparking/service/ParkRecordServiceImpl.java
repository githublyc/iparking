package com.example.iparking.service;

import com.example.iparking.common.Result;
import com.example.iparking.dao.DishonCarMapper;
import com.example.iparking.dao.ParkMapper;
import com.example.iparking.pojo.DishonCarDO;
import com.example.iparking.pojo.ParkDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParkRecordServiceImpl {

    @Autowired
    ParkMapper parkMapper;
    @Autowired
    DishonCarMapper dishonCarMapper;

    private Result admission(Map<String, Object> map) {
        try {
            long startTime = System.currentTimeMillis();
            Result result = new Result();
            // 定义返回
            Map<String, String> resultMap = new HashMap<>();
            // 定义时间格式
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 当前时间
            Date nowDate = new Date();
            String nowTime = format.format(nowDate);

            // 判断车牌号是否为空
            if ("".equals(map.get("carnum").toString())) {
                result.setCode(500);
                result.setMsg("车牌号为空，不做操作！");
                return result;
            }
            // 车牌号
            String carNumber = map.get("carnum").toString();

            // 根据 车牌颜色 判断 车辆类型
            String carType = "";
            if (map.get("carnum_color").toString() != null) {
                switch (map.get("carnum_color").toString()) {
                    case "0":
                        carType = "0";
                        break;
                    case "1":
                        carType = "1";
                        break;
                    case "2":
                        carType = "2";
                        break;
                    case "3":
                        carType = "4";
                        break;
                    case "4":
                        carType = "1";
                        break;
                    case "5":
                        carType = "4";
                        break;
                }
            } else {
                result.setCode(500);
                result.setMsg("车牌颜色不可为空！");
                return result;
            }

            // 定义 参数 推送消息到平台上时 使用
            String ifArrears = "0"; // 无欠费记录

            // 查询车场信息
            ParkDO parkDO = parkMapper.getParkByParkKey(Integer.valueOf(map.get("parkid").toString()));
            if (parkDO == null) {
                result.setCode(500);
                result.setMsg("暂未查询到该车场信息！");
                return result;
            }

            // 根据 车牌号查询 是否是失信车辆
            DishonCarDO dishonCarDO = dishonCarMapper.selectByCarNumber(carNumber);
            if (dishonCarDO != null) {
                // 判断 法官电话 是否为空
                if (!"".equals(dishonCarDO.getJudgePhone())) {
                    // 组装数据 推送微信消息
                    Map<String, Object> param = new HashMap<>();
                    param.put("first", "威海停车温馨提示");
                    param.put("keyword1", carNumber);// 车牌号
                    param.put("keyword2", dishonCarDO.getDisName());// 车主
                    param.put("keyword3", nowTime);// 时间
                    param.put("keyword4", parkDO.getParkName());// 停车场
                    param.put("keyword5", parkDO.getDetailedAddress()); // 地点
                    param.put("remark", "该车辆被识别发现，请及时处理"); // remark
                    param.put("phoneList", dishonCarDO.getJudgePhone().split(",")); //手机号
                    param.put("openId", "");
                    param.put("wxUrl", "");
                    //parkRecordUtils.sendWxMsg(param);
                }
            }

            String vehicleType = "1";
            String paymentStatus = "1";

//            if ("1".equals(vehicleType)) {
//                // 判断 车辆类型（包月车/临时车）
//                Map<String, String> monthlyCar = entryAndExitUtil.isMonthlyCar(String.valueOf(parkDO.getParkKey()), carNumber, map.get("carnum_color").toString(), "1", nowTime);
//                if ("200".equals(monthlyCar.get("code"))) {
//                    if ("1".equals(monthlyCar.get("isTure"))) {
//                        // 包月车
//                        if ("0".equals(monthlyCar.get("isExpire"))) {
//                            // 未过期
//                            vehicleType = "3";
//                            paymentStatus = "3";
//                            associationId = monthlyCar.get("associationId");
//                            carTypeShow = "包月车";
//                        } else if ("1".equals(monthlyCar.get("isExpire"))) {
//                            // 已过期 判断 包月车过期设置找给你的 进场设置 （禁止（返回提示 禁止入场）/允许（入场操作））
//                            Integer inType = entryAndExitUtil.getEndMonthly(Integer.valueOf(map.get("parkid").toString()));
//                            if (1 == inType) {
//                                result.setCode(200);
//                                result.setMsg("包月车过期，禁止入场！！");
//                                resultMap.put("paymentStatus", "102");
//                                resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                                resultMap.put("recordKey", "0"); // 返回停车记录id
//                                result.setData(resultMap);
//                                long queryTime = System.currentTimeMillis() - startTime;
//                                StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                                return result;
//                            } else {
//                                paymentStatus = "1";
//                                vehicleType = "2"; // 月临车
//                                carTypeShow = "月临车";
//                            }
//                            // 过期
//                            vehicleType = "1";
//                            paymentStatus = "1";
//                            carTypeShow = "临停车";
//                        } else if ("2".equals(monthlyCar.get("isExpire"))) {
//                            // 包月未开始
//                            vehicleType = "1";
//                            paymentStatus = "1";
//                            carTypeShow = "临停车";
//                        } else {
//                            // 一位多车情况
//                            vehicleType = "1";
//                            paymentStatus = "1";
//                            carTypeShow = "临停车";
//                            businessType = "1"; // 出场时不需要判断是否是包月
//                            oneMoreCar = "1";
//                        }
//                    } else {
//                        // 临时车
//                        vehicleType = "1";
//                        paymentStatus = "1";
//                        carTypeShow = "临停车";
//                    }
//                } else {
//                    result.setCode(500);
//                    result.setMsg("入场失败，请稍后再试！");
//                    System.out.println("道闸入场失败");
//                    return result;
//                }
//            }
//
//            //如果不是包月车，判断是不是特殊车
//            if (!"3".equals(paymentStatus)){
//                //判断是否是免费车、储值车
//                Map<String, String> isSpecialVehicle = entryAndExitUtil.isSpecialVehicle(String.valueOf(parkDO.getParkKey()), plateNumber);
//                if ("200".equals(isSpecialVehicle.get("code"))) {
//                    vehicleType = isSpecialVehicle.get("type");
//                    paymentStatus = isSpecialVehicle.get("type");
//                    if ("4".equals(isSpecialVehicle.get("type"))) {
//                        carTypeShow = "免费车";
//                    } else {
//                        carTypeShow = "储值车";
//                    }
//                }
//
//                //判断是否是信用停车
//                Map<String, String> creditCar = entryAndExitUtil.isCreditCar(String.valueOf(parkDO.getParkKey()), plateNumber);
//                if ("200".equals(creditCar.get("code"))) {
//                    vehicleType = creditCar.get("type");
//                    paymentStatus = creditCar.get("type");// 7 信用停车
//                }
//            }
//
//            // 如果是军警车 设置paymentstatus=4
//            if ("3".equals(map.get("carnum_color").toString())) {
//                paymentStatus = "4";
//                carTypeShow = "军警车";
//            }
//
//            //判断车辆是否是临时车
//            //Integer carStatus = vehicleMapper.getCarStatus(map.get("parkid").toString(), plateNumber);
//            //判断是否一位多车
//            if ("1".equals(vehicleType)) {
//                //临时车
//                //判断该车场临时车是否可以入场
//                if ("1".equals(parkDO.getTemporaryVehicleMobilization())) {
//                    //不管 进不进，都要生成一条停车记录
//                    // 1.生成 p_park_record 信息
//                    ParkRecordDO parkRecordDO = new ParkRecordDO();
//                    parkRecordDO.setParkKey(Integer.valueOf(map.get("parkid").toString()));
//                    parkRecordDO.setParkName(parkDO.getParkName());
//                    parkRecordDO.setParkModule(parkDO.getParkModule());
//                    parkRecordDO.setAreaKey(0);
//                    parkRecordDO.setPlateNumber(map.get("carnum").toString());
//                    parkRecordDO.setCarType(carType);
//                    parkRecordDO.setVehicleType("1");//
//                    parkRecordDO.setPlateStatus("1");
//                    parkRecordDO.setPlateColor(map.get("carnum_color").toString());
//                    parkRecordDO.setEnterWay("1");
//                    parkRecordDO.setPaymentStatus("1");
//                    parkRecordDO.setEntryDrivewayKey(Integer.valueOf(map.get("driveway_key").toString()));
//                    parkRecordDO.setEntryDrivewayName(map.get("driveway_name").toString());
//                    parkRecordDO.setSensorEntryTime(format.format(nowDate));
//                    parkRecordDO.setStopStatus("1");
//                    parkRecordDO.setVerifyWay("2");
//                    parkRecordDO.setVerifyStatus("1");
//                    parkRecordDO.setClearVerifyStatus("0");
//                    parkRecordDO.setClearOverStatus("0");
//                    parkRecordDO.setCreatedAt(nowDate);
//                    parkRecordDO.setUpdatedAt(nowDate);
//                    parkRecordDO.setAmountPaid(new BigDecimal(0));
//                    parkRecordDO.setAmountPayable(new BigDecimal(0));
//                    parkRecordDO.setAmountPrepay(new BigDecimal(0));
//                    parkRecordDO.setUnpaidAmount(new BigDecimal(0));
//                    parkRecordDO.setImgNumber(0);
//                    parkRecordDO.setBusinessType("1");
//                    parkRecordMapper.insert(parkRecordDO);
//
//                    //3、在p_park_record_resume表中记录入场履历信息
//                    ParkRecordResumeDO parkRecordResume = new ParkRecordResumeDO();
//                    parkRecordResume.setRecordKey(parkRecordDO.getRecordKey());
//                    //操作类型,包括：自动驶入，人工驶入，自动驶离，人工驶离等
//                    parkRecordResume.setOperationType("社区手动进场");
//                    //parkRecordResume.setOperatorType("1");
//                    //操作前状态(1：未缴费 2：正常缴费 3：包月 4：免费)
//                    parkRecordResume.setBeforeStatus("未缴费");
//                    //操作后状态(1：未缴费 2：正常缴费 3：包月 4：免费)
//                    parkRecordResume.setAfterStatus("未缴费");
//                    //描述操作内容
//                    parkRecordResume.setDescription("社区手动进场");
//                    parkRecordResume.setCreatedAt(nowDate);
//                    parkRecordResumeMapper.insertSelective(parkRecordResume);
//
//                    /** 发送mqtt 消息 */
//                    Map<String, String> mqttMap = new HashMap<>();
//                    mqttMap.put("inoutType", "in");
//                    mqttMap.put("ifArrears", ifArrears);
//                    mqttMap.put("plateNumber", plateNumber);
//                    mqttMap.put("parkKey", map.get("parkid").toString());
//                    mqttMap.put("parkName", parkDO.getParkName());
//                    mqttMap.put("cdKey", map.get("driveway_key").toString());
//                    mqttMap.put("drivewayName", map.get("driveway_name").toString());
//                    if (!map.get("img").toString().isEmpty()) {
//                        mqttMap.put("imgUrl", map.get("img").toString());
//                    } else {
//                        mqttMap.put("imgUrl", "");
//                    }
//                    mqttMap.put("inoutTime", format.format(nowDate));
//                    mqttMap.put("errMsg", "该车场禁止临时车入内！");
//                    //MqqttServers.pushMsgToPlatform(mqttMap);
//                    //组装参数
//                    MqttDTO mqttDTO = new MqttDTO();
//                    mqttDTO.setParam(mqttMap);
//                    //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                    kafkaProductor.sendMessage(
//                            KafkaConstants.TOPIC.MQTT_CLIENT,
//                            PkCreat.getTablePk(),
//                            JsonUtils.toSyncJson(mqttDTO, KafkaConstants.ACTION.PUSH_MSG_TO_PLATFORM)
//                    );
//                    //临时车禁止进入
//                    //推送到闸机
//                    //MqqttServers.pushMsgForBlackCar(map.get("parkid").toString(), map.get("driveway_key").toString(), dataMap.get("ip"));
//                    MqttDTO mqttDTO2 = new MqttDTO();
//                    mqttDTO2.setParkKey(map.get("parkid").toString());
//                    mqttDTO2.setCdKey(map.get("driveway_key").toString());
//                    mqttDTO2.setIp(dataMap.get("ip"));
//                    //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                    kafkaProductor.sendMessage(
//                            KafkaConstants.TOPIC.MQTT_CLIENT,
//                            PkCreat.getTablePk(),
//                            JsonUtils.toSyncJson(mqttDTO2, KafkaConstants.ACTION.PUSH_MSG_FOR_BLACK_CAR)
//                    );
//                    // 返回
//                    result.setCode(200);
//                    resultMap.put("paymentStatus", "99999");
//                    resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                    resultMap.put("recordKey", parkRecordResume.getRecordKey().toString()); // 返回停车记录id
//                    result.setMsg("该车场禁止临时车入内！");
//                    result.setData(resultMap);
//                    long queryTime = System.currentTimeMillis() - startTime;
//                    StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                    return result;
//                }
//            }
//
//            // 余位显示
//            // 查询车场显示屏设置 判断是否需要满位推送 is_remain_park_space=0 开启
//            String brethNumReport = "1";
//            // 定义车场余位数 (从redis中获取)
//            int brethNum = 0;
//            // 剩余区域车位数
//            int areaNum = 0;
////            brethNumStr =  redisService.get(RedisKeyConstants.PARK_LAST_SPACE + String.valueOf(parkDO.getParkKey()));
////            if ("".equals(brethNumStr) || brethNumStr == null){
////                redisService.set(RedisKeyConstants.PARK_LAST_SPACE + String.valueOf(parkDO.getParkKey()),String.valueOf(parkDO.getLastSpace()));
////                brethNum = parkDO.getLastSpace();
////            } else {
////                brethNum = Integer.valueOf(redisService.get(RedisKeyConstants.PARK_LAST_SPACE + String.valueOf(parkDO.getParkKey())));
////            }
//            brethNum = parkMapper.getParkBrethNum(parkDO.getParkKey());
//
//            ParkSetScreen parkSetScreen = parkSetScreenMapper.getMsgByParkKey(String.valueOf(parkDO.getParkKey()));
//            if (parkSetScreen != null) {
//                if (parkSetScreen.getIsRemainParkSpace() == 0) {
//                    brethNumReport = "0";
//                }
//            }
//
//            // 查询 区域
//            ParkAreaQuery query = new ParkAreaQuery();
//            query.setParkKey(map.get("parkid").toString());
//            query.setDrivewayKey(map.get("driveway_key").toString());
//            ParkArea parkArea = parkAreaMapper.getParkAreaByDriveway(query);
//            int areaKey = 0;
//            String ifNoPermission = "0";
//            String moreCarEnter = "0";
//            if (parkArea != null) {
//                areaKey = parkArea.getId();
//                ifNoPermission = parkArea.getIfNoPermission();
//                moreCarEnter = parkArea.getMoreCarEnter();
//                areaNum = parkArea.getDiviceOverSpace();
//            }
//            List<String> drivewayInList = Arrays.asList(parkArea.getDrivewayIn().split(","));
//            List<Map<String,Object>> drivewayList = drivewayMapper.queryDrivewayInfoByDrivewayKeyList(drivewayInList);
//            if (drivewayList.isEmpty()) {
//                result.setCode(500);
//                result.setMsg("操作失败：暂未查询到车道信息");
//                return result;
//            }
//            //  如果车辆是包月车 查询包月车相关车道 判断当前车道 是否在 包月车道中
//            if (!"".equals(associationId)) {
//                int isVehicleDriveway = entryAndExitUtil.isVehcileDriveway(map.get("parkid").toString(), associationId, map.get("driveway_key").toString());
//                if (isVehicleDriveway == 0) {
//                    // 判断 包月车无权限车道是否可进（1可进2不可进）
//                    // 判断区域信息是否为空
//                    if (areaKey == 0) {
//                        // 区域为空 从 车场包月费率设置中获取
//                        ParkSetRuleQuery parkSetRuleQuery = new ParkSetRuleQuery();
//                        parkSetRuleQuery.setParkKey(map.get("parkid").toString());
//                        parkSetRuleQuery.setType("1"); // 包月
//                        ParkSetRule parkSetRule = parkSetRuleMapper.getMsgByParkKey(parkSetRuleQuery);
//                        if (parkSetRule != null) {
//                            if ("1".equals(parkSetRule.getIfNoPermission())) {
//                                // 包月车无权限车道可进 （按临时车计费）
//                                vehicleType = "1";
//                                paymentStatus = "1";
//                                carTypeShow = "临停车";
//                                businessType = "1"; // 出场时不需要判断是否是包月
//                            } else {
//                                // 包月车无权限车道不可进
//                                // 该车道不是包月车道/ 包月信息中没有勾选车道信息
//                                result.setCode(200);
//                                result.setMsg("无权限通过该车道！");
//                                resultMap.put("paymentStatus", "1002"); // 包月车无权限通过该车道
//                                resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                                resultMap.put("recordKey", "0"); // 返回停车记录id
//                                result.setData(resultMap);
//                                System.out.println("包月车无权限车道不可进");
//                                long queryTime = System.currentTimeMillis() - startTime;
//                                StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                                return result;
//                            }
//                        } else {
//                            // 车场包月信息未设置 返回 无权限
//                            result.setCode(200);
//                            result.setMsg("无权限通过该车道！");
//                            resultMap.put("paymentStatus", "1002"); // 包月车无权限通过该车道
//                            resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                            resultMap.put("recordKey", "0"); // 返回停车记录id
//                            result.setData(resultMap);
//                            System.out.println("车场包月信息未设置 返回 无权限");
//                            long queryTime = System.currentTimeMillis() - startTime;
//                            StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                            return result;
//                        }
//                    } else {
//                        // 在区域中获取
//                        if ("1".equals(ifNoPermission)) {
//                            // 包月车无权限车道可进 （按临时车计费）
//                            vehicleType = "1";
//                            paymentStatus = "1";
//                            carTypeShow = "临停车";
//                            businessType = "1"; // 出场时不需要判断是否是包月
//                        } else {
//                            result.setCode(200);
//                            result.setMsg("无权限通过该车道！");
//                            resultMap.put("paymentStatus", "1002"); // 包月车无权限通过该车道
//                            resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                            resultMap.put("recordKey", "0"); // 返回停车记录id
//                            result.setData(resultMap);
//                            System.out.println("包月车无权限车道不可进-quyu");
//                            long queryTime = System.currentTimeMillis() - startTime;
//                            StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                            return result;
//                        }
//                    }
//                }
//            }
//
//            // 判断 一位多车 是否可以 进入车场
//            if ("1".equals(oneMoreCar)) {
//                // 判断区域信息是否为空
//                if (areaKey == 0) {
//                    // 区域为空 从 车场包月费率设置中获取
//                    ParkSetRuleQuery parkSetRuleQuery = new ParkSetRuleQuery();
//                    parkSetRuleQuery.setParkKey(map.get("parkid").toString());
//                    parkSetRuleQuery.setType("1"); // 包月
//                    ParkSetRule parkSetRule = parkSetRuleMapper.getMsgByParkKey(parkSetRuleQuery);
//                    if (parkSetRule != null) {
//                        if ("1".equals(parkSetRule.getMoreCarEnter())) {
//                            // 一位多车是否可进（1可进） （按临时车计费）
//                            vehicleType = "1";
//                            paymentStatus = "1";
//                            carTypeShow = "临停车";
//                            businessType = "1"; // 出场时不需要判断是否是包月
//                        } else {
//                            // 一位多车是否不可进
//                            result.setCode(200);
//                            result.setMsg("无权限通过该车道！");
//                            resultMap.put("paymentStatus", "1002"); // 一位多车是否不可进
//                            resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                            resultMap.put("recordKey", "0"); // 返回停车记录id
//                            result.setData(resultMap);
//                            System.out.println("一位多车是否不可进");
//                            long queryTime = System.currentTimeMillis() - startTime;
//                            StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                            return result;
//                        }
//                    } else {
//                        // 车场包月信息未设置 返回 无权限
//                        result.setCode(200);
//                        result.setMsg("无权限通过该车道！");
//                        resultMap.put("paymentStatus", "1002"); // 一位多车是否不可进
//                        resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                        resultMap.put("recordKey", "0"); // 返回停车记录id
//                        result.setData(resultMap);
//                        System.out.println("车场包月信息未设置 返回 无权限");
//                        long queryTime = System.currentTimeMillis() - startTime;
//                        StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                        return result;
//                    }
//                } else {
//                    if ("1".equals(moreCarEnter)) {
//                        // 一位多车是否可进（1可进）（按临时车计费）
//                        vehicleType = "1";
//                        paymentStatus = "1";
//                        carTypeShow = "临停车";
//                        businessType = "1"; // 出场时不需要判断是否是包月
//                    } else {
//                        result.setCode(200);
//                        result.setMsg("无权限通过该车道！");
//                        resultMap.put("paymentStatus", "1002"); // 一位多车是否不可进
//                        resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                        resultMap.put("recordKey", "0"); // 返回停车记录id
//                        result.setData(resultMap);
//                        System.out.println("无权限通过该车道-quyu");
//                        long queryTime = System.currentTimeMillis() - startTime;
//                        StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                        return result;
//                    }
//                }
//            }
//
//            // 查询数据库中是否存在逾期数据
//            boolean overFlag = false;
//            String recordId = "";
//            // 可进莫出/正常进出 修改 逾期数据的stopStatus=2 加一个异常状态值 重复驶入（1正常2重复驶入3重复驶离）
//            ParkRecordQuery overQuery = new ParkRecordQuery();
//            overQuery.setPaymentOvertime(String.valueOf(parkDO.getPaymentOvertime()));
//            overQuery.setParkKey(String.valueOf(parkDO.getParkKey()));
//            overQuery.setPlateNumber(plateNumber);
//            overQuery.setNowTime(nowDate);
//            List<ParkRecordDO> overList = parkRecordMapper.queryOverdueListByCondition_02(overQuery);
//            if (overList != null && overList.size() > 0) {
//
//                // 获取 入场时间
//                String sensorEntryTime1 = overList.get(0).getSensorEntryTime() != null ? overList.get(0).getSensorEntryTime() : overList.get(0).getArtificialEntryTime();
//                // 和当前时间 比较是否小于三分钟
//                // 当前时间 - 入场时间
//                long diff1 = nowDate.getTime() - format.parse(sensorEntryTime1).getTime();
//                // 计算分钟
//                long min1 = diff1 / (1000 * 60);
//                if (min1 < 3) {
//                    result.setCode(200);
//                    result.setMsg("车辆入库成功！");
//                    resultMap.put("paymentStatus", "0");
//                    resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                    resultMap.put("recordKey", overList.get(0).getRecordKey().toString()); // 返回停车记录id
//                    result.setData(resultMap);
//                    long queryTime = System.currentTimeMillis() - startTime;
//                    StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                    return result;
//                } else {
//                    // 判断 是否可以 进入
//                    if ("1".equals(parkDO.getOverdueAdmission())){
//                        for (ParkRecordDO entity : overList) {
//                            // 获取 入场时间
//                            String sensorEntryTime = entity.getSensorEntryTime() != null ? entity.getSensorEntryTime() : entity.getArtificialEntryTime();
//                            // 和当前时间 比较是否小于三分钟
//                            // 当前时间 - 入场时间
//                            long diff = nowDate.getTime() - format.parse(sensorEntryTime).getTime();
//                            // 计算分钟
//                            long min = diff / (1000 * 60);
//                            if (min < 3) {
//                                overFlag = true;
//                                recordId = String.valueOf(entity.getRecordKey());
//                            } else {
//                                // 修改stopStatus=2 驶离，重复入场值 设置为重复入场
//                                entity.setStopStatus("2");// 驶离
//                                entity.setIsRepeat("1");// 重复驶入
//                                parkRecordMapper.updateParkRecordMsg(entity);
//                            }
//                        }
//                        if (overFlag) {
//                            // 返回入场成功，不做操作 todo
//                            result.setCode(200);
//                            result.setMsg("车辆入库成功！");
//                            resultMap.put("paymentStatus", "0");
//                            resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                            resultMap.put("recordKey", recordId); // 返回停车记录id
//                            result.setData(resultMap);
//                            long queryTime = System.currentTimeMillis() - startTime;
//                            StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                            return result;
//                        } else {
//                            // 判断车辆是否是黑名单，是 发送消息通知
//                            boolean isBlack = entryAndExitUtil.isBlack(map.get("carnum").toString(), map.get("parkid").toString());
//                            if (isBlack) {
//                                // 黑名单 推送消息
//                                //MqqttServers.pushMsgForBlackCar(map.get("parkid").toString(), map.get("driveway_key").toString(), dataMap.get("ip"));
//                                MqttDTO mqttDTO2 = new MqttDTO();
//                                mqttDTO2.setParkKey(map.get("parkid").toString());
//                                mqttDTO2.setCdKey(map.get("driveway_key").toString());
//                                mqttDTO2.setIp(dataMap.get("ip"));
//                                //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                                kafkaProductor.sendMessage(
//                                        KafkaConstants.TOPIC.MQTT_CLIENT,
//                                        PkCreat.getTablePk(),
//                                        JsonUtils.toSyncJson(mqttDTO2, KafkaConstants.ACTION.PUSH_MSG_FOR_BLACK_CAR)
//                                );
//                                // 黑名单 禁止入场
//                                result.setCode(200);
//                                result.setMsg("黑名单车辆！");
//                                resultMap.put("paymentStatus", "99"); // 黑名单车辆  返回值
//                                resultMap.put("recordKey", "0"); // 返回停车记录id
//                                resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                                result.setData(resultMap);
//                                long queryTime = System.currentTimeMillis() - startTime;
//                                StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                                return result;
//                            }
//                            // 欠费禁止入场
//                            // 查询该车辆是否有欠费记录
//                            int arrearsCount = entryAndExitUtil.ifArrears0816(plateNumber);
//                            if (arrearsCount > 0) {
//                                ifArrears = "1";
//                                // 有欠费记录
//                                // 查询车场 欠费车辆出入场方式 （1可进莫出2禁止进入3正常进出）
//                                if ("2".equals(parkDO.getArrears())) {
//                                    // 禁止进入
//                                    // 推送消息
//                                    //MqqttServers.pushMsgForArrears(map.get("parkid").toString(), map.get("driveway_key").toString(), dataMap.get("ip"), "1");
//                                    //组装参数
//                                    MqttDTO mqttDTO3 = new MqttDTO();
//                                    mqttDTO3.setParkKey(map.get("parkid").toString());
//                                    mqttDTO3.setCdKey(map.get("driveway_key").toString());
//                                    mqttDTO3.setIp(dataMap.get("ip"));
//                                    mqttDTO3.setType("1");
//                                    //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                                    kafkaProductor.sendMessage(
//                                            KafkaConstants.TOPIC.MQTT_CLIENT,
//                                            PkCreat.getTablePk(),
//                                            JsonUtils.toSyncJson(mqttDTO3, KafkaConstants.ACTION.PUSH_MSG_TO_ARREARS)
//                                    );
//                                    // 返回
//                                    result.setCode(200);
//                                    resultMap.put("paymentStatus", "100");
//                                    resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                                    resultMap.put("recordKey", "0"); // 返回停车记录id
//                                    result.setMsg("该车辆存在欠费记录，禁止入场！");
//                                    result.setData(resultMap);
//                                    long queryTime = System.currentTimeMillis() - startTime;
//                                    StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                                    return result;
//                                }
//                            }
//                            if (parkArea != null) {
//                                // 根据 车场主键、车场区域主键 查询 该区域 目前在停车辆数量 p_park_record表中的 stop_status  当前车辆停车状态(1：在停2：驶离)
////                                int isStopCount = parkRecordMapper.getParkingSpace(parkDO.getParkKey(), parkArea.getId());
//                                // 根据 车场主键、车场区域主键 查询 该区域 临时车位总数
////                                int temporarySpaceTotal = parkArea.getTemporarySpaceTotal();
//                                //  在停车辆数量 与 该车场区域的临时车位总数比较 是否 已满
//                                if (parkArea.getDiviceOverSpace() <= 0) {
//                                    // 车位已满 判断车位已满后的入场模式 (区域表 p_park_area 中的 full_admission_mode 字段)（禁止（返回提示 禁止入场）/允许指定（需要后期确认）/允许所有（入场操作））
//                                    int fullAdmissionMode = parkArea.getFullAdmissionMode();
//                                    //1禁止所有车入场  2允许所有车入场  3允许指定车入场
//                                    if (fullAdmissionMode == 1) {
//                                        //禁止入场
//                                        if ("0".equals(brethNumReport)) {
//                                            // 推送 余位数量 消息
//                                            for (int i = 0; i < drivewayList.size(); i++) {
//                                                if (!"".equals(drivewayList.get(i).get("ip").toString())) {
//                                                    //MqqttServers.pushMsgForBrethNum(String.valueOf(parkDO.getParkKey()), drivewayList.get(i).get("driveway_key").toString(), drivewayList.get(i).get("ip").toString(), "0");
//                                                    MqttDTO mqttDTO = new MqttDTO();
//                                                    mqttDTO.setParkKey(String.valueOf(parkDO.getParkKey()));
//                                                    mqttDTO.setCdKey(drivewayList.get(i).get("driveway_key").toString());
//                                                    mqttDTO.setIp(drivewayList.get(i).get("ip").toString());
//                                                    mqttDTO.setBrethNum("0");
//                                                    //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                                                    kafkaProductor.sendMessage(
//                                                            KafkaConstants.TOPIC.MQTT_CLIENT,
//                                                            PkCreat.getTablePk(),
//                                                            JsonUtils.toSyncJson(mqttDTO, KafkaConstants.ACTION.PUSH_MSG_FOR_BRETH_NUM)
//                                                    );
//                                                }
//                                            }
//                                        }
//                                        resultMap.put("paymentStatus", "999");
//                                        resultMap.put("recordKey", "0"); // 返回停车记录id·
//                                        resultMap.put("openMode", String.valueOf(parkArea.getOpenGateMode())); // 满位后入口开闸模式
//                                        result.setData(resultMap);
//                                        result.setCode(200);
//                                        result.setMsg("该区域车位已满，禁止入场！");
//                                        long queryTime = System.currentTimeMillis() - startTime;
//                                        StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                                        return result;
//                                    }
//                                    // 2允许所有车入场  3允许指定车入场 暂时不做操作
//                                } else {
//                                    // brethNum = temporarySpaceTotal - isStopCount;
//                                    //  从redis中获取 区域的deviceOverSpace
////                                brethNumStr =  redisService.get(RedisKeyConstants.AREA_LAST_SPACE + String.valueOf(parkDO.getParkKey()) + String.valueOf(parkArea.getId()));
////                                if ("".equals(brethNumStr) || brethNumStr == null){
////                                    redisService.set(RedisKeyConstants.AREA_LAST_SPACE + String.valueOf(parkDO.getParkKey()) + String.valueOf(parkArea.getId()),String.valueOf(parkArea.getDiviceOverSpace()));
////                                    brethNum = parkArea.getDiviceOverSpace();
////                                } else {
////                                    brethNum = Integer.valueOf(redisService.get(RedisKeyConstants.AREA_LAST_SPACE + String.valueOf(parkDO.getParkKey()) + String.valueOf(parkArea.getId()) ));
////                                }
////                                brethNum = areaMapper.getOverSpace(parkArea.getId());
//                                }
//                            }
//                            // 根据车场主键，车牌号。车辆类型、当前时间 判断 是否是 特殊车场的特殊车辆
////                            int isSpecialCar = entryAndExitUtil.isSpecialCar(String.valueOf(parkDO.getParkKey()), plateNumber, nowTime, carType);
////                            if (isSpecialCar == 0) {
////                                // 特殊车场特殊车辆
////                                // 1.生成 p_park_record 信息
////                                ParkRecordDO parkRecordDO = new ParkRecordDO();
////                                parkRecordDO.setParkKey(Integer.valueOf(map.get("parkid").toString()));
////                                parkRecordDO.setParkName(parkDO.getParkName());
////                                parkRecordDO.setParkModule(parkDO.getParkModule());
////                                parkRecordDO.setAreaKey(areaKey);
////                                parkRecordDO.setPlateNumber(map.get("carnum").toString());
////                                parkRecordDO.setCarType(carType);
////                                parkRecordDO.setVehicleType("5");// 特殊车场特殊车辆
////                                parkRecordDO.setPlateStatus("1");
////                                parkRecordDO.setPlateColor(map.get("carnum_color").toString());
////                                parkRecordDO.setEnterWay("0");
////                                parkRecordDO.setPaymentStatus("5");
////                                parkRecordDO.setEntryDrivewayKey(Integer.valueOf(map.get("driveway_key").toString()));
////                                parkRecordDO.setEntryDrivewayName(map.get("driveway_name").toString());
////                                parkRecordDO.setSensorEntryTime(format.format(nowDate));
////                                parkRecordDO.setStopStatus("1");
////                                parkRecordDO.setVerifyWay("1");
////                                parkRecordDO.setVerifyStatus("2");
////                                parkRecordDO.setClearVerifyStatus("0");
////                                parkRecordDO.setClearOverStatus("0");
////                                parkRecordDO.setCreatedAt(nowDate);
////                                parkRecordDO.setUpdatedAt(nowDate);
////                                parkRecordDO.setAmountPaid(new BigDecimal(0));
////                                parkRecordDO.setAmountPayable(new BigDecimal(0));
////                                parkRecordDO.setAmountPrepay(new BigDecimal(0));
////                                parkRecordDO.setUnpaidAmount(new BigDecimal(0));
////                                parkRecordDO.setImgNumber(0);
////                                parkRecordDO.setBusinessType(businessType);
////                                parkRecordMapper.insert(parkRecordDO);
////
////
////                                //3、在p_park_record_resume表中记录入场履历信息
////                                ParkRecordResumeDO parkRecordResume = new ParkRecordResumeDO();
////                                parkRecordResume.setRecordKey(parkRecordDO.getRecordKey());
////                                //操作类型,包括：自动驶入，人工驶入，自动驶离，人工驶离等
////                                parkRecordResume.setOperationType("自动驶入");
////                                //parkRecordResume.setOperatorType("1");
////                                //操作前状态(1：未缴费 2：正常缴费 3：包月 4：免费)
////                                parkRecordResume.setBeforeStatus("未缴费");
////                                //操作后状态(1：未缴费 2：正常缴费 3：包月 4：免费)
////                                parkRecordResume.setAfterStatus("未缴费");
////                                //描述操作内容
////                                parkRecordResume.setDescription("自动驶入");
////                                parkRecordResume.setCreatedAt(nowDate);
////                                parkRecordResumeMapper.insertSelective(parkRecordResume);
////
////                                /** 发送mqtt 消息 */
////                                Map<String, String> mqttMap = new HashMap<>();
////                                mqttMap.put("inoutType", "in");
////                                mqttMap.put("ifArrears", ifArrears);
////                                mqttMap.put("plateNumber", plateNumber);
////                                mqttMap.put("parkKey", map.get("parkid").toString());
////                                mqttMap.put("parkName", parkDO.getParkName());
////                                mqttMap.put("cdKey", map.get("driveway_key").toString());
////                                mqttMap.put("drivewayName", map.get("driveway_name").toString());
////                                if (!map.get("img").toString().isEmpty()) {
////                                    mqttMap.put("imgUrl", map.get("img").toString());
////                                } else {
////                                    mqttMap.put("imgUrl", "");
////                                }
////                                mqttMap.put("inoutTime", format.format(nowDate));
////                                mqttMap.put("carType", "特殊车");
////                                //MqqttServers.pushMsgToPlatform(mqttMap);
////                                //组装参数
////                                MqttDTO mqttDTO = new MqttDTO();
////                                mqttDTO.setParam(mqttMap);
////                                //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
////                                kafkaProductor.sendMessage(
////                                        KafkaConstants.TOPIC.MQTT_CLIENT,
////                                        PkCreat.getTablePk(),
////                                        JsonUtils.toSyncJson(mqttDTO, KafkaConstants.ACTION.PUSH_MSG_TO_PLATFORM)
////                                );
////                                // 推送车位数 放到了下面的接口中
//////                                if ("0".equals(brethNumReport)) {
//////                                    // 推送消息 泊位剩余数量
//////                                    for (int i = 0; i < drivewayList.size(); i++) {
//////                                        if (!"".equals(drivewayList.get(i).get("ip").toString())) {
//////                                            MqttDTO mqttDTO3 = new MqttDTO();
//////                                            mqttDTO3.setParkKey(String.valueOf(parkDO.getParkKey()));
//////                                            mqttDTO3.setCdKey(drivewayList.get(i).get("driveway_key").toString());
//////                                            mqttDTO3.setIp(drivewayList.get(i).get("ip").toString());
//////                                            mqttDTO3.setBrethNum(areaNum > 1 ? String.valueOf(areaNum - 1) : "0");
//////                                            //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//////                                            kafkaProductor.sendMessage(
//////                                                    KafkaConstants.TOPIC.MQTT_CLIENT,
//////                                                    PkCreat.getTablePk(),
//////                                                    JsonUtils.toSyncJson(mqttDTO3, KafkaConstants.ACTION.PUSH_MSG_FOR_BRETH_NUM)
//////                                            );
//////                                        }
//////                                    }
//////                                }
////                                // 更新车场剩余总车位数 以及区域剩余车位数
////                                // updateParkSpaceNum(parkDO.getParkKey(), areaKey, brethNum - 1, areaNum - 1,plateNumber,parkDO.getIsPushTrafficPolice());
////                                result.setCode(200);
////                                resultMap.put("paymentStatus", "0");
////                                resultMap.put("openMode", "0"); // 满位后入口开闸模式
////                                resultMap.put("recordKey", String.valueOf(parkRecordDO.getRecordKey())); // 返回停车记录id
////                                result.setData(resultMap);
////                                result.setMsg("车辆入库成功！");
////                                long queryTime = System.currentTimeMillis() - startTime;
////                                StaticLog.info("***===***入场耗时：" + queryTime + "ms");
////                                return result;
////                            }
//                            // 生成新的 停车记录
//                            ParkRecordDO parkRecordDO = new ParkRecordDO();
//                            parkRecordDO.setParkKey(parkDO.getParkKey());
//                            parkRecordDO.setParkName(parkDO.getParkName());
//                            parkRecordDO.setParkModule(parkDO.getParkModule());
//                            parkRecordDO.setAreaKey(areaKey);
//                            parkRecordDO.setPlateNumber(plateNumber);
//                            parkRecordDO.setCarType(carType);
//                            parkRecordDO.setPlateStatus("1");
//                            parkRecordDO.setPlateColor(map.get("carnum_color").toString());
//                            parkRecordDO.setEnterWay("0");
//                            parkRecordDO.setPaymentStatus(paymentStatus);
//                            parkRecordDO.setVehicleType(vehicleType);
//                            parkRecordDO.setEntryDrivewayKey(Integer.valueOf(map.get("driveway_key").toString()));
//                            parkRecordDO.setEntryDrivewayName(map.get("driveway_name").toString());
//                            parkRecordDO.setSensorEntryTime(format.format(nowDate));
//                            parkRecordDO.setStopStatus("1");
//                            parkRecordDO.setVerifyWay("1");
//                            parkRecordDO.setVerifyStatus("2");
//                            parkRecordDO.setClearVerifyStatus("0");
//                            parkRecordDO.setClearOverStatus("0");
//                            parkRecordDO.setCreatedAt(nowDate);
//                            parkRecordDO.setUpdatedAt(nowDate);
//                            parkRecordDO.setAmountPaid(new BigDecimal(0));
//                            parkRecordDO.setAmountPayable(new BigDecimal(0));
//                            parkRecordDO.setAmountPrepay(new BigDecimal(0));
//                            parkRecordDO.setUnpaidAmount(new BigDecimal(0));
//                            parkRecordDO.setImgNumber(0);
//                            parkRecordDO.setBusinessType(businessType);
//                            parkRecordMapper.insert(parkRecordDO);
//
//                            //3、在p_park_record_resume表中记录入场履历信息
//                            ParkRecordResumeDO parkRecordResume = new ParkRecordResumeDO();
//                            parkRecordResume.setRecordKey(parkRecordDO.getRecordKey());
//                            //操作类型,包括：自动驶入，人工驶入，自动驶离，人工驶离等
//                            parkRecordResume.setOperationType("自动驶入");
//                            //操作前状态(1：未缴费 2：正常缴费 3：包月 4：免费)
//                            parkRecordResume.setBeforeStatus("未缴费");
//                            //操作后状态(1：未缴费 2：正常缴费 3：包月 4：免费)
//                            parkRecordResume.setAfterStatus("未缴费");
//                            //描述操作内容
//                            parkRecordResume.setDescription("自动驶入");
//                            parkRecordResume.setCreatedAt(nowDate);
//                            parkRecordResumeMapper.insertSelective(parkRecordResume);
//
//
//                            /** 发送mqtt 消息 */
//                            Map<String, String> mqttMap = new HashMap<>();
//                            mqttMap.put("inoutType", "in");
//                            mqttMap.put("ifArrears", ifArrears);
//                            mqttMap.put("plateNumber", plateNumber);
//                            mqttMap.put("parkKey", map.get("parkid").toString());
//                            mqttMap.put("parkName", parkDO.getParkName());
//                            mqttMap.put("cdKey", map.get("driveway_key").toString());
//                            mqttMap.put("drivewayName", map.get("driveway_name").toString());
//                            if (!map.get("img").toString().isEmpty()) {
//                                mqttMap.put("imgUrl", map.get("img").toString());
//                            } else {
//                                mqttMap.put("imgUrl", "");
//                            }
//                            mqttMap.put("inoutTime", format.format(nowDate));
//                            mqttMap.put("carType", carTypeShow);
//                            //MqqttServers.pushMsgToPlatform(mqttMap);
//                            //组装参数
//                            MqttDTO mqttDTO = new MqttDTO();
//                            mqttDTO.setParam(mqttMap);
//                            //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                            kafkaProductor.sendMessage(
//                                    KafkaConstants.TOPIC.MQTT_CLIENT,
//                                    PkCreat.getTablePk(),
//                                    JsonUtils.toSyncJson(mqttDTO, KafkaConstants.ACTION.PUSH_MSG_TO_PLATFORM)
//                            );
//                            // 推送车位数 放到了下面的接口中
////                            if ("0".equals(brethNumReport)) {
////                                // 推送泊位剩余数量
////                                for (int i = 0; i < drivewayList.size(); i++) {
////                                    if (!"".equals(drivewayList.get(i).get("ip").toString())) {
////                                        MqttDTO mqttDTO3 = new MqttDTO();
////                                        mqttDTO3.setParkKey(String.valueOf(parkDO.getParkKey()));
////                                        mqttDTO3.setCdKey(drivewayList.get(i).get("driveway_key").toString());
////                                        mqttDTO3.setIp(drivewayList.get(i).get("ip").toString());
////                                        mqttDTO3.setBrethNum(areaNum > 1 ? String.valueOf(areaNum - 1) : "0");
////                                        //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
////                                        kafkaProductor.sendMessage(
////                                                KafkaConstants.TOPIC.MQTT_CLIENT,
////                                                PkCreat.getTablePk(),
////                                                JsonUtils.toSyncJson(mqttDTO3, KafkaConstants.ACTION.PUSH_MSG_FOR_BRETH_NUM)
////                                        );
////                                    }
////                                }
////                            }
//                            // 更新车场剩余总车位数 以及区域剩余车位数
//                            // updateParkSpaceNum(parkDO.getParkKey(), areaKey, brethNum - 1, areaNum - 1,plateNumber,parkDO.getIsPushTrafficPolice());
//                            result.setCode(200);
//                            resultMap.put("paymentStatus", "0");
//                            resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                            resultMap.put("recordKey", String.valueOf(parkRecordDO.getRecordKey())); // 返回停车记录id
//                            result.setData(resultMap);
//                            result.setMsg("车辆入库成功！");
//                            long queryTime = System.currentTimeMillis() - startTime;
//                            StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                            return result;
//
//                        }
//                    }
//                    else if (("1".equals(parkDO.getOverdueAdmissionMonth()) || StringUtils.isEmpty(parkDO.getOverdueAdmissionMonth())) && "包月车".equals(carTypeShow)){
//                        // 有效的包月车,并且有逾期记录,将逾期记录在停状态更新为驶离,走一遍入场逻辑
//                        // !!! 消除逾期记录时并未修改剩余泊位数,入场也没有更新泊位
//                        overList.forEach(e -> {
//                            e.setStopStatus("2");
//                            e.setPaymentStatus("3");
//                            e.setUnpaidAmount(new BigDecimal("0"));
//                            e.setArtificialExitTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                            parkRecordMapper.updateByPrimaryKeySelective(e);
//                        });
//                        for (ParkRecordDO entity : overList) {
//                            // 获取 入场时间
//                            String sensorEntryTime = entity.getSensorEntryTime() != null ? entity.getSensorEntryTime() : entity.getArtificialEntryTime();
//                            // 和当前时间 比较是否小于三分钟
//                            // 当前时间 - 入场时间
//                            long diff = nowDate.getTime() - format.parse(sensorEntryTime).getTime();
//                            // 计算分钟
//                            long min = diff / (1000 * 60);
//                            if (min < 3) {
//                                overFlag = true;
//                                recordId = String.valueOf(entity.getRecordKey());
//                            } else {
//                                // 修改stopStatus=2 驶离，重复入场值 设置为重复入场
//                                entity.setStopStatus("2");// 驶离
//                                entity.setIsRepeat("1");// 重复驶入
//                                parkRecordMapper.updateParkRecordMsg(entity);
//                            }
//                        }
//                        if (overFlag) {
//                            // 返回入场成功，不做操作 todo
//                            result.setCode(200);
//                            result.setMsg("车辆入库成功！");
//                            resultMap.put("paymentStatus", "0");
//                            resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                            resultMap.put("recordKey", recordId); // 返回停车记录id
//                            result.setData(resultMap);
//                            long queryTime = System.currentTimeMillis() - startTime;
//                            StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                            return result;
//                        } else {
//                            // 判断车辆是否是黑名单，是 发送消息通知
//                            boolean isBlack = entryAndExitUtil.isBlack(map.get("carnum").toString(), map.get("parkid").toString());
//                            if (isBlack) {
//                                // 黑名单 推送消息
//                                MqttDTO mqttDTO2 = new MqttDTO();
//                                mqttDTO2.setParkKey(map.get("parkid").toString());
//                                mqttDTO2.setCdKey(map.get("driveway_key").toString());
//                                mqttDTO2.setIp(dataMap.get("ip"));
//                                //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                                kafkaProductor.sendMessage(
//                                        KafkaConstants.TOPIC.MQTT_CLIENT,
//                                        PkCreat.getTablePk(),
//                                        JsonUtils.toSyncJson(mqttDTO2, KafkaConstants.ACTION.PUSH_MSG_FOR_BLACK_CAR)
//                                );
//                                // 黑名单 禁止入场
//                                result.setCode(200);
//                                result.setMsg("黑名单车辆！");
//                                resultMap.put("paymentStatus", "99"); // 黑名单车辆  返回值
//                                resultMap.put("recordKey", "0"); // 返回停车记录id
//                                resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                                result.setData(resultMap);
//                                long queryTime = System.currentTimeMillis() - startTime;
//                                StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                                return result;
//                            }
//                            // 欠费禁止入场
//                            // 查询该车辆是否有欠费记录
//                            int arrearsCount = entryAndExitUtil.ifArrears0816(plateNumber);
//                            if (arrearsCount > 0) {
//                                ifArrears = "1";
//                                // 有欠费记录
//                                // 查询车场 欠费车辆出入场方式 （1可进莫出2禁止进入3正常进出）
//                                if ("2".equals(parkDO.getArrears())) {
//                                    // 禁止进入
//                                    // 推送消息
//                                    MqttDTO mqttDTO2 = new MqttDTO();
//                                    mqttDTO2.setParkKey(map.get("parkid").toString());
//                                    mqttDTO2.setCdKey(map.get("driveway_key").toString());
//                                    mqttDTO2.setIp(dataMap.get("ip"));
//                                    mqttDTO2.setType("1");
//                                    //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                                    kafkaProductor.sendMessage(
//                                            KafkaConstants.TOPIC.MQTT_CLIENT,
//                                            PkCreat.getTablePk(),
//                                            JsonUtils.toSyncJson(mqttDTO2, KafkaConstants.ACTION.PUSH_MSG_TO_ARREARS)
//                                    );
//                                    // 返回
//                                    result.setCode(200);
//                                    resultMap.put("paymentStatus", "100");
//                                    resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                                    resultMap.put("recordKey", "0"); // 返回停车记录id
//                                    result.setMsg("该车辆存在欠费记录，禁止入场！");
//                                    result.setData(resultMap);
//                                    long queryTime = System.currentTimeMillis() - startTime;
//                                    StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                                    return result;
//                                }
//                            }
//                            if (parkArea != null) {
//                                // 根据 车场主键、车场区域主键 查询 该区域 目前在停车辆数量 p_park_record表中的 stop_status  当前车辆停车状态(1：在停2：驶离)
////                                int isStopCount = parkRecordMapper.getParkingSpace(parkDO.getParkKey(), parkArea.getId());
//                                // 根据 车场主键、车场区域主键 查询 该区域 临时车位总数
////                                int temporarySpaceTotal = parkArea.getTemporarySpaceTotal();
//                                //  在停车辆数量 与 该车场区域的临时车位总数比较 是否 已满
//                                if (parkArea.getDiviceOverSpace() <= 0) {
//                                    // 车位已满 判断车位已满后的入场模式 (区域表 p_park_area 中的 full_admission_mode 字段)（禁止（返回提示 禁止入场）/允许指定（需要后期确认）/允许所有（入场操作））
//                                    int fullAdmissionMode = parkArea.getFullAdmissionMode();
//                                    //1禁止所有车入场  2允许所有车入场  3允许指定车入场
//                                    if (fullAdmissionMode == 1) {
//                                        //禁止入场
//                                        if ("0".equals(brethNumReport)) {
//                                            // 推送 余位数量 消息
//                                            for (int i = 0; i < drivewayList.size(); i++) {
//                                                if (!"".equals(drivewayList.get(i).get("ip").toString())) {
//                                                    MqttDTO mqttDTO2 = new MqttDTO();
//                                                    mqttDTO2.setParkKey(String.valueOf(parkDO.getParkKey()));
//                                                    mqttDTO2.setCdKey(drivewayList.get(i).get("driveway_key").toString());
//                                                    mqttDTO2.setIp(drivewayList.get(i).get("ip").toString());
//                                                    mqttDTO2.setBrethNum("0");
//                                                    //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                                                    kafkaProductor.sendMessage(
//                                                            KafkaConstants.TOPIC.MQTT_CLIENT,
//                                                            PkCreat.getTablePk(),
//                                                            JsonUtils.toSyncJson(mqttDTO2, KafkaConstants.ACTION.PUSH_MSG_FOR_BRETH_NUM)
//                                                    );
//                                                }
//                                            }
//                                        }
//                                        resultMap.put("paymentStatus", "999");
//                                        resultMap.put("recordKey", "0"); // 返回停车记录id·
//                                        resultMap.put("openMode", String.valueOf(parkArea.getOpenGateMode())); // 满位后入口开闸模式
//                                        result.setData(resultMap);
//                                        result.setCode(200);
//                                        result.setMsg("该区域车位已满，禁止入场！");
//                                        long queryTime = System.currentTimeMillis() - startTime;
//                                        StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                                        return result;
//                                    }
//                                }
//                            }
//                            // 生成新的 停车记录
//                            ParkRecordDO parkRecordDO = new ParkRecordDO();
//                            parkRecordDO.setParkKey(parkDO.getParkKey());
//                            parkRecordDO.setParkName(parkDO.getParkName());
//                            parkRecordDO.setParkModule(parkDO.getParkModule());
//                            parkRecordDO.setAreaKey(areaKey);
//                            parkRecordDO.setPlateNumber(plateNumber);
//                            parkRecordDO.setCarType(carType);
//                            parkRecordDO.setPlateStatus("1");
//                            parkRecordDO.setPlateColor(map.get("carnum_color").toString());
//                            parkRecordDO.setEnterWay("0");
//                            parkRecordDO.setPaymentStatus(paymentStatus);
//                            parkRecordDO.setVehicleType(vehicleType);
//                            parkRecordDO.setEntryDrivewayKey(Integer.valueOf(map.get("driveway_key").toString()));
//                            parkRecordDO.setEntryDrivewayName(map.get("driveway_name").toString());
//                            parkRecordDO.setSensorEntryTime(format.format(nowDate));
//                            parkRecordDO.setStopStatus("1");
//                            parkRecordDO.setVerifyWay("1");
//                            parkRecordDO.setVerifyStatus("2");
//                            parkRecordDO.setClearVerifyStatus("0");
//                            parkRecordDO.setClearOverStatus("0");
//                            parkRecordDO.setCreatedAt(nowDate);
//                            parkRecordDO.setUpdatedAt(nowDate);
//                            parkRecordDO.setAmountPaid(new BigDecimal(0));
//                            parkRecordDO.setAmountPayable(new BigDecimal(0));
//                            parkRecordDO.setAmountPrepay(new BigDecimal(0));
//                            parkRecordDO.setUnpaidAmount(new BigDecimal(0));
//                            parkRecordDO.setImgNumber(0);
//                            parkRecordDO.setBusinessType(businessType);
//                            parkRecordMapper.insert(parkRecordDO);
//
//                            //3、在p_park_record_resume表中记录入场履历信息
//                            ParkRecordResumeDO parkRecordResume = new ParkRecordResumeDO();
//                            parkRecordResume.setRecordKey(parkRecordDO.getRecordKey());
//                            //操作类型,包括：自动驶入，人工驶入，自动驶离，人工驶离等
//                            parkRecordResume.setOperationType("自动驶入");
//                            //操作前状态(1：未缴费 2：正常缴费 3：包月 4：免费)
//                            parkRecordResume.setBeforeStatus("未缴费");
//                            //操作后状态(1：未缴费 2：正常缴费 3：包月 4：免费)
//                            parkRecordResume.setAfterStatus("未缴费");
//                            //描述操作内容
//                            parkRecordResume.setDescription("自动驶入");
//                            parkRecordResume.setCreatedAt(nowDate);
//                            parkRecordResumeMapper.insertSelective(parkRecordResume);
//                            // 更新剩余车位数
//                            // updateParkSpaceNum(parkDO.getParkKey(),areaKey,brethNum - 1,areaNum - 1,plateNumber,parkDO.getIsPushTrafficPolice());
//
//                            /** 发送mqtt 消息 */
//                            Map<String, String> mqttMap = new HashMap<>();
//                            mqttMap.put("inoutType", "in");
//                            mqttMap.put("ifArrears", ifArrears);
//                            mqttMap.put("plateNumber", plateNumber);
//                            mqttMap.put("parkKey", map.get("parkid").toString());
//                            mqttMap.put("parkName", parkDO.getParkName());
//                            mqttMap.put("cdKey", map.get("driveway_key").toString());
//                            mqttMap.put("drivewayName", map.get("driveway_name").toString());
//                            if (!map.get("img").toString().isEmpty()) {
//                                mqttMap.put("imgUrl", map.get("img").toString());
//                            } else {
//                                mqttMap.put("imgUrl", "");
//                            }
//                            mqttMap.put("inoutTime", format.format(nowDate));
//                            mqttMap.put("carType", carTypeShow);
////                        MqqttServers.pushMsgToPlatform(mqttMap);
//                            MqttDTO mqttDTO = new MqttDTO();
//                            mqttDTO.setParam(mqttMap);
//                            //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                            kafkaProductor.sendMessage(
//                                    KafkaConstants.TOPIC.MQTT_CLIENT,
//                                    PkCreat.getTablePk(),
//                                    JsonUtils.toSyncJson(mqttDTO, KafkaConstants.ACTION.PUSH_MSG_TO_PLATFORM)
//                            );
//                            // 推送车位数 放到了下面的接口中
////                            if ("0".equals(brethNumReport)) {
////                                // 推送泊位剩余数量
////                                for (int i = 0; i < drivewayList.size(); i++) {
////                                    if (!"".equals(drivewayList.get(i).get("ip").toString())) {
////                                        MqttDTO mqttDTO2 = new MqttDTO();
////                                        mqttDTO2.setParkKey(String.valueOf(parkDO.getParkKey()));
////                                        mqttDTO2.setCdKey(drivewayList.get(i).get("driveway_key").toString());
////                                        mqttDTO2.setIp(drivewayList.get(i).get("ip").toString());
////                                        mqttDTO2.setBrethNum(areaNum > 1 ? String.valueOf((areaNum -1)) : "0");
////                                        //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
////                                        kafkaProductor.sendMessage(
////                                                KafkaConstants.TOPIC.MQTT_CLIENT,
////                                                PkCreat.getTablePk(),
////                                                JsonUtils.toSyncJson(mqttDTO2, KafkaConstants.ACTION.PUSH_MSG_FOR_BRETH_NUM)
////                                        );
////                                    }
////                                }
////                            }
//                            result.setCode(200);
//                            resultMap.put("paymentStatus", "0");
//                            resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                            resultMap.put("recordKey", String.valueOf(parkRecordDO.getRecordKey())); // 返回停车记录id
//                            result.setData(resultMap);
//                            result.setMsg("车辆入库成功！");
//                            long queryTime = System.currentTimeMillis() - startTime;
//                            StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                            return result;
//                        }
//                    }
//                    else {
//                        // 禁止入场 联系客服
//                        result.setCode(200);
//                        resultMap.put("paymentStatus", "1001");// 重复入场
//                        resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                        resultMap.put("recordKey", "0"); // 返回停车记录id
//                        result.setData(resultMap);
//                        result.setMsg("联系客服人员！");
//                        return result;
//                    }
//                }
//
//
//            }
//            else {
//                // 无逾期记录 新增 入场
//                boolean isBlack = entryAndExitUtil.isBlack(map.get("carnum").toString(), map.get("parkid").toString());
//                if (isBlack) {
//                    // 黑名单 推送消息
//                    //MqqttServers.pushMsgForBlackCar(map.get("parkid").toString(), map.get("driveway_key").toString(), dataMap.get("ip"));
//                    MqttDTO mqttDTO2 = new MqttDTO();
//                    mqttDTO2.setParkKey(map.get("parkid").toString());
//                    mqttDTO2.setCdKey(map.get("driveway_key").toString());
//                    mqttDTO2.setIp(dataMap.get("ip"));
//                    //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                    kafkaProductor.sendMessage(
//                            KafkaConstants.TOPIC.MQTT_CLIENT,
//                            PkCreat.getTablePk(),
//                            JsonUtils.toSyncJson(mqttDTO2, KafkaConstants.ACTION.PUSH_MSG_FOR_BLACK_CAR)
//                    );
//                    // 黑名单 禁止入场
//                    result.setCode(200);
//                    result.setMsg("黑名单车辆！");
//                    resultMap.put("paymentStatus", "99"); // 黑名单车辆  返回值
//                    resultMap.put("recordKey", "0"); // 返回停车记录id
//                    resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                    result.setData(resultMap);
//                    long queryTime = System.currentTimeMillis() - startTime;
//                    StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                    return result;
//                }
//
//                // 欠费禁止入场
//                // 查询该车辆是否有欠费记录
//                int arrearsCount = entryAndExitUtil.ifArrears0816(plateNumber);
//                if (arrearsCount > 0) {
//                    ifArrears = "1";
//                    // 有欠费记录
//                    // 查询车场 欠费车辆出入场方式 （1可进莫出2禁止进入3正常进出）
//                    if ("2".equals(parkDO.getArrears())) {
//                        // 禁止进入
//                        // 推送消息
//                        //MqqttServers.pushMsgForArrears(map.get("parkid").toString(), map.get("driveway_key").toString(), dataMap.get("ip"), "1");
//                        MqttDTO mqttDTO4 = new MqttDTO();
//                        mqttDTO4.setParkKey(map.get("parkid").toString());
//                        mqttDTO4.setCdKey(map.get("driveway_key").toString());
//                        mqttDTO4.setIp(dataMap.get("ip"));
//                        mqttDTO4.setType("1");
//                        //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                        kafkaProductor.sendMessage(
//                                KafkaConstants.TOPIC.MQTT_CLIENT,
//                                PkCreat.getTablePk(),
//                                JsonUtils.toSyncJson(mqttDTO4, KafkaConstants.ACTION.PUSH_MSG_TO_ARREARS)
//                        );
//                        // 返回
//                        result.setCode(200);
//                        resultMap.put("paymentStatus", "100");
//                        resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                        resultMap.put("recordKey", "0"); // 返回停车记录id
//                        result.setMsg("该车辆存在欠费记录，禁止入场！");
//                        result.setData(resultMap);
//                        long queryTime = System.currentTimeMillis() - startTime;
//                        StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                        return result;
//                    }
//                }
//
//                if (parkArea != null) {
//                    // 从 redis 中获取 剩余泊位数 区域的deviceOverSpace
////                    brethNumStr =  redisService.get(RedisKeyConstants.AREA_LAST_SPACE + String.valueOf(parkDO.getParkKey()) + String.valueOf(parkArea.getId()));
////                    if ("".equals(brethNumStr) || brethNumStr == null){
////                        redisService.set(RedisKeyConstants.AREA_LAST_SPACE + String.valueOf(parkDO.getParkKey()) + String.valueOf(parkArea.getId()),String.valueOf(parkArea.getDiviceOverSpace()));
////                        brethNum = parkArea.getDiviceOverSpace();
////                    } else {
////                        brethNum = Integer.valueOf(redisService.get(RedisKeyConstants.AREA_LAST_SPACE + String.valueOf(parkDO.getParkKey()) + String.valueOf(parkArea.getId()) ));
////                    }
////                    brethNum = areaMapper.getOverSpace(parkArea.getId());
//                    // 当 areaNum = 0 时 判断车位已满后的入场模式 (区域表 p_park_area 中的 full_admission_mode 字段)（禁止（返回提示 禁止入场）/允许指定（需要后期确认）/允许所有（入场操作））
//                    if (areaNum <= 0) {
//                        int fullAdmissionMode = parkArea.getFullAdmissionMode();
//                        //1禁止所有车入场  2允许所有车入场  3允许指定车入场
//                        if (fullAdmissionMode == 1) {
//                            //禁止入场
//                            // 推送 余位数量 消息
//                            for (int i = 0; i < drivewayList.size(); i++) {
//                                if (!"".equals(drivewayList.get(i).get("ip").toString())) {
//                                    //MqqttServers.pushMsgForBrethNum(String.valueOf(parkDO.getParkKey()), drivewayList.get(i).get("driveway_key").toString(), drivewayList.get(i).get("ip").toString(), "0");
//                                    //组装参数
//                                    MqttDTO mqttDTO3 = new MqttDTO();
//                                    mqttDTO3.setParkKey(String.valueOf(parkDO.getParkKey()));
//                                    mqttDTO3.setCdKey(drivewayList.get(i).get("driveway_key").toString());
//                                    mqttDTO3.setIp(drivewayList.get(i).get("ip").toString());
//                                    mqttDTO3.setBrethNum("0");
//                                    //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                                    kafkaProductor.sendMessage(
//                                            KafkaConstants.TOPIC.MQTT_CLIENT,
//                                            PkCreat.getTablePk(),
//                                            JsonUtils.toSyncJson(mqttDTO3, KafkaConstants.ACTION.PUSH_MSG_FOR_BRETH_NUM)
//                                    );
//                                }
//                            }
//                            resultMap.put("paymentStatus", "999");
//                            resultMap.put("recordKey", "0"); // 返回停车记录id·
//                            resultMap.put("openMode", String.valueOf(parkArea.getOpenGateMode())); // 满位后入口开闸模式
//                            result.setData(resultMap);
//                            result.setCode(200);
//                            result.setMsg("该区域车位已满，禁止入场！");
//                            long queryTime = System.currentTimeMillis() - startTime;
//                            StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                            return result;
//                        }
//                    }
//                }
//                // 根据车场主键，车牌号。车辆类型、当前时间 判断 是否是 特殊车场的特殊车辆
////                int isSpecialCar = entryAndExitUtil.isSpecialCar(String.valueOf(parkDO.getParkKey()), plateNumber, nowTime, carType);
////                if (isSpecialCar == 0) {
////                    // 特殊车场特殊车辆
////                    // 1.生成 p_park_record 信息
////                    ParkRecordDO parkRecordDO = new ParkRecordDO();
////                    parkRecordDO.setParkKey(Integer.valueOf(map.get("parkid").toString()));
////                    parkRecordDO.setParkName(parkDO.getParkName());
////                    parkRecordDO.setParkModule(parkDO.getParkModule());
////                    parkRecordDO.setAreaKey(areaKey);
////                    parkRecordDO.setPlateNumber(map.get("carnum").toString());
////                    parkRecordDO.setCarType(carType);
////                    parkRecordDO.setVehicleType("5");// 特殊车场特殊车辆
////                    parkRecordDO.setPlateStatus("1");
////                    parkRecordDO.setPlateColor(map.get("carnum_color").toString());
////                    parkRecordDO.setEnterWay("0");
////                    parkRecordDO.setPaymentStatus("5");
////                    parkRecordDO.setEntryDrivewayKey(Integer.valueOf(map.get("driveway_key").toString()));
////                    parkRecordDO.setEntryDrivewayName(map.get("driveway_name").toString());
////                    parkRecordDO.setSensorEntryTime(format.format(nowDate));
////                    parkRecordDO.setStopStatus("1");
////                    parkRecordDO.setVerifyWay("1");
////                    parkRecordDO.setVerifyStatus("2");
////                    parkRecordDO.setClearVerifyStatus("0");
////                    parkRecordDO.setClearOverStatus("0");
////                    parkRecordDO.setCreatedAt(nowDate);
////                    parkRecordDO.setUpdatedAt(nowDate);
////                    parkRecordDO.setAmountPaid(new BigDecimal(0));
////                    parkRecordDO.setAmountPayable(new BigDecimal(0));
////                    parkRecordDO.setAmountPrepay(new BigDecimal(0));
////                    parkRecordDO.setUnpaidAmount(new BigDecimal(0));
////                    parkRecordDO.setImgNumber(0);
////                    parkRecordDO.setBusinessType(businessType);
////                    parkRecordMapper.insert(parkRecordDO);
////
////                    //3、在p_park_record_resume表中记录入场履历信息
////                    ParkRecordResumeDO parkRecordResume = new ParkRecordResumeDO();
////                    parkRecordResume.setRecordKey(parkRecordDO.getRecordKey());
////                    //操作类型,包括：自动驶入，人工驶入，自动驶离，人工驶离等
////                    parkRecordResume.setOperationType("自动驶入");
////                    //parkRecordResume.setOperatorType("1");
////                    //操作前状态(1：未缴费 2：正常缴费 3：包月 4：免费)
////                    parkRecordResume.setBeforeStatus("未缴费");
////                    //操作后状态(1：未缴费 2：正常缴费 3：包月 4：免费)
////                    parkRecordResume.setAfterStatus("未缴费");
////                    //描述操作内容
////                    parkRecordResume.setDescription("自动驶入");
////                    parkRecordResume.setCreatedAt(nowDate);
////                    parkRecordResumeMapper.insertSelective(parkRecordResume);
////
////                    /** 发送mqtt 消息 */
////                    Map<String, String> mqttMap = new HashMap<>();
////                    mqttMap.put("inoutType", "in");
////                    mqttMap.put("ifArrears", ifArrears);
////                    mqttMap.put("plateNumber", plateNumber);
////                    mqttMap.put("parkKey", map.get("parkid").toString());
////                    mqttMap.put("parkName", parkDO.getParkName());
////                    mqttMap.put("cdKey", map.get("driveway_key").toString());
////                    mqttMap.put("drivewayName", map.get("driveway_name").toString());
////                    if (!map.get("img").toString().isEmpty()) {
////                        mqttMap.put("imgUrl", map.get("img").toString());
////                    } else {
////                        mqttMap.put("imgUrl", "");
////                    }
////                    mqttMap.put("inoutTime", format.format(nowDate));
////                    mqttMap.put("carType", carTypeShow);
////                    //MqqttServers.pushMsgToPlatform(mqttMap);
////                    //组装参数
////                    MqttDTO mqttDTO = new MqttDTO();
////                    mqttDTO.setParam(mqttMap);
////                    //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
////                    kafkaProductor.sendMessage(
////                            KafkaConstants.TOPIC.MQTT_CLIENT,
////                            PkCreat.getTablePk(),
////                            JsonUtils.toSyncJson(mqttDTO, KafkaConstants.ACTION.PUSH_MSG_TO_PLATFORM)
////                    );
////                    // 推送车位数 放到了下面的接口中
//////                    if ("0".equals(brethNumReport)) {
//////                        // 推送消息 泊位剩余数量
//////                        for (int i = 0; i < drivewayList.size(); i++) {
//////                            if (!"".equals(drivewayList.get(i).get("ip").toString())) {
//////                                MqttDTO mqttDTO3 = new MqttDTO();
//////                                mqttDTO3.setParkKey(String.valueOf(parkDO.getParkKey()));
//////                                mqttDTO3.setCdKey(drivewayList.get(i).get("driveway_key").toString());
//////                                mqttDTO3.setIp(drivewayList.get(i).get("ip").toString());
//////                                mqttDTO3.setBrethNum(areaNum > 1 ? String.valueOf(areaNum - 1) : "0");
//////                                //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//////                                kafkaProductor.sendMessage(
//////                                        KafkaConstants.TOPIC.MQTT_CLIENT,
//////                                        PkCreat.getTablePk(),
//////                                        JsonUtils.toSyncJson(mqttDTO3, KafkaConstants.ACTION.PUSH_MSG_FOR_BRETH_NUM)
//////                                );
//////                            }
//////                        }
//////                    }
////                    // 更新车场剩余总车位数 以及区域剩余车位数
////                    // updateParkSpaceNum(parkDO.getParkKey(), areaKey, brethNum - 1, areaNum - 1,plateNumber,parkDO.getIsPushTrafficPolice());
////                    result.setCode(200);
////                    resultMap.put("paymentStatus", "0");
////                    resultMap.put("recordKey", String.valueOf(parkRecordDO.getRecordKey())); // 返回停车记录id
////                    result.setData(resultMap);
////                    result.setMsg("车辆入库成功！");
////                    long queryTime = System.currentTimeMillis() - startTime;
////                    StaticLog.info("***===***入场耗时：" + queryTime + "ms");
////                    return result;
////                }
//
//                // 生成新的 停车记录
//                ParkRecordDO parkRecordDO = new ParkRecordDO();
//                parkRecordDO.setParkKey(parkDO.getParkKey());
//                parkRecordDO.setParkName(parkDO.getParkName());
//                parkRecordDO.setParkModule(parkDO.getParkModule());
//                parkRecordDO.setAreaKey(areaKey);
//                parkRecordDO.setPlateNumber(plateNumber);
//                parkRecordDO.setCarType(carType);
//                parkRecordDO.setPlateStatus("1");
//                parkRecordDO.setPlateColor(map.get("carnum_color").toString());
//                parkRecordDO.setEnterWay("0");
//                parkRecordDO.setPaymentStatus(paymentStatus);
//                parkRecordDO.setVehicleType(vehicleType);
//                parkRecordDO.setEntryDrivewayKey(Integer.valueOf(map.get("driveway_key").toString()));
//                parkRecordDO.setEntryDrivewayName(map.get("driveway_name").toString());
//                parkRecordDO.setSensorEntryTime(format.format(nowDate));
//                parkRecordDO.setStopStatus("1");
//                parkRecordDO.setVerifyWay("1");
//                parkRecordDO.setVerifyStatus("2");
//                parkRecordDO.setClearVerifyStatus("0");
//                parkRecordDO.setClearOverStatus("0");
//                parkRecordDO.setCreatedAt(nowDate);
//                parkRecordDO.setUpdatedAt(nowDate);
//                parkRecordDO.setAmountPaid(new BigDecimal(0));
//                parkRecordDO.setAmountPayable(new BigDecimal(0));
//                parkRecordDO.setAmountPrepay(new BigDecimal(0));
//                parkRecordDO.setUnpaidAmount(new BigDecimal(0));
//                parkRecordDO.setImgNumber(0);
//                parkRecordDO.setBusinessType(businessType);
//                parkRecordMapper.insert(parkRecordDO);
//
//                //3、在p_park_record_resume表中记录入场履历信息
//                ParkRecordResumeDO parkRecordResume = new ParkRecordResumeDO();
//                parkRecordResume.setRecordKey(parkRecordDO.getRecordKey());
//                //操作类型,包括：自动驶入，人工驶入，自动驶离，人工驶离等
//                parkRecordResume.setOperationType("自动驶入");
//                //操作前状态(1：未缴费 2：正常缴费 3：包月 4：免费)
//                parkRecordResume.setBeforeStatus("未缴费");
//                //操作后状态(1：未缴费 2：正常缴费 3：包月 4：免费)
//                parkRecordResume.setAfterStatus("未缴费");
//                //描述操作内容
//                parkRecordResume.setDescription("自动驶入");
//                parkRecordResume.setCreatedAt(nowDate);
//                parkRecordResumeMapper.insertSelective(parkRecordResume);
//
//
//                /** 发送mqtt 消息 */
//                Map<String, String> mqttMap = new HashMap<>();
//                mqttMap.put("inoutType", "in");
//                mqttMap.put("ifArrears", ifArrears);
//                mqttMap.put("plateNumber", plateNumber);
//                mqttMap.put("parkKey", map.get("parkid").toString());
//                mqttMap.put("parkName", parkDO.getParkName());
//                mqttMap.put("cdKey", map.get("driveway_key").toString());
//                mqttMap.put("drivewayName", map.get("driveway_name").toString());
//                if (!map.get("img").toString().isEmpty()) {
//                    mqttMap.put("imgUrl", map.get("img").toString());
//                } else {
//                    mqttMap.put("imgUrl", "");
//                }
//                mqttMap.put("inoutTime", format.format(nowDate));
//                mqttMap.put("carType", carTypeShow);
//                //MqqttServers.pushMsgToPlatform(mqttMap);
//                //组装参数
//                MqttDTO mqttDTO = new MqttDTO();
//                mqttDTO.setParam(mqttMap);
//                //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
//                kafkaProductor.sendMessage(
//                        KafkaConstants.TOPIC.MQTT_CLIENT,
//                        PkCreat.getTablePk(),
//                        JsonUtils.toSyncJson(mqttDTO, KafkaConstants.ACTION.PUSH_MSG_TO_PLATFORM)
//                );
//                // 推送车位数 放到了下面的接口中
////                if ("0".equals(brethNumReport)) {
////                    // 推送泊位剩余数量
////                    for (int i = 0; i < drivewayList.size(); i++) {
////                        if (!"".equals(drivewayList.get(i).get("ip").toString())) {
////                            MqttDTO mqttDTO3 = new MqttDTO();
////                            mqttDTO3.setParkKey(String.valueOf(parkDO.getParkKey()));
////                            mqttDTO3.setCdKey(drivewayList.get(i).get("driveway_key").toString());
////                            mqttDTO3.setIp(drivewayList.get(i).get("ip").toString());
////                            mqttDTO3.setBrethNum(areaNum > 1 ? String.valueOf(areaNum - 1) : "0");
////                            //     ================  发送Mqtt->kafka消息 2022-01-10 编辑  =======================
////                            kafkaProductor.sendMessage(
////                                    KafkaConstants.TOPIC.MQTT_CLIENT,
////                                    PkCreat.getTablePk(),
////                                    JsonUtils.toSyncJson(mqttDTO3, KafkaConstants.ACTION.PUSH_MSG_FOR_BRETH_NUM)
////                            );
////                        }
////                    }
////                }
//                // 更新车场剩余总车位数 以及区域剩余车位数
//                // updateParkSpaceNum(parkDO.getParkKey(), areaKey, brethNum - 1, areaNum - 1,plateNumber,parkDO.getIsPushTrafficPolice());
//                result.setCode(200);
//                resultMap.put("paymentStatus", "0");
//                resultMap.put("openMode", "0"); // 满位后入口开闸模式
//                resultMap.put("recordKey", String.valueOf(parkRecordDO.getRecordKey())); // 返回停车记录id
//                result.setData(resultMap);
//                result.setMsg("车辆入库成功！");
//                long queryTime = System.currentTimeMillis() - startTime;
//                StaticLog.info("***===***入场耗时：" + queryTime + "ms");
//                return result;
//            }
        } catch (Exception e) {
            Result result = new Result();
            result.setCode(500);
            result.setMsg("入场失败，请稍后再试！");
            return result;
        }
        return null;
    }
}
