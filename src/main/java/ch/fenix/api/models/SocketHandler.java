package ch.fenix.api.models;

import ch.fenix.api.services.MessageService;
import ch.fenix.api.services.OnlineService;
import ch.fenix.api.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.transaction.Transactional;
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
    private OnlineService onlineService;

    public SocketHandler(Socket socket, UserService userService, MessageService messageService, OnlineService onlineService) {
        try {
            this.onlineService = onlineService;
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
        System.out.println("Sent: " + msg);
    }

    private void readMessage() {
        running = true;
        new Thread(() -> {
            while (running) {
                try {
                    parseMessage(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Socket Disconnected");
                    running = false;
                }
            }
            if (user != null) {
                onlineService.removeUser(user.getTel());
            }
        }).start();
    }

    private void parseMessage(String message) throws IOException {
        System.out.println("received:" + message);
        if (message == null) {
            throw new IOException();
        }
        ObjectMapper mapper = new ObjectMapper();
        SocketMsg msg = mapper.readValue(message, SocketMsg.class);
        if (msg.getTel() != null) {
            handleUserMessage(msg.getTel());
        }
        if (msg.getMessage() != null) {
            handleMessage(msg.getMessage());
        }
    }

    private void handleMessage(Message msg) {
        User receiver = userService.getUserByTel(msg.getReceiverTel());
        ObjectMapper mapper = new ObjectMapper();
        if (receiver != null) {
            msg.setReceiver(receiver);
            PrintWriter writer = onlineService.getOnlineReceiveListenerByUser(receiver.getTel());
            if (writer != null) {
                try {
                    String message = mapper.writeValueAsString(new SocketMsg(msg));
                    System.out.println("Sent: " + message);
                    writer.println(message);
                } catch (JsonProcessingException e) {
                }
            } else {
                user.getMessages().add(msg);
                messageService.saveMessage(msg);
            }
        }
    }

    private void handleUserMessage(String tel) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        user = userService.getUserByTel(tel);
        if (user == null) {
            User user = new User(tel);
            userService.saveUser(user);
            this.user = user;
        }
        onlineService.addOnlineUser(tel, writer);
        sendMessage(mapper.writeValueAsString(new SocketMsg(user.getMessages())));
        messageService.deleteMessagesByReceiver(user);
    }
}
