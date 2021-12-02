package ch.fenix.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SocketMsg {
    private Message message;
    private List<Message> messages;
    private String tel;

    public SocketMsg(List<Message> messages) {
        this.messages = messages;
    }

    public SocketMsg(Message message) {
        this.message = message;
    }
}
