package ch.shkermit.tpi.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.shkermit.tpi.chatapp.dto.LoginUserDTO;
import ch.shkermit.tpi.chatapp.dto.RegisterUserDTO;
import ch.shkermit.tpi.chatapp.exception.SessionException.SessionNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.projection.TokenUserProjection;
import ch.shkermit.tpi.chatapp.projection.UserProjection;
import ch.shkermit.tpi.chatapp.service.UserService;
import ch.shkermit.tpi.chatapp.service.UserSessionService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/auth")
public class Authentification {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private ProjectionFactory projectionFactory;

    @PostMapping("login")
    public ResponseEntity<TokenUserProjection> login(@RequestBody @Valid LoginUserDTO userDTO) throws UsersNotExistException {
        User user = userService.getUser(userDTO.getUsername(), userDTO.getPassword());

        return ResponseEntity.ok().body(getTokenUserProjectionByUser(user));
    }

    @PostMapping("register")
    public ResponseEntity<TokenUserProjection> register(@RequestBody @Valid RegisterUserDTO userDTO) throws UsersAlreadyExistException {
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setPassword(userDTO.getPassword());

        userService.createUser(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(getTokenUserProjectionByUser(newUser));
    }

    @GetMapping("logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserSession userSession) throws SessionNotExistException {
        userSessionService.deleteSession(userSession);

        return ResponseEntity.ok().body("{\"message\": \"User logged out\"}");
    }

    @SuppressWarnings("null")
    private TokenUserProjection getTokenUserProjectionByUser(User user) {
        String token = userSessionService.createSession(user);

        UserProjection userProjection = projectionFactory.createProjection(UserProjection.class, user);

        return new TokenUserProjection(token, userProjection);
    }
}
