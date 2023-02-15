package com.example.geotwoSpring.controller;

import com.example.geotwoSpring.dto.ColumnInfo;
import com.example.geotwoSpring.dto.UserDto;
import com.example.geotwoSpring.service.ConnectService;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ConnectController {

    private final ConnectService cxService;
    @Autowired
    public ConnectController(ConnectService cxService) {
        this.cxService = cxService;
    }

    @GetMapping("/")
    public String connectController(Model model){

        return "connect";
    }

    @PostMapping("/connect")
    @ResponseBody
    public HashMap<String, String> makeConnect(UserDto user){
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
        model.addAttribute("data", data);
        return"/showDataList";
    }
}
