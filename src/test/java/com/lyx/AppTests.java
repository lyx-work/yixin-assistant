package com.lyx;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyx.entity.ExcelEntity;
import com.lyx.process.service.FunctionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class AppTests
{
    @Autowired
    private FunctionServiceImpl functionService;

    @Autowired
    private ObjectMapper oMapper;

	@Test
	public void test1() throws IOException
    {
        JsonNode orderMasterList = oMapper.readTree(FileUtil.file("/Users/lyx/my-dir/download/暂不要删除/orderMasterListHave2.json"));

        List<ExcelEntity> excelEntityList = CollUtil.newArrayList();
        for (JsonNode el : orderMasterList)
        {
            excelEntityList.addAll(functionService.orderMaster2ExcelEntityList(el, "token=M2RjYWU5ODctY2NjZS00MmQ4LWE0ODgtZDNkZTY3NjFhNjc0"));
        }

        System.out.println(oMapper.writeValueAsString(excelEntityList));
    }
}