package ch.shkermit.tpi.chatapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.service.UserService;

public class UserIdentityService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userService.getUser(username);
        } catch (UsersNotExistException e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }

}
