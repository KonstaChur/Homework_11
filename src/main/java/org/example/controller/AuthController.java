package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.service.JwtCreator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private static final List<User> users = List.of(
            new User().setName("User_1").setPassword("123"),
            new User().setName("User_2").setPassword("123").setPlayer(true));

    private final JwtCreator jwtCreator;

    @GetMapping("/authenticate")
    public String authenticate(String user, String password) {
        return users.stream()
                .filter(e -> e.getName().equals(user) && e.getPassword().equals(password))
                .findFirst()
                .map(jwtCreator::createJwt)
                .orElseThrow(AuthError::new);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class AuthError extends RuntimeException {}
}


