package ch.fenix.api.repository;

import ch.fenix.api.models.Message;
import ch.fenix.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> getAllByReceiver(User receiver);
    void deleteMessagesByReceiver(User receiver);
}
