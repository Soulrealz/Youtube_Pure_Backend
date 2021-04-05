package youtube.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    // names cannot be userID or videoID because theres
    // a conflict with the names for the joincolumns below
    private int uID;
    private int vID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User commenter;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video onVideo;

    @ManyToMany(mappedBy = "likedComments")
    @JsonManagedReference
    private List<User> likedByUsers;

    @ManyToMany(mappedBy = "dislikedComments")
    @JsonManagedReference
    private List<User> dislikedByUsers;

    public Comment(String text, User user, Video video) {
        this.text = text;
        commenter = user;
        onVideo = video;
        uID = user.getId();
        vID = video.getId();
        commentedOn = LocalDateTime.now();
        likedByUsers = new ArrayList<>();
        dislikedByUsers = new ArrayList<>();
    }

}
