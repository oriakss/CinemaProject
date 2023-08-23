package cinema.repository;

import cinema.model.Movie;
import cinema.model.Ticket;
import cinema.model.User;
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM movies");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                int tickets = resultSet.getInt("tickets");
                List<Ticket> ticketList = new ArrayList<>();
                for (int i = 1; i <= tickets; i++) {
                    ticketList.add(new Ticket(null, this, i, price, true));
                }
                for (int i = 1; i <= 50 - tickets; i++) {
                    ticketList.add(new Ticket(null, this, i, price, false));
                }
                Movie movie = new Movie(id, title, date, new ArrayList<>());
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
