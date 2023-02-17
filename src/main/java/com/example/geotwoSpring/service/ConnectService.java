package com.example.geotwoSpring.service;

import com.example.geotwoSpring.dto.ColumnInfo;
import com.example.geotwoSpring.dto.DatabaseType;
import com.example.geotwoSpring.dto.UserDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

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

    public boolean insertIntoTable(ArrayList<ArrayList<String>> data) throws SQLException, IOException {
        boolean error = false;
        ArrayList<ColumnInfo> columnInfo = getTableInfo();
        String s = " values (" + "?,".repeat(columnInfo.size() - 1) + "?)";

        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement("insert into " + tableNm + s);
        int[] columnSequence = new int[columnInfo.size()];

        //칼럼 이름으로 칼럼 순서 맞추기
        for (int i = 0; i < columnSequence.length; i++) {
            columnSequence[i] = data.get(0).indexOf(columnInfo.get(i).getName());
        }

        System.out.println(Arrays.toString(columnSequence));

        for (int i = 1; i < data.size(); i++) {
            for (int j = 0; j < columnSequence.length; j++) {
                try {
                    //type비교해서 맞으면 파라미터값 세팅, 틀리면 에러
                    switch(columnInfo.get(j).getType()){
                        case "VARCHAR2":
                            boolean isNumeric = data.get(i).get(columnSequence[j]).chars().allMatch(Character::isDigit);
                            //문자열에 정수 들어온경우 체크
                            if (isNumeric) {
                                throw new Exception("error");
                            } else {
                                pst.setString(j + 1, data.get(i).get(columnSequence[j]));
                            }
                            break;
                        case "NUMBER":
                            pst.setInt(j + 1, Integer.parseInt(data.get(i).get(columnSequence[j])));
                            break;
                        case "DATE":
                            pst.setDate(j + 1, java.sql.Date.valueOf(data.get(i).get(columnSequence[j])));
                            break;
                    }
                } catch (Exception e) { // 에러 나왔을 경우 error text 만들어주기
                    error = true; //error flag true로 바꾸기
                    CreateTxt errorTxt = CreateTxt.getInstance();
                    //실패 row and column 전달
                    errorTxt.createErrorTxt(i+1, columnSequence[j]+1);
                }
            }
            //error 발생시 insert문 add안함
            if(!error) {
                pst.addBatch();
                if ((i % 10) == 0) {
                    pst.executeBatch();
                    pst.clearBatch();
                }
            }
        }
        //error 발생시 커밋안함
        if(error) {
            System.out.println("ERROR ");
            conn.rollback();
        }else{
            System.out.println("Data Insert");
            pst.executeBatch();
            conn.commit();
        }

        pst.close();
        conn.setAutoCommit(true);
        return error;
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

    public void deleteData() throws SQLException {
//        ArrayList<ColumnInfo> columns = getTableInfo(tableNm);
//        ArrayList<String> colNm = new ArrayList<>();
//        for (int i = 0; i < columns.size(); i++) {
//            colNm.add(columns.get(i).getName());
//        }
        System.out.println(tableNm);
        String query = "DELETE FROM " + tableNm;
        PreparedStatement pst =conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();
        rs.close();
        pst.close();
//        return colNm;
    }
}
