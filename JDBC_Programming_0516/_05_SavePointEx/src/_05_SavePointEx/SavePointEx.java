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
			String updateQuery = "UPDATE savepoint SET total = ? " + "WHERE id = ?"; // �տ� ?�� 1, �ڿ� ?�� 2

			selectPstmt = con.prepareStatement(selectQuery);
			updatePstmt = con.prepareStatement(updateQuery);

			selectPstmt.setInt(1, 100); // ���� �����ϸ� varchar�� ��ȯ�ؼ� ����
			rs = selectPstmt.executeQuery();

			// ������ ���������� ���¸� �����س��´�
			Savepoint save1 = con.setSavepoint(); // ù ��° ����

			while (rs.next()) {
				String id = rs.getString("id");
				int oldTotal = rs.getInt("total");
				int newTotal = oldTotal * 2;

				updatePstmt.setInt(1, newTotal); // ù ��° ����ǥ
				updatePstmt.setString(2, id); // �� ��° ����ǥ���� id
				updatePstmt.executeUpdate(); // ������Ʈ�� ����

				System.out.println("NEW Total of " + oldTotal + " is " + newTotal);
				if (newTotal >= 5000) {
					System.out.println("save1�� Rollback ��...");
					con.rollback(save1); // �� ���� �ι�� �� ������ ����
				}
			}

			System.out.println("===============================");

			selectPstmt.setInt(1, 100);
			rs = selectPstmt.executeQuery();

			Savepoint save2 = con.setSavepoint(); // �� ��° ���� ����

			while (rs.next()) {
				String id = rs.getString("id");
				int oldTotal = rs.getInt("total");
				int newTotal = oldTotal * 2;

				updatePstmt.setInt(1, newTotal);
				updatePstmt.setString(2, id);
				updatePstmt.executeUpdate();

				System.out.println("New Total of " + oldTotal + " is " + newTotal);
				if (newTotal >= 5000) {
					System.out.println("save2�� Rollback ��...");
					con.rollback(save2); // �� ���� �ι�� �� ������ ����
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
