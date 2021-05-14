package com.lyx;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExcelModel
{
    private String p1;

    private String p2;

    private String p3;

    private String p4;
}