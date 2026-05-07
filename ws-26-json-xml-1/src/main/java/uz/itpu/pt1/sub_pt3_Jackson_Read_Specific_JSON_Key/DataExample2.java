package uz.itpu.pt1.sub_pt3_Jackson_Read_Specific_JSON_Key;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import uz.itpu.pt1.sub_pt1_Jackson_JSON_Example.JacksonObjectMapperExample;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class DataExample2 {
    public static void main(String[] args) throws IOException, URISyntaxException {
        //read json file data to String
        byte[] jsonData = Files.readAllBytes(Paths.get(
                JacksonObjectMapperExample.class.getClassLoader().getResource("employee.txt").toURI()));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //read JSON like DOM Parser
        JsonNode rootNode = objectMapper.readTree(jsonData);

        if (rootNode.has("id")) {
            JsonNode local_id = rootNode.get("id");
            long aLong = local_id.asLong();
            System.out.println("aLong = " + aLong);
        }
        JsonNode idNode = rootNode.path("id");
        System.out.println("id = "+idNode.asInt());

        JsonNode phoneNosNode = rootNode.path("phoneNumbers");
        Iterator<JsonNode> elements = phoneNosNode.elements();
        while(elements.hasNext()){
            JsonNode phone = elements.next();
            System.out.println("Phone No = "+phone.asLong());
        }
    }
}
