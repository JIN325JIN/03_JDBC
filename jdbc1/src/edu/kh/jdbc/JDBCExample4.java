package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {

	public static void main(String[] args) {

		//부서명을 입력 받아, 해당하는 부서에 근무하는 사원의 사번이름 직급명을 직급코드 오름차순으로 조회하도록
		//일치하는 부서가 없습니다. : 플래그 // 바로 리턴
		//sql 문자열 : 양쪽 ' 따옴표' 필요
		//총무부 입력 -> '총무부' 
		
		
		
		
		
		//1.JDBC 객체 참조용 변수 선언
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				Scanner sc = null;
				
				try {
					//2. DriverManager 객체를 이용해서 Connection 객체 생성
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
					
					
					//3. sql 작성
					sc = new Scanner(System.in);
					System.out.print("부서명 입력 : ");
					String dept = sc.next();
					

					//JAVA 13부터 지원하는 text block(""")문법
					//자동으로 개행 포함+ 문자열 연결이 처리됨
					//기존처럼 + 연산자로 문자열을 연결할 필요가 없음
					
					String sql = """
							SELECT EMP_ID, EMP_NAME, DEPT_TITLE, JOB_NAME
							FROM EMPLOYEE E
							JOIN JOB J ON(E.JOB_CODE =J.JOB_CODE)
							LEFT JOIN DEPARTMENT ON (DEPT_CODE = DEPT_ID)
							WHERE DEPT_TITLE =
							"""+ "\'"+dept +"\' ORDER BY J.JOB_CODE ";
					// JOIN에서 USING 써도되지만 더 정확한걸 추천
									
					//4. Statement 객체 생성 
					stmt = conn.createStatement();
					//5. Statement 객체를 이용하여 sql 수행 후 결과 반환 받기
					rs =stmt.executeQuery(sql);
					
					//6. 조회 결과가 담겨있는 resultset을 커서 이용해 1행씩 접근해 각행에 작성된 컬럼값 얻어오기
					//->while반복문으로 데이터 꺼내어 출력
					boolean flag = true;
					
					//조회 결과 있다면 false, 없다면 true
					
					while(rs.next()) {
						String empId = rs.getString("EMP_ID");
						String empName =rs.getString("EMP_NAME");
						String deptTitle =rs.getString("DEPT_TITLE");
						String jobCode =rs.getString("JOB_NAME");
					
							System.out.printf("사번 : %s / 이름 : %s / 부서 : %s / 직급 : %s \n",
									empId,empName,deptTitle,jobCode);			
							
						if(deptTitle!=null) flag=false;
					}
					
					if(flag) System.out.println("해당 하는 부서 : 없음");
					
				} catch (Exception e) {
					e.printStackTrace();		
				}finally {
					
					//7. 사용완료된 jdbc 객체 자원 반환 (close)
					//-> 생성된 역순으로 close!
					
					try {
						if(rs != null) rs.close();
						if(stmt!= null)stmt.close();
						if(conn!= null) conn.close();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
	}
}
