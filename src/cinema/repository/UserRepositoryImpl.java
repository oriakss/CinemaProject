package cinema.repository;

import cinema.model.User;
import cinema.model.UserRole;
import cinema.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            throw new RuntimeException(e);  //писать логичные исключения
        }
        return true;
    }

    @Override
    public boolean signUp(User user) {
        try (Connection connection = ConnectionManager.open()) {
            boolean isNotExistUser = checkUserByLogin(user.getLogin());
            if (isNotExistUser) {
                PreparedStatement stmt = connection
                        .prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, UserRole.USER.name());
                return stmt.execute();
            } else {
                throw new RuntimeException(String.format("Логин '%s' уже существует!", user.getLogin()));
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean signIn(User user) {
        try (Connection connection = ConnectionManager.open()) {
            boolean isNotExistUserLogin = checkUserByLogin(user.getLogin());
            if (isNotExistUserLogin) {
                throw new RuntimeException(
                        String.format("Пользователя с логином '%s' не существует!", user.getLogin()));
            } else {
                PreparedStatement stmt = connection.prepareStatement("SELECT password FROM users WHERE username=?");
                stmt.setString(1, user.getLogin());
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next() && resultSet.getString("password").equals(user.getPassword())) {
                    return true;
                } else {
                    throw new RuntimeException("Не верный пароль!");
                }
            }
        } catch (SQLException e) {
            return false;
        }
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
            throw new RuntimeException(e);  //писать логичные исключения
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String login = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                User user = new User(id, login, password, role);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean IsNotExistAdminAndManager() {
        return true;
    }

    @Override
    public boolean createAdminAndManager(User admin, User manager) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement adminStatement = connection
                    .prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
            PreparedStatement managerStatement = connection
                    .prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
            adminStatement.setString(1, admin.getLogin());
            adminStatement.setString(2, admin.getPassword());
            adminStatement.setString(3, admin.getRole().name());
            managerStatement.setString(1, manager.getLogin());
            managerStatement.setString(2, manager.getPassword());
            managerStatement.setString(3, manager.getRole().name());
            adminStatement.execute();
            managerStatement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
