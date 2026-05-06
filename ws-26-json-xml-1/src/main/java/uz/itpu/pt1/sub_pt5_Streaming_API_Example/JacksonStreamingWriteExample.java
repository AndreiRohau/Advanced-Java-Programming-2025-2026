package uz.itpu.pt1.sub_pt5_Streaming_API_Example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import uz.itpu.pt1.sub_pt1_Jackson_JSON_Example.Employee;
import uz.itpu.pt1.sub_pt1_Jackson_JSON_Example.JacksonObjectMapperExample;

/*
JsonParser is the jackson json streaming API to read json data,
we are using it to read data from the file and then parseJSON() method is used
to loop through the tokens and process them to create our java object.
Notice that parseJSON() method is called recursively for “address”
because it’s a nested object in the json data.
For parsing arrays, we are looping through the json document.
We can use JsonGenerator class to generate json data with streaming API.
...
...
JsonGenerator is easy to use in comparison to JsonParser.
That’s all for quick reference tutorial to Jackson JSON Parser Java API.
Jackson JSON Java API is easy to use and provide a lot of options for the ease of developers
working with JSON data. Download project from below link and play around with it
to explore more options about Jackson Json API.
 */
public class JacksonStreamingWriteExample {

    public static void main(String[] args) throws IOException {
        Employee emp = JacksonObjectMapperExample.createEmployee();

        JsonGenerator jsonGenerator = new JsonFactory()
                .createGenerator(new FileOutputStream("stream_emp.txt"));
        //for pretty printing
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());

        jsonGenerator.writeStartObject(); // start root object
        jsonGenerator.writeNumberField("id", emp.getId());
        jsonGenerator.writeStringField("name", emp.getName());
        jsonGenerator.writeBooleanField("permanent", emp.isPermanent());

        jsonGenerator.writeObjectFieldStart("address"); //start address object
        jsonGenerator.writeStringField("street", emp.getAddress().getStreet());
        jsonGenerator.writeStringField("city", emp.getAddress().getCity());
        jsonGenerator.writeNumberField("zipcode", emp.getAddress().getZipcode());
        jsonGenerator.writeEndObject(); //end address object

        jsonGenerator.writeArrayFieldStart("phoneNumbers");
        for(long num : emp.getPhoneNumbers())
            jsonGenerator.writeNumber(num);
        jsonGenerator.writeEndArray();

        jsonGenerator.writeStringField("role", emp.getRole());

        jsonGenerator.writeArrayFieldStart("cities"); //start cities array
        for(String city : emp.getCities())
            jsonGenerator.writeString(city);
        jsonGenerator.writeEndArray(); //closing cities array

        jsonGenerator.writeObjectFieldStart("properties");
        Set<String> keySet = emp.getProperties().keySet();
        for(String key : keySet){
            String value = emp.getProperties().get(key);
            jsonGenerator.writeStringField(key, value);
        }
        jsonGenerator.writeEndObject(); //closing properties
        jsonGenerator.writeEndObject(); //closing root object

        jsonGenerator.flush();
        jsonGenerator.close();
    }

}
