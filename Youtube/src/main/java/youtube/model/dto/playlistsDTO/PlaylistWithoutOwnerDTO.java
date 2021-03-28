package youtube.model.dto.playlistsDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.pojo.Playlist;

import java.time.LocalDateTime;

@Component
@Setter
@Getter
@NoArgsConstructor
public class PlaylistWithoutOwnerDTO {
    private String title;
    private LocalDateTime uploadDate;

    public PlaylistWithoutOwnerDTO(Playlist playlist) {
        title = playlist.getTitle();
        uploadDate = playlist.getCreatedDate();
    }
}
