package ch.fenix.api.listeners;

import ch.fenix.api.models.Message;

public interface MessageReceiveListener {
    void messageReceived(Message message);
}
