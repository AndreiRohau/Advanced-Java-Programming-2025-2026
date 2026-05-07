package uz.itpu.pt1.sub_pt2_json_to_map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import uz.itpu.pt1.sub_pt1_Jackson_JSON_Example.JacksonObjectMapperExample;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

// Jackson JSON - Converting JSON to Map
public class DataExample {
    public static void main(String[] args) throws IOException, URISyntaxException {
        //converting json to Map
        byte[] mapData = Files.readAllBytes(Paths.get(
                JacksonObjectMapperExample.class.getClassLoader().getResource("data.txt").toURI()));
        Map<String,String> myMap = new HashMap<String, String>();

        ObjectMapper objectMapper = new ObjectMapper();

        myMap = objectMapper.readValue(mapData, HashMap.class);
        System.out.println("Map is: "+myMap);

        //another way
        myMap = objectMapper.readValue(mapData, new TypeReference<HashMap<String,String>>() {});
        System.out.println("Map using TypeReference: "+myMap);
    }
}
