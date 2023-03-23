package com.example.iparking.service;

import com.example.iparking.pojo.ParkRecordQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
@SpringBootTest
@RunWith(SpringRunner.class)
public class RecordServiceTest {
    @Autowired
    RecordService recordService;
    @Test
    public void parkingAmount() throws ParseException {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap = recordService.parkingAmount("2022-08-26 00:00:00","2023-01-17 00:00:00",2);//"2023-01-16 09:22:28","2023-01-17 08:51:59"
        System.out.println(returnMap.get("parkingAmount"));
    }
    @Test
    public void countAmount() throws ParseException {
        int id =23873;
        BigDecimal tst = recordService.countAmount(id,1);
        System.out.println("end:" + tst);
    }
    @Test
    public void countAmountX() throws ParseException {
        ParkRecordQuery parkRecordQuery = new ParkRecordQuery();
        parkRecordQuery.setVehicleType("1");
        parkRecordQuery.setParkKeyList("363");
        parkRecordQuery.setGcExitTimeStart("2022-08-26 00:00:00");
        parkRecordQuery.setGcExitTimeEnd("2023-01-17 00:00:00");
        parkRecordQuery.setLotModel("2");
        BigDecimal tst = recordService.countAmountX(parkRecordQuery);
        System.out.println("end:" + tst);
    }
}