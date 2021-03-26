package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import youtube.exceptions.AuthenticationException;
import youtube.model.pojo.User;
import youtube.model.repository.UserRepository;
import javax.servlet.http.HttpSession;

@Component
public class SessionManager {
    private static final String LOGGED_USER_ID = "LOGGED_USER_ID";

    @Autowired
    private UserRepository repository;

    public User getLoggedUser(HttpSession session){
        if(session.getAttribute(LOGGED_USER_ID) == null){
            throw new AuthenticationException("You have to log in!");
        }
        else{
            int userId = (int) session.getAttribute(LOGGED_USER_ID);
            return repository.findById(userId).get();
        }
    }

    public void loginUser(HttpSession ses, int id) {
        ses.setAttribute(LOGGED_USER_ID, id);
    }

    public void logoutUser(HttpSession ses) {
        ses.invalidate();
    }
}
