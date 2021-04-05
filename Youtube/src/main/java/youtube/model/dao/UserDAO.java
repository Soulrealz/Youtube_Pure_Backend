package youtube.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import youtube.model.pojo.User;
import youtube.model.utils.Log4JLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String selectAllWhereUsernameLike =
            "SELECT * FROM users WHERE username LIKE ?;";

    public List<User> searchByName(String likeParam) {
        // Adding % to make it be xxxWORDxxx and still match
        String param = "%" + likeParam +"%";

        List<User> users = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(selectAllWhereUsernameLike);
            ps.setString(1, param);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getBoolean("verified"));

                // Checking if returned user is verified, will not display unverified users
                if (user.getVerified()) {
                    users.add(user);
                }
            }
            return users;
        } catch (SQLException throwables) {
            Log4JLogger.getLogger().error("Could not execute SQL query.\n", throwables);
            throw new youtube.exceptions.SQLException("Unavailable resource");
        }
    }
}
