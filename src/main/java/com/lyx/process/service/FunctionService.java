package com.lyx.process.service;

import org.springframework.http.ResponseEntity;

public interface FunctionService
{
    ResponseEntity export(String tokenStr);
}