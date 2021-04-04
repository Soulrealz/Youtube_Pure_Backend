package youtube.model.dto.videosDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.pojo.Video;

import java.time.LocalDateTime;

// Video DTO which includes the whole information for a certain playlist without its owner
// It is used to show videos of already KNOWN user
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
