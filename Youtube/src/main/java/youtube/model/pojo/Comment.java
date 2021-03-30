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
    // names cannot be userID or videoID because for some reason
    // theres a conflict with the names for the joincolumns below
    private int uID;
    private int vID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User commenter;
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video onVideo;

    public Comment(String text, User user, Video video) {
        this.text = text;
        commenter = user;
        onVideo = video;
        uID = user.getId();
        vID = video.getId();
        commentedOn = LocalDateTime.now();
    }

}
