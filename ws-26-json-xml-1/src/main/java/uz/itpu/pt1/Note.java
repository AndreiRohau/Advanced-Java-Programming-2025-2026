package uz.itpu.pt1;

import com.google.gson.annotations.SerializedName; // For Gson
import com.fasterxml.jackson.annotation.JsonProperty; // For Jackson
import java.time.LocalDateTime;

public class Note {
    @SerializedName("note_title") // Gson mapping
    @JsonProperty("note_title")    // Jackson mapping
    private String title;

    private String content;
    private Author author; // Demonstrates nested JSON
    private LocalDateTime createdAt;

    // Default constructor is required for deserialization
    public Note() {}

    public Note(String title, String content, Author author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }

    public Note(String jacksonTips, String s, Author jane, LocalDateTime now) {
        this(jacksonTips, s, jane);
        this.createdAt = now;
    }

    // Standard Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", createdAt=" + createdAt +
                '}';
    }
}



