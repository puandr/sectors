package com.example.sectors_app.controller;

import com.example.sectors_app.dto.UserInputDto;
import com.example.sectors_app.exception.CustomException;
import com.example.sectors_app.model.UserInput;
import com.example.sectors_app.service.UserInputService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.owasp.encoder.Encode;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ResponseEntity<UserInputDto> saveUserInput(@Valid @RequestBody UserInputDto userInputDto, HttpSession session) {
        logger.info("UserInputController: createUserInput - Request for creating user input received");

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            userId = UUID.randomUUID().toString();
            session.setAttribute("userId", userId);
            logger.info("UserInputController: createUserInput - Generated new UUID for user: " + userId);
        }

        userInputDto.setName(Encode.forHtml(userInputDto.getName()));

        //TODO refactor exceptions to be more verbose about exact error
        try {
            UserInput userInput = userInputDto.toEntity();
            userInput.setUserId(userId);
            UserInput createdUserInput = userInputService.saveUserInput(userInput);
            UserInputDto responseDTO = UserInputDto.fromEntity(createdUserInput);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ex) {
            throw new CustomException("Invalid user input data provided", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            throw new CustomException("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user input", description = "Update an existing user input including name, selected sectors, and agreement to terms.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User input updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user input data provided"),
            @ApiResponse(responseCode = "404", description = "User input not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInputDto> updateUserInput(@PathVariable Long id, @Valid @RequestBody UserInputDto userInputDTO) {
        logger.info("UserInputController: updateUserInput - Request for updating user input received");

        UserInput existingUserInput = userInputService.getUserInputById(id).orElse(null);
        if (existingUserInput == null) {
            throw new CustomException("User input not found", HttpStatus.NOT_FOUND);
        }

        userInputDTO.setName(Encode.forHtml(userInputDTO.getName()));

        try {
            existingUserInput.setName(userInputDTO.getName());
            existingUserInput.setSelectedSectors(userInputDTO.getSelectedSectors());
            existingUserInput.setAgreeToTerms(userInputDTO.isAgreeToTerms());

            UserInput updatedUserInput = userInputService.saveUserInput(existingUserInput);
            UserInputDto responseDTO = UserInputDto.fromEntity(updatedUserInput);
            return ResponseEntity.ok(responseDTO);
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
    public ResponseEntity<List<UserInputDto>> getAllUserInputs() {
        logger.info("UserInputController: getAllUserInputs - request for retrieving all user inputs recieved");
        List<UserInput> userInputs = userInputService.getAllUserInputs();
        List<UserInputDto> userInputDTOs = userInputs.stream().map(UserInputDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(userInputDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user input by ID", description = "Retrieve a user input by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user input"),
            @ApiResponse(responseCode = "404", description = "User input not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInputDto> getUserInputById(@PathVariable Long id) {
        logger.info("UserInputController: getUserInputById - Request for retrieving user inputs by id recieved");

        UserInput userInput = userInputService.getUserInputById(id).orElse(null);
        if (userInput != null) {
            UserInputDto userInputDto = UserInputDto.fromEntity(userInput);
            return ResponseEntity.ok(userInputDto);
        } else {
            throw new CustomException("User input not found", HttpStatus.NOT_FOUND);
        }
    }
}
