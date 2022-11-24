package com.example.iparking.dao;

import com.example.iparking.IparkingApplicationTests;
import com.example.iparking.pojo.ParkDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ParkMapperTest extends IparkingApplicationTests {

    @Autowired
    ParkMapper parkMapper;

    @Test
    public void getParkByParkKey() {
        ParkDO parkDO = parkMapper.getParkByParkKey(168);
        System.out.println(parkDO);
    }
}