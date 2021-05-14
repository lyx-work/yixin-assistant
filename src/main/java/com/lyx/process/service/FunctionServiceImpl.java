package com.lyx.process.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyx.common.CommonResult;
import com.lyx.common.Constant;
import com.lyx.config.YiqiApi;
import com.lyx.entity.ExcelEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("functionServiceImpl")
public class FunctionServiceImpl implements FunctionService
{
    @Autowired
    private ObjectMapper oMapper;

    @Autowired
    @Qualifier("yiqiApi")
    private YiqiApi yiqiApi;

    @Override
    public ResponseEntity hwTodayData(String day, boolean isWq)
    {
        try
        {
            Constant.assNum++;

            // 1.查询数据

            // 请求数据
            if (StrUtil.isBlank(day))
            {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(oMapper.writeValueAsString(CommonResult.errorMsg("必须输入日期")));
            }
            Map<String,Object> jsonData = CollUtil.newHashMap();
            jsonData.put("pageNum", "1");
            jsonData.put("pageSize", "100000");
            if (isWq)
            {
                Map<String, Object> vo = CollUtil.newHashMap();
                vo.put("status", "6");
                vo.put("fundSource", 5);
                jsonData.put("vo", vo);
            }
            else
            {
                Map<String, Object> vo = CollUtil.newHashMap();
                vo.put("fundSource", 1);
                jsonData.put("vo", vo);
            }
            CommonResult<JsonNode> rep = yiqiApi.post(oMapper.writeValueAsString(jsonData), "https://unisrvs.17ebank.com:9117/car-af-apm-core-r/api/paliquidate/paliquidateDspList");
            JsonNode yiqiBody = rep.getData(); // 一汽返回的body
            if (!StrUtil.equals(yiqiBody.get("level").asText(), "success"))
            {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(oMapper.writeValueAsString(CommonResult.errorMsg(yiqiBody.get("message").asText())));
            }

            // 处理数据，只保留[委托开始日期]为某一天的
            JsonNode arr = yiqiBody.get("data");
            List<JsonNode> dataOneDay = CollUtil.newArrayList();
            for (int i = 0; i <= arr.size()-1; i++)
            {
                JsonNode jsonNode = arr.get(i);
                if (DateUtil.betweenDay(DateUtil.parseDate(jsonNode.get("beginDate").asText()), DateUtil.parseDate(day), true) == 0)
                {
                    dataOneDay.add(jsonNode);
                }
            }

            // 转换数据 dataOneDay List<JsonNode> ---> List<Hw>
            List<ExcelEntity> transformedData = dataOneDay.stream()
                                                .map(o -> this.yiqiJsonNode2Hw(o, isWq))
                                                .collect(Collectors.toList());

            // 2.生成excel文件

            // 模板文件
            TemplateExportParams exportParams = new TemplateExportParams("hhwtqs-template.xlsx");

            // 要写入的数据
            Map<String, Object> excelData = CollUtil.newHashMap();
            excelData.put("dList", transformedData);

            // 输出文件
            String excelFilePath = FileUtil.getTmpDirPath() + File.separator + IdUtil.fastSimpleUUID() + ".xlsx";
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, excelData);
            BufferedOutputStream outputStream = FileUtil.getOutputStream(excelFilePath);
            workbook.write(outputStream);
            outputStream.close();

            // 3.下载文件
            byte[] fileWillDownload = FileUtil.readBytes(FileUtil.file(excelFilePath));
            FileUtil.del(excelFilePath);
            String fileName;
            if (isWq)
            {
                fileName = "委托清收-" + day + ".xlsx";
            }
            else
            {
                fileName = "核后委托清收-" + day + ".xlsx";
            }
            return ResponseEntity
                    .ok()
                    .header("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "utf-8"))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(fileWillDownload);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(JSONUtil.toJsonStr(CommonResult.errorMsg("发生异常：" + e.getCause())));
        }
    }



