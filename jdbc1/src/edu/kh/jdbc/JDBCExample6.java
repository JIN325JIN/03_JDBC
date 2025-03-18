package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample6 {

	public static void main(String[] args) {

		//아이디 비밀번호 이름 입력 받아 아이디랑 비밀번호가 (where절 )일치하는 사용자의 이름을 수정해라(update : dml) 

		
		//1. jdbc객체 참조변수 선언하기 + 키보드 입력용 객체인 sc 선언
		Connection conn = null;
		PreparedStatement pstmt = null;
		Scanner sc = null;
	
		try {
			//2. DriverManager 이용해서 커낵션 객체 생성
			//2-1 oracle드라이버 메모리에 로드
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2-2 db연결 정보 작성
			
			//	String type = "jdbc:oracle:thin:@"; //드라이버의 종류
			//String host = "localhost"; //DB서버 컴퓨터의 IP또는 도메인 주소, localhost ==현재 컴퓨터
			//String port = ":1521";// 프로그램 연결을 위한 port번호 
			//String dbName = ":XE";//DBMS이름 (XE = eXpress Edition)
			
			//위 구구절절을 한문장으로 줄이기 가능
			String url ="jdbc:oracle:thin:@localhost:1521:XE";
			
			String userName ="kh";//사용자 계정명
			String password = "kh1234";//계정 비밀번호 
			
			//2-3 db연결 정보와 driverManager를 이용해서 Connection 생성
			
			conn = DriverManager.getConnection(url,userName,password);
		
			//3.sql작성 + 오토커밋 끄기
			conn.setAutoCommit(false);
			// + DML 수행전에 해줘야 할것 !!!!
			//트랜잭션 !!!!!
			//-> 오토 커밋 끄기!!!
			//-> 개발자가 트랜잭션을 마음대로 제어하기 위해서
			//주체가 db가 아닌 개발자!!!!
			
			sc = new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String id = sc.nextLine();
			
			System.out.print("비밀번호 입력 : ");
			String pw = sc.nextLine();
			
			System.out.print("수정할 이름 입력 : ");
			String name = sc.nextLine();
			
			String sql = """
				UPDATE TB_USER
				SET USER_NAME = ?
				WHERE USER_ID = ?
				AND USER_PW = ?
					""";
			
			//4.PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//5. ? 위치홀더에 알맞은 값 대입
			
			//pstmt.set.자료형(순서, 대입할 값)
			pstmt.setString(1,name);
			pstmt.setString(2,id);
			pstmt.setString(3,pw);
			
			
			//6.sql insert수행후 결과(int) 반환 받기
			
			//executeQuery(): select 수행, resultset반환
			//executeUpdate (): dml 수행, 결과 행 갯수(int)반환
			//-> 보통 dml 실패시 0, 성공시 0 초과된 값이 반환된다.
			
			//pstmt에서 executeQuery().executeUpdate() : 매개변수 자리에 아무것도 없어야한다!!
			
			int result =pstmt.executeUpdate();//괄호안에sql넣으면 안됨!!!!!!
			
			//7. result값에 따른 결과 처리 + 트랜잭션 제어처리
			
			if(result>0) {// INSERT 성공시
			
				System.out.println("수정 성공!");
				conn.commit();// insert했다면 commit수행 => 영구반영
				
			}else {
				
				System.out.println("아이디 비밀번호 불일치");
				conn.rollback();
			}	
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			//8.사용한 jdbc객체 자원반환
			
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
				if(sc != null)	sc.close();
			} catch (Exception e) {
				e.printStackTrace();		
			}
		}
	}
}
