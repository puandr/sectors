package com.example.sectors_app.controller;

import com.example.sectors_app.exception.CustomException;
import com.example.sectors_app.model.UserInput;
import com.example.sectors_app.service.UserInputService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-inputs")
public class UserInputController {
    private static final Logger logger = LoggerFactory.getLogger(UserInputController.class);


    private final UserInputService userInputService;

    public UserInputController(UserInputService userInputService) {
        this.userInputService = userInputService;
    }

    @PostMapping
    @Operation(summary = "Create a new user input", description = "Store a new user input including name, selected sectors, and agreement to terms.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User input created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user input data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInput> saveUserInput(@RequestBody UserInput userInput) {
        logger.info("UserInputController: createUserInput - Request for creating user input received");

        //TODO refactor exceptions to be more verbose about exact error
        try {
            UserInput createdUserInput = userInputService.saveUserInput(userInput);
            return new ResponseEntity<>(createdUserInput, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ex) {
            throw new CustomException("Invalid user input data provided", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            throw new CustomException("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Get all user inputs", description = "Retrieve a list of all user inputs from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of user inputs"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserInput>> getAllUserInputs() {
        logger.info("UserInputController: getAllUserInputs - request for retrieving all user inputs recieved");

        List<UserInput> userInputs = userInputService.getAllUserInputs();
        return ResponseEntity.ok(userInputs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user input by ID", description = "Retrieve a user input by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user input"),
            @ApiResponse(responseCode = "404", description = "User input not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInput> getUserInputById(@PathVariable Long id) {
        logger.info("UserInputController: getUserInputById - Request for retrieving user inputs by id recieved");

        UserInput userInput = userInputService.getUserInputById(id).orElse(null);
        if (userInput != null) {
            return ResponseEntity.ok(userInput);
        } else {
            throw new CustomException("User input not found", HttpStatus.NOT_FOUND);
        }
    }
}
