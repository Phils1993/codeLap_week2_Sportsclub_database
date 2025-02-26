package persistence;

import entities.Member;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberMapper {

    private static Database database;

    public MemberMapper(Database database) {
        this.database = database;
    }

    public List<Member> getAllMembers() throws DatabaseException {

        List<Member> memberList = new ArrayList<>();

        String sql = "select member_id, name, address, m.zip, gender, city, year " +
                "from member m inner join zip using(zip) " +
                "order by member_id";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int memberId = rs.getInt("member_id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int zip = rs.getInt("zip");
                    String city = rs.getString("city");
                    String gender = rs.getString("gender");
                    int year = rs.getInt("year");
                    memberList.add(new Member(memberId, name, address, zip, city, gender, year));
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return memberList;
    }

    public Member getMemberById(int memberId) throws DatabaseException {
        Member member = null;

        String sql = "select member_id, name, address, m.zip, gender, city, year " +
                "from member m inner join zip using(zip) " +
                "where member_id = ?";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, memberId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    memberId = rs.getInt("member_id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int zip = rs.getInt("zip");
                    String city = rs.getString("city");
                    String gender = rs.getString("gender");
                    int year = rs.getInt("year");
                    member = new Member(memberId, name, address, zip, city, gender, year);
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int a = 1;
        return member;
    }

    public boolean deleteMember(int member_id) throws DatabaseException {
        boolean result = false;
        String sql = "delete from member where member_id = ?";
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, member_id);
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

    public Member insertMember(Member member) throws DatabaseException {
        boolean result = false;
        int newId = 0;
        String sql = "insert into member (name, address, zip, gender, year) values (?,?,?,?,?)";
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, member.getName());
                ps.setString(2, member.getAddress());
                ps.setInt(3, member.getZip());
                ps.setString(4, member.getGender());
                ps.setInt(5, member.getYear());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    result = true;
                }
                ResultSet idResultset = ps.getGeneratedKeys();
                if (idResultset.next()) {
                    newId = idResultset.getInt(1);
                    member.setMemberId(newId);
                } else {
                    member = null;
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            // TODO: Make own throwable exception and let it bubble upwards
            throwables.printStackTrace();
        }
        return member;
    }

    public boolean updateMember(Member member) throws DatabaseException {
        boolean result = false;
        String sql = "update member " +
                "set name = ?, address = ?, zip = ?, gender = ?, year = ? " +
                "where member_id = ?";
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, member.getName());
                ps.setString(2, member.getAddress());
                ps.setInt(3, member.getZip());
                ps.setString(4, member.getGender());
                ps.setInt(5, member.getYear());
                ps.setInt(6, member.getMemberId());
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

    public static Map<String, Integer> findNoOfEachParticipantsOnEachTeam() throws DatabaseException {
        String sql = ("SELECT team.team_id, COUNT(registration.member_id) AS number_of_participants\n" +
                "FROM team\n" +
                "LEFT JOIN registration ON team.team_id = registration.team_id\n" +
                "GROUP BY team.team_id;");

        Map<String, Integer> participantsMap = new HashMap<>();


        try (Connection connection = database.connect()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = prepareStatement.executeQuery();

                while (resultSet.next()) {
                    String teamId = resultSet.getString("team_id");
                    int numberOfParticipants = resultSet.getInt("number_of_participants");
                    participantsMap.put(teamId, numberOfParticipants);
                }

            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get any teamID from the database", e);
        }
        return participantsMap;
    }

    public static Map<String, Integer> findNoOfEachParticipantsOnSport() throws DatabaseException {
        String sql = ("SELECT s.sport, COUNT(r.member_id) AS number_of_participants\n" +
                "FROM sport s\n" +
                "JOIN team t ON s.sport_id = t.sport_id\n" +
                "LEFT JOIN registration r ON t.team_id = r.team_id\n" +
                "GROUP BY s.sport;");
        Map<String, Integer> SportsParticipantsMap = new HashMap<>();

        try (Connection connection = database.connect()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = prepareStatement.executeQuery();


                while (resultSet.next()) {
                    String sport = resultSet.getString("sport");
                    int numberOfParticipants = resultSet.getInt("number_of_participants");
                    SportsParticipantsMap.put(sport, numberOfParticipants);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get any teamID from the database", e);
        }
        return SportsParticipantsMap;
    }

    public static Map<String, Integer> memberGender() throws DatabaseException {
        String sql = ("SELECT gender, COUNT(member_id) AS number_of_members FROM member GROUP BY gender");

        Map<String, Integer> memberGender = new HashMap<>();

        try (Connection connection = database.connect()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = prepareStatement.executeQuery();


                while (resultSet.next()) {
                    String gender = resultSet.getString("gender");
                    int numberOfMembers = resultSet.getInt("number_of_members");
                    memberGender.put(gender, numberOfMembers);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get any teamID from the database", e);
        }

        return memberGender;
    }

    public static int sumOfIncome() throws SQLException {
        int total = 0;
        String sql = ("SELECT SUM(price) AS total_income\n" +
                "FROM registration;");

        try (Connection connection = database.connect()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = prepareStatement.executeQuery();

                if (resultSet.next()) {
                    total += resultSet.getInt("total_income");
                }
            }

            return total;
        }
    }

    public static Map<String, Integer> sumOfIncomeForEachTeam() throws DatabaseException, SQLException  {
        String sql = ("SELECT team.team_id, SUM(registration.price) AS total_income\n" +
                "FROM team\n" +
                "LEFT JOIN registration ON team.team_id = registration.team_id\n" +
                "GROUP BY team.team_id;\n");

        Map<String, Integer> incomeMap = new HashMap<>();
        try (Connection connection = database.connect()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = prepareStatement.executeQuery();

                while   (resultSet.next()) {
                    String team_id = resultSet.getString("team_id");
                    int totalIncome = resultSet.getInt("total_income");
                    incomeMap.put(team_id, totalIncome);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return incomeMap;
    }

    public static Map<String, Integer> averageIncomeForEachTeam() throws DatabaseException, SQLException  {
        String sql = ("SELECT team.team_id, AVG(registration.price) AS average_payment\n" +
                "FROM team\n" +
                "LEFT JOIN registration ON team.team_id = registration.team_id\n" +
                "GROUP BY team.team_id;\n");
        Map<String, Integer> averageTeamIncome = new HashMap<>();
        try (Connection connection = database.connect()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = prepareStatement.executeQuery();
                while (resultSet.next()) {
                    String team_id = resultSet.getString("team_id");
                    int averagePayment = resultSet.getInt("average_payment");
                    averageTeamIncome.put(team_id, averagePayment);
                }
            }
        }
        return averageTeamIncome;
    }
}

