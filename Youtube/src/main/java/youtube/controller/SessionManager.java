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

    // Getting verified user
    public User getVerifiedLoggedUser(HttpSession session){
        if(session.getAttribute(LOGGED_USER_ID) == null){
            throw new AuthenticationException("You have to log in!");
        }
        else {
            int userId = (int) session.getAttribute(LOGGED_USER_ID);

            // Checking if such a user exists in DB
            Optional<User> user = repository.findById(userId);
            if (user.isEmpty()) {
                throw new NotFoundException("No such user exists");
            }

            // Returns user only if user has a verified email address
            if (user.get().getVerified()) {
                return user.get();
            }
            else throw new BadRequestException("Please confirm your email address before doing anything else.");
        }
    }

    // Method to initially verify a user
    public User getUnverifiedUser(HttpSession session) {
        if(session.getAttribute(LOGGED_USER_ID) == null){
            throw new AuthenticationException("You have to log in!");
        }
        else {
            int userId = (int) session.getAttribute(LOGGED_USER_ID);

            // Checking if such a user exists in DB
            Optional<User> user = repository.findById(userId);
            if (user.isEmpty()) {
                throw new NotFoundException("No such user exists");
            }

            return user.get();
        }
    }

    public User checkIfThereIsLoggedUser(HttpSession session) {
        if(session.getAttribute(LOGGED_USER_ID) != null){
            int userId = (int) session.getAttribute(LOGGED_USER_ID);

            // Checking if such a user exists in DB
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
