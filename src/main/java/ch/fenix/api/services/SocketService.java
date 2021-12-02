package ch.fenix.api.services;

import ch.fenix.api.models.SocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;

@Service
@RequiredArgsConstructor
public class SocketService {
    private static ServerSocket serverSocket;
    private static boolean running = false;
    private final UserService userService;
    private final MessageService messageService;
    private final OnlineService onlineService;

    private void initSocket() throws IOException {
        if (serverSocket == null || serverSocket.isClosed()) {
            serverSocket = new ServerSocket(25555);
        }
    }

    @Autowired
    public void listen() {
        running = true;
        new Thread(() -> {
            while (running) {
                try {
                    initSocket();
                    new SocketHandler(serverSocket.accept(), userService, messageService, onlineService);
                    System.out.println("Socket Connected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopListening() {
        try {
            serverSocket.close();
            initSocket();
            running = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
