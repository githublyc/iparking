package com.example.iparking.util;

import com.example.iparking.pojo.SendReq;
import com.example.iparking.pojo.SendRes;
import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;


import java.io.*;
import java.net.*;

public class SMSClient {

    private static String apId = "cs1124";
    private static String secretKey = "CScs1129*";
    private static String ecName = "威海市城市开发投资有限公司";//集团名称
    private static String sign = "odeNKvvuy";//网关签名编码
    private static String addSerial = "";//拓展码 填空
    public static String url = "http://112.35.1.155:1992/sms/norsubmit";//请求url

    public static int sendMsg(String mobiles, String content) throws UnsupportedEncodingException {
        SendReq sendReq = new SendReq();
        sendReq.setApId(apId);
        sendReq.setEcName(ecName);
        sendReq.setSecretKey(secretKey);
        sendReq.setContent(content);
        sendReq.setMobiles(mobiles);
        sendReq.setAddSerial(addSerial);
        sendReq.setSign(sign);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(sendReq.getEcName());
        stringBuffer.append(sendReq.getApId());
        stringBuffer.append(sendReq.getSecretKey());
        stringBuffer.append(sendReq.getMobiles());
        stringBuffer.append(sendReq.getContent());
        stringBuffer.append(sendReq.getSign());
        stringBuffer.append(sendReq.getAddSerial());
        //参数校验序列，生成方法：将ecName、apId、secretKey、mobiles、content、sign、addSerial按序拼接（无间隔符），通过MD5（32位小写）计算得出值。
        System.out.println(stringBuffer.toString());
        sendReq.setMac(Md5Util.MD5(stringBuffer.toString()).toLowerCase());
        System.out.println(sendReq.getMac());
        String reqText = JSON.toJSONString(sendReq);
        System.out.println(reqText);
        String encode = Base64.encodeBase64String(reqText.getBytes("UTF-8"));
        System.out.println(encode);

        String resStr = sendPost(url, encode);

        System.out.print("发送短信结果：" + resStr);

        SendRes sendRes = JSON.parseObject(resStr, SendRes.class);

        if (sendRes.isSuccess() && !"".equals(sendRes.getMsgGroup()) && "success".equals(sendRes.getRspcod())) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数
     * @return 所代表远程资源的响应结果
     */
    private static String sendPost(String url, String param) {
        OutputStreamWriter out = null;

        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("contentType","utf-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(param);
            out.flush();


            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}

