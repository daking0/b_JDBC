package _0514_hw;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*
 1. emp_test 테이블을 프로그래밍으로 만들고
  콘솔로부터 3명의 정보를 입력받고
  다시 검색해서 화면에 출력하세요

CREATE TABLE emp_test (
  eno varchar2(4),
  ename varchar2(10),
  job varchar2(6)
);


2. eno를 입력하면 해당 사원을 검색해서 출력하세요
 */
public class basic {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp","bitcamp");

			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();

			//테이블 삭제
			try {
				sb.setLength(0);
				sb.append("DROP TABLE emp_test");
				stmt.executeUpdate(sb.toString());
				System.out.println("DROP TABLE emp_test");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			//테이블 생성
			sb.setLength(0);
			sb.append("CREATE TABLE emp_test(" 
					 + " eno varchar2(4),");
			sb.append("ename varchar2(10),");
			sb.append("job varchar2(6))");

			// 3-2) stmt를 통해 query문 실행
			int updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("createCount: " + updateCount);

			// 3-2) 데이터 넣기
			//sb.setLength(0); 
			int s=0;
			for(int i=0; i<3 ; i++) {
				sb.setLength(0);
				System.out.println("사번: ");
				String eno1 =scan.next();
				System.out.println("이름: ");
				String name = scan.next();
				System.out.println("직업: ");
				String job = scan.next();
				
				sb.append("INSERT INTO emp_test ");
				sb.append("VALUES ("+"'"+eno1+"'");
				sb.append(", "+"'"+name+"'");
				sb.append(", "+"'"+job+"'"+")");
				updateCount = stmt.executeUpdate(sb.toString()); // 명령 보내짐
				System.out.println("INSERT COUNT : " + s);
				s++;
			}

			// 3-3) 검색하기
			sb.setLength(0);
			sb.append("SELECT * FROM emp_test");
			rs = stmt.executeQuery(sb.toString());
			System.out.println("입력");
			String eno = scan.next();
			while (rs.next()) {
				String check = rs.getString("eno");
				if(check.equals(eno)) {
					System.out.println("사번 : " + rs.getString("eno"));
					System.out.println("이름 : " + rs.getString("ename"));
					System.out.println("직업 : " + rs.getString("job"));
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
