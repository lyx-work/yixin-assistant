package com.lyx.process.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.lyx.common.CommonResult;
import com.lyx.common.Constant;
import com.lyx.config.InvokeYixinConfig;
import com.lyx.entity.ExcelEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

@Service("functionServiceImpl")
public class FunctionServiceImpl implements FunctionService
{
    @Autowired
    @Qualifier("invokeYixinConfig")
    private InvokeYixinConfig invokeYixin;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;



    @Override
    public ResponseEntity export(String tokenStr)
    {
        Constant.ASS_COUNT++;

        // ①判断输入的tokenStr正不正确
        CommonResult trueTokenRep = this.isTrueToken(tokenStr);
        if (!trueTokenRep.isSuccess())
        {
            return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(trueTokenRep);
        }

        // ②获得需要导出到excel的数据
        List<JsonNode> orderMasterList = listNeedExportOrderMasterList(tokenStr);
        List<ExcelEntity> excelEntityList = CollUtil.newArrayList();
        for (JsonNode el : orderMasterList)
        {
            excelEntityList.addAll(this.orderMaster2ExcelEntityList(el, tokenStr));
        }
        Map<String, Object> excelData = CollUtil.newHashMap();
        excelData.put("ocList", excelEntityList);

        // ③写入数据，生成新的文件
        TemplateExportParams excelTemplate = new TemplateExportParams("template.xlsx");
        Workbook workbook = ExcelExportUtil.exportExcel(excelTemplate, excelData);
        File newExcelFile = FileUtil.file(FileUtil.getUserHomePath() + "/" + IdUtil.simpleUUID() + ".xlsx");
        try
        (
            BufferedOutputStream outputStream = FileUtil.getOutputStream(newExcelFile)
        )
        {
            workbook.write(outputStream);
        }
        catch (Exception e)
        {
            return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(CommonResult.errorMsg(StrUtil.format("出现异常：{}", e.getMessage())));
        }

        // ④将生成的文件读取为二进制数据，并删除原文件
        byte[] fileData = FileReader.create(newExcelFile).readBytes();
        FileUtil.del(newExcelFile);

        // ⑤下载文件
        return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment;fileName=yixin-export.xlsx")
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .body(fileData);
    }



    /**
     * 判断输入的tokenStr正不正确
     */
    public CommonResult isTrueToken(String tokenStr)
    {
        String jsonData = "{\"applyNo\":\"\",\"customerName\":\"\",\"pickUpCarCompany\":\"\",\"pickUpCarManager\":\"\",\"applyBeginTime\":\"\",\"applyUser\":\"\",\"status\":\"wait_distribute\",\"index\":1,\"pageSize\":2}";
        CommonResult<JsonNode> yixinBody = invokeYixin.post("/visit/outsource/inside/pageQuery", tokenStr, jsonData);

        return yixinBody;
    }

    /**
     * 获得所有的需要导出的主单
     */
    private List<JsonNode> listNeedExportOrderMasterList(String tokenStr)
    {
        List<String> omIdList = this.listNeedExportOrderMasterIdList(tokenStr);

        List<JsonNode> orderMasterList = CollUtil.newArrayList();
        for (String el : omIdList)
        {
            String jsonData = StrUtil.format("{\"id\":\"{}\"}", el);
            CommonResult<JsonNode> yixinBody = invokeYixin.post("/visit/outsource/detail/taskDetail", tokenStr, jsonData);
            if (yixinBody.isSuccess())
            {
                orderMasterList.add(yixinBody.getData());
            }
        }

        return orderMasterList;
    }

    /**
     * 获得所有的需要导出的主单的id
     */
    private List<String> listNeedExportOrderMasterIdList(String tokenStr)
    {
        String jsonData = "{\"applyNo\":\"\",\"customerName\":\"\",\"pickUpCarCompany\":\"\",\"pickUpCarManager\":\"\",\"applyBeginTime\":\"\",\"applyUser\":\"\",\"status\":\"wait_distribute\",\"index\":1,\"pageSize\":300}";
        CommonResult<JsonNode> yixinBodyRep = invokeYixin.post("/visit/outsource/inside/pageQuery", tokenStr, jsonData);

        if (!yixinBodyRep.isSuccess())
        {
            return CollUtil.newArrayList();
        }

        JsonNode data = yixinBodyRep.getData();
        JsonNode orderMasterList = data.at("/items");

        List<String> orderMasterIdList = CollUtil.newArrayList();
        for (JsonNode el : orderMasterList)
        {
            orderMasterIdList.add(el.get("id").asText());
        }

        return orderMasterIdList;
    }

