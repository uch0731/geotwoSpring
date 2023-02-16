package com.example.geotwoSpring.dto;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DatabaseType {
    //1521 하고 orcl도 입력받아야함
    //총 유알엘을 스트링으로 만들고 입력받은 부분을 리플레이스

    ORACLE("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@[HOST]:[PORT]:[SID]"),
    MYSQL(" "," "),
    MSSQL(" "," ");


    private final String driver;
    private final String url;

    DatabaseType(String driver, String startUrl){
        this.driver = driver;
        this.url = startUrl;
    }


    public String getDriver(){
        return driver;
    }

    public String getUrl(){
        return url;
    }
}
