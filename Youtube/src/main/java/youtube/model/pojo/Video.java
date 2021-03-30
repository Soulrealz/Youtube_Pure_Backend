package youtube.model.pojo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import youtube.model.dto.videosDTO.UploadVideoDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private LocalDateTime uploadDate;
    private String description;
    private String path;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference
    private User owner;
    @OneToMany(mappedBy = "onVideo")
    private List<Comment> comments;

    @ManyToMany(mappedBy = "videos")
    @JsonBackReference
    private List<Playlist> includedInPlaylists;

    public Video(UploadVideoDTO videoDTO){
        this.title = videoDTO.getTitle();
        this.description = videoDTO.getDescription();
        this.path = videoDTO.getPath();
        this.uploadDate = LocalDateTime.now();
    }
}
