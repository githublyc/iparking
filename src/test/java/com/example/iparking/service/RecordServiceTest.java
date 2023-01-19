package com.example.iparking.service;

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
        returnMap = recordService.parkingAmount("2023-01-16 09:22:28","2023-01-17 08:51:59");//"2023-01-16 09:22:28","2023-01-17 08:51:59"
        System.out.println(returnMap.get("parkingAmount"));
    }
    @Test
    public void countAmount() throws ParseException {
        BigDecimal tst = recordService.countAmount();
        System.out.println(tst);
    }
}