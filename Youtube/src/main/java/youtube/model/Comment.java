package youtube.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class Comment {
    private String text;
    private LocalDateTime commentedOn;
    private int userId;
    private int videoId;
}
