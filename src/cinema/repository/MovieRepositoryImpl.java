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
            PreparedStatement ticketsStatement = connection.prepareStatement(
                    "SELECT * FROM tickets");
            ResultSet moviesResultSet = moviesStatement.executeQuery();
            ResultSet ticketsResultSet = ticketsStatement.executeQuery();

            while (moviesResultSet.next()) {
                int id = moviesResultSet.getInt("id");
                String title = moviesResultSet.getString("title");
                LocalDateTime date = moviesResultSet.getTimestamp("date").toLocalDateTime();
                double ticketPrice = moviesResultSet.getDouble("ticket_price");
                int tickets = moviesResultSet.getInt("tickets");

                List<Ticket> ticketList = new ArrayList<>();
                Movie movie = new Movie(id, title, date, ticketPrice, ticketList);
                int ticketId;
                String userName;
                int seatNum;

                while (ticketsResultSet.next()) {
                    if (title.equals(ticketsResultSet.getString("movie"))) {
                        ticketId = ticketsResultSet.getInt("id");
                        userName = ticketsResultSet.getString("user");
                        seatNum = ticketsResultSet.getInt("seat_num");
                        ticketList.add(new Ticket(ticketId, userName, title, seatNum, ticketPrice, false));
                    }
                }
                for (int i = 51 - tickets; i <= 50; i++) {
                    ticketList.add(new Ticket(null, title, i, ticketPrice, true));
                }
                ticketsResultSet.beforeFirst();
                movies.add(movie);
            }
            return movies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToMovieTable(Movie movie) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection
                    .prepareStatement("INSERT INTO movies (title, date, ticket_price, tickets) VALUES (?, ?, ?, ?)");
            stmt.setString(1, movie.getTitle());
            stmt.setTimestamp(2, Timestamp.valueOf(movie.getDate()));
            stmt.setDouble(3, movie.getTicketPrice());
            stmt.setObject(4, movie.getUnPurchasedTickets());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMovieTable(Movie movie) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection
                    .prepareStatement("UPDATE movies SET tickets = ? WHERE title = ? AND date = ?");
            stmt.setObject(1, movie.getUnPurchasedTickets());
            stmt.setString(2, movie.getTitle());
            stmt.setTimestamp(3, Timestamp.valueOf(movie.getDate()));
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
