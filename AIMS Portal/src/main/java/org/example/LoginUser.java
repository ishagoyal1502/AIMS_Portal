package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

class LoginUser {
    public int log(int login_index, String login_id, String pass){
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        String query="";
        int ret;
        switch (login_index) {
            case 1:
                query = "select * from student where loginid=\'" + login_id + "\' AND password=\'" + pass + "\';";
                break;
            case 2:
                query = "select * from faculty where loginid=\'" + login_id + "\' AND password=\'" + pass + "\';";
                break;
            case 3:
                query = "select * from admin where loginid=\'" + login_id + "\' AND password=\'" + pass + "\';";
                break;
        }
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()==false){
                System.out.println("Invalid login id or password");
                ret=0;
            }else{
                System.out.println("Welcome "+ login_id);
                ret=1;
            }
            stmt.close();
            c.close();
        }catch(Exception e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            ret=0;
        }


        return ret;


    }
}
