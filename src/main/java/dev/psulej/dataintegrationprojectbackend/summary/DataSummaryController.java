package dev.psulej.dataintegrationprojectbackend.summary;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/summary")
@AllArgsConstructor
public class DataSummaryController {
    DataSummaryService dataSummaryService;

    @GetMapping
    List<DataSummary> getSummary(){
        return dataSummaryService.getSummary();
    }
}
