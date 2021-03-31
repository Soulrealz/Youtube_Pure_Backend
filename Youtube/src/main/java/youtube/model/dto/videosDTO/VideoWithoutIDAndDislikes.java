package youtube.model.dto.videosDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;

import java.time.LocalDateTime;

@Getter
@Setter
@Component
@NoArgsConstructor
public class VideoWithoutIDAndDislikes {
    private String title;
    private LocalDateTime uploadDate;
    private String description;
    private String ownerName;
    private int likes;

    public static final String selectVideosAndSortByLikes = "SELECT v.title, v.upload_date, v.description, u.username, COUNT(v.title) AS likes\n" +
            "FROM users_like_videos AS ulv\n" +
            "JOIN videos AS v ON (v.id = ulv.video_id)\n" +
            "JOIN users AS u ON (u.id = v.owner_id)\n" +
            "GROUP BY v.title\n" +
            "ORDER BY likes DESC\n" +
            "LIMIT ?\n" +
            "OFFSET ?;";

    public VideoWithoutIDAndDislikes(Video video) {
        title = video.getTitle();
        uploadDate = video.getUploadDate();
        description = video.getDescription();
        ownerName = video.getOwner().getUsername();
        likes = video.getLikedByUsers().size();
    }
}
