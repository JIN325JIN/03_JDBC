package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample2 {
public static void main(String[] args) {
		
		//입력 받은 급여보다 초과해서 받는 사원의 사번, 이름, 급여 조회하기
		
		//1.JDBC 객체 참조용 변수 선언
		Connection conn = null;// DB연결 정보 저장 객체
		Statement stmt = null; // SQL 수행, 결과 반환용 객체
		ResultSet  rs = null; // SELECT 수행 결과 저장용 객체 
		
		Scanner sc =null;// 키보드 입력용 객체 
		
		
		try {
		//2. DriverManager 객체를 이용해서 Connection 객체 생성
			//2-1 oracle JDBC  Driver 객체 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2-2 db연결 정보 작성
			String type = "jdbc:oracle:thin:@"; //드라이버의 종류
			
			String host = "localhost"; //DB서버 컴퓨터의 IP또는 도메인 주소, localhost ==현재 컴퓨터
			
			String port = ":1521";// 프로그램 연결을 위한 port번호 
			
			String dbName = ":XE";//DBMS이름 (XE = eXpress Edition)
			
			//jdbc:oracle:thin:@localhost:1521:XE
			
			String userName ="kh";//사용자 계정명
			String password = "kh1234";//계정 비밀번호 
			
			
			
			//2-3 db연결 정보와 driverManager를 이용해서 Connection 생성
			
			conn = DriverManager.getConnection(type+host+port+dbName,userName,password);
			System.out.println(conn);//oracle.jdbc.driver.T4CConnection@6973bf95
			
			//3. sql작성
			//입력받은 급여
			//int 형 변수 input 사용
			
			sc = new Scanner(System.in);
			System.out.print("급여 입력 : ");
			int input = sc.nextInt();
			
			
			String sql = "SELECT EMP_ID,EMP_NAME,SALARY FROM EMPLOYEE WHERE SALARY >"+input;
			
			//4. Statement 객체 생성 
			stmt = conn.createStatement();
			
			//5. Statement 객체를 이용하여 sql 수행 후 결과 반환 받기
			
			//executeQuery(): select실행, resultset반환
			//executeUpdate() : dml실행, 결과 행의 갯수 반환 (int)
			
			rs =stmt.executeQuery(sql);
			
			
			//6. 조회 결과가 담겨있는 resultset을 커서 이용해 1행씩 접근해 각행에 작성된 컬럼값 얻어오기
			//->while반복문으로 데이터 꺼내어 출력
			while(rs.next()) {
			
			String empId = rs.getString("EMP_ID");
			String empName =rs.getString("EMP_NAME");
			int salary = rs.getInt("SALARY");
				
				
				System.out.printf("사번 : %s / 이름 : %s / 급여 : %d 원 \n",empId,empName,salary);			
		
			}
		
		} catch (Exception e) {
			//최상위 예외 Exception 을 이용해서 모든 예외를 처리 (oop의 다형성 업캐스팅 덕분에 )
			e.printStackTrace();
		}finally {
		
			
			//7. 사용완료된 jdbc 객체 자원 반환 (close)
			//-> 생성된 역순으로 close!
		
				try {
					if(rs != null) rs.close();
					if(stmt!=null) stmt.close();
					if (conn!= null)conn.close();
					
					if(sc != null) sc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}	
	}
}
