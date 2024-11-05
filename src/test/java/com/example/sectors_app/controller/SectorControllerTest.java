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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private Sector createSector(Long id, String valueTag, String name) {
        Sector sector = new Sector();
        sector.setId(id);
        sector.setValueTag(valueTag);
        sector.setName(name);
        sector.setChildren(Collections.emptyList());
        return sector;
    }

    @Test
    public void getAllSectors_ReturnsSectorsList() throws Exception {
        Sector sector = createSector(1L, "sector1", "Sector 1");
        when(sectorService.getAllSectors()).thenReturn(List.of(sector));

        mockMvc.perform(get("/api/sectors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sector.getId()))
                .andExpect(jsonPath("$[0].name").value(sector.getName()));
    }

    @Test
    public void getSectorById_ReturnsSector() throws Exception {
        Sector sector = createSector(1L, "sector1", "Sector 1");
        when(sectorService.getSectorById(1L)).thenReturn(Optional.of(sector));

        mockMvc.perform(get("/api/sectors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sector.getId()))
                .andExpect(jsonPath("$.name").value(sector.getName()));
    }

    @Test
    public void getSectorById_NotFound() throws Exception {
        when(sectorService.getSectorById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sectors/1"))
                .andExpect(status().isNotFound());
    }
}