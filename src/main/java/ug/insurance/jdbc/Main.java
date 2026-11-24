package ug.insurance.jdbc;

import java.sql.*;
import java.util.Scanner;
import io.github.cdimascio.dotenv.Dotenv;
import ug.insurance.jdbc.employee.EmployeeCRUD;

public class Main{
    private static Scanner input = new Scanner(System.in);



    public static void main(String[] args){
        boolean isRunning = true;

        EmployeeCRUD crudOperations = new EmployeeCRUD(input);

        System.out.println("Connecting to url: " + crudOperations.getDburl());

        while(isRunning){
            crudOperations.printMenu();
            int choice = input.nextInt();
            input.nextLine();
            switch(choice){
                case 1:
                    crudOperations.printAllDepartments();
                    break;
                case 2:
                    crudOperations.getEmployeeById();
                    break;
                case 3:
                    crudOperations.addNewEmployee();
                    break;
                case 4:
                    crudOperations.assignDepartment();
                    break;
                case 5:
                case 0:
                    isRunning = false;
                    break;
            }
        }
    }
}
