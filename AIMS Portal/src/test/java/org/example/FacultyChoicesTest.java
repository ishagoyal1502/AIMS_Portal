package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class FacultyChoicesTest {

    @Test
    public void testingViewCourseCatalog(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into course_catalog(course_code) values('course1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.viewCourseCatalog();
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[0];

            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("The course catalog: ", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingOfferCourseCourseDoesNotExist(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.offerCourse("fac1",2023,1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            c.close();
            assertEquals("Invalid course code. Try again!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingOfferCourseCourseIsAlreadyOffered(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into course_catalog(course_code) values('course1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into faculty(loginid) values('fac1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into offerings(course_code,faculty_id) values('course1','fac1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();



            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.offerCourse("fac1",2023,1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

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
            stmt.close();
            c.close();
            assertEquals("Course has already been offered this sem!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingOfferCourseCannotBeOfferedWrongSem(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into course_catalog(course_code,sem) values('course1',1);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into faculty(loginid) values('fac1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.offerCourse("fac1",2023,2);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Course cannot be offered in this semester!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingOfferCourseCannotBeOfferedWrongDepartment(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into course_catalog(course_code,sem,department) values('course1',0,'cse');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into faculty(loginid,department) values('fac1','ee');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.offerCourse("fac1",2023,2);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Course cannot be offered, is from another department!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingOfferCourseCanBeOfferedYearMentioned(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into course_catalog(course_code,sem,department,year) values('course1',0,'cse','{1}');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into faculty(loginid,department) values('fac1','cse');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\nY\n5\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.offerCourse("fac1",2023,2);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

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

            stmt.close();
            c.close();
            assertEquals("Course offered successfully!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingOfferCourseCanBeOfferedYearNotMentioned(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into course_catalog(course_code,sem,department,year) values('course1',0,'cse','{}');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into faculty(loginid,department) values('fac1','cse');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\nY\n5\n1\n2\n0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.offerCourse("fac1",2023,2);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

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

            stmt.close();
            c.close();
            assertEquals("Course offered successfully!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingProfileEdit(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{

            String query="insert into faculty(loginid,department,name) values('fac1','cse','faculty1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "1\nFaculty1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.profile("fac1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();
            assertEquals("name changes successfully!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingProfile(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{

            String query="insert into faculty(loginid,department,name) values('fac1','cse','faculty1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.profile("fac1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();
            assertEquals("Enter 0 to exit profile", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnterGradesCourseDoesNotExist(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.enterGrades("fac1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];


            c.close();
            assertEquals("The course does not exist!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnterGradesCourseIsNotOffered(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into faculty(loginid) values('fac1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into course_catalog(course_code) values('course1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();


            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.enterGrades("fac1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();

            c.close();
            assertEquals("This course has no offerings!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnterGradesCourseIsNotOfferedByYou(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into faculty(loginid) values('fac1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into faculty(loginid) values('fac2');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into course_catalog(course_code) values('course1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into offerings(course_code,faculty_id) values('course1','fac2');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();



            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.enterGrades("fac1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from offerings;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();

            c.close();
            assertEquals("You are not the instructor for this course!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingEnterGradesCourseHasNoEnrollments(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into faculty(loginid) values('fac1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into course_catalog(course_code) values('course1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into offerings(course_code,faculty_id) values('course1','fac1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();


            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.enterGrades("fac1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from offerings;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();

            c.close();
            assertEquals("The course has no enrollments!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingEnterGradesCourseHasEnrollments(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into faculty(loginid) values('fac1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into course_catalog(course_code) values('course1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();


            query="insert into student(loginid) values('stu1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into student(loginid) values('stu2');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into student(loginid) values('stu3');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into student(loginid) values('stu4');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into student(loginid) values('stu5');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into student(loginid) values('stu6');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into student(loginid) values('stu7');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into student(loginid) values('stu8');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into student(loginid) values('stu9');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into offerings(course_code,faculty_id) values('course1','fac1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into enrollments(course_code,student_id,grade) values('course1','stu1',11);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course1','stu2',11);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course1','stu3',11);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course1','stu4',11);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course1','stu5',11);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course1','stu6',11);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course1','stu7',11);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course1','stu8',11);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course1','stu9',11);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();


            String input = "sample_csv_file.csv\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.enterGrades("fac1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from offerings;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from enrollments;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from student;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();

            c.close();
            assertEquals("Grades updated!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingViewOfferedCourses(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into faculty(loginid) values('fac1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into course_catalog(course_code) values('course1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into offerings(course_code,faculty_id) values('course1','fac1');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            FacultyChoices facultySelect = new FacultyChoices();
            facultySelect.viewOfferedCourses("fac1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[0];

            query="delete from offerings;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();

            c.close();
            assertEquals("Courses offered by you in this semester are: ", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }




}