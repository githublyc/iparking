package com.example.iparking.dao;

import com.example.iparking.pojo.Record;

import java.util.List;

/**
* Mapper
* @author MyName
* @version 1.0
* @date 2023-01-18
 * Copyright © MyCompany
*/

public interface RecordMapper {
    List<Record> selectAll();
}

