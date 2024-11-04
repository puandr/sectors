package com.example.sectors_app.repository;

import com.example.sectors_app.model.UserInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInputRepository extends JpaRepository<UserInput, Long> {
}

