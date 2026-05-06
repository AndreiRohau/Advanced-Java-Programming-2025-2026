package uz.itpu.pt1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;

// For java.util.Date, use setDateFormat.
// For modern java.time types, you may need a custom adapter or a standard string pattern.
public class Ex4GsonCustom {
    public static void main(String[] args) {
        // Configure ISO 8601 date pattern
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .setPrettyPrinting()
                .create();

        Note note = new Note("Gson Tips", "Use builders!", new Author("John"), LocalDateTime.now());

        String json = gson.toJson(note);
        System.out.println(json);
    }
}