    private ExcelEntity yiqiJsonNode2Hw(JsonNode yiqiJsonNode, boolean isWq)
    {
        ExcelEntity excelEntity = new ExcelEntity();

        // ●个案序列号
        // ●委托日期
        // ●预计退案日期
        // ●姓名
        // ●委案金额
        // ●车辆型号
        // ●牌照号
        excelEntity.setBargainNo(yiqiJsonNode.get("bargainNo").asText())
                    .setBeginDate(DateUtil.parseDateTime(yiqiJsonNode.get("beginDate").asText()))
                    .setEndDate(DateUtil.parseDateTime(yiqiJsonNode.get("endDate").asText()))
                    .setName(yiqiJsonNode.get("name").asText())
                    .setLiquidateSubject(yiqiJsonNode.get("liquidateSubject").asDouble())
                    .setCarKind(yiqiJsonNode.get("vspecification").asText());
        // ●牌照号
        if (Objects.nonNull(yiqiJsonNode.get("vlicenseplateno")))
        {
            excelEntity.setCarNumber(yiqiJsonNode.get("vlicenseplateno").asText());
        }

        // ●证件号
        // ●逾期金额
        // ●本人手机
        // ●单位地址
        // ●家庭地址
        // ●联系人2姓名
        // ●联系人2关系
        // ●联系人2手机
        // ●联系人3姓名
        // ●联系人3关系
        // ●联系人3手机
        // 通过 id 获得 bargainNo
        String id = yiqiJsonNode.get("id").asText();
        CommonResult<JsonNode> rep = yiqiApi.get("https://unisrvs.17ebank.com:9117/car-af-apm-core-r/api/paliquidate/getEndFllowDetailById/" + id);
        if (rep.isSuccess())
        {
            JsonNode body = rep.getData();

            // 通过 bargainNo 获得身份证号
            String bargainNo = body.at("/data/applyBasicDto/bargainNo").asText();
            CommonResult<JsonNode> mRep = yiqiApi.get("https://unisrvs.17ebank.com:9117/car-af-apm-core-r/api/composite/overdue/overdueInfo?bargainNo=" + bargainNo);
            if (mRep.isSuccess())
            {
                JsonNode mBody = mRep.getData();
                excelEntity.setIdCard(mBody.at("/data/bargain/vunipayidno").asText());
            }

            // 通过 bargainNo 获得逾期金额
            CommonResult<JsonNode> oRep = yiqiApi.get("https://unisrvs.17ebank.com:9117/car-af-apm-core-r/api/composite/overdue/prepaymentInfo?bargainNo=" + bargainNo);
            if (oRep.isSuccess())
            {
                JsonNode oBody = oRep.getData();

                JsonNode moneyArr = oBody.get("data");
                if (Objects.nonNull(moneyArr) && !moneyArr.isEmpty())
                {
                    JsonNode moneyOne = moneyArr.get(0).get("totalShouldPay");
                    if (Objects.nonNull(moneyOne))
                    {
                        excelEntity.setOverMoney(moneyOne.asDouble());
                    }
                }
            }

            // 通过 applybasicId 获得手机号、单位地址、家庭地址、联系人2、联系人3
            String applybasicId = body.at("/data/applyBasicDto/applybasicId").asText();
            CommonResult<JsonNode> pRep = yiqiApi.get("https://unisrvs.17ebank.com:9117/car-af-apm-core-r/api/composite/overdue/borrowerInfo?applyBasicId=" + applybasicId);
            if (pRep.isSuccess())
            {
                JsonNode pBody = pRep.getData();
                JsonNode phoneArr = pBody.at("/data/customerConnections");
                for (int i=0; i <= phoneArr.size()-1; i++)
                {
                    JsonNode jsonNode = phoneArr.get(i);
                    if (StrUtil.equals(jsonNode.get("connecttype").asText(), "10")){
                        excelEntity.setPhoneNumber(jsonNode.get("address").asText());
                        break;
                    }
                }
                excelEntity.setWorkAddress(pBody.at("/data/occupationIncomeInfo/compaddr").asText());
                excelEntity.setFamilyAddress(pBody.at("/data/clientBaseInfo/habitation/address").asText());
                excelEntity.setLink2Name(pBody.at("/data/bargainDealerInfo/dealerWorker/vname").asText());
                excelEntity.setLink2relation("销售顾问");
                excelEntity.setLink2phone(pBody.at("/data/bargainDealerInfo/dealerWorker/vmobiletelephone").asText());
                excelEntity.setLink3Name(pBody.at("/data/bargainDealerInfo/vthirdname").asText());
                excelEntity.setLink3relation("第三联系人");
                excelEntity.setLink3phone(pBody.at("/data/bargainDealerInfo/vthirdphone").asText());
            }
        }


        // ●逾期天数
        // 导出 委托清收 时才赋值
        if (isWq)
        {
            excelEntity.setOverDay(yiqiJsonNode.get("overDuedays").asInt());
        }

        return excelEntity;
    }
}