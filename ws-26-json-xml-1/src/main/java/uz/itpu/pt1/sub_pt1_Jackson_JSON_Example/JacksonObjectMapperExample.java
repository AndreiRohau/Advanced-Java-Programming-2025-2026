package uz.itpu.pt1.sub_pt1_Jackson_JSON_Example;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

// Jackson JSON Example
public class JacksonObjectMapperExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        //read json file data to String
        byte[] jsonData = Files.readAllBytes(Paths.get(
                JacksonObjectMapperExample.class.getClassLoader().getResource("employee.txt").toURI()));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object
        Employee emp = objectMapper.readValue(jsonData, Employee.class);

        System.out.println("Employee Object\n"+emp);

        //convert Object to json string
        Employee emp1 = createEmployee();
        //configure Object mapper for pretty print
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //writing to console, can write to any output stream such as file
        StringWriter stringEmp = new StringWriter();
        objectMapper.writeValue(stringEmp, emp1);
        System.out.println("Employee JSON is\n"+stringEmp);
    }

    public static Employee createEmployee() {

        Employee emp = new Employee();
        emp.setId(100);
        emp.setName("David");
        emp.setPermanent(false);
        emp.setPhoneNumbers(new long[] { 123456, 987654 });
        emp.setRole("Manager");

        Address add = new Address();
        add.setCity("Bangalore");
        add.setStreet("BTM 1st Stage");
        add.setZipcode(560100);
        emp.setAddress(add);

        List<String> cities = new ArrayList<String>();
        cities.add("Los Angeles");
        cities.add("New York");
        emp.setCities(cities);

        Map<String, String> props = new HashMap<String, String>();
        props.put("salary", "1000 Rs");
        props.put("age", "28 years");
        emp.setProperties(props);

        return emp;
    }

}

