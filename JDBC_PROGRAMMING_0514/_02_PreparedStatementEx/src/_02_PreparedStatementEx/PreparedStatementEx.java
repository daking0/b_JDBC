package _02_PreparedStatementEx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
Statement								    : 기본
PreparedStatement extends Statement         : 속도향상, 변수전달 편리  / 미리 캐시 메모리에 저장(변수만 바꿔서 전달)
CallableStatement extends PreparedStatement : 트리거, 프로시저 
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
		Connection con = null; // 접속
		PreparedStatement pstmt = null; // 대화
		ResultSet rs = null; // 검색시 데이터 저장할 공간

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp", "bitcamp");
			StringBuffer sb = new StringBuffer();
			int uCount =0;
			
			//테이블 삭제
			try {
				sb.setLength(0);
				sb.append("DROP TABLE aaa");
				pstmt = con.prepareStatement(sb.toString());
				uCount = pstmt.executeUpdate();
				System.out.println("DROP COUNT: "+ uCount);
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			
			//테이블 생성
			sb.setLength(0);
			sb.append("CREATE TABLE AAA(id VARCHAR2(10), ");
			sb.append("password VARCHAR2(10))");
			pstmt = con.prepareStatement(sb.toString()); //바로 여기서 찾아서 속도가 빨라진다 (동일 쿼리 반복할 때는 pre가 빠르다)
			uCount = pstmt.executeUpdate();
			System.out.println("CREATE COUNT: "+ uCount);
			
			sb.setLength(0);
			sb.append("INSERT INTO aaa VALUES(?, ?)"); 
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, "chicken"); // VALUES(?, ?) ?가 1로 대체
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
