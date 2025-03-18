package edu.kh.jdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


//import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와 클래스명, 메서드명만 작성해도 호출 가능하게 함.
import static edu.kh.jdbc.common.JDBCTemplate.*;
import edu.kh.jdbc.dto.User;

//(model 중 하나 )DAO : (Data Access Object)
//데이터가 저장된 곳에 접근하는 용도의 객체
//>DB에 접근하여 JAVA에서 원하는 결과를 얻기위해 SQL을 수행하고 결과를 반환 받는 역할

public class UserDAO {

	//필드
	//-DB 접근 관련한 JDBC 객체 참조 변수 미리 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	
	//conn은 어디에? : 트랜잭션제어 처리라서 service에서 사용할거임~~
	
	//메서드
	/** 전달 받은 Connection 을 이용해서 DB에 접근하여 전달받은 아이디 (input)와 일치하는 USER정보를 DB조회하기 
	 * @param conn : Service에서 생성한 Connection 객체
	 * @param input : View에서 입력받은 아이디 
	 * @return : 아이디가 일치하는 회원의 User 또는 null
	 */
	public User selectId(Connection conn, String input) {
		
		//1. 결과 저장용 변수 선언
		User user = null;
		
		try {
			//2. sql작성 
			String sql = "SELECT * FROM TB_USER WHERE USER_ID = ?";
			
			//3.preparedStatement객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//4.? (위치홀더)에 값 세팅 
			pstmt.setString(1, input);
			
			//5.SQL수행후 결과 반환 받기 
			rs = pstmt.executeQuery();
			
			//6. 조회 결과가 있을 경우 + 중복되는 아이디가 없다고 가정
			//-> 1행만 조회되기 때문에 while문 보다는 if를 사용하는게 효과적
			
			if(rs.next()) {
				//첫행에 데이터가 존재한다면 
				
				//각 컬럼에 값 얻어오기
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				
				//java.sql.Date
				Date enrollDate = rs.getDate("ENROLL_DATE");
				
				//조회된 컬럼값을 이용해서 User객체 생성 
				// 멍청한 누나 졸리다 집에 가고싶어 오늘 저녁은 칼국수 맛있겠다 무슨 칼국수? 멸치? 닭? 혜성칼국수는 둘 다 있지
				// 면 리필해달라고 하면 더 주는 걸로 기억하는데 지금은 잘 모르겠다 예전에는 달라고 하면 줬는데 진짜 맛있는데 누나도 나중에 가봐 
				
				user = new User(userNo,userId,userPw,userName,enrollDate.toString());
				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			
			//사용한 JDBC객체 자원 반환하기 
			//JDBCTemplate.close(rs);
			//JDBCTemplate.close(pstmt);
			
			
			//import static edu.kh.jdbc.common.JDBCTemplate.*;
			close(rs);
			close(pstmt);
			
			
			//Connection 객체는 생성된 Service에서 close!
		}	
		
		return user; // 결과 반환 (생성된 User 객체 또는 null)

	}



}
