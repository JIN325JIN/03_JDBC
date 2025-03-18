package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JDBCExample7 {

	public static void main(String[] args) {

		// EMPLOYEE	테이블에서 사번, 이름, 성별, 급여, 직급명, 부서명을 조회
		// 단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것
				
		// - 조건 1 : 성별 (M, F)
		// - 조건 2 : 급여 범위
		// - 조건 3 : 급여 오름차순/내림차순
				
		// [실행화면]
		// 조회할 성별(M/F) : F (upper)
		// 급여 범위(최소, 최대 순서로 작성) :
		// 3000000
		// 4000000
		// 급여 정렬(1.ASC, 2.DESC) : 2
				
		// 사번 | 이름   | 성별 | 급여    | 직급명 | 부서명
		//--------------------------------------------------------
		// 218  | 이오리 | F    | 3890000 | 사원   | 없음
		// 203  | 송은희 | F    | 3800000 | 차장   | 해외영업2부
		// 212  | 장쯔위 | F    | 3550000 | 대리   | 기술지원부
		// 222  | 이태림 | F    | 3436240 | 대리   | 기술지원부
		// 207  | 하이유 | F    | 3200000 | 과장   | 해외영업1부
		// 210  | 윤은해 | F    | 3000000 | 사원   | 해외영업1부
		
		
		//1.jdbc객체 참조변수 선언하기
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Scanner sc = null;
		
		try {
			//2. db연결 정보 작성하기
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url ="jdbc:oracle:thin:@localhost:1521:XE";
			String userName ="kh";//사용자 계정명
			String password = "kh1234";//계정 비밀번호 
			conn = DriverManager.getConnection(url,userName,password);
			
			//3.sql 작성 + 오토커밋 끄기
			conn.setAutoCommit(false);
			
			sc = new Scanner(System.in);
			
			System.out.print("조회할 성별 입력(M/F) :");
			String gender = sc.next().toUpperCase();
			
			System.out.print("급여 범위(최소 ,최대 순서) ");
			int min = sc.nextInt();
			int max = sc.nextInt();
			
			System.out.print("급여 정렬 (1:ASC, 2: DESC ): ");
			int order = sc.nextInt();
			
			String sql = """
					SELECT EMP_ID,EMP_NAME,DECODE(SUBSTR(EMP_NO,8,1),'1','M','2','F')AS GENDER,SALARY,JOB_NAME,NVL(DEPT_TITLE,'없음') AS "DEPT_TITLE"
					FROM EMPLOYEE
					JOIN JOB USING (JOB_CODE)
					LEFT JOIN DEPARTMENT ON (EMPLOYEE.DEPT_CODE = DEPARTMENT.DEPT_ID)
					WHERE SALARY BETWEEN ? AND ?
					AND DECODE(SUBSTR(EMP_NO,8,1),'1','M','2','F') = ?
					ORDER BY SALARY
					
						""";
			if(order==1) {
				
				sql +="ASC";
				
			}else if (order ==2) {
				sql +="DESC";;
			}
			//4.PSTMTSTATEMENT 객체 생성
			
			pstmt = conn.prepareStatement(sql);
			
			//5. ? 위치홀더에 알맞은 값 대입
			pstmt.setInt(1,min);
			pstmt.setInt(2,max);
			pstmt.setString(3,gender);
			
			//6.SQL 수행 후 결과 반환 받기
			rs =pstmt.executeQuery();
			
			//7. 커서를 이ㅣ용해서 한행씩 접근하여 컬럼값 얻어오기
			System.out.println("사번 | 이름   | 성별 | 급여    | 직급명 | 부서명");
			System.out.println("------------------------------------------------------");
			boolean flag = true;// 조회 결과가 없음, false :조회 결과 존재함
			
			while(rs.next()) {
				flag = false;//while문이 1회 이상 반복 함 = 조회결과 1행이라도 있다.
				
				String empId = rs.getString("EMP_ID");//컬럼명, 별칭, 조회된 컬럼 순서 가능
				String empName =rs.getString("EMP_NAME");
				String empgender = rs.getString("GENDER");
				int salary = rs.getInt("SALARY");
				String jobName = rs.getString("JOB_NAME");
				String deptTitle =rs.getString("DEPT_TITLE");
				
				
				System.out.printf("%-4s | %3s | %-4s | %7d | %-3s  | %s \n",
						empId, empName, gender, salary, jobName, deptTitle);
			}
			if(flag) {
				System.out.println("조회 결과가 없습니다.");
			}
		}		
		catch(Exception e){
			e.printStackTrace();
		}finally {
			
			try {
				//8.사용한 자원 반환
				if(pstmt!=null) pstmt.close();
				if(conn!= null) conn.close();
				if(sc != null) sc.close();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
