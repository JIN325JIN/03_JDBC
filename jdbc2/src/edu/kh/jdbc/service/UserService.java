package edu.kh.jdbc.service;

import java.sql.Connection;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

//(MOdel 중 하나) Service : 비즈니스 로직을 처리하는 계층
//데이터를 가공하고 트랜잭션 (commit, rollback) 관리 수행


public class UserService {

	
	//필드
	private UserDAO dao = new UserDAO();

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
	
	
	
	
	
	//메서드
	
	
	
	
	
}
