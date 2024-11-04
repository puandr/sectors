package com.example.sectors_app.controller;

import com.example.sectors_app.model.UserInput;
import com.example.sectors_app.repository.UserInputRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class UserInputControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInputRepository userInputRepository;

    @BeforeEach
    void setUp() {
        userInputRepository.deleteAll();
    }

    //TODO refactor tests

    @Test
    void shouldSaveUserInput() throws Exception {
        String userInputJson = "{" +
                "\"name\": \"John Doe\"," +
                "\"selectedSectors\": [1, 2]," +
                "\"agreeToTerms\": true" +
                "}";

        mockMvc.perform(post("/api/user-input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInputJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.selectedSectors.length()").value(2))
                .andExpect(jsonPath("$.agreeToTerms").value(true));
    }

    @Test
    void shouldNotSaveUserInputWithMissingFields() throws Exception {
        String userInputJson = "{" +
                "\"name\": \"\"," +
                "\"selectedSectors\": []," +
                "\"agreeToTerms\": true" +
                "}";

        mockMvc.perform(post("/api/user-input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInputJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    void shouldReturn400ForInvalidUserInput() throws Exception {
        String userInputJson = "{" +
                "\"name\": \"John Doe\"," +
                "\"selectedSectors\": \"invalid\"," +
                "\"agreeToTerms\": true" +
                "}";

        mockMvc.perform(post("/api/user-input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInputJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenTermsNotAgreed() throws Exception {
        String userInputJson = "{" +
                "\"name\": \"John Doe\"," +
                "\"selectedSectors\": [1, 2]," +
                "\"agreeToTerms\": false" +
                "}";

        mockMvc.perform(post("/api/user-input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInputJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.agreeToTerms").exists());
    }

    @Test
    void shouldHandleBoundaryConditionsForSelectedSectors() throws Exception {
        String userInputJson = "{" +
                "\"name\": \"John Doe\"," +
                "\"selectedSectors\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]," +
                "\"agreeToTerms\": true" +
                "}";

        mockMvc.perform(post("/api/user-input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInputJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.selectedSectors.length()").value(10));
    }

    @Test
    void shouldNotCreateDuplicateUserInput() throws Exception {
        UserInput userInput = new UserInput();
        userInput.setName("John Doe");
        userInput.setSelectedSectors(Arrays.asList(1L, 2L));
        userInput.setAgreeToTerms(true);
        userInputRepository.save(userInput);

        String userInputJson = "{" +
                "\"name\": \"John Doe\"," +
                "\"selectedSectors\": [1, 2]," +
                "\"agreeToTerms\": true" +
                "}";

        mockMvc.perform(post("/api/user-input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInputJson))
                .andExpect(status().isConflict());
    }
}

