package edu.kh.jdbc.common;

import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

public class CreateXMLFile {

	public static void main(String[] args) {

		//XML (extensible Markup Language) :단순화된 데이터 기술 형식
		
		//XML에 저장되는 데이터 형식 : key : value 형식 (MAP)
		//-> KEY, VALUE 모두 String (문자열) 형식
		//properties : 
		
		//XML 파일을 읽고 ,쓰기 위한 IO 관련된 클래스 필요
		//properties : 컬렉션 객체
		//-> 맵의 후손
		//-> key, value 모두 String (문자열 형식)
		//-> XML파일을 읽고 쓰는데 특화된 메서드 제공
	
		
		try {
			Scanner sc = new Scanner(System.in);
			
			//properties 생성
			Properties prop = new Properties(); 
			
			System.out.print("생성할 파일 이름 :");
			String fileName = sc.next();
			
			//FileOutputStream 생성
			FileOutputStream fos =new FileOutputStream(fileName+".xml");
			
			//propterties 객체를 이용해서 XML파일 생성
	         prop.storeToXML(fos, fileName+".xml file!!!");
			System.out.println(".xml 파일 생성 완료 !");
			
		} catch (Exception e) {
			System.out.println("xml 파일 생성 중 예외 발생 : ");
			e.printStackTrace();
		}
		
	}

}
