package _01_JDBCEx;

/*1.jar�� �����Ѵ�(Add Externals Libraries)
2. import java.sql.*; ���´�
3. ����Ŭ ����̹� ��ü�� �ε��Ѵ�(�޸𸮿� �ø�)
4. Connecton ��ü�� �����Ѵ�(����Ŭ�� �����Ѵ�)
5. Statement ��ü�� �����Ѵ�(����Ŭ�� ��ȭ)
6. SQL���� ����� �ִٸ�(SELECT�� ����)
   ResultaSet ��ü�� �����Ѵ�(������ ���尴ü)
7. ResultSet -> Statement -> Connection ����
	(��, Open�� ��������) Close()�Ѵ�
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcEx {
	// static �ʱ�ȭ ����
	// static �ʵ带 �ʱ�ȭ �ϴ� ��
	// ���� ���� ȣ���
	static { // static�������� static �ʱ�ȭ
		// 1)DriverManager ���ο��� ����� Oracle ����̹� ��ü�� �����ؼ� �޸𸮿� �ε���
		// ���α׷����� ���� ������ ���� �����Ƿ� Ŭ���� ������ �������� �ʾҴ�.
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} // oracle�� ����̹��� �޸𸮿� �ø��� / db������ ���� �� Ȯ���� ���Ƽ� try catch�� ���� ����.
	}// ���⼭ ���ϰ� main ù �ٿ��� �ϱ⵵������ ���⼭ �̷��� �ϴ°� �� �����ǰ� ����(���ʿ� �ٷ� �����)

	public static void main(String[] args) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 2)����Ŭ�� ������ �Ѵ�
			/* Connection */ con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bitcamp",
					"bitcamp");
			// localhost �ٸ���� ip ������ �ȴ� , bitcamp�� �� id��pw�� �ش�

			// 3) ����Ŭ�� ��ȭ�� �ϱ� ���� Statement ����
			/* Statement */ stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();

			// 3-0) ���ึ�� ���̺� ����
			try {
				sb.setLength(0); // ũ�⸦ 0���� �ʱ�ȭ
				sb.append("DROP TABLE test1");
				stmt.executeUpdate(sb.toString());
				System.out.println("DROP TABLE test1");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				// e.printStackTrace();
			}

			// 3-1)query���� ������ ��ü ���� - ���̺� ����
			sb.setLength(0);
			sb.append("CREATE TABLE test1(" 
					 + "id VARCHAR2(10),");
			sb.append("age NUMBER)");

			// 3-2) stmt�� ���� query�� ����
			int updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("createCount: " + updateCount); // ����� bitcamp�� ���������

			// 3-2) ������ �ֱ�
			sb.setLength(0); // 0���� �ʱ�ȭ
			sb.append("INSERT INTO test1 ");
			sb.append("VALUES ('aaa000'");
			sb.append(", 10)");
			updateCount = stmt.executeUpdate(sb.toString()); // ��� ������
			System.out.println("INSERT COUNT : " + updateCount);

			//con.commit(); // Ʈ����� �Ϸ� - �ڹٰ� auto commit �Ѵ�

			// 3-3) �˻��ϱ�
			sb.setLength(0);
			sb.append("SELECT * FROM test1");
			/* ResultSet */ rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				System.out.println("id : " + rs.getString(1));
				System.out.println("age : " + rs.getString(2));
			}

			// 3-4) Update�� �ϱ�
			sb.setLength(0);
			sb.append("UPDATE test1 SET id = 'bbb000', ");
			sb.append("age=20 ");
			sb.append("WHERE id = 'aaa000'");
			updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("UPDATE COUNT : " + updateCount);

			// 3-5) �˻��ϱ�
			sb.setLength(0);
			sb.append("SELECT * FROM test1");
			rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				System.out.println("id : " + rs.getString(1));
				System.out.println("age : " + rs.getString(2));
			}

			// 3-6) �����
			sb.setLength(0);
			sb.append("DELETE FROM test1");
			updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("DELETE COUNT: " + updateCount);
			con.commit();

			// 3-7) �˻��ϱ�
			sb.setLength(0);
			sb.append("SELECT * FROM test1");
			rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				System.out.println("id : " + rs.getString(1));
				System.out.println("age : " + rs.getString(2));
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
