package _06_ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// 커넥션 풀을 사용해서 오라클에 쿼리를 전송한다.
public class ConnectionPoolEx {
	public static void main(String[] args) {
		ConnectionPool cp = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "bitcamp";
		String password = "bitcamp";
		int initCons =-1;
		int maxCons = -1;
		/* Singleton패턴으로 만든 클래스는 
		 * 생성자가 private 이므로 외부에서 객체를 생성할 수 없다
		cp = new ConnectionPool("","","",0,0);   =>not visible
		*/
		
		try {
			cp = ConnectionPool.getInstance(url, user, password, initCons, maxCons); // 최초로 하나 만들어진다
			con = cp.getConnection();
			pstmt = con.prepareStatement("SELECT * FROM dept");
			rs = pstmt.executeQuery();
			
//			con2 =cp.getConnection();
//			pstmt = con2.prepareStatement("SELECT * FROM dept");
//			rs = pstmt.executeQuery();
			
			pstmt = con.prepareStatement("SELECT * FROM dept");
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.print(rs.getString(1)+ ", ");
				System.out.print(rs.getString(2) + ", ");
				System.out.print(rs.getString(3) + ", ");
				System.out.println(rs.getString(4));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) cp.releaseConnection(con);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		cp.closeAll(); //더이상 모든걸 쓰지 않으니까 다 연결 끊는다(정상적 종료)
	}
}
