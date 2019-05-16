package _06_ConnectionPool;
/*
 1. �����带 6�� �����ϼ���
   ������� ConnectionPool�κ��� Connection��ü�� ��û�Ͽ� ������ �����մϴ�.
   ó���� ConnectionPool�� �ʱ� �����ڿ��� 5���� Connection��ü�� ������ �ϸ�
   6��° �����尡 ��û�ϸ� ConnectionPool�� �ٽ� �߰��� 5���� Connection ��ü�� ����� �˴ϴ�.

   1��° ������� �Ϲ� ȭ���� �����ϴ� �л� �� ������ ���� ���� �л��� �̸��� �˻��� ó���մϴ�
   2��° ������� 10�� �μ� ������ �޿� �� ������ ������ Ÿ �μ� ����� �˻��� ó���մϴ�
   3��° ������� �����а� �л��� ���� �⸻��� ���� ���̺� ������ ó���մϴ�
   4��° ������� �Ϲ�ȭ�� ������ �����ϴ� �л��� ������ 4.5���� �������� ������ ó���մϴ�
   5��° ������� �� ����� ������ �ٹ����� employee���̺� ������ ó���մϴ�
   6��° ������� ȭ�а��� �����а� �л��� �˻��� ó���մϴ�

   ���� �ش� ��ȣ�� �������� ����� ȭ�鿡 ����ϵ��� �մϴ�.
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Ŀ�ؼ� Ǯ�� ����ؼ� ����Ŭ�� ������ �����Ѵ�.
public class hw{
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
