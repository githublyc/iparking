package com.example.iparking.dao;

import com.example.iparking.pojo.TemporaryRateDayandnight;

/**
 * @Description: 临时车费率子表-按白天夜间收费
 * @Author lxd
 * @Date 2021-04-07 14:01:53
 */
public interface TemporaryRateDayandnightMapper {
	
	TemporaryRateDayandnight getDayAndNightMsgByRateId(String rateId);

}
