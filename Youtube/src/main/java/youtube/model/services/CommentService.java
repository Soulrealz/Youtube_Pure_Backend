package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import youtube.exceptions.AuthenticationException;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.commentsDTO.CommentDTO;
import youtube.model.dto.commentsDTO.EditedCommentDTO;
import youtube.model.pojo.Comment;
import youtube.model.pojo.User;
import youtube.model.pojo.Video;
import youtube.model.repository.CommentRepository;
import youtube.model.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    public CommentDTO makeComment(int videoID, String text, User user) {
        // Check if video exists
        Optional<Video> video = videoRepository.findById(videoID);
        if (video.isEmpty()) {
            throw new NotFoundException("Cannot comment on nonexistent video");
        }

        // Make and save comment
        Comment comment = new Comment(text, user, video.get());
        commentRepository.save(comment);

        return new CommentDTO(comment);
    }

    public List<CommentDTO> getComments(int videoID) {
        Optional<Video> video = videoRepository.findById(videoID);
        if (video.isEmpty()) {
            throw new NotFoundException("No such video.");
        }

        List<CommentDTO> ls = new ArrayList<>();
        for (Comment comment : video.get().getComments()) {
            ls.add(new CommentDTO(comment));
        }
        return ls;
    }

    public EditedCommentDTO editComment(User user, String text, int commentID) {
        // Check if comment exists or if trying to edit comment of another user
        Comment comment = returnExistingComment(commentRepository.findById(commentID));
        if (!user.getComments().contains(comment)) {
            throw new BadRequestException("Cannot edit comment of another user");
        }

        // Edit and save updated comment
        comment.setText(text);
        commentRepository.save(comment);

        return new EditedCommentDTO(comment);
    }

    public void deleteComment(User user, int commentID) {
        // Check if comment exists or if trying to delete comment of other user
        Comment comment = returnExistingComment(commentRepository.findById(commentID));
        if (!user.getComments().contains(comment)) {
            throw new BadRequestException("Cannot delete comment of another user");
        }

        commentRepository.delete(comment);
    }

    public CommentDTO likeComment(User user, int commentID) {
        List<Comment> likedComments = user.getLikedComments();

        // Check if comment exists or is liked
        Comment comment = returnExistingComment(commentRepository.findById(commentID));
        if (likedComments.contains(comment)) {
            throw new BadRequestException("Cannot like an already liked comment");
        }

        // Remove opposite status
        user.getDislikedComments().remove(comment);

        // Add status
        likedComments.add(comment);
        userRepository.save(user);

        return new CommentDTO(comment);
    }
    public CommentDTO dislikeComment(User user, int commentID) {
        List<Comment> dislikedComments = user.getDislikedComments();

        // Check if comment exists or is disliked
        Comment comment = returnExistingComment(commentRepository.findById(commentID));
        if (dislikedComments.contains(comment)) {
            throw new BadRequestException("Cannot dislike an already disliked comment");
        }

        // Remove opposite status
        user.getLikedComments().remove(comment);

        // Add status
        dislikedComments.add(comment);
        userRepository.save(user);

        return new CommentDTO(comment);
    }
    public CommentDTO removeCurrStatusOnComment(User user, int commentID) {
        // Check if comment exists
        Comment comment = returnExistingComment(commentRepository.findById(commentID));

        // Remove any status
        user.getDislikedComments().remove(comment);
        user.getLikedComments().remove(comment);

        userRepository.save(user);

        return new CommentDTO(comment);
    }
    
    private Comment returnExistingComment(Optional<Comment> comment) {
        if (comment.isEmpty()) {
            throw new NotFoundException("Comment doesn't exist");
        }
        else return comment.get();
    }
}
