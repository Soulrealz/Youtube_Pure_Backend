package youtube.model.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private User owner;
    @OneToMany(mappedBy = "onVideo")
    private List<Video> videos;
}
