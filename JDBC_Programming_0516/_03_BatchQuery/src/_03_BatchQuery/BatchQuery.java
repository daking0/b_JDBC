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
			// new oracle.jdbc.driver.OracleDriver(); //������ ���� ��� ���, �� ��ü ����� ���
			// Class.forName("oracle.jdbc.driver.OracleDriver"); //�������� ����Ʈ���� ���
			// DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver() );
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp", "bitcamp");
			if (con != null)
				System.out.println("���Ἲ��");
			stmt = con.createStatement();
			
			/*
			 ����ڰ� �Է��ϴ� ������ DML ������ DDL���� 
			 �ƴϸ� SELECT������ �� �� ���� 
			 execute�� ����Ѵ�.
			 ���ϰ��� true�� ResultSet�� ��ȯ���� �ִٴ� �ǹ�
			 ���ϰ��� false�� count�� ��´�.
			
			String sql = "";
			if(stmt.execute(sql)) {
				rs = stmt.getResultSet();
			}else{
				uCount = stmt.getUpdateCount();
			}
			 */
			
			stmt.executeUpdate("CREATE TABLE test4(" + "id VARCHAR2(10)," + "password VARCHAR2(10))");

			/*
			 * JDBC�� DML ������ ������ �� �ڵ����� COMMIT�Ѵ�. �׷��� ��ġ���� ���� �Ϸ��� ������ ������ ��� ����Ǿ�� �ϴ� ���, ����
			 * �߰����� ������ �߻��Ǹ� ����� �κб��� �̹� Ŀ��ó���� �Ǳ� ������ ROLLBACK�� ���� ���ؼ� ������ �߻��� �� �ִ�. �̷� ����
			 * ���� Ʈ������� �Ϸ� �� �������� COMMIT�� ó���ϴ� ���� �ٶ����ϴ�.
			 */
			con.setAutoCommit(false);
			stmt.addBatch("INSERT INTO test4 " + "VALUES('aaa000', '0000')");
			stmt.addBatch("INSERT INTO test4 " + "VALUES('bbb111', '1111')");
			stmt.addBatch("INSERT INTO test4 " + "VALUES('ccc222', '2222')");
			stmt.addBatch("INSERT INTO test4 " + "VALUES('ddd444', '4444')");
			int[] uCounts = stmt.executeBatch();

			con.commit();
			isCommit = true; // ���� �߻����ϸ� true�� �����ְ�
			con.setAutoCommit(true); // �ٽ� ������� ������

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
					con.rollback(); // ���� �߻������� �ѹ�
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
