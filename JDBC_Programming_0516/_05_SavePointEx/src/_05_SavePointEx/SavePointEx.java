package _05_SavePointEx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

public class SavePointEx {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp", "bitcamp");

			con.setAutoCommit(false);

			String selectQuery = "SELECT id, total FROM savepoint " + "WHERE total > ?";
			String updateQuery = "UPDATE savepoint SET total = ? " + "WHERE id = ?"; // 앞에 ?가 1, 뒤에 ?가 2

			selectPstmt = con.prepareStatement(selectQuery);
			updatePstmt = con.prepareStatement(updateQuery);

			selectPstmt.setInt(1, 100); // 숫자 대입하면 varchar로 변환해서 들어간다
			rs = selectPstmt.executeQuery();

			// 최초의 복원데이터 상태를 저장해놓는다
			Savepoint save1 = con.setSavepoint(); // 첫 번째 시점

			while (rs.next()) {
				String id = rs.getString("id");
				int oldTotal = rs.getInt("total");
				int newTotal = oldTotal * 2;

				updatePstmt.setInt(1, newTotal); // 첫 번째 물음표
				updatePstmt.setString(2, id); // 두 번째 물음표에는 id
				updatePstmt.executeUpdate(); // 업데이트를 적용

				System.out.println("NEW Total of " + oldTotal + " is " + newTotal);
				if (newTotal >= 5000) {
					System.out.println("save1로 Rollback 됨...");
					con.rollback(save1); // 한 번만 두배로 된 값으로 복원
				}
			}

			System.out.println("===============================");

			selectPstmt.setInt(1, 100);
			rs = selectPstmt.executeQuery();

			Savepoint save2 = con.setSavepoint(); // 두 번째 시점 저장

			while (rs.next()) {
				String id = rs.getString("id");
				int oldTotal = rs.getInt("total");
				int newTotal = oldTotal * 2;

				updatePstmt.setInt(1, newTotal);
				updatePstmt.setString(2, id);
				updatePstmt.executeUpdate();

				System.out.println("New Total of " + oldTotal + " is " + newTotal);
				if (newTotal >= 5000) {
					System.out.println("save2로 Rollback 됨...");
					con.rollback(save2); // 한 번만 두배로 된 값으로 복원
				}
			}

			System.out.println("===============================");

			con.commit();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM savepoint");
			while (rs.next()) {
				String id = rs.getString("id");
				int total = rs.getInt("total");
				System.out.println("id: " + id + "m total: " + total);
			}

			con.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (selectPstmt != null)
					selectPstmt.close();
				if (updatePstmt != null)
					updatePstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
