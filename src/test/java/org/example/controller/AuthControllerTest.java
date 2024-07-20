package org.example.controller;

import org.example.model.User;
import org.example.service.JwtCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private JwtCreator jwtCreator;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateSuccess() {

        String user = "User_2";
        String password = "123";
        User player = new User().setName(user).setPassword(password).setPlayer(true);
        String expectedToken = "jwtToken";

        when(jwtCreator.createJwt(player)).thenReturn(expectedToken);

        String token = authController.authenticate(user, password);

        assertEquals(expectedToken, token);
        verify(jwtCreator, times(1)).createJwt(player);
    }

    @Test
    public void testAuthenticateFailure() {

        String user = "User_3";
        String password = "123";

        AuthController.AuthError exception = assertThrows(AuthController.AuthError.class, () -> {
            authController.authenticate(user, password);
        });

        assertNotNull(exception);
        verify(jwtCreator, never()).createJwt(any(User.class));
    }
}
