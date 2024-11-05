package com.example.sectors_app.controller;

import com.example.sectors_app.repository.SectorRepository;
import com.example.sectors_app.service.SectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class SectorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SectorRepository sectorRepository;

    @MockBean
    private SectorService sectorService;

    @BeforeEach
    void setUp() {
        sectorRepository.deleteAll();
    }

    @Test
    public void getSectorById_NotFound() throws Exception {
        mockMvc.perform(get("/api/sectors/999"))
                .andExpect(status().isNotFound());
    }

}

