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
import youtube.model.validations.UserValidation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

   @OneToMany(mappedBy = "owner")
   @JsonManagedReference
   private List<Video> videos;

   @OneToMany(mappedBy = "commenter")
   private List<Comment> comments;

   @OneToMany(mappedBy = "owner")
   @JsonManagedReference
   private List<Playlist> playlists;

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

   public User(RegisterRequestUserDTO userDTO) {
      username = userDTO.getUsername();
      email = userDTO.getEmail();
      age = userDTO.getAge();
      password = userDTO.getPassword();
      city = userDTO.getCity();
   }


   //method used for editing user profile
   public void editUser(EditRequestUserDTO userDTO){
      if(userDTO.getEmail() != null) {
         if(!UserValidation.validateEmail(email)) {
            throw new BadRequestException("You have entered invalid email.");
         }
         email = userDTO.getEmail();
      }
      if(userDTO.getAge() != 0) {
         if(!UserValidation.validateAge(age)) {
            throw new BadRequestException("You have entered invalid age.");
         }
         age = userDTO.getAge();
      }
      if(userDTO.getCity() != null) {
         if(!UserValidation.validateCity(city)) {
            throw new BadRequestException("You have entered invalid city.");
         }
         city = userDTO.getCity();
      }
      if(userDTO.getPassword() != null && userDTO.getConfirmPassword() != null) {
         if(!UserValidation.validatePasswordConfirmation(userDTO.getPassword(), userDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match.");
         }

         PasswordEncoder encoder = new BCryptPasswordEncoder();
         userDTO.setPassword(encoder.encode(userDTO.getPassword()));
         password = userDTO.getPassword();
      }

   }
}
