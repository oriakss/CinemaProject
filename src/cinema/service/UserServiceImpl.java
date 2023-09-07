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
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public boolean IsNotExistAdminAndManager() {
        return userRepository.IsNotExistAdminAndManager();
    }

    @Override
    public void createAdminAndManager(User admin, User manager) {
        userRepository.createAdminAndManager(admin, manager);
    }

    @Override
    public boolean deleteAccount(int id) {
        return userRepository.deleteAccount(id);
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
