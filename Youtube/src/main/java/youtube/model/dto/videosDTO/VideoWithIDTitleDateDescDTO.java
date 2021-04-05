package youtube.model.dto.videosDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VideoWithIDTitleDateDescDTO {
    private int id;
    private String title;
    private LocalDateTime uploadDate;
    private String description;
}
