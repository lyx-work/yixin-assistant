package com.lyx.common;

import cn.hutool.core.map.MapUtil;

import java.util.Map;

public class ConstantAndVar
{
    public static final Map<String, String> PROVINCE_MAP = MapUtil.newHashMap();

    /**
     * 接口访问次数.
     */
    public static long assCount = 989;

    static
    {
        PROVINCE_MAP.put("河北", "河北省");
        PROVINCE_MAP.put("山西", "山西省");
        PROVINCE_MAP.put("辽宁", "辽宁省");
        PROVINCE_MAP.put("吉林", "吉林省");
        PROVINCE_MAP.put("黑龙江", "黑龙江省");
        PROVINCE_MAP.put("江苏", "江苏省");
        PROVINCE_MAP.put("浙江", "浙江省");
        PROVINCE_MAP.put("安徽", "安徽省");
        PROVINCE_MAP.put("福建", "福建省");
        PROVINCE_MAP.put("江西", "江西省");
        PROVINCE_MAP.put("山东", "山东省");
        PROVINCE_MAP.put("河南", "河南省");
        PROVINCE_MAP.put("湖北", "湖北省");
        PROVINCE_MAP.put("湖南", "湖南省");
        PROVINCE_MAP.put("广东", "广东省");
        PROVINCE_MAP.put("海南", "海南省");
        PROVINCE_MAP.put("四川", "四川省");
        PROVINCE_MAP.put("贵州", "贵州省");
        PROVINCE_MAP.put("云南", "云南省");
        PROVINCE_MAP.put("陕西", "陕西省");
        PROVINCE_MAP.put("甘肃", "甘肃省");
        PROVINCE_MAP.put("青海", "青海省");
        PROVINCE_MAP.put("内蒙古", "内蒙古自治区");
        PROVINCE_MAP.put("广西", "广西壮族自治区");
        PROVINCE_MAP.put("西藏", "西藏自治区");
        PROVINCE_MAP.put("宁夏", "宁夏回族自治区");
        PROVINCE_MAP.put("新疆", "新疆维吾尔自治区");
        PROVINCE_MAP.put("北京", "北京市");
        PROVINCE_MAP.put("天津", "天津市");
        PROVINCE_MAP.put("上海", "上海市");
        PROVINCE_MAP.put("重庆", "重庆市");
    }
}
