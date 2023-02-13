package com.example.geotwoSpring.dto;

public class UserDto {
    private String host;
    private int port;
    private String sid;
    private String userNm;
    private String userPW;
    private String dbType;
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        setPort(Integer.parseInt(port));
    }

    public void setPort(int port) {
        // 파라미터 체크
        this.port = port;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getSid() {
        return sid;
    }

    public String getUserNm() {
        return userNm;
    }

    public String getUserPW() {
        return userPW;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }


    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
