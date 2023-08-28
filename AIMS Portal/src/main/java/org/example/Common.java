package org.example;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Common {

    public void viewGrades(int curr_year){
        Scanner sc=new Scanner(System.in);
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();

        try{
            String query="select name, grade, enrollment_year from student order by enrollment_year desc, name;";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);

            System.out.println("name        year        grade");
            while(rs.next()!=false){
                int year=curr_year-rs.getInt("enrollment_year")+1;
                System.out.println(rs.getString("name")+"       "+year+"        "+rs.getBigDecimal("grade"));
            }
            c.close();
        }catch(Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }


    public void calculateGrade(){
        Scanner sc=new Scanner(System.in);
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        double total_credits=0;
        double curr_credit=0;
        double grade=0;
        double pe_credit=0;
        double pc_credit=0;
        double oe_credit=0;

        String loginid="";
        double prev1_credit=0;

        try{
            String query="select loginid, total_credits,curr_credit,prev1_credit,pe_credit,pc_credit,oe_credit from student";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);

            while(rs.next()!=false){
                total_credits=rs.getDouble("total_credits");
                curr_credit=rs.getDouble("curr_credit");
                prev1_credit=rs.getDouble("prev1_credit");

                loginid=rs.getString("loginid");
                query="select e.grade, c.credit, c.type from enrollments e inner join course_catalog c on c.course_code=e.course_code where e.grade<11 and student_id=\'"+loginid+"\';";
                Statement st=c.createStatement();
                ResultSet r=st.executeQuery(query);

                while(r.next()!=false){
                    grade+=r.getInt("grade")*r.getDouble("credit");
                    if(r.getString("type").equals("PC") && r.getInt("grade")!=0){
                        pc_credit+=r.getDouble("credit");
                    }else if(r.getString("type").equals("PE") && r.getInt("grade")!=0){
                        pe_credit+=r.getDouble("credit");
                    }else if(r.getInt("grade")!=0){
                        oe_credit+=r.getDouble("credit");
                    }
                }
                grade=grade/(total_credits+curr_credit);
                st.close();
                query="update student set grade=?, total_credits=?, prev1_credit=?, prev2_credit=?, pc_credit=?, pe_credit=?, oe_credit=?, curr_credit=? where loginid=?";
                PreparedStatement stmt1=c.prepareStatement(query);
                stmt1.setDouble(1,grade);
                stmt1.setDouble(2,total_credits+curr_credit);
                stmt1.setDouble(3,curr_credit);
                stmt1.setDouble(4,prev1_credit);
                stmt1.setDouble(5,pc_credit);
                stmt1.setDouble(6,pe_credit);
                stmt1.setDouble(7,oe_credit);
                stmt1.setDouble(8,0);
                stmt1.setString(9,loginid);
                int count=stmt1.executeUpdate();
                if(count==0){
                    System.out.println("error");
                }else{
                    System.out.println("successfully changed");
                }
                c.commit();
                stmt1.close();
            }
            stmt.close();


            c.close();
        }catch(Exception e){
            System.out.println(e.getClass().getName()+": "+ e.getMessage() );
        }
    }



}
