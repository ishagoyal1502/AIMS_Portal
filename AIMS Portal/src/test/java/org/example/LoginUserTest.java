package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class LoginUserTest {

    @Test
    public void loginAsStudentSuccess(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into student (loginid,password) values(?,?);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.setString(1,"stu1");
            stmt.setString(2,"stu1");
            stmt.executeUpdate();
            c.commit();

            LoginUser loginUser=new LoginUser();
            int result=loginUser.log(1,"stu1","stu1");
            query="delete from student;";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals(1,result);
        }catch(Exception e){
            System.out.println(e);
        }

    }

    @Test
    public void loginAsFacultySuccess(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into faculty (loginid,password) values(?,?);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.setString(1,"fac1");
            stmt.setString(2,"fac1");
            stmt.executeUpdate();
            c.commit();

            LoginUser loginUser=new LoginUser();
            int result=loginUser.log(2,"fac1","fac1");

            query="delete from faculty";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals(1,result);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void loginAsAdminSuccess(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into admin (loginid,password) values(?,?);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.setString(1,"admin1");
            stmt.setString(2,"admin1");
            stmt.executeUpdate();
            c.commit();
            LoginUser loginUser=new LoginUser();
            int result=loginUser.log(3,"admin1","admin1");

            query="delete from admin";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals(1,result);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void loginAsStudentFail(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into student (loginid,password) values(?,?);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.setString(1,"stu1");
            stmt.setString(2,"stu1");
            stmt.executeUpdate();
            c.commit();

            LoginUser loginUser=new LoginUser();
            int r=loginUser.log(1,"st1","stu1");

            query="delete from student";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals(0,r);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void loginAsFacultyFail(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into faculty (loginid,password) values(?,?);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.setString(1,"fac1");
            stmt.setString(2,"fac1");
            stmt.executeUpdate();
            c.commit();

            LoginUser loginUser=new LoginUser();
            int r=loginUser.log(2,"stu1","stu1");

            query="delete from faculty";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals(0,r);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    @Test
    public void loginAsAdminFail(){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        try{
            String query="insert into admin (loginid,password) values(?,?);";
            PreparedStatement stmt=c.prepareStatement(query);
            stmt.setString(1,"admin1");
            stmt.setString(2,"admin1");
            stmt.executeUpdate();
            c.commit();

            LoginUser loginUser=new LoginUser();
            int r=loginUser.log(3,"stu1","stu1");

            query="delete from admin";
            stmt=c.prepareStatement(query);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            assertEquals(0,r);
        }catch(Exception e){
            System.out.println(e);
        }
    }

}