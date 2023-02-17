package com.example.geotwoSpring.controller;

import com.example.geotwoSpring.service.ConnectService;
import com.example.geotwoSpring.service.DataBaseService;
import com.example.geotwoSpring.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class ExcelController {
    private final ExcelService excelService;
    private final DataBaseService dbService;

    @Autowired
    public ExcelController(DataBaseService dbService, ExcelService excelService) {
        this.dbService = dbService;
        this.excelService = excelService;
    }

    @GetMapping(value = "/createExcel")
    public String createExcel(Model model) throws SQLException, IOException {
        System.out.println("create");
        excelService.createExcelFromTable(dbService);
        return"/showDataList";
    }
}
