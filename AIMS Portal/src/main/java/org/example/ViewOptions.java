package org.example;

class ViewOptions {

    public void options(int login_index){
        switch (login_index){
            case 1:
                System.out.println("");
                System.out.println("");
                System.out.println("STUDENT OPTIONS");
                System.out.println("Press 1 to view profile"); //done
                System.out.println("Press 2 to view enrolled courses"); //done
                System.out.println("Press 3 to view courses offered for enrollment");//done
                System.out.println("Press 4 to enroll in a course"); //done
                System.out.println("Press 5 to drop in a course"); //done
                //System.out.println("Press 6 to audit in a course");
                System.out.println("Press 6 to get grades"); //done
                System.out.println("Press 0 to logout");
                System.out.println("");
                System.out.println("");
                break;
            case 2:
                System.out.println("");
                System.out.println("");
                System.out.println("FACULTY OPTIONS");
                System.out.println("Press 1 to view profile");
                System.out.println("Press 2 to view course catalog"); //done
                System.out.println("Press 3 to offer a course"); //done
                System.out.println("Press 4 to view student grades");
                System.out.println("Press 5 to enter student grades");
                System.out.println("Press 6 to view offered courses");

                System.out.println("Press 0 to logout");
                System.out.println("");
                System.out.println("");
                break;
            case 3:
                System.out.println("");
                System.out.println("");
                System.out.println("ACADEMIC OFFICE OPTIONS");
                System.out.println("Press 1 to view profile");//done
                System.out.println("Press 2 to add a new course to the course catalog"); //done
                System.out.println("Press 3 to edit course catalog"); //done
                System.out.println("Press 4 to view student grades");
                System.out.println("Press 5 to end semester"); //done
                System.out.println("Press 6 to generate student transcript");//done
                System.out.println("Press 7 to view graduation status");//done
                System.out.println("Press 0 to logout");
                System.out.println("");
                System.out.println("");
                break;
        }
        return;
    }

}
