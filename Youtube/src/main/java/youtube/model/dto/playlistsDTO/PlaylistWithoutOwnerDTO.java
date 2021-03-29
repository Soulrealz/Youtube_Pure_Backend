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

@Component
@Setter
@Getter
@NoArgsConstructor
public class PlaylistWithoutOwnerDTO {
    private String title;
    private LocalDateTime uploadDate;
    private List<VideoWithoutOwnerDTO> videos;

    public PlaylistWithoutOwnerDTO(Playlist playlist) {
        title = playlist.getTitle();
        uploadDate = playlist.getCreatedDate();
        videos = new ArrayList<>();

        for (Video video : playlist.getVideos()) {
            videos.add(new VideoWithoutOwnerDTO(video));
        }
    }
}
