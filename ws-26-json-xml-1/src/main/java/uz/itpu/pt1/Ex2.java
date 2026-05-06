package uz.itpu.pt1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;

public class Ex2 {
    public static void main(String[] args) throws JsonProcessingException {
        Note note = new Note("title_1", "content_1", new Author("author_name"));

        // Serialization (Object to JSON):
        // Gson
        Gson gson = new Gson();
        String json1 = gson.toJson(note);
        System.out.println("json1: " + json1);

        // Jackson
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json2 = mapper.writeValueAsString(note);
        System.out.println("json2: " + json2);


        // Deserialization (JSON to Object):
        // Gson
        Note note1 = gson.fromJson(json1, Note.class);
        System.out.println(note1);

        // Jackson
        Note note2 = mapper.readValue(json2, Note.class);
        System.out.println(note2);
    }
}
