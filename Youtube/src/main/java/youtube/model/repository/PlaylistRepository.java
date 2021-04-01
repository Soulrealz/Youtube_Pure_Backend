package youtube.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.User;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Playlist findByTitle(String title);
    List<Playlist> findAllByOwner(User user);
    Optional<Playlist> findById(int id);
}
