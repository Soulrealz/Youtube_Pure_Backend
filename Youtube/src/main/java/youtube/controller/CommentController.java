package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import youtube.model.dto.CommentDTO;
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
    @PutMapping("/videos")
    public CommentDTO makeComment(@RequestParam String name, @RequestBody String text, HttpSession ses) {
        User u = sessionManager.getLoggedUser(ses);
        return commentService.makeComment(name, text, u);
    }

    // Editing X comment
    @PostMapping("/videos/comment/{id}")
    public CommentDTO editComment(@PathVariable(name = "id") String commentId, @RequestBody String text, HttpSession ses) {
        User u = sessionManager.getLoggedUser(ses);
        return commentService.editComment(u, text, commentId);
    }

    // Showing all comments on X video
    @GetMapping("/video/comments")
    public List<CommentDTO> showComments(@RequestParam(name = "name") String name) {
        return commentService.getComments(name);
    }

    // Deleting X comment on Y video
    @DeleteMapping("/comments/{id}")
    public String deleteComment(@PathVariable(name = "id") String id, HttpSession ses) {
        User u = sessionManager.getLoggedUser(ses);
        commentService.deleteComment(u, id);
        return "Deleted comment";
    }


}
