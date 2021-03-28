package youtube.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@NoArgsConstructor
public class UploadVideoDTO {
    private String title;
    private String description;
    private String path;


}
