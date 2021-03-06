package youtube.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    List<Video> findAllByTitle(String title);
    Video findByTitle(String title);
    List<Video> findAllByOwner(User user);

    // Retrieving all videos and ordering them by there upload date
    List<Video> findAllByIdGreaterThanOrderByUploadDateDesc(int id);

    // Retrieving all videos
    List<Video> findAllByIdGreaterThan(int id);
}
