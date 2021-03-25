package youtube.model.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
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
}
