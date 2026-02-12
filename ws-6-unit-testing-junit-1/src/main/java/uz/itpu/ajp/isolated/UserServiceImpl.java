package uz.itpu.ajp.isolated;

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
        Long id = userRepository.save(user);
        return id;
    }
}
