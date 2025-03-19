package edu.kh.jdbc.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

//(MOdel 중 하나) Service : 비즈니스 로직을 처리하는 계층
//데이터를 가공하고 트랜잭션 (commit, rollback) 관리 수행


/**
 * 
 */
/**
 * 
 */
public class UserService {

	
	//필드
	private UserDAO dao = new UserDAO();

	
	
	
	
	
	//메서드
	/**전달 받은 아이디와 일치하는 USER 정보 반환 서비스 
	 * @param input (view 에서 입력된 id )
	 * @return 아이디가 일치하는 회원 정보가 담긴 User 객체, 없으면 null 반환 
	 */
	public User selectId(String input) {
		
		//서비스에서 메서드만들때 1. 커넥션
		
		
		//1.커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		//2. 데이터 가공 같은 비지니스 로직 ( 내가 하고싶은거) 없으면 넘어감
		//->매개변수 따로따로 가져오면  가공 같은 
		
		//3.DAO단의 메서드 호출 결과 반환 
		User user = dao.selectId(conn,input);
		
		
		//4. DML (commit // rollback)
		
		//5.다쓴 커넥션 자원 반환
		
		JDBCTemplate.close(conn);
		
		//6. 결과를 view 리턴

		return user;//null 말고 user로 리턴
	}

	/**1. user 등록 서비스
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public int insertUser(User user) throws Exception {
		
		//1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		//2. 데이터 가공(할게 없으면 넘어감)
		//스프링부트로 회원전용 게시판 프로그램 만들시 로그인.회원가입 
		
		//3.DAO메서드 호출 후 결과 반환 받기
		int result = dao.insertUser(conn,user);
		
		//4. DML 수행 (insert)수행 결과에 따라트랜잭션 제어 처리
		if(result>0) { //insert 성공
			JDBCTemplate.commit(conn);
		}else {
			//insert 실패
			JDBCTemplate.rollback(conn);
		}
		
		//5. Connection 반환
		JDBCTemplate.close(conn);
		
		
		//6. 결과 뷰쪽으로 반환
		
		return result;
	}

	/**2. User 전체 조회 서비스
	 * @return : 조회된 User들이 담긴 list
	 */
	public List<User> selectAll() throws Exception {
		//1.connection 생성
		Connection conn = JDBCTemplate.getConnection();
		//2. 데이터 가공
		
		//3.DAO 메서드 호출 (select호출)+ 결과반환 (List<user>)받기
		List<User> userList = dao.selectAll(conn);
		
		//4.Connection 반환
		JDBCTemplate.close(conn);//import static하면 바로 close 호출해도됨
		
		//5. 결과 반환
		return userList;
	}

	/** 3. User 중 이름에 검색어가 포함된 회원 조회 서비스 
	 * @param keyword : 입력한 키워드 
	 * @return searchList : 조회된 회원 리스트
	 */
	public List<User> selectName(String keyword) throws Exception {
		Connection conn= JDBCTemplate.getConnection();
		
		List<User> searchList = dao.selectName(conn,keyword);
		
		JDBCTemplate.close(conn);
		
		return searchList;
	}

	/** 4.User_No를 입력받아 일치하는 User 조회 서비스
	 * @param input
	 * @return user(회원 정보 또는 null)
	 */
	public User selectUser(int input) throws Exception{
		
		Connection conn = JDBCTemplate.getConnection();
		User user = dao.selectUser(conn,input);
		
		JDBCTemplate.close(conn);
		
		return user;
	}

	public int deleteUser(int input) throws Exception{
		
		Connection conn = JDBCTemplate.getConnection();
		int result = dao.deleteUser(conn,input);
		
		//결과에 따른 트랜잭션 처리 필요
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	/** ID,PW가 일치하는 회원의 USER_NO조회 서비스 
	 * @param userId
	 * @param userPw
	 * @return userNo
	 */
	public int selectUserNo(String userId, String userPw) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int userNo = dao.selectUser(conn, userId,userPw);
		JDBCTemplate.close(conn);
		
		return userNo;
	}

	/**userNo가 일치하는 회원의 이름 수정 서비스 
	 * @param userName
	 * @param userNo
	 * @return result
	 */
	public int updateName(String userName, int userNo) throws Exception  {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateName(conn,userName,userNo);
		
		
		if(result>0) JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		
		JDBCTemplate.close(conn);
		return result;
	}

	/**아이디 중복 확인 서비스 
	 * @param userId
	 * @return count
	 */
	public int idCheck(String userId) throws Exception  {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int count = dao.idCheck(conn,userId);
		
		JDBCTemplate.close(conn);
		
		return count;
	}

	/**userList에 있는 모든 user객체를 insert서비스 
	 * @param userList
	 * @return
	 * @throws Exception 
	 */
	public int multiInsertUser(List<User> userList) throws Exception {
		
		//다중 insert방법
		//1. sql을 이용한 다중 insert
		
		//2. java반복문을 이용한 다중 insert사용 (우리는 이거 사용)
		Connection conn = JDBCTemplate.getConnection();
		int count = 0;//삽입 성공한 행의 개수 count 
		
		
		//1행씩 삽입
		
		for(User user : userList) {
			int result = dao.insertUser(conn, user);
			count += result; //삽입 성공한 행의 개수를 count 누적
			
			
		}	
		//트랜잭션 제어처리 
		//전체 삽입 성공시 commit / 아니면 rollback ( 일부 삽입, 전체 실패)
		if(count== userList.size()) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);
		
		return count;
	}
	
	

	
}
