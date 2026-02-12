package uz.itpu.ajp.isolated;

public interface UserRepository {
    Long save(User user);

    User getUserByEmail(String email);
}
