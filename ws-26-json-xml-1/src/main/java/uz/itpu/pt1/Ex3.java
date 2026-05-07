package uz.itpu.pt1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ex3 {
    public static void main(String[] args) throws JsonProcessingException {
        // JSON Arrays and Collections
        String arr = "[{\"content\":\"content_1\",\"author\":{\"name\":\"author_name\"},\"createdAt\":\"2026-05-07T00:21:06.9036301\",\"note_title\":\"title_1\"}, {}, null]";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, ctx) ->
                                LocalDateTime.parse(json.getAsString()))
                .create();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Read JSON Arrays:
        // Gson
        Type listType = new TypeToken<ArrayList<Note>>(){}.getType();
        List<Note> notes1 = gson.fromJson(arr, listType);
        System.out.println(notes1);

        // Jackson
        List<Note> notes2 = mapper.readValue(arr, new TypeReference<List<Note>>(){});
        System.out.println(notes2);
        System.out.println();

    }
}
