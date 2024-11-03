package com.example.sectors_app.controller;

import com.example.sectors_app.model.Sector;
import com.example.sectors_app.repository.SectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SectorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SectorRepository sectorRepository;

    @BeforeEach
    void setUp() {
        sectorRepository.deleteAll();

        Sector sector1 = new Sector();
        sector1.setValueTag("1");
        sector1.setName("Manufacturing");

        Sector sector2 = new Sector();
        sector2.setValueTag("19");
        sector2.setName("Construction materials");
        sector2.setParent(sector1);

        sectorRepository.saveAll(Arrays.asList(sector1, sector2));
    }

    @Test
    public void shouldReturnAllSectors() throws Exception {
        mockMvc.perform(get("/api/sectors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Manufacturing"))
                .andExpect(jsonPath("$[0].children[0].name").value("Construction materials"))
                .andExpect(jsonPath("$[0].children.length()").value(1))
                .andExpect(jsonPath("$[1].name").value("Construction materials"))
                .andExpect(jsonPath("$[1].children.length()").value(0));
    }

    @Test
    void shouldReturnEmptyListWhenNoSectorsExist() throws Exception {
        sectorRepository.deleteAll();
        mockMvc.perform(get("/api/sectors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    //TODO add hierarchy check test
//    @Test
//    @Transactional
//    void shouldPreserveHierarchy() throws Exception {
//        Sector parentSector = sectorRepository.findByName("Manufacturing");
//
//        mockMvc.perform(get("/api/sectors/" + parentSector.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Manufacturing"))
//                .andExpect(jsonPath("$.children[0].name").value("Construction materials"))
//                .andExpect(jsonPath("$.children.length()").value(1));
//    }
}

