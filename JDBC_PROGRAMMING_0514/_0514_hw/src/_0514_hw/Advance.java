package _0514_hw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*
 1. dept_test1�� emp_test1 ���̺��� ���α׷������� ���弼��
   3�� �μ��� �Է��ϰ� 1�μ��� ����� 2�� ���α׷������� �Է��ϼ���

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

2. �μ��� ����� ���α׷������� ����ϼ���

3. �μ��� �Է��ϸ� �μ��� �Ҽӵ� ����� ����ϼ���

4. �����ȣ�� �Է��ϸ� �ش� ��������� �μ� ������ ����ϼ���
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
               
               //���̺� ����
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
               
               
               //���̺� ����
               sb.setLength(0);
               sb.append("CREATE TABLE dept_test1(" 
                      + " dno varchar2(2),");
               sb.append("dname varchar2(14))");
                              
               // 3-2) stmt�� ���� query�� ����
               int updateCount = stmt.executeUpdate(sb.toString());
               System.out.println("createCount: " + updateCount);
               
               //���̺� ����
               sb.setLength(0);
               sb.append("CREATE TABLE emp_test1(" 
                      + " eno varchar2(4),");
               sb.append("ename varchar2(10),");
               sb.append("job varchar2(6),");
               sb.append("dno varchar2(2))");
               
               updateCount = stmt.executeUpdate(sb.toString());
               System.out.println("createCount: " + updateCount);
               
               //�Է�
               int s=0;
               String dno="", dname="", eno ="";
               String name ="", job="", dno1= "";
               for(int i=0; i<2 ; i++) {
                  System.out.println("�μ���ȣ: ");
                  dno = scan.next();
                  System.out.println("�μ���: ");
                  dname = scan.next();
                  sb.setLength(0);
                 sb.append("INSERT INTO dept_test1 ");
                 sb.append("VALUES ("+"'"+dno+"'");
                 sb.append(", "+"'"+dname+"'"+")");
                 updateCount = stmt.executeUpdate(sb.toString()); // ��� ������
                  for(int k=0;k<2;k++) {
                     System.out.println("�μ���ȣ: ");
                     dno1 = scan.next();
                     System.out.println("���: ");
                     eno =scan.next();
                     System.out.println("�̸�: ");
                     name = scan.next();
                     System.out.println("����: ");
                     job = scan.next();
                    sb.setLength(0);
                     sb.append("INSERT INTO emp_test1 ");
                     sb.append("VALUES ("+"'"+eno+"'");
                     sb.append(", "+"'"+name+"'");
                     sb.append(", "+"'"+job+"'");
                     sb.append(", "+"'"+dno1+"'"+")");
                     updateCount = stmt.executeUpdate(sb.toString()); // ��� ������
                  }
                  
                  
                  System.out.println("INSERT COUNT : " + s);
                  s++;
               }
               
               
               
               //�μ��� ����� ���α׷������� ����ϼ���
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
                  System.out.println("�����ȣ : "+rs.getString("eno"));
                  System.out.println("����̸� : "+rs.getString("ename"));
                  System.out.println("�μ���ȣ : "+rs.getString("dno"));
               }
               
               
               //�μ��� �Է��ϸ� �μ��� �Ҽӵ� ����� ����ϼ���
               sb.setLength(0);
               System.out.println("�μ���ȣ �Է�");
               String deno = scan.next();
               sb.append("SELECT dno,eno,ename FROM emp_test1"+" WHERE dno = '"+deno+"'");
               rs = stmt.executeQuery(sb.toString());
               
               while (rs.next()) {
                  String check = rs.getString("dno");
                  if(check.equals(deno)) {
                     System.out.println("��� : " + rs.getString("eno"));
                     System.out.println("�̸� : " + rs.getString("ename"));
                     break;
                  }
               }
               
               //�����ȣ�� �Է��ϸ� �ش� ��������� �μ� ������ ����ϼ���
               sb.setLength(0);
               System.out.println("�����ȣ �Է�");
               String enoo = scan.next();
               sb.append("SELECT dno, dname, eno, ename, job FROM emp_test1"+" JOIN dept_test1 USING (dno)"+" WHERE eno ="+enoo);
               rs = stmt.executeQuery(sb.toString());
               
               while (rs.next()) {
                  String check = rs.getString("eno");
                  if(check.equals(enoo)) {
                     System.out.println("�μ���ȣ : "+rs.getString("dno"));
                      System.out.println("�μ��� : "+rs.getString("dname"));
                      System.out.println("��� : " + rs.getString("eno"));
                      System.out.println("�̸� : " + rs.getString("ename"));
                      System.out.println("���� : "+rs.getString("job"));
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
