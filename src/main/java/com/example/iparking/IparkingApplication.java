package com.example.iparking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.iparking.dao")
public class IparkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(IparkingApplication.class, args);
	}

}
