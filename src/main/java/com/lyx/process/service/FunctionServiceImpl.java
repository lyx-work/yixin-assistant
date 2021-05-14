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
}