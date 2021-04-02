package youtube.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youtube.model.pojo.Playlist;
import youtube.model.pojo.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Playlist findByTitle(String title);
    List<Playlist> findAllByOwner(User user);
    Optional<Playlist> findById(int id);
}
