package com.lyx.entity;

import cn.hutool.core.date.DateTime;

/**
 * excel 导出数据实体
 */
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

    /**
     * 备注
     */
    private String remark;

    private String pAG;
    private String pAH;



    public String getVisitType()
    {
        return visitType;
    }

    public void setVisitType(String visitType)
    {
        this.visitType = visitType;
    }

    public String getpB()
    {
        return pB;
    }

    public void setpB(String pB)
    {
        this.pB = pB;
    }

    public String getpC()
    {
        return pC;
    }

    public void setpC(String pC)
    {
        this.pC = pC;
    }

    public String getContractCode()
    {
        return contractCode;
    }

    public void setContractCode(String contractCode)
    {
        this.contractCode = contractCode;
    }

    public String getpE()
    {
        return pE;
    }

    public void setpE(String pE)
    {
        this.pE = pE;
    }

    public String getBelongArea()
    {
        return belongArea;
    }

    public void setBelongArea(String belongArea)
    {
        this.belongArea = belongArea;
    }

    public String getServiceOb()
    {
        return serviceOb;
    }

    public void setServiceOb(String serviceOb)
    {
        this.serviceOb = serviceOb;
    }

    public String getpH()
    {
        return pH;
    }

    public void setpH(String pH)
    {
        this.pH = pH;
    }

    public String getpI()
    {
        return pI;
    }

    public void setpI(String pI)
    {
        this.pI = pI;
    }

    public String getpJ()
    {
        return pJ;
    }

    public void setpJ(String pJ)
    {
        this.pJ = pJ;
    }

    public String getpK()
    {
        return pK;
    }

    public void setpK(String pK)
    {
        this.pK = pK;
    }

    public String getCarCode()
    {
        return carCode;
    }

    public void setCarCode(String carCode)
    {
        this.carCode = carCode;
    }

    public String getpM()
    {
        return pM;
    }

    public void setpM(String pM)
    {
        this.pM = pM;
    }

    public String getpN()
    {
        return pN;
    }

    public void setpN(String pN)
    {
        this.pN = pN;
    }

    public String getpO()
    {
        return pO;
    }

    public void setpO(String pO)
    {
        this.pO = pO;
    }

    public Integer getOverdueDays()
    {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays)
    {
        this.overdueDays = overdueDays;
    }

    public String getpQ()
    {
        return pQ;
    }

    public void setpQ(String pQ)
    {
        this.pQ = pQ;
    }

    public String getpR()
    {
        return pR;
    }

    public void setpR(String pR)
    {
        this.pR = pR;
    }

    public String getpS()
    {
        return pS;
    }

    public void setpS(String pS)
    {
        this.pS = pS;
    }

    public String getpT()
    {
        return pT;
    }

    public void setpT(String pT)
    {
        this.pT = pT;
    }

    public String getpU()
    {
        return pU;
    }

    public void setpU(String pU)
    {
        this.pU = pU;
    }

    public String getToVisitName()
    {
        return toVisitName;
    }

    public void setToVisitName(String toVisitName)
    {
        this.toVisitName = toVisitName;
    }

    public String getAddressType()
    {
        return addressType;
    }

    public void setAddressType(String addressType)
    {
        this.addressType = addressType;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getArea()
    {
        return area;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getDetailAddress()
    {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress)
    {
        this.detailAddress = detailAddress;
    }

    public String getpAB()
    {
        return pAB;
    }

    public void setpAB(String pAB)
    {
        this.pAB = pAB;
    }

    public String getpAC()
    {
        return pAC;
    }

    public void setpAC(String pAC)
    {
        this.pAC = pAC;
    }

    public DateTime getGiveTime()
    {
        return giveTime;
    }

    public void setGiveTime(DateTime giveTime)
    {
        this.giveTime = giveTime;
    }

    public String getpAE()
    {
        return pAE;
    }

    public void setpAE(String pAE)
    {
        this.pAE = pAE;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getpAG()
    {
        return pAG;
    }

    public void setpAG(String pAG)
    {
        this.pAG = pAG;
    }

    public String getpAH()
    {
        return pAH;
    }

    public void setpAH(String pAH)
    {
        this.pAH = pAH;
    }

    @Override
    public String toString()
    {
        return "ExcelEntity{" +
                "visitType='" + visitType + '\'' +
                ", pB='" + pB + '\'' +
                ", pC='" + pC + '\'' +
                ", contractCode='" + contractCode + '\'' +
                ", pE='" + pE + '\'' +
                ", belongArea='" + belongArea + '\'' +
                ", serviceOb='" + serviceOb + '\'' +
                ", pH='" + pH + '\'' +
                ", pI='" + pI + '\'' +
                ", pJ='" + pJ + '\'' +
                ", pK='" + pK + '\'' +
                ", carCode='" + carCode + '\'' +
                ", pM='" + pM + '\'' +
                ", pN='" + pN + '\'' +
                ", pO='" + pO + '\'' +
                ", overdueDays=" + overdueDays +
                ", pQ='" + pQ + '\'' +
                ", pR='" + pR + '\'' +
                ", pS='" + pS + '\'' +
                ", pT='" + pT + '\'' +
                ", pU='" + pU + '\'' +
                ", toVisitName='" + toVisitName + '\'' +
                ", addressType='" + addressType + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", pAB='" + pAB + '\'' +
                ", pAC='" + pAC + '\'' +
                ", giveTime=" + giveTime +
                ", pAE='" + pAE + '\'' +
                ", remark='" + remark + '\'' +
                ", pAG='" + pAG + '\'' +
                ", pAH='" + pAH + '\'' +
                '}';
    }
}
