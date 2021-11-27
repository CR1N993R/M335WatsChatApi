package ch.fenix.api.models;

import ch.fenix.api.services.MessageService;
import ch.fenix.api.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHandler {
    private User user;
    private boolean running;
    private BufferedReader reader;
    private PrintWriter writer;
    private UserService userService;
    private MessageService messageService;

    public SocketHandler(Socket socket, UserService userService, MessageService messageService) {
        try {
            this.userService = userService;
            this.messageService = messageService;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            readMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        writer.println(msg);
    }

    private void readMessage() {
        running = true;
        new Thread(() -> {
            while (running) {
                try {
                    parseMessage(reader.readLine());
                } catch (IOException e) {
                    running = false;
                }
            }
            user.setOnline(false);
        }).start();
    }

    private void parseMessage(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SocketMsg msg = mapper.readValue(message, SocketMsg.class);
        if (msg.getMessage() != null) {
            handleMessage(msg.getMessage());
        }
        if (msg.getUser() != null) {
            handleUserMessage(msg.getUser());
        }
    }

    private void handleMessage(Message msg) {
        User receiver = userService.getUserByTel(msg.getReceiverTel());
        if (receiver != null) {
            msg.setReceiver(receiver);
            if (receiver.addMessage(msg)) {
                messageService.saveMessage(msg);
            }
        }
    }

    private void handleUserMessage(User u) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        user = userService.getUserByTel(u.getTel());
        if (user == null) {
            userService.saveUser(u);
            user = u;
        }
        user.setOnline(true);
        sendMessage(mapper.writeValueAsString(user.getMessages()));
        messageService.deleteMessagesByReceiver(user);
    }
}
