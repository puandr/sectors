package com.example.sectors_app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class PingControllerTest {

    @Test
    void testPing() {
        PingController pingController = new PingController();

        ResponseEntity<String> response = pingController.ping();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("pong");
    }
}
