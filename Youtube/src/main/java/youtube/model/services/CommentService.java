package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import youtube.exceptions.AuthenticationException;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.CommentDTO;
import youtube.model.pojo.Comment;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.CommentRepository;
import youtube.model.repository.VideoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VideoRepository videoRepository;

    public CommentDTO makeComment(String title, String text, User u) {
        Video v = videoRepository.findByTitle(title);
        if (v == null) {
            throw new NotFoundException("Cannot comment on nonexistent video");
        }

        Comment comment = new Comment(text, u, v);
        commentRepository.save(comment);
        return new CommentDTO(comment);
    }
    public List<CommentDTO> getComments(String vidName) {
        Video v = videoRepository.findByTitle(vidName);
        if (v == null) {
            throw new NotFoundException("No such video.");
        }

        List<CommentDTO> ls = new ArrayList<>();
        for (Comment c : v.getComments()) {
            ls.add(new CommentDTO(c));
        }

        return ls;
    }
    public CommentDTO editComment(User u, String text, String commentId) {
        // Checking if comment that was selected for editing
        // was made by current logged in user
        boolean isMadeByUser = false;
        int cID = Integer.parseInt(commentId);
        var v = u.getComments();
        for (Comment c : v) {
            if (c.getId() == cID) {
                isMadeByUser = true;
                break;
            }
        }

        if (!isMadeByUser) {
            throw new AuthenticationException("Cannot edit comment of another user");
        }

        // Editing and updating in db
        Comment c = commentRepository.findById(cID).get();
        c.setText(text);
        commentRepository.save(c);
        return new CommentDTO(c);
    }
    public void deleteComment(User u, String id) {
        var v = commentRepository.findById(Integer.parseInt(id));
        if (v.isEmpty()) {
            throw new NotFoundException("Cannot delete nonexistent comment");
        }

        boolean isCommentByCurrUser = false;
        for (Comment c : u.getComments()) {
            if (c.getId() == v.get().getId()) {
                isCommentByCurrUser = true;
                break;
            }
        }

        if (!isCommentByCurrUser) {
            throw new AuthenticationException("Cannot delete comment that was not made by you");
        }

        commentRepository.delete(v.get());
    }
}
