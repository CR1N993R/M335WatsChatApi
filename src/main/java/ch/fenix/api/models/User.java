package ch.fenix.api.models;

import ch.fenix.api.listeners.MessageReceiveListener;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String tel;
    @Setter
    private boolean online;
    @Transient
    private MessageReceiveListener receiveListener;
    @OneToMany(mappedBy = "receiver")
    private List<Message> messages = new ArrayList<>();

    public boolean addMessage(Message message) {
        if (online &&  receiveListener != null) {
            receiveListener.messageReceived(message);
            return false;
        }else {
            messages.add(message);
            return true;
        }
    }
}
