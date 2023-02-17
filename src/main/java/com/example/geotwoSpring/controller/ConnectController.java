package com.example.geotwoSpring.controller;

import oracle.jdbc.proxy.annotation.Post;
import com.example.geotwoSpring.dto.ColumnInfo;
import com.example.geotwoSpring.dto.UserDto;
import com.example.geotwoSpring.service.ConnectService;
import com.example.geotwoSpring.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ConnectController {

    private final ExcelService excelService;
    private final ConnectService cxService;

    @Autowired
    public ConnectController(ConnectService cxService, ExcelService excelService) {
        this.cxService = cxService;
        this.excelService = excelService;
    }

    @GetMapping("/")
    public String connectController(Model model){

        return "connect";
    }

    @PostMapping("/connect")
    @ResponseBody
    public HashMap<String, String> makeConnect( UserDto user){
        System.out.println(user.getUserNm());
        HashMap<String, String> result = new HashMap<>();
        try{
            if(cxService.connectDB(user)){
                result.put("status","success");
            }else{
                throw new Exception();
            }

        }catch (Exception e){
            result.put("status","fail");
        }
        return result;
    }

    @GetMapping("/success")
    public String successController(Model model){
        return "/cxSuccess";
    }

    @PostMapping(value = "/show")
    public String showTable(String schema, Model model) throws SQLException {
        List<String> tables = cxService.getTableNameList(schema);
        System.out.println(tables);
        model.addAttribute("tables", tables);
        return "/showTable";
    }

    @PostMapping(value = "/showCol")
    public String showColumn(String tableNm, Model model) throws SQLException {
        List<ColumnInfo> columnInfos = cxService.getTableInfo(tableNm);
        System.out.println(tableNm);
        model.addAttribute("columnInfos", columnInfos);
        return "/showColumn";
    }


    @GetMapping(value = "/showData")
    public String showData(Model model) throws SQLException {
        System.out.println("start");
        ArrayList<ArrayList<String>> data = cxService.selectAllFromTable();
        System.out.println(data);
        model.addAttribute("col",data.get(0));
        data.remove(0);
        model.addAttribute("data", data);
        return"/showDataList";
    }

    @GetMapping(value = "/deleteData")
    public String deleteData(Model model) throws SQLException {
        System.out.println("delete");
        cxService.deleteData();
        ArrayList<ArrayList<String>> data = cxService.selectAllFromTable();
        System.out.println(data);
        model.addAttribute("col",data.get(0));
        data.remove(0);
        model.addAttribute("data", data);
        return"/showDataList";
    }

    @GetMapping(value = "/createExcel")
    public String createExcel(Model model) throws SQLException, IOException {
        System.out.println("create");
        excelService.createExcelFromTable(cxService);
        return"/showDataList";
    }

    @PostMapping(value = "/inputData")
    @ResponseBody
    public HashMap<String, String> inputData(@RequestParam("file") MultipartFile file) throws SQLException, IOException {
        HashMap<String, String> result = new HashMap<>();
        boolean fail = cxService.insertIntoTable(excelService.readExcel(file));
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
