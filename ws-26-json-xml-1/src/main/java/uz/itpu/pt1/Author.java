package uz.itpu.pt1;

import java.util.Objects;

public class Author {
    private String name;
    public Author() {}
    public Author(String name) { this.name = name; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Author)) return false;

        Author author = (Author) o;
        return Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                '}';
    }
}
