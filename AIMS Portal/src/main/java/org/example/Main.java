package org.example;
import java.util.*;
import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        ConnectDB conn=new ConnectDB();
        Connection c=conn.connect();
        Scanner sc = new Scanner(System.in);
        int login_index;
        do {
            System.out.println("");
            System.out.println("");
            System.out.println("Press 1 to login as Student");
            System.out.println("Press 2 to login as Faculty");
            System.out.println("Press 3 to login as Academic Office");
            System.out.println("Press 0 to EXIT");
            System.out.println("");
            System.out.println("");
            login_index = sc.nextInt();

            if (login_index == 0) {
                System.out.println("Thank you for using AIMS portal");
                try{
                    c.close();
                }catch(Exception e){
                    System.out.println(e);
                }

                return;
            }
            if (login_index != 1 && login_index != 2 && login_index != 3) {
                System.out.println("Invalid input!");
            } else {
                String random = sc.nextLine();
                System.out.println("Login ID: ");
                String login_id = sc.nextLine();
                System.out.println("Password: ");
                String pass = sc.nextLine();

                LoginUser newUser = new LoginUser();
                int result = newUser.log(login_index, login_id, pass);

                System.out.println("");
                if (result == 1) {

                    ViewOptions view = new ViewOptions();
                    int choice;
                    do {
                        view.options(login_index);

                        choice = sc.nextInt();
                        random = sc.nextLine();
                        if (choice == 0) {
                            System.out.println("Are you sure you want to logout? (Y\\N)");
                            String logout = sc.nextLine();
                            if (logout.equals("Y")) {
                                System.out.println("Logged out succesfully!");
                            } else {
                                System.out.println("Still logged in!");
                                choice=-1;
                            }
                        } else {
                            String query = "select curr_year,curr_sem from admin";
                            int curr_year = 0;
                            int curr_sem = 0;
                            try {
                                Statement stmt = c.createStatement();
                                ResultSet rs = stmt.executeQuery(query);
                                if (rs.next() != false) {
                                    curr_year = rs.getInt("curr_year");
                                    curr_sem = rs.getInt("curr_sem");
                                }
                                stmt.close();
                            } catch (Exception e) {
                                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                            }


                            switch (login_index) {
                                case 1:
                                    StudentChoices studentSelect = new StudentChoices();
                                    //studentSelect.choices(choice,c);
                                    if(choice==1){
                                        studentSelect.profile(login_id);
                                    }else if(choice==2){
                                        studentSelect.viewEnrolled(login_id);
                                    }else if (choice == 3) {
                                        studentSelect.viewOfferings();
                                    }else if(choice==4){
                                        studentSelect.enroll(login_id,curr_year,curr_sem);
                                    }else if(choice==5 ){
                                        studentSelect.dropAudit(login_id);
                                    }else if(choice==6){
                                        studentSelect.getGrades(login_id);
                                    }
                                    break;
                                case 2:
                                    FacultyChoices facultySelect = new FacultyChoices();
                                    if(choice==1){
                                        facultySelect.profile(login_id);
                                    }else if (choice == 2) {
                                        facultySelect.viewCourseCatalog();
                                    } else if (choice == 3) {
                                        facultySelect.offerCourse(login_id, curr_year, curr_sem);
                                    }else if(choice==4){
                                        Common commonSelect=new Common();
                                        commonSelect.viewGrades(curr_year);
                                    }else if(choice==5){
                                        facultySelect.enterGrades(login_id);
                                    }else if(choice==6){
                                        facultySelect.viewOfferedCourses(login_id);
                                    }
                                    break;
                                case 3:
                                    AdminChoices adminSelect = new AdminChoices();
                                    if(choice==1){
                                        adminSelect.profile(login_id);
                                    }else if (choice == 2) {
                                        adminSelect.addCourse();
                                    } else if(choice==3){
                                        adminSelect.editCourseCatalog();
                                    }else if (choice == 4) {
                                        Common commonSelect=new Common();
                                        commonSelect.viewGrades(curr_year);
                                    }else if(choice==5){
                                        int flag=adminSelect.endSemester(curr_year,curr_sem);
                                        if(flag==1){
                                            Common commonSelect=new Common();
                                            commonSelect.calculateGrade();
                                        }
                                    }else if(choice==6){
                                        adminSelect.gradesheet(curr_year,curr_sem);
                                    }else if(choice==7){
                                        adminSelect.graduationStatus(curr_year,curr_sem);
                                    }
                                    break;

                            }
                        }
                    }while(choice!=0);


                }
            }

        }while(login_index!=0);
    }
}