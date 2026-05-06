package uz.itpu.pt1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;

// Jackson Configuration (ISO 8601)
public class Ex4JacksonCustom {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // 1. Enable Java 8 Date/Time support
        mapper.registerModule(new JavaTimeModule());

        // 2. Force ISO 8601 string format (instead of [2023, 10, 1] arrays)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Note note = new Note("Jackson Tips", "Use modules!", new Author("Jane"), LocalDateTime.now());

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(note);
        System.out.println(json);
    }
}

