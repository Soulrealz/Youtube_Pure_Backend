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
public class VideoWithoutIDDTO {
    private String title;
    private LocalDateTime uploadDate;
    private String description;
    private String ownerName;

    public VideoWithoutIDDTO(Video video) {
        title = video.getTitle();
        uploadDate = video.getUploadDate();
        description = video.getDescription();
        ownerName = video.getOwner().getUsername();
    }
}
