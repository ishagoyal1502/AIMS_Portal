package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

class ConnectDB {
    Connection c = null;
    public Connection connect(){
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aims","postgres", "postgresql");
            c.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        //System.out.println("Opened database successfully");
        return c;
    }

    public void close(){
        try{
            c.close();
        } catch (Exception e){
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
}
