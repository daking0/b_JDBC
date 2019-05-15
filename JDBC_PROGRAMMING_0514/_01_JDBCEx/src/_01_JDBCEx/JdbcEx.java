package _01_JDBCEx;

/*1.jar을 포함한다(Add Externals Libraries)
2. import java.sql.*; 적는다
3. 오라클 드라이버 객체를 로딩한다(메모리에 올림)
4. Connecton 객체를 생성한다(오라클을 접속한다)
5. Statement 객체를 생성한다(오라클과 대화)
6. SQL문에 결과가 있다면(SELECT문 사용시)
   ResultaSet 객체를 생성한다(데이터 저장객체)
7. ResultSet -> Statement -> Connection 순서
	(즉, Open한 역순으로) Close()한다
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcEx {
	// static 초기화 영역
	// static 필드를 초기화 하는 곳
	// 가장 먼저 호출됨
	static { // static영역에서 static 초기화
		// 1)DriverManager 내부에서 사용할 Oracle 드라이버 객체를 생성해서 메모리에 로딩함
		// 프로그램에서 직접 접근은 하지 않으므로 클래스 변수에 대입하지 않았다.
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} // oracle의 드라이버를 메모리에 올린다 / db에서는 오류 날 확률이 높아서 try catch를 많이 쓴다.
	}// 여기서 안하고 main 첫 줄에서 하기도하지만 여기서 이렇게 하는게 더 강조되고 좋다(최초에 바로 실행됨)

	public static void main(String[] args) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 2)오라클에 접속을 한다
			/* Connection */ con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp",
					"bitcamp");
			// localhost 다른사람 ip 넣으면 된다 , bitcamp는 각 id와pw에 해당

			// 3) 오라클과 대화를 하기 위한 Statement 생성
			/* Statement */ stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();

			// 3-0) 실행마다 테이블 삭제
			try {
				sb.setLength(0); // 크기를 0으로 초기화
				sb.append("DROP TABLE test1");
				stmt.executeUpdate(sb.toString());
				System.out.println("DROP TABLE test1");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				// e.printStackTrace();
			}

			// 3-1)query문을 저장한 객체 생성 - 테이블 생성
			sb.setLength(0);
			sb.append("CREATE TABLE test1(" 
					 + "id VARCHAR2(10),");
			sb.append("age NUMBER)");

			// 3-2) stmt를 통해 query문 실행
			int updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("createCount: " + updateCount); // 연결된 bitcamp에 만들어진다

			// 3-2) 데이터 넣기
			sb.setLength(0); // 0으로 초기화
			sb.append("INSERT INTO test1 ");
			sb.append("VALUES ('aaa000'");
			sb.append(", 10)");
			updateCount = stmt.executeUpdate(sb.toString()); // 명령 보내짐
			System.out.println("INSERT COUNT : " + updateCount);

			//con.commit(); // 트랜잭션 완료 - 자바가 auto commit 한다

			// 3-3) 검색하기
			sb.setLength(0);
			sb.append("SELECT * FROM test1");
			/* ResultSet */ rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				System.out.println("id : " + rs.getString(1));
				System.out.println("age : " + rs.getString(2));
			}

			// 3-4) Update문 하기
			sb.setLength(0);
			sb.append("UPDATE test1 SET id = 'bbb000', ");
			sb.append("age=20 ");
			sb.append("WHERE id = 'aaa000'");
			updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("UPDATE COUNT : " + updateCount);

			// 3-5) 검색하기
			sb.setLength(0);
			sb.append("SELECT * FROM test1");
			rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				System.out.println("id : " + rs.getString(1));
				System.out.println("age : " + rs.getString(2));
			}

			// 3-6) 지우기
			sb.setLength(0);
			sb.append("DELETE FROM test1");
			updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("DELETE COUNT: " + updateCount);
			con.commit();

			// 3-7) 검색하기
			sb.setLength(0);
			sb.append("SELECT * FROM test1");
			rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				System.out.println("id : " + rs.getString(1));
				System.out.println("age : " + rs.getString(2));
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
