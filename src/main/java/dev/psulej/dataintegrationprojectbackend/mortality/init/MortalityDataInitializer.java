package dev.psulej.dataintegrationprojectbackend.mortality.init;

import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.Voivodeship;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.MortalityDataRepository;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.VoivodeshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MortalityDataInitializer {

    public static final int INSERT_BATCH_SIZE = 250;

    private final MortalityDataRepository mortalityDataRepository;
    private final VoivodeshipRepository voivodeshipRepository;

    @Transactional
    public void initializeMortalityDataFromFile() {
        mortalityDataRepository.truncateTable();
        readMortalityDataFromFile();
    }


    private void readMortalityDataFromFile() {
        try {
            File xlsxFile = ResourceUtils.getFile("classpath:data/mortality_data.xlsx");
            FileInputStream fis = new FileInputStream(xlsxFile);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            List<MortalityData> mortalityDataList = new ArrayList<>();

            List<Voivodeship> voivodeships = voivodeshipRepository.findAll();
            Map<String, Voivodeship> voivodeshipByName = voivodeships.stream()
                    .collect(Collectors.toMap(Voivodeship::getName, voivodeship -> voivodeship));

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                int year = Integer.parseInt(row.getCell(2).getStringCellValue());
                if (shouldLoadData(year)) {
                    String voivodeshipName = row.getCell(1).getStringCellValue();
                    int month = Integer.parseInt(row.getCell(3).getStringCellValue());
                    int womanUnder65Age = (int) row.getCell(4).getNumericCellValue();
                    int womanOver65Age = (int) row.getCell(5).getNumericCellValue();
                    int manUnder65Age = (int) row.getCell(6).getNumericCellValue();
                    int manOver65Age = (int) row.getCell(7).getNumericCellValue();

                    log.info("Mortality data [voivodeship: {}, year: {}, month: {}, womanUnder65Age: {}," +
                                    " womanOver65Age: {}, manUnder65Age: {}, manOver65Age: {}]",
                            voivodeshipName, year, month, womanUnder65Age, womanOver65Age, manUnder65Age, manOver65Age);

                    Voivodeship voivodeship = null;

                    if(!voivodeshipByName.containsKey(voivodeshipName)) {
                        voivodeship = voivodeshipRepository.save(Voivodeship.fromName(voivodeshipName));
                        voivodeshipByName.put(voivodeshipName, voivodeship);
                    } else {
                        voivodeship = voivodeshipByName.get(voivodeshipName);
                    }

                    Instant mortalityMeasurementTime = ZonedDateTime.of(
                            LocalDateTime.of(year, month, 1, 0, 0), ZoneOffset.UTC
                    ).toInstant();

                    MortalityData mortalityData = MortalityData.builder()
                            .voivodeship(voivodeship)
                            .date(mortalityMeasurementTime)
                            .womanUnder65Age(womanUnder65Age)
                            .womanOver65Age(womanOver65Age)
                            .manUnder65Age(manUnder65Age)
                            .manOver65Age(manOver65Age)
                            .build();

                    mortalityDataList.add(mortalityData);
                    if (mortalityDataList.size() == INSERT_BATCH_SIZE) {
                        mortalityDataRepository.saveAll(mortalityDataList);
                        mortalityDataRepository.flush();
                        log.info("Mortality data batch persisted (size = {})", mortalityDataList.size());
                        mortalityDataList.clear();
                    }
                }
            }
            if (mortalityDataList.size() > 0) {
                mortalityDataRepository.saveAll(mortalityDataList);
                log.info("Mortality data batch persisted (size = {})", mortalityDataList.size());
            }

            log.info("Weather data file has been processed");
            workbook.close();
            fis.close();
        } catch (IOException e) {
            log.error("Error occurred during loading mortality data file", e);
        }
    }

    private static boolean shouldLoadData(int year) {
        return year == 2019 || year == 2020;
    }
}
