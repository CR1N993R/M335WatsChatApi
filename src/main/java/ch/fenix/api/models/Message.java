package ch.fenix.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime sentAt;
    private String message;
    @ManyToOne
    private User sender;
    @ManyToOne
    @JoinColumn
    @Setter
    private User receiver;
    private String receiverTel;
}
