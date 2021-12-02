package ch.fenix.api.services;

import ch.fenix.api.models.Message;
import ch.fenix.api.models.User;
import ch.fenix.api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;

    public void saveMessage(Message message) {
        messageRepository.saveAndFlush(message);
    }

    public void deleteMessagesByReceiver(User receiver) {
        messageRepository.deleteMessagesByReceiver(receiver);
    }
}
