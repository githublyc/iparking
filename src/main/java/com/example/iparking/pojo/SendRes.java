package com.example.iparking.pojo;

/**
 * 发送短信响应实体
 */
public class SendRes {
    private String rspcod;//响应码
    private String msgGroup;//消息批次号，由云MAS平台生成，用于验证短信提交报告和状态报告的一致性（取值msgGroup）注:如果数据验证不通过msgGroup为空
    private boolean success;

    public String getRspcod() {
        return rspcod;
    }

    public void setRspcod(String rspcod) {
        this.rspcod = rspcod;
    }

    public String getMsgGroup() {
        return msgGroup;
    }

    public void setMsgGroup(String msgGroup) {
        this.msgGroup = msgGroup;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
