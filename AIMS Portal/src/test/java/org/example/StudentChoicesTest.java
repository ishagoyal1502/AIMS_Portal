package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;


import static org.junit.jupiter.api.Assertions.*;


class StudentChoicesTest {

static ByteArrayOutputStream byteArrayOutputStream;
    @Test
    public void testingViewOfferings(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into faculty(loginid,name) values(?,?)";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.setString(1,"a");
            stmt.setString(2,"a");
            stmt.executeUpdate();
            c.commit();

            query="insert into course_catalog(course_code,course_name) values(?,?);";
            stmt=c.prepareStatement(query);
            stmt.setString(1,"a");
            stmt.setString(2,"a");
            stmt.executeUpdate();
            c.commit();

            query="insert into offerings(course_code,faculty_id) values(?,?);";
            stmt=c.prepareStatement(query);
            stmt.setString(1,"a");
            stmt.setString(2,"a");
            stmt.executeUpdate();
            c.commit();


            StudentChoices studentSelect=new StudentChoices();
            InputStream stdin = System.in;
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(byteArrayOutputStream);
            PrintStream stdout = System.out;
            System.setOut(ps);
            studentSelect.viewOfferings();
            System.setIn(stdin);
            System.setOut(stdout);
            String outputText = byteArrayOutputStream.toString();
            String output = outputText;

            String expected="a  a  a\r\n";

            query="delete from offerings;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();


            assertEquals(expected,output);
            c.close();
        }catch(Exception e){
            System.out.println(e);
        }




    }

    @Test
    public void testingViewEnrolledHasEnrolled(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into student(loginid) values(?)";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.setString(1,"a");
            stmt.executeUpdate();
            c.commit();

            query="insert into course_catalog(course_code,course_name) values(?,?)";
            stmt=c.prepareStatement(query);
            stmt.setString(1,"a");
            stmt.setString(2,"a");
            stmt.executeUpdate();
            c.commit();

            query="insert into enrollments(course_code,student_id,grade) values(?,?,?)";
            stmt=c.prepareStatement(query);
            stmt.setString(1,"a");
            stmt.setString(2,"a");
            stmt.setInt(3,11);
            stmt.executeUpdate();
            c.commit();

            StudentChoices studentSelect=new StudentChoices();
            InputStream stdin = System.in;
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(byteArrayOutputStream);
            PrintStream stdout = System.out;
            System.setOut(ps);
            studentSelect.viewEnrolled("a");
            System.setOut(stdout);
            String outputText = byteArrayOutputStream.toString();
            String output = outputText;

            String expected="a  a\r\n";


            query="delete from enrollments;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from student;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals(expected,output);

        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingViewEnrolledHasNotEnrolled(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?)";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "a");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,course_name) values(?,?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "a");
            stmt.setString(2, "a");
            stmt.executeUpdate();
            c.commit();


            StudentChoices studentSelect = new StudentChoices();
            InputStream stdin = System.in;
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(byteArrayOutputStream);
            PrintStream stdout = System.out;
            System.setOut(ps);
            studentSelect.viewEnrolled("stu3");
            System.setOut(stdout);
            String outputText = byteArrayOutputStream.toString();
            String output = outputText;


            String expected = "You are currently not enrolled in any course\r\n";

            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals(expected, output);
            //assertEquals(expected,output);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingEnrollCourseDoesNotExist(){
        String input="CC90\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StudentChoices studentSelect=new StudentChoices();
        studentSelect.enroll("a",2023,1);
        String[] lines = out.toString().split(System.lineSeparator());
        String actual = lines[lines.length-1];
        assertEquals("The course does not exist!", actual);
    }

    @Test
    public void testingEnrollCourseIsNotOffered(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into course_catalog(course_code,course_name) values(?,?)";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "a");
            stmt.setString(2, "a");
            stmt.executeUpdate();
            c.commit();


            String input = "a\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.enroll("stu1", 2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();
            assertEquals("The course has not been offered for enrollment!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnrolledAlreadyEnrolled(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?)";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into offerings(course_code,faculty_id) values(?,?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setString(2, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into enrollments(course_code, student_id) values(?,?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setString(2, "stu1");
            stmt.executeUpdate();
            c.commit();



            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.enroll("stu1", 2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from offerings;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("You have already enrolled in this course!", actual);


        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingEnrolledCreditLimitExceeded(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?)";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit) values(?,?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setInt(2,3);
            stmt.executeUpdate();
            c.commit();

            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into offerings(course_code,faculty_id) values(?,?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setString(2, "fac1");
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.enroll("stu1", 2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from offerings;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("Cannot register, credit limit exceeded", actual);


        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnrolledDifferentDepartment(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,prev1_credit,prev2_credit,curr_credit,department) values(?,?,?,?,?)";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.setDouble(2,100);
            stmt.setDouble(3,100);
            stmt.setDouble(4,0);
            stmt.setString(5, "ee");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit,department,type) values(?,?,?,?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setInt(2,3);
            stmt.setString(3, "cse");
            stmt.setString(4, "PC");
            stmt.executeUpdate();
            c.commit();

            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into offerings(course_code,faculty_id) values(?,?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setString(2, "fac1");
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.enroll("stu1", 2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from offerings;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("Cannot register, course is offered by another department!", actual);


        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingEnrolledCgConstraint(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,prev1_credit,prev2_credit,curr_credit,department,grade) values(?,?,?,?,?,?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.setDouble(2,100);
            stmt.setDouble(3,100);
            stmt.setDouble(4,0);
            stmt.setString(5, "cse");
            stmt.setDouble(6, 6);
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit,department,type) values(?,?,?,?);";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setInt(2,3);
            stmt.setString(3, "cse");
            stmt.setString(4, "PC");
            stmt.executeUpdate();
            c.commit();

            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into offerings values(\'course1\',\'fac1\',9,\'{}\')";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.enroll("stu1", 2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from offerings;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("Cannot register, you do not satisfy the CG constraint", actual);


        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnrolledNotForYourYear(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,prev1_credit,prev2_credit,curr_credit,department,grade,enrollment_year) values(?,?,?,?,?,?,?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.setDouble(2,100);
            stmt.setDouble(3,100);
            stmt.setDouble(4,0);
            stmt.setString(5, "cse");
            stmt.setDouble(6, 6);
            stmt.setInt(7, 2023);
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit,department,type) values(?,?,?,?);";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setInt(2,3);
            stmt.setString(3, "cse");
            stmt.setString(4, "PC");
            stmt.executeUpdate();
            c.commit();

            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into offerings values(\'course1\',\'fac1\',0,\'{2}\')";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.enroll("stu1", 2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from offerings;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("Cannot register, course is not offered for your year!", actual);


        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnrolledPrereqNotSatisfied1(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,prev1_credit,prev2_credit,curr_credit,department,grade,enrollment_year) values(?,?,?,?,?,?,?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.setDouble(2,100);
            stmt.setDouble(3,100);
            stmt.setDouble(4,0);
            stmt.setString(5, "cse");
            stmt.setDouble(6, 6);
            stmt.setInt(7, 2023);
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit,department,type,prereq) values(?,?,?,?,?);";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setInt(2,3);
            stmt.setString(3, "cse");
            stmt.setString(4, "PC");
            stmt.setString(5, "course2");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit,department,type) values(\'course2\',3,\'cse\',\'OE\');";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();


            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into offerings values(\'course1\',\'fac1\',0,\'{1}\')";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query = "insert into enrollments(course_code,student_id,grade) values(\'course2\',\'stu1\',11)";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();


            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.enroll("stu1", 2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from offerings;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("Cannot register, you do not satisfy the pre-requisites", actual);


        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnrolledPrereqNotSatisfied2(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,prev1_credit,prev2_credit,curr_credit,department,grade,enrollment_year) values(?,?,?,?,?,?,?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.setDouble(2,100);
            stmt.setDouble(3,100);
            stmt.setDouble(4,0);
            stmt.setString(5, "cse");
            stmt.setDouble(6, 6);
            stmt.setInt(7, 2023);
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit,department,type,prereq) values(?,?,?,?,?);";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setInt(2,3);
            stmt.setString(3, "cse");
            stmt.setString(4, "PC");
            stmt.setString(5, "course2");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit,department,type) values(\'course2\',3,\'cse\',\'OE\');";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();


            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into offerings values(\'course1\',\'fac1\',0,\'{1}\')";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();



            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.enroll("stu1", 2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from offerings;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("Cannot register, you do not satisfy the pre-requisites", actual);


        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnrolledAllCriteriaSatisfied(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,prev1_credit,prev2_credit,curr_credit,department,grade,enrollment_year) values(?,?,?,?,?,?,?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.setDouble(2,100);
            stmt.setDouble(3,100);
            stmt.setDouble(4,0);
            stmt.setString(5, "cse");
            stmt.setDouble(6, 6);
            stmt.setInt(7, 2023);
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit,department,type,prereq) values(?,?,?,?,\'\');";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.setInt(2,3);
            stmt.setString(3, "cse");
            stmt.setString(4, "PC");
            stmt.executeUpdate();
            c.commit();



            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into offerings values(\'course1\',\'fac1\',0,\'{1}\')";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();



            String input = "course1\nY\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.enroll("stu1", 2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from offerings;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("Successfully enrolled", actual);


        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingDropAuditNotEnrolled(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code) values(?);";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.dropAudit("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();

            assertEquals("You are not enrolled in this course!", actual);


        }catch(Exception e){
            System.out.println(e);
        }

    }

    @Test
    public void testingDropAuditCompleteCourse(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code) values(?);";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into enrollments(course_code,student_id,grade) values('course1','stu1',8);";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.dropAudit("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("You cannot drop an already completed course!", actual);


        }catch(Exception e){
            System.out.println(e);
        }

    }

    @Test
    public void testingDropAuditDroppedCourse(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code) values(?);";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into enrollments(course_code,student_id,grade) values('course1','stu1',12);";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.dropAudit("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("You cannot drop an already dropped course!", actual);


        }catch(Exception e){
            System.out.println(e);
        }

    }

    @Test
    public void testingDropAuditDropCourse(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,curr_credit) values(?,10);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into faculty(loginid) values(?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "fac1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,credit) values(?,3);";
            stmt = c.prepareStatement(query);
            stmt.setString(1, "course1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into enrollments(course_code,student_id,grade) values('course1','stu1',11);";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\nY\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.dropAudit("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from faculty;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();

            assertEquals("Course successfully dropped!", actual);


        }catch(Exception e){
            System.out.println(e);
        }

    }


    @Test
    public void testingGetGradesChoiceIsOne(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query = "insert into student(loginid,grade) values(?,10);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();
            String input = "1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.getGrades("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            double cg=10;
            assertEquals("Your cg is "+cg, actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingGetGradesChoiceIsTwoNotEnrolled(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,grade) values(?,10);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();
            String input = "2\ncourse1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.getGrades("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("You are not enrolled in this course", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingGetGradesEnrolledGrade11(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,grade) values(?,10);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code) values('course1');";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query = "insert into enrollments(course_code,student_id,grade) values('course1','stu1',11);";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "2\ncourse1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.getGrades("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();
            assertEquals("The course is currently ongoing", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingGetGradesEnrolledGrade12(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,grade) values(?,10);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code) values('course1');";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query = "insert into enrollments(course_code,student_id,grade) values('course1','stu1',12);";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "2\ncourse1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.getGrades("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();
            assertEquals("The course has been dropped", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingGetGradesEnrolled(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid,grade) values(?,10);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code) values('course1');";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query = "insert into enrollments(course_code,student_id,grade) values('course1','stu1',8);";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "2\ncourse1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.getGrades("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from enrollments;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();
            double grade=8;
            assertEquals("Your grade in this course is "+grade, actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingProfile(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();
            String input = "0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.profile("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Enter 0 to exit profile.", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingProfileEditNameInvalid(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();
            String input = "1\n\n0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.profile("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Enter 0 to exit profile.", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingProfileEditName(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();
            String input = "1\nSTU1\n0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.profile("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Enter 0 to exit profile.", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingProfileCreditDetails(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try {
            String query = "insert into student(loginid) values(?);";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, "stu1");
            stmt.executeUpdate();
            c.commit();
            String input = "2\n0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            StudentChoices studentSelect = new StudentChoices();
            studentSelect.profile("stu1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from student;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Enter 0 to exit profile.", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

}