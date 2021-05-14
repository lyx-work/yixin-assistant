package com.lyx.process.service;

import org.springframework.http.ResponseEntity;

public interface FunctionService
{
    public ResponseEntity export(String tokenStr);
}