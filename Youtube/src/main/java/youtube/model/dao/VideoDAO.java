package youtube.model.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import youtube.exceptions.CustomSQLException;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.utils.Log4JLogger;
import youtube.model.utils.PairVideoInt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class VideoDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String selectVideosAndSortByLikes =
            "SELECT v.title, v.upload_date, v.description, u.username, COUNT(v.title) AS likes\n" +
            "FROM users_like_videos AS ulv\n" +
            "JOIN videos AS v ON (v.id = ulv.video_id)\n" +
            "JOIN users AS u ON (u.id = v.owner_id)\n" +
            "GROUP BY v.title\n" +
            "ORDER BY likes DESC\n" +
            "LIMIT ?\n" +
            "OFFSET ?;";

    // Pair of current video and how many likes it has
    public List<PairVideoInt> orderByLikes(int limit, int offset) {
        List<PairVideoInt> videos = new ArrayList<>();

        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(selectVideosAndSortByLikes);
            ps.setInt(1, limit);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));

                Video video = new Video();
                video.setTitle(rs.getString("title"));
                video.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
                video.setDescription(rs.getString("description"));
                video.setOwner(user);

                videos.add(new PairVideoInt(video, rs.getInt("likes")));
            }

            return videos;
        } catch (SQLException throwables) {
            Log4JLogger.getLogger().error("Could not execute SQL query.\n", throwables);
            throw new CustomSQLException("Unavailable resource");
        }
    }

}
