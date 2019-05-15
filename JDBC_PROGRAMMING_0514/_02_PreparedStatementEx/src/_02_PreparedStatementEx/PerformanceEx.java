package _02_PreparedStatementEx;
import java.sql.*; 
public class PerformanceEx{ 
	static{ 
		try{ 
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
		}catch(ClassNotFoundException cnfe){ 
			cnfe.printStackTrace(); 
		} 
	} 
	public static void main(String[] args){



		long start; 
		long end; 
		Connection con = null; 
		PreparedStatement pstmt = null; 
		Statement stmt=null; 

		try{ 
			con = DriverManager.getConnection( 
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"bitcamp", 
					"bitcamp"); 
			stmt = con.createStatement(); 
			String sql ="create table performance(id varchar(10)," + 
					"password varchar(10))"; 
			stmt.executeUpdate(sql); 

			start=System.currentTimeMillis(); 
			for(int i=0;i<10000;i++){ 
				stmt.executeUpdate("insert into performance values" +"('"+i+"','"+i+"')"); 
				}

			end = System.currentTimeMillis(); 
			System.out.println("Statement process time = " 
					+(end - start)); 

			sql ="insert into performance values(?,?)"; 
			pstmt = con.prepareStatement(sql); 

			start = System.currentTimeMillis(); 
			for(int j=0;j<10000;j++){ 
				pstmt.setString(1,""+j); 
				pstmt.setString(2,""+j); 
				pstmt.executeUpdate(); 
			} 
			end = System.currentTimeMillis();





			System.out.println("PreparedStatement process time = " 
					+(end - start)); 
			stmt.executeUpdate("drop table performance"); 
		}catch(SQLException ee){ 
			ee.printStackTrace(); 
		}finally{ 
			try { 
				if(pstmt != null) pstmt.close(); 
				if(stmt != null) stmt.close(); 
				if(con != null) con.close(); 
			}catch(SQLException se){} 
		} 
	} 
}
