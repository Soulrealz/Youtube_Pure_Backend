package youtube.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.User;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Playlist findByTitle(String title);
    List<Playlist> findAllByOwner(User user);
    Playlist findById(int id);
}
