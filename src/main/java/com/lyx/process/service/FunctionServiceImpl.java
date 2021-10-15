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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lyx.common.CommonResult;
import com.lyx.common.ConstantAndVar;
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
import java.util.Set;

@Service("functionServiceImpl")
public class FunctionServiceImpl implements FunctionService
{
    @Autowired
    @Qualifier("invokeYixinConfig")
    private InvokeYixinConfig invokeYixin;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper oMapper;



    @Override
    public ResponseEntity export(String tokenStr)
    {
        ConstantAndVar.assCount++;

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
        List<JsonNode> orderMasterIdAndRegionList = this.listNeedExportOrderMasterIdList(tokenStr);

        List<JsonNode> orderMasterList = CollUtil.newArrayList();
        for (JsonNode el : orderMasterIdAndRegionList)
        {
            String jsonData = StrUtil.format("{\"id\":\"{}\"}", el.get("id").asText());
            CommonResult<JsonNode> yixinBody = invokeYixin.post("/visit/outsource/detail/taskDetail", tokenStr, jsonData);
            if (yixinBody.isSuccess())
            {
                JsonNode data = yixinBody.getData();
                ((ObjectNode)data).put("region", el.get("region").asText());
                orderMasterList.add(data);
            }
        }

        return orderMasterList;
    }

    /**
     * 获得所有的需要导出的主单的id与主单所在区域
     * @return https://panoramic-mayonnaise-894.notion.site/a56c4c99168c465182bcf1928c81d3e7
     */
    private List<JsonNode> listNeedExportOrderMasterIdList(String tokenStr)
    {
        String jsonData = "{\"applyNo\":\"\",\"customerName\":\"\",\"pickUpCarCompany\":\"\",\"pickUpCarManager\":\"\",\"applyBeginTime\":\"\",\"applyUser\":\"\",\"status\":\"wait_distribute\",\"index\":1,\"pageSize\":300}";
        CommonResult<JsonNode> yixinBodyRep = invokeYixin.post("/visit/outsource/inside/pageQuery", tokenStr, jsonData);

        if (!yixinBodyRep.isSuccess())
        {
            return CollUtil.newArrayList();
        }

        JsonNode data = yixinBodyRep.getData();
        JsonNode orderMasterList = data.at("/items");

        List<JsonNode> orderMasterIdList = CollUtil.newArrayList();
        for (JsonNode el : orderMasterList)
        {
            ObjectNode node = oMapper.createObjectNode()
                                    .put("id", el.get("id").asText())
                                    .put("region", el.get("region").asText());
            orderMasterIdList.add(node);
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
            excelEntity.setBelongArea(this.getProvinceByYixinProvince(orderMaster.get("region").asText())); // 所属区域

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

            excelEntity.setOverdueDays(this.getOverdueDaysByContractCode(orderMaster.get("id").asText(), tokenStr));
            excelEntity.setRemark(StrUtil.format("派单人-{}", orderMaster.get("linkmanName").asText()));

            result.add(excelEntity);
        }

        return result;
    }

    /**
     * 通过id获取逾期天数
     */
    public int getOverdueDaysByContractCode(String id, String tokenStr)
    {
        String jsonStr = StrUtil.format("{\"id\":\"{}\"}", id);
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
     * 输入：易鑫系统所属区域字符串
     * 输出：所属区域的省
     */
    private String getProvinceByYixinProvince(String carCode)
    {
        Set<String> provinceMiniNameList = ConstantAndVar.PROVINCE_MAP.keySet();
        for (String el : provinceMiniNameList)
        {
            if (StrUtil.contains(carCode, el))
            {
                return ConstantAndVar.PROVINCE_MAP.get(el);
            }
        }

        return "获取失败，请手动填写";
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
