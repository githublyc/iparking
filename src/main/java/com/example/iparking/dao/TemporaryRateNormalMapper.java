package com.example.iparking.dao;

import com.example.iparking.pojo.TemporaryRateNormal;

/**
 * @Description: 临时车费率子表-标准收费
 * @Author lxd
 * @Date 2021-04-07 14:01:53
 */
public interface TemporaryRateNormalMapper {
    TemporaryRateNormal getNormalMsgByRateId(String rateId);
}
