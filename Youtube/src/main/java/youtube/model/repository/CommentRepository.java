package youtube.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youtube.model.pojo.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
