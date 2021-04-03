package youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import youtube.exceptions.AuthenticationException;
import youtube.exceptions.BadRequestException;
import youtube.exceptions.NotFoundException;
import youtube.model.pojo.User;
import youtube.model.repository.UserRepository;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
public class SessionManager {
    private static final String LOGGED_USER_ID = "LOGGED_USER_ID";

    @Autowired
    private UserRepository repository;

    public User getLoggedUser(HttpSession session){
        if(session.getAttribute(LOGGED_USER_ID) == null){
            throw new AuthenticationException("You have to log in!");
        }
        else {
            int userId = (int) session.getAttribute(LOGGED_USER_ID);
            Optional<User> user = repository.findById(userId);
            if (user.isEmpty()) {
                throw new NotFoundException("No such user exists");
            }

            if (user.get().getVerified()) {
                return user.get();
            }
            else throw new BadRequestException("Please confirm your email address before doing anything else.");
        }
    }

    public User checkIfThereIsLoggedUser(HttpSession session) {
        if(session.getAttribute(LOGGED_USER_ID) != null){
            int userId = (int) session.getAttribute(LOGGED_USER_ID);
            Optional<User> user = repository.findById(userId);
            if (user.isEmpty()) {
                throw new NotFoundException("No such user exists");
            }

            return user.get();
        }
        return null;
    }

    public void loginUser(HttpSession ses, int id) {
        ses.setAttribute(LOGGED_USER_ID, id);
    }

    public void logoutUser(HttpSession ses) {
        ses.invalidate();
    }
}
