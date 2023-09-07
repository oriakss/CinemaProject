package cinema.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private int id;
    private String login;
    private String password;
    private UserRole role;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.role = UserRole.USER;
    }

    public User(Integer id, String login, String password, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = UserRole.valueOf(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + login + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
