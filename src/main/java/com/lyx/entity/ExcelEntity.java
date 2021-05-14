package com.lyx.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * excel 导出数据实体
 * 核后委托清收 与 委托清收 导出
 */
@Data
@Accessors(chain = true)
public class ExcelEntity
{
    /**
     * 个案序列号
     * 在一汽中就是合同编号
     */
    private String bargainNo;

    /**+
     * 委托日期
     */
    private Date beginDate;

    /**
     * 预计退案日期
     */
    private Date endDate;

    /**
     * 姓名
     */
    private String name;

    /**
     * 合同编号
     * pX 为excel中隐藏的列
     */
    private String pE;

    /**
     * 证件号
     * 就是身份证号码
     */
    private String idCard;

    /**
     * 委案金额
     */
    private Double liquidateSubject;

    /**
     * 逾期天数
     */
    private Integer overDay;

    /**
     * 逾期金额
     */
    private Double overMoney;

    private String pJ;
    private String pK;
    private String pL;
    private String pM;
    private String pN;
    private String pO;
    private String pP;
    private String pQ;
    private String pR;

    /**
     * 本人手机
     */
    private String phoneNumber;

    private String pT;
    private String pU;
    private String pV;
    private String pW;
    private String pX;
    private String pY;
    private String pZ;
    private String pAA;

    /**
     * 车型
     */
    private String carKind;

    /**
     * 牌照号
     */
    private String carNumber;

    private String pAD;
    private String pAE;
    private String pAF;
    private String pAG;
    private String pAH;

    /**
     * 单位地址
     */
    private String workAddress;

    /**
     * 家庭地址
     */
    private String familyAddress;

    private String pAK;
    private String pAL;
    private String pAM;
    private String pAN;
    private String pAO;
    private String pAP;
    private String pAQ;
    private String pAR;

    /**
     * 联系人2姓名
     */
    private String link2Name;

    /**
     * 联系人2关系
     */
    private String link2relation;

    /**
     * 联系人2手机
     */
    private String link2phone;

    /**
     * 联系人3姓名
     */
    private String link3Name;

    /**
     * 联系人3关系
     */
    private String link3relation;

    /**
     * 联系人3手机
     */
    private String link3phone;

    private String pAY;
    private String pAZ;
    private String pBA;
}