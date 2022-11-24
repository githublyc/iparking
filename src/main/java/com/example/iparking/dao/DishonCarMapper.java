package com.example.iparking.dao;

import com.example.iparking.pojo.DishonCarDO;

public interface DishonCarMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DishonCarDO record);

    int insertSelective(DishonCarDO record);

    DishonCarDO selectByPrimaryKey(Integer id);

    DishonCarDO selectByCarNumber(String carNumber);

    int updateByPrimaryKeySelective(DishonCarDO record);

    int updateByPrimaryKey(DishonCarDO record);
}