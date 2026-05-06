package uz.itpu.pt1.sub_pt5_Streaming_API_Example;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import uz.itpu.pt1.sub_pt1_Jackson_JSON_Example.Address;
import uz.itpu.pt1.sub_pt1_Jackson_JSON_Example.Employee;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
Streaming API Example

Jackson JSON Java API also provide streaming support that is helpful in working with
large json data because it reads the whole file as tokens and uses less memory.
The only problem with streaming API is that we need to take care of all the tokens
while parsing the JSON data. If we have json data as {“role”:“Manager”} then we will get following
tokens in order - { (start object), “role” (key name), “Manager” (key value) and } (end object).
Colon (:) is the delimiter in JSON and hence not considered as a token.
 */
public class JacksonStreamingReadExample {
    public static void main(String[] args) throws JsonParseException, IOException, URISyntaxException {

        //create JsonParser object
        File file = new File(JacksonStreamingReadExample.class.getClassLoader().getResource("employee.txt").toURI());
        JsonParser jsonParser = new JsonFactory().createParser(file);

        //loop through the tokens
        Employee emp = new Employee();
        Address address = new Address();
        emp.setAddress(address);
        emp.setCities(new ArrayList<String>());
        emp.setProperties(new HashMap<String, String>());
        List<Long> phoneNums = new ArrayList<Long>();
        boolean insidePropertiesObj=false;

        parseJSON(jsonParser, emp, phoneNums, insidePropertiesObj);

        long[] nums = new long[phoneNums.size()];
        int index = 0;
        for(Long l :phoneNums){
            nums[index++] = l;
        }
        emp.setPhoneNumbers(nums);

        jsonParser.close();
        //print employee object
        System.out.println("Employee Object\n\n" + emp);
    }

    private static void parseJSON(JsonParser jsonParser, Employee emp,
                                  List<Long> phoneNums, boolean insidePropertiesObj) throws JsonParseException, IOException {

        //loop through the JsonTokens
        while(jsonParser.nextToken() != JsonToken.END_OBJECT){
            String name = jsonParser.getCurrentName();
            if("id".equals(name)){
                jsonParser.nextToken();
                emp.setId(jsonParser.getIntValue());
            }else if("name".equals(name)){
                jsonParser.nextToken();
                emp.setName(jsonParser.getText());
            }else if("permanent".equals(name)){
                jsonParser.nextToken();
                emp.setPermanent(jsonParser.getBooleanValue());
            }else if("address".equals(name)){
                jsonParser.nextToken();
                //nested object, recursive call
                parseJSON(jsonParser, emp, phoneNums, insidePropertiesObj);
            }else if("street".equals(name)){
                jsonParser.nextToken();
                emp.getAddress().setStreet(jsonParser.getText());
            }else if("city".equals(name)){
                jsonParser.nextToken();
                emp.getAddress().setCity(jsonParser.getText());
            }else if("zipcode".equals(name)){
                jsonParser.nextToken();
                emp.getAddress().setZipcode(jsonParser.getIntValue());
            }else if("phoneNumbers".equals(name)){
                jsonParser.nextToken();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    phoneNums.add(jsonParser.getLongValue());
                }
            }else if("role".equals(name)){
                jsonParser.nextToken();
                emp.setRole(jsonParser.getText());
            }else if("cities".equals(name)){
                jsonParser.nextToken();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    emp.getCities().add(jsonParser.getText());
                }
            }else if("properties".equals(name)){
                jsonParser.nextToken();
                while(jsonParser.nextToken() != JsonToken.END_OBJECT){
                    String key = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    String value = jsonParser.getText();
                    emp.getProperties().put(key, value);
                }
            }
        }
    }
}
