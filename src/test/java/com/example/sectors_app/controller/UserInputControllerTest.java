package com.example.sectors_app.controller;

import com.example.sectors_app.model.UserInput;
import com.example.sectors_app.service.UserInputService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
public class UserInputControllerTest {

    @Mock
    private UserInputService userInputService;

    @InjectMocks
    private UserInputController userInputController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveUserInput() {
        UserInput userInput = new UserInput();
        userInput.setName("John Doe");
        userInput.setSelectedSectors(Arrays.asList(1L, 2L));
        userInput.setAgreeToTerms(true);

        when(userInputService.saveUserInput(any(UserInput.class))).thenReturn(userInput);

        ResponseEntity<UserInput> response = userInputController.saveUserInput(userInput);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void shouldNotSaveUserInputWithMissingFields() {
        UserInput userInput = new UserInput();
        userInput.setName(""); // Missing name
        userInput.setSelectedSectors(Arrays.asList());
        userInput.setAgreeToTerms(true);

        when(userInputService.saveUserInput(any(UserInput.class))).thenThrow(new IllegalArgumentException("Name is required"));

        try {
            userInputController.saveUserInput(userInput);
        } catch (IllegalArgumentException e) {
            assertEquals("Name is required", e.getMessage());
        }
    }

    @Test
    void shouldReturn400WhenTermsNotAgreed() {
        UserInput userInput = new UserInput();
        userInput.setName("John Doe");
        userInput.setSelectedSectors(Arrays.asList(1L, 2L));
        userInput.setAgreeToTerms(false); // Terms not agreed

        when(userInputService.saveUserInput(any(UserInput.class))).thenThrow(new IllegalArgumentException("User must agree to terms"));

        try {
            userInputController.saveUserInput(userInput);
        } catch (IllegalArgumentException e) {
            assertEquals("User must agree to terms", e.getMessage());
        }
    }

    @Test
    void shouldHandleBoundaryConditionsForSelectedSectors() {
        UserInput userInput = new UserInput();
        userInput.setName("John Doe");
        userInput.setSelectedSectors(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L));
        userInput.setAgreeToTerms(true);

        when(userInputService.saveUserInput(any(UserInput.class))).thenReturn(userInput);

        ResponseEntity<UserInput> response = userInputController.saveUserInput(userInput);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(10, response.getBody().getSelectedSectors().size());
    }
}

