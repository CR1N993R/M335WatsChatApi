package ch.fenix.api.models;

import lombok.Getter;

import java.util.List;

@Getter
public class SocketMsg {
    private Message message;
    private List<Message> messages;
    private User user;
    private String text;
}
