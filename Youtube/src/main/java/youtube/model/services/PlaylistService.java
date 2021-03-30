package youtube.model.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.playlistsDTO.PlaylistWithoutIdDTO;
import youtube.model.dto.playlistsDTO.PlaylistWithoutOwnerDTO;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.PlaylistRepository;
import youtube.model.repository.VideoRepository;

import java.time.LocalDateTime;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private VideoRepository videoRepository;

    public void createPlaylist(String title, User user) {
        if(playlistRepository.findByTitle(title) != null) {
            throw new BadRequestException("This playlist title is already used.");
        }

        Playlist playlist = new Playlist();
        playlist.setTitle(title);
        playlist.setOwner(user);
        playlist.setCreatedDate(LocalDateTime.now());
        playlistRepository.save(playlist);
    }

    public PlaylistWithoutIdDTO getByName(String title) {
        if(playlistRepository.findByTitle(title) == null) {
            throw new NotFoundException("There is no playlist with that name.");
        }

        Playlist playlist = playlistRepository.findByTitle(title);
        return new PlaylistWithoutIdDTO(playlist);
    }

    public PlaylistWithoutOwnerDTO addVideo(User user, int id, String title) {
        Playlist currentPlaylist = playlistRepository.findById(id);
        if(currentPlaylist == null) {
            throw new NotFoundException("This playlist doesn't exist.");
        }

        if(currentPlaylist.getOwner() != user) {
            throw new BadRequestException("You can't edit someone else's playlist.");
        }

        Video video = videoRepository.findByTitle(title);

        if(video == null) {
            throw new BadRequestException("The video, you want to add, doesn't exist.");
        }

        if(currentPlaylist.getVideos().contains(video)) {
            throw new BadRequestException("This playlist already contains this video.");
        }

        currentPlaylist.getVideos().add(video);
        playlistRepository.save(currentPlaylist);
        return new PlaylistWithoutOwnerDTO(playlistRepository.findByTitle(currentPlaylist.getTitle()));
    }
}
