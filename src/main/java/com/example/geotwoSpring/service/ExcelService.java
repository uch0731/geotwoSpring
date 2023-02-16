package com.example.geotwoSpring.service;

import com.example.geotwoSpring.dto.ColumnInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class ExcelService {
    private String uploadPath = "C:\\Users\\GEOTWO\\Desktop\\유창차라라" + ".xlsx";;
    private String readPath;

    public void createExcelFromTable(ConnectService cxService)
            throws SQLException, IOException {

        ArrayList<ArrayList<String>> data = cxService.selectAllFromTable();
        ArrayList<ColumnInfo> columnInfo = cxService.getTableInfo();

        XSSFWorkbook workBook = new XSSFWorkbook();
        Sheet xSheet = workBook.createSheet("1");;
        Row xRow;
        Cell xCell;

        File file = new File(uploadPath);
        FileOutputStream excel = new FileOutputStream(file);

        xRow = xSheet.createRow(0);

        //column 명 설정
        if(data.size() != 0){
            for(int i =0; i< columnInfo.size(); i++) {
                xCell = xRow.createCell(i);
                xSheet.setColumnWidth(i,10000);
                xCell.setCellValue(columnInfo.get(i).getName());
            }
        }

        //엑셀파일에 데이터 쓰기
        for(int i =1; i< data.size(); i++) {
            xRow = xSheet.createRow(i);
            for(int j =0; j<data.get(i).size(); j++) {
                String tempData = data.get(i).get(j);
                xCell = xRow.createCell(j);
                xCell.setCellValue(tempData);
            }
        }
        workBook.write(excel);
        if(excel != null) {
            excel.close();
        }
    }
}
