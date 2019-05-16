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
				   ResultSet.TYPE_SCROLL_SENSITIVE, //커서이동 양방향 가능
				   ResultSet.CONCUR_UPDATABLE); //데이터를 동적으로 갱신 가능
			rs = pstmt.executeQuery();
			
			//처음부터 끝까지 검색
			System.out.println("next()======================");
			while(rs.next()) {
				System.out.println("name: "+ rs.getString(1) +
								   ", gender: "+rs.getString(2));
			}
			
			//행이 쭉 내려갔다가 다시 거슬러올라간다
			System.out.println("previous()===================");
			while(rs.previous()) { 
				System.out.println("name: "+ rs.getString(1) +
								   ", gender: "+rs.getString(2));
			}
			
			//첫 번째 값 검색
			System.out.println("first()==+++=================");
			rs.first(); //첫 번째 값으로로 옮겨간다
			System.out.println("name: "+ rs.getString(1) +
				        	   ", gender: "+rs.getString(2));
			
			//마지막 값 검색
			System.out.println("last()==+++=================");
			rs.last();
			System.out.println("name: "+ rs.getString(1) +
				        	   ", gender: "+rs.getString(2));
			
			//이동 위치 지정
			System.out.println("absolute(1)==+++=================");
			rs.absolute(1); //첫 번째 행으로 이동
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
