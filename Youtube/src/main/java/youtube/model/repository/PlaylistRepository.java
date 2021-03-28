package youtube.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youtube.model.pojo.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Playlist findByTitle(String title);
}
