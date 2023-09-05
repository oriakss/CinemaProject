package cinema.model;

public class User {

    private int id;
    private String login;
    private String password;
    private UserRole role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

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

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
