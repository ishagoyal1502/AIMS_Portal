package org.example;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.sql.Connection;
import java.sql.*;



class AdminChoices {


    public void addCourse() {
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter course details.");
        System.out.println("Course code:");

        try{
            String courseCode = sc.nextLine();
            String query="select * from course_catalog where course_code=\'"+courseCode+"\';";
            Statement s=c.createStatement();
            ResultSet rs=s.executeQuery(query);
            if(rs.next()!=false){
                System.out.println("Course with the same code already exists!");
                c.close();
                return;
            }

        System.out.println("Course name:");
        String courseName = sc.nextLine();
        System.out.println("No. of lectures per week:");
        int lValue = sc.nextInt();
        System.out.println("T:");
        int tValue = sc.nextInt();
        System.out.println("P:");
        int pValue = sc.nextInt();
        String random=sc.nextLine();
        System.out.println(random);
        System.out.println("Do you want to specify the years for which the course is? (Y\\N)");
        String yesNo=sc.nextLine();
        System.out.println(yesNo);
        ArrayList<Integer> years=new ArrayList<>();
        if(yesNo.equals("Y")){
            System.out.println("Enter the years one by one, enter 0 when u wish to not enter a year anymore");
            int year;
            do{
                year=sc.nextInt();
                if(year!=0){
                    years.add(year);
                }
            }while(year!=0);
            random=sc.nextLine();
        }
        System.out.println("Do you want to specify the sem for which the course is? (Y\\N)");
        yesNo=sc.nextLine();
        int sem=0;
        if(yesNo.equals("Y")){
            System.out.println("press 1 for sem 1, press 2 for sem 2, press 0 for both sems:");
            sem=sc.nextInt();
            random = sc.nextLine();
        }
        System.out.println("Any prerequisites? (Y\\N) ");
        yesNo=sc.nextLine();
        String preReq="";
        if(yesNo.equals("Y")){
            System.out.println("Enter all the pre-requisites in a single line without spaces and seperated with a ','");
            preReq = sc.nextLine();
        }
        System.out.println("Department ");
        String department = sc.nextLine();
        System.out.println("Type");
        String type = sc.nextLine();
        query = "insert into course_catalog values (?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, courseCode);
            stmt.setString(2, courseName);
            stmt.setInt(3, lValue);
            stmt.setInt(4, tValue);
            stmt.setInt(5, pValue);
            stmt.setString(6, preReq);
            stmt.setString(7, department);
            stmt.setString(8, type);
            stmt.setInt(9, (lValue+pValue)/2);
            stmt.setArray(10, c.createArrayOf("INT",years.toArray()));
            stmt.setInt(11, sem);
            int count = stmt.executeUpdate();
            c.commit();
            if (count == 1) {
                System.out.println("Course added successfully!");
            } else {
                System.out.println("error");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return;
    }


    public void editCourseCatalog() {
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the course code of the course you wish to edit: ");

        try {
            String course_code = sc.nextLine();
            String query = "select * from course_catalog where course_code=\'" + course_code + "\';";
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next() == false) {
                System.out.println("There is no course with this code!");
                stmt.close();
                c.close();
            } else {
                System.out.println("");
                System.out.println("Enter new name");
                String course_name = sc.nextLine();
                query = "update course_catalog set course_name=? where course_code=?";
                PreparedStatement st = c.prepareStatement(query);
                st.setString(1, course_name);
                st.setString(2, course_code);
                int count = st.executeUpdate();
                c.commit();
                if (count == 1) {
                    System.out.println("Course name updated successfully!");
                } else {
                    System.out.println("error");
                }
                st.close();
            }
            c.close();
        }catch(Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public int endSemester(int curr_year, int curr_sem){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc=new Scanner(System.in);
        int flag=0;
        try{
            String query="select * from enrollments where grade=11";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            if(rs.next()!=false){
                System.out.println("The semester cannot be ended as some students have yet to be graded!");
                flag=0;
            }else{
                query="update admin set curr_year=?, curr_sem=?;";
                PreparedStatement st=c.prepareStatement(query);
                if(curr_sem==1){
                    st.setInt(1,curr_year);
                    st.setInt(2,2);
                }else{
                    st.setInt(1,curr_year+1);
                    st.setInt(2,1);
                }
                int count=st.executeUpdate();
                if(count==0){
                    System.out.println("error");
                    flag=0;
                }else{
                    System.out.println("Sem change successfull");
                    flag=1;
                }
                c.commit();
                st.close();
            }
            stmt.close();
            c.close();
        }catch(Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return flag;
    }
    public void gradesheet(int curr_year, int curr_sem) {
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the loginid of the student whose gradesheet/transcript you wish to generate");

        try {
            String loginid = sc.nextLine();
            int student_year = 0;
            String query = "select * from student where loginid=\'" + loginid + "\';";
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next() == false) {
                System.out.println("there is no student with this id");
                stmt.close();
                c.close();
                return;
            }
            student_year = curr_year - rs.getInt("enrollment_year") + 1;
            String gradesheet = loginid + "gradeSheet.txt";
            File myObj = new File(gradesheet);
            myObj.createNewFile();
            FileWriter fWriter = new FileWriter(gradesheet);
            fWriter.write("Name: "+rs.getString("name")+"\n");
            fWriter.write("ID: "+rs.getString("loginid")+"\n");
            fWriter.write("Total credits: "+rs.getString("total_credits")+"\n");
            fWriter.write("CG: "+rs.getString("grade")+"\n");
            for (int i = 1; i < student_year; i++) {
                query = "select e.course_code, e.grade, c.course_name from enrollments e inner join course_catalog c on c.course_code=e.course_code where student_id=\'" + loginid + "\' and e.year=" + i + " and e.sem=1;";
                rs = stmt.executeQuery(query);
                fWriter.write("\nyear=" + i + ", sem=1\n");
                while (rs.next() != false) {
                    fWriter.write(rs.getString("course_code") + "  " + rs.getString("course_name") + "  " + rs.getInt("grade") + "\n");
                }
                query = "select e.course_code, e.grade, c.course_name from enrollments e inner join course_catalog c on c.course_code=e.course_code where student_id=\'" + loginid + "\' and e.year=" + i + " and e.sem=2;";
                rs = stmt.executeQuery(query);
                fWriter.write("\nyear=" + i + ", sem=2\n");
                while (rs.next() != false) {
                    fWriter.write(rs.getString("course_code") + "  " + rs.getString("course_name") + "  " + rs.getInt("grade") + "\n");
                }
            }
            stmt.close();
            if (curr_sem == 2) {
                query = "select e.course_code, e.grade, c.course_name from enrollments e inner join course_catalog c on c.course_code=e.course_code where student_id=\'" + loginid + "\' and e.year=" + student_year + " and e.sem=1;";
                stmt = c.createStatement();
                rs = stmt.executeQuery(query);
                fWriter.write("\nyear=" + student_year + ", sem=1\n");
                while (rs.next() != false) {
                    fWriter.write(rs.getString("course_code") + "  " + rs.getString("course_name") + "  " + rs.getInt("grade") + "\n");
                }
                stmt.close();
            }
            System.out.println("Gradesheet creates successfully. Exit portal to view.");
            fWriter.close();
            c.close();
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    public void graduationStatus(int curr_year, int curr_sem){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc=new Scanner(System.in);
        try{
        if(curr_sem==2){
            System.out.println("The even sem is ongoing , no student is set to graduate now!");
            c.close();
            return;
        }
        int student_year=curr_year-4;

            String query="select grade, total_credits, pe_credit, pc_credit, oe_credit, name from student where enrollment_year="+student_year+" order by name";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            int i=0;
            while(rs.next()!=false){
                double total_credits=rs.getDouble("total_credits");
                double pc_credit=rs.getDouble("pc_credit");
                double pe_credit=rs.getDouble("pe_credit");
                double oe_credit=rs.getDouble("oe_credit");
                double grade=rs.getDouble("grade");
                    if(total_credits>=145 && pc_credit>=60 && pe_credit>=45 && oe_credit>=40 && grade>=5 ){
                        i++;
                        System.out.println(rs.getString("name")+"        can graduate");
                    }else{
                        System.out.println(rs.getString("name")+"        cannot graduate");
                    }
            }
            System.out.println("Only "+i+" number of students are set to graduate");
            stmt.close();
            c.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }


    public void profile(String loginid){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc=new Scanner(System.in);
        try{
            String query="select * from admin where loginid=\'"+loginid+"\';";
            Statement stmt=c.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            if(rs.next()!=false){
                System.out.println("login id: "+rs.getString("loginid"));
                System.out.println("Current year: "+rs.getInt("curr_year"));
                System.out.println("Current sem: "+rs.getInt("curr_sem"));
            }
            stmt.close();
            c.close();
        }catch(Exception e){
            System.out.println(e);
        }

    }
}
