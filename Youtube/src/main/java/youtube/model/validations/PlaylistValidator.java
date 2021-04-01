package youtube.model.validations;

import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.User;

import java.util.Optional;

public class PlaylistValidator {
    public static void validate(Optional<Playlist> currentPlaylist, User user) {
        if(currentPlaylist.isEmpty()) {
            throw new NotFoundException("This playlist doesn't exist.");
        }

        if(currentPlaylist.get().getOwner() != user) {
            throw new BadRequestException("You can't edit someone else's playlist.");
        }
    }
}
