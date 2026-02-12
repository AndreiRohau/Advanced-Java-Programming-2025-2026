package uz.itpu.ajp.isolated;

import static java.util.Objects.nonNull;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserServiceValidator userServiceValidator;

    public UserServiceImpl(UserRepository userRepository,
                       UserServiceValidator userServiceValidator) {
        this.userRepository = userRepository;
        this.userServiceValidator = userServiceValidator;
    }

    @Override
    public Long saveUser(User user) {
        userServiceValidator.validate(user);
        User userByEmail = userRepository.getUserByEmail(user.getEmail());
        if (nonNull(userByEmail)) {
            throw new RuntimeException("User with this email already exists. Use another email for registration.");
        }
        Long id = userRepository.save(user);
        return id;
    }
}
