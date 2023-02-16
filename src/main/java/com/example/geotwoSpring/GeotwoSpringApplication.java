package com.example.geotwoSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class GeotwoSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeotwoSpringApplication.class, args);

//		String host = "172.16.119.93";
//		String port = "1521";
//		String sid = "orcl";
//
//		String id = "sol_test2";
//		String pw = "geotwo";
//
//		String schemaName = "SOL_TEST2";
////        String tableName = "SAC";
//		String tableName = "SAC2";
//
//		String readExcelPath = "C:\\Users\\GEOTWO\\Desktop\\유창차라라\\예술의전당error.xlsx";
////        String readExcelPath = "C:\\Users\\GEOTWO\\Desktop\\유창차라라\\예술의전당.xlsx";
//		String uploadExcelPath = "C:\\Users\\GEOTWO\\Desktop\\유창차라라" + ".xlsx";
//
//		String dbType = "oracle"; //데이터베이스 이름 적기
//
//		UserDto dbUser = new UserDto();
//		dbUser.setHost(host);
//		dbUser.setPort(port);
//		dbUser.setSid(sid);
//		dbUser.setUserNm(id);
//		dbUser.setUserPW(pw);
//		dbUser.setDbType(dbType);
//
//		DatabaseType type = DatabaseType.valueOf(dbUser.getDbType().trim().toUpperCase());
//		String dbUrl = type.getUrl().replace("[HOST]", dbUser.getHost())
//				.replace("[PORT]", dbUser.getPort()+"")
//				.replace("[SID]", dbUser.getSid());
//		try {
//			Class.forName(type.getDriver());
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException(e);
//		}
//
//		try {
//			Connection conn = DriverManager.getConnection(dbUrl, dbUser.getUserNm(), dbUser.getUserPW());
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}

	}

}
