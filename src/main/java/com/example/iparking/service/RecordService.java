package com.example.iparking.service;

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
    public Map<String, Object> parkingAmount(String startTime, String endTime) throws ParseException;

    public BigDecimal countAmount() throws ParseException;
}