package uz.itpu.pt1;

public class Ex1 {
    public static void main(String[] args) {
        String jsonString = "{\"title\": \"My Note\", \"content\": \"Hello World\"}";
        String jsonStringArr = "[{\"title\": \"My Note\", \"content\": \"Hello World\"},{}]";
        String jsonStringArrNull = "[{\"title\": \"My Note\", \"content\": \"Hello World\"}, {}, null]";
        String jsonStringComprehensive = "[{\"title\": \"My Note\", \"content\": \"Hello World\"}, {\"arr\": [{},null,\"\"], \"obj\": {}, \"nul\": null}, null]";
        System.out.println(jsonString);
    }
}
