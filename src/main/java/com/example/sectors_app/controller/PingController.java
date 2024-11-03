package com.example.sectors_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {
    private static final Logger logger = LoggerFactory.getLogger(PingController.class);
    @Operation(summary = "Ping the server", description = "Returns a simple 'pong' response to indicate the server is running.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Server is running"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<String> ping() {
        logger.info("PingController - Ping request received");
        return ResponseEntity.ok("pong");
    }
}
