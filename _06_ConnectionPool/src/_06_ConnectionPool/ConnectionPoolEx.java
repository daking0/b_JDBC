package _06_ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Ŀ�ؼ� Ǯ�� ����ؼ� ����Ŭ�� ������ �����Ѵ�.
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
		/* Singleton�������� ���� Ŭ������ 
		 * �����ڰ� private �̹Ƿ� �ܺο��� ��ü�� ������ �� ����
		cp = new ConnectionPool("","","",0,0);   =>not visible
		*/
		
		try {
			cp = ConnectionPool.getInstance(url, user, password, initCons, maxCons); // ���ʷ� �ϳ� ���������
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
		
		cp.closeAll(); //���̻� ���� ���� �����ϱ� �� ���� ���´�(������ ����)
	}
}
