package cinema.repository;

import cinema.model.Ticket;
import cinema.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TicketRepositoryImpl implements TicketRepository {

    public void addToDB(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO tickets (user, movie, seat_num, price) VALUES (?, ?, ?, ?)");
            stmt.setString(1, ticket.getUser());
            stmt.setString(2, ticket.getMovie());
            stmt.setInt(3, ticket.getSeatNum());
            stmt.setDouble(4, ticket.getPrice());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
