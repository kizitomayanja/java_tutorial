package ug.insurance.jdbc.serialMethods;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class DeserializeEmployee {

    public void deserialize(){
        try(
                FileInputStream fileIn = new FileInputStream("employee.ser");
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                ){
            Employee employee = (Employee) objectIn.readObject();
            System.out.println(employee);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
