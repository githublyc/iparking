package com.example.iparking.pojo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 发送短信请求实体
 */
public class SendReq {
    @JSONField(ordinal = 0)
    private String ecName;//集团客户名称
    @JSONField(ordinal = 1)
    private String apId;//用户名
    @JSONField(ordinal = 2)
    private String secretKey;//密码
    @JSONField(ordinal = 3)
    private String mobiles;//手机号码逗号分隔。(如“18137282928,18137282922,18137282923”)
    @JSONField(ordinal = 4)
    private String content;//发送短信内容
    @JSONField(ordinal = 5)
    private String sign;//网关签名编码，必填，签名编码在中国移动集团开通帐号后分配，可以在云MAS网页端管理子系统-SMS接口管理功能中下载。
    @JSONField(ordinal = 6)
    private String addSerial;//扩展码，根据向移动公司申请的通道填写，如果申请的精确匹配通道，则填写空字符串("")，否则添加移动公司允许的扩展码。
    @JSONField(ordinal = 7)
    private String mac;//API输入参数签名结果，签名算法：将ecName，apId，secretKey，mobiles，content ，sign，addSerial按照顺序拼接，然后通过md5(32位小写)计算后得出的值。

    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAddSerial() {
        return addSerial;
    }

    public void setAddSerial(String addSerial) {
        this.addSerial = addSerial;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
