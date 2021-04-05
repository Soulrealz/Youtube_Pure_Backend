package youtube.model.dto.videosDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Component
public class SearchVideoDTO {
    private String name;
}
