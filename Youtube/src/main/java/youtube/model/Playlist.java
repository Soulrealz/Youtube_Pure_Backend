package youtube.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Playlist {
    private String title;
    private LocalDateTime createDate;
    private int ownerId;
}
