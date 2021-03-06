package youtube.model.pojo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import youtube.exceptions.BadRequestException;
import youtube.model.dto.usersDTO.EditRequestUserDTO;
import youtube.model.dto.usersDTO.RegisterRequestUserDTO;
import youtube.model.utils.UserValidator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;
   private String username;
   private String email;
   private int age;
   private String password;
   private String city;
   private LocalDateTime registerDate;
   private Boolean verified = false;

   @OneToMany(mappedBy = "owner")
   @JsonManagedReference
   private List<Video> videos;

   @OneToMany(mappedBy = "commenter")
   private List<Comment> comments;

   @OneToMany(mappedBy = "owner")
   @JsonManagedReference
   private List<Playlist> playlists;

   // Subscribe relationship
   @ManyToMany
   @JoinTable(
           name = "subscriptions",
           joinColumns = {@JoinColumn(name = "subscribed_to_id")},
           inverseJoinColumns = {@JoinColumn(name = "subscriber_id")}
   )
   @JsonManagedReference
   private List<User> subscribers;

   @ManyToMany(mappedBy = "subscribers")
   @JsonBackReference
   private List<User> subscribedTo;
   //---------------------------------------------------------------------

   // Comments like/dislike relationship
   @ManyToMany
   @JoinTable(
           name = "users_like_comments",
           joinColumns = { @JoinColumn(name = "user_id")},
           inverseJoinColumns = { @JoinColumn(name = "comment_id")}
   )
   @JsonManagedReference
   private List<Comment> likedComments;

   @ManyToMany
   @JoinTable(
           name = "users_dislike_comments",
           joinColumns = { @JoinColumn(name = "user_id")},
           inverseJoinColumns = { @JoinColumn(name = "comment_id")}
   )
   @JsonManagedReference
   private List<Comment> dislikedComments;
   //---------------------------------------------------------------------

   // Videos like/dislike relationship
   @ManyToMany
   @JoinTable(
           name = "users_like_videos",
           joinColumns = { @JoinColumn(name = "user_id")},
           inverseJoinColumns = { @JoinColumn(name = "video_id")}
   )
   @JsonManagedReference
   private List<Video> likedVideos;

   @ManyToMany
   @JoinTable(
           name = "users_dislike_videos",
           joinColumns = { @JoinColumn(name = "user_id")},
           inverseJoinColumns = { @JoinColumn(name = "video_id")}
   )
   @JsonManagedReference
   private List<Video> dislikedVideos;
   //---------------------------------------------------------------------



   public User(RegisterRequestUserDTO userDTO) {
      username = userDTO.getUsername();
      email = userDTO.getEmail();
      age = userDTO.getAge();
      password = userDTO.getPassword();
      city = userDTO.getCity();
   }

   public User(int id, String username, Boolean verified) {
      this.id = id;
      this.username = username;
      this.verified = verified;
   }

   // method used for editing user profile
   public void editUser(EditRequestUserDTO userDTO){
      if(userDTO.getEmail() != null) {
         if(!UserValidator.validateEmail(email)) {
            throw new BadRequestException("You have entered invalid email.");
         }
         email = userDTO.getEmail();
      }
      if(userDTO.getAge() != 0) {
         if(!UserValidator.validateAge(age)) {
            throw new BadRequestException("You have entered invalid age.");
         }
         age = userDTO.getAge();
      }
      if(userDTO.getCity() != null) {
         if(!UserValidator.validateCity(city)) {
            throw new BadRequestException("You have entered invalid city.");
         }
         city = userDTO.getCity();
      }
      if(userDTO.getPassword() != null && userDTO.getConfirmPassword() != null) {
         if(!UserValidator.validatePasswordConfirmation(userDTO.getPassword(), userDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match.");
         }

         PasswordEncoder encoder = new BCryptPasswordEncoder();
         userDTO.setPassword(encoder.encode(userDTO.getPassword()));
         password = userDTO.getPassword();
      }

   }
}
