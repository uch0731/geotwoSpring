package com.example.geotwoSpring.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//txt 파일 만들기
public class CreateTxt {
    private static CreateTxt instance;
    private Date date;

    private CreateTxt(){
        this.date = new Date(System.currentTimeMillis());
    }

    //싱글톤 방식
    public static CreateTxt getInstance() {
        if (instance == null) {
            synchronized(CreateTxt.class) {
                instance = new CreateTxt();
            }
        }
        return instance;
    }

    //error txt 만들기
    public void createErrorTxt(int errorRow, int errorCol) throws IOException {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-z");
        String uploadTxtPath = "C:\\Users\\GEOTWO\\Desktop\\error_" + formatter.format(date) +"_log.txt";
        String errorPoint = "행: " + errorRow + " 열: " + errorCol;

        File file = new File(uploadTxtPath);
        if(!file.exists()){
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(errorPoint);
        writer.newLine();
        writer.flush();
        writer.close();
    }
}
