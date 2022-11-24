package com.example.iparking.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class ParkDO {
    private Integer parkKey;
    // 车场名称
    private String parkName;
    // 车场编码
    private String parkCode;
    // 车场模型(1：路内停车场2：路外停车场)
    private String parkModule;
    // 车场类型(1：普通停车场2：一类停车场3：二类停车场4：三类停车场5：四类停车场6：五类停车场7：六类停车场)
    private String parkType;
    //是否无牌入场
    private String unlicensed;
    //无牌入场方式
    private String unlicensedMethod;
    //欠费入场方式
    private String arrears;
    // 车场总泊位数
    private Integer parkSpaceTotal;
    // 车场开放泊位数
    private Integer parkSpaceOpen;
    // 车场经营方式(1：自营2：租赁)
    private String parkOperateType;
    // 车场收费标准(1：普通收费标准2：一类收费标准3：二类收费标准4：三类收费标准5:四类收费标准)
    private String parkChargeStandard;
    // 车场所属街道
    private Integer streetKey;
    // 车场所属区域
    private Integer areaKey;
    // 车场详细地址
    private String detailedAddress;
    // 车场经度
    private BigDecimal parkLongitude;
    // 车场纬度
    private BigDecimal parkLatitude;
    // 车场产权单位
    private String parkPropertyUnit;
    // 车场所属机构(对应机构的key)
    private String organKey;
    // 证书号
    private String certificationNum;
    // 证书有效时间
    private String certificationTime;
    // 是否对账(1：否2：是)
    private String ifReconciliation;
    // 分成比例（%）
    private Integer sharingRate;
    // 车场使用状态(1：启用2：停用)
    private String parkUseStatus;
    // 车场删除状态(1：未删除 -1：删除)
    private String parkDeleteStatus;
    // 是否是事故停车场(1：否2：是)
    private String ifAccidentPark;
    // 营业开始时间
    private String openTimeStart;
    // 营业结束时间
    private String openTimeEnd;
    // 创建时间(不自动更新)
    private Date createdAt;
    // 创建者
    private String createdBy;
    // 修改时间(自动更新)
    private Date updatedAt;
    // 修改者
    private String updatedBy;
    // 停车免费时长
    private int freeTime;
    // 是否包月车 （0：不包月 1：包月）
    private String isMonthly;
    /** 是否可以开发票 (0否1可)*/
    private String isInvoice;
    /** 缴费超时时间（小时数）逾期 */
    private Integer paymentOvertime;
    /** 预交费支付后离场时间 */
    private Integer prepaidLeaveTime;
    /** 淡旺季开关（0关闭1开启） */
    private String isSeason;

    /** 车场启用时间 */
    private Date enableTime;

    /** 是否开始一位多车（0关闭1开启） */
    private String isOneMore;

    // 导入时 使用
//    private String areaMsg;
//    private String organMsg;
    /**
     *  是否启用反向寻车 0 否  1 是
     */
    private Integer isEnableCarSearch;

    /**
     * 反向寻车地址
     */
    private String searchCarAddress;

    private Long isRecovery;//是否强制追缴
    private Long recoveryTime;//追缴时限

    /**
     * 高德poiid
     */
    private String poiid;


    private String overdueAdmission; // 逾期入场方式（1入场2禁止入场）
    private String overdueAdmissionMonth; // 包月车逾期入场方式（1入场2禁止入场）
    private String isPushTrafficPolice; // 是否推送车场信息给交警（1推送2不推送）
    // etc 秘钥
    private String etcPrivateKey;
    // etc 公钥
    private String etcPublicKey;

    // 是否启用无感支付 0未启用1启用
    private String ifnosense;
    // 是否启用ETC支付 0未启用1启用
    private String ifetc;

    private Integer lastSpace;// 剩余泊位数 车场
    private Integer lastSpaceInArea;// 剩余泊位数 区域

    private Integer isPushData;// 是否推送停车数据(0关闭，1开启) 大数据局
    private Integer isPushDataProperty;// 是否推送停车数据(0关闭，1开启) 红色物业


    private String temporaryVehicleMobilization;// 临时车是否可进（0：允许进入；1：禁止进入）
    private String etcSoftBoxIp; // etc软盒ip
    private String etcAppid; // etc应用appid
}
