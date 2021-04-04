package youtube.model.dto.playlistsDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import youtube.model.dto.videosDTO.VideoWithoutOwnerDTO;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.Video;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Playlist DTO which includes the whole information for a certain playlist without its id
@Component
@NoArgsConstructor
@Getter
@Setter
public class PlaylistWithoutIdDTO {
    private String title;
    private String ownerName;
    private LocalDateTime createdDate;
    private List<VideoWithoutOwnerDTO> videos;

    public PlaylistWithoutIdDTO(Playlist playlist) {
        title = playlist.getTitle();
        ownerName = playlist.getOwner().getUsername();
        createdDate = playlist.getCreatedDate();

        videos = new ArrayList<>();

        for (Video video : playlist.getVideos()) {
            videos.add(new VideoWithoutOwnerDTO(video));
        }
    }
}
