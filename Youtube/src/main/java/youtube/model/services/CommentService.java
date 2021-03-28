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
    public List<CommentDTO> getComments(String vidID) {
        var v = videoRepository.findById(Integer.parseInt(vidID));
        if (v.isEmpty()) {
            throw new NotFoundException("No such video.");
        }

        List<CommentDTO> ls = new ArrayList<>();
        for (Comment c : v.get().getComments()) {
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

}
