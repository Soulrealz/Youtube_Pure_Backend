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
import youtube.model.utils.PlaylistValidator;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private VideoRepository videoRepository;

    public void createPlaylist(String title, User user) {

        // Checks if there is already a playlist with this name
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

        // Checks if playlist with this name exists
        if(playlistRepository.findByTitle(title) == null) {
            throw new NotFoundException("There is no playlist with that name.");
        }

        Playlist playlist = playlistRepository.findByTitle(title);
        return new PlaylistWithoutIdDTO(playlist);
    }

    public PlaylistWithoutOwnerDTO addVideo(User user, int id, String title) {
        Optional<Playlist> currentPlaylist = playlistRepository.findById(id);
        PlaylistValidator.validate(currentPlaylist, user);

        Video video = videoRepository.findByTitle(title);

        // Checks if the video, the user want to add, actually exists
        if(video == null) {
            throw new BadRequestException("The video, you want to add, doesn't exist.");
        }

        // Checks if the playlist already has this video in it
        if(currentPlaylist.get().getVideos().contains(video)) {
            throw new BadRequestException("This playlist already contains this video.");
        }

        currentPlaylist.get().getVideos().add(video);
        playlistRepository.save(currentPlaylist.get());
        return new PlaylistWithoutOwnerDTO(playlistRepository.findByTitle(currentPlaylist.get().getTitle()));
    }

    public PlaylistWithoutOwnerDTO removeVideo(User user, int id, String title) {
        Optional<Playlist> currentPlaylist = playlistRepository.findById(id);
        PlaylistValidator.validate(currentPlaylist, user);

        Video video = videoRepository.findByTitle(title);

        // Checks if the video, the user want to remove, actually exists
        if(video == null) {
            throw new BadRequestException("The video, you want to add, doesn't exist.");
        }

        // Checks if this video is included in this playlist at all
        if(!currentPlaylist.get().getVideos().contains(video)) {
            throw new BadRequestException("This playlist already doesn't contain this video.");
        }

        currentPlaylist.get().getVideos().remove(video);
        playlistRepository.save(currentPlaylist.get());
        return new PlaylistWithoutOwnerDTO(playlistRepository.findByTitle(currentPlaylist.get().getTitle()));
    }

    public String deletePlaylist(int id, User user) {
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
        return "You have successfully deleted " + playlist.get().getTitle() + " playlist.";
    }
}
