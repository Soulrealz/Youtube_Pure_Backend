package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import youtube.exceptions.AuthenticationException;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.commentsDTO.CommentDTO;
import youtube.model.pojo.Comment;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.CommentRepository;
import youtube.model.repository.VideoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VideoRepository videoRepository;

    public CommentDTO makeComment(String title, String text, User user) {
        Video video = videoRepository.findByTitle(title);
        if (video == null) {
            throw new NotFoundException("Cannot comment on nonexistent video");
        }

        Comment comment = new Comment(text, user, video);
        commentRepository.save(comment);
        return new CommentDTO(comment);
    }
    public List<CommentDTO> getComments(String vidName) {
        Video video = videoRepository.findByTitle(vidName);
        if (video == null) {
            throw new NotFoundException("No such video.");
        }

        List<CommentDTO> ls = new ArrayList<>();
        for (Comment comment : video.getComments()) {
            ls.add(new CommentDTO(comment));
        }
        return ls;
    }
    public CommentDTO editComment(User user, String text, String commentId) {
        // Checking if comment that was selected for editing
        // was made by current logged in user
        boolean isMadeByUser = false;
        int commentID = Integer.parseInt(commentId);
        List<Comment> listComments = user.getComments();
        for (Comment comment : listComments) {
            if (comment.getId() == commentID) {
                isMadeByUser = true;
                break;
            }
        }

        if (!isMadeByUser) {
            throw new AuthenticationException("Cannot edit comment of another user");
        }

        // Editing and updating in db
        Comment comment = commentRepository.findById(commentID).get();
        comment.setText(text);
        commentRepository.save(comment);
        return new CommentDTO(comment);
    }
    public void deleteComment(User user, String id) {
        Optional<Comment> comment = commentRepository.findById(Integer.parseInt(id));
        if (comment.isEmpty()) {
            throw new NotFoundException("Cannot delete nonexistent comment");
        }

        boolean isCommentByCurrUser = false;
        for (Comment comm : user.getComments()) {
            if (comm.getId() == comment.get().getId()) {
                isCommentByCurrUser = true;
                break;
            }
        }

        if (!isCommentByCurrUser) {
            throw new AuthenticationException("Cannot delete comment that was not made by you");
        }

        commentRepository.delete(comment.get());
    }
}
