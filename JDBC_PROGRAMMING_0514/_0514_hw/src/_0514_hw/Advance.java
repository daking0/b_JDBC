package _0514_hw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*
 1. dept_test1와 emp_test1 테이블을 프로그래밍으로 만드세요
   3개 부서를 입력하고 1부서당 사원을 2명씩 프로그래밍으로 입력하세요

CREATE TABLE dept_test1 (
  dno varchar2(2),
  dname varchar2(14)
);

CREATE TABLE emp_test1 (
  eno varchar2(4),
  ename varchar2(10),
  job varchar2(6),
  dno varchar2(2)
);

2. 부서별 사원을 프로그래밍으로 출력하세요

3. 부서를 입력하면 부서에 소속된 사원을 출력하세요

4. 사원번호를 입력하면 해당 사원정보와 부서 정보를 출력하세요
 */
public class Advance{
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
               con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "madang","madang");

               stmt = con.createStatement();
               StringBuffer sb = new StringBuffer();
               
               //테이블 삭제
               try {
                  sb.setLength(0);
                  sb.append("DROP TABLE dept_test1");
                  stmt.executeUpdate(sb.toString());
                  System.out.println("DROP TABLE dept_test1");
                  
                  sb.setLength(0);
                  sb.append("DROP TABLE emp_test1");
                  stmt.executeUpdate(sb.toString());
                  System.out.println("DROP TABLE emp_test1");
               } catch (SQLException e) {
                  System.out.println(e.getMessage());
               }
               
               
               //테이블 생성
               sb.setLength(0);
               sb.append("CREATE TABLE dept_test1(" 
                      + " dno varchar2(2),");
               sb.append("dname varchar2(14))");
                              
               // 3-2) stmt를 통해 query문 실행
               int updateCount = stmt.executeUpdate(sb.toString());
               System.out.println("createCount: " + updateCount);
               
               //테이블 생성
               sb.setLength(0);
               sb.append("CREATE TABLE emp_test1(" 
                      + " eno varchar2(4),");
               sb.append("ename varchar2(10),");
               sb.append("job varchar2(6),");
               sb.append("dno varchar2(2))");
               
               updateCount = stmt.executeUpdate(sb.toString());
               System.out.println("createCount: " + updateCount);
               
               //입력
               int s=0;
               String dno="", dname="", eno ="";
               String name ="", job="", dno1= "";
               for(int i=0; i<2 ; i++) {
                  System.out.println("부서번호: ");
                  dno = scan.next();
                  System.out.println("부서명: ");
                  dname = scan.next();
                  sb.setLength(0);
                 sb.append("INSERT INTO dept_test1 ");
                 sb.append("VALUES ("+"'"+dno+"'");
                 sb.append(", "+"'"+dname+"'"+")");
                 updateCount = stmt.executeUpdate(sb.toString()); // 명령 보내짐
                  for(int k=0;k<2;k++) {
                     System.out.println("부서번호: ");
                     dno1 = scan.next();
                     System.out.println("사번: ");
                     eno =scan.next();
                     System.out.println("이름: ");
                     name = scan.next();
                     System.out.println("직업: ");
                     job = scan.next();
                    sb.setLength(0);
                     sb.append("INSERT INTO emp_test1 ");
                     sb.append("VALUES ("+"'"+eno+"'");
                     sb.append(", "+"'"+name+"'");
                     sb.append(", "+"'"+job+"'");
                     sb.append(", "+"'"+dno1+"'"+")");
                     updateCount = stmt.executeUpdate(sb.toString()); // 명령 보내짐
                  }
                  
                  
                  System.out.println("INSERT COUNT : " + s);
                  s++;
               }
               
               
               
               //부서별 사원을 프로그래밍으로 출력하세요
//               sb.setLength(0);
//               sb.append("SELECT * FROM emp_test1");
//               rs = stmt.executeQuery(sb.toString());
               
               sb.setLength(0);
               sb.append("SELECT dno, eno, ename FROM emp_test1 "/*+"JOIN dept_test1 USING (dno)"*/);
               sb.append("GROUP BY dno, eno, ename");
//               sb.append("JOIN dept_test1 USING (dno)");
//               sb.append("JOIN dept_test1 USING (dno)");
               rs = stmt.executeQuery(sb.toString());
               
               while (rs.next()) {
                  System.out.println("사원번호 : "+rs.getString("eno"));
                  System.out.println("사원이름 : "+rs.getString("ename"));
                  System.out.println("부서번호 : "+rs.getString("dno"));
               }
               
               
               //부서를 입력하면 부서에 소속된 사원을 출력하세요
               sb.setLength(0);
               System.out.println("부서번호 입력");
               String deno = scan.next();
               sb.append("SELECT dno,eno,ename FROM emp_test1"+" WHERE dno = '"+deno+"'");
               rs = stmt.executeQuery(sb.toString());
               
               while (rs.next()) {
                  String check = rs.getString("dno");
                  if(check.equals(deno)) {
                     System.out.println("사번 : " + rs.getString("eno"));
                     System.out.println("이름 : " + rs.getString("ename"));
                     break;
                  }
               }
               
               //사원번호를 입력하면 해당 사원정보와 부서 정보를 출력하세요
               sb.setLength(0);
               System.out.println("사원번호 입력");
               String enoo = scan.next();
               sb.append("SELECT dno, dname, eno, ename, job FROM emp_test1"+" JOIN dept_test1 USING (dno)"+" WHERE eno ="+enoo);
               rs = stmt.executeQuery(sb.toString());
               
               while (rs.next()) {
                  String check = rs.getString("eno");
                  if(check.equals(enoo)) {
                     System.out.println("부서번호 : "+rs.getString("dno"));
                      System.out.println("부서명 : "+rs.getString("dname"));
                      System.out.println("사번 : " + rs.getString("eno"));
                      System.out.println("이름 : " + rs.getString("ename"));
                      System.out.println("업무 : "+rs.getString("job"));
                      break;
                  }
               }
               
            }catch (SQLException e) {
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
