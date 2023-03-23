package com.example.iparking.dao;

import com.example.iparking.pojo.ParkRecordQuery;
import com.example.iparking.pojo.Record;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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

    List<Integer> selectRecordByQuery(ParkRecordQuery parkRecordQuery);

    String selectStartTime(int id);

    String selectEndTime(int id);

    int updateAmount(@Param("amount") BigDecimal amount, @Param("id") int id);
}

