package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    public void testingMainInput0(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Main mainClass = new Main();
            mainClass.main(new String[0]);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            c.close();
            assertEquals("Thank you for using AIMS portal", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingMainInputInvalid(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "4\n0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Main mainClass = new Main();
            mainClass.main(new String[0]);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            c.close();
            assertEquals("Thank you for using AIMS portal", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingMainInputValid1(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "1\nstu1\nstu1\n0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Main mainClass = new Main();
            mainClass.main(new String[0]);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            c.close();
            assertEquals("Thank you for using AIMS portal", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingMainInputValidStudentExistsAndChoice0(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "1\nstu1\nstu1\n0\nY\n0\n";
            String query="insert into student(loginid,password) values('stu1','stu1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Main mainClass = new Main();
            mainClass.main(new String[0]);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query="delete from student;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Thank you for using AIMS portal", actual);
        }catch(Exception e){
            System.out.println(e);
        }


    }

    @Test
    public void testingMainInputValidStudentExistsAndChoice0No(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "1\nstu1\nstu1\n0\nN\n0\nY\n0\n";
            String query="insert into student(loginid,password) values('stu1','stu1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Main mainClass = new Main();
            mainClass.main(new String[0]);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query="delete from student;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Thank you for using AIMS portal", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingMainInputValidStudentExistsAndChoices(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "1\nstu1\nstu1\n1\n2\n3\n4\n5\n6\n0\nY\n0\n";
            String query="insert into student(loginid,password) values('stu1','stu1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into admin(loginid, curr_year,curr_sem) values('admin1',2023,1);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Main mainClass = new Main();
            mainClass.main(new String[0]);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query="delete from student;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from admin;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Thank you for using AIMS portal", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingMainInputValidFacultyExistsAndChoices(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "2\nfac1\nfac1\n1\n2\n3\n4\n5\n6\n0\nY\n0\n";
            String query="insert into faculty(loginid,password) values('fac1','fac1');";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            query="insert into admin(loginid, curr_year,curr_sem) values('admin1',2023,1);";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();

            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Main mainClass = new Main();
            mainClass.main(new String[0]);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];
            query="delete from faculty;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            query="delete from admin;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Thank you for using AIMS portal", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testingMainInputValidAdminExistsAndChoices(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String input = "3\nadmin1\nadmin1\n1\n2\n3\n4\n5\n6\n7\n0\nY\n0\n";
            String query="insert into admin(loginid,password,curr_year,curr_sem) values('admin1','admin1',2023,1);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();



            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            Main mainClass = new Main();
            mainClass.main(new String[0]);
            String[] lines = out.toString().split(System.lineSeparator());
            String actual = lines[lines.length - 1];

            query="delete from admin;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals("Thank you for using AIMS portal", actual);
        }catch(Exception e){
            System.out.println(e);
        }
    }






}