package com.example.iparking.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
*  Entity定义
* @author MyName
* @version 1.0
* @date 2023-01-18
* Copyright © MyCompany
*/
@Getter @Setter @Accessors(chain = true)
public class Record {
    private static final long serialVersionUID = -5742504038504026797L;

    /**
    * 车牌号 
    */
    @Length(max=255, message="车牌号长度应小于255")
    private String plateNumber;

    /**
    * 入场时间 
    */
    @Length(max=255, message="入场时间长度应小于255")
    private String entryTime;

    /**
    * 出场时间 
    */
    @Length(max=255, message="出场时间长度应小于255")
    private String exitTime;

    /**
    * 计费金额 
    */
    private BigDecimal amount;



} 
