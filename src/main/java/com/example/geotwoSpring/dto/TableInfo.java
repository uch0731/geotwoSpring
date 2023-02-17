package com.example.geotwoSpring.dto;
import java.util.ArrayList;

//table 정보
public class TableInfo {

    private String tableName;
    private ArrayList<ColumnInfo> columnInfo = new ArrayList<>();


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ArrayList<ColumnInfo> getColumnInfo() {
        return columnInfo;
    }

    public void setColumnInfo(ArrayList<ColumnInfo> columnInfo) {
        this.columnInfo = columnInfo;
    }
}
