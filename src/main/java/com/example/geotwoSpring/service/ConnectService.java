package com.example.geotwoSpring.service;

import com.example.geotwoSpring.dto.ColumnInfo;
import com.example.geotwoSpring.dto.DatabaseType;
import com.example.geotwoSpring.dto.TableInfo;
import com.example.geotwoSpring.dto.UserDto;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Table;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ConnectService {

    private String tableNm;
    private String schema;
    private Connection conn;
    private TableInfo targetTable = new TableInfo();

    public boolean connectDB (UserDto user) throws Exception {
        DatabaseType type = DatabaseType.valueOf(user.getDbType().trim().toUpperCase());
        String dbUrl = type.getUrl().replace("[HOST]", user.getHost())
                .replace("[PORT]", user.getPort()+"")
                .replace("[SID]", user.getSid());

        try {
            Class.forName(type.getDriver());
            System.out.println("드라이버 검색 성공");
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 검색 실패");
            return false;
        }

        try {
            this.conn = DriverManager.getConnection(dbUrl, user.getUserNm(), user.getUserPW());
            System.out.println("데이터베이스 연결 성공");
            return true;
        } catch (SQLException e) {
            System.out.println("데이터베이스 연결 실패(HOST,PORT,SID,ID,PW 확인)");
            return false;
        }

    }

    public ArrayList<String> getTableNameList(String schema) throws SQLException{
        this.schema = schema.trim().toUpperCase();
        ArrayList<String> tableNames = new ArrayList<>();
        ResultSet rs = conn.getMetaData().getTables(null, this.schema, null, null);
        while(rs.next()) {
            String tableNm = rs.getString("TABLE_NAME");
            tableNames.add(tableNm);
        }
        rs.close();

        return tableNames;
    }

    public ArrayList<ColumnInfo> getColumnInfo(String tableNm) throws SQLException {

        this.tableNm = tableNm.trim().toUpperCase();
        this.targetTable.setTableName(this.tableNm);
        ArrayList<ColumnInfo> columns = new ArrayList<>();
        ResultSet rs = conn.getMetaData().getColumns(null, schema, this.tableNm, "%");

        while(rs.next()) {
            ColumnInfo temp = new ColumnInfo();
            temp.setName(rs.getString("COLUMN_NAME"));
            temp.setType(rs.getString("TYPE_NAME"));
            temp.setSize(rs.getInt("COLUMN_SIZE"));
            columns.add(temp);
        }

        rs.close();
        this.targetTable.setColumnInfo(columns);
        return columns;
    }
    public ArrayList<ColumnInfo> getColumnInfo() throws SQLException {
        return getColumnInfo(this.tableNm);
    }
    public TableInfo getTargetTable(){
        return targetTable;
    }

    //DB connection 주기
    public void giveConnToDB(DataBaseService dbService){
        dbService.setConn(conn);
    }


    public void closeConnection() throws SQLException {
        try{
            if (conn != null
                    || conn.isClosed())
            {
                conn.close();
            }
        } catch(Exception e){
            // 로그
        }
    }
}
