package com.example.sectors_app.config;

import com.example.sectors_app.service.SectorImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

//TODO check, if more test are needed
@Transactional
public class SectorDataLoaderTest {
    @Mock
    private SectorImportService sectorImportService;

    @InjectMocks
    private SectorDataLoader sectorDataLoader;

    private final String filePath = "src/main/resources/data/index.html";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(sectorDataLoader, "filePath", filePath);
    }

    @Test
    void testRun() {
        sectorDataLoader.run(null);

        verify(sectorImportService, times(1)).importSectors(filePath);
    }
}
