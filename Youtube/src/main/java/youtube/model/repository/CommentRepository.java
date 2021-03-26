package youtube.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import youtube.model.pojo.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
