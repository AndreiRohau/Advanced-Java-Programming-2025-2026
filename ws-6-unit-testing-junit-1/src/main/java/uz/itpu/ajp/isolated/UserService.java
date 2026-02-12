package uz.itpu.ajp.isolated;

public interface UserService {
    /**
     * Saves user and returns generated id
     * @param user
     * @return id of saved user
     */
    Long saveUser(User user);
}
