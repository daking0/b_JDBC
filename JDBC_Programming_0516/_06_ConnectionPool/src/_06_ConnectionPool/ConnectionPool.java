package _06_ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectionPool {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	ArrayList<Connection> free; // ��������� ���� Connection ��ü
	ArrayList<Connection> used; // ������� Connection ��ü
	String url;
	String user;
	String password;
	int initialCons = 0; // �ʱ� Ŀ�ؼ� ��
	int maxCons = 0; // �ִ� Ŀ�ؼ� ��
	int numCons = 0; // ���� Ŀ�ؼ� ��
	static ConnectionPool cp = null; // Singleton ����(������ �ϳ��� �ִ� ���)�� ���� static ����

	/*
	 * Singleton ���� �����ͺ��̽� ����ó�� �Ͽ������� �����Ǿ�� �� ������ ���������� ���� ��ü�� ���鵵�� ������� �ʰ� �� 1����
	 * ��ü�� ���鵵�� �����ϰ� �� 1���� ��ü�θ� ����� ���� �����ϴ� ���α׷� ����̴�.
	 * 
	 * 1) static���� Ŭ���� ������ �����Ѵ� 2) �����ڸ� private���� �����Ѵ� 3) �Ϲ������� Ŭ���� ��ü�� �����ϴ�
	 * getInstance()�޼��带 �����. 4) getInstance()�޼��忡���� ���ʷ� 1���� ��ü�� �����ϵ��� �Ѵ�. 5) �ܺ�
	 * Ŭ�������� �� Ŭ���� ��ü�� ��û�� ���� �ݵ�� getInstance �޼��常�� ����ؼ� ��ü�� ���� �� �ִ�. 6) �ƹ��� ����
	 * �����忡�� ��û�� �ص� ��ȯ�ϴ� ��ü�� �����ϰ� ������ ��ü�̴�. (�ܺο��� ������ΰ� �� �ϳ�)
	 */

	public static ConnectionPool getInstance(String url, String user, String password, int initCons, int maxCons) {
		// getInstance �� ȣ���ؾ߸� ��ü ��������

		try {
			if (cp == null) {
				// static �޼��忡�� ����ȭ �ϴ� ���
				synchronized (ConnectionPool.class) { // Ŭ���� ��ü(Ŭ������ ���ÿ� �������� ���Ѵ�)
					cp = new ConnectionPool(url, user, password, initCons, maxCons); // ���� �� Ŭ���� �ȿ��� ���� return
				}
			} // �ι�° ������ ��(�ٸ� ��������� ��� �䱸�ص�) if�� �ȵ��� ���ͼ� �׳� ���� ���� return = �� �ϳ��� ��ü�� ���� �� �ִ�
				// => db���� ���� ��ü�� �� �ϳ��� �༭ ������ �����Ѵ�.
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cp;
	}
	/*
	 * �ش� ��ü�� ����ȭ�ϴ� �Ǵٸ� ��� public static synchronized ConnectionPool
	 * getInstance(String url, String password,int initCons, int maxCons) { }
	 */

	// ������ private (���� ���� �� �ִ�)
	// �� �ȿ��� ��� ��ü�� �����ؼ� ����ڴٴ� ������ ���� �� =>Singleton ����
	// ���� �����ؼ� �ٰ�(������ �ϳ��� �����ϱ� ���ؼ�)
	private ConnectionPool(String url, String user, String password, int initCons, int maxCons) throws SQLException {
		this.url = url;
		this.user = user;
		this.password = password;
		this.initialCons = initCons; // ���ʿ� Connection�� �� �� ������
		this.maxCons = maxCons; // �ִ�� ����� �������

		if (initialCons < 0) // -1�� �ָ� �˾Ƽ� �����ض�
			this.initialCons = 5; // �׷��� ���� 5���� ����
		if (maxCons < 0)
			this.maxCons = 10; // �˾Ƽ� 10���� ����

		// �ʱ� Ŀ�ؼ� ������ ������ ArrayList�� ������ �� �ֵ���
		// �ʱ� Ŀ�ؼ� ������ŭ ArrayList�� �����Ѵ�.
		free = new ArrayList<Connection>(initialCons);
		used = new ArrayList<Connection>(initialCons);

		// �ʱ� Ŀ�ؼ� ������ŭ Connection ��ü�� ��������
		while (this.numCons < this.initialCons) {
			addConnection(); // 1���� ��ü�� �߰��ϴ� �޼��� ->�̰� ��ȯ�ϸ� getNewConnection() �Ǽ� 5���� ����
		}
	}

	private void addConnection() throws SQLException {
		free.add(getNewConnection()); // 5���� ��ϵǸ� �����

	}

	private Connection getNewConnection() throws SQLException {
		Connection con = null;
		con = DriverManager.getConnection(this.url, this.user, this.password);
		System.out.println("About to connect to " + con);
		this.numCons++;
		return con;
	}

	// �ܺο��� ����Ŭ ���� ��ü�� �䱸�ϴ� �޼���
	public synchronized Connection getConnection() throws SQLException { // ���ÿ� ��û���ϵ��� ����ȭ
		if (free.isEmpty()) {
			while (this.numCons < maxCons) {
				addConnection();
			}
		}

		Connection _con = free.get(free.size() - 1);
		free.remove(_con);
		used.add(_con);
		
		
		
		return _con;
	}

}
