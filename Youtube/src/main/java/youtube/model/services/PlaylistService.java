package youtube.model.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.PlaylistWithoutIdDTO;
import youtube.model.pojo.Playlist;
import youtube.model.repository.PlaylistRepository;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    public PlaylistWithoutIdDTO getByName(String title) {
        if(playlistRepository.findByTitle(title) == null) {
            throw new NotFoundException("There is no playlist with that name.");
        }

        Playlist playlist = playlistRepository.findByTitle(title);
        return new PlaylistWithoutIdDTO(playlist);
    }
}
