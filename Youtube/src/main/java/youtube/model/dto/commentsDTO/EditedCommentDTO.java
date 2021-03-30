package youtube.model.dto.commentsDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.pojo.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class EditedCommentDTO {
    private String text;
    private LocalDateTime editedOn;
    private int likes;
    private int dislikes;

    public EditedCommentDTO(Comment comment) {
        text = comment.getText();
        editedOn = LocalDateTime.now();
        likes = comment.getLikedByUsers().size();
        dislikes = comment.getDislikedByUsers().size();
    }
}
