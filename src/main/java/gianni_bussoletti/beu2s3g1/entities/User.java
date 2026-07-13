package gianni_bussoletti.beu2s3g1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@ToString
@Getter
@Setter

public class User {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false, unique = true)
    private String mail;
    @Column(nullable = false)
    private String password;
    @Column(name = "avatar_url", nullable = false)
    private String avatarURL;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;


    public User(String name, String surname, String password, String mail, LocalDate birthDate) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.mail = mail;
        this.birthDate = birthDate;
        this.avatarURL = "https://picsum.photos/200";
    }
}


