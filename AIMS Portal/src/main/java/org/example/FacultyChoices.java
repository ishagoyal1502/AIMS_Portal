package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.sql.Array;
import java.io.File;
import java.io.FileWriter;


class FacultyChoices {

    public void viewCourseCatalog(){
        Scanner sc=new Scanner(System.in);
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        String query="select * from course_catalog";
        System.out.println("The course catalog: ");
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()!=false){
                System.out.println(rs.getString("course_code")+"  "+rs.getString("course_name")+"  "+rs.getString("department"));
            }
            stmt.close();
            c.close();
        }catch(Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return;
    }

    public void offerCourse(String loginid, int curr_year, int curr_sem){
        Scanner sc=new Scanner(System.in);
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        System.out.println("Enter the course code of the course you want to offer: ");

        try {
            String code=sc.nextLine();
            int result=0;
            Array course_year;
            String query="select * from offerings where course_code=\'"+code+"\';";
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()!=false){
                System.out.println("Course has already been offered this sem!");
                stmt.close();
                c.close();
                return;
            }
            query="select * from course_catalog where course_code=\'"+code+"\';";
            rs = stmt.executeQuery(query);
            if(rs.next()==false){
                System.out.println("Invalid course code. Try again!");
                stmt.close();
                c.close();
                return;
            }else{
                course_year=rs.getArray("year");
                int course_sem=rs.getInt("sem");
                String course_department=rs.getString("department");
                if(course_sem!=curr_sem && course_sem!=0){
                    System.out.println("Course cannot be offered in this semester!");
                }else{
                    query="select department from faculty where loginid=\'"+loginid+"\';";
                    stmt=c.createStatement();
                    rs=stmt.executeQuery(query);
                    if(rs.next()==false){
                        System.out.println("error");
                    }else{
                        String faculty_department=rs.getString("department");
                        if(!faculty_department.equals(course_department)){
                            System.out.println("Course cannot be offered, is from another department!");
                        }else{
                            System.out.println("Do you want any cg constraints? (Y\\N)");
                            String yesNo=sc.nextLine();

                            double constraint=0.0;
                            if(yesNo.equals("Y")){
                                System.out.println("Enter the cg limit: ");
                                constraint=sc.nextDouble();
                            }
                            query="insert into offerings values(?,?,?,?)";
                            PreparedStatement stmt1 = c.prepareStatement(query);
                            stmt1.setString(1, code);
                            stmt1.setString(2, loginid);
                            stmt1.setDouble(3,constraint);
                            Integer[] courseYearArray=(Integer[])course_year.getArray();
                            if(courseYearArray.length==0){
                                System.out.println("Enter the year you want to offer this course to: ");
                                ArrayList<Integer> years=new ArrayList<>();
                                int year;
                                do{
                                    year=sc.nextInt();
                                    if(year!=0){
                                        years.add(year);
                                    }
                                }while(year!=0);
                                stmt1.setArray(4,c.createArrayOf("INT",years.toArray()));
                                //course_year=sc.nextInt();
                            }else{
                                stmt1.setArray(4,course_year);
                            }
                            int count = stmt1.executeUpdate();
                            c.commit();
                            if (count == 1) {
                                System.out.println("Course offered successfully!");
                            } else {
                                System.out.println("error");
                            }
                            stmt1.close();
                        }
                    }
                }
            }
            stmt.close();
            c.close();
        }catch(Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }


    public void enterGrades(String loginid){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter course code");

        try{
            String course_code=sc.nextLine();
            String query="select * from course_catalog where course_code=\'"+course_code+"\';";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            if(rs.next()==false){
                System.out.println("The course does not exist!");
                stmt.close();
                c.close();
                return;
            }

            query="select * from offerings where course_code=\'"+course_code+"\';";
            rs=stmt.executeQuery(query);
            String faculty_id="";
            if(rs.next()==false){
                System.out.println("This course has no offerings!");
                stmt.close();
                c.close();
                return;
            }else{
                faculty_id=rs.getString("faculty_id");
                if(!faculty_id.equals(loginid)){
                    System.out.println("You are not the instructor for this course!");
                    stmt.close();
                    c.close();
                    return;
                }
            }

            query="select * from enrollments where course_code=\'"+course_code+"\';";
            stmt=c.createStatement();
            rs=stmt.executeQuery(query);
            if(rs.next()==false){
                System.out.println("The course has no enrollments!");
                stmt.close();
                c.close();
                return;
            }else{
                System.out.println("Enter filepath");
                String filepath=sc.nextLine();

                BufferedReader br = new BufferedReader(new FileReader(filepath));
                String student_loginid="";
                String grade="";
                String line="";
                while((line=br.readLine())!=null){
                    String[] data=line.split(",");
                    student_loginid=data[0];
                    grade=data[1];
                    query="update enrollments set grade=? where course_code=? and student_id=? and grade=11;";
                    PreparedStatement st=c.prepareStatement(query);
                    if(grade.equals("A")){
                        st.setInt(1,10);
                    }else if(grade.equals("A-")){
                        st.setInt(1,9);
                    }else if(grade.equals("B")){
                        st.setInt(1,8);
                    }else if(grade.equals("B-")){
                        st.setInt(1,7);
                    }else if(grade.equals("C")){
                        st.setInt(1,6);
                    }else if(grade.equals("C-")){
                        st.setInt(1,5);
                    }else if(grade.equals("D")){
                        st.setInt(1,4);
                    }else if(grade.equals("E")){
                        st.setInt(1,2);
                    }else if(grade.equals("F")){
                        st.setInt(1,0);
                    }else{
                        st.setInt(1,11);
                    }
                    st.setString(2,course_code);
                    st.setString(3,student_loginid);
                    int count=st.executeUpdate();
                    System.out.println("eagoswrioghw");
                    if(count==0){
                        System.out.println("There is no student with the id: "+student_loginid);
                    }
                    c.commit();
                    st.close();
                }
                System.out.println("Grades updated!");
            }

            stmt.close();
            c.close();
        }catch(Exception e){
            System.out.println(e);
        }

    }


    public void profile( String loginid){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc=new Scanner(System.in);
        try{
            String query="select * from faculty where loginid=\'"+loginid+"\';";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            if(rs.next()!=false){
                System.out.println("login id: "+rs.getString("loginid"));
                System.out.println("Name: "+rs.getString("name"));
                System.out.println("Department: "+rs.getString("department"));

                System.out.println("");
                System.out.println("Enter 1 to edit name");
                System.out.println("Enter 0 to exit profile");
                int choice=sc.nextInt();
                if(choice==1){
                    System.out.println("Enter name");
                    String rn=sc.nextLine();
                    String name=sc.nextLine();
                    query="update faculty set name=? where loginid=?;";
                    PreparedStatement st=c.prepareStatement(query);
                    st.setString(1,name);
                    st.setString(2,loginid);
                    int count=st.executeUpdate();
                    if(count==0){
                        System.out.println("error");
                    }else{
                        System.out.println("name changes successfully!");
                    }
                    c.commit();
                    st.close();
                }
            }
            stmt.close();
            c.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }


    public void viewOfferedCourses(String loginid){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc=new Scanner(System.in);
        try{
            System.out.println("Courses offered by you in this semester are: ");
            String query="select o.course_code, c.course_name from offerings o inner join course_catalog c on c.course_code=o.course_code where o.faculty_id=\'"+loginid+"\';";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next()!=false){
                System.out.println(rs.getString("course_code")+"  "+rs.getString("course_name"));
            }
            stmt.close();
            c.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
