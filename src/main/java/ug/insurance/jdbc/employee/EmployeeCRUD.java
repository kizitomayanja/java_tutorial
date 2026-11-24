package ug.insurance.jdbc.employee;

import io.github.cdimascio.dotenv.Dotenv;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class EmployeeCRUD {

    private Scanner input;

    public EmployeeCRUD(Scanner scanner){
        this.input = scanner;
    }
    private Dotenv dotenv = Dotenv.load();

    private void print(String message){
        System.out.println(message);
    }

    private String dburl = dotenv.get("dburl");
    private String dbuser = dotenv.get("dbuser");
    private String dbpassword = dotenv.get("dbpassword");
    private String dbUpdate = dotenv.get("dbupdateuser");

    public String getDburl(){
        return this.dburl;
    }

    public void printMenu(){
        System.out.println("======Choose your option.======");
        System.out.println("1. Get employee table details");
        System.out.println("2. Get employee details by Id");
        System.out.println("3. Register new employee.");
        print("4. Update Employee Department.");
        print("5. Test Serialization");
        System.out.println("0. Exit");
    }

    public void getEmployeeById(){

        System.out.println("Enter id of employee");
        int id = input.nextInt();

        String url = dotenv.get("url");
        String user = dotenv.get("dbuser");
        String password = dotenv.get("dbpassword");

        String sql = """
                select 
                    e.employee_id,
                    e.first_name,
                    e.last_name,
                    e.email,
                    e.salary,
                    d.department_name
                from employees e 
                    left join departments d 
                        on e.department_id = d.department_id 
                where e.department_id is not null and e.employee_id = ?;
                """;
        try(Connection connection = DriverManager.getConnection(dburl, dbuser, dbpassword);
            PreparedStatement statement = connection.prepareStatement(sql);

        ){
            statement.setInt(1, id);
            try(ResultSet results = statement.executeQuery()){
                while(results.next()){
                    long employee_id = results.getLong("employee_id");
                    String first_name = results.getString("first_name");
                    String last_name = results.getString("last_name");
                    double salary = results.getDouble("salary");
                    String email = results.getString("email");
                    String department_name = results.getString("department_name");

                    System.out.println("Employee ID Number:  " + employee_id);
                    System.out.println("   Name:         " + first_name + "  " + last_name);
                    System.out.println("   Email:        " + email);
                    System.out.println("   Salary:       " + salary);
                    System.out.println("   Department:   " + department_name);
                }
            }


        }catch(SQLException e){
            System.out.println("Database connection failed");
            e.printStackTrace();
        }
    }

    public void printAllDepartments(){

        String url = dotenv.get("url");
        String user = dotenv.get("dbuser");
        String password = dotenv.get("dbpassword");
//        String url = System.getenv("dburl");
//        String user = System.getenv("dbuser");
//        String password = System.getenv("dbpassword");
        //System.out.println("Printing env variable: " + url);
        String sql_statement = "select e.employee_id, e.first_name, e.last_name, e.email, e.salary, d.department_name from employees e left join departments d on e.department_id = d.department_id;";

        try(Connection connection = DriverManager.getConnection(dburl, dbuser, dbpassword);
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql_statement);
        ){
            System.out.println("Connection to Postgresql Database Successful");
            System.out.println("Database: " + connection.getCatalog());
            //      printResults(results);

            while(results.next()){
                int employeeID = results.getInt("employee_id");
                String first_name = results.getString("first_name");
                String last_name = results.getString("last_name");
                String email = results.getString("email");
                long salary = results.getLong("salary");
                String department = results.getString("department_name");
                System.out.println("Employee ID Number:  " + employeeID);
                System.out.println("   Name:         " + first_name + "  " + last_name);
                System.out.println("   Email:        " + email);
                System.out.println("   Salary:       " + salary);
                System.out.println("   Department:       " + department);
            }
        }
        catch(SQLException e){
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }

    private void registerNewEmployee(String first_name, String last_name,String email, BigDecimal salary, int department){
        String sql = """
                INSERT INTO employees
                (first_name, last_name, email, salary, department_id)
                VALUES
                (?, ?, ?, ?, ?);
                """; // Yeah, I started with this, because why not?

        try(
                Connection conn = DriverManager.getConnection(dburl, dbuser, dbpassword);
                PreparedStatement statement = conn.prepareStatement(sql);
        ){
            statement.setString(1,first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            statement.setBigDecimal(4, salary);
            statement.setInt(5, department);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 0) {
                print("Employee Added Successfully with Department.");
            }else print("I think something happened.");


        }catch (SQLException e){
            print("Database Connection Failed.");
            e.printStackTrace();
        }
    }
    private void registerNewEmployee(String first_name, String last_name,String email, BigDecimal salary){
        String sql = """
                INSERT INTO employees
                (first_name, last_name, email, salary)
                VALUES
                (?, ?, ?, ?);
                """; // Yeah, I started with this, because why not?

        try(
                Connection conn = DriverManager.getConnection(dburl, dbuser, dbpassword);
                PreparedStatement statement = conn.prepareStatement(sql);
                ){
            statement.setString(1,first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            statement.setBigDecimal(4, salary);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 0) {
                print("Employee Added Successfully");
            } else print("I think something wrong happened!");


        }catch (SQLException e){
            print("Database Connection Failed.");
            e.printStackTrace();
        }
    }
    private void printOnlyDepartments(){
        String sql = """
                SELECT * from departments;
""";
        print("Printing available departments");
        try(
                Connection conn = DriverManager.getConnection(dburl, dbuser, dbpassword);
                Statement statement = conn.createStatement();
                ResultSet results = statement.executeQuery(sql);
                ){
            while(results.next()){
                print(results.getInt("department_id") + ".  " + results.getString("department_name"));
            }

        }catch (SQLException e){
            print("Connection Unsuccessful");
        }
    }
    private void printAllEmployees(){
        print("All current employees");
        String sql = """
                SELECT e.employee_id, e.first_name, e.last_name, d.department_name
                FROM employees e
                LEFT JOIN departments d
                ON e.department_id = d.department_id
                ORDER BY e.employee_id asc;
                """;

        try(
                Connection conn = DriverManager.getConnection(dburl, dbuser, dbpassword);
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                ){
            while(resultSet.next()){
                print("Employee Name: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                print("         ID: " + resultSet.getInt("employee_id"));
                print("         Department: " + resultSet.getString("department_name"));
            }
        } catch (SQLException e){
            print("Connection failed");
            e.printStackTrace();
        }
    }

    public void assignDepartment(){
        printAllEmployees();
        print("Enter ID of employee you'd like to assign department: ");
        int id = input.nextInt();
        input.nextLine();
        printOnlyDepartments();
        print("Choose department to assign");
        int dep = input.nextInt();

        String sql = """
                UPDATE employees
                SET department_id = ?
                WHERE employee_id = ?;
                """;

        try(
                Connection conn = DriverManager.getConnection(dburl,dbuser,dbpassword);
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ){
            preparedStatement.setInt(1, dep);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            print(" Department updated successfully.");

        }catch (SQLException e){
            print("Unsuccessful Database connection");
            e.printStackTrace();
        }
    }
    public void addNewEmployee(){
        print("Enter Employee's First name");
        String first_name = input.nextLine();
        print("Enter Employee's Last Name");
        String last_name = input.nextLine();
        print("Enter Employee's Email");
        String email = input.nextLine();
        print("Enter Employee's Salary");
        BigDecimal salary = input.nextBigDecimal();
        print("Are you assigning department now?(This can be done later)\n 1. Yes\n 2. No");
        int choice = input.nextInt();
        switch(choice) {
            case 1: {
                print("Here's the list of departments available.");
                printOnlyDepartments();
                print("Choose department:");
                int dep = input.nextInt();
                registerNewEmployee(first_name,last_name,email,salary,dep);
                input.nextLine();
                break;
            }
            case 2: {
                registerNewEmployee(first_name,last_name,email,salary);
                input.nextLine();
                break;
            }
            default:
                print("Invalid choice! Registration unsuccessful");
                break;

        }



    }
}
