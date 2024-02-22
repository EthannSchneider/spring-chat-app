package ch.shkermit.tpi.chatapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.repository.UserRepository;
import ch.shkermit.tpi.chatapp.service.UserService;

public class DefaultUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;

    @Override
    public User getUser(String username) throws UsersNotExistException {
        return userRepository.findByUsername(username).orElseThrow(UsersNotExistException::new);
    }

    @Override
    public User getUser(String username, String password) throws UsersNotExistException {
        User user = userRepository.findByUsername(username).orElseThrow(UsersNotExistException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UsersNotExistException();
        }

        return user;
    }
    
    @Override
    public User createUser(User user) throws UsersAlreadyExistException {
        if (userRepository.findByEmail(user.getEmail()).isPresent() || userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsersAlreadyExistException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
}
