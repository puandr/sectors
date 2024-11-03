package com.example.sectors_app.config;

import com.example.sectors_app.service.SectorImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SectorDataLoader implements ApplicationRunner {
    private final SectorImportService sectorImportService;
    private final static Logger logger = LoggerFactory.getLogger(SectorDataLoader.class);

    @Value("${sector.import.file-path}")
    private String filePath;

    public SectorDataLoader(SectorImportService sectorImportService) {
        this.sectorImportService = sectorImportService;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("SectorDataLoader - executing importSectors");
        sectorImportService.importSectors(filePath);
    }

}
