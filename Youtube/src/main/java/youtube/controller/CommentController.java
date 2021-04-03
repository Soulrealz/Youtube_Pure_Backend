package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import youtube.exceptions.BadRequestException;
import youtube.model.dto.commentsDTO.CommentDTO;
import youtube.model.dto.commentsDTO.EditedCommentDTO;
import youtube.model.pojo.User;
import youtube.model.services.CommentService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class CommentController extends AbstractController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private SessionManager sessionManager;

    // Making comments on X video
    // PathVar - video
    // RequestBody - comment text
    @PutMapping("/videos/{id}/comments")
    public CommentDTO makeComment(@PathVariable(name = "id") int videoID, @RequestBody String text, HttpSession ses) {
        User User = sessionManager.getLoggedUser(ses);
        return commentService.makeComment(videoID, text, User);
    }


    // Showing all comments on X video
    // PathVar - which video
    @GetMapping("/videos/{id}/comments")
    public List<CommentDTO> showComments(@PathVariable(name = "id") int videoID) {
        return commentService.getComments(videoID);
    }


    // Editing X comment
    // PathVar - which comment to edit
    // RequestBody - new comment text
    @PostMapping("/videos/{id}/comments")
    public EditedCommentDTO editComment(@PathVariable(name = "id") int commentID, @RequestBody String text, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        return commentService.editComment(user, text, commentID);
    }


    // Deleting X comment on Y video
    // PathVar - which comment to delete
    @DeleteMapping("/videos/{id}/comments")
    public String deleteComment(@PathVariable(name = "id") int commentID, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        commentService.deleteComment(user, commentID);
        return "Deleted comment";
    }


    // PathVar - which comment to like/dislike/remove like/remove dislike
    // RequestParam - 1 = like, -1 = dislike, 0 = remove current status(if any)
    @PostMapping("/comments/{id}")
    public CommentDTO reactToComment(@PathVariable(name = "id") int commentID, @RequestParam(name = "react") String action, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        if (action.equals("1")) return commentService.likeComment(user, commentID);
        else if (action.equals("-1")) return commentService.dislikeComment(user, commentID);
        else if (action.equals("0")) return commentService.neutralStateComment(user, commentID);
        else throw new BadRequestException("No such reaction possible");
    }
}
