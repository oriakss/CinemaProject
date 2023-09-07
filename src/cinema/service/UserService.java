package cinema.service;

import cinema.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    boolean signUp(User user);

    boolean signIn(User user);

    Optional<User> getUserById(Integer userId);

    List<User> getAllUsers();

    boolean IsNotExistAdminAndManager();

    void createAdminAndManager(User admin, User manager);

    boolean deleteAccount(int id);
}
