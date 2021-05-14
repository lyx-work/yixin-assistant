package com.lyx;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyx.entity.ExcelEntity;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Test
{
    public static void main(String[] args) throws IOException
    {
        // 模板文件
        TemplateExportParams exportParams = new TemplateExportParams("/Users/lyx/my-dir/download/aa.xlsx");

        // 要写入的数据
        ArrayList<Object> list = CollUtil.newArrayList();
        list.add(new ExcelEntity().setBargainNo("a1").setEndDate(DateUtil.date()));
        list.add(new ExcelEntity().setBargainNo("a2").setEndDate(DateUtil.date()));
        list.add(new ExcelEntity().setBargainNo("a3").setEndDate(DateUtil.date()));

        Map<String, Object> excelData = CollUtil.newHashMap();
        excelData.put("dList", list);

        // 输出文件
        String excelFilePath = "/Users/lyx/my-dir/download/" + IdUtil.fastSimpleUUID() + ".xlsx";
        System.out.println(excelFilePath);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, excelData);
        BufferedOutputStream outputStream = FileUtil.getOutputStream(excelFilePath);
        workbook.write(outputStream);
        outputStream.close();
    }
}