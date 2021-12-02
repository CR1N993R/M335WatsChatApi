package ch.fenix.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String tel;
    @JsonIgnore
    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private List<Message> messages = new ArrayList<>();

    public User(String tel) {
        this.tel = tel;
    }
}
