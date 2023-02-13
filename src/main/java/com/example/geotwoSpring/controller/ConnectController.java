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

import java.sql.SQLException;
import java.util.ArrayList;
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
    public String create(UserForm userForm) {
        UserDto user = new UserDto();
        user.setHost(userForm.getHost());
        user.setUserNm(userForm.getUserNm());
        user.setPort(userForm.getPort());
        user.setSid(userForm.getSid());
        user.setUserPW(userForm.getUserPW());
        user.setDbType(userForm.getDbType());

        System.out.println("host : "+user.getHost());
        System.out.println("port : "+user.getPort());
        System.out.println("sid : "+user.getSid());
        System.out.println("UserNm : "+user.getUserNm());
        System.out.println("UserPw : "+user.getUserPW());
        System.out.println("DBType : "+user.getDbType());

        try{
            if(cxService.connectDB(user)){
                return "/cxSuccess";
            }else{
                throw new Exception();
            }

        }catch (Exception e){
            return "/cxError";
        }
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
        ArrayList<ColumnInfo> columnInfos = cxService.getTableInfo(tableNm);
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
