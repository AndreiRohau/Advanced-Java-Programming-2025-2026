package uz.itpu.pt1.sub_pt4_Edit_JSON_Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uz.itpu.pt1.sub_pt1_Jackson_JSON_Example.JacksonObjectMapperExample;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

// If you will execute above code and look for the new file,
// you will notice that it doesn’t have “role” and “properties” key.
// You will also notice that “id” value is updated to 500 and
// a new key “test” is added to updated_emp.txt file.
public class EditJsonDocExample {
    public static void main(String[] args) throws IOException, URISyntaxException {
        byte[] jsonData = Files.readAllBytes(Paths.get(
                JacksonObjectMapperExample.class.getClassLoader().getResource("employee.txt").toURI()));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter();

        //create JsonNode
        JsonNode rootNode = objectMapper.readTree(jsonData);

        //update JSON data
        ((ObjectNode) rootNode).put("id", 500);
        //add new key value
        ((ObjectNode) rootNode).put("test", "test value");
        //remove existing key
        ((ObjectNode) rootNode).remove("role");
        ((ObjectNode) rootNode).remove("properties");
        objectMapper.writeValue(new File("updated_emp.txt"), rootNode);
    }
}
