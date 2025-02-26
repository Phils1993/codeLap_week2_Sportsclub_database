package persistence;

import entities.Registration;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationMapper {

    private static Database database;

    public RegistrationMapper(Database database) {
        this.database = database;
    }

    public boolean addToTeam(Registration registration) throws DatabaseException
    {
        boolean result = false;
        String sql = "insert into registration (member_id, team_id, price) values (?,?,?)";
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, registration.getMember_id());
                ps.setString(2, registration.getTeam_id());
                ps.setInt(3, registration.getPrice());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    result = true;
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            // TODO: Make own throwable exception and let it bubble upwards
            throwables.printStackTrace();
        }
        return result;
    }

    public List<Registration> getAllRegistations() throws DatabaseException {

        List<Registration> registrationList = new ArrayList<>();

        String sql = "SELECT r.member_id, m.name, s.sport, r.team_id, r.price\n" +
                "FROM registration r\n" +
                "JOIN member m ON r.member_id = m.member_id\n" +
                "JOIN team t ON r.team_id = t.team_id\n" +
                "JOIN sport s ON t.sport_id = s.sport_id;\n";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int member_id = rs.getInt("member_id");
                    String name = rs.getString("name");
                    String sport = rs.getString("sport");
                    String team_id = rs.getString("team_id");
                    int price = rs.getInt("price");

                    registrationList.add(new Registration(member_id, team_id, price, sport, name));
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return registrationList;
    }
}
