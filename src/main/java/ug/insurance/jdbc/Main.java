package ug.insurance.jdbc;

import java.sql.*;
import java.util.Scanner;
import io.github.cdimascio.dotenv.Dotenv;
import ug.insurance.jdbc.employee.EmployeeCRUD;
import ug.insurance.jdbc.serialMethods.DeserializeEmployee;
import ug.insurance.jdbc.serialMethods.SerializeEmployee;
import ug.insurance.jdbc.threads.ThreadDemo;

public class Main{
    private static Scanner input = new Scanner(System.in);

    private static void testSerial(){
        SerializeEmployee serializeEmployee = new SerializeEmployee();
        serializeEmployee.serialize();
        DeserializeEmployee deserializeEmployee = new DeserializeEmployee();
        deserializeEmployee.deserialize();
    }

    private static void print(String message){
        System.out.println(message);
    }

    public static void printMenu(){
        print("======Choose your option.======");
        print("1. Get employee table details");
        print("2. Get employee details by Id");
        print("3. Register new employee.");
        print("4. Update Employee Department.");
        print("5. Test Serialization");
        print("6. Test Thread");
        print("0. Exit");
    }



    public static void main(String[] args) throws InterruptedException {
        boolean isRunning = true;

        EmployeeCRUD crudOperations = new EmployeeCRUD(input);

        System.out.println("Connecting to url: " + crudOperations.getDburl());

        while(isRunning){
            printMenu();

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
                    testSerial();
                    break;
                case 6:
                    ThreadDemo threadDemo = new ThreadDemo(input);
                    threadDemo.testThreads();
                    break;
                case 0:
                    isRunning = false;
                    break;
            }
        }
    }
}
