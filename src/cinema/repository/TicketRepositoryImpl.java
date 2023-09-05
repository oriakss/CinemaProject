package cinema.repository;

import cinema.model.Ticket;
import cinema.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketRepositoryImpl implements TicketRepository {

    public void addToTicketTable(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO tickets (user, movie, seat_num, price) VALUES (?, ?, ?, ?)");
            stmt.setString(1, ticket.getUser());
            stmt.setString(2, ticket.getMovie());
            stmt.setInt(3, ticket.getSeatNum());
            stmt.setDouble(4, ticket.getPrice());
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean removeFromTicketTable(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM tickets WHERE id = ?");
            stmt.setInt(1, ticket.getId());
            return stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getIdFromTicketTable(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id FROM tickets WHERE user = ? AND movie = ? AND seat_num = ?");
            statement.setString(1, ticket.getUser());
            statement.setString(2, ticket.getMovie());
            statement.setInt(3, ticket.getSeatNum());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
