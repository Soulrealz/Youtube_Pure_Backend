package youtube.model.dto.commentsDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Component
public class CommentWithTextDTO {
    private String text;
}
