package cinema.service;

import cinema.model.User;
import cinema.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean signUp(User user) {
        return userRepository.signUp(user);
    }

    @Override
    public boolean signIn(User user) {
        return userRepository.signIn(user);
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public List<User> getAll() {
        userRepository.getAll().forEach(System.out::println);
        return null;
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
