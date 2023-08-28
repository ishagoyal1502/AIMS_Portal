package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class CommonTest {

    @Test
    public void testingViewGrades(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{

            String query="insert into student(loginid,department,name) values('stu1','cse','student1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Common commonSelect = new Common();
            commonSelect.viewGrades(2023);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[0];

            query="delete from student;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            stmt.close();
            c.close();
            assertEquals("name        year        grade", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingCalculateGrade(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{

            String query="insert into student(loginid,total_credits,curr_credit,prev1_credit) values('stu1',60,20,24);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into course_catalog(course_code,credit,type) values('course1',4,'PE');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into course_catalog(course_code,credit,type) values('course2',4,'PC');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into course_catalog(course_code,credit,type) values('course3',4,'OE');";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into enrollments(course_code,student_id,grade) values('course1','stu1',8);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course2','stu1',8);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="insert into enrollments(course_code,student_id,grade) values('course3','stu1',8);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();



            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Common commonSelect = new Common();
            commonSelect.calculateGrade();
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

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
            assertEquals("successfully changed", actual);
        }catch(Exception e){
            System.out.println(e);
        }

    }

}