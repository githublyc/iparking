package com.example.iparking.dao;

import com.example.iparking.pojo.ParkDO;

public interface ParkMapper {
    ParkDO getParkByParkKey(int parkKey);
}
