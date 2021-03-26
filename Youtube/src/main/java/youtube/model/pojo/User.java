package youtube.model.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import youtube.model.dto.RegisterRequestUserDTO;

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

   @OneToMany(mappedBy = "owner")
   private List<Video> videos;
   @OneToMany(mappedBy = "commenter")
   private List<Comment> comments;

   public User(RegisterRequestUserDTO userDTO) {
      username = userDTO.getUsername();
      email = userDTO.getEmail();
      age = userDTO.getAge();
      password = userDTO.getPassword();
      city = userDTO.getCity();
   }
}
