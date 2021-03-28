package youtube.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.pojo.Playlist;

import java.time.LocalDateTime;

@Component
@NoArgsConstructor
@Getter
@Setter
public class PlaylistWithoutIdDTO {
    private String title;
    private String ownerName;
    private LocalDateTime createdDate;


    public PlaylistWithoutIdDTO(Playlist playlist) {
        title = playlist.getTitle();
        ownerName = playlist.getOwner().getUsername();
        createdDate = playlist.getCreatedDate();
    }
}
