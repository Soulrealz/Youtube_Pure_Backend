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
public class CommentDTO {
    private String text;
    private LocalDateTime commentedOn;
    private int likes;
    private int dislikes;

    public CommentDTO(Comment comment) {
        text = comment.getText();
        commentedOn = comment.getCommentedOn();
        likes = comment.getLikedByUsers().size();
        dislikes = comment.getDislikedByUsers().size();
    }
}
