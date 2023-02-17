package com.example.geotwoSpring.controller;

import com.example.geotwoSpring.service.ConnectService;
import com.example.geotwoSpring.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ExcelController {
    private final ExcelService excelService;
    private final ConnectService cxService;

    @Autowired
    public ExcelController(ConnectService cxService, ExcelService excelService) {
        this.cxService = cxService;
        this.excelService = excelService;
    }
}
