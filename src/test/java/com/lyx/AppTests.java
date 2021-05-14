package com.lyx;

import com.lyx.process.service.FunctionService;
import com.lyx.process.service.FunctionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppTests
{
    @Autowired
    @Qualifier("functionServiceImpl")
    private FunctionServiceImpl functionService;

	@Test
	void contextLoads()
    {
	}
}