    /**
     * 输入：易鑫系统获得的主单
     * 输出：多个子单生成的excelEntity
     *
     * 这个方法下边挂着 getCarCodeByContractCode  getProvinceByCarCode  getAreaByAddress yixinAddressTypeInt2SVSAddressType
     *
     * @param orderMaster https://www.notion.so/7ea874785dba4f039dc7f59ef3200207
     * @param tokenStr 认证token
     */
    private List<ExcelEntity> orderMaster2ExcelEntityList(JsonNode orderMaster, String tokenStr)
    {
        ArrayNode orderChildList = (ArrayNode) orderMaster.get("subTasks");

        List<ExcelEntity> result = CollUtil.newArrayList();
        for (JsonNode oc : orderChildList)
        {
            ExcelEntity excelEntity = new ExcelEntity();

            excelEntity.setVisitType(orderMaster.get("reasonText").asText());

            excelEntity.setContractCode(orderMaster.get("applyNo").asText());
            excelEntity.setCarCode(this.getCarCodeByContractCode(excelEntity.getContractCode(), tokenStr));
            excelEntity.setBelongArea(this.getProvinceByCarCode(excelEntity.getCarCode()));

            excelEntity.setServiceOb(orderMaster.get("customerName").asText());
            excelEntity.setToVisitName(excelEntity.getServiceOb());

            excelEntity.setAddressType(this.yixinAddressTypeInt2SVSAddressType(oc.get("addressType").asInt()));

            String provinceName = oc.get("provinceName").asText();
            if (!StrUtil.endWith(provinceName, '省'))
            {
                provinceName = provinceName + '省';
            }
            excelEntity.setProvince(provinceName);

            String cityName = oc.get("cityName").asText();
            if (!StrUtil.endWith(cityName, '市'))
            {
                cityName = cityName + '市';
            }
            excelEntity.setCity(cityName);

            excelEntity.setDetailAddress(oc.get("address").asText());
            excelEntity.setArea(this.getAreaByAddress(excelEntity.getProvince() + excelEntity.getCity() + excelEntity.getDetailAddress()));
            excelEntity.setGiveTime(DateUtil.parseDate(orderMaster.get("assignDate").asText()));

            excelEntity.setOverdueDays(this.getOverdueDaysByContractCode(excelEntity.getContractCode(), tokenStr));
            excelEntity.setRemark(StrUtil.format("派单人-{}", orderMaster.get("linkmanName").asText()));

            result.add(excelEntity);
        }

        return result;
    }

    /**
     * 通过合同编号获取逾期天数
     */
    public int getOverdueDaysByContractCode(String contractCode, String tokenStr)
    {
        String jsonStr = StrUtil.format("{\"applyNo\":\"{}\"}", contractCode);
        CommonResult<JsonNode> yixinBody = invokeYixin.post("/visit/outsource/detail/repayInfo", tokenStr, jsonStr);
        return yixinBody.getData().get("overdueDays").asInt();
    }

    /**
     * 通过合同编号获得车牌号
     */
    private String getCarCodeByContractCode(String contractCode, String tokenStr)
    {
        String jsonStr = StrUtil.format("{\"applyNo\":\"{}\"}", contractCode);
        CommonResult<JsonNode> yixinBody = invokeYixin.post("/recall/car", tokenStr, jsonStr);
        return yixinBody.getData().get("licensePlateNum").asText();
    }

    /**
     * 输入：车牌号
     * 输出：省
     */
    private String getProvinceByCarCode(String carCode)
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
     * 输入：详细地址，包括省市.
     * 输出：所属的区，如果没获取到返回 ""
     */
    private String getAreaByAddress(String address)
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

    /**
     * 输入：易鑫的地址类型数字
     * 输出：SVS的地址类型
     */
    private String yixinAddressTypeInt2SVSAddressType(int typeInt)
    {
        switch (typeInt)
        {
            case 1:
            {
                return "居住地址";
            }
            case 2:
            {
                return "户籍地址";
            }
            case 3:
            {
                return "单位地址";
            }
            default:
            {
                return "未知";
            }
        }
    }
}
