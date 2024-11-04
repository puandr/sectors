package com.example.sectors_app.controller;

import com.example.sectors_app.model.Sector;
import com.example.sectors_app.service.SectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@WebMvcTest(SectorController.class)
public class SectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SectorService sectorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = standaloneSetup(new SectorController(sectorService)).build();
    }

    @Test
    void shouldReturnAllSectors() throws Exception {
        Sector sector1 = new Sector();
        sector1.setId(1L);
        sector1.setValueTag("1");
        sector1.setName("Manufacturing");

        Sector sector2 = new Sector();
        sector2.setId(2L);
        sector2.setValueTag("19");
        sector2.setName("Construction materials");
        sector2.setParent(sector1);

        List<Sector> sectors = Arrays.asList(sector1, sector2);
        when(sectorService.getAllSectors()).thenReturn(sectors);

        mockMvc.perform(get("/api/sectors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Manufacturing"))
                .andExpect(jsonPath("$[1].name").value("Construction materials"));
    }

    @Test
    void shouldReturnEmptyListWhenNoSectorsExist() throws Exception {
        when(sectorService.getAllSectors()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/sectors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

