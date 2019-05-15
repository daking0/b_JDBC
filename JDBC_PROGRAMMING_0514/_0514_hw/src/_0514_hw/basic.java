package _0514_hw;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*
 1. emp_test ���̺��� ���α׷������� �����
  �ַܼκ��� 3���� ������ �Է¹ް�
  �ٽ� �˻��ؼ� ȭ�鿡 ����ϼ���

CREATE TABLE emp_test (
  eno varchar2(4),
  ename varchar2(10),
  job varchar2(6)
);


2. eno�� �Է��ϸ� �ش� ����� �˻��ؼ� ����ϼ���
 */
public class basic {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp","bitcamp");

			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();

			//���̺� ����
			try {
				sb.setLength(0);
				sb.append("DROP TABLE emp_test");
				stmt.executeUpdate(sb.toString());
				System.out.println("DROP TABLE emp_test");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			//���̺� ����
			sb.setLength(0);
			sb.append("CREATE TABLE emp_test(" 
					 + " eno varchar2(4),");
			sb.append("ename varchar2(10),");
			sb.append("job varchar2(6))");

			// 3-2) stmt�� ���� query�� ����
			int updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("createCount: " + updateCount);

			// 3-2) ������ �ֱ�
			//sb.setLength(0); 
			int s=0;
			for(int i=0; i<3 ; i++) {
				sb.setLength(0);
				System.out.println("���: ");
				String eno1 =scan.next();
				System.out.println("�̸�: ");
				String name = scan.next();
				System.out.println("����: ");
				String job = scan.next();
				
				sb.append("INSERT INTO emp_test ");
				sb.append("VALUES ("+"'"+eno1+"'");
				sb.append(", "+"'"+name+"'");
				sb.append(", "+"'"+job+"'"+")");
				updateCount = stmt.executeUpdate(sb.toString()); // ��� ������
				System.out.println("INSERT COUNT : " + s);
				s++;
			}

			// 3-3) �˻��ϱ�
			sb.setLength(0);
			sb.append("SELECT * FROM emp_test");
			rs = stmt.executeQuery(sb.toString());
			System.out.println("�Է�");
			String eno = scan.next();
			while (rs.next()) {
				String check = rs.getString("eno");
				if(check.equals(eno)) {
					System.out.println("��� : " + rs.getString("eno"));
					System.out.println("�̸� : " + rs.getString("ename"));
					System.out.println("���� : " + rs.getString("job"));
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
