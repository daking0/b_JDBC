package _02_PreparedStatementEx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
Statement								    : �⺻
PreparedStatement extends Statement         : �ӵ����, �������� ��  / �̸� ĳ�� �޸𸮿� ����(������ �ٲ㼭 ����)
CallableStatement extends PreparedStatement : Ʈ����, ���ν��� 
 */

public class PreparedStatementEx {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Connection con = null; // ����
		PreparedStatement pstmt = null; // ��ȭ
		ResultSet rs = null; // �˻��� ������ ������ ����

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp", "bitcamp");
			StringBuffer sb = new StringBuffer();
			int uCount =0;
			
			//���̺� ����
			try {
				sb.setLength(0);
				sb.append("DROP TABLE aaa");
				pstmt = con.prepareStatement(sb.toString());
				uCount = pstmt.executeUpdate();
				System.out.println("DROP COUNT: "+ uCount);
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			
			//���̺� ����
			sb.setLength(0);
			sb.append("CREATE TABLE AAA(id VARCHAR2(10), ");
			sb.append("password VARCHAR2(10))");
			pstmt = con.prepareStatement(sb.toString()); //�ٷ� ���⼭ ã�Ƽ� �ӵ��� �������� (���� ���� �ݺ��� ���� pre�� ������)
			uCount = pstmt.executeUpdate();
			System.out.println("CREATE COUNT: "+ uCount);
			
			sb.setLength(0);
			sb.append("INSERT INTO aaa VALUES(?, ?)"); 
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, "chicken"); // VALUES(?, ?) ?�� 1�� ��ü
			pstmt.setString(2, "baseball");
			uCount = pstmt.executeUpdate();
			System.out.println("INSERT COUNT: "+uCount);
			con.commit();
			
			sb.setLength(0);
			sb.append("SELECT * FROM aaa");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				System.out.print("id: "+rs.getString(1));
				System.out.println(", password: " + rs.getString(2));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
