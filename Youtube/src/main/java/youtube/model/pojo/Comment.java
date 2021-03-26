package youtube.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    private LocalDateTime commentedOn;
    private int userId;
    private int videoId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User commenter;
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video onVideo;
}
