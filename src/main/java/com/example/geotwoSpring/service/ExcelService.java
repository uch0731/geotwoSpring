package com.example.geotwoSpring.service;

import com.example.geotwoSpring.dto.ColumnInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ExcelService {
    private String uploadPath = "C:\\Users\\GEOTWO\\Desktop\\유창차라라" + ".xlsx";;

    public ArrayList<ArrayList<String>> readExcel(MultipartFile file){

        ArrayList<ArrayList<String>> data = new ArrayList<>();
        try {
            FileInputStream fis = (FileInputStream) file.getInputStream();
            XSSFWorkbook workBook = new XSSFWorkbook (fis);

            XSSFSheet sheet = workBook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();

            for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
                ArrayList<String> temp = new ArrayList<>();
                XSSFRow row = sheet.getRow(rowIndex);

                if (row != null) {
                    int cells = row.getPhysicalNumberOfCells();

                    for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
                        XSSFCell cell = row.getCell(columnIndex);
                        String value = "";

                        switch (cell.getCellType()) {
                            case NUMERIC:
                                if( DateUtil.isCellDateFormatted(cell)) {
                                    Date date = cell.getDateCellValue();
                                    value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                                }
                                else {
                                    value = (int)cell.getNumericCellValue() + "";
                                }
                                break;
                            case STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case BLANK:
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }
                        temp.add(value);
                    }
                    data.add(temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void createExcelFromTable(DataBaseService dbService)
            throws SQLException, IOException {

        ArrayList<ArrayList<String>> data = dbService.selectAllFromTable();
        ArrayList<ColumnInfo> columnInfo = dbService.getTargetTable().getColumnInfo();

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
