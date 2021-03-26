package youtube.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.pojo.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Component
public class CommentDTO {
    private String text;
    private LocalDateTime commentedOn;

    public CommentDTO(Comment c) {
        text = c.getText();
        commentedOn = c.getCommentedOn();
    }
}
