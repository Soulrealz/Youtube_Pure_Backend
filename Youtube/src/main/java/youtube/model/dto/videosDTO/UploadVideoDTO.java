package youtube.model.dto.videosDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

// User DTO used to take the information which the user has given for creating his new video
@Component
@Setter
@Getter
@NoArgsConstructor
public class UploadVideoDTO {
    private String title;
    private String description;
    private String path;


}
