package ch.fenix.api.services;

import ch.fenix.api.models.User;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class OnlineService {
    private static Map<String, PrintWriter> onlineUsers = new HashMap<>();

    public synchronized void addOnlineUser(String tel, PrintWriter writer) {
        onlineUsers.putIfAbsent(tel, writer);
    }

    public synchronized PrintWriter getOnlineReceiveListenerByUser(String tel) {
        return onlineUsers.get(tel);
    }

    public synchronized void removeUser(String tel) {
        onlineUsers.remove(tel);
    }
}
