package _06_ConnectionPool;
/*
 1. 스레드를 6개 생성하세요
   스레드는 ConnectionPool로부터 Connection객체를 요청하여 업무를 진행합니다.
   처음에 ConnectionPool은 초기 생성자에서 5개의 Connection객체를 만들어야 하며
   6번째 스레드가 요청하면 ConnectionPool은 다시 추가로 5개의 Connection 객체를 만들게 됩니다.

   1번째 스레드는 일반 화학을 수강하는 학생 중 성적이 가장 낮은 학생의 이름을 검색을 처리합니다
   2번째 스레드는 10번 부서 사원들과 급여 및 연봉이 동일한 타 부서 사원을 검색을 처리합니다
   3번째 스레드는 물리학과 학생의 과목별 기말고사 성적 테이블 생성을 처리합니다
   4번째 스레드는 일반화학 과목을 수강하는 학생의 성적을 4.5만점 기준으로 수정을 처리합니다
   5번째 스레드는 각 사원의 정보와 근무지를 employee테이블에 저장을 처리합니다
   6번째 스레드는 화학과와 물리학과 학생을 검색을 처리합니다

   각각 해당 번호의 스레드의 결과가 화면에 출력하도록 합니다.
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// 커넥션 풀을 사용해서 오라클에 쿼리를 전송한다.
public class hw{
	public static void main(String[] args) {
		ConnectionPool cp = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "bitcamp";
		String password = "bitcamp";
		int initCons =-1;
		int maxCons = -1;
		/* Singleton패턴으로 만든 클래스는 
		 * 생성자가 private 이므로 외부에서 객체를 생성할 수 없다
		cp = new ConnectionPool("","","",0,0);   =>not visible
		*/
		
		try {
			cp = ConnectionPool.getInstance(url, user, password, initCons, maxCons); // 최초로 하나 만들어진다
			con = cp.getConnection();
			
			pstmt = con.prepareStatement("SELECT * FROM dept");
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.print(rs.getString(1)+ ", ");
				System.out.print(rs.getString(2) + ", ");
				System.out.print(rs.getString(3) + ", ");
				System.out.println(rs.getString(4));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) cp.releaseConnection(con);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		cp.closeAll(); //더이상 모든걸 쓰지 않으니까 다 연결 끊는다(정상적 종료)
	}
}
