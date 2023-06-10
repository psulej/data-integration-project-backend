package dev.psulej.dataintegrationprojectbackend.init;

import dev.psulej.dataintegrationprojectbackend.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.repository.WeatherDataRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class InitDataReader implements CommandLineRunner {

    public static final int INSERT_BATCH_SIZE = 250;

    WeatherDataRepository weatherDataRepository;

    public InitDataReader(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        readCSV();
//        readXLSX();
    }

    private void readXLSX() {
        try {
            File xlsxFile = ResourceUtils.getFile("classpath:data/mortality_data.xlsx");
            FileInputStream fis = new FileInputStream(xlsxFile);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            List<MortalityData> mortalityDataList = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                int year = Integer.parseInt(row.getCell(2).getStringCellValue());
                if (year == 2019 || year == 2020) {
                    String voivodeshipName = row.getCell(1).getStringCellValue();
                    int month = Integer.parseInt(row.getCell(3).getStringCellValue());
                    int womanUnder65 = (int) row.getCell(4).getNumericCellValue();
                    int womanAfter65 = (int) row.getCell(5).getNumericCellValue();
                    int manUnder65 = (int) row.getCell(6).getNumericCellValue();
                    int manAfter65 = (int) row.getCell(7).getNumericCellValue();

                    log.info("Mortality data [voivodeship: {}, year: {}, month: {}, womanUnder65: {}," +
                                    " womanAfter65: {}, manUnder65: {}, manAfter65: {}]",
                            voivodeshipName, year, month, womanUnder65, womanAfter65, manUnder65, manAfter65);

                    MortalityData mortalityData = MortalityData.builder()
                            .year(year)
                            .monthNumber(month)
                            .womanUnder65(womanUnder65)
                            .womanAfter65(womanAfter65)
                            .manUnder65(manUnder65)
                            .manAfter65(manAfter65)
                            .build();

                    mortalityDataList.add(mortalityData);
                    if (mortalityDataList.size() == INSERT_BATCH_SIZE) {
                        // repository.save(mortalityData)
                        mortalityDataList.clear();
                    }
                }
            }
            if (mortalityDataList.size() > 0) {
                // repository.save(mortalityDataList)
            }
            workbook.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private void readCSV() throws FileNotFoundException {
        File csvFile = ResourceUtils.getFile("classpath:data/meteorological_data.csv");
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withHeader("Date", "Temperature", "Pressure", "WindVelocity", "WindDirection", "Precipitation")
                .withSkipHeaderRecord();
        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, format)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            List<WeatherData> weatherDataList = new ArrayList<>();

            for (CSVRecord csvRecord : csvParser) {
                String date = csvRecord.get(0);
                Date parsedDate = sdf.parse(date);
                String pressure = csvRecord.get(1);
                String windVelocity = csvRecord.get(2);
                String windDirection = csvRecord.get(3);
                String precipitation = csvRecord.get(4);

                WeatherData weatherData = WeatherData.builder()
                        .date(parsedDate.toInstant())
                        .pressure(parseFloatSafely(pressure))
                        .windVelocity(parseFloatSafely(windVelocity))
                        .windDirection(parseFloatSafely(windDirection))
                        .precipitation(parseFloatSafely(precipitation))
                        .build();
                weatherDataList.add(weatherData);

                if (weatherDataList.size() == INSERT_BATCH_SIZE) {
                    weatherDataRepository.saveAll(weatherDataList);
                    log.info("Weather data batch persisted (size = {})", INSERT_BATCH_SIZE);
                    weatherDataList.clear();
                }
            }

            if (weatherDataList.size() > 0) {
                weatherDataRepository.saveAll(weatherDataList);
                log.info("Weather data batch persisted (size = {})", weatherDataList.size());
            }

            log.info("Weather data file has been processed");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private float parseFloatSafely(String value) {
        if (!StringUtils.hasText(value)) {
            return 0f;
        }
        return Float.parseFloat(value);
    }
}
