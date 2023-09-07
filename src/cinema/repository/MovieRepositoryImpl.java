package cinema.repository;

import cinema.model.Movie;
import cinema.model.Ticket;
import cinema.util.ConnectionManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MovieRepositoryImpl implements MovieRepository {

    @Override
    public boolean checkIsEmptyMovieTable() {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM movies");
            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Movie> getMovieTable() {
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement moviesStatement = connection.prepareStatement("SELECT * FROM movies");
            PreparedStatement ticketsStatement = connection.prepareStatement("SELECT * FROM tickets");
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
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
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
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void removeFromMovieTable(String title) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM movies WHERE title = ?");
            stmt.setString(1, title);
            stmt.execute();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateMovieTable(Movie movie) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection
                    .prepareStatement("UPDATE movies SET date = ?, ticket_price = ?, tickets = ?  WHERE title = ?");
            stmt.setTimestamp(1, Timestamp.valueOf(movie.getDate()));
            stmt.setDouble(2, movie.getTicketPrice());
            stmt.setObject(3, movie.getUnPurchasedTickets());
            stmt.setString(4, movie.getTitle());
            stmt.execute();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
