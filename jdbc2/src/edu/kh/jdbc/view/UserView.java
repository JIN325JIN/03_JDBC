package edu.kh.jdbc.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.dto.User;
import edu.kh.jdbc.service.UserService;

//view : 사용자와 직접 상호작용하는 화면 (UI)담당, 입력을 받고 결과를 출력하는 역할


public class UserView {

	//필드
	private Scanner sc = new Scanner(System.in);
	private UserService service = new UserService();
	
	//메서드
	
	/**
	 * JDBCTemplate 사용 테스트
	 */
	public void test() {
		//입력된 ID와 일치하는 USER정보 조회
		System.out.println("ID 입력 :");
		String input = sc.next();
		
		//서비스 호출 후 결과 반환 받기
		User user = service.selectId(input);
		
		//결과에 따라 사용자에게 보여줄 응답 화면 결정
		if(user==null) {
			System.out.println("없어요");
		}else {
			System.out.println(user);
		}
	}
	
	/** User 관리 프로그램 메인 메뉴
	 */
	public void mainMenu() {
		
		int input = 0;
		
		do {
			try {
				
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. User 등록(INSERT)");
				System.out.println("2. User 전체 조회(SELECT)");
				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("7. User 등록(아이디 중복 검사)");
				System.out.println("8. 여러 User 등록하기");
				System.out.println("0. 프로그램 종료");
				
				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거
				
				switch(input) {
				case 1: insertUser(); break;
				case 2: selectAll(); break;
				case 3: selectName(); break;
				case 4: selectUser(); break;
				case 5: deleteUser(); break;
				case 6: updateName(); break;
				case 7: insertUser2(); break;
				case 8: multiInsertUser(); break;
				
				case 0 : System.out.println("\n[프로그램 종료]\n"); break;
				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				
				System.out.println("\n-------------------------------------\n");
				
			} catch (InputMismatchException e) {
				// Scanner를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***잘못 입력 하셨습니다***\n");
				
				input = -1; // 잘못 입력해서 while문 멈추는걸 방지
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거
				
			} catch (Exception e) {
				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
				e.printStackTrace();
			}
			
		}while(input != 0);
		
	} // mainMenu() 종료




	

	/**
	 * 1.user 등록 관련 view
	 * @throws Exception 
	 */
	private void insertUser() throws Exception {
		System.out.println("\n====1. User 등록 ====\n");
		
		System.out.print("ID : ");
		String userId = sc.next();
		
		System.out.print("PW : ");
		String userPw = sc.next();
		
		System.out.print("Name : ");
		String userName = sc.next();
		
		//입력받은값 3개를 한번에 묶어서 전달 할 수 있도록 
		//Userdto객체를 생성한 후에 필드의 값을 세팅
		
		User user = new User();
		
		//setter 이용
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		//서비스 호출(insert) 후 결과 반환 (int, 행의 갯수 )받기
		int result = service.insertUser(user);
		
		//반환된 결과에 따라 출력할 내용을 선택
		if(result>0) {
			System.out.println("\n"+ userId+"사용자가 등록되었습니다.\n");
		}else {
			System.out.println("***등록 실패***");
		}
	}

	/**2.user 전체 조회 관련 view (select)
	 * @throws Exception 
	 * 
	 */
	private void selectAll() throws Exception {
		System.out.println("\n===2.user 전체 조회 ===\n");
		
		//서비스 호출 (select)후 결과 (List<user> 반환받기
		List <User> userList = service.selectAll();
		
		//결과에 따라 처리하기
		
		//1)조회결과가 없을 경우
		if(userList.isEmpty()) {
			System.out.println("\n===조회결과가 없습니다 .===\n");
			return;
		}
		
		//2)있을경우 향상된 포문 이용해서 user list에 있는 user객체 출력
		
		
		for(User user : userList) {
			System.out.println(user);
		}
		
	}

	/**3.User 중 이름에 검색어가 포함된 회원 조회
	 * 
	 */
	private void selectName() throws Exception{
		System.out.println("\n===User중 이름에 검색어가 포함된 회원 조회===");
		
		System.out.print("검색어 입력 : ");
		String keyword = sc.next();
		
		//서비스 호출 후 결과 반환 하기
		List<User> searchList = service.selectName(keyword);
		
		
		if(searchList.isEmpty()) {
			System.out.println("검색 결과 없음");
			return ;
		}
		for(User user:searchList) {
			System.out.println(user);
		}
		
	}

	/**4. User_no를 입력받아 일치하는 User 조회
	 * @throws Exception 
	 * 
	 * 
	 */
	private void selectUser() throws Exception {
		System.out.println("\n4.User_no를 입력받아 일치하는 User 조회");
		System.out.println("사용자번호 입력 : ");
		int input = sc.nextInt();
		
		//사용자 번호 == pk ==즉 중복이 있을수 없다.
		//== 일치하는 사용자가 있다면 딱 1행만 조회
		//-> 1행의 조회결과를 담기위해서 user dto 객체 1개 사용
		User user = service.selectUser(input);
		if(user ==null) {
			System.out.println("User_No가 일치하는 회원 없음");
			return;
		}
		System.out.println(user);
		
	}

	/**5. User_no를 입력받아 일치하는 User 삭제
	 * 
	 */
	private void deleteUser() throws Exception{
		System.out.println("\nUser_No를 입력받아 일치하는 User삭제 \n");
	
		System.out.println("삭제할 사용자 번호 입력 :");
		int input = sc.nextInt();
	
		int result = service.deleteUser(input);
		
		if(result>0) {
			System.out.println("삭제 성공");
			
		}else {
			System.out.println("사용자 번호가 일치하는 User 가 존재하지 않는다 .");
		}
	
	
	}

	/**6. ID,PW가 일치하는 회원 (select)이 있을경우 이름 수정 update
	 * 
	 */
	private void updateName() throws Exception{
		
		System.out.println("\n6. ID,PW가 일치하는 회원이 있을경우 이름 수정");
		
		System.out.print("ID : ");
		String userId = sc.next();
		
		System.out.print("PW : ");
		String userPw = sc.next();
		
		
		//입력 받은 ID,PW가 일치하는 회원이 존재하는지 조회 (select)
		int userNo = service.selectUserNo(userId,userPw);
		
		
		if(userNo==0) {//조회결과 없음
			System.out.println("아이디, 비밀번호가 일치하는 사용자가 없음");
			return;
		}
		//조회결과 있을때
		System.out.print("수정할 이름을 입력하세요 : ");
		String userName = sc.next();
		
		//위에서 조회된 회원(user_no) 이름 수정 서비스 호출 후 결과 반환받기
		//서비스 호출 update 후 결과 반환 int 받기
		
		int result = service.updateName(userName,userNo);
		
		if(result>0) System.out.println("수정 성공!!");
		else System.out.println("수정 실패.....");
		
		
	}


	/**7. user등록 (아이디 중복검사)
	 * @throws Exception 
	 * 
	 */
	private void insertUser2() throws Exception {
		System.out.println("\n7.user등록 (아이디 중복검사) ");
	
		String userId = null;//입력된 아이디를 저장할 변수
		
		while(true) {
			System.out.print("ID: ");
			userId= sc.next();
			
			//입력받은 userId가 중복인지 검사하는 서비스 (select)호출 후 결과반환(인트형)받기
			// 중복 ==1, 아니면 ==0 
			int count = service.idCheck(userId);
					
			if(count ==0) {
				System.out.println("사용가능한 아이디 입니다.");
				break;
			}
			System.out.println("이미 사용중인 아이디 입니다. 다시 입력하십시오");
			
		}
		//pw,name입력받기
		System.out.print("PW : ");
		String userPw = sc.next();
		
		System.out.print("Name : ");
		String userName = sc.next();
		
		//입력받은값 3개를 한번에 묶어서 전달 할 수 있도록 
		//Userdto객체를 생성한 후에 필드의 값을 세팅
		
		User user = new User();
		
		//setter 이용
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		//서비스 호출(insert) 후 결과 반환 (int, 행의 갯수 )받기
		int result = service.insertUser(user);
		
		//반환된 결과에 따라 출력할 내용을 선택
		if(result>0) {
			System.out.println("\n"+ userId+"사용자가 등록되었습니다.\n");
		}else {
			System.out.println("***등록 실패***");
		}
	
	
	}

	/**8.여러 user등록하기 
	 * @throws Exception 
	 * 
	 */
	private void multiInsertUser() throws Exception {
		
		
		/*등록할 user 수 : 2
		 * 1번째 userId = user100
		 * -> 사용 가능한 아이디 입니다.
		 * 1번째 userpw : pass100
		 * 1번째 user name : 유저백
		 * ---------------------------
		 * 2번째 userId = user200
		 * -> 사용가능한 아이디 입니다.
		 * 2번째 userPW: pass200
		 * 2번째 userName: 유저이백 
		 * 
		 * --전체 삽입 성공해야 성공
		 * 그 외에는 삽입 실패.
		 * 
		 * 
		 * */
		
		System.out.println("\n==8.여러 user 등록하기 ===\n");
		System.out.println("등록 할 user 수 : ");
		
		int input = sc.nextInt();
		sc.nextLine();//버퍼에 남은 개행문자 제거 
		
		//입력 받은 회원 정보를 저장 할 List객체 생성
		List<User>userList = new ArrayList<User>();
		
		for(int i =0; i <input;i++) {
			
			String userId = null;//입력된 아이디를 저장할 변수
			
			while(true) {
				System.out.print((i+1) +"번째 User ID: ");
				userId= sc.next();
				
				//입력받은 userId가 중복인지 검사하는 서비스 (select)호출 후 결과반환(인트형)받기
				// 중복 ==1, 아니면 ==0 
				int count = service.idCheck(userId);
						
				if(count ==0) {
					System.out.println("사용가능한 아이디 입니다.");
					break;
				}
				System.out.println("이미 사용중인 아이디 입니다. 다시 입력하십시오");
				
			}
			//pw,name입력받기
			System.out.print((i+1)+"번째 PW : ");
			String userPw = sc.next();
			
			System.out.print((i+1)+"번째 User Name : ");
			String userName = sc.next();
			
			System.out.println("--------------------------------------------");
			//입력 받은 값 3개를 한번에 묶어서 전달 할 수있도록
			//User 객체를 생성한 후 userList에 추가
			User user = new User();
			
			user.setUserId(userId);
			user.setUserPw(userPw);
			user.setUserName(userName);
			
			//user.list에 user 추가
			userList.add(user);

			
		}//for문 종료

		//입력 받은 모든 사용자를 insert하는 서비스 호출
		//-> 결과로 삽입 된 행의 갯수 반환
		
		int result = service.multiInsertUser(userList);
		
		if(result == userList.size()) {
			System.out.println("전체 삽입 성공!!");
		}else {
			System.out.println("삽입 실패...");
		}
	}
}
