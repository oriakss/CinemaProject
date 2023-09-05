package cinema.repository;

import cinema.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    boolean signUp(User user);

    boolean signIn(User user);

    Optional<User> getUserById(Integer userId);

    List<User> getAll();

    boolean IsNotExistAdminAndManager();

    boolean createAdminAndManager(User admin, User manager);
}
