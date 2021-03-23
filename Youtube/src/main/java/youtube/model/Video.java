package youtube.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Video {
    private String title;
    private String description;
    private String path;
    private String link;
    private int ownerId;
}
