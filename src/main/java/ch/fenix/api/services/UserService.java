package ch.fenix.api.services;

import ch.fenix.api.models.User;
import ch.fenix.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public User getUserByTel(String tel) {
        return userRepository.getUserByTel(tel);
    }

    public void saveUser(User user) {
        userRepository.saveAndFlush(user);
    }
}
