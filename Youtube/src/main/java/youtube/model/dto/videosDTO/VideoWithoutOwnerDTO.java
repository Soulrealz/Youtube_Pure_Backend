package youtube.model.dto.videosDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.pojo.Video;

import java.time.LocalDateTime;

@Component
@Setter
@Getter
@NoArgsConstructor
public class VideoWithoutOwnerDTO {
    private int id;
    private String title;
    private LocalDateTime uploadDate;
    private String description;


    public VideoWithoutOwnerDTO(Video video) {
        id = video.getId();
        title = video.getTitle();
        uploadDate = video.getUploadDate();
        description = video.getDescription();
    }
}
