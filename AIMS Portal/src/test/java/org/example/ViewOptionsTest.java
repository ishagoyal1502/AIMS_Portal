package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static org.junit.jupiter.api.Assertions.*;

class ViewOptionsTest {
    ViewOptions viewOptions=new ViewOptions();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(byteArrayOutputStream);
    PrintStream stdout = System.out;

    @Test
    public void loginIndexIsOne(){
        System.setOut(ps);
        viewOptions.options(1);
        System.setOut(stdout);
        String outputText = byteArrayOutputStream.toString();
        String output = outputText;
        assertEquals("\r\n\r\nSTUDENT OPTIONS\r\nPress 1 to view profile\r\nPress 2 to view enrolled courses\r\nPress 3 to view courses offered for enrollment\r\nPress 4 to enroll in a course\r\nPress 5 to drop in a course\r\nPress 6 to get grades\r\nPress 0 to logout\r\n\r\n\r\n",output);
    }

    @Test
    public void loginIndexIsTwo(){
        System.setOut(ps);
        viewOptions.options(2);
        System.setOut(stdout);
        String outputText = byteArrayOutputStream.toString();
        String output = outputText;
        assertEquals("\r\n\r\nFACULTY OPTIONS\r\nPress 1 to view profile\r\nPress 2 to view course catalog\r\nPress 3 to offer a course\r\nPress 4 to view student grades\r\nPress 5 to enter student grades\r\nPress 6 to view offered courses\r\nPress 0 to logout\r\n\r\n\r\n",output);
    }

    @Test
    public void loginIndexIsThree(){
        System.setOut(ps);
        viewOptions.options(3);
        System.setOut(stdout);
        String outputText = byteArrayOutputStream.toString();
        String output = outputText;
        assertEquals("\r\n\r\nACADEMIC OFFICE OPTIONS\r\nPress 1 to view profile\r\nPress 2 to add a new course to the course catalog\r\nPress 3 to edit course catalog\r\nPress 4 to view student grades\r\nPress 5 to end semester\r\nPress 6 to generate student transcript\r\nPress 7 to view graduation status\r\nPress 0 to logout\r\n\r\n\r\n",output);
    }

}