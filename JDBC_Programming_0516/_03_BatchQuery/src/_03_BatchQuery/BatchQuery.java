package _03_BatchQuery;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BatchQuery {
	static {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean isCommit = false;

		try {
			// new oracle.jdbc.driver.OracleDriver(); //여러개 만들 경우 사용, 각 객체 만드는 방식
			// Class.forName("oracle.jdbc.driver.OracleDriver"); //의존성을 떨어트리는 방식
			// DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver() );
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp", "bitcamp");
			if (con != null)
				System.out.println("연결성공");
			stmt = con.createStatement();
			
			/*
			 사용자가 입력하는 쿼리가 DML 종류나 DDL인지 
			 아니면 SELECT문인지 잘 모를 때는 
			 execute를 사용한다.
			 리턴값이 true면 ResultSet의 반환값이 있다는 의미
			 리턴값이 false면 count를 얻는다.
			
			String sql = "";
			if(stmt.execute(sql)) {
				rs = stmt.getResultSet();
			}else{
				uCount = stmt.getUpdateCount();
			}
			 */
			
			stmt.executeUpdate("CREATE TABLE test4(" + "id VARCHAR2(10)," + "password VARCHAR2(10))");

			/*
			 * JDBC는 DML 문장을 실행할 때 자동으로 COMMIT한다. 그러나 배치쿼리 같이 일련의 과정의 실행이 모두 보장되어야 하는 경우, 만일
			 * 중간에서 에러가 발생되면 실행된 부분까지 이미 커밋처리가 되기 때문에 ROLLBACK을 하지 못해서 문제가 발생할 수 있다. 이럴 경우는
			 * 차라리 트랜잭션이 완료 시 수동으로 COMMIT을 처리하는 것이 바람직하다.
			 */
			con.setAutoCommit(false);
			stmt.addBatch("INSERT INTO test4 " + "VALUES('aaa000', '0000')");
			stmt.addBatch("INSERT INTO test4 " + "VALUES('bbb111', '1111')");
			stmt.addBatch("INSERT INTO test4 " + "VALUES('ccc222', '2222')");
			stmt.addBatch("INSERT INTO test4 " + "VALUES('ddd444', '4444')");
			int[] uCounts = stmt.executeBatch();

			con.commit();
			isCommit = true; // 오류 발생안하면 true로 변해주고
			con.setAutoCommit(true); // 다시 원래대로 돌린다

			rs = stmt.executeQuery("SELECT * FROM test4");
			while (rs.next()) {
				String id = rs.getString("id");
				String password = rs.getString("password");
				System.out.println("id: " + id + ", " + "password: " + password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (isCommit == false)
					con.rollback(); // 오류 발생했으면 롤백
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
