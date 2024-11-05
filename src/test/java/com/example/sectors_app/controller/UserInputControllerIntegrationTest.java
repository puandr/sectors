package com.example.sectors_app.controller;

import com.example.sectors_app.dto.UserInputDto;
import com.example.sectors_app.exception.CustomException;
import com.example.sectors_app.model.UserInput;
import com.example.sectors_app.repository.UserInputRepository;
import com.example.sectors_app.service.UserInputService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class UserInputControllerIntegrationTest {

    @Mock
    private UserInputService userInputService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInputRepository userInputRepository;

    @InjectMocks
    private UserInputController userInputController;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        userInputRepository.deleteAll();
    }

    @Test
    void saveUserInput_ShouldReturnCreatedStatus_WhenInputIsValid() {
        UserInputDto userInputDto = new UserInputDto(null, "TestUser", List.of(1L, 2L), true);
        UserInput savedUserInput = new UserInput();
        savedUserInput.setId(1L);
        savedUserInput.setName("TestUser");
        savedUserInput.setSelectedSectors(List.of(1L, 2L));
        savedUserInput.setAgreeToTerms(true);
        savedUserInput.setUserId("uniqueUserId");
        savedUserInput.setCreatedAt(LocalDateTime.now());
        savedUserInput.setUpdatedAt(LocalDateTime.now());

        when(userInputService.saveUserInput(any(UserInput.class))).thenReturn(savedUserInput);

        MockHttpSession session = new MockHttpSession();  // Create a mock session
        ResponseEntity<UserInputDto> response = userInputController.saveUserInput(userInputDto, session);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("TestUser", response.getBody().getName());
        assertEquals(List.of(1L, 2L), response.getBody().getSelectedSectors());
        assertTrue(response.getBody().isAgreeToTerms());
        verify(userInputService, times(1)).saveUserInput(any(UserInput.class));
    }

    @Test
    void saveUserInput_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
        String invalidUserInputJson = """
                    {
                      "name": "",
                      "selectedSectors": [],
                      "agreeToTerms": false
                    }
                """;

        mockMvc.perform(post("/api/user-inputs")
                        .contentType("application/json")
                        .content(invalidUserInputJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("Name is mandatory"))
                .andExpect(jsonPath("$.errors.selectedSectors").value("At least one sector should be selected"));
    }

    @Test
    void saveUserInput_ShouldThrowCustomException_WhenDataIntegrityViolationOccurs() {
        UserInputDto userInputDto = new UserInputDto(null, "TestUser", List.of(1L, 2L), true);

        when(userInputService.saveUserInput(any(UserInput.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            userInputController.saveUserInput(userInputDto, new MockHttpSession());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid user input data provided", exception.getMessage());
    }

    @Test
    void saveUserInput_ShouldThrowCustomException_WhenUnexpectedErrorOccurs() {
        UserInputDto userInputDto = new UserInputDto(null, "TestUser", List.of(1L, 2L), true);

        when(userInputService.saveUserInput(any(UserInput.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        MockHttpSession session = new MockHttpSession();
        CustomException exception = assertThrows(CustomException.class, () -> {
            userInputController.saveUserInput(userInputDto, session);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("An unexpected error occurred", exception.getMessage());
    }

    @Test
    void updateUserInput_ShouldReturnOkStatus_WhenInputIsValid() {
        UserInput existingUserInput = new UserInput();
        existingUserInput.setId(1L);
        existingUserInput.setName("OldName");
        existingUserInput.setSelectedSectors(List.of(1L));
        existingUserInput.setAgreeToTerms(true);
        existingUserInput.setUserId("uniqueUserId");

        UserInputDto updatedDto = new UserInputDto(null, "UpdatedName", List.of(1L, 2L), true);
        when(userInputService.getUserInputById(1L)).thenReturn(Optional.of(existingUserInput));
        when(userInputService.saveUserInput(any(UserInput.class))).thenReturn(existingUserInput);

        ResponseEntity<UserInputDto> response = userInputController.updateUserInput(1L, updatedDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UpdatedName", response.getBody().getName());
        verify(userInputService, times(1)).saveUserInput(any(UserInput.class));
    }

    @Test
    void updateUserInput_ShouldThrowCustomException_WhenUserInputNotFound() {
        when(userInputService.getUserInputById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userInputController.updateUserInput(1L, new UserInputDto(null, "Name", List.of(1L), true));
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User input not found", exception.getMessage());
    }

    @Test
    void updateUserInput_ShouldThrowCustomException_WhenDataIntegrityViolationOccurs() {
        UserInput existingUserInput = new UserInput();
        existingUserInput.setId(1L);
        existingUserInput.setName("OldName");
        existingUserInput.setSelectedSectors(List.of(1L));
        existingUserInput.setAgreeToTerms(true);

        UserInputDto updatedDto = new UserInputDto(null, "UpdatedName", List.of(1L, 2L), true);
        when(userInputService.getUserInputById(1L)).thenReturn(Optional.of(existingUserInput));
        when(userInputService.saveUserInput(any(UserInput.class)))
                .thenThrow(new DataIntegrityViolationException("Data integrity violation"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            userInputController.updateUserInput(1L, updatedDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid user input data provided", exception.getMessage());
    }

    @Test
    void updateUserInput_ShouldThrowCustomException_WhenUnexpectedErrorOccurs() {
        UserInput existingUserInput = new UserInput();
        existingUserInput.setId(1L);
        existingUserInput.setName("OldName");
        existingUserInput.setSelectedSectors(List.of(1L));
        existingUserInput.setAgreeToTerms(true);

        UserInputDto updatedDto = new UserInputDto(null, "UpdatedName", List.of(1L, 2L), true);
        when(userInputService.getUserInputById(1L)).thenReturn(Optional.of(existingUserInput));
        when(userInputService.saveUserInput(any(UserInput.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            userInputController.updateUserInput(1L, updatedDto);
        });
    }
}

