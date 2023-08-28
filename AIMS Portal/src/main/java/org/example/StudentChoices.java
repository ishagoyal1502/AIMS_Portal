package org.example;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.sql.Array;

class StudentChoices {

//    private Scanner sc;
//
//    public StudentChoices(InputStream inputStream) {
//        this.sc = new Scanner(inputStream);
//        //this.printStream = printStream;
//    }
    public void viewOfferings(){
        Scanner sc=new Scanner(System.in);
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        String query="select c.course_code , c.course_name, f.name as faculty_name from offerings o inner join course_catalog c on o.course_code=c.course_code inner join faculty f on f.loginid=o.faculty_id order by o.course_code; ";
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int i=0;
            while(rs.next()!=false){
                i++;
                System.out.println(rs.getString("course_code")+"  "+rs.getString("course_name")+ "  "+rs.getString("faculty_name"));
            }
            if(i==0){
                System.out.println("There are currently no course offered");
            }
            stmt.close();
            c.close();
        }catch(Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    public void viewEnrolled(String loginid){
        Scanner sc=new Scanner(System.in);
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        String query="select e.course_code, c.course_name from enrollments e inner join course_catalog c on c.course_code=e.course_code where student_id=\'"+loginid+"\' and grade=11 order by e.course_code";
        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int i=0;
            while(rs.next()!=false){
                i++;
                System.out.println(rs.getString("course_code")+"  "+rs.getString("course_name"));
            }
            if(i==0){
                System.out.println("You are currently not enrolled in any course");
            }
            stmt.close();
            c.close();
        }catch(Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }
    public void enroll(String loginid,int curr_year, int curr_sem){
        Scanner sc=new Scanner(System.in);
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        String query;
        try{
            System.out.println("Enter the code of the course you want to enroll in: ");
            String course_code=sc.nextLine();
            String student_department="";
            double student_grade=0;
            double student_credit_limit=0;
            double student_curr_credit=0;
            int student_year=0;


            query="select * from course_catalog where course_code=\'"+course_code+"\';";
            Statement stmt = c.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            if(rs.next()==false){
                System.out.println("The course does not exist!");
                stmt.close();
                c.close();
                return;
            }

            query="select * from offerings where course_code=\'"+course_code+"\';";

            rs=stmt.executeQuery(query);
            if(rs.next()==false){
                System.out.println("The course has not been offered for enrollment!");
                stmt.close();
                c.close();
                return;
            }

            query="select * from student where loginid=\'"+loginid+"\';";

            rs = stmt.executeQuery(query);
            if(rs.next()!=false){
                student_department=rs.getString("department");
                student_grade=rs.getDouble("grade");
                student_year=curr_year-rs.getInt("enrollment_year")+1;
                student_credit_limit=1.5*(rs.getDouble("prev1_credit")+rs.getDouble("prev2_credit"))/2;
                student_curr_credit=rs.getDouble("curr_credit");
            }


            query="select * from enrollments where course_code=\'"+course_code+"\' and student_id=\'"+loginid+"\';";
            Statement st=c.createStatement();
            ResultSet r=st.executeQuery(query);
            int flag=0;
            if(r.next()!=false){
                System.out.println("You have already enrolled in this course!");
                flag=1;
                st.close();
                stmt.close();
                c.close();
                return;
                //System.out.println("herererere");
            }

            query="select o.constraint, o.year as year, c.prereq, c.credit, c.department, c.type from offerings o inner join course_catalog c on o.course_code=c.course_code where o.course_code=\'"+course_code+"\';";
            stmt=c.createStatement();
            rs=stmt.executeQuery(query);



            double course_credit=0;
            if(rs.next()!=false){
                //System.out.println("herererere");
                course_credit=rs.getDouble("credit");
                if(student_curr_credit+rs.getDouble("credit")>student_credit_limit){
                    System.out.println("Cannot register, credit limit exceeded");
                    flag=1;

                }else if((rs.getString("type").equals("PC")|| rs.getString("type").equals("PE")) && !rs.getString("department").equals(student_department)){
                    System.out.println("Cannot register, course is offered by another department!");
                    flag=1;
                }else if(student_grade<rs.getDouble("constraint")){
                    System.out.println("Cannot register, you do not satisfy the CG constraint");
                    flag=1;
                }else{
                    if(rs.getArray("year")!=null){
                        Array year=rs.getArray("year");
                        Integer[] years=(Integer[])year.getArray();
                        int i=0;
                        for(i=0;i<years.length;i++){
                            if(years[i]==student_year){
                                break;
                            }
                        }
                        if(i==years.length){
                            System.out.println("Cannot register, course is not offered for your year!");
                            flag=1;
                        }
                    }
                    if(flag==0) {
                        String prereq = rs.getString("prereq");
                        if (!prereq.equals("")) {
                            String[] courses = prereq.split(",");
                            for (int i = 0; i < courses.length; i++) {
                                Statement stmt1 = c.createStatement();
                                String query1 = "select grade from enrollments where student_id=\'" + loginid + "\' and course_code=\'" + courses[i] + "\';";
                                ResultSet rs1 = stmt1.executeQuery(query1);
                                if (rs1.next() != false) {
                                    if (rs1.getInt("grade") < 5 || rs1.getInt("grade") == 11) {
                                        System.out.println("Cannot register, you do not satisfy the pre-requisites");
                                        stmt1.close();
                                        flag = 1;
                                        break;
                                    }
                                } else {
                                    System.out.println("Cannot register, you do not satisfy the pre-requisites");
                                    stmt1.close();
                                    flag = 1;
                                    break;
                                }
                                stmt1.close();
                            }
                        }
                    }
                }
                if(flag==0){
                        System.out.println("Are you sure you want to register in this course? (Y\\N)");
                        String yesNo = sc.nextLine();
                        if (yesNo.equals("Y")) {
                            query = "insert into enrollments values(?,?,?,?,?)";
                            PreparedStatement stmt1 = c.prepareStatement(query);
                            stmt1.setString(1, course_code);
                            stmt1.setString(2, loginid);
                            stmt1.setDouble(3, 11);
                            stmt1.setInt(4, student_year);
                            stmt1.setInt(5, curr_sem);
                            int count = stmt1.executeUpdate();
                            c.commit();
                            System.out.println("Successfully enrolled");
                            query = "update student set curr_credit=? where loginid=?";
                            student_curr_credit = student_curr_credit + course_credit;
                            stmt1 = c.prepareStatement(query);
                            stmt1.setDouble(1, student_curr_credit);
                            stmt1.setString(2, loginid);
                            count = stmt1.executeUpdate();
                            c.commit();
                            stmt1.close();
                        }
                }
            }
            c.close();
        }catch(Exception e){
            System.out.println(e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    public void profile(String loginid){
        try{
            Scanner sc=new Scanner(System.in);
            ConnectDB conn=new ConnectDB();
            Connection c=conn.connect();
            int profile_choice=0;
            do{
            String query="select * from student where loginid=\'"+loginid+"\';";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            if(rs.next()!=false){
                System.out.println("");
                System.out.println("PROFILE");
                System.out.println("Name: "+ rs.getString("name"));
                System.out.println("Login ID: "+ rs.getString("loginid"));
                System.out.println("Department: "+ rs.getString("department"));
                System.out.println("Enrollment year: "+ rs.getInt("enrollment_year"));
                System.out.println("Grade: "+ rs.getDouble("grade"));
                System.out.println("Total credits: "+ rs.getDouble("total_credits"));
                System.out.println("Currently enrolled credits: "+ rs.getDouble("curr_credit"));

                System.out.println("");
                System.out.println("PROFILE CHOICES");
                System.out.println("Enter 1 to edit name.");
                System.out.println("Enter 2 to view credit details.");
                System.out.println("Enter 0 to exit profile.");
                profile_choice=sc.nextInt();
                String random=sc.nextLine();
                switch(profile_choice){
                    case 1:
                        System.out.println("Enter name:");
                        String name=sc.nextLine();
                        if(name.equals("")){
                            System.out.println("Invalid name!");
                        }else{
                            String query1="update student set name=? where loginid=?";
                            PreparedStatement stmt1 = c.prepareStatement(query1);
                            stmt1.setString(1, name);
                            stmt1.setString(2, loginid);
                            int count=stmt1.executeUpdate();
                            c.commit();
                            System.out.println("Name changed successfully!");
                            stmt1.close();
                        }
                        break;
                    case 2:
                        System.out.println("Total credits: "+ rs.getDouble("total_credits"));
                        System.out.println("Program core credits: "+ rs.getDouble("pc_credit"));
                        System.out.println("Program elective credits: "+ rs.getDouble("pe_credit"));
                        System.out.println("Open elective credits: "+ rs.getDouble("oe_credit"));
                        System.out.println("Currently enrolled credits: "+ rs.getDouble("curr_credit"));
                        break;
                    }
                }
            }while(profile_choice!=0);
            c.close();
            sc.close();
        }catch(Exception e){
            System.out.println(e.getClass().getName()+": "+ e.getMessage() );
        }

    }

    public void dropAudit(String loginid){
        Scanner sc=new Scanner(System.in);
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        double new_grade=12;
        System.out.println("Enter the course code you wish to drop: ");

        try{
            String course_code=sc.nextLine();
            String query="select * from enrollments where course_code=\'"+course_code+"\' and student_id=\'"+loginid+"\';";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            if(rs.next()==false){
                System.out.println("You are not enrolled in this course!");
            }else{
                double grade=rs.getDouble("grade");
                if(grade<11){
                    System.out.println("You cannot drop an already completed course!");
                }else if(grade==12){
                    System.out.println("You cannot drop an already dropped course!");
                }else{
                    System.out.println("Are you sure you wish to drop the course? (Y\\N)");
                    String yesNo=sc.nextLine();
                    if(yesNo.equals("Y")){
                        query="update enrollments set grade=? where course_code=? and student_id=?";
                        PreparedStatement st=c.prepareStatement(query);
                        st.setDouble(1,new_grade);
                        st.setString(2,course_code);
                        st.setString(3,loginid);
                        int count=st.executeUpdate();
                        c.commit();
                        System.out.println("Course successfully dropped!");
                        st.close();
                    }
                }
                query="select credit from course_catalog where course_code=\'"+course_code+"\';";
                rs=stmt.executeQuery(query);
                if(rs.next()!=false){
                    double credit=rs.getDouble("credit");
                    query="select curr_credit from student where loginid=\'"+loginid+"\';";
                    Statement stmt1=c.createStatement();
                    ResultSet rs1=stmt1.executeQuery(query);
                    double curr_credit=0;
                    if(rs1.next()!=false){
                        curr_credit=rs1.getDouble("curr_credit");
                        query="update student set curr_credit=? where loginid=?";
                        PreparedStatement st=c.prepareStatement(query);
                        st.setDouble(1,curr_credit-credit);
                        st.setString(2,loginid);
                        int count=st.executeUpdate();
                        c.commit();
                        st.close();
                    }
                    stmt1.close();
                }
                stmt.close();
            }
            c.close();
        }catch(Exception e){
            System.out.println(e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    public void getGrades(String loginid){
        Scanner sc=new Scanner(System.in);
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        System.out.println("Enter 1 for viewing cg");
        System.out.println("Enter 2 for viewing grade of a particular course");

        try {
            int choice=sc.nextInt();
            if (choice == 1) {
                String query = "select grade from student where loginid=\'" + loginid + "\';";
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next() != false) {
                    System.out.println("Your cg is " + rs.getDouble("grade"));
                }
                stmt.close();
            }else if(choice==2){
                String random=sc.nextLine();
                System.out.println("Enter the course code");
                String course_code=sc.nextLine();
                String query="select grade from enrollments where course_code=\'"+course_code+"\' and student_id=\'"+loginid+"\';";
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next() != false) {
                    if(rs.getDouble("grade")==11){
                        System.out.println("The course is currently ongoing");
                    }else if(rs.getDouble("grade")==12){
                        System.out.println("The course has been dropped");
                    }else if(rs.getDouble("grade")<11){
                        System.out.println("Your grade in this course is "+rs.getDouble("grade"));
                    }
                }else{
                    System.out.println("You are not enrolled in this course");
                }
                stmt.close();
            }
            c.close();
        }catch(Exception e){
            System.out.println(e.getClass().getName()+": "+ e.getMessage() );
        }
    }



}
