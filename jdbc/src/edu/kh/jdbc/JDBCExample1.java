package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample1 {

	
		public static void main(String[] args) {
			
			/*JDBC : Java Data Base Connectivity
			 * 
			 * - 자바에서 DB에 연결 할수 있게 해주는 JAVA API (JAVA에서 제공하는 코드)
			 * -> Java.sql 패키지에 존재함.
			 * */ 
			
			
			//Java 코드를 이용해 Employee 테이블에서 사번, 이름, 부서코드, 직급코드, 급여 , 입사일 조회 후 이클립스 콘솔에 출력
			
			
			/*1. JDBC 객체 참조용 변수 선언 */
			
			//java.sql.Connection 
			//특정 DBMS 와 연결하기 위한 정보를 저장한 객체 
			//== 즉 DBeaber 에서 사용하는 DB연결과 같은 역할의 객체 
			//(DB의 서버 주소, 포트번호, DB이름, 계정명, 비밀번호)
			
			Connection conn = null;
			
			//java. sql.Statement
			//->역할 
			//1) 자바에서 작성된 SQL을 DB에 전달
			//2) DB에서 SQL 수행한 결과를 반환 받아옴 (다시 DB에서 데이터 싣고 JAVA로돌아옴) 
			Statement stmt = null;
			
			//java.sql.ResultSet
			//-SELECT 조회 결과를 저장 하는 객체
			ResultSet rs = null;
			
			
			/*2. DriverManager 객체를 이용해서 Connection 객체 생성하기 */
			//java.sql.DriverManager
			//-DB연결 정보와 JDBC 드라이버를 이요해서 원하는 DB와 연결 할 수 있는 Connection 객체를 생성하는 객체 
			
			
			//2-1 ) Oracle JDBC Driver 객체를 메모리에 로드하기 
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				
				//Class.forName("패키지명 + 클래스명");
				//-> 해당 클래스를 읽어 메모리에 적재
				//-> JVM이 프로그램 동작에 사용할 객체를 생성하는 구문
				
				//oracle.jdbd.driver.OracleDriver
				//-Oracle DBMS 연결 시 필요한 코드가 담겨있는 클래스
				//ojdbc 라이브러리 파일 내에 존재함.
				//Oracle 만들어서 제공하는 클래스
			
				
				//2-2)DB 연결 정보 작성
				String type = "jdbc:oracle:thin:@"; //드라이버의 종류
				
				String host = "localhost"; //DB서버 컴퓨터의 IP또는 도메인 주소, localhost ==현재 컴퓨터
				
				String port = ":1521";// 프로그램 연결을 위한 port번호 
				
				String dbName = ":XE";//DBMS이름 (XE = eXpress Edition)
				
				//jdbc:oracle:thin:@localhost:1521:XE
				
				String userName ="kh";//사용자 계정명
				String password = "kh1234";//계정 비밀번호 
				
				
				//2-3)DB연결 정보와 DriverManager를 이용해서 Connection 객체 생성
				conn = DriverManager.getConnection(type+host+port+dbName,userName,password);
				
				
				//Connection 객체가 잘 생성되었는지 확인 (객체 주소 반환)
				
				System.out.println(conn);//oracle.jdbc.driver.T4CConnection@6973bf95
				
				/*3. SQl 작성 */
				//!! 주의 사항!!
				//-> JDBC 코드에서 SQL작성시 세미콜론(;) 작성하면 안된다!!!
				//-> SQL명령어가 올바르게 종료되지 않았습니다 : 예외발생
				
				//EMPLOYEE테이블에서 사번,이름,부서코드,직급코드,급여,입사일 조회
				
				String sql ="SELECT EMP_ID,EMP_NAME,DEPT_CODE,JOB_CODE,SALARY,HIRE_DATE FROM EMPLOYEE";//길어서 엔터칠때는 공백주의하기!! 공백 필수임!!!
				
				/* 4.Statement 객체 생성*/
				stmt = conn.createStatement();
				//연결된 DB에 SQL을 전달하고 , 결과를 반환 받을 역할을 할 statement객체를 생성해둠
				
				/*5.Statement 객체를 이용해서 sql 수행후 결과 반환 받기 */
				
				//1.ResultSet Statement.executeQuery(sql)
				//-> sql이 select문 일때 결과로 ResultSet 객체 반환
				
				
				//2. int Statement.executeUpdate(sql); 
				//-> Sql이 DML(INSERT,UPDATE,DELETE)일때 실행 메서드
				//-> 결과로 int반환 ( 삽입, 수정, 삭제된. 행의 갯수 리턴)
				rs = stmt.executeQuery(sql);//쿼리를 수행한다.
				
				/*6. 조회결과가 담겨있는 resultSet을 커서(cursor)를 이용해서 1행씩 접근해 각 행에 작성된 컬럼 값 얻어오기*/
				
				
				//boolean ResultSet.next() : 커서를 다음행으로 이동 시킨 후 이동된 행의 값이 있으면 true 없으면 false 반환
				//맨 처음 호출시 첫번째 행 부터 시작 
				
				while(rs.next()) {// 우리 employee 테이블 23행짜리니까 24행 될때 false라서 반복 멈춤
					//자료형 ResultSet.get자료형 (컬럼명|컬럼 순서);
					//현재 행에서 지정된 컬럼의 값을 얻어와 반환
					//-> 지정된 자료형 형태로 값이 반환됨
					//(자료형을 잘못 지정하면 예외 발생)
					
					//[java]                           [db]
					//String 						char,Varchar2
					//int,long 						Number(정수만 저장된 컬럼)
					//float,double 					Number(정수+실수)
					//java.sql.Date 				Date
					
					String empId =rs.getString("EMP_ID");//varchar2니까 string
					String empName = rs.getString("EMP_NAME");
					String deptCode = rs.getString("DEPT_CODE");
					String jobCode = rs.getString("JOB_CODE");
					int salary = rs.getInt("SALARY");//salary는 number니까 이와 같은 예시임
					Date hireDate = rs.getDate("HIRE_DATE");//java.util이랑 헷갈리지 않게 주의하면서 spl!!!!
					
					System.out.printf("사번 : %s / 이름 : %s / 부서코드 : %s / 직급코드 : %s / 급여 : %d / 입사일 : %s\n" ,empId,empName,deptCode,jobCode,salary,hireDate.toString() );
				}
				
			} catch (ClassNotFoundException e) {
				System.out.println("해당 Class를 찾을 수 없습니다.");
				e.printStackTrace();
			}catch (SQLException e) {
				//SQL Exception : DB 연결과 관련된 모든 예외의 최상위 부모 
				e.printStackTrace();
			}finally {
				
				/*7. 사용 완료된 JDBC 객체 자원 반환 (close)*/
				//-> 자원 반환을 하지 않으면 DB와 연결된 Connection이 그대로 남아있어서 다른 클라이언트가 (ex.java 프로그램) 추가적으로 연결되지 못하는 문제가 발생 될 수 있다!!
				//->DBMS는 최대 Connection수 개수 제한을 하고 있기 때문에 
				
				try {
					
					//만들어진 역순으로 close 수행 권장
					if(rs!=null) rs.close();
					if(stmt != null) stmt.close();
					if(conn!=null) conn.close();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			


	}

}
