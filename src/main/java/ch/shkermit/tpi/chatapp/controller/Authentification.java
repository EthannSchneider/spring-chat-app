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
import ch.shkermit.tpi.chatapp.projection.MessageProjection;
import ch.shkermit.tpi.chatapp.projection.TokenUserProjection;
import ch.shkermit.tpi.chatapp.projection.UserProjection;
import ch.shkermit.tpi.chatapp.service.UserService;
import ch.shkermit.tpi.chatapp.service.UserSessionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
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
@Tags({ @Tag(name = "Authentification", description = "Authentification API") })
public class Authentification {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private ProjectionFactory projectionFactory;

    @PostMapping("login")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User logged in"),
        @ApiResponse(responseCode = "404", description = "User not exist", content = @Content),
    })
    public ResponseEntity<TokenUserProjection> login(@RequestBody @Valid LoginUserDTO userDTO) throws UsersNotExistException {
        User user = userService.getUser(userDTO.getUsername(), userDTO.getPassword());

        return ResponseEntity.ok().body(getTokenUserProjectionByUser(user));
    }

    @PostMapping("register")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "User created"),
        @ApiResponse(responseCode = "400", description = "User already exists", content = @Content)
    })
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
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User logged out"),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public ResponseEntity<MessageProjection> logout(@AuthenticationPrincipal UserSession userSession) throws SessionNotExistException {
        userSessionService.deleteSession(userSession);

        return ResponseEntity.ok().body(new MessageProjection("User logged out"));
    }

    @SuppressWarnings("null")
    private TokenUserProjection getTokenUserProjectionByUser(User user) {
        String token = userSessionService.createSession(user);

        UserProjection userProjection = projectionFactory.createProjection(UserProjection.class, user);

        return new TokenUserProjection(token, userProjection);
    }
}
