package com.example.iparking.util;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class SMSClientTest {

    @Test
    public void sendMsg() throws UnsupportedEncodingException {
        SMSClient client = new SMSClient();
        int result = client.sendMsg("15610163972","这是测试！");
        System.out.println(result);
    }
}