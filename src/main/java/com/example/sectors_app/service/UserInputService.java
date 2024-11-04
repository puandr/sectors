package com.example.sectors_app.service;

import com.example.sectors_app.model.UserInput;
import com.example.sectors_app.repository.UserInputRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInputService {

    private final UserInputRepository userInputRepository;

    public UserInputService(UserInputRepository userInputRepository) {
        this.userInputRepository = userInputRepository;
    }

    public UserInput saveUserInput(UserInput userInput) {
        return userInputRepository.save(userInput);
    }

    public List<UserInput> getAllUserInputs() {
        return userInputRepository.findAll();
    }

    public Optional<UserInput> getUserInputById(Long id) {
        return userInputRepository.findById(id);
    }

    public void deleteUserInputById(Long id) {
        userInputRepository.deleteById(id);
    }
}
