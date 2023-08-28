package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class AdminChoicesTest {
    static ByteArrayOutputStream byteArrayOutputStream;
    @Test
    public void testingAddCourseSameCodeExists(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into course_catalog(course_code) values('course1')";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.addCourse();
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from course_catalog;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Course with the same code already exists!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingAddCourseSuccess(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "course1\ncourse1\n3\n4\n5\nY\n1\n0\nY\n0\nY\ncourse2\ncse\nPC\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.addCourse();
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            String query="delete from course_catalog;";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Course added successfully!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingEditCourseCatalog(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "course1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.editCourseCatalog();
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            String query="delete from course_catalog;";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("There is no course with this code!", actual);
        }catch(Exception e){
            System.out.println(e);
        }

    }


    @Test
    public void testingEditCourseCatalogChangeName(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into course_catalog(course_code,course_name) values('course1','course1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            String input = "course1\nCourse1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.editCourseCatalog();
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from course_catalog;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Course name updated successfully!", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingEndSemester() {
        ConnectDB conn = new ConnectDB();
        Connection c = conn.connect();
        try {
            String query = "insert into student(loginid) values('stu1')";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query = "insert into course_catalog(course_code,course_name) values('course1','course1')";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query = "insert into enrollments(course_code,student_id,grade) values('course1','stu1',11)";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();


            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.endSemester(2023, 1);
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
            stmt.close();
            c.close();
            assertEquals("The semester cannot be ended as some students have yet to be graded!", actual);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testingEndSemesterNoGradePendingSem1() {
        ConnectDB conn = new ConnectDB();
        Connection c = conn.connect();
        try {
            String query = "insert into admin(loginid) values('admin1')";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.endSemester(2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from admin;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Sem change successfull", actual);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testingEndSemesterNoGradePendingSem2() {
        ConnectDB conn = new ConnectDB();
        Connection c = conn.connect();
        try {
            String query = "insert into admin(loginid) values('admin1')";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.endSemester(2023, 2);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query = "delete from admin;";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Sem change successfull", actual);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testingGradesheet(){
        ConnectDB conn = new ConnectDB();
        Connection c = conn.connect();
        try {
            String query = "insert into student(loginid,enrollment_year) values('stu1',2022)";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into course_catalog(course_code) values('course1');";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into course_catalog(course_code) values('course2');";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into course_catalog(course_code) values('course3');";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,year,sem) values('course1','stu1',1,1);";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,year,sem) values('course2','stu1',1,2);";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,year,sem) values('course3','stu1',2,1);";
            stmt = c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            String input = "stu1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.gradesheet(2023, 2);
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


            c.close();
            assertEquals("Gradesheet creates successfully. Exit portal to view.", actual);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testingGradesheetStudentDoesNotExist(){
        ConnectDB conn = new ConnectDB();
        Connection c = conn.connect();
        try{
            String input = "stu1\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.gradesheet(2023, 2);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            c.close();
            assertEquals("there is no student with this id", actual);

        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingGraduationStatusEvenSem(){
        ConnectDB conn = new ConnectDB();
        Connection c = conn.connect();
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.graduationStatus(2023, 2);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            c.close();
            assertEquals("The even sem is ongoing , no student is set to graduate now!", actual);

        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingGraduationStatus(){
        ConnectDB conn = new ConnectDB();
        Connection c = conn.connect();
        try{
            String query="insert into student(loginid,total_credits,pc_credit,pe_credit,oe_credit,enrollment_year,grade) values('stu1',170,70,50,50,2019,6);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into student(loginid,total_credits,pc_credit,pe_credit,oe_credit,enrollment_year,grade) values('stu2',160,60,50,50,2019,4);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.graduationStatus(2023, 1);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from student;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            c.close();

            assertEquals("Only 1 number of students are set to graduate", actual);

        }catch(Exception e){
            System.out.println(e);
        }
    }


    @Test
    public void testingProfile(){
        ConnectDB conn = new ConnectDB();
        Connection c = conn.connect();
        try{
            String query="insert into admin values('admin1','admin1',2023,1);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            AdminChoices adminSelect = new AdminChoices();
            adminSelect.profile("admin1");
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query="delete from admin;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Current sem: 1", actual);

        }catch(Exception e){
            System.out.println(e);
        }
    }
}
