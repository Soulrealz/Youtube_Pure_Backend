package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import youtube.model.dto.CommentDTO;
import youtube.model.pojo.User;
import youtube.model.services.CommentService;

import javax.servlet.http.HttpSession;

@RestController
public class CommentController extends AbstractController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private SessionManager sessionManager;

    // Making comments on X video
    @PutMapping("/videos/{id}")
    public CommentDTO makeComment(@RequestBody String text, @PathVariable(name = "id") String vidID, HttpSession ses) {
        User u = sessionManager.getLoggedUser(ses);
        return commentService.makeComment(vidID, text, u);
    }
}
