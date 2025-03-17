package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample5 {

	
	
	public static void main(String[] args) {

		//아이디, 비밀번호, 이름을 입력받아
		//TB_USER 테이블에 삽입 하기
		

		
		/*java.sql.PreparedStatement
		 * - sql 중간에 ? (위치홀더, placeholder )를 작성하여 
		 * ? 자리에 java값을 대입 할 준비가 되어있는 statement
		 * 
		 * 
		 * 장점 1: sql작성이 간단해짐
		 * 장점 2: ? 에 값 대입시 자료형에 맞는 형태의 리터럴로 대입됨!
		 * 
		 * 
		 * ex)String 대입 -> '값'(자동으로 ''추가)
		 * ex)int 대입 -> '값'
		 * 장점 3: 성능 , 속도에서 우위를 가지고 있음
		 * 
		 * preparedStatement 는 statement의 자식 
		 * 
		 * Statement : select . dml (insert,update,delete)
		 * preparedstatement -> select,dml 둘다 가능 
		 * 
		 * */
		
		
		//1.  jdbc객체 참조변수 선언하기
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select 구문이면 result set 필요한데, 
		//SELECT가 아니기 때문에 ResultSet 필요없음!
		
		Scanner sc = null;
		
		try {
			//2. 드라이버 매니저 이용해서 커낵션 객체 생성
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
		
			//3.sql작성
			
			sc = new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String id = sc.nextLine();
			
			System.out.print("비밀번호 입력 : ");
			String pw = sc.nextLine();
			
			System.out.print("이름 입력 : ");
			String name = sc.nextLine();
			
			String sql = """
				INSERT INTO TB_USER VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT )
					""";
			
			//4.PreparedStatement 객체 생성
			//-> 객체 생성과 동시에 sql담겨지게 됨
			//-> 미리 ?(위치홀더)에 값을 받을 준비를 해야되기 때문에...
			
			
			pstmt = conn.prepareStatement(sql);
			
			//5. 위치홀더에 알맞은 값 대입
			
			//pstmt.set.자료형(순서, 대입할 값)
			pstmt.setString(1,id);
			pstmt.setString(2,pw);
			pstmt.setString(3,name);
			//-> 여기 까지 작성하면 sql작성된 상태!
			
			// + DML 수행전에 해줘야 할것 !!!!
			//트랜잭션 !!!!!
			//-> 오토 커밋 끄기!!!
			//-> 개발자가 트랜잭션을 마음대로 제어하기 위해서
			//주체가 db가 아닌 개발자!!!!
			conn.setAutoCommit(false);
			
			//6.sql insert수행후 결과(int) 반환 받기
			
			//executeQuery(): select 수행, resultset반환
			//executeUpdate (): dml 수행, 결과 행 갯수(int)반환
			//-> 보통 dml 실패시 0, 성공시 0 초과된 값이 반환된다.
			
			//pstmt에서 executeQuery().executeUpdate() : 매개변수 자리에 아무것도 없어야한다!!
			
			int result =pstmt.executeUpdate();//괄호안에sql넣으면 안됨!!!!!!

			
			//7. result값에 따른 결과 처리 + 트랜잭션 제어처리
			
			if(result>0) {// INSERT 성공시
			
				System.out.println(name + "님이 추가되었습니다.");
				conn.commit();// insert했다면 commit수행 => 영구반영
			}else {//실패
				
				System.out.println("추가 실패");
				conn.rollback();//실패시 트랜잭션 롤백해서 비우기	
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
