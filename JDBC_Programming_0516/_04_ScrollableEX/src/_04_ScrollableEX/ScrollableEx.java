package _04_ScrollableEX;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScrollableEx {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp", "bitcamp");
			sql = "SELECT * FROM scrolltest ";
			pstmt =con.prepareStatement(sql, 
				   ResultSet.TYPE_SCROLL_SENSITIVE, //Ŀ���̵� ����� ����
				   ResultSet.CONCUR_UPDATABLE); //�����͸� �������� ���� ����
			rs = pstmt.executeQuery();
			
			//ó������ ������ �˻�
			System.out.println("next()======================");
			while(rs.next()) {
				System.out.println("name: "+ rs.getString(1) +
								   ", gender: "+rs.getString(2));
			}
			
			//���� �� �������ٰ� �ٽ� �Ž����ö󰣴�
			System.out.println("previous()===================");
			while(rs.previous()) { 
				System.out.println("name: "+ rs.getString(1) +
								   ", gender: "+rs.getString(2));
			}
			
			//ù ��° �� �˻�
			System.out.println("first()==+++=================");
			rs.first(); //ù ��° �����η� �Űܰ���
			System.out.println("name: "+ rs.getString(1) +
				        	   ", gender: "+rs.getString(2));
			
			//������ �� �˻�
			System.out.println("last()==+++=================");
			rs.last();
			System.out.println("name: "+ rs.getString(1) +
				        	   ", gender: "+rs.getString(2));
			
			//�̵� ��ġ ����
			System.out.println("absolute(1)==+++=================");
			rs.absolute(1); //ù ��° ������ �̵�
			System.out.println("name: "+ rs.getString(1) +
				        	   ", gender: "+rs.getString(2));
			
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
