package cinema.repository;

import cinema.exeptions.UserExistsException;
import cinema.exeptions.UserNotFound;
import cinema.exeptions.WrongPasswordException;
import cinema.model.User;
import cinema.model.UserRole;
import cinema.util.ConnectionManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cinema.util.PasswordHashing.hashPassword;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private boolean checkUserByLogin(String login) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT username FROM users WHERE username=?");
            stmt.setString(1, login);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next() && resultSet.getString("username").equals(login)) {
                return false;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean signUp(User user) {
        try (Connection connection = ConnectionManager.open()) {
            boolean isNotExistUser = checkUserByLogin(user.getLogin());
            if (isNotExistUser) {
                PreparedStatement stmt = connection
                        .prepareStatement("INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)");
                stmt.setString(1, user.getLogin());
                String[] passAndSalt = hashPassword(user.getPassword(), null);
                stmt.setString(2, passAndSalt[0]);
                stmt.setString(3, passAndSalt[1]);
                stmt.setString(4, UserRole.USER.name());
                return !stmt.execute();
            } else
                throw new UserExistsException(String.format("Login '%s' already exists!", user.getLogin()));
        } catch (UserExistsException e) {
            System.out.println();
            System.out.println(e.getMessage());
            return false;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean signIn(User user) {
        try (Connection connection = ConnectionManager.open()) {
            boolean isNotExistUserLogin = checkUserByLogin(user.getLogin());
            if (isNotExistUserLogin) {
                throw new UserNotFound(
                        String.format("User with login '%s' does not exist!", user.getLogin()));
            } else {
                PreparedStatement stmt = connection
                        .prepareStatement("SELECT password, salt FROM users WHERE username=?");
                stmt.setString(1, user.getLogin());
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    String password = resultSet.getString("password");
                    String salt = resultSet.getString("salt");
                    String checkPass = hashPassword(user.getPassword(), salt)[0];
                    if (password.equals(checkPass)) {
                        return true;
                    } else {
                        throw new WrongPasswordException("Wrong password!");
                    }
                }
            }
        } catch (WrongPasswordException | UserNotFound e) {
            System.out.println();
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        Optional<User> user;
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id=?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User entity = new User();
                entity.setId(resultSet.getInt("id"));
                entity.setLogin(resultSet.getString("username"));
                entity.setPassword(resultSet.getString("password"));
                entity.setRole(UserRole.valueOf(resultSet.getString("role")));
                user = Optional.of(entity);
                return user;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("role").equals(UserRole.ADMIN.name())
                        || resultSet.getString("role").equals(UserRole.MANAGER.name()))
                    continue;
                int id = resultSet.getInt("id");
                String login = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                User user = new User(id, login, password, role);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean IsNotExistAdminAndManager() {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals(UserRole.ADMIN.name()) || role.equals(UserRole.MANAGER.name())) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void createAdminAndManager(User admin, User manager) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement adminStatement = connection
                    .prepareStatement("INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)");
            PreparedStatement managerStatement = connection
                    .prepareStatement("INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)");
            adminStatement.setString(1, admin.getLogin());
            String[] adminPassAndSalt = hashPassword(admin.getPassword(), null);
            adminStatement.setString(2, adminPassAndSalt[0]);
            adminStatement.setString(3, adminPassAndSalt[1]);
            adminStatement.setString(4, admin.getRole().name());
            managerStatement.setString(1, manager.getLogin());
            String[] managerPassAndSalt = hashPassword(manager.getPassword(), null);
            managerStatement.setString(2, managerPassAndSalt[0]);
            managerStatement.setString(3, managerPassAndSalt[1]);
            managerStatement.setString(4, manager.getRole().name());
            adminStatement.execute();
            managerStatement.execute();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean deleteAccount(int id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            stmt.setInt(1, id);
            return !stmt.execute();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
