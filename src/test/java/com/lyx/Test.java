package com.lyx;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class Test
{
    public static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws IOException
    {
    }

    /**
     * 输入：车牌号
     * 输出：省
     */
    private static String getProvinceByCarCode(String carCode)
    {
        if (StrUtil.startWith(carCode, "京"))
        {
            return "北京市";
        }
        if (StrUtil.startWith(carCode, "津"))
        {
            return "天津市";
        }
        if (StrUtil.startWith(carCode, "冀"))
        {
            return "河北省";
        }
        if (StrUtil.startWith(carCode, "晋"))
        {
            return "山西省";
        }
        if (StrUtil.startWith(carCode, "蒙"))
        {
            return "内蒙古自治区";
        }
        if (StrUtil.startWith(carCode, "辽"))
        {
            return "辽宁省";
        }
        if (StrUtil.startWith(carCode, "吉"))
        {
            return "吉林省";
        }
        if (StrUtil.startWith(carCode, "黑"))
        {
            return "黑龙江省";
        }
        if (StrUtil.startWith(carCode, "沪"))
        {
            return "上海市";
        }
        if (StrUtil.startWith(carCode, "苏"))
        {
            return "江苏省";
        }
        if (StrUtil.startWith(carCode, "浙"))
        {
            return "浙江省";
        }
        if (StrUtil.startWith(carCode, "皖"))
        {
            return "安徽省";
        }
        if (StrUtil.startWith(carCode, "闽"))
        {
            return "福建省";
        }
        if (StrUtil.startWith(carCode, "赣"))
        {
            return "江西省";
        }
        if (StrUtil.startWith(carCode, "鲁"))
        {
            return "山东省";
        }
        if (StrUtil.startWith(carCode, "豫"))
        {
            return "河南省";
        }
        if (StrUtil.startWith(carCode, "鄂"))
        {
            return "湖北省";
        }
        if (StrUtil.startWith(carCode, "湘"))
        {
            return "湖南省";
        }
        if (StrUtil.startWith(carCode, "粤"))
        {
            return "广东省";
        }
        if (StrUtil.startWith(carCode, "桂"))
        {
            return "广西壮族自治区";
        }
        if (StrUtil.startWith(carCode, "琼"))
        {
            return "海南省";
        }
        if (StrUtil.startWith(carCode, "渝"))
        {
            return "重庆市";
        }
        if (StrUtil.startWith(carCode, "川"))
        {
            return "四川省";
        }
        if (StrUtil.startWith(carCode, "黔"))
        {
            return "贵州省";
        }
        if (StrUtil.startWith(carCode, "滇"))
        {
            return "云南省";
        }
        if (StrUtil.startWith(carCode, "藏"))
        {
            return "西藏自治区";
        }
        if (StrUtil.startWith(carCode, "陕"))
        {
            return "陕西省";
        }
        if (StrUtil.startWith(carCode, "甘"))
        {
            return "甘肃省";
        }
        if (StrUtil.startWith(carCode, "青"))
        {
            return "青海省";
        }
        if (StrUtil.startWith(carCode, "宁"))
        {
            return "宁夏回族自治区";
        }
        if (StrUtil.startWith(carCode, "新"))
        {
            return "新疆维吾尔自治区";
        }
        if (StrUtil.startWith(carCode, "台"))
        {
            return "台湾省";
        }
        if (StrUtil.startWith(carCode, "港"))
        {
            return "香港特别行政区";
        }
        if (StrUtil.startWith(carCode, "澳"))
        {
            return "澳门特别行政区";
        }

        return "未知";
    }

    /**
     * 输入：详细地下，包括省市.
     * 输出：所属的区，如果没获取到返回 ""
     */
    private static String getAreaByAddress(String address)
    {
        String url = "https://restapi.amap.com/v3/geocode/geo?address={address}&output=JSON&key=a23b77b278ece3a9103bd800a4e5fff2";

        JsonNode body;
        try
        {
            body = restTemplate.getForObject(url, JsonNode.class, address);
        }
        catch (Exception e)
        {
            return StrUtil.EMPTY;
        }
        if (body.get("count").asInt() == 0)
        {
            return StrUtil.EMPTY;
        }

        return body.get("geocodes").get(0).get("district").asText();
    }
}