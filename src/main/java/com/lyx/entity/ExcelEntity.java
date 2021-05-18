package com.lyx.entity;

import cn.hutool.core.date.DateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * excel 导出数据实体
 */
@Data
@Accessors(chain = true)
public class ExcelEntity
{
    /**
     * 外访类型
     */
    private String visitType;

    private String pB;
    private String pC;


    /**+
     * 合同编号
     */
    private String  contractCode;

    private String pE;

    /**+
     * 所属区域
     */
    private String belongArea;


    /**+
     * 服务对象
     */
    private String serviceOb;

    private String pH;
    private String pI;
    private String pJ;
    private String pK;

    /**
     * 车牌号
     */
    private String carCode;

    private String pM;
    private String pN;
    private String pO;

    /**
     * 逾期天数
     */
    private Integer overdueDays;

    private String pQ;
    private String pR;
    private String pS;
    private String pT;
    private String pU;

    /**
     * 被访人姓名
     */
    private String toVisitName;

    /**
     * 地址类型
     */
    private String addressType;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 详细地址
     */
    private String detailAddress;

    private String pAB;
    private String pAC;

    /**
     * 委案时间
     */
    private DateTime giveTime;

    private String pAE;
    private String pAF;
    private String pAG;
    private String pAH;
}