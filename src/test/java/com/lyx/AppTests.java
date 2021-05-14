package com.lyx;

import com.lyx.process.service.FunctionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class AppTests
{
    @Autowired
    @Qualifier("functionServiceImpl")
    private FunctionServiceImpl functionService;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

	@Test
	public void test1()
    {
        functionService.listNeedExportOrderMaster("token=ZmI4ZTgzNDUtMmM4NC00Y2ZhLWI5MTEtMjE3NzViMmY2NDJi");
	}
}
