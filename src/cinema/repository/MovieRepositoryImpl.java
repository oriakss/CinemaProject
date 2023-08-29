package cinema.repository;

import cinema.model.Movie;
import cinema.model.Ticket;
import cinema.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieRepositoryImpl implements MovieRepository {

    public boolean checkIsEmptyMovieTable() {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM movies");
            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movie> getMovieTable() {
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement moviesStatement = connection.prepareStatement("SELECT * FROM movies");
            PreparedStatement ticketsStatement = connection.prepareStatement("SELECT (user, movie) FROM tickets");
//            PreparedStatement usersStatement = connection.prepareStatement("SELECT * FROM users");
            ResultSet moviesResultSet = moviesStatement.executeQuery();
            ResultSet ticketsResultSet = ticketsStatement.executeQuery();
//            ResultSet usersResultSet = usersStatement.executeQuery();

            while (moviesResultSet.next()) {
                int id = moviesResultSet.getInt("id");
                String title = moviesResultSet.getString("title");
                LocalDateTime date = moviesResultSet.getTimestamp("date").toLocalDateTime();
                int tickets = moviesResultSet.getInt("tickets");

                List<Ticket> ticketList = new ArrayList<>();
                Movie movie = new Movie(id, title, date, ticketList);
//                User user = null;
                String userStr = null;

                while (ticketsResultSet.next()) {
                    if (title.equals(ticketsResultSet.getString("movie"))) {
                        userStr = ticketsResultSet.getString("user");
//                        while (usersResultSet.next()) {
//                            String userName = usersResultSet.getString("username");
//                            if (userStr.equals(userName)) {
//                                int userId = usersResultSet.getInt("id");
//                                String password = usersResultSet.getString("password");
//                                String role = usersResultSet.getString("role");
//                                user = new User(userId, userName, password, role);
//                                break;
//                            }
//                        }
                        break;
                    }
                }
                for (int i = 1; i <= tickets; i++) {
                    ticketList.add(new Ticket(null, title, i, 15.00, true));
                }
                for (int i = 1; i <= 50 - tickets; i++) {
                    ticketList.add(new Ticket(userStr, title, i, 15.00, false));
                }
                movies.add(movie);
            }
            return movies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToDB(Movie movie) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection
                    .prepareStatement("INSERT INTO movies (title, date, tickets) VALUES (?, ?, ?)");
            stmt.setString(1, movie.getTitle());
            stmt.setTimestamp(2, Timestamp.valueOf(movie.getDate()));
            stmt.setObject(3, movie.getUnPurchasedTickets());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInDB(Movie movie) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection
                    .prepareStatement("UPDATE movies SET tickets = (?) WHERE title = (?) AND date = (?)");
            stmt.setObject(1, movie.getUnPurchasedTickets());
            stmt.setString(2, movie.getTitle());
            stmt.setTimestamp(3, Timestamp.valueOf(movie.getDate()));
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
