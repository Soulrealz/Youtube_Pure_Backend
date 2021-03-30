package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import youtube.model.dto.commentsDTO.CommentDTO;
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
    // RequestParam - video
    // RequestBody - comment text
    @PutMapping("/videos/comments")
    public CommentDTO makeComment(@RequestParam String name, @RequestBody String text, HttpSession ses) {
        User User = sessionManager.getLoggedUser(ses);
        return commentService.makeComment(name, text, User);
    }

    // Editing X comment
    // PathVar - which comment to edit
    // RequestBody - new comment text
    @PostMapping("/videos/comments/{id}")
    public CommentDTO editComment(@PathVariable(name = "id") String commentId, @RequestBody String text, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        return commentService.editComment(user, text, commentId);
    }

    // Showing all comments on X video
    // RequestParam - which video
    @GetMapping("/videos/comments")
    public List<CommentDTO> showComments(@RequestParam(name = "name") String name) {
        return commentService.getComments(name);
    }

    // Return other type with more information
    // Deleting X comment on Y video
    // PathVar - which comment to delete
    @DeleteMapping("/videos/comments/{id}")
    public String deleteComment(@PathVariable(name = "id") String id, HttpSession ses) {
        User user = sessionManager.getLoggedUser(ses);
        commentService.deleteComment(user, id);
        return "Deleted comment";
    }


}
