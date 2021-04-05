package youtube.model.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.GenericResponseDTO;
import youtube.model.dto.playlistsDTO.ResponsePlaylistDTO;
import youtube.model.dto.playlistsDTO.PlaylistWithoutOwnerDTO;
import youtube.model.dto.videosDTO.VideoWithIdDTO;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.PlaylistRepository;
import youtube.model.repository.VideoRepository;
import youtube.model.utils.PlaylistValidator;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private VideoRepository videoRepository;

    public ResponsePlaylistDTO createPlaylist(String title, User user) {
        if(title.length() <= 0){
            throw new BadRequestException("This title is invalid.");
        }
        // Checks if there is already a playlist with this name
        if(playlistRepository.findByTitle(title) != null) {
            throw new BadRequestException("This playlist title is already used.");
        }

        Playlist playlist = new Playlist();
        playlist.setTitle(title);
        playlist.setOwner(user);
        playlist.setCreatedDate(LocalDateTime.now());
        playlistRepository.save(playlist);

        return new ResponsePlaylistDTO(playlist);
    }
    public GenericResponseDTO deletePlaylist(int id, User user) {
        Optional<Playlist> playlist = playlistRepository.findById(id);

        // Checks if the playlist, the user want to delete, actually exists
        if(playlist.isEmpty()) {
            throw new NotFoundException("This playlist doesn't exist.");
        }

        // Checks if the playlist, the user want to delete, is his
        if(playlist.get().getOwner() != user) {
            throw new BadRequestException("You can't delete someone else's playlist.");
        }

        playlistRepository.delete(playlist.get());

        return new GenericResponseDTO("You have successfully deleted " + playlist.get().getTitle() + " playlist.");
    }

    public ResponsePlaylistDTO getByName(String title) {

        // Checks if playlist with this name exists
        if(playlistRepository.findByTitle(title) == null) {
            throw new NotFoundException("There is no playlist with that name.");
        }

        Playlist playlist = playlistRepository.findByTitle(title);
        return new ResponsePlaylistDTO(playlist);
    }

    public PlaylistWithoutOwnerDTO addVideo(User user, int id, VideoWithIdDTO videoDTO) {
        Optional<Playlist> currentPlaylist = playlistRepository.findById(id);
        PlaylistValidator.validate(currentPlaylist, user);

        Optional<Video> video = videoRepository.findById(videoDTO.getId());

        // Checks if the video, the user want to add, actually exists
        if(video.isEmpty()) {
            throw new BadRequestException("The video, you want to add, doesn't exist.");
        }

        // Checks if the playlist already has this video in it
        if(currentPlaylist.get().getVideos().contains(video.get())) {
            throw new BadRequestException("This playlist already contains this video.");
        }

        currentPlaylist.get().getVideos().add(video.get());
        playlistRepository.save(currentPlaylist.get());
        return new PlaylistWithoutOwnerDTO(playlistRepository.findByTitle(currentPlaylist.get().getTitle()));
    }
    public PlaylistWithoutOwnerDTO removeVideo(User user, int id, VideoWithIdDTO videoDTO) {
        Optional<Playlist> currentPlaylist = playlistRepository.findById(id);
        PlaylistValidator.validate(currentPlaylist, user);

        Optional<Video> video = videoRepository.findById(videoDTO.getId());

        // Checks if the video, the user want to remove, actually exists
        if(video.isEmpty()) {
            throw new BadRequestException("The video, you want to remove, doesn't exist.");
        }

        // Checks if this video is included in this playlist at all
        if(!currentPlaylist.get().getVideos().contains(video.get())) {
            throw new BadRequestException("This playlist already doesn't contain this video.");
        }

        currentPlaylist.get().getVideos().remove(video.get());
        playlistRepository.save(currentPlaylist.get());
        return new PlaylistWithoutOwnerDTO(playlistRepository.findByTitle(currentPlaylist.get().getTitle()));
    }
}
