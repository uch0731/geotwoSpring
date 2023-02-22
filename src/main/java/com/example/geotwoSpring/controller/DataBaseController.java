package com.example.geotwoSpring.controller;

import com.example.geotwoSpring.service.ConnectService;
import com.example.geotwoSpring.service.DataBaseService;
import com.example.geotwoSpring.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class DataBaseController {
    private final DataBaseService dbService;
    private final ExcelService excelService;

    @Autowired
    public DataBaseController(ExcelService excelService, DataBaseService dbService) {
        this.excelService = excelService;
        this.dbService = dbService;
    }

    @GetMapping(value = "/showData")
    public String showData(Model model) throws SQLException {
        System.out.println("start");
        ArrayList<ArrayList<String>> data = dbService.selectAllFromTable();
        System.out.println(data);
        model.addAttribute("tableNm", dbService.getTargetTable().getTableName());
        model.addAttribute("col",data.get(0));
        data.remove(0);
        model.addAttribute("data", data);
        return"/showDataList";
    }

    @GetMapping(value = "/deleteData")
    public String deleteData(Model model) throws SQLException {
        System.out.println("delete");
        dbService.deleteData();
        ArrayList<ArrayList<String>> data = dbService.selectAllFromTable();
        System.out.println(data);
        model.addAttribute("col",data.get(0));
        data.remove(0);
        model.addAttribute("data", data);
        return"/showDataList";
    }

    @PostMapping(value = "/inputData")
    @ResponseBody
    public HashMap<String, String> inputData(@RequestParam("file") MultipartFile file) throws SQLException, IOException {
        HashMap<String, String> result = new HashMap<>();
        boolean fail = dbService.insertIntoTable(excelService.readExcel(file));
        if(fail){
            result.put("status","fail");
        }else{
            result.put("status","success");
        }
        return result;
    }

    //json 방식
//    @PostMapping(value = "/inputData")
//    public String InputData(@RequestBody HashMap<String, String> data, Model model) throws SQLException, IOException {
//        System.out.println("input");
//
//        return"/showDataList";
//    }

}
