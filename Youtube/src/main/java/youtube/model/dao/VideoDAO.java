package youtube.model.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import youtube.exceptions.SQLException;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.utils.Log4JLogger;
import youtube.model.utils.PairVideoInt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    private static final String selectAllWhereTitleLike =
            "SELECT * FROM videos WHERE title LIKE ? LIMIT ? OFFSET ?;";

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
        } catch (java.sql.SQLException throwables) {
            Log4JLogger.getLogger().error("Could not execute SQL query.\n", throwables);
            throw new SQLException("Unavailable resource");
        }
    }

    public List<Video> searchByName(String likeParam, int limit, int offset) {
        // Adding % to make it be xxxWORDxxx and still match
        String param = "%" + likeParam +"%";

        List<Video> videos = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(selectAllWhereTitleLike);
            ps.setString(1, param);
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Video video = new Video(rs.getInt("id"),
                        rs.getString("title"),
                        rs.getTimestamp("upload_date").toLocalDateTime(),
                        rs.getString("description"));

                videos.add(video);
            }

            return videos;
        } catch (java.sql.SQLException throwables) {
            Log4JLogger.getLogger().error("Could not execute SQL query.\n", throwables);
            throw new youtube.exceptions.SQLException("Unavailable resource");
        }
    }
}
