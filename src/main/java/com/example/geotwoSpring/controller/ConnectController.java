package com.example.geotwoSpring.controller;

import com.example.geotwoSpring.dto.ColumnInfo;
import com.example.geotwoSpring.dto.UserDto;
import com.example.geotwoSpring.service.ConnectService;
import com.example.geotwoSpring.service.DataBaseService;
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

@Controller
public class ConnectController {

    private final DataBaseService dbService;
    private final ConnectService cxService;

    @Autowired
    public ConnectController(ConnectService cxService, DataBaseService dbService) {
        this.cxService = cxService;
        this.dbService = dbService;

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
                cxService.giveConnToDB(dbService);
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
        System.out.println(tableNm);
        List<ColumnInfo> columnInfos = cxService.getColumnInfo(tableNm);
        model.addAttribute("tableNm", tableNm);
        model.addAttribute("columnInfos", columnInfos);
        dbService.setTargetTable(cxService.getTargetTable());
        return "/showColumn";
    }
}
