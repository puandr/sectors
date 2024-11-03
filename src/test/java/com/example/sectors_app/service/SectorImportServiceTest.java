package com.example.sectors_app.service;

import com.example.sectors_app.model.Sector;
import com.example.sectors_app.repository.SectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

//TODO: Failing test, logic is wrong, sectorRepository.save is not invoked

//TODO more tests: if sectors exist, right hierarchy

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SectorImportServiceTest {
    @Value("${sector.import.file-path}")
    private String filePath;

    @Mock
    private SectorRepository sectorRepository;

    @InjectMocks
    private SectorImportService sectorImportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

//        when(sectorRepository.findAll()).thenReturn(Collections.emptyList());
    }

    @Test
    void testImportService() {
        sectorImportService.importSectors(filePath);

        verify(sectorRepository, atLeastOnce()).save(any(Sector.class));
    }
}
