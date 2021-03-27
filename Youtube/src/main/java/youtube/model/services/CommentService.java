package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.CommentDTO;
import youtube.model.pojo.Comment;
import youtube.model.pojo.User;
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
        var v = videoRepository.findById(Integer.parseInt(title));
        if (v.isEmpty()) {
            throw new NotFoundException("Cannot comment on nonexistent video");
        }

        Comment comment = new Comment(text, u, v.get());
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
}
