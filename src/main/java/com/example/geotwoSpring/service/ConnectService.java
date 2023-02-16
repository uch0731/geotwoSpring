package com.example.geotwoSpring.service;

import com.example.geotwoSpring.dto.ColumnInfo;
import com.example.geotwoSpring.dto.DatabaseType;
import com.example.geotwoSpring.dto.UserDto;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class ConnectService {

    String tableNm;
    String schema;
    Connection conn;

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

    public ArrayList<ColumnInfo> getTableInfo(String tableNm) throws SQLException {
        this.tableNm = tableNm.trim().toUpperCase();
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
        return columns;
    }
    public ArrayList<ColumnInfo> getTableInfo() throws SQLException {
        return getTableInfo(this.tableNm);
    }
    public ArrayList<ArrayList<String>> selectAllFromTable() throws SQLException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<ColumnInfo> columns = getTableInfo(tableNm);
        ArrayList<String> colNm = new ArrayList<>();

        for (int i = 0; i < columns.size(); i++) {
            colNm.add(columns.get(i).getName());
        }
        data.add(colNm);

        String query = "SELECT * FROM " + tableNm;
        PreparedStatement pst =conn.prepareStatement(query);
        pst.setFetchSize(1000);
        ResultSet rs = pst.executeQuery();

        while(rs.next()) {
            ArrayList<String> temp = new ArrayList<>();
            for(int i =0; i< columns.size(); i++) {
                String tempData = rs.getString(i+1);
                temp.add(tempData);
            }
            data.add(temp);
        }

        rs.close();
        pst.close();
        return data;
    }

    public ArrayList<String> deleteData() throws SQLException {
        ArrayList<ColumnInfo> columns = getTableInfo(tableNm);
        ArrayList<String> colNm = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            colNm.add(columns.get(i).getName());
        }
        String query = "delete FROM " + tableNm;
        PreparedStatement pst =conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        rs.close();
        pst.close();
        return colNm;
    }
}
