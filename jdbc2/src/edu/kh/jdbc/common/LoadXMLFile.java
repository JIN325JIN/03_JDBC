package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class LoadXMLFile {

	public static void main(String[] args) {

		//XML파일 읽어오기 (FileInputStream, Properties)
		try {

			//1. 프로퍼티즈 객체 생성
			Properties prop = new Properties();
			
			//driver.xml 파일을 읽기위한 InputStream 객체 생성
			FileInputStream fis = new FileInputStream("driver.xml");
			
			//연결된 driver.xml 파일에 있는 내용을 모두 읽어와
			//properties 객체에 K:V 형식으로 저장
			
			
			prop.loadFromXML(fis);
			
			//prop.getProperty("key") : key가 일치하는 속성값 (value 값) 을 얻어옴
			
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String userName = prop.getProperty("userName");
			String password = prop.getProperty("password");
			
			
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url,userName,password);
					
			System.out.println(conn);//oracle.jdbc.driver.T4CConnection@29176cc1
			
			
		} catch (Exception e) {
		
		e.printStackTrace();
		
		}
		
		/*왜 XML 파일을 이용해서 JDBC를 진행 하는가 ? 
		 * 
		 * 
		 * 1.DB 연결 정보 / 드라이버 정보 등 코드 중복 제거
		 * 2. 보안 측면 에서 별도 관리 필요!
		 * 3. 재 컴파일을 진행하지 않기 위해서
		 * 4. XML파일에 작성된 문자열 형태를 그대로 읽어오기 때문에 XML파일에 SQL문 작성시 다루기가 좀 더 편리해짐.
		 * 
		 * */
		
	}

}
