package com.lyx;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyx.process.service.FunctionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

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
        System.out.println(FileUtil.getUserHomePath());
    }
}