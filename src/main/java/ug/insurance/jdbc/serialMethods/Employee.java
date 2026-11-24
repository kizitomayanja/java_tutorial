package ug.insurance.jdbc.serialMethods;

import java.io.Serializable;

public class Employee implements Serializable {
    private String first_name;
    private String last_name;
    private String email;
    private Double salary;
    private int department_id;
    private int employee_id;

    public Employee(
            int employee_id,
            String first_name,
            String last_name,
            String email,
            Double salary,
            int department_id
    ){
        this.employee_id = employee_id;
        this.department_id = department_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.salary = salary;
    }

    public Employee(
            int employee_id,
            String first_name,
            String last_name,
            String email,
            Double salary
    ){
        this.employee_id = employee_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.salary = salary;
    }

    @Override
    public String toString(){
        Integer dep = department_id;
        return "Employee{" +
                "employee_id=" + employee_id +
                ", firstName=" + first_name +
                ", lastName=" + last_name +
                ", email=" + email +
                ", salary=" + salary +
                ((Integer.valueOf(department_id) == null) ? (", department_id=" + Integer.valueOf(department_id).toString()) :  "")
                 +
                "}";
    }
}
