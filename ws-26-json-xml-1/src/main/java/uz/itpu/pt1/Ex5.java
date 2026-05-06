package uz.itpu.pt1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class Ex5 {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        System.out.println("Raw JSON: " + body);

        // Parse JSON body with Jackson
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> post = mapper.readValue(body, new TypeReference<>() {});
        System.out.println("userId : " + post.get("userId"));
        System.out.println("id     : " + post.get("id"));
        System.out.println("title  : " + post.get("title"));
        System.out.println("body   : " + post.get("body"));
    }
}
