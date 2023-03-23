package com.example.iparking.service;

import com.example.iparking.pojo.ParkRecordQuery;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

/**
* 相关Service
* @author MyName
* @version 1.0
* @date 2023-01-18
 * Copyright © MyCompany
*/
public interface RecordService {
    Map<String, Object> parkingAmount(String startTime, String endTime, int type) throws ParseException;

    BigDecimal countAmount(int id, int type) throws ParseException;

    BigDecimal countAmountX(ParkRecordQuery parkRecordQuery) throws ParseException;
}