package edu.kh.jdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와 클래스명, 메서드명만 작성해도 호출 가능하게 함.
import static edu.kh.jdbc.common.JDBCTemplate.*;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dto.User;

//(model 중 하나 )DAO : (Data Access Object)
//데이터가 저장된 곳에 접근하는 용도의 객체
//>DB에 접근하여 JAVA에서 원하는 결과를 얻기위해 SQL을 수행하고 결과를 반환 받는 역할

/**
 * 
 */
/**
 * 
 */
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


	/**1.User 등록 DAO
	 * @param conn : DB연결 정보가 담겨있는 Connecntion 객체
	 * @param user : VIEW에서 입력받은 id,pw,name이 셋팅된 객체
	 * @return Insert결과행의 개수
	 * @throws Exception 
	 */
	public int insertUser(Connection conn, User user) throws Exception {
		
		
		//SQL 수행 중 발생하는 예외를 catch로 처리하지 않고, throws를 이용해서
		//호출부로 던져 처리 -> catch문 필요없다!
		
		//1.결과 저장용 변수 선언
		int result =0;
		
		try {
			//2.SQL작성
			String sql = """
					INSERT INTO TB_USER 
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT )
				
					""";
			
			//3.preparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//4.? 위치홀더 알맞은 값 대입 
			pstmt.setString(1,user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			//5. sql(insert)수행 (excuteupdate())후 결과(삽입된 행의 갯수)반환 받기
			
			result = pstmt.executeUpdate();
			
		}finally {
			//6. 사용한 jdbc객체 자원 반환
			
			close(pstmt);
			
		}
		//결과 저장용 변수에 저장된 값 반환
		return result;
	}

	/** 2. User 전체 조회 DAO
	 * @param conn 
	 * @return userList
	 * @throws Exception 
	 */
	public List<User> selectAll(Connection conn) throws Exception {

		
		//1. 결과 저장용 변수 선언
		List<User> userList = new ArrayList<User>();
		try {
			//2.SQL작성
			
			String sql = """
					SELECT USER_NO,USER_ID,USER_PW,USER_NAME,TO_CHAR(ENROLL_DATE,'YYYY"년"MM"월" DD"일') AS ENROLL_DATE
					FROM TB_USER
					ORDER BY USER_NO
					""";
			
			//3.preparedStatement 객체 생성
			pstmt =conn.prepareStatement(sql);
			
			
			//4.? 위치홀더 알맞은 값 대입 (없으면 패스 )
			//5. sql(insert)수행 (excutequery())후 결과(result set)반환 받기
			rs = pstmt.executeQuery();
			
			
			//6. 조회 결과를 1행씩 접근하여 컬럼 값 얻어오기
			//몇행이 조회될지 모른다! -> while
			//->1행 : if문 
			
			while(rs.next()) {
				
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_Name");
				String enrollDate = rs.getString("ENROLL_DATE");
				//java.sql.date 타입으로 값을 저장하지 않은 이유 
				//-> select 문에서 to_char 를 이용하여 문자열로 변환해 조회했기 때문에 
				
				
				//User객체 새로 생성하여 컬럼값 세팅하기 
				User user = new User(userNo,userId,userPw,userName,enrollDate);
				
				userList.add(user);
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}
		//조회 결과가 담긴 List 반환
		return userList;
	}

	/**3. 이름에 검색어가 포함되는 회원 모두 조회 DAO
	 * @param conn
	 * @param keyword
	 * @return 
	 */
	public List<User> selectName(Connection conn, String keyword) throws Exception{
		//결과 저장용 변수 선언
		List<User> searchList = new ArrayList<User>();
		
		try {
			//sql작성
			String sql = """
					SELECT USER_NO,USER_ID,USER_PW,USER_NAME,TO_CHAR(ENROLL_DATE,'YYYY"년"MM"월" DD"일') AS ENROLL_DATE
					FROM TB_USER
					WHERE USER_NAME LIKE '%'||?||'%'
					ORDER BY USER_NO
					""";
			//플레이스 홀더 쓸때 문자열 이어쓰기 조심하기 
			//'%?%'  는 물음표 포함하냐는 의미가 되어버림!!
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//위치홀더에 알맞은 값 세팅하기
			pstmt.setString(1,keyword);
			
			//DB수행 후 결과 반환 받기
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				int userNo= rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_Name");
				String enrollDate = rs.getString("ENROLL_DATE");
				
				User user = new User(userNo,userId,userPw,userName,enrollDate);
				
				searchList.add(user);
			}
		}finally {
			
			close(rs);
			close(pstmt);
		}
		return searchList;
	}

	/**4.User_no를 입력받아 일치하는 User조회 DAO
	 * @param conn
	 * @param input
	 * @return user객체 or null
	 * @throws Exception 
	 */
	public User selectUser(Connection conn, int input) throws Exception {
		
		User user = null;
		
		try {
			String sql = """
					SELECT USER_NO,USER_ID,USER_PW,USER_NAME,TO_CHAR(ENROLL_DATE,'YYYY"년"MM"월" DD"일') AS ENROLL_DATE
					FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, input);
			rs =pstmt.executeQuery();
			
			if(rs.next()) {
				
				int userNo= rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_Name");
				String enrollDate = rs.getString("ENROLL_DATE");
				
				user = new User(userNo,userId,userPw,userName,enrollDate);
			}
		}finally {
			
			close(rs);
			close(pstmt);
		}
		return user;
	}

	/** User NO를 입력받아 일치하는 User삭제 DAO
	 * @param conn
	 * @param input
	 * @return result
	 */
	public int deleteUser(Connection conn, int input) throws Exception {
		
		int result = 0;
		
		try {
		
			String sql ="""
					DELETE FROM TB_USER
					WHERE USER_NO = ?
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, input);
			result = pstmt.executeUpdate();
			
			
		}finally {
			
		close(pstmt);	
		}
		return result;
	}


	/**Id.Pw 가 일치하는 회원의 USER_NO 조회 DAO
	 * @param conn
	 * @param userId
	 * @param userPw
	 * @return userNo
	 */
	public int selectUser(Connection conn, String userId, String userPw) throws Exception {
		
		int userNo = 0;
		
		try {
			String sql ="""
					SELECT USER_NO
					FROM TB_USER
					WHERE USER_ID =?
					AND USER_PW =?
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, userId);
			pstmt.setNString(2, userPw);
			
			rs= pstmt.executeQuery();
			
			//조회된 행은 딱 1행이 나옴
	
			//조회된 1행이 있을 경우
			if(rs.next()) {
				userNo = rs.getInt("USER_NO");
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}
		
		return userNo; // 조회 성공, user_no, 실패 0반환
	}


	/**
	 * @param conn
	 * @param userName
	 * @param userNo
	 * @return result
	 * @throws Exception 
	 */
	public int updateName(Connection conn, String userName, int userNo) throws Exception {
		
		int result = 0;
		
		try {
			String sql ="""
					UPDATE TB_USER
					SET USER_NAME = ?
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,userName);
			pstmt.setInt(2,userNo);
		
			result= pstmt.executeUpdate();
			
		}finally{
		close(pstmt);	
		}
		
		return result;
	}


	public int idCheck(Connection conn, String userId) throws Exception  {
		
		int count =0 ;
		try {
			String sql = """
					SELECT COUNT(*) 
					FROM TB_USER
					WHERE USER_ID = ?
					""";
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, userId);
		
		rs = pstmt.executeQuery();
		if(rs.next()) {
			count = rs.getInt(1);//조회된 컬럼 순서번호를 이용해 컬럼값 얻어오기
		}
		
		
		}finally {
			
			close(rs);
			close(pstmt);
		}
		
		
		return count;
	}
}
