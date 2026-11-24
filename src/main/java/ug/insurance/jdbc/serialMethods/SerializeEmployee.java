package ug.insurance.jdbc.serialMethods;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializeEmployee {

    public void serialize(){
        Employee employee = new Employee(
                1,
                "Jane",
                "Smith",
                "jane.smith@does.com",
                1_000_000.0,
                1);

        try(
                FileOutputStream fileOut = new FileOutputStream("employee.ser");
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        ){
            objectOut.writeObject(employee);
            System.out.println("Employee serialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
